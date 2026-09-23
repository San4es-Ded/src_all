package rockstar.modules.player;








import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.server.*;
import rockstar.client.module.*;
import rockstar.client.i18n.*;
import rockstar.client.event.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.*;

import net.minecraft.entity.Entity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.Text;
import pyrock.events.network.ReceivePacketEvent;
import pyrock.events.player.ClientPlayerTickEvent;
import rockstar.client.setting.SettingOwner;
import rockstar.client.setting.ModeSetting;
import rockstar.client.setting.MultiSelectSetting;
import rockstar.client.setting.SliderSetting;
import rockstar.client.event.EventListener;
import rockstar.client.internal.inventory.EntityTargetFilter;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.module.ModuleCategory;
import rockstar.client.module.ModuleInfo;
import rockstar.client.util.TextUtils;
import rockstar.client.server.ServerUtils;
import rockstar.client.module.Module;
import rockstar.client.util.Stopwatch;

@ModuleInfo(name="Auto Leave", category=ModuleCategory.PLAYER)
public class AutoLeaveModule
extends Module {
    private ModeSetting internalField0668;
    private ModeSetting.InternalType0088 internalField0237;
    private ModeSetting.InternalType0088 internalField0238;
    private ModeSetting.InternalType0088 internalField1066;
    private SliderSetting internalField0383;
    private SliderSetting internalField0382;
    private SliderSetting internalField1142;
    private MultiSelectSetting internalField0675;
    private MultiSelectSetting.InternalType0091 internalField0245;
    private MultiSelectSetting.InternalType0091 internalField0244;
    private MultiSelectSetting.InternalType0091 internalField1075;
    private MultiSelectSetting.InternalType0091 internalField1074;
    private MultiSelectSetting.InternalType0091 internalField1073;
    private MultiSelectSetting.InternalType0091 internalField1072;
    private ModeSetting internalField0669;
    private ModeSetting.InternalType0088 internalField1067;
    private ModeSetting.InternalType0088 internalField1068;
    private ModeSetting.InternalType0088 internalField1065;
    private final Stopwatch internalField0519 = new Stopwatch();
    private boolean internalField0277;
    private final EventListener<ClientPlayerTickEvent> internalField0157 = clientPlayerTickEvent -> {
        if (this.internalField0237.isSelected()) {
            EntityTargetFilter typedValue183 = new EntityTargetFilter.InternalType0309().internalMethod00547(this.internalField0245.isSelected()).internalMethod08543(this.internalField0244.isSelected()).internalMethod07990(this.internalField1075.isSelected()).internalMethod09114(this.internalField1074.isSelected()).internalMethod09525(this.internalField1072.isSelected()).internalMethod08126(this.internalField1073.isSelected()).internalMethod03468(this.internalField0383.internalMethod08576()).internalMethod03528();
            for (Entity entity : AutoLeaveModule.internalField0149.world.getEntities()) {
                if (entity == null || entity == AutoLeaveModule.internalField0149.player || AutoLeaveModule.internalField0149.player == null || ServerUtils.internalField0277 || !typedValue183.internalMethod05417(entity)) continue;
                if (this.internalField1067.isSelected()) {
                    AutoLeaveModule.internalField0149.player.networkHandler.sendChatCommand("hub");
                } else if (this.internalField1068.isSelected()) {
                    AutoLeaveModule.internalField0149.player.networkHandler.getConnection().disconnect(Text.of((String)LanguageManager.internalMethod07214("modules.auto_leave.near_player")));
                } else if (this.internalField1065.isSelected()) {
                    AutoLeaveModule.internalField0149.player.networkHandler.sendChatCommand("spawn");
                }
                this.toggle();
                break;
            }
        }
        if (this.internalField0238.isSelected() && AutoLeaveModule.internalField0149.player != null && AutoLeaveModule.internalField0149.player.getHealth() + AutoLeaveModule.internalField0149.player.getAbsorptionAmount() <= this.internalField0382.internalMethod08576()) {
            if (this.internalField1067.isSelected()) {
                AutoLeaveModule.internalField0149.player.networkHandler.sendChatCommand("hub");
            } else if (this.internalField1068.isSelected()) {
                AutoLeaveModule.internalField0149.player.networkHandler.getConnection().disconnect(Text.of((String)LanguageManager.internalMethod07214("modules.auto_leave.low_health")));
            } else if (this.internalField1065.isSelected()) {
                AutoLeaveModule.internalField0149.player.networkHandler.sendChatCommand("spawn");
            }
            this.toggle();
        }
        if (!this.internalField0277) {
            return;
        }
        if (this.internalField0519.internalMethod02365((long)this.internalField1142.internalMethod08576() * 1000L)) {
            AutoLeaveModule.internalField0149.player.networkHandler.sendChatCommand("an" + ServerUtils.internalField0228);
            this.internalField0277 = false;
        }
    };
    private final EventListener<ReceivePacketEvent> internalField0158 = receivePacketEvent -> {
        GameMessageS2CPacket gameMessageS2CPacket;
        Packet<?> packet = receivePacketEvent.getPacket();
        if (packet instanceof GameMessageS2CPacket && (gameMessageS2CPacket = (GameMessageS2CPacket)packet).content().getString().contains(LanguageManager.internalMethod07214("modules.auto_leave.banned_word")) && this.internalField1066.isSelected()) {
            AutoLeaveModule.internalField0149.player.networkHandler.sendChatCommand("hub");
            this.internalField0519.internalMethod00701();
            this.internalField0277 = true;
        }
    };

    public AutoLeaveModule() {
        this.internalMethod09546();
    }

    private void internalMethod09546() {
        this.internalField0668 = new ModeSetting(this, "modules.settings.auto_leave.leave");
        this.internalField0237 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.auto_leave.leave.distance");
        this.internalField0238 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.auto_leave.leave.health");
        this.internalField1066 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.auto_leave.leave.ban");
        this.internalField0383 = new SliderSetting((SettingOwner)this, "modules.settings.auto_leave.distance", () -> this.internalField0238.isSelected() || this.internalField1066.isSelected()).internalMethod05660(f -> " %s".formatted(LanguageManager.internalMethod07214("block")) + TextUtils.internalMethod05759(f)).internalMethod08673(1.0f).internalMethod05900(1.0f).internalMethod02732(150.0f).internalMethod08074(30.0f);
        this.internalField0382 = new SliderSetting((SettingOwner)this, "modules.settings.auto_leave.health", () -> this.internalField0237.isSelected() || this.internalField1066.isSelected()).internalMethod08673(1.0f).internalMethod05900(1.0f).internalMethod02732(20.0f).internalMethod08074(10.0f);
        this.internalField1142 = new SliderSetting((SettingOwner)this, "modules.settings.auto_leave.delay", () -> !this.internalField1066.isSelected() || this.internalField0237.isSelected() || this.internalField0238.isSelected()).internalMethod06240(LanguageManager.internalMethod07214("sec") + ".").internalMethod08673(1.0f).internalMethod05900(1.0f).internalMethod02732(60.0f).internalMethod08074(40.0f);
        this.internalField0675 = new MultiSelectSetting((SettingOwner)this, "targets", () -> this.internalField0238.isSelected() || this.internalField1066.isSelected());
        this.internalField0245 = new MultiSelectSetting.InternalType0091(this.internalField0675, "players").select();
        this.internalField0244 = new MultiSelectSetting.InternalType0091(this.internalField0675, "mobs");
        this.internalField1075 = new MultiSelectSetting.InternalType0091(this.internalField0675, "invisibles").select();
        this.internalField1074 = new MultiSelectSetting.InternalType0091(this.internalField0675, "nakedPlayers").select();
        this.internalField1073 = new MultiSelectSetting.InternalType0091(this.internalField0675, "rockUsers").select();
        this.internalField1072 = new MultiSelectSetting.InternalType0091(this.internalField0675, "friends");
        this.internalField0669 = new ModeSetting(this, "modules.settings.auto_leave.mode");
        this.internalField1067 = new ModeSetting.InternalType0088(this.internalField0669, "modules.settings.auto_leave.mode.hub");
        this.internalField1068 = new ModeSetting.InternalType0088(this.internalField0669, "modules.settings.auto_leave.mode.server");
        this.internalField1065 = new ModeSetting.InternalType0088(this.internalField0669, "modules.settings.auto_leave.mode.spawn");
    }
}
