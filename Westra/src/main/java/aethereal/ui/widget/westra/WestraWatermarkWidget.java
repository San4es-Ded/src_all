package aethereal.ui.widget.westra;

import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.event.DrawEvent;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import aethereal.util.MathUtil;
import aethereal.util.ServerUtil;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import platform.inject.accessors.BossBarHudAccessor;

public class WestraWatermarkWidget extends WestraWidget implements Interface {
   private static final DateTimeFormatter E = DateTimeFormatter.ofPattern("HH:mm");
   private static final float R = 2.5F;
   private final BooleanSetting Q = new BooleanSetting("Держать по центру экрана", true);
   private final BooleanSetting F = new BooleanSetting("Словесная марка", true);
   private final BooleanSetting G = new BooleanSetting("Логин в клиенте", true);
   private final BooleanSetting H = new BooleanSetting("Частота кадров", true);
   private final BooleanSetting I = new BooleanSetting("Задержка игрока", true);
   private final BooleanSetting J = new BooleanSetting("Задержка сервера", false);
   private final BooleanSetting K = new BooleanSetting("Скорость игрока", false);
   private final BooleanSetting L = new BooleanSetting("Координаты", false);
   private final BooleanSetting M = new BooleanSetting("Текущее время", true);
   private float N;

   public WestraWatermarkWidget() {
      super("Инфо-панель");
      this.j().a(2);
      this.a(new Setting[]{this.Q, this.F, this.G, this.H, this.I, this.J, this.K, this.L, this.M});
   }

   @Override
   public void a(DrawEvent event) {
      this.d().a(true);
      this.d().a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
      this.N = MathUtil.c(this.N, aM_.method_47599(), 0.1F);
      List<String[]> pairs = this.u();
      boolean mark = this.F.c();
      float height = 16.0F;
      float markSize = 6.75F;
      float target = 19.0F;
      if (mark) {
         target += WestraStyle.c(markSize) + 2.0F;
      }

      for (String[] pair : pairs) {
         target += 9.0F + s(pair[0], pair[1]);
      }

      target += 7.0F;
      float width = MathUtil.c(this.j().f(), target, 0.3F);
      boolean centered = this.Q.c();
      float x = centered ? (aM_.method_22683().method_4486() - width) / 2.0F : this.j().a();
      float y = centered ? this.j().b() + this.w() : this.j().b();
      this.j().a(x);
      this.j().c(width);
      this.j().d(height);
      float animation = this.a();
      if (animation <= 0.0F) {
         super.a(event);
      } else {
         WestraStyle.d(event, x, y, width, height, animation);
         WestraStyle.g(event, x + 2.5F, y + 2.5F, 11.0F, animation);
         float cursor = x + 3.0F + 11.0F + 5.0F;
         float center = y + height / 2.0F;
         if (mark) {
            WestraStyle.c(event, cursor, Fonts.d.a("W", markSize, center), markSize, animation);
            cursor += WestraStyle.c(markSize) + 2.0F;
         }

         for (int index = 0; index < pairs.size(); index++) {
            if (index > 0 || mark) {
               this.t(event, cursor + 4.0F, y, height, animation);
            }

            cursor += 9.0F;
            cursor += this.r(event, cursor, y, height, pairs.get(index)[0], pairs.get(index)[1], -1, animation);
         }

         super.a(event);
      }
   }

   private float w() {
      Map<?, ?> bars = ((BossBarHudAccessor)aM_.field_1705.method_1740()).getBossBars();
      if (bars.isEmpty()) {
         return 0.0F;
      } else {
         int extra = bars.size() - 1;
         float pixels = 12 + extra * 19 + 6;
         return pixels
            * aM_.method_22683().method_4476((Integer)aM_.field_1690.method_42474().method_41753(), aM_.method_1573())
            / aM_.method_22683().method_4476(2, aM_.method_1573());
      }
   }

   private List<String[]> u() {
      List<String[]> pairs = new ArrayList<>();
      if (this.G.c()) {
         pairs.add(new String[]{"", Westra.h().g().b()});
      }

      if (this.H.c()) {
         pairs.add(new String[]{"fps", String.valueOf(Math.round(this.N))});
      }

      if (this.I.c()) {
         pairs.add(new String[]{"ping", ServerUtil.d() + "ms"});
      }

      if (this.J.c()) {
         pairs.add(new String[]{"tps", String.format("%.1f", Westra.h().d().v().j().a())});
      }

      if (this.K.c()) {
         pairs.add(new String[]{"bps", String.format("%.2f", ServerUtil.c())});
      }

      if (this.L.c() && aM_.field_1724 != null) {
         pairs.add(
            new String[]{"xyz", (int)aM_.field_1724.method_23317() + " " + (int)aM_.field_1724.method_23318() + " " + (int)aM_.field_1724.method_23321()}
         );
      }

      if (this.M.c()) {
         pairs.add(new String[]{"", LocalTime.now().format(E)});
      }

      return pairs;
   }
}
