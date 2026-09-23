package aethereal.notification;

import aethereal.render.AnimationUtil;
import aethereal.util.CounterUtil;
import lombok.Generated;
import net.minecraft.class_1799;

public class Notification {
   private final AnimationUtil a = new AnimationUtil();
   private final CounterUtil b = new CounterUtil();
   private final Object c;
   private final Object d;
   private final int e;
   private int f;

   @Generated
   public void a(int time) {
      this.f = time;
   }

   @Generated
   public AnimationUtil a() {
      return this.a;
   }

   @Generated
   public CounterUtil b() {
      return this.b;
   }

   @Generated
   public Object c() {
      return this.c;
   }

   @Generated
   public Object d() {
      return this.d;
   }

   @Generated
   public int e() {
      return this.e;
   }

   @Generated
   public int f() {
      return this.f;
   }

   public Notification(Object symbol, int color, Object message, int time) {
      if (!(symbol instanceof String) && !(symbol instanceof class_1799)) {
         throw new IllegalArgumentException("Icon must be either String or ItemStack");
      } else {
         this.d = symbol;
         this.e = color;
         this.c = message;
         this.f = time;
      }
   }

   public Notification(Object symbol, Object message, int time) {
      this(symbol, -1, message, time);
   }
}
