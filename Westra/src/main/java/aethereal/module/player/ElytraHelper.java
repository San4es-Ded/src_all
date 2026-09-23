package aethereal.module.player;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.InputEvent;
import aethereal.event.PacketEvent;
import aethereal.setting.BindSetting;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import aethereal.util.InventoryUtil;
import lombok.Generated;
import net.minecraft.class_1304;
import net.minecraft.class_1802;
import net.minecraft.class_7439;
import net.minecraft.class_2828.class_2829;

@ModuleRegister(
   a = "Elytra Helper",
   b = "Выполняет действия с элитрой по нажатию назначенной клавиши",
   c = Category.Player
)
public class ElytraHelper extends Module implements Interface {
   private final BooleanSetting b = new BooleanSetting("Автостарт после свапа", false);
   private final BooleanSetting c;
   private final BooleanSetting d;
   private final BindSetting e;
   private final BindSetting f;
   private boolean g;
   private boolean h;
   private int i;

   @Generated
   public BooleanSetting q() {
      return this.b;
   }

   @Generated
   public BooleanSetting r() {
      return this.c;
   }

   @Generated
   public BooleanSetting s() {
      return this.d;
   }

   @Generated
   public BindSetting t() {
      return this.e;
   }

   @Generated
   public BindSetting u() {
      return this.f;
   }

   @Generated
   public boolean v() {
      return this.g;
   }

   @Generated
   public boolean w() {
      return this.h;
   }

   @Generated
   public int x() {
      return this.i;
   }

   public ElytraHelper() {
      BooleanSetting booleanSetting = new BooleanSetting("Использовать /fly при свапе", false);
      BooleanSetting booleanSetting2 = this.b;
      this.c = booleanSetting.a(booleanSetting2::c);
      BooleanSetting booleanSetting3 = new BooleanSetting("Автофейерверк", false);
      BooleanSetting booleanSetting4 = this.b;
      this.d = booleanSetting3.a(booleanSetting4::c);
      this.e = new BindSetting("Кнопка фейерверка", -1).a(() -> this.z());
      this.f = new BindSetting("Кнопка переключения", -1)
         .a(
            () -> {
               int slot = aM_.field_1724.method_6118(class_1304.field_6174).method_7909() == class_1802.field_8833
                  ? InventoryUtil.a()
                  : InventoryUtil.b(class_1802.field_8833);
               Westra.h().d().v().a().b(slot, 1, 1);
               if (this.b.c() && InventoryUtil.b(class_1802.field_8833) == slot) {
                  this.i = aM_.field_1724.field_6012;
                  this.g = true;
               }
            }
         );
      this.a(new Setting[]{this.f, this.e, this.b, this.c, this.d});
   }

   @EventTarget
   public void a(InputEvent event) {
      if (aM_.field_1724.field_6012 < 5) {
         this.g = false;
      } else if (this.g && this.y()) {
         this.b(event);
      }
   }

   private boolean y() {
      if (this.c.c() && !this.h) {
         if (this.i + 1 == aM_.field_1724.field_6012) {
            aM_.field_1724.field_3944.method_45730("fly");
         }

         if (aM_.field_1724.method_31549().field_7478 && this.i <= aM_.field_1724.field_6012 && aM_.field_1724.method_24828()) {
            double x = aM_.field_1724.method_23317();
            double y = aM_.field_1724.method_23318() + 0.19999997F;
            double z = aM_.field_1724.method_23321();
            aM_.field_1724.field_3944.method_52787(new class_2829(x, y, z, false, aM_.field_1724.field_5976));
            aM_.field_1724.method_5814(x, y, z);
            this.i = aM_.field_1724.field_6012 + 9;
         }

         if (aM_.field_1724.method_31549().field_7478 && !aM_.field_1724.method_24828()) {
            aM_.field_1724.method_31549().field_7479 = true;
            aM_.field_1724.method_18800(0.0, 0.0, 0.0);
            aM_.field_1724.method_7355();
            this.g = false;
         }

         if (!this.b(15) && (!this.b(3) || aM_.field_1724.method_6118(class_1304.field_6174).method_7909() == class_1802.field_8833)) {
            return false;
         } else {
            this.g = false;
            return false;
         }
      } else {
         return true;
      }
   }

   private void b(InputEvent event) {
      boolean wearingElytra = aM_.field_1724.method_6118(class_1304.field_6174).method_7909() == class_1802.field_8833;
      if (!aM_.field_1724.method_6128() && wearingElytra) {
         event.b(aM_.field_1724.field_6012 % 2 == 0);
         this.i = aM_.field_1724.field_6012;
         if (!this.d.c()) {
            this.g = false;
         }
      }

      if (aM_.field_1724.method_6128() && this.d.c() && !aM_.field_1724.method_5799() && !aM_.field_1724.method_24828() && this.b(2)) {
         this.z();
         this.g = false;
      }

      if (this.b(10)) {
         this.g = false;
      }
   }

   private void z() {
      if (aM_.field_1724.method_6128()) {
         Westra.h().d().v().b().a(class_1802.field_8639.method_7854());
      }
   }

   private boolean b(int delay) {
      return this.i + delay < aM_.field_1724.field_6012;
   }

   @EventTarget
   public void a(PacketEvent event) {
      if (event.c()
         && event.d() instanceof class_7439 class_7439VarD
         && this.g
         && class_7439VarD.comp_763().getString().contains("Эту команду могут писать только донатеры выше рангом")) {
         this.h = true;
      }
   }

   @Override
   public void c() {
      super.c();
      this.g = false;
   }
}
