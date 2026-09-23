package haron.modules.utilities;

import haron.events.ClientTickEvent;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.settings.NumberSetting;
import haron.settings.ModeSetting;
import meteordevelopment.orbit.EventHandler;
import ru.haron.mixin.accessor.MinecraftClientAccessor;

@ModuleInfo(a="Tape Mouse", b="Автокликер", c=ModuleCategory.UTILITIES)
public class TapeMouse
extends HaronModule {
    private final NumberSetting e = new NumberSetting("Задержка (тики)", 10.0f, 1.0f, 100.0f, 1.0f);
    private final ModeSetting f = new ModeSetting("Кнопка мыши", new String[]{"Левая", "Правая"}, "Левая");
    private int g = 0;
    public static int a;
    public static boolean b;

    @Override
    public void e() {
        this.g = 0;
        super.e();
    }

    @Override
    public void f() {
        this.g = 0;
        super.f();
    }

    @EventHandler
    public void a(ClientTickEvent q8krcw2) {
        if (TapeMouse.c.player == null || TapeMouse.c.world == null || TapeMouse.c.player.isUsingItem()) {
            return;
        }
        if (this.g > 0) {
            int n = this.g;
            this.g = (n & 0xFFFFFFFE) - (~n & 1);
        } else {
            if (this.f.b("Левая")) {
                ((MinecraftClientAccessor)c).invokeDoAttack();
            } else {
                ((MinecraftClientAccessor)c).invokeDoItemUse();
            }
            this.g = this.e.b();
        }
    }
}

