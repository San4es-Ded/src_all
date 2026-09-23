package wtf.wyvern.client.modules.impl.combat.rotation;

import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.math.MathHelper;
import wtf.wyvern.utility.component.RotationComponent;
import wtf.wyvern.utility.game.player.rotation.Rotation;
import wtf.astroguard.J2C.FastNative;

/**
 * ReallyWorld rotation: fast target acquisition with a small smooth tremor.
 * All output remains aligned to the current mouse-sensitivity GCD.
 */
@FastNative
public final class ReallyWorldRotation extends RotationBase {

    @Override
    public void update(Rotation targetAngle, boolean elytraVisual) {
        if (mc.player == null || targetAngle == null) {
            return;
        }

        double tick = mc.player.age;
        float yawShake = (float) (Math.sin(tick * 0.82D) * 0.20D
                + Math.sin(tick * 0.31D + 1.7D) * 0.04D);
        float pitchShake = (float) (Math.cos(tick * 0.67D + 0.4D) * 0.13D);

        float targetYaw = targetAngle.getYaw() + yawShake;
        float targetPitch = MathHelper.clamp(targetAngle.getPitch() + pitchShake, -90.0F, 90.0F);
        float gcd = Rotation.gcd();
        if (gcd > 0.0F) {
            targetYaw -= (targetYaw - this.lastYaw) % gcd;
            targetPitch -= (targetPitch - this.lastPitch) % gcd;
        }

        float speed = elytraVisual ? 1440.0F : 360.0F;
        int priority = elytraVisual ? 3 : 1;
        RotationComponent.update(new Rotation(targetYaw, targetPitch),
                speed, speed, speed, speed, 0, priority, false);

        this.lastYaw = targetYaw;
        this.lastPitch = targetPitch;
    }

    public void reset() {
        if (mc.player != null) {
            this.lastYaw = mc.player.getYaw();
            this.lastPitch = mc.player.getPitch();
        } else {
            this.lastYaw = 0.0F;
            this.lastPitch = 0.0F;
        }
    }

    private int getPing() {
        if (mc.getNetworkHandler() != null && mc.player != null) {
            PlayerListEntry entry = mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid());
            if (entry != null) return entry.getLatency();
        }
        return 50;
    }
}
