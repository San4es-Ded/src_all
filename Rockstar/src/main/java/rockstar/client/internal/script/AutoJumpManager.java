package rockstar.client.internal.script;




import rockstar.client.rotation.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import lombok.Generated;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import pyrock.events.player.InputEvent;
import rockstar.client.RockstarClient;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.rotation.RotationBehavior;
import rockstar.client.rotation.Rotation;
import rockstar.client.rotation.RotationManager;
import rockstar.client.rotation.RotationPriority;
import rockstar.client.internal.core.JumpAngleOptimizer;
import rockstar.client.internal.core.JumpPhysicsModel;
import rockstar.client.internal.core.TerrainHeightSampler;

public final class AutoJumpManager
implements MinecraftClientAccess {
    private static final int internalField0227 = 11;
    private static final double internalField0194 = 2.0;
    private static final double internalField0193 = 2.5;
    private static final double internalField1045 = 24.0;
    private static final double internalField1043 = 32.0;
    private final JumpAngleOptimizer internalField0529 = new JumpAngleOptimizer(11);
    private final TerrainHeightSampler internalField0533 = new TerrainHeightSampler(24, 2.0);
    private final JumpPhysicsModel.InternalType0401 internalField0842 = new JumpPhysicsModel.InternalType0401();
    private boolean internalField0277;
    private boolean internalField0276;
    private boolean internalField1099;
    private boolean internalField1100;
    private boolean internalField1102;
    private int internalField1055;

    public boolean internalMethod07057(ClientPlayerEntity clientPlayerEntity) {
        return this.internalField1099 && clientPlayerEntity.input.hasForwardMovement() && (clientPlayerEntity.getHungerManager().getFoodLevel() > 6 || clientPlayerEntity.getAbilities().allowFlying);
    }

    public void internalMethod01829() {
        this.internalField1099 = false;
        this.internalField0277 = false;
        this.internalField0276 = false;
        ClientPlayerEntity clientPlayerEntity = AutoJumpManager.internalField0149.player;
        if (clientPlayerEntity == null || AutoJumpManager.internalField0149.world == null || clientPlayerEntity.networkHandler == null) {
            return;
        }
        if (!this.internalMethod04287(clientPlayerEntity)) {
            this.internalMethod08583();
            return;
        }
        boolean bl = clientPlayerEntity.isGliding();
        boolean bl2 = clientPlayerEntity.isOnGround();
        if (bl) {
            this.internalField1055 = 10;
        } else if (this.internalField1055 > 0) {
            --this.internalField1055;
        }
        if (!clientPlayerEntity.input.hasForwardMovement()) {
            this.internalMethod08583();
            return;
        }
        boolean bl3 = bl2 || !bl && this.internalMethod08269(clientPlayerEntity);
        boolean bl4 = this.internalField1102 ? this.internalField1100 : AutoJumpManager.internalField0149.options.jumpKey.isPressed();
        this.internalField1099 = true;
        this.internalField1102 = true;
        this.internalField0277 = bl3 && !bl4;
        this.internalField0276 = true;
        this.internalField1100 = this.internalField0277;
        if (bl || this.internalField1055 > 0) {
            this.internalMethod04893(clientPlayerEntity, bl, bl2);
        }
    }

    public void internalMethod02345(InputEvent inputEvent) {
        if (!this.internalField1099) {
            return;
        }
        inputEvent.setJump(this.internalField0277);
        inputEvent.setSprint(inputEvent.isSprint() || this.internalField0276);
    }

    public void internalMethod01834() {
        this.internalField1099 = false;
        this.internalField0277 = false;
        this.internalField0276 = false;
        this.internalField1055 = 0;
        this.internalMethod08583();
    }

    public void internalMethod08583() {
        this.internalField1100 = false;
        this.internalField1102 = false;
    }

    private void internalMethod04893(ClientPlayerEntity clientPlayerEntity, boolean bl, boolean bl2) {
        RotationManager typedValue269 = RockstarClient.getInstance().internalMethod02368();
        if (typedValue269 == null) {
            return;
        }
        float f = typedValue269.internalMethod01525() ? clientPlayerEntity.getPitch() : typedValue269.internalMethod09074().internalMethod00171();
        double d = Math.toRadians(clientPlayerEntity.getYaw());
        double d2 = -Math.sin(d);
        double d3 = Math.cos(d);
        this.internalMethod04891(clientPlayerEntity, d2, d3);
        this.internalMethod03646(clientPlayerEntity, bl, bl2, d2, d3);
        float f2 = this.internalField0529.internalMethod01373(this.internalField0842, f, 90.0f, this.internalField0533, this.internalMethod01828());
        typedValue269.internalMethod00418(new Rotation(clientPlayerEntity.getYaw(), MathHelper.clamp((float)f2, (float)-90.0f, (float)90.0f)), RotationBehavior.internalField1004, 180.0f, 180.0f, 180.0f, RotationPriority.internalField0122);
    }

    private void internalMethod03646(ClientPlayerEntity clientPlayerEntity, boolean bl, boolean bl2, double d, double d2) {
        Vec3d vec3d = clientPlayerEntity.getVelocity();
        this.internalField0842.internalField0194 = 0.0;
        this.internalField0842.internalField0193 = clientPlayerEntity.getY();
        this.internalField0842.internalField1045 = Math.max(0.0, vec3d.x * d + vec3d.z * d2);
        this.internalField0842.internalField1043 = vec3d.y;
        this.internalField0842.internalField0277 = bl;
        this.internalField0842.internalField0276 = bl2;
        this.internalField0842.internalField1099 = this.internalField1100;
        this.internalField0842.internalField0227 = -1;
    }

    private int internalMethod01828() {
        PlayerListEntry playerListEntry;
        int n = 0;
        if (internalField0149.getNetworkHandler() != null && AutoJumpManager.internalField0149.player != null && (playerListEntry = internalField0149.getNetworkHandler().getPlayerListEntry(AutoJumpManager.internalField0149.player.getUuid())) != null) {
            n = playerListEntry.getLatency();
        }
        return MathHelper.clamp((int)(1 + n / 50), (int)1, (int)6);
    }

    private void internalMethod04891(ClientPlayerEntity clientPlayerEntity, double d, double d2) {
        this.internalField0533.internalMethod01747();
        Vec3d vec3d = clientPlayerEntity.getEntityPos();
        for (int i = 0; i < this.internalField0533.internalMethod01746(); ++i) {
            double d3 = (double)i * 2.0;
            this.internalField0533.internalMethod00954(this.internalMethod00705(clientPlayerEntity, vec3d.x + d * d3, vec3d.y, vec3d.z + d2 * d3));
        }
    }

    private double internalMethod00705(ClientPlayerEntity clientPlayerEntity, double d, double d2, double d3) {
        double d4 = this.internalMethod02437(clientPlayerEntity, d, d2 + 2.5, d3, 32.0);
        if (!Double.isNaN(d4)) {
            return d4;
        }
        if (AutoJumpManager.internalField0149.world.getBlockState(BlockPos.ofFloored((double)d, (double)(d2 + 2.5), (double)d3)).isAir()) {
            return -4096.0;
        }
        double d5 = this.internalMethod02437(clientPlayerEntity, d, d2 + 24.0, d3, 21.5);
        return Double.isNaN(d5) ? d2 + 24.0 : d5;
    }

    private double internalMethod02437(ClientPlayerEntity clientPlayerEntity, double d, double d2, double d3, double d4) {
        Vec3d vec3d = new Vec3d(d, d2, d3);
        BlockHitResult blockHitResult = AutoJumpManager.internalField0149.world.raycast(new RaycastContext(vec3d, vec3d.add(0.0, -d4, 0.0), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.ANY, (Entity)clientPlayerEntity));
        return blockHitResult.getType() == HitResult.Type.MISS ? Double.NaN : blockHitResult.getPos().y;
    }

    private boolean internalMethod04287(ClientPlayerEntity clientPlayerEntity) {
        return AutoJumpManager.internalField0149.currentScreen == null && !clientPlayerEntity.isSpectator() && !clientPlayerEntity.hasVehicle() && !clientPlayerEntity.isClimbing() && !clientPlayerEntity.isTouchingWater() && !clientPlayerEntity.isInLava() && !clientPlayerEntity.getAbilities().flying && !this.internalMethod04940(clientPlayerEntity).isEmpty();
    }

    private boolean internalMethod08269(ClientPlayerEntity clientPlayerEntity) {
        return !clientPlayerEntity.isOnGround() && !clientPlayerEntity.isTouchingWater() && !this.internalMethod04940(clientPlayerEntity).isEmpty();
    }

    private ItemStack internalMethod04940(ClientPlayerEntity clientPlayerEntity) {
        for (EquipmentSlot equipmentSlot : EquipmentSlot.VALUES) {
            ItemStack itemStack = clientPlayerEntity.getEquippedStack(equipmentSlot);
            if (!LivingEntity.canGlideWith((ItemStack)itemStack, (EquipmentSlot)equipmentSlot)) continue;
            return itemStack;
        }
        return ItemStack.EMPTY;
    }

    @Generated
    public boolean internalMethod01830() {
        return this.internalField0277;
    }

    @Generated
    public boolean internalMethod01835() {
        return this.internalField0276;
    }

    @Generated
    public boolean internalMethod08584() {
        return this.internalField1099;
    }
}

