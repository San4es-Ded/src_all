package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;
import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_4587;

public class HandAnimationEvent extends Event implements IEvent {
   private final class_4587 a;
   private final class_1268 b;
   private final float c;
   private final int d;

   @Generated
   public HandAnimationEvent(class_4587 matrices, class_1268 hand, float swingProgress, int armX) {
      this.a = matrices;
      this.b = hand;
      this.c = swingProgress;
      this.d = armX;
   }

   @Generated
   public class_4587 b() {
      return this.a;
   }

   @Generated
   public class_1268 c() {
      return this.b;
   }

   @Generated
   public float d() {
      return this.c;
   }

   @Generated
   public int e() {
      return this.d;
   }
}
