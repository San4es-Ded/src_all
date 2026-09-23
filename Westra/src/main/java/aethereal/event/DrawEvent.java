package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;
import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.render.Draw2DProcessor;
import aethereal.render.Draw3DProcessor;
import lombok.Generated;
import net.minecraft.class_332;
import net.minecraft.class_4587;

public class DrawEvent extends Event implements Interface, IEvent {
   private final Draw2DProcessor b = Westra.h().d().i();
   private final Draw3DProcessor c = Westra.h().d().j();
   private final DrawEvent.a d;
   private final float e;
   private final class_4587 f;
   private class_332 g;

   @Generated
   public Draw2DProcessor d() {
      return this.b;
   }

   @Generated
   public Draw3DProcessor e() {
      return this.c;
   }

   @Generated
   public DrawEvent.a f() {
      return this.d;
   }

   @Generated
   public float g() {
      return this.e;
   }

   @Generated
   public class_4587 h() {
      return this.f;
   }

   @Generated
   public class_332 i() {
      return this.g;
   }

   public DrawEvent(class_4587 stack, float tickDelta, DrawEvent.a type) {
      this.f = stack;
      this.e = tickDelta;
      this.d = type;
   }

   public DrawEvent(class_332 context, float tickDelta, DrawEvent.a type) {
      this.g = context;
      this.f = context.method_51448();
      this.e = tickDelta;
      this.d = type;
   }

   public boolean b() {
      return this.d == DrawEvent.a.D2D;
   }

   public boolean c() {
      return this.d == DrawEvent.a.D3D;
   }

   public static enum a {
      D2D,
      D3D;
   }
}
