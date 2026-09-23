package aethereal.notification;

import aethereal.api.Compile;
import aethereal.config.BaseProcessor;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.NativeMethodLookup;
import aethereal.event.DrawEvent;
import aethereal.event.TickEvent;
import aethereal.render.EasingList;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_408;

public class NotificationProcessor extends BaseProcessor implements Interface {
   private final Notification b = new Notification("o", "Пример отображения уведомления", 0);
   private final List<Notification> c = new ArrayList<>();

   @Compile
   @Override
   public void setup() {
   }

   @Generated
   public Notification a() {
      return this.b;
   }

   @Generated
   public List<Notification> b() {
      return this.c;
   }

   @Override
   public void unSetup() {
   }

   @EventTarget
   public void a(DrawEvent event) {
      if (event.b() && !this.b().isEmpty()) {
         for (Notification notification : this.b()) {
            notification.a().a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
         }
      }
   }

   @EventTarget
   public void a(TickEvent event) {
      List<Notification> notifications = new ArrayList<>(this.b());
      notifications.remove(this.b);
      boolean preview = aM_.field_1755 instanceof class_408 && notifications.isEmpty();
      if (!this.b().contains(this.b)) {
         this.b().add(this.b);
      }

      for (Notification notification : new ArrayList<>(this.b())) {
         if (notification == this.b) {
            notification.a().a(preview);
            if (!preview && notification.a().c() == 0.0F) {
               this.b().remove(this.b);
            }
         } else {
            boolean finished = notification.b().a(notification.f() - 100);
            notification.a().a(!finished);
            if (finished && notification.a().c() == 0.0F) {
               this.b().remove(notification);
            }
         }
      }
   }

   public void a(Notification notification) {
      int time = notification.f();
      notification.a(time + (int)this.b().stream().filter(current -> current.f() >= time).filter(current2 -> (current2.f() - time) % 50 == 0).count() * 50);
      this.b().add(notification);
   }

   static {
      NativeMethodLookup.lookup(NotificationProcessor.class, 34);
   }
}
