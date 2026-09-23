package aethereal.core;

import aethereal.api.Compile;
import aethereal.api.Ultra;
import aethereal.event.DrawEvent;
import aethereal.event.KeyEvent;
import aethereal.render.EasingList;
import aethereal.render.ScaleUtil;
import aethereal.ui.screen.GUIPanel;
import aethereal.ui.screen.GUIScreen;
import java.io.File;
import java.util.List;
import lombok.Generated;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents.ClientStopping;
import net.minecraft.class_2561;
import net.minecraft.class_310;

public class Westra {
   private static Westra a;
   private Processor_2 b;
   private GUIScreen c;
   private Client d;
   private User e;
   private static volatile User jc$unifiedPendingUser$;
   private static volatile Westra jc$unifiedClient$;

   @Compile
   @Ultra
   protected void a() {
      this.e = new User("1", "Westra", "Westra", "Владелец", "01.01.2099 00:00", "");
      a = this;
      jc$bindUnifiedClient$(this);
      this.b = new Processor_2();
      this.d = new Client(false);
      ClientLifecycleEvents.CLIENT_STOPPING.register((ClientStopping)client -> this.a(client));
      EventManager.a(this);
      this.b.a();
   }

   @Compile
   @Ultra
   protected void b() {
      this.b.b();
   }

   @Compile
   @Ultra
   public String c() {
      if (new File("ide\\fruzek").exists()) {
         return "fruzek";
      } else {
         return new File("ide\\dezz").exists() ? "dezz" : null;
      }
   }

   public static void jc$publishUnifiedUser$(User user) {
      jc$unifiedPendingUser$ = user;
      Westra westra = jc$unifiedClient$;
      if (westra != null) {
         westra.e = user;
      }
   }

   private static void jc$bindUnifiedClient$(Westra westra) {
      jc$unifiedClient$ = westra;
      User user = jc$unifiedPendingUser$;
      if (user != null) {
         westra.e = user;
      }
   }

   @Generated
   public void a(Processor_2 processor) {
      this.b = processor;
   }

   @Generated
   public void a(GUIScreen guiScreen) {
      this.c = guiScreen;
   }

   @Generated
   public void a(Client client) {
      this.d = client;
   }

   @Generated
   public void a(User user) {
      this.e = user;
   }

   @Generated
   public static Westra h() {
      return a;
   }

   @Generated
   public Processor_2 d() {
      return this.b;
   }

   @Generated
   public GUIScreen e() {
      return this.c;
   }

   @Generated
   public Client f() {
      return this.d;
   }

   @Generated
   public User g() {
      return this.e;
   }

   public Westra() {
      this.a();
   }

   public void a(class_310 client) {
      this.b();
   }

   @EventTarget
   public void a(KeyEvent event) {
      if (event.d() == 1 && Interface.aM_.field_1755 == null && event.b() == 344) {
         class_310 class_310Var = Interface.aM_;
         GUIScreen gUIScreen;
         if (this.c != null) {
            gUIScreen = this.c;
         } else {
            GUIScreen gUIScreen2 = new GUIScreen(class_2561.method_43470(""));
            gUIScreen = gUIScreen2;
            this.c = gUIScreen2;
         }

         class_310Var.method_1507(gUIScreen);
      }
   }

   @EventTarget(
      a = 0
   )
   public void a(DrawEvent event) {
      if (event.b()) {
         ScaleUtil.a(event.i(), 2);

         for (Module module : h().d().t().e()) {
            module.f().a(0.0F, 1.0F, 0.3F, EasingList.i, event.g());
            module.f().a(module.m());
            module.g().a(0.0F, 1.0F, 0.3F, EasingList.i, event.g());
            module.g().a(module.n());
         }

         for (GUIPanel panel : this.e() == null ? List.of() : this.e().c()) {
            panel.b().a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
            panel.b().a(Interface.aM_.field_1755 instanceof GUIScreen);
         }
      }
   }

   @EventTarget(
      a = 4
   )
   public void b(DrawEvent event) {
      if (event.b()) {
         ScaleUtil.a(event.i());
      }
   }

   static {
      NativeMethodLookup.lookup(Westra.class, 0);
   }
}
