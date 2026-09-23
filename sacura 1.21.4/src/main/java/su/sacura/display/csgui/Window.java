package su.sacura.display.csgui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import su.sacura.display.csgui.helper.WindowRender;
import su.sacura.util.type.MinecraftWrapper;

public class Window extends Screen implements MinecraftWrapper {
    private static Window instance;
    private final WindowRender windowRender;

    public Window() {
        super(Text.of("Плакать"));
        this.windowRender = new WindowRender(100.0F, 100.0F, 500.0F, 300.0F);
    }

    public static Window getInstance() {
        if (instance == null)
            instance = new Window();
        return instance;
    }

    @Override
    public void init() {
        super.init();
        // Никаких tick() тут — тикаем в render()
    }

    public void openGui() {
        // Сначала включаем анимацию открытия, потом открываем Screen
        this.windowRender.onOpen();
        mc.setScreen(this);
    }

    @Override
    public void close() {
        // Не вызываем super.close(), чтобы не сбросить Screen мгновенно —
        // даём анимации закрытия доиграть. Screen закроется сам в render().
        this.windowRender.onClose();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Тикаем анимацию каждый кадр
        this.windowRender.tick();

        // Когда анимация закрытия завершена — закрываем экран
        if (this.windowRender.isClosing() && this.windowRender.getAlpha() < 0.01F) {
            mc.setScreen(null);
            return;
        }

        this.windowRender.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.windowRender.mouseClicked(mouseX, mouseY, button))
            return true;
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (this.windowRender.mouseReleased(mouseX, mouseY, button))
            return true;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.windowRender.mouseDragged(mouseX, mouseY, button, deltaX, deltaY))
            return true;
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (this.windowRender.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount))
            return true;
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // Сначала отдаём ввод компонентам (StringComponent перехватит ESC и Enter)
        if (this.windowRender.keyPressed(keyCode, scanCode, modifiers))
            return true;

        // Только если никто не обработал — ESC закрывает окно
        if (keyCode == 256) {
            this.close();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (this.windowRender.charTyped(chr, modifiers))
            return true;
        return super.charTyped(chr, modifiers);
    }

    // Не ставим игру на паузу (для одиночной игры — по желанию)
    @Override
    public boolean shouldPause() {
        return false;
    }
}