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

public class ColorSetting
extends AbstractSetting {
    private ColorRGBA internalField0777;
    private boolean internalField0277 = true;
    private transient ColorPickerPopup internalField0927;

    public ColorSetting(@NotNull SettingOwner typedValue159, String string, @NotNull BooleanSupplier booleanSupplier) {
        super(typedValue159, string, booleanSupplier);
    }

    public ColorSetting(@NotNull SettingOwner typedValue159, String string) {
        super(typedValue159, string);
    }

    public ColorSetting internalMethod04886(ColorRGBA colorRGBA) {
        this.internalMethod07054(colorRGBA);
        return this;
    }

    public void internalMethod07054(ColorRGBA colorRGBA) {
        if (Objects.equals(this.internalField0777, colorRGBA)) {
            return;
        }
        this.notifyChanged();
        this.internalField0777 = colorRGBA;
    }

    public ColorSetting internalMethod05166(boolean bl) {
        this.internalField0277 = bl;
        return this;
    }

    @Override
    public JsonElement toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("r", (Number)Float.valueOf(this.internalField0777.getRed()));
        jsonObject.addProperty("g", (Number)Float.valueOf(this.internalField0777.getGreen()));
        jsonObject.addProperty("b", (Number)Float.valueOf(this.internalField0777.getBlue()));
        jsonObject.addProperty("a", (Number)Float.valueOf(this.internalField0777.getAlpha()));
        return jsonObject;
    }

    @Override
    public void fromJson(JsonElement jsonElement) {
        if (jsonElement == null || !jsonElement.isJsonObject()) {
            return;
        }
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        if (!(this.internalMethod01054(jsonObject, "r") && this.internalMethod01054(jsonObject, "g") && this.internalMethod01054(jsonObject, "b") && this.internalMethod01054(jsonObject, "a"))) {
            return;
        }
        double d = jsonObject.get("r").getAsDouble();
        double d2 = jsonObject.get("g").getAsDouble();
        double d3 = jsonObject.get("b").getAsDouble();
        double d4 = jsonObject.get("a").getAsDouble();
        if (!(Double.isFinite(d) && Double.isFinite(d2) && Double.isFinite(d3) && Double.isFinite(d4))) {
            return;
        }
        this.internalMethod07054(new ColorRGBA(this.internalMethod02185((int)d), this.internalMethod02185((int)d2), this.internalMethod02185((int)d3), this.internalMethod02185((int)d4)));
    }

    private boolean internalMethod01054(JsonObject jsonObject, String string) {
        return jsonObject.has(string) && jsonObject.get(string).isJsonPrimitive() && jsonObject.get(string).getAsJsonPrimitive().isNumber() && Double.isFinite(jsonObject.get(string).getAsDouble());
    }

    @Override
    public boolean isValidJson(JsonElement jsonElement) {
        if (jsonElement == null || !jsonElement.isJsonObject()) {
            return false;
        }
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        return this.internalMethod01054(jsonObject, "r") && this.internalMethod01054(jsonObject, "g") && this.internalMethod01054(jsonObject, "b") && this.internalMethod01054(jsonObject, "a");
    }

    private int internalMethod02185(int n) {
        return MathHelper.clamp((int)n, (int)0, (int)255);
    }

    @Override
    public UiContainer createComponent() {
        return new UiContainer().internalMethod09266(17.0f).internalMethod03907(new TextLabel(Fonts.internalField1154.internalMethod01432(8.0f), () -> LanguageManager.internalMethod07214(this.internalField0248)).internalMethod02959(typedValue011 -> ThemeColors.internalField1613.mulAlpha(0.75f + 0.25f * typedValue011.hover())).internalMethod02902().fill()).internalMethod03907(new ColorSwatch(this::internalMethod05620).internalMethod06125(10.0f, 10.0f).interactive(false)).internalMethod03062(5.0f).internalMethod01192(FlexDirection.internalField1246).internalMethod07607(LayoutAlignment.internalField1377).internalMethod01855(TextAlignment.internalField0621).internalMethod05382((typedParameter1015, f, f2) -> {
            if (typedParameter1015 == MouseButton.internalField0102) {
                this.internalMethod08105();
            }
        }).internalMethod04332(CursorType.internalField0567);
    }

    public void internalMethod03824() {
        if (this.internalField0927 != null && this.internalField0927.alive()) {
            this.internalField0927.close();
        }
    }

    private void internalMethod08105() {
        ColorPickerPopup typedValue009;
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (this.internalField0927 != null && this.internalField0927.alive()) {
            return;
        }
        double d = minecraftClient.getWindow().getScaleFactor();
        float f = (float)(minecraftClient.mouse.getX() / d);
        float f2 = (float)(minecraftClient.mouse.getY() / d);
        ColorRGBA colorRGBA = this.internalField0777 != null ? this.internalField0777 : ColorRGBA.WHITE;
        ColorPickerPopup typedValue010 = new ColorPickerPopup(f, f2, this.internalField0277, colorRGBA, LanguageManager.internalMethod07214(this.internalField0248), this::internalMethod04886);
        Screen screen = minecraftClient.currentScreen;
        if (screen instanceof LayeredRockstarScreen) {
            LayeredRockstarScreen typedParameter1003 = (LayeredRockstarScreen)screen;
            typedValue009 = typedParameter1003.openWindow(typedValue010);
        } else {
            typedValue009 = HudContextMenu.internalMethod03683(typedValue010);
        }
        this.internalField0927 = typedValue009;
    }

    @Generated
    public ColorRGBA internalMethod05620() {
        return this.internalField0777;
    }

    @Generated
    public boolean internalMethod04496() {
        return this.internalField0277;
    }

    @Generated
    public ColorPickerPopup internalMethod07595() {
        return this.internalField0927;
    }
}

