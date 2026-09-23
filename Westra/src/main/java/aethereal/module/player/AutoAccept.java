package aethereal.module.player;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.PacketEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.MultiModeSetting;
import aethereal.setting.Setting;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.class_7439;

@ModuleRegister(
   a = "Auto Accept",
   b = "Автоматически принимает выбранные запросы",
   c = Category.Player
)
public class AutoAccept extends Module {
   public final MultiModeSetting b = new MultiModeSetting(
      "Принимать запросы", new BooleanSetting("В клановую команду", true), new BooleanSetting("Телепортации", true)
   );
   private final BooleanSetting c = new BooleanSetting("Принимать только друзей", true);
   private final List<String> d = List.of(
      "просит телепортироваться",
      "хочет телепортироваться",
      "Заявка буудет автоматически отменена через",
      "просит телепортироваться к Вам.",
      "120 секунд",
      "has requested to teleport",
      "teleport to you",
      "This request will timeout after",
      "120 seconds"
   );
   private final List<String> e = List.of("приглашает вас в клан", "invites you to the clan");

   public AutoAccept() {
      this.a(new Setting[]{this.b, this.c});
   }

   @EventTarget
   public void a(PacketEvent event) {
      if (event.c() && event.d() instanceof class_7439 class_7439VarD) {
         String chat = class_7439VarD.comp_763().getString().toLowerCase();
         if (this.c.c() && Westra.h().d().e().a().stream().noneMatch(friend -> chat.contains(friend.a().toLowerCase()))) {
            return;
         }

         if (this.b.a("Телепортации").c()) {
            Stream<String> stream = this.d.stream();
            if (stream.anyMatch(v1 -> chat.contains(v1))) {
               aM_.field_1724.field_3944.method_45730("tpaccept");
            }
         }

         if (this.b.a("В клановую команду").c()) {
            Stream<String> stream2 = this.e.stream();
            if (stream2.anyMatch(v1 -> chat.contains(v1))) {
               aM_.field_1724.field_3944.method_45730("clan accept " + chat.split("")[1]);
            }
         }
      }
   }
}
