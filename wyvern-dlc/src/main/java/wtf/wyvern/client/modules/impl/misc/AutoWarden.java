package wtf.wyvern.client.modules.impl.misc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.WorldChunk;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.BooleanSetting;
import wtf.wyvern.client.modules.api.setting.impl.ButtonSetting;
import wtf.wyvern.client.modules.api.setting.impl.SliderSetting;
import wtf.wyvern.client.modules.api.setting.impl.StringSetting;
import wtf.wyvern.core.eventbus.EventTarget;
import wtf.wyvern.core.events.impl.player.EventInteractBlock;
import wtf.wyvern.core.events.impl.player.EventUpdate;
import wtf.wyvern.utility.game.other.BaritoneBridge;
import wtf.wyvern.utility.game.other.BossBarUtil;

import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static wtf.wyvern.utility.interfaces.IMinecraft.mc;

@ModuleAnnotation(
        name = "AutoWarden",
        category = Category.MISC,
        description = "Автоматизирует маршрут по сундукам фермы вардена"
)
public final class AutoWarden extends Module {
    public static final AutoWarden INSTANCE = new AutoWarden();

    private enum State {
        DRINK_POTION,
        TP_WARDEN,
        VERIFY_TP_WARDEN,
        LOOT_WARDEN,
        REDEEM_DARENA,
        WAIT_ESCAPE,
        LEAVE_ARENA,
        TP_BASE,
        VERIFY_TP_BASE,
        DEPOSIT
    }

    private enum SetupStage {
        SELECT_LOOT_CHESTS,
        SELECT_POTION_CHESTS,
        SET_BASE_CMD,
        SET_WARDEN_CMD,
        DONE
    }

    private static final long ACTION_DELAY_MS = 150L;
    private static final long LOOT_ACTION_MIN_MS = 220L;
    private static final long LOOT_ACTION_JITTER_MS = 280L;
    private static final long PATH_REPATH_MS = 500L;
    private static final long CHEST_ZERO_BUFFER_MS = 1000L;
    private static final long NO_CHEST_TIMEOUT_MS = 4000L;
    private static final long KNOWN_TIMER_TTL_MS = 8000L;
    private static final long ESCAPE_MAX_WAIT_MS = 40000L;
    private static final int APPROACH_TIMER_THRESHOLD_S = 40;
    private static final int COMMIT_TIMER_THRESHOLD_S = 10;
    private static final long UNDER_ATTACK_WINDOW_MS = 3000L;
    private static final double TP_MOVE_THRESHOLD_SQ = 16.0;

    private static final long RESPAWN_SETTLE_MS = 3000L;
    private static final long JUMP_MIN_INTERVAL_MS = 900L;
    private static final long JUMP_MAX_INTERVAL_MS = 6000L;
    private static final long WOBBLE_MIN_INTERVAL_MS = 350L;
    private static final long WOBBLE_MAX_INTERVAL_MS = 2200L;
    private static final double WOBBLE_MIN_STRENGTH = 0.015;
    private static final double WOBBLE_MAX_STRENGTH = 0.075;
    // Ванильный лимит взаимодействия с блоком - 4.5 блока от глаз игрока.
    private static final double REACH_SQ = 4.5 * 4.5;
    private static final long OPEN_TIMEOUT_MS = 1200L;
    private static final int MAX_OPEN_ATTEMPTS = 4;
    private static final long TP_MIN_WAIT_MS = 8000L;
    private static final long TP_BOSSBAR_MAX_WAIT_MS = 20000L;
    // Сервер показывает, что игрок невидим, но не говорит на сколько - считаем запас достаточным.
    private static final int UNKNOWN_DURATION = Integer.MAX_VALUE;
    private static final double LOOT_SCAN_RANGE = 48.0;
    private static final Pattern TIMER_PATTERN = Pattern.compile("(\\d{1,2}):(\\d{2})");
    // INSTANCE создаётся первым в <clinit>, а его конструктор читает конфиг, поэтому
    // путь и Gson нельзя держать в static final полях - они бы ещё были null.
    private static Path setupPath() {
        return FabricLoader.getInstance().getConfigDir().resolve("wyvern").resolve("autowarden_setup.json");
    }

    private static Gson setupGson() {
        return SetupGsonHolder.GSON;
    }

    private static final class SetupGsonHolder {
        private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    }

    public final StringSetting wardenHomeCmd = new StringSetting("Команда на вардена", "clan home ward", 64, () -> true);
    public final StringSetting baseHomeCmd = new StringSetting("Команда на базу", "clan home storage", 64, () -> true);
    public final StringSetting bossBarKeyword = new StringSetting("Ключевое слово босс-бара", "", 32, () -> true);
    public final SliderSetting fleeDistance = new SliderSetting("Дистанция побега", 10.0F, 4.0F, 24.0F, 1.0F);
    public final SliderSetting playerPanicDistance = new SliderSetting("Дистанция паники", 5.0F, 2.0F, 12.0F, 1.0F);
    public final BooleanSetting avoidPlayers = new BooleanSetting("Избегать игроков", true);
    public final BooleanSetting chatLogs = new BooleanSetting("Логи в чат", true);
    public final SliderSetting minInvisSeconds = new SliderSetting("Мин. запас невидимости (сек)", 180.0F, 10.0F, 480.0F, 10.0F);
    public final SliderSetting potionDurationSeconds = new SliderSetting("Длительность зелья (сек)", 180.0F, 30.0F, 900.0F, 10.0F);
    public final BooleanSetting humanMovement = new BooleanSetting("Человечная ходьба", true);
    public final SliderSetting jumpRate = new SliderSetting("Частота прыжков", 45.0F, 0.0F, 100.0F, 5.0F,
            () -> INSTANCE.humanMovement.isEnabled());
    public final SliderSetting wobbleRate = new SliderSetting("Виляние", 40.0F, 0.0F, 100.0F, 5.0F,
            () -> INSTANCE.humanMovement.isEnabled());
    public final ButtonSetting resetSetupButton = new ButtonSetting("Сбросить настройку", () -> INSTANCE.resetSetup());

    private final BaritoneBridge baritone = new BaritoneBridge();

    private SetupStage setupStage = SetupStage.SELECT_LOOT_CHESTS;
    private boolean setupComplete;
    private boolean prevSneaking;
    private final List<BlockPos> lootChests = new ArrayList<>();
    private final List<BlockPos> potionChests = new ArrayList<>();

    private State state = State.DRINK_POTION;
    private long stateUntil;
    private long lastActionAt;
    private long lastLootActionAt;
    private long nextLootActionDelay = LOOT_ACTION_MIN_MS;
    private long lastPathAt;
    private long noChestSince;
    private boolean drinking;
    private int potionSplitStep;
    private int potionSplitContainerSlotId = -1;
    private boolean hasLootedThisCycle;
    private boolean sawBossBar;
    private long escapeDeadline;
    private BlockPos targetChest;
    private BlockPos lastAnnouncedTarget;
    private Vec3d preTpPos;
    private boolean sawTpBossBar;
    private long tpDeadline;
    private float lastHealth = -1.0F;
    private long lastHitAt;
    private long assumedInvisUntil;
    private boolean loggedInvisState;
    private long nextJumpAt;
    private long nextWobbleAt;
    private boolean awaitingRespawn;
    private long respawnedAt;
    private BlockPos openTarget;
    private long openAttemptAt;
    private int openAttempts;
    private final Set<BlockPos> unreachableChests = new HashSet<>();
    private final Set<BlockPos> lootedChests = new HashSet<>();
    private final Map<BlockPos, Long> zeroSince = new HashMap<>();
    private final Map<BlockPos, Integer> knownTimers = new HashMap<>();
    private final Map<BlockPos, Long> knownTimerAt = new HashMap<>();

    public AutoWarden() {
        loadSetup();
    }

    @Override
    public void onEnable() {
        resetState();
        baritone.configureSafeMovement();
        if (!setupComplete) {
            prevSneaking = mc.player != null && mc.player.isSneaking();
            announceSetupStage();
        }
        super.onEnable();
    }

    @Override
    public void onDisable() {
        stopDrinking();
        baritone.stop();
        resetState();
        super.onDisable();
    }

    private void resetState() {
        state = State.DRINK_POTION;
        stateUntil = 0L;
        lastActionAt = 0L;
        lastLootActionAt = 0L;
        nextLootActionDelay = LOOT_ACTION_MIN_MS;
        lastPathAt = 0L;
        noChestSince = 0L;
        drinking = false;
        potionSplitStep = 0;
        potionSplitContainerSlotId = -1;
        hasLootedThisCycle = false;
        sawBossBar = false;
        escapeDeadline = 0L;
        targetChest = null;
        lastAnnouncedTarget = null;
        preTpPos = null;
        sawTpBossBar = false;
        tpDeadline = 0L;
        lastHealth = -1.0F;
        lastHitAt = 0L;
        openTarget = null;
        openAttemptAt = 0L;
        openAttempts = 0;
        assumedInvisUntil = 0L;
        loggedInvisState = false;
        nextJumpAt = 0L;
        nextWobbleAt = 0L;
        unreachableChests.clear();
        lootedChests.clear();
        zeroSince.clear();
        knownTimers.clear();
        knownTimerAt.clear();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mc.player == null || mc.world == null || mc.interactionManager == null) {
            return;
        }

        if (!setupComplete) {
            tickSetup();
            return;
        }

        long now = System.currentTimeMillis();

        if (handleDeath(now)) {
            return;
        }

        if (!drinking && potionSplitStep == 0 && mc.currentScreen == null) {
            selectEmptyHotbarSlot();
        }

        updateCombatTracking(now);
        tickHumanMovement(now);
        switch (state) {
            case DRINK_POTION -> tickDrinkPotion(now);
            case TP_WARDEN -> tickTpWarden(now);
            case VERIFY_TP_WARDEN -> tickVerifyTpWarden(now);
            case LOOT_WARDEN -> tickLootWarden(now);
            case REDEEM_DARENA -> tickRedeemDarena(now);
            case WAIT_ESCAPE -> tickWaitEscape(now);
            case LEAVE_ARENA -> tickLeaveArena(now);
            case TP_BASE -> tickTpBase(now);
            case VERIFY_TP_BASE -> tickVerifyTpBase(now);
            case DEPOSIT -> tickDeposit(now);
        }
    }

    @EventTarget
    public void onInteractBlock(EventInteractBlock event) {
        if (setupComplete || mc.world == null) {
            return;
        }
        if (setupStage != SetupStage.SELECT_LOOT_CHESTS && setupStage != SetupStage.SELECT_POTION_CHESTS) {
            return;
        }
        BlockPos pos = event.getPos().toImmutable();
        var block = mc.world.getBlockState(pos).getBlock();
        if (!(block instanceof ChestBlock) && !(block instanceof BarrelBlock)) {
            return;
        }
        event.cancel();

        List<BlockPos> list = setupStage == SetupStage.SELECT_LOOT_CHESTS ? lootChests : potionChests;
        if (list.contains(pos)) {
            list.remove(pos);
            log("&cСундук снят с пометки &7(" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ")");
        } else {
            list.add(pos);
            log("&aСундук помечен &7(" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ") &7[" + list.size() + "]");
        }
        saveSetup();
    }

    private void tickSetup() {
        boolean sneaking = mc.player.isSneaking();
        boolean justSneaked = sneaking && !prevSneaking;
        prevSneaking = sneaking;
        if (justSneaked) {
            advanceSetupStage();
        }
    }

    private void advanceSetupStage() {
        switch (setupStage) {
            case SELECT_LOOT_CHESTS -> {
                log("&aПомечено сундуков для лута: &f" + lootChests.size());
                setupStage = SetupStage.SELECT_POTION_CHESTS;
                announceSetupStage();
            }
            case SELECT_POTION_CHESTS -> {
                log("&aПомечено сундуков с зельями: &f" + potionChests.size());
                setupStage = SetupStage.SET_BASE_CMD;
                announceSetupStage();
            }
            case SET_BASE_CMD -> {
                setupStage = SetupStage.SET_WARDEN_CMD;
                announceSetupStage();
            }
            case SET_WARDEN_CMD -> {
                setupStage = SetupStage.DONE;
                setupComplete = true;
                log("&aНастройка завершена! Модуль готов к работе.");
            }
            case DONE -> {
            }
        }
        saveSetup();
    }

    private void announceSetupStage() {
        switch (setupStage) {
            case SELECT_LOOT_CHESTS ->
                    log("&eНастройка AutoWarden: кликай по сундукам, куда складывать лут с вардена. Присядь, чтобы продолжить.");
            case SELECT_POTION_CHESTS ->
                    log("&eТеперь кликай по сундукам с зельями невидимости. Присядь, чтобы продолжить.");
            case SET_BASE_CMD ->
                    log("&eУкажи команду телепорта на базу в настройке &fКоманда на базу&e (сейчас: &f" + baseHomeCmd.getValue() + "&e). Присядь, чтобы продолжить.");
            case SET_WARDEN_CMD ->
                    log("&eУкажи команду телепорта на вардена в настройке &fКоманда на вардена&e (сейчас: &f" + wardenHomeCmd.getValue() + "&e). Присядь, чтобы продолжить.");
            case DONE -> {
            }
        }
    }

    private void resetSetup() {
        lootChests.clear();
        potionChests.clear();
        setupComplete = false;
        setupStage = SetupStage.SELECT_LOOT_CHESTS;
        prevSneaking = mc.player != null && mc.player.isSneaking();
        saveSetup();
        log("&cНастройка сброшена, начинаем заново.");
        announceSetupStage();
    }

    /**
     * Помеченные при настройке сундуки нельзя отсеивать по наличию блок-энтити: сразу
     * после телепорта их чанк ещё не прогружен, и проверка выкидывала бы все метки, из-за
     * чего модуль стоял на месте. Отбрасываем только те метки, чей чанк уже загружен и
     * сундука там больше нет.
     */
    private BlockPos nearestFrom(List<BlockPos> candidates) {
        List<BlockPos> valid = new ArrayList<>();
        for (BlockPos pos : candidates) {
            if (unreachableChests.contains(pos)) {
                continue;
            }
            if (!isChunkLoaded(pos) || isContainer(pos)) {
                valid.add(pos);
            }
        }
        return nearest(valid);
    }

    private boolean isChunkLoaded(BlockPos pos) {
        return mc.world.getChunkManager().getChunk(pos.getX() >> 4, pos.getZ() >> 4, ChunkStatus.FULL, false) != null;
    }

    private boolean isContainer(BlockPos pos) {
        var block = mc.world.getBlockState(pos).getBlock();
        return block instanceof ChestBlock || block instanceof BarrelBlock;
    }

    /**
     * Ведёт игрока к сундуку и открывает его. Возвращает true, пока цель не открыта -
     * вызывающему состоянию остаётся только выйти из тика.
     *
     * Расстояние меряется от глаз, а не от ног: сундуки на базе часто стоят на другой
     * высоте, и проверка по позиции ног не давала подойти на "дистанцию открытия".
     */
    private boolean approachAndOpen(BlockPos pos, long now) {
        if (!withinReach(pos)) {
            pathTo(pos, now);
            return true;
        }

        baritone.stop();

        if (!pos.equals(openTarget)) {
            openTarget = pos;
            openAttempts = 0;
            openAttemptAt = 0L;
        }

        if (openAttemptAt != 0L && now - openAttemptAt < OPEN_TIMEOUT_MS) {
            return true;
        }

        if (openAttempts >= MAX_OPEN_ATTEMPTS) {
            unreachableChests.add(pos);
            openTarget = null;
            openAttempts = 0;
            openAttemptAt = 0L;
            targetChest = null;
            log("&cСундук не открывается &7(" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ")&c, пропускаю");
            return true;
        }

        openContainer(pos);
        openAttempts++;
        openAttemptAt = now;
        lastActionAt = now;
        return true;
    }

    private boolean withinReach(BlockPos pos) {
        return mc.player.getEyePos().squaredDistanceTo(Vec3d.ofCenter(pos)) <= REACH_SQ;
    }

    private void clearOpenTarget() {
        openTarget = null;
        openAttempts = 0;
        openAttemptAt = 0L;
    }

    private static final class SetupData {
        boolean setupComplete;
        String stage = SetupStage.SELECT_LOOT_CHESTS.name();
        List<int[]> lootChests = new ArrayList<>();
        List<int[]> potionChests = new ArrayList<>();
    }

    private void loadSetup() {
        Path path = setupPath();
        if (!Files.exists(path)) {
            return;
        }
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            SetupData data = setupGson().fromJson(reader, SetupData.class);
            if (data == null) {
                return;
            }
            setupComplete = data.setupComplete;
            try {
                setupStage = SetupStage.valueOf(data.stage);
            } catch (IllegalArgumentException | NullPointerException ignored) {
                setupStage = SetupStage.SELECT_LOOT_CHESTS;
            }
            lootChests.clear();
            for (int[] p : data.lootChests) {
                lootChests.add(new BlockPos(p[0], p[1], p[2]));
            }
            potionChests.clear();
            for (int[] p : data.potionChests) {
                potionChests.add(new BlockPos(p[0], p[1], p[2]));
            }
        } catch (Exception ignored) {
        }
    }

    private void saveSetup() {
        try {
            SetupData data = new SetupData();
            data.setupComplete = setupComplete;
            data.stage = setupStage.name();
            for (BlockPos pos : lootChests) {
                data.lootChests.add(new int[]{pos.getX(), pos.getY(), pos.getZ()});
            }
            for (BlockPos pos : potionChests) {
                data.potionChests.add(new int[]{pos.getX(), pos.getY(), pos.getZ()});
            }
            Path path = setupPath();
            Files.createDirectories(path.getParent());
            try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                setupGson().toJson(data, writer);
            }
        } catch (Exception ignored) {
        }
    }

    private void tickDrinkPotion(long now) {
        StatusEffectInstance invis = mc.player.getStatusEffect(StatusEffects.INVISIBILITY);
        int secondsLeft = invisibilitySecondsLeft(now, invis);
        if (hasInvisibility(now, invis) && secondsLeft >= (int) minInvisSeconds.getCurrent()) {
            if (!loggedInvisState) {
                loggedInvisState = true;
                log("&bНевидимость есть &7(" + (secondsLeft == UNKNOWN_DURATION ? "время неизвестно" : secondsLeft + "s") + ")");
            }
            state = State.TP_WARDEN;
            return;
        }
        loggedInvisState = false;

        if (drinking) {
            tickDrinking(now, invis);
            return;
        }

        if (mc.currentScreen instanceof HandledScreen<?> screen) {
            baritone.stop();
            clearOpenTarget();
            if (!canLootAct(now)) {
                return;
            }

            // Экран нельзя закрывать, пока разбор стака не завершён: остаток с курсора
            // при закрытии улетает в инвентарь, и вместо одного зелья забирается весь слот.
            boolean splitInProgress = potionSplitStep != 0
                    || !screen.getScreenHandler().getCursorStack().isEmpty();

            if (!splitInProgress && hasInvisPotionInInventory()) {
                mc.player.closeHandledScreen();
                lastActionAt = now;
                return;
            }
            if (tickTakeOnePotion(screen)) {
                markLootAction(now);
                lastActionAt = now;
                return;
            }
            if (splitInProgress) {
                return;
            }
            potionSplitStep = 0;
            mc.player.closeHandledScreen();
            lastActionAt = now;
            return;
        }

        if (hasInvisPotionInInventory()) {
            beginDrinking(now);
            return;
        }

        if (!canAct(now)) {
            return;
        }

        boolean targetGone = targetChest != null
                && (unreachableChests.contains(targetChest)
                || (isChunkLoaded(targetChest) && !isContainer(targetChest)));
        if (targetChest == null || targetGone) {
            targetChest = nearestFrom(potionChests);
            if (targetChest == null) {
                targetChest = findMarkedInvisChest();
            }
            if (targetChest == null) {
                targetChest = findNearestContainer();
            }
        }
        if (targetChest == null) {
            return;
        }
        approachAndOpen(targetChest, now);
    }

    /**
     * Наличие невидимости определяется тремя источниками, потому что одного списка
     * эффектов мало: многие сервера его клиенту не синкают. Флаг невидимости в метаданных
     * сущности приходит всегда, а свой таймер после выпитого зелья закрывает случай,
     * когда сервер прячет и то и другое.
     */
    private boolean hasInvisibility(long now, StatusEffectInstance invis) {
        if (invis != null) {
            return true;
        }
        if (mc.player.isInvisible()) {
            return true;
        }
        return now < assumedInvisUntil;
    }

    /** Остаток в секундах или {@link #UNKNOWN_DURATION}, если источник времени не даёт. */
    private int invisibilitySecondsLeft(long now, StatusEffectInstance invis) {
        if (invis != null) {
            return invis.getDuration() / 20;
        }
        if (now < assumedInvisUntil) {
            return (int) ((assumedInvisUntil - now) / 1000L);
        }
        return mc.player.isInvisible() ? UNKNOWN_DURATION : 0;
    }

    private void beginDrinking(long now) {
        int slot = findPotionHotbarSlot();
        if (slot == -1) {
            int screenSlot = findPotionScreenSlot();
            if (screenSlot == -1) {
                return;
            }
            mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, screenSlot, 8, SlotActionType.SWAP, mc.player);
            lastActionAt = now;
            return;
        }
        selectHotbarSlot(slot);
        mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
        mc.options.useKey.setPressed(true);
        drinking = true;
        stateUntil = now + 3000L;
        log("&bПью зелье невидимости");
    }

    private void tickDrinking(long now, StatusEffectInstance invis) {
        if (invis != null || now >= stateUntil || !mc.player.isUsingItem()) {
            // Сервер может не присылать эффект клиенту, поэтому держим свой таймер:
            // без него модуль считал бы, что баффа нет, и уходил бы с фермы сразу.
            assumedInvisUntil = now + (long) (potionDurationSeconds.getCurrent() * 1000.0F);
            stopDrinking();
            selectEmptyHotbarSlot();
            targetChest = null;
            state = State.TP_WARDEN;
            return;
        }
        mc.options.useKey.setPressed(true);
    }

    private void stopDrinking() {
        drinking = false;
        if (mc.options != null) {
            mc.options.useKey.setPressed(false);
        }
        if (mc.player != null && mc.player.isUsingItem() && mc.interactionManager != null) {
            mc.interactionManager.stopUsingItem(mc.player);
        }
    }

    private void tickTpWarden(long now) {
        if (!canAct(now) || mc.player.networkHandler == null) {
            return;
        }
        String cmd = wardenHomeCmd.getValue();
        if (cmd == null || cmd.isBlank()) {
            return;
        }
        preTpPos = mc.player.getPos();
        mc.player.networkHandler.sendChatCommand(cmd);
        lootedChests.clear();
        zeroSince.clear();
        knownTimers.clear();
        knownTimerAt.clear();
        targetChest = null;
        unreachableChests.clear();
        clearOpenTarget();
        hasLootedThisCycle = false;
        sawTpBossBar = false;
        tpDeadline = now + TP_BOSSBAR_MAX_WAIT_MS;
        state = State.VERIFY_TP_WARDEN;
        stateUntil = now + TP_MIN_WAIT_MS;
        lastActionAt = now;
        log("&aТелепорт на вардена (&f" + cmd + "&a)");
    }

    private void tickVerifyTpWarden(long now) {
        if (waitingOnTpBossBar(now)) {
            return;
        }
        if (preTpPos != null && mc.player.getPos().squaredDistanceTo(preTpPos) < TP_MOVE_THRESHOLD_SQ) {
            log("&cКоманда телепорта на вардена (&f" + wardenHomeCmd.getValue() + "&c) не сработала, отключаю модуль");
            setToggled(false);
            return;
        }

        noChestSince = now;
        state = State.LOOT_WARDEN;
        stateUntil = now;
    }

    private boolean waitingOnTpBossBar(long now) {
        if (now >= tpDeadline) {
            return false;
        }
        if (findTrackedBossBar() != null) {
            sawTpBossBar = true;
            return true;
        }
        return now < stateUntil;
    }

    private void tickLootWarden(long now) {
        if (now < stateUntil) {
            return;
        }

        if (mc.currentScreen instanceof HandledScreen<?> screen) {
            baritone.stop();
            clearOpenTarget();
            if (!canLootAct(now)) {
                return;
            }
            if (quickMoveContainerItem(screen, stack -> !stack.isEmpty(), true)) {
                hasLootedThisCycle = true;
                markLootAction(now);
                lastActionAt = now;
                return;
            }
            if (targetChest != null) {
                lootedChests.add(targetChest);
                knownTimers.remove(targetChest);
                knownTimerAt.remove(targetChest);
                zeroSince.remove(targetChest);
                log("&eСундук залутан &7(" + targetChest.getX() + ", " + targetChest.getY() + ", " + targetChest.getZ() + ")");
            }
            mc.player.closeHandledScreen();
            targetChest = null;
            lastActionAt = now;
            return;
        }

        if (fleeFromThreatsIfNeeded(now)) {
            return;
        }

        if (!hasInvisibility(now, mc.player.getStatusEffect(StatusEffects.INVISIBILITY))) {
            baritone.stop();
            log("&cНевидимость закончилась, ухожу немедленно");
            state = State.REDEEM_DARENA;
            return;
        }

        if (isInventoryFull()) {
            baritone.stop();
            log("&cИнвентарь полон, иду сдавать лут");
            state = State.REDEEM_DARENA;
            return;
        }

        if (!canAct(now)) {
            return;
        }

        scanChestTimers(now);

        BlockPos readyChest = findReadyLootChest(now);
        if (readyChest != null) {
            noChestSince = now;
            targetChest = readyChest;
            if (mc.player.squaredDistanceTo(Vec3d.ofCenter(readyChest)) > 9.0) {
                pathTo(readyChest, now);
                return;
            }
            openContainer(readyChest);
            lastActionAt = now;
            return;
        }

        BlockPos smallest = findSmallestKnownCandidate();
        Integer smallestTimer = smallest != null ? knownTimers.get(smallest) : null;
        boolean smallestImminent = smallestTimer != null && smallestTimer <= COMMIT_TIMER_THRESHOLD_S;

        BlockPos candidate;
        boolean known;
        if (smallest != null && smallestImminent) {
            candidate = smallest;
            known = true;
        } else {
            BlockPos unrevealed = findNearestUnrevealedContainer();
            if (unrevealed != null) {
                candidate = unrevealed;
                known = false;
            } else if (smallest != null) {
                candidate = smallest;
                known = true;
            } else {
                candidate = findNearestUnlootedContainer();
                known = false;
            }
        }

        if (candidate == null) {
            if (now - noChestSince > NO_CHEST_TIMEOUT_MS) {
                baritone.stop();
                state = State.REDEEM_DARENA;
            }
            return;
        }

        noChestSince = now;
        targetChest = candidate;
        if (!candidate.equals(lastAnnouncedTarget)) {
            lastAnnouncedTarget = candidate;
            if (known) {
                Integer cd = knownTimers.get(candidate);
                log("&bИду к сундуку с меньшим кд &7(" + (cd == null ? "?" : cd + "s") + ")");
            } else {
                log("&7Подхожу прогрузить голограмму ближайшего сундука");
            }
        }
        if (mc.player.squaredDistanceTo(Vec3d.ofCenter(candidate)) > 9.0) {
            pathTo(candidate, now);
        }
    }

    private boolean fleeFromThreatsIfNeeded(long now) {
        if (!avoidPlayers.isEnabled()) {
            return false;
        }
        if (now - lastHitAt > UNDER_ATTACK_WINDOW_MS) {
            return false;
        }
        BlockPos threat = findNearestPlayer();
        if (threat == null) {
            return false;
        }
        double dist = mc.player.squaredDistanceTo(Vec3d.ofCenter(threat));
        if (dist > playerPanicDistance.getCurrent() * playerPanicDistance.getCurrent()) {
            return false;
        }
        if (now - lastPathAt >= PATH_REPATH_MS) {
            baritone.fleeFrom(threat);
            lastPathAt = now;
        }
        return true;
    }

    private BlockPos findNearestPlayer() {
        BlockPos nearestPlayer = null;
        double nearestPlayerDist = Double.MAX_VALUE;
        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player == mc.player) {
                continue;
            }
            double dist = mc.player.squaredDistanceTo(player);
            if (dist < nearestPlayerDist) {
                nearestPlayerDist = dist;
                nearestPlayer = player.getBlockPos();
            }
        }
        return nearestPlayer;
    }

    /**
     * После смерти игрока сервер кидает на спавн, и вся цепочка состояний становится
     * бессмысленной. Ждём респавна, а затем начинаем цикл заново с телепорта на базу:
     * там сложим остатки лута, выпьем невидимость и уйдём к вардену.
     */
    private boolean handleDeath(long now) {
        boolean dead = !mc.player.isAlive() || mc.player.getHealth() <= 0.0F;
        if (dead) {
            if (!awaitingRespawn) {
                awaitingRespawn = true;
                stopDrinking();
                baritone.stop();
                log("&cПогиб, жду респавна");
            }
            return true;
        }

        if (!awaitingRespawn) {
            return false;
        }

        // Даём миру прогрузиться после респавна, иначе команда телепорта уйдёт впустую.
        if (respawnedAt == 0L) {
            respawnedAt = now;
            return true;
        }
        if (now - respawnedAt < RESPAWN_SETTLE_MS) {
            return true;
        }

        awaitingRespawn = false;
        respawnedAt = 0L;
        resetState();
        state = State.TP_BASE;
        stateUntil = now;
        log("&eВозрождение: иду на базу за баффом");
        return true;
    }

    /**
     * Baritone ведёт по идеальной прямой и без прыжков, что читается со стороны как бот.
     * Поверх его управления добавляем прыжки и небольшие боковые импульсы, чтобы трек
     * выглядел как у живого игрока. Пути это не ломает: Baritone каждый тик доруливает
     * обратно к своей цели.
     */
    private void tickHumanMovement(long now) {
        if (!humanMovement.isEnabled() || mc.currentScreen != null || drinking) {
            return;
        }
        if (!baritone.isPathing()) {
            return;
        }

        Vec3d velocity = mc.player.getVelocity();
        double horizontalSpeed = Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z);
        // Игрок стоит или упёрся - дёргать его в этот момент только подозрительно.
        if (horizontalSpeed < 0.08 || !mc.player.isOnGround()) {
            return;
        }

        float jumpPercent = jumpRate.getCurrent();
        if (jumpPercent > 0.0F) {
            if (nextJumpAt == 0L) {
                nextJumpAt = now + rollInterval(jumpPercent, JUMP_MIN_INTERVAL_MS, JUMP_MAX_INTERVAL_MS);
            } else if (now >= nextJumpAt) {
                mc.player.jump();
                nextJumpAt = now + rollInterval(jumpPercent, JUMP_MIN_INTERVAL_MS, JUMP_MAX_INTERVAL_MS);
            }
        }

        float wobblePercent = wobbleRate.getCurrent();
        if (wobblePercent > 0.0F) {
            if (nextWobbleAt == 0L) {
                nextWobbleAt = now + rollInterval(wobblePercent, WOBBLE_MIN_INTERVAL_MS, WOBBLE_MAX_INTERVAL_MS);
            } else if (now >= nextWobbleAt) {
                double strength = WOBBLE_MIN_STRENGTH
                        + (WOBBLE_MAX_STRENGTH - WOBBLE_MIN_STRENGTH) * (wobblePercent / 100.0);
                strength *= 0.6 + ThreadLocalRandom.current().nextDouble() * 0.8;
                if (ThreadLocalRandom.current().nextBoolean()) {
                    strength = -strength;
                }
                Vec3d side = new Vec3d(-velocity.z, 0.0, velocity.x).normalize().multiply(strength);
                mc.player.setVelocity(velocity.add(side));
                nextWobbleAt = now + rollInterval(wobblePercent, WOBBLE_MIN_INTERVAL_MS, WOBBLE_MAX_INTERVAL_MS);
            }
        }
    }

    /** Чем выше процент, тем короче пауза; поверх неё всегда есть случайный разброс. */
    private long rollInterval(float percent, long minInterval, long maxInterval) {
        double scale = Math.clamp(percent / 100.0F, 0.0F, 1.0F);
        double base = maxInterval - (maxInterval - minInterval) * scale;
        double jitter = base * 0.45;
        return (long) (base - jitter + ThreadLocalRandom.current().nextDouble() * jitter * 2.0);
    }

    private void updateCombatTracking(long now) {
        float health = mc.player.getHealth();
        if (lastHealth >= 0.0F && health < lastHealth - 0.01F) {
            lastHitAt = now;
        }
        lastHealth = health;
    }

    private void scanChestTimers(long now) {
        for (Entity entity : mc.world.getEntities()) {
            if (!(entity instanceof ArmorStandEntity as) || !as.isCustomNameVisible()) {
                continue;
            }
            Text name = as.getCustomName();
            if (name == null || name.getString().isEmpty()) {
                continue;
            }
            if (mc.player.distanceTo(as) > LOOT_SCAN_RANGE) {
                continue;
            }

            BlockPos chestPos = findChestBelow(as.getBlockPos());
            if (chestPos == null || lootedChests.contains(chestPos) || isMarkedBaseChest(chestPos)) {
                continue;
            }

            int timer = parseTimerSeconds(name.getString());
            if (timer < 0) {
                continue;
            }

            knownTimers.put(chestPos, timer);
            knownTimerAt.put(chestPos, now);
            if (timer > 0) {
                zeroSince.remove(chestPos);
            } else {
                zeroSince.putIfAbsent(chestPos, now);
            }
        }

        knownTimers.keySet().removeIf(pos -> now - knownTimerAt.getOrDefault(pos, 0L) > KNOWN_TIMER_TTL_MS);
        zeroSince.keySet().removeIf(pos -> !knownTimers.containsKey(pos));
    }

    private BlockPos findReadyLootChest(long now) {
        BlockPos best = null;
        double bestDist = Double.MAX_VALUE;

        for (Map.Entry<BlockPos, Integer> entry : knownTimers.entrySet()) {
            if (entry.getValue() != 0) {
                continue;
            }
            Long since = zeroSince.get(entry.getKey());
            if (since == null || now - since < CHEST_ZERO_BUFFER_MS) {
                continue;
            }
            double dist = mc.player.squaredDistanceTo(Vec3d.ofCenter(entry.getKey()));
            if (dist < bestDist) {
                bestDist = dist;
                best = entry.getKey();
            }
        }
        return best;
    }

    private BlockPos findSmallestKnownCandidate() {
        BlockPos best = null;
        int bestTimer = Integer.MAX_VALUE;
        double bestDist = Double.MAX_VALUE;

        for (Map.Entry<BlockPos, Integer> entry : knownTimers.entrySet()) {
            int timer = entry.getValue();
            if (timer <= 0 || timer > APPROACH_TIMER_THRESHOLD_S) {
                continue;
            }
            double dist = mc.player.squaredDistanceTo(Vec3d.ofCenter(entry.getKey()));
            if (timer < bestTimer || (timer == bestTimer && dist < bestDist)) {
                bestTimer = timer;
                bestDist = dist;
                best = entry.getKey();
            }
        }
        return best;
    }

    private BlockPos findNearestUnrevealedContainer() {
        List<BlockPos> containers = new ArrayList<>();
        for (WorldChunk chunk : loadedChunks()) {
            for (BlockEntity blockEntity : chunk.getBlockEntities().values()) {
                if (!(blockEntity instanceof ChestBlockEntity) && !(blockEntity instanceof BarrelBlockEntity)) {
                    continue;
                }
                BlockPos pos = blockEntity.getPos().toImmutable();
                if (!lootedChests.contains(pos) && !knownTimers.containsKey(pos) && !isMarkedBaseChest(pos)) {
                    containers.add(pos);
                }
            }
        }
        return nearest(containers);
    }

    private BlockPos findNearestUnlootedContainer() {
        List<BlockPos> containers = new ArrayList<>();
        for (WorldChunk chunk : loadedChunks()) {
            for (BlockEntity blockEntity : chunk.getBlockEntities().values()) {
                if (!(blockEntity instanceof ChestBlockEntity) && !(blockEntity instanceof BarrelBlockEntity)) {
                    continue;
                }
                BlockPos pos = blockEntity.getPos().toImmutable();
                if (!lootedChests.contains(pos) && !isMarkedBaseChest(pos)) {
                    containers.add(pos);
                }
            }
        }
        return nearest(containers);
    }

    private boolean isMarkedBaseChest(BlockPos pos) {
        return potionChests.contains(pos) || lootChests.contains(pos);
    }

    private BlockPos findChestBelow(BlockPos armorStandPos) {
        for (int dy = 0; dy <= 3; dy++) {
            BlockPos check = armorStandPos.down(dy);
            if (mc.world.getBlockState(check).getBlock() instanceof ChestBlock) {
                return check;
            }
        }
        return null;
    }

    private void tickRedeemDarena(long now) {
        if (!canAct(now) || mc.player.networkHandler == null) {
            return;
        }
        mc.player.networkHandler.sendChatCommand("darena");
        state = State.WAIT_ESCAPE;
        stateUntil = now + 1000L;
        escapeDeadline = now + ESCAPE_MAX_WAIT_MS;
        sawBossBar = false;
        lastActionAt = now;
        log("&dПишу &f/darena&d, выбираю награду");
    }

    private void tickWaitEscape(long now) {
        if (mc.currentScreen instanceof HandledScreen<?> screen && clickFirstItem(screen, Items.PUFFERFISH)) {
            mc.player.closeHandledScreen();
            lastActionAt = now;
            log("&aЗабрал награду на д арене");
            return;
        }

        if (now < stateUntil) {
            return;
        }

        BossBar bar = findTrackedBossBar();
        if (bar != null) {
            if (!sawBossBar) {
                sawBossBar = true;
                log("&eЖду таймер побега (&f" + Math.max(0, Math.round(bar.getPercent() * 30.0F)) + "s&e)");
            }

            if (hasLootedThisCycle && avoidPlayers.isEnabled()) {
                fleeFromNearestPlayer(now);
            }
            return;
        }

        if (sawBossBar || now >= escapeDeadline) {
            baritone.stop();
            state = State.LEAVE_ARENA;
            stateUntil = now + 500L;
            log("&aТаймер вышел, ухожу с д арены");
        }
    }

    private void fleeFromNearestPlayer(long now) {
        BlockPos nearestPlayer = null;
        double nearestDist = Double.MAX_VALUE;
        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player == mc.player) {
                continue;
            }
            double dist = mc.player.squaredDistanceTo(player);
            if (dist < nearestDist) {
                nearestDist = dist;
                nearestPlayer = player.getBlockPos();
            }
        }
        if (nearestPlayer != null && nearestDist <= fleeDistance.getCurrent() * fleeDistance.getCurrent()
                && now - lastPathAt >= PATH_REPATH_MS) {
            baritone.fleeFrom(nearestPlayer);
            lastPathAt = now;
        }
    }

    private BossBar findTrackedBossBar() {
        String keyword = bossBarKeyword.getValue();
        if (keyword != null && !keyword.isBlank()) {
            return BossBarUtil.findByNameContains(keyword.toLowerCase(Locale.ROOT));
        }
        var bars = BossBarUtil.getBossBars();
        return bars.isEmpty() ? null : bars.iterator().next();
    }

    private void tickLeaveArena(long now) {
        if (now < stateUntil || !canAct(now) || mc.player.networkHandler == null) {
            return;
        }
        mc.player.networkHandler.sendChatCommand("darena");
        state = State.TP_BASE;
        stateUntil = now + 1000L;
        lastActionAt = now;
        log("&dПишу &f/darena&d для выхода");
    }

    private void tickTpBase(long now) {
        if (now < stateUntil || !canAct(now) || mc.player.networkHandler == null) {
            return;
        }
        String cmd = baseHomeCmd.getValue();
        if (cmd == null || cmd.isBlank()) {
            return;
        }
        preTpPos = mc.player.getPos();
        mc.player.networkHandler.sendChatCommand(cmd);
        sawTpBossBar = false;
        tpDeadline = now + TP_BOSSBAR_MAX_WAIT_MS;
        state = State.VERIFY_TP_BASE;
        stateUntil = now + TP_MIN_WAIT_MS;
        lastActionAt = now;
        log("&aТелепорт на базу (&f" + cmd + "&a)");
    }

    private void tickVerifyTpBase(long now) {
        if (waitingOnTpBossBar(now)) {
            return;
        }
        if (preTpPos != null && mc.player.getPos().squaredDistanceTo(preTpPos) < TP_MOVE_THRESHOLD_SQ) {
            log("&cКоманда телепорта на базу (&f" + baseHomeCmd.getValue() + "&c) не сработала, отключаю модуль");
            setToggled(false);
            return;
        }
        unreachableChests.clear();
        clearOpenTarget();
        state = State.DEPOSIT;
        stateUntil = now + 3000L;
    }

    private void tickDeposit(long now) {
        if (now < stateUntil) {
            return;
        }

        if (mc.currentScreen instanceof HandledScreen<?> screen) {
            baritone.stop();
            clearOpenTarget();
            if (!canLootAct(now)) {
                return;
            }
            if (quickMovePlayerItem(screen)) {
                markLootAction(now);
                return;
            }
            mc.player.closeHandledScreen();
            hasLootedThisCycle = false;
            targetChest = null;
            state = State.DRINK_POTION;
            lastActionAt = now;
            log("&aРесурсы сложены, новый цикл");
            return;
        }

        if (!canAct(now)) {
            return;
        }

        BlockPos deposit = nearestFrom(lootChests);
        if (deposit == null) {
            BlockPos fallback = findNearestContainer();
            deposit = fallback != null && !potionChests.contains(fallback)
                    && !unreachableChests.contains(fallback) ? fallback : null;
        }
        if (deposit == null) {
            log("&cНе нашёл сундук для лута, иду пить зелье");
            state = State.DRINK_POTION;
            return;
        }
        approachAndOpen(deposit, now);
    }

    private void pathTo(BlockPos pos, long now) {
        if (now - lastPathAt < PATH_REPATH_MS) {
            return;
        }
        baritone.goNear(pos, 2);
        lastPathAt = now;
    }

    private boolean tickTakeOnePotion(HandledScreen<?> screen) {
        ItemStack cursor = screen.getScreenHandler().getCursorStack();
        if (potionSplitStep == 0) {
            Slot slot = findContainerPotionSlot(screen);
            if (slot == null) {
                return false;
            }
            potionSplitContainerSlotId = slot.id;
            mc.interactionManager.clickSlot(screen.getScreenHandler().syncId, slot.id, 0, SlotActionType.PICKUP, mc.player);
            potionSplitStep = 1;
            return true;
        }
        if (potionSplitStep == 1) {
            if (cursor.isEmpty()) {
                potionSplitStep = 0;
                return false;
            }
            int emptySlot = findEmptyPlayerSlotId(screen);
            if (emptySlot == -1) {
                mc.interactionManager.clickSlot(screen.getScreenHandler().syncId, potionSplitContainerSlotId, 0, SlotActionType.PICKUP, mc.player);
                potionSplitStep = 0;
                return false;
            }
            mc.interactionManager.clickSlot(screen.getScreenHandler().syncId, emptySlot, 1, SlotActionType.PICKUP, mc.player);
            potionSplitStep = cursor.getCount() > 1 ? 2 : 0;
            return true;
        }

        if (!cursor.isEmpty()) {
            mc.interactionManager.clickSlot(screen.getScreenHandler().syncId, potionSplitContainerSlotId, 0, SlotActionType.PICKUP, mc.player);
        }
        potionSplitStep = 0;
        return true;
    }

    private Slot findContainerPotionSlot(HandledScreen<?> screen) {
        for (Slot slot : screen.getScreenHandler().slots) {
            if (slot.inventory == mc.player.getInventory()) {
                continue;
            }
            if (!slot.getStack().isEmpty() && isInvisibilityPotion(slot.getStack())) {
                return slot;
            }
        }
        return null;
    }

    private int findEmptyPlayerSlotId(HandledScreen<?> screen) {
        for (Slot slot : screen.getScreenHandler().slots) {
            if (slot.inventory == mc.player.getInventory() && slot.getStack().isEmpty()) {
                return slot.id;
            }
        }
        return -1;
    }

    private boolean quickMoveContainerItem(HandledScreen<?> screen, StackPredicate predicate, boolean closeWhenEmpty) {
        for (Slot slot : screen.getScreenHandler().slots) {
            if (slot.inventory == mc.player.getInventory()) {
                continue;
            }
            ItemStack stack = slot.getStack();
            if (stack.isEmpty() || !predicate.test(stack)) {
                continue;
            }
            mc.interactionManager.clickSlot(screen.getScreenHandler().syncId, slot.id, 0, SlotActionType.QUICK_MOVE, mc.player);
            return true;
        }
        if (closeWhenEmpty) {
            mc.player.closeHandledScreen();
        }
        return false;
    }

    private boolean quickMovePlayerItem(HandledScreen<?> screen) {
        for (Slot slot : screen.getScreenHandler().slots) {
            if (slot.inventory != mc.player.getInventory()) {
                continue;
            }
            ItemStack stack = slot.getStack();
            if (stack.isEmpty() || isInvisibilityPotion(stack)) {
                continue;
            }
            mc.interactionManager.clickSlot(screen.getScreenHandler().syncId, slot.id, 0, SlotActionType.QUICK_MOVE, mc.player);
            return true;
        }
        return false;
    }

    private boolean clickFirstItem(HandledScreen<?> screen, Item item) {
        for (Slot slot : screen.getScreenHandler().slots) {
            if (!slot.getStack().isOf(item)) {
                continue;
            }
            mc.interactionManager.clickSlot(screen.getScreenHandler().syncId, slot.id, 0, SlotActionType.PICKUP, mc.player);
            return true;
        }
        return false;
    }

    private BlockPos findMarkedInvisChest() {
        List<BlockPos> candidates = new ArrayList<>();
        for (WorldChunk chunk : loadedChunks()) {
            for (BlockEntity blockEntity : chunk.getBlockEntities().values()) {
                if (!(blockEntity instanceof ChestBlockEntity)) {
                    continue;
                }
                BlockPos pos = blockEntity.getPos();
                BlockEntity signEntity = chunk.getBlockEntity(pos.up());
                if (!(signEntity instanceof SignBlockEntity sign)) {
                    continue;
                }
                String line = sign.getFrontText().getMessage(0, false).getString().toLowerCase(Locale.ROOT);
                if (line.contains("inv") || line.contains("invis") || line.contains("невид") || line.contains("инвиз")) {
                    candidates.add(pos.toImmutable());
                }
            }
        }
        return nearest(candidates);
    }

    private BlockPos findNearestContainer() {
        List<BlockPos> containers = new ArrayList<>();
        for (WorldChunk chunk : loadedChunks()) {
            for (BlockEntity blockEntity : chunk.getBlockEntities().values()) {
                if (blockEntity instanceof ChestBlockEntity || blockEntity instanceof BarrelBlockEntity) {
                    containers.add(blockEntity.getPos().toImmutable());
                }
            }
        }
        return nearest(containers);
    }

    private List<WorldChunk> loadedChunks() {
        List<WorldChunk> chunks = new ArrayList<>();
        int playerCX = mc.player.getChunkPos().x;
        int playerCZ = mc.player.getChunkPos().z;
        int dist = Math.min(4, mc.options.getClampedViewDistance());

        for (int cx = playerCX - dist; cx <= playerCX + dist; cx++) {
            for (int cz = playerCZ - dist; cz <= playerCZ + dist; cz++) {
                WorldChunk chunk = mc.world.getChunkManager().getChunk(cx, cz, ChunkStatus.FULL, false);
                if (chunk != null) {
                    chunks.add(chunk);
                }
            }
        }
        return chunks;
    }

    private BlockPos nearest(List<BlockPos> positions) {
        if (positions.isEmpty()) {
            return null;
        }
        return positions.stream()
                .min(Comparator.comparingDouble(pos -> mc.player.squaredDistanceTo(Vec3d.ofCenter(pos))))
                .orElse(null);
    }

    private void openContainer(BlockPos pos) {
        Vec3d hit = Vec3d.ofCenter(pos);
        mc.interactionManager.interactBlock(
                mc.player,
                Hand.MAIN_HAND,
                new BlockHitResult(hit, Direction.UP, pos, false)
        );
    }

    private boolean isInventoryFull() {
        for (int slot = 9; slot < 36; slot++) {
            if (mc.player.getInventory().getStack(slot).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private boolean hasInvisPotionInInventory() {
        for (int slot = 0; slot < 36; slot++) {
            if (isInvisibilityPotion(mc.player.getInventory().getStack(slot))) {
                return true;
            }
        }
        return false;
    }

    private int findPotionHotbarSlot() {
        for (int slot = 0; slot < 9; slot++) {
            if (isInvisibilityPotion(mc.player.getInventory().getStack(slot))) {
                return slot;
            }
        }
        return -1;
    }

    private int findPotionScreenSlot() {
        for (int slot = 9; slot <= 35; slot++) {
            Slot screenSlot = mc.player.playerScreenHandler.getSlot(slot);
            if (isInvisibilityPotion(screenSlot.getStack())) {
                return screenSlot.id;
            }
        }
        return -1;
    }

    private void selectHotbarSlot(int slot) {
        if (slot < 0 || slot > 8 || mc.player.getInventory().selectedSlot == slot) {
            return;
        }
        mc.player.getInventory().selectedSlot = slot;
        mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(slot));
    }

    private void selectEmptyHotbarSlot() {
        if (mc.player.getInventory().getStack(mc.player.getInventory().selectedSlot).isEmpty()) {
            return;
        }
        for (int slot = 0; slot < 9; slot++) {
            if (mc.player.getInventory().getStack(slot).isEmpty()) {
                selectHotbarSlot(slot);
                return;
            }
        }
    }

    private boolean isInvisibilityPotion(ItemStack stack) {
        if (!stack.isOf(Items.POTION) && !stack.isOf(Items.SPLASH_POTION) && !stack.isOf(Items.LINGERING_POTION)) {
            return false;
        }
        String name = stack.getName().getString().toLowerCase(Locale.ROOT);
        return name.contains("invis") || name.contains("невид") || name.contains("инвиз");
    }

    private boolean canAct(long now) {
        return now - lastActionAt >= ACTION_DELAY_MS;
    }

    private boolean canLootAct(long now) {
        return now - lastLootActionAt >= nextLootActionDelay;
    }

    private void markLootAction(long now) {
        lastLootActionAt = now;
        nextLootActionDelay = LOOT_ACTION_MIN_MS + ThreadLocalRandom.current().nextLong(LOOT_ACTION_JITTER_MS);
    }

    private void log(String message) {
        if (!chatLogs.isEnabled() || mc == null) {
            return;
        }
        MutableText line = Text.literal("AutoWarden").setStyle(Style.EMPTY.withColor(Formatting.GOLD).withBold(true))
                .append(Text.literal(" | ").setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY).withBold(false)))
                .append(Text.literal(message.replace('&', '§')).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withBold(false)));
        mc.execute(() -> {
            if (mc.inGameHud != null && mc.inGameHud.getChatHud() != null) {
                mc.inGameHud.getChatHud().addMessage(line);
            } else if (mc.player != null) {
                mc.player.sendMessage(line, false);
            }
        });
    }

    private int parseTimerSeconds(String text) {
        String clean = text.replaceAll("§[0-9a-fk-or]", "");
        Matcher m = TIMER_PATTERN.matcher(clean);
        if (m.find()) {
            return Integer.parseInt(m.group(1)) * 60 + Integer.parseInt(m.group(2));
        }
        return -1;
    }

    @FunctionalInterface
    private interface StackPredicate {
        boolean test(ItemStack stack);
    }
}
