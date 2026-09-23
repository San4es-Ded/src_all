package wtf.wyvern.core.font;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

public class FormattedTextProcessor {
   public static List<TextSegment> processText(Text text, int defaultColor) {
      List<TextSegment> segments = new ArrayList<>(4);
      text.visit((style, string) -> {
         if (!string.isEmpty()) {
            appendLegacyAwareSegments(segments, string, style, defaultColor);
         }

         return Optional.empty();
      }, Style.EMPTY);
      return segments;
   }

   private static void appendLegacyAwareSegments(List<TextSegment> segments, String text,
                                                 Style style, int defaultColor) {
      int firstCode = text.indexOf(Formatting.FORMATTING_CODE_PREFIX);
      if (firstCode < 0) {
         segments.add(fromStyle(text, style, defaultColor));
         return;
      }

      int color = extractColor(style, defaultColor);
      boolean bold = style.isBold();
      boolean italic = style.isItalic();
      boolean underlined = style.isUnderlined();
      boolean strikethrough = style.isStrikethrough();
      int segmentStart = 0;

      for (int i = firstCode; i + 1 < text.length(); ++i) {
         if (text.charAt(i) != Formatting.FORMATTING_CODE_PREFIX) {
            continue;
         }

         Formatting formatting = Formatting.byCode(text.charAt(i + 1));
         if (formatting == null) {
            continue;
         }

         if (i > segmentStart) {
            segments.add(new TextSegment(text.substring(segmentStart, i), color,
                    bold, italic, underlined, strikethrough));
         }

         if (formatting == Formatting.RESET) {
            color = defaultColor;
            bold = false;
            italic = false;
            underlined = false;
            strikethrough = false;
         } else if (formatting.isColor()) {
            Integer rgb = formatting.getColorValue();
            if (rgb != null) {
               color = 0xFF000000 | rgb;
            }
            bold = false;
            italic = false;
            underlined = false;
            strikethrough = false;
         } else if (formatting == Formatting.BOLD) {
            bold = true;
         } else if (formatting == Formatting.ITALIC) {
            italic = true;
         } else if (formatting == Formatting.UNDERLINE) {
            underlined = true;
         } else if (formatting == Formatting.STRIKETHROUGH) {
            strikethrough = true;
         }

         i++;
         segmentStart = i + 1;
      }

      if (segmentStart < text.length()) {
         segments.add(new TextSegment(text.substring(segmentStart), color,
                 bold, italic, underlined, strikethrough));
      }
   }

   private static TextSegment fromStyle(String text, Style style, int defaultColor) {
      return new TextSegment(text, extractColor(style, defaultColor), style.isBold(),
              style.isItalic(), style.isUnderlined(), style.isStrikethrough());
   }

   private static int extractColor(Style style, int defaultColor) {
      TextColor textColor = style.getColor();
      return textColor != null ? textColor.getRgb() | -16777216 : defaultColor;
   }

   public static record TextSegment(String text, int color, boolean bold, boolean italic, boolean underlined, boolean strikethrough) {
      public TextSegment(String text, int color, boolean bold, boolean italic, boolean underlined, boolean strikethrough) {
         this.text = text;
         this.color = color;
         this.bold = bold;
         this.italic = italic;
         this.underlined = underlined;
         this.strikethrough = strikethrough;
      }

      public String text() {
         return this.text;
      }

      public int color() {
         return this.color;
      }

      public boolean bold() {
         return this.bold;
      }

      public boolean italic() {
         return this.italic;
      }

      public boolean underlined() {
         return this.underlined;
      }

      public boolean strikethrough() {
         return this.strikethrough;
      }
   }
}
