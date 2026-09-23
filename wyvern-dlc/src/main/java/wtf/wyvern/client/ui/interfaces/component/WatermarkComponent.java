package wtf.wyvern.client.ui.interfaces.component;

import java.util.Iterator;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.util.Identifier;
import org.joml.Vector4f;
import wtf.wyvern.Wyvern;
import wtf.wyvern.core.animations.base.Animation;
import wtf.wyvern.core.animations.base.Easing;
import wtf.wyvern.core.font.Fonts;
import wtf.wyvern.core.theme.Theme;
import wtf.wyvern.client.ui.interfaces.draggable.DraggableHudElement;
import wtf.wyvern.render.display.base.BorderRadius;
import wtf.wyvern.render.display.base.CustomDrawContext;
import wtf.wyvern.render.display.base.color.ColorRGBA;
import wtf.wyvern.render.display.shader.DrawUtil;
import net.minecraft.util.math.MathHelper;
import wtf.wyvern.client.modules.impl.misc.NameProtect;

public class WatermarkComponent extends DraggableHudElement {
    private static final java.time.format.DateTimeFormatter HH_MM = java.time.format.DateTimeFormatter.ofPattern("HH:mm");
    private static final Identifier GLOW_TEXTURE = Identifier.of("wyvern", "icons/glow.png");
    private final Animation widthAnimation;
    private final Animation widthAnimation2;
    private final boolean compact;

    public WatermarkComponent(String name, float initialX, float initialY, float windowWidth, float windowHeight, float offsetX, float offsetY, Align align, boolean compact) {
        super(name, initialX, initialY, windowWidth, windowHeight, offsetX, offsetY, align);
        this.widthAnimation = new Animation(200L, Easing.CUBIC_OUT);
        this.widthAnimation2 = new Animation(200L, Easing.CUBIC_OUT);
        this.compact = compact;
    }

    public void render(CustomDrawContext ctx) {
        if (mc.player != null) {
            float posX = this.getX();
            float posY = this.getY();
            Theme theme = Wyvern.getInstance().getThemeManager().getCurrentTheme();
            ColorRGBA themeColor = theme.getColor();

            if (compact) {
                renderV2(ctx, posX, posY, themeColor);
            } else {
                renderClassic(ctx, posX, posY, themeColor);
            }
        }
    }

    private void renderV2(CustomDrawContext ctx, float posX, float posY, ColorRGBA themeColor) {
        float size = 26.0F;
        float height = 24.0F;
        float cornerRadius = 5.5F; // Радиус как у Potions
        float microRadius = 1.5F;  // Самое легкое скругление для правых углов

        // --- ЦВЕТА (В стиле шапки Potions/Keybinds) ---
        Vector4f outlineVec = new Vector4f(0.09F, 0.09F, 0.09F, 1.0F); // #171717
        ColorRGBA figmaOutlineColor = new ColorRGBA((int)(outlineVec.x * 255), (int)(outlineVec.y * 255), (int)(outlineVec.z * 255), 255);

        Vector4f bodyVec = new Vector4f(0.043F, 0.043F, 0.043F, 1.0F); // #0B0B0B (11, 11, 11)
        ColorRGBA bodyColor = new ColorRGBA((int)(bodyVec.x * 255), (int)(bodyVec.y * 255), (int)(bodyVec.z * 255), 255);

        float strokeThickness = 1.0F;

        // --- РЕНДЕР ПЕРВОГО РЕКТА (ЛОГОТИП) ---
        BorderRadius outerRounding1 = new BorderRadius(cornerRadius, microRadius, microRadius, cornerRadius);
        DrawUtil.drawRoundedRect(ctx.getMatrices(), posX, posY, size, height, outerRounding1, figmaOutlineColor);

        BorderRadius innerRounding1 = new BorderRadius(cornerRadius - strokeThickness, microRadius - strokeThickness, microRadius - strokeThickness, cornerRadius - strokeThickness);
        DrawUtil.drawRoundedRect(ctx.getMatrices(), posX + strokeThickness, posY + strokeThickness, size - (strokeThickness * 2), height - (strokeThickness * 2), innerRounding1, bodyColor);

        String logoChar = "A";
        float logoSize = 12F;
        float logoW = Fonts.WYVERN.getWidth(logoChar, logoSize);
        float logoH = 6.5F;
        float logoX = posX + (size - logoW) / 2.0F + 0.5F;
        float logoY = posY + (height - logoH) / 2.0F - 1.7f;

        // Glow for Logo
        float glowW = 34.0F;
        float glowH = 20.0F;
        ctx.drawTexture(GLOW_TEXTURE, logoX + (logoW / 2.0F) - (glowW / 2.0F) - 0.5F, logoY + (logoH / 2.0F) - (glowH / 2.0F), glowW, glowH, themeColor.withAlpha(22));
        ctx.drawText(Fonts.WYVERN.getFont(logoSize), logoChar, logoX, logoY, themeColor);

        // --- ДАННЫЕ ДЛЯ ВТОРОГО РЕКТА ---
        // Keep the compact watermark behind the same NameProtect filter as
        // the classic layout.
        String name = NameProtect.getWatermarkName();
        String time = HH_MM.format(java.time.LocalTime.now());

        // Иконки (предположительно 'G' - user/avatar, 't' - time/clock в шрифте WYVERN)
        String userIcon = "G";
        String clockIcon = "LZXCV";
        float userIconSize = 11.5F;
        float clockIconSize = 10.2F;

        float textScale = 6.5F;
        float dotScale = 11.0F;

        float nameW = Fonts.MEDIUM.getWidth(name, textScale);
        float timeW = Fonts.MEDIUM.getWidth(time, textScale);
        float slashW = Fonts.BOLD.getWidth("/", textScale);
        float dotW = Fonts.BOLD.getWidth(".", dotScale);
        float userIconW = Fonts.WYVERN.getWidth(userIcon, userIconSize);
        float clockIconW = Fonts.WYVERN.getWidth(clockIcon, clockIconSize);

        float spacing = 6.5F;
        float innerGap = 3.5F;

        float rect2Width = spacing * 2 + userIconW + innerGap + dotW + innerGap + nameW + innerGap + slashW + innerGap + clockIconW + innerGap + dotW + innerGap + timeW;
        float posX2 = posX + size + 4.0F;

        // --- РЕНДЕР ВТОРОГО РЕКТА (ИНФОРМАЦИЯ) ---
        BorderRadius outerRounding2 = new BorderRadius(microRadius, cornerRadius, cornerRadius, microRadius);
        DrawUtil.drawRoundedRect(ctx.getMatrices(), posX2, posY, rect2Width, height, outerRounding2, figmaOutlineColor);

        BorderRadius innerRounding2 = new BorderRadius(microRadius - strokeThickness, cornerRadius - strokeThickness, cornerRadius - strokeThickness, microRadius - strokeThickness);
        DrawUtil.drawRoundedRect(ctx.getMatrices(), posX2 + strokeThickness, posY + strokeThickness, rect2Width - (strokeThickness * 2), height - (strokeThickness * 2), innerRounding2, bodyColor);

        // Отрисовка содержимого второго ректа
        float currentX = posX2 + spacing;
        float contentY = posY + (height / 2.0F);

        // 1. User Icon (G)
        float uX = currentX - 0.5F;
        float uY = contentY - (userIconSize / 2.0F) + 1.2f;
        ctx.drawTexture(GLOW_TEXTURE, uX + (userIconW / 2.0F) - (glowW / 2.0F) - 0.5F, uY + (userIconSize / 2.0F) - (glowH / 2.0F), glowW, glowH, themeColor.withAlpha(22));
        ctx.drawText(Fonts.WYVERN.getFont(userIconSize), userIcon, uX, uY, themeColor);
        currentX += userIconW + innerGap;

        // 2. Separator Dot
        ctx.drawText(Fonts.BOLD.getFont(dotScale), ".", currentX, contentY - (dotScale / 2.0F) - 1.0F, ColorRGBA.WHITE.withAlpha(110));
        currentX += dotW + innerGap;

        // 3. Player Name
        ctx.drawText(Fonts.MEDIUM.getFont(textScale), name, currentX, contentY - (textScale / 2.0F) + 1.0F, ColorRGBA.WHITE);
        currentX += nameW + innerGap;

        // 4. Slash /
        ctx.drawText(Fonts.BOLD.getFont(textScale), "/", currentX, contentY - (textScale / 2.0F) + 0.5F, ColorRGBA.WHITE.withAlpha(150));
        currentX += slashW + innerGap;

        // 5. Clock Icon
        float cX = currentX;
        float cY = contentY - (clockIconSize / 2.0F) + 1.0F;
        ctx.drawTexture(GLOW_TEXTURE, cX + (clockIconW / 2.0F) - (glowW / 2.0F) - 0.5F, cY + (clockIconSize / 2.0F) - (glowH / 2.0F), glowW, glowH, themeColor.withAlpha(22));
        ctx.drawText(Fonts.WYVERN.getFont(clockIconSize), clockIcon, cX, cY, themeColor);
        currentX += clockIconW + innerGap;

        // 6. Separator Dot
        ctx.drawText(Fonts.BOLD.getFont(dotScale), ".", currentX, contentY - (dotScale / 2.0F) - 1.0F, ColorRGBA.WHITE.withAlpha(110));
        currentX += dotW + innerGap;

        // 7. Time
        ctx.drawText(Fonts.MEDIUM.getFont(textScale), time, currentX, contentY - (textScale / 2.0F) + 1.0F, ColorRGBA.WHITE);

        this.width = size + 4.0F + rect2Width;
        this.height = height;
    }

    private void renderClassic(CustomDrawContext ctx, float posX, float posY, ColorRGBA themeColor) {
        String name = NameProtect.getWatermarkName();
        String fpsVal = String.valueOf(mc.getCurrentFps());
        String ip = mc.getCurrentServerEntry() != null ? mc.getCurrentServerEntry().address : "Singleplayer";
        String version = " 1.21.4";

        float padding = 6.0F;
        float interSectionGap = 8.0F;

        float logoW = Fonts.MEDIUM.getWidth("Wyvern " + version, 7.5F);
        float nameW = Fonts.MEDIUM.getWidth(name, 7.2F) + 10.5F;
        float fpsW = Fonts.MEDIUM.getWidth(fpsVal + " fps", 7.2F) + 10.0F;
        float ipW = Fonts.MEDIUM.getWidth(ip, 7.2F) + 10.0F;

        float targetWidth = padding * 2 + logoW + 8.0F + nameW + interSectionGap + fpsW + interSectionGap + ipW + 2.0F;
        this.widthAnimation.update(targetWidth);
        float currentWidth = this.widthAnimation.getValue();
        float totalHeight = 18.0F;
        float rounding = 4.0F;

        DrawUtil.drawBlur(ctx.getMatrices(), posX, posY, currentWidth, totalHeight, 15.0F, BorderRadius.all(rounding), ColorRGBA.WHITE.withAlpha(255));
        DrawUtil.drawRoundedRect(ctx.getMatrices(), posX, posY, currentWidth, totalHeight, BorderRadius.all(rounding), new ColorRGBA(0, 0, 0, 125));

        float logoPartWidth = logoW + padding * 2;
        DrawUtil.drawRoundedRect(ctx.getMatrices(), posX, posY, logoPartWidth, totalHeight, new BorderRadius(rounding, 0, 0, rounding), new ColorRGBA(0, 0, 0, 255));

        ctx.drawText(Fonts.MEDIUM.getFont(7.5F), "Wyvern ", posX + padding, posY + 6.0F, ColorRGBA.WHITE);
        ctx.drawText(Fonts.MEDIUM.getFont(7.5F), version, posX + padding + Fonts.MEDIUM.getWidth("Wyvern ", 7.5F), posY + 6.0F, themeColor);

        float currentX = posX + logoPartWidth + 7.0F;
        float iconY = posY + 7.0F;
        float textY = posY + 7.0F;

        ctx.drawText(Fonts.FONT.getFont(8.5F), "e", currentX - 2.0F, iconY, themeColor);
        ctx.drawText(Fonts.MEDIUM.getFont(7.2F), name, currentX + 8.0F, textY - 0.2F, ColorRGBA.WHITE);
        currentX += nameW + interSectionGap;

        ctx.drawText(Fonts.FONT.getFont(8.5F), "m", currentX - 3.5F, iconY + 0.2F, themeColor);
        ctx.drawText(Fonts.MEDIUM.getFont(7.2F), fpsVal, currentX + 7.5F, textY - 0.2F, ColorRGBA.WHITE);
        ctx.drawText(Fonts.MEDIUM.getFont(7.2F), "FPS", currentX + 7.5F + Fonts.MEDIUM.getWidth(fpsVal, 7.2F) + 1.5F, textY - 0.2F, new ColorRGBA(180, 180, 180, 255));
        currentX += fpsW + interSectionGap;

        ctx.drawText(Fonts.FONT.getFont(8.8F), "q", currentX - 3.0F, iconY + 0.1F, themeColor);
        ctx.drawText(Fonts.MEDIUM.getFont(7.2F), ip, currentX + 7.5F, textY - 0.6F, ColorRGBA.WHITE);

        this.width = currentWidth;
        this.height = totalHeight;
    }
}
