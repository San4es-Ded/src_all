package rockstar.client.internal.inventory;


import rockstar.client.*;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import org.jetbrains.annotations.Nullable;

public final class BestToolSelector {
   private BestToolSelector() {
   }

   @Nullable
   public static BestToolSelector.InternalType0256 internalMethod06939(BlockState localValue0) {
      MinecraftClient localValue1 = MinecraftClient.getInstance();
      ClientPlayerEntity localValue2 = localValue1.player;
      if (localValue2 == null) {
         return null;
      } else {
         PlayerInventory localValue3 = localValue2.getInventory();
         int localValue4 = -1;
         float localValue5 = 0.0F;
         boolean localValue6 = false;

         for (int localValue7 = 0; localValue7 < 9; localValue7++) {
            ItemStack localValue8 = localValue3.getStack(localValue7);
            if (!localValue8.isEmpty()) {
               float localValue9 = internalMethod00662(localValue8, localValue0);
               if (localValue9 > localValue5) {
                  localValue5 = localValue9;
                  localValue4 = localValue7;
                  localValue6 = true;
               }
            }
         }

         for (int localValue10 = 9; localValue10 < 36; localValue10++) {
            ItemStack localValue11 = localValue3.getStack(localValue10);
            if (!localValue11.isEmpty()) {
               float localValue12 = internalMethod00662(localValue11, localValue0);
               if (localValue12 > localValue5) {
                  localValue5 = localValue12;
                  localValue4 = localValue10;
                  localValue6 = false;
               }
            }
         }

         return localValue4 < 0 ? null : new BestToolSelector.InternalType0256(localValue4, localValue6, localValue5);
      }
   }

   public static float internalMethod00662(ItemStack localValue0, BlockState localValue1) {
      return localValue0 != null && !localValue0.isEmpty() ? localValue0.getMiningSpeedMultiplier(localValue1) : 1.0F;
   }

   public static boolean internalMethod04395(BlockState localValue0) {
      MinecraftClient localValue1 = MinecraftClient.getInstance();
      ClientPlayerEntity localValue2 = localValue1.player;
      if (localValue2 != null && localValue1.interactionManager != null) {
         BestToolSelector.InternalType0256 localValue3 = internalMethod06939(localValue0);
         if (localValue3 == null) {
            return false;
         } else {
            ItemStack localValue4 = localValue2.getMainHandStack();
            float localValue5 = internalMethod00662(localValue4, localValue0);
            if (localValue3.internalField0205 <= localValue5) {
               return false;
            } else if (localValue3.internalField0277) {
               int localValue7 = localValue2.getInventory().getSelectedSlot();
               if (localValue3.internalField0227 == localValue7) {
                  return false;
               } else {
                  internalMethod00041(localValue3.internalField0227);
                  return true;
               }
            } else {
               int localValue6 = localValue2.getInventory().getSelectedSlot();
               localValue1.interactionManager.clickSlot(localValue2.currentScreenHandler.syncId, localValue3.internalField0227, localValue6, SlotActionType.SWAP, localValue2);
               return true;
            }
         }
      } else {
         return false;
      }
   }

   public static void internalMethod00041(int localValue0) {
      MinecraftClient localValue1 = MinecraftClient.getInstance();
      if (localValue1.player != null) {
         if (localValue0 >= 0 && localValue0 <= 8) {
            if (localValue1.player.getInventory().getSelectedSlot() != localValue0) {
               localValue1.player.getInventory().setSelectedSlot(localValue0);
               localValue1.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(localValue0));
            }
         }
      }
   }

   public static final class InternalType0256 {
      final int internalField0227;
      final boolean internalField0277;
      final float internalField0205;

      public InternalType0256(int localValue1, boolean localValue2, float localValue3) {
         this.internalField0227 = localValue1;
         this.internalField0277 = localValue2;
         this.internalField0205 = localValue3;
      }

      @Override
      public final String toString() {
         return "InternalType0256[slotIdx=" + this.internalField0227 + ", inHotbar=" + this.internalField0277 + ", speed=" + this.internalField0205 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0277);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0205);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         BestToolSelector.InternalType0256 other = (BestToolSelector.InternalType0256) localValue1;
         return java.util.Objects.equals(this.internalField0227, other.internalField0227)
            && java.util.Objects.equals(this.internalField0277, other.internalField0277)
            && java.util.Objects.equals(this.internalField0205, other.internalField0205);
      }

      public int internalMethod07557() {
         return this.internalField0227;
      }

      public boolean internalMethod07558() {
         return this.internalField0277;
      }

      public float internalMethod07556() {
         return this.internalField0205;
      }
   }
}
