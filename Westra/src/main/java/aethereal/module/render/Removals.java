package aethereal.module.render;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.RemovalsEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.MultiModeSetting;
import aethereal.setting.Setting;
import lombok.Generated;

@ModuleRegister(
   a = "Removals",
   b = "Убирает выбранные визуальные эффекты и элементы игры",
   c = Category.Render
)
public class Removals extends Module {
   private final MultiModeSetting b = new MultiModeSetting(
      "Отключённые элементы",
      new BooleanSetting("Тряска при уроне", true),
      new BooleanSetting("Скорбоард", false),
      new BooleanSetting("Боссбар", false),
      new BooleanSetting("Эффект портала", true),
      new BooleanSetting("Огонь", true),
      new BooleanSetting("Обрезка камеры", true),
      new BooleanSetting("Частицы разрушения", false),
      new BooleanSetting("Чёрные сердца", true),
      new BooleanSetting("Частицы погоды", false),
      new BooleanSetting("Погружение воды/лавы", false),
      new BooleanSetting("Тошнота", true),
      new BooleanSetting("Слепота", true),
      new BooleanSetting("Тыква", true),
      new BooleanSetting("Свечение", true),
      new BooleanSetting("Тьма", true)
   );

   @Generated
   public MultiModeSetting q() {
      return this.b;
   }

   public Removals() {
      this.a(new Setting[]{this.b});
   }

   @EventTarget
   public void a(RemovalsEvent event) {
      switch (Removals.AnonymousClass1.a[event.b().ordinal()]) {
         case 1:
            event.a(this.b.a("Тряска при уроне").c());
            break;
         case 2:
            event.a(this.b.a("Скорбоард").c());
            break;
         case 3:
            event.a(this.b.a("Боссбар").c());
            break;
         case 4:
            event.a(this.b.a("Эффект портала").c());
            break;
         case 5:
            event.a(this.b.a("Огонь").c());
            break;
         case 6:
            event.a(this.b.a("Обрезка камеры").c());
            break;
         case 7:
            event.a(this.b.a("Частицы разрушения").c());
            break;
         case 8:
            event.a(this.b.a("Погружение воды/лавы").c());
            break;
         case 9:
            event.a(this.b.a("Тошнота").c());
            break;
         case 10:
            event.a(this.b.a("Слепота").c());
            break;
         case 11:
            event.a(this.b.a("Тыква").c());
            break;
         case 12:
            event.a(this.b.a("Частицы погоды").c());
            break;
         case 13:
            event.a(this.b.a("Свечение").c());
            break;
         case 14:
            event.a(this.b.a("Тьма").c());
            break;
         case 15:
            event.a(this.b.a("Чёрные сердца").c());
      }
   }

   static class AnonymousClass1 {
      static final int[] a = new int[RemovalsEvent.a.values().length];

      static {
         try {
            a[RemovalsEvent.a.HURT_CAM.ordinal()] = 1;
         } catch (NoSuchFieldError var15) {
         }

         try {
            a[RemovalsEvent.a.SCOREBOARD.ordinal()] = 2;
         } catch (NoSuchFieldError var14) {
         }

         try {
            a[RemovalsEvent.a.BOSS_BAR.ordinal()] = 3;
         } catch (NoSuchFieldError var13) {
         }

         try {
            a[RemovalsEvent.a.PORTAL.ordinal()] = 4;
         } catch (NoSuchFieldError var12) {
         }

         try {
            a[RemovalsEvent.a.FIRE.ordinal()] = 5;
         } catch (NoSuchFieldError var11) {
         }

         try {
            a[RemovalsEvent.a.CLIP.ordinal()] = 6;
         } catch (NoSuchFieldError var10) {
         }

         try {
            a[RemovalsEvent.a.BREAK_PARTICLES.ordinal()] = 7;
         } catch (NoSuchFieldError var9) {
         }

         try {
            a[RemovalsEvent.a.WATER.ordinal()] = 8;
         } catch (NoSuchFieldError var8) {
         }

         try {
            a[RemovalsEvent.a.NAUSEA.ordinal()] = 9;
         } catch (NoSuchFieldError var7) {
         }

         try {
            a[RemovalsEvent.a.BLINDNESS.ordinal()] = 10;
         } catch (NoSuchFieldError var6) {
         }

         try {
            a[RemovalsEvent.a.PUMPKIN.ordinal()] = 11;
         } catch (NoSuchFieldError var5) {
         }

         try {
            a[RemovalsEvent.a.WEATHER.ordinal()] = 12;
         } catch (NoSuchFieldError var4) {
         }

         try {
            a[RemovalsEvent.a.GLOW.ordinal()] = 13;
         } catch (NoSuchFieldError var3) {
         }

         try {
            a[RemovalsEvent.a.DARKNESS.ordinal()] = 14;
         } catch (NoSuchFieldError var2) {
         }

         try {
            a[RemovalsEvent.a.BLACK_HEARTS.ordinal()] = 15;
         } catch (NoSuchFieldError var1) {
         }
      }
   }
}
