package aethereal.ui.widget;

import aethereal.config.ThemeInfo;
import aethereal.config.ThemeProcessor;
import aethereal.core.GlobalEvent;
import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.event.DrawEvent;
import aethereal.module.misc.StreamerMode;
import aethereal.render.ColorUtil;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import aethereal.ui.element.DragInfo;
import aethereal.util.InventoryUtil;
import aethereal.util.ServerUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import net.minecraft.class_10130;
import net.minecraft.class_1068;
import net.minecraft.class_1304;
import net.minecraft.class_1542;
import net.minecraft.class_1657;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2960;
import net.minecraft.class_408;
import net.minecraft.class_742;
import net.minecraft.class_7923;
import net.minecraft.class_9334;

public class EnvironmentWidget extends Widget implements Interface {
   protected final BooleanSetting f = new BooleanSetting("Показывать броню", true);
   protected final Map<UUID, EnvironmentWidget.b> g = new HashMap<>();
   protected final List<UUID> h = new ArrayList<>();
   private final UUID[] i = new UUID[2];

   public EnvironmentWidget() {
      super(new DragInfo("Окружение", 0.0F, 0.0F, 0.0F, 0.0F));
      this.j().a(this);
      this.a(new Setting[]{this.f});
   }

   @Override
   public void a(DrawEvent event) {
      this.k(event);
      super.a(event);
   }

   protected void k(DrawEvent event) {
      this.d().a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
      float animation = this.a();
      float x = this.j().a();
      float y = this.j().b();
      Stream<UUID> stream = this.h.stream();
      Map<UUID, EnvironmentWidget.b> map = this.g;
      List<EnvironmentWidget.b> shown = stream.<EnvironmentWidget.b>map(v1 -> map.get(v1)).filter(v0 -> Objects.nonNull(v0)).toList();
      float width = 14.5F + Fonts.e.a("Окружение", this.e) + 5.0F;

      for (EnvironmentWidget.b data : shown) {
         width = Math.max(width, Math.max(20.0F + Fonts.e.a(data.d + (int)data.f + "HP", 6.5F) + 34.0F, data.b.size() * 15.0F - 2.0F));
      }

      this.j().c(width);
      if (animation > 0.0F) {
         this.a(event, "x", "Окружение", width, animation);
      }

      float contentY = y + this.d + 3.0F;
      Iterator<EnvironmentWidget.b> it = shown.iterator();

      while (it.hasNext()) {
         contentY += this.a(event, it.next(), x, contentY, width, animation);
      }

      this.j().d(Math.max(this.d, contentY - y - 2.0F));
   }

   protected float a(DrawEvent event, EnvironmentWidget.b data, float x, float y, float width, float animation) {
      float textY = y + (12.0F - Fonts.e.a(6.5F)) / 2.0F - 0.5F;
      String health = (int)data.f + "HP";
      this.a(event, x, y, width, 12.0F, 3.0F, animation, false);
      event.d()
         .a(
            event.h(),
            x + 2.0F,
            y + 2.0F,
            8.0F,
            8.0F,
            1.5F,
            ColorUtil.a(-1, animation),
            0.125F,
            0.125F,
            0.125F,
            0.125F,
            aM_.method_1531().method_4619(data.e).method_4624()
         );
      Fonts.e.a(event.h(), health, x + width - 3.0F - Fonts.e.a(health, 6.5F), textY, 6.5F, ColorUtil.a(-1, animation));
      float right = x + width - 4.0F - Fonts.e.a(health, 6.5F);
      if (this.f.c()) {
         for (int i = 3; i >= 0; i--) {
            if (!data.c[i].method_7960()) {
               right -= 7.0F;
               event.e().a(event.i(), InventoryUtil.a(data.c[i]), right - 2.0F, y + 2.0F, 0, animation, 0.5F, false);
            }
         }
      }

      Fonts.e.c(event.h(), data.d, x + 12.5F, textY, 6.5F, ColorUtil.a(-1, animation), right - 16.5F - x);
      if (data.b.isEmpty()) {
         return 14.0F;
      } else {
         long now = System.currentTimeMillis();
         List<EnvironmentWidget.a> history = new ArrayList<>(data.b.values());
         Collections.reverse(history);

         for (int i2 = 0; i2 < history.size(); i2++) {
            EnvironmentWidget.a entry = history.get(i2);
            float itemX = x + i2 * 15.0F;
            float itemY = y + 12.0F + 2.0F;
            int left = entry.a(now);
            this.a(event, itemX, itemY, 13.0F, 13.0F, 2.0F, animation, entry.c != 0L);
            event.e().a(event.i(), entry.a, itemX + 2.1F, itemY + 2.1F, 0, animation, 0.55F, entry.c == 0L);
            if (left > 0) {
               String text = left > 99 ? "99+" : String.valueOf(left);
               float textWidth = Fonts.e.a(text, 6.5F);
               float badge = textWidth + 6.0F;
               float badgeX = itemX + (13.0F - badge) / 2.0F;
               float badgeY = itemY - 4.0F;
               float textX = badgeX + (badge - textWidth - 0.3F) / 2.0F;
               float textY2 = badgeY + (8.0F - Fonts.e.a(6.5F)) / 2.0F + 1.0F;
               int color = ColorUtil.a(ColorUtil.a(255, 60, 60, 255), animation);
               this.a(event, badgeX + 1.5F, badgeY + 1.0F, badge - 3.0F, 8.0F, 2.0F, animation, false);
               Fonts.e.a(event.h(), text, textX, textY2, 6.5F, color);
               Fonts.e.a(event.h(), text, textX + 0.3F, textY2, 6.5F, color);
            }
         }

         return 29.0F;
      }
   }

   private void a(DrawEvent event, float x, float y, float width, float height, float radius, float animation, boolean empty) {
      ThemeProcessor theme = Westra.h().d().o();
      int background = ColorUtil.a(theme.a(ThemeInfo.BACKGROUND_HUD).a(), theme.a(ThemeInfo.PRIMARY).a(), theme.a(ThemeInfo.PRIMARY).b() / 6.0F);
      event.d()
         .b(
            event.h(),
            x,
            y,
            width,
            height,
            radius,
            ColorUtil.a(empty ? ColorUtil.a(background, ColorUtil.a(255, 60, 60, 255), 0.35F) : background, theme.a(ThemeInfo.BACKGROUND_HUD).b() * animation),
            animation
         );
   }

   @Override
   public void a(GlobalEvent event) {
      if (aM_.field_1687 != null && aM_.field_1724 != null) {
         class_1657 class_1657Var;
         if (Westra.h().d().t().B().s() instanceof class_1657 class_1657VarS) {
            class_1657Var = class_1657VarS;
         } else if (Westra.h().d().t().X().s() instanceof class_1657 class_1657VarS2) {
            class_1657Var = class_1657VarS2;
         } else {
            class_1657Var = null;
         }

         if (class_1657Var != null && !class_1657Var.method_5667().equals(this.i[0])) {
            this.i[1] = this.i[0];
            this.i[0] = class_1657Var.method_5667();
         }

         long now = System.currentTimeMillis();
         List<? extends class_1657> nearby = aM_.field_1687
            .method_18456()
            .stream()
            .filter(player -> player != aM_.field_1724 && player.method_5805() && !Westra.h().d().e().d(player.method_5477().getString()))
            .sorted(Comparator.<class_1657>comparingInt(player2 -> {
               if (player2.method_5667().equals(this.i[0])) {
                  return 0;
               } else {
                  return player2.method_5667().equals(this.i[1]) ? 1 : 2;
               }
            }).thenComparingInt(player3 -> -a(player3)).thenComparing(player4 -> player4.method_5477().getString(), (v0, v1) -> v0.compareToIgnoreCase(v1)))
            .limit(2L)
            .toList();
         this.h.clear();

         for (class_1657 player5 : nearby) {
            this.h.add(player5.method_5667());
            this.g.computeIfAbsent(player5.method_5667(), EnvironmentWidget.b::new).a(player5, now);
         }

         this.g.values().removeIf(data -> !this.h.contains(data.a) && now - data.g > 30000L);
      }

      this.d().a(aM_.field_1687 != null && (!this.h.isEmpty() || aM_.field_1755 instanceof class_408));
      super.a(event);
   }

   private static int a(class_1657 player) {
      int score = 0;

      for (int i2 = 0; i2 < 4; i2++) {
         String name = class_7923.field_41178.method_10221(player.method_6118(class_1304.values()[5 - i2]).method_7909()).method_12832();
         int i;
         if (name.startsWith("netherite_")) {
            i = 4;
         } else if (name.startsWith("diamond_")) {
            i = 3;
         } else if (name.startsWith("iron_")) {
            i = 2;
         } else {
            i = name.startsWith("leather_") ? 1 : 0;
         }

         score += i;
      }

      return score;
   }

   static int a(class_1799 stack) {
      if (stack.method_31574(class_1802.field_8551)
         || stack.method_31574(class_1802.field_22021)
         || stack.method_31574(class_1802.field_8543)
         || stack.method_31574(class_1802.field_8479)
         || stack.method_31574(class_1802.field_8614)
         || stack.method_31574(class_1802.field_49098)
         || stack.method_31574(class_1802.field_8449)) {
         return 3;
      } else if (stack.method_57826(class_9334.field_50075)) {
         return 2;
      } else {
         return !stack.method_31574(class_1802.field_8287) && !stack.method_31574(class_1802.field_8814) ? 0 : 1;
      }
   }

   static int b(class_1799 stack) {
      if (stack.method_31574(class_1802.field_8367)) {
         return 150;
      } else if (stack.method_31574(class_1802.field_8479) || stack.method_31574(class_1802.field_8449) || stack.method_31574(class_1802.field_8814)) {
         return 60;
      } else if (stack.method_31574(class_1802.field_8463)) {
         return 30;
      } else if (stack.method_31574(class_1802.field_8551)) {
         return 25;
      } else {
         return stack.method_31574(class_1802.field_22021) ? 15 : 0;
      }
   }

   static final class a {
      final class_1799 a;
      long b;
      long c;
      int d;

      a(class_1799 stack, int hand) {
         this.a = stack.method_7972();
         this.a.method_7974(0);
         this.a.method_57379(class_9334.field_53966, new class_10130(0.0F, Optional.of(class_2960.method_60655("westra", "widget"))));
         this.d = hand;
      }

      int a(long now) {
         int total = EnvironmentWidget.b(this.a);
         return total != 0 && this.b != 0L ? Math.max((int)Math.ceil((total * 1000L - (now - this.b)) / 1000.0), 0) : 0;
      }
   }

   static final class b {
      final UUID a;
      final LinkedHashMap<class_1792, EnvironmentWidget.a> b = new LinkedHashMap<>();
      final class_1799[] c = new class_1799[6];
      String d = "";
      class_2960 e;
      float f;
      long g;

      private b(UUID id) {
         this.a = id;
         Arrays.fill(this.c, class_1799.field_8037);
      }

      void a(class_1657 player, long now) {
         StreamerMode streamer = Westra.h().d().t().aE();
         this.d = streamer.m() && streamer.r().c() ? streamer.a(player.method_5477().getString()) : player.method_5477().getString();
         class_2960 class_2960VarComp_1626;
         if (player instanceof class_742 client) {
            class_2960VarComp_1626 = client.method_52814().comp_1626();
         } else {
            class_2960VarComp_1626 = class_1068.method_4648(this.a).comp_1626();
         }

         this.e = class_2960VarComp_1626;
         this.f = ServerUtil.a.a(player);
         this.g = now;

         for (int i = 0; i < 4; i++) {
            this.c[i] = player.method_6118(class_1304.values()[5 - i]);
         }

         class_1799 main = player.method_6047();
         class_1799 off = player.method_6079();

         for (EnvironmentWidget.a entry : this.b.values()) {
            if (entry.c == 0L && entry.a.method_7947() == 1 && this.c[entry.d].method_31574(entry.a.method_7909()) && (entry.d == 4 ? main : off).method_7960()
               )
             {
               entry.c = now;
            }
         }

         this.c[4] = main;
         this.c[5] = off;

         for (int hand = 4; hand < 6; hand++) {
            if (EnvironmentWidget.a(this.c[hand]) > 0) {
               this.a(player, this.c[hand], hand, now);
            }
         }

         this.b.values().removeIf(entry2 -> entry2.c != 0L && now - entry2.c > 10000L);
      }

      private void a(class_1657 player, class_1799 stack, int hand, long now) {
         EnvironmentWidget.a entry = this.b.remove(stack.method_7909());
         if (entry != null) {
            if (stack.method_7947() < entry.a.method_7947()
               && EnvironmentWidget.b(stack) > 0
               && Interface.aM_
                  .field_1687
                  .method_8390(class_1542.class, player.method_5829().method_1014(2.0), item -> item.method_6983().method_31574(stack.method_7909()))
                  .isEmpty()) {
               entry.b = now;
            }

            entry.a.method_7939(stack.method_7947());
            entry.d = hand;
            entry.c = 0L;
            this.b.put(stack.method_7909(), entry);
         } else {
            if (this.b.size() >= 9) {
               Optional<class_1792> map = this.b
                  .entrySet()
                  .stream()
                  .filter(candidate -> EnvironmentWidget.a(candidate.getValue().a) < EnvironmentWidget.a(stack))
                  .findFirst()
                  .map(v0 -> v0.getKey());
               LinkedHashMap<class_1792, EnvironmentWidget.a> linkedHashMap = this.b;
               map.ifPresent(linkedHashMap::remove);
               if (this.b.size() >= 9) {
                  return;
               }
            }

            this.b.put(stack.method_7909(), new EnvironmentWidget.a(stack, hand));
         }
      }
   }
}
