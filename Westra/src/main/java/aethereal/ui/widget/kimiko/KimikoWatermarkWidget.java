package aethereal.ui.widget.kimiko;

import aethereal.config.ThemeInfo;
import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.event.DrawEvent;
import aethereal.render.ColorUtil;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import aethereal.ui.element.DragInfo;
import aethereal.ui.widget.Widget;
import aethereal.util.MathUtil;
import aethereal.util.ServerUtil;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class KimikoWatermarkWidget extends Widget implements Interface {
   private static final float g = 20.0F;
   private static final float h = 9.0F;
   private static final float i = 8.0F;
   private static final float j = 11.0F;
   private static final float k = 1.0F;
   private static final float l = 6.0F;
   private static final long m = 1200L;
   private static final DateTimeFormatter n = DateTimeFormatter.ofPattern("H:mm");
   private final BooleanSetting o = new BooleanSetting("Логин в клиенте", true);
   private final BooleanSetting p = new BooleanSetting("Частота кадров", true);
   private final BooleanSetting q = new BooleanSetting("Задержка игрока", true);
   private final BooleanSetting r = new BooleanSetting("Текущее время", true);
   private float s;

   public KimikoWatermarkWidget() {
      super(new DragInfo("Инфо-панель", 0.0F, 0.0F, 0.0F, 0.0F));
      this.j().a(this);
      this.j().a(2);
      this.a(new Setting[]{this.o, this.p, this.q, this.r});
   }

   @Override
   public void a(DrawEvent event) {
      this.d().a(true);
      this.d().a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
      this.s = MathUtil.c(this.s, aM_.method_47599(), 0.1F);
      String brand = "Westra Recode";
      List<String> blocks = this.t();
      float width = 9.0F + Fonts.d.a(brand, 11.0F);

      for (String block : blocks) {
         width += 14.0F + Fonts.c.a(block, 8.0F);
      }

      width += 9.0F;
      float target = MathUtil.c(this.j().f(), width, 0.3F);
      float x = this.j().a();
      float y = this.j().b();
      this.j().c(target);
      this.j().d(20.0F);
      float animation = this.a();
      if (animation <= 0.0F) {
         super.a(event);
      } else {
         this.a(event, x, y, target, 20.0F, true, animation);
         float cursor = x + 9.0F;
         int accent = Westra.h().d().o().a(ThemeInfo.PRIMARY).a();
         float phase = (float)(System.currentTimeMillis() % 1200L) / 1200.0F;
         float brandY = y + (20.0F - Fonts.d.a(11.0F)) / 2.0F - 1.0F;

         for (int index = 0; index < brand.length(); index++) {
            String glyph = String.valueOf(brand.charAt(index));
            float wave = (float)(Math.sin((phase + index * 0.12F) * Math.PI * 2.0) * 0.5 + 0.5);
            int color = ColorUtil.a(ColorUtil.a(255, 255, 255, 255), accent, 0.35F + 0.65F * wave);
            Fonts.d.a(event.h(), glyph, cursor, brandY, 11.0F, ColorUtil.a(color, animation));
            cursor += Fonts.d.a(glyph, 11.0F);
         }

         int muted = Westra.h().d().o().a(ThemeInfo.TEXT_DISABLED).a();
         float textY = y + (20.0F - Fonts.c.a(8.0F)) / 2.0F - 0.5F;

         for (String block : blocks) {
            cursor += 6.0F;
            event.d().a(event.h(), cursor, y + 10.0F - 1.0F, 2.0F, 2.0F, 1.0F, ColorUtil.a(muted, 0.8F * animation));
            cursor += 8.0F;
            Fonts.c.a(event.h(), block, cursor, textY, 8.0F, ColorUtil.a(-1, animation));
            cursor += Fonts.c.a(block, 8.0F);
         }

         super.a(event);
      }
   }

   private List<String> t() {
      List<String> blocks = new ArrayList<>();
      if (this.o.c()) {
         blocks.add(Westra.h().g().b());
      }

      if (this.p.c()) {
         blocks.add(Math.round(this.s) + " fps");
      }

      if (this.q.c()) {
         blocks.add(ServerUtil.d() + " ms");
      }

      if (this.r.c()) {
         blocks.add(LocalTime.now().format(n));
      }

      return blocks;
   }
}
