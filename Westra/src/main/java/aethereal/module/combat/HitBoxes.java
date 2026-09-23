package aethereal.module.combat;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.BoundingBoxEvent;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import net.minecraft.class_1657;
import net.minecraft.class_238;

@ModuleRegister(
   a = "Hit Boxes",
   b = "Увеличивает хитбокс игроков, упрощая попадания по ним",
   c = Category.Combat
)
public class HitBoxes extends Module {
   private final SliderSetting b = new SliderSetting("Расширение X и Z", 0.0F, 0.0F, 1.0F, 0.1F);
   private final SliderSetting c = new SliderSetting("Расширение Y", 0.0F, 0.0F, 1.0F, 0.1F);

   public HitBoxes() {
      this.a(new Setting[]{this.b, this.c});
   }

   @EventTarget
   public void a(BoundingBoxEvent event) {
      if (event.c() instanceof class_1657 class_1657VarC) {
         class_238 box = event.b();
         class_238 changedBox = new class_238(
            box.field_1323 - this.b.h() / 2.0F,
            box.field_1322,
            box.field_1321 - this.b.h() / 2.0F,
            box.field_1320 + this.b.h() / 2.0F,
            box.field_1325 + this.c.h().floatValue(),
            box.field_1324 + this.b.h() / 2.0F
         );
         if (class_1657VarC.method_5628() != aM_.field_1724.method_5628() && !Westra.h().d().e().d(class_1657VarC.method_5477().getString())) {
            event.a(changedBox);
         }
      }
   }
}
