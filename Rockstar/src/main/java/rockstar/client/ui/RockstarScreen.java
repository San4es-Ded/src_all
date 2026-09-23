package rockstar.client.ui;


import rockstar.client.*;
import rockstar.client.internal.core.*;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Click;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.ui.MouseButton;
import rockstar.client.internal.core.DrawCallCounter;

public abstract class RockstarScreen
extends Screen {
    protected RockstarScreen() {
        super((Text)Text.empty());
    }

    public abstract void render(UiRenderContext localValue1);

    /**
     * Rockstar screens render their own animated/blurred background.  Since
     * 1.21.11 extracts vanilla GUI elements and submits them later, the
     * inherited panorama/darkening layer would otherwise be queued here and
     * drawn over all of Rockstar's immediate-mode controls.
     */
    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public final void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        UiRenderContext iII = UiRenderContext.internalMethod02316(context, mouseX, mouseY, delta);
        DrawCallCounter.internalMethod08091();
        try {
            this.render(iII);
        }
        finally {
            DrawCallCounter.internalMethod08092();
        }
    }

    @Override
    public final boolean mouseClicked(Click click, boolean doubled) {
        MouseButton typedParameter1015 = MouseButton.internalMethod01669(click.button());
        this.onMouseClicked(click.x(), click.y(), typedParameter1015);
        return super.mouseClicked(click, doubled);
    }

    @Override
    public final boolean mouseReleased(Click click) {
        MouseButton typedParameter1015 = MouseButton.internalMethod01669(click.button());
        this.onMouseReleased(click.x(), click.y(), typedParameter1015);
        return super.mouseReleased(click);
    }

    @Override
    public final boolean mouseDragged(Click click, double deltaX, double deltaY) {
        MouseButton typedParameter1015 = MouseButton.internalMethod01669(click.button());
        this.onMouseDragged(click.x(), click.y(), typedParameter1015, deltaX, deltaY);
        return super.mouseDragged(click, deltaX, deltaY);
    }

    public void onMouseClicked(double d, double d2, MouseButton typedParameter1015) {
    }

    public void onMouseReleased(double d, double d2, MouseButton typedParameter1015) {
    }

    public void onMouseDragged(double d, double d2, MouseButton typedParameter1015, double d3, double d4) {
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        return this.keyPressed(input.key(), input.scancode(), input.modifiers());
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return super.keyPressed(new KeyInput(keyCode, scanCode, modifiers));
    }

    @Override
    public boolean keyReleased(KeyInput input) {
        return this.keyReleased(input.key(), input.scancode(), input.modifiers());
    }

    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return super.keyReleased(new KeyInput(keyCode, scanCode, modifiers));
    }

    @Override
    public boolean charTyped(CharInput input) {
        return this.charTyped((char)input.codepoint(), input.modifiers());
    }

    public boolean charTyped(char chr, int modifiers) {
        return super.charTyped(new CharInput(chr, modifiers));
    }
}
