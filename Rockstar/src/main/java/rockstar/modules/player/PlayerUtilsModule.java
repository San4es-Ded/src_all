package rockstar.modules.player;







import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.module.*;
import rockstar.client.inventory.*;
import rockstar.client.i18n.*;
import rockstar.client.event.*;
import rockstar.client.*;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import pyrock.events.game.BlockBreakEvent;
import pyrock.events.game.InternalAttackEvent;
import pyrock.events.game.StartBreakBlockEvent;
import pyrock.events.player.InputEvent;
import rockstar.client.setting.SettingOwner;
import rockstar.client.setting.ModeSetting;
import rockstar.client.setting.MultiSelectSetting;
import rockstar.client.setting.SliderSetting;
import rockstar.client.event.EventListener;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.module.ModuleCategory;
import rockstar.client.RockstarClient;
import rockstar.client.module.ModuleInfo;
import rockstar.client.util.GameUtils;
import rockstar.client.inventory.InventoryUtils;
import rockstar.client.inventory.InventorySlot;
import rockstar.client.inventory.SlotCollection;
import rockstar.client.inventory.InventorySlots;
import rockstar.client.inventory.HotbarSlot;
import rockstar.client.inventory.MainInventorySlot;
import rockstar.client.util.MathUtils;
import rockstar.client.module.Module;
import rockstar.modules.combat.AuraModule;
import rockstar.client.util.Stopwatch;

@ModuleInfo(name="Player Utils", category=ModuleCategory.PLAYER, internalMethod09633="modules.descriptions.player_utils")
public class PlayerUtilsModule
extends Module {
    private MultiSelectSetting internalField0675;
    private MultiSelectSetting.InternalType0091 internalField0245;
    private MultiSelectSetting.InternalType0091 internalField0244;
    private MultiSelectSetting.InternalType0091 internalField1075;
    private MultiSelectSetting.InternalType0091 internalField1074;
    private MultiSelectSetting.InternalType0091 internalField1073;
    private MultiSelectSetting.InternalType0091 internalField1072;
    private MultiSelectSetting.InternalType0091 internalField1491;
    private MultiSelectSetting.InternalType0091 internalField1488;
    private ModeSetting internalField0668;
    private ModeSetting.InternalType0088 internalField0237;
    private ModeSetting.InternalType0088 internalField0238;
    private ModeSetting.InternalType0088 internalField1066;
    private SliderSetting internalField0383;
    private final Stopwatch internalField0519 = new Stopwatch();
    private final Stopwatch internalField0518 = new Stopwatch();
    private boolean internalField0277;
    private boolean internalField0276;
    private boolean internalField1099;
    private boolean internalField1100;
    private boolean internalField1102;
    private int internalField0227 = -1;
    private int internalField0228 = -1;
    private InventorySlot internalField0022;
    private long internalField0229 = -1L;
    private long internalField0230;
    private int internalField1053 = -1;
    private final EventListener<InternalAttackEvent> internalField0157 = internalAttackEvent -> {
        if (this.internalField1074.isSelected() && internalAttackEvent.getEntity() instanceof PlayerEntity && RockstarClient.getInstance().internalMethod03375().internalMethod00380(internalAttackEvent.getEntity().getName().getString())) {
            internalAttackEvent.cancel();
        }
    };
    private final EventListener<StartBreakBlockEvent> internalField0158 = startBreakBlockEvent -> {
        InventorySlot typedValue222;
        if (!this.internalField1488.isSelected() || PlayerUtilsModule.internalField0149.player == null || PlayerUtilsModule.internalField0149.world == null) {
            return;
        }
        BlockState blockState = PlayerUtilsModule.internalField0149.world.getBlockState(startBreakBlockEvent.getBlockPos());
        if (blockState.isAir()) {
            return;
        }
        ItemStack itemStack = PlayerUtilsModule.internalField0149.player.getMainHandStack();
        float f = this.internalMethod03077(itemStack, blockState);
        InventorySlot typedValue223 = this.internalMethod07342(blockState, f);
        HotbarSlot typedValue231 = InventoryUtils.internalMethod06160();
        if (typedValue223 == null || typedValue223.equals(typedValue231)) {
            return;
        }
        if (this.internalField1100) {
            this.internalMethod09870();
        }
        this.internalField0227 = PlayerUtilsModule.internalField0149.player.getInventory().getSelectedSlot();
        if (this.internalField1100) {
            typedValue222 = this.internalMethod07342(blockState, f);
            if (typedValue222 == null || typedValue222.equals(typedValue231)) {
                return;
            }
            this.internalMethod09870();
        }
        if (typedValue223 instanceof HotbarSlot) {
            typedValue222 = (HotbarSlot)typedValue223;
            if (((HotbarSlot)typedValue222).internalMethod08745() == this.internalField0227) {
                return;
            }
            this.internalField0228 = ((HotbarSlot)typedValue222).internalMethod08745();
            InventoryUtils.internalMethod01980((HotbarSlot)typedValue222);
            this.internalField1102 = false;
            this.internalField1100 = true;
            return;
        }
        if (typedValue223 instanceof MainInventorySlot) {
            this.internalField0228 = this.internalField0227;
            this.internalField0022 = typedValue223;
            InventoryUtils.internalMethod08821(typedValue223.internalMethod06662(), this.internalField0228);
            this.internalField1102 = true;
            this.internalField1100 = true;
        }
    };
    private final EventListener<BlockBreakEvent> internalField1028 = blockBreakEvent -> {
        if (!this.internalField1488.isSelected()) {
            return;
        }
        if (this.internalField1100 && PlayerUtilsModule.internalField0149.player != null && !PlayerUtilsModule.internalField0149.options.attackKey.isPressed()) {
            this.internalMethod09870();
        }
    };
    private final EventListener<InputEvent> internalField1029 = inputEvent -> {
        if (this.internalField1491.isSelected() && this.internalField1099 && this.internalField0238.isSelected() && PlayerUtilsModule.internalField0149.player.isOnGround() && (float)PlayerUtilsModule.internalField0149.player.age % this.internalField0383.internalMethod08576() == 5.0f) {
            inputEvent.setJump(true);
        }
    };

    public PlayerUtilsModule() {
        this.internalMethod09674();
    }

    private void internalMethod09674() {
        this.internalField0675 = new MultiSelectSetting(this, "modules.settings.player_utils.select_setting").internalMethod03035(1);
        this.internalField0245 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.auto_respawn");
        this.internalField0244 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.auto_fish");
        this.internalField1075 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.fast_ladder");
        this.internalField1074 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.no_friend_damage");
        this.internalField1073 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.fast_break");
        this.internalField1072 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.player_utils.block_trap");
        this.internalField1491 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.anti_afk");
        this.internalField1488 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.auto_tool");
        this.internalField0668 = new ModeSetting((SettingOwner)this, "modules.settings.anti_afk.mode", () -> !this.internalField1491.isSelected());
        this.internalField0237 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.anti_afk.send");
        this.internalField0238 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.anti_afk.jump");
        this.internalField1066 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.anti_afk.swing");
        this.internalField0383 = new SliderSetting(this, "modules.settings.anti_afk.delay", "modules.settings.anti_afk.delay.description", () -> !this.internalField1491.isSelected()).internalMethod05900(5.0f).internalMethod02732(60.0f).internalMethod08673(5.0f).internalMethod08074(50.0f);
    }

    @Override
    public void internalMethod08229() {
        SlotCollection<InventorySlot> typedValue228;
        InventorySlot typedValue222;
        LivingEntity livingEntity;
        if (this.internalField1100 && (PlayerUtilsModule.internalField0149.player == null || !PlayerUtilsModule.internalField0149.options.attackKey.isPressed())) {
            this.internalMethod09870();
        }
        if (this.internalField1073.isSelected()) {
            PlayerUtilsModule.internalField0149.interactionManager.blockBreakingCooldown = 0;
            if (PlayerUtilsModule.internalField0149.interactionManager.getBlockBreakingProgress() > 1) {
                PlayerUtilsModule.internalField0149.interactionManager.blockBreakingCooldown = 1;
            }
        }
        if (this.internalField1491.isSelected()) {
            if (this.internalField0519.internalMethod02365(10000L)) {
                this.internalField1099 = true;
            }
            if (GameUtils.internalMethod00469()) {
                this.internalField1099 = false;
                this.internalField0519.internalMethod00701();
            }
            if (this.internalField1099 && (float)PlayerUtilsModule.internalField0149.player.age % this.internalField0383.internalMethod08576() == 5.0f) {
                if (this.internalField0237.isSelected()) {
                    PlayerUtilsModule.internalField0149.player.networkHandler.sendChatMessage(LanguageManager.internalMethod00160("player_utils.hello_message", String.valueOf(Math.random())));
                    this.internalField1099 = false;
                    this.internalField0519.internalMethod00701();
                } else if (this.internalField1066.isSelected()) {
                    PlayerUtilsModule.internalField0149.player.swingHand(Hand.MAIN_HAND);
                    this.internalField1099 = false;
                    this.internalField0519.internalMethod00701();
                }
            }
        }
        if (this.internalField0245.isSelected()) {
            if (PlayerUtilsModule.internalField0149.currentScreen instanceof DeathScreen) {
                if (this.internalField1053 == -1 && !PlayerUtilsModule.internalField0149.player.isAlive()) {
                    this.internalField1053 = 0;
                }
                ++this.internalField1053;
                int n = 20;
                if (this.internalField1053 >= n) {
                    PlayerUtilsModule.internalField0149.player.requestRespawn();
                    internalField0149.setScreen(null);
                    this.internalField1053 = -1;
                }
            } else {
                this.internalField1053 = -1;
            }
        }
        if (this.internalField0244.isSelected() && PlayerUtilsModule.internalField0149.player.getMainHandStack().getItem() instanceof FishingRodItem) {
            if (PlayerUtilsModule.internalField0149.player.fishHook != null) {
                if (((Boolean)PlayerUtilsModule.internalField0149.player.fishHook.getDataTracker().get(FishingBobberEntity.CAUGHT_FISH)).booleanValue() && this.internalField0229 == -1L) {
                    this.internalField0229 = System.currentTimeMillis();
                    this.internalField0230 = (long)(180.0f + MathUtils.internalMethod07919(0.0f, 220.0f) + (float)internalField0149.getNetworkHandler().getPlayerListEntry(PlayerUtilsModule.internalField0149.player.getUuid()).getLatency() / 2.0f);
                }
                if (this.internalField0229 != -1L && System.currentTimeMillis() - this.internalField0229 >= this.internalField0230) {
                    this.internalMethod09675();
                    this.internalField0229 = -1L;
                    this.internalField0277 = true;
                    this.internalField0518.internalMethod00701();
                }
            } else {
                if (this.internalField0277 && this.internalField0518.internalMethod02365((long)(600.0f + MathUtils.internalMethod07919(20.0f, 70.0f)))) {
                    this.internalMethod09675();
                    this.internalField0277 = false;
                    this.internalField0276 = false;
                    this.internalField0518.internalMethod00701();
                } else if (!this.internalField0277 && this.internalField0276 && this.internalField0518.internalMethod02365((long)(3000.0f + MathUtils.internalMethod07919(40.0f, 110.0f)))) {
                    this.internalMethod09675();
                    this.internalField0276 = false;
                    this.internalField0518.internalMethod00701();
                }
                this.internalField0229 = -1L;
            }
        }
        if (PlayerUtilsModule.internalField0149.player != null && PlayerUtilsModule.internalField0149.world.getBlockState(PlayerUtilsModule.internalField0149.player.getBlockPos()).isOf(Blocks.LADDER) && this.internalField1075.isSelected()) {
            PlayerUtilsModule.internalField0149.player.setVelocity(PlayerUtilsModule.internalField0149.player.getVelocity().multiply(1.0, 1.43, 1.0));
        }
        if (this.internalField1072.isSelected() && (livingEntity = RockstarClient.getInstance().internalMethod04463().internalMethod01783()) != null && PlayerUtilsModule.internalField0149.player.getEntityPos().distanceTo(livingEntity.getEntityPos()) <= (double)RockstarClient.getInstance().getModuleManager().getModule(AuraModule.class).internalMethod05151().internalMethod08576() && (typedValue222 = (typedValue228 = InventorySlots.internalMethod03558().internalMethod07591(InventorySlots.internalMethod02872())).internalMethod03297(itemStack -> itemStack.getItem() instanceof BlockItem)) != null) {
            BlockPos[] blockPosArray = this.internalMethod01337(livingEntity);
            this.internalMethod04359(typedValue222.internalMethod06662(), blockPosArray);
        }
        super.internalMethod08229();
    }

    private void internalMethod09675() {
        PlayerUtilsModule.internalField0149.interactionManager.interactItem((PlayerEntity)PlayerUtilsModule.internalField0149.player, Hand.MAIN_HAND);
        PlayerUtilsModule.internalField0149.player.swingHand(Hand.MAIN_HAND);
    }

    private InventorySlot internalMethod07342(BlockState blockState, float f) {
        SlotCollection<InventorySlot> typedValue228 = InventorySlots.internalMethod02872().internalMethod07591(InventorySlots.internalMethod03558());
        InventorySlot typedValue222 = null;
        float f2 = f;
        for (InventorySlot typedValue223 : typedValue228.internalMethod02638()) {
            float f3;
            ItemStack itemStack = typedValue223.internalMethod03427();
            if (itemStack == null || itemStack.isEmpty() || !((f3 = this.internalMethod03077(itemStack, blockState)) > f2)) continue;
            f2 = f3;
            typedValue222 = typedValue223;
        }
        if (typedValue222 == null || f2 <= 1.0f || f2 <= f) {
            return null;
        }
        return typedValue222;
    }

    private float internalMethod03077(ItemStack itemStack, BlockState blockState) {
        if (itemStack == null || itemStack.isEmpty()) {
            return 0.0f;
        }
        return itemStack.getMiningSpeedMultiplier(blockState);
    }

    private void internalMethod09870() {
        if (!this.internalField1100 || PlayerUtilsModule.internalField0149.player == null) {
            return;
        }
        if (this.internalField1102 && this.internalField0022 != null && this.internalField0228 != -1) {
            InventoryUtils.internalMethod08821(this.internalField0022.internalMethod06662(), this.internalField0228);
        }
        if (this.internalField0227 != -1) {
            InventoryUtils.internalMethod03663(this.internalField0227);
        }
        this.internalField1100 = false;
        this.internalField1102 = false;
        this.internalField0227 = -1;
        this.internalField0228 = -1;
        this.internalField0022 = null;
    }

    private void internalMethod04359(int n2, BlockPos[] blockPosArray) {
        for (BlockPos blockPos : blockPosArray) {
            BlockHitResult blockHitResult;
            if (!PlayerUtilsModule.internalField0149.world.isAir(blockPos)) continue;
            if (n2 == 40) {
                blockHitResult = new BlockHitResult(blockPos.down().toCenterPos(), Direction.UP, blockPos.down(), false);
                PlayerUtilsModule.internalField0149.interactionManager.sendSequencedPacket(PlayerUtilsModule.internalField0149.world, n -> new PlayerInteractBlockC2SPacket(Hand.OFF_HAND, blockHitResult, n));
                continue;
            }
            if (n2 >= 36) {
                PlayerUtilsModule.internalField0149.player.networkHandler.sendPacket((Packet)new UpdateSelectedSlotC2SPacket(n2 - 36));
                blockHitResult = new BlockHitResult(blockPos.down().toCenterPos(), Direction.UP, blockPos.down(), false);
                PlayerUtilsModule.internalField0149.interactionManager.sendSequencedPacket(PlayerUtilsModule.internalField0149.world, n -> new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, blockHitResult, n));
                PlayerUtilsModule.internalField0149.player.networkHandler.sendPacket((Packet)new UpdateSelectedSlotC2SPacket(PlayerUtilsModule.internalField0149.player.getInventory().getSelectedSlot()));
                continue;
            }
            PlayerUtilsModule.internalField0149.interactionManager.clickSlot(PlayerUtilsModule.internalField0149.player.currentScreenHandler.syncId, n2, 0, SlotActionType.PICKUP, (PlayerEntity)PlayerUtilsModule.internalField0149.player);
            PlayerUtilsModule.internalField0149.interactionManager.clickSlot(PlayerUtilsModule.internalField0149.player.currentScreenHandler.syncId, 44, 0, SlotActionType.PICKUP, (PlayerEntity)PlayerUtilsModule.internalField0149.player);
        }
    }

    private BlockPos @NotNull [] internalMethod01337(LivingEntity livingEntity) {
        BlockPos blockPos = livingEntity.getBlockPos();
        return new BlockPos[]{blockPos.add(1, 0, 0), blockPos.add(-1, 0, 0), blockPos.add(0, 0, 1), blockPos.add(0, 0, -1), blockPos.add(0, 3, 0), blockPos.add(1, 2, 0), blockPos.add(-1, 2, 0), blockPos.add(0, 2, 1), blockPos.add(0, 2, -1)};
    }

    @Override
    public void onDisable() {
        this.internalField0277 = false;
    }
}
