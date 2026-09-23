package ru.haron.mixin;

import haron.gui.friends.FriendUtils;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ClientPlayerInteractionManager.class})
public class AttackBlockMixin {
    @Inject(method={"attackEntity"}, at={@At(value="HEAD")}, cancellable=true)
    private void haron$blockTeammate(PlayerEntity attacker, Entity target, CallbackInfo ci) {
        if (target == null || !(target instanceof PlayerEntity)) {
            return;
        }
        String name = target.getName().getString();
        if (FriendUtils.isFriendName(name) && attacker.getScoreboardTeam() != null && ((PlayerEntity)target).getScoreboardTeam() != null && attacker.getScoreboardTeam().getName().equals(((PlayerEntity)target).getScoreboardTeam().getName())) {
            ci.cancel();
        }
    }
}

