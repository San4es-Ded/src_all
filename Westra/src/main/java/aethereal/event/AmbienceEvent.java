package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;
import lombok.Generated;
import net.minecraft.class_4184;
import net.minecraft.class_6854;
import net.minecraft.class_9958;
import net.minecraft.class_1959.class_1963;

public class AmbienceEvent {
   public static class a extends Event implements IEvent {
      private float a;
      private float b;
      private float c;
      private float d;

      @Generated
      public void a(float red) {
         this.a = red;
      }

      @Generated
      public void b(float green) {
         this.b = green;
      }

      @Generated
      public void c(float blue) {
         this.c = blue;
      }

      @Generated
      public void d(float alpha) {
         this.d = alpha;
      }

      @Generated
      public a(float red, float green, float blue, float alpha) {
         this.a = red;
         this.b = green;
         this.c = blue;
         this.d = alpha;
      }

      @Generated
      public float b() {
         return this.a;
      }

      @Generated
      public float c() {
         return this.b;
      }

      @Generated
      public float d() {
         return this.c;
      }

      @Generated
      public float e() {
         return this.d;
      }
   }

   public static class b extends Event implements IEvent {
      private class_4184 a;
      private float b;
      private class_9958 c;

      @Generated
      public void a(class_4184 camera) {
         this.a = camera;
      }

      @Generated
      public void a(float viewDistance) {
         this.b = viewDistance;
      }

      @Generated
      public void a(class_9958 fog) {
         this.c = fog;
      }

      @Generated
      public b(class_4184 camera, float viewDistance, class_9958 fog) {
         this.a = camera;
         this.b = viewDistance;
         this.c = fog;
      }

      @Generated
      public class_4184 b() {
         return this.a;
      }

      @Generated
      public float c() {
         return this.b;
      }

      @Generated
      public class_9958 d() {
         return this.c;
      }

      public void a(float start, float end, class_6854 shape, float red, float green, float blue, float alpha) {
         this.c = new class_9958(start, end, shape, red, green, blue, alpha);
      }
   }

   public static class c extends Event implements IEvent {
      private long a;

      @Generated
      public void a(long time) {
         this.a = time;
      }

      @Generated
      public c(long time) {
         this.a = time;
      }

      @Generated
      public long b() {
         return this.a;
      }
   }

   public static class d extends Event implements IEvent {
      private final AmbienceEvent.d.a a;
      private float b;
      private class_1963 c;

      @Generated
      public AmbienceEvent.d.a b() {
         return this.a;
      }

      @Generated
      public float c() {
         return this.b;
      }

      @Generated
      public void a(float floatValue) {
         this.b = floatValue;
      }

      @Generated
      public class_1963 d() {
         return this.c;
      }

      @Generated
      public void a(class_1963 precipitationValue) {
         this.c = precipitationValue;
      }

      public d(AmbienceEvent.d.a type, float value) {
         this.a = type;
         this.b = value;
      }

      public d(AmbienceEvent.d.a type, class_1963 value) {
         this.a = type;
         this.c = value;
      }

      public static enum a {
         RAIN_GRADIENT,
         THUNDER_GRADIENT,
         PRECIPITATION_PARTICLES,
         PRECIPITATION;
      }
   }
}
