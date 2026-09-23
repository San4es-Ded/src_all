package haron.modules.utilities;

import haron.events.PlayerDeathEvent;
import haron.events.ClientTickEvent;
import haron.media.chat.w53bpe;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.settings.TextTokenType;
import haron.settings.ValidatedTextSetting;
import haron.settings.BooleanSetting;
import java.util.Objects;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;

@ModuleInfo(a="Auto Respawn", b="Автоматически возрождает игрока после смерти", c=ModuleCategory.UTILITIES)
public class AutoRespawn
extends HaronModule {
    private final BooleanSetting e = new BooleanSetting("Авто возрождение", "Автоматически возрождаться после смерти", true);
    private final BooleanSetting f = new BooleanSetting("Отправлять команду после возрождения", "Отправлять команду после возрождения", false);
    private final ValidatedTextSetting g;
    private boolean h;
    private boolean i;
    private int j;
    public static int a;
    public static boolean b;

    public AutoRespawn() {
        ValidatedTextSetting trepl22 = new ValidatedTextSetting("Команда", TextTokenType.COMMAND, "/home");
        BooleanSetting xcv91t2 = this.f;
        Objects.requireNonNull(xcv91t2);
        this.g = trepl22.a(xcv91t2::k);
        this.h = false;
    }

    @Override
    public void f() {
        super.f();
        this.h = false;
    }

    @EventHandler
    public void a(ClientTickEvent q8krcw2) {
        Screen screen;
        if (AutoRespawn.c.player == null || AutoRespawn.c.world == null) {
            return;
        }
        if (AutoRespawn.c.currentScreen instanceof DeathScreen) {
            int n = this.j;
            this.j = 2 * (n | 1) - (n ^ 1);
        }
        if (this.i && !(AutoRespawn.c.currentScreen instanceof DeathScreen) && AutoRespawn.c.player.age > 30) {
            w53bpe.a(this.g.a());
            this.i = false;
            this.j = 0;
        }
        if ((screen = AutoRespawn.c.currentScreen) instanceof DeathScreen) {
            DeathScreen deathScreen = (DeathScreen)screen;
            if (!this.h) {
                this.h = true;
            }
            if (((Boolean)this.e.k()).booleanValue() && this.a(deathScreen)) {
                AutoRespawn.c.player.requestRespawn();
                AutoRespawn.c.currentScreen = null;
                this.j = 0;
                return;
            }
            return;
        }
        if (!this.h) {
            return;
        }
        if (AutoRespawn.c.player.isAlive()) {
            this.h = false;
            if (!((Boolean)this.f.k()).booleanValue() || this.g.a().isEmpty()) {
                return;
            }
            w53bpe.a(this.g.a());
        }
    }

    @EventHandler
    public void a(PlayerDeathEvent c5scvm2) {
        if (c5scvm2.a() == AutoRespawn.c.player) {
            this.i = true;
        }
    }

    private boolean a(DeathScreen deathScreen) {
        try {
            for (Object e : deathScreen.children()) {
                if (!(e instanceof ButtonWidget) || !((ButtonWidget)e).active) continue;
                return true;
            }
            return false;
        }
        catch (Exception exception) {
            return false;
        }
    }
}

