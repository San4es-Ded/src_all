package ru.haron.mixin;

import haron.events.PlayerJoinedEvent;
import haron.events.PlayerLeftEvent;
import haron.events.EventDispatcher;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ClientPlayNetworkHandler.class})
public class PlayerListMixin {
    @Unique
    private static final ConcurrentHashMap<UUID, String> uuidToNameCache = new ConcurrentHashMap();

    @Inject(method={"onPlayerList"}, at={@At(value="HEAD")})
    private void onPlayerListUpdate(PlayerListS2CPacket PlayerListS2CPacketVar, CallbackInfo callbackInfo) {
        for (PlayerListS2CPacket.Entry class_2705Var : PlayerListS2CPacketVar.getEntries()) {
            UUID uuidProfileId = class_2705Var.profileId();
            if (PlayerListS2CPacketVar.getActions().contains(PlayerListS2CPacket.Action.ADD_PLAYER) && class_2705Var.profile() != null && class_2705Var.profile().getName() != null) {
                String name = class_2705Var.profile().getName();
                if (!uuidToNameCache.containsKey(uuidProfileId)) {
                    uuidToNameCache.put(uuidProfileId, name);
                    EventDispatcher.EVENT_BUS.post((Object)new PlayerJoinedEvent(name, uuidProfileId));
                }
            }
            if (!PlayerListS2CPacketVar.getActions().contains(PlayerListS2CPacket.Action.UPDATE_LISTED) || class_2705Var.listed()) continue;
            String str = uuidToNameCache.get(uuidProfileId);
            if (str != null) {
                EventDispatcher.EVENT_BUS.post((Object)new PlayerLeftEvent(str, uuidProfileId));
                uuidToNameCache.remove(uuidProfileId);
                continue;
            }
            EventDispatcher.EVENT_BUS.post((Object)new PlayerLeftEvent("", uuidProfileId));
        }
    }

    @Inject(method={"onGameJoin"}, at={@At(value="HEAD")})
    private void onGameJoin(GameJoinS2CPacket GameJoinS2CPacketVar, CallbackInfo callbackInfo) {
        uuidToNameCache.clear();
    }
}

