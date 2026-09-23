package rockstar.modules.player;







import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.module.*;
import rockstar.client.inventory.*;
import rockstar.client.event.*;
import rockstar.client.internal.script.*;
import rockstar.client.*;
import rockstar.client.module.Module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.AxeItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MaceItem;
import net.minecraft.item.TridentItem;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import pyrock.events.game.PickupEvent;
import pyrock.events.window.KeyPressEvent;
import pyrock.events.window.MouseEvent;

@ModuleInfo(
   name = "Auto Shulker",
   category = ModuleCategory.PLAYER,
   internalMethod09633 = "modules.descriptions.auto_shulker"
)
public class AutoShulkerModule extends Module {
   private static final Set<Item> internalField0546 = Set.of(
      Items.GOLDEN_APPLE,
      Items.ENCHANTED_GOLDEN_APPLE,
      Items.GOLDEN_CARROT,
      Items.CHORUS_FRUIT,
      Items.ENDER_PEARL,
      Items.ENDER_EYE,
      Items.EXPERIENCE_BOTTLE,
      Items.POTION,
      Items.SPLASH_POTION,
      Items.LINGERING_POTION,
      Items.FIREWORK_ROCKET,
      Items.SNOWBALL
   );
   private static final Set<Item> internalField0545 = Set.of(
      Items.NETHERITE_INGOT,
      Items.NETHERITE_SCRAP,
      Items.NETHERITE_BLOCK,
      Items.ANCIENT_DEBRIS,
      Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE,
      Items.NETHER_STAR,
      Items.BEACON,
      Items.ELYTRA,
      Items.DIAMOND,
      Items.DIAMOND_BLOCK,
      Items.EMERALD,
      Items.EMERALD_BLOCK,
      Items.ECHO_SHARD,
      Items.HEAVY_CORE,
      Items.DRAGON_EGG,
      Items.DRAGON_BREATH,
      Items.ENDER_CHEST
   );
   private static final Set<Item> internalField1200 = Set.of(
      Items.NETHERITE_HELMET, Items.NETHERITE_CHESTPLATE, Items.NETHERITE_LEGGINGS, Items.NETHERITE_BOOTS
   );
   private static final Set<Item> internalField1199 = Set.of(Items.GOLDEN_HELMET, Items.GOLDEN_BOOTS);
   private static final Set<Item> internalField1202 = Set.of(
      Items.PLAYER_HEAD, Items.WITHER_SKELETON_SKULL, Items.SKELETON_SKULL, Items.DRAGON_HEAD, Items.CREEPER_HEAD, Items.ZOMBIE_HEAD, Items.PIGLIN_HEAD
   );
   private ModeSetting internalField0668;
   private ModeSetting.InternalType0088 internalField0237;
   private ModeSetting.InternalType0088 internalField0238;
   private KeybindSetting internalField0648;
   private MultiSelectSetting internalField0675;
   private MultiSelectSetting.InternalType0091 internalField0245;
   private MultiSelectSetting.InternalType0091 internalField0244;
   private MultiSelectSetting.InternalType0091 internalField1075;
   private MultiSelectSetting.InternalType0091 internalField1074;
   private MultiSelectSetting.InternalType0091 internalField1073;
   private MultiSelectSetting.InternalType0091 internalField1072;
   private MultiSelectSetting.InternalType0091 internalField1491;
   private MultiSelectSetting.InternalType0091 internalField1488;
   private SliderSetting internalField0383;
   private final Stopwatch internalField0519 = new Stopwatch();
   private final Stopwatch internalField0518 = new Stopwatch();
   private final Stopwatch internalField1189 = new Stopwatch();
   private final Map<Integer, ItemStack> internalField0543 = new HashMap<>();
   private final List<ItemStack> internalField0416 = new ArrayList<>();
   private final Set<Integer> internalField1201 = new HashSet<>();
   private final ItemStack[] internalField0299 = new ItemStack[36];
   private final Stopwatch internalField1186 = new Stopwatch();
   private boolean internalField0277;
   private AutoShulkerModule.InternalType0453 internalField0900;
   private boolean internalField0276;
   private int internalField1053;
   private int internalField1055;
   private int internalField1056;
   private boolean internalField1099;
   private final EventListener<PickupEvent> internalField0157;
   private final EventListener<KeyPressEvent> internalField0158;
   private final EventListener<MouseEvent> internalField1028;

   public AutoShulkerModule() {
      this.internalField0900 = AutoShulkerModule.InternalType0453.internalField0900;
      this.internalField1053 = -1;
      this.internalField1055 = -1;
      this.internalField0157 = localValue1 -> {
         if (internalField0149.player != null && localValue1.getEntity() == internalField0149.player) {
            if (this.internalMethod08806(localValue1.getItemStack())) {
               this.internalMethod03079(localValue1.getItemStack());
               this.internalField0277 = true;
               this.internalField1186.internalMethod00701();
               if (this.internalField0668.internalMethod06103(this.internalField0237)) {
                  this.internalField0276 = true;
                  this.internalField0519.internalMethod00701();
               }
            }
         }
      };
      this.internalField0158 = localValue1 -> {
         if (this.internalField0648.internalMethod02165(localValue1.getKey()) && localValue1.getAction() == 1) {
            this.internalMethod09289();
         }
      };
      this.internalField1028 = localValue1 -> {
         if (this.internalField0648.internalMethod02165(localValue1.getButton()) && localValue1.getAction() == 1) {
            this.internalMethod09289();
         }
      };
      this.internalMethod09287();
   }

   private void internalMethod09287() {
      this.internalField0668 = new ModeSetting(this, "modules.settings.auto_shulker.mode");
      this.internalField0237 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.auto_shulker.mode.auto").select();
      this.internalField0238 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.auto_shulker.mode.bind");
      this.internalField0648 = new KeybindSetting(
         this, "modules.settings.auto_shulker.bind", () -> !this.internalField0668.internalMethod06103(this.internalField0238)
      );
      this.internalField0675 = new MultiSelectSetting(this, "modules.settings.auto_shulker.valuables");
      this.internalField0245 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.auto_shulker.valuables.armor").select();
      this.internalField0244 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.auto_shulker.valuables.weapons").select();
      this.internalField1075 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.auto_shulker.valuables.enchants").select();
      this.internalField1074 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.auto_shulker.valuables.totems").select();
      this.internalField1073 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.auto_shulker.valuables.heads").select();
      this.internalField1072 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.auto_shulker.valuables.consumables").select();
      this.internalField1491 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.auto_shulker.valuables.resources").select();
      this.internalField1488 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.auto_shulker.valuables.donate").select();
      this.internalField0383 = new SliderSetting(
            this, "modules.settings.auto_shulker.delay", () -> !this.internalField0668.internalMethod06103(this.internalField0237)
         )
         .internalMethod05900(0.0F)
         .internalMethod02732(2000.0F)
         .internalMethod08673(50.0F)
         .internalMethod08074(300.0F);
   }

   private void internalMethod09289() {
      if (this.internalField0900 == AutoShulkerModule.InternalType0453.internalField0900 && internalField0149.currentScreen == null) {
         this.internalField0276 = true;
         this.internalField0519.internalMethod00701();
      }
   }

   private void internalMethod03079(ItemStack localValue1) {
      if (localValue1 != null && !localValue1.isEmpty()) {
         if (!this.internalMethod03080(localValue1)) {
            if (this.internalField0416.size() >= 64) {
               this.internalField0416.remove(0);
            }

            this.internalField0416.add(localValue1.copy());
         }
      }
   }

   private boolean internalMethod03080(ItemStack localValue1) {
      if (localValue1 != null && !localValue1.isEmpty()) {
         for (ItemStack localValue3 : this.internalField0416) {
            if (ItemStack.areItemsAndComponentsEqual(localValue3, localValue1)) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   @Override
   public void internalMethod08229() {
      if (internalField0149.player != null && internalField0149.world != null && internalField0149.interactionManager != null) {
         this.internalMethod09468();
         switch (this.internalField0900) {
            case internalField0900:
               this.internalMethod09470();
               break;
            case internalField0901:
               this.internalMethod09472();
               break;
            case internalField1372:
               this.internalMethod09473();
               break;
            case internalField1369:
               this.internalMethod10033();
               break;
            case internalField1370:
               this.internalMethod10035();
               break;
            case internalField1371:
               this.internalMethod10040();
               break;
            case internalField1648:
               this.internalMethod10042();
         }

         PlayerInventory localValue1 = internalField0149.player.getInventory();

         for (int localValue2 = 0; localValue2 < this.internalField0299.length; localValue2++) {
            this.internalField0299[localValue2] = localValue1.getStack(localValue2).copy();
         }
      } else {
         this.internalField0276 = false;
         this.internalMethod10090();
         this.internalMethod10096();
      }
   }

   private void internalMethod09468() {
      PlayerInventory localValue1 = internalField0149.player.getInventory();
      boolean localValue2 = this.internalField0277 && !this.internalField1186.internalMethod02365(1000L);

      for (int localValue3 = 0; localValue3 < this.internalField0299.length; localValue3++) {
         if (localValue3 != 8) {
            ItemStack localValue4 = localValue1.getStack(localValue3);
            if (!localValue4.isEmpty() && this.internalMethod03080(localValue4)) {
               ItemStack localValue5 = this.internalField0299[localValue3];
               boolean localValue6 = localValue5 == null || localValue5.isEmpty() || !ItemStack.areItemsAndComponentsEqual(localValue5, localValue4);
               if (localValue6 && localValue2) {
                  this.internalField1201.add(localValue3);
               }
            } else {
               this.internalField1201.remove(localValue3);
            }
         }
      }
   }

   private void internalMethod09470() {
      if (this.internalField0276 && internalField0149.currentScreen == null) {
         if (!this.internalField0668.internalMethod06103(this.internalField0237)
            || this.internalField0519.internalMethod02365((long)this.internalField0383.internalMethod08576())) {
            if (!this.internalMethod09290()) {
               this.internalField0276 = false;
            } else {
               InventorySlot localValue1 = this.internalMethod00566();
               if (localValue1 == null) {
                  this.internalField0276 = false;
               } else if (this.internalMethod09288()) {
                  this.internalField0276 = false;
                  this.internalField1055 = InventoryUtils.internalMethod06160().internalMethod08745();
                  int localValue2 = localValue1.internalMethod06662();
                  if (localValue2 == 44) {
                     this.internalField1053 = -1;
                  } else {
                     this.internalField1053 = localValue2;
                     InventoryUtils.internalMethod08821(localValue2, 8);
                  }

                  this.internalField0900 = AutoShulkerModule.InternalType0453.internalField0901;
                  this.internalField0518.internalMethod00701();
               }
            }
         }
      }
   }

   private void internalMethod09472() {
      if (!this.internalMethod09288()) {
         if (this.internalField0518.internalMethod02365(3000L)) {
            this.internalMethod10088();
         }
      } else if (this.internalField0518.internalMethod02365(150L)) {
         if (!this.internalMethod00187(InventoryUtils.internalMethod02738(8).internalMethod03427())) {
            this.internalMethod10088();
         } else {
            InventoryUtils.internalMethod03663(8);
            internalField0149.interactionManager.interactItem(internalField0149.player, Hand.MAIN_HAND);
            this.internalField0900 = AutoShulkerModule.InternalType0453.internalField1372;
            this.internalField0518.internalMethod00701();
         }
      }
   }

   private void internalMethod09473() {
      if (this.internalMethod09469()) {
         this.internalField0543.clear();
         this.internalField1099 = false;
         this.internalField0900 = AutoShulkerModule.InternalType0453.internalField1369;
         this.internalField0518.internalMethod00701();
      } else {
         if (this.internalField0518.internalMethod02365(3000L)) {
            this.internalField0900 = AutoShulkerModule.InternalType0453.internalField1371;
            this.internalField0518.internalMethod00701();
         }
      }
   }

   private void internalMethod10033() {
      if (!this.internalMethod09469()) {
         this.internalField0900 = AutoShulkerModule.InternalType0453.internalField1371;
         this.internalField0518.internalMethod00701();
      } else {
         ScreenHandler localValue1 = internalField0149.player.currentScreenHandler;
         int localValue2 = this.internalMethod04125(localValue1);
         if (localValue2 == -1 && !this.internalField0518.internalMethod02365(15000L)) {
            if (!this.internalField1099) {
               this.internalField1099 = true;
               this.internalField1189.internalMethod00701();
               return;
            }

            if (!this.internalField1189.internalMethod02365(500L)) {
               return;
            }
         }

         if (localValue2 != -1 && !this.internalField0518.internalMethod02365(15000L)) {
            this.internalField1099 = false;
            Slot localValue3 = localValue1.getSlot(localValue2);
            ItemStack localValue4 = localValue3.getStack().copy();
            internalField0149.interactionManager.clickSlot(localValue1.syncId, localValue2, 0, SlotActionType.QUICK_MOVE, internalField0149.player);
            if (ItemStack.areEqual(localValue3.getStack(), localValue4)) {
               this.internalField0543.put(localValue2, localValue4);
            }
         } else {
            this.internalField1201.clear();
            this.internalField0900 = AutoShulkerModule.InternalType0453.internalField1370;
            this.internalField0518.internalMethod00701();
         }
      }
   }

   private void internalMethod10035() {
      if (this.internalMethod09469()) {
         internalField0149.player.closeHandledScreen();
         this.internalField0518.internalMethod00701();
      } else if (internalField0149.currentScreen != null) {
         if (this.internalField0518.internalMethod02365(3000L)) {
            this.internalMethod10088();
         }
      } else {
         this.internalField0900 = AutoShulkerModule.InternalType0453.internalField1371;
         this.internalField0518.internalMethod00701();
      }
   }

   private void internalMethod10040() {
      if (internalField0149.currentScreen == null && this.internalMethod09288()) {
         if (this.internalField0518.internalMethod02365(150L)) {
            if (this.internalField1053 != -1 && this.internalField1056 < 3) {
               this.internalField1056++;
               InventoryUtils.internalMethod08821(this.internalField1053, 8);
               this.internalField0900 = AutoShulkerModule.InternalType0453.internalField1648;
               this.internalField0518.internalMethod00701();
            } else {
               this.internalMethod10088();
            }
         }
      } else {
         if (this.internalField0518.internalMethod02365(3000L)) {
            this.internalMethod10088();
         }
      }
   }

   private void internalMethod10042() {
      if (this.internalMethod09288() && this.internalField0518.internalMethod02365(500L)) {
         boolean localValue1 = this.internalMethod00187(internalField0149.player.playerScreenHandler.getSlot(this.internalField1053).getStack());
         boolean localValue2 = this.internalMethod00187(InventoryUtils.internalMethod02738(8).internalMethod03427());
         if (!localValue1 && localValue2) {
            this.internalField0900 = AutoShulkerModule.InternalType0453.internalField1371;
            this.internalField0518.internalMethod00701();
         } else {
            this.internalMethod10088();
         }
      }
   }

   private void internalMethod10088() {
      if (this.internalField1055 != -1) {
         InventoryUtils.internalMethod03663(this.internalField1055);
      }

      this.internalMethod10096();
   }

   private void internalMethod10090() {
      this.internalField0416.clear();
      this.internalField1201.clear();
      Arrays.fill(this.internalField0299, null);
      this.internalField0277 = false;
   }

   private void internalMethod10096() {
      this.internalField0900 = AutoShulkerModule.InternalType0453.internalField0900;
      this.internalField1099 = false;
      this.internalField1053 = -1;
      this.internalField1055 = -1;
      this.internalField1056 = 0;
      this.internalField0543.clear();
   }

   private boolean internalMethod09288() {
      GuiMoveModule localValue1 = RockstarClient.getInstance().getModuleManager().getModule(GuiMoveModule.class);
      return localValue1 == null || localValue1.internalMethod06994().isEmpty();
   }

   private InventorySlot internalMethod00566() {
      SlotCollection<InventorySlot> localValue1 = InventorySlots.internalMethod02872()
         .internalMethod07591(InventorySlots.internalMethod03558())
         .internalMethod07591(InventorySlots.internalMethod07766());
      return localValue1.internalMethod03297(stack -> this.internalMethod00187(stack));
   }

   private boolean internalMethod09290() {
      for (int localValue2 : this.internalField1201) {
         if (!internalField0149.player.getInventory().getStack(localValue2).isEmpty()) {
            return true;
         }
      }

      return false;
   }

   private boolean internalMethod00187(ItemStack localValue1) {
      return localValue1 != null && !localValue1.isEmpty() && this.internalMethod07808(localValue1)
         ? localValue1.getName().getString().toLowerCase().contains("\u0440\u044e\u043a\u0437\u0430\u043a")
         : false;
   }

   private boolean internalMethod07808(ItemStack localValue1) {
      return localValue1.getItem() instanceof BlockItem localValue2 && localValue2.getBlock() instanceof ShulkerBoxBlock;
   }

   private boolean internalMethod09469() {
      if (internalField0149.player != null && internalField0149.currentScreen instanceof HandledScreen) {
         ScreenHandler localValue1 = internalField0149.player.currentScreenHandler;
         return localValue1 instanceof GenericContainerScreenHandler || localValue1 instanceof ShulkerBoxScreenHandler;
      } else {
         return false;
      }
   }

   private int internalMethod04125(ScreenHandler localValue1) {
      int localValue2 = 0;

      while (true) {
         if (localValue2 >= localValue1.slots.size()) {
            return -1;
         }

         Slot localValue3 = localValue1.getSlot(localValue2);
         if (localValue3.inventory == internalField0149.player.getInventory() && this.internalField1201.contains(localValue3.getIndex())) {
            ItemStack localValue4 = localValue3.getStack();
            if (!localValue4.isEmpty()) {
               ItemStack localValue5 = this.internalField0543.get(localValue2);
               if (localValue5 == null) {
                  break;
               }

               if (!ItemStack.areEqual(localValue5, localValue4)) {
                  this.internalField0543.remove(localValue2);
                  break;
               }
            }
         }

         localValue2++;
      }

      return localValue2;
   }

   private boolean internalMethod08806(ItemStack localValue1) {
      if (localValue1 != null && !localValue1.isEmpty() && !this.internalMethod00187(localValue1)) {
         Item localValue2 = localValue1.getItem();
         if (!this.internalField1488.isSelected() || CustomItemUtils.internalMethod03238(localValue1) == null && !ItemNbtUtils.internalMethod01122(localValue1)) {
            if (this.internalField1075.isSelected() && localValue2 == Items.ENCHANTED_BOOK) {
               return true;
            } else if (!this.internalField0245.isSelected()
               || !internalField1200.contains(localValue2) && (!internalField1199.contains(localValue2) || !localValue1.hasEnchantments())) {
               if (!this.internalField0244.isSelected()
                  || localValue2 != Items.NETHERITE_SWORD
                     && !(localValue2 instanceof AxeItem)
                     && !(localValue2 instanceof MaceItem)
                     && !(localValue2 instanceof TridentItem)
                     && !(localValue2 instanceof BowItem)
                     && !(localValue2 instanceof CrossbowItem)) {
                  if (this.internalField1074.isSelected() && localValue2 == Items.TOTEM_OF_UNDYING) {
                     return true;
                  } else if (this.internalField1073.isSelected() && internalField1202.contains(localValue2)) {
                     return true;
                  } else {
                     return this.internalField1072.isSelected() && internalField0546.contains(localValue2)
                        ? true
                        : this.internalField1491.isSelected() && (internalField0545.contains(localValue2) || this.internalMethod07808(localValue1));
                  }
               } else {
                  return true;
               }
            } else {
               return true;
            }
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.internalField0276 = false;
      this.internalMethod10090();
      this.internalMethod10096();
   }

   @Override
   public void onDisable() {
      super.onDisable();
      if (this.internalField0900 != AutoShulkerModule.InternalType0453.internalField0900
         && internalField0149.player != null
         && internalField0149.interactionManager != null) {
         if (this.internalMethod09469()) {
            internalField0149.player.closeHandledScreen();
         }

         if (this.internalField1053 != -1) {
            InventoryUtils.internalMethod08821(this.internalField1053, 8);
         }

         if (this.internalField1055 != -1) {
            InventoryUtils.internalMethod03663(this.internalField1055);
         }
      }

      this.internalField0276 = false;
      this.internalMethod10090();
      this.internalMethod10096();
   }

   static enum InternalType0453 {
      internalField0900,
      internalField0901,
      internalField1372,
      internalField1369,
      internalField1370,
      internalField1371,
      internalField1648;
   }
}
