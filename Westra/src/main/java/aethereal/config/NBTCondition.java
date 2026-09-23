package aethereal.config;

import aethereal.core.Interface;
import lombok.Generated;
import net.minecraft.class_1799;
import net.minecraft.class_2520;

public class NBTCondition {
   private final String a;

   @Generated
   public String a() {
      return this.a;
   }

   public NBTCondition(String value) {
      this.a = value;
   }

   public boolean a(class_1799 stack) {
      class_2520 nbt = stack.method_57358(Interface.aM_.field_1687.method_30349());
      return nbt != null
         && nbt.toString()
            .replaceAll("§.", "")
            .toLowerCase()
            .replaceAll("\\s+", "")
            .trim()
            .contains(this.a.replaceAll("§.", "").toLowerCase().replaceAll("\\s+", "").trim());
   }
}
