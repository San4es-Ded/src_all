package aethereal.config;

import aethereal.autobuy.ItemFilter;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_1799;

public class AttributeProcessor implements ItemFilter {
   private final List<AttributeCondition> a = new ArrayList<>();

   public AttributeProcessor a(AttributeCondition condition) {
      this.a.add(condition);
      return this;
   }

   @Override
   public boolean a(class_1799 stack) {
      return this.a.stream().allMatch(condition -> condition.a(stack));
   }
}
