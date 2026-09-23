package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;
import lombok.Generated;
import net.minecraft.class_332;

public class CrosshairEvent extends Event implements IEvent {
   private final class_332 a;
   private final float b;

   @Generated
   public class_332 b() {
      return this.a;
   }

   @Generated
   public float c() {
      return this.b;
   }

   public CrosshairEvent(class_332 context, float partialTicks) {
      this.a = context;
      this.b = partialTicks;
   }
}
