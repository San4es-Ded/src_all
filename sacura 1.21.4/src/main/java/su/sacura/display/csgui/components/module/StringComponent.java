package su.sacura.display.csgui.components.module;

import java.awt.Color;
import net.minecraft.client.gui.DrawContext;
import su.sacura.features.modules.settings.impl.StringSetting;
import su.sacura.util.impl.render.RenderUtils;
import su.sacura.util.impl.render.providers.FontProvider;

public class StringComponent extends Component {
    public StringSetting setting;
    private boolean focused = false;
    private String currentText;
    private long lastBlink = 0L;
    private boolean cursorVisible = true;

    public StringComponent(StringSetting setting, float width, float height) {
        super(width, height);
        this.setting = setting;
        this.currentText = (String)setting.get();
    }

    public void render(DrawContext context, float x, float y, int mouseX, int mouseY, int globalAlpha) {
        this.x = x;
        this.y = y;
        this.height = 40.0F;

        String desc = this.setting.getDescription();
        boolean hasDesc = (desc != null && !desc.isEmpty());

        if (hasDesc) {
            FontProvider.regular.draw(context.getMatrices().peek().getPositionMatrix(), this.setting.getName(), x + 5.0F, y + 5.0F, 7.0F, (new Color(255, 255, 255, globalAlpha)).getRGB());
            FontProvider.regular.draw(context.getMatrices().peek().getPositionMatrix(), desc, x + 5.0F, y + 15.0F, 6.0F, (new Color(200, 200, 200, globalAlpha)).getRGB());
        } else {
            float collapsedHeight = 38.0F;
            FontProvider.regular.draw(context.getMatrices().peek().getPositionMatrix(), this.setting.getName(), x + 5.0F, y + collapsedHeight / 2.0F - 3.5F, 7.0F, (new Color(255, 255, 255, globalAlpha)).getRGB());
        }

        float valX = x + 5.0F;
        float valY = y + (hasDesc ? 28 : 22);
        int r = 50, g = 50, b = 50;
        int a = (int)(200.0F * globalAlpha / 255.0F);
        int borderColor = (a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF;
        float boxHeight = 16.0F;

        int bgAlpha = focused ? (int)(40.0F * globalAlpha / 255.0F) : (int)(10.0F * globalAlpha / 255.0F);
        RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), valX, valY - 2.0F, this.width - 10.0F, boxHeight, 3.0F, (new Color(200, 200, 200, bgAlpha)).getRGB());
        RenderUtils.border(context.getMatrices().peek().getPositionMatrix(), valX, valY - 2.0F, this.width - 10.0F, boxHeight, 3.0F, 0.1F, 0.5F, 1.0F, borderColor);

        String display = this.currentText == null ? "" : this.currentText;
        if (!display.isEmpty()) {
            FontProvider.regular.draw(context.getMatrices().peek().getPositionMatrix(), display, valX + 5.0F, valY + 1.5F, 7.0F,
                    (new Color(255, 255, 255, globalAlpha)).getRGB());
        }

        // Мигающий курсор
        if (this.focused) {
            long now = System.currentTimeMillis();
            if (now - this.lastBlink > 500L) {
                this.cursorVisible = !this.cursorVisible;
                this.lastBlink = now;
            }
            if (this.cursorVisible) {
                // Ширина текста + маленький отступ (3px), чтобы курсор стоял чуть правее последней буквы
                float textWidth = display.isEmpty() ? 0.0F : FontProvider.regular.getWidth(display, 7.0F);
                float cursorX = valX + 5.0F + textWidth + 3.0F;
                RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), cursorX, valY + 1.0F, 1.0F, 11.0F, 0.0F,
                        (new Color(255, 255, 255, globalAlpha)).getRGB());
            }
        } else {
            this.cursorVisible = true;
        }
    }

    public float getHeight() {
        String desc = this.setting.getDescription();
        return (desc != null && !desc.isEmpty()) ? 48.0F : 40.0F;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        String desc = this.setting.getDescription();
        boolean hasDesc = (desc != null && !desc.isEmpty());
        float valX = this.x + 5.0F;
        float valY = this.y + (hasDesc ? 28 : 22);

        if (mouseX >= valX && mouseX <= (valX + this.width - 10.0F) && mouseY >= (valY - 2.0F) && mouseY <= (valY - 2.0F + 16.0F)) {
            if (button == 0) {
                this.focused = true;
                this.lastBlink = System.currentTimeMillis();
                this.cursorVisible = true;
                return true;
            }
        } else {
            if (this.focused) {
                this.setting.set(this.currentText);
            }
            this.focused = false;
        }
        return false;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.focused) {
            if (keyCode == 256) { // ESC
                this.focused = false;
                this.currentText = (String)this.setting.get();
                return true;
            }
            if (keyCode == 257 || keyCode == 335) { // Enter / Numpad Enter
                this.setting.set(this.currentText);
                this.focused = false;
                return true;
            }
            if (keyCode == 259) { // Backspace
                if (this.currentText != null && this.currentText.length() > 0) {
                    this.currentText = this.currentText.substring(0, this.currentText.length() - 1);
                }
                return true;
            }
        }
        return false;
    }

    public boolean charTyped(char chr, int modifiers) {
        if (this.focused) {
            if (chr >= ' ' && chr != 127) {
                if (this.currentText == null) this.currentText = "";
                this.currentText += chr;
            }
            return true;
        }
        return false;
    }
}