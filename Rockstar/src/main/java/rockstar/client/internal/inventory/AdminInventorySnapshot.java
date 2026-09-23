package rockstar.client.internal.inventory;


import rockstar.client.*;
import rockstar.client.util.LegacyItemTypes;
import globals.shared.proto.Packets;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtSizeTracker;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.SlotActionType;

public final class AdminInventorySnapshot {
   public static final int internalField0227 = 46;
   public static final int internalField0228 = -999;
   private final String internalField0248;
   private final ItemStack[] internalField0299;
   private final ItemStack internalField0878;

   private AdminInventorySnapshot(String localValue1, ItemStack[] localValue2, ItemStack localValue3) {
      this.internalField0248 = localValue1;
      this.internalField0299 = localValue2;
      this.internalField0878 = localValue3;
   }

   public String internalMethod05975() {
      return this.internalField0248;
   }

   public ItemStack internalMethod01242(int localValue1) {
      return localValue1 >= 0 && localValue1 < this.internalField0299.length ? this.internalField0299[localValue1] : ItemStack.EMPTY;
   }

   public ItemStack internalMethod03217() {
      return this.internalField0878;
   }

   public static void internalMethod07337(Packets.InternalType0116 localValue0) {
      MinecraftClient localValue1 = MinecraftClient.getInstance();
      localValue1.execute(
         () -> {
            try {
               if (localValue1.player == null || localValue1.world == null || localValue1.interactionManager == null) {
                  return;
               }

               if (!localValue0.action().isBlank()) {
                  internalMethod00460(localValue1, localValue0);
               }

               internalMethod06692(localValue1);
            } catch (Throwable localValue3) {
               RockstarClient.internalField0572
                  .error("[Admin] invsee: \u0437\u0430\u043f\u0440\u043e\u0441 \u043d\u0435 \u0432\u044b\u043f\u043e\u043b\u043d\u0435\u043d", localValue3);
            }
         }
      );
   }

   private static void internalMethod00460(MinecraftClient localValue0, Packets.InternalType0116 localValue1) {
      SlotActionType localValue2;
      try {
         localValue2 = SlotActionType.valueOf(localValue1.action());
      } catch (IllegalArgumentException localValue5) {
         return;
      }

      PlayerScreenHandler localValue3 = localValue0.player.playerScreenHandler;
      int localValue4 = localValue1.slot();
      if (localValue4 == -999 || localValue4 >= 0 && localValue4 < localValue3.slots.size()) {
         if (localValue0.player.currentScreenHandler != localValue3) {
            localValue0.player.closeHandledScreen();
         }

         localValue0.interactionManager.clickSlot(localValue3.syncId, localValue4, localValue1.button(), localValue2, localValue0.player);
      }
   }

   private static void internalMethod06692(MinecraftClient localValue0) throws Exception {
      PlayerScreenHandler localValue1 = localValue0.player.playerScreenHandler;
      DynamicRegistryManager localValue2 = localValue0.world.getRegistryManager();
      NbtCompound localValue3 = new NbtCompound();
      localValue3.putString("nick", localValue0.player.getGameProfile().name());
      NbtList localValue4 = new NbtList();

      for (int localValue5 = 0; localValue5 < 46; localValue5++) {
         ItemStack localValue6 = localValue5 < localValue1.slots.size() ? localValue1.getSlot(localValue5).getStack() : ItemStack.EMPTY;
         localValue4.add(LegacyItemTypes.toNbtAllowEmpty(localValue6, localValue2));
      }

      localValue3.put("slots", localValue4);
      localValue3.put("cursor", LegacyItemTypes.toNbtAllowEmpty(localValue1.getCursorStack(), localValue2));
      ByteArrayOutputStream localValue7 = new ByteArrayOutputStream(8192);
      NbtIo.writeCompressed(localValue3, localValue7);
      RockstarClient.getInstance().internalMethod06050().send(new Packets.InternalType0499(Base64.getEncoder().encodeToString(localValue7.toByteArray())));
   }

   public static AdminInventorySnapshot internalMethod06909(Packets.InternalType0498 localValue0) {
      MinecraftClient localValue1 = MinecraftClient.getInstance();
      if (localValue1.world != null && localValue0.data() != null && !localValue0.data().isBlank()) {
         try {
            byte[] localValue2 = Base64.getDecoder().decode(localValue0.data());

            NbtCompound localValue3;
            try (ByteArrayInputStream localValue4 = new ByteArrayInputStream(localValue2)) {
               localValue3 = NbtIo.readCompressed(localValue4, NbtSizeTracker.of(8388608L));
            }

            DynamicRegistryManager localValue12 = localValue1.world.getRegistryManager();
            NbtList localValue5 = localValue3.getList("slots").orElseGet(net.minecraft.nbt.NbtList::new);
            ItemStack[] localValue6 = new ItemStack[46];

            for (int localValue7 = 0; localValue7 < 46; localValue7++) {
               localValue6[localValue7] = localValue7 < localValue5.size() ? LegacyItemTypes.fromNbtOrEmpty(localValue12, localValue5.getCompound(localValue7)) : ItemStack.EMPTY;
            }

            ItemStack localValue13 = LegacyItemTypes.fromNbtOrEmpty(localValue12, localValue3.getCompound("cursor").orElseGet(net.minecraft.nbt.NbtCompound::new));
            String localValue8 = localValue3.getString("nick").orElse("");
            if (localValue8.isBlank()) {
               localValue8 = localValue0.nickname();
            }

            return new AdminInventorySnapshot(localValue8, localValue6, localValue13);
         } catch (Throwable localValue11) {
            RockstarClient.internalField0572
               .error(
                  "[Admin] invsee: \u0441\u043d\u0438\u043c\u043e\u043a \u0438\u043d\u0432\u0435\u043d\u0442\u0430\u0440\u044f \u043d\u0435 \u0440\u0430\u0437\u043e\u0431\u0440\u0430\u043d",
                  localValue11
               );
            return null;
         }
      } else {
         return null;
      }
   }
}
