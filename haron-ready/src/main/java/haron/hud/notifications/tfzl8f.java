package haron.hud.notifications;

import haron.hud.core.HudIconRenderer;
import haron.hud.notifications.ijsfy3;
import haron.modules.hud.ClientColor;
import haron.render.font.FontRenderer;
import haron.render.font.ClientFonts;
import haron.render.ShapeRenderer;
import haron.theme.pryrvd;
import java.awt.Color;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.math.MatrixStack;

public class tfzl8f
extends ijsfy3 {
    private static final Color TEXT_WHITE;
    private static final Color TEXT_DIM;
    private static final Color AVATAR_COLOR;
    private static final float BLOCK_H = 32.0f;
    private static final float RADIUS = 12.0f;
    private static final float AVATAR_SIZE = 20.0f;
    private static final float AVATAR_BG_SIZE = 22.0f;
    private static final float AVATAR_RADIUS = 7.0f;
    private static final int FONT_SIZE = 14;
    private static final String TG_PREFIX;
    private static final String TG_HARON;
    private static final String TG_VISUALS;

    public void renderStandalone(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3) {
        if (f3 < 0.01f) {
            return;
        }
        float f4 = this.c();
        float f5 = this.d();
        Color color = this.a(pryrvd.a(pryrvd.PANEL_BG_TOP, 242), f3);
        Color color2 = this.a(pryrvd.a(pryrvd.PANEL_BG_BOT, 245), f3);
        s7swsm2.a(f, f2, f4, f5, 12.0f, color, color, color, color, matrixStack);
        s7swsm2.a(f, f2, 42.0f, f5, 12.0f, color2, color2, color2, color2, matrixStack);
        s7swsm2.a(f + 34.0f, f2, 8.0f, f5, 0.0f, color2, color2, color2, color2, matrixStack);
        this.a(matrixStack, s7swsm2, f, f2, this.a(), this.b(), f3);
    }

    private void drawPingIcon(ShapeRenderer s7swsm2, MatrixStack matrixStack, float f, float f2, Color color) {
        s7swsm2.a(f, f2 + 2.0f, 5.8f, 1.2f, 0.5f, color, color, color, color, matrixStack);
        s7swsm2.a(f + 0.8f, f2 - 0.8f, 4.5f, 1.2f, 0.5f, color, color, color, color, matrixStack);
        s7swsm2.a(f + 1.6f, f2 - 3.6f, 3.2f, 1.2f, 0.5f, color, color, color, color, matrixStack);
    }

    private void drawFpsIcon(ShapeRenderer s7swsm2, MatrixStack matrixStack, float f, float f2, Color color) {
        s7swsm2.a(f, f2 - 3.5f, 1.2f, 7.0f, 0.5f, color, color, color, color, matrixStack);
        s7swsm2.a(f + 3.0f, f2 - 2.2f, 1.2f, 4.4f, 0.5f, color, color, color, color, matrixStack);
        s7swsm2.a(f + 6.0f, f2 - 4.2f, 1.2f, 5.8f, 0.5f, color, color, color, color, matrixStack);
    }

    private void drawSeparator(ShapeRenderer s7swsm2, MatrixStack matrixStack, float f, float f2, Color color) {
        s7swsm2.a(f, f2 - 8.0f, 1.0f, 16.0f, 0.5f, color, color, color, color, matrixStack);
    }

    private int getFps() {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (minecraftClient == null) {
            return 0;
        }
        return Math.max(0, minecraftClient.getCurrentFps());
    }

    private float drawPart(FontRenderer v6hnga2, String string, float f, float f2, Color color, float f3, MatrixStack matrixStack) {
        v6hnga2.a(string, f, (double)f2, this.a(color, f3), matrixStack);
        return f;
    }

    private int getPingMs() {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (minecraftClient == null || minecraftClient.player == null) {
            return 0;
        }
        ClientPlayNetworkHandler clientPlayNetworkHandler = minecraftClient.getNetworkHandler();
        if (clientPlayNetworkHandler == null) {
            return 0;
        }
        PlayerListEntry playerListEntry = clientPlayNetworkHandler.getPlayerListEntry(minecraftClient.player.getUuid());
        if (playerListEntry == null) {
            return 0;
        }
        return playerListEntry.getLatency();
    }

    private float measureContentWidth() {
        FontRenderer v6hnga2 = ClientFonts.MEDIUM[14];
        float f = v6hnga2.a(String.valueOf(this.getFps()));
        float f2 = v6hnga2.a("fps");
        float f3 = v6hnga2.a(String.valueOf(this.getPingMs()));
        float f4 = v6hnga2.a("ms");
        float f5 = v6hnga2.a("t.me/ ");
        float f6 = v6hnga2.a("nativevm");
        float f7 = v6hnga2.a("");
        float f8 = 19.0f + f + 3.0f + f2 + 8.0f + 1.0f + 8.0f + 10.0f + f3 + 3.0f + f4 + 8.0f + 1.0f + 8.0f + f5 + f6 + f7 + 6.0f;
        return 38.0f + f8 - 12.0f;
    }

    public tfzl8f() {
        this.g = 9.0f;
        this.h = 0.0f;
    }

    static {
        TG_PREFIX = "t.me/ ";
        TG_HARON = "nativevm";
        TG_VISUALS = "";
        TEXT_WHITE = new Color(245, 240, 235, 255);
        TEXT_DIM = new Color(180, 150, 130, 220);
        AVATAR_COLOR = new Color(255, 255, 255, 255);
    }

    @Override
    public float b() {
        return 32.0f;
    }

    @Override
    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, float f4, float f5) {
        if (f5 < 0.01f) {
            return;
        }
        FontRenderer v6hnga2 = ClientFonts.MEDIUM[14];
        float f6 = f2 + 16.0f;
        float f7 = f6 - 4.55f;
        float f8 = f6 - 3.6f;
        float f9 = f + 9.0f;
        float f10 = f6 - 11.0f;
        Color color = this.a(new Color(18, 12, 9, 190), f5);
        s7swsm2.a(f9, f10, 22.0f, 22.0f, 7.0f, color, color, color, color, matrixStack);
        float f11 = f + 10.0f;
        float f12 = f6 - 10.0f;
        HudIconRenderer.drawWatermarkIcon(s7swsm2, matrixStack, f11, f12, 20.0f, this.a(ClientColor.currentColor() != null ? ClientColor.currentColor() : AVATAR_COLOR, f5));
        float f13 = f + 47.0f;
        this.drawFpsIcon(s7swsm2, matrixStack, f13, f6, this.a(pryrvd.ACCENT, f5));
        f13 += 10.0f;
        f13 = this.drawPart(v6hnga2, String.valueOf(this.getFps()), f13, f7, TEXT_WHITE, f5, matrixStack);
        f13 += v6hnga2.a(String.valueOf(this.getFps())) + 3.0f;
        f13 = this.drawPart(v6hnga2, "fps", f13, f8, TEXT_DIM, f5, matrixStack);
        this.drawSeparator(s7swsm2, matrixStack, f13 += v6hnga2.a("fps") + 8.0f, f6, this.a(pryrvd.a(pryrvd.ACCENT, 70), f5));
        this.drawPingIcon(s7swsm2, matrixStack, f13 += 9.0f, f6, this.a(pryrvd.ACCENT, f5));
        f13 += 10.0f;
        f13 = this.drawPart(v6hnga2, String.valueOf(this.getPingMs()), f13, f7, TEXT_WHITE, f5, matrixStack);
        f13 += v6hnga2.a(String.valueOf(this.getPingMs())) + 3.0f;
        f13 = this.drawPart(v6hnga2, "ms", f13, f8, TEXT_DIM, f5, matrixStack);
        this.drawSeparator(s7swsm2, matrixStack, f13 += v6hnga2.a("ms") + 8.0f, f6, this.a(pryrvd.a(pryrvd.ACCENT, 70), f5));
        f13 += 9.0f;
        f13 = this.drawPart(v6hnga2, "t.me/ ", f13, f7, TEXT_DIM, f5, matrixStack);
        f13 += v6hnga2.a("t.me/ ");
        f13 = this.drawPart(v6hnga2, "nativevm", f13, f7, pryrvd.ACCENT, f5, matrixStack);
        this.drawPart(v6hnga2, "", f13 += v6hnga2.a("nativevm"), f7, TEXT_WHITE, f5, matrixStack);
    }

    @Override
    public float a() {
        return this.measureContentWidth();
    }
}

