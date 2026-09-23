package wtf.wyvern.client.modules.impl.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.ModeSetting;
import wtf.wyvern.client.modules.api.setting.impl.SliderSetting;
import wtf.wyvern.core.eventbus.EventTarget;
import wtf.wyvern.core.events.impl.player.EventAttack;
import wtf.wyvern.core.events.impl.render.EventRender3D;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(
        name = "HitEffect",
        category = Category.RENDER,
        description = "Поднимает полупрозрачную копию скина игрока при ударе"
)
public final class HitEffect extends Module {
    public static final HitEffect INSTANCE = new HitEffect();

    /**
     * The model renderer is shared by every player, so the animation is exposed
     * through a render-thread-local context and applied only while a copy is drawn.
     */
    private static final ThreadLocal<Float> ACTIVE_ARM_ANIMATION = new ThreadLocal<>();

    private static final int FULL_BRIGHT = 0x00F000F0;
    private static final int MAX_COPIES = 24;
    private static final String MODE_UP = "Вверх";
    private static final String MODE_BACK = "Назад";
    private static final float BACK_DISTANCE = 1.25F;

    private final ModeSetting mode = new ModeSetting("Режим", MODE_UP, MODE_BACK);
    private final ModeSetting look = new ModeSetting("Вид", "Чамс", "Скин");
    private final SliderSetting speed = new SliderSetting(
            "Скорость",
            1.5F,
            0.1F,
            5.0F,
            0.1F,
            "Скорость подъёма в блоках в секунду"
    );
    private final SliderSetting height = new SliderSetting(
            "Высота",
            2.0F,
            0.25F,
            6.0F,
            0.25F,
            "Максимальная высота подъёма копии"
    );
    private final List<SkinCopy> copies = new ArrayList<>();

    @FastNative
    @EventTarget
    public void onAttack(EventAttack event) {
        if (mc.world == null || mc.player == null
                || !(event.getTarget() instanceof PlayerEntity target)
                || target == mc.player) {
            return;
        }

        if (copies.size() >= MAX_COPIES) {
            copies.removeFirst();
        }
        copies.add(new SkinCopy(target, mc.world, mode.is(MODE_BACK)));
    }

    @EventTarget
    public void onRender(EventRender3D event) {
        if (copies.isEmpty()) {
            return;
        }
        if (mc.world == null) {
            copies.clear();
            return;
        }

        long now = System.nanoTime();
        float riseSpeed = Math.max(0.01F, speed.getCurrent());
        float maxHeight = Math.max(0.01F, height.getCurrent());
        Vec3d camera = mc.gameRenderer.getCamera().getPos();
        EntityRenderDispatcher dispatcher = mc.getEntityRenderDispatcher();
        VertexConsumerProvider.Immediate buffers = mc.getBufferBuilders().getEntityVertexConsumers();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);

        try {
            Iterator<SkinCopy> iterator = copies.iterator();
            while (iterator.hasNext()) {
                SkinCopy copy = iterator.next();
                if (copy.world != mc.world) {
                    iterator.remove();
                    continue;
                }

                float elapsedSeconds = (now - copy.createdAtNanos) / 1_000_000_000.0F;
                float yOffset = elapsedSeconds * riseSpeed;
                if (yOffset >= maxHeight) {
                    iterator.remove();
                    continue;
                }

                float progress = MathHelper.clamp(yOffset / maxHeight, 0.0F, 1.0F);
                float backOffset = copy.movesBack
                        ? BACK_DISTANCE * (1.0F - (1.0F - progress) * (1.0F - progress))
                        : 0.0F;

                // Устанавливаем начальную прозрачность на 0.4F и плавно снижаем её по мере движения (progress от 0 до 1)
                float alpha = 0.4F * (1.0F - progress);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);

                boolean chamsLook = look.is("Чамс");
                if (!copy.movesBack) {
                    ACTIVE_ARM_ANIMATION.set(progress);
                }
                if (chamsLook) {
                    // The model "renders" into a discarding provider purely so the
                    // posed cuboids get captured, then the copy is drawn as chams
                    // boxes in the same style as the Chams module. The fade comes
                    // from the shader color set above.
                    wtf.wyvern.render.level.ChamsRenderer.beginDirectCapture();
                }
                try {
                    dispatcher.render(
                            copy.player,
                            copy.position.x + copy.backDirection.x * backOffset - camera.x,
                            copy.position.y + yOffset - camera.y,
                            copy.position.z + copy.backDirection.z * backOffset - camera.z,
                            event.getPartialTicks(),
                            event.getMatrix(),
                            chamsLook ? layer -> wtf.wyvern.render.level.ChamsRenderer.noopConsumer() : buffers,
                            FULL_BRIGHT
                    );
                } finally {
                    if (!copy.movesBack) {
                        ACTIVE_ARM_ANIMATION.remove();
                    }
                    if (chamsLook) {
                        wtf.wyvern.render.level.ChamsRenderer.renderDirect();
                    }
                }

                if (!chamsLook) {
                    buffers.draw();
                }
            }
        } finally {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.depthMask(true);
            RenderSystem.enableDepthTest();
            RenderSystem.disableBlend();
        }
    }

    @FastNative
    @Override
    public void onDisable() {
        copies.clear();
        super.onDisable();
    }

    @FastNative
    public static float getActiveArmAnimationProgress() {
        Float progress = ACTIVE_ARM_ANIMATION.get();
        return progress == null ? -1.0F : progress;
    }

    /**
     * Adds a two-stage pose: the arms fan out first, then sweep above the head.
     * The caller restores the original model values after the draw call.
     */
    @FastNative
    public static void applyArmAnimation(PlayerEntityModel model, float progress) {
        float sideProgress = smoothStep(MathHelper.clamp(progress / 0.38F, 0.0F, 1.0F));
        float raiseProgress = smoothStep(MathHelper.clamp((progress - 0.24F) / 0.76F, 0.0F, 1.0F));
        float flourish = MathHelper.sin(progress * MathHelper.PI) * 0.08F;

        // ModelPart roll fans the hands away from the torso. The opposite signs
        // keep the movement mirrored for the left and right arms.
        model.rightArm.roll -= sideProgress * 1.15F + flourish;
        model.leftArm.roll += sideProgress * 1.15F + flourish;
        model.rightArm.yaw += sideProgress * 0.10F;
        model.leftArm.yaw -= sideProgress * 0.10F;

        // Once the arms reach the sides, smoothly rotate them into an overhead pose.
        model.rightArm.pitch -= sideProgress * 0.20F + raiseProgress * 2.85F;
        model.leftArm.pitch -= sideProgress * 0.20F + raiseProgress * 2.85F;
    }

    @FastNative
    private static float smoothStep(float value) {
        return value * value * (3.0F - 2.0F * value);
    }

    private static final class SkinCopy {
        private final ClientWorld world;
        private final OtherClientPlayerEntity player;
        private final Vec3d position;
        private final Vec3d backDirection;
        private final boolean movesBack;
        private final long createdAtNanos;

        private SkinCopy(PlayerEntity source, ClientWorld world, boolean movesBack) {
            Set<PlayerModelPart> visibleParts = EnumSet.noneOf(PlayerModelPart.class);
            for (PlayerModelPart part : PlayerModelPart.values()) {
                if (source.isPartVisible(part)) {
                    visibleParts.add(part);
                }
            }

            this.world = world;
            this.player = new OtherClientPlayerEntity(world, source.getGameProfile()) {
                @Override
                public boolean isPartVisible(PlayerModelPart modelPart) {
                    return visibleParts.contains(modelPart);
                }
            };
            this.position = source.getPos();
            float yawRadians = source.getBodyYaw() * MathHelper.RADIANS_PER_DEGREE;
            this.backDirection = new Vec3d(MathHelper.sin(yawRadians), 0.0D, -MathHelper.cos(yawRadians));
            this.movesBack = movesBack;
            this.createdAtNanos = System.nanoTime();

            player.copyPositionAndRotation(source);
            player.setPose(source.getPose());
            player.setYaw(source.getYaw());
            player.prevYaw = source.getYaw();
            player.setPitch(source.getPitch());
            player.prevPitch = source.getPitch();
            player.setBodyYaw(source.getBodyYaw());
            player.prevBodyYaw = source.getBodyYaw();
            player.setHeadYaw(source.getHeadYaw());
            player.prevHeadYaw = source.getHeadYaw();
            player.limbAnimator.reset();
            player.handSwinging = false;
            player.handSwingProgress = 0.0F;
            player.lastHandSwingProgress = 0.0F;

            for (EquipmentSlot slot : EquipmentSlot.values()) {
                player.equipStack(slot, ItemStack.EMPTY);
            }
        }
    }
}
