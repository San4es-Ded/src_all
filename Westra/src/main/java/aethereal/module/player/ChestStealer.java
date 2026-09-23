package aethereal.module.player;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.ContainerEvent;
import aethereal.event.RayTraceEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import aethereal.util.CounterUtil;
import lombok.Generated;
import net.minecraft.class_1713;
import net.minecraft.class_1735;
import net.minecraft.class_476;
import net.minecraft.class_495;

@ModuleRegister(
   a = "Chest Stealer",
   b = "Автоматически забирает предметы из открытого сундука",
   c = Category.Player
)
public class ChestStealer extends Module implements Interface {
   private final BooleanSetting b = new BooleanSetting("Игнорировать сущностей", true);
   private final BooleanSetting c = new BooleanSetting("Авто-закрытие сундука", true);
   private final CounterUtil d = new CounterUtil();

   @Generated
   public BooleanSetting q() {
      return this.b;
   }

   @Generated
   public BooleanSetting r() {
      return this.c;
   }

   @Generated
   public CounterUtil s() {
      return this.d;
   }

   public ChestStealer() {
      this.a(new Setting[]{this.b, this.c});
   }

   @EventTarget
   public void a(RayTraceEvent event) {
      if (this.b.c()) {
         event.a(true);
      }
   }

   @EventTarget
   public void a(ContainerEvent event) {
      class_1735 target;
      if (event.h() == ContainerEvent.Phase.POST
         && (event.b() instanceof class_476 || event.b() instanceof class_495)
         && (
               target = event.e()
                  .stream()
                  .filter(slot -> slot.field_7871 != aM_.field_1724.method_31548())
                  .filter(v0 -> v0.method_7681())
                  .findFirst()
                  .orElse(null)
            )
            != null
         && this.d.a(5L, 5L)) {
         aM_.field_1761.method_2906(event.c().field_7763, target.field_7874, 0, class_1713.field_7794, aM_.field_1724);
         boolean empty = event.e()
            .stream()
            .filter(slot2 -> slot2.field_7871 != aM_.field_1724.method_31548())
            .noneMatch(slot3 -> slot3.method_7681() && slot3 != target);
         if (empty && this.c.c()) {
            aM_.field_1724.method_3137();
         }

         this.d.b();
      }
   }
}
