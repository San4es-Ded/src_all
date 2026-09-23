package aethereal.ui.widget;

import aethereal.config.ThemeInfo;
import aethereal.core.GlobalEvent;
import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.event.BackendEvent;
import aethereal.event.DrawEvent;
import aethereal.event.PacketEvent;
import aethereal.notification.Notification;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import aethereal.ui.element.DragInfo;
import net.minecraft.class_1542;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2561;
import net.minecraft.class_2775;
import net.minecraft.class_408;
import net.minecraft.class_746;
import net.minecraft.class_9334;

public class NotificationWidget extends Widget implements Interface {
   private final BooleanSetting f = new BooleanSetting("Оповещать о поднятии донат-предметов", true);
   private final BooleanSetting g = new BooleanSetting("Обновления и уведомления друзей", true);

   public NotificationWidget() {
      super(new DragInfo("Уведомления", 0.0F, 0.0F, 0.0F, 0.0F));
      this.j().a(this);
      this.j().a(1);
      this.a(new Setting[]{this.g, this.f});
   }

   @Override
   public void a(GlobalEvent event) {
      this.d().a(aM_.field_1755 instanceof class_408 || !Westra.h().d().m().b().isEmpty());
      super.a(event);
   }

   @Override
   public void a(DrawEvent event) {
      this.k(event);
      super.a(event);
   }

   protected void k(DrawEvent event) {
      this.d().a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
      float contentY = this.j().b();

      for (Notification notification : Westra.h().d().m().b()) {
         float animation = notification.a().c() * this.a();
         if (animation > 0.0F) {
            Object message = notification.c();
            float fA;
            if (message instanceof class_2561 value) {
               fA = Fonts.e.a(value, this.e);
            } else {
               fA = Fonts.e.a(String.valueOf(message), this.e);
            }

            float width = 17.5F + fA + 4.0F;
            float x = (aM_.method_22683().method_4486() - width) / 2.0F;
            int color = notification.e() == -1 ? Westra.h().d().o().a(ThemeInfo.PRIMARY).a() : notification.e();
            if (notification.d() instanceof class_1799 stack) {
               this.a(event, x, contentY, stack, message, width, animation, color);
            } else {
               this.a(event, x, contentY, (String)notification.d(), message, width, animation, color);
            }

            this.j().a(x);
            this.j().c(width);
            this.j().d(this.d);
            contentY += (this.d + 3.5F) * animation;
         }
      }
   }

   @Override
   public void a(PacketEvent event) {
      if (this.f.c()
         && event.c()
         && event.d() instanceof class_2775 class_2775VarD
         && aM_.field_1687.method_8469(class_2775VarD.method_11912()) instanceof class_1657 class_746VarMethod_8469) {
         class_746 class_746Var = (class_746)class_746VarMethod_8469;
         if (aM_.field_1687.method_8469(class_2775VarD.method_11915()) instanceof class_1542 class_1542VarMethod_8469
            && class_746Var != aM_.field_1724
            && !class_1542VarMethod_8469.method_6983().method_7964().getString().contains("Упс.")
            && (
               class_1542VarMethod_8469.method_6983().method_57826(class_9334.field_49631)
                     && class_1542VarMethod_8469.method_6983().method_57826(class_9334.field_49632)
                  || class_1542VarMethod_8469.method_6983().method_31574(class_1802.field_8367)
            )) {
            Westra.h()
               .d()
               .m()
               .a(
                  new Notification(
                     class_1542VarMethod_8469.method_6983().method_7972(),
                     class_746Var.method_5477()
                        .method_27661()
                        .method_27693(" подобрал ")
                        .method_10852(class_1542VarMethod_8469.method_6983().method_7964())
                        .method_27693(class_2775VarD.method_11913() > 1 ? " x" + class_2775VarD.method_11913() : ""),
                     1500
                  )
               );
         }
      }

      super.a(event);
   }

   @Override
   public void a(BackendEvent event) {
      String message = event.d().a().a(event.d().c(), "message");
      if ("friend".equals(event.d().b()) && this.g.c() && message != null) {
         Westra.h().d().m().a(new Notification("o", message, 5000));
      }

      if ("application".equals(event.d().b()) && message != null) {
         Westra.h().d().m().a(new Notification("o", message, 15000));
      }

      super.a(event);
   }
}
