package aethereal.config;

import aethereal.autobuy.ItemFilter;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_1799;

public class PotionProcessor implements ItemFilter {
   private final List<PotionCondition> a = new ArrayList<>();

   @Generated
   public List<PotionCondition> a() {
      return this.a;
   }

   public PotionProcessor a(PotionCondition condition) {
      this.a.add(condition);
      return this;
   }

   @Override
   public boolean a(class_1799 stack) {
      return this.a.stream().allMatch(condition -> condition.a(stack));
   }
}
