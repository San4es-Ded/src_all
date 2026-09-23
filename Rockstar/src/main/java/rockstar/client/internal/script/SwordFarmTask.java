package rockstar.client.internal.script;









import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.rotation.*;
import rockstar.client.inventory.*;
import rockstar.client.event.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import rockstar.modules.combat.*;
import rockstar.modules.movement.*;
import rockstar.modules.visual.*;
import rockstar.modules.player.*;
import rockstar.modules.other.*;

import java.util.function.Predicate;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeResultCollection;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.c2s.play.CraftRequestC2SPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.recipe.NetworkRecipeId;
import net.minecraft.recipe.RecipeDisplayEntry;
import net.minecraft.recipe.display.SlotDisplayContexts;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.context.ContextParameterMap;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import pyrock.events.network.ReceivePacketEvent;
import pyrock.events.player.ClientPlayerTickEvent;

public class SwordFarmTask extends AutoFarmModeState {
   private final TextSetting internalField0384;
   private final SliderSetting internalField0383;
   private final BooleanSetting internalField0650;
   private SwordFarmTask.InternalType0172 internalField0903;
   private final Stopwatch internalField0519;
   private final Stopwatch internalField0518;
   private final Stopwatch internalField1189;
   private BlockPos internalField0352;
   private Rotation internalField0118;
   private int internalField1055;
   private boolean internalField0277;
   private NetworkRecipeId internalField0588;
   private int internalField1056;
   private int internalField1054;
   private final EventListener<ClientPlayerTickEvent> internalField0157;
   private final EventListener<ReceivePacketEvent> internalField0158;

   public SwordFarmTask(AutoFarmModule localValue1, ModeSetting localValue2) {
      super(localValue1, localValue2, "modules.settings.auto_farm.modes.sword");
      this.internalField0903 = SwordFarmTask.InternalType0172.internalField0903;
      this.internalField0519 = new Stopwatch();
      this.internalField0518 = new Stopwatch();
      this.internalField1189 = new Stopwatch();
      this.internalField1056 = -1;
      this.internalField1054 = 0;
      this.internalField0157 = localValue1x -> {
         if (internalField0149.player != null && internalField0149.world != null) {
            if (this.internalField0903 == SwordFarmTask.InternalType0172.internalField0903 && !this.internalMethod02548(Items.STICK)) {
               this.internalMethod05961("modules.sword_farm.no_sticks");
               this.internalMethod08387();
            } else {
               switch (this.internalField0903) {
                  case internalField0903:
                     this.internalMethod08539();
                     break;
                  case internalField0902:
                     this.internalMethod08548();
                     break;
                  case internalField1373:
                     this.internalMethod09615();
                     break;
                  case internalField1374:
                     this.internalMethod09616();
                     break;
                  case internalField1375:
                     this.internalMethod09621();
                     break;
                  case internalField1376:
                     this.internalMethod03608(SwordFarmTask.InternalType0172.internalField0903);
                     break;
                  case internalField1651:
                     this.internalMethod09622();
                     break;
                  case internalField1650:
                     this.internalMethod09779();
                     break;
                  case internalField1652:
                     this.internalMethod09780();
                     break;
                  case internalField1649:
                     this.internalMethod03608(SwordFarmTask.InternalType0172.internalField0903);
                     break;
                  case internalField1653:
                     this.internalMethod09788();
                     break;
                  case internalField1656:
                     this.internalMethod09789();
                     break;
                  case internalField1655:
                     this.internalMethod10030();
                     break;
                  case internalField1654:
                     this.internalMethod10031();
                     break;
                  case internalField1833:
                     this.internalMethod10037();
                     break;
                  case internalField1832:
                     this.internalMethod10038();
                     break;
                  case internalField1831:
                     this.internalMethod10085();
                     break;
                  case internalField1830:
                     this.internalMethod03608(SwordFarmTask.InternalType0172.internalField1653);
               }
            }
         }
      };
      this.internalField0158 = localValue1x -> {
         if (localValue1x.getPacket() instanceof GameMessageS2CPacket localValue2x) {
            String localValue4 = localValue2x.content().getString();
            if (localValue4.contains("\u0423 \u0412\u0430\u0441 \u043a\u0443\u043f\u0438\u043b\u0438")) {
               this.internalField0277 = true;
               this.internalMethod00320().internalMethod08501();
            }
         }
      };
      this.internalField0384 = new TextSetting(localValue1, "modules.settings.auto_farm.sword.price", () -> !this.isSelected())
         .internalMethod00011("15000")
         .internalMethod07009(true);
      this.internalField0383 = new SliderSetting(localValue1, "modules.settings.auto_farm.sword.relist_cooldown", () -> !this.isSelected())
         .internalMethod08673(5.0F)
         .internalMethod05900(5.0F)
         .internalMethod02732(300.0F)
         .internalMethod08074(60.0F)
         .internalMethod06240("sec");
      this.internalField0650 = new BooleanSetting(localValue1, "modules.settings.auto_farm.sword.craft_all", () -> !this.isSelected());
   }

   @Override
   public void internalMethod04694() {
      if (internalField0149.player == null || internalField0149.world == null) {
         this.internalMethod08387();
      } else if (!this.internalMethod02548(Items.STICK)) {
         this.internalMethod05961("modules.sword_farm.no_sticks");
         this.internalMethod08387();
      } else {
         this.internalField0903 = SwordFarmTask.InternalType0172.internalField0903;
         this.internalField0352 = null;
         this.internalField0118 = null;
         this.internalField1055 = 0;
         this.internalField0277 = false;
         this.internalField0588 = null;
         this.internalField0519.internalMethod00701();
      }
   }

   @Override
   public void internalMethod04697() {
      this.internalField0903 = SwordFarmTask.InternalType0172.internalField0903;
      this.internalField0352 = null;
      this.internalField0118 = null;
      this.internalField1055 = 0;
      this.internalField0588 = null;
   }

   @Override
   public AutoFarmPhase internalMethod02315() {
      return switch (this.internalField0903) {
         case internalField0903 -> AutoFarmPhase.internalField0848;
         case internalField0902, internalField1373, internalField1374, internalField1375, internalField1376 -> AutoFarmPhase.internalField1634;
         case internalField1651, internalField1650, internalField1652, internalField1649 -> AutoFarmPhase.internalField1635;
         case internalField1653, internalField1656, internalField1655, internalField1654, internalField1833, internalField1832, internalField1831, internalField1830 -> AutoFarmPhase.internalField1636;
      };
   }

   @Override
   public AutoFarmUnit internalMethod02317() {
      return AutoFarmUnit.internalField1351;
   }

   @Override
   public ItemStack internalMethod04126() {
      return new ItemStack(Items.DIAMOND_SWORD);
   }

   private void internalMethod08539() {
      if (this.internalField0519.internalMethod02365(350L)) {
         boolean localValue1 = this.internalMethod04735() > 0;
         if (!localValue1 || this.internalMethod02548(Items.EMERALD) && this.internalMethod04733() > 4) {
            if (!this.internalMethod02548(Items.EMERALD)) {
               this.internalMethod01490(SwordFarmTask.InternalType0172.internalField0902);
            } else {
               this.internalMethod01490(SwordFarmTask.InternalType0172.internalField1651);
            }
         } else {
            this.internalMethod01490(SwordFarmTask.InternalType0172.internalField1653);
         }
      }
   }

   private void internalMethod08548() {
      if (this.internalField0519.internalMethod02365(350L)) {
         internalField0149.player.networkHandler.sendChatCommand("shop");
         this.internalMethod01490(SwordFarmTask.InternalType0172.internalField1373);
      }
   }

   private void internalMethod09615() {
      if (this.internalField0519.internalMethod02365(600L)) {
         ScreenHandler localValue1 = this.internalMethod04596();
         if (localValue1 != null) {
            int localValue2 = internalMethod00115(localValue1, localValue0 -> localValue0.getItem() == Items.GOLD_INGOT);
            if (localValue2 == -1) {
               if (this.internalField0518.internalMethod02365(5000L)) {
                  this.internalMethod05961("modules.sword_farm.shop_no_gold");
                  this.internalMethod08387();
               }
            } else {
               internalField0149.interactionManager.clickSlot(localValue1.syncId, localValue2, 0, SlotActionType.PICKUP, internalField0149.player);
               this.internalMethod01490(SwordFarmTask.InternalType0172.internalField1374);
            }
         }
      }
   }

   private void internalMethod09616() {
      if (this.internalField0519.internalMethod02365(600L)) {
         ScreenHandler localValue1 = this.internalMethod04596();
         if (localValue1 != null) {
            int localValue2 = internalMethod01853(localValue1, "\u0418\u0437\u0443\u043c\u0440\u0443\u0434");
            if (localValue2 == -1) {
               if (this.internalField0518.internalMethod02365(5000L)) {
                  this.internalMethod05961("modules.sword_farm.shop_no_emerald");
                  this.internalMethod08387();
               }
            } else {
               internalField0149.interactionManager.clickSlot(localValue1.syncId, localValue2, 1, SlotActionType.PICKUP, internalField0149.player);
               this.internalMethod01490(SwordFarmTask.InternalType0172.internalField1375);
            }
         }
      }
   }

   private void internalMethod09621() {
      if (this.internalField0519.internalMethod02365(600L)) {
         ScreenHandler localValue1 = this.internalMethod04596();
         if (localValue1 != null) {
            int localValue2 = internalMethod00115(localValue1, localValue0 -> localValue0.getItem() == Items.LIME_STAINED_GLASS_PANE);
            if (localValue2 == -1) {
               if (this.internalField0518.internalMethod02365(5000L)) {
                  this.internalMethod05961("modules.sword_farm.shop_no_confirm");
                  this.internalMethod08387();
               }
            } else {
               internalField0149.interactionManager.clickSlot(localValue1.syncId, localValue2, 0, SlotActionType.PICKUP, internalField0149.player);
               this.internalMethod01490(SwordFarmTask.InternalType0172.internalField1376);
            }
         }
      }
   }

   private void internalMethod09622() {
      if (this.internalField0352 == null || !this.internalMethod05520(this.internalField0352) || !this.internalMethod06798(this.internalField0352)) {
         this.internalField0352 = this.internalMethod05644();
      }

      if (this.internalField0352 == null) {
         this.internalMethod05961("modules.sword_farm.no_crafting_table");
         this.internalMethod08387();
      } else {
         this.internalMethod01490(SwordFarmTask.InternalType0172.internalField1650);
      }
   }

   private void internalMethod09779() {
      if (internalField0149.currentScreen instanceof HandledScreen) {
         this.internalMethod01490(SwordFarmTask.InternalType0172.internalField1652);
      } else if (this.internalField0352 != null && this.internalMethod05520(this.internalField0352)) {
         Vec3d localValue1 = Vec3d.ofCenter(this.internalField0352);
         if (this.internalMethod03716(localValue1)) {
            if (this.internalField0519.internalMethod02365(350L)) {
               BlockHitResult localValue2 = new BlockHitResult(localValue1, Direction.UP, this.internalField0352, false);
               internalField0149.interactionManager.interactBlock(internalField0149.player, Hand.MAIN_HAND, localValue2);
               internalField0149.player.swingHand(Hand.MAIN_HAND);
               this.internalField0519.internalMethod00701();
            }
         }
      } else {
         this.internalMethod01490(SwordFarmTask.InternalType0172.internalField1651);
      }
   }

   private void internalMethod09780() {
      if (this.internalField0519.internalMethod02365(350L)) {
         if (internalField0149.player.currentScreenHandler instanceof CraftingScreenHandler localValue1) {
            if (!this.internalMethod02548(Items.STICK)) {
               this.internalMethod05961("modules.sword_farm.no_sticks");
               this.internalMethod08387();
            } else {
               boolean localValue6 = this.internalMethod02110(localValue1);
               boolean localValue3 = localValue1.getSlot(0).getStack().isEmpty();
               int localValue4 = this.internalMethod04733();
               boolean localValue5 = !this.internalMethod02548(Items.EMERALD) || this.internalMethod04735() > 0 && localValue4 <= 4;
               if (localValue5) {
                  if (!localValue3 && localValue4 > 0) {
                     internalField0149.interactionManager.clickSlot(localValue1.syncId, 0, 0, SlotActionType.QUICK_MOVE, internalField0149.player);
                     this.internalField0519.internalMethod00701();
                  } else if (!localValue6 && localValue4 > 0) {
                     this.internalMethod02109(localValue1);
                     this.internalField0519.internalMethod00701();
                  } else {
                     this.internalMethod01490(SwordFarmTask.InternalType0172.internalField1649);
                  }
               } else if (!localValue3) {
                  internalField0149.interactionManager.clickSlot(localValue1.syncId, 0, 0, SlotActionType.QUICK_MOVE, internalField0149.player);
                  this.internalField0519.internalMethod00701();
               } else {
                  if (this.internalField0588 == null) {
                     this.internalField0588 = this.internalMethod05501();
                  }

                  if (this.internalField0588 == null) {
                     if (this.internalField0518.internalMethod02365(5000L)) {
                        this.internalMethod05961("modules.sword_farm.recipe_not_found");
                        this.internalMethod08387();
                     }
                  } else {
                     internalField0149.player
                        .networkHandler
                        .sendPacket(new CraftRequestC2SPacket(localValue1.syncId, this.internalField0588, this.internalField0650.internalMethod04496()));
                     this.internalField0519.internalMethod00701();
                  }
               }
            }
         } else {
            this.internalMethod01490(SwordFarmTask.InternalType0172.internalField1650);
         }
      }
   }

   private void internalMethod02109(CraftingScreenHandler localValue1) {
      for (int localValue2 = 1; localValue2 <= 9 && localValue2 < localValue1.slots.size(); localValue2++) {
         if (!localValue1.getSlot(localValue2).getStack().isEmpty()) {
            internalField0149.interactionManager.clickSlot(localValue1.syncId, localValue2, 0, SlotActionType.QUICK_MOVE, internalField0149.player);
         }
      }
   }

   private int internalMethod04733() {
      int localValue1 = 0;

      for (MainInventorySlot localValue3 : InventorySlots.internalMethod03558().internalMethod02638()) {
         if (localValue3.internalMethod06664()) {
            localValue1++;
         }
      }

      for (HotbarSlot localValue5 : InventorySlots.internalMethod02872().internalMethod02638()) {
         if (localValue5.internalMethod06664()) {
            localValue1++;
         }
      }

      return localValue1;
   }

   private NetworkRecipeId internalMethod05501() {
      if (internalField0149.player != null && internalField0149.world != null) {
         ClientRecipeBook localValue1 = internalField0149.player.getRecipeBook();
         if (localValue1 == null) {
            return null;
         } else {
            ContextParameterMap localValue2 = SlotDisplayContexts.createParameters(internalField0149.world);
            NetworkRecipeId localValue3 = null;

            for (RecipeResultCollection localValue5 : localValue1.getOrderedResults()) {
               for (RecipeDisplayEntry localValue7 : localValue5.getAllRecipes()) {
                  for (ItemStack localValue9 : localValue7.getStacks(localValue2)) {
                     if (!localValue9.isEmpty()) {
                        if (internalMethod02958(localValue9)) {
                           return localValue7.id();
                        }

                        if (localValue3 == null && internalMethod08935(localValue9)) {
                           localValue3 = localValue7.id();
                        }
                     }
                  }
               }
            }

            return localValue3;
         }
      } else {
         return null;
      }
   }

   private boolean internalMethod02110(CraftingScreenHandler localValue1) {
      for (int localValue2 = 1; localValue2 <= 9 && localValue2 < localValue1.slots.size(); localValue2++) {
         if (!localValue1.getSlot(localValue2).getStack().isEmpty()) {
            return false;
         }
      }

      return true;
   }

   private void internalMethod09788() {
      if (this.internalField0519.internalMethod02365(350L)) {
         if (this.internalMethod04735() == 0) {
            this.internalMethod01490(SwordFarmTask.InternalType0172.internalField0903);
         } else {
            String localValue1 = this.internalField0384.internalMethod08926() != null && !this.internalField0384.internalMethod08926().isBlank()
               ? this.internalField0384.internalMethod08926().trim()
               : "15000";
            internalField0149.player.networkHandler.sendChatCommand("ah sellgui " + localValue1);
            this.internalMethod01490(SwordFarmTask.InternalType0172.internalField1656);
         }
      }
   }

   private void internalMethod09789() {
      if (this.internalField0519.internalMethod02365(350L)) {
         ScreenHandler localValue1 = this.internalMethod04596();
         if (localValue1 != null) {
            if (this.internalMethod07597(localValue1) == 0) {
               this.internalMethod01490(SwordFarmTask.InternalType0172.internalField1655);
            } else {
               int localValue2 = this.internalMethod00402(localValue1);
               if (localValue2 == 0) {
                  this.internalMethod01490(SwordFarmTask.InternalType0172.internalField1655);
               } else {
                  if (this.internalField1056 != -1 && localValue2 >= this.internalField1056) {
                     this.internalField1054++;
                     if (this.internalField1054 >= 3) {
                        this.internalMethod01490(SwordFarmTask.InternalType0172.internalField1655);
                        return;
                     }
                  } else {
                     this.internalField1054 = 0;
                  }

                  this.internalField1056 = localValue2;

                  for (int localValue3 = 0; localValue3 < localValue1.slots.size(); localValue3++) {
                     Slot localValue4 = (Slot)localValue1.slots.get(localValue3);
                     if (localValue4.inventory == internalField0149.player.getInventory() && internalMethod05931(localValue4.getStack())) {
                        internalField0149.interactionManager.clickSlot(localValue1.syncId, localValue3, 0, SlotActionType.QUICK_MOVE, internalField0149.player);
                        break;
                     }
                  }

                  this.internalField0519.internalMethod00701();
               }
            }
         }
      }
   }

   private int internalMethod00402(ScreenHandler localValue1) {
      int localValue2 = 0;

      for (Slot localValue4 : localValue1.slots) {
         if (localValue4.inventory == internalField0149.player.getInventory() && internalMethod05931(localValue4.getStack())) {
            localValue2 += localValue4.getStack().getCount();
         }
      }

      return localValue2;
   }

   private int internalMethod07597(ScreenHandler localValue1) {
      int localValue2 = 0;

      for (Slot localValue4 : localValue1.slots) {
         if (localValue4.inventory != internalField0149.player.getInventory() && localValue4.getStack().isEmpty()) {
            localValue2++;
         }
      }

      return localValue2;
   }

   private void internalMethod10030() {
      if (this.internalField0519.internalMethod02365(350L)) {
         ScreenHandler localValue1 = this.internalMethod04596();
         if (localValue1 == null) {
            this.internalMethod01490(SwordFarmTask.InternalType0172.internalField1653);
         } else {
            int localValue2 = internalMethod01853(localValue1, "\u041f\u043e\u0434\u0442\u0432\u0435\u0440\u0434\u0438\u0442\u044c");
            if (localValue2 == -1) {
               if (this.internalField0518.internalMethod02365(5000L)) {
                  this.internalMethod05961("modules.sword_farm.no_lime_dye");
                  this.internalMethod08387();
               }
            } else {
               internalField0149.interactionManager.clickSlot(localValue1.syncId, localValue2, 0, SlotActionType.PICKUP, internalField0149.player);
               this.internalField0277 = false;
               this.internalField1189.internalMethod00701();
               this.internalMethod10086();
               this.internalMethod01490(SwordFarmTask.InternalType0172.internalField1654);
            }
         }
      }
   }

   private void internalMethod10031() {
      if (this.internalField0277) {
         this.internalField0277 = false;
         this.internalMethod01490(SwordFarmTask.InternalType0172.internalField0903);
      } else {
         if (this.internalField1189.internalMethod02365((long)this.internalField0383.internalMethod08576() * 1000L)) {
            this.internalMethod01490(SwordFarmTask.InternalType0172.internalField1833);
         }
      }
   }

   private void internalMethod10037() {
      if (this.internalField0519.internalMethod02365(350L)) {
         this.internalMethod10086();
         internalField0149.player.networkHandler.sendChatCommand("ah");
         this.internalMethod01490(SwordFarmTask.InternalType0172.internalField1832);
      }
   }

   private void internalMethod10038() {
      if (this.internalField0519.internalMethod02365(650L)) {
         ScreenHandler localValue1 = this.internalMethod04596();
         if (localValue1 != null) {
            int localValue2 = internalMethod01853(localValue1, "\u0425\u0440\u0430\u043d\u0438\u043b\u0438\u0449\u0435");
            if (localValue2 == -1) {
               if (this.internalField0518.internalMethod02365(5000L)) {
                  this.internalMethod05961("modules.sword_farm.no_storage");
                  this.internalMethod08387();
               }
            } else {
               internalField0149.interactionManager.clickSlot(localValue1.syncId, localValue2, 0, SlotActionType.PICKUP, internalField0149.player);
               this.internalMethod01490(SwordFarmTask.InternalType0172.internalField1831);
            }
         }
      }
   }

   private void internalMethod10085() {
      if (this.internalField0519.internalMethod02365(350L)) {
         ScreenHandler localValue1 = this.internalMethod04596();
         if (localValue1 != null) {
            int localValue2 = this.internalMethod08086(localValue1);
            if (localValue2 == 0) {
               this.internalMethod01490(SwordFarmTask.InternalType0172.internalField1830);
            } else {
               if (this.internalField1056 != -1 && localValue2 >= this.internalField1056) {
                  this.internalField1054++;
                  if (this.internalField1054 >= 3) {
                     this.internalMethod01490(SwordFarmTask.InternalType0172.internalField1830);
                     return;
                  }
               } else {
                  this.internalField1054 = 0;
               }

               this.internalField1056 = localValue2;

               for (int localValue3 = 0; localValue3 < localValue1.slots.size(); localValue3++) {
                  Slot localValue4 = (Slot)localValue1.slots.get(localValue3);
                  if (localValue4.inventory != internalField0149.player.getInventory() && internalMethod05931(localValue4.getStack())) {
                     internalField0149.interactionManager.clickSlot(localValue1.syncId, localValue3, 0, SlotActionType.QUICK_MOVE, internalField0149.player);
                     break;
                  }
               }

               this.internalField0519.internalMethod00701();
            }
         }
      }
   }

   private int internalMethod08086(ScreenHandler localValue1) {
      int localValue2 = 0;

      for (Slot localValue4 : localValue1.slots) {
         if (localValue4.inventory != internalField0149.player.getInventory() && internalMethod05931(localValue4.getStack())) {
            localValue2 += localValue4.getStack().getCount();
         }
      }

      return localValue2;
   }

   private void internalMethod03608(SwordFarmTask.InternalType0172 localValue1) {
      if (this.internalField0519.internalMethod02365(350L)) {
         this.internalMethod10086();
         this.internalMethod01490(localValue1);
      }
   }

   private void internalMethod10086() {
      if (internalField0149.player != null) {
         if (internalField0149.player.currentScreenHandler != null
            && internalField0149.player.currentScreenHandler != internalField0149.player.playerScreenHandler) {
            internalField0149.player.networkHandler.sendPacket(new CloseHandledScreenC2SPacket(internalField0149.player.currentScreenHandler.syncId));
         }

         internalField0149.player.closeHandledScreen();
      }
   }

   private ScreenHandler internalMethod04596() {
      if (!(internalField0149.currentScreen instanceof HandledScreen)) {
         return null;
      } else if (internalField0149.player == null) {
         return null;
      } else {
         ScreenHandler localValue1 = internalField0149.player.currentScreenHandler;
         return localValue1 == internalField0149.player.playerScreenHandler ? null : localValue1;
      }
   }

   private boolean internalMethod03716(Vec3d localValue1) {
      Rotation localValue2 = RotationUtils.internalMethod05580(localValue1);
      if (this.internalField0118 == null || this.internalField0118.internalMethod00735(localValue2) > 0.5F) {
         this.internalField0118 = localValue2;
         this.internalField1055 = 0;
      }

      RockstarClient.getInstance()
         .internalMethod02368()
         .internalMethod00418(localValue2, RotationBehavior.internalField1003, 180.0F, 180.0F, 180.0F, RotationPriority.internalField1012);
      Rotation localValue3 = RockstarClient.getInstance().internalMethod02368().internalMethod09074();
      if (localValue3 != null && localValue3.internalMethod00735(localValue2) <= 1.5F) {
         this.internalField1055++;
         return this.internalField1055 >= 1;
      } else {
         return false;
      }
   }

   private void internalMethod01490(SwordFarmTask.InternalType0172 localValue1) {
      this.internalField0903 = localValue1;
      this.internalField0519.internalMethod00701();
      this.internalField0518.internalMethod00701();
      this.internalField0118 = null;
      this.internalField1055 = 0;
      this.internalField1056 = -1;
      this.internalField1054 = 0;
   }

   private boolean internalMethod02548(Item localValue1) {
      return InventorySlots.internalMethod02872().internalMethod05924(localValue1) || InventorySlots.internalMethod03558().internalMethod05924(localValue1);
   }

   private int internalMethod04735() {
      int localValue1 = 0;

      for (MainInventorySlot localValue3 : InventorySlots.internalMethod03558().internalMethod02638()) {
         if (internalMethod05931(localValue3.internalMethod03427())) {
            localValue1++;
         }
      }

      for (HotbarSlot localValue5 : InventorySlots.internalMethod02872().internalMethod02638()) {
         if (internalMethod05931(localValue5.internalMethod03427())) {
            localValue1++;
         }
      }

      return localValue1;
   }

   private static boolean internalMethod05931(ItemStack localValue0) {
      return localValue0.isEmpty() ? false : internalMethod02958(localValue0) || internalMethod08935(localValue0);
   }

   private static boolean internalMethod02958(ItemStack localValue0) {
      return localValue0.getName().getString().contains("\u0418\u0437\u0443\u043c\u0440\u0443\u0434\u043d\u044b\u0439 \u043c\u0435\u0447");
   }

   private static boolean internalMethod08935(ItemStack localValue0) {
      return localValue0.getItem() == Items.DIAMOND_SWORD && EnchantmentUtils.internalMethod03526(localValue0, Enchantments.SHARPNESS) == 3;
   }

   private static int internalMethod00115(ScreenHandler localValue0, Predicate<ItemStack> localValue1) {
      for (int localValue2 = 0; localValue2 < localValue0.slots.size(); localValue2++) {
         Slot localValue3 = (Slot)localValue0.slots.get(localValue2);
         if (localValue3.inventory != MinecraftClient.getInstance().player.getInventory() && localValue1.test(localValue3.getStack())) {
            return localValue2;
         }
      }

      return -1;
   }

   private static int internalMethod01853(ScreenHandler localValue0, String localValue1) {
      for (int localValue2 = 0; localValue2 < localValue0.slots.size(); localValue2++) {
         Slot localValue3 = (Slot)localValue0.slots.get(localValue2);
         if (localValue3.inventory != MinecraftClient.getInstance().player.getInventory()) {
            ItemStack localValue4 = localValue3.getStack();
            if (!localValue4.isEmpty() && localValue4.getName().getString().contains(localValue1)) {
               return localValue2;
            }
         }
      }

      return -1;
   }

   private boolean internalMethod05520(BlockPos localValue1) {
      return internalField0149.world.getBlockState(localValue1).getBlock() == Blocks.CRAFTING_TABLE;
   }

   private boolean internalMethod06798(BlockPos localValue1) {
      double localValue2 = internalField0149.player.getBlockInteractionRange() + 0.5;
      return internalField0149.player.getEyePos().squaredDistanceTo(Vec3d.ofCenter(localValue1)) <= localValue2 * localValue2;
   }

   private BlockPos internalMethod05644() {
      BlockPos localValue1 = internalField0149.player.getBlockPos();
      Vec3d localValue2 = internalField0149.player.getEyePos();
      double localValue3 = internalField0149.player.getBlockInteractionRange();
      double localValue5 = localValue3 * localValue3;
      BlockPos localValue7 = null;
      double localValue8 = Double.MAX_VALUE;

      for (int localValue10 = -6; localValue10 <= 6; localValue10++) {
         for (int localValue11 = -6; localValue11 <= 6; localValue11++) {
            for (int localValue12 = -6; localValue12 <= 6; localValue12++) {
               BlockPos localValue13 = localValue1.add(localValue10, localValue11, localValue12);
               if (internalField0149.world.getBlockState(localValue13).getBlock() == Blocks.CRAFTING_TABLE) {
                  double localValue14 = localValue2.squaredDistanceTo(Vec3d.ofCenter(localValue13));
                  if (!(localValue14 > localValue5) && localValue14 < localValue8) {
                     localValue8 = localValue14;
                     localValue7 = localValue13;
                  }
               }
            }
         }
      }

      return localValue7;
   }

   static enum InternalType0172 {
      internalField0903,
      internalField0902,
      internalField1373,
      internalField1374,
      internalField1375,
      internalField1376,
      internalField1651,
      internalField1650,
      internalField1652,
      internalField1649,
      internalField1653,
      internalField1656,
      internalField1655,
      internalField1654,
      internalField1833,
      internalField1832,
      internalField1831,
      internalField1830;
   }
}
