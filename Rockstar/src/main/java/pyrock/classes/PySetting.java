package pyrock.classes;





import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.i18n.*;
import rockstar.client.animation.*;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.setting.Setting;
import rockstar.client.setting.Vector2Setting;
import rockstar.client.setting.KeybindSetting;
import rockstar.client.setting.RegistryListSetting;
import rockstar.client.setting.BooleanSetting;
import rockstar.client.setting.ButtonSetting;
import rockstar.client.setting.ColorSetting;
import rockstar.client.setting.GradientColorSetting;
import rockstar.client.setting.SectionSetting;
import rockstar.client.setting.ModeSetting;
import rockstar.client.setting.VectorRangeSetting;
import rockstar.client.setting.RangeSetting;
import rockstar.client.setting.MultiSelectSetting;
import rockstar.client.setting.SliderSetting;
import rockstar.client.setting.TextSetting;
import rockstar.client.setting.TimeSetting;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.animation.Easing;
import rockstar.client.util.TextUtils;

public class PySetting {
    private final Setting s;

    public PySetting(Setting typedValue157) {
        this.s = typedValue157;
    }

    public Setting raw() {
        return this.s;
    }

    public String rawName() {
        return this.s.getName();
    }

    public String name() {
        return LanguageManager.internalMethod07214(this.s.getName());
    }

    public boolean visible() {
        return this.s.isVisible();
    }

    public String type() {
        if (this.s instanceof BooleanSetting) {
            return "boolean";
        }
        if (this.s instanceof SliderSetting) {
            return "slider";
        }
        if (this.s instanceof RangeSetting) {
            return "range";
        }
        if (this.s instanceof ModeSetting) {
            return "mode";
        }
        if (this.s instanceof MultiSelectSetting) {
            return "select";
        }
        if (this.s instanceof ColorSetting) {
            return "color";
        }
        if (this.s instanceof ButtonSetting) {
            return "button";
        }
        if (this.s instanceof KeybindSetting) {
            return "bind";
        }
        if (this.s instanceof TextSetting) {
            return "text";
        }
        if (this.s instanceof TimeSetting) {
            return "time";
        }
        if (this.s instanceof GradientColorSetting) {
            return "gradient";
        }
        if (this.s instanceof VectorRangeSetting) {
            return "position";
        }
        if (this.s instanceof Vector2Setting) {
            return "bezier";
        }
        if (this.s instanceof RegistryListSetting) {
            return "blocks";
        }
        if (this.s instanceof SectionSetting) {
            return "info";
        }
        return "other";
    }

    public boolean boolGet() {
        return ((BooleanSetting)this.s).internalMethod04496();
    }

    public void boolToggle() {
        ((BooleanSetting)this.s).toggle();
    }

    public void boolSet(boolean bl) {
        ((BooleanSetting)this.s).internalMethod02034(bl);
    }

    public float numGet() {
        return ((SliderSetting)this.s).internalMethod08576();
    }

    public void numSet(float f) {
        ((SliderSetting)this.s).internalMethod08074(f);
    }

    public float numMin() {
        return ((SliderSetting)this.s).internalMethod05288();
    }

    public float numMax() {
        return ((SliderSetting)this.s).internalMethod05291();
    }

    public float numStep() {
        return ((SliderSetting)this.s).internalMethod08575();
    }

    public String[] options() {
        ArrayList<String> arrayList;
        block3: {
            Object object;
            block2: {
                arrayList = new ArrayList<String>();
                object = this.s;
                if (!(object instanceof ModeSetting)) break block2;
                ModeSetting typedValue170 = (ModeSetting)object;
                for (ModeSetting.InternalType0088 nestedValue0042 : typedValue170.internalMethod06723()) {
                    arrayList.add(nestedValue0042.getName());
                }
                break block3;
            }
            object = this.s;
            if (!(object instanceof MultiSelectSetting)) break block3;
            MultiSelectSetting typedValue173 = (MultiSelectSetting)object;
            for (MultiSelectSetting.InternalType0091 nestedValue0044 : typedValue173.internalMethod01792()) {
                arrayList.add(nestedValue0044.getName());
            }
        }
        return arrayList.toArray(new String[0]);
    }

    public String[] optionLabels() {
        String[] stringArray = this.options();
        String[] stringArray2 = new String[stringArray.length];
        for (int i = 0; i < stringArray.length; ++i) {
            stringArray2[i] = LanguageManager.internalMethod07214(stringArray[i]);
        }
        return stringArray2;
    }

    public int modeIndex() {
        ModeSetting typedValue170 = (ModeSetting)this.s;
        List<ModeSetting.InternalType0088> list = typedValue170.internalMethod06723();
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i) != typedValue170.internalMethod07418()) continue;
            return i;
        }
        return -1;
    }

    public void modeSelect(int n) {
        ModeSetting typedValue170 = (ModeSetting)this.s;
        if (n >= 0 && n < typedValue170.internalMethod06723().size()) {
            typedValue170.internalMethod06723().get(n).select();
        }
    }

    public boolean selOn(int n) {
        MultiSelectSetting typedValue173 = (MultiSelectSetting)this.s;
        return n >= 0 && n < typedValue173.internalMethod01792().size() && typedValue173.internalMethod01792().get(n).isSelected();
    }

    public void selToggle(int n) {
        MultiSelectSetting typedValue173 = (MultiSelectSetting)this.s;
        if (n >= 0 && n < typedValue173.internalMethod01792().size()) {
            typedValue173.internalMethod01792().get(n).toggle();
        }
    }

    public int selCount() {
        return ((MultiSelectSetting)this.s).internalMethod07492().size();
    }

    public ColorRGBA colorGet() {
        return ((ColorSetting)this.s).internalMethod05620();
    }

    public void colorSet(ColorRGBA colorRGBA) {
        ((ColorSetting)this.s).internalMethod04886(colorRGBA);
    }

    public void click() {
        Runnable runnable = ((ButtonSetting)this.s).internalMethod03496();
        if (runnable != null) {
            runnable.run();
        }
    }

    public int bindKey() {
        return ((KeybindSetting)this.s).internalMethod07477();
    }

    public String bindName() {
        return TextUtils.internalMethod04982(((KeybindSetting)this.s).internalMethod07477());
    }

    public void bindSet(int n) {
        ((KeybindSetting)this.s).internalMethod02164(n);
    }

    public String textGet() {
        String string = ((TextSetting)this.s).internalMethod08926();
        return string == null ? "" : string;
    }

    public void textSet(String string) {
        ((TextSetting)this.s).internalMethod00011(string);
    }

    public int timeGet() {
        return ((TimeSetting)this.s).internalMethod09221();
    }

    public void timeSet(int n) {
        ((TimeSetting)this.s).internalMethod04593(n);
    }

    public long timeMillis() {
        return ((TimeSetting)this.s).internalMethod02082();
    }

    public int timeTicks() {
        return ((TimeSetting)this.s).internalMethod08553();
    }

    public String timeFormatted() {
        return ((TimeSetting)this.s).internalMethod08164();
    }

    public ColorRGBA gradientFirst() {
        return ((GradientColorSetting)this.s).internalMethod05319();
    }

    public ColorRGBA gradientSecond() {
        return ((GradientColorSetting)this.s).internalMethod01482();
    }

    public void gradientSet(ColorRGBA colorRGBA, ColorRGBA colorRGBA2) {
        ((GradientColorSetting)this.s).internalMethod00046(colorRGBA, colorRGBA2);
    }

    public float posX() {
        return ((VectorRangeSetting)this.s).internalMethod03695();
    }

    public float posY() {
        return ((VectorRangeSetting)this.s).internalMethod03697();
    }

    public void posSet(float f, float f2) {
        ((VectorRangeSetting)this.s).internalMethod07235(f, f2);
    }

    public float bezierEase(float f) {
        float f2 = Math.max(0.0f, Math.min(1.0f, f));
        Easing typedValue214 = ((Vector2Setting)this.s).internalMethod03095();
        return typedValue214 == null ? f2 : typedValue214.ease(f2, 0.0f, 1.0f, 1.0f);
    }

    public List<String> blocksSelected() {
        ArrayList<String> arrayList = new ArrayList<String>();
        for (Identifier identifier : ((RegistryListSetting)this.s).internalMethod00086()) {
            arrayList.add(identifier.toString());
        }
        return arrayList;
    }

    public boolean blockSelected(String string) {
        Identifier identifier = PySetting.blockId(string);
        return identifier != null && ((RegistryListSetting)this.s).internalMethod05933(identifier);
    }

    public void blockToggle(String string) {
        Identifier identifier = PySetting.blockId(string);
        if (identifier == null) {
            return;
        }
        RegistryListSetting typedValue163 = (RegistryListSetting)this.s;
        if (typedValue163.internalMethod05933(identifier)) {
            typedValue163.internalMethod05585((Block)Registries.BLOCK.get(identifier));
        } else {
            typedValue163.internalMethod02288(identifier);
        }
    }

    private static Identifier blockId(String string) {
        if (string == null || string.isBlank()) {
            return null;
        }
        String string2 = string.trim();
        return Identifier.tryParse((String)(string2.indexOf(58) < 0 ? "minecraft:" + string2 : string2));
    }
}

