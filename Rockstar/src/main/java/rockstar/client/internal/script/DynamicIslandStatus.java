package rockstar.client.internal.script;







import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.animation.*;
import rockstar.client.internal.ui.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import lombok.Generated;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.setting.MultiSelectSetting;
import rockstar.client.internal.script.DynamicIslandHud;
import rockstar.client.internal.core.MutableVector3;
import rockstar.client.internal.ui.DynamicIslandStatusContainer;
import rockstar.client.animation.AnimatedValue;
import rockstar.client.animation.Easing;
import rockstar.client.ui.ThemeColors;
import rockstar.client.ui.ScreenMetricsAccess;
import rockstar.client.ui.UiNode;

public abstract class DynamicIslandStatus
extends MultiSelectSetting.InternalType0091
implements ScreenMetricsAccess {
    public MutableVector3 size = new MutableVector3(48.0f, 15.0f, 7.0f);
    public final AnimatedValue animation = new AnimatedValue(500L, Easing.internalField1327);
    private UiNode element;

    public DynamicIslandStatus(MultiSelectSetting typedValue173, String string) {
        this(typedValue173, string, true);
    }

    public DynamicIslandStatus(MultiSelectSetting typedValue173, String string, boolean bl) {
        super(typedValue173, (String)(bl ? "hud.dynamic_island.statuses." + string : DynamicIslandStatus.sanitizeName(string)));
        this.select();
    }

    public final UiNode element(DynamicIslandHud typedValue201) {
        if (this.element == null) {
            this.element = new DynamicIslandStatusContainer(this, typedValue201);
        }
        return this.element;
    }

    public boolean isExpandable() {
        return false;
    }

    public boolean drawsOwnBackground() {
        return false;
    }

    public void prepare(DynamicIslandHud typedValue201) {
    }

    public UiNode content(DynamicIslandHud typedValue201) {
        return null;
    }

    public float radius(DynamicIslandHud typedValue201) {
        return 7.0f;
    }

    public void render(UiRenderContext iII, DynamicIslandHud typedValue201, float f, float f2, float f3, float f4, float f5) {
    }

    public void click(float f, float f2, int n) {
    }

    public abstract boolean canShow();

    public ColorRGBA getColor() {
        return ThemeColors.internalMethod07738();
    }

    private static String sanitizeName(String string) {
        return string == null || string.isBlank() ? "Script Status" : string.trim();
    }

    @Generated
    public MutableVector3 getSize() {
        return this.size;
    }

    @Generated
    public AnimatedValue getAnimation() {
        return this.animation;
    }

    @Generated
    public UiNode getElement() {
        return this.element;
    }
}

