package aethereal.module.misc;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.ScoreboardEvent;
import aethereal.event.TextVisitEvent;
import aethereal.friend.FriendConstructor;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import java.util.Optional;
import java.util.regex.Pattern;
import lombok.Generated;
import net.minecraft.class_2561;
import net.minecraft.class_2583;
import net.minecraft.class_5250;

@ModuleRegister(
   a = "Streamer Mode",
   b = "Скрывает личные данные при стриминге и записи",
   c = Category.Misc
)
public class StreamerMode extends Module {
   private final BooleanSetting b = new BooleanSetting("Скрывать скины игроков", true);
   private final BooleanSetting c = new BooleanSetting("Скрывать имена друзей", true);
   private final BooleanSetting d = new BooleanSetting("Скрывать номер анархии", true);

   @Generated
   public BooleanSetting q() {
      return this.b;
   }

   @Generated
   public BooleanSetting r() {
      return this.c;
   }

   @Generated
   public BooleanSetting s() {
      return this.d;
   }

   public StreamerMode() {
      this.a(new Setting[]{this.b, this.c, this.d});
   }

   @EventTarget
   public void a(TextVisitEvent event) {
      event.a(this.a(event.b()));
   }

   @EventTarget
   public void a(ScoreboardEvent event) {
      if (this.d.c()) {
         class_5250 text = class_2561.method_43473();
         event.b().method_27658((style, part) -> {
            text.method_10852(class_2561.method_43470(part.replaceAll("Анархия-\\d+", "Анархия-???")).method_10862(style));
            return Optional.empty();
         }, class_2583.field_24360);
         event.a(text);
      }
   }

   public String a(String text) {
      String result = text.replaceAll("(?i)" + aM_.method_1548().method_1676(), "Protected");
      if (this.c.c()) {
         for (FriendConstructor friend : Westra.h().d().e().a()) {
            result = Pattern.compile(friend.a(), 82).matcher(result).replaceAll("Protected");
         }
      }

      if (this.d.c()) {
         result = result.replaceAll("Анархия-\\d+", "Анархия-???");
      }

      return result;
   }
}
