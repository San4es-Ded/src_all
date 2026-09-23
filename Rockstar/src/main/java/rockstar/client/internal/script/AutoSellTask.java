package rockstar.client.internal.script;













import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.rotation.*;
import rockstar.client.render.*;
import rockstar.client.inventory.*;
import rockstar.client.i18n.*;
import rockstar.client.event.*;
import rockstar.client.internal.ui.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import rockstar.modules.combat.*;
import rockstar.modules.movement.*;
import rockstar.modules.visual.*;
import rockstar.modules.player.*;
import rockstar.modules.other.*;

import rockstar.client.compat.RenderSystem;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.Generated;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.enums.ChestType;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import pyrock.events.network.ReceivePacketEvent;
import pyrock.events.player.ClientPlayerTickEvent;

public class AutoSellTask extends AutoFarmModeState {
   private final TextSetting internalField0384;
   private final TextSetting internalField0385;
   private final SliderSetting internalField0383;
   private final ButtonSetting internalField0663;
   private static final float internalField0205 = 1.5F;
   private HudContextMenu internalField0574;
   private AutoSellTask.InternalType0360 internalField0479;
   private Item internalField0152;
   ItemStack internalField0878;
   private boolean internalField0277;
   private final Stopwatch internalField0519;
   private final Stopwatch internalField0518;
   private final Stopwatch internalField1189;
   private boolean internalField0276;
   private final Deque<BlockPos> internalField0796;
   private final Set<BlockPos> internalField0546;
   private final Set<Integer> internalField0545;
   private BlockPos internalField0352;
   private int internalField1053;
   private ScreenHandler internalField0876;
   private String internalField1077;
   private boolean internalField1099;
   private int internalField1055;
   private int internalField1056;
   private Rotation internalField0118;
   private int internalField1054;
   private String internalField1076;
   private final EventListener<ReceivePacketEvent> internalField0157;
   private final EventListener<ClientPlayerTickEvent> internalField0158;

   public AutoSellTask(AutoFarmModule localValue1, ModeSetting localValue2) {
      super(localValue1, localValue2, "modules.settings.auto_farm.modes.auto_sell");
      this.internalField0479 = AutoSellTask.InternalType0360.internalField0479;
      this.internalField0878 = ItemStack.EMPTY;
      this.internalField0519 = new Stopwatch();
      this.internalField0518 = new Stopwatch();
      this.internalField1189 = new Stopwatch();
      this.internalField0796 = new ArrayDeque<>();
      this.internalField0546 = new HashSet<>();
      this.internalField0545 = new HashSet<>();
      this.internalField1077 = "";
      this.internalField1055 = -1;
      this.internalField0157 = localValue1x -> {
         if (localValue1x.getPacket() instanceof GameMessageS2CPacket localValue2x) {
            if (localValue2x.content().getString().contains("\u0423 \u0412\u0430\u0441 \u043a\u0443\u043f\u0438\u043b\u0438")) {
               this.internalField0276 = true;
               this.internalMethod00320().internalMethod08501();
            }

            if (localValue2x.content()
               .getString()
               .contains("\u0441\u043b\u0438\u0448\u043a\u043e\u043c \u0434\u043e\u0440\u043e\u0433\u043e. \u0412\u0432\u0435\u0434\u0438\u0442\u0435")) {
               internalField0149.player.networkHandler.sendChatCommand(this.internalField1076);
               this.internalField1053 = 0;
               this.internalField0545.clear();
               this.internalMethod06224(AutoSellTask.InternalType0360.internalField1541);
            }
         }
      };
      this.internalField0158 = localValue1x -> {
         if (internalField0149.player != null && internalField0149.world != null) {
            if (this.internalField0277) {
               switch (this.internalField0479) {
                  case internalField0478:
                     this.internalMethod09740();
                     break;
                  case internalField1171:
                     this.internalMethod09744();
                     break;
                  case internalField1172:
                     this.internalMethod09745();
                     break;
                  case internalField1173:
                     this.internalMethod09930();
                     break;
                  case internalField1174:
                     this.internalMethod09931();
                     break;
                  case internalField1541:
                     this.internalMethod09177();
                     break;
                  case internalField1542:
                     this.internalMethod09181();
                     break;
                  case internalField1540:
                     this.internalMethod09976();
                     break;
                  case internalField1543:
                     this.internalMethod09977();
                     break;
                  case internalField1545:
                     this.internalMethod09978();
                     break;
                  case internalField1546:
                     this.internalMethod09979();
                     break;
                  case internalField1544:
                     this.internalMethod10027();
               }
            }
         }
      };
      this.internalField0384 = new TextSetting(localValue1, "modules.settings.auto_sell.quantity", () -> !this.isSelected() || !this.internalMethod09002())
         .internalMethod00011("64")
         .internalMethod07009(true);
      this.internalField0385 = new TextSetting(localValue1, "modules.settings.auto_sell.price", () -> !this.isSelected() || !this.internalMethod09002())
         .internalMethod00011("10000")
         .internalMethod07009(true);
      this.internalField0383 = new SliderSetting(localValue1, "modules.settings.auto_sell.relist_cooldown", () -> !this.isSelected() || !this.internalMethod09002())
         .internalMethod08673(5.0F)
         .internalMethod05900(5.0F)
         .internalMethod02732(300.0F)
         .internalMethod08074(60.0F)
         .internalMethod06240("sec");
      this.internalField0663 = new ButtonSetting(localValue1, "modules.settings.auto_sell.confirm", () -> !this.isSelected() || !this.internalMethod09002())
         .internalMethod07149(this::internalMethod09739);
   }

   @Override
   public void internalMethod04694() {
      if (internalField0149.player != null && internalField0149.world != null) {
         ItemStack localValue1 = internalField0149.player.getMainHandStack();
         if (localValue1.isEmpty()) {
            ClientMessages.internalMethod09025(Text.of(LanguageManager.internalMethod07214("modules.auto_sell.no_item_in_hand")));
            this.internalMethod08387();
         } else {
            this.internalField0152 = localValue1.getItem();
            this.internalField0878 = localValue1.copy();
            this.internalField0277 = false;
            this.internalField0276 = false;
            this.internalField0796.clear();
            this.internalField0546.clear();
            this.internalField0352 = null;
            this.internalField0876 = null;
            this.internalField1053 = 0;
            this.internalField1099 = false;
            this.internalField1055 = -1;
            this.internalField1056 = 0;
            this.internalField0118 = null;
            this.internalField1054 = 0;
            this.internalField0519.internalMethod00701();
            this.internalMethod09011();
            this.internalField0479 = AutoSellTask.InternalType0360.internalField0479;
            internalField0149.setScreen(new AutoSellScreen(this));
         }
      } else {
         this.internalMethod08387();
      }
   }

   @Override
   public void internalMethod04697() {
      this.internalField0479 = AutoSellTask.InternalType0360.internalField0479;
      this.internalField0277 = false;
      this.internalMethod10029();
      if (internalField0149.currentScreen instanceof AutoSellScreen) {
         internalField0149.setScreen(null);
      }
   }

   public boolean internalMethod02403() {
      return this.internalField0063.isEnabled();
   }

   private boolean internalMethod09002() {
      return internalField0149.currentScreen instanceof AutoSellScreen;
   }

   private void internalMethod09011() {
      this.internalField0574 = new HudContextMenu(0.0F, 0.0F, 170.0F);
      this.internalField0574.internalMethod04738("modules.settings.auto_sell.title");
      this.internalField0574.internalMethod03869(new AutoSellTask.InternalType0359());
      this.internalField0574.internalMethod05995(this.internalField0384);
      this.internalField0574.internalMethod05995(this.internalField0385);
      this.internalField0574.internalMethod05995(this.internalField0383);
      this.internalField0574.internalMethod05995(this.internalField0663);
      this.internalField0574.internalMethod04092(() -> {});
   }

   private void internalMethod09739() {
      if (this.internalField0574 != null) {
         this.internalField0574.internalMethod05781(false);
      }
   }

   public void internalMethod09001() {
      if (!this.internalField0277) {
         this.internalField0277 = true;
         this.internalField0479 = AutoSellTask.InternalType0360.internalField0478;
         this.internalField0519.internalMethod00701();
         this.internalField0518.internalMethod00701();
      }
   }

   public boolean internalMethod02406() {
      return this.internalField0277;
   }

   @Override
   public AutoFarmPhase internalMethod02315() {
      return switch (this.internalField0479) {
         case internalField0479, internalField0478 -> AutoFarmPhase.internalField0848;
         case internalField1171, internalField1172, internalField1173 -> AutoFarmPhase.internalField1349;
         case internalField1174, internalField1541, internalField1542, internalField1540, internalField1543, internalField1545, internalField1546, internalField1544 -> AutoFarmPhase.internalField1636;
      };
   }

   @Override
   public AutoFarmUnit internalMethod02317() {
      return AutoFarmUnit.internalField1351;
   }

   @Override
   public ItemStack internalMethod04126() {
      return this.internalField0878 != null && !this.internalField0878.isEmpty() ? this.internalField0878 : new ItemStack(Items.EMERALD);
   }

   private boolean internalMethod09003() {
      int localValue1 = this.internalField0878.isEmpty() ? 64 : Math.max(1, this.internalField0878.getMaxCount());
      return this.internalMethod02405() >= 4 * localValue1;
   }

   private void internalMethod09740() {
      this.internalField0796.clear();
      this.internalField0546.clear();
      this.internalField0352 = null;
      if (this.internalMethod09003()) {
         this.internalMethod06224(AutoSellTask.InternalType0360.internalField1174);
      } else {
         BlockPos localValue1 = BlockPos.ofFloored(internalField0149.player.getEntityPos());

         for (BlockPos localValue3 : BlockPos.iterateOutwards(localValue1, 8, 8, 8)) {
            if (internalField0149.world.getBlockEntity(localValue3) instanceof ChestBlockEntity) {
               BlockPos localValue4 = localValue3.toImmutable();
               if (!this.internalField0546.contains(localValue4)) {
                  this.internalField0546.add(localValue4);
                  BlockPos localValue5 = this.internalMethod06252(localValue4);
                  if (localValue5 != null) {
                     this.internalField0546.add(localValue5);
                  }

                  this.internalField0796.add(localValue4);
               }
            }
         }

         this.internalMethod06224(AutoSellTask.InternalType0360.internalField1171);
      }
   }

   private void internalMethod09744() {
      if (this.internalMethod09003()) {
         this.internalMethod06224(AutoSellTask.InternalType0360.internalField1173);
      } else {
         if (this.internalField0352 == null) {
            this.internalField0352 = this.internalField0796.poll();
            if (this.internalField0352 == null) {
               this.internalMethod06224(AutoSellTask.InternalType0360.internalField1173);
               return;
            }

            this.internalField0118 = null;
            this.internalField1054 = 0;
         }

         if (internalField0149.player.currentScreenHandler instanceof GenericContainerScreenHandler) {
            this.internalMethod06224(AutoSellTask.InternalType0360.internalField1172);
         } else if (!this.internalMethod06814(this.internalField0352)
            || !(internalField0149.world.getBlockEntity(this.internalField0352) instanceof ChestBlockEntity)) {
            this.internalField0352 = null;
         } else if (this.internalMethod01633(Vec3d.ofCenter(this.internalField0352))) {
            if (this.internalField0519.internalMethod02365(350L)) {
               this.internalMethod06813(this.internalField0352);
               this.internalField0519.internalMethod00701();
            }
         }
      }
   }

   private void internalMethod09745() {
      if (internalField0149.player.currentScreenHandler instanceof GenericContainerScreenHandler localValue1) {
         if (this.internalField0519.internalMethod02365(350L)) {
            if (this.internalMethod09003()) {
               this.internalMethod10029();
               this.internalMethod06224(AutoSellTask.InternalType0360.internalField1173);
            } else {
               for (int localValue4 = 0; localValue4 < localValue1.slots.size(); localValue4++) {
                  Slot localValue3 = (Slot)localValue1.slots.get(localValue4);
                  if (localValue3.inventory != internalField0149.player.getInventory() && this.internalMethod03197(localValue3.getStack())) {
                     internalField0149.interactionManager.clickSlot(localValue1.syncId, localValue4, 0, SlotActionType.QUICK_MOVE, internalField0149.player);
                     this.internalField0519.internalMethod00701();
                     return;
                  }
               }

               this.internalMethod10029();
               this.internalField0352 = null;
               this.internalMethod06224(AutoSellTask.InternalType0360.internalField1171);
            }
         }
      } else {
         this.internalField0352 = null;
         this.internalMethod06224(AutoSellTask.InternalType0360.internalField1171);
      }
   }

   private void internalMethod09930() {
      if (this.internalField0519.internalMethod02365(350L)) {
         this.internalMethod10029();
         this.internalMethod06224(AutoSellTask.InternalType0360.internalField1174);
      }
   }

   private int internalMethod05273(TextSetting localValue1, int localValue2) {
      try {
         String localValue3 = localValue1.internalMethod08926();
         return localValue3 != null && !localValue3.isBlank() ? Math.max(1, Integer.parseInt(localValue3.trim().replaceAll("[^0-9]", ""))) : localValue2;
      } catch (NumberFormatException localValue4) {
         return localValue2;
      }
   }

   private int internalMethod02402() {
      return this.internalMethod05273(this.internalField0384, 64);
   }

   private void internalMethod09931() {
      if (this.internalField0519.internalMethod02365(350L)) {
         int localValue1 = this.internalMethod02402();
         if (this.internalMethod02405() < localValue1) {
            if (this.internalField1099) {
               this.internalMethod06224(AutoSellTask.InternalType0360.internalField1543);
            } else {
               this.internalMethod10028();
            }
         } else {
            String localValue2 = String.valueOf(this.internalMethod05273(this.internalField0385, 10000));
            this.internalField1076 = "ah sellgui " + localValue2;
            internalField0149.player.networkHandler.sendChatCommand(this.internalField1076);
            this.internalField1053 = 0;
            this.internalField0545.clear();
            this.internalMethod06224(AutoSellTask.InternalType0360.internalField1541);
         }
      }
   }

   private void internalMethod09177() {
      if (this.internalField0519.internalMethod02365(350L)) {
         ScreenHandler localValue1 = this.internalMethod06132();
         if (localValue1 == null) {
            if (this.internalField0518.internalMethod02365(5000L)) {
               this.internalMethod06224(AutoSellTask.InternalType0360.internalField1174);
            }
         } else {
            int localValue2 = this.internalMethod02402();
            int localValue3 = this.internalMethod08528(localValue1);
            if (localValue3 == -1 && this.internalField1053 == 0) {
               this.internalField1099 = true;
               this.internalMethod10029();
               this.internalMethod06224(AutoSellTask.InternalType0360.internalField1543);
            } else if (localValue3 != -1 && this.internalMethod07367(localValue1) >= localValue2) {
               this.internalField0545.add(localValue3);
               int localValue4 = this.internalMethod07367(localValue1);
               this.internalMethod04137(localValue1, localValue3, localValue2);
               if (this.internalMethod07367(localValue1) < localValue4) {
                  this.internalField1053++;
               }

               this.internalField0519.internalMethod00701();
            } else {
               this.internalMethod06224(AutoSellTask.InternalType0360.internalField1542);
            }
         }
      }
   }

   private void internalMethod04137(ScreenHandler localValue1, int localValue2, int localValue3) {
      int localValue4 = localValue3;

      for (int localValue5 = localValue3 + 64; localValue4 > 0 && localValue5-- > 0; localValue4--) {
         ItemStack localValue6 = localValue1.getCursorStack();
         if (!this.internalMethod03197(localValue6)) {
            int localValue7 = this.internalMethod08322(localValue1);
            if (localValue7 == -1) {
               break;
            }

            internalField0149.interactionManager.clickSlot(localValue1.syncId, localValue7, 0, SlotActionType.PICKUP, internalField0149.player);
            localValue6 = localValue1.getCursorStack();
            if (localValue6.isEmpty()) {
               break;
            }
         }

         internalField0149.interactionManager.clickSlot(localValue1.syncId, localValue2, 1, SlotActionType.PICKUP, internalField0149.player);
      }

      ItemStack localValue9 = localValue1.getCursorStack();
      if (!localValue9.isEmpty()) {
         int localValue10 = this.internalMethod08160(localValue1);
         if (localValue10 == -1) {
            localValue10 = this.internalMethod08322(localValue1);
         }

         if (localValue10 != -1) {
            internalField0149.interactionManager.clickSlot(localValue1.syncId, localValue10, 0, SlotActionType.PICKUP, internalField0149.player);
         }
      }
   }

   private void internalMethod09181() {
      if (this.internalField0519.internalMethod02365(350L)) {
         if (this.internalField1053 == 0) {
            this.internalMethod10029();
            if (this.internalField1099) {
               this.internalMethod06224(AutoSellTask.InternalType0360.internalField1540);
            } else {
               this.internalMethod10028();
            }
         } else {
            ScreenHandler localValue1 = this.internalMethod06132();
            if (localValue1 == null) {
               this.internalMethod06224(AutoSellTask.InternalType0360.internalField1174);
            } else {
               int localValue2 = this.internalMethod07415(localValue1, "\u041f\u043e\u0434\u0442\u0432\u0435\u0440\u0434\u0438\u0442\u044c");
               if (localValue2 == -1) {
                  if (this.internalField0518.internalMethod02365(5000L)) {
                     ClientMessages.internalMethod09025(Text.of(LanguageManager.internalMethod07214("modules.auto_sell.no_lime_dye")));
                     this.internalMethod08387();
                  }
               } else {
                  internalField0149.interactionManager.clickSlot(localValue1.syncId, localValue2, 0, SlotActionType.PICKUP, internalField0149.player);
                  this.internalField1099 = true;
                  this.internalField0276 = false;
                  this.internalField1189.internalMethod00701();
                  this.internalMethod10029();
                  this.internalMethod06224(AutoSellTask.InternalType0360.internalField1540);
               }
            }
         }
      }
   }

   private void internalMethod09976() {
      if (this.internalField0276) {
         this.internalField0276 = false;
         this.internalMethod06224(AutoSellTask.InternalType0360.internalField0478);
      } else {
         if (this.internalField1189.internalMethod02365((long)this.internalField0383.internalMethod08576() * 1000L)) {
            this.internalMethod06224(AutoSellTask.InternalType0360.internalField1543);
         }
      }
   }

   private void internalMethod09977() {
      if (this.internalField0519.internalMethod02365(350L)) {
         this.internalMethod10029();
         this.internalField0876 = null;
         this.internalField1055 = -1;
         this.internalField1056 = 0;
         internalField0149.player.networkHandler.sendChatCommand("ah");
         this.internalMethod06224(AutoSellTask.InternalType0360.internalField1545);
      }
   }

   private void internalMethod09978() {
      if (this.internalField0519.internalMethod02365(650L)) {
         ScreenHandler localValue1 = this.internalMethod06132();
         if (localValue1 == null) {
            if (this.internalField0518.internalMethod02365(5000L)) {
               this.internalMethod06224(AutoSellTask.InternalType0360.internalField1540);
            }
         } else {
            int localValue2 = this.internalMethod07415(localValue1, "\u0425\u0440\u0430\u043d\u0438\u043b\u0438\u0449\u0435");
            if (localValue2 == -1) {
               if (this.internalField0518.internalMethod02365(5000L)) {
                  this.internalMethod06224(AutoSellTask.InternalType0360.internalField1540);
               }
            } else {
               this.internalField0876 = localValue1;
               this.internalField1077 = internalField0149.currentScreen != null ? internalField0149.currentScreen.getTitle().getString() : "";
               internalField0149.interactionManager.clickSlot(localValue1.syncId, localValue2, 0, SlotActionType.PICKUP, internalField0149.player);
               this.internalMethod06224(AutoSellTask.InternalType0360.internalField1546);
            }
         }
      }
   }

   private void internalMethod09979() {
      if (this.internalField0519.internalMethod02365(350L)) {
         ScreenHandler localValue1 = this.internalMethod06132();
         if (localValue1 == null) {
            if (this.internalField0518.internalMethod02365(5000L)) {
               this.internalMethod06224(AutoSellTask.InternalType0360.internalField1540);
            }
         } else {
            String localValue2 = internalField0149.currentScreen != null ? internalField0149.currentScreen.getTitle().getString() : "";
            boolean localValue3 = localValue1 == this.internalField0876 && localValue2.equals(this.internalField1077);
            if (localValue3) {
               if (this.internalField0518.internalMethod02365(5000L)) {
                  this.internalMethod06224(AutoSellTask.InternalType0360.internalField1540);
               }
            } else {
               int localValue4 = this.internalMethod00132(localValue1);
               if (localValue4 == 0) {
                  this.internalField1099 = false;
                  this.internalMethod06224(AutoSellTask.InternalType0360.internalField1544);
               } else {
                  if (this.internalField1055 != -1 && localValue4 >= this.internalField1055) {
                     this.internalField1056++;
                     if (this.internalField1056 >= 4) {
                        this.internalMethod06224(AutoSellTask.InternalType0360.internalField1540);
                        return;
                     }
                  } else {
                     this.internalField1056 = 0;
                  }

                  this.internalField1055 = localValue4;

                  for (int localValue5 = 0; localValue5 < localValue1.slots.size(); localValue5++) {
                     Slot localValue6 = (Slot)localValue1.slots.get(localValue5);
                     if (localValue6.inventory != internalField0149.player.getInventory() && this.internalMethod03197(localValue6.getStack())) {
                        internalField0149.interactionManager.clickSlot(localValue1.syncId, localValue5, 0, SlotActionType.QUICK_MOVE, internalField0149.player);
                        this.internalField0519.internalMethod00701();
                        return;
                     }
                  }

                  this.internalMethod06224(AutoSellTask.InternalType0360.internalField1540);
               }
            }
         }
      }
   }

   private int internalMethod00132(ScreenHandler localValue1) {
      int localValue2 = 0;

      for (Slot localValue4 : localValue1.slots) {
         if (localValue4.inventory != internalField0149.player.getInventory() && this.internalMethod03197(localValue4.getStack())) {
            localValue2 += Math.max(1, localValue4.getStack().getCount());
         }
      }

      return localValue2;
   }

   private void internalMethod10027() {
      if (this.internalField0519.internalMethod02365(350L)) {
         this.internalMethod10029();
         this.internalMethod06224(AutoSellTask.InternalType0360.internalField1174);
      }
   }

   private void internalMethod10028() {
      ClientMessages.internalMethod01809(Text.of(LanguageManager.internalMethod07214("modules.auto_sell.done")));
      this.internalMethod08387();
   }

   private void internalMethod06224(AutoSellTask.InternalType0360 localValue1) {
      this.internalField0479 = localValue1;
      this.internalField0519.internalMethod00701();
      this.internalField0518.internalMethod00701();
      this.internalField0118 = null;
      this.internalField1054 = 0;
      if (localValue1 == AutoSellTask.InternalType0360.internalField1540) {
         this.internalField1189.internalMethod00701();
         this.internalField0276 = false;
      }
   }

   private boolean internalMethod03197(ItemStack localValue1) {
      if (localValue1 != null && !localValue1.isEmpty()) {
         if (localValue1.getItem() != this.internalField0152) {
            return false;
         } else if (this.internalField0152 != Items.POTION
            && this.internalField0152 != Items.SPLASH_POTION
            && this.internalField0152 != Items.LINGERING_POTION
            && this.internalField0152 != Items.TIPPED_ARROW) {
            return true;
         } else {
            PotionContentsComponent localValue2 = (PotionContentsComponent)localValue1.get(DataComponentTypes.POTION_CONTENTS);
            PotionContentsComponent localValue3 = (PotionContentsComponent)this.internalField0878.get(DataComponentTypes.POTION_CONTENTS);
            return Objects.equals(localValue2, localValue3);
         }
      } else {
         return false;
      }
   }

   private int internalMethod02405() {
      if (internalField0149.player == null) {
         return 0;
      } else {
         int localValue1 = 0;

         for (MainInventorySlot localValue3 : InventorySlots.internalMethod03558().internalMethod02638()) {
            if (!localValue3.internalMethod06664() && this.internalMethod03197(localValue3.internalMethod03427())) {
               localValue1 += localValue3.internalMethod03427().getCount();
            }
         }

         for (HotbarSlot localValue5 : InventorySlots.internalMethod02872().internalMethod02638()) {
            if (!localValue5.internalMethod06664() && this.internalMethod03197(localValue5.internalMethod03427())) {
               localValue1 += localValue5.internalMethod03427().getCount();
            }
         }

         return localValue1;
      }
   }

   private int internalMethod07367(ScreenHandler localValue1) {
      int localValue2 = 0;

      for (Slot localValue4 : localValue1.slots) {
         if (localValue4.inventory == internalField0149.player.getInventory() && this.internalMethod03197(localValue4.getStack())) {
            localValue2 += localValue4.getStack().getCount();
         }
      }

      return localValue2;
   }

   private int internalMethod08322(ScreenHandler localValue1) {
      for (int localValue2 = 0; localValue2 < localValue1.slots.size(); localValue2++) {
         Slot localValue3 = (Slot)localValue1.slots.get(localValue2);
         if (localValue3.inventory == internalField0149.player.getInventory() && this.internalMethod03197(localValue3.getStack())) {
            return localValue2;
         }
      }

      return -1;
   }

   private int internalMethod08160(ScreenHandler localValue1) {
      for (int localValue2 = 0; localValue2 < localValue1.slots.size(); localValue2++) {
         Slot localValue3 = (Slot)localValue1.slots.get(localValue2);
         if (localValue3.inventory == internalField0149.player.getInventory() && localValue3.getStack().isEmpty()) {
            return localValue2;
         }
      }

      return -1;
   }

   private int internalMethod08528(ScreenHandler localValue1) {
      for (int localValue2 = 0; localValue2 < localValue1.slots.size(); localValue2++) {
         Slot localValue3 = (Slot)localValue1.slots.get(localValue2);
         if (localValue3.inventory != internalField0149.player.getInventory() && !this.internalField0545.contains(localValue2) && localValue3.getStack().isEmpty()) {
            return localValue2;
         }
      }

      return -1;
   }

   private int internalMethod07415(ScreenHandler localValue1, String localValue2) {
      for (int localValue3 = 0; localValue3 < localValue1.slots.size(); localValue3++) {
         Slot localValue4 = (Slot)localValue1.slots.get(localValue3);
         if (localValue4.inventory != internalField0149.player.getInventory()) {
            ItemStack localValue5 = localValue4.getStack();
            if (!localValue5.isEmpty() && localValue5.getName().getString().contains(localValue2)) {
               return localValue3;
            }
         }
      }

      return -1;
   }

   private ScreenHandler internalMethod06132() {
      if (!(internalField0149.currentScreen instanceof HandledScreen)) {
         return null;
      } else if (internalField0149.player == null) {
         return null;
      } else {
         ScreenHandler localValue1 = internalField0149.player.currentScreenHandler;
         return localValue1 == internalField0149.player.playerScreenHandler ? null : localValue1;
      }
   }

   private void internalMethod10029() {
      if (internalField0149.player != null) {
         if (internalField0149.player.currentScreenHandler != null
            && internalField0149.player.currentScreenHandler != internalField0149.player.playerScreenHandler) {
            internalField0149.player.networkHandler.sendPacket(new CloseHandledScreenC2SPacket(internalField0149.player.currentScreenHandler.syncId));
         }

         if (internalField0149.currentScreen instanceof HandledScreen) {
            internalField0149.player.closeHandledScreen();
         }
      }
   }

   private void internalMethod06813(BlockPos localValue1) {
      Vec3d localValue2 = Vec3d.ofCenter(localValue1);
      BlockHitResult localValue3 = new BlockHitResult(localValue2, Direction.UP, localValue1, false);
      internalField0149.interactionManager.interactBlock(internalField0149.player, Hand.MAIN_HAND, localValue3);
      internalField0149.player.swingHand(Hand.MAIN_HAND);
   }

   private boolean internalMethod06814(BlockPos localValue1) {
      double localValue2 = internalField0149.player.getBlockInteractionRange() + 0.5;
      return internalField0149.player.getEyePos().squaredDistanceTo(Vec3d.ofCenter(localValue1)) <= localValue2 * localValue2;
   }

   private BlockPos internalMethod06252(BlockPos localValue1) {
      if (localValue1 != null && internalField0149.world != null) {
         BlockState localValue2 = internalField0149.world.getBlockState(localValue1);
         if (!(localValue2.getBlock() instanceof ChestBlock)) {
            return null;
         } else {
            ChestType localValue3 = (ChestType)localValue2.get(ChestBlock.CHEST_TYPE);
            if (localValue3 == ChestType.SINGLE) {
               return null;
            } else {
               Direction localValue4 = (Direction)localValue2.get(ChestBlock.FACING);
               Direction localValue5 = localValue3 == ChestType.LEFT ? localValue4.rotateYClockwise() : localValue4.rotateYCounterclockwise();
               return localValue1.offset(localValue5);
            }
         }
      } else {
         return null;
      }
   }

   private boolean internalMethod01633(Vec3d localValue1) {
      Rotation localValue2 = RotationUtils.internalMethod05580(localValue1);
      if (this.internalField0118 == null || this.internalField0118.internalMethod00735(localValue2) > 0.5F) {
         this.internalField0118 = localValue2;
         this.internalField1054 = 0;
      }

      RockstarClient.getInstance()
         .internalMethod02368()
         .internalMethod00418(localValue2, RotationBehavior.internalField1003, 180.0F, 180.0F, 180.0F, RotationPriority.internalField1012);
      Rotation localValue3 = RockstarClient.getInstance().internalMethod02368().internalMethod09074();
      if (localValue3 != null && localValue3.internalMethod00735(localValue2) <= 1.5F) {
         this.internalField1054++;
         return this.internalField1054 >= 1;
      } else {
         return false;
      }
   }

   @Generated
   public HudContextMenu internalMethod00450() {
      return this.internalField0574;
   }

   class InternalType0359 extends BlankUiElement {
      @Override
      public void internalMethod05619(UiRenderContext localValue1) {
         float localValue2 = 8.0F;
         localValue1.drawItem(AutoSellTask.this.internalField0878, this.internalField0205 + localValue2, this.internalField0206 + (this.internalField1047 - 16.0F) / 2.0F, 1.0F);
         SizedFont localValue3 = Fonts.internalField1154.internalMethod01432(8.0F);
         String localValue4 = AutoSellTask.this.internalField0878.isEmpty() ? "" : AutoSellTask.this.internalField0878.getName().getString();
         localValue1.drawFadeoutText(
            localValue3,
            localValue4,
            this.internalField0205 + localValue2 + 20.0F,
            this.internalField0206 + UiUtils.internalMethod07116(localValue3.internalMethod04890(), this.internalField1047),
            ThemeColors.internalMethod08459().withAlpha(RenderSystem.getShaderColor()[3] * 255.0F),
            0.8F,
            1.0F,
            this.internalField1048 - 28.0F
         );
      }

      @Override
      public float internalMethod07809() {
         return this.internalField1047 = 22.0F;
      }
   }

   static enum InternalType0360 {
      internalField0479,
      internalField0478,
      internalField1171,
      internalField1172,
      internalField1173,
      internalField1174,
      internalField1541,
      internalField1542,
      internalField1540,
      internalField1543,
      internalField1545,
      internalField1546,
      internalField1544;
   }
}
