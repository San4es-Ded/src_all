package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;
import lombok.Generated;
import net.minecraft.class_1113;

public class SoundEvent extends Event implements IEvent {
   private final class_1113 a;
   private float b;

   @Generated
   public class_1113 b() {
      return this.a;
   }

   @Generated
   public void a(float volume) {
      this.b = volume;
   }

   @Generated
   public float c() {
      return this.b;
   }

   public SoundEvent(class_1113 sound, float volume) {
      this.a = sound;
      this.b = volume;
   }
}
