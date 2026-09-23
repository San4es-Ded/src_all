package aethereal.module.render;

import aethereal.config.ThemeInfo;
import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.DrawEvent;
import aethereal.render.AnimationUtil;
import aethereal.render.ColorUtil;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.setting.BooleanSetting;
import aethereal.setting.MultiModeSetting;
import aethereal.setting.Setting;
import aethereal.util.ProjectUtil;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1667;
import net.minecraft.class_1682;
import net.minecraft.class_1684;
import net.minecraft.class_1685;
import net.minecraft.class_1686;
import net.minecraft.class_1753;
import net.minecraft.class_1764;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1835;
import net.minecraft.class_1893;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_3486;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_4587;
import net.minecraft.class_7924;
import net.minecraft.class_9304;
import net.minecraft.class_9334;
import net.minecraft.class_2350.class_2351;
import net.minecraft.class_239.class_240;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;
import org.joml.Vector2f;
import platform.inject.accessors.TridentEntityAccessor;

@ModuleRegister(
   a = "Predictions",
   b = "Прогнозирует и отображает траекторию полёта трезубца, стрел и зелий",
   c = Category.Render
)
public class Predictions extends Module {
   private final MultiModeSetting b = new MultiModeSetting(
      "Отслеживаемые предметы",
      new BooleanSetting("Стрелы", true),
      new BooleanSetting("Трезубцы", true),
      new BooleanSetting("Эндер жемчуг", true),
      new BooleanSetting("Зелья", true)
   );
   private final BooleanSetting c = new BooleanSetting("Радужный цвет", false);
   private final Map<Integer, Predictions.b> d = new HashMap<>();

   public Predictions() {
      this.a(new Setting[]{this.b, this.c});
   }

   private boolean a(class_1297 entity) {
      return entity.method_23317() != entity.field_6014 || entity.method_23318() != entity.field_6036 || entity.method_23321() != entity.field_5969;
   }

   @EventTarget
   public void a(DrawEvent event) {
      if (aM_.field_1724 != null && aM_.field_1687 != null) {
         if (event.c()) {
            Set<Integer> activeIds = new HashSet<>();
            class_238 range = aM_.field_1724.method_5829().method_1014((Integer)aM_.field_1690.method_42503().method_41753() * 16);
            if (this.b.a("Стрелы").c()) {
               aM_.field_1687
                  .method_8390(class_1667.class, range, v1 -> this.a(v1))
                  .forEach(e -> this.a(event, e, class_1802.field_8107.method_7854(), activeIds));
            }

            if (this.b.a("Трезубцы").c()) {
               aM_.field_1687
                  .method_8390(class_1685.class, range, e2 -> ((TridentEntityAccessor)e2).getReturnTimer() <= 0 && this.a(e2))
                  .forEach(e3 -> this.a(event, e3, class_1802.field_8547.method_7854(), activeIds));
            }

            if (this.b.a("Эндер жемчуг").c()) {
               aM_.field_1687
                  .method_8390(class_1684.class, range, v1 -> this.a(v1))
                  .forEach(e4 -> this.a(event, e4, class_1802.field_8634.method_7854(), activeIds));
            }

            if (this.b.a("Зелья").c()) {
               aM_.field_1687.method_8390(class_1686.class, range, v1 -> this.a(v1)).forEach(e5 -> this.a(event, e5, e5.method_7495(), activeIds));
            }

            this.b(event);
            this.d.entrySet().removeIf(entry -> {
               if (activeIds.contains(entry.getKey())) {
                  return false;
               } else {
                  AnimationUtil anim = entry.getValue().d();
                  anim.a(false);
                  anim.a(0.0F, 1.0F, 0.25F, EasingList.s, event.g());
                  return anim.c() <= 0.0F;
               }
            });
         }

         if (event.b()) {
            this.d.values().forEach(info -> this.a(event, info));
         }
      }
   }

   private void a(DrawEvent event, class_1297 entity, class_1799 item, Set<Integer> activeIds) {
      List<class_243> path = this.b(entity);
      if (path.size() >= 2) {
         activeIds.add(entity.method_5628());
         Predictions.b existing = this.d.get(entity.method_5628());
         AnimationUtil anim = existing != null ? existing.d() : new AnimationUtil();
         anim.a(true);
         anim.a(0.0F, 1.0F, 0.25F, EasingList.s, event.g());
         float alpha = anim.c();
         int primaryColor = Westra.h().d().o().a(ThemeInfo.PRIMARY).a();
         float hueBase = (float)(entity.method_5667().getLeastSignificantBits() & 65535L) / 65535.0F;
         int segCount = path.size() - 1;

         for (int i = 0; i < segCount; i++) {
            float max = Math.max(0.0F, Math.min(1.0F, alpha * segCount - i));
            if (max <= 0.0F) {
               break;
            }

            float hue = ((hueBase + (float)(path.get(i).field_1352 * 0.05000000070627959 + path.get(i).field_1350 * 0.05000000070627959)) % 1.0F + 1.0F) % 1.0F;
            int base = this.c.c() ? 0xFF000000 | Color.HSBtoRGB(hue, 1.0F, 1.0F) & 16777215 : primaryColor;
            int lineAlpha = (int)(255.0F * (0.3F + 0.7F * (1.0F - i / segCount)) * max * alpha);
            event.e().a(event.h(), path.get(i), path.get(i + 1), null, base & 16777215 | lineAlpha << 24, 1.5F);
         }

         this.d.put(entity.method_5628(), new Predictions.b(path.getLast(), segCount, item, anim));
      }
   }

   private void a(DrawEvent event, Predictions.b info) {
      float alpha = info.d().c();
      if (!(alpha <= 0.0F)) {
         Vector2f screen = ProjectUtil.a(info.a().field_1352, info.a().field_1351, info.a().field_1350);
         if (ProjectUtil.a(screen)) {
            float iconSize = Fonts.e.d().lineHeight() * 7.25F;
            String format = String.format(Locale.US, "%.1fs", info.b() / 20.0F);
            float width = 6.0F + iconSize + Fonts.e.a(format, 7.25F);
            float height = iconSize + 4.0F;
            float x = screen.x() - width / 2.0F;
            float y = screen.y() - height / 2.0F;
            class_4587 matrices = event.i().method_51448();
            matrices.method_22903();
            matrices.method_46416(screen.x(), screen.y(), 0.0F);
            matrices.method_22905(0.8F + alpha * 0.2F, 0.8F + alpha * 0.2F, 1.0F);
            matrices.method_46416(-screen.x(), -screen.y(), 0.0F);
            event.d().a(matrices, x, y, width + 1.0F, height, 2.0F, ColorUtil.a(0, 0, 0, (int)(130.0F * alpha)));
            event.e().a(event.i(), info.c(), x + 2.0F, y + 2.0F - 0.25F, 0, alpha, iconSize / 16.0F, false);
            Fonts.e.a(matrices, format, x + 4.0F + iconSize, y + 2.0F, 7.25F, ColorUtil.a(-1, alpha), 0.0F);
            matrices.method_22909();
         }
      }
   }

   private List<class_243> b(class_1297 entity) {
      class_243 vel = entity.method_18798();
      class_243 pos = entity.method_19538();
      boolean isThrowable = entity instanceof class_1682;
      double gravity = entity instanceof class_1686 ? 0.05000000070627959 : 0.030000000582077163;
      List<class_243> path = new ArrayList<>();
      path.add(pos);

      for (int i = 0;
         i < 140
            && vel.method_1027() >= 1.0000000000139336E-6
            && pos.method_10214() >= aM_.field_1687.method_31607()
            && pos.method_10214() <= aM_.field_1687.method_31607() + aM_.field_1687.method_31605();
         i++
      ) {
         double drag = aM_.field_1687.method_8316(class_2338.method_49638(pos)).method_15767(class_3486.field_15517)
            ? (isThrowable ? 0.7999999144424994 : 0.6000000001891753)
            : 0.990000120151185;
         if (isThrowable) {
            vel = new class_243(vel.field_1352 * drag, (vel.field_1351 - gravity) * drag, vel.field_1350 * drag);
         }

         class_243 next = pos.method_1019(vel);
         class_3965 impact = aM_.field_1687.method_17742(new class_3959(pos, next, class_3960.field_17558, class_242.field_1348, entity));
         if (impact.method_17783() != class_240.field_1333) {
            path.add(impact.method_17784());
            return path;
         }

         pos = next;
         if (!isThrowable) {
            vel = new class_243(vel.field_1352 * drag, vel.field_1351 * drag - 0.05000000070627959, vel.field_1350 * drag);
         }

         path.add(next);
      }

      return path;
   }

   private void b(DrawEvent event) {
      class_1799 mainStack = aM_.field_1724.method_5998(class_1268.field_5808);
      class_1792 main = mainStack.method_7909();
      class_1792 off = aM_.field_1724.method_5998(class_1268.field_5810).method_7909();
      float speed = 0.0F;
      boolean isThrowable = false;
      boolean potion = main == class_1802.field_8436 || main == class_1802.field_8150 || off == class_1802.field_8436 || off == class_1802.field_8150;
      if (main instanceof class_1753 && this.b.a("Стрелы").c()) {
         float pull = this.q();
         if (pull < 0.1F) {
            return;
         }

         speed = pull * 3.0F;
      } else if (main instanceof class_1764 && this.b.a("Стрелы").c()) {
         speed = 3.0F;
      } else if (main instanceof class_1835 && this.b.a("Трезубцы").c()) {
         speed = 2.5F;
      } else if ((main == class_1802.field_8634 || off == class_1802.field_8634) && this.b.a("Эндер жемчуг").c()) {
         speed = 1.5F;
         isThrowable = true;
      } else if (potion && this.b.a("Зелья").c()) {
         speed = 0.5F;
         isThrowable = true;
      }

      if (speed != 0.0F) {
         float[] viewSpread = main instanceof class_1764 && this.a(mainStack) ? new float[]{-10.0F, 0.0F, 10.0F} : new float[]{0.0F};

         for (float viewSpreadDegrees : viewSpread) {
            Predictions.a result = this.a(
               speed, isThrowable, potion ? -20.0F : 0.0F, potion ? 0.05000000070627959 : 0.030000000582077163, viewSpreadDegrees, event.g()
            );
            if (result.a().size() >= 2) {
               if (result.c() != null) {
                  event.e().a(event.h(), result.c().method_5829(), ColorUtil.a(255, 100, 100, 200), 1.0F);
               } else if (result.b() != null && result.d() != null) {
                  this.a(event, result.a().getLast(), 0.33F, ColorUtil.a(255, 255, 255, 200), result.d());
               }
            }
         }
      }
   }

   private void a(DrawEvent event, class_243 center, double radius, int color, class_2350 face) {
      class_2351 axis = face.method_10166();
      class_243 u = axis == class_2351.field_11052 ? new class_243(1.0, 0.0, 0.0) : new class_243(0.0, 1.0, 0.0);
      class_243 class_243Var = axis != class_2351.field_11048 && axis == class_2351.field_11051 ? new class_243(1.0, 0.0, 0.0) : new class_243(0.0, 0.0, 1.0);
      class_243 v = class_243Var;
      double step = 0.7853983338895167;
      double controlRadius = radius / Math.cos(step / 2.0);

      for (int i = 0; i < 8; i++) {
         double a1 = step * i;
         double a2 = step * (i + 1);
         double am = a1 + step / 2.0;
         event.e().a(event.h(), this.a(center, u, v, radius, a1), this.a(center, u, v, radius, a2), this.a(center, u, v, controlRadius, am), color, 1.5F);
      }
   }

   private class_243 a(class_243 center, class_243 u, class_243 v, double radius, double angle) {
      return center.method_1019(u.method_1021(Math.cos(angle) * radius)).method_1019(v.method_1021(Math.sin(angle) * radius));
   }

   private boolean a(class_1799 stack) {
      return ((class_9304)stack.method_57825(class_9334.field_49633, class_9304.field_49385))
            .method_57536(aM_.field_1687.method_30349().method_30530(class_7924.field_41265).method_46747(class_1893.field_9108))
         > 0;
   }

   private float q() {
      class_1799 active = aM_.field_1724.method_6030();
      if (aM_.field_1724.method_6115() && active.method_7909() instanceof class_1753) {
         int useTicks = active.method_7909().method_7881(active, aM_.field_1724) - aM_.field_1724.method_6014();
         float f = useTicks / 20.0F;
         return Math.min((f * f + f * 2.0F) / 3.0F, 1.0F);
      } else {
         return 0.0F;
      }
   }

   private Predictions.a a(float speed, boolean isThrowable, float pitchOffset, double gravity, float viewSpreadDegrees, float tickDelta) {
      double pitchRad = Math.toRadians(aM_.field_1724.method_5695(tickDelta));
      double yawRad = Math.toRadians(aM_.field_1724.method_5705(tickDelta));
      class_243 look = new class_243(
            -Math.sin(yawRad) * Math.cos(pitchRad),
            -Math.sin(Math.toRadians(aM_.field_1724.method_5695(tickDelta) + pitchOffset)),
            Math.cos(yawRad) * Math.cos(pitchRad)
         )
         .method_1029();
      if (viewSpreadDegrees != 0.0F) {
         class_243 right = new class_243(0.0, 1.0, 0.0).method_1036(look);
         class_243 axis = look.method_1036(
               right.method_1027() < 9.999996190428959E-11 ? new class_243(Math.cos(yawRad), 0.0, Math.sin(yawRad)) : right.method_1029()
            )
            .method_1029();
         double rad = Math.toRadians(viewSpreadDegrees);
         double c = Math.cos(rad);
         double s = Math.sin(rad);
         look = look.method_1021(c).method_1019(axis.method_1036(look).method_1021(s)).method_1019(axis.method_1021(axis.method_1026(look) * (1.0 - c)));
      }

      class_243 vel = look.method_1021(speed)
         .method_1031(
            aM_.field_1724.method_18798().field_1352,
            aM_.field_1724.method_24828() ? 0.0 : aM_.field_1724.method_18798().field_1351,
            aM_.field_1724.method_18798().field_1350
         );
      class_243 pos = aM_.field_1724.method_5836(tickDelta);
      List<class_243> path = new ArrayList<>();
      path.add(pos);

      for (int i = 0;
         i < 130
            && vel.method_1027() >= 1.0000000000139336E-6
            && pos.method_10214() >= aM_.field_1687.method_31607()
            && pos.method_10214() <= aM_.field_1687.method_31607() + aM_.field_1687.method_31605();
         i++
      ) {
         double drag = aM_.field_1687.method_8316(class_2338.method_49638(pos)).method_15767(class_3486.field_15517)
            ? (isThrowable ? 0.7999999144424994 : 0.6000000001891753)
            : 0.990000120151185;
         if (isThrowable) {
            vel = new class_243(vel.field_1352 * drag, (vel.field_1351 - gravity) * drag, vel.field_1350 * drag);
         }

         class_243 next = pos.method_1019(vel);
         class_3965 hitBlock = aM_.field_1687.method_17742(new class_3959(pos, next, class_3960.field_17558, class_242.field_1348, aM_.field_1724));
         class_243 end = hitBlock.method_17783() != class_240.field_1333 ? hitBlock.method_17784() : next;
         class_1297 hitEntity = this.a(pos, end);
         if (hitEntity != null) {
            path.add(hitEntity.method_5829().method_1014(0.30000001176381136).method_992(pos, end).orElse(end));
            return new Predictions.a(path, null, hitEntity, null);
         }

         if (hitBlock.method_17783() != class_240.field_1333) {
            path.add(hitBlock.method_17784());
            return new Predictions.a(path, hitBlock.method_17777(), null, hitBlock.method_17780());
         }

         pos = next;
         if (!isThrowable) {
            vel = new class_243(vel.field_1352 * drag, vel.field_1351 * drag - 0.05000000070627959, vel.field_1350 * drag);
         }

         path.add(next);
      }

      return new Predictions.a(path, null, null, null);
   }

   private class_1297 a(class_243 start, class_243 end) {
      class_1297 closest = null;
      double closestDist = 1.7976922776554332E308;

      for (class_1297 candidate : aM_.field_1687.method_8335(aM_.field_1724, new class_238(start, end).method_1014(1.0))) {
         if (candidate.method_5805() && !candidate.method_7325() && candidate instanceof class_1309) {
            Optional<class_243> hit = candidate.method_5829().method_1014(0.30000001176381136).method_992(start, end);
            if (hit.isPresent()) {
               double dist = start.method_1025(hit.get());
               if (dist < closestDist) {
                  closestDist = dist;
                  closest = candidate;
               }
            }
         }
      }

      return closest;
   }

   static final class a {
      private final List<class_243> a;
      private final class_2338 b;
      private final class_1297 c;
      private final class_2350 d;

      a(List<class_243> path, class_2338 hitBlock, class_1297 hitEntity, class_2350 hitSide) {
         this.a = path;
         this.b = hitBlock;
         this.c = hitEntity;
         this.d = hitSide;
      }

      public List<class_243> a() {
         return this.a;
      }

      public class_2338 b() {
         return this.b;
      }

      public class_1297 c() {
         return this.c;
      }

      public class_2350 d() {
         return this.d;
      }
   }

   static final class b {
      private final class_243 a;
      private final int b;
      private final class_1799 c;
      private final AnimationUtil d;

      b(class_243 impact, int ticks, class_1799 item, AnimationUtil anim) {
         this.a = impact;
         this.b = ticks;
         this.c = item;
         this.d = anim;
      }

      public class_243 a() {
         return this.a;
      }

      public int b() {
         return this.b;
      }

      public class_1799 c() {
         return this.c;
      }

      public AnimationUtil d() {
         return this.d;
      }
   }
}
