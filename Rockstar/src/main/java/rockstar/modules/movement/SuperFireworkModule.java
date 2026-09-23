package rockstar.modules.movement;





import rockstar.client.setting.*;
import rockstar.client.rotation.*;
import rockstar.client.module.*;
import rockstar.client.event.*;
import rockstar.client.*;

import lombok.Generated;
import net.minecraft.util.math.Vec3d;
import pyrock.events.game.FireworkEvent;
import rockstar.client.setting.SettingOwner;
import rockstar.client.setting.ModeSetting;
import rockstar.client.setting.SliderSetting;
import rockstar.client.event.EventListener;
import rockstar.client.module.ModuleCategory;
import rockstar.client.RockstarClient;
import rockstar.client.module.ModuleInfo;
import rockstar.client.rotation.Rotation;
import rockstar.client.rotation.RotationManager;
import rockstar.client.module.Module;

@ModuleInfo(name="Super Firework", category=ModuleCategory.MOVEMENT, internalMethod09633="modules.descriptions.elytra_motion")
public class SuperFireworkModule
extends Module {
    private ModeSetting internalField0668;
    private ModeSetting.InternalType0088 internalField0237;
    private ModeSetting.InternalType0088 internalField0238;
    private ModeSetting.InternalType0088 internalField1066;
    private ModeSetting.InternalType0088 internalField1067;
    private SliderSetting internalField0383;
    private final EventListener<FireworkEvent> internalField0157 = fireworkEvent -> {
        double d;
        double d2;
        Rotation typedValue266;
        if (fireworkEvent.getEntity() != SuperFireworkModule.internalField0149.player || SuperFireworkModule.internalField0149.player == null) {
            return;
        }
        RotationManager typedValue269 = RockstarClient.getInstance().internalMethod02368();
        if (typedValue269 == null) {
            return;
        }
        Rotation typedValue267 = typedValue266 = typedValue269.internalMethod01525() ? typedValue269.internalMethod07496() : typedValue269.internalMethod09074();
        if (typedValue266 == null) {
            return;
        }
        Vec3d vec3d = typedValue266.internalMethod06001();
        if (this.internalField0668.internalMethod06103(this.internalField0237)) {
            double d3;
            d2 = d3 = this.internalMethod01112(typedValue266);
            d = d3;
        } else if (!this.internalField0668.internalMethod06103(this.internalField0238)) {
            float f = this.internalMethod01621(typedValue266.internalMethod00169());
            float f2 = Math.abs(typedValue266.internalMethod00171());
            d2 = this.internalMethod01163(f, f2, this.internalField0668.internalMethod07418());
            d = this.internalMethod06320(f2, f, this.internalField0668.internalMethod07418());
        } else {
            d2 = this.internalField0383.internalMethod08576();
            d = this.internalField0383.internalMethod08576();
        }
        Vec3d vec3d2 = this.internalMethod00555(fireworkEvent.getVelocity(), vec3d, d2, d);
        fireworkEvent.setVelocity(vec3d2);
    };

    private void internalMethod09644() {
        this.internalField0668 = new ModeSetting(this, "modules.settings.elytra_motion.algorithm");
        this.internalField0237 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.elytra_motion.algorithm.default").select();
        this.internalField0238 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.elytra_motion.algorithm.custom");
        this.internalField1066 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.elytra_motion.algorithm.advanced");
        this.internalField1067 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.elytra_motion.algorithm.advanced-stable");
        this.internalField0383 = new SliderSetting((SettingOwner)this, "modules.settings.elytra_motion.custom_strength", () -> !this.internalField0668.internalMethod06103(this.internalField0238)).internalMethod05900(1.5f).internalMethod02732(2.5f).internalMethod08673(0.025f).internalMethod08074(1.78f);
    }

    public SuperFireworkModule() {
        this.internalMethod09644();
    }

    private Vec3d internalMethod00555(Vec3d vec3d, Vec3d vec3d2, double d, double d2) {
        double d3 = 0.1;
        return vec3d.add(vec3d2.x * d3 + (vec3d2.x * d - vec3d.x) * 0.5, vec3d2.y * d3 + (vec3d2.y * d2 - vec3d.y) * 0.5, vec3d2.z * d3 + (vec3d2.z * d - vec3d.z) * 0.5);
    }

    private double internalMethod01112(Rotation typedValue266) {
        double d;
        float f = typedValue266.internalMethod00169();
        float f2 = typedValue266.internalMethod00171();
        double d2 = 1.49;
        double d3 = SuperFireworkModule.internalMethod01620(f);
        if (f2 >= -45.0f && f2 <= 20.0f) {
            double d4 = -10.0;
            double d5 = (double)f2 - d4;
            d = 1.0 + 0.05 * Math.exp(-(d5 * d5) / 800.0);
        } else if (f2 > 20.0f) {
            double d6 = (double)f2 - 20.0;
            d = 1.0 - Math.min(0.35, d6 / 70.0 * 0.35);
        } else {
            double d7 = (double)Math.abs(f2) - 45.0;
            d = 1.0 - Math.min(0.3, d7 / 45.0 * 0.3);
        }
        return Math.clamp(d3 * d, 0.65, 1.49);
    }

    private static double internalMethod01620(float f) {
        double d = Math.abs(f % 360.0f);
        if (d > 180.0) {
            d = 360.0 - d;
        }
        double d2 = Math.abs(d - 45.0);
        double d3 = Math.abs(d - 135.0);
        double d4 = Math.min(d2, d3);
        double d5 = 0.47 * Math.exp(-(d4 * d4) / 288.0);
        return 1.0 + d5;
    }

    private float internalMethod01621(float f) {
        float f2 = f % 180.0f;
        if (f2 > 90.0f) {
            f2 -= 180.0f;
        } else if (f2 < -90.0f) {
            f2 += 180.0f;
        }
        return Math.abs(f2);
    }

    private double internalMethod01163(float f, float f2, ModeSetting.InternalType0088 nestedValue2011) {
        int n = (int)Math.ceil(f);
        double d = f2 >= 40.0f && f2 <= 50.0f ? 2.0 : (f2 >= 38.0f && f2 <= 52.0f ? 1.98 : (f2 >= 32.0f && f2 <= 58.0f ? 1.97 : (n == 33 || n == 57 || f2 == 33.0f || f2 == 57.0f ? 1.964 : (n == 34 || n == 56 || f2 == 34.0f || f2 == 56.0f ? 1.964 : (n == 35 || n == 55 || f2 == 35.0f || f2 == 55.0f ? 1.965 : (n == 36 || n == 54 || f2 == 36.0f || f2 == 54.0f ? 1.965 : (n == 37 || n == 53 || f2 == 37.0f || f2 == 53.0f ? 1.966 : (n == 38 || n == 52 || f2 == 38.0f || f2 == 52.0f ? 1.966 : (n == 39 || n == 51 || f2 == 39.0f || f2 == 51.0f ? 1.966 : (n == 40 || n == 50 || f2 == 40.0f || f2 == 50.0f ? 1.967 : (n == 41 || n == 49 || f2 == 41.0f || f2 == 49.0f ? 1.968 : (n == 42 || n == 48 || f2 == 42.0f || f2 == 48.0f ? 1.969 : (n == 43 || n == 47 || f2 == 43.0f || f2 == 47.0f ? 1.969 : (n == 44 || n == 46 || f2 == 44.0f || f2 == 46.0f ? 1.9695 : (n == 45 || f2 == 45.0f ? 1.9695 : (n >= 29 && n <= 61 || f2 >= 29.0f && f2 <= 61.0f ? 1.963 : (n >= 27 && n <= 63 || f2 >= 27.0f && f2 <= 63.0f ? 1.84 : (n >= 26 && n <= 64 || f2 >= 26.0f && f2 <= 64.0f ? 1.8 : (n >= 15 && n <= 75 || f2 >= 15.0f && f2 <= 75.0f ? 1.74 : (n >= 13 && n <= 77 || f2 >= 13.0f && f2 <= 77.0f ? 1.7 : (n >= 12 && n <= 78 || f2 >= 12.0f && f2 <= 78.0f ? 1.671 : 1.626)))))))))))))))))))));
        if (d < 1.9 && f2 > 10.0f) {
            d += 0.05;
        }
        return d * (double)(nestedValue2011 == this.internalField1067 ? 0.98f : 1.0f);
    }

    private double internalMethod06320(float f, float f2, ModeSetting.InternalType0088 nestedValue2011) {
        double d = f >= 30.0f && f <= 40.0f ? 2.0 * (double)(nestedValue2011 == this.internalField1067 ? 0.95f : 1.0f) : (f >= 35.0f && f <= 45.0f ? 1.99 * (double)(nestedValue2011 == this.internalField1067 ? 0.95f : 1.0f) : (f >= 40.0f && f <= 50.0f ? 1.97 * (double)(nestedValue2011 == this.internalField1067 ? 0.95f : 1.0f) : (f >= 50.0f && f <= 60.0f ? 1.96 * (double)(nestedValue2011 == this.internalField1067 ? 0.95f : 1.0f) : (f >= 51.0f && f <= 61.0f ? 1.89 * (double)(nestedValue2011 == this.internalField1067 ? 0.98f : 1.0f) : (f >= 52.0f && f <= 65.0f ? 1.7 : 1.6)))));
        return d;
    }

    @Generated
    public ModeSetting internalMethod00485() {
        return this.internalField0668;
    }

    @Generated
    public ModeSetting.InternalType0088 internalMethod02379() {
        return this.internalField0237;
    }

    @Generated
    public ModeSetting.InternalType0088 internalMethod02563() {
        return this.internalField0238;
    }

    @Generated
    public ModeSetting.InternalType0088 internalMethod09102() {
        return this.internalField1066;
    }

    @Generated
    public ModeSetting.InternalType0088 internalMethod09141() {
        return this.internalField1067;
    }

    @Generated
    public SliderSetting internalMethod06257() {
        return this.internalField0383;
    }
}
