package aethereal.module.player;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.TickEvent;
import aethereal.setting.BindSetting;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import aethereal.util.ChatUtil;
import net.minecraft.class_1268;
import net.minecraft.class_1713;
import net.minecraft.class_1802;
import net.minecraft.class_2868;
import net.minecraft.class_3675;

@ModuleRegister(
   a = "Click Pearl",
   b = "Бросает эндер-жемчуг по клавише и возвращает прежний слот",
   c = Category.Player
)
public class ClickPearl extends Module implements Interface {
   private final BindSetting b = new BindSetting("Клавиша броска", -1);
   private final SliderSetting c = new SliderSetting("Шаг", 50.0F, 20.0F, 200.0F, 5.0F);
   private final BooleanSetting d2 = new BooleanSetting("Искать в инвентаре", false);
   private final BooleanSetting e = new BooleanSetting("Сообщать об ошибке", true);
   private ClickPearl.a f2 = ClickPearl.a.IDLE;
   private long g2;
   private long h2;
   private int i2 = -1;
   private int j2 = -1;
   private boolean k2;

   public ClickPearl() {
      this.a(new Setting[]{this.b, this.c, this.d2, this.e});
   }

   @Override
   public void c() {
      this.k2 = false;
      this.F();
      super.c();
   }

   private void F() {
      this.f2 = ClickPearl.a.IDLE;
      this.g2 = 0L;
      this.i2 = -1;
      this.j2 = -1;
   }

   @EventTarget
   public void a(TickEvent event) {
      if (aM_.field_1724 == null || aM_.field_1687 == null) {
         this.F();
         this.k2 = false;
      } else if (this.f2 != ClickPearl.a.IDLE) {
         this.G();
      } else if (this.H() && aM_.field_1755 == null) {
         if (System.currentTimeMillis() - this.h2 >= 200L) {
            if (!aM_.field_1724.method_7357().method_7904(class_1802.field_8634.method_7854())) {
               int slot = this.I();
               if (slot == -1) {
                  if (this.e.c()) {
                     ChatUtil.a("Эндер-жемчуг не найден.");
                  }
               } else {
                  this.i2 = aM_.field_1724.method_31548().field_7545;
                  this.j2 = slot;
                  this.f2 = ClickPearl.a.SWAP;
                  this.g2 = System.currentTimeMillis();
               }
            }
         }
      }
   }

   private void G() {
      long step = (long)this.c.c().floatValue();
      if (System.currentTimeMillis() - this.g2 >= step) {
         switch (this.f2) {
            case SWAP:
               this.J(this.j2);
               this.f2 = ClickPearl.a.THROW;
               this.g2 = System.currentTimeMillis();
               break;
            case THROW:
               aM_.field_1761.method_2919(aM_.field_1724, class_1268.field_5808);
               aM_.field_1724.method_6104(class_1268.field_5808);
               this.h2 = System.currentTimeMillis();
               this.f2 = ClickPearl.a.RESTORE;
               this.g2 = this.h2;
               break;
            case RESTORE:
               if (this.i2 != -1) {
                  this.J(this.i2);
               }

               this.F();
               break;
            default:
               this.F();
         }
      }
   }

   private void J(int slot) {
      if (slot >= 0 && slot <= 8 && aM_.field_1724 != null) {
         if (aM_.field_1724.method_31548().field_7545 != slot) {
            aM_.field_1724.method_31548().field_7545 = slot;
            if (aM_.field_1724.field_3944 != null) {
               aM_.field_1724.field_3944.method_52787(new class_2868(slot));
            }
         }
      }
   }

   private boolean H() {
      int key = this.b.c();
      if (key != -1 && aM_.method_22683() != null) {
         boolean down = class_3675.method_15987(aM_.method_22683().method_4490(), key);
         boolean pressed = down && !this.k2;
         this.k2 = down;
         return pressed;
      } else {
         this.k2 = false;
         return false;
      }
   }

   private int I() {
      for (int slot = 0; slot < 9; slot++) {
         if (aM_.field_1724.method_31548().method_5438(slot).method_31574(class_1802.field_8634)) {
            return slot;
         }
      }

      if (this.d2.c()) {
         for (int slotx = 9; slotx < 36; slotx++) {
            if (aM_.field_1724.method_31548().method_5438(slotx).method_31574(class_1802.field_8634) && aM_.field_1761 != null) {
               aM_.field_1761
                  .method_2906(aM_.field_1724.field_7498.field_7763, slotx, aM_.field_1724.method_31548().field_7545, class_1713.field_7791, aM_.field_1724);
               return aM_.field_1724.method_31548().field_7545;
            }
         }
      }

      return -1;
   }

   private static enum a {
      IDLE,
      SWAP,
      THROW,
      RESTORE;
   }
}
