package rockstar.client.internal.script;






import rockstar.client.ui.*;
import rockstar.client.server.*;
import rockstar.client.i18n.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import rockstar.client.util.LegacyItemTypes;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Generated;
import lombok.SneakyThrows;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Language;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.ui.ThemeColors;
import rockstar.client.internal.core.AttributeItemType;
import rockstar.client.server.ServerUtils;
import rockstar.client.MinecraftClientAccess;

public final class ItemNbtUtils
implements MinecraftClientAccess {
    private static Language internalField0346;
    private static boolean internalField0277;

    public static List<ItemStack> internalMethod00825(ItemStack itemStack) {
        ArrayList<ItemStack> arrayList = new ArrayList<ItemStack>();
        ContainerComponent containerComponent = (ContainerComponent)itemStack.get(DataComponentTypes.CONTAINER);
        if (containerComponent == null) {
            BundleContentsComponent bundleContentsComponent = (BundleContentsComponent)itemStack.get(DataComponentTypes.BUNDLE_CONTENTS);
            if (bundleContentsComponent == null) {
                return arrayList;
            }
            for (ItemStack itemStack2 : bundleContentsComponent.iterate()) {
                arrayList.add(itemStack2);
            }
            return arrayList;
        }
        for (ItemStack itemStack3 : containerComponent.iterateNonEmpty()) {
            arrayList.add(itemStack3);
        }
        return arrayList;
    }

    public static NbtCompound internalMethod05003(ItemStack itemStack) {
        try {
            NbtCompound nbtCompound;
            NbtCompound nbtCompound2;
            DynamicRegistryManager dynamicRegistryManager = ItemNbtUtils.internalField0149.world.getRegistryManager();
            NbtElement nbtElement = LegacyItemTypes.toNbtAllowEmpty(itemStack, (RegistryWrapper.WrapperLookup)dynamicRegistryManager);
            if (itemStack.isEmpty()) {
                return null;
            }
            NbtComponent nbtComponent = (NbtComponent)itemStack.get(DataComponentTypes.CUSTOM_DATA);
            if (nbtComponent != null) {
                return nbtComponent.copyNbt();
            }
            if (nbtElement instanceof NbtCompound && (nbtCompound2 = (NbtCompound)nbtElement).contains("components") && (nbtCompound = nbtCompound2.getCompound("components").orElseGet(net.minecraft.nbt.NbtCompound::new)).contains("minecraft:custom_data")) {
                return nbtCompound.getCompound("minecraft:custom_data").orElseGet(net.minecraft.nbt.NbtCompound::new);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return null;
    }

    public static boolean internalMethod06696(ItemStack itemStack, String string) {
        NbtCompound nbtCompound;
        NbtCompound nbtCompound2 = ItemNbtUtils.internalMethod05003(itemStack);
        if (nbtCompound2 == null) {
            return false;
        }
        if (nbtCompound2.contains("PublicBukkitValues") && (nbtCompound = nbtCompound2.getCompound("PublicBukkitValues").orElseGet(net.minecraft.nbt.NbtCompound::new)).contains("minecraft:don-item")) {
            return nbtCompound.getString("minecraft:don-item").orElse("").contains(string);
        }
        if (nbtCompound2.contains("don-item")) {
            return nbtCompound2.getString("don-item").orElse("").contains(string);
        }
        return false;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @SneakyThrows(IOException.class)
    public static String internalMethod03501(String string) {
        String string2;
        ResourceManager resourceManager = internalField0149.getResourceManager();
        Identifier identifier = Identifier.of((String)"minecraft", (String)("models/item/" + string.replace("minecraft:", "") + ".json"));
        Optional optional = resourceManager.getResource(identifier);
        if (!optional.isPresent()) return null;
        BufferedReader bufferedReader = ((Resource)optional.get()).getReader();
        try {
            string2 = bufferedReader.lines().collect(Collectors.joining("\n"));
            if (bufferedReader == null) return string2;
        }
        catch (Throwable throwable) {
            try {
                if (bufferedReader == null) throw throwable;
                try {
                    bufferedReader.close();
                    throw throwable;
                }
                catch (Throwable throwable2) {
                    throwable.addSuppressed(throwable2);
                }
                throw throwable;
            }
            catch (Exception exception) {
                System.err.println("\u041e\u0448\u0438\u0431\u043a\u0430 \u043f\u0440\u0438 \u043f\u043e\u043b\u0443\u0447\u0435\u043d\u0438\u0438 \u0441\u0435\u0440\u0432\u0435\u0440\u043d\u043e\u0439 \u043c\u043e\u0434\u0435\u043b\u0438: " + exception.getMessage());
                return null;
            }
        }
        bufferedReader.close();
        return string2;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static String internalMethod06695(ItemStack itemStack) {
        Language language = ItemNbtUtils.internalMethod00436();
        if (language == null) {
            return ItemNbtUtils.internalMethod02181(itemStack);
        }
        Language language2 = Language.getInstance();
        try {
            Language.setInstance((Language)language);
            String string = ItemNbtUtils.internalMethod02181(itemStack);
            return string;
        }
        finally {
            Language.setInstance((Language)language2);
        }
    }

    private static Language internalMethod00436() {
        if (internalField0346 == null && !internalField0277) {
            try {
                internalField0346 = TranslationStorage.load((ResourceManager)internalField0149.getResourceManager(), List.of("en_us", "ru_ru"), (boolean)false);
            }
            catch (Exception exception) {
                internalField0277 = true;
            }
        }
        return internalField0346;
    }

    public static String internalMethod02181(ItemStack itemStack) {
        return itemStack.getName().getString().replace("[", "").replace("] ", "").replace("- ", "").replace(" -", "").replace("xxx ", "").replace(" xxx", "").replace("ggg ", "").replace(" ggg", "").replace("gg ", "").replace(" gg", "").replace("123 ", "").replace(" 123", "").replace("\u2605", "");
    }

    public static ColorRGBA internalMethod03624(Text text) {
        for (Text text2 : text.getSiblings()) {
            ColorRGBA colorRGBA = ItemNbtUtils.internalMethod06153(text2);
            if (colorRGBA == null) continue;
            return colorRGBA.withAlpha(255.0f);
        }
        TextColor textColor = text.getStyle().getColor();
        if (textColor != null) {
            return ColorRGBA.fromInt(textColor.getRgb()).withAlpha(255.0f);
        }
        return ThemeColors.internalMethod02531();
    }

    private static ColorRGBA internalMethod06153(Text text) {
        TextColor textColor = text.getStyle().getColor();
        if (textColor != null) {
            return ColorRGBA.fromInt(textColor.getRgb());
        }
        for (Text text2 : text.getSiblings()) {
            ColorRGBA colorRGBA = ItemNbtUtils.internalMethod06153(text2);
            if (colorRGBA == null) continue;
            return colorRGBA;
        }
        return null;
    }

    public static boolean internalMethod01122(ItemStack itemStack) {
        NbtCompound nbtCompound = ItemNbtUtils.internalMethod05003(itemStack);
        if (nbtCompound == null) {
            return false;
        }
        if (nbtCompound.contains("PublicBukkitValues")) {
            NbtCompound nbtCompound2 = nbtCompound.getCompound("PublicBukkitValues").orElseGet(net.minecraft.nbt.NbtCompound::new);
            return nbtCompound2.contains("minecraft:don-item");
        }
        if (nbtCompound.contains("sixitem")) {
            return true;
        }
        return nbtCompound.contains("don-item");
    }

    public static String internalMethod08872(ItemStack itemStack) {
        NbtCompound nbtCompound = ItemNbtUtils.internalMethod05003(itemStack);
        if (nbtCompound == null) {
            return "";
        }
        NbtCompound nbtCompound2 = nbtCompound.getCompound("sphereEffect").orElseGet(net.minecraft.nbt.NbtCompound::new);
        if (nbtCompound.contains("PublicBukkitValues")) {
            NbtCompound nbtCompound3 = nbtCompound.getCompound("PublicBukkitValues").orElseGet(net.minecraft.nbt.NbtCompound::new);
            if (nbtCompound3.contains("minecraft:don-item")) {
                return nbtCompound3.getString("minecraft:don-item").orElse("");
            }
            if (nbtCompound3.contains("minecraft:spooky-item")) {
                return nbtCompound3.getString("minecraft:spooky-item").orElse("");
            }
        }
        if (nbtCompound.contains("don-item")) {
            return nbtCompound.getString("don-item").orElse("");
        }
        if (nbtCompound.contains("spooky-item")) {
            return nbtCompound.getString("spooky-item").orElse("");
        }
        if (ServerUtils.internalMethod06501("holyworld") && nbtCompound.contains("sphereEffect") && itemStack.getItem() == Items.TOTEM_OF_UNDYING && nbtCompound2.contains("rank")) {
            if (nbtCompound2.getString("rank").orElse("").equals("ETERNITY")) {
                return nbtCompound2.getString("name").orElse("");
            }
            return nbtCompound2.getString("rank").orElse("");
        }
        return "";
    }

    public static AttributeItemType internalMethod02195(ItemStack itemStack) {
        for (AttributeItemType typedValue216 : AttributeItemType.values()) {
            for (String string : typedValue216.internalMethod06734()) {
                if (!ItemNbtUtils.internalMethod08872(itemStack).equals(string)) continue;
                return typedValue216;
            }
        }
        return null;
    }

    public static int internalMethod01121(ItemStack itemStack) {
        if (itemStack.hasEnchantments()) {
            for (AttributeItemType typedValue216 : AttributeItemType.values()) {
                for (String string : typedValue216.internalMethod06734()) {
                    if (!ItemNbtUtils.internalMethod08872(itemStack).equals(string)) continue;
                    return 12 - typedValue216.internalMethod01123();
                }
            }
            return 0;
        }
        return -1;
    }

    public static int internalMethod06269(ItemStack itemStack) {
        if (itemStack.hasEnchantments() || ItemNbtUtils.internalMethod01122(itemStack)) {
            for (AttributeItemType typedValue216 : AttributeItemType.values()) {
                for (String string : typedValue216.internalMethod06734()) {
                    if (!ItemNbtUtils.internalMethod08872(itemStack).equals(string)) continue;
                    return 15 - typedValue216.internalMethod01127();
                }
            }
            return 16;
        }
        return 17;
    }

    @Generated
    private ItemNbtUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
