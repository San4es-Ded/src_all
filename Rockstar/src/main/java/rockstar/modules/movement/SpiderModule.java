package rockstar.modules.movement;






import rockstar.client.setting.*;
import rockstar.client.rotation.*;
import rockstar.client.module.*;
import rockstar.client.inventory.*;
import rockstar.client.event.*;
import rockstar.client.*;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import pyrock.events.player.ClientPlayerTickEvent;
import pyrock.events.player.EventMotion;
import pyrock.events.player.InputEvent;
import rockstar.client.setting.ModeSetting;
import rockstar.client.event.EventListener;
import rockstar.client.module.ModuleCategory;
import rockstar.client.RockstarClient;
import rockstar.client.module.ModuleInfo;
import rockstar.client.inventory.InventoryUtils;
import rockstar.client.inventory.InventorySlot;
import rockstar.client.inventory.SlotCollection;
import rockstar.client.inventory.InventorySlots;
import rockstar.client.inventory.OffhandSlot;
import rockstar.client.rotation.RotationBehavior;
import rockstar.client.rotation.Rotation;
import rockstar.client.rotation.RotationPriority;
import rockstar.client.module.Module;

@ModuleInfo(name="Spider", category=ModuleCategory.MOVEMENT, internalMethod09633="modules.descriptions.spider")
public class SpiderModule
extends Module {
    private ModeSetting internalField0668;
    private ModeSetting.InternalType0088 internalField0237;
    private ModeSetting.InternalType0088 internalField0238;
    private ModeSetting.InternalType0088 internalField1066;
    private ModeSetting.InternalType0088 internalField1067;
    private int internalField0227 = -1;
    private int internalField0228 = -1;
    private int internalField1053 = -1;
    private int internalField1055 = -1;
    private Hand internalField0050 = Hand.MAIN_HAND;
    private boolean internalField0277;
    private boolean internalField0276;
    private final EventListener<EventMotion> internalField0157 = eventMotion -> {
        if (this.internalField0238.isSelected()) {
            if (!SpiderModule.internalField0149.player.horizontalCollision || !this.internalMethod09395()) {
                return;
            }
            eventMotion.setGround(true);
            SpiderModule.internalField0149.player.setOnGround(true);
        }
    };
    private final EventListener<ClientPlayerTickEvent> internalField0158 = clientPlayerTickEvent -> {
        if (SpiderModule.internalField0149.player == null || SpiderModule.internalField0149.world == null) {
            return;
        }
        if (!this.internalField1066.isSelected() && this.internalField0277) {
            this.internalMethod09237();
        }
        if (!this.internalField1067.isSelected() && this.internalField0276) {
            this.internalMethod09394();
        }
        if (this.internalField0237.isSelected()) {
            if (!SpiderModule.internalField0149.player.horizontalCollision) {
                return;
            }
            SpiderModule.internalField0149.player.lastY -= 2.0E-232;
            if (SpiderModule.internalField0149.player.isOnGround()) {
                SpiderModule.internalField0149.player.setVelocity(SpiderModule.internalField0149.player.getVelocity().getX(), 0.42, SpiderModule.internalField0149.player.getVelocity().getZ());
            }
        } else if (this.internalField0238.isSelected()) {
            if (!SpiderModule.internalField0149.player.horizontalCollision || !this.internalMethod09395()) {
                return;
            }
            SpiderModule.internalField0149.player.lastY -= 2.0E-232;
            if (SpiderModule.internalField0149.player.isOnGround()) {
                SpiderModule.internalField0149.player.setVelocity(SpiderModule.internalField0149.player.getVelocity().getX(), 0.42, SpiderModule.internalField0149.player.getVelocity().getZ());
            }
        } else if (this.internalField1066.isSelected()) {
            if (!this.internalField0277 && !this.internalMethod09236()) {
                return;
            }
            if (!SpiderModule.internalField0149.player.horizontalCollision) {
                return;
            }
            if (this.internalField0228 >= 0 && this.internalField0228 <= 8) {
                SpiderModule.internalField0149.player.getInventory().setSelectedSlot(this.internalField0228);
            }
            SpiderModule.internalField0149.player.setPitch(75.0f);
            if (SpiderModule.internalField0149.player.age % 3 == 0) {
                internalField0149.doItemUse();
            }
        } else if (this.internalField1067.isSelected()) {
            if (!this.internalField0276 && !this.internalMethod09238()) {
                return;
            }
            if (this.internalField0050 == Hand.MAIN_HAND && this.internalField1055 >= 0 && this.internalField1055 <= 8) {
                SpiderModule.internalField0149.player.getInventory().setSelectedSlot(this.internalField1055);
            }
            if (SpiderModule.internalField0149.player.horizontalCollision) {
                RockstarClient.getInstance().internalMethod02368().internalMethod00418(new Rotation((double)SpiderModule.internalField0149.player.getYaw(), 78.5), RotationBehavior.internalField0114, 180.0f, 180.0f, 180.0f, RotationPriority.internalField1012);
                SpiderModule.internalField0149.interactionManager.interactItem((PlayerEntity)SpiderModule.internalField0149.player, this.internalField0050);
                internalField0149.doItemUse();
            }
        }
    };
    private final EventListener<InputEvent> internalField1028 = inputEvent -> {
        if (SpiderModule.internalField0149.player == null) {
            return;
        }
        if (this.internalField1066.isSelected() && SpiderModule.internalField0149.player.horizontalCollision) {
            inputEvent.setJump(true);
            return;
        }
        if (this.internalField1067.isSelected() && SpiderModule.internalField0149.player.horizontalCollision) {
            inputEvent.setJump(true);
        }
    };

    public SpiderModule() {
        this.internalMethod09235();
    }

    private void internalMethod09235() {
        this.internalField0668 = new ModeSetting(this, "modules.settings.spider.mode");
        this.internalField0237 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.flight.vanilla");
        this.internalField0238 = new ModeSetting.InternalType0088(this.internalField0668, "FunTime");
        this.internalField1066 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.spider.mode.water");
        this.internalField1067 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.spider.mode.sphere");
    }

    @Override
    public void onEnable() {
        if (SpiderModule.internalField0149.player == null) {
            return;
        }
        if (this.internalField1066.isSelected()) {
            this.internalMethod09236();
        } else if (this.internalField1067.isSelected()) {
            this.internalMethod09238();
        }
    }

    @Override
    public void onDisable() {
        this.internalMethod09237();
        this.internalMethod09394();
        this.internalField0227 = -1;
        this.internalField0228 = -1;
        this.internalField1053 = -1;
        this.internalField1055 = -1;
        this.internalField0050 = Hand.MAIN_HAND;
        this.internalField0277 = false;
        this.internalField0276 = false;
        SpiderModule.internalField0149.options.useKey.setPressed(false);
    }

    private boolean internalMethod09236() {
        if (this.internalField0277) {
            return true;
        }
        if (SpiderModule.internalField0149.player == null) {
            return false;
        }
        SlotCollection<InventorySlot> typedValue228 = InventorySlots.internalMethod03558().internalMethod07591(InventorySlots.internalMethod02872());
        InventorySlot typedValue222 = typedValue228.internalMethod02510(Items.WATER_BUCKET);
        if (typedValue222 == null || typedValue222.internalMethod06664()) {
            this.setEnabled(false, true);
            return false;
        }
        this.internalField0228 = SpiderModule.internalField0149.player.getInventory().getSelectedSlot();
        if (this.internalField0228 < 0 || this.internalField0228 > 8) {
            SpiderModule.internalField0149.player.getInventory().setSelectedSlot(this.internalField0228 = 0);
        }
        this.internalField0227 = typedValue222.internalMethod06662();
        InventoryUtils.internalMethod08821(this.internalField0227, this.internalField0228);
        this.internalField0277 = true;
        return true;
    }

    private void internalMethod09237() {
        if (!this.internalField0277) {
            this.internalField0227 = -1;
            this.internalField0228 = -1;
            return;
        }
        if (SpiderModule.internalField0149.player != null && this.internalField0227 != -1 && this.internalField0228 >= 0 && this.internalField0228 <= 8) {
            InventoryUtils.internalMethod08821(this.internalField0227, this.internalField0228);
            SpiderModule.internalField0149.player.getInventory().setSelectedSlot(this.internalField0228);
        }
        this.internalField0227 = -1;
        this.internalField0228 = -1;
        this.internalField0277 = false;
    }

    private boolean internalMethod09238() {
        if (this.internalField0276) {
            return true;
        }
        if (SpiderModule.internalField0149.player == null) {
            return false;
        }
        SlotCollection<InventorySlot> typedValue228 = InventorySlots.internalMethod07766().internalMethod07591(InventorySlots.internalMethod03558()).internalMethod07591(InventorySlots.internalMethod02872());
        InventorySlot typedValue222 = typedValue228.internalMethod02510(Items.PLAYER_HEAD);
        if (typedValue222 == null || typedValue222.internalMethod06664()) {
            this.setEnabled(false, true);
            return false;
        }
        if (typedValue222 instanceof OffhandSlot) {
            this.internalField0050 = Hand.OFF_HAND;
            this.internalField1053 = -1;
            this.internalField1055 = -1;
        } else {
            this.internalField0050 = Hand.MAIN_HAND;
            this.internalField1055 = SpiderModule.internalField0149.player.getInventory().getSelectedSlot();
            if (this.internalField1055 < 0 || this.internalField1055 > 8) {
                SpiderModule.internalField0149.player.getInventory().setSelectedSlot(this.internalField1055 = 0);
            }
            this.internalField1053 = typedValue222.internalMethod06662();
            InventoryUtils.internalMethod08821(this.internalField1053, this.internalField1055);
        }
        this.internalField0276 = true;
        return true;
    }

    private void internalMethod09394() {
        if (!this.internalField0276) {
            this.internalField1053 = -1;
            this.internalField1055 = -1;
            this.internalField0050 = Hand.MAIN_HAND;
            return;
        }
        if (SpiderModule.internalField0149.player != null && this.internalField0050 == Hand.MAIN_HAND && this.internalField1053 != -1 && this.internalField1055 >= 0 && this.internalField1055 <= 8) {
            InventoryUtils.internalMethod08821(this.internalField1053, this.internalField1055);
            SpiderModule.internalField0149.player.getInventory().setSelectedSlot(this.internalField1055);
        }
        this.internalField1053 = -1;
        this.internalField1055 = -1;
        this.internalField0050 = Hand.MAIN_HAND;
        this.internalField0276 = false;
    }

    private boolean internalMethod09395() {
        if (SpiderModule.internalField0149.world == null || SpiderModule.internalField0149.player == null) {
            return false;
        }
        Box box = SpiderModule.internalField0149.player.getBoundingBox();
        double d = Math.max((double)SpiderModule.internalField0149.player.getWidth() * 0.15, 0.03);
        Box box2 = box.expand(d, 0.0, d);
        BlockPos blockPos = BlockPos.ofFloored((double)box2.minX, (double)box.minY, (double)box2.minZ);
        BlockPos blockPos2 = BlockPos.ofFloored((double)box2.maxX, (double)box.maxY, (double)box2.maxZ);
        for (BlockPos blockPos3 : BlockPos.iterate((BlockPos)blockPos, (BlockPos)blockPos2)) {
            BlockState blockState = SpiderModule.internalField0149.world.getBlockState(blockPos3);
            if (!this.internalMethod00659(blockState, blockPos3, box, box2)) continue;
            return true;
        }
        return false;
    }

    private boolean internalMethod00659(BlockState blockState, BlockPos blockPos, Box box, Box box2) {
        if (blockState.isAir()) {
            return false;
        }
        VoxelShape voxelShape = blockState.getCollisionShape((BlockView)SpiderModule.internalField0149.world, blockPos);
        if (voxelShape.isEmpty() || !VoxelShapes.matchesAnywhere((VoxelShape)voxelShape, (VoxelShape)VoxelShapes.fullCube(), (BooleanBiFunction)BooleanBiFunction.NOT_SAME)) {
            return false;
        }
        for (Box box3 : voxelShape.getBoundingBoxes()) {
            Box box4 = box3.offset(blockPos);
            if (!box4.intersects(box2) || !(box4.maxY > box.minY) || !(box4.minY < box.maxY)) continue;
            return true;
        }
        return false;
    }
}
