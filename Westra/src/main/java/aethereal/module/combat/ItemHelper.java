package aethereal.module.combat;

import aethereal.core.Category;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.setting.BindSetting;
import aethereal.setting.Setting;
import aethereal.util.InventoryUtil;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_1792;
import net.minecraft.class_1802;

@ModuleRegister(
   a = "Item Helper",
   b = "Перемещает нужный предмет и возвращает его обратно по нажатию клавиши",
   c = Category.Combat
)
public class ItemHelper extends Module implements Interface {
   public ItemHelper() {
      List.of(
            new ItemHelper.a(this, "Зачарованное яблоко", class_1802.field_8367),
            new ItemHelper.a(this, "Золотое яблоко", class_1802.field_8463),
            new ItemHelper.a(this, "Плод хоруса", class_1802.field_8233),
            new ItemHelper.a(this, "Арбалет", class_1802.field_8399)
         )
         .forEach(item -> this.a(new Setting[]{item.b()}));
   }

   final class a {
      private final class_1792 a;
      private final BindSetting b;
      private int c = -1;
      private int d = -1;

      @Generated
      public class_1792 a() {
         return this.a;
      }

      @Generated
      public BindSetting b() {
         return this.b;
      }

      @Generated
      public int c() {
         return this.c;
      }

      @Generated
      public int d() {
         return this.d;
      }

      a(ItemHelper itemHelper, String name, class_1792 item) {
         this.a = item;
         this.b = new BindSetting(name, -1, 1).a(this::e);
      }

      private void e() {
         if (this.d == -1) {
            this.f();
         } else {
            this.g();
         }
      }

      private void f() {
         if (this.d == -1) {
            int from = InventoryUtil.b(this.a);
            int target = Interface.aM_.field_1724.method_31548().field_7545;
            if (from != -1 && from != target) {
               this.d = from;
               this.c = target;
               Westra.h().d().v().a().a(from, target, 1);
            }
         }
      }

      private void g() {
         if (this.d != -1) {
            Westra.h().d().v().a().a(this.d, this.c, 1);
            this.d = -1;
            this.c = -1;
         }
      }
   }
}
