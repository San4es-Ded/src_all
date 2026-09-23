package rockstar.modules.other;










import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.server.*;
import rockstar.client.notification.*;
import rockstar.client.module.*;
import rockstar.client.inventory.*;
import rockstar.client.i18n.*;
import rockstar.client.event.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.*;

import java.util.Locale;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import pyrock.events.game.WorldChangeEvent;
import pyrock.events.network.ReceivePacketEvent;
import pyrock.events.player.ClientPlayerTickEvent;
import rockstar.client.setting.SettingOwner;
import rockstar.client.setting.ModeSetting;
import rockstar.client.setting.TextSetting;
import rockstar.client.event.EventListener;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.module.ModuleCategory;
import rockstar.client.RockstarClient;
import rockstar.client.module.ModuleInfo;
import rockstar.client.internal.inventory.ServerSelectMenuNavigator;
import rockstar.client.server.KnownServer;
import rockstar.client.server.ServerUtils;
import rockstar.client.inventory.SlotCollection;
import rockstar.client.inventory.InventorySlots;
import rockstar.client.inventory.HotbarSlot;
import rockstar.client.module.Module;
import rockstar.client.util.Stopwatch;
import rockstar.client.notification.NotificationType;

@ModuleInfo(name="Auto Join", category=ModuleCategory.OTHER, internalMethod09633="modules.descriptions.auto_join")
public class AutoJoinModule
extends Module {
    private ModeSetting internalField0668;
    private ModeSetting.InternalType0088 internalField0237;
    private ModeSetting.InternalType0088 internalField0238;
    private TextSetting internalField0384;
    private final Stopwatch internalField0519 = new Stopwatch();
    private final Stopwatch internalField0518 = new Stopwatch();
    private final Stopwatch internalField1189 = new Stopwatch();
    private final Stopwatch internalField1186 = new Stopwatch();
    private long internalField1060;
    private boolean internalField0277;
    private boolean internalField0276;
    private boolean internalField1099;
    private final EventListener<ClientPlayerTickEvent> internalField0157 = clientPlayerTickEvent -> this.internalMethod09490();
    private final EventListener<ReceivePacketEvent> internalField0158 = receivePacketEvent -> {
        Object object = receivePacketEvent.getPacket();
        if (!(object instanceof GameMessageS2CPacket)) {
            return;
        }
        GameMessageS2CPacket gameMessageS2CPacket = (GameMessageS2CPacket)object;
        if (!(ServerUtils.internalMethod01786(KnownServer.internalField0578) || ServerUtils.internalMethod01786(KnownServer.internalField1220) || ServerUtils.internalMethod01786(KnownServer.internalField0579))) {
            return;
        }
        object = gameMessageS2CPacket.content().getString().toLowerCase(Locale.ROOT);
        if (this.internalMethod04367((String)object)) {
            this.internalField0276 = false;
            if (this.internalField0237.isSelected()) {
                this.internalMethod06980(2500L);
            } else {
                this.internalField0518.internalMethod00701();
            }
            if (!this.internalField1099) {
                this.internalField1099 = true;
                RockstarClient.getInstance().internalMethod02503().internalMethod00599(NotificationType.internalField1280, LanguageManager.internalMethod07214("auto_join.retry"), LanguageManager.internalMethod07214("auto_join.retry.desc"));
            }
            return;
        }
        if (this.internalField0238.isSelected() && (ServerUtils.internalMethod01786(KnownServer.internalField0578) ? ((String)object).contains(LanguageManager.internalMethod07214("auto_join.already_connected_ft")) : ((String)object).contains(LanguageManager.internalMethod07214("auto_join.already_connected")))) {
            this.disable();
        }
    };
    private final EventListener<WorldChangeEvent> internalField1028 = worldChangeEvent -> {
        if (this.internalField0237.isSelected()) {
            this.internalMethod06980(1500L);
            return;
        }
        if (this.internalField0276) {
            this.disable();
        }
    };

    public AutoJoinModule() {
        this.internalMethod09489();
    }

    private void internalMethod09489() {
        this.internalField0668 = new ModeSetting(this, "modules.settings.auto_join.mode");
        this.internalField0237 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.auto_join.mode.duels_st").select();
        this.internalField0238 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.auto_join.mode.grief");
        this.internalField0384 = new TextSetting((SettingOwner)this, "modules.settings.auto_join.anarchy_number", this.internalField0237::isSelected).internalMethod00011("306").internalMethod07009(true);
    }

    private void internalMethod09490() {
        Object object;
        Object object2;
        if (ServerUtils.internalMethod01786(KnownServer.internalField0579) && this.internalField0237.isSelected()) {
            SlotCollection<HotbarSlot> typedValue228 = InventorySlots.internalMethod02872();
            if (this.internalField1060 > 0L) {
                if (!this.internalField1186.internalMethod02365(this.internalField1060)) {
                    return;
                }
                this.internalField1060 = 0L;
            }
            if (typedValue228.internalMethod02510(Items.DIAMOND_SWORD) != null || ServerUtils.internalMethod07321()) {
                this.internalMethod09638();
                return;
            }
            if (!this.internalField0519.internalMethod02365(300L)) {
                return;
            }
            object2 = AutoJoinModule.internalField0149.player.currentScreenHandler;
            if (object2 instanceof GenericContainerScreenHandler) {
                object = (GenericContainerScreenHandler)object2;
                if (AutoJoinModule.internalField0149.currentScreen != null && AutoJoinModule.internalField0149.currentScreen.getTitle().getString().contains("\u0412\u044b\u0431\u0435\u0440\u0438\u0442\u0435 \u0440\u0435\u0436\u0438\u043c")) {
                    AutoJoinModule.internalField0149.interactionManager.clickSlot(((GenericContainerScreenHandler)object).syncId, 13, 0, SlotActionType.QUICK_MOVE, (PlayerEntity)AutoJoinModule.internalField0149.player);
                    this.internalField0276 = true;
                    this.internalMethod06980(8000L);
                    this.internalField0519.internalMethod00701();
                    return;
                }
            }
            if ((object = typedValue228.internalMethod02510(Items.COMPASS)) != null) {
                AutoJoinModule.internalField0149.player.getInventory().setSelectedSlot(((HotbarSlot)object).internalMethod08745());
                AutoJoinModule.internalField0149.interactionManager.sendSequencedPacket(AutoJoinModule.internalField0149.world, n -> new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, n, AutoJoinModule.internalField0149.player.getYaw(), AutoJoinModule.internalField0149.player.getPitch()));
                this.internalField0519.internalMethod00701();
            }
        }
        if (this.internalField0238.isSelected()) {
            int n2 = this.internalMethod09041();
            if (n2 <= 0) {
                this.disable();
                return;
            }
            if (ServerUtils.internalMethod01786(KnownServer.internalField0578) || ServerUtils.internalMethod01786(KnownServer.internalField0579)) {
                if (ServerUtils.internalMethod07319() != n2 || !this.internalField0276) {
                    if (!this.internalField0518.internalMethod02365(500L)) {
                        return;
                    }
                    AutoJoinModule.internalField0149.player.networkHandler.sendChatCommand("an" + this.internalField0384.internalMethod08926());
                    this.internalField0276 = true;
                    this.internalField0518.internalMethod00701();
                } else {
                    this.disable();
                }
            }
            if (ServerUtils.internalMethod01786(KnownServer.internalField1220)) {
                if (ServerUtils.internalField1055 == n2 && this.internalField0276) {
                    this.disable();
                    return;
                }
                if (!this.internalField1189.internalMethod02365(300L)) {
                    return;
                }
                if (!this.internalField0277) {
                    if (ServerSelectMenuNavigator.internalMethod00388()) {
                        this.internalField0277 = true;
                        this.internalField1189.internalMethod00701();
                    }
                    return;
                }
                object2 = AutoJoinModule.internalField0149.player.currentScreenHandler;
                if (object2 instanceof GenericContainerScreenHandler) {
                    object = (GenericContainerScreenHandler)object2;
                    if (AutoJoinModule.internalField0149.currentScreen != null) {
                        object2 = AutoJoinModule.internalField0149.currentScreen.getTitle().getString();
                        if (ServerSelectMenuNavigator.internalMethod00290((String)object2)) {
                            int n3 = ServerSelectMenuNavigator.internalMethod06353((GenericContainerScreenHandler)object, true);
                            if (n3 != -1) {
                                ServerSelectMenuNavigator.internalMethod06352((GenericContainerScreenHandler)object, n3);
                                this.internalField1189.internalMethod00701();
                            }
                            return;
                        }
                        if (ServerSelectMenuNavigator.internalMethod06582((String)object2, true)) {
                            ServerSelectMenuNavigator.InternalType0222 nestedValue2030 = ServerSelectMenuNavigator.internalMethod01194((GenericContainerScreenHandler)object, (String)object2, n2, true);
                            if (nestedValue2030 == ServerSelectMenuNavigator.InternalType0222.internalField1192) {
                                this.internalField1189.internalMethod00701();
                                this.internalField0276 = true;
                            } else if (nestedValue2030 == ServerSelectMenuNavigator.InternalType0222.internalField0535) {
                                this.internalField1189.internalMethod00701();
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean internalMethod04367(String string) {
        return string.contains("\u0441\u0435\u0440\u0432\u0435\u0440 \u0437\u0430\u043f\u043e\u043b\u043d\u0435\u043d") || string.contains("\u0441\u0435\u0440\u0432\u0435\u0440 \u043f\u0435\u0440\u0435\u043f\u043e\u043b\u043d\u0435\u043d") || string.contains("\u043d\u0435\u0442 \u0441\u0432\u043e\u0431\u043e\u0434\u043d\u044b\u0445 \u0441\u043b\u043e\u0442\u043e\u0432") || string.contains("\u043a\u0438\u043a\u043d\u0443\u0442\u044b \u043f\u0440\u0438 \u043f\u043e\u0434\u043a\u043b\u044e\u0447\u0435\u043d\u0438\u0438") || string.contains("\u0441\u0435\u0440\u0432\u0435\u0440 \u043d\u0435\u0434\u043e\u0441\u0442\u0443\u043f\u0435\u043d");
    }

    private void internalMethod06980(long l) {
        this.internalField1060 = l;
        this.internalField1186.internalMethod00701();
    }

    private void internalMethod09638() {
        RockstarClient.getInstance().internalMethod02503().internalMethod00599(NotificationType.internalField0704, LanguageManager.internalMethod07214("auto_join.success"), LanguageManager.internalMethod07214("auto_join.success.desc"));
        this.disable();
    }

    private int internalMethod09041() {
        try {
            return Integer.parseInt(this.internalField0384.internalMethod08926());
        }
        catch (NumberFormatException numberFormatException) {
            return -1;
        }
    }

    @Override
    public void onEnable() {
        this.internalField0277 = false;
        this.internalField0276 = false;
        this.internalField1099 = false;
        this.internalField1060 = 0L;
        this.internalField0519.internalMethod00701();
        this.internalField0518.internalMethod00701();
        this.internalField1189.internalMethod00701();
        this.internalField1186.internalMethod00701();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.internalField0277 = false;
        this.internalField0276 = false;
        this.internalField1099 = false;
        this.internalField1060 = 0L;
        this.internalField1189.internalMethod00701();
        super.onDisable();
    }
}
