package haron.modules.utilities;

import haron.core.BooleanCoercion;
import haron.events.KeyInputEvent;
import haron.inventory.rk1ts2;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.modules.utilities.ItemSwapBinding;
import haron.settings.NumberSetting;
import haron.settings.KeybindSetting;
import haron.settings.BooleanSetting;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.Potion;
import net.minecraft.registry.entry.RegistryEntry;

@ModuleInfo(a="Fast Swap", b="Быстрый выбор предметов по нажатию клавиш", c=ModuleCategory.UTILITIES)
public class FastSwap
extends HaronModule {
    private final KeybindSetting e = new KeybindSetting("Кнопка хоруса", -1);
    private final KeybindSetting f = new KeybindSetting("Кнопка эндерпёрла", -1);
    private final KeybindSetting g = new KeybindSetting("Кнопка зелья исцеления", -1);
    private final KeybindSetting h = new KeybindSetting("Кнопка трапки", -1);
    private final KeybindSetting i = new KeybindSetting("Кнопка пласта", -1);
    private final KeybindSetting j = new KeybindSetting("Кнопка дезориентации", -1);
    private final KeybindSetting k = new KeybindSetting("Кнопка явной пыли", -1);
    private final KeybindSetting l = new KeybindSetting("Кнопка шаровой молнии", -1);
    private final KeybindSetting m = new KeybindSetting("Кнопка кома слизи", -1);
    private final KeybindSetting n = new KeybindSetting("Кнопка черепашьего захвата", -1);
    private final KeybindSetting o = new KeybindSetting("Кнопка паучьей судьбы", -1);
    private final KeybindSetting p = new KeybindSetting("Кнопка стана", -1);
    private final KeybindSetting q = new KeybindSetting("Кнопка магнитного шара", -1);
    private final KeybindSetting r = new KeybindSetting("Кнопка взрывной трапки", -1);
    private final KeybindSetting s = new KeybindSetting("Кнопка взрывной штучки", -1);
    private final KeybindSetting t = new KeybindSetting("Кнопка стана звезды", -1);
    private final KeybindSetting u = new KeybindSetting("Кнопка кома снега", -1);
    private final BooleanSetting v = new BooleanSetting("Отображать бинды", true);
    private final NumberSetting w = new NumberSetting("Размер шрифта биндов", 1.0f, 0.5f, 2.0f, 0.1f);
    private final List<ItemSwapBinding> x;
    public static int a;
    public static boolean b;

    public List<ItemSwapBinding> G() {
        return this.x;
    }

    public FastSwap() {
        ItemSwapBinding[] rrca84Array = new ItemSwapBinding[]{new ItemSwapBinding(this.h, itemStack -> {
            return BooleanCoercion.from(itemStack.getItem() != Items.NETHERITE_SCRAP ? 0 : 1);
        }), new ItemSwapBinding(this.i, itemStack -> {
            int n = itemStack.getItem() != Items.DRIED_KELP ? 0 : 1;
            return BooleanCoercion.from(n);
        }), new ItemSwapBinding(this.j, itemStack -> {
            return BooleanCoercion.from(itemStack.getItem() != Items.ENDER_EYE ? 0 : 1);
        }), new ItemSwapBinding(this.k, itemStack -> {
            return BooleanCoercion.from(itemStack.getItem() != Items.SUGAR ? 0 : 1);
        }), new ItemSwapBinding(this.l, itemStack -> {
            return BooleanCoercion.from(itemStack.getItem() != Items.NETHER_STAR ? 0 : 1);
        }), new ItemSwapBinding(this.m, itemStack -> {
            return BooleanCoercion.from(itemStack.getItem() != Items.SLIME_BALL ? 0 : 1);
        }), new ItemSwapBinding(this.n, itemStack -> {
            int n = itemStack.getItem() != Items.TURTLE_SCUTE ? 0 : 1;
            return BooleanCoercion.from(n);
        }), new ItemSwapBinding(this.o, itemStack -> {
            return BooleanCoercion.from(itemStack.getItem() != Items.COBWEB ? 0 : 1);
        }), new ItemSwapBinding(this.p, itemStack -> {
            return BooleanCoercion.from(itemStack.getItem() != Items.ENDER_EYE ? 0 : 1);
        }), new ItemSwapBinding(this.q, itemStack -> {
            return BooleanCoercion.from(itemStack.getItem() != Items.FIREWORK_STAR ? 0 : 1);
        }), new ItemSwapBinding(this.e, itemStack -> {
            return BooleanCoercion.from(itemStack.getItem() != Items.CHORUS_FRUIT ? 0 : 1);
        }), new ItemSwapBinding(this.f, itemStack -> {
            return BooleanCoercion.from(itemStack.getItem() != Items.ENDER_PEARL ? 0 : 1);
        }), new ItemSwapBinding(this.g, this::a), new ItemSwapBinding(this.r, itemStack -> {
            return BooleanCoercion.from(itemStack.getItem() != Items.PRISMARINE_SHARD ? 0 : 1);
        }), new ItemSwapBinding(this.s, itemStack -> {
            return BooleanCoercion.from(itemStack.getItem() != Items.FIRE_CHARGE ? 0 : 1);
        }), new ItemSwapBinding(this.t, itemStack -> {
            int n = 153;
            return BooleanCoercion.from(itemStack.getItem() != Items.NETHER_STAR ? 0 : 1);
        }), new ItemSwapBinding(this.u, itemStack -> {
            int n = 457;
            return BooleanCoercion.from(itemStack.getItem() != Items.SNOWBALL ? 0 : 1);
        })};
        this.x = Collections.unmodifiableList(Arrays.asList(rrca84Array));
    }

    public KeybindSetting B() {
        return this.s;
    }

    public KeybindSetting C() {
        return this.t;
    }

    public KeybindSetting D() {
        return this.u;
    }

    public NumberSetting F() {
        return this.w;
    }

    public KeybindSetting x() {
        return this.o;
    }

    public KeybindSetting s() {
        return this.j;
    }

    public KeybindSetting n() {
        return this.e;
    }

    @EventHandler
    public void a(KeyInputEvent dsgqgn2) {
        if (FastSwap.c.player == null || FastSwap.c.currentScreen != null) {
            return;
        }
        int pressedKey = dsgqgn2.a();
        this.x.stream().filter(rrca842 -> {
            int boundKey = rrca842.c.a();
            return BooleanCoercion.from(boundKey != -1 && boundKey != 0 && boundKey == pressedKey ? 1 : 0);
        }).findFirst().flatMap(rrca842 -> {
            return rk1ts2.a(rrca842.d, false, true);
        }).ifPresent(n -> {
            FastSwap.c.player.getInventory().selectedSlot = n;
        });
    }

    public boolean a(ItemStack itemStack) {
        Optional optional;
        if (itemStack.getItem() != Items.POTION) {
            return false;
        }
        if (itemStack.getItem() instanceof PotionItem && (optional = ((PotionContentsComponent)itemStack.getOrDefault(DataComponentTypes.POTION_CONTENTS, (Object)PotionContentsComponent.DEFAULT)).potion()).isPresent()) {
            Iterator iterator = ((Potion)((RegistryEntry)optional.get()).value()).getEffects().iterator();
            while (iterator.hasNext()) {
                if (((StatusEffectInstance)iterator.next()).getEffectType() != StatusEffects.INSTANT_HEALTH) continue;
                return true;
            }
        }
        return false;
    }

    public KeybindSetting o() {
        return this.f;
    }

    public KeybindSetting p() {
        return this.g;
    }

    public KeybindSetting t() {
        return this.k;
    }

    public KeybindSetting v() {
        return this.m;
    }

    public KeybindSetting q() {
        return this.h;
    }

    public KeybindSetting z() {
        return this.q;
    }

    public KeybindSetting w() {
        return this.n;
    }

    public KeybindSetting u() {
        return this.l;
    }

    public KeybindSetting r() {
        return this.i;
    }

    public BooleanSetting E() {
        return this.v;
    }

    public KeybindSetting y() {
        return this.p;
    }

    public KeybindSetting A() {
        return this.r;
    }
}
