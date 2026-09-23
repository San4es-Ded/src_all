package rockstar.modules.movement;







import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.rotation.*;
import rockstar.client.module.*;
import rockstar.client.inventory.*;
import rockstar.client.event.*;
import rockstar.client.*;

import net.minecraft.entity.Entity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.Hand;
import pyrock.events.player.InputEvent;
import rockstar.client.setting.BooleanSetting;
import rockstar.client.setting.SliderSetting;
import rockstar.client.event.EventListener;
import rockstar.client.module.ModuleCategory;
import rockstar.client.RockstarClient;
import rockstar.client.module.ModuleInfo;
import rockstar.client.util.GameUtils;
import rockstar.client.inventory.InventoryUtils;
import rockstar.client.inventory.SlotCollection;
import rockstar.client.inventory.InventorySlots;
import rockstar.client.inventory.HotbarSlot;
import rockstar.client.inventory.MainInventorySlot;
import rockstar.client.rotation.RotationBehavior;
import rockstar.client.rotation.Rotation;
import rockstar.client.rotation.RotationPriority;
import rockstar.client.module.Module;
import rockstar.client.util.Stopwatch;

@ModuleInfo(name="Elytra Strafe", category=ModuleCategory.MOVEMENT)
public class ElytraStrafeModule
extends Module {
    private SliderSetting internalField0383;
    private SliderSetting internalField0382;
    private BooleanSetting internalField0650;
    private final Stopwatch internalField0519 = new Stopwatch();
    private final EventListener<InputEvent> internalField0157 = inputEvent -> {
        if (ElytraStrafeModule.internalField0149.player == null || !ElytraStrafeModule.internalField0149.player.isGliding()) {
            return;
        }
        float f2 = (float)Math.toDegrees(GameUtils.internalMethod01047(ElytraStrafeModule.internalField0149.player.getYaw(), inputEvent.getForward(), inputEvent.getStrafe()));
        float f = ElytraStrafeModule.internalField0149.options.sneakKey.isPressed() || ElytraStrafeModule.internalField0149.options.jumpKey.isPressed()
            ? (inputEvent.getStrafe() + inputEvent.getForward() > 0.1f ? -45.0f : -90.0f)
            : 0.0f;
        if (ElytraStrafeModule.internalField0149.options.sneakKey.isPressed()) {
            f *= -1.0f;
        }
        RockstarClient.getInstance().internalMethod02368().internalMethod00418(new Rotation(f2, f), RotationBehavior.internalField0114, 180.0f, 180.0f, 180.0f, RotationPriority.internalField0121);
    };

    public ElytraStrafeModule() {
        this.internalMethod09374();
    }

    private void internalMethod09374() {
        this.internalField0383 = new SliderSetting(this, "modules.settings.elytra_target.fireworkSlot").internalMethod05900(1.0f).internalMethod02732(9.0f).internalMethod08673(1.0f).internalMethod08074(7.0f).internalMethod06240(" slot");
        this.internalField0382 = new SliderSetting(this, "modules.settings.elytra_target.fireworkDelay").internalMethod05900(0.1f).internalMethod02732(2.0f).internalMethod08673(0.1f).internalMethod08074(1.0f).internalMethod06240(" sec");
        this.internalField0650 = new BooleanSetting(this, "modules.settings.elytra_strafe.autoTakeoff").internalMethod06630();
    }

    @Override
    public void internalMethod08229() {
        if (ElytraStrafeModule.internalField0149.player == null) {
            return;
        }
        if (this.internalField0650.internalMethod04496()) {
            boolean bl;
            boolean bl2 = bl = InventoryUtils.internalMethod06826().internalMethod00210() == Items.ELYTRA;
            if (!ElytraStrafeModule.internalField0149.player.isGliding() && bl && !ElytraStrafeModule.internalField0149.player.isOnGround() && !ElytraStrafeModule.internalField0149.player.isInFluid()) {
                ElytraStrafeModule.internalField0149.player.startGliding();
                ElytraStrafeModule.internalField0149.player.networkHandler.sendPacket((Packet)new ClientCommandC2SPacket((Entity)ElytraStrafeModule.internalField0149.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
            } else if (ElytraStrafeModule.internalField0149.player.isOnGround() && bl && !ElytraStrafeModule.internalField0149.player.isInFluid() && !ElytraStrafeModule.internalField0149.player.isGliding()) {
                ElytraStrafeModule.internalField0149.player.jump();
            }
        }
        if (!ElytraStrafeModule.internalField0149.player.isGliding()) {
            return;
        }
        if (this.internalField0519.internalMethod02365((long)(this.internalField0382.internalMethod08576() * 1000.0f)) && !ElytraStrafeModule.internalField0149.player.isUsingItem()) {
            this.internalMethod09375();
        }
    }

    private void internalMethod09375() {
        SlotCollection<HotbarSlot> typedValue228 = InventorySlots.internalMethod02872();
        HotbarSlot typedValue231 = typedValue228.internalMethod02510(Items.FIREWORK_ROCKET);
        if (typedValue231 != null) {
            ElytraStrafeModule.internalField0149.player.networkHandler.sendPacket((Packet)new UpdateSelectedSlotC2SPacket(typedValue231.internalMethod08745()));
            ElytraStrafeModule.internalField0149.interactionManager.sendSequencedPacket(ElytraStrafeModule.internalField0149.world, n -> new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, n, ElytraStrafeModule.internalField0149.player.getYaw(), ElytraStrafeModule.internalField0149.player.getPitch()));
            ElytraStrafeModule.internalField0149.player.networkHandler.sendPacket((Packet)new UpdateSelectedSlotC2SPacket(ElytraStrafeModule.internalField0149.player.getInventory().getSelectedSlot()));
            this.internalField0519.internalMethod00701();
            return;
        }
        SlotCollection<MainInventorySlot> typedValue229 = InventorySlots.internalMethod03558();
        MainInventorySlot typedValue233 = typedValue229.internalMethod02510(Items.FIREWORK_ROCKET);
        if (typedValue233 != null) {
            InventoryUtils.internalMethod08821(typedValue233.internalMethod06662(), (int)(this.internalField0383.internalMethod08576() - 1.0f));
            this.internalField0519.internalMethod00701();
        }
    }
}
