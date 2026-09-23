package haron.effects;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.registry.entry.RegistryEntry;

public class TimedEffectEntry {
    private final String a;
    private final Item b;
    private final RegistryEntry<StatusEffect> c;
    private final boolean d;
    private final int e;
    private final String f;
    private final String g;
    private final boolean h;

    public TimedEffectEntry(String string, Item item, int n, boolean bl) {
        this.a = string;
        this.b = item;
        this.c = null;
        this.d = false;
        this.e = n;
        this.h = bl;
        this.f = TimedEffectEntry.a(n / 20);
        this.g = TimedEffectEntry.$sf$0(string, this.f);
    }

    public TimedEffectEntry(String string, RegistryEntry<StatusEffect> registryEntry, int n, boolean bl) {
        this.a = string;
        this.b = null;
        this.c = registryEntry;
        this.d = true;
        this.e = n;
        this.h = bl;
        this.f = TimedEffectEntry.a(n / 20);
        this.g = TimedEffectEntry.$sf$0(string, this.f);
    }

    public int e() {
        return this.e;
    }

    public Item b() {
        return this.b;
    }

    public RegistryEntry<StatusEffect> c() {
        return this.c;
    }

    public boolean h() {
        return this.h;
    }

    public String f() {
        return this.f;
    }

    public boolean d() {
        return this.d;
    }

    public String a() {
        return this.a;
    }

    private static String a(int totalSeconds) {
        int minutes = Math.max(0, totalSeconds) / 60;
        int seconds = Math.max(0, totalSeconds) % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    public String g() {
        return this.g;
    }

    private static /* synthetic */ String $sf$0(String string, String string2) {
        return string + " закончится через: " + string2;
    }
}
