package aethereal.util;

import aethereal.core.Interface;
import java.util.Objects;
import lombok.Generated;
import net.minecraft.class_1297;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import platform.inject.accessors.ClientPlayerEntityAccessor;

public class Rotation implements Interface {
   private float b;
   private float c;

   @Generated
   public void a(float yaw) {
      this.b = yaw;
   }

   @Generated
   public void b(float pitch) {
      this.c = pitch;
   }

   @Generated
   public Rotation() {
   }

   @Generated
   public Rotation(float yaw, float pitch) {
      this.b = yaw;
      this.c = pitch;
   }

   @Generated
   public float c() {
      return this.b;
   }

   @Generated
   public float d() {
      return this.c;
   }

   public Rotation(class_1297 entity) {
      this.b = entity.method_36454();
      this.c = entity.method_36455();
   }

   public double a(Rotation targetRotation) {
      if (targetRotation == null) {
         return 0.0;
      } else {
         double yawDelta = class_3532.method_15393(targetRotation.c() - this.b);
         double pitchDelta = class_3532.method_15393(targetRotation.d() - this.c);
         return Math.hypot(Math.abs(yawDelta), Math.abs(pitchDelta));
      }
   }

   public static Rotation a() {
      if (aM_.field_1724 == null) {
         return new Rotation(Look.b(), Look.c());
      } else {
         float py = aM_.field_1724.method_36454();
         float fy = Look.b();
         return new Rotation(py + class_3532.method_15393(fy - py), Look.c());
      }
   }

   public static Rotation a(class_243 eye, class_243 point) {
      class_243 diff = point.method_1020(eye);
      double dist = Math.sqrt(diff.field_1352 * diff.field_1352 + diff.field_1350 * diff.field_1350);
      float yaw = (float)Math.toDegrees(Math.atan2(diff.field_1350, diff.field_1352)) - 90.0F;
      float pitch = (float)(-Math.toDegrees(Math.atan2(diff.field_1351, dist)));
      return new Rotation(class_3532.method_15393(yaw), class_3532.method_15363(pitch, -90.0F, 90.0F));
   }

   public static Rotation b() {
      ClientPlayerEntityAccessor accessor = (ClientPlayerEntityAccessor)aM_.field_1724;
      return new Rotation(Objects.requireNonNull(accessor).getLastYaw(), accessor.getLastPitch());
   }
}
