package ru.prism.module.impl.utils;

import net.minecraft.client.option.Perspective;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import ru.prism.manager.event_impl.EventTick;
import ru.prism.manager.events.orbit.EventHandler;
import ru.prism.module.api.Category;
import ru.prism.module.api.Module;
import ru.prism.module.api.ModuleInfo;
import ru.prism.module.api.settings.impl.BindSetting;
import ru.prism.module.api.settings.impl.ModeSetting;
import ru.prism.utils.other.Instance;

@ModuleInfo(
        name = "Free Look",
        desc = "Отцепляет камеру от тела — осматривайся, пока персонаж стоит как вкопанный.",
        category = Category.UTILITIES
)
public class FreeLook extends Module {

    public static FreeLook get() {
        return Instance.get(FreeLook.class);
    }

    public static boolean active;
    public static float cameraYaw;
    public static float cameraPitch;

    public final ModeSetting mode = new ModeSetting(this, "Режим", "Удержание", "Переключение");
    public final BindSetting key = new BindSetting(this, "Клавиша", GLFW.GLFW_KEY_LEFT_ALT, false);

    private Perspective previousPerspective = Perspective.FIRST_PERSON;
    private boolean activatedByThisModule = false;
    private boolean previousKeyPressed = false;

    @EventHandler
    public void onTick(EventTick event) {
        if (mc.player != null && mc.world != null && mc.getWindow() != null && mc.currentScreen == null) {
            int keyCode = key.get();
            if (keyCode <= -1) {
                resetIfNeeded();
            } else {
                boolean pressed = InputUtil.isKeyPressed(mc.getWindow(), keyCode);
                if (mode.is("Переключение")) {
                    if (pressed && !previousKeyPressed) {
                        if (active) {
                            disableFreeLook();
                            activatedByThisModule = false;
                        } else {
                            enableFreeLook();
                            activatedByThisModule = true;
                        }
                    }
                } else if (pressed && !active) {
                    enableFreeLook();
                    activatedByThisModule = true;
                } else if (!pressed && active && activatedByThisModule) {
                    disableFreeLook();
                    activatedByThisModule = false;
                }
                previousKeyPressed = pressed;
            }
        } else {
            resetIfNeeded();
        }
    }

    private void enableFreeLook() {
        if (mc.player != null && mc.options != null) {
            previousPerspective = mc.options.getPerspective();
            cameraYaw = mc.player.getYaw(1.0F);
            cameraPitch = mc.player.getPitch(1.0F);
            mc.options.setPerspective(Perspective.THIRD_PERSON_BACK);
            active = true;
        }
    }

    private void disableFreeLook() {
        if (mc.options != null) {
            mc.options.setPerspective(previousPerspective);
        }
        active = false;
    }

    private void resetIfNeeded() {
        if (active && activatedByThisModule) {
            disableFreeLook();
        }
        activatedByThisModule = false;
        previousKeyPressed = false;
    }

    @Override
    public void onDisable() {
        if (active) {
            disableFreeLook();
        }
        activatedByThisModule = false;
        previousKeyPressed = false;
        super.onDisable();
    }
}
