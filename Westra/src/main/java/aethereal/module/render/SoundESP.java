package aethereal.module.render;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.DrawEvent;
import aethereal.event.SoundEvent;
import aethereal.render.ColorUtil;
import aethereal.render.Fonts;
import aethereal.setting.BooleanSetting;
import aethereal.setting.MultiModeSetting;
import aethereal.setting.Setting;
import aethereal.util.CounterUtil;
import aethereal.util.ProjectUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_1113;
import net.minecraft.class_243;
import net.minecraft.class_2561;
import org.joml.Vector2f;

@ModuleRegister(
   a = "Sound ESP",
   b = "Отображает место, где был воспроизведён выбранный звук",
   c = Category.Render
)
public class SoundESP extends Module {
   private final MultiModeSetting b = new MultiModeSetting(
      "Отслеживать звуки", new BooleanSetting("Трезубец", true), new BooleanSetting("Фейерверк", true), new BooleanSetting("Взрывы", true)
   );
   private final List<SoundESP.a> c = new ArrayList<>();

   public SoundESP() {
      this.a(new Setting[]{this.b});
   }

   @EventTarget
   public void a(SoundEvent e) {
      String path = e.b().method_4775().method_12832();
      if (path.contains("entity.firework_rocket.launch") && this.b.a("Фейерверк").c()
         || path.contains("entity.generic.explode") && this.b.a("Взрывы").c()
         || path.contains("item.trident.return") && this.b.a("Трезубец").c()) {
         boolean exists = false;

         for (SoundESP.a info : this.c) {
            if (Math.abs(info.b().method_4784() - e.b().method_4784()) <= 0.5
               && Math.abs(info.b().method_4779() - e.b().method_4779()) <= 0.5
               && Math.abs(info.b().method_4778() - e.b().method_4778()) <= 0.5) {
               exists = true;
               break;
            }
         }

         if (!exists) {
            this.c.add(new SoundESP.a(e.b()));
         }
      }
   }

   @EventTarget
   public void a(DrawEvent event) {
      if (event.b()) {
         int color = ColorUtil.a(0, 0, 0, 100);

         for (SoundESP.a soundInfo : new ArrayList<>(this.c)) {
            if (soundInfo.a().a(5500L)) {
               this.c.remove(soundInfo);
            } else {
               Vector2f screenPos = ProjectUtil.a(soundInfo.b().method_4784(), soundInfo.b().method_4779(), soundInfo.b().method_4778());
               if (ProjectUtil.a(screenPos)) {
                  String path = soundInfo.b().method_4775().method_12832();
                  String str;
                  if (path.contains("firework_rocket")) {
                     str = "Фейерверк";
                  } else if (path.contains("explode")) {
                     str = "Взрывы";
                  } else {
                     str = path.contains("trident") ? "Трезубец" : "Звук";
                  }

                  int distance = (int)aM_.field_1724
                     .method_19538()
                     .method_1022(new class_243(soundInfo.b().method_4784(), soundInfo.b().method_4779(), soundInfo.b().method_4778()));
                  int timeAlive = (int)(soundInfo.a().c() / 1000L);
                  class_2561 text = class_2561.method_43470(str + " [" + distance + "м/" + timeAlive + " сек]");
                  float textWidth = Fonts.e.a(text, 7.5F);
                  float textHeight = Fonts.e.d().lineHeight() * 7.5F;
                  float textX = screenPos.x() - textWidth / 2.0F;
                  float textY = screenPos.y();
                  event.d().a(event.i().method_51448(), textX - 2.0F, textY, textWidth + 4.0F, textHeight, 0.0F, color);
                  Fonts.e.a(event.i().method_51448(), text, textX, textY, 7.5F);
               }
            }
         }
      }
   }

   public static class a {
      public final CounterUtil a = new CounterUtil();
      public final class_1113 b;

      @Generated
      public CounterUtil a() {
         return this.a;
      }

      @Generated
      public class_1113 b() {
         return this.b;
      }

      public a(class_1113 sound) {
         this.b = sound;
         this.a.b();
      }
   }
}
