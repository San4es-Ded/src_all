package rockstar.client.internal.ui;




import rockstar.client.ui.*;
import rockstar.client.internal.script.*;
import rockstar.client.*;
import net.minecraft.client.gui.DrawContext;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.internal.script.AutoSellTask;
import rockstar.client.ui.MouseButton;
import rockstar.client.internal.script.HudContextMenu;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.ui.ScreenMetricsAccess;
import rockstar.client.ui.RockstarScreen;

public class AutoSellScreen
extends RockstarScreen
implements MinecraftClientAccess,
ScreenMetricsAccess {
    private final AutoSellTask internalField0692;

    public AutoSellScreen(AutoSellTask typedValue312) {
        this.internalField0692 = typedValue312;
    }

    @Override
    public void render(UiRenderContext iII) {
        HudContextMenu typedValue190 = this.internalField0692.internalMethod00450();
        if (typedValue190 == null) {
            this.close();
            return;
        }
        typedValue190.internalMethod08630(170.0f);
        typedValue190.internalMethod03623(internalField0389.internalMethod03585() / 2.0f - typedValue190.internalMethod08827() / 2.0f);
        typedValue190.internalMethod03701(internalField0389.internalMethod03589() / 2.0f - typedValue190.internalMethod07809() / 2.0f);
        typedValue190.internalMethod03398(iII);
        if (!typedValue190.internalMethod08805() && typedValue190.internalMethod06960().internalMethod02881() <= 0.02f) {
            this.internalField0692.internalMethod09001();
            this.close();
        }
    }

    @Override
    public void onMouseClicked(double d, double d2, MouseButton typedParameter1015) {
        HudContextMenu typedValue190 = this.internalField0692.internalMethod00450();
        if (typedValue190 != null) {
            typedValue190.internalMethod01643(d, d2, typedParameter1015);
        }
    }

    @Override
    public void onMouseReleased(double d, double d2, MouseButton typedParameter1015) {
        HudContextMenu typedValue190 = this.internalField0692.internalMethod00450();
        if (typedValue190 != null) {
            typedValue190.internalMethod02863(d, d2, typedParameter1015);
        }
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        HudContextMenu typedValue190 = this.internalField0692.internalMethod00450();
        if (typedValue190 != null) {
            typedValue190.internalMethod02890(mouseX, mouseY, horizontalAmount, verticalAmount);
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        HudContextMenu typedValue190 = this.internalField0692.internalMethod00450();
        if (typedValue190 != null) {
            typedValue190.internalMethod05727(keyCode, scanCode, modifiers);
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    public boolean charTyped(char chr, int modifiers) {
        HudContextMenu typedValue190 = this.internalField0692.internalMethod00450();
        if (typedValue190 != null) {
            typedValue190.internalMethod05413(chr, modifiers);
        }
        return super.charTyped(chr, modifiers);
    }

    public boolean shouldPause() {
        return false;
    }

    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
    }

    public void close() {
        super.close();
        if (!this.internalField0692.internalMethod02406() && this.internalField0692.internalMethod02403()) {
            this.internalField0692.internalMethod08387();
        }
    }
}

