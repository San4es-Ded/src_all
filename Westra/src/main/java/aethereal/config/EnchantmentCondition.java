package aethereal.config;

import aethereal.autobuy.ItemType;
import aethereal.core.Interface;
import aethereal.render.AnimationUtil;
import lombok.Generated;
import net.minecraft.class_1799;
import net.minecraft.class_1887;
import net.minecraft.class_1893;
import net.minecraft.class_2561;
import net.minecraft.class_5321;
import net.minecraft.class_7924;
import net.minecraft.class_9304;
import net.minecraft.class_9334;
import net.minecraft.class_6880.class_6883;

public class EnchantmentCondition implements Condition {
   private final AnimationUtil a = new AnimationUtil();
   private final class_5321<class_1887> b;
   private ItemType c;
   private int d;

   @Generated
   @Override
   public AnimationUtil a() {
      return this.a;
   }

   @Generated
   public class_5321<class_1887> i() {
      return this.b;
   }

   @Generated
   @Override
   public void a(ItemType type) {
      this.c = type;
   }

   @Generated
   @Override
   public ItemType h() {
      return this.c;
   }

   @Generated
   @Override
   public void a(int requiredLevel) {
      this.d = requiredLevel;
   }

   @Generated
   @Override
   public int g() {
      return this.d;
   }

   public EnchantmentCondition(class_5321<class_1887> key) {
      this(key, 0, ItemType.ON);
   }

   public EnchantmentCondition(class_5321<class_1887> key, int requiredLevel) {
      this(key, requiredLevel, ItemType.ON);
   }

   public EnchantmentCondition(class_5321<class_1887> key, int requiredLevel, ItemType type) {
      this.b = key;
      this.c = type;
      this.d = type == ItemType.ON ? this.c(requiredLevel) : requiredLevel;
   }

   @Override
   public String f() {
      return this.b.method_29177().toString();
   }

   @Override
   public boolean b() {
      return this.c != ItemType.OFF;
   }

   @Override
   public void a(boolean enabled) {
      this.c = enabled ? ItemType.ON : ItemType.OFF;
   }

   @Override
   public boolean c() {
      return this.d > 0;
   }

   @Override
   public boolean d() {
      return this.c == ItemType.DENY;
   }

   @Override
   public void b(int delta) {
      if (this.c == ItemType.ON && this.d != 0) {
         this.d = Math.max(1, this.c(this.d + delta));
      }
   }

   public boolean a(class_1799 stack) {
      if (this.c == ItemType.OFF) {
         return true;
      } else {
         class_6883 class_6883VarMethod_46747 = Interface.aM_.field_1687.method_30349().method_30530(class_7924.field_41265).method_46747(this.b);
         class_9304 enchantments = (class_9304)stack.method_57825(class_9334.field_49633, class_9304.field_49385);
         int level = enchantments.method_57536(class_6883VarMethod_46747);
         int threshold = this.d == 0 ? 1 : this.d;
         return this.c == ItemType.DENY ? level < threshold : !enchantments.method_57543() && level >= threshold;
      }
   }

   @Override
   public String e() {
      return class_2561.method_43471("enchantment." + this.b.method_29177().method_12836() + "." + this.b.method_29177().method_12832().replace("/", "."))
            .getString()
         + (this.d == 0 ? "" : " " + this.d);
   }

   private int c(int level) {
      if (level <= 0) {
         return 0;
      } else if (Interface.aM_.field_1687 != null && Interface.aM_.field_1687.method_30349().method_30530(class_7924.field_41265).method_35842(this.b)) {
         int iMethod_8183;
         if (this.b.equals(class_1893.field_9118)) {
            iMethod_8183 = 7;
         } else {
            iMethod_8183 = !this.b.equals(class_1893.field_9111) && !this.b.equals(class_1893.field_9119)
               ? ((class_1887)Interface.aM_.field_1687.method_30349().method_30530(class_7924.field_41265).method_46747(this.b).comp_349()).method_8183()
               : 5;
         }

         return Math.max(1, Math.min(iMethod_8183, level));
      } else {
         return level;
      }
   }
}
