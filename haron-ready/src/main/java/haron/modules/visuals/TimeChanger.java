package haron.modules.visuals;

import haron.animation.Easings;
import haron.animation.AnimatedValue;
import haron.events.PacketDirection;
import haron.events.PacketEvent;
import haron.events.ClientTickEvent;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.settings.ModeSetting;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;

@ModuleInfo(a="Time Changer", b="Изменяет время суток в мире", c=ModuleCategory.VISUALS)
public class TimeChanger
extends HaronModule {
    private final ModeSetting e = new ModeSetting("Время суток", new String[]{"День", "Закат", "Ночь", "Полночь", "Рассвет"}, "День");
    private final AnimatedValue f = new AnimatedValue();
    public static int a;
    public static boolean b;

    public long n() {
        return (long)this.f.j();
    }

    @Override
    public void f() {
        super.f();
    }

    @EventHandler
    public void a(ClientTickEvent q8krcw2) {
        if (TimeChanger.c.world != null) {
            this.f.a();
            this.f.a(this.o(), 2.0, Easings.C, false);
        }
    }

    @EventHandler
    public void a(PacketEvent g07m232) {
        if (g07m232.e() == PacketDirection.RECIEVE) {
            if (g07m232.d() instanceof WorldTimeUpdateS2CPacket) {
                g07m232.b();
            } else if (b) {
                // empty if block
            }
        }
    }

    private long o() {
        String string = this.e.d();
        if ("День".equals(string)) {
            return 1000L;
        }
        if ("Закат".equals(string)) {
            return 12500L;
        }
        if ("Ночь".equals(string)) {
            return 18000L;
        }
        if ("Полночь".equals(string)) {
            return 22000L;
        }
        if ("Рассвет".equals(string)) {
            return 23000L;
        }
        return 1000L;
    }
}

