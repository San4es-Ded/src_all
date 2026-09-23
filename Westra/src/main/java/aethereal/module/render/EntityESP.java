package aethereal.module.render;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.DrawEvent;
import aethereal.module.misc.StreamerMode;
import aethereal.render.ColorUtil;
import aethereal.render.Fonts;
import aethereal.setting.BooleanSetting;
import aethereal.setting.MultiModeSetting;
import aethereal.setting.Setting;
import aethereal.util.InventoryUtil;
import aethereal.util.MathUtil;
import aethereal.util.ProjectUtil;
import aethereal.util.ServerUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_1291;
import net.minecraft.class_1293;
import net.minecraft.class_1297;
import net.minecraft.class_1304;
import net.minecraft.class_1309;
import net.minecraft.class_1429;
import net.minecraft.class_1542;
import net.minecraft.class_1588;
import net.minecraft.class_1606;
import net.minecraft.class_1646;
import net.minecraft.class_1657;
import net.minecraft.class_1667;
import net.minecraft.class_1799;
import net.minecraft.class_243;
import net.minecraft.class_2561;
import net.minecraft.class_2583;
import net.minecraft.class_5250;
import org.joml.Vector2f;

@ModuleRegister(
   a = "Entity ESP",
   b = "Отображает информацию о сущностях над их головой",
   c = Category.Render
)
public class EntityESP extends Module {
   private final MultiModeSetting b = new MultiModeSetting(
      "Отслеживаемые сущности",
      new BooleanSetting("Игроки", true),
      new BooleanSetting("Животные", false),
      new BooleanSetting("Мобы", false),
      new BooleanSetting("Предметы", false)
   );
   private final List<EntityESP.a> c = new ArrayList<>();

   @Generated
   public List<EntityESP.a> q() {
      return this.c;
   }

   public EntityESP() {
      this.a(new Setting[]{this.b});
   }

   @EventTarget
   public void a(DrawEvent event) {
      if (event.b()) {
         for (class_1297 class_746Var : aM_.field_1687.method_18112()) {
            if (class_746Var != aM_.field_1724) {
               String str;
               if (class_746Var instanceof class_1657) {
                  str = "Игроки";
               } else if (class_746Var instanceof class_1588) {
                  str = "Мобы";
               } else if (!(class_746Var instanceof class_1429) && !(class_746Var instanceof class_1606) && !(class_746Var instanceof class_1646)) {
                  str = !(class_746Var instanceof class_1542) && !(class_746Var instanceof class_1667) ? null : "Предметы";
               } else {
                  str = "Животные";
               }

               if (str != null && this.b.a(str).c()) {
                  int color = class_746Var instanceof class_1657 && Westra.h().d().e().d(class_746Var.method_5477().getString())
                     ? ColorUtil.a(0, 100, 0, 120)
                     : ColorUtil.a(0, 0, 0, 80);
                  class_243 interpolated = MathUtil.a(class_746Var, event.g());
                  class_243 entityPos = interpolated.method_1031(0.0, class_746Var.method_17682() + 0.25F, 0.0);
                  Vector2f screenPos = ProjectUtil.a(entityPos.method_10216(), entityPos.method_10214(), entityPos.method_10215());
                  if (ProjectUtil.a(screenPos)) {
                     if (!(class_746Var instanceof class_1542) && !(class_746Var instanceof class_1667)) {
                        this.a(class_746Var, event, screenPos, 7.5F, 2.0F, color);
                        this.b(
                           class_746Var,
                           event,
                           ProjectUtil.a(interpolated.field_1352, interpolated.field_1351 - 0.25, interpolated.field_1350),
                           7.5F,
                           2.0F,
                           color
                        );
                     } else {
                        this.c(class_746Var, event, screenPos, 7.5F, 2.0F, color);
                     }
                  }
               }
            }
         }
      }
   }

   private void a(class_1297 entity, DrawEvent event, Vector2f screenPos, float fontSize, float padding, int color) {
      StreamerMode streamerMode = Westra.h().d().t().aE();
      class_2561 name = entity.method_5477();
      if (streamerMode.m() && streamerMode.r().c()) {
         name = class_2561.method_43470(streamerMode.a(name.getString())).method_10862(name.method_10866());
      }

      class_5250 display = name.method_27661().method_10862(name.method_10866().method_36139(16777215));
      display.method_10855().replaceAll(sibling -> sibling.method_27661().method_10862(sibling.method_10866().method_36139(16777215)));
      class_2561 text = (entity.method_5781() != null ? entity.method_5781().method_1144().method_27661().method_10852(display) : display)
         .method_27661()
         .method_10852(class_2561.method_43470(" " + (int)ServerUtil.a.a((class_1309)entity)).method_10862(class_2583.field_24360.method_36139(16711680)));
      float textWidth = Fonts.e.a(text, fontSize);
      float textHeight = Fonts.e.d().lineHeight() * fontSize;
      float textX = screenPos.x() - textWidth / 2.0F;
      float textY = screenPos.y();
      float bgX = textX - padding;
      float bgWidth = textWidth + padding * 2.0F;
      event.d().a(event.i().method_51448(), bgX, textY, bgWidth, textHeight, 0.0F, color);
      Fonts.e.a(event.i().method_51448(), text, textX, textY, fontSize);
      this.a(entity, event, bgWidth, bgX, textY, color, textHeight);
   }

   private void a(class_1297 entity, DrawEvent event, float nameTagWidth, float nameTagX, float nameTagY, int color, float textHeight) {
      if (entity instanceof class_1657 player) {
         float spacing = textHeight * 0.3F;
         class_1799[] stacks = new class_1799[]{
            player.method_6047(),
            player.method_6118(class_1304.field_6169),
            player.method_6118(class_1304.field_6174),
            player.method_6118(class_1304.field_6172),
            player.method_6118(class_1304.field_6166),
            player.method_6079()
         };
         int count = 0;

         for (class_1799 class_1799Var : stacks) {
            if (!class_1799Var.method_7960()) {
               count++;
            }
         }

         if (count > 0) {
            float x = nameTagX + (nameTagWidth - (count * textHeight + (count - 1) * spacing)) / 2.0F;
            float y = nameTagY - textHeight - spacing;

            for (class_1799 stack : stacks) {
               if (!stack.method_7960()) {
                  event.d().a(event.i().method_51448(), x, y, textHeight, textHeight, 0.0F, color);
                  event.e().a(event.i(), InventoryUtil.a(stack), x, y, 0, 1.0F, textHeight / 16.0F, true);
                  x += textHeight + spacing;
               }
            }
         }
      }
   }

   private void b(class_1297 entity, DrawEvent event, Vector2f screenPos, float fontSize, float padding, int color) {
      if (entity instanceof class_1309 living) {
         List<EntityESP.a> trackers = new ArrayList<>();

         for (int i = this.c.size() - 1; i >= 0; i--) {
            EntityESP.a entry = this.c.get(i);
            if (entry.b() == living.method_5628()) {
               if (living.field_6012 < entry.c()) {
                  this.c.remove(i);
               } else {
                  trackers.add(entry);
               }
            }
         }

         List<class_1293> effects;
         if (trackers.isEmpty()) {
            effects = new ArrayList<>(living.method_6026());
         } else {
            effects = new ArrayList<>();

            for (EntityESP.a tracker : trackers) {
               for (class_1293 effectInstance : tracker.a()) {
                  int remaining = effectInstance.method_5584() - Math.max(0, living.field_6012 - tracker.c());
                  if (remaining > 0) {
                     class_1293 remainingEffect = effectInstance.method_5584() > 1000000
                        ? effectInstance
                        : new class_1293(effectInstance.method_5579(), remaining, effectInstance.method_5578());
                     class_1293 existing = null;

                     for (class_1293 instance : effects) {
                        if (instance.method_5579().equals(effectInstance.method_5579())) {
                           existing = instance;
                           break;
                        }
                     }

                     if (existing == null) {
                        effects.add(remainingEffect);
                     } else if (remainingEffect.method_5578() > existing.method_5578()
                        || remainingEffect.method_5578() == existing.method_5578() && remainingEffect.method_5584() > existing.method_5584()) {
                        effects.remove(existing);
                        effects.add(remainingEffect);
                     }
                  }
               }
            }

            if (effects.isEmpty()) {
               effects = new ArrayList<>(living.method_6026());
            }
         }

         float lineHeight = Fonts.e.d().lineHeight() * fontSize;
         float maxWidth = 0.0F;

         for (class_1293 effect : effects) {
            int seconds = effect.method_5584() / 20;
            maxWidth = Math.max(
               maxWidth,
               Fonts.e
                  .a(
                     class_2561.method_43471(((class_1291)effect.method_5579().comp_349()).method_5567()).getString()
                        + " "
                        + MathUtil.a(effect.method_5578())
                        + (effect.method_5584() > 1000000 ? " ∞" : " - " + seconds / 60 + ":" + String.format("%02d", seconds % 60)),
                     fontSize
                  )
            );
         }

         float textX = screenPos.x() - maxWidth / 2.0F;
         float textY = screenPos.y() + padding;
         event.d().a(event.i().method_51448(), textX - padding, textY, maxWidth + padding * 2.0F, effects.size() * lineHeight, 0.0F, color);
         float lineY = textY;

         for (class_1293 effect2 : effects) {
            String duration = effect2.method_5584() > 1000000
               ? " ∞"
               : " - " + effect2.method_5584() / 20 / 60 + ":" + String.format("%02d", effect2.method_5584() / 20 % 60);
            String line = class_2561.method_43471(((class_1291)effect2.method_5579().comp_349()).method_5567()).getString()
               + " "
               + MathUtil.a(effect2.method_5578())
               + duration;
            Fonts.e
               .a(
                  event.i().method_51448(),
                  line,
                  screenPos.x() - Fonts.e.a(line, fontSize) / 2.0F,
                  lineY,
                  fontSize,
                  ColorUtil.a(((class_1291)effect2.method_5579().comp_349()).method_5556(), 1.0F),
                  0.0F
               );
            lineY += lineHeight;
         }
      }
   }

   private void c(class_1297 entity, DrawEvent event, Vector2f screenPos, float fontSize, float padding, int color) {
      class_5250 text = entity instanceof class_1542 ? ((class_1542)entity).method_6983().method_7964().method_27661() : entity.method_5477().method_27661();
      if (entity instanceof class_1542 item && item.method_6983().method_7947() > 1) {
         text.method_10852(class_2561.method_43470(" x" + item.method_6983().method_7947()));
      }

      float textWidth = Fonts.e.a(text, fontSize);
      float textHeight = Fonts.e.d().lineHeight() * fontSize;
      float textX = screenPos.x() - textWidth / 2.0F;
      float textY = screenPos.y();
      event.d().a(event.i().method_51448(), textX - padding, textY, textWidth + padding * 2.0F, textHeight, 0.0F, color);
      Fonts.e.a(event.i().method_51448(), text, textX, textY, fontSize);
   }

   public static final class a {
      private final List<class_1293> a;
      private final int b;
      private final int c;

      public a(List<class_1293> effects, int id, int age) {
         this.a = effects;
         this.b = id;
         this.c = age;
      }

      public List<class_1293> a() {
         return this.a;
      }

      public int b() {
         return this.b;
      }

      public int c() {
         return this.c;
      }
   }
}
