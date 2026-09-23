package wtf.wyvern.client.modules.impl.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.BooleanSetting;
import wtf.wyvern.client.modules.api.setting.impl.SliderSetting;
import wtf.wyvern.core.eventbus.EventTarget;
import wtf.wyvern.core.events.impl.player.EventUpdate;
import wtf.wyvern.core.events.impl.render.EventHudRender;
import wtf.wyvern.core.events.impl.render.EventRender2D;
import wtf.wyvern.core.events.impl.render.EventRender3D;
import wtf.wyvern.core.font.Fonts;
import wtf.wyvern.render.display.base.BorderRadius;
import wtf.wyvern.render.display.base.color.ColorRGBA;
import wtf.wyvern.render.level.Render3DUtil;
import wtf.wyvern.utility.math.ProjectionUtil;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ModuleAnnotation(
        name = "WardenBoost",
        category = Category.RENDER,
        description = "ESP сундуков, таймеры голограмм и трекер вардена"
)
public final class WardenBoost extends Module {
    public static final WardenBoost INSTANCE = new WardenBoost();

    private static final Pattern TIMER_PATTERN = Pattern.compile("(\\d{1,2}):(\\d{2})");
    private static final Identifier ARROW_ICON = Identifier.of("wyvern", "icons/arrow.png");

    private static final ColorRGBA READY_COLOR = new ColorRGBA(88, 224, 122, 255);
    private static final ColorRGBA SOON_COLOR = new ColorRGBA(247, 206, 82, 255);
    private static final ColorRGBA MID_COLOR = new ColorRGBA(255, 150, 64, 255);
    private static final ColorRGBA FAR_COLOR = new ColorRGBA(255, 90, 90, 255);

    private final SliderSetting scanRange = new SliderSetting("Радиус сканирования", 48.0F, 16.0F, 96.0F, 1.0F);
    private final BooleanSetting chestEsp = new BooleanSetting("ESP сундуков", true);
    private final BooleanSetting timers = new BooleanSetting("Таймеры", true);
    private final BooleanSetting arrows = new BooleanSetting("Стрелки", true);
    private final BooleanSetting wardenTracker = new BooleanSetting("Трекер вардена", true);
    private final SliderSetting arrowRadius = new SliderSetting("Радиус стрелок", 55.0F, 20.0F, 200.0F, 5.0F);
    private final SliderSetting arrowSize = new SliderSetting("Размер стрелок", 12.0F, 5.0F, 25.0F, 1.0F);

    private final Map<BlockPos, ChestEntry> chests = new ConcurrentHashMap<>();
    private volatile boolean wardenVisible;
    private volatile double wardenDistance = -1.0;

    private int tickCounter;
    private double lastX = Double.NaN;
    private double lastZ = Double.NaN;

    private WardenBoost() {
    }

    public Map<BlockPos, ChestEntry> getChests() {
        return chests;
    }

    public boolean isWardenVisible() {
        return wardenVisible;
    }

    public double getWardenDistance() {
        return wardenDistance;
    }

    @Override
    public void onDisable() {
        chests.clear();
        wardenVisible = false;
        wardenDistance = -1.0;
        tickCounter = 0;
        lastX = Double.NaN;
        lastZ = Double.NaN;
        super.onDisable();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mc.world == null || mc.player == null) return;

        double range = scanRange.getCurrent();
        double speed = 0.0;
        if (!Double.isNaN(lastX)) {
            double dx = mc.player.getX() - lastX;
            double dz = mc.player.getZ() - lastZ;
            speed = Math.sqrt(dx * dx + dz * dz);
        }
        lastX = mc.player.getX();
        lastZ = mc.player.getZ();

        // Полный обход мира дорогой, поэтому при быстром перемещении он делается чаще,
        // а при стоянии на месте - реже.
        int interval = speed > 0.8 ? 2 : 4;
        tickCounter++;
        boolean doHeavy = tickCounter % interval == 0;

        if (doHeavy) {
            scanHeavy(range, speed);
        } else if (wardenTracker.isEnabled()) {
            updateWarden();
        } else {
            wardenVisible = false;
            wardenDistance = -1.0;
        }

        long now = System.currentTimeMillis();
        double rangeSq = range * range;
        chests.entrySet().removeIf(entry -> {
            BlockPos pos = entry.getKey();
            if (!(mc.world.getBlockState(pos).getBlock() instanceof ChestBlock)) return true;
            double dist = mc.player.squaredDistanceTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
            boolean stale = now - entry.getValue().lastUpdate > 10_000L;
            return dist > rangeSq && stale;
        });
    }

    private void scanHeavy(double range, double speed) {
        WardenEntity nearest = null;
        double nearestDist = Double.MAX_VALUE;
        double rangeSq = range * range;

        for (Entity entity : mc.world.getEntities()) {
            if (entity instanceof ArmorStandEntity as) {
                if (!as.isCustomNameVisible()) continue;
                Text name = as.getCustomName();
                if (name == null) continue;
                String nameStr = name.getString();
                if (nameStr.isEmpty()) continue;
                if (mc.player.squaredDistanceTo(as) > rangeSq) continue;

                BlockPos asPos = as.getBlockPos();
                for (int dy = 0; dy <= 3; dy++) {
                    BlockPos checkPos = asPos.down(dy);
                    BlockState state = mc.world.getBlockState(checkPos);
                    if (state.getBlock() instanceof ChestBlock) {
                        int timer = parseTimerSeconds(nameStr);
                        addOrUpdate(checkPos, Math.max(timer, 0), nameStr);
                        break;
                    }
                }
            } else if (wardenTracker.isEnabled() && entity instanceof WardenEntity warden && warden.isAlive()) {
                double dist = mc.player.distanceTo(warden);
                if (dist < nearestDist) {
                    nearest = warden;
                    nearestDist = dist;
                }
            }
        }

        if (wardenTracker.isEnabled()) {
            wardenVisible = nearest != null;
            wardenDistance = nearest != null ? nearestDist : -1.0;
        } else {
            wardenVisible = false;
            wardenDistance = -1.0;
        }

        // Сундуки без голограмм рядом с игроком - чтобы ESP не мигал, пока таймер не прогрузился.
        int r = speed > 0.8 ? 4 : 8;
        int ry = speed > 0.8 ? 2 : 4;
        BlockPos playerPos = mc.player.getBlockPos();
        for (int x = -r; x <= r; x++) {
            for (int y = -ry; y <= ry; y++) {
                for (int z = -r; z <= r; z++) {
                    BlockPos pos = playerPos.add(x, y, z);
                    if (!(mc.world.getBlockState(pos).getBlock() instanceof ChestBlock)) continue;
                    ChestEntry existing = chests.get(pos);
                    if (existing != null) {
                        existing.lastUpdate = System.currentTimeMillis();
                        continue;
                    }
                    addOrUpdate(pos, 0, null);
                }
            }
        }
    }

    private void updateWarden() {
        WardenEntity nearest = null;
        double nearestDist = Double.MAX_VALUE;
        for (Entity entity : mc.world.getEntities()) {
            if (entity instanceof WardenEntity warden && warden.isAlive()) {
                double dist = mc.player.distanceTo(warden);
                if (dist < nearestDist) {
                    nearest = warden;
                    nearestDist = dist;
                }
            }
        }
        wardenVisible = nearest != null;
        wardenDistance = nearest != null ? nearestDist : -1.0;
    }

    private void addOrUpdate(BlockPos pos, int timerSeconds, String hologramText) {
        ChestEntry existing = chests.get(pos);
        if (existing != null) {
            existing.timerSeconds = timerSeconds;
            existing.hologramText = hologramText;
            existing.lastUpdate = System.currentTimeMillis();
        } else {
            chests.put(pos.toImmutable(), new ChestEntry(pos.toImmutable(), timerSeconds, hologramText));
        }
    }

    @EventTarget
    public void onRender3D(EventRender3D event) {
        if (!chestEsp.isEnabled() || mc.world == null || mc.player == null) return;

        for (ChestEntry chest : chests.values()) {
            Render3DUtil.drawBox(new Box(chest.pos), chest.getColor().getRGB(), 1.3F, true, true, false);
        }
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        if (!timers.isEnabled() || mc.world == null || mc.player == null || mc.options.hudHidden) return;

        for (ChestEntry chest : chests.values()) {
            Vec3d worldPos = new Vec3d(chest.pos.getX() + 0.5, chest.pos.getY() + 1.3, chest.pos.getZ() + 0.5);
            if (!ProjectionUtil.canSee(worldPos)) continue;

            Vec3d screen = ProjectionUtil.worldSpaceToScreenSpace(worldPos);
            if (screen.z < 0.0 || screen.z > 1.0) continue;

            String timerStr = chest.getTimerString();
            float size = 7.0F;
            float textWidth = Fonts.MEDIUM.getWidth(timerStr, size);
            float textX = (float) screen.x - textWidth * 0.5F;
            float textY = (float) screen.y;
            float pad = 2.5F;

            event.getContext().drawRoundedRect(textX - pad, textY - pad, textWidth + pad * 2.0F,
                    size + pad * 2.0F, BorderRadius.all(3.0F), new ColorRGBA(0, 0, 0, 140));
            event.getContext().drawText(Fonts.MEDIUM.getFont(size), timerStr, textX, textY, chest.getColor());
        }
    }

    @EventTarget
    public void onHudRender(EventHudRender event) {
        if (!arrows.isEnabled() || mc.world == null || mc.player == null || mc.options.hudHidden) return;
        if (!mc.options.getPerspective().equals(Perspective.FIRST_PERSON)) return;
        if (chests.isEmpty()) return;

        MatrixStack matrices = event.getContext().getMatrices();
        float centerX = mc.getWindow().getScaledWidth() / 2.0F;
        float centerY = mc.getWindow().getScaledHeight() / 2.0F;
        float radius = arrowRadius.getCurrent();
        float size = arrowSize.getCurrent();

        BufferBuilder buffer = null;
        for (ChestEntry chest : chests.values()) {
            double diffX = chest.pos.getX() + 0.5 - mc.player.getX();
            double diffZ = chest.pos.getZ() + 0.5 - mc.player.getZ();
            float angle = (float) (Math.toDegrees(Math.atan2(diffZ, diffX)) - mc.player.getYaw() - 90.0);

            matrices.push();
            matrices.translate(centerX, centerY, 0.0F);
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(angle));
            matrices.translate(0.0F, -radius, 0.0F);

            if (buffer == null) {
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                RenderSystem.disableCull();
                RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
                RenderSystem.setShaderTexture(0, ARROW_ICON);
                buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
            }
            appendArrow(matrices, buffer, chest.getColor(), size);

            matrices.pop();
        }

        if (buffer != null) {
            BufferRenderer.drawWithGlobalProgram(buffer.end());
            RenderSystem.disableBlend();
            RenderSystem.enableCull();
        }
    }

    private void appendArrow(MatrixStack matrices, BufferBuilder buffer, ColorRGBA color, float s) {
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        int argb = color.getRGB();

        buffer.vertex(matrix, -s / 2.0F, -s, 0.0F).texture(0.0F, 0.0F).color(argb);
        buffer.vertex(matrix, s / 2.0F, -s, 0.0F).texture(1.0F, 0.0F).color(argb);
        buffer.vertex(matrix, s / 2.0F, 0.0F, 0.0F).texture(1.0F, 1.0F).color(argb);
        buffer.vertex(matrix, -s / 2.0F, 0.0F, 0.0F).texture(0.0F, 1.0F).color(argb);
    }

    private int parseTimerSeconds(String text) {
        String clean = text.replaceAll("§[0-9a-fk-or]", "");
        Matcher matcher = TIMER_PATTERN.matcher(clean);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1)) * 60 + Integer.parseInt(matcher.group(2));
        }
        return -1;
    }

    public static final class ChestEntry {
        public final BlockPos pos;
        public volatile int timerSeconds;
        public volatile String hologramText;
        public volatile long lastUpdate;

        public ChestEntry(BlockPos pos, int timerSeconds, String hologramText) {
            this.pos = pos;
            this.timerSeconds = timerSeconds;
            this.hologramText = hologramText;
            this.lastUpdate = System.currentTimeMillis();
        }

        public int getEffectiveTimer() {
            long elapsed = (System.currentTimeMillis() - lastUpdate) / 1000L;
            return (int) Math.max(0L, timerSeconds - elapsed);
        }

        public ColorRGBA getColor() {
            int seconds = getEffectiveTimer();
            if (seconds <= 0) return READY_COLOR;
            if (seconds <= 30) return SOON_COLOR;
            if (seconds <= 60) return MID_COLOR;
            return FAR_COLOR;
        }

        public String getTimerString() {
            int seconds = getEffectiveTimer();
            if (seconds <= 0) return "Готов";
            return String.format(Locale.US, "%d:%02d", seconds / 60, seconds % 60);
        }

        public double distanceTo(double px, double py, double pz) {
            double dx = pos.getX() + 0.5 - px;
            double dy = pos.getY() + 0.5 - py;
            double dz = pos.getZ() + 0.5 - pz;
            return Math.sqrt(dx * dx + dy * dy + dz * dz);
        }
    }
}
