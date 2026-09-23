package rockstar.client.setting;






import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.i18n.*;
import rockstar.client.core.*;
import rockstar.client.*;
import rockstar.client.internal.script.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import lombok.Generated;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.ui.LayoutAlignment;
import rockstar.client.ui.LayeredRockstarScreen;
import rockstar.client.render.Fonts;
import rockstar.client.ui.TextAlignment;
import rockstar.client.setting.SettingOwner;
import rockstar.client.setting.AbstractSetting;
import rockstar.client.ui.MouseButton;
import rockstar.client.internal.script.HudContextMenu;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.ui.ThemeColors;
import rockstar.client.core.CursorType;
import rockstar.client.ui.FlexDirection;
import rockstar.client.ui.ColorSwatch;
import rockstar.client.ui.ColorPickerPopup;
import rockstar.client.ui.TextLabel;
import rockstar.client.ui.UiContainer;

public class GradientColorSetting
extends AbstractSetting {
    private ColorRGBA internalField0777;
    private ColorRGBA internalField0776;
    private boolean internalField0277 = true;
    private transient ColorPickerPopup internalField0927;
    private transient ColorPickerPopup internalField0926;

    public GradientColorSetting(@NotNull SettingOwner typedValue159, String string, @NotNull BooleanSupplier booleanSupplier) {
        super(typedValue159, string, booleanSupplier);
    }

    public GradientColorSetting(@NotNull SettingOwner typedValue159, String string) {
        super(typedValue159, string);
    }

    public GradientColorSetting internalMethod00046(ColorRGBA colorRGBA, ColorRGBA colorRGBA2) {
        if (Objects.equals(this.internalField0777, colorRGBA) && Objects.equals(this.internalField0776, colorRGBA2)) {
            return this;
        }
        this.notifyChanged();
        this.internalField0777 = colorRGBA;
        this.internalField0776 = colorRGBA2;
        return this;
    }

    public GradientColorSetting internalMethod01227(ColorRGBA colorRGBA) {
        this.internalMethod05232(colorRGBA);
        return this;
    }

    public void internalMethod05232(ColorRGBA colorRGBA) {
        if (Objects.equals(this.internalField0777, colorRGBA)) {
            return;
        }
        this.notifyChanged();
        this.internalField0777 = colorRGBA;
    }

    public GradientColorSetting internalMethod04884(ColorRGBA colorRGBA) {
        this.internalMethod00780(colorRGBA);
        return this;
    }

    public void internalMethod00780(ColorRGBA colorRGBA) {
        if (Objects.equals(this.internalField0776, colorRGBA)) {
            return;
        }
        this.notifyChanged();
        this.internalField0776 = colorRGBA;
    }

    public GradientColorSetting internalMethod04397(boolean bl) {
        this.internalField0277 = bl;
        return this;
    }

    @Override
    public JsonElement toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("first", (JsonElement)this.internalMethod01252(this.internalField0777));
        jsonObject.add("second", (JsonElement)this.internalMethod01252(this.internalField0776));
        return jsonObject;
    }

    @Override
    public void fromJson(JsonElement jsonElement) {
        if (jsonElement == null || !jsonElement.isJsonObject()) {
            return;
        }
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        if (!(jsonObject.has("first") && jsonObject.get("first").isJsonObject() && jsonObject.has("second") && jsonObject.get("second").isJsonObject())) {
            return;
        }
        ColorRGBA colorRGBA = this.internalMethod04341(jsonObject.getAsJsonObject("first"));
        ColorRGBA colorRGBA2 = this.internalMethod04341(jsonObject.getAsJsonObject("second"));
        if (colorRGBA != null && colorRGBA2 != null) {
            this.internalMethod00046(colorRGBA, colorRGBA2);
        }
    }

    private JsonObject internalMethod01252(ColorRGBA colorRGBA) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("r", (Number)Float.valueOf(colorRGBA.getRed()));
        jsonObject.addProperty("g", (Number)Float.valueOf(colorRGBA.getGreen()));
        jsonObject.addProperty("b", (Number)Float.valueOf(colorRGBA.getBlue()));
        jsonObject.addProperty("a", (Number)Float.valueOf(colorRGBA.getAlpha()));
        return jsonObject;
    }

    private ColorRGBA internalMethod04341(JsonObject jsonObject) {
        if (!(this.internalMethod00434(jsonObject, "r") && this.internalMethod00434(jsonObject, "g") && this.internalMethod00434(jsonObject, "b") && this.internalMethod00434(jsonObject, "a"))) {
            return null;
        }
        double d = jsonObject.get("r").getAsDouble();
        double d2 = jsonObject.get("g").getAsDouble();
        double d3 = jsonObject.get("b").getAsDouble();
        double d4 = jsonObject.get("a").getAsDouble();
        if (!(Double.isFinite(d) && Double.isFinite(d2) && Double.isFinite(d3) && Double.isFinite(d4))) {
            return null;
        }
        return new ColorRGBA(this.internalMethod05268((int)d), this.internalMethod05268((int)d2), this.internalMethod05268((int)d3), this.internalMethod05268((int)d4));
    }

    private boolean internalMethod00434(JsonObject jsonObject, String string) {
        return jsonObject.has(string) && jsonObject.get(string).isJsonPrimitive() && jsonObject.get(string).getAsJsonPrimitive().isNumber() && Double.isFinite(jsonObject.get(string).getAsDouble());
    }

    @Override
    public boolean isValidJson(JsonElement jsonElement) {
        if (jsonElement == null || !jsonElement.isJsonObject()) {
            return false;
        }
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        return jsonObject.has("first") && jsonObject.get("first").isJsonObject() && jsonObject.has("second") && jsonObject.get("second").isJsonObject() && this.internalMethod04341(jsonObject.getAsJsonObject("first")) != null && this.internalMethod04341(jsonObject.getAsJsonObject("second")) != null;
    }

    private int internalMethod05268(int n) {
        return MathHelper.clamp((int)n, (int)0, (int)255);
    }

    @Override
    public UiContainer createComponent() {
        UiContainer typedValue006 = new UiContainer().internalMethod01192(FlexDirection.internalField1246).internalMethod01855(TextAlignment.internalField0621).internalMethod03062(3.0f).internalMethod03907(new ColorSwatch(this::internalMethod05319).internalMethod06125(10.0f, 10.0f).cursor(CursorType.internalField0567).onClick((typedParameter1015, f, f2) -> {
            if (typedParameter1015 == MouseButton.internalField0102) {
                this.internalMethod05269(true);
            }
        })).internalMethod03907(new ColorSwatch(this::internalMethod01482).internalMethod06125(10.0f, 10.0f).cursor(CursorType.internalField0567).onClick((typedParameter1015, f, f2) -> {
            if (typedParameter1015 == MouseButton.internalField0102) {
                this.internalMethod05269(false);
            }
        }));
        return new UiContainer().internalMethod09266(17.0f).internalMethod03907(new TextLabel(Fonts.internalField1154.internalMethod01432(8.0f), () -> LanguageManager.internalMethod07214(this.internalField0248)).internalMethod02959(typedValue011 -> ThemeColors.internalField1613.mulAlpha(0.75f + 0.25f * typedValue011.hover())).internalMethod02902().fill()).internalMethod03907(typedValue006).internalMethod03062(5.0f).internalMethod01192(FlexDirection.internalField1246).internalMethod07607(LayoutAlignment.internalField1377).internalMethod01855(TextAlignment.internalField0621);
    }

    private void internalMethod05269(boolean bl) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        double d = minecraftClient.getWindow().getScaleFactor();
        float f = (float)(minecraftClient.mouse.getX() / d);
        float f2 = (float)(minecraftClient.mouse.getY() / d);
        if (bl) {
            if (this.internalField0927 != null && this.internalField0927.alive()) {
                return;
            }
            ColorRGBA colorRGBA = this.internalField0777 != null ? this.internalField0777 : ColorRGBA.WHITE;
            this.internalField0927 = GradientColorSetting.internalMethod00049(new ColorPickerPopup(f, f2, this.internalField0277, colorRGBA, LanguageManager.internalMethod07214(this.internalField0248), this::internalMethod01227));
        } else {
            if (this.internalField0926 != null && this.internalField0926.alive()) {
                return;
            }
            ColorRGBA colorRGBA = this.internalField0776 != null ? this.internalField0776 : ColorRGBA.WHITE;
            this.internalField0926 = GradientColorSetting.internalMethod00049(new ColorPickerPopup(f, f2, this.internalField0277, colorRGBA, LanguageManager.internalMethod07214(this.internalField0248), this::internalMethod04884));
        }
    }

    private static ColorPickerPopup internalMethod00049(ColorPickerPopup typedValue009) {
        ColorPickerPopup typedValue010;
        Screen screen = MinecraftClient.getInstance().currentScreen;
        if (screen instanceof LayeredRockstarScreen) {
            LayeredRockstarScreen typedParameter1003 = (LayeredRockstarScreen)screen;
            typedValue010 = typedParameter1003.openWindow(typedValue009);
        } else {
            typedValue010 = HudContextMenu.internalMethod03683(typedValue009);
        }
        return typedValue010;
    }

    @Generated
    public ColorRGBA internalMethod05319() {
        return this.internalField0777;
    }

    @Generated
    public ColorRGBA internalMethod01482() {
        return this.internalField0776;
    }

    @Generated
    public boolean internalMethod04496() {
        return this.internalField0277;
    }

    @Generated
    public ColorPickerPopup internalMethod07069() {
        return this.internalField0927;
    }

    @Generated
    public ColorPickerPopup internalMethod03177() {
        return this.internalField0926;
    }
}

