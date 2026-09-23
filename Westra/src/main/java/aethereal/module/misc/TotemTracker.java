package aethereal.module.misc;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.AttackEvent;
import aethereal.event.PacketEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import aethereal.util.ChatUtil;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.class_1309;
import net.minecraft.class_2663;

@ModuleRegister(
   a = "Totem Tracker",
   b = "Пишет в чат, когда у противника лопается тотем, и считает их подряд",
   c = Category.Misc
)
public class TotemTracker extends Module implements Interface {
   private static final byte b = 35;
   private static final long c = 150L;
   private final BooleanSetting d2 = new BooleanSetting("Только по цели", true);
   private final SliderSetting e = new SliderSetting("Память цели", 4.0F, 1.0F, 10.0F, 1.0F).a(() -> this.d2.c());
   private final BooleanSetting f2 = new BooleanSetting("Свои тотемы", false);
   private final BooleanSetting g2 = new BooleanSetting("Считать подряд", true);
   private final Map<Integer, Long> h2 = new HashMap<>();
   private final Map<Integer, Integer> i2 = new HashMap<>();
   private int j2 = -1;
   private long k2;

   public TotemTracker() {
      this.a(new Setting[]{this.d2, this.e, this.f2, this.g2});
   }

   @Override
   public void b() {
      this.h2.clear();
      this.i2.clear();
      this.j2 = -1;
      super.b();
   }

   @EventTarget
   public void a(AttackEvent event) {
      if (event.b() instanceof class_1309 target) {
         this.j2 = target.method_5628();
         this.k2 = System.currentTimeMillis();
      }
   }

   @EventTarget
   public void a(PacketEvent event) {
      if (event.c() && event.d() instanceof class_2663 packet && packet.method_11470() == 35) {
         if (aM_.field_1687 != null && aM_.field_1724 != null) {
            if (packet.method_11469(aM_.field_1687) instanceof class_1309 living) {
               long now = System.currentTimeMillis();
               Long last = this.h2.put(living.method_5628(), now);
               if (last == null || now - last >= 150L) {
                  boolean self = living == aM_.field_1724;
                  if (!self || this.f2.c()) {
                     if (self || !this.d2.c() || this.q(living)) {
                        String name = living.method_5477().getString();
                        if (this.g2.c()) {
                           int count = this.i2.merge(living.method_5628(), 1, Integer::sum);
                           ChatUtil.a(self ? "Ваш тотем лопнул. Всего подряд: " + count + "." : name + " лопнул тотем. Всего подряд: " + count + ".");
                        } else {
                           ChatUtil.a(self ? "Ваш тотем лопнул." : name + " лопнул тотем.");
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private boolean q(class_1309 entity) {
      return entity.method_5628() != this.j2 ? false : System.currentTimeMillis() - this.k2 <= (long)this.e.c().floatValue() * 1000L;
   }
}
