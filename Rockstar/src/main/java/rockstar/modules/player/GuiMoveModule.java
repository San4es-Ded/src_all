package rockstar.modules.player;







import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.server.*;
import rockstar.client.module.*;
import rockstar.client.event.*;
import rockstar.client.internal.script.*;
import rockstar.client.*;
import rockstar.client.module.Module;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import lombok.Generated;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.ingame.SignEditScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.screen.slot.SlotActionType;
import pyrock.events.network.ReceivePacketEvent;
import pyrock.events.network.SendPacketEvent;
import pyrock.events.player.ClientPlayerTickEvent;
import pyrock.events.player.InputEvent;

@ModuleInfo(
   name = "Gui Move",
   category = ModuleCategory.PLAYER,
   internalMethod08049 = true
)
public class GuiMoveModule extends Module {
   private ModeSetting internalField0668;
   private ModeSetting.InternalType0088 internalField0237;
   private ModeSetting.InternalType0088 internalField0238;
   private ModeSetting.InternalType0088 internalField1066;
   private ModeSetting.InternalType0088 internalField1067;
   private ModeSetting.InternalType0088 internalField1068;
   private final List<Packet<?>> internalField0416 = new ArrayList<>();
   private int internalField0228;
   private boolean internalField0276;
   private final Stopwatch internalField0519 = new Stopwatch();
   private int internalField1053;
   private boolean internalField1099;
   private boolean internalField1100;
   private boolean internalField1102;
   private int internalField1055;
   private boolean internalField1101;
   private boolean internalField1516;
   public int internalField0227;
   public boolean internalField0277;
   private final Queue<ClickSlotC2SPacket> internalField0877 = new LinkedList<>();
   private final EventListener<SendPacketEvent> internalField0157 = localValue1 -> {
      if (!this.internalField1100) {
         if (this.internalField0668.internalMethod06103(this.internalField1068) && localValue1.getPacket() instanceof ClickSlotC2SPacket localValue6) {
            if (!this.internalField0277 && this.internalMethod10079() && localValue6.slot() != -1) {
               this.internalMethod00155(localValue6.actionType());
               if (!this.internalField1101 && this.internalMethod04672(localValue6)) {
                  this.internalField0227 = 0;
                  this.internalField1102 = true;
                  this.internalField0519.internalMethod00701();
                  localValue1.cancel();
               }
            }
         } else {
            if (localValue1.getPacket() instanceof ClickSlotC2SPacket localValue2) {
               if (!this.internalMethod10077()) {
                  return;
               }

               if (this.internalField0277) {
                  return;
               }

               if (internalField0149.currentScreen == null) {
                  this.internalField0416.add(localValue2);
                  localValue1.cancel();
                  this.internalField0276 = true;
                  if (this.internalField0668.internalMethod06103(this.internalField0238)) {
                     this.internalField0519.internalMethod00701();
                  } else if (this.internalMethod10078()) {
                     this.internalField1053 = 2;
                     this.internalField1099 = true;
                     this.internalField0519.internalMethod00701();
                  } else if (this.internalField1067.isSelected()) {
                     this.internalField1102 = true;
                  } else {
                     this.internalField0228 = 3;
                  }
               }

               if (internalField0149.currentScreen instanceof InventoryScreen && GameUtils.internalMethod00469()) {
                  this.internalField0416.add(localValue2);
                  localValue1.cancel();
               }

               if (internalField0149.player.currentScreenHandler.getCursorStack().getItem() instanceof BlockItem localValue8
                  && localValue8.getBlock() instanceof ShulkerBoxBlock
                  && ServerUtils.internalMethod01786(KnownServer.internalField1566)) {
                  internalField0149.player.currentScreenHandler.setCursorStack(ItemStack.EMPTY);
               }
            }

            if (localValue1.getPacket() instanceof CloseHandledScreenC2SPacket) {
               if (!this.internalMethod10077()) {
                  return;
               }

               if (this.internalField0668.internalMethod06103(this.internalField1068)) {
                  return;
               }

               boolean localValue5 = GameUtils.internalMethod00469();
               if (localValue5) {
                  if (!this.internalField0416.isEmpty()) {
                     this.internalField0276 = true;
                     if (this.internalField0668.internalMethod06103(this.internalField0238)) {
                        this.internalField0519.internalMethod00701();
                     } else if (this.internalMethod10078()) {
                        this.internalField1053 = 2;
                        this.internalField1099 = true;
                        this.internalField0519.internalMethod00701();
                     } else {
                        this.internalField0228 = 3;
                     }
                  }

                  localValue1.cancel();
               }
            }
         }
      }
   };
   private final EventListener<ReceivePacketEvent> internalField0158 = localValue1 -> {
      if ((this.internalMethod09769() || this.internalMethod09771())
         && this.internalField1102
         && !this.internalField0519.internalMethod02365(2000L)
         && (localValue1.getPacket() instanceof ScreenHandlerSlotUpdateS2CPacket || localValue1.getPacket() instanceof InventoryS2CPacket)) {
         this.internalField1102 = false;
         localValue1.cancel();
      }
   };
   private final EventListener<InputEvent> internalField1028 = localValue1 -> {
      if (this.internalField0668.internalMethod06103(this.internalField0238) && !this.internalField0519.internalMethod02365(190L)) {
         localValue1.setForward(0.0F);
         localValue1.setStrafe(0.0F);
         localValue1.setJump(false);
         localValue1.setSneak(false);
         localValue1.setSprint(false);
      }

      if (this.internalMethod10078() && this.internalField1053 > 0 && !this.internalField0519.internalMethod02365(195L)) {
         localValue1.setForward(0.0F);
         localValue1.setStrafe(0.0F);
         localValue1.setJump(false);
         localValue1.setSneak(false);
         localValue1.setSprint(false);
      }

      if (this.internalField0668.internalMethod06103(this.internalField1068) && this.internalField1101) {
         localValue1.setForward(0.0F);
         localValue1.setStrafe(0.0F);
         localValue1.setJump(false);
         localValue1.setSneak(false);
         localValue1.setSprint(false);
      }
   };
   private final EventListener<ClientPlayerTickEvent> internalField1029 = localValue1 -> {
      if (this.internalField0228 > 0) {
         this.internalField0228--;
      }

      if (this.internalField1053 > 0) {
         this.internalField1053--;
      }

      if (this.internalField0668.internalMethod06103(this.internalField1068)) {
         if (this.internalField1516 && this.internalField1101 && this.internalField1055 > 0) {
            this.internalMethod09663();
         }

         this.internalField1516 = this.internalField1101;
         if (this.internalMethod09956()) {
            this.internalMethod00155(null);
         } else if (this.internalField1055 > 0) {
            this.internalField1055--;
         }

         this.internalField1101 = this.internalField1055 > 0;
         this.internalField0227++;
         if (this.internalField1101) {
            this.internalField0227 = 0;
         }
      }

      if (!this.internalField0668.internalMethod06103(this.internalField0238) || this.internalField0519.internalMethod02365(100L)) {
         if (!this.internalMethod10078()
            || this.internalField1053 <= 0
            || this.internalField0519.internalMethod02365(ServerUtils.internalMethod01786(KnownServer.internalField0578) ? 120L : 99L)) {
            if (!this.internalField0668.internalMethod06103(this.internalField1068) || !this.internalField1101) {
               if (this.internalField0237.isSelected()
                  || this.internalField0668.internalMethod06103(this.internalField1068)
                  || internalField0149.currentScreen == null
                  || internalField0149.currentScreen instanceof InventoryScreen
                  || internalField0149.currentScreen instanceof CreativeInventoryScreen) {
                  internalMethod09597();
                  if (this.internalField0276 && this.internalField0668.internalMethod06103(this.internalField0238)) {
                     internalField0149.setScreen(new InventoryScreen(internalField0149.player));
                     this.internalMethod09671();
                     this.internalField0276 = false;
                     internalField0149.setScreen(null);
                  }

                  if (this.internalField1099 && this.internalMethod10078() && this.internalField1053 == 0) {
                     this.internalMethod09671();
                     this.internalField1099 = false;
                     this.internalField0276 = false;
                  }
               }
            }
         }
      }
   };

   public GuiMoveModule() {
      this.internalMethod09602();
   }

   private void internalMethod09602() {
      this.internalField0668 = new ModeSetting(this, "\u041e\u0431\u0445\u043e\u0434");
      this.internalField0237 = new ModeSetting.InternalType0088(this.internalField0668, "Vanilla").select();
      this.internalField0238 = new ModeSetting.InternalType0088(this.internalField0668, "Lony/Cake");
      this.internalField1066 = new ModeSetting.InternalType0088(this.internalField0668, "HW&RW");
      this.internalField1067 = new ModeSetting.InternalType0088(this.internalField0668, "FunTime");
      this.internalField1068 = new ModeSetting.InternalType0088(this.internalField0668, "SpookyTime");
   }

   private boolean internalMethod10077() {
      return this.internalField0668.internalMethod06103(this.internalField0238)
         || this.internalMethod10078()
         || this.internalField0668.internalMethod06103(this.internalField1068);
   }

   private boolean internalMethod10078() {
      return this.internalField0668.internalMethod06103(this.internalField1066) || this.internalField0668.internalMethod06103(this.internalField1067);
   }

   public boolean internalMethod09769() {
      return this.internalField0668.internalMethod06103(this.internalField1067);
   }

   public boolean internalMethod09771() {
      return this.isEnabled() && this.internalField0668.internalMethod06103(this.internalField1068);
   }

   public boolean internalMethod09598() {
      return !this.isEnabled() || this.internalField0227 > 0;
   }

   public boolean internalMethod09603() {
      return !this.internalField0877.isEmpty();
   }

   private boolean internalMethod10079() {
      return rockstar.client.compat.InputCompat.forward(internalField0149.player.input) != 0.0F || rockstar.client.compat.InputCompat.sideways(internalField0149.player.input) != 0.0F || this.internalField1101;
   }

   private int internalMethod00154(SlotActionType localValue1) {
      return localValue1 == SlotActionType.PICKUP ? 1 : (this.internalField1055 > 1 ? 2 : 3);
   }

   private void internalMethod00155(SlotActionType localValue1) {
      if (this.isEnabled() && this.internalField0668.internalMethod06103(this.internalField1068)) {
         this.internalField1055 = this.internalMethod00154(localValue1 == null ? SlotActionType.PICKUP : localValue1) + 1;
      }
   }

   public void internalMethod09768() {
      if (this.isEnabled() && this.internalField0668.internalMethod06103(this.internalField1068)) {
         this.internalField1055 = 8;
      }
   }

   private boolean internalMethod09956() {
      if (!(internalField0149.currentScreen instanceof HandledScreen)) {
         return false;
      } else {
         return !this.internalMethod10079() ? false : !internalField0149.player.currentScreenHandler.getCursorStack().isEmpty();
      }
   }

   private void internalMethod09663() {
      if (!this.internalField0877.isEmpty()) {
         this.internalField1100 = true;

         while (!this.internalField0877.isEmpty()) {
            ClickSlotC2SPacket localValue1 = this.internalField0877.poll();
            if (localValue1 != null) {
               internalField0149.player.networkHandler.sendPacket(localValue1);
            }
         }

         this.internalField1100 = false;
         this.internalField0227 = 0;
      }
   }

   private boolean internalMethod04672(ClickSlotC2SPacket localValue1) {
      return !this.internalField0877.contains(localValue1) && this.internalField0877.add(localValue1);
   }

   public void internalMethod09770() {
      if (this.internalMethod10077() && !this.internalField0416.isEmpty()) {
         if (!this.internalField0668.internalMethod06103(this.internalField1068)) {
            this.internalField0276 = true;
            if (this.internalField0668.internalMethod06103(this.internalField0238)) {
               this.internalField0519.internalMethod00701();
            } else if (this.internalMethod10078()) {
               this.internalField1053 = 2;
               this.internalField1099 = true;
               this.internalField0519.internalMethod00701();
            } else {
               this.internalField0228 = 3;
            }
         }
      }
   }

   private void internalMethod09671() {
      if (!this.internalField0416.isEmpty()) {
         this.internalField1100 = true;

         for (Packet localValue2 : this.internalField0416) {
            internalField0149.player.networkHandler.sendPacket(localValue2);
         }

         internalField0149.player.networkHandler.sendPacket(new CloseHandledScreenC2SPacket(internalField0149.player.currentScreenHandler.syncId));
         this.internalField0416.clear();
         this.internalField1100 = false;
      }
   }

   @Override
   public void onDisable() {
      this.internalMethod09663();
      this.internalField0227 = 1;
      this.internalField0877.clear();
      this.internalField1055 = 0;
      this.internalField1101 = false;
      this.internalField1516 = false;
      this.internalField0416.clear();
      this.internalField0228 = 0;
      this.internalField1053 = 0;
      this.internalField0276 = false;
      this.internalField1099 = false;
      this.internalField0277 = false;
      this.internalField1100 = false;
      super.onDisable();
   }

   @Override
   public void onEnable() {
      this.internalMethod09663();
      this.internalField0877.clear();
      this.internalField0227 = 1;
      super.onEnable();
   }

   public static void internalMethod09597() {
      KeyBinding[] localValue0 = new KeyBinding[]{
         internalField0149.options.forwardKey,
         internalField0149.options.backKey,
         internalField0149.options.leftKey,
         internalField0149.options.rightKey,
         internalField0149.options.jumpKey
      };
      if (!internalMethod09957()) {
         for (KeyBinding localValue4 : localValue0) {
            int localValue5 = InputUtil.fromTranslationKey(localValue4.getBoundKeyTranslationKey()).getCode();
            boolean localValue6 = InputUtil.isKeyPressed(internalField0149.getWindow(), localValue5);
            localValue4.setPressed(localValue6);
         }
      }
   }

   private static boolean internalMethod09957() {
      return internalField0149.currentScreen instanceof ChatScreen
         || internalField0149.currentScreen != null && CustomTextField.internalField0936 != null && CustomTextField.internalField0936.internalMethod00342()
         || internalField0149.currentScreen instanceof SignEditScreen
         || internalField0149.currentScreen instanceof AnvilScreen
         || internalField0149.currentScreen instanceof CreativeInventoryScreen && CreativeInventoryScreen.selectedTab == ItemGroups.getSearchGroup();
   }

   @Generated
   public ModeSetting internalMethod04827() {
      return this.internalField0668;
   }

   @Generated
   public ModeSetting.InternalType0088 internalMethod02492() {
      return this.internalField0237;
   }

   @Generated
   public ModeSetting.InternalType0088 internalMethod02661() {
      return this.internalField0238;
   }

   @Generated
   public ModeSetting.InternalType0088 internalMethod08208() {
      return this.internalField1066;
   }

   @Generated
   public ModeSetting.InternalType0088 internalMethod08244() {
      return this.internalField1067;
   }

   @Generated
   public ModeSetting.InternalType0088 internalMethod07780() {
      return this.internalField1068;
   }

   @Generated
   public List<Packet<?>> internalMethod06994() {
      return this.internalField0416;
   }

   @Generated
   public int internalMethod08053() {
      return this.internalField0228;
   }

   @Generated
   public boolean internalMethod09664() {
      return this.internalField0276;
   }

   @Generated
   public Stopwatch internalMethod03649() {
      return this.internalField0519;
   }

   @Generated
   public int internalMethod08054() {
      return this.internalField1053;
   }

   @Generated
   public boolean internalMethod09672() {
      return this.internalField1099;
   }

   @Generated
   public boolean internalMethod10014() {
      return this.internalField1100;
   }

   @Generated
   public boolean internalMethod10015() {
      return this.internalField1102;
   }

   @Generated
   public int internalMethod08059() {
      return this.internalField1055;
   }

   @Generated
   public boolean internalMethod10016() {
      return this.internalField1101;
   }

   @Generated
   public boolean internalMethod10017() {
      return this.internalField1516;
   }

   @Generated
   public int internalMethod08060() {
      return this.internalField0227;
   }

   @Generated
   public boolean internalMethod10076() {
      return this.internalField0277;
   }

   @Generated
   public Queue<ClickSlotC2SPacket> internalMethod03994() {
      return this.internalField0877;
   }

   @Generated
   public EventListener<SendPacketEvent> internalMethod06185() {
      return this.internalField0157;
   }

   @Generated
   public EventListener<ReceivePacketEvent> internalMethod07513() {
      return this.internalField0158;
   }

   @Generated
   public EventListener<InputEvent> internalMethod08728() {
      return this.internalField1028;
   }

   @Generated
   public EventListener<ClientPlayerTickEvent> internalMethod08969() {
      return this.internalField1029;
   }
}
