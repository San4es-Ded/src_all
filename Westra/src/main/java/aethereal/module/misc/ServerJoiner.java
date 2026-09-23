package aethereal.module.misc;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.PacketEvent;
import aethereal.event.TickEvent;
import aethereal.setting.ModeSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import aethereal.ui.screen.GUIScreen;
import aethereal.util.ChatUtil;
import aethereal.util.CounterUtil;
import aethereal.util.InventoryUtil;
import aethereal.util.ServerUtil;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import lombok.Generated;
import net.minecraft.class_1707;
import net.minecraft.class_1713;
import net.minecraft.class_1735;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2813;
import net.minecraft.class_2868;
import net.minecraft.class_2886;
import net.minecraft.class_3944;
import net.minecraft.class_476;
import platform.inject.invokers.ClientPlayerInteractionManagerInvoker;

@ModuleRegister(
   a = "Server Joiner",
   b = "Автоматически подключается к указанному серверу",
   c = Category.Misc
)
public class ServerJoiner extends Module implements Interface {
   private final ModeSetting b = new ModeSetting("Выберите сервер", "SpookyTime", "SpookyTime", "ReallyWorld");
   private final SliderSetting c = new SliderSetting("Укажите номер грифа (1-54)", 1.0F, 1.0F, 54.0F, 1.0F).a(() -> this.b.l("ReallyWorld"));
   private final CounterUtil d = new CounterUtil();
   private int e = -1;

   @Generated
   public ModeSetting q() {
      return this.b;
   }

   @Generated
   public SliderSetting r() {
      return this.c;
   }

   @Generated
   public CounterUtil s() {
      return this.d;
   }

   @Generated
   public int t() {
      return this.e;
   }

   public ServerJoiner() {
      this.a(new Setting[]{this.b, this.c});
   }

   @EventTarget
   public void a(TickEvent e) {
      if (!(aM_.field_1755 instanceof GUIScreen)) {
         if (this.b.l("SpookyTime")) {
            if (ServerUtil.a().contains("Хаб")) {
               int compassSlot = InventoryUtil.a(class_1802.field_8251, true);
               if (compassSlot >= 0 && compassSlot <= 8 && aM_.field_1755 == null) {
                  aM_.field_1724.field_3944.method_52787(new class_2868(compassSlot));
                  ((ClientPlayerInteractionManagerInvoker)aM_.field_1761)
                     .invokeSendSequencedPacket(
                        aM_.field_1687,
                        sequence -> new class_2886(aM_.field_1724.method_6058(), sequence, aM_.field_1724.method_36454(), aM_.field_1724.method_36455())
                     );
               }

               if (this.e != -1) {
                  aM_.field_1724
                     .field_3944
                     .method_52787(new class_2813(this.e, 0, 13, 0, class_1713.field_7790, class_1799.field_8037, Int2ObjectMaps.emptyMap()));
                  this.e = -1;
                  return;
               }

               return;
            }

            if (!ServerUtil.a().isEmpty() && !ServerUtil.a().contains("Режим: Хаб # ")) {
               ChatUtil.a("Вы находитесь не в хабе SpookyTime, а значит модуль выключается!");
               this.a();
               return;
            }

            return;
         }

         if (this.b.l("ReallyWorld")) {
            if (ServerUtil.a().isEmpty()) {
               int compassSlot2 = InventoryUtil.a(class_1802.field_8251, true);
               if (compassSlot2 >= 0 && compassSlot2 <= 8 && aM_.field_1755 == null) {
                  aM_.field_1724.field_3944.method_52787(new class_2868(compassSlot2));
                  ((ClientPlayerInteractionManagerInvoker)aM_.field_1761)
                     .invokeSendSequencedPacket(
                        aM_.field_1687,
                        sequence2 -> new class_2886(aM_.field_1724.method_6058(), sequence2, aM_.field_1724.method_36454(), aM_.field_1724.method_36455())
                     );
               }

               if (aM_.field_1755 instanceof class_476 class_476Var) {
                  class_476 screen = class_476Var;
                  class_1707 handler = (class_1707)class_476Var.method_17577();
                  if (class_476Var.method_25440().getString().contains("» Выбор сервера")) {
                     aM_.field_1724
                        .field_3944
                        .method_52787(
                           new class_2813(
                              handler.field_7763,
                              handler.method_37421(),
                              21,
                              0,
                              class_1713.field_7790,
                              handler.method_34255().method_7972(),
                              Int2ObjectMaps.emptyMap()
                           )
                        );
                  }

                  for (int i = 0; i < handler.method_17388() * 9; i++) {
                     class_1735 slot = (class_1735)handler.field_7761.get(i);
                     if (slot.method_7677().method_7964().getString().contains("ГРИФ #" + this.c.c().intValue() + " (1.16.5+)")
                        && screen.method_25440().getString().contains("Выбор мира грифа ")) {
                        if (this.d.a(5500L)) {
                           aM_.field_1724
                              .field_3944
                              .method_52787(
                                 new class_2813(
                                    handler.field_7763,
                                    handler.method_37421(),
                                    slot.field_7874,
                                    0,
                                    class_1713.field_7790,
                                    handler.method_34255().method_7972(),
                                    Int2ObjectMaps.emptyMap()
                                 )
                              );
                           this.d.b();
                           return;
                        }

                        return;
                     }
                  }

                  return;
               }

               return;
            }

            ChatUtil.a("Вы находитесь не в лобби ReallyWorld, а значит модуль выключается!");
            this.a();
         }
      }
   }

   @EventTarget
   public void a(PacketEvent event) {
      if (this.b.l("SpookyTime") && event.c() && event.d() instanceof class_3944 class_3944VarD) {
         if (class_3944VarD.method_17594().getString().contains("☫ Выберите режим:")) {
            this.e = class_3944VarD.method_17592();
         }

         event.a(true);
      }
   }
}
