package rockstar.modules.other;





import rockstar.client.rotation.*;
import rockstar.client.module.*;
import rockstar.client.inventory.*;
import rockstar.client.event.*;
import rockstar.client.*;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldView;
import pyrock.events.player.ClientPlayerTickEvent;
import rockstar.client.event.EventListener;
import rockstar.client.module.ModuleCategory;
import rockstar.client.RockstarClient;
import rockstar.client.module.ModuleInfo;
import rockstar.client.inventory.InventoryUtils;
import rockstar.client.inventory.InventorySlots;
import rockstar.client.inventory.HotbarSlot;
import rockstar.client.inventory.OffhandSlot;
import rockstar.client.rotation.RotationBehavior;
import rockstar.client.rotation.Rotation;
import rockstar.client.rotation.RotationPriority;
import rockstar.client.module.Module;

@ModuleInfo(name="AntiAim", category=ModuleCategory.OTHER)
public class AntiAimModule
extends Module {
    private int internalField0227 = -1;
    private int internalField0228 = -1;
    private int internalField1053 = -1;
    private int internalField1055 = -1;
    private int internalField1056;
    private int internalField1054 = -1000;
    private final EventListener<ClientPlayerTickEvent> internalField0157 = clientPlayerTickEvent -> {
        if (AntiAimModule.internalField0149.player == null || AntiAimModule.internalField0149.world == null || AntiAimModule.internalField0149.interactionManager == null || AntiAimModule.internalField0149.player.isSpectator()) {
            return;
        }
        if (this.internalField1055 >= 0) {
            if (AntiAimModule.internalField0149.player.age >= this.internalField1055) {
                this.internalMethod09443();
            }
            return;
        }
        if (!this.internalMethod09444()) {
            this.internalMethod09443();
            return;
        }
        BlockPos blockPos = AntiAimModule.internalField0149.player.getBlockPos();
        if (!this.internalMethod03963(blockPos)) {
            this.internalField1056 = 0;
            this.internalMethod09443();
            return;
        }
        if (AntiAimModule.internalField0149.player.age - this.internalField1054 < 3) {
            return;
        }
        OffhandSlot typedValue234 = InventorySlots.internalMethod07766().internalMethod02510(Items.LILAC);
        if (typedValue234 != null) {
            this.internalMethod04091(blockPos, Hand.OFF_HAND);
            return;
        }
        HotbarSlot typedValue231 = InventorySlots.internalMethod02872().internalMethod02510(Items.LILAC);
        if (typedValue231 == null) {
            return;
        }
        if (!this.internalMethod02649(typedValue231)) {
            return;
        }
        this.internalMethod04091(blockPos, Hand.MAIN_HAND);
    };

    @Override
    public void onDisable() {
        this.internalMethod09443();
    }

    private boolean internalMethod09444() {
        for (PlayerEntity playerEntity : AntiAimModule.internalField0149.world.getPlayers()) {
            Vec3d vec3d;
            if (playerEntity == AntiAimModule.internalField0149.player || !playerEntity.isAlive() || playerEntity.isSpectator() || playerEntity.squaredDistanceTo((Entity)AntiAimModule.internalField0149.player) > 36.0 || playerEntity.isOnGround() || playerEntity.isSwimming() || playerEntity.isClimbing() || (vec3d = AntiAimModule.internalField0149.player.getBoundingBox().getCenter().subtract(playerEntity.getEyePos())).lengthSquared() == 0.0 || !(playerEntity.getRotationVec(1.0f).dotProduct(vec3d.normalize()) >= 0.9)) continue;
            return true;
        }
        return false;
    }

    private boolean internalMethod03963(BlockPos blockPos) {
        return AntiAimModule.internalField0149.world.getBlockState(blockPos).isAir() && AntiAimModule.internalField0149.world.getBlockState(blockPos.up()).isAir() && Blocks.LILAC.getDefaultState().canPlaceAt((WorldView)AntiAimModule.internalField0149.world, blockPos);
    }

    private boolean internalMethod02649(HotbarSlot typedValue231) {
        int n = AntiAimModule.internalField0149.player.getInventory().getSelectedSlot();
        if (this.internalField0228 != typedValue231.internalMethod08745() || n != typedValue231.internalMethod08745()) {
            if (this.internalField0227 == -1) {
                this.internalField0227 = n;
            }
            this.internalField0228 = typedValue231.internalMethod08745();
            this.internalField1053 = AntiAimModule.internalField0149.player.age;
            this.internalField1056 = 0;
            InventoryUtils.internalMethod03663(this.internalField0228);
            return false;
        }
        return AntiAimModule.internalField0149.player.age > this.internalField1053;
    }

    private void internalMethod04091(BlockPos blockPos, Hand hand) {
        BlockPos blockPos2 = blockPos.down();
        Vec3d vec3d = blockPos2.toCenterPos().add(0.0, 0.5, 0.0);
        Rotation typedValue266 = this.internalMethod02994(AntiAimModule.internalField0149.player.getEyePos(), vec3d);
        RockstarClient.getInstance().internalMethod02368().internalMethod00418(typedValue266, RotationBehavior.internalField1003, 45.0f, 45.0f, 45.0f, RotationPriority.internalField1012);
        Rotation typedValue267 = RockstarClient.getInstance().internalMethod02368().internalMethod09074();
        if (Math.abs(MathHelper.wrapDegrees((float)(typedValue267.internalMethod00169() - typedValue266.internalMethod00169()))) > 3.0f || Math.abs(typedValue267.internalMethod00171() - typedValue266.internalMethod00171()) > 3.0f) {
            this.internalField1056 = 0;
            return;
        }
        if (++this.internalField1056 < 2) {
            return;
        }
        AntiAimModule.internalField0149.interactionManager.interactBlock(AntiAimModule.internalField0149.player, hand, new BlockHitResult(vec3d, Direction.UP, blockPos2, false));
        AntiAimModule.internalField0149.player.swingHand(hand);
        this.internalField1054 = AntiAimModule.internalField0149.player.age;
        this.internalField1056 = 0;
        if (this.internalField0227 != -1) {
            this.internalField1055 = AntiAimModule.internalField0149.player.age + 1;
        }
    }

    private Rotation internalMethod02994(Vec3d vec3d, Vec3d vec3d2) {
        double d = vec3d2.x - vec3d.x;
        double d2 = vec3d2.y - vec3d.y;
        double d3 = vec3d2.z - vec3d.z;
        double d4 = Math.sqrt(d * d + d3 * d3);
        return new Rotation((float)Math.toDegrees(Math.atan2(d3, d)) - 90.0f, (float)(-Math.toDegrees(Math.atan2(d2, d4))));
    }

    private void internalMethod09443() {
        if (AntiAimModule.internalField0149.player != null && this.internalField0227 != -1 && AntiAimModule.internalField0149.player.getInventory().getSelectedSlot() != this.internalField0227) {
            InventoryUtils.internalMethod03663(this.internalField0227);
        }
        this.internalField0227 = -1;
        this.internalField0228 = -1;
        this.internalField1053 = -1;
        this.internalField1055 = -1;
        this.internalField1056 = 0;
    }
}
