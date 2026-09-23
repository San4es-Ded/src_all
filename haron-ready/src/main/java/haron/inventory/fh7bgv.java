package haron.inventory;

import haron.inventory.nvlzvr;
import haron.inventory.nzsxbq;
import haron.inventory.welt51;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class fh7bgv {
    private static Field d;
    private static Field e;
    private static Field f;
    private static Field g;
    private static Method mStart;
    private static Method mEnd;
    public static int b;
    public static boolean c;
    private static final Item[] WATCHED_ITEMS;
    public static final nzsxbq a;

    private static int readEnd(Object object) throws ReflectiveOperationException {
        return (Integer)mEnd.invoke(object, new Object[0]);
    }

    public static ItemStack stackForGroup(ItemCooldownManager itemCooldownManager, Identifier identifier) {
        Item item = (Item)Registries.ITEM.get(identifier);
        if (item != Items.AIR) {
            return item.getDefaultStack();
        }
        for (Item item2 : WATCHED_ITEMS) {
            ItemStack itemStack = item2.getDefaultStack();
            if (!identifier.equals((Object)itemCooldownManager.getGroup(itemStack))) continue;
            return itemStack;
        }
        return new ItemStack((ItemConvertible)Items.PAPER);
    }

    public static nzsxbq viaPublicApi(ItemCooldownManager itemCooldownManager, Item item) {
        try {
            ItemStack itemStack = item.getDefaultStack();
            if (!itemCooldownManager.isCoolingDown(itemStack)) {
                return a;
            }
            float f = itemCooldownManager.getCooldownProgress(itemStack, 0.0f);
            if (f <= 0.0f) {
                return a;
            }
            float f2 = itemCooldownManager.getCooldownProgress(itemStack, 1.0f);
            float f3 = f - f2;
            float f4 = f3 > 1.0E-4f ? 1.0f / f3 : 40.0f;
            float f5 = f * f4;
            float f6 = f5 / 20.0f;
            return new nvlzvr(f, f6);
        }
        catch (Throwable throwable) {
            return a;
        }
    }

    private static boolean reflectionFailed() {
        return false;
    }

    private static Map<Object, Object> readEntries(ItemCooldownManager itemCooldownManager) throws IllegalAccessException {
        return (Map)e.get(itemCooldownManager);
    }

    private static int readTick(ItemCooldownManager itemCooldownManager) throws IllegalAccessException {
        return d.getInt(itemCooldownManager);
    }

    private static int readStart(Object object) throws ReflectiveOperationException {
        return (Integer)mStart.invoke(object, new Object[0]);
    }

    public static List<welt51> collectActiveGroups(ItemCooldownManager itemCooldownManager) {
        ArrayList<welt51> arrayList = new ArrayList<welt51>();
        try {
            int n = fh7bgv.readTick(itemCooldownManager);
            Map<Object, Object> map = fh7bgv.readEntries(itemCooldownManager);
            if (map == null) {
                return arrayList;
            }
            for (Map.Entry<Object, Object> entry : map.entrySet()) {
                int n2;
                Object object = entry.getKey();
                if (!(object instanceof Identifier) || (n2 = fh7bgv.readEnd(entry.getValue())) <= n) continue;
                arrayList.add(new welt51((Identifier)object, n2 - n));
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return arrayList;
    }

    static {
        WATCHED_ITEMS = new Item[]{Items.CHORUS_FRUIT, Items.POPPED_CHORUS_FRUIT, Items.ENDER_PEARL, Items.SHIELD, Items.CROSSBOW, Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE, Items.TOTEM_OF_UNDYING, Items.TRIDENT, Items.BOW, Items.WIND_CHARGE};
        a = new nzsxbq(0, 0, 0);
        try {
            d = fh7bgv.a(ItemCooldownManager.class, "tick", "field_8024");
            e = fh7bgv.a(ItemCooldownManager.class, "entries", "cooldowns", "field_8023");
            Class<?> clazz = null;
            for (Class<?> clazz2 : ItemCooldownManager.class.getDeclaredClasses()) {
                if (!clazz2.getSimpleName().equals("Entry") && !clazz2.isRecord()) continue;
                clazz = clazz2;
                break;
            }
            if (clazz == null) {
                clazz = fh7bgv.a();
            }
            if (clazz != null) {
                Method method;
                for (String string : new String[]{"startTick", "startTime", "comp_1", "method_63684"}) {
                    try {
                        method = clazz.getDeclaredMethod(string, new Class[0]);
                        if (method.getReturnType() != Integer.TYPE) continue;
                        method.setAccessible(true);
                        mStart = method;
                        break;
                    }
                    catch (NoSuchMethodException noSuchMethodException) {
                        // empty catch block
                    }
                }
                for (String string : new String[]{"endTick", "endTime", "comp_2", "method_63685"}) {
                    try {
                        method = clazz.getDeclaredMethod(string, new Class[0]);
                        if (method.getReturnType() != Integer.TYPE) continue;
                        method.setAccessible(true);
                        mEnd = method;
                        break;
                    }
                    catch (NoSuchMethodException noSuchMethodException) {
                        // empty catch block
                    }
                }
                f = fh7bgv.a(clazz, "startTick", "startTime");
                g = fh7bgv.a(clazz, "endTick", "endTime");
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public static nzsxbq a(ItemCooldownManager itemCooldownManager, Item item) {
        if (fh7bgv.reflectionFailed()) {
            return fh7bgv.viaPublicApi(itemCooldownManager, item);
        }
        return fh7bgv.a(itemCooldownManager, itemCooldownManager.getGroup(item.getDefaultStack()));
    }

    private static Field a(Class<?> clazz, String ... stringArray) {
        for (String string : stringArray) {
            try {
                Field field = clazz.getDeclaredField(string);
                field.setAccessible(true);
                return field;
            }
            catch (NoSuchFieldException noSuchFieldException) {
            }
        }
        return null;
    }

    private static Class<?> a() {
        try {
            return Class.forName("net.minecraft.entity.player.ItemCooldownManager$Entry");
        }
        catch (ClassNotFoundException classNotFoundException) {
            try {
                return Class.forName("net.minecraft.ItemCooldownManager$class_1797");
            }
            catch (ClassNotFoundException classNotFoundException2) {
                return null;
            }
        }
    }

    public static nzsxbq a(ItemCooldownManager itemCooldownManager, Identifier identifier) {
        try {
            Map<Object, Object> map = fh7bgv.readEntries(itemCooldownManager);
            if (map == null) {
                return a;
            }
            Object object = map.get(identifier);
            if (object == null) {
                return a;
            }
            return new nzsxbq(fh7bgv.readTick(itemCooldownManager), fh7bgv.readStart(object), fh7bgv.readEnd(object));
        }
        catch (Throwable throwable) {
            return a;
        }
    }
}
