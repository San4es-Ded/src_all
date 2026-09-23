package rockstar.modules.combat;









import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.rotation.*;
import rockstar.client.module.*;
import rockstar.client.event.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.internal.game.*;
import rockstar.client.*;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import lombok.Generated;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import pyrock.events.game.AttackEvent;
import pyrock.events.player.ClientPlayerTickEvent;
import pyrock.events.render.GameRendererEvent;
import rockstar.client.setting.SettingOwner;
import rockstar.client.setting.BooleanSetting;
import rockstar.client.setting.ModeSetting;
import rockstar.client.setting.MultiSelectSetting;
import rockstar.client.setting.SliderSetting;
import rockstar.client.internal.inventory.EntityComparatorUtils;
import rockstar.client.event.EventListener;
import rockstar.client.internal.game.TargetSelectionManager;
import rockstar.client.internal.inventory.EntityTargetFilter;
import rockstar.client.module.ModuleCategory;
import rockstar.client.RockstarClient;
import rockstar.client.module.ModuleInfo;
import rockstar.client.util.ClientMessages;
import rockstar.client.util.MathUtils;
import rockstar.client.internal.game.HitParticleEmitter;
import rockstar.client.internal.game.KnockbackSimulator;
import rockstar.client.internal.game.EntityMotionPredictor;
import rockstar.client.module.Module;
import rockstar.client.internal.rotation.NeuralAuraRotation;
import rockstar.client.internal.rotation.NeuralRotationHelper;

@ModuleInfo(name="Aim Assist", category=ModuleCategory.COMBAT, internalMethod09633="modules.descriptions.aim_assist")
public class AimAssistModule
extends Module {
    private static final Set<String> internalField0546 = Set.of("sword", "trident", "_axe", "mace", "stick", "pickaxe", "shovel");
    private ModeSetting internalField0668;
    private ModeSetting.InternalType0088 internalField0237;
    private ModeSetting.InternalType0088 internalField0238;
    private SliderSetting internalField0383;
    private MultiSelectSetting internalField0675;
    private MultiSelectSetting.InternalType0091 internalField0245;
    private MultiSelectSetting.InternalType0091 internalField0244;
    private MultiSelectSetting.InternalType0091 internalField1075;
    private MultiSelectSetting.InternalType0091 internalField1074;
    private MultiSelectSetting.InternalType0091 internalField1073;
    private MultiSelectSetting.InternalType0091 internalField1072;
    private MultiSelectSetting.InternalType0091 internalField1491;
    private ModeSetting internalField0669;
    private ModeSetting.InternalType0088 internalField1066;
    private ModeSetting.InternalType0088 internalField1067;
    private ModeSetting.InternalType0088 internalField1068;
    private ModeSetting internalField1272;
    private ModeSetting.InternalType0088 internalField1065;
    private ModeSetting.InternalType0088 internalField1480;
    private ModeSetting.InternalType0088 internalField1481;
    private SliderSetting internalField0382;
    private SliderSetting internalField1142;
    private SliderSetting internalField1140;
    private SliderSetting internalField1141;
    private BooleanSetting internalField0650;
    private BooleanSetting internalField0651;
    private SliderSetting internalField1143;
    private SliderSetting internalField1529;
    private BooleanSetting internalField1261;
    private BooleanSetting internalField1263;
    private BooleanSetting internalField1262;
    private SliderSetting internalField1534;
    private SliderSetting internalField1535;
    private SliderSetting internalField1536;
    private SliderSetting internalField1533;
    private BooleanSetting internalField1264;
    private BooleanSetting internalField1587;
    private SliderSetting internalField1532;
    private SliderSetting internalField1531;
    private SliderSetting internalField1530;
    private BooleanSetting internalField1590;
    private SliderSetting internalField1797;
    private final EntityMotionPredictor internalField0558 = new EntityMotionPredictor();
    private final HitParticleEmitter internalField0366 = new HitParticleEmitter();
    private final NeuralRotationHelper internalField0323 = new NeuralRotationHelper();
    private final float[] internalField0615 = new float[2];
    private float internalField1047;
    private float internalField1049;
    private float internalField1046;
    private boolean internalField0277;
    private LivingEntity internalField0505;
    private LivingEntity internalField0504;
    private long internalField0229;
    private long internalField0230;
    private long internalField1059;
    private boolean internalField0276 = true;
    private boolean internalField1099;
    private float internalField1456;
    private float internalField1457;
    private float internalField1458;
    private float internalField1459;
    private float internalField1460;
    private float internalField1461;
    private float internalField1462;
    private float internalField1455 = Float.NaN;
    private float internalField1723;
    private float internalField1731;
    private float internalField1727;
    private float internalField1728;
    private float internalField1717;
    private double internalField0193;
    private double internalField1045;
    private double internalField1043;
    private boolean internalField1100;
    private double internalField1042;
    private double internalField1044;
    private double internalField1453;
    private float internalField1718;
    private float internalField1719;
    private float internalField1721;
    private float internalField1722;
    private float internalField1720;
    private float internalField1730;
    private float internalField1729;
    private float internalField1725;
    private float internalField1726;
    private boolean internalField1102;
    private float internalField1724;
    private float internalField1732;
    private float internalField1843;
    private float internalField1844;
    private float internalField1845;
    private float internalField1854;
    private float internalField1856;
    private float internalField1859;
    private float internalField1857;
    private float internalField1858;
    private float internalField1852;
    private boolean internalField1101;
    private float internalField1855;
    private float internalField1850;
    private float internalField1849;
    private float internalField1848;
    private long internalField1058;
    private float internalField1842 = 0.15f;
    private boolean internalField1516;
    private float internalField1851;
    private final float[] internalField0616 = new float[2];
    private final float[] internalField1238 = new float[2];
    private final float[] internalField1240 = new float[2];
    private final double[] internalField0612 = new double[3];
    private final EventListener<ClientPlayerTickEvent> internalField0157 = clientPlayerTickEvent -> {
        if (this.internalMethod10025()) {
            this.internalMethod09699();
            return;
        }
        if (this.internalField1261.internalMethod04496() && !this.internalMethod09564()) {
            this.internalMethod09699();
            return;
        }
        this.internalMethod10022();
        this.internalMethod10024();
        this.internalMethod10020();
        if (this.internalMethod09566()) {
            this.internalMethod09701();
            return;
        }
        if (this.internalField0505 != null) {
            Vec3d vec3d = this.internalField0505.getVelocity();
            if (vec3d.lengthSquared() > 1.0E-6) {
                this.internalField0558.internalMethod05241(this.internalField0505, this.internalMethod08527(), 100);
            }
            if (this.internalField0650.internalMethod04496() && !this.internalField1099) {
                this.internalField1457 += 0.05f;
                if (this.internalField1457 >= this.internalField1458) {
                    this.internalField1099 = true;
                }
            }
        }
    };
    private final EventListener<GameRendererEvent> internalField0158 = gameRendererEvent -> {
        float f;
        float f2;
        float f3;
        float f4;
        float f5;
        float f6;
        float f7;
        float f8;
        if (this.internalMethod10025() || AimAssistModule.internalField0149.player == null) {
            return;
        }
        long l = System.nanoTime();
        float f9 = this.internalMethod04418(l);
        this.internalField1059 = l;
        if (f9 < 1.0E-4f || f9 > 0.1f) {
            f9 = 0.016666668f;
        }
        if (this.internalMethod09566()) {
            this.internalMethod04417(f9);
            return;
        }
        boolean bl = this.internalField1263.internalMethod04496();
        if (bl) {
            this.internalMethod09709();
        }
        this.internalField1717 = 0.0f;
        this.internalField1728 = 0.0f;
        if (this.internalMethod10023()) {
            this.internalMethod10018();
            return;
        }
        Vec3d vec3d = AimAssistModule.internalField0149.player.getEyePos();
        this.internalField1042 = vec3d.x;
        this.internalField1044 = vec3d.y;
        this.internalField1453 = vec3d.z;
        float f10 = this.internalMethod08527();
        this.internalMethod03571(this.internalField0505, f10, f9);
        float f11 = this.internalField1240[0];
        float f12 = this.internalField1240[1];
        if (!Float.isFinite(f11) || !Float.isFinite(f12)) {
            this.internalMethod10018();
            return;
        }
        float f13 = f11 * f11 + f12 * f12;
        if (f13 < 0.01f) {
            this.internalMethod10018();
            return;
        }
        float f14 = MathHelper.sqrt((float)f13);
        if (this.internalField0651.internalMethod04496()) {
            f8 = this.internalMethod04483(AimAssistModule.internalField0149.player.distanceTo((Entity)this.internalField0505));
            f12 += f8;
        }
        f8 = this.internalMethod04536(this.internalField0505);
        if (this.internalField0650.internalMethod04496()) {
            f7 = this.internalMethod08453(f14, f8);
            f6 = Math.max(f7 / this.internalField1141.internalMethod08576(), 0.01f);
            this.internalMethod03448(f14, f8, f9, f6);
            f5 = this.internalField0276 ? this.internalMethod08425(this.internalField1456) : this.internalMethod05610(f11, f12);
            f4 = f14 / f6 * f5 * this.internalField1140.internalMethod08576();
            f3 = f14 > 1.0E-4f ? 1.0f / f14 : 0.0f;
            f2 = MathHelper.clamp((float)(f11 * f3 * f4 * f9), (float)-20.0f, (float)20.0f);
            f = this.internalField0651.internalMethod04496() ? this.internalMethod03564(f12, f14, f8, f9) : 0.0f;
        } else {
            f7 = this.internalField1141.internalMethod08576() * this.internalField1140.internalMethod08576() * 10.0f * f9;
            f6 = Math.min(f14 / 10.0f, 1.0f);
            f5 = f14 > 1.0E-4f ? 1.0f / f14 : 0.0f;
            f2 = f11 * f5 * f7 * f6;
            f = this.internalField0651.internalMethod04496() ? f12 * f5 * f7 * f6 * this.internalField1143.internalMethod08576() : 0.0f;
        }
        f7 = this.internalField1529.internalMethod08576();
        f6 = this.internalMethod03827(f14, f9);
        f5 = f7 * f6;
        if (f5 > 1.0E-4f) {
            this.internalMethod03565(f2, f, f5, f9);
            f4 = this.internalMethod04416(f9);
            f2 += this.internalField0616[0] + f4 * 0.5f;
            f += this.internalField0616[1] + f4 * 0.25f;
        }
        if (this.internalField1590.internalMethod04496()) {
            this.internalMethod08454(f14, f9);
            f2 += this.internalField1855;
            f += this.internalField1850;
        }
        if (bl) {
            f2 = this.internalMethod02295(f2, this.internalField1731, f9);
            f = this.internalMethod02295(f, this.internalField1727, f9);
            if (this.internalField1262.internalMethod04496() && !this.internalMethod02296(f11, f12, f9)) {
                f = 0.0f;
                f2 = 0.0f;
            }
        }
        f4 = MathHelper.clamp((float)(1.0f - (float)Math.exp(-20.0f * f9)), (float)0.05f, (float)0.95f);
        this.internalField1459 = this.internalMethod07247(this.internalField1459, f2, f4);
        this.internalField1460 = this.internalMethod07247(this.internalField1460, f, f4 * 0.75f);
        f3 = Math.abs(f11) * 1.5f + 0.5f;
        float f15 = Math.abs(f12) * 1.5f + 0.5f;
        this.internalField1459 = MathHelper.clamp((float)this.internalField1459, (float)(-f3), (float)f3);
        this.internalField1460 = MathHelper.clamp((float)this.internalField1460, (float)(-f15), (float)f15);
        this.internalMethod09707();
        this.internalMethod10018();
    };
    private final EventListener<AttackEvent> internalField1028 = attackEvent -> {
        LivingEntity livingEntity;
        Entity entity;
        if (this.internalMethod09566() && (entity = attackEvent.getEntity()) instanceof LivingEntity && (livingEntity = (LivingEntity)entity) != AimAssistModule.internalField0149.player) {
            this.internalField0323.internalMethod00617();
        }
    };

    public AimAssistModule() {
        this.internalMethod09563();
    }

    private void internalMethod09563() {
        this.internalField0668 = new ModeSetting(this, "aimassist.mode");
        this.internalField0237 = new ModeSetting.InternalType0088(this.internalField0668, "aimassist.mode_normal").select();
        this.internalField0238 = new ModeSetting.InternalType0088(this.internalField0668, "aimassist.mode_neuro");
        this.internalField0383 = new SliderSetting(this, "aimassist.neuro_strength", "aimassist.neuro_strength.desc", () -> !this.internalMethod09566()).internalMethod05900(0.1f).internalMethod02732(1.0f).internalMethod08673(0.05f).internalMethod08074(1.0f);
        this.internalField0675 = new MultiSelectSetting(this, "modules.settings.aura.targets");
        this.internalField0245 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.aura.targets.players").select();
        this.internalField0244 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.aura.targets.animals").select();
        this.internalField1075 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.aura.targets.mobs").select();
        this.internalField1074 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.aura.targets.invisibles").select();
        this.internalField1073 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.aura.targets.nakedPlayers").select();
        this.internalField1072 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.aura.targets.rockUsers");
        this.internalField1491 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.aura.targets.friends");
        this.internalField0669 = new ModeSetting(this, "aura.targets_sort");
        this.internalField1066 = new ModeSetting.InternalType0088(this.internalField0669, "aura.ts_dist");
        this.internalField1067 = new ModeSetting.InternalType0088(this.internalField0669, "aura.ts_health");
        this.internalField1068 = new ModeSetting.InternalType0088(this.internalField0669, "aura.ts_fov").select();
        this.internalField1272 = new ModeSetting((SettingOwner)this, "aimassist.target_lock_mode", this::internalMethod09566);
        this.internalField1065 = new ModeSetting.InternalType0088(this.internalField1272, "aimassist.lock_off");
        this.internalField1480 = new ModeSetting.InternalType0088(this.internalField1272, "aimassist.lock_on_attack");
        this.internalField1481 = new ModeSetting.InternalType0088(this.internalField1272, "aimassist.lock_auto").select();
        this.internalField0382 = new SliderSetting((SettingOwner)this, "aimassist.lock_timeout", this::internalMethod09566).internalMethod05900(0.0f).internalMethod02732(10.0f).internalMethod08673(0.1f).internalMethod08074(5.0f);
        this.internalField1142 = new SliderSetting(this, "modules.settings.aura.aimDistance").internalMethod05900(3.0f).internalMethod02732(10.0f).internalMethod08673(0.1f).internalMethod08074(4.5f);
        this.internalField1140 = new SliderSetting((SettingOwner)this, "aimassist.strength", this::internalMethod09566).internalMethod05900(0.1f).internalMethod02732(2.0f).internalMethod08673(0.01f).internalMethod08074(1.0f);
        this.internalField1141 = new SliderSetting((SettingOwner)this, "aimassist.aim_speed", this::internalMethod09566).internalMethod05900(1.0f).internalMethod02732(60.0f).internalMethod08673(0.5f).internalMethod08074(18.0f);
        this.internalField0650 = new BooleanSetting((SettingOwner)this, "aimassist.repit_aim", this::internalMethod09566).internalMethod04836(false);
        this.internalField0651 = new BooleanSetting((SettingOwner)this, "aimassist.enable_vertical", this::internalMethod09566).internalMethod06630();
        this.internalField1143 = new SliderSetting((SettingOwner)this, "projectile.vertical_factor", this::internalMethod09700).internalMethod05900(0.1f).internalMethod02732(1.5f).internalMethod08673(0.01f).internalMethod08074(0.4f);
        this.internalField1529 = new SliderSetting(this, "aimassist.motor_noise", "motornoise.desc", this::internalMethod09566).internalMethod05900(0.0f).internalMethod02732(0.4f).internalMethod08673(0.01f).internalMethod08074(0.15f);
        this.internalField1261 = new BooleanSetting(this, "aimassist.only_on_weapon").internalMethod06630();
        this.internalField1263 = new BooleanSetting((SettingOwner)this, "aimassist.yield_to_mouse", this::internalMethod09566).internalMethod04836(false);
        this.internalField1262 = new BooleanSetting((SettingOwner)this, "aimassist.input_based", this::internalMethod10019).internalMethod04836(false);
        this.internalField1534 = new SliderSetting((SettingOwner)this, "aimassist.input_threshold", this::internalMethod10021).internalMethod05900(0.1f).internalMethod02732(3.0f).internalMethod08673(0.1f).internalMethod08074(0.8f);
        this.internalField1535 = new SliderSetting((SettingOwner)this, "aimassist.prediction_ticks", this::internalMethod09566).internalMethod05900(0.0f).internalMethod02732(3.0f).internalMethod08673(1.0f).internalMethod08074(2.0f);
        this.internalField1536 = new SliderSetting((SettingOwner)this, "aimassist.velo_pr_ticks", this::internalMethod09566).internalMethod05900(0.0f).internalMethod02732(3.0f).internalMethod08673(1.0f).internalMethod08074(1.0f);
        this.internalField1533 = new SliderSetting(this, "aimassist.prediction_chance", "aimassist.prediction_chance.desc", this::internalMethod09566).internalMethod05900(0.0f).internalMethod02732(100.0f).internalMethod08673(5.0f).internalMethod08074(65.0f).internalMethod06240(" %");
        this.internalField1264 = new BooleanSetting((SettingOwner)this, "aimassist.multipoint", () -> this.internalMethod09566() || !this.internalField1263.internalMethod04496()).internalMethod04836(false);
        this.internalField1587 = new BooleanSetting((SettingOwner)this, "aimassist.mp_adaptive", this::internalMethod09702).internalMethod06630();
        this.internalField1532 = new SliderSetting((SettingOwner)this, "aimassist.mp_count", this::internalMethod09708).internalMethod05900(3.0f).internalMethod02732(50.0f).internalMethod08673(1.0f).internalMethod08074(8.0f);
        this.internalField1531 = new SliderSetting((SettingOwner)this, "aimassist.mp_spread", this::internalMethod09708).internalMethod05900(0.2f).internalMethod02732(2.0f).internalMethod08673(0.05f).internalMethod08074(0.7f);
        this.internalField1530 = new SliderSetting((SettingOwner)this, "aimassist.regen", this::internalMethod09702).internalMethod05900(0.005f).internalMethod02732(0.2f).internalMethod08673(0.005f).internalMethod08074(0.025f);
        this.internalField1590 = new BooleanSetting(this, "aimassist.overshoot", "aimassist.overshoot.desc", this::internalMethod09566).internalMethod04836(true);
        this.internalField1797 = new SliderSetting(this, "aimassist.overshoot_chance", "aimassist.overshoot_chance.desc", () -> this.internalMethod09566() || !this.internalField1590.internalMethod04496()).internalMethod05900(0.0f).internalMethod02732(10.0f).internalMethod08673(1.0f).internalMethod08074(3.0f).internalMethod06240(" %");
    }

    private boolean internalMethod09566() {
        return this.internalField0668.internalMethod06103(this.internalField0238);
    }

    private boolean internalMethod09700() {
        return this.internalMethod09566() || !this.internalField0651.internalMethod04496();
    }

    private boolean internalMethod09702() {
        return this.internalMethod09566() || !this.internalField1264.internalMethod04496() || !this.internalField1263.internalMethod04496();
    }

    private boolean internalMethod09708() {
        return this.internalMethod09702() || this.internalField1587.internalMethod04496();
    }

    private boolean internalMethod09710() {
        return this.internalField1272.internalMethod06103(this.internalField1065);
    }

    private boolean internalMethod10019() {
        return this.internalMethod09566() || !this.internalField1263.internalMethod04496();
    }

    private boolean internalMethod10021() {
        return this.internalMethod10019() || !this.internalField1262.internalMethod04496();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.internalField0277 = false;
        this.internalMethod09565();
        this.internalMethod09699();
        this.internalField0558.internalMethod07114();
        this.internalField0366.internalMethod06585();
        TargetSelectionManager typedValue182 = RockstarClient.getInstance().internalMethod04463();
        if (typedValue182 != null) {
            typedValue182.internalMethod07889();
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.internalMethod09699();
        this.internalField0558.internalMethod07114();
        this.internalField0366.internalMethod06585();
        TargetSelectionManager typedValue182 = RockstarClient.getInstance().internalMethod04463();
        if (typedValue182 != null) {
            typedValue182.internalMethod07889();
        }
    }

    private void internalMethod09565() {
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        this.internalField1724 = 0.1f + threadLocalRandom.nextFloat() * 0.5f;
        this.internalField1732 = (0.5f + threadLocalRandom.nextFloat() * 0.75f) * 2.0f;
        this.internalField1843 = 0.5f + threadLocalRandom.nextFloat() * 0.5f;
        this.internalField1844 = 3.0f + threadLocalRandom.nextFloat() * 0.25f;
        this.internalField1845 = 0.5f + threadLocalRandom.nextFloat() * 0.7f;
        this.internalField1854 = 0.08f + threadLocalRandom.nextFloat() * 0.12f;
        this.internalField1856 = 0.018f + threadLocalRandom.nextFloat() * 0.005f;
        this.internalField1859 = 0.45f + threadLocalRandom.nextFloat() * 0.05f;
        this.internalField1857 = 2.0f + threadLocalRandom.nextFloat();
        this.internalField1858 = 8.0f + threadLocalRandom.nextFloat() * 3.0f;
        this.internalField1852 = 0.15f + threadLocalRandom.nextFloat() * 0.05f;
        this.internalField1725 = MathUtils.internalMethod07919(this.internalField1843, this.internalField1844);
        this.internalField1721 = 0.3f + threadLocalRandom.nextFloat() * 0.4f;
        this.internalField1722 = 0.2f + threadLocalRandom.nextFloat() * 0.3f;
        this.internalField1720 = 0.04f + threadLocalRandom.nextFloat() * 0.06f;
        this.internalField0366.internalMethod06286(0.05f + MathUtils.internalMethod07919(-0.03f, 0.15f));
        this.internalField1842 = this.internalField1530.internalMethod08576();
    }

    private void internalMethod09699() {
        this.internalField0504 = null;
        this.internalField0505 = null;
        this.internalField1456 = 0.0f;
        this.internalField1458 = 0.0f;
        this.internalField1457 = 0.0f;
        this.internalField1099 = false;
        this.internalField1100 = false;
        this.internalField0276 = true;
        this.internalField1460 = 0.0f;
        this.internalField1459 = 0.0f;
        this.internalField1462 = 0.0f;
        this.internalField1461 = 0.0f;
        this.internalField1455 = Float.NaN;
        this.internalField1723 = 0.0f;
        this.internalField1727 = 0.0f;
        this.internalField1731 = 0.0f;
        this.internalField1717 = 0.0f;
        this.internalField1728 = 0.0f;
        this.internalField1726 = 0.0f;
        this.internalField1730 = 0.0f;
        this.internalField1059 = 0L;
        this.internalField0229 = 0L;
        this.internalField0230 = 0L;
        this.internalField1043 = 0.0;
        this.internalField1045 = 0.0;
        this.internalField0193 = 0.0;
        this.internalField1058 = 0L;
        this.internalField1516 = false;
        this.internalField1101 = false;
        this.internalField1850 = 0.0f;
        this.internalField1855 = 0.0f;
        this.internalField1848 = 0.0f;
        this.internalField1849 = 0.0f;
        this.internalField1719 = 0.0f;
        this.internalField1718 = 0.0f;
        this.internalField0616[1] = 0.0f;
        this.internalField0616[0] = 0.0f;
        this.internalField0323.internalMethod00621();
        this.internalField1049 = 0.0f;
        this.internalField1047 = 0.0f;
        this.internalField1046 = 0.0f;
    }

    private void internalMethod09701() {
        if (this.internalField0505 == null || AimAssistModule.internalField0149.player == null || AimAssistModule.internalField0149.world == null) {
            this.internalField0323.internalMethod00621();
            this.internalField1049 = 0.0f;
            this.internalField1047 = 0.0f;
            return;
        }
        if (!this.internalField0323.internalMethod00618()) {
            if (!this.internalField0277) {
                this.internalField0277 = true;
                ClientMessages.internalMethod09025(Text.of((String)"Aim Assist: \u043c\u043e\u0434\u0435\u043b\u044c \u043d\u0435\u0439\u0440\u043e \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d\u0430 \u2014 \u043e\u0431\u0443\u0447\u0438 \u0435\u0451 \u0447\u0435\u0440\u0435\u0437 .neuro"));
            }
            return;
        }
        if (!this.internalField0323.internalMethod04053(AimAssistModule.internalField0149.player, AimAssistModule.internalField0149.world, this.internalField0505, NeuralAuraRotation.internalMethod02801(), this.internalField0615)) {
            return;
        }
        float f = this.internalField0383.internalMethod08576();
        this.internalField1047 = MathHelper.clamp((float)(this.internalField1047 + this.internalField0615[0] * f), (float)-90.0f, (float)90.0f);
        this.internalField1049 = MathHelper.clamp((float)(this.internalField1049 + this.internalField0615[1] * f), (float)-90.0f, (float)90.0f);
        this.internalField1046 = 0.0f;
    }

    private void internalMethod04417(float f) {
        if (AimAssistModule.internalField0149.player == null) {
            return;
        }
        if (Math.abs(this.internalField1047) < 1.0E-4f && Math.abs(this.internalField1049) < 1.0E-4f) {
            this.internalMethod10018();
            return;
        }
        this.internalField1046 += f;
        float f2 = Math.max(0.05f - (this.internalField1046 - f), f);
        float f3 = MathHelper.clamp((float)(f / f2), (float)0.0f, (float)1.0f);
        float f4 = this.internalField1047 * f3;
        float f5 = this.internalField1049 * f3;
        this.internalField1047 -= f4;
        this.internalField1049 -= f5;
        this.internalMethod03828(f4, f5);
        this.internalMethod10018();
    }

    private void internalMethod03571(LivingEntity livingEntity, float f, float f2) {
        double d;
        double d2;
        float f3;
        double d3;
        Vec3d vec3d;
        if (livingEntity == null || AimAssistModule.internalField0149.player == null) {
            this.internalField1240[1] = 0.0f;
            this.internalField1240[0] = 0.0f;
            return;
        }
        double d4 = MathHelper.lerp((double)f, (double)livingEntity.lastRenderX, (double)livingEntity.getX());
        double d5 = MathHelper.lerp((double)f, (double)livingEntity.lastRenderY, (double)livingEntity.getY());
        double d6 = MathHelper.lerp((double)f, (double)livingEntity.lastRenderZ, (double)livingEntity.getZ());
        int n = (int)this.internalField1535.internalMethod08576();
        if (n > 0 && ThreadLocalRandom.current().nextFloat() < this.internalField1533.internalMethod08576() && (vec3d = this.internalField0558.internalMethod06374()) != null && (d3 = vec3d.lengthSquared()) > 1.0E-6) {
            d4 += vec3d.x * (double)n;
            d5 += vec3d.y * (double)n;
            d6 += vec3d.z * (double)n;
        }
        if ((f3 = AimAssistModule.internalField0149.player.getAttackCooldownProgress(1.5f)) > 0.85f && !AimAssistModule.internalField0149.player.isUsingItem() && (d2 = (d3 = livingEntity.getX() - this.internalField1042) * d3 + (d = livingEntity.getZ() - this.internalField1453) * d) < 12.25 && d2 > 2.25) {
            float f4 = (f3 - 0.85f) / 0.15f;
            f4 *= f4;
            KnockbackSimulator.internalMethod06340(this.internalField0612, this.internalField1042, this.internalField1453, AimAssistModule.internalField0149.player.getYaw(), livingEntity, AimAssistModule.internalField0149.player.isSprinting(), KnockbackSimulator.internalMethod03432((LivingEntity)AimAssistModule.internalField0149.player), (int)this.internalField1536.internalMethod08576());
            d4 -= this.internalField0612[0] * (double)f4;
            d5 -= this.internalField0612[1] * (double)f4;
            d6 -= this.internalField0612[2] * (double)f4;
        }
        if (this.internalField1264.internalMethod04496() && this.internalField1263.internalMethod04496()) {
            this.internalMethod07407(livingEntity, d4, d5, d6);
            double[] dArray = this.internalField0366.internalMethod06284();
            if (dArray != null) {
                d3 = dArray[0];
                d = dArray[1];
                d2 = dArray[2];
            } else {
                d3 = d4;
                d = d5 + (double)(livingEntity.getHeight() * 0.5f);
                d2 = d6;
            }
        } else {
            d3 = d4;
        }
        d = d5 + (double)(livingEntity.getHeight() * 0.5f);
        d2 = d6;
        if (!this.internalField1100) {
            this.internalField0193 = d3;
            this.internalField1045 = d;
            this.internalField1043 = d2;
            this.internalField1100 = true;
        } else {
            double d7 = MathHelper.clamp((double)(1.0 - Math.exp(-25.0 * (double)f2)), (double)0.05, (double)0.95);
            this.internalField0193 += (d3 - this.internalField0193) * d7;
            this.internalField1045 += (d - this.internalField1045) * d7;
            this.internalField1043 += (d2 - this.internalField1043) * d7;
        }
        double d8 = this.internalField0193 - this.internalField1042;
        double d9 = this.internalField1045 - this.internalField1044;
        double d10 = this.internalField1043 - this.internalField1453;
        double d11 = d8 * d8 + d10 * d10;
        double d12 = Math.sqrt(d11);
        if (d12 < (double)1.0E-4f && Math.abs(d9) < (double)1.0E-4f) {
            this.internalField1240[1] = 0.0f;
            this.internalField1240[0] = 0.0f;
            return;
        }
        float f5 = (float)Math.toDegrees(Math.atan2(d10, d8)) - 90.0f;
        float f6 = MathHelper.clamp((float)((float)(-Math.toDegrees(Math.atan2(d9, d12)))), (float)-90.0f, (float)90.0f);
        this.internalField1240[0] = MathHelper.wrapDegrees((float)(f5 - AimAssistModule.internalField0149.player.getYaw()));
        this.internalField1240[1] = f6 - AimAssistModule.internalField0149.player.getPitch();
    }

    private void internalMethod09707() {
        this.internalMethod03828(this.internalField1459, this.internalField1460);
    }

    private void internalMethod03828(float f, float f2) {
        if (AimAssistModule.internalField0149.player == null) {
            return;
        }
        this.internalMethod05611(f, f2);
        float f3 = Float.isFinite(this.internalField1238[0]) ? this.internalField1238[0] : 0.0f;
        float f4 = Float.isFinite(this.internalField1238[1]) ? this.internalField1238[1] : 0.0f;
        float f5 = AimAssistModule.internalField0149.player.getYaw() + f3;
        float f6 = MathHelper.clamp((float)(AimAssistModule.internalField0149.player.getPitch() + f4), (float)-90.0f, (float)90.0f);
        AimAssistModule.internalField0149.player.setYaw(f5);
        AimAssistModule.internalField0149.player.setPitch(f6);
        AimAssistModule.internalField0149.player.headYaw = f5;
        this.internalField1728 = f3;
        this.internalField1717 = f4;
    }

    private void internalMethod05611(float f, float f2) {
        float f3 = this.internalMethod00502();
        if (f3 < 1.0E-4f) {
            this.internalField1238[0] = f;
            this.internalField1238[1] = f2;
            return;
        }
        this.internalField1461 += f;
        this.internalField1462 += f2;
        float f4 = (float)Math.round(this.internalField1461 / f3) * f3;
        float f5 = (float)Math.round(this.internalField1462 / f3) * f3;
        this.internalField1461 = MathHelper.clamp((float)(this.internalField1461 - f4), (float)(-f3 * 2.0f), (float)(f3 * 2.0f));
        this.internalField1462 = MathHelper.clamp((float)(this.internalField1462 - f5), (float)(-f3 * 2.0f), (float)(f3 * 2.0f));
        this.internalField1238[0] = f4;
        this.internalField1238[1] = f5;
    }

    private float internalMethod00502() {
        double d = (Double)AimAssistModule.internalField0149.options.getMouseSensitivity().getValue();
        double d2 = d * 0.6 + 0.2;
        double d3 = d2 * d2 * d2 * 8.0;
        return (float)(d3 * 0.15);
    }

    private void internalMethod03565(float f, float f2, float f3, float f4) {
        float f5 = (float)Math.exp(-f4 / 0.04f);
        float f6 = this.internalMethod00507();
        float f7 = this.internalMethod00507();
        float f8 = MathHelper.sqrt((float)(f * f + f2 * f2)) + 1.0E-4f;
        float f9 = f3 * f8 * (1.0f - f5);
        float f10 = f / f8;
        float f11 = f2 / f8;
        float f12 = -f11;
        float f13 = f10;
        float f14 = f6 * f9;
        float f15 = f7 * f9 * 0.4f;
        this.internalField0616[0] = this.internalField0616[0] * f5 + f14 * f10 + f15 * f12;
        this.internalField0616[1] = this.internalField0616[1] * f5 + f14 * f11 + f15 * f13;
    }

    private float internalMethod04416(float f) {
        this.internalField1718 += this.internalField1721 * f;
        this.internalField1719 += this.internalField1722 * f;
        if ((double)this.internalField1718 > Math.PI * 2) {
            this.internalField1718 -= (float)Math.PI * 2;
        }
        if ((double)this.internalField1719 > Math.PI * 2) {
            this.internalField1719 -= (float)Math.PI * 2;
        }
        return this.internalField1720 * (0.6f * (float)Math.sin(this.internalField1718) + 0.4f * (float)Math.sin(this.internalField1719 * 1.618f));
    }

    private float internalMethod00507() {
        if (this.internalField1516) {
            this.internalField1516 = false;
            return this.internalField1851;
        }
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        double d = Math.max(1.0E-10, threadLocalRandom.nextDouble());
        double d2 = threadLocalRandom.nextDouble();
        double d3 = Math.sqrt(-2.0 * Math.log(d));
        this.internalField1851 = (float)(d3 * Math.sin(Math.PI * 2 * d2));
        this.internalField1516 = true;
        return (float)(d3 * Math.cos(Math.PI * 2 * d2));
    }

    private float internalMethod03827(float f, float f2) {
        this.internalField1729 += f2;
        if (this.internalField1729 >= this.internalField1725) {
            this.internalField1102 = !this.internalField1102;
            this.internalField1729 = 0.0f;
            ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
            this.internalField1725 = this.internalField1102 ? this.internalField1724 + threadLocalRandom.nextFloat() * (this.internalField1732 - this.internalField1724) : this.internalField1843 + threadLocalRandom.nextFloat() * (this.internalField1844 - this.internalField1843);
        }
        float f3 = this.internalField1102 ? 7.0f : 2.5f;
        float f4 = this.internalField1102 ? this.internalField1845 : this.internalField1854;
        this.internalField1730 += (f4 - this.internalField1730) * (1.0f - (float)Math.exp(-f3 * f2));
        float f5 = this.internalField1730;
        float f6 = this.internalField1856 * (7.0f / Math.max(this.internalField1141.internalMethod08576(), 1.0f));
        this.internalField1726 = MathHelper.clamp((float)(this.internalField1726 + f2 * f6), (float)0.0f, (float)this.internalField1859);
        f5 += this.internalField1726;
        if (f < this.internalField1857) {
            f5 *= this.internalField1852;
        } else if (f < this.internalField1858) {
            float f7 = (f - this.internalField1857) / (this.internalField1858 - this.internalField1857);
            f5 *= this.internalField1852 + (1.0f - this.internalField1852) * f7;
        }
        return Math.max(f5, 0.0f);
    }

    private void internalMethod08454(float f, float f2) {
        if (this.internalField1101) {
            this.internalField1849 += f2;
            if (this.internalField1849 >= this.internalField1848) {
                this.internalField1101 = false;
                this.internalField1850 = 0.0f;
                this.internalField1855 = 0.0f;
            }
            return;
        }
        if (f < 3.0f && f > 0.5f) {
            if (ThreadLocalRandom.current().nextFloat() < this.internalField1797.internalMethod08576() * f2 * 20.0f) {
                this.internalField1101 = true;
                this.internalField1849 = 0.0f;
                this.internalField1848 = 0.08f + ThreadLocalRandom.current().nextFloat() * 0.12f;
                float f3 = 1.0f + ThreadLocalRandom.current().nextFloat() * 2.0f;
                float f4 = ThreadLocalRandom.current().nextBoolean() ? 1.0f : -1.0f;
                this.internalField1855 = f4 * f3 * f2;
                this.internalField1850 = (ThreadLocalRandom.current().nextFloat() - 0.5f) * f3 * f2 * 0.3f;
            }
        } else {
            this.internalField1850 = 0.0f;
            this.internalField1855 = 0.0f;
        }
    }

    private float internalMethod04483(float f) {
        return -MathHelper.clamp((float)(f * 0.12f), (float)0.0f, (float)1.5f);
    }

    private void internalMethod03448(float f, float f2, float f3, float f4) {
        if (f < 0.1f) {
            this.internalField0276 = false;
            this.internalField1456 = 1.0f;
            return;
        }
        if (!this.internalField0276) {
            return;
        }
        this.internalField1456 += f3 / Math.max(f4 * 0.4f, 0.01f);
        if (this.internalField1456 >= 1.0f || f < f2 * 2.0f) {
            this.internalField0276 = false;
            this.internalField1456 = 1.0f;
        }
    }

    private float internalMethod08425(float f) {
        f = MathHelper.clamp((float)f, (float)0.0f, (float)1.0f);
        return 24.0f * f * f * (1.0f - f) * (1.0f - f);
    }

    private float internalMethod05610(float f, float f2) {
        float f3 = MathHelper.sqrt((float)(f * f + f2 * f2));
        return f3 < 1.0E-4f ? 0.1f : 0.4f + 0.3f * Math.min(f3 / 5.0f, 1.0f);
    }

    private float internalMethod03564(float f, float f2, float f3, float f4) {
        float f5 = Math.abs(f);
        float f6 = f > 0.0f ? 0.4f : 0.8f;
        float f7 = this.internalField1143.internalMethod08576();
        if (f5 > 7.0f) {
            float f8 = this.internalMethod08453(f5, f3);
            float f9 = Math.max(f8 / (this.internalField1141.internalMethod08576() * 0.4f), 0.02f);
            return MathHelper.clamp((float)(Math.signum(f) * (f5 / f9) * 0.5f * f4 * this.internalField1140.internalMethod08576() * f6 * f7), (float)-20.0f, (float)20.0f);
        }
        if (f5 > 0.3f) {
            return MathHelper.clamp((float)(f * 0.6f * f6 * f7 * f4 * 10.0f), (float)-20.0f, (float)20.0f);
        }
        return f * 2.0f * 0.4f * f6 * f7 * f4 * 10.0f;
    }

    private float internalMethod08453(float f, float f2) {
        if (f < 1.0E-4f) {
            return 0.0f;
        }
        return (float)(Math.log((double)(f / Math.max(f2, 0.1f)) + 1.0) / Math.log(2.0));
    }

    private float internalMethod04536(LivingEntity livingEntity) {
        if (livingEntity == null) {
            return 1.0f;
        }
        double d = livingEntity.getX() - this.internalField1042;
        double d2 = livingEntity.getY() + (double)livingEntity.getHeight() * 0.5 - this.internalField1044;
        double d3 = livingEntity.getZ() - this.internalField1453;
        double d4 = d * d + d2 * d2 + d3 * d3;
        double d5 = Math.max(Math.sqrt(d4), 0.5);
        return (float)Math.toDegrees(Math.atan2((double)livingEntity.getWidth() * 0.5, d5));
    }

    private void internalMethod09709() {
        if (!Float.isNaN(this.internalField1455) && AimAssistModule.internalField0149.player != null) {
            this.internalField1731 = MathHelper.wrapDegrees((float)(AimAssistModule.internalField0149.player.getYaw() - this.internalField1455)) - this.internalField1728;
            this.internalField1727 = AimAssistModule.internalField0149.player.getPitch() - this.internalField1723 - this.internalField1717;
        } else {
            this.internalField1727 = 0.0f;
            this.internalField1731 = 0.0f;
        }
    }

    private void internalMethod10018() {
        if (AimAssistModule.internalField0149.player != null) {
            this.internalField1455 = AimAssistModule.internalField0149.player.getYaw();
            this.internalField1723 = AimAssistModule.internalField0149.player.getPitch();
        }
    }

    private float internalMethod02295(float f, float f2, float f3) {
        float f4;
        if (Math.abs(f) < 1.0E-4f) {
            return f;
        }
        float f5 = f4 = f3 > 1.0E-4f ? f2 / f3 : 0.0f;
        if (Math.abs(f4) < 0.1f) {
            return f;
        }
        if (f > 0.0f == f4 > 0.0f) {
            return f * 0.95f;
        }
        float f6 = 1.0f - MathHelper.clamp((float)(Math.abs(f4) / Math.abs(f)), (float)0.0f, (float)0.8f);
        return f * f6;
    }

    private boolean internalMethod02296(float f, float f2, float f3) {
        if (f3 < 1.0E-4f) {
            return false;
        }
        float f4 = this.internalField1731 / f3;
        float f5 = this.internalField1727 / f3;
        float f6 = this.internalField1534.internalMethod08576();
        if (Math.abs(f4) < f6 && Math.abs(f5) < f6) {
            return false;
        }
        boolean bl = Math.signum(f) == Math.signum(f4) || Math.abs(f) < 0.5f;
        boolean bl2 = Math.signum(f2) == Math.signum(f5) || Math.abs(f2) < 0.5f;
        return bl || bl2;
    }

    private void internalMethod07407(LivingEntity livingEntity, double d, double d2, double d3) {
        long l;
        if (!this.internalField1264.internalMethod04496() || livingEntity == null || AimAssistModule.internalField0149.player == null || AimAssistModule.internalField0149.world == null) {
            return;
        }
        long l2 = AimAssistModule.internalField0149.world.getTime();
        if (l2 - this.internalField1058 < (l = Math.max(1L, (long)(this.internalField1530.internalMethod08576() * 20.0f)))) {
            return;
        }
        this.internalField1058 = l2;
        Vec3d vec3d = new Vec3d(d, d2, d3);
        this.internalField0366.internalMethod03148((int)this.internalField1532.internalMethod08576(), this.internalField1531.internalMethod08576(), this.internalField1587.internalMethod04496());
        this.internalField0366.internalMethod07559(vec3d, livingEntity, AimAssistModule.internalField0149.player.getEntityPos(), AimAssistModule.internalField0149.player.distanceTo((Entity)livingEntity));
    }

    private void internalMethod10020() {
        boolean bl;
        if (this.internalField0505 == null || AimAssistModule.internalField0149.player == null || AimAssistModule.internalField0149.world == null) {
            return;
        }
        Entity entity = AimAssistModule.internalField0149.world.getEntityById(this.internalField0505.getId());
        boolean bl2 = bl = entity != this.internalField0505 || this.internalField0505.distanceTo((Entity)AimAssistModule.internalField0149.player) > this.internalField1142.internalMethod08576() + 1.0f || this.internalField0505.isDead() || this.internalField0505.getHealth() <= 0.0f;
        if (bl) {
            if (this.internalField0504 == this.internalField0505) {
                this.internalField0504 = null;
            }
            this.internalField0505 = null;
            this.internalField1100 = false;
            this.internalField0229 = System.currentTimeMillis();
        }
    }

    private void internalMethod10022() {
        if (this.internalMethod09710()) {
            this.internalField0504 = null;
            return;
        }
        long l = System.currentTimeMillis();
        if (this.internalField0504 != null && l - this.internalField0230 > (long)(this.internalField0382.internalMethod08576() * 1000.0f)) {
            this.internalField0504 = null;
        }
        if (this.internalField1272.internalMethod06103(this.internalField1480) && this.internalField0504 == null && AimAssistModule.internalField0149.options.attackKey.isPressed() && this.internalField0505 != null) {
            this.internalField0504 = this.internalField0505;
            this.internalField0230 = l;
        }
        if (this.internalField1272.internalMethod06103(this.internalField1481) && this.internalField0504 == null && this.internalField0505 != null && l - this.internalField0229 < 500L) {
            this.internalField0504 = this.internalField0505;
            this.internalField0230 = l;
        }
        if (this.internalField0504 != null && AimAssistModule.internalField0149.player != null && (this.internalField0504.isDead() || this.internalField0504.getHealth() <= 0.0f || this.internalField0504.distanceTo((Entity)AimAssistModule.internalField0149.player) > this.internalField1142.internalMethod08576())) {
            this.internalField0504 = null;
        }
    }

    private void internalMethod10024() {
        boolean bl;
        LivingEntity livingEntity;
        if (AimAssistModule.internalField0149.player == null) {
            return;
        }
        if (this.internalField0504 != null && !this.internalMethod09710()) {
            this.internalField0505 = this.internalField0504;
            return;
        }
        TargetSelectionManager typedValue182 = RockstarClient.getInstance().internalMethod04463();
        if (typedValue182 == null) {
            return;
        }
        Entity entity = typedValue182.internalMethod04526();
        LivingEntity livingEntity2 = entity instanceof LivingEntity ? (livingEntity = (LivingEntity)entity) : null;
        boolean bl2 = bl = livingEntity2 == null || !livingEntity2.isAlive() || livingEntity2.distanceTo((Entity)AimAssistModule.internalField0149.player) > this.internalField1142.internalMethod08576();
        if (bl) {
            LivingEntity livingEntity3;
            typedValue182.internalMethod05014(this.internalMethod05130());
            Entity entity2 = typedValue182.internalMethod04526();
            LivingEntity livingEntity4 = livingEntity2 = entity2 instanceof LivingEntity ? (livingEntity3 = (LivingEntity)entity2) : null;
        }
        if (livingEntity2 != this.internalField0505) {
            this.internalMethod04537(livingEntity2);
        }
    }

    private void internalMethod04537(LivingEntity livingEntity) {
        this.internalField0505 = livingEntity;
        this.internalField0229 = System.currentTimeMillis();
        this.internalField1100 = false;
        this.internalField1043 = 0.0;
        this.internalField1045 = 0.0;
        this.internalField0193 = 0.0;
        this.internalField1457 = 0.0f;
        this.internalField1099 = false;
        this.internalField1458 = this.internalField0505 != null ? this.internalMethod05579(this.internalField0505) : 0.0f;
        this.internalField0276 = true;
        this.internalField1456 = 0.0f;
        this.internalField1058 = 0L;
        this.internalField0366.internalMethod06585();
        this.internalField0558.internalMethod07114();
        this.internalField1101 = false;
        this.internalField1850 = 0.0f;
        this.internalField1855 = 0.0f;
        this.internalField1460 = 0.0f;
        this.internalField1459 = 0.0f;
        this.internalField0616[1] = 0.0f;
        this.internalField0616[0] = 0.0f;
    }

    private float internalMethod05579(LivingEntity livingEntity) {
        double d;
        double d2;
        float f = 0.5f;
        if (AimAssistModule.internalField0149.player == null || livingEntity == null) {
            return f;
        }
        double d3 = livingEntity.getX() - AimAssistModule.internalField0149.player.getX();
        double d4 = d3 * d3 + (d2 = livingEntity.getY() - AimAssistModule.internalField0149.player.getY()) * d2 + (d = livingEntity.getZ() - AimAssistModule.internalField0149.player.getZ()) * d;
        if (d4 > (double)1.0E-4f) {
            double d5 = Math.sqrt(d4);
            Vec3d vec3d = AimAssistModule.internalField0149.player.getRotationVector();
            float f2 = (float)((d3 * vec3d.x + d2 * vec3d.y + d * vec3d.z) / d5);
            f += (1.0f - MathHelper.clamp((float)f2, (float)-1.0f, (float)1.0f)) * 0.08f;
        }
        return f;
    }

    private EntityTargetFilter internalMethod05130() {
        return new EntityTargetFilter.InternalType0309().internalMethod00547(this.internalField0245.isSelected()).internalMethod06455(this.internalField0244.isSelected()).internalMethod08543(this.internalField1075.isSelected()).internalMethod07990(this.internalField1074.isSelected()).internalMethod09114(this.internalField1073.isSelected()).internalMethod09525(this.internalField1491.isSelected()).internalMethod08126(this.internalField1072.isSelected()).internalMethod03468(this.internalField1142.internalMethod08576()).internalMethod04109(this.internalField0669.internalMethod06103(this.internalField1067) ? EntityComparatorUtils.internalField0758 : (this.internalField0669.internalMethod06103(this.internalField1068) ? EntityComparatorUtils.internalField1302 : EntityComparatorUtils.internalField0757)).internalMethod03528();
    }

    private boolean internalMethod10023() {
        if (this.internalField0505 == null || AimAssistModule.internalField0149.player == null) {
            return true;
        }
        return this.internalField0650.internalMethod04496() && !this.internalField1099;
    }

    private float internalMethod04418(long l) {
        if (this.internalField1059 == 0L) {
            return 0.016666668f;
        }
        long l2 = l - this.internalField1059;
        if (l2 <= 0L) {
            return 0.016666668f;
        }
        return Math.min((float)l2 / 1.0E9f, 0.1f);
    }

    private float internalMethod08527() {
        try {
            return internalField0149.getRenderTickCounter().getTickProgress(false);
        }
        catch (NoSuchMethodError noSuchMethodError) {
            return 1.0f;
        }
    }

    private float internalMethod07247(float f, float f2, float f3) {
        return f + (f2 - f) * f3;
    }

    private boolean internalMethod10025() {
        return AimAssistModule.internalField0149.player == null || AimAssistModule.internalField0149.world == null || AimAssistModule.internalField0149.player.isDead();
    }

    public boolean internalMethod09564() {
        if (AimAssistModule.internalField0149.player == null) {
            return false;
        }
        ItemStack itemStack = AimAssistModule.internalField0149.player.getStackInHand(Hand.MAIN_HAND);
        if (itemStack.isEmpty()) {
            return false;
        }
        Item item = itemStack.getItem();
        try {
            RegistryEntry.Reference reference = item.getRegistryEntry();
            if (reference != null) {
                String string = reference.getIdAsString();
                for (String string2 : internalField0546) {
                    if (!string.contains(string2)) continue;
                    return true;
                }
            }
        }
        catch (Exception exception) {
            String string = item.getName().getString().toLowerCase();
            for (String string3 : internalField0546) {
                if (!string.contains(string3)) continue;
                return true;
            }
        }
        return false;
    }

    @Generated
    public NeuralRotationHelper internalMethod07497() {
        return this.internalField0323;
    }
}
