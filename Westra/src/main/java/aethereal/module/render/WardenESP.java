package aethereal.module.render;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.DrawEvent;
import aethereal.render.ColorUtil;
import aethereal.render.Fonts;
import aethereal.setting.BooleanSetting;
import aethereal.setting.ModeSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import aethereal.util.CounterUtil;
import aethereal.util.ProjectUtil;
import aethereal.util.ServerUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Generated;
import net.minecraft.class_1531;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_2586;
import net.minecraft.class_2591;
import net.minecraft.class_2818;
import net.minecraft.class_2960;
import org.joml.Vector2f;

@ModuleRegister(
   a = "Warden ESP",
   b = "Отображает сундуки в городе варденов с таймером возрождения",
   c = Category.Render
)
public class WardenESP extends Module {
   private static final Pattern b = Pattern.compile("(\\d{2}):(\\d{2})");
   private final List<WardenESP.a> c = new ArrayList<>();
   private final ModeSetting o = new ModeSetting("Режим", "Варден", "Варден", "Медный данж");
   private final BooleanSetting n = new BooleanSetting("Ограничить зоной", true);
   private final SliderSetting p = new SliderSetting("Дистанция показа таймера", 64.0F, 8.0F, 256.0F, 4.0F);
   private final SliderSetting r = new SliderSetting("Зелёная подсветка от, сек", 60.0F, 5.0F, 600.0F, 5.0F);

   public WardenESP() {
      this.a(new Setting[]{this.o, this.n, this.p, this.r});
   }

   @EventTarget
   public void a(DrawEvent event) {
      if (this.o.l("Медный данж") || aM_.field_1687.method_27983().method_29177().toString().equals("minecraft:overworld")) {
         List<class_2338> chests = this.q();
         if (event.b()) {
            for (class_1531 class_1531Var : aM_.field_1687.method_8390(class_1531.class, aM_.field_1724.method_5829().method_1014(256.0), e -> true)) {
               if (class_1531Var instanceof class_1531) {
                  Matcher matcher = b.matcher(class_1531Var.method_5477().getString());
                  if (matcher.find()) {
                     int minutes = Integer.parseInt(matcher.group(1));
                     int seconds = Integer.parseInt(matcher.group(2));
                     long ms = (minutes * 60L + seconds) * 1000L;
                     class_2338 nearest = this.a(chests, class_1531Var.method_24515());
                     if (nearest != null) {
                        WardenESP.a existing = this.b(nearest);
                        if (existing != null) {
                           existing.a(ms);
                        } else {
                           this.c.add(new WardenESP.a(nearest, ms));
                        }
                     }
                  }
               }
            }

            this.c.removeIf(info -> info.a() <= 0L);
            this.a(event, chests);
         }

         if (event.c()) {
            this.b(event, chests);
            this.b(event);
         }
      }
   }

   private void a(DrawEvent event, List<class_2338> chests) {
      int background = ColorUtil.a(11, 11, 13, 179);
      class_2338 soonest = null;
      long soonestMs = Long.MAX_VALUE;

      for (class_2338 coord : chests) {
         WardenESP.a info = this.b(coord);
         if (info != null && info.a() > 0L && info.a() < soonestMs) {
            soonestMs = info.a();
            soonest = coord;
         }
      }

      float pulse = (float)(Math.sin(System.currentTimeMillis() / 250.0) * 0.5 + 0.5);
      int green = ColorUtil.a(70, 225, 130, 255);
      double maxDistance = this.p.c().doubleValue();
      long greenMs = (long)(this.r.c() * 1000.0F);

      for (class_2338 coordx : chests) {
         WardenESP.a info = this.b(coordx);
         if (info != null && (aM_.field_1724 == null || !(aM_.field_1724.method_19538().method_1022(coordx.method_46558()) > maxDistance))) {
            Vector2f screen = ProjectUtil.a(coordx.method_10263() + 0.5, coordx.method_10264() + 1, coordx.method_10260() + 0.5);
            if (ProjectUtil.a(screen)) {
               boolean soon = info.a() <= greenMs;
               boolean arrow = coordx.equals(soonest);
               int totalSec = (int)(info.a() / 1000L);
               String text = String.format(Locale.US, "%02d:%02d", totalSec / 60, totalSec % 60);
               float textWidth = Fonts.e.a(text, 6.5F);
               float width = 12.5F + textWidth + 3.5F;
               float x = screen.x() - width / 2.0F;
               float y = screen.y() - 6.0F;
               int bg = soon ? ColorUtil.a(background, green, 0.3F + 0.3F * pulse) : background;
               float glow = soon ? 4.0F + 2.0F * pulse : 3.0F;
               event.d().a(event.h(), x, y, width, 12.0F, 3.5F, bg, 1.0F, soon ? green : bg, glow);
               if (soon) {
                  event.d().a(event.h(), x, y, width, 12.0F, 3.5F, 0.5F, ColorUtil.a(green, 0.5F + 0.5F * pulse));
               }

               event.d().a(event.h(), class_2960.method_60655("westra", "pictures/minecraft/chest.png"), x + 3.0F, y + 2.5F, 7.0F, 7.0F, 0.0F, -1);
               int textColor = soon ? ColorUtil.a(ColorUtil.a(255, 255, 255, 255), green, 0.5F + 0.5F * pulse) : ColorUtil.a(255, 255, 255, 255);
               Fonts.e.a(event.h(), text, x + 3.0F + 7.0F + 2.5F, y + (12.0F - Fonts.e.a(6.5F)) / 2.0F - 0.5F, 6.5F, textColor);
               if (arrow) {
                  this.a(event, screen.x(), y - 3.0F - 2.0F * pulse, green, 0.7F + 0.3F * pulse);
               }
            }
         }
      }
   }

   private void a(DrawEvent event, float cx, float topY, int color, float alpha) {
      int c = ColorUtil.a(color, alpha);
      int rows = 5;

      for (int row = 0; row < rows; row++) {
         float w = 7.0F * (1.0F - (float)row / rows);
         float ry = topY + row * 1.1F;
         event.d().a(event.h(), cx - w / 2.0F, ry, w, 1.2F, 0.4F, c);
      }
   }

   private void b(DrawEvent event, List<class_2338> chests) {
      for (class_2338 coord : chests) {
         if (this.b(coord) == null) {
            event.e()
               .a(
                  event.h(),
                  new class_238(
                     coord.method_10263(),
                     coord.method_10264(),
                     coord.method_10260(),
                     coord.method_10263() + 1,
                     coord.method_10264() + 1,
                     coord.method_10260() + 1
                  ),
                  ColorUtil.a(255, 100, 100, 255),
                  1.0F
               );
         }
      }
   }

   private void b(DrawEvent event) {
      List<class_2338> pots = this.s();
      if (!pots.isEmpty()) {
         double maxDistance = this.p.c().doubleValue();
         int color = ColorUtil.a(235, 170, 90, 255);

         for (class_2338 pos : pots) {
            if (aM_.field_1724 == null || !(aM_.field_1724.method_19538().method_1022(pos.method_46558()) > maxDistance)) {
               event.e()
                  .a(
                     event.h(),
                     new class_238(
                        pos.method_10263(), pos.method_10264(), pos.method_10260(), pos.method_10263() + 1, pos.method_10264() + 1, pos.method_10260() + 1
                     ),
                     color,
                     1.0F
                  );
            }
         }
      }
   }

   private class_2338 a(List<class_2338> chests, class_2338 standPos) {
      for (class_2338 coord : chests) {
         if (standPos.method_10263() == coord.method_10263() && standPos.method_10260() == coord.method_10260()) {
            return coord;
         }
      }

      return null;
   }

   private WardenESP.a b(class_2338 pos) {
      int currentAnarchy = ServerUtil.a.d();

      for (WardenESP.a info : this.c) {
         if (info.c().equals(pos) && info.e() == currentAnarchy) {
            return info;
         }
      }

      return null;
   }

   public long a(class_2338 pos) {
      for (class_1531 class_1531Var : aM_.field_1687.method_8390(class_1531.class, aM_.field_1724.method_5829().method_1014(256.0), e -> true)) {
         if (class_1531Var instanceof class_1531
            && class_1531Var.method_24515().method_10263() == pos.method_10263()
            && class_1531Var.method_24515().method_10260() == pos.method_10260()) {
            Matcher matcher = b.matcher(class_1531Var.method_5477().getString());
            if (matcher.find()) {
               return (Integer.parseInt(matcher.group(1)) * 60L + Integer.parseInt(matcher.group(2))) * 1000L;
            }
         }
      }

      WardenESP.a info = this.b(pos);
      return info == null ? -1L : info.a();
   }

   public List<class_2338> q() {
      return this.collect(false);
   }

   public List<class_2338> s() {
      return (List<class_2338>)(this.o.l("Медный данж") ? this.collect(true) : new ArrayList<>());
   }

   private List<class_2338> collect(boolean pots) {
      boolean copper = this.o.l("Медный данж");
      List<class_2338> result = new ArrayList<>();
      if (aM_.field_1724 == null) {
         return result;
      } else {
         int radius = Math.max(2, ((int)this.p.c().floatValue() >> 4) + 1);
         int centerX = aM_.field_1724.method_24515().method_10263() >> 4;
         int centerZ = aM_.field_1724.method_24515().method_10260() >> 4;

         for (int cx = centerX - radius; cx <= centerX + radius; cx++) {
            for (int cz = centerZ - radius; cz <= centerZ + radius; cz++) {
               class_2818 chunk = aM_.field_1687.method_8497(cx, cz);
               if (chunk != null) {
                  for (Entry<class_2338, class_2586> entry : chunk.method_12214().entrySet()) {
                     class_2338 pos = entry.getKey();
                     if (!this.n.c() || a(pos, copper)) {
                        class_2591<?> type = entry.getValue().method_11017();
                        boolean match = pots
                           ? type == class_2591.field_42781
                           : (copper ? type == class_2591.field_16411 : type == class_2591.field_11914 || type == class_2591.field_11891);
                        if (match) {
                           result.add(pos);
                        }
                     }
                  }
               }
            }
         }

         return result;
      }
   }

   private static boolean a(class_2338 pos, boolean copper) {
      return copper
         ? pos.method_10263() >= 1900
            && pos.method_10263() <= 2050
            && pos.method_10264() >= 0
            && pos.method_10264() <= 100
            && pos.method_10260() >= 1970
            && pos.method_10260() <= 2050
         : pos.method_10264() >= -60
            && pos.method_10264() <= -35
            && pos.method_10263() >= -2070
            && pos.method_10263() <= -1921
            && pos.method_10260() >= -2076
            && pos.method_10260() <= -1929;
   }

   public static class a {
      private final class_2338 b;
      private long c;
      private final CounterUtil a = new CounterUtil();
      private int d = ServerUtil.a.d();

      @Generated
      public CounterUtil b() {
         return this.a;
      }

      @Generated
      public class_2338 c() {
         return this.b;
      }

      @Generated
      public long d() {
         return this.c;
      }

      @Generated
      public int e() {
         return this.d;
      }

      public a(class_2338 chestPos, long current) {
         this.b = chestPos;
         this.c = current;
         this.a.b();
      }

      public void a(long current) {
         if (Math.abs(current / 1000L - this.a() / 1000L) > 5L) {
            this.c = current;
            this.a.b();
            this.d = ServerUtil.a.d();
         }
      }

      public long a() {
         return Math.max(0L, this.c - this.a.c());
      }
   }
}
