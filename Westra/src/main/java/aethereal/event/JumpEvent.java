package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;
import lombok.Generated;
import net.minecraft.class_1309;

public class JumpEvent extends Event implements IEvent {
   private final class_1309 a;

   @Generated
   public JumpEvent(class_1309 livingEntity) {
      this.a = livingEntity;
   }

   @Generated
   public class_1309 b() {
      return this.a;
   }
}
