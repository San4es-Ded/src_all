package rockstar.modules.movement;






import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.server.*;
import rockstar.client.module.*;
import rockstar.client.event.*;
import rockstar.client.*;

import net.minecraft.block.Blocks;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.consume.UseAction;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import pyrock.events.network.SendPacketEvent;
import pyrock.events.player.ClientPlayerTickEvent;
import pyrock.events.player.InputEvent;
import pyrock.events.player.SlowDownEvent;
import rockstar.client.setting.ModeSetting;
import rockstar.client.event.EventListener;
import rockstar.client.module.ModuleCategory;
import rockstar.client.module.ModuleInfo;
import rockstar.client.util.GameUtils;
import rockstar.client.server.KnownServer;
import rockstar.client.server.ServerUtils;
import rockstar.client.module.Module;

@ModuleInfo(name="No Slow", category=ModuleCategory.MOVEMENT)
public class NoSlowModule
extends Module {
    private int internalField0227;
    private ModeSetting internalField0668;
    private ModeSetting.InternalType0088 internalField0237;
    private ModeSetting.InternalType0088 internalField0238;
    private ModeSetting.InternalType0088 internalField1066;
    private ModeSetting.InternalType0088 internalField1067;
    private ModeSetting.InternalType0088 internalField1068;
    private ModeSetting.InternalType0088 internalField1065;
    private ModeSetting.InternalType0088 internalField1480;
    private int internalField0228 = -1;
    private boolean internalField0277;
    private int internalField1053;
    private final EventListener<SlowDownEvent> internalField0157 = slowDownEvent -> {
        int n2;
        if (NoSlowModule.internalField0149.player == null || NoSlowModule.internalField0149.world == null || NoSlowModule.internalField0149.interactionManager == null) {
            return;
        }
        if (this.internalField1067.isSelected() && (this.internalField1053 > 0 || this.internalMethod09391())) {
            this.internalMethod09233();
            slowDownEvent.cancel();
            return;
        }
        if (this.internalField1480.isSelected()) {
            this.internalMethod04878((SlowDownEvent)slowDownEvent);
            return;
        }
        if (this.internalMethod09234()) {
            this.internalField0227 = 0;
            this.internalMethod09390();
            return;
        }
        if (this.internalField1065.isSelected()) {
            if (this.internalMethod09232() || NoSlowModule.internalField0149.player.getMainHandStack().isOf(Items.CROSSBOW) || NoSlowModule.internalField0149.player.getMainHandStack().isOf(Items.MILK_BUCKET)) {
                NoSlowModule.internalField0149.player.setSprinting(true);
                slowDownEvent.cancel();
            }
            return;
        }
        if (this.internalField1066.isSelected() || this.internalField1068.isSelected()) {
            if (NoSlowModule.internalField0149.player.isGliding()) {
                return;
            }
            if (NoSlowModule.internalField0149.player.age % 2 == 0 && !NoSlowModule.internalField0149.player.isSneaking()) {
                slowDownEvent.cancel();
            }
            return;
        }
        this.internalMethod09392();
        if (NoSlowModule.internalField0149.player.getActiveHand() == Hand.MAIN_HAND && !this.internalField1067.isSelected() && !this.internalField0238.isSelected()) {
            NoSlowModule.internalField0149.interactionManager.sendSequencedPacket(NoSlowModule.internalField0149.world, n -> new PlayerInteractItemC2SPacket(Hand.OFF_HAND, n, NoSlowModule.internalField0149.player.getYaw(), NoSlowModule.internalField0149.player.getPitch()));
            slowDownEvent.cancel();
            return;
        }
        if (!this.internalField1067.isSelected() && !this.internalField0238.isSelected()) {
            NoSlowModule.internalField0149.interactionManager.sendSequencedPacket(NoSlowModule.internalField0149.world, n -> new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, n, NoSlowModule.internalField0149.player.getYaw(), NoSlowModule.internalField0149.player.getPitch()));
        } else {
            ++this.internalField0227;
        }
        boolean bl = NoSlowModule.internalField0149.player.getActiveItem().isOf(Items.GOLDEN_APPLE) || NoSlowModule.internalField0149.player.getActiveItem().isOf(Items.ENCHANTED_GOLDEN_APPLE);
        int n3 = n2 = bl ? 4 : 2;
        if (this.internalField0227 >= n2 && (!bl || !NoSlowModule.internalField0149.player.isOnGround()) || this.internalField0237.isSelected() || this.internalField0227 >= 2 && this.internalField0238.isSelected()) {
            slowDownEvent.cancel();
            this.internalField0227 = 0;
        }
    };
    private final EventListener<InputEvent> internalField0158 = inputEvent -> {
        if (this.internalField1065.isSelected() && NoSlowModule.internalField0149.player.isUsingItem() && this.internalMethod09232()) {
            inputEvent.setJump(false);
            if (NoSlowModule.internalField0149.player.hasStatusEffect(StatusEffects.SPEED)) {
                NoSlowModule.internalField0149.player.setVelocity(NoSlowModule.internalField0149.player.getVelocity().x * 0.71, NoSlowModule.internalField0149.player.getVelocity().y, NoSlowModule.internalField0149.player.getVelocity().z * 0.71);
            }
        }
    };
    private final EventListener<ClientPlayerTickEvent> internalField1028 = clientPlayerTickEvent -> {
        if (NoSlowModule.internalField0149.player == null || NoSlowModule.internalField0149.world == null || NoSlowModule.internalField0149.player.getItemCooldownManager() == null || internalField0149.getNetworkHandler() == null) {
            return;
        }
        if (this.internalField1053 > 0) {
            if (this.internalField1067.isSelected()) {
                this.internalMethod09233();
            }
            --this.internalField1053;
        }
        if (this.internalField1480.isSelected()) {
            this.internalMethod09407();
        }
        if (this.internalField0238.isSelected() && !this.internalMethod09234()) {
            this.internalMethod09392();
        } else {
            this.internalMethod09390();
        }
        if (!this.internalField0237.isSelected()) {
            return;
        }
        if (!NoSlowModule.internalField0149.player.getItemCooldownManager().isCoolingDown(NoSlowModule.internalField0149.player.getMainHandStack().getItem().getDefaultStack()) && !NoSlowModule.internalField0149.player.getItemCooldownManager().isCoolingDown(NoSlowModule.internalField0149.player.getOffHandStack().getItem().getDefaultStack()) && NoSlowModule.internalField0149.player.isUsingItem() && NoSlowModule.internalField0149.player.fallDistance < 1.0f && NoSlowModule.internalField0149.player.getActiveHand() == Hand.OFF_HAND) {
            internalField0149.getNetworkHandler().sendPacket((Packet)new UpdateSelectedSlotC2SPacket(NoSlowModule.internalField0149.player.getInventory().getSelectedSlot() % 8 + 1));
            internalField0149.getNetworkHandler().sendPacket((Packet)new UpdateSelectedSlotC2SPacket(NoSlowModule.internalField0149.player.getInventory().getSelectedSlot()));
        }
    };
    private final EventListener<SendPacketEvent> internalField1029 = sendPacketEvent -> {
        PlayerInteractItemC2SPacket playerInteractItemC2SPacket;
        if (NoSlowModule.internalField0149.player == null || NoSlowModule.internalField0149.world == null || !this.internalField1067.isSelected()) {
            return;
        }
        Packet<?> packet = sendPacketEvent.getPacket();
        if (packet instanceof PlayerInteractItemC2SPacket && this.internalMethod02480(this.internalMethod02967((playerInteractItemC2SPacket = (PlayerInteractItemC2SPacket)packet).getHand()))) {
            this.internalField1053 = 4;
            this.internalMethod09233();
        }
    };

    public NoSlowModule() {
        this.internalMethod09231();
    }

    private void internalMethod09231() {
        this.internalField0668 = new ModeSetting(this, "modules.settings.noslow.mode");
        this.internalField0237 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.noslow.grim");
        this.internalField0238 = new ModeSetting.InternalType0088(this.internalField0668, "GrimNew");
        this.internalField1066 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.noslow.grim_tick");
        this.internalField1067 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.noslow.spooky");
        this.internalField1068 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.noslow.holly");
        this.internalField1065 = new ModeSetting.InternalType0088(this.internalField0668, "VonTam");
        this.internalField1480 = new ModeSetting.InternalType0088(this.internalField0668, "LonyGrief");
    }

    private boolean internalMethod09232() {
        if (NoSlowModule.internalField0149.player == null || NoSlowModule.internalField0149.world == null) {
            return false;
        }
        if (GameUtils.internalMethod02397(0.0, -1.0, 0.0) == Blocks.SNOW || GameUtils.internalMethod02397(0.0, -1.0, 0.0) == Blocks.SHORT_GRASS) {
            return true;
        }
        return GameUtils.internalMethod02397(0.0, 0.0, 0.0) == Blocks.SNOW || GameUtils.internalMethod02397(0.0, 0.0, 0.0) == Blocks.SHORT_GRASS;
    }

    private boolean internalMethod09234() {
        return (NoSlowModule.internalField0149.player.getMainHandStack().getUseAction() == UseAction.BLOCK || NoSlowModule.internalField0149.player.getOffHandStack().getUseAction() == UseAction.EAT) && NoSlowModule.internalField0149.player.getActiveHand() == Hand.MAIN_HAND || !NoSlowModule.internalField0149.player.isUsingItem();
    }

    private boolean internalMethod02480(ItemStack itemStack) {
        if (itemStack == null || itemStack.isEmpty()) {
            return false;
        }
        return itemStack.isOf(Items.ENDER_EYE) || itemStack.isOf(Items.FIRE_CHARGE) || itemStack.isOf(Items.PHANTOM_MEMBRANE) || itemStack.isOf(Items.SUGAR) || itemStack.isOf(Items.SNOWBALL) || itemStack.isOf(Items.NETHER_STAR) || itemStack.isOf(Items.PRISMARINE_SHARD) || itemStack.isOf(Items.FIREWORK_STAR);
    }

    private boolean internalMethod09391() {
        return NoSlowModule.internalField0149.player.isUsingItem() && (this.internalMethod02480(NoSlowModule.internalField0149.player.getActiveItem()) || this.internalMethod02480(this.internalMethod02967(NoSlowModule.internalField0149.player.getActiveHand())));
    }

    private ItemStack internalMethod02967(Hand hand) {
        return hand == Hand.OFF_HAND ? NoSlowModule.internalField0149.player.getOffHandStack() : NoSlowModule.internalField0149.player.getMainHandStack();
    }

    private void internalMethod09233() {
        this.internalMethod09392();
        this.internalField0227 = 0;
    }

    private void internalMethod09390() {
        this.internalField0228 = -1;
        this.internalField0277 = false;
    }

    private void internalMethod09392() {
        if (!this.internalField0238.isSelected()) {
            this.internalMethod09390();
            NoSlowModule.internalField0149.player.setSprinting(!ServerUtils.internalMethod01786(KnownServer.internalField1220));
            return;
        }
        int n = NoSlowModule.internalField0149.player.age;
        if (this.internalField0228 != n) {
            this.internalField0277 = this.internalField0228 != -1 && !this.internalField0277;
            this.internalField0228 = n;
        }
        NoSlowModule.internalField0149.player.setSprinting(this.internalField0277);
    }

    private void internalMethod04878(SlowDownEvent slowDownEvent) {
        if (NoSlowModule.internalField0149.player.isGliding() || !NoSlowModule.internalField0149.player.isUsingItem()) {
            return;
        }
        if (NoSlowModule.internalField0149.player.getActiveHand() == Hand.OFF_HAND) {
            if (NoSlowModule.internalField0149.player.age % 2 == 0 && !NoSlowModule.internalField0149.player.isSneaking()) {
                slowDownEvent.cancel();
            }
            return;
        }
        if (NoSlowModule.internalField0149.player.getItemUseTime() > 0) {
            slowDownEvent.cancel();
        }
    }

    private void internalMethod09407() {
        if (NoSlowModule.internalField0149.player.networkHandler == null || NoSlowModule.internalField0149.player.isGliding()) {
            return;
        }
        if (NoSlowModule.internalField0149.player.isUsingItem() && NoSlowModule.internalField0149.player.getItemUseTime() == 0) {
            NoSlowModule.internalField0149.player.networkHandler.sendPacket((Packet)new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.DROP_ALL_ITEMS, BlockPos.ORIGIN, NoSlowModule.internalField0149.player.getHorizontalFacing()));
        }
    }

    @Override
    public void onDisable() {
        this.internalField0227 = 0;
        this.internalField1053 = 0;
        this.internalMethod09390();
    }
}
