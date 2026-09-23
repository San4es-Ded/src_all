package rockstar.client.internal.script;






import rockstar.client.server.*;
import rockstar.client.event.*;
import rockstar.client.core.*;
import rockstar.client.command.*;
import rockstar.client.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ItemPickupAnimationS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.world.GameMode;
import pyrock.events.network.ReceivePacketEvent;
import pyrock.events.render.HudRenderEvent;

public class InvseeCommandHandler implements MinecraftClientAccess {
   private PlayerEntity internalField0457;
   private final Map<UUID, InvseeCommandHandler.InternalType0013> internalField0543 = new HashMap<>();
   private final EventListener<HudRenderEvent> internalField0157 = localValue1 -> {
      if (internalField0149.world != null) {
         for (AbstractClientPlayerEntity localValue3 : internalField0149.world.getPlayers()) {
            this.internalMethod02219(localValue3);
         }
      }

      if (this.internalField0457 != null) {
         this.internalMethod00732(this.internalField0457);
         this.internalField0457 = null;
      }
   };
   private final EventListener<ReceivePacketEvent> internalField0158 = localValue1 -> {
      if (internalField0149.world != null) {
         if (localValue1.getPacket() instanceof ItemPickupAnimationS2CPacket localValue2) {
            if (internalField0149.world.getEntityById(localValue2.getCollectorEntityId()) instanceof PlayerEntity localValue4) {
               if (internalField0149.world.getEntityById(localValue2.getEntityId()) instanceof ItemEntity localValue6) {
                  ItemStack localValue7 = localValue6.getStack();
                  if (!localValue7.isEmpty()) {
                     this.internalField0543.computeIfAbsent(localValue4.getUuid(), localValue1x -> new InvseeCommandHandler.InternalType0013(localValue4)).internalMethod04046(localValue7);
                  }
               }
            }
         }
      }
   };

   public InvseeCommandHandler() {
      RockstarClient.getInstance().internalMethod03317().internalMethod00647(this);
   }

   public CommandNode internalMethod07426() {
      return CommandBuilder.internalMethod00593("invsee")
         .internalMethod05325("\u0438\u043d\u0432\u0441\u0438", "seeinv")
         .internalMethod06148("commands.invsee.description")
         .internalMethod01539("player", localValue0 -> localValue0.internalMethod00776(OperationResult::internalMethod00116))
         .internalMethod00262(this::internalMethod06242)
         .internalMethod04146();
   }

   private void internalMethod06242(ParsedCommand localValue1) {
      if (internalField0149.world != null && internalField0149.player != null) {
         String localValue2 = (String)localValue1.internalMethod02266().getFirst();
         if (localValue2 != null && !localValue2.trim().isEmpty()) {
            localValue2 = localValue2.trim();
            String localValue3 = localValue2.toLowerCase(Locale.ROOT);
            AbstractClientPlayerEntity localValue4 = null;

            for (AbstractClientPlayerEntity localValue6 : internalField0149.world.getPlayers()) {
               String localValue7 = localValue6.getName().getString();
               if (localValue7.equalsIgnoreCase(localValue2)) {
                  localValue4 = localValue6;
                  break;
               }

               if (localValue4 == null && localValue7.toLowerCase(Locale.ROOT).startsWith(localValue3)) {
                  localValue4 = localValue6;
               }
            }

            if (localValue4 != null) {
               this.internalField0457 = localValue4;
            }
         }
      }
   }

   private void internalMethod00732(PlayerEntity localValue1) {
      InvseeCommandHandler.InternalType0013 localValue2 = this.internalField0543.get(localValue1.getUuid());
      if (localValue2 == null) {
         localValue2 = new InvseeCommandHandler.InternalType0013(localValue1);
         localValue2.internalMethod00371(localValue1);
         this.internalField0543.put(localValue1.getUuid(), localValue2);
      }

      boolean localValue3 = ServerUtils.internalMethod01786(KnownServer.internalField1218);
      AbstractClientPlayerEntity localValue4 = new AbstractClientPlayerEntity(internalField0149.world, localValue1.getGameProfile()) {
         public boolean isSpectator() {
            return false;
         }

         public boolean isCreative() {
            return MinecraftClientAccess.internalField0149.interactionManager != null
               && MinecraftClientAccess.internalField0149.interactionManager.getCurrentGameMode() == GameMode.CREATIVE;
         }
      };
      List localValue5 = localValue2.internalMethod07606();
      int localValue6 = localValue4.getInventory().getMainStacks().size();

      for (int localValue7 = 0; localValue7 < localValue6; localValue7++) {
         if (localValue7 < localValue5.size()) {
            ItemStack localValue8 = ((ItemStack)localValue5.get(localValue7)).copy();
            if (localValue3 && localValue7 == 0) {
               localValue8.setCount(1);
            }

            localValue4.getInventory().getMainStacks().set(localValue7, localValue8);
         } else {
            localValue4.getInventory().getMainStacks().set(localValue7, ItemStack.EMPTY);
         }
      }

      for (int localValue10 = 0; localValue10 < rockstar.client.util.LegacyItemTypes.armorItems(localValue4).size(); localValue10++) {
         ItemStack localValue12 = localValue2.internalMethod04002(localValue10);
         if (localValue12.isEmpty()) {
            rockstar.client.util.LegacyItemTypes.armorItems(localValue4).set(localValue10, ItemStack.EMPTY);
         } else {
            ItemStack localValue9 = localValue12.copy();
            if (localValue3) {
               localValue9.setCount(1);
            }

            rockstar.client.util.LegacyItemTypes.armorItems(localValue4).set(localValue10, localValue9);
         }
      }

      ItemStack localValue11 = localValue2.internalMethod00199();
      if (localValue11.isEmpty()) {
         localValue4.equipStack(net.minecraft.entity.EquipmentSlot.OFFHAND, ItemStack.EMPTY);
      } else {
         ItemStack localValue13 = localValue11.copy();
         if (localValue3) {
            localValue13.setCount(1);
         }

         localValue4.equipStack(net.minecraft.entity.EquipmentSlot.OFFHAND, localValue13);
      }

      localValue4.getInventory().setSelectedSlot(0);
      internalField0149.send(() -> internalField0149.setScreen(new InvseeCommandHandler.InternalType0012(localValue4)));
   }

   private void internalMethod02219(PlayerEntity localValue1) {
      InvseeCommandHandler.InternalType0013 localValue2 = this.internalField0543.computeIfAbsent(localValue1.getUuid(), localValue1x -> new InvseeCommandHandler.InternalType0013(localValue1));
      localValue2.internalMethod00371(localValue1);
   }

   public static String internalMethod00329(ItemStack localValue0) {
      return Registries.ITEM.getId(localValue0.getItem()).toString();
   }

   static final class InternalType0012 extends InventoryScreen {
      private final AbstractClientPlayerEntity internalField0166;
      private float internalField0205;
      private float internalField0206;

      InternalType0012(AbstractClientPlayerEntity localValue1) {
         super(localValue1);
         this.internalField0166 = localValue1;
      }

      public void render(DrawContext context, int mouseX, int mouseY, float delta) {
         this.internalField0205 = mouseX;
         this.internalField0206 = mouseY;
         super.render(context, mouseX, mouseY, delta);
      }

      protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
         context.drawTexture(net.minecraft.client.gl.RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, this.x, this.y, 0.0F, 0.0F, this.backgroundWidth, this.backgroundHeight, 256, 256);
         InventoryScreen.drawEntity(
            context, this.x + 26, this.y + 8, this.x + 75, this.y + 78, 30, 0.0625F, this.internalField0205, this.internalField0206, this.internalField0166
         );
      }
   }

   static final class InternalType0013 {
      private final LinkedHashMap<String, ItemStack> internalField0499 = new LinkedHashMap<>();
      private final ItemStack[] internalField0299;
      private ItemStack internalField0878 = ItemStack.EMPTY;
      private ItemStack internalField0879 = ItemStack.EMPTY;

      InternalType0013(PlayerEntity localValue1) {
      this.internalField0299 = new ItemStack[rockstar.client.util.LegacyItemTypes.armorItems(localValue1).size()];
         Arrays.fill(this.internalField0299, ItemStack.EMPTY);
      }

      void internalMethod00371(PlayerEntity localValue1) {
         for (int localValue2 = 0; localValue2 < localValue1.getInventory().getMainStacks().size(); localValue2++) {
            this.internalMethod04046((ItemStack)localValue1.getInventory().getMainStacks().get(localValue2));
         }

         ItemStack localValue5 = localValue1.getMainHandStack();
         if (!localValue5.isEmpty()) {
            this.internalMethod04046(localValue5);
            this.internalField0879 = localValue5.copy();
         }

      for (int localValue3 = 0; localValue3 < this.internalField0299.length && localValue3 < rockstar.client.util.LegacyItemTypes.armorItems(localValue1).size(); localValue3++) {
         ItemStack localValue4 = rockstar.client.util.LegacyItemTypes.armorItems(localValue1).get(localValue3);
            if (!localValue4.isEmpty()) {
               this.internalField0299[localValue3] = localValue4.copy();
            }
         }

         ItemStack localValue6 = localValue1.getOffHandStack();
         if (!localValue6.isEmpty()) {
            this.internalField0878 = localValue6.copy();
            this.internalMethod04046(localValue6);
         }
      }

      void internalMethod04046(ItemStack localValue1) {
         if (localValue1 != null && !localValue1.isEmpty()) {
            String localValue2 = InvseeCommandHandler.internalMethod00329(localValue1);
            ItemStack localValue3 = this.internalField0499.get(localValue2);
            if (localValue3 == null) {
               this.internalField0499.put(localValue2, localValue1.copy());
            } else if (localValue1.getCount() > localValue3.getCount()) {
               localValue3.setCount(localValue1.getCount());
            }
         }
      }

      List<ItemStack> internalMethod07606() {
         ArrayList localValue1 = new ArrayList();
         String localValue2 = this.internalField0879.isEmpty() ? null : InvseeCommandHandler.internalMethod00329(this.internalField0879);
         if (localValue2 != null) {
            localValue1.add(this.internalField0879.copy());
         }

         for (Entry localValue4 : this.internalField0499.entrySet()) {
            if (!((String)localValue4.getKey()).equals(localValue2)) {
               localValue1.add(((ItemStack)localValue4.getValue()).copy());
            }
         }

         return localValue1;
      }

      ItemStack internalMethod04002(int localValue1) {
         return this.internalField0299[localValue1] == null ? ItemStack.EMPTY : this.internalField0299[localValue1];
      }

      ItemStack internalMethod00199() {
         return this.internalField0878 == null ? ItemStack.EMPTY : this.internalField0878;
      }
   }
}
