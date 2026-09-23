package rockstar.modules.player;








import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.rotation.*;
import rockstar.client.module.*;
import rockstar.client.inventory.*;
import rockstar.client.event.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.item.Items;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import pyrock.events.player.ClientPlayerTickEvent;
import rockstar.client.setting.BooleanSetting;
import rockstar.client.setting.SliderSetting;
import rockstar.client.internal.core.ItemCooldownHolder;
import rockstar.client.event.EventListener;
import rockstar.client.module.ModuleCategory;
import rockstar.client.RockstarClient;
import rockstar.client.module.ModuleInfo;
import rockstar.client.inventory.InventorySlot;
import rockstar.client.inventory.SlotCollection;
import rockstar.client.inventory.InventorySlots;
import rockstar.client.rotation.RotationBehavior;
import rockstar.client.rotation.Rotation;
import rockstar.client.rotation.RotationManager;
import rockstar.client.rotation.RotationPriority;
import rockstar.client.module.Module;
import rockstar.client.util.Stopwatch;

@ModuleInfo(name="Target Pearl", category=ModuleCategory.PLAYER, internalMethod09633="modules.descriptions.target_pearl")
public class TargetPearlModule
extends Module {
    private SliderSetting internalField0383;
    private SliderSetting internalField0382;
    private SliderSetting internalField1142;
    private SliderSetting internalField1140;
    private SliderSetting internalField1141;
    private SliderSetting internalField1143;
    private BooleanSetting internalField0650;
    private BooleanSetting internalField0651;
    private final Stopwatch internalField0519 = new Stopwatch();
    private Vec3d internalField0283;
    private Rotation internalField0118;
    private final EventListener<ClientPlayerTickEvent> internalField0157 = clientPlayerTickEvent -> {
        this.internalField0283 = null;
        this.internalField0118 = null;
        if (TargetPearlModule.internalField0149.player == null || TargetPearlModule.internalField0149.world == null) {
            return;
        }
        if (TargetPearlModule.internalField0149.currentScreen != null) {
            return;
        }
        if (TargetPearlModule.internalField0149.player.isUsingItem()) {
            return;
        }
        if (ItemCooldownHolder.internalField0006.internalMethod03576()) {
            return;
        }
        if (this.internalField0651.internalMethod04496() && !TargetPearlModule.internalField0149.player.getMainHandStack().isOf(Items.ENDER_PEARL) && !TargetPearlModule.internalField0149.player.getOffHandStack().isOf(Items.ENDER_PEARL)) {
            return;
        }
        if (!this.internalMethod09654()) {
            return;
        }
        EnderPearlEntity enderPearlEntity = this.internalMethod01110();
        if (enderPearlEntity == null) {
            return;
        }
        Vec3d vec3d = this.internalMethod07119(enderPearlEntity);
        if (vec3d == null) {
            return;
        }
        double d = TargetPearlModule.internalField0149.player.getEyePos().distanceTo(vec3d);
        if (d < (double)this.internalField0382.internalMethod08576() || d > (double)this.internalField1142.internalMethod08576()) {
            return;
        }
        Rotation typedValue266 = this.internalMethod05898(vec3d);
        if (typedValue266 == null) {
            return;
        }
        this.internalField0283 = vec3d;
        this.internalField0118 = typedValue266;
        float f = this.internalField1140.internalMethod08576();
        RockstarClient.getInstance().internalMethod02368().internalMethod00418(typedValue266, RotationBehavior.internalField1003, f, f, f, RotationPriority.internalField1011);
        if (!this.internalField0519.internalMethod02365((long)this.internalField1143.internalMethod08576())) {
            return;
        }
        RotationManager typedValue269 = RockstarClient.getInstance().internalMethod02368();
        if (typedValue269.internalMethod09074().internalMethod00735(typedValue266) > this.internalField1141.internalMethod08576()) {
            return;
        }
        ItemCooldownHolder.internalField0006.internalMethod07082(Items.ENDER_PEARL);
        this.internalField0519.internalMethod00701();
    };

    public TargetPearlModule() {
        this.internalMethod09653();
    }

    private void internalMethod09653() {
        this.internalField0383 = new SliderSetting(this, "modules.settings.target_pearl.track_range").internalMethod05900(10.0f).internalMethod02732(80.0f).internalMethod08673(1.0f).internalMethod08074(50.0f).internalMethod05660(f -> " m");
        this.internalField0382 = new SliderSetting(this, "modules.settings.target_pearl.min_landing").internalMethod05900(2.0f).internalMethod02732(15.0f).internalMethod08673(0.5f).internalMethod08074(4.0f).internalMethod05660(f -> " m");
        this.internalField1142 = new SliderSetting(this, "modules.settings.target_pearl.max_landing").internalMethod05900(10.0f).internalMethod02732(80.0f).internalMethod08673(1.0f).internalMethod08074(45.0f).internalMethod05660(f -> " m");
        this.internalField1140 = new SliderSetting(this, "modules.settings.target_pearl.aim_speed").internalMethod05900(40.0f).internalMethod02732(180.0f).internalMethod08673(5.0f).internalMethod08074(180.0f);
        this.internalField1141 = new SliderSetting(this, "modules.settings.target_pearl.max_angle").internalMethod05900(0.5f).internalMethod02732(20.0f).internalMethod08673(0.5f).internalMethod08074(5.0f).internalMethod05660(f -> "\u00b0");
        this.internalField1143 = new SliderSetting(this, "modules.settings.target_pearl.cooldown").internalMethod05900(0.0f).internalMethod02732(1000.0f).internalMethod08673(25.0f).internalMethod08074(50.0f).internalMethod05660(f -> " ms");
        this.internalField0650 = new BooleanSetting(this, "modules.settings.target_pearl.own_pearls");
        this.internalField0651 = new BooleanSetting(this, "modules.settings.target_pearl.only_holding");
    }

    @Override
    public void onEnable() {
        this.internalField0519.internalMethod00701();
        this.internalField0283 = null;
        this.internalField0118 = null;
    }

    private boolean internalMethod09654() {
        SlotCollection<InventorySlot> typedValue228 = InventorySlots.internalMethod02872().internalMethod07591(InventorySlots.internalMethod03558()).internalMethod07591(InventorySlots.internalMethod07766());
        return typedValue228.internalMethod03297(itemStack -> itemStack != null && !itemStack.isEmpty() && itemStack.isOf(Items.ENDER_PEARL)) != null;
    }

    private EnderPearlEntity internalMethod01110() {
        EnderPearlEntity enderPearlEntity = null;
        double d = Double.MAX_VALUE;
        float f = this.internalField0383.internalMethod08576() * this.internalField0383.internalMethod08576();
        for (Entity entity : TargetPearlModule.internalField0149.world.getEntities()) {
            double d2;
            if (!(entity instanceof EnderPearlEntity)) continue;
            EnderPearlEntity enderPearlEntity2 = (EnderPearlEntity)entity;
            if (!this.internalField0650.internalMethod04496() && enderPearlEntity2.getOwner() == TargetPearlModule.internalField0149.player || enderPearlEntity2.isRemoved() || (d2 = enderPearlEntity2.squaredDistanceTo((Entity)TargetPearlModule.internalField0149.player)) > (double)f || !(d2 < d)) continue;
            d = d2;
            enderPearlEntity = enderPearlEntity2;
        }
        return enderPearlEntity;
    }

    private Vec3d internalMethod07119(EnderPearlEntity enderPearlEntity) {
        Vec3d vec3d = enderPearlEntity.getEntityPos();
        Vec3d vec3d2 = enderPearlEntity.getVelocity();
        for (int i = 0; i < 200; ++i) {
            Vec3d vec3d3 = vec3d.add(vec3d2);
            BlockHitResult blockHitResult = TargetPearlModule.internalField0149.world.raycast(new RaycastContext(vec3d, vec3d3, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, (Entity)enderPearlEntity));
            if (blockHitResult.getType() == HitResult.Type.BLOCK) {
                return blockHitResult.getPos();
            }
            vec3d2 = vec3d2.multiply(0.99).add(0.0, -0.03, 0.0);
            vec3d = vec3d3;
            if (!(vec3d.y < (double)(TargetPearlModule.internalField0149.world.getBottomY() - 16))) continue;
            return null;
        }
        return null;
    }

    private Rotation internalMethod05898(Vec3d vec3d) {
        Vec3d vec3d2 = TargetPearlModule.internalField0149.player.getEyePos();
        double d = vec3d.x - vec3d2.x;
        double d2 = vec3d.y - vec3d2.y;
        double d3 = vec3d.z - vec3d2.z;
        double d4 = Math.hypot(d, d3);
        if (d4 < 0.001) {
            return null;
        }
        float f = (float)(Math.toDegrees(Math.atan2(d3, d)) - 90.0);
        Float f2 = this.internalMethod04877(d4, d2);
        if (f2 == null) {
            return null;
        }
        return new Rotation(f, f2.floatValue());
    }

    private Float internalMethod04877(double d, double d2) {
        float f;
        float f2 = Float.NaN;
        double d3 = Double.MAX_VALUE;
        for (f = -89.0f; f <= 60.0f; f += 1.0f) {
            double d4;
            Double d5 = this.internalMethod03829(d, f);
            if (d5 == null || !((d4 = Math.abs(d5 - d2)) < d3)) continue;
            d3 = d4;
            f2 = f;
        }
        if (Float.isNaN(f2)) {
            return null;
        }
        f = f2 - 1.0f;
        float f3 = f2 + 1.0f;
        for (float f4 = f; f4 <= f3; f4 += 0.05f) {
            double d6;
            Double d7 = this.internalMethod03829(d, f4);
            if (d7 == null || !((d6 = Math.abs(d7 - d2)) < d3)) continue;
            d3 = d6;
            f2 = f4;
        }
        if (d3 > 1.5) {
            return null;
        }
        return Float.valueOf(MathHelper.clamp((float)f2, (float)-90.0f, (float)90.0f));
    }

    private Double internalMethod03829(double d, float f) {
        double d2 = Math.toRadians(f);
        double d3 = Math.cos(d2);
        double d4 = d3 * 1.5;
        double d5 = -Math.sin(d2) * 1.5;
        if (d4 <= 1.0E-4) {
            return null;
        }
        double d6 = 0.0;
        double d7 = 0.0;
        for (int i = 0; i < 200; ++i) {
            double d8 = d6;
            double d9 = d7;
            d6 += d4;
            d7 += d5;
            if (d6 >= d) {
                double d10 = (d - d8) / d4;
                return d9 + d5 * d10;
            }
            d4 *= 0.99;
            d5 = d5 * 0.99 - 0.03;
        }
        return null;
    }
}
