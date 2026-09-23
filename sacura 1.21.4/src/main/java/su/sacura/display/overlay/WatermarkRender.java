package su.sacura.display.overlay;

import java.awt.Color;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.network.PlayerListEntry;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import su.sacura.display.csgui.helper.ThemeStorage;
import su.sacura.features.modules.settings.impl.ModeListSetting;
import su.sacura.util.impl.math.helper.AnimationUtil;
import su.sacura.util.impl.math.helper.MathUtil;
import su.sacura.util.impl.render.RenderUtils;
import su.sacura.util.impl.render.providers.ColorProvider;
import su.sacura.util.impl.render.providers.FontProvider;
import su.sacura.util.type.MinecraftWrapper;

public class WatermarkRender
        implements MinecraftWrapper {
    private static float animatedFps = 0.0f;
    private static double bps = 0.0;
    private static float posX = 20.0f;
    private static float posY = 20.0f;
    private static boolean dragging = false;
    private static float dragOffsetX = 0.0f;
    private static float dragOffsetY = 0.0f;
    private static boolean wasMouseDown = false;

    public static void render(DrawContext drawContext, int scWidth, int scHeight, ModeListSetting setting) {
        PlayerListEntry entry;
        boolean isMouseDown;
        double mouseX = WatermarkRender.mc.mouse.getX() * (double)mc.getWindow().getScaledWidth() / (double)mc.getWindow().getWidth();
        double mouseY = WatermarkRender.mc.mouse.getY() * (double)mc.getWindow().getScaledHeight() / (double)mc.getWindow().getHeight();
        boolean bl = isMouseDown = GLFW.glfwGetMouseButton((long)mc.getWindow().getHandle(), (int)0) == 1;
        if (WatermarkRender.mc.currentScreen instanceof ChatScreen) {
            if (dragging) {
                posX = (float)(mouseX - (double)dragOffsetX);
                posY = (float)(mouseY - (double)dragOffsetY);
                if (!isMouseDown) {
                    dragging = false;
                }
            } else if (isMouseDown && !wasMouseDown) {
                float checkX = posX;
                float checkY = posY;
                boolean hovered = false;
                if (((Boolean)setting.getValueByName("Логотип").get()).booleanValue()) {
                    if (mouseX >= (double)checkX && mouseX <= (double)(checkX + 20.0f) && mouseY >= (double)checkY && mouseY <= (double)(checkY + 20.0f)) {
                        hovered = true;
                    }
                    checkX += 23.0f;
                }
                String username = mc.getSession().getUsername();
                if (((Boolean)setting.getValueByName("Никнейм").get()).booleanValue()) {
                    float w = 30.0f + FontProvider.regular.getWidth(username, 8.0f);
                    if (mouseX >= (double)checkX && mouseX <= (double)(checkX + w) && mouseY >= (double)checkY && mouseY <= (double)(checkY + 20.0f)) {
                        hovered = true;
                    }
                    checkX += w + 3.0f;
                }
                String fpsText = Math.round(animatedFps) + " fps";
                if (((Boolean)setting.getValueByName("Кадры в секунду").get()).booleanValue()) {
                    float w = 30.0f + FontProvider.regular.getWidth(fpsText, 8.0f);
                    if (mouseX >= (double)checkX && mouseX <= (double)(checkX + w) && mouseY >= (double)checkY && mouseY <= (double)(checkY + 20.0f)) {
                        hovered = true;
                    }
                }
                checkX = posX;
                checkY = posY + 23.0f;
                String pingText = "localhost";
                if (((Boolean)setting.getValueByName("Латенси").get()).booleanValue()) {
                    PlayerListEntry entry2;
                    Object pt = "localhost";
                    if (mc.getNetworkHandler() != null && WatermarkRender.mc.player != null && (entry2 = mc.getNetworkHandler().getPlayerListEntry(WatermarkRender.mc.player.getUuidAsString())) != null && !mc.isInSingleplayer()) {
                        pt = entry2.getLatency() + " latency";
                    }
                    float w = 30.0f + FontProvider.regular.getWidth((String)pt, 8.0f);
                    if (mouseX >= (double)checkX && mouseX <= (double)(checkX + w) && mouseY >= (double)checkY && mouseY <= (double)(checkY + 20.0f)) {
                        hovered = true;
                    }
                    checkX += w + 3.0f;
                }
                if (((Boolean)setting.getValueByName("Скорость").get()).booleanValue()) {
                    double b = 0.0;
                    if (WatermarkRender.mc.player != null) {
                        // Исправлено: lastRenderDistance -> prevX
                        double xDist = WatermarkRender.mc.player.getX() - WatermarkRender.mc.player.prevX;
                        // Исправлено: prevCameraY -> prevZ
                        double zDist = WatermarkRender.mc.player.getZ() - WatermarkRender.mc.player.prevZ;
                        double dist = Math.sqrt(xDist * xDist + zDist * zDist);
                        b = MathUtil.round(dist * 20.0, 1);
                    }
                    String bt = b + " bps";
                    float w = 30.0f + FontProvider.regular.getWidth(bt, 8.0f);
                    if (mouseX >= (double)checkX && mouseX <= (double)(checkX + w) && mouseY >= (double)checkY && mouseY <= (double)(checkY + 20.0f)) {
                        hovered = true;
                    }
                    checkX += w + 3.0f;
                }
                if (((Boolean)setting.getValueByName("Тики сервера").get()).booleanValue()) {
                    float t = 20.0f;
                    if (WatermarkRender.mc.world != null) {
                        t = WatermarkRender.mc.world.getTickManager().getTickRate();
                    }
                    String tt = MathUtil.round(t, 1) + " tps";
                    float w = 30.0f + FontProvider.regular.getWidth(tt, 8.0f);
                    if (mouseX >= (double)checkX && mouseX <= (double)(checkX + w) && mouseY >= (double)checkY && mouseY <= (double)(checkY + 20.0f)) {
                        hovered = true;
                    }
                }
                if (hovered) {
                    dragging = true;
                    dragOffsetX = (float)(mouseX - (double)posX);
                    dragOffsetY = (float)(mouseY - (double)posY);
                }
            }
        }
        wasMouseDown = isMouseDown;
        Matrix4f matrix = drawContext.getMatrices().peek().getPositionMatrix();
        animatedFps = AnimationUtil.animate(mc.getCurrentFps(), animatedFps, 0.05f);
        String username = mc.getSession().getUsername();
        String fpsText = Math.round(animatedFps) + " fps";
        float x = posX;
        Color accent = ThemeStorage.accentColor;
        Color bg = ThemeStorage.backgroundColor;
        Color text = ThemeStorage.textColor;
        int bgRGB = new Color(bg.getRed(), bg.getGreen(), bg.getBlue(), 160).getRGB();
        int textRGB = text.getRGB();
        if (((Boolean)setting.getValueByName("Логотип").get()).booleanValue()) {
            RenderUtils.blur(matrix, x, posY, 20.0f, 20.0f, 6.0f, 10.0f, textRGB);
            RenderUtils.rect(matrix, x, posY, 20.0f, 20.0f, 6.0f, bgRGB);
            FontProvider.logo.draw(matrix, "a", x + 5.0f, posY + 2.0f, 14.0f, ColorProvider.wave(accent.getRGB(), new Color(10, 5, 20).getRGB(), 1.0));
            x += 23.0f;
        }
        if (((Boolean)setting.getValueByName("Никнейм").get()).booleanValue()) {
            float usernameWidth = 30.0f + FontProvider.regular.getWidth(username, 8.0f);
            RenderUtils.blur(matrix, x, posY, usernameWidth, 20.0f, 6.0f, 10.0f, textRGB);
            RenderUtils.rect(matrix, x, posY, usernameWidth, 20.0f, 6.0f, bgRGB);
            FontProvider.icons.draw(matrix, "W", x + 5.0f, posY + 4.5f, 10.0f, ColorProvider.wave(accent.getRGB(), new Color(10, 5, 20).getRGB(), 1.0));
            FontProvider.regular.drawWave(matrix, username, x + 20.0f, posY + 5.0f, 8.0f, textRGB, new Color(100, 100, 100).getRGB());
            x += usernameWidth + 3.0f;
        }
        if (((Boolean)setting.getValueByName("Кадры в секунду").get()).booleanValue()) {
            float fpsWidth = 30.0f + FontProvider.regular.getWidth(fpsText, 8.0f);
            RenderUtils.blur(matrix, x, posY, fpsWidth, 20.0f, 6.0f, 10.0f, textRGB);
            RenderUtils.rect(matrix, x, posY, fpsWidth, 20.0f, 6.0f, bgRGB);
            FontProvider.icons.draw(matrix, "X", x + 4.0f, posY + 4.5f, 11.0f, ColorProvider.wave(accent.getRGB(), new Color(10, 5, 20).getRGB(), 1.0));
            FontProvider.regular.drawWave(matrix, fpsText, x + 22.0f, posY + 5.0f, 8.0f, textRGB, new Color(100, 100, 100).getRGB());
        }
        Object pingText = "localhost";
        if (mc.getNetworkHandler() != null && WatermarkRender.mc.player != null && (entry = mc.getNetworkHandler().getPlayerListEntry(WatermarkRender.mc.player.getUuidAsString())) != null && !mc.isInSingleplayer()) {
            pingText = entry.getLatency() + " latency";
        }
        float pingX = posX;
        float pingY = posY + 23.0f;
        if (((Boolean)setting.getValueByName("Латенси").get()).booleanValue()) {
            float pingWidth = 30.0f + FontProvider.regular.getWidth((String)pingText, 8.0f);
            RenderUtils.blur(matrix, pingX, pingY, pingWidth, 20.0f, 6.0f, 10.0f, textRGB);
            RenderUtils.rect(matrix, pingX, pingY, pingWidth, 20.0f, 6.0f, bgRGB);
            FontProvider.icons.draw(matrix, "Q", pingX + 5.0f, pingY + 4.5f, 10.0f, ColorProvider.wave(accent.getRGB(), new Color(10, 5, 20).getRGB(), 1.0));
            FontProvider.regular.drawWave(matrix, (String)pingText, pingX + 20.0f, pingY + 5.0f, 8.0f, textRGB, new Color(100, 100, 100).getRGB());
            pingX += pingWidth + 3.0f;
        }
        if (WatermarkRender.mc.player != null) {
            // Исправлено: lastRenderDistance -> prevX
            double xDist = WatermarkRender.mc.player.getX() - WatermarkRender.mc.player.prevX;
            // Исправлено: prevCameraY -> prevZ
            double zDist = WatermarkRender.mc.player.getZ() - WatermarkRender.mc.player.prevZ;
            double dist = Math.sqrt(xDist * xDist + zDist * zDist);
            bps = MathUtil.round(dist * 20.0, 1);
        }
        if (((Boolean)setting.getValueByName("Скорость").get()).booleanValue()) {
            String bpsText = bps + " bps";
            float bpsWidth = 30.0f + FontProvider.regular.getWidth(bpsText, 8.0f);
            RenderUtils.blur(matrix, pingX, pingY, bpsWidth, 20.0f, 6.0f, 10.0f, textRGB);
            RenderUtils.rect(matrix, pingX, pingY, bpsWidth, 20.0f, 6.0f, bgRGB);
            FontProvider.icons.draw(matrix, "U", pingX + 5.0f, pingY + 4.5f, 10.0f, ColorProvider.wave(accent.getRGB(), new Color(10, 5, 20).getRGB(), 1.0));
            FontProvider.regular.drawWave(matrix, bpsText, pingX + 20.0f, pingY + 5.0f, 8.0f, textRGB, new Color(100, 100, 100).getRGB());
            pingX += bpsWidth + 3.0f;
        }
        float tps = 20.0f;
        if (WatermarkRender.mc.world != null) {
            tps = WatermarkRender.mc.world.getTickManager().getTickRate();
        }
        if (((Boolean)setting.getValueByName("Тики сервера").get()).booleanValue()) {
            String tickText = MathUtil.round(tps, 1) + " tps";
            float tickWidth = 30.0f + FontProvider.regular.getWidth(tickText, 8.0f);
            RenderUtils.blur(matrix, pingX, pingY, tickWidth, 20.0f, 6.0f, 10.0f, textRGB);
            RenderUtils.rect(matrix, pingX, pingY, tickWidth, 20.0f, 6.0f, bgRGB);
            FontProvider.icons.draw(matrix, "T", pingX + 5.0f, pingY + 4.5f, 10.0f, ColorProvider.wave(accent.getRGB(), new Color(10, 5, 20).getRGB(), 1.0));
            FontProvider.regular.drawWave(matrix, tickText, pingX + 20.0f, pingY + 5.0f, 8.0f, textRGB, new Color(100, 100, 100).getRGB());
        }
    }
}