package rockstar.client.internal.framework;




import rockstar.client.setting.*;
import rockstar.client.internal.ui.*;
import rockstar.client.*;
import lombok.Generated;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;
import rockstar.client.setting.Setting;
import rockstar.client.setting.SettingOwner;
import rockstar.client.setting.SliderSetting;
import rockstar.client.RockstarClient;
import rockstar.client.internal.ui.SwingPresetEditorScreen;
import rockstar.client.internal.framework.SettingGroupBase;

public class SwingTransformSettings
extends SettingGroupBase {
    private final SliderSetting internalField0383 = new InternalType0500(this, "swing.anchorX").internalMethod08673(0.05f).internalMethod05900(-5.0f).internalMethod02732(5.0f).internalMethod08074(0.0f);
    private final SliderSetting internalField0382 = new InternalType0500(this, "swing.anchorY").internalMethod08673(0.05f).internalMethod05900(-5.0f).internalMethod02732(5.0f).internalMethod08074(0.0f);
    private final SliderSetting internalField1142 = new InternalType0500(this, "swing.anchorZ").internalMethod08673(0.05f).internalMethod05900(-5.0f).internalMethod02732(5.0f).internalMethod08074(0.0f);
    private final SliderSetting internalField1140 = new InternalType0500(this, "swing.moveX").internalMethod08673(0.05f).internalMethod05900(-5.0f).internalMethod02732(5.0f).internalMethod08074(0.0f);
    private final SliderSetting internalField1141 = new InternalType0500(this, "swing.moveY").internalMethod08673(0.05f).internalMethod05900(-5.0f).internalMethod02732(5.0f).internalMethod08074(0.0f);
    private final SliderSetting internalField1143 = new InternalType0500(this, "swing.moveZ").internalMethod08673(0.05f).internalMethod05900(-3.0f).internalMethod02732(3.0f).internalMethod08074(0.0f);
    private final SliderSetting internalField1529 = new InternalType0500(this, "swing.rotateX").internalMethod08673(15.0f).internalMethod05900(-360.0f).internalMethod02732(360.0f).internalMethod08074(0.0f);
    private final SliderSetting internalField1534 = new InternalType0500(this, "swing.rotateY").internalMethod08673(15.0f).internalMethod05900(-360.0f).internalMethod02732(360.0f).internalMethod08074(0.0f);
    private final SliderSetting internalField1535 = new InternalType0500(this, "swing.rotateZ").internalMethod08673(15.0f).internalMethod05900(-360.0f).internalMethod02732(360.0f).internalMethod08074(0.0f);

    @Generated
    public SliderSetting internalMethod01003() {
        return this.internalField0383;
    }

    @Generated
    public SliderSetting internalMethod01734() {
        return this.internalField0382;
    }

    @Generated
    public SliderSetting internalMethod08438() {
        return this.internalField1142;
    }

    @Generated
    public SliderSetting internalMethod08568() {
        return this.internalField1140;
    }

    @Generated
    public SliderSetting internalMethod07873() {
        return this.internalField1141;
    }

    @Generated
    public SliderSetting internalMethod07987() {
        return this.internalField1143;
    }

    @Generated
    public SliderSetting internalMethod09766() {
        return this.internalField1529;
    }

    @Generated
    public SliderSetting internalMethod09853() {
        return this.internalField1534;
    }

    @Generated
    public SliderSetting internalMethod09483() {
        return this.internalField1535;
    }

    public static class InternalType0500
    extends SliderSetting {
        public InternalType0500(@NotNull SettingOwner typedValue159, String string) {
            super(typedValue159, string);
        }

        @Override
        public void internalMethod04736(float f) {
            super.internalMethod04736(f);
            if (rockstar.client.compat.InputCompat.hasShiftDown()) {
                InternalType0500 nestedValue0186;
                for (Setting typedValue157 : RockstarClient.getInstance().internalMethod00061().internalMethod04274().getSettings()) {
                    if (!typedValue157.getName().equals(this.getName()) || !(typedValue157 instanceof InternalType0500)) continue;
                    nestedValue0186 = (InternalType0500)typedValue157;
                    nestedValue0186.internalMethod05709(f);
                }
                for (Setting typedValue157 : RockstarClient.getInstance().internalMethod00061().internalMethod05639().getSettings()) {
                    if (!typedValue157.getName().equals(this.getName()) || !(typedValue157 instanceof InternalType0500)) continue;
                    nestedValue0186 = (InternalType0500)typedValue157;
                    nestedValue0186.internalMethod05709(f);
                }
            }
        }

        private void internalMethod05709(float f) {
            this.internalField1047 = MathHelper.clamp((float)((float)((double)Math.round((double)f * (1.0 / (double)this.internalField1048)) / (1.0 / (double)this.internalField1048))), (float)this.internalField0205, (float)this.internalField0206);
        }
    }
}
