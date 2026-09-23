package rockstar.client.internal.script;





import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.internal.network.*;
import rockstar.client.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;
import pyrock.utility.render.ColorRGBA;
import pyrock.utility.render.CustomDrawContext;

public final class EmojiTextNode extends UiNode {
   private static final Pattern internalField0293 = Pattern.compile("\\X");
   private static final Map<String, EmojiTextNode.InternalType0325> internalField0543 = new HashMap<>();
   private static final EmojiTextNode.InternalType0325 internalField0482 = new EmojiTextNode.InternalType0325(
      RockstarClient.id("textures/emoji/peace.png")
   );
   private final SizedFont internalField0447;
   private final Supplier<String> internalField0017;
   private final Supplier<ColorRGBA> internalField0018;
   private final UiNode.InternalType0353 internalField0334;

   public EmojiTextNode(SizedFont localValue1, Supplier<String> localValue2, Supplier<ColorRGBA> localValue3) {
      this(localValue1, localValue2, localValue3, () -> 0.0F);
   }

   public EmojiTextNode(SizedFont localValue1, Supplier<String> localValue2, Supplier<ColorRGBA> localValue3, UiNode.InternalType0353 localValue4) {
      this.internalField0447 = localValue1;
      this.internalField0017 = localValue2;
      this.internalField0018 = localValue3;
      this.internalField0334 = localValue4;
      this.interactive(false);
   }

   @Override
   public void measure() {
      String localValue1 = this.internalMethod03458();
      if (!this.explicitW) {
         this.prefW = internalMethod06011(this.internalField0447, localValue1);
      }

      if (!this.explicitH) {
         this.prefH = Math.max(this.internalField0447.internalMethod04890(), internalMethod03930(this.internalField0447));
      }
   }

   @Override
   public void drawSelf(UiRenderContext localValue1, float localValue2) {
      String localValue3 = this.internalMethod03458();
      if (!localValue3.isEmpty()) {
         internalMethod06314(
            localValue1,
            this.internalField0447,
            localValue3,
            this.x() + this.internalField0334.get(),
            this.y(),
            this.h(),
            this.internalField0018 == null ? ColorRGBA.WHITE : this.internalField0018.get()
         );
      }
   }

   public static float internalMethod06011(SizedFont localValue0, String localValue1) {
      if (localValue1 != null && !localValue1.isEmpty()) {
         Matcher localValue2 = internalField0293.matcher(localValue1);
         float localValue3 = 0.0F;

         while (localValue2.find()) {
            String localValue4 = localValue2.group();
            localValue3 += internalMethod04049(localValue4)
               ? internalMethod03930(localValue0) + FontFamily.internalMethod04944(localValue0.internalMethod07850())
               : localValue0.internalMethod00965(localValue4);
         }

         return localValue3;
      } else {
         return 0.0F;
      }
   }

   public static boolean internalMethod05608(String localValue0) {
      if (localValue0 != null && !localValue0.isEmpty()) {
         Matcher localValue1 = internalField0293.matcher(localValue0);

         while (localValue1.find()) {
            if (internalMethod04049(localValue1.group())) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public static void internalMethod06314(CustomDrawContext localValue0, SizedFont localValue1, String localValue2, float localValue3, float localValue4, float localValue5, ColorRGBA localValue6) {
      if (localValue2 != null && !localValue2.isEmpty() && localValue6 != null) {
         Matcher localValue7 = internalField0293.matcher(localValue2);
         float localValue8 = localValue3;

         while (localValue7.find()) {
            String localValue9 = localValue7.group();
            if (internalMethod04049(localValue9)) {
               float localValue10 = internalMethod03930(localValue1);
               internalMethod04204(localValue9).internalMethod03656(localValue0, localValue8, localValue4 + (localValue5 - localValue10) / 2.0F, localValue10, localValue6.getAlpha());
               localValue8 += localValue10 + FontFamily.internalMethod04944(localValue1.internalMethod07850());
            } else {
               localValue0.drawText(localValue1, localValue9, localValue8, localValue4 + (localValue5 - localValue1.internalMethod04890()) / 2.0F, localValue6);
               localValue8 += localValue1.internalMethod00965(localValue9);
            }
         }
      }
   }

   private static float internalMethod03930(SizedFont localValue0) {
      return localValue0.internalMethod07850() * 1.4F;
   }

   private String internalMethod03458() {
      String localValue1 = this.internalField0017 == null ? "" : this.internalField0017.get();
      return localValue1 == null ? "" : localValue1;
   }

   private static boolean internalMethod04049(String localValue0) {
      return localValue0.codePoints()
         .anyMatch(
            localValue0x -> localValue0x == 169
               || localValue0x == 174
               || localValue0x == 8252
               || localValue0x == 8265
               || localValue0x == 8482
               || localValue0x == 8505
               || localValue0x == 12336
               || localValue0x == 12349
               || localValue0x == 12951
               || localValue0x == 12953
               || localValue0x >= 8960 && localValue0x <= 9215
               || localValue0x >= 9728 && localValue0x <= 10175
               || localValue0x >= 11008 && localValue0x <= 11263
               || localValue0x >= 126976 && localValue0x <= 129791
         );
   }

   private static EmojiTextNode.InternalType0325 internalMethod04204(String localValue0) {
      if (!localValue0.equals("\u270c") && !localValue0.equals("\u270c\ufe0f")) {
         EmojiTextNode.InternalType0325 localValue1 = internalField0543.get(localValue0);
         if (localValue1 != null) {
            return localValue1;
         } else {
            Integer localValue2 = EmojiAtlas.internalMethod02569(internalMethod01244(localValue0));
            if (localValue2 != null) {
               localValue1 = EmojiTextNode.InternalType0325.internalMethod01663(localValue2);
               internalField0543.put(localValue0, localValue1);
               return localValue1;
            } else {
               BufferedImage localValue3 = new BufferedImage(96, 96, 2);
               Graphics2D localValue4 = localValue3.createGraphics();
               localValue4.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
               localValue4.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
               localValue4.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
               Font localValue5 = internalMethod00308(localValue0);
               localValue4.setFont(localValue5);
               localValue4.setPaint(internalMethod01134(localValue0));
               FontMetrics localValue6 = localValue4.getFontMetrics(localValue5);
               int localValue7 = localValue6.stringWidth(localValue0);
               localValue4.drawString(localValue0, (96 - localValue7) / 2.0F, (96 - localValue6.getHeight()) / 2.0F + localValue6.getAscent());
               localValue4.dispose();
               Identifier localValue8 = Identifier.of(
                  RockstarClient.internalField1077, "dynamic_island/emoji/" + UUID.nameUUIDFromBytes(localValue0.getBytes(StandardCharsets.UTF_8))
               );
               MinecraftClient.getInstance()
                  .getTextureManager()
                  .registerTexture(localValue8, new NativeImageBackedTexture(() -> "Rockstar script download", WebImageUtils.internalMethod04372(localValue3, false)));
               localValue1 = new EmojiTextNode.InternalType0325(localValue8);
               internalField0543.put(localValue0, localValue1);
               return localValue1;
            }
         }
      } else {
         return internalField0482;
      }
   }

   private static String internalMethod01244(String localValue0) {
      StringBuilder localValue1 = new StringBuilder();
      localValue0.codePoints().filter(localValue0x -> localValue0x != 65038 && localValue0x != 65039).forEach(localValue1x -> {
         if (!localValue1.isEmpty()) {
            localValue1.append('-');
         }

         localValue1.append(Integer.toHexString(localValue1x));
      });
      return localValue1.toString();
   }

   private static Font internalMethod00308(String localValue0) {
      for (String localValue4 : new String[]{"Apple Color Emoji", "Segoe UI Emoji", "Noto Color Emoji"}) {
         Font localValue5 = new Font(localValue4, 0, 76);
         if (localValue5.canDisplay(localValue0.codePointAt(0))) {
            return localValue5;
         }
      }

      return new Font("Dialog", 0, 76);
   }

   private static GradientPaint internalMethod01134(String localValue0) {
      boolean localValue1 = localValue0.codePoints().anyMatch(localValue0x -> localValue0x == 10084 || localValue0x >= 128147 && localValue0x <= 128159);
      return localValue1
         ? new GradientPaint(12.0F, 12.0F, new Color(255, 115, 125), 84.0F, 84.0F, new Color(221, 38, 63))
         : new GradientPaint(12.0F, 12.0F, new Color(255, 235, 112), 84.0F, 84.0F, new Color(255, 157, 30));
   }

   static final class InternalType0325 {
      private final Identifier internalField0354;
      private final int internalField0227;

      InternalType0325(Identifier localValue1) {
         this(localValue1, -1);
      }

      private InternalType0325(Identifier localValue1, int localValue2) {
         this.internalField0354 = localValue1;
         this.internalField0227 = localValue2;
      }

      static EmojiTextNode.InternalType0325 internalMethod01663(int localValue0) {
         return new EmojiTextNode.InternalType0325(null, localValue0);
      }

      void internalMethod03656(CustomDrawContext localValue1, float localValue2, float localValue3, float localValue4, float localValue5) {
         if (this.internalField0227 >= 0) {
            EmojiAtlas.internalMethod02186(localValue1, this.internalField0227, localValue2, localValue3, localValue4, localValue5);
         } else {
            localValue1.drawTexture(this.internalField0354, localValue2, localValue3, localValue4, localValue4, ColorRGBA.WHITE.withAlpha(localValue5));
         }
      }

      @Override
      public final String toString() {
         return "InternalType0325[id=" + this.internalField0354 + ", index=" + this.internalField0227 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0354);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         EmojiTextNode.InternalType0325 other = (EmojiTextNode.InternalType0325) localValue1;
         return java.util.Objects.equals(this.internalField0354, other.internalField0354)
            && java.util.Objects.equals(this.internalField0227, other.internalField0227);
      }

      public Identifier internalMethod01268() {
         return this.internalField0354;
      }

      public int internalMethod06607() {
         return this.internalField0227;
      }
   }
}
