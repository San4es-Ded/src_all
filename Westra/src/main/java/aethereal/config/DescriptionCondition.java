package aethereal.config;

import aethereal.autobuy.ItemType;
import aethereal.core.Interface;
import aethereal.render.AnimationUtil;
import aethereal.util.MathUtil;
import java.util.stream.Collectors;
import lombok.Generated;
import net.minecraft.class_1799;
import net.minecraft.class_1836;
import net.minecraft.class_1792.class_9635;

public class DescriptionCondition implements Condition {
   private final AnimationUtil a = new AnimationUtil();
   private final String b;
   private ItemType c;
   private int d;

   @Generated
   @Override
   public AnimationUtil a() {
      return this.a;
   }

   @Generated
   public String i() {
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

   public DescriptionCondition(String description) {
      this(description, 0, ItemType.ON);
   }

   public DescriptionCondition(String description, int requiredLevel) {
      this(description, requiredLevel, ItemType.ON);
   }

   public DescriptionCondition(String description, int requiredLevel, ItemType type) {
      this.b = description;
      this.c = type;
      this.d = type == ItemType.ON && requiredLevel > 0 ? Math.min(this.j(), requiredLevel) : requiredLevel;
   }

   @Override
   public String f() {
      return this.b;
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
         this.d = Math.max(1, Math.min(this.j(), this.d + delta));
      }
   }

   public boolean a(class_1799 stack) {
      if (this.c == ItemType.OFF) {
         return true;
      } else {
         String tooltip = stack.method_7950(class_9635.field_51353, Interface.aM_.field_1724, class_1836.field_41070)
            .stream()
            .skip(1L)
            .map(line -> line.getString().replaceAll("§.", "").toLowerCase().replaceAll("\\s+", "").trim())
            .collect(Collectors.joining(""));
         String needle = this.b.replaceAll("§.", "").toLowerCase().replaceAll("\\s+", "").trim();
         if (this.c == ItemType.DENY) {
            return !tooltip.contains(needle);
         } else if (!tooltip.contains(needle)) {
            return false;
         } else if (this.d == 0) {
            return true;
         } else {
            String after = tooltip.substring(tooltip.indexOf(needle) + needle.length()).trim();
            int space = after.indexOf(32);
            int iA;
            if (after.isEmpty()) {
               iA = 0;
            } else {
               iA = MathUtil.a(space > 0 ? after.substring(0, space) : after);
            }

            return Math.max(1, iA) >= this.d;
         }
      }
   }

   @Override
   public String e() {
      return this.d == 0 ? this.b : this.b + " " + MathUtil.a(this.d - 1);
   }

   private int j() {
      return !"Окисление".equals(this.b) && !"Вампиризм".equals(this.b) ? 3 : 2;
   }
}
