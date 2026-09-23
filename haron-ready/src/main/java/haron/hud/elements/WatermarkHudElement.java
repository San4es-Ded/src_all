package haron.hud.elements;

import haron.hud.core.HudElement;
import haron.module.ModuleManager;
import haron.modules.hud.ClientColor;
import haron.modules.hud.Watermark;
import haron.render.font.FontRenderer;
import haron.render.font.ClientFonts;
import haron.render.icons.HaronIcons;
import haron.render.ShapeRenderer;
import haron.theme.pryrvd;
import java.awt.Color;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

public class WatermarkHudElement
extends HudElement {
    private static final float PAD_X = 2.5f;
    private static final float PAD_Y = 6.0f;
    private static final float ICON_BASE = 12.0f;
    private static final float ICON_GAP = 2.5f;
    private static final float RADIUS = 6.5f;
    private boolean settingsBound;

    private void bindSettings() {
        if (this.settingsBound) {
            return;
        }
        this.f().a(ModuleManager.WATERMARK);
        this.settingsBound = true;
    }

    private static Color withAlpha(Color color, float f) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.max(0, Math.min(255, Math.round((float)color.getAlpha() * f))));
    }

    private String buildText() {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        Watermark p1d01p2 = ModuleManager.WATERMARK;
        StringBuilder stringBuilder = new StringBuilder("@nativevm");
        if (p1d01p2.k()) {
            if (p1d01p2.showFps.get()) {
                stringBuilder.append(" / ").append(Math.round(minecraftClient.getCurrentFps())).append(" fps");
            }
            if (p1d01p2.showPing.get() && minecraftClient.getNetworkHandler() != null) {
                int n = minecraftClient.getNetworkHandler().getPlayerListEntry(minecraftClient.player.getUuid()).getLatency();
                stringBuilder.append(" / ").append(n).append(" ms");
            }
        }
        return stringBuilder.toString();
    }

    public WatermarkHudElement(float f, float f2) {
        super(f, f2);
    }

    @Override
    protected void a() {
        this.bindSettings();
        float f = this.g();
        Watermark p1d01p2 = ModuleManager.WATERMARK;
        float f2 = p1d01p2.k() ? p1d01p2.scale.get() : 1.0f;
        float f3 = f * f2;
        FontRenderer v6hnga2 = ClientFonts.b[Math.max(6, Math.min(16, Math.round(14.0f * f3)))];
        float f4 = 12.0f * f3;
        String string = this.buildText();
        float f5 = v6hnga2.a(string);
        float f6 = 2.5f * f3 + f4 + 2.5f * f3 + f5 + 2.5f * f3;
        float f7 = Math.max(f4, v6hnga2.b(string)) + 6.0f * f3 * 2.0f;
        this.d = f6;
        this.e = f7;
    }

    @Override
    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2) {
        if (this.a.player == null) {
            return;
        }
        this.a();
        float f3 = this.g();
        Watermark p1d01p2 = ModuleManager.WATERMARK;
        float f4 = p1d01p2.k() ? p1d01p2.scale.get() : 1.0f;
        float f5 = f3 * f4;
        float f6 = this.b;
        float f7 = this.c;
        float f8 = this.d;
        float f9 = this.e;
        float f10 = 6.5f * f5;
        float f11 = 2.5f * f5;
        float f12 = 12.0f * f5;
        float f13 = 2.5f * f5;
        Color color = pryrvd.a(pryrvd.PANEL_BG_TOP, 232);
        Color color2 = pryrvd.a(pryrvd.PANEL_BG_BOT, 236);
        float f14 = 0.8f * f5;
        Color color3 = WatermarkHudElement.withAlpha(Color.WHITE, 0.08f);
        s7swsm2.a(f6 - f14, f7 - f14, f8 + f14 * 2.0f, f9 + f14 * 2.0f, f10 + f14, color3, color3, color3, color3, matrixStack);
        s7swsm2.a(f6, f7, f8, f9, f10, WatermarkHudElement.withAlpha(color, 1.0f), WatermarkHudElement.withAlpha(color, 1.0f), WatermarkHudElement.withAlpha(color2, 1.0f), WatermarkHudElement.withAlpha(color2, 1.0f), matrixStack);
        float f15 = f6 + f11;
        float f16 = f7 + (f9 - f12) / 2.0f;
        float f17 = 1.5f * f5;
        float f18 = f12 * 0.25f + f17;
        Color color4 = WatermarkHudElement.withAlpha(Color.BLACK, 0.35f);
        s7swsm2.a(f15 - f17, f16 - f17, f12 + f17 * 2.0f, f12 + f17 * 2.0f, f18, color4, color4, color4, color4, matrixStack);
        Color color5 = ClientColor.currentColor();
        s7swsm2.a(HaronIcons.get("logo"), f15, f16, f12, f12, f12 * 0.25f, 1.0f, 1.0f, -1.0f, -1.0f, WatermarkHudElement.withAlpha(color5, 1.0f), matrixStack);
        String string = this.buildText();
        FontRenderer v6hnga2 = ClientFonts.b[Math.max(6, Math.min(16, Math.round(14.0f * f5)))];
        float f19 = f15 + f12 + f13;
        float f20 = f7 + (f9 - v6hnga2.b(string)) / 2.0f;
        v6hnga2.a(string, f19, (double)f20, WatermarkHudElement.withAlpha(Color.WHITE, 1.0f), matrixStack);
    }
}

