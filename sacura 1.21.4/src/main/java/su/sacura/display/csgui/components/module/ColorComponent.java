package su.sacura.display.csgui.components.module;

import java.awt.Color;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import su.sacura.display.csgui.helper.ThemeStorage;
import su.sacura.features.modules.settings.impl.ColorSetting;
import su.sacura.util.impl.math.helper.AnimationUtil;
import su.sacura.util.impl.render.RenderUtils;
import su.sacura.util.impl.render.providers.FontProvider;

public class ColorComponent extends Component {
    public ColorSetting setting;

    private boolean expanded = false;
    private float hue = 0.0F;
    private float saturation = 0.0F;
    private float brightness = 0.0F;
    private float alpha = 0.0F;
    private boolean draggingHue = false;
    private boolean draggingSB = false;
    private boolean draggingAlpha = false;
    private boolean popupOpen = false;
    private float popupX = 0.0F;
    private float popupY = 0.0F;
    private float animation = 0.0F;

    public ColorComponent(ColorSetting setting, float width, float height) {
        super(width, height);
        this.setting = setting;
        updateHSB();
    }

    private void updateHSB() {
        int color = ((Integer)this.setting.get()).intValue();
        float[] hsb = Color.RGBtoHSB(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, null);
        this.hue = hsb[0];
        this.saturation = hsb[1];
        this.brightness = hsb[2];
        this.alpha = (color >> 24 & 0xFF) / 255.0F;
    }

    private void updateColor() {
        int rgb = Color.HSBtoRGB(this.hue, this.saturation, this.brightness);
        int a = (int)(this.alpha * 255.0F);
        int color = a << 24 | rgb & 0xFFFFFF;
        this.setting.set(Integer.valueOf(color));
    }

    private int applyAlpha(int color, int alpha) {
        int r = color >> 16 & 0xFF;
        int g = color >> 8 & 0xFF;
        int b = color & 0xFF;
        int a = color >> 24 & 0xFF;
        a = (int)(a * alpha / 255.0F);
        return a << 24 | r << 16 | g << 8 | b;
    }

    private void drawHsvSquare(DrawContext context, float sx, float sy, float sw, float sh, float radius, int globalAlpha) {
        int hueColor = Color.HSBtoRGB(this.hue, 1.0F, 1.0F) & 0xFFFFFF;
        hueColor = globalAlpha << 24 | hueColor;
        int whiteOpaque = globalAlpha << 24 | 0xFFFFFF;
        int whiteTransp = 0x00FFFFFF;
        int blackTransp = 0x00000000;
        int blackOpaque = globalAlpha << 24;

        RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), sx, sy, sw, sh, radius, hueColor);

        RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), sx, sy, sw, sh, radius,
                whiteOpaque, whiteOpaque, whiteTransp, whiteTransp);

        RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), sx, sy, sw, sh, radius,
                blackTransp, blackOpaque, blackOpaque, blackTransp);
    }

    public void render(DrawContext context, float x, float y, int mouseX, int mouseY, int globalAlpha) {
        this.x = x;
        this.y = y;
        this.height = getHeight();
        float delta = 0.07F;
        this.animation = AnimationUtil.lerp(this.animation, this.expanded ? 1.0F : 0.0F, delta);
        String desc = this.setting.getDescription();
        boolean hasDesc = (desc != null && !desc.isEmpty());
        if (hasDesc) {
            FontProvider.regular.draw(context.getMatrices().peek().getPositionMatrix(), this.setting.getName(), x + 5.0F, y + 5.0F, 7.0F, (new Color(255, 255, 255, globalAlpha)).getRGB());
            FontProvider.regular.draw(context.getMatrices().peek().getPositionMatrix(), desc, x + 5.0F, y + 15.0F, 6.0F, (new Color(200, 200, 200, globalAlpha)).getRGB());
        } else {
            FontProvider.regular.draw(context.getMatrices().peek().getPositionMatrix(), this.setting.getName(), x + 5.0F, y + ((hasDesc ? 28 : 18) / 2) - 3.5F, 7.0F, (new Color(255, 255, 255, globalAlpha)).getRGB());
        }
        float boxSize = 10.0F;
        float boxX = x + this.width - boxSize - 7.0F;
        float boxY = y + ((hasDesc ? 28 : 18) - boxSize) / 2.0F;
        RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), boxX, boxY - 1.0F, boxSize + 1.0F, boxSize + 1.0F, 4.5F, ((Integer)this.setting.get()).intValue());
        if (this.animation > 0.01F) {
            float pickerY = y + (hasDesc ? 28 : 18);
            float sbSize = this.width - 40.0F;
            float sbX = x + 5.0F;
            float sbY = pickerY + 5.0F;
            float sbHeight = 60.0F;
            context.enableScissor((int)x, (int)pickerY, (int)(x + this.width), (int)(pickerY + 70.0F * this.animation));

            drawHsvSquare(context, sbX, sbY, sbSize, sbHeight, 5.0F, globalAlpha);

            float indX = sbX + this.saturation * sbSize;
            float indY = sbY + (1.0F - this.brightness) * sbHeight;
            float renderX = Math.max(sbX + 3.0F, Math.min(sbX + sbSize - 3.0F, indX));
            float renderY = Math.max(sbY + 3.0F, Math.min(sbY + sbHeight - 3.0F, indY));
            RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), renderX - 3.0F, renderY - 3.0F, 6.0F, 6.0F, 3.0F, (new Color(0, 0, 0, globalAlpha)).getRGB());
            RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), renderX - 2.0F, renderY - 2.0F, 4.0F, 4.0F, 2.0F, (new Color(255, 255, 255, globalAlpha)).getRGB());

            float hueX = sbX + sbSize + 5.0F;
            float hueY = sbY;
            float hueW = 10.0F;
            float hueH = sbHeight;
            float i;
            for (i = 0.0F; i < hueH; i++) {
                int color = Color.HSBtoRGB(i / hueH, 1.0F, 1.0F);
                color = applyAlpha(color, globalAlpha);
                RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), hueX, hueY + i, hueW, 1.0F, 0.0F, color, 0.0F);
            }
            RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), hueX, hueY + this.hue * hueH - 1.0F, hueW, 2.0F, 1.0F, (new Color(255, 255, 255, globalAlpha)).getRGB());

            float alphaX = hueX + hueW + 5.0F;
            float alphaY = sbY;
            float alphaW = 10.0F;
            float alphaH = sbHeight;

            Color bgMenu = ThemeStorage.backgroundColor;
            int bgAlpha = globalAlpha << 24 | (bgMenu.getRed() & 0xFF) << 16 | (bgMenu.getGreen() & 0xFF) << 8 | (bgMenu.getBlue() & 0xFF);
            RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), alphaX, alphaY, alphaW, alphaH, 0.0F, bgAlpha);

            int baseColor = ((Integer)this.setting.get()).intValue();
            int baseRgb = baseColor & 0xFFFFFF;
            int colorFull = globalAlpha << 24 | baseRgb;
            int colorTrans = baseRgb;
            RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), alphaX, alphaY, alphaW, alphaH, 0.0F,
                    colorTrans, colorFull, colorFull, colorTrans);
            RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), alphaX, alphaY + this.alpha * alphaH - 1.0F, alphaW, 2.0F, 0.0F, (new Color(255, 255, 255, globalAlpha)).getRGB());

            context.disableScissor();
            if (this.expanded)
                if (this.draggingSB) {
                    this.saturation = Math.min(1.0F, Math.max(0.0F, (mouseX - sbX) / sbSize));
                    this.brightness = Math.min(1.0F, Math.max(0.0F, 1.0F - (mouseY - sbY) / sbHeight));
                    updateColor();
                } else if (this.draggingHue) {
                    this.hue = Math.min(1.0F, Math.max(0.0F, (mouseY - hueY) / hueH));
                    updateColor();
                } else if (this.draggingAlpha) {
                    this.alpha = Math.min(1.0F, Math.max(0.0F, (mouseY - alphaY) / alphaH));
                    updateColor();
                }
        }
        if (this.popupOpen) {
            float pW = 50.0F;
            float pH = 24.0F;
            RenderUtils.rect(context.getMatrices().peek().getPositionMatrix(), this.popupX, this.popupY, pW, pH, 2.0F, (new Color(20, 20, 20, 255)).getRGB());
            FontProvider.regular.draw(context.getMatrices().peek().getPositionMatrix(), "Copy", this.popupX + 3.0F, this.popupY + 3.0F, 6.0F, -1);
            FontProvider.regular.draw(context.getMatrices().peek().getPositionMatrix(), "Paste", this.popupX + 3.0F, this.popupY + 13.0F, 6.0F, -1);
        }
    }

    public float getHeight() {
        String desc = this.setting.getDescription();
        float baseHeight = (desc != null && !desc.isEmpty()) ? 28.0F : 18.0F;
        return baseHeight + 70.0F * this.animation;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.popupOpen) {
            float pW = 50.0F;
            float pH = 24.0F;
            if (mouseX >= this.popupX && mouseX <= (this.popupX + pW) && mouseY >= this.popupY && mouseY <= (this.popupY + pH)) {
                if (mouseY < (this.popupY + 12.0F)) {
                    copyToClipboard();
                } else {
                    pasteFromClipboard();
                }
                this.popupOpen = false;
                return true;
            }
            this.popupOpen = false;
            return true;
        }
        String desc = this.setting.getDescription();
        boolean hasDesc = (desc != null && !desc.isEmpty());
        float baseHeight = hasDesc ? 28.0F : 18.0F;
        float boxSize = 10.0F;
        float boxX = this.x + this.width - boxSize - 7.0F;
        float boxY = this.y + (baseHeight - boxSize) / 2.0F;
        if (mouseX >= boxX && mouseX <= (boxX + boxSize) && mouseY >= boxY && mouseY <= (boxY + boxSize) && button == 0) {
            this.expanded = !this.expanded;
            if (!this.expanded)
                this.popupOpen = false;
            return true;
        }
        if (this.expanded && this.animation > 0.9F) {
            float pickerY = this.y + baseHeight;
            float sbSize = this.width - 40.0F;
            float sbX = this.x + 5.0F;
            float sbY = pickerY + 5.0F;
            float sbHeight = 60.0F;
            if (mouseX >= sbX && mouseX <= (sbX + sbSize) && mouseY >= sbY && mouseY <= (sbY + sbHeight)) {
                if (button == 1) {
                    this.popupOpen = true;
                    this.popupX = (float)mouseX;
                    this.popupY = (float)mouseY;
                    return true;
                }
                this.draggingSB = true;
                return true;
            }
            float hueX = sbX + sbSize + 5.0F;
            float hueY = sbY;
            float hueW = 10.0F;
            float hueH = sbHeight;
            if (mouseX >= hueX && mouseX <= (hueX + hueW) && mouseY >= hueY && mouseY <= (hueY + hueH)) {
                this.draggingHue = true;
                return true;
            }
            float alphaX = hueX + hueW + 5.0F;
            float alphaY = sbY;
            float alphaW = 10.0F;
            float alphaH = sbHeight;
            if (mouseX >= alphaX && mouseX <= (alphaX + alphaW) && mouseY >= alphaY && mouseY <= (alphaY + alphaH)) {
                this.draggingAlpha = true;
                return true;
            }
        }
        return false;
    }

    public void mouseReleased(int button) {
        this.draggingSB = false;
        this.draggingHue = false;
        this.draggingAlpha = false;
    }

    private void copyToClipboard() {
        int color = ((Integer)this.setting.get()).intValue();
        String hex = String.format("#%08X", color);
        MinecraftClient.getInstance().keyboard.setClipboard(hex);
    }

    private void pasteFromClipboard() {
        try {
            String data = MinecraftClient.getInstance().keyboard.getClipboard();
            if (data != null) {
                try {
                    int color = parseColor(data);
                    this.setting.set(Integer.valueOf(color));
                    updateHSB();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int parseColor(String s) {
        s = s.trim().toLowerCase();
        if (s.startsWith("#")) {
            s = s.substring(1);
            if (s.length() == 6)
                return 0xFF000000 | Integer.parseInt(s, 16);
            if (s.length() == 8) {
                long v = Long.parseLong(s, 16);
                return (int)v;
            }
        } else if (s.startsWith("rgb")) {
            s = s.replace("rgba", "").replace("rgb", "").replace("(", "").replace(")", "");
            String[] parts = s.split(",");
            if (parts.length >= 3) {
                int r = Integer.parseInt(parts[0].trim());
                int g = Integer.parseInt(parts[1].trim());
                int b = Integer.parseInt(parts[2].trim());
                int a = 255;
                if (parts.length > 3) {
                    String aStr = parts[3].trim();
                    if (aStr.contains(".")) {
                        a = (int)(Float.parseFloat(aStr) * 255.0F);
                    } else {
                        a = Integer.parseInt(aStr);
                    }
                }
                return a << 24 | r << 16 | g << 8 | b;
            }
        } else if (s.contains(",")) {
            String[] parts = s.split(",");
            if (parts.length >= 3) {
                int r = Integer.parseInt(parts[0].trim());
                int g = Integer.parseInt(parts[1].trim());
                int b = Integer.parseInt(parts[2].trim());
                int a = 255;
                if (parts.length > 3)
                    a = Integer.parseInt(parts[3].trim());
                return a << 24 | r << 16 | g << 8 | b;
            }
        }
        return 0;
    }
}