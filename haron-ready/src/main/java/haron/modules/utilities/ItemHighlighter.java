package haron.modules.utilities;

import haron.core.BooleanCoercion;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.modules.utilities.PotionEffectPolarity;
import haron.settings.NumberSetting;
import haron.settings.ColorSetting;
import haron.settings.ItemHighlightSetting;
import haron.settings.BooleanSetting;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

@ModuleInfo(a="Item Highliter", b="Подсвечивает указанные предметы в инвентаре", c=ModuleCategory.UTILITIES)
public class ItemHighlighter
extends HaronModule {
    private final BooleanSetting e = new BooleanSetting("Пульсация", false);
    private final NumberSetting f = new NumberSetting("Альфа", 170.0f, 0.0f, 255.0f, 1.0f);
    private final ItemHighlightSetting g = new ItemHighlightSetting("Дезориентация", Items.ENDER_EYE, false, new Color(100, 255, 100));
    private final ItemHighlightSetting h = new ItemHighlightSetting("Огненный смерч", Items.FIRE_CHARGE, false, new Color(255, 100, 0));
    private final ItemHighlightSetting i = new ItemHighlightSetting("Явная пыль", Items.SUGAR, false, new Color(255, 255, 255));
    private final ItemHighlightSetting j = new ItemHighlightSetting("Тотем бессмертия", Items.TOTEM_OF_UNDYING, false, new Color(255, 215, 0));
    private final ItemHighlightSetting k = new ItemHighlightSetting("Бутылка опыта", Items.EXPERIENCE_BOTTLE, false, new Color(100, 255, 200));
    private final ItemHighlightSetting l = new ItemHighlightSetting("Трапка", Items.NETHERITE_SCRAP, false, new Color(150, 150, 255));
    private final ItemHighlightSetting m = new ItemHighlightSetting("Пласт", Items.DRIED_KELP, false, new Color(180, 180, 180));
    private final ItemHighlightSetting n = new ItemHighlightSetting("Золотое яблоко", Items.GOLDEN_APPLE, false, new Color(255, 215, 0));
    private final ItemHighlightSetting o = new ItemHighlightSetting("Чарка", Items.ENCHANTED_GOLDEN_APPLE, false, new Color(255, 100, 255));
    private final ItemHighlightSetting p = new ItemHighlightSetting("Хорус", Items.CHORUS_FRUIT, false, new Color(200, 100, 255));
    private final ItemHighlightSetting q = new ItemHighlightSetting("Эндер пёрл", Items.ENDER_PEARL, false, new Color(0, 255, 150));
    private final ItemHighlightSetting r = new ItemHighlightSetting("Снежок заморозка", Items.SNOWBALL, false, new Color(200, 230, 255));
    private final ItemHighlightSetting s = new ItemHighlightSetting("Трапка 2", Items.POPPED_CHORUS_FRUIT, false, new Color(200, 170, 140));
    private final ItemHighlightSetting t = new ItemHighlightSetting("Стан", Items.NETHER_STAR, false, new Color(180, 140, 100));
    private final ItemHighlightSetting u = new ItemHighlightSetting("Светильник джека", Items.JACK_O_LANTERN, BooleanCoercion.from(777949212), new Color(255, 150, 0));
    private final ItemHighlightSetting v = new ItemHighlightSetting("Взрывная трапка", Items.PRISMARINE_SHARD, BooleanCoercion.from(-1775773688), new Color(255, 50, 50));
    private final BooleanSetting w = new BooleanSetting("Положительные зелья", BooleanCoercion.from(842908090));
    private final ColorSetting x;
    private final BooleanSetting y;
    private final ColorSetting z;
    private final Map<Item, ItemHighlightSetting> A;
    public static int a;
    public static boolean b;

    public Map<Item, ItemHighlightSetting> K() {
        return this.A;
    }

    public ColorSetting H() {
        return this.x;
    }

    public BooleanSetting G() {
        return this.w;
    }

    public ItemHighlighter() {
        ColorSetting f40tf12 = new ColorSetting("Цвет положительных", new Color(100, 255, 100));
        BooleanSetting xcv91t2 = this.w;
        Objects.requireNonNull(xcv91t2);
        this.x = f40tf12.a(xcv91t2::k);
        this.y = new BooleanSetting("Негативные зелья", true);
        ColorSetting f40tf13 = new ColorSetting("Цвет негативных", new Color(255, 100, 100));
        BooleanSetting xcv91t3 = this.y;
        Objects.requireNonNull(xcv91t3);
        this.z = f40tf13.a(xcv91t3::k);
        HashMap<Item, ItemHighlightSetting> hashMap = new HashMap<Item, ItemHighlightSetting>();
        hashMap.put(Items.ENDER_EYE, this.g);
        hashMap.put(Items.FIRE_CHARGE, this.h);
        hashMap.put(Items.SUGAR, this.i);
        hashMap.put(Items.TOTEM_OF_UNDYING, this.j);
        hashMap.put(Items.EXPERIENCE_BOTTLE, this.k);
        hashMap.put(Items.NETHERITE_SCRAP, this.l);
        hashMap.put(Items.DRIED_KELP, this.m);
        hashMap.put(Items.GOLDEN_APPLE, this.n);
        hashMap.put(Items.ENCHANTED_GOLDEN_APPLE, this.o);
        hashMap.put(Items.CHORUS_FRUIT, this.p);
        hashMap.put(Items.ENDER_PEARL, this.q);
        hashMap.put(Items.SNOWBALL, this.r);
        hashMap.put(Items.POPPED_CHORUS_FRUIT, this.s);
        hashMap.put(Items.NETHER_STAR, this.t);
        hashMap.put(Items.JACK_O_LANTERN, this.u);
        hashMap.put(Items.PRISMARINE_SHARD, this.v);
        this.A = Collections.unmodifiableMap(hashMap);
    }

    public ItemHighlightSetting B() {
        return this.r;
    }

    public ItemHighlightSetting C() {
        return this.s;
    }

    public ItemHighlightSetting D() {
        return this.t;
    }

    public ItemHighlightSetting F() {
        return this.v;
    }

    public BooleanSetting I() {
        return this.y;
    }

    public ColorSetting J() {
        return this.z;
    }

    private boolean b(Item item) {
        return BooleanCoercion.from(item == Items.POTION || item == Items.SPLASH_POTION || item == Items.LINGERING_POTION ? 1 : 0);
    }

    public Color b(ItemStack itemStack) {
        Item item = itemStack.getItem();
        ItemHighlightSetting v7v81t2 = this.A.get(item);
        if (v7v81t2 != null && ((Boolean)v7v81t2.k()).booleanValue()) {
            return v7v81t2.d();
        }
        if (this.b(item)) {
            PotionEffectPolarity efo3iq2 = this.c(itemStack);
            if (efo3iq2 == PotionEffectPolarity.POSITIVE && ((Boolean)this.w.k()).booleanValue()) {
                return this.x.a();
            }
            if (efo3iq2 == PotionEffectPolarity.NEGATIVE && ((Boolean)this.y.k()).booleanValue()) {
                return this.z.a();
            }
        }
        return Color.WHITE;
    }

    public ItemHighlightSetting x() {
        return this.n;
    }

    public ItemHighlightSetting s() {
        return this.i;
    }

    private PotionEffectPolarity c(ItemStack itemStack) {
        PotionContentsComponent potionContentsComponent = (PotionContentsComponent)itemStack.get(DataComponentTypes.POTION_CONTENTS);
        if (potionContentsComponent == null) {
            return PotionEffectPolarity.NONE;
        }
        int n = 0;
        int n2 = 0;
        boolean bl = false;
        Iterator iterator = potionContentsComponent.getEffects().iterator();
        while (iterator.hasNext()) {
            bl = true;
            StatusEffectCategory statusEffectCategory = ((StatusEffect)((StatusEffectInstance)iterator.next()).getEffectType().value()).getCategory();
            if (statusEffectCategory == StatusEffectCategory.BENEFICIAL) {
                ++n;
                continue;
            }
            if (statusEffectCategory != StatusEffectCategory.HARMFUL) continue;
            ++n2;
        }
        if (!bl) {
            return PotionEffectPolarity.NONE;
        }
        if (n > n2) {
            return PotionEffectPolarity.POSITIVE;
        }
        return n2 <= n ? (n <= 0 ? PotionEffectPolarity.NONE : PotionEffectPolarity.POSITIVE) : PotionEffectPolarity.NEGATIVE;
    }

    public int n() {
        if (!((Boolean)this.e.k()).booleanValue()) {
            return this.f.b();
        }
        int n = this.f.b();
        double d = Math.sin((double)System.currentTimeMillis() / 100.0) * 0.5 + 0.5;
        int n2 = Math.max(0, n / 2);
        int n3 = n / 2;
        int n4 = Math.min(255, (n & ~n3) + (n3 & ~n) + 2 * (n & n3));
        int n5 = (int)((double)((n4 ^ n2) - 2 * (~n4 & n2)) * d);
        return (n2 ^ n5) + 2 * (n2 & n5);
    }

    public boolean a(ItemStack itemStack) {
        if (!this.k() || itemStack.isEmpty()) {
            return false;
        }
        Item item = itemStack.getItem();
        ItemHighlightSetting v7v81t2 = this.A.get(item);
        if (v7v81t2 != null && ((Boolean)v7v81t2.k()).booleanValue()) {
            return true;
        }
        if (this.b(item)) {
            PotionEffectPolarity efo3iq2 = this.c(itemStack);
            if (efo3iq2 == PotionEffectPolarity.POSITIVE && ((Boolean)this.w.k()).booleanValue()) {
                return true;
            }
            if (efo3iq2 == PotionEffectPolarity.NEGATIVE && ((Boolean)this.y.k()).booleanValue()) {
                return true;
            }
        }
        return false;
    }

    public Color a(Item item) {
        ItemHighlightSetting v7v81t2 = this.A.get(item);
        return v7v81t2 != null ? v7v81t2.d() : Color.WHITE;
    }

    public List<Item> o() {
        if (ItemHighlighter.c.player == null) {
            return Collections.emptyList();
        }
        ArrayList<Item> arrayList = new ArrayList<Item>();
        for (ItemStack itemStack : ItemHighlighter.c.player.getInventory().main) {
            Item item;
            if (!this.a(itemStack) || arrayList.contains(item = itemStack.getItem())) continue;
            arrayList.add(item);
        }
        return arrayList;
    }

    public BooleanSetting p() {
        int n = 561;
        return this.e;
    }

    public ItemHighlightSetting t() {
        return this.j;
    }

    public ItemHighlightSetting v() {
        int n = 323;
        return this.l;
    }

    public ItemHighlightSetting q() {
        return this.g;
    }

    public ItemHighlightSetting z() {
        return this.p;
    }

    public ItemHighlightSetting w() {
        return this.m;
    }

    public ItemHighlightSetting u() {
        return this.k;
    }

    public ItemHighlightSetting r() {
        return this.h;
    }

    public ItemHighlightSetting E() {
        return this.u;
    }

    public ItemHighlightSetting y() {
        return this.o;
    }

    public ItemHighlightSetting A() {
        return this.q;
    }
}

