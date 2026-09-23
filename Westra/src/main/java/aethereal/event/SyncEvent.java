package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;
import lombok.Generated;
import net.minecraft.class_1799;

public class SyncEvent extends Event implements IEvent {
   private final int a;
   private class_1799 b;

   @Generated
   public void a(class_1799 stack) {
      this.b = stack;
   }

   @Generated
   public int b() {
      return this.a;
   }

   @Generated
   public class_1799 c() {
      return this.b;
   }

   public SyncEvent(int slot, class_1799 stack) {
      this.a = slot;
      this.b = stack;
   }
}
