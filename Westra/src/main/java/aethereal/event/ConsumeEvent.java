package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;
import lombok.Generated;
import net.minecraft.class_1799;

public class ConsumeEvent extends Event implements IEvent {
   private final class_1799 a;

   @Generated
   public ConsumeEvent(class_1799 stack) {
      this.a = stack;
   }

   @Generated
   public class_1799 b() {
      return this.a;
   }
}
