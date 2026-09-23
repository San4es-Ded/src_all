package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;
import lombok.Generated;
import net.minecraft.class_1792;

public class CooldownEvent extends Event implements IEvent {
   private final class_1792 a;
   private final int b;

   @Generated
   public class_1792 b() {
      return this.a;
   }

   @Generated
   public int c() {
      return this.b;
   }

   public CooldownEvent(class_1792 item, int cooldown) {
      this.a = item;
      this.b = cooldown;
   }
}
