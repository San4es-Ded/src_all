package pulse.media.chat;

import java.awt.Color;
import lombok.Generated;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import pulse.client.MinecraftContext;
import pulse.module.ModuleRegistry;
import pulse.util.ColorUtils;
import ru.pulse.Pulse;

public final class ChatMessages implements MinecraftContext {
   private static final String e = "[pulse] ";
   public static int a;
   public static boolean b;

   public static void a(Text text) {
      if (c.player != null) {
         c.player.sendMessage(text, false);
      }
   }

   public static void a(String str) {
      if (c.player != null) {
         c.player.networkHandler.sendChatCommand(str);
      }
   }

   public static void a(Object obj) {
      if (obj == null) {
         obj = "null";
      }

      if (c.player == null) {
         Pulse.getLOGGER().info("(CHAT) " + obj);
      } else {
         c.inGameHud
            .getChatHud()
            .addMessage(
               b("[Pulse] ")
                  .append(Text.literal("> ").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(16777215))))
                  .append(Text.literal(String.valueOf(obj)).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(11184810))))
            );
      }
   }

   public static void b(Text text) {
      if (c.player != null) {
         c.inGameHud
            .getChatHud()
            .addMessage(
               b("[Pulse] ")
                  .append(Text.literal("> ").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(16777215))))
                  .append(text)
            );
      }
   }

   public static MutableText b(String str) {
      MutableText text = Text.literal("");
      Color color = new Color(a());
      Color endColor = a(color);
      int length = str.length();

      for (int i = 0; i < length; i++) {
         text.append(
            Text.literal(String.valueOf(str.charAt(i)))
               .setStyle(
                  Style.EMPTY
                     .withColor(TextColor.fromRgb(ColorUtils.a(color, endColor, length <= 1 ? 0.0F : i / ((length & -2) - (~length & 1))).getRGB()))
               )
         );
      }

      return text;
   }

   private static int a() {
      return ModuleRegistry.CLIENT_COLOR.o();
   }

   private static Color a(Color color) {
      float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
      return new Color(Color.HSBtoRGB(hsb[0], Math.max(0.0F, hsb[1] - 0.3F), Math.min(1.0F, hsb[2] + 0.2F)));
   }

   @Generated
   private ChatMessages() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static String b(String str, String str2, int i, int i2, int i3, int i4) {
      return null;
   }
}
