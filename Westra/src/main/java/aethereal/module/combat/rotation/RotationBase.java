package aethereal.module.combat.rotation;

import aethereal.core.Interface;
import aethereal.util.Rotation;
import java.security.SecureRandom;
import lombok.Generated;
import net.minecraft.class_1309;
import net.minecraft.class_640;

public abstract class RotationBase implements Interface {
   protected final SecureRandom rng = new SecureRandom();
   protected float lastYaw;
   protected float lastPitch;

   public abstract void a(class_1309 var1, Rotation var2, int var3);

   public void b() {
      if (aM_.field_1724 != null) {
         this.lastYaw = aM_.field_1724.method_36454();
         this.lastPitch = aM_.field_1724.method_36455();
      }
   }

   @Generated
   public float c() {
      return this.lastYaw;
   }

   @Generated
   public float d() {
      return this.lastPitch;
   }

   protected int e() {
      if (aM_.method_1562() != null && aM_.field_1724 != null) {
         class_640 entry = aM_.method_1562().method_2871(aM_.field_1724.method_5667());
         if (entry != null) {
            return entry.method_2959();
         }
      }

      return 50;
   }
}
