package wtf.wyvern.client.modules.impl.movement;

import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.SliderSetting;
import wtf.wyvern.core.eventbus.EventTarget;
import wtf.wyvern.core.events.impl.other.EventGameUpdate;
import wtf.wyvern.core.events.impl.other.EventTick;
import wtf.wyvern.core.events.impl.player.EventLook;
import wtf.wyvern.utility.component.RotationComponent;
import wtf.wyvern.utility.game.player.PlayerInventoryUtil;
import wtf.wyvern.utility.game.player.PlayerIntersectionUtil;
import wtf.wyvern.utility.game.player.rotation.Rotation;
import wtf.wyvern.utility.interfaces.IMinecraft;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(
        name = "AntiElytraTarget",
        category = Category.MOVEMENT,
        description = "Сразу летит в самую быструю точку на элитрах"
)
@FastNative
public final class AntiElytraTarget extends Module implements IMinecraft {
    public static final AntiElytraTarget INSTANCE = new AntiElytraTarget();

    private final SliderSetting fireworkDelay = new SliderSetting(
            "Тайминг фейерверка",
            8.0F,
            2.0F,
            40.0F,
            1.0F
    );

    private int fireworkTimer;
    private float lastYaw;
    private float lastPitch;
    private float escapeYaw;
    private boolean controlling;

    private AntiElytraTarget() {
    }

    @EventTarget
    private void onLook(EventLook event) {
        if (controlling) {
            event.cancel();
        }
    }

    @EventTarget
    private void onGameUpdate(EventGameUpdate event) {
        if (!isEnabled() || mc.player == null || !mc.player.isGliding()) {
            releaseControl();
            return;
        }

        controlling = true;
        Rotation rotation = findFastestRotation();
        lastYaw = rotation.getYaw();
        lastPitch = rotation.getPitch();
        RotationComponent.update(
                rotation,
                360.0F,
                360.0F,
                360.0F,
                360.0F,
                0,
                4,
                false
        );
    }

    @EventTarget
    private void onTick(EventTick event) {
        if (mc.player == null || !isEnabled() || !mc.player.isGliding()) {
            fireworkTimer = 0;
            return;
        }

        if (fireworkTimer > 0) {
            fireworkTimer--;
            return;
        }

        if (useFirework()) {
            fireworkTimer = Math.max(1, Math.round(fireworkDelay.getCurrent()));
        }
    }

    private Rotation findFastestRotation() {
        double bestScore = Double.NEGATIVE_INFINITY;
        float bestYaw = escapeYaw;
        float bestPitch = -28.0F;

        for (float pitch = -18.0F; pitch >= -45.0F; pitch -= 3.0F) {
            for (float yawOffset = -35.0F; yawOffset <= 35.0F; yawOffset += 7.0F) {
                float yaw = MathHelper.wrapDegrees(escapeYaw + yawOffset);
                double score = scoreFlightSpeed(pitch);
                if (score > bestScore) {
                    bestScore = score;
                    bestYaw = yaw;
                    bestPitch = pitch;
                }
            }
        }

        return applyGcd(new Rotation(bestYaw, MathHelper.clamp(bestPitch, -90.0F, 90.0F)));
    }

    private double scoreFlightSpeed(float pitch) {
        return pitch < 0.0F ? -pitch : 0.0D;
    }

    private void updateEscapeYaw() {
        if (mc.player == null) {
            return;
        }

        Vec3d velocity = mc.player.getVelocity();
        Vec3d horizontal = new Vec3d(velocity.x, 0.0D, velocity.z);
        if (horizontal.lengthSquared() > 1.0E-4) {
            escapeYaw = (float) Math.toDegrees(Math.atan2(-horizontal.x, horizontal.z));
            return;
        }

        escapeYaw = mc.player.getYaw();
    }

    private Rotation applyGcd(Rotation rotation) {
        float gcd = Rotation.gcd();
        if (gcd <= 0.0F) {
            return rotation;
        }

        float yaw = rotation.getYaw();
        float pitch = rotation.getPitch();
        yaw -= (yaw - lastYaw) % gcd;
        pitch -= (pitch - lastPitch) % gcd;
        return new Rotation(yaw, MathHelper.clamp(pitch, -90.0F, 90.0F));
    }

    private boolean useFirework() {
        if (mc.player == null || mc.getNetworkHandler() == null) {
            return false;
        }

        if (mc.player.getOffHandStack().isOf(Items.FIREWORK_ROCKET)) {
            sendFireworkInteract(Hand.OFF_HAND);
            return true;
        }

        if (mc.player.getMainHandStack().isOf(Items.FIREWORK_ROCKET)) {
            sendFireworkInteract(Hand.MAIN_HAND);
            return true;
        }

        if (mc.player.isUsingItem()) {
            return false;
        }

        PlayerInventoryUtil.swapAndUseHvH(Items.FIREWORK_ROCKET);
        return true;
    }

    private void sendFireworkInteract(Hand hand) {
        PlayerIntersectionUtil.sendSequencedPacket(sequence ->
                new PlayerInteractItemC2SPacket(hand, sequence, mc.player.getYaw(), mc.player.getPitch()));
        mc.interactionManager.interactItem(mc.player, hand);
    }

    @Override
    public void onEnable() {
        fireworkTimer = 0;
        updateEscapeYaw();
        if (mc.player != null) {
            lastYaw = mc.player.getYaw();
            lastPitch = mc.player.getPitch();
        }
        super.onEnable();
    }

    @Override
    public void onDisable() {
        fireworkTimer = 0;
        releaseControl();
        super.onDisable();
    }

    private void releaseControl() {
        if (controlling) {
            RotationComponent.instance.stopRotation();
            controlling = false;
        }
    }
}
