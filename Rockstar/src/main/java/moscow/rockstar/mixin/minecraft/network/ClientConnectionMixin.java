package moscow.rockstar.mixin.minecraft.network;



import rockstar.client.ui.*;
import rockstar.client.internal.ui.*;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.CloseScreenS2CPacket;
import net.minecraft.network.packet.s2c.play.TeamS2CPacket;
import net.minecraft.scoreboard.Team;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pyrock.events.network.ReceivePacketEvent;
import pyrock.events.network.SendPacketEvent;
import rockstar.client.RockstarClient;
import rockstar.client.internal.ui.AuctionSearchTracker;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.ui.RockstarScreen;

@Mixin(value={ClientConnection.class})
public class ClientConnectionMixin
implements MinecraftClientAccess {
    @Unique
    private static boolean stackOverflowFix;

    @Inject(method={"handlePacket"}, at={@At(value="HEAD")}, cancellable=true)
    private static <T extends PacketListener> void triggerReceivePacketEvent(Packet<T> packet, PacketListener packetListener, CallbackInfo callbackInfo) {
        if (packet instanceof CloseScreenS2CPacket && ClientConnectionMixin.internalField0149.currentScreen instanceof RockstarScreen) {
            callbackInfo.cancel();
            return;
        }
        ReceivePacketEvent receivePacketEvent = new ReceivePacketEvent(packet);
        RockstarClient.getInstance().internalMethod03317().internalMethod06883(receivePacketEvent);
        if (receivePacketEvent.isCancelled()) {
            callbackInfo.cancel();
        }
        if (packet instanceof TeamS2CPacket) {
            TeamS2CPacket teamS2CPacket = (TeamS2CPacket)packet;
            try {
                if (teamS2CPacket.getTeamName() != null && teamS2CPacket.getTeamName().startsWith("collideRule_") && ClientConnectionMixin.internalField0149.world != null) {
                    Team team = ClientConnectionMixin.internalField0149.world.getScoreboard().getTeam(teamS2CPacket.getTeamName());
                    if (teamS2CPacket.getPlayerNames() != null && !teamS2CPacket.getPlayerNames().isEmpty()) {
                        if (team == null) {
                            callbackInfo.cancel();
                            return;
                        }
                        for (String string : teamS2CPacket.getPlayerNames()) {
                            if (team.getPlayerList().contains(string)) continue;
                            callbackInfo.cancel();
                            return;
                        }
                    }
                }
            }
            catch (Exception exception) {
                System.err.println("NetworkFix: \u041e\u0442\u043c\u0435\u043d\u0435\u043d \u043f\u0440\u043e\u0431\u043b\u0435\u043c\u043d\u044b\u0439 TeamS2CPacket: " + exception.getMessage());
                callbackInfo.cancel();
            }
        }
    }

    @Inject(method={"send(Lnet/minecraft/network/packet/Packet;)V"}, at={@At(value="HEAD")}, cancellable=true)
    public void triggerSendPacketEvent(Packet<?> packet, CallbackInfo callbackInfo) {
        Packet<?> packet2;
        SendPacketEvent sendPacketEvent = new SendPacketEvent(packet);
        if (stackOverflowFix) {
            return;
        }
        AuctionSearchTracker.internalMethod05770(packet);
        RockstarClient.getInstance().internalMethod03317().internalMethod06883(sendPacketEvent);
        if (sendPacketEvent.isCancelled()) {
            callbackInfo.cancel();
        }
        if ((packet2 = sendPacketEvent.getPacket()) != packet) {
            callbackInfo.cancel();
            stackOverflowFix = true;
            internalField0149.getNetworkHandler().sendPacket(packet2);
            stackOverflowFix = false;
        }
    }
}
