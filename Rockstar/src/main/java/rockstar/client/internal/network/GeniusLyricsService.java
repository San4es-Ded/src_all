package rockstar.client.internal.network;



import rockstar.client.i18n.*;
import rockstar.client.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeniusLyricsService {
   private static final Logger internalField0572 = LoggerFactory.getLogger("rockstar-lyrics");
   private static final String internalField0248 = "https://genius.com/api/search/song?per_page=10&q=";
   public static String internalMethod04363(String localValue0, String localValue1) {
      if (localValue0 != null && localValue1 != null && !localValue0.isBlank() && !localValue1.isBlank()) {
         try {
            GeniusLyricsService.InternalType0311 localValue2 = internalMethod04494(localValue0, localValue1);
            if (localValue2 == null) {
               internalField0572.info("[lyrics] genius: {} - {} \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d", localValue0, localValue1);
               return null;
            }

            return internalMethod08423(localValue2.internalField0248);
         } catch (IOException localValue3) {
            internalField0572.warn("[lyrics] genius \u043d\u0435\u0434\u043e\u0441\u0442\u0443\u043f\u0435\u043d: {}", localValue3.toString());
         } catch (Exception localValue4) {
            internalField0572.warn("[lyrics] genius: {}", localValue4.toString());
         }

         return null;
      } else {
         return null;
      }
   }

   private static GeniusLyricsService.InternalType0311 internalMethod04494(String localValue0, String localValue1) throws IOException {
      LinkedHashSet localValue2 = new LinkedHashSet();
      localValue2.add(localValue0 + " " + localValue1);
      String localValue3 = internalMethod08602(localValue1);
      if (!localValue3.equals(localValue1)) {
         localValue2.add(localValue0 + " " + localValue3);
      }

      GeniusLyricsService.InternalType0311 localValue4 = null;
      int localValue5 = -1;

      for (String localValue7 : (Iterable<String>)(Iterable<?>)localValue2) {
         JsonObject localValue8 = internalMethod04630("https://genius.com/api/search/song?per_page=10&q=" + internalMethod08811(localValue7));

         for (JsonObject localValue10 : internalMethod05720(localValue8)) {
            int localValue11 = internalMethod05408(localValue10, localValue0, localValue1);
            String localValue12 = internalMethod03760(localValue10, "url");
            if (!localValue12.isBlank() && localValue11 > localValue5) {
               localValue5 = localValue11;
               localValue4 = new GeniusLyricsService.InternalType0311(localValue12);
            }
         }

         if (localValue5 >= 250) {
            break;
         }
      }

      return localValue5 >= 100 ? localValue4 : null;
   }

   private static JsonObject internalMethod04630(String localValue0) throws IOException {
      HttpDownloadUtils.InternalType0449 localValue1 = HttpDownloadUtils.internalMethod02390(
         localValue0,
         "User-Agent",
         "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36",
         "Accept-Language",
         "ru,en-US;q=0.9,en;q=0.8",
         "Accept",
         "application/json, text/plain, */*",
         "Referer",
         "https://genius.com/"
      );
      if (!localValue1.internalMethod02740()) {
         return null;
      } else {
         try {
            return JsonParser.parseString(localValue1.internalMethod00694()).getAsJsonObject();
         } catch (Exception localValue3) {
            return null;
         }
      }
   }

   private static List<JsonObject> internalMethod05720(JsonObject localValue0) {
      ArrayList localValue1 = new ArrayList();
      if (localValue0 == null) {
         return localValue1;
      } else {
         JsonObject localValue2 = localValue0.getAsJsonObject("response");
         if (localValue2 == null) {
            return localValue1;
         } else {
            internalMethod05517(localValue2.getAsJsonArray("hits"), localValue1);
            JsonArray localValue3 = localValue2.getAsJsonArray("sections");
            if (localValue3 != null) {
               for (JsonElement localValue5 : localValue3) {
                  if (localValue5.isJsonObject()) {
                     internalMethod05517(localValue5.getAsJsonObject().getAsJsonArray("hits"), localValue1);
                  }
               }
            }

            return localValue1;
         }
      }
   }

   private static void internalMethod05517(JsonArray localValue0, List<JsonObject> localValue1) {
      if (localValue0 != null) {
         for (JsonElement localValue3 : localValue0) {
            if (localValue3.isJsonObject()) {
               JsonObject localValue4 = localValue3.getAsJsonObject().getAsJsonObject("result");
               if (localValue4 != null) {
                  localValue1.add(localValue4);
               }
            }
         }
      }
   }

   private static int internalMethod05408(JsonObject localValue0, String localValue1, String localValue2) {
      String localValue3 = internalMethod04426(localValue2);
      String localValue4 = internalMethod04426(internalMethod03760(localValue0, "title"));
      if (!localValue3.isEmpty() && !localValue4.isEmpty()) {
         short localValue5;
         if (localValue4.equals(localValue3)) {
            localValue5 = 160;
         } else if (!localValue4.startsWith(localValue3) && !localValue4.contains(localValue3)) {
            if (!localValue3.contains(localValue4)) {
               return -1;
            }

            localValue5 = 90;
         } else {
            localValue5 = 125;
         }

         String localValue6 = internalMethod03760(localValue0, "artist_names");
         if (localValue6.isBlank()) {
            JsonObject localValue7 = localValue0.getAsJsonObject("primary_artist");
            localValue6 = internalMethod03760(localValue7, "name");
         }

         String localValue10 = internalMethod04426(localValue1);
         String localValue8 = internalMethod04426(localValue6);
         short localValue9 = 0;
         if (!localValue10.isEmpty() && localValue8.equals(localValue10)) {
            localValue9 = 140;
         } else if (!localValue10.isEmpty() && (localValue8.contains(localValue10) || localValue10.contains(localValue8))) {
            localValue9 = 100;
         }

         if (localValue8.startsWith("genius ") && localValue9 == 0) {
            localValue9 -= 100;
         }

         return localValue5 + localValue9;
      } else {
         return -1;
      }
   }

   private static String internalMethod03760(JsonObject localValue0, String localValue1) {
      if (localValue0 != null && localValue0.has(localValue1) && !localValue0.get(localValue1).isJsonNull()) {
         try {
            return localValue0.get(localValue1).getAsString();
         } catch (Exception localValue3) {
            return "";
         }
      } else {
         return "";
      }
   }

   private static String internalMethod04426(String localValue0) {
      if (localValue0 == null) {
         return "";
      } else {
         String localValue1 = Normalizer.normalize(localValue0, Form.NFKD)
            .replaceAll("\\p{M}+", "")
            .toLowerCase(Locale.ROOT)
            .replace('\u0451', '\u0435')
            .replaceAll("[^\\p{L}\\p{N}]+", " ")
            .trim();
         return localValue1.replaceAll("\\s+", " ");
      }
   }

   private static String internalMethod08602(String localValue0) {
      return localValue0 == null
         ? ""
         : localValue0.replaceAll("(?i)\\s*[\\[(](feat\\.?|ft\\.?|featuring|prod\\.?|with)\\b.*?[\\])]", "")
            .replaceAll("(?i)\\s*[-\u2013\u2014]\\s*(remaster(?:ed)?|live|radio edit|single version|official audio).*$", "")
            .trim();
   }

   private static String internalMethod08811(String localValue0) {
      return URLEncoder.encode(localValue0, StandardCharsets.UTF_8);
   }

   private static String internalMethod08423(String localValue0) throws IOException {
      HttpDownloadUtils.InternalType0449 localValue1 = HttpDownloadUtils.internalMethod02390(
         localValue0,
         "User-Agent",
         "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36",
         "Accept-Language",
         "ru,en-US;q=0.9,en;q=0.8",
         "Accept",
         "text/html,application/xhtml+xml"
      );
      return localValue1.internalMethod02740() ? internalMethod03332(localValue1.internalMethod00694()) : null;
   }

   public static String internalMethod03332(String localValue0) {
      if (localValue0 != null && !localValue0.isBlank()) {
         Document localValue1 = Jsoup.parse(localValue0);
         Elements localValue2 = localValue1.select("[data-lyrics-container=true]");
         if (localValue2.isEmpty()) {
            localValue2 = localValue1.select("div[class*=Lyrics__Container]");
         }

         StringBuilder localValue3 = new StringBuilder();

         for (Element localValue5 : localValue2) {
            Element localValue6 = localValue5.clone();
            localValue6.select("[data-exclude-from-selection], button, script, style, svg").remove();

            for (Element localValue8 : localValue6.select("br")) {
               localValue8.after(new TextNode("\n"));
               localValue8.remove();
            }

            String localValue9 = internalMethod08629(localValue6.wholeText());
            if (!localValue9.isBlank()) {
               if (!localValue3.isEmpty()) {
                  localValue3.append("\n\n");
               }

               localValue3.append(localValue9);
            }
         }

         return localValue3.isEmpty() ? null : localValue3.toString();
      } else {
         return null;
      }
   }

   private static String internalMethod08629(String localValue0) {
      String[] localValue1 = localValue0.replace('\u00a0', ' ').replace("\r", "").split("\n", -1);
      StringBuilder localValue2 = new StringBuilder();
      boolean localValue3 = true;

      for (String localValue7 : localValue1) {
         String localValue8 = localValue7.strip().replaceAll("[ \\t]+", " ");
         if (localValue8.isEmpty()) {
            if (!localValue3 && !localValue2.isEmpty()) {
               localValue2.append('\n');
               localValue3 = true;
            }
         } else {
            if (!localValue2.isEmpty() && !localValue3) {
               localValue2.append('\n');
            }

            localValue2.append(localValue8);
            localValue3 = false;
         }
      }

      return localValue2.toString().strip();
   }

   static final class InternalType0311 {
      final String internalField0248;

      InternalType0311(String localValue1) {
         this.internalField0248 = localValue1;
      }

      @Override
      public final String toString() {
         return "InternalType0311[url=" + this.internalField0248 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0248);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         GeniusLyricsService.InternalType0311 other = (GeniusLyricsService.InternalType0311) localValue1;
         return java.util.Objects.equals(this.internalField0248, other.internalField0248);
      }

      public String internalMethod03530() {
         return this.internalField0248;
      }
   }
}
