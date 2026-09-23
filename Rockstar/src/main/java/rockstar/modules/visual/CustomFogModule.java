package rockstar.modules.visual;




import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.module.*;
import rockstar.client.*;

import lombok.Generated;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import rockstar.client.setting.SettingOwner;
import rockstar.client.setting.BooleanSetting;
import rockstar.client.setting.ColorSetting;
import rockstar.client.setting.RangeSetting;
import rockstar.client.setting.SliderSetting;
import rockstar.client.module.ModuleCategory;
import rockstar.client.module.ModuleInfo;
import rockstar.client.ui.ThemeColors;
import rockstar.client.module.Module;

@ModuleInfo(name="Custom Fog", category=ModuleCategory.VISUALS, internalMethod08049=true)
public class CustomFogModule
extends Module {
    private RangeSetting internalField0672;
    private BooleanSetting internalField0650;
    private SliderSetting internalField0383;
    private ColorSetting internalField0665;

    public CustomFogModule() {
        this.internalMethod09304();
    }

    private void internalMethod09304() {
        this.internalField0672 = new RangeSetting(this, "modules.settings.custom_fog.distance").internalMethod08834(1.0f).internalMethod08219(100.0f).internalMethod08853(1.0f).internalMethod08255(40.0f).internalMethod01407(1.0f).internalMethod06328(100.0f);
        this.internalField0650 = new BooleanSetting(this, "theme.sync").internalMethod06630();
        this.internalField0383 = new SliderSetting((SettingOwner)this, "modules.settings.custom_fog.sync_alpha", () -> !this.internalField0650.internalMethod04496()).internalMethod06240("%").internalMethod05900(0.0f).internalMethod02732(100.0f).internalMethod08673(1.0f).internalMethod08074(46.0f);
        this.internalField0665 = new ColorSetting(this, "modules.settings.custom_fog.color", this.internalField0650::internalMethod04496).internalMethod04886(ThemeColors.internalMethod02531().withAlpha(118.0f)).internalMethod05166(true);
    }

    public boolean internalMethod00459(Camera camera) {
        if (!this.isEnabled() || CustomFogModule.internalField0149.world == null || CustomFogModule.internalField0149.player == null) {
            return false;
        }
        Entity entity = camera.getFocusedEntity();
        if (camera.getSubmersionType() == CameraSubmersionType.WATER) {
            return false;
        }
        if (camera.getSubmersionType() == CameraSubmersionType.LAVA) {
            return false;
        }
        if (camera.getSubmersionType() == CameraSubmersionType.POWDER_SNOW) {
            return false;
        }
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)entity;
            if (livingEntity.hasStatusEffect(StatusEffects.BLINDNESS)) {
                return false;
            }
            if (livingEntity.hasStatusEffect(StatusEffects.NIGHT_VISION)) {
                return false;
            }
        }
        return true;
    }

    @Generated
    public RangeSetting internalMethod02398() {
        return this.internalField0672;
    }

    @Generated
    public BooleanSetting internalMethod02354() {
        return this.internalField0650;
    }

    @Generated
    public SliderSetting internalMethod00043() {
        return this.internalField0383;
    }

    @Generated
    public ColorSetting internalMethod02396() {
        return this.internalField0665;
    }
}
