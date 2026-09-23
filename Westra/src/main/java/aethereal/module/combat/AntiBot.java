package aethereal.module.combat;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.PacketEvent;
import aethereal.event.TickEvent;
import aethereal.util.ChatUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.class_1299;
import net.minecraft.class_1304;
import net.minecraft.class_1657;
import net.minecraft.class_2604;
import net.minecraft.class_640;
import net.minecraft.class_1297.class_5529;

@ModuleRegister(
   a = "Anti Bot",
   b = "Скрывает фальшивых игроков, появляющихся в мире",
   c = Category.Combat
)
public class AntiBot extends Module {
   private final List<UUID> b = new ArrayList<>();

   @Override
   public void b() {
      super.b();
      this.b.clear();
   }

   @Override
   public void c() {
      super.c();
      this.b.clear();
   }

   @EventTarget
   public void a(PacketEvent event) {
      if (event.c() && event.d() instanceof class_2604 class_2604VarD && class_2604VarD.method_11169() == class_1299.field_6097) {
         class_640 entry = aM_.method_1562().method_2871(class_2604VarD.method_11164());
         boolean skin = entry != null && entry.method_52810() != null && entry.method_52810().comp_1911() != null;
         boolean texture = entry != null && !entry.method_2966().getProperties().get("textures").isEmpty();
         boolean ping = entry == null || entry.method_2959() == 0;
         if (!skin && !texture && ping) {
            this.b.add(class_2604VarD.method_11164());
         }
      }
   }

   @EventTarget
   public void a(TickEvent event) {
      List<UUID> checked = new ArrayList<>();

      for (UUID uuid : this.b) {
         for (class_1657 player : aM_.field_1687.method_18456()) {
            if (player.method_5667().equals(uuid)) {
               boolean armor = !player.method_6118(class_1304.field_6169).method_7960()
                  && !player.method_6118(class_1304.field_6174).method_7960()
                  && !player.method_6118(class_1304.field_6172).method_7960()
                  && !player.method_6118(class_1304.field_6166).method_7960();
               if (armor) {
                  ChatUtil.a("Фальшивый игрок был обнаружен, и удален из мира.");
                  aM_.field_1687.method_2945(player.method_5628(), class_5529.field_26999);
               }

               checked.add(uuid);
               break;
            }
         }
      }

      this.b.removeAll(checked);
   }
}
