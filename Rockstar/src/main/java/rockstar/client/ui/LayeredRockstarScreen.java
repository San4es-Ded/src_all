package rockstar.client.ui;



import rockstar.client.render.*;
import rockstar.client.*;
import rockstar.client.internal.ui.*;
import rockstar.client.internal.script.*;
import rockstar.client.compat.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.Vector2f;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.internal.ui.SettingHistoryManager;
import rockstar.client.ui.MouseButton;
import rockstar.client.internal.script.CustomTextField;
import rockstar.client.ui.UiUtils;
import rockstar.client.render.UiBatchRenderer;
import rockstar.client.ui.UiNode;
import rockstar.client.ui.RockstarScreen;
import rockstar.client.internal.script.KeybindButton;
import rockstar.client.internal.script.NumberInputField;

public abstract class LayeredRockstarScreen
extends RockstarScreen {
    protected final List<UiNode> roots = new ArrayList<UiNode>();
    protected final List<UiNode> overlays = new ArrayList<UiNode>();
    private long lastTime = 0L;
    private static long renderClock = 0L;
    protected float contentAlpha = 1.0f;

    public static long renderClock() {
        return renderClock;
    }

    protected <T extends UiNode> T add(T t) {
        this.roots.add(t);
        return t;
    }

    protected void clearRoots() {
        this.roots.clear();
    }

    protected void clearOverlays() {
        this.overlays.forEach(UiNode::discard);
        this.overlays.clear();
    }

    public void init() {
        super.init();
        this.clearOverlays();
    }

    public <T extends UiNode> T openWindow(T t) {
        this.overlays.add(t);
        return t;
    }

    @Override
    public void render(UiRenderContext iII) {
        long l;
        renderClock = l = System.currentTimeMillis();
        float f = this.lastTime == 0L ? 16.0f : Math.min(64.0f, (float)(l - this.lastTime));
        this.lastTime = l;
        float f2 = iII.internalMethod05259();
        float f3 = iII.internalMethod05261();
        try (UiBatchRenderer typedValue250 = this.lowDrawBatching() ? UiBatchRenderer.internalMethod04455() : UiBatchRenderer.internalMethod03756();){
            for (UiNode typedValue005 : this.roots) {
                typedValue005.measure();
                typedValue005.primeSize();
                typedValue005.centerWithin(this.width, this.height);
                typedValue005.tick(f, f2, f3);
            }
            for (UiNode typedValue005 : this.roots) {
                typedValue005.draw(iII, this.contentAlpha);
            }
            this.overlays.removeIf(typedValue004 -> !typedValue004.alive());
            if (!this.overlays.isEmpty() && !UiBatchRenderer.internalMethod02577()) {
                UiBatchRenderer.internalMethod02576();
            }
            for (UiNode typedValue005 : this.overlays) {
                typedValue005.measure();
                typedValue005.primeSize();
                typedValue005.tick(f, f2, f3);
            }
            for (UiNode typedValue005 : this.overlays) {
                typedValue005.draw(iII, this.contentAlpha);
            }
            RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            this.afterRender(iII);
        }
    }

    protected boolean lowDrawBatching() {
        return false;
    }

    protected void afterRender(UiRenderContext iII) {
    }

    public void removed() {
        NumberInputField.internalMethod05283(null);
        Vector2f vector2f = UiUtils.internalMethod03634();
        for (UiNode typedValue004 : this.overlays) {
        typedValue004.mouseReleased(vector2f.x(), vector2f.y(), MouseButton.internalField0102);
        }
        for (UiNode typedValue004 : this.roots) {
        typedValue004.mouseReleased(vector2f.x(), vector2f.y(), MouseButton.internalField0102);
        }
        KeybindButton.internalMethod06276();
        this.overlays.forEach(UiNode::close);
        super.removed();
    }

    @Override
    public void onMouseClicked(double d, double d2, MouseButton typedParameter1015) {
        int n;
        CustomTextField typedValue194 = CustomTextField.internalField0936;
        boolean bl = false;
        for (n = this.overlays.size() - 1; n >= 0; --n) {
            if (!this.overlays.get(n).mouseClicked((float)d, (float)d2, typedParameter1015)) continue;
            bl = true;
            break;
        }
        if (!bl) {
            for (n = this.roots.size() - 1; n >= 0 && !this.roots.get(n).mouseClicked((float)d, (float)d2, typedParameter1015); --n) {
            }
        }
        if (typedValue194 != null && CustomTextField.internalField0936 == typedValue194 && typedValue194.internalMethod00342() && !typedValue194.internalMethod04931(d, d2)) {
            typedValue194.internalMethod07508(false);
        }
    }

    @Override
    public void onMouseReleased(double d, double d2, MouseButton typedParameter1015) {
        for (UiNode typedValue004 : this.overlays) {
            typedValue004.mouseReleased((float)d, (float)d2, typedParameter1015);
        }
        for (UiNode typedValue004 : this.roots) {
            typedValue004.mouseReleased((float)d, (float)d2, typedParameter1015);
        }
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        int n;
        for (n = this.overlays.size() - 1; n >= 0; --n) {
            if (!this.overlays.get(n).mouseScrolled((float)mouseX, (float)mouseY, (float)horizontalAmount, (float)verticalAmount)) continue;
            return true;
        }
        for (n = this.roots.size() - 1; n >= 0; --n) {
            if (!this.roots.get(n).mouseScrolled((float)mouseX, (float)mouseY, (float)horizontalAmount, (float)verticalAmount)) continue;
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        KeyInput input = new KeyInput(keyCode, scanCode, modifiers);
        int n;
        if (!KeybindButton.internalMethod06277()) {
            if (input.hasCtrlOrCmd() && keyCode == 90 && SettingHistoryManager.internalMethod07603()) {
                return true;
            }
            if (input.hasCtrlOrCmd() && keyCode == 89 && SettingHistoryManager.internalMethod07605()) {
                return true;
            }
        }
        for (n = this.overlays.size() - 1; n >= 0; --n) {
            if (!this.overlays.get(n).keyPressed(keyCode, scanCode, modifiers)) continue;
            return true;
        }
        for (n = this.roots.size() - 1; n >= 0; --n) {
            if (!this.roots.get(n).keyPressed(keyCode, scanCode, modifiers)) continue;
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        int n;
        for (n = this.overlays.size() - 1; n >= 0; --n) {
            if (!this.overlays.get(n).keyReleased(keyCode, scanCode, modifiers)) continue;
            return true;
        }
        for (n = this.roots.size() - 1; n >= 0; --n) {
            if (!this.roots.get(n).keyReleased(keyCode, scanCode, modifiers)) continue;
            return true;
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        int n;
        for (n = this.overlays.size() - 1; n >= 0; --n) {
            if (!this.overlays.get(n).charTyped(chr, modifiers)) continue;
            return true;
        }
        for (n = this.roots.size() - 1; n >= 0; --n) {
            if (!this.roots.get(n).charTyped(chr, modifiers)) continue;
            return true;
        }
        return super.charTyped(chr, modifiers);
    }

    public boolean shouldPause() {
        return false;
    }

    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
    }
}
