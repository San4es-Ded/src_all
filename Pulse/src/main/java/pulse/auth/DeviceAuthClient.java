package pulse.auth;

import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.json.JSONObject;
import ru.pulse.Pulse;

public class DeviceAuthClient {
   private static final String c = "/auth/device/init";
   private static final String d = "/auth/device/poll";
   private static final int e = 1000;
   private static final int f = 300;
   private volatile boolean g = false;
   public static int a;
   public static boolean b;

   public String a() {
      try {
         String string = UUID.randomUUID().toString();
         Pulse.getLOGGER().info("Р—Р°РїСѓСЃРє Р°РІС‚РѕСЂРёР·Р°С†РёРё...");
         if (!this.a(string)) {
            Pulse.getLOGGER().error("РћС€РёР±РєР° РёРЅРёС†РёР°Р»РёР·Р°С†РёРё Р·Р°РїСЂРѕСЃР° Р°РІС‚РѕСЂРёР·Р°С†РёРё");
            return null;
         }

         this.b(string);
         String strC = this.c(string);
         if (strC != null) {
            Pulse.getLOGGER().info("РўРѕРєРµРЅ СѓСЃРїРµС€РЅРѕ РїРѕР»СѓС‡РµРЅ!");
         } else {
            Pulse.showMessage("РќРµ СѓРґР°Р»РѕСЃСЊ РїРѕР»СѓС‡РёС‚СЊ С‚РѕРєРµРЅ");
         }

         return strC;
      } catch (Exception e2) {
         Pulse.getLOGGER().error("РћС€РёР±РєР° Device Auth Flow", e2);
         return null;
      }
   }

   private boolean a(String str) {
      try {
         JSONObject jSONObjectD = this.d(Pulse.getDirectApiUrl() + "/auth/device/init?state=" + URLEncoder.encode(str, "UTF-8"));
         if (jSONObjectD != null && jSONObjectD.getBoolean("success")) {
            int i5 = jSONObjectD.getInt("expiresIn");
            Pulse.getLOGGER().info("Р—Р°РїСЂРѕСЃ Р°РІС‚РѕСЂРёР·Р°С†РёРё СЃРѕР·РґР°РЅ");
            Pulse.getLOGGER().info("РСЃС‚РµРєР°РµС‚ С‡РµСЂРµР· " + i5 / 60 + " РјРёРЅСѓС‚");
            return true;
         } else {
            return false;
         }
      } catch (Exception e2) {
         Pulse.getLOGGER().error("РћС€РёР±РєР° init request", e2);
         return false;
      }
   }

   private void b(String str) {
      String str2 = Pulse.getMainUrl() + "/auth/device?state=" + str;

      try {
         if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(new URI(str2));
         } else {
            String lowerCase = System.getProperty("os.name").toLowerCase();
            if (lowerCase.contains("win")) {
               Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + str2);
            } else if (lowerCase.contains("mac")) {
               Runtime.getRuntime().exec("open " + str2);
            } else {
               Runtime.getRuntime().exec("xdg-open " + str2);
            }
         }
      } catch (Exception e2) {
         Pulse.showMessage("РќРµ СѓРґР°Р»РѕСЃСЊ РѕС‚РєСЂС‹С‚СЊ Р±СЂР°СѓР·РµСЂ Р°РІС‚РѕРјР°С‚РёС‡РµСЃРєРё. РћС‚РєСЂРѕР№С‚Рµ СЃСЃС‹Р»РєСѓ РІСЂСѓС‡РЅСѓСЋ: " + str2);
         Pulse.getLOGGER().error("РќРµ СѓРґР°Р»РѕСЃСЊ РѕС‚РєСЂС‹С‚СЊ Р±СЂР°СѓР·РµСЂ Р°РІС‚РѕРјР°С‚РёС‡РµСЃРєРё");
         Pulse.getLOGGER().error("РћС‚РєСЂРѕР№С‚Рµ СЃСЃС‹Р»РєСѓ РІСЂСѓС‡РЅСѓСЋ: " + str2);
      }
   }

   private String c(String str) {
      int i = 0;
      boolean z = false;
      String str2 = Pulse.getMainUrl() + "/auth/device?state=" + str;
      Pulse.getLOGGER().info("РћР¶РёРґР°РЅРёРµ РїРѕРґС‚РІРµСЂР¶РґРµРЅРёСЏ РІ Р±СЂР°СѓР·РµСЂРµ...");

      while (i < 300 && !this.g) {
         try {
            if (++i >= 150 && !z) {
               z = true;
               String str3 = "Р’Р°Рј РЅРµРѕР±С…РѕРґРёРјРѕ РїСЂРѕР№С‚Рё Р°РІС‚РѕСЂРёР·Р°С†РёСЋ РІ Р±СЂР°СѓР·РµСЂРµ!\nР•СЃР»Рё СЃСЃС‹Р»РєР° РЅРµ РѕС‚РєСЂС‹Р»Р°СЃСЊ Р°РІС‚РѕРјР°С‚РёС‡РµСЃРєРё, РѕС‚РєСЂРѕР№С‚Рµ РµС‘ РІСЂСѓС‡РЅСѓСЋ:\n" + str2;
               new Thread(() -> Pulse.showMessage(str3)).start();
            }

            if (i % 30 == 0) {
               int i3 = ((300 ^ i) - 2 * (-301 & i)) * 1;
               Pulse.getLOGGER().info("Р’СЃРµ РµС‰Рµ РѕР¶РёРґР°РµРј РїРѕРґС‚РІРµСЂР¶РґРµРЅРёСЏ... (" + i3 / 60 + " РјРёРЅ " + i3 % 60 + " СЃРµРє РґРѕ С‚Р°Р№РјР°СѓС‚Р°)");
            }

            JSONObject jSONObjectD = this.d(Pulse.getDirectApiUrl() + "/auth/device/poll?state=" + URLEncoder.encode(str, "UTF-8"));
            if (jSONObjectD == null) {
               Thread.sleep(1000L);
            } else if (jSONObjectD.getBoolean("success")) {
               String string = jSONObjectD.getString("status");
               if ("confirmed".equals(string)) {
                  return jSONObjectD.getString("token");
               }

               if ("expired".equals(string)) {
                  Pulse.showMessage("Р—Р°РїСЂРѕСЃ Р°РІС‚РѕСЂРёР·Р°С†РёРё РёСЃС‚РµРє (РїСЂРѕС€Р»Рѕ Р±РѕР»РµРµ 15 РјРёРЅСѓС‚)");
                  return null;
               }

               if ("not_found".equals(string)) {
                  Pulse.showMessage("Р—Р°РїСЂРѕСЃ Р°РІС‚РѕСЂРёР·Р°С†РёРё РЅРµ РЅР°Р№РґРµРЅ");
                  return null;
               }

               if ("cancelled".equals(string)) {
                  Pulse.showMessage("РђРІС‚РѕСЂРёР·Р°С†РёСЏ РѕС‚РјРµРЅРµРЅР° РїРѕР»СЊР·РѕРІР°С‚РµР»РµРј");
                  return null;
               }

               Thread.sleep(1000L);
            } else {
               Thread.sleep(1000L);
            }
         } catch (InterruptedException e2) {
            Thread.currentThread().interrupt();
            return null;
         } catch (Exception e3) {
            Pulse.getLOGGER().error("РћС€РёР±РєР° polling: " + e3.getMessage());
         }
      }

      if (this.g) {
         Pulse.showMessage("РђРІС‚РѕСЂРёР·Р°С†РёСЏ РѕС‚РјРµРЅРµРЅР° РїРѕР»СЊР·РѕРІР°С‚РµР»РµРј");
         return null;
      } else {
         Pulse.showMessage("Р’СЂРµРјСЏ РѕР¶РёРґР°РЅРёСЏ Р°РІС‚РѕСЂРёР·Р°С†РёРё РёСЃС‚РµРєР»Рѕ! РџРµСЂРµР·Р°РїСѓСЃС‚РёС‚Рµ РєР»РёРµРЅС‚ Рё РїРѕРїСЂРѕР±СѓР№С‚Рµ СЃРЅРѕРІР°");
         return null;
      }
   }

   private JSONObject d(String str) {
      try {
         HttpURLConnection httpURLConnection = (HttpURLConnection)new URL(str).openConnection();
         httpURLConnection.setRequestMethod("GET");
         httpURLConnection.setConnectTimeout(5000);
         httpURLConnection.setReadTimeout(5000);
         if (httpURLConnection.getResponseCode() != 200) {
            return null;
         }

         InputStream inputStream = httpURLConnection.getInputStream();
         ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
         byte[] bArr = new byte[1024];

         while (true) {
            int i2 = inputStream.read(bArr);
            if (i2 == -1) {
               return new JSONObject(new String(byteArrayOutputStream.toByteArray(), StandardCharsets.UTF_8));
            }

            byteArrayOutputStream.write(bArr, 0, i2);
         }
      } catch (Exception e2) {
         return null;
      }
   }

   public void b() {
      this.g = true;
   }

   public static String a(String str, String str2, int i, int i2, int i3, int i4) {
      return null;
   }
}
