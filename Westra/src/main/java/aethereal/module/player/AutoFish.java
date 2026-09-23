package aethereal.module.player;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.HotbarEvent;
import aethereal.event.PacketEvent;
import aethereal.event.TickEvent;
import aethereal.util.ChatUtil;
import aethereal.util.CounterUtil;
import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_1802;
import net.minecraft.class_2767;
import net.minecraft.class_3414;
import net.minecraft.class_3417;

@ModuleRegister(
   a = "Auto Fish",
   b = "Автоматически ловит рыбу в AFK-режиме",
   c = Category.Player
)
public class AutoFish extends Module implements Interface {
   private final CounterUtil b = new CounterUtil();
   private boolean c;

   @Generated
   public CounterUtil q() {
      return this.b;
   }

   @Generated
   public boolean r() {
      return this.c;
   }

   @Override
   public void b() {
      super.b();
      if (aM_.field_1724 != null && aM_.field_1724.method_31548().method_5438(aM_.field_1724.method_31548().field_7545).method_7909() == class_1802.field_8378) {
         if (aM_.field_1724.field_7513 == null) {
            this.d(false);
         }

         ChatUtil.a(this.j() + " активирован, удачной рыбалки!");
      }
   }

   @EventTarget
   public void a(PacketEvent event) {
      if (event.d() instanceof class_2767 class_2767VarD
         && ((class_3414)class_2767VarD.method_11894().comp_349()).comp_3319().equals(class_3417.field_14660.comp_3319())
         && aM_.field_1724.field_7513.method_5649(class_2767VarD.method_11890(), class_2767VarD.method_11889(), class_2767VarD.method_11893())
            <= 0.48999979194765847
         && aM_.field_1724.field_7513 != null) {
         this.d(true);
         this.b.b();
      }
   }

   @EventTarget
   public void a(TickEvent event) {
      if (this.b.a(450L) && this.c) {
         this.d(false);
      }
   }

   @EventTarget
   public void a(HotbarEvent event) {
      if (this.c) {
         event.a(true);
      }
   }

   public void d(boolean cast) {
      aM_.field_1761.method_2919(aM_.field_1724, class_1268.field_5808);
      aM_.field_1724.method_6104(class_1268.field_5808);
      this.c = cast;
   }
}
