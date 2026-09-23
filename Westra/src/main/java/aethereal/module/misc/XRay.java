package aethereal.module.misc;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.DrawEvent;
import aethereal.event.PacketEvent;
import aethereal.event.TickEvent;
import aethereal.render.ColorUtil;
import aethereal.util.ChatUtil;
import aethereal.util.CounterUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_2637;

@ModuleRegister(
   a = "X Ray",
   b = "Подсвечивает найденные древние обломки при взрыве динамита",
   c = Category.Misc
)
public class XRay extends Module implements Interface {
   private final List<class_2338> b = new ArrayList<>();
   private final CounterUtil c = new CounterUtil();
   private boolean d;

   @Generated
   public List<class_2338> s() {
      return this.b;
   }

   @Generated
   public CounterUtil q() {
      return this.c;
   }

   @Generated
   public boolean r() {
      return this.d;
   }

   @Override
   public void c() {
      super.c();
      this.b.clear();
      this.d = false;
      this.c.b();
   }

   @Override
   public void b() {
      super.b();
      this.b.clear();
      this.d = false;
      this.c.b();
   }

   @EventTarget
   public void a(DrawEvent draw) {
      if (draw.c()) {
         this.b
            .removeIf(
               blockPos -> aM_.field_1687.method_8320(blockPos).method_26204().equals(class_2246.field_10124)
                  || blockPos.method_19770(aM_.field_1724.method_19538()) >= 6400.0
                  || !aM_.field_1687.method_2935().method_12123(blockPos.method_10263() >> 4, blockPos.method_10260() >> 4)
            );
         this.b.forEach(pos -> draw.e().a(draw.h(), new class_238(pos), ColorUtil.a(255, 165, 0, 150), 1.0F));
      }
   }

   @EventTarget
   public void a(PacketEvent packet) {
      if (packet.d() instanceof class_2637 class_2637VarD) {
         class_2637VarD.method_30621((blockPos, blockState) -> {
            if (blockState.method_26204().equals(class_2246.field_22109)) {
               class_2338 add = blockPos.method_10062();
               if (!this.b.contains(add)) {
                  this.b.add(add);
                  this.d = true;
                  this.c.b();
               }
            }
         });
      }
   }

   @EventTarget
   public void a(TickEvent e) {
      this.c.a();
      if (this.d && this.c.b(5L)) {
         ChatUtil.a("Обнаружено &c" + this.b.size() + "&7 древних обломков ");
         this.d = false;
      }
   }
}
