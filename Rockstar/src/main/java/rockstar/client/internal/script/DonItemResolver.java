package rockstar.client.internal.script;




import rockstar.client.util.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import rockstar.client.internal.inventory.DonItemCatalog;
import rockstar.client.util.CustomItemUtils;
import rockstar.client.internal.script.ItemNbtUtils;
import rockstar.client.util.ClientMessages;

public final class DonItemResolver {
    private static final String internalField0248 = "PublicBukkitValues";
    private static final String internalField0247 = "minecraft:don-item";
    private static final String internalField1077 = "minecraft:ftid";
    private static final Map<String, Long> internalField0543 = new HashMap<String, Long>();
    private static Map<String, String> internalField0544;
    private static Map<String, String> internalField1197;

    private DonItemResolver() {
    }

    public static String internalMethod03278(ItemStack itemStack) {
        if (itemStack == null || itemStack.isEmpty()) {
            return null;
        }
        NbtComponent nbtComponent = (NbtComponent)itemStack.get(DataComponentTypes.CUSTOM_DATA);
        if (nbtComponent == null) {
            return null;
        }
        NbtCompound nbtCompound = nbtComponent.copyNbt();
        if (nbtCompound.contains(internalField0248)) {
            NbtCompound nbtCompound2 = nbtCompound.getCompound(internalField0248).orElseGet(NbtCompound::new);
            if (nbtCompound2.contains(internalField0247)) {
                return nbtCompound2.getString(internalField0247).orElse("");
            }
            if (nbtCompound2.contains(internalField1077)) {
                return nbtCompound2.getString(internalField1077).orElse("");
            }
        }
        if (nbtCompound.contains(internalField0247)) {
            return nbtCompound.getString(internalField0247).orElse("");
        }
        if (nbtCompound.contains("don-item")) {
            return nbtCompound.getString("don-item").orElse("");
        }
        return null;
    }

    public static boolean internalMethod06709(ItemStack itemStack) {
        return DonItemResolver.internalMethod03278(itemStack) != null;
    }

    public static boolean internalMethod05530(ItemStack itemStack, String string, ItemStack itemStack2) {
        String string2;
        String string3 = string2 = string != null && !string.isBlank() ? string : DonItemResolver.internalMethod06910(itemStack);
        if (string2 == null) {
            return false;
        }
        String string4 = DonItemResolver.internalMethod05925(DonItemResolver.internalMethod03278(itemStack));
        String string5 = DonItemResolver.internalMethod05925(DonItemResolver.internalMethod03278(itemStack2));
        if (string4 != null && string4.equals(string5)) {
            return true;
        }
        String string6 = DonItemResolver.internalMethod06910(itemStack2);
        if (string6 != null) {
            return string6.equalsIgnoreCase(string2);
        }
        if (DonItemResolver.internalMethod02473().containsKey(string2)) {
            return false;
        }
        return ItemNbtUtils.internalMethod02181(itemStack2).equalsIgnoreCase(string2);
    }

    public static String internalMethod06910(ItemStack itemStack) {
        String string;
        for (String object2 : DonItemResolver.internalMethod05057(itemStack)) {
            String string2 = object2.toLowerCase(Locale.ROOT);
            for (Map.Entry<String, String> entry : DonItemResolver.internalMethod02473().entrySet()) {
                if (!string2.contains(entry.getValue().toLowerCase(Locale.ROOT))) continue;
                return entry.getKey();
            }
        }
        String string3 = DonItemResolver.internalMethod05925(DonItemResolver.internalMethod03278(itemStack));
        if (string3 != null && (string = DonItemResolver.internalMethod07068().get(string3)) != null) {
            return string;
        }
        CustomItemUtils.InternalType0254 nestedValue2032 = CustomItemUtils.internalMethod03238(itemStack);
        if (nestedValue2032 != null && nestedValue2032.internalMethod07218() == CustomItemUtils.InternalType0440.internalField1338) {
            return nestedValue2032.internalMethod01319();
        }
        return null;
    }

    public static void internalMethod03133(String string, ItemStack itemStack) {
        if (string == null || string.isBlank()) {
            return;
        }
        String string2 = ItemNbtUtils.internalMethod02181(itemStack);
        if (!string2.toLowerCase(Locale.ROOT).contains(string.toLowerCase(Locale.ROOT))) {
            return;
        }
        long l = System.currentTimeMillis();
        Long l2 = internalField0543.get(string);
        if (l2 != null && l - l2 < 60000L) {
            return;
        }
        internalField0543.put(string, l);
        String string3 = DonItemResolver.internalMethod06910(itemStack);
        Object object = string3 != null ? "\u044d\u0442\u043e " + string3 : "\u043d\u0435\u0442 \u0441\u0435\u0440\u0432\u0435\u0440\u043d\u043e\u0433\u043e \u043e\u043f\u0438\u0441\u0430\u043d\u0438\u044f";
        ClientMessages.internalMethod03058(Text.of((String)(string + ": \u043b\u043e\u0442 \u00ab" + string2 + "\u00bb \u043d\u0435 \u0442\u043e\u0442 \u043f\u0440\u0435\u0434\u043c\u0435\u0442 (" + (String)object + ") \u2014 \u043f\u0440\u043e\u043f\u0443\u0441\u043a\u0430\u044e")));
    }

    private static String internalMethod05925(String string) {
        return string == null || string.isBlank() ? null : CustomItemUtils.internalMethod00580(string);
    }

    private static List<String> internalMethod05057(ItemStack itemStack) {
        if (itemStack == null || itemStack.isEmpty()) {
            return List.of();
        }
        LoreComponent loreComponent = (LoreComponent)itemStack.get(DataComponentTypes.LORE);
        if (loreComponent == null) {
            return List.of();
        }
        ArrayList<String> arrayList = new ArrayList<String>(loreComponent.lines().size());
        for (Text text : loreComponent.lines()) {
            arrayList.add(text.getString());
        }
        return arrayList;
    }

    private static Map<String, String> internalMethod02473() {
        if (internalField0544 == null) {
            internalField0544 = DonItemCatalog.internalMethod06733(DonItemCatalog.InternalType0189.internalField0407);
        }
        return internalField0544;
    }

    private static Map<String, String> internalMethod07068() {
        if (internalField1197 != null) {
            return internalField1197;
        }
        HashMap<String, String> hashMap = new HashMap<String, String>();
        for (DonItemCatalog.InternalType0503 nestedValue2066 : DonItemCatalog.internalMethod02566(DonItemCatalog.InternalType0189.internalField0407)) {
            for (DonItemCatalog.InternalType0190 nestedValue2027 : nestedValue2066.internalMethod07260()) {
                String string = DonItemResolver.internalMethod05925(DonItemResolver.internalMethod03278(nestedValue2027.internalMethod05752()));
                if (string == null || nestedValue2027.internalMethod06194() == null) continue;
                hashMap.put(string, nestedValue2027.internalMethod06194());
            }
        }
        internalField1197 = hashMap;
        return internalField1197;
    }
}
