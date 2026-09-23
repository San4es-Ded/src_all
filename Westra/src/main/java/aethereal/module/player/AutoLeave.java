package aethereal.module.player;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.TickEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.MultiModeSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import aethereal.util.ChatUtil;
import aethereal.util.ServerUtil;
import net.minecraft.class_1657;

@ModuleRegister(
   a = "Auto Leave",
   b = "Автоматически выходит в хаб по триггерам",
   c = Category.Player
)
public class AutoLeave extends Module {
   private final MultiModeSetting b = new MultiModeSetting(
      "Условия срабатывания", new BooleanSetting("Малое ХП", true), new BooleanSetting("Игроки рядом", true)
   );
   private final SliderSetting c = new SliderSetting("Минимум ХП", 8.0F, 1.0F, 20.0F, 0.5F).a(() -> this.b.a("Малое ХП").c());
   private final SliderSetting d = new SliderSetting("Дистанция игроков", 8.0F, 8.0F, 128.0F, 1.0F).a(() -> this.b.a("Игроки рядом").c());

   public AutoLeave() {
      this.a(new Setting[]{this.b, this.c, this.d});
   }

   @EventTarget
   public void a(TickEvent event) {
      if (!aM_.field_1687.method_27983().method_29177().toString().equals("minecraft:lobby")) {
         if (!ServerUtil.a.a() || ServerUtil.a.d() != -1) {
            class_1657 near = aM_.field_1687
               .method_18456()
               .stream()
               .filter(
                  player -> player != aM_.field_1724
                     && !(aM_.field_1724.method_5858(player) > this.d.c() * this.d.c())
                     && !Westra.h().d().e().d(player.method_5477().getString())
               )
               .findFirst()
               .orElse(null);
            if ((this.b.a("Малое ХП").c() && aM_.field_1724.method_6032() <= this.c.c() || this.b.a("Игроки рядом").c() && near != null) && !ServerUtil.e()) {
               aM_.field_1724.field_3944.method_45730("hub");
               if (near != null) {
                  ChatUtil.a(
                     "Покинул анархию: рядом игрок &c"
                        + near.method_5477().getString()
                        + "&7 в &c"
                        + Math.round(Math.sqrt(aM_.field_1724.method_5858(near)))
                        + "&7 блоках."
                  );
               } else {
                  ChatUtil.a("Покинул &cанархию: критически мало здоровья&7.");
               }

               this.a();
            }
         }
      }
   }
}
