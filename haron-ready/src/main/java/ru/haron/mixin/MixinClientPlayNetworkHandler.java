package ru.haron.mixin;

import haron.events.TotemPopEvent;
import haron.events.GameJoinedEvent;
import haron.events.WorldClearedEvent;
import haron.events.EventDispatcher;
import haron.player.PlayerCapeTracker;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ClientPlayNetworkHandler.class})
public class MixinClientPlayNetworkHandler {
    private static final Map<UUID, ItemStack> cachedTotems = new HashMap<UUID, ItemStack>();

    @Inject(method={"onEntityStatus"}, at={@At(value="HEAD")})
    private void onEntityStatus(EntityStatusS2CPacket EntityStatusS2CPacketVar, CallbackInfo callbackInfo) {
        Entity EntityVarGetEntity;
        if (EntityStatusS2CPacketVar.getStatus() == 35 && (EntityVarGetEntity = EntityStatusS2CPacketVar.getEntity((World)MinecraftClient.getInstance().world)) instanceof LivingEntity) {
            LivingEntity LivingEntityVar = (LivingEntity)EntityVarGetEntity;
            EventDispatcher.EVENT_BUS.post((Object)new TotemPopEvent(LivingEntityVar, this.getCachedTotem(LivingEntityVar)));
        }
    }

    private ItemStack getCachedTotem(LivingEntity LivingEntityVar) {
        UUID uuidGetUuid = LivingEntityVar.getUuid();
        ItemStack ItemStackVarGetMainHandStack = LivingEntityVar.getMainHandStack();
        ItemStack ItemStackVarGetOffHandStack = LivingEntityVar.getOffHandStack();
        ItemStack ItemStackVarCopy = null;
        if (ItemStackVarGetMainHandStack.getItem() == Items.TOTEM_OF_UNDYING) {
            ItemStackVarCopy = ItemStackVarGetMainHandStack.copy();
        } else if (ItemStackVarGetOffHandStack.getItem() == Items.TOTEM_OF_UNDYING) {
            ItemStackVarCopy = ItemStackVarGetOffHandStack.copy();
        }
        if (ItemStackVarCopy != null) {
            cachedTotems.put(uuidGetUuid, ItemStackVarCopy);
            return ItemStackVarCopy;
        }
        ItemStack ItemStackVarRemove = cachedTotems.remove(uuidGetUuid);
        return ItemStackVarRemove != null ? ItemStackVarRemove : new ItemStack((ItemConvertible)Items.TOTEM_OF_UNDYING);
    }

    @Inject(method={"onPlayerList"}, at={@At(value="RETURN")})
    private void onPlayerList(PlayerListS2CPacket PlayerListS2CPacketVar, CallbackInfo callbackInfo) {
        PlayerCapeTracker.c().a(PlayerListS2CPacketVar);
    }

    @Inject(method={"onGameJoin"}, at={@At(value="TAIL")})
    public void onGameJoin(GameJoinS2CPacket GameJoinS2CPacketVar, CallbackInfo callbackInfo) {
        EventDispatcher.EVENT_BUS.post((Object)new GameJoinedEvent());
        PlayerCapeTracker.c().a();
    }

    @Inject(method={"clearWorld"}, at={@At(value="HEAD")})
    public void onClearWorld(CallbackInfo callbackInfo) {
        cachedTotems.clear();
        PlayerCapeTracker.c().b();
        EventDispatcher.EVENT_BUS.post((Object)new WorldClearedEvent());
    }
}

