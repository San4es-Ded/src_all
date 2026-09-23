package haron.gui.core;

import haron.core.BooleanCoercion;
import haron.gui.core.GuiLayerRegistry;
import haron.gui.core.GuiLayer;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

public class GuiInput {
    public static boolean contains(float x, float y, float width, float height, double pointX, double pointY) {
        return a(x, y, width, height, pointX, pointY);
    }
    private static float j;
    private static float k;
    private static float l;
    private static float m;
    public static int a;
    public static boolean b;
    private static long e;
    private static long f;
    private static final double[] c;
    private static final double[] d;
    private static boolean g;
    private static boolean h;
    private static boolean i;
    private static boolean textCursor;
    private static long textCursorHandle;

    public static void setTextCursor() {
        if (h) {
            return;
        }
        textCursor = true;
    }

    static {
        e = 0L;
        f = 0L;
        c = new double[1];
        d = new double[1];
        g = false;
        h = false;
        i = false;
        textCursor = false;
        textCursorHandle = 0L;
    }

    public static double e() {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (minecraftClient.getWindow() == null) {
            return 0.0;
        }
        GLFW.glfwGetCursorPos((long)minecraftClient.getWindow().getHandle(), (double[])c, (double[])d);
        return c[0] * (double)minecraftClient.getWindow().getScaledWidth() / (double)minecraftClient.getWindow().getWidth();
    }

    public static void i() {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (minecraftClient.getWindow() != null) {
            long l = minecraftClient.getWindow().getHandle();
            if (textCursor) {
                if (textCursorHandle == 0L) {
                    textCursorHandle = GLFW.glfwCreateStandardCursor((int)221186);
                }
                GLFW.glfwSetCursor((long)l, (long)textCursorHandle);
            } else if (g) {
                if (e == 0L) {
                    e = GLFW.glfwCreateStandardCursor((int)221188);
                }
                GLFW.glfwSetCursor((long)l, (long)e);
            } else {
                if (f == 0L) {
                    f = GLFW.glfwCreateStandardCursor((int)221185);
                }
                GLFW.glfwSetCursor((long)l, (long)f);
            }
        }
    }

    public static boolean b(GuiLayer v0ahvd2, double d, double d2) {
        return GuiLayerRegistry.a().d(v0ahvd2, d, d2);
    }

    public static void b() {
        h = true;
    }

    public static void c() {
        h = false;
    }

    public static void h() {
        g = false;
        textCursor = false;
    }

    public static double f() {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (minecraftClient.getWindow() == null) {
            return 0.0;
        }
        GLFW.glfwGetCursorPos((long)minecraftClient.getWindow().getHandle(), (double[])c, (double[])d);
        return d[0] * (double)minecraftClient.getWindow().getScaledHeight() / (double)minecraftClient.getWindow().getHeight();
    }

    public static boolean d() {
        return h;
    }

    public static boolean a(double d, double d2, double d3, double d4, double d5, double d6) {
        if (!(d5 >= d && d6 >= d2 && d5 <= d + d3 && d6 <= d2 + d4)) {
            return false;
        }
        if (i) {
            return BooleanCoercion.from(d5 < (double)j || d6 < (double)k || d5 > (double)(j + l) || d6 > (double)(k + m) ? 0 : 1);
        }
        return true;
    }

    public static void a(float f, float f2, float f3, float f4) {
        int n = 888;
        i = true;
        j = f;
        k = f2;
        l = f3;
        m = f4;
    }

    public static boolean a(float f, float f2, float f3, float f4, double d, double d2) {
        return GuiInput.a((double)f, (double)f2, (double)f3, (double)f4, d, d2);
    }

    public static boolean a(GuiLayer v0ahvd2, double d, double d2) {
        if (!(!i || d >= (double)j && d2 >= (double)k && d <= (double)(j + l) && d2 <= (double)(k + m))) {
            return false;
        }
        return GuiLayerRegistry.a().a(v0ahvd2, d, d2);
    }

    public static boolean a(GuiLayer v0ahvd2, float f, float f2, float f3, float f4, double d, double d2) {
        return GuiInput.a(f, f2, f3, f4, d, d2) ? GuiInput.a(v0ahvd2, d, d2) : false;
    }

    public static void a() {
        i = false;
    }

    public static boolean a(double d, double d2) {
        if (i) {
            return BooleanCoercion.from(d < (double)j || d2 < (double)k || d > (double)(j + l) || d2 > (double)(k + m) ? 0 : 1);
        }
        return true;
    }

    public static boolean k() {
        return i;
    }

    public static void g() {
        if (h) {
            return;
        }
        g = true;
        textCursor = false;
    }

    public static void j() {
        g = false;
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (minecraftClient.getWindow() != null) {
            if (f == 0L) {
                f = GLFW.glfwCreateStandardCursor((int)221185);
            }
            GLFW.glfwSetCursor((long)minecraftClient.getWindow().getHandle(), (long)f);
        }
    }
}
