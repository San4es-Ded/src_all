package aethereal.module.combat;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.TickEvent;
import aethereal.handler.UseableHandler;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import aethereal.util.InventoryUtil;
import aethereal.util.ServerUtil;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_1309;
import net.minecraft.class_1802;
import net.minecraft.class_243;
import platform.inject.accessors.ItemCooldownEntryAccessor;
import platform.inject.accessors.ItemCooldownManagerAccessor;

@ModuleRegister(
   a = "Mace Helper",
   b = "Автоматизирует действия при использовании булавы",
   c = Category.Combat
)
public class MaceHelper extends Module {
   public int d;
   public boolean e;
   public BooleanSetting b = new BooleanSetting("Усиление урона", true);
   public BooleanSetting c = new BooleanSetting("Авто-переключение булавы", false);
   int[] f = new int[]{-1, -1};

   @Generated
   public BooleanSetting q() {
      return this.b;
   }

   @Generated
   public BooleanSetting r() {
      return this.c;
   }

   @Generated
   public int s() {
      return this.d;
   }

   @Generated
   public boolean t() {
      return this.e;
   }

   @Generated
   public int[] u() {
      return this.f;
   }

   public MaceHelper() {
      this.a(new Setting[]{this.b, this.c});
   }

   @EventTarget
   public void a(TickEvent event) {
      this.d--;
      List<UseableHandler.a> tasks = Westra.h().d().v().b().a();
      if (this.d <= 198 && aM_.field_1724.method_24828() || aM_.field_1724.method_5799() || aM_.field_1724.field_6012 < 5) {
         if (this.e && tasks.isEmpty()) {
            if (this.f[1] > 8) {
               Westra.h().d().v().a().a(this.f[0], this.f[1], 1);
            } else {
               aM_.field_1724.method_31548().field_7545 = this.f[0];
            }

            this.e = false;
            this.f = new int[]{-1, -1};
         }

         this.d = 0;
      }

      if (!tasks.isEmpty() && tasks.getFirst().a().method_7909() == class_1802.field_49098) {
         this.d = 200;
      }

      int hotbar = InventoryUtil.a(class_1802.field_49814, true);
      int slotMace = InventoryUtil.a(class_1802.field_49814, false);
      if (this.c.c() && slotMace != -1) {
         if (!ServerUtil.a.a() || ServerUtil.e()) {
            ItemCooldownManagerAccessor cooldowns = (ItemCooldownManagerAccessor)aM_.field_1724.method_7357();
            Object entry = cooldowns.getEntries().get(aM_.field_1724.method_7357().method_62836(class_1802.field_49814.method_7854()));
            if (entry == null || ((ItemCooldownEntryAccessor)entry).getEndTick() - cooldowns.getTick() <= 10) {
               Aura aura = Westra.h().d().t().B();
               TriggerBot triggerBot = Westra.h().d().t().X();
               boolean fromAura = aura.s() != null;
               class_1309 target = fromAura ? aura.s() : triggerBot.s();
               if (target == null
                  || target.method_6039()
                  || aM_.field_1724.method_24828()
                  || MaceUtil.a()
                  || this.e
                  || aM_.field_1724.field_6017 <= 0.0F
                  || !tasks.isEmpty()
                  || !Westra.h().d().v().a().a().isEmpty()
                  || Math.hypot(
                        target.method_19538().field_1352 - aM_.field_1724.method_19538().field_1352,
                        target.method_19538().field_1350 - aM_.field_1724.method_19538().field_1350
                     )
                     > 6.0) {
                  return;
               }

               class_243 landing;
               if ((fromAura ? aura.b : triggerBot.d) <= 1
                  || (landing = MaceUtil.a(aM_.field_1724, aM_.field_1687).orElse(null)) == null
                  || aM_.field_1724.method_23318() + aM_.field_1724.method_18798().field_1351 <= landing.method_10214()
                  || aM_.field_1724.method_23318() - landing.method_10214() <= 3.5) {
                  return;
               }

               this.e = true;
               int[] iArr = new int[]{aM_.field_1724.method_31548().field_7545, hotbar != -1 ? hotbar : slotMace};
               this.f = iArr;
               if (hotbar == -1) {
                  Westra.h().d().v().a().a(slotMace, aM_.field_1724.method_31548().field_7545, 1);
               } else {
                  aM_.field_1724.method_31548().field_7545 = hotbar;
               }
            }
         }
      }
   }
}
