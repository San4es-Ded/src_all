package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_1703;
import net.minecraft.class_1735;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_465;

public class ContainerEvent extends Event implements IEvent {
   private final class_465<?> screen;
   private final class_1703 handler;
   private final class_332 context;
   private final List<class_1735> slots;
   private final int mouseX;
   private final int mouseY;
   private final ContainerEvent.Phase phase;
   private class_2561 title;

   @Generated
   public class_465<?> b() {
      return this.screen;
   }

   @Generated
   public class_1703 c() {
      return this.handler;
   }

   @Generated
   public class_332 d() {
      return this.context;
   }

   @Generated
   public List<class_1735> e() {
      return this.slots;
   }

   @Generated
   public int f() {
      return this.mouseX;
   }

   @Generated
   public int g() {
      return this.mouseY;
   }

   @Generated
   public ContainerEvent.Phase h() {
      return this.phase;
   }

   @Generated
   public void a(class_2561 title) {
      this.title = title;
   }

   @Generated
   public class_2561 i() {
      return this.title;
   }

   public ContainerEvent(class_465<?> screen, class_332 context, int mouseX, int mouseY, ContainerEvent.Phase type) {
      this.screen = screen;
      this.handler = screen.method_17577();
      this.slots = screen.method_17577().field_7761;
      this.context = context;
      this.mouseX = mouseX;
      this.mouseY = mouseY;
      this.phase = type;
      this.title = screen.method_25440();
   }

   public ContainerEvent(class_465<?> screen, class_2561 title) {
      this.screen = screen;
      this.handler = screen.method_17577();
      this.slots = screen.method_17577().field_7761;
      this.context = null;
      this.mouseX = 0;
      this.mouseY = 0;
      this.phase = ContainerEvent.Phase.TITLE;
      this.title = title;
   }

   public static enum Phase {
      PRE,
      POST,
      TITLE;
   }
}
