package aethereal.module.misc;

import aethereal.config.ThemeInfo;
import aethereal.config.ThemeProcessor;
import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.BackendEvent;
import aethereal.event.DrawEvent;
import aethereal.event.PacketEvent;
import aethereal.event.TickEvent;
import aethereal.render.ColorUtil;
import aethereal.render.Draw2DProcessor;
import aethereal.render.Fonts;
import aethereal.setting.BindSetting;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import aethereal.util.ChatUtil;
import aethereal.util.CounterUtil;
import aethereal.util.ProjectUtil;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import net.minecraft.class_243;
import net.minecraft.class_2561;
import net.minecraft.class_2583;
import net.minecraft.class_2797;
import net.minecraft.class_2960;
import net.minecraft.class_5250;
import org.joml.Vector2f;

@ModuleRegister(
   a = "Communication",
   b = "Связывает вас с другими игроками через групповые и глобальные сообщения (party, IRC и др.)",
   c = Category.Misc
)
public class Communication extends Module implements Interface {
   private final BindSetting b = new BindSetting("Отправление метки друзьям", -1).a(() -> {
      JsonObject posObject = new JsonObject();
      posObject.addProperty("x", aM_.field_1724.method_19538().field_1352);
      posObject.addProperty("y", aM_.field_1724.method_19538().field_1351);
      posObject.addProperty("z", aM_.field_1724.method_19538().field_1350);
      Westra.h().f().a(false, "friend", "type", "mark", "pos", posObject);
   });
   private final BooleanSetting c = new BooleanSetting("Клиентский чат", false);
   private final List<Communication.a> d = new ArrayList<>();

   public Communication() {
      this.a(new Setting[]{this.b, this.c});
   }

   @EventTarget
   public void a(TickEvent event) {
      this.d.removeIf(mark -> mark.a().a(5000L));
   }

   @EventTarget
   public void a(PacketEvent event) {
      if (this.c.c() && event.b() && event.d() instanceof class_2797 class_2797VarD) {
         String content = class_2797VarD.comp_945();
         if (content.startsWith("@")) {
            Westra.h().f().a(false, "irc", "message", content.substring(1));
            event.a(true);
         }
      }
   }

   @EventTarget
   public void a(DrawEvent event) {
      if (event.b()) {
         for (Communication.a mark : this.d) {
            this.a(event, mark, aM_.field_1724.method_33571());
         }
      }
   }

   private void a(DrawEvent event, Communication.a mark, class_243 eyes) {
      String[] parts = mark.c().split(",\\s*");
      if (parts.length >= 3) {
         class_243 position = new class_243(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]), Double.parseDouble(parts[2]));
         Vector2f screen = ProjectUtil.a(position.field_1352, position.field_1351, position.field_1350);
         if (ProjectUtil.a(screen)) {
            ThemeProcessor theme = Westra.h().d().o();
            int primary = theme.a(ThemeInfo.PRIMARY).a();
            int background = ColorUtil.a(theme.a(ThemeInfo.BACKGROUND_HUD).a(), theme.a(ThemeInfo.BACKGROUND_HUD).b());
            class_2561 text = class_2561.method_43470(mark.b().toUpperCase(Locale.ROOT))
               .method_10852(class_2561.method_43470(" /  ").method_10862(class_2583.field_24360.method_36139(primary)))
               .method_10852(class_2561.method_43470(String.format(Locale.US, "%.1fм", eyes.method_1022(position))));
            float width = 16.5F + Fonts.e.a(text, 6.25F);
            float height = 12.0F;
            float x = screen.x() - width / 2.0F;
            float y = screen.y() - height / 2.0F;
            Draw2DProcessor draw = event.d();
            draw.a(event.h(), x, y, width - 0.5F, height, 3.5F, background, 1.0F, background, 6.0F);
            draw.a(event.h(), draw.c().b(mark.b()), null, x + 3.0F - 0.5F, y + 3.0F - 1.0F, 8.0F, 8.0F, 1.0F, 1.0F);
            Fonts.e.a(event.h(), text, x + 3.0F + 8.0F + 2.0F, y + (height - Fonts.e.a(6.25F)) / 2.0F - 0.5F, 6.25F);
         }
      }
   }

   @EventTarget
   public void a(BackendEvent event) {
      String payload = event.d().c();
      String type = event.d().a().a(payload, "type");
      String user = event.d().a().a(payload, "user");
      String message = event.d().a().a(payload, "message");
      String priority = event.d().a().a(payload, "priority");
      if ("irc".equals(event.d().b()) && this.c.c()) {
         Communication.Prefix prefix = Communication.Prefix.a(priority);
         class_5250 line = class_2561.method_43473();
         if (prefix != null) {
            line.method_10852(
                  class_2561.method_43470(prefix.a()).method_10862(class_2583.field_24360.method_27704(class_2960.method_60655("westra", "prefixes")))
               )
               .method_10852(class_2561.method_43470(""));
         }

         line.method_10852(ChatUtil.b("[" + user + "] → " + message));
         ChatUtil.a((Object)"[IRC]", (class_2561)line);
      }

      if ("friend".equals(event.d().b()) && "mark".equals(type)) {
         JsonObject pos = event.d().a().b(payload, "pos").getAsJsonObject();
         String position = String.format("%.0f, %.0f, %.0f", pos.get("x").getAsDouble(), pos.get("y").getAsDouble(), pos.get("z").getAsDouble());
         String login = event.d().a().a(payload, "minecraft");
         this.d.removeIf(mark -> mark.b().equalsIgnoreCase(login));
         this.d.add(new Communication.a(new CounterUtil(), login, position));
      }
   }

   public static enum Prefix {
      ADMIN("Администратор", "\ue100"),
      STAFF("Сотрудник", "\ue101"),
      YOUTUBER("Ютубер", "\ue102"),
      SHADE("shade", "\ue103"),
      DANGEROUS("dangerous", "\ue104"),
      DEVSTVENIK("девственник", "\ue105"),
      DRUN("друн", "\ue106"),
      QCOLD("qcold", "\ue107"),
      WIN("win", "\ue108"),
      BURMALDA("бурмалда", "\ue109"),
      VOZDUXAN("воздухан", "\ue110");

      private final String l;
      private final String m;

      private Prefix(String role, String glyph) {
         this.l = role;
         this.m = glyph;
      }

      public String a() {
         return this.m;
      }

      public static Communication.Prefix a(String role) {
         return Arrays.stream(values()).filter(prefix -> prefix.l.equalsIgnoreCase(role)).findFirst().orElse(null);
      }
   }

   public static final class a {
      private final CounterUtil a;
      private final String b;
      private final String c;

      public a(CounterUtil counterUtil, String login, String position) {
         this.a = counterUtil;
         this.b = login;
         this.c = position;
      }

      public CounterUtil a() {
         return this.a;
      }

      public String b() {
         return this.b;
      }

      public String c() {
         return this.c;
      }
   }
}
