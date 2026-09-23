package aethereal.command;

import aethereal.config.ThemeInfo;
import aethereal.core.EventTarget;
import aethereal.core.Westra;
import aethereal.event.DrawEvent;
import aethereal.event.PacketEvent;
import aethereal.event.TickEvent;
import aethereal.render.ColorUtil;
import aethereal.render.Fonts;
import aethereal.util.ChatUtil;
import aethereal.util.ProjectUtil;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Generated;
import net.minecraft.class_2172;
import net.minecraft.class_243;
import net.minecraft.class_2561;
import net.minecraft.class_2583;
import net.minecraft.class_2960;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import net.minecraft.class_7439;
import net.minecraft.class_7833;
import org.joml.Vector2f;

@Command(
   a = "way"
)
public class WayCommand extends BaseCommand {
   private final List<WayCommand.b> c = new ArrayList<>();
   private final List<String> d = new ArrayList<>();
   private WayCommand.a e = WayCommand.a.NONE;

   @Generated
   public List<WayCommand.b> c() {
      return this.c;
   }

   @Generated
   public void a(WayCommand.a eventMode) {
      this.e = eventMode;
   }

   @Override
   public void a(LiteralArgumentBuilder<class_2172> builder) {
      ((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)builder.then(
                           ((LiteralArgumentBuilder)this.a("add").executes(context -> {
                              ChatUtil.a("Использование: .way add <название> <x> <z> или .way add <название> <x> <y> <z>\"");
                              return 1;
                           })).then(((RequiredArgumentBuilder)this.d("название").executes(context2 -> {
                              ChatUtil.a("Использование: .way add <название> <x> <z> или .way add <название> <x> <y> <z>\"");
                              return 1;
                           })).then(((RequiredArgumentBuilder)this.e("x").executes(context3 -> {
                              ChatUtil.a("Использование: .way add <название> <x> <z> или .way add <название> <x> <y> <z>\"");
                              return 1;
                           })).then(((RequiredArgumentBuilder)this.e("y или z").executes(context4 -> {
                              this.a(
                                 this.a(context4, "название"), new class_243(this.b(context4, "x"), aM_.field_1724.method_23318(), this.b(context4, "y или z"))
                              );
                              return 1;
                           })).then(this.e("z").executes(context5 -> {
                              this.a(this.a(context5, "название"), new class_243(this.b(context5, "x"), this.b(context5, "y или z"), this.b(context5, "z")));
                              return 1;
                           })))))
                        ))
                        .then(this.a("me").executes(context6 -> {
                           this.a("me", aM_.field_1724.method_19538());
                           return 1;
                        })))
                     .then(((LiteralArgumentBuilder)this.a("remove").executes(context7 -> {
                        ChatUtil.a("Использование: .way remove <название>");
                        return 1;
                     })).then(this.d("название").suggests(this.a(() -> this.c, v0 -> v0.a())).executes(context8 -> {
                        String name = this.a(context8, "название");
                        if (!this.g(name)) {
                           ChatUtil.a("Метка с именем &c" + name + " &7отсутствует");
                           return 1;
                        } else {
                           this.c.removeIf(way -> way.a().equalsIgnoreCase(name));
                           ChatUtil.a("Метка с именем &c" + name + " &7успешно удалена");
                           return 1;
                        }
                     }))))
                  .then(this.a("list").executes(context9 -> {
                     if (this.c.isEmpty()) {
                        ChatUtil.a("Список меток не содержит элементов");
                        return 1;
                     } else {
                        ChatUtil.a("Список всех меток (" + this.c.size() + "):");

                        for (WayCommand.b way : this.c) {
                           ChatUtil.a("— &c" + way.a() + " &7[&f" + this.a(way.b()) + "&7]&f");
                        }

                        return 1;
                     }
                  })))
               .then(this.a("event").executes(context10 -> {
                  this.e = WayCommand.a.WAY;
                  aM_.field_1724.field_3944.method_45731("event delay");
                  return 1;
               })))
            .then(this.a("clear").executes(context11 -> {
               ChatUtil.a("Количество удалённых меток: " + this.c.size());
               this.c.clear();
               return 1;
            })))
         .executes(context12 -> {
            ChatUtil.a("Использование: .way <add|me|remove|list|clear|event>");
            return 1;
         });
   }

   @EventTarget
   public void a(PacketEvent event) {
      if (this.e != WayCommand.a.NONE && event.c()) {
         if (event.d() instanceof class_7439 class_7439VarD) {
            String message = class_7439VarD.comp_763().getString();
            if (message.contains("[Ивенты]")
               || message.contains("Аир-дроп:")
               || message.contains("|| /warp portal")
               || message.contains("|| Координаты:")
               || message.contains("Призван игроком:")
               || message.contains("Уровень лута:")
               || message.contains("Статус:")
               || message.contains("[1]")
               || message.contains("[2]")) {
               event.a(true);
               this.d.add(message);
            }
         }
      }
   }

   @EventTarget
   public void a(TickEvent event) {
      if (this.e != WayCommand.a.NONE && !this.d.isEmpty()) {
         boolean found = false;

         label51:
         for (int i = 0; i < this.d.size(); i++) {
            String message = this.d.get(i);
            if (message.contains("[1] Маяк убийца")
               || message.contains("[1] Вулкан")
               || message.contains("[1] Метеоритный дождь")
               || message.contains("[1] Гейзер")) {
               for (int j = i; j < this.d.size(); j++) {
                  String next = this.d.get(j);
                  if (next.contains("|| Координаты:")) {
                     Matcher matcher = Pattern.compile("\\[(-?\\d+) (-?\\d+) (-?\\d+)]").matcher(next);
                     if (matcher.find()) {
                        class_243 pos = new class_243(
                           Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3))
                        );
                        if (this.e == WayCommand.a.GPS) {
                           Westra.h().d().u().d().a(pos);
                        } else {
                           this.a("Ивент", pos);
                        }

                        found = true;
                     }
                     break label51;
                  }
               }
               break;
            }
         }

         if (!found) {
            ChatUtil.a("Нет активного события с координатами.");
         }

         this.d.clear();
         this.e = WayCommand.a.NONE;
      }
   }

   @EventTarget
   public void a(DrawEvent event) {
      if (event.b()) {
         for (WayCommand.b way : this.c) {
            this.a(event, way, aM_.field_1724.method_33571());
         }

         class_243 gps = Westra.h().d().u().d().c();
         if (gps != null) {
            this.a(event, gps, aM_.field_1724.method_33571());
         }
      }
   }

   public void a(String name, class_243 pos) {
      String trimmed = name.length() > 6 ? name.substring(0, 6) : name;
      boolean replaced = this.c.removeIf(way -> way.a().equalsIgnoreCase(trimmed) || way.b().method_1022(pos) <= 5.0);
      this.c.add(new WayCommand.b(trimmed, pos));
      ChatUtil.a("Метка &c" + trimmed + (replaced ? " &7успешно переставлена: " : " &7успешно добавлена: ") + this.a(pos));
   }

   private boolean g(String name) {
      return this.c.stream().anyMatch(way -> way.a().equalsIgnoreCase(name));
   }

   private String a(class_243 pos) {
      return (int)pos.method_10216() + ", " + (int)pos.method_10214() + ", " + (int)pos.method_10215();
   }

   private void a(DrawEvent event, WayCommand.b way, class_243 eyes) {
      Vector2f screen = ProjectUtil.a(way.b().field_1352, way.b().field_1351, way.b().field_1350);
      if (ProjectUtil.a(screen)) {
         int primary = Westra.h().d().o().a(ThemeInfo.PRIMARY).a();
         int background = Westra.h().d().o().a(ThemeInfo.BACKGROUND_HUD).a();
         class_2561 text = class_2561.method_43470(way.a().toUpperCase(Locale.ROOT))
            .method_10852(class_2561.method_43470("  /  ").method_10862(class_2583.field_24360.method_36139(primary)))
            .method_10852(class_2561.method_43470(String.format(Locale.US, "%.1fм", eyes.method_1022(way.b()))));
         float width = 14.0F + Fonts.e.a(text, 6.25F);
         float x = screen.x() - width / 2.0F;
         float y = screen.y() - 5.75F;
         event.d()
            .a(
               event.h(),
               x,
               y,
               width,
               11.5F,
               3.5F,
               ColorUtil.a(background, Westra.h().d().o().a(ThemeInfo.BACKGROUND_HUD).b()),
               1.0F,
               ColorUtil.a(background, Westra.h().d().o().a(ThemeInfo.BACKGROUND_HUD).b()),
               6.0F
            );
         Fonts.a.a(event.h(), "F", x + 3.0F, y + 3.0F, 5.5F, primary);
         Fonts.e.a(event.h(), text, x + 3.0F + 5.5F + 2.5F, y + (11.5F - Fonts.e.a(6.25F)) / 2.0F - 0.25F, 6.25F);
      }
   }

   private void a(DrawEvent event, class_243 gps, class_243 eyes) {
      class_4587 matrices = event.h();
      int primary = Westra.h().d().o().a(ThemeInfo.PRIMARY).a();
      double dx = gps.field_1352 - aM_.field_1724.method_23317();
      double dz = gps.field_1350 - aM_.field_1724.method_23321();
      float targetYaw = (float)Math.toDegrees(Math.atan2(-dx, dz));
      float angle = class_3532.method_15393(targetYaw - aM_.field_1724.method_36454());
      float cx = aM_.method_22683().method_4486() / 2.0F;
      float cy = aM_.method_22683().method_4502() * 0.25F;
      matrices.method_22903();
      matrices.method_46416(cx, cy, 0.0F);
      matrices.method_22907(class_7833.field_40718.rotationDegrees(angle));
      event.d().a(matrices, class_2960.method_60655("westra", "pictures/triangle.png"), -7.0F, -7.0F, 14.0F, 14.0F, 0.0F, primary);
      matrices.method_22909();
      class_2561 text = class_2561.method_43470(String.format(Locale.US, "%.1fм", eyes.method_1022(gps)));
      Fonts.d.a(matrices, text, cx - Fonts.d.a(text, 7.0F) / 2.0F, cy + 7.0F + 2.0F, 7.0F);
   }

   public static enum a {
      NONE,
      WAY,
      GPS;
   }

   public static class b {
      private final String a;
      private final class_243 b;

      @Generated
      public b(String name, class_243 position) {
         this.a = name;
         this.b = position;
      }

      @Generated
      public String a() {
         return this.a;
      }

      @Generated
      public class_243 b() {
         return this.b;
      }
   }
}
