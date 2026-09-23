package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_1799;
import net.minecraft.class_2561;

public class TooltipEvent extends Event implements IEvent {
   private final class_1799 a;
   private final List<class_2561> b;

   @Generated
   public class_1799 b() {
      return this.a;
   }

   @Generated
   public List<class_2561> c() {
      return this.b;
   }

   public TooltipEvent(class_1799 stack, List<class_2561> lines) {
      this.a = stack;
      this.b = lines;
   }
}
