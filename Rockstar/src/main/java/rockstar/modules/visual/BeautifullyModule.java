package rockstar.modules.visual;






import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.module.*;
import rockstar.client.event.*;
import rockstar.client.animation.*;
import rockstar.client.*;

import lombok.Generated;
import moscow.rockstar.mixin.accessors.CameraAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import pyrock.events.render.CameraUpdateEvent;
import rockstar.client.setting.SettingOwner;
import rockstar.client.setting.MultiSelectSetting;
import rockstar.client.setting.SliderSetting;
import rockstar.client.event.EventListener;
import rockstar.client.module.ModuleCategory;
import rockstar.client.RockstarClient;
import rockstar.client.module.ModuleInfo;
import rockstar.client.animation.AnimatedValue;
import rockstar.client.animation.Easing;
import rockstar.client.util.MathUtils;
import rockstar.client.module.Module;

@ModuleInfo(name="Beautifully", category=ModuleCategory.VISUALS, internalMethod08049=true)
public class BeautifullyModule
extends Module {
    public static final long internalField0229 = 200L;
    private MultiSelectSetting internalField0675;
    private MultiSelectSetting.InternalType0091 internalField0245;
    private MultiSelectSetting.InternalType0091 internalField0244;
    private MultiSelectSetting.InternalType0091 internalField1075;
    private MultiSelectSetting.InternalType0091 internalField1074;
    private MultiSelectSetting.InternalType0091 internalField1073;
    private MultiSelectSetting.InternalType0091 internalField1072;
    private SliderSetting internalField0383;
    private final AnimatedValue internalField0808 = new AnimatedValue(300L, Easing.internalField1818);
    private final AnimatedValue internalField0809 = new AnimatedValue(300L, Easing.internalField1818);
    private final EventListener<CameraUpdateEvent> internalField0157 = cameraUpdateEvent -> {
        if (!this.internalField0245.isSelected()) {
            this.internalField0808.internalMethod07060(cameraUpdateEvent.isThirdPerson() ? 1.0f : 0.0f);
            this.internalField0809.internalMethod07060(cameraUpdateEvent.isThirdPerson() && cameraUpdateEvent.isInverseView() ? 1.0f : 0.0f);
            return;
        }
        boolean bl = cameraUpdateEvent.isThirdPerson();
        boolean bl2 = cameraUpdateEvent.isInverseView();
        float f = cameraUpdateEvent.getTickDelta();
        this.internalField0808.internalMethod06645(bl ? Easing.internalMethod05127(0.31, 0.87, 0.41, 1.3) : Easing.internalMethod05127(0.17, 0.85, 0.29, 0.99));
        this.internalField0808.internalMethod07061(400L);
        this.internalField0809.internalMethod06645(Easing.internalMethod05127(0.31, 0.87, 0.43, 0.94));
        Entity entity = cameraUpdateEvent.getFocusedEntity();
        float f2 = this.internalField0808.internalMethod07059(bl ? 1.0f : 0.0f);
        float f3 = this.internalField0809.internalMethod07059(bl && bl2 ? 1.0f : 0.0f);
        if (this.internalField0808.internalMethod02884() && this.internalField0809.internalMethod02884()) {
            return;
        }
        CameraAccessor cameraAccessor = (CameraAccessor)(Object)cameraUpdateEvent.getCamera();
        double d = MathHelper.lerp((double)f, (double)entity.lastX, (double)entity.getX());
        double d2 = MathHelper.lerp((double)f, (double)entity.lastY, (double)entity.getY()) + (double)MathHelper.lerp((float)f, (float)cameraAccessor.getLastCameraY(), (float)cameraAccessor.getCameraY());
        double d3 = MathHelper.lerp((double)f, (double)entity.lastZ, (double)entity.getZ());
        cameraAccessor.invokeSetPos(new Vec3d(d, d2, d3));
        cameraAccessor.setThirdPerson(bl || f2 >= 0.1f);
        if (f2 > 0.001f) {
            float f4;
            cameraAccessor.invokeSetRotation(entity.getYaw(f), entity.getPitch(f));
            if (bl2 || f3 > 0.001f) {
                f4 = bl2 ? 180.0f * f3 : -180.0f * f3;
                cameraAccessor.invokeSetRotation(cameraUpdateEvent.getCamera().getYaw() + f4, MathUtils.internalMethod02587(cameraUpdateEvent.getCamera().getPitch(), -cameraUpdateEvent.getCamera().getPitch(), this.internalField0809.internalMethod02881()));
            }
            f4 = entity instanceof LivingEntity ? ((LivingEntity)entity).getScale() : 1.0f;
            cameraAccessor.invokeMoveBy(-cameraAccessor.invokeClipToSpace(4.0f * f4 * f2), 0.0f, 0.0f);
        }
    };

    public BeautifullyModule() {
        this.internalMethod09645();
    }

    private void internalMethod09645() {
        this.internalField0675 = new MultiSelectSetting(this, "modules.settings.beautifully.select");
        this.internalField0245 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.beautifully.smooth_f5").select();
        this.internalField0244 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.beautifully.chat_animation").select();
        this.internalField1075 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.beautifully.tab_animation").select();
        this.internalField1074 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.beautifully.inventory_animation").select();
        this.internalField1073 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.beautifully.chat_history", "modules.settings.beautifully.chat_history.desc");
        this.internalField1072 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.beautifully.custom_tab_columns", "modules.settings.beautifully.custom_tab_columns.desc");
        this.internalField0383 = new SliderSetting((SettingOwner)this, "modules.settings.beautifully.tab_columns", () -> !this.internalField1072.isSelected()).internalMethod05900(1.0f).internalMethod02732(5.0f).internalMethod08673(1.0f).internalMethod08074(2.0f);
    }

    private static BeautifullyModule internalMethod06904() {
        if (RockstarClient.getInstance() == null || RockstarClient.getInstance().getModuleManager() == null) {
            return null;
        }
        BeautifullyModule typedValue318 = RockstarClient.getInstance().getModuleManager().getModule(BeautifullyModule.class);
        return typedValue318 != null && typedValue318.isEnabled() ? typedValue318 : null;
    }

    public static boolean internalMethod09646() {
        BeautifullyModule typedValue318 = BeautifullyModule.internalMethod06904();
        return typedValue318 != null && typedValue318.internalField0244.isSelected();
    }

    public static boolean internalMethod09647() {
        BeautifullyModule typedValue318 = BeautifullyModule.internalMethod06904();
        return typedValue318 != null && typedValue318.internalField1075.isSelected();
    }

    public static boolean internalMethod09825() {
        BeautifullyModule typedValue318 = BeautifullyModule.internalMethod06904();
        return typedValue318 != null && typedValue318.internalField1074.isSelected();
    }

    public static boolean internalMethod09826() {
        BeautifullyModule typedValue318 = BeautifullyModule.internalMethod06904();
        return typedValue318 != null && typedValue318.internalField1073.isSelected();
    }

    public static int internalMethod08757() {
        BeautifullyModule typedValue318 = BeautifullyModule.internalMethod06904();
        if (typedValue318 == null || !typedValue318.internalField1072.isSelected()) {
            return 0;
        }
        return (int)typedValue318.internalField0383.internalMethod08576();
    }

    @Generated
    public MultiSelectSetting internalMethod06775() {
        return this.internalField0675;
    }

    @Generated
    public MultiSelectSetting.InternalType0091 internalMethod05320() {
        return this.internalField0245;
    }

    @Generated
    public MultiSelectSetting.InternalType0091 internalMethod05496() {
        return this.internalField0244;
    }

    @Generated
    public MultiSelectSetting.InternalType0091 internalMethod08798() {
        return this.internalField1075;
    }

    @Generated
    public MultiSelectSetting.InternalType0091 internalMethod08837() {
        return this.internalField1074;
    }

    @Generated
    public MultiSelectSetting.InternalType0091 internalMethod08374() {
        return this.internalField1073;
    }

    @Generated
    public MultiSelectSetting.InternalType0091 internalMethod08405() {
        return this.internalField1072;
    }

    @Generated
    public SliderSetting internalMethod04488() {
        return this.internalField0383;
    }

    @Generated
    public AnimatedValue internalMethod01947() {
        return this.internalField0808;
    }

    @Generated
    public AnimatedValue internalMethod08176() {
        return this.internalField0809;
    }

    @Generated
    public EventListener<CameraUpdateEvent> internalMethod03320() {
        return this.internalField0157;
    }
}
