package aethereal.module.player;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.TickEvent;
import aethereal.setting.BindSetting;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import aethereal.util.ChatUtil;
import aethereal.util.InventoryUtil;
import aethereal.util.Rotation;
import net.minecraft.class_1268;
import net.minecraft.class_1802;

@ModuleRegister(
   a = "Wind Jump",
   b = "Подкидывает вверх зарядом ветра по нажатию клавиши",
   c = Category.Player
)
public class WindJump extends Module {
   private static final int IDLE = 0;
   private static final int ROTATING = 1;
   private static final int ARMING = 2;
   private static final int USING = 3;
   private final SliderSetting b = new SliderSetting("Скорость доводки", 220.0F, 60.0F, 360.0F, 10.0F);
   private final BooleanSetting c = new BooleanSetting("Возвращать слот обратно", true);
   private final BooleanSetting d = new BooleanSetting("Сообщать в чат", false);
   private final BindSetting e = new BindSetting("Клавиша заряда", -1, 1).a(() -> {
      if (this.f == 0) {
         this.f = 1;
         this.g = 0;
         this.h = 0;
      }
   });
   private int f = 0;
   private int g;
   private int h;
   private long i;
   private int j = -1;

   public WindJump() {
      this.a(new Setting[]{this.e, this.b, this.c, this.d});
   }

   @Override
   public void c() {
      super.c();
      this.q();
   }

   @EventTarget
   public void a(TickEvent event) {
      if (aM_.field_1724 == null || aM_.field_1687 == null) {
         this.q();
      } else if (this.f != 0) {
         Westra.h().d().k().a(new Rotation(aM_.field_1724.method_36454(), 90.0F), this.b.c(), 1, 1);
         switch (this.f) {
            case 1:
               this.g++;
               if (aM_.field_1724.method_36455() >= 89.0F && this.g >= 2) {
                  this.f = 2;
                  this.h = 0;
               } else if (this.g > 10) {
                  this.q();
               }

               return;
            case 2:
               if (aM_.field_1724.method_36455() < 89.0F) {
                  this.f = 1;
                  this.h = 0;
                  return;
               } else {
                  this.h++;
                  if (this.h >= 1) {
                     if (!this.r()) {
                        this.q();
                        return;
                     }

                     this.f = 3;
                     this.i = System.currentTimeMillis();
                     if (this.d.c()) {
                        ChatUtil.a("Заряд ветра использован.");
                     }
                  }

                  return;
               }
            case 3:
               if (System.currentTimeMillis() - this.i >= 50L) {
                  this.q();
               }

               return;
         }
      }
   }

   private boolean r() {
      int slot = InventoryUtil.a(class_1802.field_49098, true);
      if (slot == -1) {
         if (this.d.c()) {
            ChatUtil.a("Нет заряда ветра в хотбаре.");
         }

         return false;
      } else {
         this.j = aM_.field_1724.method_31548().field_7545;
         aM_.field_1724.method_31548().field_7545 = slot;
         aM_.field_1761.method_2919(aM_.field_1724, class_1268.field_5808);
         aM_.field_1724.method_6104(class_1268.field_5808);
         return true;
      }
   }

   private void q() {
      if (this.j != -1 && aM_.field_1724 != null && this.c.c()) {
         aM_.field_1724.method_31548().field_7545 = this.j;
      }

      this.j = -1;
      this.f = 0;
      this.g = 0;
      this.h = 0;
   }
}
