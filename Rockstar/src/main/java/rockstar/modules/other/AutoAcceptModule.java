package rockstar.modules.other;





import rockstar.client.setting.*;
import rockstar.client.server.*;
import rockstar.client.module.*;
import rockstar.client.event.*;
import rockstar.client.*;

import globals.client.Information;
import globals.shared.proto.Packets;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import pyrock.events.network.ReceivePacketEvent;
import rockstar.client.setting.MultiSelectSetting;
import rockstar.client.event.EventListener;
import rockstar.client.module.ModuleCategory;
import rockstar.client.RockstarClient;
import rockstar.client.module.ModuleInfo;
import rockstar.client.server.ServerUtils;
import rockstar.client.module.Module;

@ModuleInfo(name="Auto Accept", category=ModuleCategory.OTHER, internalMethod09633="modules.descriptions.auto_accept")
public class AutoAcceptModule
extends Module {
    private MultiSelectSetting internalField0675;
    private MultiSelectSetting.InternalType0091 internalField0245;
    private MultiSelectSetting.InternalType0091 internalField0244;
    private MultiSelectSetting.InternalType0091 internalField1075;
    private final EventListener<ReceivePacketEvent> internalField0157 = receivePacketEvent -> {
        Packet<?> packet = receivePacketEvent.getPacket();
        if (packet instanceof GameMessageS2CPacket) {
            GameMessageS2CPacket gameMessageS2CPacket = (GameMessageS2CPacket)packet;
            if (AutoAcceptModule.internalField0149.player != null && (gameMessageS2CPacket.content().getString().contains("\u0442\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c\u0441\u044f") || gameMessageS2CPacket.content().getString().contains("\u0437\u0430\u043f\u0440\u0430\u0448\u0438\u0432\u0430\u0435\u0442 \u0442\u0435\u043b\u0435\u043f\u043e\u0440\u0442")) && !ServerUtils.internalField0277 && this.internalMethod02498(gameMessageS2CPacket.content().getString())) {
                AutoAcceptModule.internalField0149.player.networkHandler.sendChatCommand("tpaccept");
            }
        }
    };

    public AutoAcceptModule() {
        this.internalMethod09872();
    }

    private void internalMethod09872() {
        this.internalField0675 = new MultiSelectSetting(this, "modules.settings.auto_accept.mode");
        this.internalField0245 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.auto_accept.mode.all");
        this.internalField0244 = new MultiSelectSetting.InternalType0091(this.internalField0675, "rockUsers", this.internalField0245::isSelected);
        this.internalField1075 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.auto_accept.mode.friends_only", this.internalField0245::isSelected).select();
    }

    private boolean internalMethod02498(String string) {
        if (this.internalField0245.isSelected()) {
            return true;
        }
        String string2 = string.replaceAll("\u00a7[0-9a-fklmnor]", "").trim();
        String string3 = this.internalMethod00974(string2);
        if (string3 == null) {
            return false;
        }
        if (this.internalField0244.isSelected()) {
            for (Packets.InternalType0018 nestedValue2002 : Information.getVisiblePlayers()) {
                if (nestedValue2002.gameInfo() == null || nestedValue2002.gameInfo().nickname() == null || !nestedValue2002.gameInfo().nickname().equals(string3)) continue;
                return true;
            }
        }
        if (this.internalField1075.isSelected()) {
            return RockstarClient.getInstance().internalMethod03375().internalMethod00380(string3);
        }
        return false;
    }

    private String internalMethod00974(String string) {
        String[] stringArray;
        if (string.contains("\u043f\u0440\u043e\u0441\u0438\u0442 \u0442\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c\u0441\u044f") && (stringArray = string.split(" ")).length > 0) {
            return stringArray[0];
        }
        if (string.contains("\u0445\u043e\u0447\u0435\u0442 \u0442\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c\u0441\u044f \u043a") && (stringArray = string.split(" ")).length > 1) {
            return stringArray[1];
        }
        if (string.contains("\u0a77 \u043f\u0440\u043e\u0441\u0438\u0442 \u0442\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c\u0441\u044f") && (stringArray = string.split(" ")).length >= 2) {
            return stringArray[1];
        }
        if (string.contains("\u279d \u041d\u0438\u043a:") && (stringArray = string.split(":")).length >= 2) {
            return stringArray[1].trim();
        }
        if (string.contains("\u0442\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c\u0441\u044f")) {
            stringArray = string.split(" ");
            for (int i = 0; i < stringArray.length - 1; ++i) {
                if (!stringArray[i].equals("\u043f\u0440\u043e\u0441\u0438\u0442") && !stringArray[i].equals("\u0437\u0430\u043f\u0440\u0430\u0448\u0438\u0432\u0430\u0435\u0442")) continue;
                return stringArray[i - 1];
            }
        }
        return null;
    }
}
