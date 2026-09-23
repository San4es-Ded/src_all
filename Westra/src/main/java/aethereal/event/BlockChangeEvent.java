package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;
import lombok.Generated;
import net.minecraft.class_2338;
import net.minecraft.class_2680;

public class BlockChangeEvent extends Event implements IEvent {
   private final class_2338 a;
   private final class_2680 b;
   private final class_2680 c;

   @Generated
   public BlockChangeEvent(class_2338 pos, class_2680 oldState, class_2680 state) {
      this.a = pos;
      this.b = oldState;
      this.c = state;
   }

   @Generated
   public class_2338 b() {
      return this.a;
   }

   @Generated
   public class_2680 c() {
      return this.b;
   }

   @Generated
   public class_2680 d() {
      return this.c;
   }
}
