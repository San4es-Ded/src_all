package haron.modules.utilities;

import haron.events.MouseScrollEvent;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.settings.NumberSetting;
import haron.settings.KeybindSetting;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

@ModuleInfo(a="Zoom", b="Плавный зум с регулировкой колесиком мыши", c=ModuleCategory.UTILITIES)
public class Zoom
extends HaronModule {
    private final KeybindSetting g = new KeybindSetting("Кнопка зума", 67);
    private final NumberSetting h = new NumberSetting("Стартовый FOV", 70.0f, 70.0f, 120.0f, 1.0f);
    private double originalFov = 70.0;
    private double currentFov = 70.0;
    private double targetFov = 70.0;
    private boolean isHolding = false;
    private int originalSlot = 0;
    public static Zoom INSTANCE;
    public static volatile double b;
    public static volatile boolean a;

    private boolean isPressed() {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (minecraftClient.getWindow() == null) {
            return false;
        }
        int n = this.g.a();
        if (n < 0) {
            return false;
        }
        long l = minecraftClient.getWindow().getHandle();
        return n < 8 ? GLFW.glfwGetMouseButton((long)l, (int)n) == 1 : GLFW.glfwGetKey((long)l, (int)n) == 1;
    }

    public static void tickZoom() {
        if (!INSTANCE.k()) {
            if (a) {
                INSTANCE.p();
            }
            return;
        }
        INSTANCE.updateLogic();
    }

    private void updateLogic() {
        double d;
        boolean bl;
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (minecraftClient.options == null) {
            return;
        }
        boolean bl2 = bl = this.isPressed() && minecraftClient.currentScreen == null;
        if (bl) {
            if (!this.isHolding) {
                this.originalSlot = minecraftClient.player != null ? minecraftClient.player.getInventory().selectedSlot : 0;
                this.isHolding = true;
                this.targetFov = Math.min(this.originalFov, (double)this.h.a());
            }
        } else if (this.isHolding) {
            this.isHolding = false;
            this.targetFov = this.originalFov;
        }
        this.currentFov = Math.abs(d = this.targetFov - this.currentFov) < 0.01 ? this.targetFov : (this.currentFov += d * 0.2);
        if (!this.isHolding && Math.abs(this.currentFov - this.originalFov) < 0.1) {
            this.currentFov = this.originalFov;
        }
        a = this.isHolding || Math.abs(this.currentFov - this.originalFov) > 0.1;
        b = this.currentFov;
        if (this.isHolding && minecraftClient.player != null) {
            minecraftClient.player.getInventory().selectedSlot = this.originalSlot;
        }
    }

    public Zoom() {
        INSTANCE = this;
    }

    static {
        b = 70.0;
        a = false;
    }

    @Override
    public void e() {
        super.e();
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (minecraftClient.options != null) {
            this.currentFov = this.originalFov = ((Integer)minecraftClient.options.getFov().getValue()).doubleValue();
            this.targetFov = this.originalFov;
        }
    }

    @Override
    public void f() {
        this.p();
        super.f();
    }

    @EventHandler
    public void a(MouseScrollEvent jz15mf2) {
        if (this.k() && this.isHolding && MinecraftClient.getInstance().currentScreen == null) {
            jz15mf2.a(true);
            double d = -jz15mf2.e() * 2.0;
            this.targetFov = Math.max(70.0, Math.min(this.originalFov, this.targetFov + d));
        }
    }

    private void p() {
        this.isHolding = false;
        a = false;
        this.currentFov = this.originalFov;
        b = this.originalFov;
    }
}

