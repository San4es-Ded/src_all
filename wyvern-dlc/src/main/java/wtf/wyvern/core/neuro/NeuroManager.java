package wtf.wyvern.core.neuro;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import wtf.wyvern.Wyvern;
import wtf.wyvern.client.modules.impl.combat.Aura;
import wtf.wyvern.client.modules.impl.misc.FakePlayer;
import wtf.wyvern.core.eventbus.EventManager;
import wtf.wyvern.core.eventbus.EventTarget;
import wtf.wyvern.core.events.impl.other.EventTick;
import wtf.wyvern.core.events.impl.player.EventAttack;
import wtf.wyvern.utility.game.other.MessageUtil;
import wtf.wyvern.utility.game.player.rotation.Rotation;
import wtf.wyvern.utility.game.player.rotation.RotationUtil;
import wtf.wyvern.utility.interfaces.IMinecraft;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Owns training sessions and persisted Neuro models. */
public final class NeuroManager implements IMinecraft {
    private static final String FILE_SUFFIX = ".neuro.json";
    // A 24x12 MLP cannot learn a useful controller from the old two-second
    // minimum. Ten seconds is still quick, but captures multiple acquire,
    // settle and cooldown cycles.
    private static final int MINIMUM_SAMPLES = 200;
    private static final int MINIMUM_HITS = 8;

    private final File modelDirectory;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Map<String, NeuroModel> models = new LinkedHashMap<>();
    private TrainingSession session;
    private boolean trainingJob;
    private boolean restoreAura;

    public NeuroManager() {
        this.modelDirectory = new File(Wyvern.DIRECTORY, "neuro");
        if (!modelDirectory.exists()) {
            modelDirectory.mkdirs();
        }
        loadModels();
        EventManager.register(this);
    }

    public void start(FakePlayer.Placement placement) {
        if (mc.player == null || mc.world == null) {
            MessageUtil.displayError("Neuro: сначала зайди в мир");
            return;
        }
        synchronized (this) {
            if (trainingJob) {
                MessageUtil.displayWarning("Neuro: дождись завершения текущего обучения");
                return;
            }
            if (session != null) {
                MessageUtil.displayWarning("Neuro: запись уже идёт. Используй .neuro save <name> или .neuro stop");
                return;
            }
        }

        boolean auraWasEnabled = Aura.INSTANCE.isEnabled();
        if (auraWasEnabled) {
            Aura.INSTANCE.setToggled(false);
        }
        OtherClientPlayerEntity target = FakePlayer.INSTANCE.beginTraining(placement);
        if (target == null) {
            if (auraWasEnabled) {
                Aura.INSTANCE.setToggled(true);
            }
            MessageUtil.displayError("Neuro: не удалось создать FakePlayer");
            return;
        }

        double sensitivity = (Double) mc.options.getMouseSensitivity().getValue();
        synchronized (this) {
            this.restoreAura = auraWasEnabled;
            this.session = new TrainingSession(sensitivity, Rotation.gcd());
        }
        MessageUtil.displayInfo("Neuro: запись началась. Бей FakePlayer вручную; Aura временно выключена");
        MessageUtil.displayInfo("Neuro: минимум 200 тиков и 8 ударов; лучше 30-60 секунд, затем .neuro save <name>");
    }

    public void stop() {
        TrainingSession stopped;
        synchronized (this) {
            stopped = session;
            session = null;
        }
        if (stopped == null) {
            MessageUtil.displayInfo("Neuro: активной записи нет");
            return;
        }
        finishRecordingState();
        MessageUtil.displayWarning("Neuro: запись отменена, данные удалены");
    }

    public void save(String requestedName) {
        String name = validateName(requestedName);
        if (name == null) {
            MessageUtil.displayError("Neuro: имя — 1-32 символа: A-Z, a-z, 0-9, _ или -");
            return;
        }

        TrainingSnapshot snapshot;
        synchronized (this) {
            if (trainingJob) {
                MessageUtil.displayWarning("Neuro: обучение уже выполняется");
                return;
            }
            if (session == null) {
                MessageUtil.displayError("Neuro: сначала запусти .neuro start");
                return;
            }
            if (session.sampleCount() < MINIMUM_SAMPLES || session.hitCount() < MINIMUM_HITS) {
                MessageUtil.displayWarning("Neuro: мало данных — " + session.sampleCount()
                        + "/" + MINIMUM_SAMPLES + " тиков, " + session.hitCount()
                        + "/" + MINIMUM_HITS + " ударов");
                return;
            }
            snapshot = session.snapshot();
            session = null;
            trainingJob = true;
        }
        finishRecordingState();
        MessageUtil.displayInfo("Neuro: запись готова — " + snapshot.samples().size()
                + " примеров, " + snapshot.hitCount() + " ударов. Обучаю " + name + "...");

        Thread.ofVirtual().name("Wyvern-Neuro-" + name).start(() -> {
            try {
                NeuroModel model = NeuroModel.train(name, snapshot.samples(),
                        snapshot.sensitivity(), snapshot.gcd(),
                        (epoch, total, loss) -> postInfo(String.format(Locale.ROOT,
                                "Neuro: эпоха %d/%d, loss %.5f", epoch, total, loss)));
                writeModel(model);
                MinecraftClient.getInstance().execute(() -> completeTraining(model));
            } catch (Throwable error) {
                synchronized (NeuroManager.this) {
                    trainingJob = false;
                }
                postError("Neuro: ошибка обучения — " + error.getMessage());
            }
        });
    }

    public synchronized String statusText() {
        if (session != null) {
            return "Neuro: запись — " + session.sampleCount() + " тиков, "
                    + session.hitCount() + " ударов";
        }
        if (trainingJob) {
            return "Neuro: сеть обучается в фоне";
        }
        return "Neuro: ожидание; моделей: " + models.size();
    }

    public synchronized String modelListText() {
        if (models.isEmpty()) {
            return "Neuro: сохранённых моделей нет";
        }
        return "Neuro: " + models.values().stream()
                .map(model -> model.getName() + " [" + model.getSampleCount() + "/" + model.getHitCount() + "]")
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .reduce((left, right) -> left + ", " + right)
                .orElse("");
    }

    @EventTarget
    private void onTick(EventTick event) {
        TrainingSession current;
        synchronized (this) {
            current = session;
        }
        if (current == null) {
            return;
        }
        if (mc.player == null || mc.world == null) {
            synchronized (this) {
                if (session == current) session = null;
            }
            finishRecordingState();
            return;
        }
        OtherClientPlayerEntity target = FakePlayer.INSTANCE.ensureTrainingTarget();
        if (target == null) {
            return;
        }
        int previousCount = current.sampleCount();
        current.capture(target);
        int count = current.sampleCount();
        if (count != previousCount && count > 0 && count % 100 == 0) {
            MessageUtil.displayInfo("Neuro: записано " + count + " тиков, ударов: " + current.hitCount());
        }
    }

    @EventTarget
    private void onAttack(EventAttack event) {
        TrainingSession current;
        synchronized (this) {
            current = session;
        }
        if (current == null || !FakePlayer.INSTANCE.isFakePlayer(event.getTarget())) {
            return;
        }
        event.cancel();
        // FakePlayer's own listener deliberately skips training hits to avoid
        // duplicate sounds. Capture the cooldown and crit state before it is
        // reset for the next local practice hit.
        FakePlayer.INSTANCE.simulateAttackFeedback();
        if (mc.player != null) {
            mc.player.resetLastAttackedTicks();
        }
        int before = current.hitCount();
        current.markAttack(FakePlayer.INSTANCE.getFakePlayer());
        if (current.hitCount() != before && current.hitCount() % 5 == 0) {
            MessageUtil.displayInfo("Neuro: ударов записано: " + current.hitCount());
        }
    }

    private void completeTraining(NeuroModel model) {
        synchronized (this) {
            models.put(key(model.getName()), model);
            trainingJob = false;
        }
        MessageUtil.displayInfo(String.format(Locale.ROOT,
                "Neuro(%s) готова: %d примеров, %d ударов, сенса %.3f",
                model.getName(), model.getSampleCount(), model.getHitCount(), model.getTrainedSensitivity()));
        MessageUtil.displayInfo("Neuro-модель сохранена");
    }

    private void finishRecordingState() {
        FakePlayer.INSTANCE.endTraining();
        boolean enableAura;
        synchronized (this) {
            enableAura = restoreAura;
            restoreAura = false;
        }
        if (enableAura && !Aura.INSTANCE.isEnabled()) {
            Aura.INSTANCE.setToggled(true);
        }
    }

    private void loadModels() {
        File[] files = modelDirectory.listFiles(file -> file.isFile()
                && file.getName().toLowerCase(Locale.ROOT).endsWith(FILE_SUFFIX));
        if (files == null) {
            return;
        }
        for (File file : files) {
            try {
                String json = Files.readString(file.toPath(), StandardCharsets.UTF_8);
                NeuroModel model = gson.fromJson(json, NeuroModel.class);
                if (model != null && model.isValid()) {
                    models.put(key(model.getName()), model);
                }
            } catch (Exception ignored) {
            }
        }
    }

    private void writeModel(NeuroModel model) throws IOException {
        Files.createDirectories(modelDirectory.toPath());
        File target = new File(modelDirectory, key(model.getName()) + FILE_SUFFIX);
        File temporary = new File(modelDirectory, target.getName() + ".tmp");
        Files.writeString(temporary.toPath(), gson.toJson(model), StandardCharsets.UTF_8);
        try {
            Files.move(temporary.toPath(), target.toPath(),
                    StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException ignored) {
            Files.move(temporary.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private void postInfo(String message) {
        MinecraftClient.getInstance().execute(() -> {
            if (mc.player != null) MessageUtil.displayInfo(message);
        });
    }

    private void postError(String message) {
        MinecraftClient.getInstance().execute(() -> {
            if (mc.player != null) MessageUtil.displayError(message);
        });
    }

    private static String validateName(String name) {
        if (name == null || !name.matches("[A-Za-z0-9_-]{1,32}")) {
            return null;
        }
        return name;
    }

    private static String key(String name) {
        return name.toLowerCase(Locale.ROOT);
    }

    private static final class TrainingSession {
        private final List<NeuroSample> samples = new ArrayList<>();
        private final double sensitivity;
        private final double gcd;
        private double[] previousInputs;
        private float previousYaw;
        private float previousPitch;
        private float previousYawStep;
        private float previousPitchStep;
        private float olderYawStep;
        private float olderPitchStep;
        private int ticksSinceAttack = 40;
        private int lastPlayerAge = Integer.MIN_VALUE;
        private int lastAttackAge = Integer.MIN_VALUE;
        private int hitCount;
        private boolean pendingAttack;

        private TrainingSession(double sensitivity, double gcd) {
            this.sensitivity = sensitivity;
            this.gcd = gcd;
        }

        private synchronized void capture(OtherClientPlayerEntity target) {
            if (mc.player == null || target == null || lastPlayerAge == mc.player.age) {
                return;
            }
            int age = mc.player.age;
            float currentYaw = mc.player.getYaw();
            float currentPitch = mc.player.getPitch();
            Rotation targetRotation = targetRotation(target);
            if (targetRotation == null) {
                return;
            }

            if (previousInputs != null) {
                float yawStep = MathHelper.wrapDegrees(currentYaw - previousYaw);
                float pitchStep = currentPitch - previousPitch;
                NeuroSample sample = new NeuroSample(previousInputs, yawStep, pitchStep,
                        samples.size(), pendingAttack);
                samples.add(sample);
                if (pendingAttack) {
                    hitCount++;
                    ticksSinceAttack = 0;
                    pendingAttack = false;
                } else {
                    ticksSinceAttack++;
                }
                olderYawStep = previousYawStep;
                olderPitchStep = previousPitchStep;
                previousYawStep = yawStep;
                previousPitchStep = pitchStep;
            }

            previousYaw = currentYaw;
            previousPitch = currentPitch;
            previousInputs = createInputs(target, targetRotation, currentYaw, currentPitch);
            lastPlayerAge = age;
        }

        private synchronized void markAttack(OtherClientPlayerEntity target) {
            if (mc.player == null || target == null || lastAttackAge == mc.player.age) {
                return;
            }
            lastAttackAge = mc.player.age;
            if (!samples.isEmpty() && lastPlayerAge == mc.player.age) {
                NeuroSample last = samples.getLast();
                if (!last.attack()) {
                    last.markAttack();
                    hitCount++;
                }
            } else {
                pendingAttack = true;
            }
            ticksSinceAttack = 0;
            Rotation targetRotation = targetRotation(target);
            if (targetRotation != null) {
                previousInputs = createInputs(target, targetRotation,
                        mc.player.getYaw(), mc.player.getPitch());
                previousYaw = mc.player.getYaw();
                previousPitch = mc.player.getPitch();
            }
        }

        private double[] createInputs(OtherClientPlayerEntity target, Rotation targetRotation,
                                      float yaw, float pitch) {
            return NeuroInputs.create(targetRotation.getYaw(), targetRotation.getPitch(),
                    yaw, pitch, previousYawStep, previousPitchStep,
                    olderYawStep, olderPitchStep,
                    mc.player.distanceTo(target), mc.player.getAttackCooldownProgress(0.5F),
                    ticksSinceAttack, mc.player.getVelocity());
        }

        private Rotation targetRotation(OtherClientPlayerEntity target) {
            if (mc.player == null || target == null) {
                return null;
            }
            return RotationUtil.fromVec3d(target.getBoundingBox().getCenter()
                    .subtract(mc.player.getEyePos()));
        }

        private synchronized int sampleCount() {
            return samples.size();
        }

        private synchronized int hitCount() {
            return hitCount;
        }

        private synchronized TrainingSnapshot snapshot() {
            List<NeuroSample> copy = samples.stream().map(NeuroSample::new).toList();
            return new TrainingSnapshot(copy, hitCount, sensitivity, gcd);
        }
    }

    private record TrainingSnapshot(List<NeuroSample> samples, int hitCount,
                                    double sensitivity, double gcd) {
    }
}
