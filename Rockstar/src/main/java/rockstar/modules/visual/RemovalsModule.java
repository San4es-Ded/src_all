package rockstar.modules.visual;





import rockstar.client.setting.*;
import rockstar.client.module.*;
import rockstar.client.event.*;
import rockstar.client.internal.script.*;
import rockstar.client.*;

import lombok.Generated;
import pyrock.events.player.ClientPlayerTickEvent;
import pyrock.events.render.Render3DEvent;
import rockstar.modules.player.FreeCameraModule;
import rockstar.client.setting.SettingOwner;
import rockstar.client.setting.BooleanSetting;
import rockstar.client.setting.MultiSelectSetting;
import rockstar.client.event.EventListener;
import rockstar.client.module.ModuleCategory;
import rockstar.client.RockstarClient;
import rockstar.client.module.ModuleInfo;
import rockstar.client.internal.script.GhostBlockRenderer;
import rockstar.client.module.Module;

@ModuleInfo(name="Removals", category=ModuleCategory.VISUALS, internalMethod08049=true, internalMethod09633="modules.descriptions.removals")
public class RemovalsModule
extends Module {
    private double internalField0194;
    private MultiSelectSetting internalField0675;
    private MultiSelectSetting.InternalType0091 internalField0245;
    private MultiSelectSetting.InternalType0091 internalField0244;
    private MultiSelectSetting.InternalType0091 internalField1075;
    private MultiSelectSetting.InternalType0091 internalField1074;
    private MultiSelectSetting.InternalType0091 internalField1073;
    private MultiSelectSetting.InternalType0091 internalField1072;
    private MultiSelectSetting.InternalType0091 internalField1491;
    private MultiSelectSetting.InternalType0091 internalField1488;
    private MultiSelectSetting.InternalType0091 internalField1490;
    private MultiSelectSetting.InternalType0091 internalField1489;
    private MultiSelectSetting.InternalType0091 internalField1493;
    private MultiSelectSetting.InternalType0091 internalField1487;
    private MultiSelectSetting.InternalType0091 internalField1486;
    private MultiSelectSetting.InternalType0091 internalField1492;
    private MultiSelectSetting.InternalType0091 internalField1764;
    private MultiSelectSetting.InternalType0091 internalField1765;
    private BooleanSetting internalField0650;
    private MultiSelectSetting internalField0674;
    private MultiSelectSetting.InternalType0091 internalField1766;
    private MultiSelectSetting.InternalType0091 internalField1769;
    private MultiSelectSetting.InternalType0091 internalField1770;
    private MultiSelectSetting.InternalType0091 internalField1767;
    private MultiSelectSetting.InternalType0091 internalField1768;
    private final EventListener<ClientPlayerTickEvent> internalField0157 = clientPlayerTickEvent -> {
        if (this.internalField1492.isSelected()) {
            RemovalsModule.internalField0149.options.getFovEffectScale().setValue(0.0);
        }
    };
    private final EventListener<Render3DEvent> internalField0158 = render3DEvent -> {
        boolean bl = RockstarClient.getInstance().getModuleManager().getModule(FreeCameraModule.class).internalMethod09312();
        GhostBlockRenderer.internalMethod04614(render3DEvent.getTickDelta(), this.isEnabled() && this.internalField1072.isSelected() && this.internalField0650.internalMethod04496() && !bl);
        GhostBlockRenderer.internalMethod04333(render3DEvent);
    };

    public RemovalsModule() {
        this.internalMethod09208();
    }

    private void internalMethod09208() {
        this.internalField0675 = new MultiSelectSetting(this, "modules.settings.removals.effects");
        this.internalField0245 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.removals.hurtCam").select();
        this.internalField0244 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.removals.scoreboard");
        this.internalField1075 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.removals.bossBar");
        this.internalField1074 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.removals.portal").select();
        this.internalField1073 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.removals.fire").select();
        this.internalField1072 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.removals.clip").select();
        this.internalField1491 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.removals.breakParticles");
        this.internalField1488 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.removals.totem").select();
        this.internalField1490 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.removals.water");
        this.internalField1489 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.removals.nausea").select();
        this.internalField1493 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.removals.blindness").select();
        this.internalField1487 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.removals.darkness").select();
        this.internalField1486 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.removals.pumpkin").select();
        this.internalField1492 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.removals.fov").select();
        this.internalField1764 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.removals.weather").select();
        this.internalField1765 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.removals.glowing");
        this.internalField0650 = new BooleanSetting((SettingOwner)this, "modules.settings.removals.clipTransparency", () -> !this.internalField1072.isSelected()).internalMethod06630();
        this.internalField0674 = new MultiSelectSetting(this, "modules.settings.removals.sounds");
        this.internalField1766 = new MultiSelectSetting.InternalType0091(this.internalField0674, "modules.settings.removals.beacon").select();
        this.internalField1769 = new MultiSelectSetting.InternalType0091(this.internalField0674, "modules.settings.removals.phantoms").select();
        this.internalField1770 = new MultiSelectSetting.InternalType0091(this.internalField0674, "modules.settings.removals.weatherSound").select();
        this.internalField1767 = new MultiSelectSetting.InternalType0091(this.internalField0674, "modules.settings.removals.waterSound");
        this.internalField1768 = new MultiSelectSetting.InternalType0091(this.internalField0674, "modules.settings.removals.lavaSound");
    }

    @Override
    public void onEnable() {
        this.internalField0194 = (Double)RemovalsModule.internalField0149.options.getFovEffectScale().getValue();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        RemovalsModule.internalField0149.options.getFovEffectScale().setValue(this.internalField0194);
        GhostBlockRenderer.internalMethod05215();
        super.onDisable();
    }

    @Generated
    public double internalMethod05873() {
        return this.internalField0194;
    }

    @Generated
    public MultiSelectSetting internalMethod05724() {
        return this.internalField0675;
    }

    @Generated
    public MultiSelectSetting.InternalType0091 internalMethod05278() {
        return this.internalField0245;
    }

    @Generated
    public MultiSelectSetting.InternalType0091 internalMethod05450() {
        return this.internalField0244;
    }

    @Generated
    public MultiSelectSetting.InternalType0091 internalMethod08477() {
        return this.internalField1075;
    }

    @Generated
    public MultiSelectSetting.InternalType0091 internalMethod08525() {
        return this.internalField1074;
    }

    @Generated
    public MultiSelectSetting.InternalType0091 internalMethod08070() {
        return this.internalField1073;
    }

    @Generated
    public MultiSelectSetting.InternalType0091 internalMethod08111() {
        return this.internalField1072;
    }

    @Generated
    public MultiSelectSetting.InternalType0091 internalMethod09705() {
        return this.internalField1491;
    }

    @Generated
    public MultiSelectSetting.InternalType0091 internalMethod09727() {
        return this.internalField1488;
    }

    @Generated
    public MultiSelectSetting.InternalType0091 internalMethod09499() {
        return this.internalField1490;
    }

    @Generated
    public MultiSelectSetting.InternalType0091 internalMethod09519() {
        return this.internalField1489;
    }

    @Generated
    public MultiSelectSetting.InternalType0091 internalMethod09435() {
        return this.internalField1493;
    }

    @Generated
    public MultiSelectSetting.InternalType0091 internalMethod09454() {
        return this.internalField1487;
    }

    @Generated
    public MultiSelectSetting.InternalType0091 internalMethod09196() {
        return this.internalField1486;
    }

    @Generated
    public MultiSelectSetting.InternalType0091 internalMethod09215() {
        return this.internalField1492;
    }

    @Generated
    public MultiSelectSetting.InternalType0091 internalMethod09965() {
        return this.internalField1764;
    }

    @Generated
    public MultiSelectSetting.InternalType0091 internalMethod09969() {
        return this.internalField1765;
    }

    @Generated
    public BooleanSetting internalMethod05665() {
        return this.internalField0650;
    }

    @Generated
    public MultiSelectSetting internalMethod06360() {
        return this.internalField0674;
    }

    @Generated
    public MultiSelectSetting.InternalType0091 internalMethod10128() {
        return this.internalField1766;
    }

    @Generated
    public MultiSelectSetting.InternalType0091 internalMethod10129() {
        return this.internalField1769;
    }

    @Generated
    public MultiSelectSetting.InternalType0091 internalMethod10103() {
        return this.internalField1770;
    }

    @Generated
    public MultiSelectSetting.InternalType0091 internalMethod10104() {
        return this.internalField1767;
    }

    @Generated
    public MultiSelectSetting.InternalType0091 internalMethod10036() {
        return this.internalField1768;
    }

    @Generated
    public EventListener<ClientPlayerTickEvent> internalMethod07519() {
        return this.internalField0157;
    }

    @Generated
    public EventListener<Render3DEvent> internalMethod00761() {
        return this.internalField0158;
    }
}
