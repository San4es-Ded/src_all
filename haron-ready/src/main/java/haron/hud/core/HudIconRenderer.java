package haron.hud.core;

import haron.render.icons.HaronIcons;
import haron.render.ShapeRenderer;
import java.awt.Color;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;
import ru.haron.Haron;

public final class HudIconRenderer {
    private static boolean loggedWatermarkIcon = false;

    public static void drawHaronMark(ShapeRenderer s7swsm2, MatrixStack matrixStack, float f, float f2, float f3, Color color) {
        Identifier identifier = HaronIcons.get("pulse_ico");
        if (identifier == null) {
            identifier = HaronIcons.get("logo");
        }
        if (identifier == null || color.getAlpha() < 1) {
            return;
        }
        s7swsm2.a(identifier, f, f2, f3, f3, 0.0f, 1.0f, 1.0f, -1.0f, -1.0f, color, matrixStack);
    }

    private static void logWatermarkIcon(Identifier identifier, float f, Color color) {
        if (loggedWatermarkIcon) {
            return;
        }
        loggedWatermarkIcon = true;
        Haron.getLOGGER().info("[PulseIcons] drawWatermarkIcon -> identifier={} size={} tint=rgba({},{},{},{})", (Object)identifier, (Object)Float.valueOf(f), (Object)color.getRed(), (Object)color.getGreen(), (Object)color.getBlue(), (Object)color.getAlpha());
    }

    private static void tryEnableStencil() {
        try {
            Framebuffer framebuffer = MinecraftClient.getInstance().getFramebuffer();
            try {
                framebuffer.getClass().getMethod("enableStencil", new Class[0]).invoke((Object)framebuffer, new Object[0]);
                return;
            }
            catch (NoSuchMethodException noSuchMethodException) {
                try {
                    framebuffer.getClass().getMethod("enableStencilTest", new Class[0]).invoke((Object)framebuffer, new Object[0]);
                }
                catch (NoSuchMethodException noSuchMethodException2) {}
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public static void drawRotated180(ShapeRenderer s7swsm2, MatrixStack matrixStack, Identifier identifier, float f, float f2, float f3, Color color) {
        if (s7swsm2 == null || identifier == null || color.getAlpha() < 1) {
            return;
        }
        float f4 = f + f3 / 2.0f;
        float f5 = f2 + f3 / 2.0f;
        matrixStack.push();
        matrixStack.translate(f4, f5, 0.0f);
        matrixStack.scale(-1.0f, -1.0f, 1.0f);
        matrixStack.translate(-f4, -f5, 0.0f);
        s7swsm2.a(identifier, f, f2, f3, f3, color, matrixStack);
        matrixStack.pop();
    }

    public static void drawWatermarkIcon(ShapeRenderer s7swsm2, MatrixStack matrixStack, float f, float f2, float f3, Color color) {
        Identifier identifier = HaronIcons.get("logo");
        if (s7swsm2 == null || identifier == null || color.getAlpha() < 1) {
            return;
        }
        HudIconRenderer.logWatermarkIcon(identifier, f3, color);
        s7swsm2.a(identifier, f, f2, f3, f3, 0.0f, 1.0f, 1.0f, -1.0f, -1.0f, color, matrixStack);
    }

    public static Identifier clickGui(String string) {
        Identifier identifier = HaronIcons.get(string);
        if (identifier != null) {
            return identifier;
        }
        return Identifier.of((String)"haron", (String)HudIconRenderer.$sf$0(string));
    }

    public static void draw(ShapeRenderer s7swsm2, MatrixStack matrixStack, Identifier identifier, float f, float f2, float f3, Color color) {
        if (s7swsm2 == null || identifier == null || color.getAlpha() < 1) {
            return;
        }
        s7swsm2.a(identifier, f, f2, f3, f3, color, matrixStack);
    }

    public static void drawFlippedVertical(ShapeRenderer s7swsm2, MatrixStack matrixStack, Identifier identifier, float f, float f2, float f3, Color color) {
        if (s7swsm2 == null || identifier == null || color.getAlpha() < 1) {
            return;
        }
        float f4 = f2 + f3 / 2.0f;
        matrixStack.push();
        matrixStack.translate(0.0f, f4, 0.0f);
        matrixStack.scale(1.0f, -1.0f, 1.0f);
        matrixStack.translate(0.0f, -f4, 0.0f);
        s7swsm2.a(identifier, f, f2, f3, f3, color, matrixStack);
        matrixStack.pop();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void drawWatermarkIconRounded(ShapeRenderer s7swsm2, MatrixStack matrixStack, float f, float f2, float f3, float f4, Color color) {
        Identifier identifier = HaronIcons.get("logo");
        if (s7swsm2 == null || identifier == null || color.getAlpha() < 1) {
            return;
        }
        HudIconRenderer.logWatermarkIcon(identifier, f3, color);
        try {
            HudIconRenderer.tryEnableStencil();
            GL11.glEnable((int)2960);
            GL11.glStencilMask((int)255);
            GL11.glClearStencil((int)0);
            GL11.glClear((int)1024);
            GL11.glStencilFunc((int)519, (int)1, (int)255);
            GL11.glStencilOp((int)7681, (int)7681, (int)7681);
            GL11.glColorMask((boolean)false, (boolean)false, (boolean)false, (boolean)false);
            Color color2 = new Color(255, 255, 255, 255);
            s7swsm2.a(f, f2, f3, f3, f4, color2, color2, color2, color2, matrixStack);
            GL11.glColorMask((boolean)true, (boolean)true, (boolean)true, (boolean)true);
            GL11.glStencilMask((int)0);
            GL11.glStencilFunc((int)514, (int)1, (int)255);
            GL11.glStencilOp((int)7680, (int)7680, (int)7680);
            s7swsm2.a(identifier, f, f2, f3, f3, 0.0f, 1.0f, 1.0f, -1.0f, -1.0f, color, matrixStack);
        }
        finally {
            GL11.glStencilMask((int)255);
            GL11.glDisable((int)2960);
            GL11.glColorMask((boolean)true, (boolean)true, (boolean)true, (boolean)true);
        }
    }

    private HudIconRenderer() {
    }

    private static /* synthetic */ String $sf$0(String string) {
        return "textures/clickgui/" + string;
    }
}

