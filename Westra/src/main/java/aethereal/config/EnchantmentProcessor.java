package aethereal.config;

import aethereal.autobuy.ItemFilter;
import aethereal.autobuy.ItemType;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_1799;
import net.minecraft.class_1887;
import net.minecraft.class_1893;
import net.minecraft.class_5321;

public class EnchantmentProcessor implements ItemFilter {
   private final List<EnchantmentCondition> a = new ArrayList<>();

   @Generated
   public List<EnchantmentCondition> b() {
      return this.a;
   }

   public EnchantmentProcessor a(EnchantmentCondition condition) {
      this.a.add(condition);
      return this;
   }

   public EnchantmentProcessor a(class_5321<class_1887> key) {
      return this.a(new EnchantmentCondition(key));
   }

   public EnchantmentProcessor a(class_5321<class_1887> key, int requiredLevel) {
      return this.a(new EnchantmentCondition(key, requiredLevel));
   }

   public EnchantmentProcessor b(class_5321<class_1887> key) {
      return this.a(new EnchantmentCondition(key, 0, ItemType.DENY));
   }

   public EnchantmentProcessor b(class_5321<class_1887> key, int maxLevel) {
      return this.a(new EnchantmentCondition(key, maxLevel, ItemType.DENY));
   }

   public EnchantmentProcessor a() {
      return this.b(class_1893.field_9097).b(class_1893.field_9121).b(class_1893.field_9113).b(class_1893.field_9109);
   }

   @Override
   public boolean a(class_1799 stack) {
      return this.a.stream().allMatch(condition -> condition.a(stack));
   }
}
