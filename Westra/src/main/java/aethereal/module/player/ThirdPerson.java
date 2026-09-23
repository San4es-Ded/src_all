package aethereal.module.player;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.KeyEvent;
import aethereal.event.TickEvent;
import aethereal.setting.BindSetting;
import aethereal.setting.ModeSetting;
import aethereal.setting.Setting;
import aethereal.util.Look;
import aethereal.util.MathUtil;
import aethereal.util.Rotation;
import net.minecraft.class_5498;

@ModuleRegister(
   a = "Third Person",
   b = "Свободный обзор от третьего лица без изменения направления движения",
   c = Category.Player
)
public class ThirdPerson extends Module {
   private boolean c;
   private Rotation e;
   private final ModeSetting b = new ModeSetting("Режим активации осмотра", "По нажатию", "По нажатию", "По зажатию");
   private final BindSetting d = new BindSetting("Кнопка осмотра", 342, 0).a(() -> {
      if (this.b.l("По зажатию")) {
         this.d(true);
      } else {
         this.d(!this.c);
      }
   }).b(() -> {
      if (this.c && this.b.l("По зажатию")) {
         this.d(false);
      }
   });

   public ThirdPerson() {
      this.a(new Setting[]{this.b, this.d});
   }

   @EventTarget
   public void a(KeyEvent event) {
      if (this.c && event.b() == aM_.field_1690.field_1824.method_1429().method_1444()) {
         event.a(true);
      }
   }

   @EventTarget
   public void a(TickEvent event) {
      if (this.c) {
         if (aM_.field_1755 != null) {
            this.d(false);
         } else {
            Westra.h().d().k().a(new Rotation(aM_.field_1724.method_36454(), MathUtil.b(aM_.field_1724.method_36455(), -89.0F, 89.0F)), 360.0F, 0, 1);
         }
      }
   }

   private void d(boolean active) {
      if (active) {
         this.e = new Rotation(Look.b(), Look.c());
      } else {
         Look.a(this.e.c());
         Look.b(this.e.d());
      }

      aM_.field_1690.method_31043(active ? class_5498.field_26665 : class_5498.field_26664);
      this.c = active;
   }
}
