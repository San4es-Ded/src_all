package rockstar.client.internal.ui;




import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.*;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.setting.Setting;
import rockstar.client.ui.MouseButton;
import rockstar.client.ui.SettingComponent;
import rockstar.client.ui.UiUtils;
import rockstar.client.ui.LegacyUiElement;
import rockstar.client.ui.UiContainer;

public class SettingRowContainer
extends UiContainer {
    private static final LegacyUiElement internalField0241 = new LegacyUiElement(){

        @Override
        protected void internalMethod05619(UiRenderContext iII) {
        }
    };
    private final SettingComponent<?> internalField0555;

    public SettingRowContainer(Setting typedValue157) {
        this.internalField0555 = UiUtils.internalMethod06603(typedValue157, internalField0241);
        this.internalMethod09609();
        this.internalMethod03855(typedValue157::isVisible);
    }

    @Override
    public float desiredH() {
        return this.internalField0555 == null ? 0.0f : this.internalField0555.internalMethod07809();
    }

    @Override
    public void onTick(float f, float f2, float f3) {
    }

    @Override
    public void drawSelf(UiRenderContext iII, float f) {
        if (this.internalField0555 == null) {
            return;
        }
        this.internalField0555.internalMethod05191(this.x(), this.y(), this.w(), this.internalField0555.internalMethod07809());
        this.internalField0555.internalMethod07890(this.y());
        this.internalField0555.internalMethod07901(this.internalField0555.internalMethod07809());
        this.internalField0555.internalMethod03398(iII);
    }

    @Override
    public boolean mouseClicked(float f, float f2, MouseButton typedParameter1015) {
        if (this.internalField0555 == null || !this.contains(f, f2)) {
            return false;
        }
        this.internalField0555.internalMethod01643(f, f2, typedParameter1015);
        return true;
    }

    @Override
    public void mouseReleased(float f, float f2, MouseButton typedParameter1015) {
        if (this.internalField0555 != null) {
            this.internalField0555.internalMethod02863(f, f2, typedParameter1015);
        }
        super.mouseReleased(f, f2, typedParameter1015);
    }

    @Override
    public boolean mouseScrolled(float f, float f2, float f3, float f4) {
        return false;
    }
}

