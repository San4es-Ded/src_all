package rockstar.modules.other;





import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.module.*;
import rockstar.client.event.*;
import rockstar.client.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import pyrock.events.game.WorldChangeEvent;
import pyrock.events.network.ReceivePacketEvent;
import rockstar.client.setting.ModeSetting;
import rockstar.client.event.EventListener;
import rockstar.client.module.ModuleCategory;
import rockstar.client.module.ModuleInfo;
import rockstar.client.module.Module;
import rockstar.client.util.Stopwatch;

@ModuleInfo(name="Auto Duels", category=ModuleCategory.OTHER, internalMethod09633="modules.descriptions.auto_duels")
public class AutoDuelsModule
extends Module {
    private ModeSetting internalField0668;
    private ModeSetting.InternalType0088 internalField0237;
    private ModeSetting.InternalType0088 internalField0238;
    private ModeSetting.InternalType0088 internalField1066;
    private ModeSetting internalField0669;
    private ModeSetting.InternalType0088 internalField1067;
    private ModeSetting.InternalType0088 internalField1068;
    private ModeSetting.InternalType0088 internalField1065;
    private ModeSetting.InternalType0088 internalField1480;
    private ModeSetting.InternalType0088 internalField1481;
    private ModeSetting.InternalType0088 internalField1483;
    private ModeSetting.InternalType0088 internalField1485;
    private ModeSetting.InternalType0088 internalField1484;
    private ModeSetting.InternalType0088 internalField1482;
    private final Stopwatch internalField0519 = new Stopwatch();
    private final List<String> internalField0416 = new ArrayList<String>();
    private final EventListener<ReceivePacketEvent> internalField0157 = receivePacketEvent -> {
        Object object = receivePacketEvent.getPacket();
        if (object instanceof GameMessageS2CPacket) {
            GameMessageS2CPacket gameMessageS2CPacket = (GameMessageS2CPacket)object;
            if (((String)(object = gameMessageS2CPacket.content().getString())).contains("\u043f\u0440\u0438\u043d\u044f\u043b") && !((String)object).contains("\u043d\u0435 \u043f\u0440\u0438\u043d\u044f\u043b") || ((String)object).contains("\u043a\u043e\u043c\u0430\u043d\u0434\u044b")) {
                this.internalField0416.clear();
                this.toggle();
            }
            if (((String)object).contains("\u0411\u0430\u043b\u0430\u043d\u0441") || ((String)object).contains("\u043e\u0442\u043a\u043b\u044e\u0447\u0438\u043b \u0437\u0430\u043f\u0440\u043e\u0441\u044b")) {
                receivePacketEvent.cancel();
            }
        }
    };
    private final EventListener<WorldChangeEvent> internalField0158 = worldChangeEvent -> this.disable();

    public AutoDuelsModule() {
        this.internalMethod09497();
    }

    private void internalMethod09497() {
        this.internalField0668 = new ModeSetting(this, "modules.settings.auto_duels.prefer");
        this.internalField0237 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.auto_duels.prefer.soft");
        this.internalField0238 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.auto_duels.prefer.ansoft");
        this.internalField1066 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.auto_duels.prefer.random");
        this.internalField0669 = new ModeSetting(this, "modules.settings.auto_duels.kit");
        this.internalField1067 = new ModeSetting.InternalType0088(this.internalField0669, "modules.settings.auto_duels.kit.shield");
        this.internalField1068 = new ModeSetting.InternalType0088(this.internalField0669, "modules.settings.auto_duels.kit.thorns3");
        this.internalField1065 = new ModeSetting.InternalType0088(this.internalField0669, "modules.settings.auto_duels.kit.bow");
        this.internalField1480 = new ModeSetting.InternalType0088(this.internalField0669, "modules.settings.auto_duels.kit.totem");
        this.internalField1481 = new ModeSetting.InternalType0088(this.internalField0669, "modules.settings.auto_duels.kit.nodebuff");
        this.internalField1483 = new ModeSetting.InternalType0088(this.internalField0669, "modules.settings.auto_duels.kit.balls");
        this.internalField1485 = new ModeSetting.InternalType0088(this.internalField0669, "modules.settings.auto_duels.kit.classic");
        this.internalField1484 = new ModeSetting.InternalType0088(this.internalField0669, "modules.settings.auto_duels.kit.cheats");
        this.internalField1482 = new ModeSetting.InternalType0088(this.internalField0669, "modules.settings.auto_duels.kit.nether");
    }

    @Override
    public void internalMethod08229() {
        ArrayList<String> arrayList = new ArrayList<String>();
        for (PlayerListEntry object : AutoDuelsModule.internalField0149.player.networkHandler.getPlayerList()) {
            arrayList.add(object.getProfile().name());
        }
        if (this.internalField1066.isSelected()) {
            Collections.shuffle(arrayList);
        } else if (this.internalField0237.isSelected()) {
            Collections.reverse(arrayList);
        }
        for (String string : arrayList) {
            if (!this.internalField0519.internalMethod02365(750L) || this.internalField0416.contains(string) || string.equals(AutoDuelsModule.internalField0149.player.getNameForScoreboard())) continue;
            AutoDuelsModule.internalField0149.player.networkHandler.sendChatCommand("duel " + string);
            this.internalField0416.add(string);
            this.internalField0519.internalMethod00701();
        }
        if (AutoDuelsModule.internalField0149.player.currentScreenHandler instanceof GenericContainerScreenHandler) {
            String string = AutoDuelsModule.internalField0149.currentScreen.getTitle().getString();
            if (string.contains("\u0412\u044b\u0431\u043e\u0440 \u043d\u0430\u0431\u043e\u0440\u0430")) {
                AutoDuelsModule.internalField0149.interactionManager.clickSlot(AutoDuelsModule.internalField0149.player.currentScreenHandler.syncId, this.internalField0669.internalMethod06723().indexOf(this.internalField0669.internalMethod07213()), 0, SlotActionType.PICKUP, (PlayerEntity)AutoDuelsModule.internalField0149.player);
                AutoDuelsModule.internalField0149.player.currentScreenHandler.onSlotClick(this.internalField0669.internalMethod06723().indexOf(this.internalField0669.internalMethod07213()), 0, SlotActionType.PICKUP, (PlayerEntity)AutoDuelsModule.internalField0149.player);
            } else if (string.contains("\u041d\u0430\u0441\u0442\u0440\u043e\u0439\u043a\u0430 \u043f\u043e\u0435\u0434\u0438\u043d\u043a\u0430")) {
                AutoDuelsModule.internalField0149.interactionManager.clickSlot(AutoDuelsModule.internalField0149.player.currentScreenHandler.syncId, 0, 0, SlotActionType.PICKUP, (PlayerEntity)AutoDuelsModule.internalField0149.player);
                AutoDuelsModule.internalField0149.player.currentScreenHandler.onSlotClick(0, 0, SlotActionType.PICKUP, (PlayerEntity)AutoDuelsModule.internalField0149.player);
            }
        }
        super.internalMethod08229();
    }

    @Override
    public void onEnable() {
        this.internalField0519.internalMethod00701();
        super.onEnable();
    }
}
