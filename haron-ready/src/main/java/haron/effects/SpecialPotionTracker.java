package haron.effects;

import haron.core.BooleanCoercion;
import haron.effects.PotionEffectRequirement;
import haron.effects.SpecialPotionDefinition;
import haron.effects.TimedEffectEntry;
import haron.events.ClientTickEvent;
import haron.hud.core.HudServiceInfo;
import haron.hud.core.HudService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.entry.RegistryEntry;

@HudServiceInfo(enabledByDefault=true)
public class SpecialPotionTracker
extends HudService {
    private static final MinecraftClient a = MinecraftClient.getInstance();
    private static final long g = 1000L;
    private final List<SpecialPotionDefinition> b = Arrays.asList(new SpecialPotionDefinition("Зелье Радиации", Arrays.asList(new PotionEffectRequirement((RegistryEntry<StatusEffect>)StatusEffects.POISON, 1), new PotionEffectRequirement((RegistryEntry<StatusEffect>)StatusEffects.SLOWNESS, 2)), (RegistryEntry<StatusEffect>)StatusEffects.POISON, true), new SpecialPotionDefinition("Снотворное", Arrays.asList(new PotionEffectRequirement((RegistryEntry<StatusEffect>)StatusEffects.WEAKNESS, 1), new PotionEffectRequirement((RegistryEntry<StatusEffect>)StatusEffects.WITHER, 2)), (RegistryEntry<StatusEffect>)StatusEffects.WITHER, true), new SpecialPotionDefinition("Хлопушка", Arrays.asList(new PotionEffectRequirement((RegistryEntry<StatusEffect>)StatusEffects.SPEED, 4)), (RegistryEntry<StatusEffect>)StatusEffects.SPEED, false), new SpecialPotionDefinition("Святая вода", Arrays.asList(new PotionEffectRequirement((RegistryEntry<StatusEffect>)StatusEffects.REGENERATION, 2)), (RegistryEntry<StatusEffect>)StatusEffects.REGENERATION, false), new SpecialPotionDefinition("Зелье гнева", Arrays.asList(new PotionEffectRequirement((RegistryEntry<StatusEffect>)StatusEffects.STRENGTH, 4)), (RegistryEntry<StatusEffect>)StatusEffects.STRENGTH, false), new SpecialPotionDefinition("Зелье Палладина", Arrays.asList(new PotionEffectRequirement((RegistryEntry<StatusEffect>)StatusEffects.HEALTH_BOOST, 2)), (RegistryEntry<StatusEffect>)StatusEffects.HEALTH_BOOST, false), new SpecialPotionDefinition("Зелье Ассасина", Arrays.asList(new PotionEffectRequirement((RegistryEntry<StatusEffect>)StatusEffects.STRENGTH, 3)), (RegistryEntry<StatusEffect>)StatusEffects.STRENGTH, false));
    private List<TimedEffectEntry> c = new ArrayList<TimedEffectEntry>();
    private TimedEffectEntry d = null;
    private long e = 0L;
    private int f = 0;
    private boolean h = false;
    private boolean i = false;
    private boolean j = false;

    public boolean e() {
        if (!this.i) {
            return false;
        }
        this.i = false;
        return true;
    }

    public boolean i() {
        return this.h;
    }

    public TimedEffectEntry h() {
        return this.d;
    }

    public boolean f() {
        int n = 976;
        return BooleanCoercion.from(!this.c.isEmpty() ? 1 : 0);
    }

    public boolean d() {
        if (!this.h) {
            return false;
        }
        this.h = false;
        return true;
    }

    private boolean a(SpecialPotionDefinition i6a0kf2) {
        return false;
    }

    @EventHandler
    public void a(ClientTickEvent q8krcw2) {
        long l;
        int n;
        if (SpecialPotionTracker.a.player == null || SpecialPotionTracker.a.world == null) {
            if (this.j) {
                this.i = true;
                this.j = false;
            }
            this.c.clear();
            this.d = null;
            return;
        }
        List<TimedEffectEntry> list = this.k();
        int n2 = n = !list.isEmpty() ? 1 : 0;
        if (n != 0 && !this.j) {
            this.h = true;
        } else if (n == 0 && this.j) {
            this.i = true;
        }
        this.j = BooleanCoercion.from(n);
        this.c = list;
        if (this.c.isEmpty()) {
            this.d = null;
            return;
        }
        if (this.c.size() == 1) {
            this.f = 0;
            this.d = this.c.get(0);
            return;
        }
        if (this.f >= this.c.size()) {
            this.f = 0;
        }
        if ((l = System.currentTimeMillis()) - this.e >= 1000L) {
            int n3 = this.f;
            this.f = ((n3 | 1) + (n3 & 1)) % this.c.size();
            this.e = l;
        }
        this.d = this.c.get(this.f);
    }

    private List<TimedEffectEntry> k() {
        return Collections.emptyList();
    }

    public List<TimedEffectEntry> g() {
        int n = 269;
        return this.c;
    }

    public boolean j() {
        return this.i;
    }
}

