package haron.modules.utilities;

import haron.events.ClientTickEvent;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.settings.NumberSetting;
import haron.settings.KeybindSetting;
import haron.settings.BooleanSetting;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.option.Perspective;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;

@ModuleInfo(a="Free Look", b="Позволяет вращать камеру отдельно от игрока.", c=ModuleCategory.UTILITIES)
public class FreeLook
extends HaronModule {
    public static boolean active;
    public static float freeYaw;
    public static float freePitch;
    public final NumberSetting pDistanceTo = new NumberSetting("Дистанция до камеры", 7.5f, 1.0f, 25.0f, 0.5f);
    public final BooleanSetting free = new BooleanSetting("Свободная камера", false);
    public final KeybindSetting key = new KeybindSetting("Клавиша", 342);
    private float startYaw;
    private float startPitch;
    private Perspective previousPerspective = Perspective.FIRST_PERSON;
    private boolean activatedByThisModule = false;
    private boolean previousKeyPressed = false;

    @EventHandler
    public void onTick(ClientTickEvent q8krcw2) {
        boolean bl;
        if (FreeLook.c.player == null || c.getWindow() == null) {
            this.resetIfNeeded();
            return;
        }
        boolean bl2 = bl = GLFW.glfwGetKey((long)c.getWindow().getHandle(), (int)this.key.a()) == 1;
        if (((Boolean)this.free.k()).booleanValue()) {
            if (bl && !active) {
                this.enableFreeLook();
            } else if (!bl && active && this.activatedByThisModule) {
                this.disableFreeLook();
            }
            if (active) {
                FreeLook.c.options.setPerspective(Perspective.THIRD_PERSON_BACK);
                FreeLook.c.player.setYaw(this.startYaw);
                FreeLook.c.player.setPitch(this.startPitch);
                FreeLook.c.player.headYaw = this.startYaw;
                FreeLook.c.player.bodyYaw = this.startYaw;
            }
        } else if (bl && !this.previousKeyPressed) {
            if (active) {
                this.disableFreeLook();
            } else {
                this.enableFreeLook();
            }
        }
        this.previousKeyPressed = bl;
    }

    private void resetIfNeeded() {
        if (active && this.activatedByThisModule) {
            this.disableFreeLook();
        }
        this.activatedByThisModule = false;
        this.previousKeyPressed = false;
    }

    public static void rotateTowards(double d, double d2) {
        freePitch = MathHelper.clamp((float)((float)((double)freePitch + d2 * 0.15)), (float)-90.0f, (float)90.0f);
        freeYaw = (float)((double)freeYaw + d * 0.15);
    }

    private void disableFreeLook() {
        if (FreeLook.c.options != null) {
            FreeLook.c.options.setPerspective(this.previousPerspective);
        }
        active = false;
        this.activatedByThisModule = false;
    }

    private void enableFreeLook() {
        if (FreeLook.c.player == null || FreeLook.c.options == null) {
            return;
        }
        this.previousPerspective = FreeLook.c.options.getPerspective();
        this.startYaw = FreeLook.c.player.getYaw();
        this.startPitch = FreeLook.c.player.getPitch();
        freeYaw = this.startYaw;
        freePitch = this.startPitch;
        FreeLook.c.options.setPerspective(Perspective.THIRD_PERSON_BACK);
        active = true;
        this.activatedByThisModule = true;
    }

    @Override
    public void e() {
        super.e();
        if (((Boolean)this.free.k()).booleanValue()) {
            if (FreeLook.c.player != null) {
                this.startYaw = FreeLook.c.player.getYaw();
                this.startPitch = FreeLook.c.player.getPitch();
            }
        }
    }

    @Override
    public void f() {
        if (this.activatedByThisModule) {
            this.disableFreeLook();
        }
        super.f();
    }
}

