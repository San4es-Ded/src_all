package rockstar.client.internal.game;


import rockstar.client.*;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;

public final class CriticalHitUtils {
    public static boolean internalMethod03429(PlayerEntity playerEntity) {
        if (playerEntity == null) {
            return false;
        }
        if (playerEntity.getAbilities().flying) {
            return false;
        }
        if (playerEntity.hasStatusEffect(StatusEffects.BLINDNESS) || playerEntity.hasStatusEffect(StatusEffects.SLOW_FALLING)) {
            return false;
        }
        return !playerEntity.isSubmergedInWater() && !playerEntity.isInLava();
    }

    public static boolean internalMethod05020(PlayerEntity playerEntity) {
        if (playerEntity == null || !playerEntity.isSprinting()) {
            return false;
        }
        if (playerEntity.getAttackCooldownProgress(1.0f) < 0.75f) {
            return false;
        }
        if (!CriticalHitUtils.internalMethod03429(playerEntity)) {
            return false;
        }
        if (playerEntity.isOnGround()) {
            return false;
        }
        return playerEntity.getVelocity().y < 0.0;
    }
}

