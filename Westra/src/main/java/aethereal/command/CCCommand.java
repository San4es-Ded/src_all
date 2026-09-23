package aethereal.command;

import aethereal.core.EventTarget;
import aethereal.core.Westra;
import aethereal.event.TickEvent;
import aethereal.friend.FriendConstructor;
import aethereal.util.ServerUtil;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.class_2172;
import net.minecraft.class_640;

@Command(
   a = "ccc"
)
public class CCCommand extends BaseCommand {
   private int c;

   @Override
   public void a(LiteralArgumentBuilder<class_2172> builder) {
      builder.executes(
         context -> {
            if (!ServerUtil.a.a() && !ServerUtil.d.a()) {
               return 1;
            } else {
               StringBuilder name = new StringBuilder();

               for (int i = 0; i < 3 + (int)(Math.random() * 3.0); i++) {
                  name.append(
                     "абвгдежзийклмнопрстуфхцчшщъыьэюяАБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
                        .charAt(
                           (int)(
                              Math.random()
                                 * "абвгдежзийклмнопрстуфхцчшщъыьэюяАБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
                                    .length()
                           )
                        )
                  );
               }

               aM_.field_1724.field_3944.method_45729("/clan create " + name);
               this.c = aM_.field_1724.field_6012 + 2;
               return 1;
            }
         }
      );
   }

   @EventTarget
   public void a(TickEvent eventTick) {
      if (this.c >= aM_.field_1724.field_6012) {
         for (FriendConstructor constructor : Westra.h().d().e().e()) {
            class_640 entry = aM_.field_1724
               .field_3944
               .method_2880()
               .stream()
               .filter(listEntry -> listEntry.method_2966().getName().equalsIgnoreCase(constructor.a()))
               .findFirst()
               .orElse(null);
            if (entry != null && !constructor.a().equals(aM_.method_1548().method_1676())) {
               aM_.field_1724.field_3944.method_45729("/clan invite " + constructor.a());
            }
         }

         this.c = -3;
      }
   }
}
