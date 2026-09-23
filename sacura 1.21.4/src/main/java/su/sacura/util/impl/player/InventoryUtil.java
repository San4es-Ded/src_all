package su.sacura.util.impl.player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import su.sacura.util.type.MinecraftWrapper;

public class InventoryUtil implements MinecraftWrapper {
    public static void moveItemTest(int from, int to, boolean air) {
        if (from == to) return;
        testPick(from, 0);
        testPick(to, 0);
        if (air) testPick(from, 0);
    }

    public static void testPick(int slot, int button) {
        mc.interactionManager.clickSlot(0, slot, button, SlotActionType.PICKUP, (PlayerEntity)mc.player);
    }

    public static boolean isPotionActive(RegistryEntry<StatusEffect> statusEffect) {
        return mc.player.getActiveStatusEffects().containsKey(statusEffect);
    }

    public static void moveToOffhand(Item item) {
        if (mc.player == null || mc.interactionManager == null) return;
        PlayerInventory inventory = mc.player.getInventory();
        if (inventory.offHand.get(0).getItem() == item) return;
        int i;
        for (i = 0; i < 9; i++) {
            if (inventory.getStack(i).getItem() == item) {
                mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, i + 36, 40, SlotActionType.SWAP, (PlayerEntity)mc.player);
                return;
            }
        }
        for (i = 9; i < 36; i++) {
            if (inventory.getStack(i).getItem() == item) {
                mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, i, 40, SlotActionType.SWAP, (PlayerEntity)mc.player);
                return;
            }
        }
    }

    public static Stream<Slot> slots() {
        List<Slot> list = new ArrayList<>();
        int size = mc.player.currentScreenHandler.slots.size();
        for (int i = 0; i < size; i++) {
            list.add(mc.player.currentScreenHandler.slots.get(i));
        }
        return list.stream();
    }

    public static Slot getPotion(RegistryEntry<StatusEffect> effect) {
        return slots().filter(s -> {
            PotionContentsComponent component = s.getStack().get(DataComponentTypes.POTION_CONTENTS);
            if (component == null) return false;
            return StreamSupport.stream(component.getEffects().spliterator(), false)
                    .anyMatch(inst -> inst.getEffectType().equals(effect));
        }).findFirst().orElse(null);
    }

    public static Slot getFoodMaxSaturationSlot() {
        return slots().filter(s -> (s.getStack().get(DataComponentTypes.FOOD) != null
                        && !s.getStack().get(DataComponentTypes.FOOD).canAlwaysEat()))
                .max(Comparator.comparingDouble(s -> s.getStack().get(DataComponentTypes.FOOD).saturation())).orElse(null);
    }

    public static void inventorySwapClick2(Item item, boolean useFromInventory, boolean rotation) {
        int currentSlot = mc.player.getInventory().selectedSlot;
        if (mc.player.isSneaking() && !mc.player.getMainHandStack().isOf(Items.BOW) && mc.player.getActiveHand() == Hand.MAIN_HAND) {
            for (int j = 0; j < 9; j++) {
                if (mc.player.getInventory().getStack(j).getItem() == item) {
                    swapSlotsUniversal(j, 40, false, true);
                    if (rotation)
                        mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(mc.player.getYaw(), mc.player.getPitch(), mc.player.isOnGround(), false));
                    mc.player.networkHandler.sendPacket(new PlayerInteractItemC2SPacket(Hand.OFF_HAND, 0, mc.player.getYaw(), mc.player.getPitch()));
                    swapSlotsUniversal(j, 40, false, true);
                    return;
                }
            }
            return;
        }
        int i;
        for (i = 0; i < 9; i++) {
            if (mc.player.getInventory().getStack(i).getItem() == item) {
                if (i != currentSlot) mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(i));
                if (rotation) mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(mc.player.getYaw(), mc.player.getPitch(), mc.player.isOnGround(), false));
                mc.player.networkHandler.sendPacket(new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, 0, mc.player.getYaw(), mc.player.getPitch()));
                if (i != currentSlot) mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(currentSlot));
                return;
            }
        }
        if (useFromInventory)
            for (i = 9; i < 36; i++) {
                if (mc.player.getInventory().getStack(i).getItem() == item) {
                    int nextSlot = (currentSlot + 1) % 9;
                    swapSlotsUniversal(i, nextSlot, false, true);
                    mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(nextSlot));
                    if (rotation) mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(mc.player.getYaw(), mc.player.getPitch(), mc.player.isOnGround(), false));
                    mc.player.networkHandler.sendPacket(new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, 0, mc.player.getYaw(), mc.player.getPitch()));
                    mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(currentSlot));
                    swapSlotsUniversal(i, nextSlot, false, true);
                    return;
                }
            }
    }

    public static int getItem(Class<?> itemClass, boolean hotBarOnly) {
        PlayerInventory inventory = mc.player.getInventory();
        int startSlot = hotBarOnly ? 0 : 9;
        int endSlot = hotBarOnly ? 9 : inventory.size();
        for (int i = startSlot; i < endSlot; i++) {
            if (i < inventory.size()) {
                ItemStack itemStack = inventory.getStack(i);
                if (!itemStack.isEmpty() && itemClass.isInstance(itemStack.getItem())) return i;
            }
        }
        return -1;
    }

    public static int getPearls() {
        for (int i = 0; i < 9; i++) {
            if (mc.player.getInventory().getStack(i).getItem() instanceof net.minecraft.item.EnderPearlItem) return i;
        }
        return -1;
    }

    public static void swapSlotsUniversal(int slot1, int slot2, boolean cursor, boolean conversion) {
        if (cursor) {
            mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, slot1, 0, SlotActionType.PICKUP, (PlayerEntity)mc.player);
            mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, slot2, 0, SlotActionType.PICKUP, (PlayerEntity)mc.player);
            mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, slot1, 0, SlotActionType.PICKUP, (PlayerEntity)mc.player);
        } else if (slot1 < 9 && conversion) {
            mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, slot1 + 36, slot2, SlotActionType.SWAP, (PlayerEntity)mc.player);
        } else {
            mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, slot1, slot2, SlotActionType.SWAP, (PlayerEntity)mc.player);
        }
    }

    public static void swapSlots(int slot1, int slot2) {
        if (slot1 < 9 && slot2 < 9) {
            mc.interactionManager.clickSlot(0, slot1 + 36, slot2, SlotActionType.SWAP, (PlayerEntity)mc.player);
        } else if (slot1 < 9) {
            mc.interactionManager.clickSlot(0, slot2, slot1, SlotActionType.SWAP, (PlayerEntity)mc.player);
        } else if (slot2 < 9) {
            mc.interactionManager.clickSlot(0, slot1, slot2, SlotActionType.SWAP, (PlayerEntity)mc.player);
        }
    }

    public static void moveItem(int from, int to, boolean air) {
        if (from != to) {
            pickupItem(from, 0);
            pickupItem(to, 0);
            if (air) pickupItem(from, 0);
        }
    }

    public static void pickupItem(int slot, int button) {
        mc.interactionManager.clickSlot(0, slot, button, SlotActionType.PICKUP, (PlayerEntity)mc.player);
    }

    public static int getHotBarSlot(Item input) {
        for (int i = 0; i < 9; i++) {
            if (mc.player.getInventory().getStack(i).getItem() == input) return i;
        }
        return -1;
    }

    public static boolean doesHotbarHaveItem(Item item) {
        for (int i = 0; i < 9; i++) {
            if (mc.player.getInventory().getStack(i).getItem() == item) return true;
        }
        return false;
    }

    public static int getItemIndex(Item item) {
        for (int i = 0; i < 45; i++) {
            if (mc.player.getInventory().getStack(i).getItem() == item) return i;
        }
        return -1;
    }

    public static void windowClick(int conteinerId, int slot, int mouse, SlotActionType type, PlayerEntity player) {
        mc.interactionManager.clickSlot(conteinerId, slot, mouse, type, player);
    }

    public static void startFly() {
        mc.player.dropSelectedItem(true);
        mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket((Entity)mc.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
    }

    public static void use(int hotbarSlot, int inventorySlot, boolean useFromInventory) {
        int currentItem = mc.player.getInventory().selectedSlot;
        if (hotbarSlot != -1) {
            mc.player.getInventory().selectedSlot = hotbarSlot;
            mc.interactionManager.interactItem((PlayerEntity)mc.player, Hand.MAIN_HAND);
            mc.player.getInventory().selectedSlot = currentItem;
        } else if (useFromInventory && inventorySlot != -1) {
            windowClick(0, inventorySlot, mc.player.getInventory().selectedSlot, SlotActionType.SWAP, (PlayerEntity)mc.player);
            mc.interactionManager.interactItem((PlayerEntity)mc.player, Hand.MAIN_HAND);
            windowClick(0, inventorySlot, mc.player.getInventory().selectedSlot, SlotActionType.SWAP, (PlayerEntity)mc.player);
            mc.player.getInventory().updateItems();
        }
    }

    public static class TotemUtil {
        public static BlockPos getBlock(float distance, Block block) {
            return getSphere(getPlayerPosLocal(), distance, 6, false, true, 0).stream()
                    .filter(position -> (MinecraftWrapper.mc.world.getBlockState(position).isOf(block)))
                    .min(Comparator.comparing(blockPos -> Double.valueOf(getDistanceOfEntityToBlock((Entity)MinecraftWrapper.mc.player, blockPos))))
                    .orElse(null);
        }

        public static BlockPos getBlock(float distance) {
            return getSphere(getPlayerPosLocal(), distance, 6, false, true, 0).stream()
                    .filter(position -> (!MinecraftWrapper.mc.world.getBlockState(position).isOf(Blocks.AIR)))
                    .min(Comparator.comparing(blockPos -> Double.valueOf(getDistanceOfEntityToBlock((Entity)MinecraftWrapper.mc.player, blockPos))))
                    .orElse(null);
        }

        public static List<BlockPos> getSphere(BlockPos blockPos, float n, int n2, boolean b, boolean b2, int n3) {
            ArrayList<BlockPos> list = new ArrayList<>();
            int x = blockPos.getX(), y = blockPos.getY(), z = blockPos.getZ();
            for (int n4 = x - (int)n; n4 <= x + n; n4++) {
                for (int n5 = z - (int)n; n5 <= z + n; ) {
                    int n6 = b2 ? (y - (int)n) : y;
                    for (;; n5++) {
                        if (n6 < (b2 ? (y + n) : (y + n2))) {
                            double n7 = ((x - n4) * (x - n4) + (z - n5) * (z - n5) + (b2 ? ((y - n6) * (y - n6)) : 0));
                            if (n7 < (n * n) && (!b || n7 >= ((n - 1.0F) * (n - 1.0F)))) list.add(new BlockPos(n4, n6 + n3, n5));
                            n6++; continue;
                        }
                    }
                }
            }
            return list;
        }

        public static BlockPos getPlayerPosLocal() {
            if (MinecraftWrapper.mc.player == null) return BlockPos.ORIGIN;
            return new BlockPos((int)Math.floor(MinecraftWrapper.mc.player.getX()), (int)Math.floor(MinecraftWrapper.mc.player.getY()), (int)Math.floor(MinecraftWrapper.mc.player.getZ()));
        }

        public static double getDistanceOfEntityToBlock(Entity entity, BlockPos blockPos) {
            return getDistance(entity.getX(), entity.getY(), entity.getZ(), blockPos.getX(), blockPos.getY(), blockPos.getZ());
        }

        public static double getDistance(double n, double n2, double n3, double n4, double n5, double n6) {
            double n7 = n - n4, n8 = n2 - n5, n9 = n3 - n6;
            return MathHelper.sqrt((float)(n7 * n7 + n8 * n8 + n9 * n9));
        }
    }
}