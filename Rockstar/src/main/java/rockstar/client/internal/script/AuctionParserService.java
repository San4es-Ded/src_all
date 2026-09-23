package rockstar.client.internal.script;






import rockstar.client.util.*;
import rockstar.client.event.*;
import rockstar.client.data.*;
import rockstar.client.internal.ui.*;
import rockstar.client.*;
import lombok.Generated;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.Text;
import pyrock.events.network.ReceivePacketEvent;
import pyrock.events.player.ClientPlayerTickEvent;
import rockstar.client.event.EventListener;
import rockstar.client.RockstarClient;
import rockstar.client.internal.script.AuctionAutoParseConfig;
import rockstar.client.data.AuctionItem;
import rockstar.client.internal.ui.AuctionMarketScanner;
import rockstar.client.util.ClientMessages;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.util.Stopwatch;

public class AuctionParserService
implements MinecraftClientAccess {
    private static AuctionParserService internalField0799;
    private final AuctionMarketScanner internalField0795 = new AuctionMarketScanner();
    private boolean internalField0277;
    private final Stopwatch internalField0519 = new Stopwatch();
    private final EventListener<ReceivePacketEvent> internalField0157 = receivePacketEvent -> {
        if (this.internalField0795.internalMethod04384()) {
            return;
        }
        Packet<?> packet = receivePacketEvent.getPacket();
        if (packet instanceof GameMessageS2CPacket) {
            GameMessageS2CPacket gameMessageS2CPacket = (GameMessageS2CPacket)packet;
            this.internalField0795.internalMethod00573(gameMessageS2CPacket.content().getString());
        }
    };
    private final EventListener<ClientPlayerTickEvent> internalField0158 = clientPlayerTickEvent -> {
        if (AuctionParserService.internalField0149.player == null || AuctionParserService.internalField0149.world == null) {
            return;
        }
        if (this.internalField0795.internalMethod04384()) {
            return;
        }
        this.internalField0795.internalMethod04378();
        if (this.internalField0795.internalMethod04384()) {
            this.internalField0277 = true;
        }
    };

    private AuctionParserService() {
        RockstarClient.getInstance().internalMethod03317().internalMethod00647(this);
    }

    public static AuctionParserService internalMethod07210() {
        if (internalField0799 == null) {
            internalField0799 = new AuctionParserService();
        }
        return internalField0799;
    }

    public boolean internalMethod02126() {
        return !this.internalField0795.internalMethod04384();
    }

    public boolean internalMethod02128() {
        boolean bl = this.internalField0277;
        this.internalField0277 = false;
        return bl;
    }

    public double internalMethod03919(String string) {
        return this.internalField0795.internalMethod00572(string);
    }

    public boolean internalMethod08858() {
        if (AuctionItem.internalMethod00895().isEmpty()) {
            ClientMessages.internalMethod03058(Text.of((String)"\u0421\u043f\u0438\u0441\u043e\u043a \u043f\u0440\u0435\u0434\u043c\u0435\u0442\u043e\u0432 \u043f\u0443\u0441\u0442!"));
            return false;
        }
        if (this.internalMethod02126()) {
            ClientMessages.internalMethod03058(Text.of((String)"\u041f\u0430\u0440\u0441\u0438\u043d\u0433 \u0443\u0436\u0435 \u0438\u0434\u0451\u0442."));
            return false;
        }
        this.internalField0277 = false;
        this.internalField0519.internalMethod00701();
        this.internalField0795.internalMethod01389(AuctionItem.internalMethod00895());
        return this.internalMethod02126();
    }

    public boolean internalMethod08859() {
        if (!AuctionAutoParseConfig.internalMethod05800()) {
            this.internalField0519.internalMethod00701();
            return false;
        }
        if (this.internalMethod02126()) {
            return false;
        }
        if (AuctionItem.internalMethod00895().isEmpty()) {
            return false;
        }
        if (!this.internalField0519.internalMethod02365(AuctionAutoParseConfig.internalMethod08854())) {
            return false;
        }
        ClientMessages.internalMethod01809(Text.of((String)"\u0410\u0432\u0442\u043e\u043f\u0430\u0440\u0441: \u043e\u0431\u043d\u043e\u0432\u043b\u044f\u044e \u0446\u0435\u043d\u044b \u0440\u044b\u043d\u043a\u0430\u2026"));
        return this.internalMethod08858();
    }

    public long internalMethod02125() {
        if (this.internalMethod02126()) {
            return 0L;
        }
        return Math.max(0L, AuctionAutoParseConfig.internalMethod08854() - this.internalField0519.internalMethod00700());
    }

    @Generated
    public AuctionMarketScanner internalMethod06357() {
        return this.internalField0795;
    }
}

