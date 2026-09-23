package aethereal.command;

import aethereal.core.EventTarget;
import aethereal.event.TickEvent;
import aethereal.util.ServerUtil;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import lombok.Generated;
import net.minecraft.class_2172;

@Command(
   a = "rct"
)
public class RCTCommand extends BaseCommand {
   private int c = -1;

   @Generated
   public void b(int anarchy) {
      this.c = anarchy;
   }

   @Generated
   public int f() {
      return this.c;
   }

   @Override
   public void a(LiteralArgumentBuilder<class_2172> builder) {
      ((LiteralArgumentBuilder)builder.executes(context -> {
         this.c();
         return 1;
      })).then(this.e("номер").executes(context2 -> {
         this.a(this.b(context2, "номер"));
         return 1;
      }));
   }

   public void c() {
      this.a(0);
   }

   public void a(int number) {
      this.c = number;
   }

   public void d() {
      this.c = -1;
   }

   public boolean e() {
      return this.c >= 0;
   }

   @EventTarget
   public void a(TickEvent eventTick) {
      if (this.e()) {
         int iB;
         if (ServerUtil.a.a()) {
            iB = ServerUtil.a.d();
         } else {
            iB = ServerUtil.d.a() ? ServerUtil.d.b() : -1;
         }

         if (iB == -1 && this.c > 0) {
            if ((ServerUtil.a.a() || ServerUtil.d.a()) && aM_.field_1724.field_6012 % 20 == 0) {
               aM_.field_1724.field_3944.method_45730("an" + this.c);
               this.d();
               return;
            }

            return;
         }

         if (aM_.field_1724.field_6012 % 2 == 0) {
            if (iB == -1) {
               this.b(-1);
               return;
            }

            if (this.c == 0) {
               this.b(iB);
            }

            aM_.field_1724.field_3944.method_45730("hub");
         }
      }
   }
}
