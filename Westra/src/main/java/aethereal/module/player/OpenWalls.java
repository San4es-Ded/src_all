package aethereal.module.player;

import aethereal.core.Category;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import java.util.Set;
import lombok.Generated;
import net.minecraft.class_1922;
import net.minecraft.class_2199;
import net.minecraft.class_2238;
import net.minecraft.class_2260;
import net.minecraft.class_2304;
import net.minecraft.class_2338;
import net.minecraft.class_2406;
import net.minecraft.class_243;
import net.minecraft.class_2480;
import net.minecraft.class_2496;
import net.minecraft.class_259;
import net.minecraft.class_265;
import net.minecraft.class_2680;
import net.minecraft.class_3708;
import net.minecraft.class_3710;
import net.minecraft.class_3711;
import net.minecraft.class_3713;
import net.minecraft.class_3715;
import net.minecraft.class_3716;
import net.minecraft.class_3718;
import net.minecraft.class_3865;
import net.minecraft.class_3922;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_4739;
import net.minecraft.class_746;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;

@ModuleRegister(
   a = "Open Walls",
   b = "Позволяет открывать хранилища сквозь стены",
   c = Category.Player
)
public class OpenWalls extends Module {
   private final Set<Class<?>> b = Set.of(
      class_4739.class,
      class_3865.class,
      class_2304.class,
      class_2496.class,
      class_2480.class,
      class_2199.class,
      class_2238.class,
      class_3710.class,
      class_2260.class,
      class_3922.class,
      class_3711.class,
      class_3713.class,
      class_3715.class,
      class_2406.class,
      class_3716.class,
      class_3718.class,
      class_3708.class
   );

   @Generated
   public Set<Class<?>> q() {
      return this.b;
   }

   public class_3965 a(class_746 player) {
      class_243 start = player.method_5836(1.0F);
      class_243 end = start.method_1019(player.method_5828(1.0F).method_1021(player.method_55754()));
      return player.method_37908().method_17742(new class_3959(start, end, class_3960.field_17559, class_242.field_1348, player) {
         public class_265 method_17748(class_2680 state, class_1922 world, class_2338 pos) {
            for (Class<?> clazz : Westra.h().d().t().a().q()) {
               if (clazz.isInstance(state.method_26204())) {
                  return super.method_17748(state, world, pos);
               }
            }

            return class_259.method_1073();
         }
      });
   }
}
