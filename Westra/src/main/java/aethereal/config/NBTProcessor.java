package aethereal.config;

import aethereal.autobuy.ItemFilter;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_1799;

public class NBTProcessor implements ItemFilter {
   private final List<NBTCondition> a = new ArrayList<>();

   @Generated
   public List<NBTCondition> a() {
      return this.a;
   }

   public NBTProcessor a(NBTCondition condition) {
      this.a.add(condition);
      return this;
   }

   public NBTProcessor a(String value) {
      return this.a(new NBTCondition(value));
   }

   @Override
   public boolean a(class_1799 stack) {
      return this.a.stream().allMatch(condition -> condition.a(stack));
   }
}
