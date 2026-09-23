package rockstar.modules.player;


import rockstar.client.module.*;
import rockstar.client.*;

import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import rockstar.client.module.ModuleCategory;
import rockstar.client.module.ModuleInfo;
import rockstar.client.module.Module;

@ModuleInfo(name="No Fall", category=ModuleCategory.PLAYER)
public class NoFallModule
extends Module {
    @Override
    public void internalMethod08229() {
        if ((double)NoFallModule.internalField0149.player.fallDistance > 2.5) {
            Vec3d vec3d = NoFallModule.internalField0149.player.getEntityPos();
            NoFallModule.internalField0149.player.networkHandler.sendPacket((Packet)new PlayerMoveC2SPacket.Full(vec3d.x, vec3d.y, vec3d.z, NoFallModule.internalField0149.player.getYaw(), NoFallModule.internalField0149.player.getPitch(), true, true));
            NoFallModule.internalField0149.player.networkHandler.sendPacket((Packet)new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, 0, NoFallModule.internalField0149.player.getYaw(), NoFallModule.internalField0149.player.getPitch()));
            NoFallModule.internalField0149.player.fallDistance = 0.0f;
        }
    }
}
