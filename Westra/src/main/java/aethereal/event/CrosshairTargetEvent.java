package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;
import lombok.Generated;
import net.minecraft.class_239;

public class CrosshairTargetEvent extends Event implements IEvent {
   private final float a;
   private class_239 b;

   @Generated
   public void a(class_239 target) {
      this.b = target;
   }

   @Generated
   public float b() {
      return this.a;
   }

   @Generated
   public class_239 c() {
      return this.b;
   }

   public CrosshairTargetEvent(float tickDelta) {
      this.a = tickDelta;
   }
}
