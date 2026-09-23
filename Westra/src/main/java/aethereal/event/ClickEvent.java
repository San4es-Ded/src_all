package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;
import lombok.Generated;

public class ClickEvent extends Event implements IEvent {
   private final ClickEvent.a a;
   private final double b;
   private final double c;
   private final int d;

   @Generated
   public ClickEvent.a e() {
      return this.a;
   }

   @Generated
   public double f() {
      return this.b;
   }

   @Generated
   public double g() {
      return this.c;
   }

   @Generated
   public int h() {
      return this.d;
   }

   public ClickEvent(double mouseX, double mouseY, int button, ClickEvent.a type) {
      this.b = mouseX;
      this.c = mouseY;
      this.d = button;
      this.a = type;
   }

   public boolean b() {
      return this.a == ClickEvent.a.PRESS;
   }

   public boolean c() {
      return this.a == ClickEvent.a.RELEASE;
   }

   public boolean d() {
      return this.a == ClickEvent.a.DRAG;
   }

   public static enum a {
      PRESS,
      RELEASE,
      DRAG;
   }
}
