package rockstar.client.internal.game;



import rockstar.client.inventory.*;
import rockstar.client.*;
import lombok.Generated;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.inventory.HotbarSlot;

public final class HotbarSlotSwitcher
implements MinecraftClientAccess {
    public static void internalMethod07593(HotbarSlot typedValue231, Runnable runnable) {
        if (typedValue231 == null || runnable == null || HotbarSlotSwitcher.internalField0149.player == null || internalField0149.getNetworkHandler() == null) {
            return;
        }
        int n = HotbarSlotSwitcher.internalField0149.player.getInventory().getSelectedSlot();
        if (typedValue231.internalMethod08745() == n) {
            runnable.run();
            return;
        }
        internalField0149.getNetworkHandler().sendPacket((Packet)new UpdateSelectedSlotC2SPacket(typedValue231.internalMethod08745()));
        try {
            runnable.run();
        }
        finally {
            internalField0149.getNetworkHandler().sendPacket((Packet)new UpdateSelectedSlotC2SPacket(n));
        }
    }

    @Generated
    private HotbarSlotSwitcher() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

