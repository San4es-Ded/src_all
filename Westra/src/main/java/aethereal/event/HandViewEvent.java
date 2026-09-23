package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;
import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_1799;
import net.minecraft.class_4587;

public class HandViewEvent extends Event implements IEvent {
   private final class_4587 a;
   private final class_1799 b;
   private final class_1268 c;

   @Generated
   public HandViewEvent(class_4587 matrices, class_1799 stack, class_1268 hand) {
      this.a = matrices;
      this.b = stack;
      this.c = hand;
   }

   @Generated
   public class_4587 b() {
      return this.a;
   }

   @Generated
   public class_1799 c() {
      return this.b;
   }

   @Generated
   public class_1268 d() {
      return this.c;
   }
}
