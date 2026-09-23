package rockstar.client.internal.script;









import rockstar.client.util.*;
import rockstar.client.server.*;
import rockstar.client.rotation.*;
import rockstar.client.notification.*;
import rockstar.client.inventory.*;
import rockstar.client.i18n.*;
import rockstar.client.event.*;
import rockstar.client.*;
import rockstar.modules.combat.*;
import rockstar.modules.movement.*;
import rockstar.modules.visual.*;
import rockstar.modules.player.*;
import rockstar.modules.other.*;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Predicate;
import lombok.Generated;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.item.BundleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.BundleItemSelectedC2SPacket;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.network.packet.s2c.play.CooldownUpdateS2CPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.screen.sync.ItemStackHash;
import net.minecraft.util.Hand;
import pyrock.events.network.ReceivePacketEvent;
import pyrock.events.network.SendPacketEvent;
import pyrock.events.player.ClientPlayerTickEvent;

public class ItemCooldownManager implements MinecraftClientAccess {
   private Item internalField0152 = null;
   private HotbarSlot internalField0226 = null;
   private boolean internalField0277;
   private InventorySlot internalField0022 = null;
   private final Stopwatch internalField0519 = new Stopwatch();
   private ItemCooldownManager.InternalType0444 internalField0296;
   private int internalField0227;
   private InventorySlot internalField0023;
   private HotbarSlot internalField0225;
   private int internalField0228;
   private int internalField1053;
   private ItemStack internalField0878;
   private ItemStack internalField0879;
   private boolean internalField0276;
   private Object internalField0290;
   private HotbarSlot internalField1052;
   private int internalField1055;
   private boolean internalField1099;
   private int internalField1054;
   private int internalField1464;
   private Item internalField0153;
   private int internalField1470;
   private final Map<Item, ItemCooldownManager.InternalType0202> internalField0543;
   private Item internalField1026;
   private final EventListener<ReceivePacketEvent> internalField0157;
   private final EventListener<ClientPlayerTickEvent> internalField0158;
   private final EventListener<SendPacketEvent> internalField1028;

   public ItemCooldownManager() {
      this.internalField0296 = ItemCooldownManager.InternalType0444.internalField0296;
      this.internalField0228 = -1;
      this.internalField1053 = 45;
      this.internalField0878 = ItemStack.EMPTY;
      this.internalField0879 = ItemStack.EMPTY;
      this.internalField1055 = -1;
      this.internalField1054 = -1;
      this.internalField1464 = -1;
      this.internalField0543 = new HashMap<>();
      this.internalField1026 = null;
      this.internalField0157 = localValue1 -> {
         if (localValue1.getPacket() instanceof CooldownUpdateS2CPacket localValue2 && this.internalField1026 != null) {
            this.internalField0543
               .put(this.internalField1026, new ItemCooldownManager.InternalType0202(localValue2.cooldown() / 20.0F, System.currentTimeMillis(), false));
            this.internalField1026 = null;
         }
      };
      this.internalField0158 = localValue1 -> {
         this.internalMethod08418();
         if (this.internalField0277) {
            this.internalMethod03575();
         }

         for (Entry localValue3 : this.internalField0543.entrySet()) {
            ItemCooldownManager.InternalType0202 localValue4 = (ItemCooldownManager.InternalType0202)localValue3.getValue();
            float localValue5 = this.internalMethod07081((Item)localValue3.getKey());
            if (localValue5 <= 0.0F && !localValue4.internalField0277) {
               localValue3.setValue(new ItemCooldownManager.InternalType0202(localValue4.internalField0205, localValue4.internalField0229, true));
               RockstarClient.getInstance()
                  .internalMethod02503()
                  .internalMethod02784(
                     new ItemNotification(LanguageManager.internalMethod07214("alerts.cooldown_ready"), new ItemStack((ItemConvertible)localValue3.getKey()))
                  );
            }
         }

         if ((this.internalMethod03579() || internalField0149.player.networkHandler.getServerInfo() == null)
            && this.internalField0023 != null
            && this.internalField0023 instanceof MainInventorySlot localValue6) {
            this.internalField0227++;
            if (this.internalField0227 == 2) {
               internalField0149.doItemUse();
            }

            if (this.internalField0227 == 2) {
               InventoryUtils.internalMethod08821(localValue6.internalMethod06662(), InventoryUtils.internalMethod06160().internalMethod08745());
               InventoryUtils.internalMethod01980(this.internalField0225);
               this.internalField0023 = null;
               this.internalField0225 = null;
               this.internalField0227 = 0;
            }
         }

         this.internalMethod03578();
      };
      this.internalField1028 = localValue0 -> {
         if (localValue0.getPacket() instanceof PlayerInteractItemC2SPacket localValue1) {
            Rotation localValue3 = RockstarClient.getInstance().internalMethod02368().internalMethod00024();
            localValue0.setPacket(new PlayerInteractItemC2SPacket(localValue1.getHand(), localValue1.getSequence(), localValue3.internalMethod00169(), localValue3.internalMethod00171()));
         }
      };
      RockstarClient.getInstance().internalMethod03317().internalMethod00647(this);
   }

   private void internalMethod03575() {
      if (internalField0149.player != null
         && internalField0149.world != null
         && internalField0149.interactionManager != null
         && internalField0149.player.getItemCooldownManager() != null) {
         if (this.internalField0022 != null && !(this.internalField0022 instanceof HotbarSlot)) {
         }

         if (this.internalField0296 == ItemCooldownManager.InternalType0444.internalField0295) {
            if (this.internalField0022 instanceof HotbarSlot) {
               internalField0149.interactionManager.interactItem(internalField0149.player, Hand.MAIN_HAND);
               this.internalField0296 = ItemCooldownManager.InternalType0444.internalField1114;
            } else if (this.internalMethod08407()) {
               internalField0149.interactionManager.interactItem(internalField0149.player, this.internalMethod03978());
               this.internalField0296 = ItemCooldownManager.InternalType0444.internalField1114;
            }
         } else if (this.internalField0296 == ItemCooldownManager.InternalType0444.internalField1115) {
            if (this.internalMethod08407()) {
               this.internalMethod01188(this.internalField1053 == 45 ? Hand.OFF_HAND : Hand.MAIN_HAND);
               if (this.internalField0228 != this.internalField1053) {
                  this.internalMethod07523(this.internalField0228, this.internalField0878, this.internalField0879, true);
                  this.internalMethod08408();
               }

               this.internalMethod08415();
            }
         } else if (this.internalField0296 == ItemCooldownManager.InternalType0444.internalField1114) {
            if (this.internalField0022 instanceof HotbarSlot) {
               InventoryUtils.internalMethod01980(this.internalField0226);
               this.internalMethod08415();
            } else if (this.internalField0022 instanceof MainInventorySlot) {
               InventoryUtils.internalMethod08821(this.internalField0022.internalMethod06662(), this.internalMethod03574());
               this.internalMethod08415();
            } else {
               this.internalMethod08415();
            }
         } else {
            this.internalField0277 = false;
            this.internalField0296 = ItemCooldownManager.InternalType0444.internalField0296;
         }
      } else {
         this.internalField0277 = false;
         this.internalField0296 = ItemCooldownManager.InternalType0444.internalField0296;
      }
   }

   public void internalMethod07082(Item localValue1) {
      this.internalMethod04839(localValue1, localValue0 -> true, null);
   }

   public void internalMethod02474(Item localValue1, Predicate<ItemStack> localValue2) {
      this.internalMethod04839(localValue1, localValue2, null);
   }

   public void internalMethod04839(Item localValue1, Predicate<ItemStack> localValue2, String localValue3) {
      if (internalField0149.player != null
         && internalField0149.world != null
         && internalField0149.interactionManager != null
         && internalField0149.currentScreen == null) {
         if (!this.internalField0277 && this.internalField0290 == null) {
            SlotCollection<InventorySlot> localValue4 = InventorySlots.internalMethod02872()
               .internalMethod07591(InventorySlots.internalMethod03558())
               .internalMethod07591(InventorySlots.internalMethod07766());
            Predicate<ItemStack> localValue5 = localValue2x -> localValue2x != null && !localValue2x.isEmpty() && localValue2x.getItem() == localValue1 && localValue2.test(localValue2x);
            InventorySlot localValue6 = localValue4.internalMethod03297(localValue3x -> localValue5.test(localValue3x) && this.internalMethod07598(localValue3x, localValue1));
            if (localValue6 == null) {
               localValue6 = localValue4.internalMethod03297(localValue5);
            }

            ItemCooldownManager.InternalType0201 localValue7 = localValue6 == null ? this.internalMethod04349(localValue4, localValue5, localValue1) : null;
            String localValue8 = localValue3 != null && !localValue3.isBlank() ? localValue3 : CustomItemUtils.internalField0543.getOrDefault(localValue1, localValue1.getName().getString());
            if (localValue8 == null) {
               localValue8 = localValue1.getName().getString();
            }

            if (localValue6 == null && localValue7 == null) {
               RockstarClient.getInstance()
                  .internalMethod02503()
                  .internalMethod00599(
                     NotificationType.internalField0705,
                     LanguageManager.internalMethod07214("swap.item_not_found"),
                     LanguageManager.internalMethod00160("swap.item_required", localValue8)
                  );
            } else {
               ItemStack localValue9 = localValue6 != null ? localValue6.internalMethod03427() : localValue7.internalMethod01665();
               if (internalField0149.player.getItemCooldownManager().isCoolingDown(localValue9)) {
                  ItemCooldownManager.InternalType0202 localValue13 = this.internalField0543.get(localValue1);
                  if (localValue13 != null) {
                     float localValue14 = Math.max(0.0F, localValue13.internalField0205 - (float)(System.currentTimeMillis() - localValue13.internalField0229) / 1000.0F);
                     if (localValue14 > 0.01F) {
                        RockstarClient.getInstance()
                           .internalMethod02503()
                           .internalMethod02784(new ItemNotification(LanguageManager.internalMethod00160("alerts.cooldown", localValue14), localValue9));
                     }
                  }
               } else {
                  if (localValue7 != null) {
                     localValue6 = this.internalMethod01465(localValue7);
                     if (localValue6 == null) {
                        RockstarClient.getInstance()
                           .internalMethod02503()
                           .internalMethod00599(
                              NotificationType.internalField0705,
                              LanguageManager.internalMethod07214("swap.bundle_no_space"),
                              LanguageManager.internalMethod00160("swap.bundle_no_space.description", localValue8)
                           );
                        return;
                     }
                  }

                  this.internalField1026 = localValue1;
                  this.internalField0276 = this.internalMethod08409();
                  AssistModule localValue10 = RockstarClient.getInstance().getModuleManager().getModule(AssistModule.class);
                  if (localValue10 != null && localValue10.internalMethod09439()) {
                     this.internalMethod07635(localValue6);
                  } else if (this.internalMethod03579() && !localValue6.internalMethod03427().isOf(Items.SPLASH_POTION)) {
                     this.internalMethod04533(localValue6);
                  } else {
                     this.internalField0152 = localValue1;
                     this.internalField0226 = InventoryUtils.internalMethod06160();
                     this.internalField0022 = localValue6;
                     this.internalField0277 = true;
                     this.internalField0296 = ItemCooldownManager.InternalType0444.internalField0295;
                     this.internalField0519.internalMethod00701();
                     if (localValue6 instanceof HotbarSlot localValue11) {
                        InventoryUtils.internalMethod01980(localValue11);
                     } else if (localValue6 instanceof MainInventorySlot localValue12) {
                        InventoryUtils.internalMethod08821(localValue12.internalMethod06662(), this.internalMethod03574());
                        this.internalField0296 = ItemCooldownManager.InternalType0444.internalField0295;
                     }
                  }
               }
            }
         }
      }
   }

   private ItemCooldownManager.InternalType0201 internalMethod04349(SlotCollection<InventorySlot> localValue1, Predicate<ItemStack> localValue2, Item localValue3) {
      ItemCooldownManager.InternalType0201 localValue4 = null;

      for (InventorySlot localValue6 : localValue1.internalMethod02638()) {
         BundleContentsComponent localValue7 = (BundleContentsComponent)localValue6.internalMethod03427().get(DataComponentTypes.BUNDLE_CONTENTS);
         if (localValue7 != null && !localValue7.isEmpty()) {
            for (int localValue8 = 0; localValue8 < localValue7.size(); localValue8++) {
               ItemStack localValue9 = localValue7.get(localValue8);
               if (!localValue9.contains(DataComponentTypes.BUNDLE_CONTENTS) && localValue2.test(localValue9)) {
                  if (this.internalMethod07598(localValue9, localValue3)) {
                     return new ItemCooldownManager.InternalType0201(localValue6, localValue8, localValue9);
                  }

                  if (localValue4 == null) {
                     localValue4 = new ItemCooldownManager.InternalType0201(localValue6, localValue8, localValue9);
                  }
               }
            }
         }
      }

      return localValue4;
   }

   private InventorySlot internalMethod01465(ItemCooldownManager.InternalType0201 localValue1) {
      ScreenHandler localValue2 = internalField0149.player.currentScreenHandler;
      if (localValue2 != null && internalField0149.getNetworkHandler() != null && localValue2.getCursorStack().isEmpty()) {
         MainInventorySlot localValue3 = InventorySlots.internalMethod03558().internalMethod01069();
         if (localValue3 == null) {
            return null;
         } else {
            int localValue4 = localValue1.internalMethod06030().internalMethod06662();
            ItemStack localValue5 = localValue1.internalMethod06030().internalMethod03427();
            this.internalMethod04461(localValue4, localValue5, -1);
            if (localValue1.internalMethod06648() > 0) {
               this.internalMethod04461(localValue4, localValue5, localValue1.internalMethod06648());
            }

            this.internalMethod07137(localValue4, 1);
            this.internalMethod07137(localValue3.internalMethod06662(), 1);
            if (!localValue2.getCursorStack().isEmpty()) {
               this.internalMethod07137(localValue4, 0);
            }

            if (!localValue2.getCursorStack().isEmpty()) {
               this.internalMethod07137(localValue3.internalMethod06662(), 0);
            }

            if (localValue3.internalMethod06664()) {
               return null;
            } else {
               this.internalField1054 = localValue4;
               this.internalField1464 = localValue3.internalMethod06662();
               this.internalField0153 = localValue1.internalMethod01665().getItem();
               this.internalField1470 = 0;
               return localValue3;
            }
         }
      } else {
         return null;
      }
   }

   private void internalMethod03578() {
      if (this.internalField1464 >= 0) {
         if (!this.internalField0277
            && this.internalField0023 == null
            && internalField0149.currentScreen == null
            && internalField0149.player != null
            && internalField0149.player.currentScreenHandler == internalField0149.player.playerScreenHandler) {
            if (++this.internalField1470 >= 5) {
               this.internalMethod08406();
               this.internalField1054 = -1;
               this.internalField1464 = -1;
               this.internalField0153 = null;
               this.internalField1470 = 0;
            }
         } else {
            this.internalField1470 = 0;
         }
      }
   }

   private void internalMethod08406() {
      ScreenHandler localValue1 = internalField0149.player.currentScreenHandler;
      if (localValue1 != null && localValue1.getCursorStack().isEmpty()) {
         if (this.internalField1464 < localValue1.slots.size() && this.internalField1054 < localValue1.slots.size()) {
            ItemStack localValue2 = localValue1.getSlot(this.internalField1464).getStack();
            if (!localValue2.isEmpty() && localValue2.getItem() == this.internalField0153) {
               if (localValue1.getSlot(this.internalField1054).getStack().contains(DataComponentTypes.BUNDLE_CONTENTS)) {
                  this.internalMethod07137(this.internalField1464, 0);
                  this.internalMethod07137(this.internalField1054, 0);
                  if (!localValue1.getCursorStack().isEmpty()) {
                     this.internalMethod07137(this.internalField1464, 0);
                  }
               }
            }
         }
      }
   }

   private void internalMethod04461(int localValue1, ItemStack localValue2, int localValue3) {
      BundleItem.setSelectedStackIndex(localValue2, localValue3);
      internalField0149.getNetworkHandler().sendPacket(new BundleItemSelectedC2SPacket(localValue1, localValue3));
   }

   private void internalMethod07137(int localValue1, int localValue2) {
      internalField0149.interactionManager
         .clickSlot(internalField0149.player.currentScreenHandler.syncId, localValue1, localValue2, SlotActionType.PICKUP, internalField0149.player);
   }

   private void internalMethod07635(InventorySlot localValue1) {
      if (internalField0149.player != null
         && internalField0149.world != null
         && internalField0149.interactionManager != null
         && internalField0149.player.networkHandler != null) {
         if (localValue1 instanceof HotbarSlot localValue2) {
            this.internalMethod07510(localValue2);
            this.internalMethod08415();
         } else {
            this.internalField0228 = localValue1.internalMethod06662();
            this.internalField1053 = this.internalField0228 != 45 && this.internalMethod08416() ? 36 + internalField0149.player.getInventory().getSelectedSlot() : 45;
            this.internalField0878 = localValue1.internalMethod03427().copy();
            this.internalField0879 = this.internalMethod07376(this.internalField1053).copy();
            this.internalField0022 = localValue1;
            this.internalField0277 = true;
            this.internalField0296 = ItemCooldownManager.InternalType0444.internalField1115;
            this.internalField0519.internalMethod00701();
            if (this.internalField0228 != this.internalField1053) {
               this.internalMethod07523(this.internalField0228, this.internalField0879, this.internalField0878, false);
            }
         }
      }
   }

   private ItemStack internalMethod07376(int localValue1) {
      ScreenHandler localValue2 = internalField0149.player.currentScreenHandler;
      return localValue2 != null && localValue1 >= 0 && localValue1 < localValue2.slots.size() ? localValue2.getSlot(localValue1).getStack() : ItemStack.EMPTY;
   }

   private void internalMethod07510(HotbarSlot localValue1) {
      int localValue2 = internalField0149.player.getInventory().getSelectedSlot();
      Rotation localValue3 = RockstarClient.getInstance().internalMethod02368().internalMethod00024();
      if (localValue1.internalMethod08745() != localValue2) {
         internalField0149.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(localValue1.internalMethod08745()));
      }

      internalField0149.interactionManager
         .sendSequencedPacket(
            internalField0149.world, localValue1x -> new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, localValue1x, localValue3.internalMethod00169(), localValue3.internalMethod00171())
         );
      if (localValue1.internalMethod08745() != localValue2) {
         internalField0149.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(localValue2));
      }
   }

   private void internalMethod01188(Hand localValue1) {
      Rotation localValue2 = RockstarClient.getInstance().internalMethod02368().internalMethod00024();
      internalField0149.interactionManager
         .sendSequencedPacket(internalField0149.world, localValue2x -> new PlayerInteractItemC2SPacket(localValue1, localValue2x, localValue2.internalMethod00169(), localValue2.internalMethod00171()));
   }

   private void internalMethod07523(int localValue1, ItemStack localValue2, ItemStack localValue3, boolean localValue4) {
      ScreenHandler localValue5 = internalField0149.player.currentScreenHandler;
      Int2ObjectOpenHashMap<ItemStackHash> localValue6 = new Int2ObjectOpenHashMap<>();
      var localValue8 = internalField0149.player.networkHandler.getComponentHasher();
      localValue6.put(localValue1, ItemStackHash.fromItemStack(localValue2, localValue8));
      localValue6.put(this.internalField1053, ItemStackHash.fromItemStack(localValue3, localValue8));
      ClickSlotC2SPacket localValue7 = new ClickSlotC2SPacket(
           localValue5.syncId,
           localValue5.getRevision(),
           (short)localValue1,
           (byte)(this.internalField1053 == 45 ? 40 : this.internalField1053 - 36),
           SlotActionType.SWAP,
           localValue6,
           ItemStackHash.EMPTY
      );
      if (localValue4) {
         this.internalMethod05420(localValue7);
      } else {
         internalField0149.player.networkHandler.sendPacket(localValue7);
      }
   }

   private void internalMethod05420(ClickSlotC2SPacket localValue1) {
      GuiMoveModule localValue2 = RockstarClient.getInstance().getModuleManager().getModule(GuiMoveModule.class);
      boolean localValue3 = localValue2 != null && localValue2.internalField0277;
      if (localValue2 != null) {
         localValue2.internalField0277 = true;
      }

      try {
         internalField0149.player.networkHandler.sendPacket(localValue1);
      } finally {
         if (localValue2 != null) {
            localValue2.internalField0277 = localValue3;
         }
      }
   }

   private void internalMethod08408() {
      ScreenHandler localValue1 = internalField0149.player.currentScreenHandler;
      if (localValue1 != null && this.internalField0228 >= 0 && this.internalField0228 < localValue1.slots.size() && this.internalField1053 >= 0 && this.internalField1053 < localValue1.slots.size()) {
         localValue1.getSlot(this.internalField0228).setStack(this.internalField0878.copy());
         localValue1.getSlot(this.internalField1053).setStack(this.internalField0879.copy());
      }
   }

   private boolean internalMethod07598(ItemStack localValue1, Item localValue2) {
      if (localValue1 != null && !localValue1.isEmpty()) {
         if (localValue1.getItem() != localValue2) {
            return false;
         } else {
            CustomItemUtils.InternalType0254 localValue3 = CustomItemUtils.internalMethod03238(localValue1);
            return localValue3 != null && localValue3.internalMethod08994();
         }
      } else {
         return false;
      }
   }

   private void internalMethod08415() {
      this.internalField0277 = false;
      this.internalField0296 = ItemCooldownManager.InternalType0444.internalField0296;
      this.internalField0152 = null;
      this.internalField0226 = null;
      this.internalField0022 = null;
      this.internalField0228 = -1;
      this.internalField1053 = 45;
      this.internalField0878 = ItemStack.EMPTY;
      this.internalField0879 = ItemStack.EMPTY;
      this.internalField0276 = false;
   }

   public boolean internalMethod05917(Object localValue1, HotbarSlot localValue2) {
      if (localValue1 == null || localValue2 == null || internalField0149.player == null || internalField0149.getNetworkHandler() == null || this.internalField0277) {
         return false;
      } else if (this.internalField0290 == null) {
         this.internalField0290 = localValue1;
         this.internalField1052 = InventoryUtils.internalMethod06160();
         this.internalField1055 = localValue2.internalMethod08745();
         this.internalField1099 = false;
         this.internalMethod03721(localValue2.internalMethod08745());
         return true;
      } else {
         return this.internalField0290 == localValue1 && this.internalField1055 == localValue2.internalMethod08745();
      }
   }

   public boolean internalMethod03170(Object localValue1) {
      return this.internalField0290 == localValue1;
   }

   public void internalMethod03169(Object localValue1) {
      if (this.internalField0290 == localValue1) {
         this.internalField1099 = true;
      }
   }

   public void internalMethod01775(Object localValue1) {
      if (this.internalField0290 == localValue1) {
         this.internalMethod09888();
      }
   }

   private void internalMethod08418() {
      if (this.internalField0290 != null) {
         if (internalField0149.player == null || internalField0149.getNetworkHandler() == null) {
            this.internalMethod09889();
         } else if (this.internalField1099) {
            this.internalMethod09888();
         } else {
            if (internalField0149.player.getInventory().getSelectedSlot() != this.internalField1055) {
               this.internalMethod03721(this.internalField1055);
            }
         }
      }
   }

   private void internalMethod09888() {
      if (this.internalField1052 != null && internalField0149.player != null && internalField0149.getNetworkHandler() != null) {
         this.internalMethod03721(this.internalField1052.internalMethod08745());
      }

      this.internalMethod09889();
   }

   private void internalMethod09889() {
      this.internalField0290 = null;
      this.internalField1052 = null;
      this.internalField1055 = -1;
      this.internalField1099 = false;
   }

   private void internalMethod03721(int localValue1) {
      internalField0149.player.getInventory().setSelectedSlot(localValue1);
      internalField0149.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(localValue1));
   }

   private void internalMethod04533(InventorySlot localValue1) {
      HotbarSlot localValue2 = InventoryUtils.internalMethod06160();
      if (localValue1 instanceof HotbarSlot localValue4) {
         InventoryUtils.internalMethod01980(localValue4);
         internalField0149.doItemUse();
         InventoryUtils.internalMethod01980(localValue2);
      } else {
         if (localValue1 instanceof MainInventorySlot localValue3) {
            InventoryUtils.internalMethod08821(localValue3.internalMethod06662(), InventoryUtils.internalMethod06160().internalMethod08745());
            this.internalField0023 = localValue3;
            this.internalField0225 = localValue2;
            this.internalField0227 = 0;
         }
      }
   }

   public float internalMethod07081(Item localValue1) {
      ItemCooldownManager.InternalType0202 localValue2 = this.internalField0543.get(localValue1);
      if (localValue2 == null) {
         return 0.0F;
      } else {
         float localValue3 = localValue2.internalField0205 - (float)(System.currentTimeMillis() - localValue2.internalField0229) / 1000.0F;
         return Math.max(0.0F, localValue3);
      }
   }

   public Set<Item> internalMethod06597() {
      return this.internalField0543.keySet();
   }

   private boolean internalMethod03579() {
      GuiMoveModule localValue1 = RockstarClient.getInstance().getModuleManager().getModule(GuiMoveModule.class);
      return ServerUtils.internalMethod01786(KnownServer.internalField0578) && localValue1 != null && localValue1.internalMethod09769();
   }

   private boolean internalMethod08407() {
      GuiMoveModule localValue1 = RockstarClient.getInstance().getModuleManager().getModule(GuiMoveModule.class);
      if (localValue1 == null) {
         return true;
      } else {
         return localValue1.internalMethod09771()
            ? !localValue1.internalMethod09603() && !localValue1.internalMethod10016() && localValue1.internalMethod09598()
            : localValue1.internalMethod06994().isEmpty() && !localValue1.internalMethod10016();
      }
   }

   private boolean internalMethod08409() {
      if (ServerUtils.internalMethod01786(KnownServer.internalField0578)) {
         return false;
      } else {
         AssistModule localValue1 = RockstarClient.getInstance().getModuleManager().getModule(AssistModule.class);
         return localValue1 != null && localValue1.internalMethod09437() && !this.internalMethod08416();
      }
   }

   private boolean internalMethod08416() {
      AutoTotemModule localValue1 = RockstarClient.getInstance().getModuleManager().getModule(AutoTotemModule.class);
      if (localValue1 != null && localValue1.internalMethod09475()) {
         return true;
      } else {
         AutoSwapModule localValue2 = RockstarClient.getInstance().getModuleManager().getModule(AutoSwapModule.class);
         return localValue2 != null && localValue2.internalMethod09275();
      }
   }

   private Hand internalMethod03978() {
      if (this.internalField0022 instanceof OffhandSlot) {
         return Hand.OFF_HAND;
      } else {
         return this.internalField0276 ? Hand.OFF_HAND : Hand.MAIN_HAND;
      }
   }

   private int internalMethod03574() {
      if (this.internalField0276) {
         return 40;
      } else {
         return this.internalField0296 == ItemCooldownManager.InternalType0444.internalField1114
            ? this.internalField0226.internalMethod08745()
            : InventoryUtils.internalMethod06160().internalMethod08745();
      }
   }

   @Generated
   public boolean internalMethod03576() {
      return this.internalField0277;
   }

   static final class InternalType0201 {
      private final InventorySlot internalField0022;
      private final int internalField0227;
      private final ItemStack internalField0878;

      InternalType0201(InventorySlot localValue1, int localValue2, ItemStack localValue3) {
         this.internalField0022 = localValue1;
         this.internalField0227 = localValue2;
         this.internalField0878 = localValue3;
      }

      @Override
      public final String toString() {
         return "InternalType0201[slot=" + this.internalField0022 + ", index=" + this.internalField0227 + ", stack=" + this.internalField0878 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0022);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0878);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         ItemCooldownManager.InternalType0201 other = (ItemCooldownManager.InternalType0201) localValue1;
         return java.util.Objects.equals(this.internalField0022, other.internalField0022)
            && java.util.Objects.equals(this.internalField0227, other.internalField0227)
            && java.util.Objects.equals(this.internalField0878, other.internalField0878);
      }

      public InventorySlot internalMethod06030() {
         return this.internalField0022;
      }

      public int internalMethod06648() {
         return this.internalField0227;
      }

      public ItemStack internalMethod01665() {
         return this.internalField0878;
      }
   }

   static final class InternalType0202 {
      final float internalField0205;
      final long internalField0229;
      final boolean internalField0277;

      InternalType0202(float localValue1, long localValue2, boolean localValue4) {
         this.internalField0205 = localValue1;
         this.internalField0229 = localValue2;
         this.internalField0277 = localValue4;
      }

      @Override
      public final String toString() {
         return "InternalType0202[durationSeconds=" + this.internalField0205 + ", startTime=" + this.internalField0229 + ", notified=" + this.internalField0277 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0205);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0229);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0277);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         ItemCooldownManager.InternalType0202 other = (ItemCooldownManager.InternalType0202) localValue1;
         return java.util.Objects.equals(this.internalField0205, other.internalField0205)
            && java.util.Objects.equals(this.internalField0229, other.internalField0229)
            && java.util.Objects.equals(this.internalField0277, other.internalField0277);
      }

      public float internalMethod00221() {
         return this.internalField0205;
      }

      public long internalMethod00222() {
         return this.internalField0229;
      }

      public boolean internalMethod00223() {
         return this.internalField0277;
      }
   }

   static enum InternalType0444 {
      internalField0296,
      internalField0295,
      internalField1114,
      internalField1115;
   }
}
