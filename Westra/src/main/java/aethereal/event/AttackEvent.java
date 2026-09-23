package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;
import lombok.Generated;
import net.minecraft.class_1297;

public class AttackEvent extends Event implements IEvent {
   private final class_1297 a;

   @Generated
   public class_1297 b() {
      return this.a;
   }

   public AttackEvent(class_1297 entity) {
      this.a = entity;
   }
}
