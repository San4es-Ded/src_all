package rockstar.modules.combat;










import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.rotation.*;
import rockstar.client.module.*;
import rockstar.client.inventory.*;
import rockstar.client.event.*;
import rockstar.client.animation.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.game.*;
import rockstar.client.*;

import java.util.Optional;
import lombok.Generated;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.util.math.Vec3d;
import pyrock.events.game.PostAttackEvent;
import pyrock.events.game.WorldChangeEvent;
import pyrock.events.player.InputEvent;
import rockstar.client.setting.SettingOwner;
import rockstar.client.setting.BooleanSetting;
import rockstar.client.setting.ModeSetting;
import rockstar.client.setting.SliderSetting;
import rockstar.client.event.EventListener;
import rockstar.client.module.ModuleCategory;
import rockstar.client.RockstarClient;
import rockstar.client.module.ModuleInfo;
import rockstar.client.util.CombatUtils;
import rockstar.client.internal.script.ElytraUtils;
import rockstar.client.internal.game.EntityPositionTracker;
import rockstar.client.inventory.InventoryUtils;
import rockstar.client.rotation.RotationBehavior;
import rockstar.client.rotation.Rotation;
import rockstar.client.rotation.RotationManager;
import rockstar.client.rotation.RotationUtils;
import rockstar.client.rotation.RotationPriority;
import rockstar.client.module.Module;
import rockstar.modules.movement.ElytraStrafeModule;

@ModuleInfo(name="Elytra Target", category=ModuleCategory.COMBAT, internalMethod09633="modules.descriptions.elytra_target")
public class ElytraTargetModule
extends Module {
    private BooleanSetting internalField0650;
    private BooleanSetting internalField0651;
    private SliderSetting internalField0383;
    private SliderSetting internalField0382;
    private SliderSetting internalField1142;
    private ModeSetting internalField0668;
    private ModeSetting.InternalType0088 internalField0237;
    private ModeSetting.InternalType0088 internalField0238;
    private SliderSetting internalField1140;
    private BooleanSetting internalField1261;
    private SliderSetting internalField1141;
    private LivingEntity internalField0505;
    private Vec3d internalField0283 = Vec3d.ZERO;
    private double internalField0194 = Double.NaN;
    private boolean internalField0277 = false;
    private Vec3d internalField0282 = Vec3d.ZERO;
    private final EventListener<InputEvent> internalField0157 = inputEvent -> {
        if (!this.internalField1261.internalMethod04496()) {
            this.internalField0277 = false;
            this.internalField0194 = Double.NaN;
            this.internalField0282 = Vec3d.ZERO;
            return;
        }
        if (ElytraTargetModule.internalField0149.player == null || !ElytraTargetModule.internalField0149.player.isGliding()) {
            this.internalField0277 = false;
            this.internalField0194 = Double.NaN;
            this.internalField0282 = Vec3d.ZERO;
            return;
        }
        if (this.internalField0505 == null) {
            this.internalField0277 = false;
            this.internalField0194 = Double.NaN;
            this.internalField0282 = Vec3d.ZERO;
            return;
        }
        boolean inRange = ElytraTargetModule.internalField0149.player.distanceTo((Entity)this.internalField0505) < this.internalField1141.internalMethod08576();
        if (!inRange) {
            this.internalField0277 = false;
            this.internalField0194 = Double.NaN;
            this.internalField0282 = Vec3d.ZERO;
            return;
        }
        double d = ElytraTargetModule.internalField0149.player.getY();
        if (ElytraTargetModule.internalField0149.player.isOnGround()) {
            this.internalField0194 = Double.NaN;
            this.internalField0277 = false;
        } else if (!this.internalField0277) {
            if (Double.isNaN(this.internalField0194)) {
                this.internalField0194 = d;
            } else if (d > this.internalField0194) {
                this.internalField0194 = d;
            } else if (d < this.internalField0194) {
                this.internalField0277 = true;
                this.internalField0282 = ElytraTargetModule.internalField0149.player.getEntityPos();
                this.internalField0194 = d;
            }
        }
        if (this.internalField0277) {
            inputEvent.setForward(0.0f);
            inputEvent.setStrafe(0.0f);
            ElytraTargetModule.internalField0149.player.setVelocity(Vec3d.ZERO);
            ElytraTargetModule.internalField0149.player.setPosition(this.internalField0282);
        }
    };
    private final EventListener<PostAttackEvent> internalField0158 = postAttackEvent -> {
        long l;
        LivingEntity livingEntity;
        if (!this.internalMethod09240()) {
            return;
        }
        if (CombatUtils.internalMethod06028() != null) {
            ElytraUtils.internalMethod07049(false);
            if (ElytraTargetModule.internalField0149.player.isSprinting() && ElytraTargetModule.internalField0149.player.input.hasForwardMovement() && ElytraTargetModule.internalField0149.player.checkGliding()) {
                internalField0149.getNetworkHandler().sendPacket((Packet)new ClientCommandC2SPacket((Entity)ElytraTargetModule.internalField0149.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
            }
        }
        if ((livingEntity = Optional.ofNullable(this.internalField0505).orElseGet(this::internalMethod04803)) != null) {
            ElytraUtils.internalMethod03028(ElytraUtils.internalMethod01674(livingEntity));
        }
        if (this.internalField0650.internalMethod04496() && this.internalMethod01908(l = this.internalMethod05106(livingEntity, livingEntity != null ? (double)ElytraTargetModule.internalField0149.player.distanceTo((Entity)livingEntity) : Double.MAX_VALUE))) {
            ElytraUtils.internalMethod07048(this.internalField0383.internalMethod08576());
        }
    };
    private final EventListener<WorldChangeEvent> internalField1028 = worldChangeEvent -> this.disable();

    public ElytraTargetModule() {
        this.internalMethod09894();
    }

    private void internalMethod09894() {
        this.internalField0650 = new BooleanSetting(this, "modules.settings.elytra_target.auto_fireworks").internalMethod06630();
        this.internalField0651 = new BooleanSetting((SettingOwner)this, "modules.settings.elytra_target.smart_fireworks", () -> !this.internalField0650.internalMethod04496()).internalMethod06630();
        this.internalField0383 = new SliderSetting((SettingOwner)this, "modules.settings.elytra_target.fireworkSlot", () -> !this.internalField0650.internalMethod04496()).internalMethod05900(1.0f).internalMethod02732(9.0f).internalMethod08673(1.0f).internalMethod08074(7.0f).internalMethod06240(" slot");
        this.internalField0382 = new SliderSetting((SettingOwner)this, "modules.settings.elytra_target.fireworkDelay", () -> !this.internalField0650.internalMethod04496() && this.internalField0651.internalMethod04496()).internalMethod05900(0.25f).internalMethod02732(3.0f).internalMethod08673(0.05f).internalMethod08074(0.45f).internalMethod06240(" s");
        this.internalField1142 = new SliderSetting(this, "modules.settings.elytra_target.engageRange").internalMethod05900(6.0f).internalMethod02732(50.0f).internalMethod08673(1.0f).internalMethod08074(24.0f).internalMethod06240(" blocks");
        this.internalField0668 = new ModeSetting((SettingOwner)this, "modules.settings.elytra_target.prediction_mode", "motion");
        this.internalField0237 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.elytra_target.prediction_mode.motion");
        this.internalField0238 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.elytra_target.prediction_mode.server_pos").select();
        this.internalField1140 = new SliderSetting((SettingOwner)this, "modules.settings.elytra_target.lead_strength", () -> !this.internalField0668.internalMethod06103(this.internalField0237)).internalMethod05900(0.0f).internalMethod02732(5.0f).internalMethod08673(0.1f).internalMethod08074(3.0f).internalMethod06240(" ticks");
        this.internalField1261 = new BooleanSetting(this, "modules.settings.elytra_target.air_freeze");
        this.internalField1141 = new SliderSetting((SettingOwner)this, "modules.settings.elytra_target.freeze_distance", () -> !this.internalField1261.internalMethod04496()).internalMethod05900(1.0f).internalMethod02732(10.0f).internalMethod08673(0.1f).internalMethod08074(3.0f).internalMethod06240(" blocks");
    }

    @Override
    public void internalMethod08229() {
        if (!RockstarClient.getInstance().getModuleManager().getModule(AuraModule.class).isEnabled()) {
            this.internalMethod09239();
            return;
        }
        if (!this.internalMethod09240()) {
            this.internalMethod09239();
            return;
        }
        this.internalField0505 = this.internalMethod04803();
        this.internalMethod07540(this.internalField0505);
        if (this.internalField0650.internalMethod04496()) {
            this.internalMethod06533(this.internalField0505);
        }
        if (this.internalField0505 != null) {
            this.internalMethod09896();
        }
    }

    private LivingEntity internalMethod04803() {
        LivingEntity livingEntity = RockstarClient.getInstance().internalMethod04463().internalMethod01783();
        return livingEntity instanceof PlayerEntity ? livingEntity : null;
    }

    private void internalMethod06533(LivingEntity livingEntity) {
        PlayerEntity playerEntity;
        if (ElytraTargetModule.internalField0149.player == null || !ElytraTargetModule.internalField0149.player.isGliding()) {
            return;
        }
        if (RockstarClient.getInstance().getModuleManager().getModule(ElytraStrafeModule.class).isEnabled()) {
            return;
        }
        double d = livingEntity == null ? Double.MAX_VALUE : (double)ElytraTargetModule.internalField0149.player.distanceTo((Entity)livingEntity);
        boolean bl2 = livingEntity instanceof PlayerEntity && this.internalMethod05025(playerEntity = (PlayerEntity)livingEntity);
        boolean bl3 = ElytraTargetModule.internalField0149.player.getY() < (livingEntity != null ? livingEntity.getY() + 3.0 : ElytraTargetModule.internalField0149.player.getY() + 5.0);
        long l = this.internalMethod05106(livingEntity, d);
        boolean shouldUseFirework = this.internalField0651.internalMethod04496() ? d > 15.0 || bl2 || this.internalMethod09897() || bl3 : d > 15.0 || bl2;
        if (shouldUseFirework && this.internalMethod01908(l)) {
            ElytraUtils.internalMethod07048(this.internalField0383.internalMethod08576());
        }
    }

    private long internalMethod05106(LivingEntity livingEntity, double d) {
        PlayerEntity playerEntity;
        long l = (long)(this.internalField0382.internalMethod08576() * 1000.0f);
        if (!this.internalField0651.internalMethod04496()) {
            return l;
        }
        if (livingEntity instanceof PlayerEntity && this.internalMethod05025(playerEntity = (PlayerEntity)livingEntity)) {
            return (long)((float)l * 0.68f);
        }
        if (d < 8.0) {
            return (long)((float)l * 1.35f);
        }
        if (this.internalMethod09897()) {
            return (long)((float)l * 0.78f);
        }
        return l;
    }

    private boolean internalMethod09897() {
        return ElytraTargetModule.internalField0149.player.getY() < (this.internalField0505 != null ? this.internalField0505.getY() + 2.0 : (double)ElytraTargetModule.internalField0149.world.getSeaLevel());
    }

    private void internalMethod07540(LivingEntity livingEntity) {
        if (livingEntity == null || !ElytraTargetModule.internalField0149.player.isGliding()) {
            this.internalField0283 = Vec3d.ZERO;
            return;
        }
        Vec3d vec3d = InternalType0411.internalMethod06110(livingEntity, this.internalField0668, this.internalField0237, this.internalField0238, this.internalField1140.internalMethod08576());
        if (vec3d == null) {
            this.internalField0283 = Vec3d.ZERO;
            return;
        }
        this.internalField0283 = vec3d;
        RotationManager typedValue269 = RockstarClient.getInstance().internalMethod02368();
        Rotation typedValue266 = RotationUtils.internalMethod05580(this.internalField0283);
        typedValue269.internalMethod00418(typedValue266, RotationBehavior.internalField1003, 180.0f, 180.0f, 180.0f, RotationPriority.internalField1010);
    }

    private void internalMethod09896() {
        if (InventoryUtils.internalMethod06826().internalMethod00210() == Items.ELYTRA && ElytraTargetModule.internalField0149.player.isSprinting() && ElytraTargetModule.internalField0149.player.input.hasForwardMovement() && ElytraTargetModule.internalField0149.player.checkGliding()) {
            internalField0149.getNetworkHandler().sendPacket((Packet)new ClientCommandC2SPacket((Entity)ElytraTargetModule.internalField0149.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
        }
    }

    private boolean internalMethod05025(PlayerEntity playerEntity) {
        double d;
        double d2 = ElytraTargetModule.internalField0149.player.getVelocity().horizontalLengthSquared();
        return d2 + 1.0E-4 < (d = playerEntity.getVelocity().horizontalLengthSquared());
    }

    private boolean internalMethod01908(long l) {
        return ElytraUtils.internalMethod07135().internalMethod02365(l);
    }

    private boolean internalMethod09240() {
        return rockstar.client.util.LegacyItemTypes.armorItem(ElytraTargetModule.internalField0149.player.getInventory(), 2).getItem() == Items.ELYTRA;
    }

    private void internalMethod09239() {
        this.internalField0505 = null;
        this.internalField0283 = Vec3d.ZERO;
    }

    @Override
    public void onDisable() {
        this.internalMethod09239();
    }

    @Generated
    public BooleanSetting internalMethod06593() {
        return this.internalField0650;
    }

    @Generated
    public BooleanSetting internalMethod07244() {
        return this.internalField0651;
    }

    @Generated
    public SliderSetting internalMethod04337() {
        return this.internalField0383;
    }

    @Generated
    public SliderSetting internalMethod05061() {
        return this.internalField0382;
    }

    @Generated
    public SliderSetting internalMethod08019() {
        return this.internalField1142;
    }

    @Generated
    public ModeSetting internalMethod06651() {
        return this.internalField0668;
    }

    @Generated
    public ModeSetting.InternalType0088 internalMethod00768() {
        return this.internalField0237;
    }

    @Generated
    public ModeSetting.InternalType0088 internalMethod00957() {
        return this.internalField0238;
    }

    @Generated
    public SliderSetting internalMethod08150() {
        return this.internalField1140;
    }

    @Generated
    public BooleanSetting internalMethod08428() {
        return this.internalField1261;
    }

    @Generated
    public SliderSetting internalMethod08963() {
        return this.internalField1141;
    }

    @Generated
    public LivingEntity internalMethod03335() {
        return this.internalField0505;
    }

    @Generated
    public double internalMethod06710() {
        return this.internalField0194;
    }

    @Generated
    public boolean internalMethod09895() {
        return this.internalField0277;
    }

    @Generated
    public Vec3d internalMethod07161() {
        return this.internalField0282;
    }

    @Generated
    public EventListener<InputEvent> internalMethod04157() {
        return this.internalField0157;
    }

    @Generated
    public EventListener<PostAttackEvent> internalMethod05542() {
        return this.internalField0158;
    }

    @Generated
    public EventListener<WorldChangeEvent> internalMethod09082() {
        return this.internalField1028;
    }

    @Generated
    public Vec3d internalMethod04271() {
        return this.internalField0283;
    }

    static class InternalType0411 {
        private InternalType0411() {
        }

        public static Vec3d internalMethod06110(LivingEntity livingEntity, ModeSetting typedValue170, ModeSetting.InternalType0088 nestedValue2011, ModeSetting.InternalType0088 nestedValue2012, double d) {
            if (livingEntity == null) {
                return null;
            }
            if (typedValue170.internalMethod06103(nestedValue2011)) {
                if (livingEntity instanceof PlayerEntity) {
                    PlayerEntity playerEntity = (PlayerEntity)livingEntity;
                    Vec3d vec3d = playerEntity.getEntityPos().subtract(new Vec3d(playerEntity.lastX, playerEntity.lastY, playerEntity.lastZ));
                    return playerEntity.getEyePos().add(vec3d.multiply(d));
                }
                return livingEntity.getEyePos();
            }
            if (typedValue170.internalMethod06103(nestedValue2012)) {
                return EntityPositionTracker.internalMethod01255((Entity)livingEntity);
            }
            return livingEntity.getEntityPos();
        }
    }
}
