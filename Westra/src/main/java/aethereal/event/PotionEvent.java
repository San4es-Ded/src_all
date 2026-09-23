package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;
import lombok.Generated;
import net.minecraft.class_2338;

public class PotionEvent extends Event implements IEvent {
   private final PotionEvent.a a;
   private final int b;
   private final class_2338 c;

   @Generated
   public PotionEvent(PotionEvent.a type, int data, class_2338 pos) {
      this.a = type;
      this.b = data;
      this.c = pos;
   }

   @Generated
   public PotionEvent.a b() {
      return this.a;
   }

   @Generated
   public int c() {
      return this.b;
   }

   @Generated
   public class_2338 d() {
      return this.c;
   }

   public static enum a {
      PARTICLES;
   }
}
