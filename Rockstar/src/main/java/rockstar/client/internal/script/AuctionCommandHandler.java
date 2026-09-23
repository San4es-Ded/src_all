package rockstar.client.internal.script;




import rockstar.client.util.*;
import rockstar.client.event.*;
import rockstar.client.*;
import net.minecraft.network.packet.c2s.play.ChatCommandSignedC2SPacket;
import pyrock.events.network.SendPacketEvent;
import rockstar.client.event.EventListener;
import rockstar.client.RockstarClient;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.util.MathUtils;

public class AuctionCommandHandler
implements MinecraftClientAccess {
    private final EventListener<SendPacketEvent> internalField0157 = sendPacketEvent -> {
        Object object = sendPacketEvent.getPacket();
        if (object instanceof ChatCommandSignedC2SPacket) {
            ChatCommandSignedC2SPacket chatCommandSignedC2SPacket = (ChatCommandSignedC2SPacket)object;
            if (AuctionCommandHandler.internalField0149.player == null) {
                return;
            }
            object = chatCommandSignedC2SPacket.command();
            if (((String)object).startsWith("ah me")) {
                AuctionCommandHandler.internalField0149.player.networkHandler.sendChatMessage("/ah " + AuctionCommandHandler.internalField0149.player.getName().getString());
                sendPacketEvent.cancel();
            }
            if (((String)object).startsWith("ah sell ")) {
                String string = ((String)object).replaceFirst("ah sell ", "");
                String string2 = MathUtils.internalMethod07216(string);
                AuctionCommandHandler.internalField0149.player.networkHandler.sendChatMessage("/ah sell " + Math.round(Float.parseFloat(string2)));
                sendPacketEvent.cancel();
            }
        }
    };

    public AuctionCommandHandler() {
        RockstarClient.getInstance().internalMethod03317().internalMethod00647(this);
    }
}

