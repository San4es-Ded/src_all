package rockstar.modules.movement;






import rockstar.client.setting.*;
import rockstar.client.server.*;
import rockstar.client.module.*;
import rockstar.client.event.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;

import lombok.Generated;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SplashPotionItem;
import net.minecraft.item.consume.UseAction;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.Hand;
import pyrock.events.network.SendPacketEvent;
import pyrock.events.player.ClientPlayerTickEvent;
import pyrock.events.player.KeepSprintEvent;
import rockstar.client.setting.BooleanSetting;
import rockstar.client.event.EventListener;
import rockstar.client.module.ModuleCategory;
import rockstar.client.module.ModuleInfo;
import rockstar.client.server.KnownServer;
import rockstar.client.server.ServerUtils;
import rockstar.client.internal.core.PlayerSprintAccess;
import rockstar.client.module.Module;

@ModuleInfo(name="Auto Sprint", category=ModuleCategory.MOVEMENT, internalMethod08049=true)
public class AutoSprintModule
extends Module {
    private BooleanSetting internalField0650;
    private BooleanSetting internalField0651;
    private int internalField0227;
    private final EventListener<KeepSprintEvent> internalField0157 = keepSprintEvent -> {
        if (this.internalField0650.internalMethod04496()) {
            keepSprintEvent.cancel();
        }
    };
    private final EventListener<ClientPlayerTickEvent> internalField0158 = clientPlayerTickEvent -> {
        if (this.internalField0227 > 0) {
            AutoSprintModule.internalField0149.options.sprintKey.setPressed(false);
            --this.internalField0227;
            return;
        }
        AutoSprintModule.internalField0149.options.sprintKey.setPressed(true);
    };
    private final EventListener<SendPacketEvent> internalField1028 = sendPacketEvent -> {
        if (AutoSprintModule.internalField0149.player == null || !this.internalField0650.internalMethod04496()) {
            return;
        }
        if (sendPacketEvent.getPacket() instanceof UpdateSelectedSlotC2SPacket && this.internalMethod09383()) {
            this.internalMethod09384();
            return;
        }
        Packet<?> packet = sendPacketEvent.getPacket();
        if (packet instanceof PlayerInteractItemC2SPacket) {
            PlayerInteractItemC2SPacket playerInteractItemC2SPacket = (PlayerInteractItemC2SPacket)packet;
            if (ServerUtils.internalMethod01786(KnownServer.internalField0579) && this.internalMethod06476(playerInteractItemC2SPacket.getHand()).getItem() instanceof SplashPotionItem) {
                this.internalMethod09384();
            }
        }
    };

    public AutoSprintModule() {
        this.internalMethod09382();
    }

    private void internalMethod09382() {
        this.internalField0650 = new BooleanSetting(this, "modules.settings.keepSprint");
        this.internalField0651 = new BooleanSetting(this, "modules.settings.auto_sprint.ignore_hunger");
    }

    private boolean internalMethod09383() {
        if (!AutoSprintModule.internalField0149.player.isUsingItem()) {
            return false;
        }
        UseAction useAction = AutoSprintModule.internalField0149.player.getActiveItem().getUseAction();
        return useAction == UseAction.EAT || useAction == UseAction.DRINK;
    }

    private ItemStack internalMethod06476(Hand hand) {
        return hand == Hand.OFF_HAND ? AutoSprintModule.internalField0149.player.getOffHandStack() : AutoSprintModule.internalField0149.player.getMainHandStack();
    }

    private void internalMethod09384() {
        this.internalField0227 = 2;
        AutoSprintModule.internalField0149.options.sprintKey.setPressed(false);
        if (!AutoSprintModule.internalField0149.player.isSprinting()) {
            return;
        }
        AutoSprintModule.internalField0149.player.setSprinting(false);
        ((PlayerSprintAccess)AutoSprintModule.internalField0149.player).rockstar$syncSprinting();
    }

    @Override
    public void onDisable() {
        this.internalField0227 = 0;
    }

    @Generated
    public BooleanSetting internalMethod02831() {
        return this.internalField0651;
    }
}
