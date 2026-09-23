package aethereal.module.player;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.BlockChangeEvent;
import aethereal.event.DrawEvent;
import aethereal.event.TickEvent;
import aethereal.render.AnimationUtil;
import aethereal.render.ColorUtil;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.util.CounterUtil;
import aethereal.util.ProjectUtil;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import lombok.Generated;
import net.minecraft.class_1792;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2561;
import net.minecraft.class_3341;
import net.minecraft.class_345;
import net.minecraft.class_5250;
import net.minecraft.class_1259.class_1260;
import net.minecraft.class_1259.class_1261;
import org.joml.Vector2f;
import platform.inject.accessors.BossBarHudAccessor;

@ModuleRegister(
   a = "Structures",
   b = "Отображает время до исчезновения структур трапки и пласта",
   c = Category.Player
)
public class Structures extends Module {
   private final List<class_2248> allowedBlocks = Arrays.asList(
      class_2246.field_10153,
      class_2246.field_10614,
      class_2246.field_10480,
      class_2246.field_10505,
      class_2246.field_10462,
      class_2246.field_10266,
      class_2246.field_23880,
      class_2246.field_10006,
      class_2246.field_10295,
      class_2246.field_10541,
      class_2246.field_23152,
      class_2246.field_22108,
      class_2246.field_10087,
      class_2246.field_10458,
      class_2246.field_23151
   );
   private final List<Structures.a> c = new ObjectArrayList();
   private final Set<class_2338> d = new ObjectOpenHashSet();
   private int e;

   @EventTarget
   public void a(BlockChangeEvent event) {
      if (this.allowedBlocks.contains(event.d().method_26204()) && !this.allowedBlocks.contains(event.c().method_26204())) {
         this.d.add(event.b());
         this.e = 2;
      }
   }

   @EventTarget
   public void a(TickEvent event) {
      this.c.removeIf(structure -> {
         if (structure.c().a(structure.d().d())) {
            structure.a();
            return true;
         } else {
            return false;
         }
      });
      this.c.forEach(structure2 -> {
         structure2.b().a(!structure2.c().a(structure2.d().d() - 200L));
         float remaining = Math.max(0.0F, (float)(structure2.d().d() - structure2.c().c()) / 1000.0F);
         if (structure2.e().method_14662(aM_.field_1724.method_24515())) {
            structure2.a(remaining / ((float)structure2.d().d() / 1000.0F), remaining);
         } else {
            structure2.a();
         }
      });
      if (this.e > 0) {
         int i = this.e - 1;
         this.e = i;
         if (i == 0) {
            class_3341 box = (class_3341)class_3341.method_35411(this.d).orElse(null);
            if (box != null) {
               for (Structures.b type : Structures.b.values()) {
                  if (type.a(box, this.d)) {
                     this.c.add(new Structures.a(type, box));
                     break;
                  }
               }
            }

            this.d.clear();
         }
      }
   }

   @EventTarget
   public void a(DrawEvent event) {
      if (event.b()) {
         for (Structures.a structure : this.c) {
            String text = String.format(Locale.US, "%.1f", Math.max(0.0F, (float)(structure.d().d() - structure.c().c()) / 1000.0F));
            structure.b().a(0.0F, 1.0F, 0.35F, EasingList.i, event.g());
            class_2338 center = structure.e().method_22874();
            Vector2f project = ProjectUtil.a(center.method_10263() + 0.5, center.method_10264() + 0.5, center.method_10260() + 0.5);
            float textWidth = Fonts.d.a(class_2561.method_43470(text), 7.5F);
            float textHeight = Fonts.d.d().lineHeight() * 7.5F;
            float totalTextWidth = textWidth + 4.0F;
            float totalWidth = 14.0F + totalTextWidth;
            float iconBoxX = project.x() - totalWidth / 2.0F;
            float iconBoxY = project.y() - 6.0F;
            int rectAlpha = (int)(120.0F * structure.b().c());
            event.d().a(event.i().method_51448(), iconBoxX, iconBoxY, 12.0F, 12.0F, 0.0F, ColorUtil.a(0, 0, 0, rectAlpha));
            event.e().a(event.i(), structure.d().e().method_7854(), iconBoxX + 1.5F, iconBoxY + 1.5F, 0, structure.b().c(), 0.55F, false);
            float textBoxX = iconBoxX + 12.0F + 2.0F;
            event.d().a(event.i().method_51448(), textBoxX, iconBoxY, totalTextWidth, 12.0F, 0.0F, ColorUtil.a(0, 0, 0, rectAlpha));
            Fonts.d
               .a(
                  event.i().method_51448(),
                  text,
                  textBoxX + (totalTextWidth - textWidth) / 2.0F,
                  iconBoxY + (12.0F - textHeight) / 2.0F + 0.5F,
                  7.5F,
                  ColorUtil.a(-1, structure.b().c())
               );
         }
      }
   }

   @Override
   public void c() {
      this.c.forEach(v0 -> v0.a());
      this.d.clear();
      this.c.clear();
      this.e = 0;
      super.c();
   }

   static class a implements Interface {
      private final AnimationUtil b = new AnimationUtil();
      private final CounterUtil c = new CounterUtil();
      private final Structures.b d;
      private final class_3341 e;
      private class_345 f;

      @Generated
      public AnimationUtil b() {
         return this.b;
      }

      @Generated
      public CounterUtil c() {
         return this.c;
      }

      @Generated
      public Structures.b d() {
         return this.d;
      }

      @Generated
      public class_3341 e() {
         return this.e;
      }

      @Generated
      public class_345 f() {
         return this.f;
      }

      public a(Structures.b type, class_3341 box) {
         this.d = type;
         this.e = box;
         this.c.b();
      }

      public void a(float progress, float remaining) {
         class_5250 class_5250VarMethod_43470 = class_2561.method_43470(String.format(Locale.US, "%s — %.1f сек", this.d.a(), remaining));
         if (this.f == null) {
            this.f = new class_345(UUID.randomUUID(), class_5250VarMethod_43470, progress, class_1260.field_5786, class_1261.field_5795, false, false, false);
            ((BossBarHudAccessor)aM_.field_1705.method_1740()).getBossBars().put(this.f.method_5407(), this.f);
         } else {
            this.f.method_5413(class_5250VarMethod_43470);
            this.f.method_5408(progress);
         }
      }

      public void a() {
         if (this.f != null) {
            ((BossBarHudAccessor)aM_.field_1705.method_1740()).getBossBars().remove(this.f.method_5407());
            this.f = null;
         }
      }
   }

   public static enum b {
      DRAGON_TRAPKA("Драконья трапка", new int[][]{{7, 7, 7}, {7, 7, 6}}, true, 30000L, class_1802.field_22021),
      TRAPKA("Трапка", new int[][]{{5, 5, 5}, {5, 5, 4}, {5, 6, 5}, {5, 6, 4}}, true, 15000L, class_1802.field_22021),
      DRAGON_PLAST("Драконий пласт", new int[][]{{7, 7, 2}, {7, 7, 1}}, true, 20000L, class_1802.field_8551),
      PLAST("Пласт", new int[][]{{5, 5, 2}, {5, 5, 1}}, true, 20000L, class_1802.field_8551),
      GARMOSHKA("Пласт", new int[][]{{5, 5, 5}, {5, 6, 5}}, false, 20000L, class_1802.field_8551);

      private final String f;
      private final int[][] g;
      private final boolean h;
      private final long i;
      private final class_1792 j;

      @Generated
      private b(final String displayName, final int[][] dimensions, final boolean hollow, final long cooldown, final class_1792 item) {
         this.f = displayName;
         this.g = dimensions;
         this.h = hollow;
         this.i = cooldown;
         this.j = item;
      }

      @Generated
      public String a() {
         return this.f;
      }

      @Generated
      public int[][] b() {
         return this.g;
      }

      @Generated
      public boolean c() {
         return this.h;
      }

      @Generated
      public long d() {
         return this.i;
      }

      @Generated
      public class_1792 e() {
         return this.j;
      }

      public boolean a(class_3341 box, Set<class_2338> positions) {
         int[] size = new int[]{box.method_35414(), box.method_14660(), box.method_14663()};
         Arrays.sort(size);

         for (int[] dimension : this.g) {
            int[] sorted = (int[])dimension.clone();
            Arrays.sort(sorted);
            if (Arrays.equals(size, sorted)) {
               return this.h
                  == positions.stream()
                     .noneMatch(
                        pos -> pos.method_10263() > box.method_35415()
                           && pos.method_10263() < box.method_35418()
                           && pos.method_10264() > box.method_35416()
                           && pos.method_10264() < box.method_35419()
                           && pos.method_10260() > box.method_35417()
                           && pos.method_10260() < box.method_35420()
                     );
            }
         }

         return false;
      }
   }
}
