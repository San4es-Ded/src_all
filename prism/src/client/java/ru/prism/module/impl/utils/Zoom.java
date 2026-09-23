package ru.prism.module.impl.utils;

import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import ru.prism.manager.event_impl.EventRender3D;
import ru.prism.manager.event_impl.MouseScrollEvent;
import ru.prism.manager.events.orbit.EventHandler;
import ru.prism.module.api.Category;
import ru.prism.module.api.Module;
import ru.prism.module.api.ModuleInfo;
import ru.prism.module.api.settings.impl.BindSetting;
import ru.prism.module.api.settings.impl.SliderSetting;
import ru.prism.utils.other.Instance;

@ModuleInfo(
        name = "Zoom",
        desc = "Плавно приближает мир, пока держишь клавишу, и так же мягко возвращает обратно.",
        category = Category.UTILITIES
)
public class Zoom extends Module {

    public static Zoom get() {
        return Instance.get(Zoom.class);
    }

    public final BindSetting key = new BindSetting(this, "Клавиша", GLFW.GLFW_KEY_C, false);
    public final SliderSetting startFov = new SliderSetting(this, "Стартовый FOV", 50.0F, 2.0F, 120.0F, 1.0F);

    private static volatile double zoomFov = 70.0;
    private static volatile boolean zoomActive;

    private double originalFov = 70.0;
    private double targetFov = 70.0;
    private double currentFov = 70.0;
    private double originalSensitivity = 0.5;
    private int lockedSlot = -1;
    private boolean zooming;
    private boolean smoothActive;
    private boolean previousKeyPressed;

    public static float applyFov(float fov) {
        return zoomActive ? (float) zoomFov : fov;
    }

    @EventHandler
    public void onRender(EventRender3D event) {
        if (mc.player == null || mc.world == null || mc.options == null || mc.getWindow() == null) {
            reset();
            return;
        }

        boolean pressed = key.get() > -1 && InputUtil.isKeyPressed(mc.getWindow(), key.get());
        if (pressed && !previousKeyPressed) {
            startZoom();
        } else if (!pressed && previousKeyPressed) {
            stopZoom();
        }
        previousKeyPressed = pressed;

        if (zooming && mc.currentScreen != null) {
            stopZoom();
        }

        if (!zooming && smoothActive && Math.abs(currentFov - targetFov) <= 0.2) {
            double fov = mc.options.getFov().getValue();
            originalFov = fov;
            targetFov = fov;
            currentFov = fov;
            smoothActive = false;
        }

        if (Math.abs(currentFov - targetFov) <= 0.1) {
            currentFov = targetFov;
        } else {
            currentFov += (targetFov - currentFov) * (24.0 / Math.max(1, mc.getCurrentFps()));
            if (Math.abs(currentFov - targetFov) <= 0.2) {
                currentFov = targetFov;
            }
        }

        zoomFov = currentFov;
        zoomActive = smoothActive;

        if (zooming) {
            if (lockedSlot >= 0 && mc.player != null) {
                mc.player.getInventory().setSelectedSlot(lockedSlot);
            }
            double factor = originalFov > 2.0 ? targetFov / originalFov : 1.0;
            mc.options.getMouseSensitivity().setValue(Math.min(originalSensitivity, Math.max(originalSensitivity * factor, 0.0)));
        }
    }

    @EventHandler
    public void onScroll(MouseScrollEvent event) {
        if (!isEnabled() || !zooming || mc.currentScreen != null) return;

        event.cancel();
        double delta = -event.getVerticalAmount() * 20.0;
        if (originalFov > 2.0) {
            delta *= (targetFov - 2.0) / (originalFov - 2.0);
        }
        targetFov = Math.max(2.0, Math.min(originalFov, targetFov + delta));
    }

    private void startZoom() {
        if (!smoothActive) {
            originalFov = mc.options.getFov().getValue();
            originalSensitivity = mc.options.getMouseSensitivity().getValue();
            smoothActive = true;
        }
        targetFov = Math.min(originalFov, Math.max(2.0, startFov.getValue()));
        currentFov = mc.options.getFov().getValue();
        if (mc.player != null) {
            lockedSlot = mc.player.getInventory().getSelectedSlot();
        }
        zooming = true;
    }

    private void stopZoom() {
        zooming = false;
        targetFov = originalFov;
        lockedSlot = -1;
        if (mc.options != null) {
            mc.options.getMouseSensitivity().setValue(originalSensitivity);
        }
    }

    private void reset() {
        if (mc.options != null) {
            originalFov = mc.options.getFov().getValue();
            originalSensitivity = mc.options.getMouseSensitivity().getValue();
            mc.options.getMouseSensitivity().setValue(originalSensitivity);
        }
        zooming = false;
        smoothActive = false;
        previousKeyPressed = false;
        lockedSlot = -1;
        targetFov = originalFov;
        currentFov = originalFov;
        zoomFov = currentFov;
        zoomActive = false;
    }

    @Override
    protected void onEnable() {
        reset();
        super.onEnable();
    }

    @Override
    protected void onDisable() {
        reset();
        super.onDisable();
    }
}
