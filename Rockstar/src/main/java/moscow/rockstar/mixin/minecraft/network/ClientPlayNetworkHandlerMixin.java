package moscow.rockstar.mixin.minecraft.network;


import rockstar.client.internal.game.*;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import moscow.rockstar.mixin.accessors.EntityS2CPacketAccessor;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.CooldownUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityPositionSyncS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityS2CPacket;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pyrock.events.game.EventSetCooldown;
import rockstar.modules.visual.RemovalsModule;
import rockstar.client.RockstarClient;
import rockstar.client.internal.game.EntityPositionTracker;
import rockstar.client.MinecraftClientAccess;
import rockstar.modules.combat.BackTrackModule;

@Mixin(value={ClientPlayNetworkHandler.class})
public class ClientPlayNetworkHandlerMixin
implements MinecraftClientAccess {
    @WrapWithCondition(method={"onEntityStatus"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/particle/ParticleManager;addEmitter(Lnet/minecraft/entity/Entity;Lnet/minecraft/particle/ParticleEffect;I)V")})
    private boolean rockstar$hideTotemParticles(ParticleManager particleManager, Entity entity, ParticleEffect particleEffect, int n) {
        RemovalsModule typedValue322 = RockstarClient.getInstance().getModuleManager().getModule(RemovalsModule.class);
        return !typedValue322.isEnabled() || !typedValue322.internalMethod09727().isSelected();
    }

    @WrapWithCondition(method={"onEntityStatus"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/render/GameRenderer;showFloatingItem(Lnet/minecraft/item/ItemStack;)V")})
    private boolean rockstar$hideTotemItem(GameRenderer gameRenderer, ItemStack itemStack) {
        RemovalsModule typedValue322 = RockstarClient.getInstance().getModuleManager().getModule(RemovalsModule.class);
        return !typedValue322.isEnabled() || !typedValue322.internalMethod09727().isSelected();
    }

    @Inject(method={"onEntity"}, at={@At(value="TAIL")})
    public void onEntity(EntityS2CPacket entityS2CPacket, CallbackInfo callbackInfo) {
        ClientPlayNetworkHandler clientPlayNetworkHandler = (ClientPlayNetworkHandler)(Object)this;
        ClientWorld clientWorld = clientPlayNetworkHandler.getWorld();
        if (clientWorld == null) {
            return;
        }
        int n = ((EntityS2CPacketAccessor)(Object)entityS2CPacket).getId();
        Entity entity = clientWorld.getEntityById(n);
        if (entity == null) {
            return;
        }
        double d = (double)entityS2CPacket.getDeltaX() / 4096.0;
        double d2 = (double)entityS2CPacket.getDeltaY() / 4096.0;
        double d3 = (double)entityS2CPacket.getDeltaZ() / 4096.0;
        Vec3d vec3d = EntityPositionTracker.internalMethod01255(entity).add(d, d2, d3);
        EntityPositionTracker.internalMethod03410(entity, vec3d);
        BackTrackModule internalValue0005 = RockstarClient.getInstance().getModuleManager().getModule(BackTrackModule.class);
        internalValue0005.internalMethod01533(entity, vec3d, System.currentTimeMillis());
    }

    @Inject(method={"onEntityPositionSync"}, at={@At(value="HEAD")})
    private void onEntityPositionSyncPacket(EntityPositionSyncS2CPacket entityPositionSyncS2CPacket, CallbackInfo callbackInfo) {
        ClientPlayNetworkHandler clientPlayNetworkHandler = (ClientPlayNetworkHandler)(Object)this;
        ClientWorld clientWorld = clientPlayNetworkHandler.getWorld();
        if (clientWorld == null) {
            return;
        }
        int n = entityPositionSyncS2CPacket.id();
        Entity entity = clientWorld.getEntityById(n);
        if (entity == null) {
            return;
        }
        Vec3d vec3d = entityPositionSyncS2CPacket.values().position();
        EntityPositionTracker.internalMethod03410(entity, vec3d);
        BackTrackModule internalValue0005 = RockstarClient.getInstance().getModuleManager().getModule(BackTrackModule.class);
        internalValue0005.internalMethod07322(entity, vec3d, System.currentTimeMillis());
    }

    @Inject(method={"onCooldownUpdate"}, at={@At(value="HEAD")}, cancellable=true)
    private void handleCooldown(CooldownUpdateS2CPacket cooldownUpdateS2CPacket, CallbackInfo callbackInfo) {
        EventSetCooldown eventSetCooldown = new EventSetCooldown(cooldownUpdateS2CPacket.cooldown(), cooldownUpdateS2CPacket.cooldownGroup());
        RockstarClient.getInstance().internalMethod03317().internalMethod06883(eventSetCooldown);
        if (eventSetCooldown.getCooldown() != cooldownUpdateS2CPacket.cooldown()) {
            callbackInfo.cancel();
            ClientPlayNetworkHandlerMixin.internalField0149.player.getItemCooldownManager().set(cooldownUpdateS2CPacket.cooldownGroup(), eventSetCooldown.getCooldown());
        }
    }
}
