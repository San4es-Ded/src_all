package rockstar.client.internal.framework;






import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.internal.core.*;
import rockstar.client.internal.config.*;
import rockstar.client.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import rockstar.client.setting.Vector2Setting;
import rockstar.client.setting.BooleanSetting;
import rockstar.client.setting.SliderSetting;
import rockstar.client.RockstarClient;
import rockstar.client.util.MathUtils;
import rockstar.client.internal.framework.SwingTransformSettings;
import rockstar.client.internal.framework.SettingGroupBase;
import rockstar.client.internal.core.SwingTransform;
import rockstar.client.internal.core.SwingPreset;
import rockstar.client.internal.config.SwingPresetManager;

public class SwingAnimationManager {
    private final List<SwingPreset> internalField0416 = new ArrayList<SwingPreset>();
    private String internalField0248 = "autosave";
    private final SettingGroupBase internalField0734 = new SettingGroupBase();
    private final SwingTransformSettings internalField0732 = new SwingTransformSettings();
    private final SwingTransformSettings internalField0731 = new SwingTransformSettings();
    private final Vector2Setting internalField0646 = new Vector2Setting(this.internalField0734, "animation").internalMethod06395(0.5f, 1.0f).internalMethod06615(0.5f, 0.0f);
    private final BooleanSetting internalField0650 = new BooleanSetting(this.internalField0734, "swing.back").internalMethod06630();
    private final SliderSetting internalField0383 = new SliderSetting(this.internalField0734, "swing.wing_speed").internalMethod08673(0.5f).internalMethod05900(1.0f).internalMethod02732(5.0f).internalMethod08074(2.0f);

    private void internalMethod01190() {
        this.internalField0416.add(new SwingPreset("swings.block_hit", new Vec2f(0.5f, 1.0f), new Vec2f(0.5f, 0.0f), true, 2.0f, new SwingTransform(0.0f, -0.05f, -0.7f, 1.0500001f, -0.7f, -1.1f, -120.0f, -135.0f, -60.0f), new SwingTransform(0.0f, -0.05f, -0.7f, 1.0500001f, -0.7f, -1.1f, -120.0f, -180.0f, -60.0f)));
        this.internalField0416.add(new SwingPreset("swings.bonk", new Vec2f(0.40131578f, 0.53543305f), new Vec2f(0.0f, -0.24409449f), true, 2.0f, new SwingTransform(0.0f, -0.4f, -0.65000004f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f), new SwingTransform(0.0f, -0.4f, -0.65000004f, 0.0f, 0.0f, 0.0f, -45.0f, 0.0f, 0.0f)));
        this.internalField0416.add(new SwingPreset("swings.rotate_360", new Vec2f(0.43421054f, 0.61417323f), new Vec2f(0.04605263f, -0.26771653f), false, 2.0f, new SwingTransform(0.0f, -0.4f, -0.65000004f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f), new SwingTransform(0.0f, -0.4f, -0.65000004f, 0.0f, 0.0f, 0.0f, -360.0f, 0.0f, 0.0f)));
        this.internalField0416.add(new SwingPreset("swings.from_me", new Vec2f(0.42105263f, 0.87401575f), new Vec2f(0.3881579f, -0.4566929f), true, 2.0f, new SwingTransform(0.0f, 0.0f, -1.1f, 0.2f, 0.0f, -0.1f, -135.0f, 45.0f, 60.0f), new SwingTransform(0.0f, 0.0f, -1.1f, 0.2f, 0.0f, -0.3f, -180.0f, 45.0f, 60.0f)));
    }

    public SwingAnimationManager() {
        this.internalMethod01190();
        if (!this.internalField0416.isEmpty()) {
            this.internalMethod01403(this.internalField0416.getFirst());
        }
    }

    public SwingTransform internalMethod01686(float f) {
        f = this.internalField0646.internalMethod03095().ease(f, 0.0f, 1.0f, 1.0f);
        if (this.internalField0650.internalMethod04496()) {
            f = MathHelper.sin((float)(MathHelper.sqrt((float)f) * (float)Math.PI));
        }
        return new SwingTransform(this.internalMethod01236(this.internalField0732.internalMethod01003(), this.internalField0731.internalMethod01003(), f), this.internalMethod01236(this.internalField0732.internalMethod01734(), this.internalField0731.internalMethod01734(), f), this.internalMethod01236(this.internalField0732.internalMethod08438(), this.internalField0731.internalMethod08438(), f), this.internalMethod01236(this.internalField0732.internalMethod08568(), this.internalField0731.internalMethod08568(), f), this.internalMethod01236(this.internalField0732.internalMethod07873(), this.internalField0731.internalMethod07873(), f), this.internalMethod01236(this.internalField0732.internalMethod07987(), this.internalField0731.internalMethod07987(), f), this.internalMethod01236(this.internalField0732.internalMethod09766(), this.internalField0731.internalMethod09766(), f), this.internalMethod01236(this.internalField0732.internalMethod09853(), this.internalField0731.internalMethod09853(), f), this.internalMethod01236(this.internalField0732.internalMethod09483(), this.internalField0731.internalMethod09483(), f));
    }

    public final void internalMethod01403(SwingPreset typedValue116) {
        if (typedValue116 == null) {
            return;
        }
        this.internalField0646.internalMethod07286(typedValue116.internalMethod00271()).internalMethod01741(typedValue116.internalMethod05536());
        this.internalField0650.internalMethod04836(typedValue116.internalMethod05999());
        this.internalField0383.internalMethod04736(typedValue116.internalMethod05998());
        this.internalMethod04816(this.internalField0732, typedValue116.internalMethod02271());
        this.internalMethod04816(this.internalField0731, typedValue116.internalMethod03556());
        this.internalMethod06192(typedValue116.internalMethod02665());
        SwingPresetManager internalValue0003 = RockstarClient.getInstance().internalMethod01001();
        if (internalValue0003 != null) {
            internalValue0003.internalMethod03314(null);
        }
    }

    private void internalMethod04816(SwingTransformSettings typedValue110, SwingTransform typedValue112) {
        typedValue110.internalMethod01003().internalMethod04736(typedValue112.internalMethod01052());
        typedValue110.internalMethod01734().internalMethod04736(typedValue112.internalMethod01057());
        typedValue110.internalMethod08438().internalMethod04736(typedValue112.internalMethod08670());
        typedValue110.internalMethod08568().internalMethod04736(typedValue112.internalMethod08672());
        typedValue110.internalMethod07873().internalMethod04736(typedValue112.internalMethod08693());
        typedValue110.internalMethod07987().internalMethod04736(typedValue112.internalMethod08695());
        typedValue110.internalMethod09766().internalMethod04736(typedValue112.internalMethod09416());
        typedValue110.internalMethod09853().internalMethod04736(typedValue112.internalMethod09417());
        typedValue110.internalMethod09483().internalMethod04736(typedValue112.internalMethod09426());
    }

    private float internalMethod01236(SliderSetting typedValue174, SliderSetting typedValue175, float f) {
        return MathUtils.internalMethod02587(typedValue174.internalMethod08576(), typedValue175.internalMethod08576(), f);
    }

    public String internalMethod02484() {
        SwingPresetManager internalValue0003 = RockstarClient.getInstance().internalMethod01001();
        return internalValue0003.internalMethod02695() != null ? internalValue0003.internalMethod02695().internalMethod02141() : this.internalField0248;
    }

    @Generated
    public List<SwingPreset> internalMethod05754() {
        return this.internalField0416;
    }

    @Generated
    public SettingGroupBase internalMethod04275() {
        return this.internalField0734;
    }

    @Generated
    public SwingTransformSettings internalMethod04274() {
        return this.internalField0732;
    }

    @Generated
    public SwingTransformSettings internalMethod05639() {
        return this.internalField0731;
    }

    @Generated
    public Vector2Setting internalMethod05663() {
        return this.internalField0646;
    }

    @Generated
    public BooleanSetting internalMethod05664() {
        return this.internalField0650;
    }

    @Generated
    public SliderSetting internalMethod03324() {
        return this.internalField0383;
    }

    @Generated
    public void internalMethod06192(String string) {
        this.internalField0248 = string;
    }
}

