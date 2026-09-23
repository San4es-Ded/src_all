package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;
import lombok.Generated;
import net.minecraft.class_2561;

public class ScoreboardEvent extends Event implements IEvent {
   private class_2561 a;

   @Generated
   public void a(class_2561 title) {
      this.a = title;
   }

   @Generated
   public class_2561 b() {
      return this.a;
   }

   public ScoreboardEvent(class_2561 title) {
      this.a = title;
   }
}
