package aethereal.module.render;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.ScoreboardEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.ModeSetting;
import aethereal.setting.MultiModeSetting;
import aethereal.setting.Setting;
import aethereal.setting.StringSetting;
import java.util.Locale;
import java.util.Optional;
import net.minecraft.class_124;
import net.minecraft.class_2561;
import net.minecraft.class_2583;
import net.minecraft.class_5250;

@ModuleRegister(
   a = "Board Spoofer",
   b = "Подменяет значения доната, монет и токенов в Scoreboard",
   c = Category.Render
)
public class BoardSpoofer extends Module {
   private final MultiModeSetting b = new MultiModeSetting(
      "Элементы настройки", new BooleanSetting("Ранг", true), new BooleanSetting("Монеты", true), new BooleanSetting("Токены", true)
   );
   private final ModeSetting c = new ModeSetting(
         "Выберите привилегию", "Игрок", "Игрок", "Барон", "Страж", "Герой", "Аспид", "Сквид", "Глава", "Элита", "Титан", "Принц", "Князь", "Герцог"
      )
      .a(() -> this.b.a("Ранг").c());
   private final StringSetting d = new StringSetting("Число монет", "", true).a(() -> this.b.a("Монеты").c());
   private final StringSetting e = new StringSetting("Число токенов", "", true).a(() -> this.b.a("Токены").c());

   public BoardSpoofer() {
      this.a(new Setting[]{this.b, this.c, this.d, this.e});
   }

   @EventTarget
   public void a(ScoreboardEvent event) {
      class_2561 title = event.b();
      if (this.b.a("Ранг").c()) {
         title = this.a(title, "Ранг: ", this.c.c());
      }

      if (this.b.a("Монеты").c()) {
         Locale locale = Locale.US;
         Object[] objArr = new Object[]{Long.parseLong(this.d.c().isEmpty() ? "0" : this.d.c())};
         title = this.a(title, "Монет: ", String.format(locale, "%,d", objArr));
      }

      if (this.b.a("Токены").c()) {
         title = this.a(title, "Токенов: ", this.e.c().isEmpty() ? "0" : this.e.c());
      }

      event.a(title);
   }

   private class_2561 a(class_2561 text, String label, String newValue) {
      class_5250 rebuilt = class_2561.method_43473();
      StringBuilder seen = new StringBuilder();
      text.method_27658((style, part) -> {
         int previousLength = seen.length();
         seen.append(part);
         int labelIndex = seen.indexOf(label);
         if (labelIndex != -1 && labelIndex + label.length() > previousLength) {
            rebuilt.method_10852(class_2561.method_43470(part.substring(0, labelIndex + label.length() - previousLength)).method_10862(style));
            rebuilt.method_10852(class_2561.method_43470(newValue).method_10862("Ранг: ".equals(label) ? this.a(newValue) : style));
            return Optional.of(true);
         } else {
            rebuilt.method_10852(class_2561.method_43470(part).method_10862(style));
            return Optional.empty();
         }
      }, class_2583.field_24360);
      return (class_2561)(rebuilt.method_10855().isEmpty() ? text : rebuilt);
   }

   private class_2583 a(String rank) {
      switch (rank) {
         case "Страж":
            return class_2583.field_24360.method_27706(class_124.field_1054);
         case "Барон":
         case "Сквид":
            return class_2583.field_24360.method_27706(class_124.field_1075);
         case "Герой":
            return class_2583.field_24360.method_27706(class_124.field_1060);
         case "Аспид":
            return class_2583.field_24360.method_27706(class_124.field_1062);
         case "Глава":
         case "Титан":
            return class_2583.field_24360.method_27706(class_124.field_1065);
         case "Элита":
            return class_2583.field_24360.method_27706(class_124.field_1064);
         case "Принц":
         case "Князь":
            return class_2583.field_24360.method_27706(class_124.field_1061);
         case "Герцог":
            return class_2583.field_24360.method_27706(class_124.field_1079);
         default:
            return class_2583.field_24360.method_27706(class_124.field_1068);
      }
   }
}
