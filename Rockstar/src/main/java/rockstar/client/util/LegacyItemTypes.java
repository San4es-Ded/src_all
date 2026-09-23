package rockstar.client.util;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import org.jetbrains.annotations.Nullable;

import java.util.AbstractList;
import java.util.List;
import java.util.Optional;

/**
 * Semantic replacement for the specialized item subclasses removed in 1.21.5+.
 * Vanilla now describes tools and armor through tags and data components.
 */
public final class LegacyItemTypes {
    private static final EquipmentSlot[] LEGACY_ARMOR_ORDER = {
        EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD
    };

    private LegacyItemTypes() {
    }

    public static boolean isSword(ItemStack stack) {
        return stack != null && stack.isIn(ItemTags.SWORDS);
    }

    public static boolean isSword(Item item) {
        return item != null && isSword(item.getDefaultStack());
    }

    public static boolean isPickaxe(ItemStack stack) {
        return stack != null && stack.isIn(ItemTags.PICKAXES);
    }

    public static boolean isPickaxe(Item item) {
        return item != null && isPickaxe(item.getDefaultStack());
    }

    public static boolean isArmor(ItemStack stack) {
        return getArmorSlot(stack) != null;
    }

    public static boolean isArmor(Item item) {
        return item != null && isArmor(item.getDefaultStack());
    }

    public static @Nullable EquipmentSlot getArmorSlot(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return null;
        }
        if (stack.isIn(ItemTags.HEAD_ARMOR)) {
            return EquipmentSlot.HEAD;
        }
        if (stack.isIn(ItemTags.CHEST_ARMOR)) {
            return EquipmentSlot.CHEST;
        }
        if (stack.isIn(ItemTags.LEG_ARMOR)) {
            return EquipmentSlot.LEGS;
        }
        if (stack.isIn(ItemTags.FOOT_ARMOR)) {
            return EquipmentSlot.FEET;
        }
        return null;
    }

    public static @Nullable EquipmentSlot getArmorSlot(Item item) {
        return item == null ? null : getArmorSlot(item.getDefaultStack());
    }

    public static int getDefense(ItemStack stack) {
        EquipmentSlot slot = getArmorSlot(stack);
        if (slot == null) {
            return 0;
        }
        AttributeModifiersComponent modifiers = stack.getOrDefault(
            DataComponentTypes.ATTRIBUTE_MODIFIERS,
            AttributeModifiersComponent.DEFAULT
        );
        return (int)Math.round(modifiers.applyOperations(EntityAttributes.ARMOR, 0.0, slot));
    }

    public static int getToughness(ItemStack stack) {
        EquipmentSlot slot = getArmorSlot(stack);
        if (slot == null) {
            return 0;
        }
        AttributeModifiersComponent modifiers = stack.getOrDefault(
            DataComponentTypes.ATTRIBUTE_MODIFIERS,
            AttributeModifiersComponent.DEFAULT
        );
        return (int)modifiers.applyOperations(EntityAttributes.ARMOR_TOUGHNESS, 0.0, slot);
    }

    /**
     * Mutable four-slot view matching the old PlayerInventory armor order:
     * boots, leggings, chestplate, helmet.
     */
    public static List<ItemStack> armorItems(LivingEntity entity) {
        return new AbstractList<>() {
            @Override
            public ItemStack get(int index) {
                return entity.getEquippedStack(LEGACY_ARMOR_ORDER[index]);
            }

            @Override
            public ItemStack set(int index, ItemStack stack) {
                EquipmentSlot slot = LEGACY_ARMOR_ORDER[index];
                ItemStack previous = entity.getEquippedStack(slot);
                entity.equipStack(slot, stack);
                return previous;
            }

            @Override
            public int size() {
                return LEGACY_ARMOR_ORDER.length;
            }
        };
    }

    public static List<ItemStack> armorItems(PlayerInventory inventory) {
        return armorItems(inventory.player);
    }

    public static ItemStack armorItem(PlayerInventory inventory, int legacyIndex) {
        return armorItems(inventory).get(legacyIndex);
    }

    public static NbtCompound toNbt(ItemStack stack, RegistryWrapper.WrapperLookup registries) {
        NbtElement encoded = ItemStack.CODEC
            .encodeStart(registries.getOps(NbtOps.INSTANCE), stack)
            .getOrThrow();
        if (encoded instanceof NbtCompound compound) {
            return compound;
        }
        throw new IllegalStateException("Item stack did not encode to an NBT compound");
    }

    public static NbtCompound toNbtAllowEmpty(ItemStack stack, RegistryWrapper.WrapperLookup registries) {
        NbtElement encoded = ItemStack.OPTIONAL_CODEC
            .encodeStart(registries.getOps(NbtOps.INSTANCE), stack)
            .getOrThrow();
        return encoded instanceof NbtCompound compound ? compound : new NbtCompound();
    }

    public static ItemStack fromNbtOrEmpty(RegistryWrapper.WrapperLookup registries, NbtCompound nbt) {
        return ItemStack.OPTIONAL_CODEC
            .parse(registries.getOps(NbtOps.INSTANCE), nbt)
            .result()
            .orElse(ItemStack.EMPTY);
    }

    public static ItemStack fromNbtOrEmpty(RegistryWrapper.WrapperLookup registries, Optional<NbtCompound> nbt) {
        return nbt.map(value -> fromNbtOrEmpty(registries, value)).orElse(ItemStack.EMPTY);
    }
}
