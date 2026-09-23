package aethereal.handler;

import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.event.ClickEvent;
import aethereal.event.ContainerEvent;
import aethereal.event.TickEvent;
import aethereal.ui.shader.GradientUtil;
import aethereal.util.ChatUtil;
import aethereal.util.MathUtil;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Generated;
import net.minecraft.class_1109;
import net.minecraft.class_1707;
import net.minecraft.class_1713;
import net.minecraft.class_1735;
import net.minecraft.class_1836;
import net.minecraft.class_2561;
import net.minecraft.class_2813;
import net.minecraft.class_2815;
import net.minecraft.class_2960;
import net.minecraft.class_3417;
import net.minecraft.class_465;
import net.minecraft.class_476;
import net.minecraft.class_5250;
import net.minecraft.class_1792.class_9635;
import platform.inject.accessors.HandledScreenAccessor;

@Handler_2
public class ANFindHandler extends BaseHandler implements Interface {
   private final List<ANFindHandler.a> b = List.of(
      new ANFindHandler.a("Команды х1"),
      new ANFindHandler.a("Команды х2"),
      new ANFindHandler.a("Команды х3"),
      new ANFindHandler.a("Команды х5"),
      new ANFindHandler.a("Команды х10")
   );
   private boolean c;
   private boolean d;
   private ANFindHandler.Phase e;
   private int f;

   @EventTarget
   public void a(ContainerEvent event) {
      this.c = false;
      if (event.h() == ContainerEvent.Phase.POST
         && (event.b().method_25440().getString().contains("☬ Выберите режим:") || event.b().method_25440().getString().contains("☬ Выберите тип режима:"))) {
         HandledScreenAccessor screen = (HandledScreenAccessor)event.b();
         float x = screen.getX() + screen.getBackgroundWidth() - 17;
         float y = screen.getY() + 5;
         this.c = MathUtil.a(event.f(), event.g(), x, y, 10.0F, 10.0F);
         Westra.h()
            .d()
            .i()
            .a(
               event.d().method_51448(),
               class_2960.method_60655("westra", this.c ? "pictures/minecraft/join_button_hovered.png" : "pictures/minecraft/join_button.png"),
               x,
               y,
               10.0F,
               10.0F,
               0.0F,
               -1
            );
         if (this.c) {
            event.d()
               .method_51434(event.b().method_64506(), List.of(class_2561.method_30163("Авто-поиск анархии с наименьшим онлайном")), event.f(), event.g());
         }
      }
   }

   @EventTarget
   public void a(ClickEvent event) {
      if (event.b() && this.c) {
         class_465<?> class_465Var = (class_465<?>)aM_.field_1755;
         if (class_465Var instanceof class_465
            && (
               class_465Var.method_25440().getString().contains("☬ Выберите режим:")
                  || class_465Var.method_25440().getString().contains("☬ Выберите тип режима:")
            )) {
            this.d = !this.d;
            if (this.d) {
               this.e = ANFindHandler.Phase.SELECT_MODE;
               this.f = 0;
               this.b.forEach(v0 -> v0.a());
            }

            aM_.method_1483().method_4873(class_1109.method_47978(class_3417.field_15015, 1.0F));
         }
      }
   }

   @EventTarget
   public void a(TickEvent event) {
      if (this.d) {
         if (aM_.field_1755 instanceof class_476 screen) {
            ANFindHandler.a mode = this.b.get(this.f);
            switch (this.e) {
               case SELECT_MODE:
                  if (this.a(screen, "Анархия 1.21.11")) {
                     this.e = ANFindHandler.Phase.SELECT_TYPE;
                  }
                  break;
               case SELECT_TYPE:
                  if (this.a(screen, mode.a)) {
                     this.e = ANFindHandler.Phase.COLLECT;
                  }
                  break;
               case COLLECT:
                  this.a(screen, mode);
            }
         }

         this.d = false;
      }
   }

   private void a(class_476 screen, ANFindHandler.a mode) {
      boolean selected = false;
      int bestOnline = Integer.MAX_VALUE;
      String bestServer = null;

      for (class_1735 slot : ((class_1707)screen.method_17577()).field_7761) {
         String name = slot.method_7677().method_7964().getString();
         if (slot.method_7677().method_7958() && name.contains(mode.a)) {
            selected = true;
         }

         if (name.contains("Анархия-")) {
            for (class_2561 line : slot.method_7677().method_7950(class_9635.field_51353, aM_.field_1724, class_1836.field_41070)) {
               Matcher matcher = Pattern.compile("Онлайн режима: (\\d+)").matcher(line.getString());
               if (matcher.find()) {
                  int online = Integer.parseInt(matcher.group(1));
                  if (online < bestOnline) {
                     bestOnline = online;
                     bestServer = name.replaceAll("§.", "");
                  }
                  break;
               }
            }
         }
      }

      if (selected && bestServer != null) {
         mode.c = bestOnline;
         mode.b = bestServer;
         int i = this.f + 1;
         this.f = i;
         if (i >= this.b.size()) {
            this.a();
         } else {
            this.e = ANFindHandler.Phase.SELECT_TYPE;
         }
      }
   }

   private void a() {
      this.d = false;
      this.b
         .stream()
         .filter(mode -> mode.b != null)
         .min(Comparator.comparingInt(mode2 -> mode2.c))
         .ifPresent(
            best -> {
               String anarchy = best.b.replace("»", "").replace("Анархия-", "").trim();
               if (!anarchy.isEmpty()) {
                  class_5250 hover = GradientUtil.a("Минимальный онлайн по командам:\n", -7620097, -11503416, 1, 5.0F);
                  this.b
                     .stream()
                     .filter(mode3 -> mode3.b != null)
                     .forEach(
                        mode4 -> hover.method_10852(
                           class_2561.method_43470("§7• §f" + mode4.a.replace("Команды ", "") + ": " + mode4.b.trim() + " — " + mode4.c + " игроков\n")
                        )
                     );
                  ChatUtil.a(
                     class_2561.method_43470("§a✔ §7Успешно подключился к анархии §a#" + anarchy + "§7, с онлайном §a" + best.c + "§7 — ")
                        .method_10852(ChatUtil.a((Object)"§c[Подробнее]", (class_2561)hover))
                  );
                  aM_.field_1724.field_3944.method_52787(new class_2815(aM_.field_1724.field_7512.field_7763));
                  aM_.field_1724.field_3944.method_45730("an" + anarchy);
               }
            }
         );
   }

   private boolean a(class_476 screen, String contains) {
      for (class_1735 slot : ((class_1707)screen.method_17577()).field_7761) {
         if (slot.method_7677().method_7964().getString().contains(contains)) {
            aM_.field_1724
               .field_3944
               .method_52787(
                  new class_2813(
                     ((class_1707)screen.method_17577()).field_7763,
                     ((class_1707)screen.method_17577()).method_37421(),
                     slot.field_7874,
                     0,
                     class_1713.field_7790,
                     ((class_1707)screen.method_17577()).method_34255().method_7972(),
                     Int2ObjectMaps.emptyMap()
                  )
               );
            return true;
         }
      }

      return false;
   }

   static enum Phase {
      SELECT_MODE,
      SELECT_TYPE,
      COLLECT;
   }

   public static class a {
      final String a;
      String b;
      int c;

      @Generated
      public String b() {
         return this.a;
      }

      @Generated
      public String c() {
         return this.b;
      }

      @Generated
      public int d() {
         return this.c;
      }

      public a(String title) {
         this.a = title;
      }

      public void a() {
         this.b = null;
         this.c = -1;
      }
   }
}
