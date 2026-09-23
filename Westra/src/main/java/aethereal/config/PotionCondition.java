package aethereal.config;

import java.util.stream.StreamSupport;
import lombok.Generated;
import net.minecraft.class_1291;
import net.minecraft.class_1293;
import net.minecraft.class_1799;
import net.minecraft.class_1844;
import net.minecraft.class_6880;
import net.minecraft.class_9334;

public class PotionCondition {
   private final class_6880<class_1291> a;
   private final int b;
   private final int c;

   @Generated
   public class_6880<class_1291> a() {
      return this.a;
   }

   @Generated
   public int b() {
      return this.b;
   }

   @Generated
   public int c() {
      return this.c;
   }

   public PotionCondition(class_6880<class_1291> effect, int requiredLevel, int requiredDuration) {
      if (effect == null) {
         throw new IllegalArgumentException("Effect cannot be null");
      } else {
         this.a = effect;
         this.b = requiredLevel;
         this.c = requiredDuration;
      }
   }

   public boolean a(class_1799 stack) {
      class_1844 contents = (class_1844)stack.method_57824(class_9334.field_49651);
      return contents == null
         ? false
         : StreamSupport.<class_1293>stream(contents.method_57397().spliterator(), false)
            .anyMatch(effect -> effect.method_5579().equals(this.a) && effect.method_5578() + 1 == this.b && effect.method_5584() >= this.c);
   }

   @Override
   public String toString() {
      return "PotionCondition{effect="
         + this.a.method_40230().map(k -> k.method_29177().toString()).orElse("unknown")
         + ", requiredLevel="
         + this.b
         + ", requiredDuration="
         + this.c
         + "}";
   }
}
