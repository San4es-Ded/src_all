package rockstar.client.inventory;


import rockstar.client.*;
import java.util.function.Predicate;
import lombok.Generated;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import org.jetbrains.annotations.NotNull;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.inventory.InventorySlot;
import rockstar.client.inventory.HotbarSlots;
import rockstar.client.inventory.ArmorSlot;
import rockstar.client.inventory.HotbarSlot;
import rockstar.client.inventory.MainInventorySlot;
import rockstar.client.inventory.OffhandSlot;

public final class InventoryUtils
implements MinecraftClientAccess {
    public static HotbarSlot internalMethod02738(int n) {
        return new HotbarSlot(n);
    }

    public static MainInventorySlot internalMethod02743(int n) {
        return new MainInventorySlot(n);
    }

    public static ArmorSlot internalMethod02737(int n) {
        return new ArmorSlot(n);
    }

    public static ArmorSlot internalMethod06159() {
        return InventoryUtils.internalMethod02737(3);
    }

    public static ArmorSlot internalMethod06826() {
        return InventoryUtils.internalMethod02737(2);
    }

    public static ArmorSlot internalMethod07877() {
        return InventoryUtils.internalMethod02737(1);
    }

    public static ArmorSlot internalMethod07992() {
        return InventoryUtils.internalMethod02737(0);
    }

    public static OffhandSlot internalMethod06162() {
        return new OffhandSlot();
    }

    public static boolean internalMethod06573(Item item) {
        return InventoryUtils.internalMethod06162().internalMethod03381(item);
    }

    public static boolean internalMethod02721(Predicate<ItemStack> predicate) {
        return InventoryUtils.internalMethod06162().internalMethod07038(predicate);
    }

    public static boolean internalMethod04983() {
        return InventoryUtils.internalMethod06162().internalMethod06664();
    }

    public static void internalMethod01016(InventorySlot typedValue222, InventorySlot typedValue223) {
        if (internalField0149.getNetworkHandler() == null) {
            return;
        }
        typedValue222.internalMethod06668();
        typedValue223.internalMethod06668();
        if (!typedValue223.internalMethod06664()) {
            typedValue222.internalMethod06668();
        }
        internalField0149.getNetworkHandler().sendPacket((Packet)new CloseHandledScreenC2SPacket(0));
    }

    public static void internalMethod03592(int n) {
        if (internalField0149.getNetworkHandler() == null) {
            return;
        }
        InventoryUtils.internalField0149.interactionManager.clickSlot(InventoryUtils.internalField0149.player.currentScreenHandler.syncId, n, 0, SlotActionType.QUICK_MOVE, (PlayerEntity)InventoryUtils.internalField0149.player);
    }

    public static void internalMethod03980(int n, int n2) {
        InventoryUtils.internalMethod07228(n, n2, false);
    }

    public static void internalMethod07228(int n, int n2, boolean bl) {
        if (internalField0149.getNetworkHandler() == null) {
            return;
        }
        InventoryUtils.internalField0149.interactionManager.clickSlot(InventoryUtils.internalField0149.player.currentScreenHandler.syncId, n, 0, SlotActionType.PICKUP, (PlayerEntity)InventoryUtils.internalField0149.player);
        InventoryUtils.internalField0149.interactionManager.clickSlot(InventoryUtils.internalField0149.player.currentScreenHandler.syncId, n2, 0, SlotActionType.PICKUP, (PlayerEntity)InventoryUtils.internalField0149.player);
        if (bl) {
            InventoryUtils.internalField0149.interactionManager.clickSlot(InventoryUtils.internalField0149.player.currentScreenHandler.syncId, n, 0, SlotActionType.PICKUP, (PlayerEntity)InventoryUtils.internalField0149.player);
        }
    }

    public static void internalMethod05750(int n, int n2) {
        InventoryUtils.internalField0149.interactionManager.clickSlot(InventoryUtils.internalField0149.player.playerScreenHandler.syncId, InventoryUtils.internalMethod03591(n), 0, SlotActionType.PICKUP, (PlayerEntity)InventoryUtils.internalField0149.player);
        InventoryUtils.internalField0149.interactionManager.clickSlot(InventoryUtils.internalField0149.player.playerScreenHandler.syncId, InventoryUtils.internalMethod03591(n2), 0, SlotActionType.PICKUP, (PlayerEntity)InventoryUtils.internalField0149.player);
        InventoryUtils.internalField0149.interactionManager.clickSlot(InventoryUtils.internalField0149.player.playerScreenHandler.syncId, InventoryUtils.internalMethod03591(n), 0, SlotActionType.PICKUP, (PlayerEntity)InventoryUtils.internalField0149.player);
    }

    public static void internalMethod07879(int n, int n2) {
        if (internalField0149.getNetworkHandler() == null) {
            return;
        }
        InventoryUtils.internalField0149.interactionManager.clickSlot(InventoryUtils.internalField0149.player.currentScreenHandler.syncId, n, 1, SlotActionType.PICKUP, (PlayerEntity)InventoryUtils.internalField0149.player);
        InventoryUtils.internalField0149.interactionManager.clickSlot(InventoryUtils.internalField0149.player.currentScreenHandler.syncId, n2, 0, SlotActionType.PICKUP, (PlayerEntity)InventoryUtils.internalField0149.player);
    }

    public static void internalMethod08185(int n, int n2) {
        if (internalField0149.getNetworkHandler() == null) {
            return;
        }
        InventoryUtils.internalField0149.interactionManager.clickSlot(InventoryUtils.internalField0149.player.currentScreenHandler.syncId, n, 0, SlotActionType.PICKUP, (PlayerEntity)InventoryUtils.internalField0149.player);
        InventoryUtils.internalField0149.interactionManager.clickSlot(InventoryUtils.internalField0149.player.currentScreenHandler.syncId, n2, 1, SlotActionType.PICKUP, (PlayerEntity)InventoryUtils.internalField0149.player);
        InventoryUtils.internalField0149.interactionManager.clickSlot(InventoryUtils.internalField0149.player.currentScreenHandler.syncId, n, 0, SlotActionType.PICKUP, (PlayerEntity)InventoryUtils.internalField0149.player);
    }

    public static void internalMethod08821(int n, int n2) {
        if (internalField0149.getNetworkHandler() == null) {
            return;
        }
        InventoryUtils.internalField0149.interactionManager.clickSlot(InventoryUtils.internalField0149.player.currentScreenHandler.syncId, n, n2, SlotActionType.SWAP, (PlayerEntity)InventoryUtils.internalField0149.player);
    }

    public static boolean internalMethod05222(InventorySlot typedValue222, int n) {
        HotbarSlot typedValue231 = InventoryUtils.internalMethod02738(n);
        InventoryUtils.internalMethod01016(typedValue222, typedValue231);
        return true;
    }

    public static boolean internalMethod05406(InventorySlot typedValue222, int n) {
        ArmorSlot typedValue230 = InventoryUtils.internalMethod02737(n);
        InventoryUtils.internalMethod01016(typedValue222, typedValue230);
        return true;
    }

    public static void internalMethod02096(InventorySlot typedValue222) {
        OffhandSlot typedValue234 = InventoryUtils.internalMethod06162();
        InventoryUtils.internalMethod01016(typedValue222, typedValue234);
    }

    @NotNull
    public static HotbarSlot internalMethod06160() {
        if (InventoryUtils.internalField0149.player == null || InventoryUtils.internalField0149.player.getInventory() == null) {
            return new HotbarSlot(0);
        }
        return InventoryUtils.internalMethod02738(InventoryUtils.internalField0149.player.getInventory().getSelectedSlot());
    }

    public static void internalMethod03663(int n) {
        if (InventoryUtils.internalField0149.player == null || InventoryUtils.internalField0149.player.getInventory() == null || internalField0149.getNetworkHandler() == null || InventoryUtils.internalMethod06160().internalMethod08745() == n) {
            return;
        }
        if (n < 0 || n > 8) {
            throw new IllegalArgumentException("Hotbar slot ID must be between 0 and 8");
        }
        InventoryUtils.internalField0149.player.getInventory().setSelectedSlot(n);
    }

    public static void internalMethod08457(int n) {
        if (InventoryUtils.internalField0149.player == null || InventoryUtils.internalField0149.player.getInventory() == null || internalField0149.getNetworkHandler() == null || InventoryUtils.internalMethod06160().internalMethod08745() == n) {
            return;
        }
        if (n < 0 || n > 8) {
            throw new IllegalArgumentException("Hotbar slot ID must be between 0 and 8");
        }
        internalField0149.getNetworkHandler().sendPacket((Packet)new UpdateSelectedSlotC2SPacket(InventoryUtils.internalField0149.player.getInventory().getSelectedSlot()));
    }

    public static void internalMethod01980(HotbarSlot typedValue231) {
        InventoryUtils.internalMethod03663(typedValue231.internalMethod08745());
    }

    public static boolean internalMethod03250(Item item) {
        HotbarSlot typedValue231 = (HotbarSlot)new HotbarSlots().internalMethod02510(item);
        if (typedValue231 != null) {
            InventoryUtils.internalMethod01980(typedValue231);
            return true;
        }
        return false;
    }

    public static int internalMethod02720(Predicate<ItemStack> predicate) {
        if (InventoryUtils.internalField0149.player == null || InventoryUtils.internalField0149.player.currentScreenHandler == null) {
            return -1;
        }
        for (int i = 0; i < InventoryUtils.internalField0149.player.currentScreenHandler.slots.size(); ++i) {
            ItemStack itemStack = InventoryUtils.internalField0149.player.currentScreenHandler.getSlot(i).getStack();
            if (!predicate.test(itemStack)) continue;
            return i;
        }
        return -1;
    }

    public static int internalMethod06572(Item item) {
        return InventoryUtils.internalMethod02720(itemStack -> itemStack.getItem() == item);
    }

    private static int internalMethod03591(int n) {
        if (n >= 0 && n <= 8) {
            return 36 + n;
        }
        return n;
    }

    @Generated
    private InventoryUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

