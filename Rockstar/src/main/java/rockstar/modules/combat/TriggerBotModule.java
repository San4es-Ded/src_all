package rockstar.modules.combat;









import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.server.*;
import rockstar.client.rotation.*;
import rockstar.client.module.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.internal.game.*;
import rockstar.client.*;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.consume.UseAction;
import net.minecraft.util.Hand;
import rockstar.client.setting.SettingOwner;
import rockstar.client.setting.BooleanSetting;
import rockstar.client.setting.ModeSetting;
import rockstar.client.setting.MultiSelectSetting;
import rockstar.client.internal.inventory.EntityTargetFilter;
import rockstar.client.module.ModuleCategory;
import rockstar.client.RockstarClient;
import rockstar.client.module.ModuleInfo;
import rockstar.client.util.CombatUtils;
import rockstar.client.internal.game.CriticalHitUtils;
import rockstar.client.internal.game.SprintResetManager;
import rockstar.client.internal.rotation.PlayerFallSimulator;
import rockstar.client.server.KnownServer;
import rockstar.client.server.ServerUtils;
import rockstar.client.util.MathUtils;
import rockstar.client.module.Module;
import rockstar.client.util.Stopwatch;

@ModuleInfo(name="Trigger Bot", category=ModuleCategory.COMBAT, internalMethod09633="modules.descriptions.trigger_bot")
public class TriggerBotModule
extends Module {
    private BooleanSetting internalField0650;
    private BooleanSetting internalField0651;
    private BooleanSetting internalField1261;
    private MultiSelectSetting internalField0675;
    private MultiSelectSetting.InternalType0091 internalField0245;
    private MultiSelectSetting.InternalType0091 internalField0244;
    private MultiSelectSetting.InternalType0091 internalField1075;
    private MultiSelectSetting.InternalType0091 internalField1074;
    private MultiSelectSetting.InternalType0091 internalField1073;
    private MultiSelectSetting.InternalType0091 internalField1072;
    private MultiSelectSetting.InternalType0091 internalField1491;
    private ModeSetting internalField0668;
    private ModeSetting.InternalType0088 internalField0237;
    private ModeSetting.InternalType0088 internalField0238;
    private ModeSetting.InternalType0088 internalField1066;
    private final Stopwatch internalField0519 = new Stopwatch();

    public TriggerBotModule() {
        this.internalMethod09516();
    }

    private void internalMethod09516() {
        this.internalField0650 = new BooleanSetting(this, "modules.settings.aura.onlyCrits").internalMethod06630();
        this.internalField0651 = new BooleanSetting((SettingOwner)this, "modules.settings.aura.smart_criticals", () -> !this.internalField0650.internalMethod04496());
        this.internalField1261 = new BooleanSetting(this, "modules.settings.aura.useHit");
        this.internalField0675 = new MultiSelectSetting(this, "modules.settings.aura.targets");
        this.internalField0245 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.aura.targets.players").select();
        this.internalField0244 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.aura.targets.animals").select();
        this.internalField1075 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.aura.targets.mobs").select();
        this.internalField1074 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.aura.targets.invisibles").select();
        this.internalField1073 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.aura.targets.nakedPlayers").select();
        this.internalField1072 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.aura.targets.rockUsers");
        this.internalField1491 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.aura.targets.friends");
        this.internalField0668 = new ModeSetting(this, "modules.settings.aura.sprint_reset");
        this.internalField0237 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.aura.sprint_reset.smart").select();
        this.internalField0238 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.aura.sprint_reset.normal");
        this.internalField1066 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.aura.sprint_reset.packet");
    }

    @Override
    public void internalMethod08229() {
        if (TriggerBotModule.internalField0149.player == null || TriggerBotModule.internalField0149.interactionManager == null) {
            return;
        }
        if (this.internalMethod09518()) {
            super.internalMethod08229();
            return;
        }
        Entity entity = TriggerBotModule.internalField0149.targetedEntity;
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)entity;
            if (this.internalMethod01325().internalMethod05417((Entity)livingEntity)) {
                if (SprintResetManager.internalMethod03032()) {
                    super.internalMethod08229();
                    return;
                }
                if (this.internalMethod09662() && CriticalHitUtils.internalMethod05020((PlayerEntity)TriggerBotModule.internalField0149.player)) {
                    SprintResetManager.internalMethod04017((PlayerEntity)TriggerBotModule.internalField0149.player);
                    super.internalMethod08229();
                    return;
                }
                if (this.internalMethod07155(livingEntity)) {
                    if (this.internalMethod08268(livingEntity)) {
                        super.internalMethod08229();
                        return;
                    }
                    this.internalMethod07154(livingEntity);
                }
            }
        }
        super.internalMethod08229();
    }

    private boolean internalMethod07155(LivingEntity livingEntity) {
        if (TriggerBotModule.internalField0149.player == null) {
            return false;
        }
        if (this.internalMethod09518()) {
            return false;
        }
        if (AntiBotModule.internalMethod07596(livingEntity)) {
            return false;
        }
        if (livingEntity == TriggerBotModule.internalField0149.player || livingEntity.isRemoved() || !livingEntity.isAlive()) {
            return false;
        }
        if (!this.internalMethod01325().internalMethod05417((Entity)livingEntity)) {
            return false;
        }
        if (TriggerBotModule.internalField0149.player.getAttackCooldownProgress(0.0f) < 0.8f || !this.internalField0519.internalMethod02365(500L)) {
            return false;
        }
        CriticalsModule typedValue118 = RockstarClient.getInstance().getModuleManager().getModule(CriticalsModule.class);
        if (typedValue118.internalMethod09260() && !typedValue118.internalMethod09913()) {
            return false;
        }
        return !this.internalMethod09662() || !this.internalMethod00017(livingEntity) || CombatUtils.internalMethod06040(livingEntity, true);
    }

    private boolean internalMethod09518() {
        if (!this.internalField1261.internalMethod04496() || TriggerBotModule.internalField0149.player == null || !TriggerBotModule.internalField0149.player.isUsingItem()) {
            return false;
        }
        return TriggerBotModule.internalField0149.player.getActiveItem().getItem().getUseAction(TriggerBotModule.internalField0149.player.getActiveItem()) == UseAction.EAT;
    }

    private EntityTargetFilter internalMethod01325() {
        return new EntityTargetFilter.InternalType0309().internalMethod00547(this.internalField0245.isSelected()).internalMethod06455(this.internalField0244.isSelected()).internalMethod08543(this.internalField1075.isSelected()).internalMethod07990(this.internalField1074.isSelected()).internalMethod09114(this.internalField1073.isSelected()).internalMethod09525(this.internalField1491.isSelected()).internalMethod08126(this.internalField1072.isSelected()).internalMethod09220(false).internalMethod03528();
    }

    private boolean internalMethod00017(LivingEntity livingEntity) {
        float f = RockstarClient.getInstance().getModuleManager().getModule(AuraModule.class).internalMethod04184(livingEntity);
        return f <= livingEntity.getHealth();
    }

    private void internalMethod07154(LivingEntity livingEntity) {
        TriggerBotModule.internalField0149.interactionManager.attackEntity((PlayerEntity)TriggerBotModule.internalField0149.player, (Entity)livingEntity);
        TriggerBotModule.internalField0149.player.swingHand(Hand.MAIN_HAND);
        this.internalField0519.internalMethod00701();
    }

    private boolean internalMethod09662() {
        if (this.internalField0651.internalMethod04496()) {
            return TriggerBotModule.internalField0149.options != null && TriggerBotModule.internalField0149.options.jumpKey.isPressed() || !TriggerBotModule.internalField0149.player.isOnGround();
        }
        return this.internalField0650.internalMethod04496();
    }

    private boolean internalMethod08268(LivingEntity livingEntity) {
        boolean bl = this.internalField0668.internalMethod06103(this.internalField0238);
        boolean bl2 = this.internalField0668.internalMethod06103(this.internalField1066);
        if (RockstarClient.getInstance().getModuleManager().getModule(KnockbackTweaksModule.class).isEnabled()) {
            return false;
        }
        if (!bl && !bl2 || TriggerBotModule.internalField0149.player == null) {
            return false;
        }
        if (SprintResetManager.internalMethod03032() || SprintResetManager.internalMethod06977((Entity)TriggerBotModule.internalField0149.player)) {
            return true;
        }
        if (!TriggerBotModule.internalField0149.player.isSprinting()) {
            SprintResetManager.internalMethod04865((Entity)TriggerBotModule.internalField0149.player);
            return false;
        }
        SprintResetManager.internalMethod06906((PlayerEntity)TriggerBotModule.internalField0149.player, () -> this.internalMethod00016(livingEntity), bl2);
        return true;
    }

    private void internalMethod00016(LivingEntity livingEntity) {
        if (!this.isEnabled() || TriggerBotModule.internalField0149.player == null || TriggerBotModule.internalField0149.interactionManager == null || livingEntity == null || livingEntity.isRemoved() || !livingEntity.isAlive()) {
            return;
        }
        if (this.internalMethod07155(livingEntity)) {
            this.internalMethod07154(livingEntity);
        }
    }

    public boolean internalMethod09517() {
        boolean bl;
        LivingEntity livingEntity;
        Object object;
        block8: {
            block7: {
                if (!this.internalField0668.internalMethod06103(this.internalField0237)) {
                    return false;
                }
                if (RockstarClient.getInstance().getModuleManager().getModule(KnockbackTweaksModule.class).isEnabled()) {
                    return false;
                }
                object = TriggerBotModule.internalField0149.targetedEntity;
                if (!(object instanceof LivingEntity)) break block7;
                livingEntity = (LivingEntity)object;
                if (TriggerBotModule.internalField0149.player != null) break block8;
            }
            return false;
        }
        if (!this.internalMethod01325().internalMethod05417((Entity)livingEntity)) {
            return false;
        }
        if (TriggerBotModule.internalField0149.player.isSubmergedInWater()) {
            return false;
        }
        object = RockstarClient.getInstance().getModuleManager().getModule(CriticalsModule.class);
        bl = ((CriticalsModule)object).internalMethod09260()
            && (
                ((CriticalsModule)object).internalMethod09914() && this.internalField0519.internalMethod02365(500L)
                    || TriggerBotModule.internalField0149.player.isOnGround()
                    || !TriggerBotModule.internalField0149.player.isOnGround()
                        && PlayerFallSimulator.internalMethod02421(TriggerBotModule.internalField0149.player)
                            .internalMethod04604(
                                CombatUtils.internalMethod03105(livingEntity),
                                ServerUtils.internalMethod01786(KnownServer.internalField0578)
                                        || ServerUtils.internalMethod01786(KnownServer.internalField1567)
                                        || ServerUtils.internalMethod08700()
                                    ? MathUtils.internalField0858.nextInt(3)
                                    : 1
                            )
            );
        return this.internalMethod09662() && this.internalMethod00017(livingEntity) && (bl || CombatUtils.internalMethod06040(livingEntity, true) || !this.internalField0519.internalMethod02365(ServerUtils.internalMethod01786(KnownServer.internalField0579) || ServerUtils.internalMethod08700() ? (long)MathUtils.internalMethod07919(50.0f, 150.0f) : 50L));
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (TriggerBotModule.internalField0149.player != null) {
            SprintResetManager.internalMethod04865((Entity)TriggerBotModule.internalField0149.player);
        }
    }
}
