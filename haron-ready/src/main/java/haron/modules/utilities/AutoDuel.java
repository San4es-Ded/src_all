package haron.modules.utilities;

import haron.events.PacketEvent;
import haron.events.WorldChangedEvent;
import haron.events.ClientTickEvent;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.settings.TextTokenType;
import haron.settings.ModeSetting;
import haron.settings.ValidatedTextSetting;
import haron.settings.BooleanSetting;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import meteordevelopment.orbit.EventHandler;

@ModuleInfo(a="Auto Duel", b="Автоматически отправляет запросы на дуэли игрокам", c=ModuleCategory.UTILITIES)
public class AutoDuel
extends HaronModule {
    private final ModeSetting a = new ModeSetting("Набор", new String[]{"Щит", "Шипы 3", "Лук", "Тотемы", "Нодебафф", "Шары", "Классик", "Читерский рай", "Незеритка"}, "Шары");
    private final BooleanSetting b = new BooleanSetting("Ставить деньги", false);
    private final ValidatedTextSetting e;
    private static final Pattern f = Pattern.compile("^\\w{3,16}$");
    private final List<String> g;
    private long h;
    private long i;
    private String j;
    private boolean k;

    public AutoDuel() {
        ValidatedTextSetting trepl22 = new ValidatedTextSetting("Сумма ставки", TextTokenType.PRICE, "2000", "Введите сумму");
        BooleanSetting xcv91t2 = this.b;
        Objects.requireNonNull(xcv91t2);
        this.e = trepl22.a(xcv91t2::k);
        this.g = new ArrayList<String>();
        this.h = 0L;
        this.i = 0L;
        this.j = "";
        this.k = false;
    }

    private List<String> n() {
        return Collections.emptyList();
    }

    @Override
    public void f() {
    }

    @EventHandler
    public void a(ClientTickEvent q8krcw2) {
    }

    @EventHandler
    public void a(PacketEvent g07m232) {
    }

    @EventHandler
    public void a(WorldChangedEvent m7z9q12) {
    }
}

