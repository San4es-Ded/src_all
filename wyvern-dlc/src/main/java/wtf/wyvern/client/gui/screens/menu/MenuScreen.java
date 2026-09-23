package wtf.wyvern.client.gui.screens.menu;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.Window;
import net.minecraft.text.Text;
import wtf.wyvern.Wyvern;
import wtf.wyvern.core.animations.base.Animation;
import wtf.wyvern.core.animations.base.Easing;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.impl.render.Menu;
import wtf.wyvern.utility.interfaces.IClient;
import wtf.wyvern.utility.interfaces.IMinecraft;
import wtf.wyvern.client.gui.screens.menu.wonderful.CsGuiInputHandler;
import wtf.wyvern.client.gui.screens.menu.wonderful.CsGuiRenderer;
import wtf.wyvern.client.gui.screens.menu.wonderful.ClickGuiState;

public class MenuScreen extends Screen implements IClient {
    private static final ClickGuiState STATE = new ClickGuiState();
    private final ClickGuiState state = STATE;
    private final CsGuiRenderer renderer = new CsGuiRenderer();
    private final CsGuiInputHandler inputHandler = new CsGuiInputHandler(state, renderer);

    protected final Animation openAnimation;
    public final Animation openAnimationMetanoise;
    public boolean needToClose;
    public boolean search;
    public Runnable savedRunnable;

    public MenuScreen() {
        super(Text.of("Menu"));
        this.openAnimation = new Animation(200L, Easing.CUBIC_OUT);
        this.openAnimationMetanoise = new Animation(1000L, Easing.CUBIC_OUT);
        state.refreshModules();
    }

    @Override
    protected void init() {
        super.init();
        this.needToClose = false;
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.updateAnimations();
        float progress = this.openAnimation.getValue();

        if (this.needToClose && progress < 0.02F) {
            mc.setScreen(null);
            return;
        }

        Window window = IMinecraft.mc.getWindow();
        int categoryCount = Category.values().length;
        state.updatePosition(window, categoryCount);
        state.setRenderOffsetY((1.0f - progress) * 15.0f);

        renderer.render(context, mouseX, mouseY, window, progress, state);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (needToClose) return true;
        return inputHandler.mouseClicked(mouseX, mouseY, button, IMinecraft.mc.getWindow()) || super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (needToClose) return true;
        return inputHandler.mouseReleased(button) || super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (needToClose) return true;
        return inputHandler.mouseDragged(mouseX, mouseY, button) || super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (needToClose) return true;
        return inputHandler.mouseScrolled(mouseX, mouseY, verticalAmount) || super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (inputHandler.keyPressed(keyCode, modifiers)) {
            return true;
        }
        if (keyCode == 256) {
            this.needToClose = true;
            Menu.INSTANCE.setEnabled(false);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (needToClose) return true;
        return inputHandler.charTyped(chr, modifiers) || super.charTyped(chr, modifiers);
    }

    public boolean shouldCloseOnEsc() {
        return true;
    }

    private void updateAnimations() {
        this.openAnimation.update(!this.needToClose);
        this.openAnimationMetanoise.update(!this.needToClose);
    }
}
