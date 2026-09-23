package aethereal.module.misc;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.DrawEvent;
import aethereal.event.TickEvent;
import aethereal.render.ColorUtil;
import aethereal.setting.BooleanSetting;
import aethereal.setting.MultiModeSetting;
import aethereal.setting.Setting;
import aethereal.util.ServerUtil;
import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_1531;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_238;

@ModuleRegister(
   a = "Mine Assistant",
   b = "Помощник, упрощающий добычу ресурсов в шахте под FunTime/SpookyTime",
   c = Category.Misc
)
public class MineAssistant extends Module implements Interface {
   private final MultiModeSetting b = new MultiModeSetting(
      "Выберите подсвечиваемые руды",
      new BooleanSetting("Алмазная", true),
      new BooleanSetting("Редстоуновая", false),
      new BooleanSetting("Железная", false),
      new BooleanSetting("Лазуритовая", false),
      new BooleanSetting("Золотая", true),
      new BooleanSetting("Древние", true),
      new BooleanSetting("Угольная", false)
   );
   private final List<MineAssistant.a> c = Arrays.asList(
      new MineAssistant.a(class_2246.field_10442, Color.CYAN.getRGB(), "Алмазная"),
      new MineAssistant.a(class_2246.field_29029, Color.CYAN.getRGB(), "Алмазная"),
      new MineAssistant.a(class_2246.field_10080, Color.RED.getRGB(), "Редстоуновая"),
      new MineAssistant.a(class_2246.field_29030, Color.RED.getRGB(), "Редстоуновая"),
      new MineAssistant.a(class_2246.field_10212, Color.LIGHT_GRAY.getRGB(), "Железная"),
      new MineAssistant.a(class_2246.field_29027, Color.LIGHT_GRAY.getRGB(), "Железная"),
      new MineAssistant.a(class_2246.field_10090, Color.BLUE.getRGB(), "Лазуритовая"),
      new MineAssistant.a(class_2246.field_29028, Color.BLUE.getRGB(), "Лазуритовая"),
      new MineAssistant.a(class_2246.field_10571, Color.YELLOW.getRGB(), "Золотая"),
      new MineAssistant.a(class_2246.field_29026, Color.YELLOW.getRGB(), "Золотая"),
      new MineAssistant.a(class_2246.field_22109, new Color(102, 51, 0).getRGB(), "Древние"),
      new MineAssistant.a(class_2246.field_10418, Color.DARK_GRAY.getRGB(), "Угольная"),
      new MineAssistant.a(class_2246.field_29219, Color.DARK_GRAY.getRGB(), "Угольная"),
      new MineAssistant.a(class_2246.field_10124, -1, null),
      new MineAssistant.a(class_2246.field_10340, -1, null),
      new MineAssistant.a(class_2246.field_10474, -1, null),
      new MineAssistant.a(class_2246.field_10445, -1, null)
   );
   private class_238 d;

   @Generated
   public class_238 r() {
      return this.d;
   }

   public MineAssistant() {
      this.a(new Setting[]{this.b});
   }

   @EventTarget
   public void a(TickEvent event) {
      if (ServerUtil.a.a() || ServerUtil.d.a()) {
         this.q();
      }
   }

   public void q() {
      for (class_1531 class_1531Var : aM_.field_1687.method_8390(class_1531.class, aM_.field_1724.method_5829().method_1014(256.0), e -> true)) {
         if (class_1531Var instanceof class_1531 && class_1531Var.method_5477().getString().contains("Авто-Шахта")) {
            if (this.d != null && !(this.d.method_995() <= 15.0)) {
               return;
            }

            int scanY = (int)Math.floor(class_1531Var.method_23318()) - 2;
            int startX = (int)Math.floor(class_1531Var.method_23317());
            int startZ = (int)Math.floor(class_1531Var.method_23321());
            int minX = startX;
            int maxX = startX;
            int minZ = startZ;
            int maxZ = startZ;

            while (this.a(aM_.field_1687.method_8320(new class_2338(minX - 2, scanY, startZ)).method_26204()) != null) {
               minX--;
            }

            while (this.a(aM_.field_1687.method_8320(new class_2338(maxX + 2, scanY, startZ)).method_26204()) != null) {
               maxX++;
            }

            while (this.a(aM_.field_1687.method_8320(new class_2338(startX, scanY, minZ - 1)).method_26204()) != null) {
               minZ--;
            }

            while (this.a(aM_.field_1687.method_8320(new class_2338(startX, scanY, maxZ + 1)).method_26204()) != null) {
               maxZ++;
            }

            this.d = new class_238(minX, scanY + 1, minZ, maxX + 1, scanY - 8, maxZ + 1);
            return;
         }
      }

      this.d = null;
   }

   @EventTarget
   public void a(DrawEvent event) {
      if (event.c() && this.d != null) {
         for (int x = (int)this.d.field_1323; x <= (int)this.d.field_1320; x++) {
            for (int y = (int)this.d.field_1322; y <= (int)this.d.field_1325; y++) {
               for (int z = (int)this.d.field_1321; z <= (int)this.d.field_1324; z++) {
                  class_2338 pos = new class_2338(x, y, z);
                  MineAssistant.a info = this.a(aM_.field_1687.method_8320(pos).method_26204());
                  if (info != null && info.b() != -1 && this.b.a(info.c()).c()) {
                     event.e().a(event.h(), new class_238(pos), ColorUtil.a(info.b(), 150), 1.0F);
                  }
               }
            }
         }
      }
   }

   public MineAssistant.a a(class_2248 block) {
      for (MineAssistant.a info : this.c) {
         if (info.a == block) {
            return info;
         }
      }

      return null;
   }

   public static class a {
      final class_2248 a;
      final int b;
      final String c;

      @Generated
      public class_2248 a() {
         return this.a;
      }

      @Generated
      public int b() {
         return this.b;
      }

      @Generated
      public String c() {
         return this.c;
      }

      a(class_2248 block, int color, String name) {
         this.a = block;
         this.b = color;
         this.c = name;
      }
   }
}
