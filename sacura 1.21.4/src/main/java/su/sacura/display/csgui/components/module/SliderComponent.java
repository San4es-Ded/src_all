package su.sacura.display.csgui.components.module;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;
import net.minecraft.client.gui.DrawContext;
import su.sacura.display.csgui.helper.ThemeStorage;
import su.sacura.features.modules.settings.impl.SliderSetting;
import su.sacura.util.impl.render.RenderUtils;
import su.sacura.util.impl.render.providers.FontProvider;

public class SliderComponent extends Component {
    public SliderSetting setting;

    private boolean dragging = false;
    private float animation = 0.0F;
    private float velocity = 0.0F;
    private long lastTime = 0L;

    private static final float SPRING_STIFFNESS = 200.0F;
    private static final float SPRING_DAMPING = 22.0F;

    public SliderComponent(SliderSetting setting, float width, float height) {
        super(width, height);
        this.setting = setting;
    }

    public void render(DrawContext context, float x, float y, int mouseX, int mouseY, int globalAlpha) {
        float sliderYOffset;
        this.x = x;
        this.y = y;
        this.height = getHeight();
        long currentTime = System.nanoTime();
        if (this.lastTime == 0L)
            this.lastTime = currentTime;
        float delta = (float)(currentTime - this.lastTime) / 1.0E9F;
        this.lastTime = currentTime;
        if (delta > 0.1F)
            delta = 0.1F;
        if (delta < 0.0F)
            delta = 0.0F;
        String desc = this.setting.getDescription();
        boolean hasDesc = (desc != null && !desc.isEmpty());
        FontProvider.regular.draw(context.getMatrices().peek().getPositionMatrix(), this.setting.getName(), x + 5.0F, y + 5.0F, 7.0F, (new Color(255, 255, 255, globalAlpha)).getRGB());
        String valueString = String.valueOf(roundToPlace(((Float)this.setting.get()).floatValue(), 2)).replace(".", ",");
        float valueWidth = FontProvider.regular.getWidth(valueString, 7.0F);
        FontProvider.regular.draw(context.getMatrices().peek().getPositionMatrix(), valueString + " юнитов", x + this.width - valueWidth - 35.0F, y + 5.0F, 7.0F, (new Color(255, 255, 255, globalAlpha)).getRGB());
        if (hasDesc) {
            FontProvider.regular.draw(context.getMatrices().peek().getPositionMatrix(), desc, x + 5.0F, y + 15.0F, 6.0F, (new Color(200, 200, 200, globalAlpha)).getRGB());
            sliderYOffset = 28.0F;
        } else {
            sliderYOffset = 18.0F;
        }
        float sliderX = x + 5.0F, sliderWidth = this.width - 10.0F, sliderHeight = 4.0F, sliderY = y + sliderYOffset;
        if (this.dragging) {
            float diff = Math.min(sliderWidth, Math.max(0.0F, mouseX - sliderX)), f1 = this.setting.min, f2 = this.setting.max;
            if (diff == 0.0F) {
                this.setting.set(Float.valueOf(f1));
            } else {
                float value = roundToPlace(diff / sliderWidth * (f2 - f1) + f1, 2);
                this.setting.set(Float.valueOf(value));
            }
        }
        Color accent = ThemeStorage.accentColor;
        Color borderC = ThemeStorage.borderColor;
        Color textC = ThemeStorage.textColor;
        float min = this.setting.min;
        float max = this.setting.max;
        float current = ((Float)this.setting.get()).floatValue();
        float renderWidth = sliderWidth * (current - min) / (max - min);

        float force = (renderWidth - this.animation) * SPRING_STIFFNESS - this.velocity * SPRING_DAMPING;
        this.velocity += force * delta;
        this.animation += this.velocity * delta;
        if (Math.abs(renderWidth - this.animation) < 0.05F && Math.abs(this.velocity) < 0.5F) {
            this.animation = renderWidth;
            this.velocity = 0.0F;
        }
        if (this.animation < 0.0F) {
            this.animation = 0.0F;
            this.velocity = 0.0F;
        }
        if (this.animation > sliderWidth) {
            this.animation = sliderWidth;
            this.velocity = 0.0F;
        }

        RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), sliderX, sliderY, sliderWidth, sliderHeight, 1.0F, (new Color(borderC.getRed(), borderC.getGreen(), borderC.getBlue(), globalAlpha)).getRGB());
        int r = accent.getRed();
        int g = accent.getGreen();
        int b = accent.getBlue();
        int color = globalAlpha << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF;
        RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), sliderX, sliderY, this.animation, sliderHeight, 1.0F, color);
        RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), Math.max(sliderX, sliderX + this.animation - 7.0F), sliderY - 1.5F, 7.0F, 7.0F, 2.5F, (new Color(textC.getRed(), textC.getGreen(), textC.getBlue(), globalAlpha)).getRGB());
    }

    public float getHeight() {
        String desc = this.setting.getDescription();
        return (desc != null && !desc.isEmpty()) ? 40.0F : 30.0F;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        String desc = this.setting.getDescription();
        boolean hasDesc = (desc != null && !desc.isEmpty());
        float sliderYOffset = hasDesc ? 28.0F : 18.0F, sliderX = this.x + 5.0F, sliderWidth = this.width - 10.0F, sliderHeight = 8.0F, sliderY = this.y + sliderYOffset;
        if (mouseX >= sliderX && mouseX <= (sliderX + sliderWidth) && mouseY >= (sliderY - 2.0F) && mouseY <= (sliderY + sliderHeight + 2.0F) && button == 0) {
            this.dragging = true;
            return true;
        }
        return false;
    }

    public void mouseReleased(int button) {
        if (button == 0)
            this.dragging = false;
    }

    private float roundToPlace(float value, int places) {
        if (places < 0)
            throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.floatValue();
    }
}