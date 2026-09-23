package aethereal.command;

import aethereal.core.Westra;
import aethereal.util.ChatUtil;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import lombok.Generated;
import net.minecraft.class_2172;
import net.minecraft.class_243;

@Command(
   a = "gps"
)
public class GPSCommand extends BaseCommand {
   private class_243 c;

   @Generated
   public class_243 c() {
      return this.c;
   }

   @Override
   public void a(LiteralArgumentBuilder<class_2172> builder) {
      ((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)builder.then(this.a("off").executes(context -> {
         if (this.c == null) {
            ChatUtil.a("GPS-метка сейчас отсутствует");
            return 1;
         } else {
            this.c = null;
            ChatUtil.a("GPS-метка больше не отображается");
            return 1;
         }
      }))).then(((RequiredArgumentBuilder)this.e("x").executes(context2 -> {
         ChatUtil.a("Использование: .gps <x> <z>, .gps <x> <y> <z> или .gps off");
         return 1;
      })).then(((RequiredArgumentBuilder)this.e("y или z").executes(context3 -> {
         this.a(new class_243(this.b(context3, "x"), aM_.field_1724.method_23318(), this.b(context3, "y или z")));
         return 1;
      })).then(this.e("z").executes(context4 -> {
         this.a(new class_243(this.b(context4, "x"), this.b(context4, "y или z"), this.b(context4, "z")));
         return 1;
      }))))).then(this.a("event").executes(context5 -> {
         Westra.h().d().u().c().a(WayCommand.a.GPS);
         aM_.field_1724.field_3944.method_45731("event delay");
         return 1;
      }))).executes(context6 -> {
         ChatUtil.a("Использование: .gps <x> <z>, .gps <x> <y> <z> или .gps off");
         return 1;
      });
   }

   public void a(class_243 pos) {
      this.c = pos;
      ChatUtil.a("GPS-метка установлена: " + (int)pos.method_10216() + ", " + (int)pos.method_10214() + ", " + (int)pos.method_10215());
   }
}
