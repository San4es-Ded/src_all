package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;
import lombok.Generated;
import net.minecraft.class_1297;
import net.minecraft.class_238;

public class BoundingBoxEvent extends Event implements IEvent {
   public class_238 a;
   public class_1297 b;

   @Generated
   public void a(class_238 box) {
      this.a = box;
   }

   @Generated
   public void a(class_1297 entity) {
      this.b = entity;
   }

   @Generated
   public BoundingBoxEvent(class_238 box, class_1297 entity) {
      this.a = box;
      this.b = entity;
   }

   @Generated
   public class_238 b() {
      return this.a;
   }

   @Generated
   public class_1297 c() {
      return this.b;
   }
}
