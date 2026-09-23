package wtf.wyvern.core.player;

import lombok.Generated;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import wtf.wyvern.client.modules.impl.misc.FreeCam;
import wtf.wyvern.client.modules.impl.movement.AirStuck;
import wtf.wyvern.utility.game.player.PlayerIntersectionUtil;
import wtf.wyvern.utility.game.player.SimulatedPlayer;
import wtf.wyvern.utility.interfaces.IClient;
import wtf.wyvern.utility.math.Timer;
import wtf.astroguard.J2C.FastNative;

@FastNative
public final class AttackUtil implements IClient {
    private static final Timer attackTimer = new Timer();
    private static int count = 0;

    public static void attackEntity(Entity entity) {
        mc.interactionManager.attackEntity(mc.player, entity);
        mc.player.swingHand(Hand.MAIN_HAND);
        attackTimer.reset();
        ++count;
    }

    public static boolean canAttack() {
        if (hasPreAttackRestrictions()) {
            return true;
        }

        if (mc.player.isOnGround()) {
            // Do not make Aura appear frozen during normal ground combat. If the
            // player is actively jumping and there is room above, wait for the
            // falling half of the jump and turn that hit into a real critical.
            if (!mc.options.jumpKey.isPressed()) {
                return true;
            }
            Vec3d requestedJump = new Vec3d(0.0D, 0.6D, 0.0D);
            Vec3d allowedJump = mc.player.adjustMovementForCollisions(requestedJump);
            return allowedJump.y < 0.45D;
        }

        // A vanilla critical is valid only while descending with accumulated fall
        // distance. Waiting through the rising half prevents ordinary air hits.
        return mc.player.fallDistance > 0.0F && mc.player.getVelocity().y < -1.0E-4D;
    }

    private static boolean hasPreAttackRestrictions() {
        return mc.player.isInLava()
                || mc.player.isClimbing()
                || mc.player.isTouchingWater() && mc.player.isSubmergedInWater()
                || mc.player.hasStatusEffect(StatusEffects.LEVITATION)
                || mc.player.hasStatusEffect(StatusEffects.SLOW_FALLING)
                || mc.player.hasStatusEffect(StatusEffects.BLINDNESS)
                || PlayerIntersectionUtil.isPlayerInBlock(Blocks.COBWEB)
                || mc.player.isGliding()
                || mc.player.hasVehicle()
                || mc.player.getAbilities().flying
                || FreeCam.INSTANCE.isEnabled()
                || AirStuck.INSTANCE.frozen;
    }

    public static boolean hasPreMovementRestrictions(SimulatedPlayer simulatedPlayer) {
        return simulatedPlayer.hasStatusEffect(StatusEffects.BLINDNESS) || simulatedPlayer.hasStatusEffect(StatusEffects.LEVITATION) || PlayerIntersectionUtil.isBoxInBlock(simulatedPlayer.boundingBox, Blocks.COBWEB) || simulatedPlayer.isSubmergedInWater() || simulatedPlayer.isInLava() || simulatedPlayer.isClimbing() || !PlayerIntersectionUtil.canChangeIntoPose(EntityPose.STANDING) && mc.player.isInSneakingPose() || mc.player.getAbilities().flying;
    }

    public static boolean isPlayerInCriticalState() {
        boolean crit = mc.player.fallDistance > 0.0F && ((double)mc.player.fallDistance < 0.08D || !SimulatedPlayer.simulateLocalPlayer(1).onGround);
        return !mc.player.isOnGround() && crit;
    }

    public static boolean isPrePlayerInCriticalState(SimulatedPlayer simulatedPlayer) {
        boolean crit = simulatedPlayer.fallDistance > 0.0F && ((double)simulatedPlayer.fallDistance < 0.08D || !SimulatedPlayer.simulateLocalPlayer(2).onGround);
        return !simulatedPlayer.onGround && crit;
    }

    @Generated
    private AttackUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
