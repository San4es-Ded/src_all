package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;
import lombok.Generated;
import net.minecraft.class_243;

public class CameraPositionEvent extends Event implements IEvent {
   private class_243 a;

   @Generated
   public void a(class_243 position) {
      this.a = position;
   }

   @Generated
   public class_243 b() {
      return this.a;
   }

   public CameraPositionEvent(class_243 position) {
      this.a = position;
   }
}
