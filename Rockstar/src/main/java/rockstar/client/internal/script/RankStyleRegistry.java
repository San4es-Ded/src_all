package rockstar.client.internal.script;


import rockstar.client.*;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.text.Style;
import pyrock.utility.render.ColorRGBA;

public class RankStyleRegistry {
   private static final Map<Integer, String> internalField0543 = new HashMap<>();
   private static final Map<Integer, Integer> internalField0544 = new HashMap<>();
   private static final Map<String, String> internalField1197 = new HashMap<>();
   private static final Map<String, Integer> internalField1196 = new HashMap<>();
   private static final int[] internalField0618 = new int[]{
      42240,
      42244,
      42248,
      42258,
      42262,
      42272,
      42276,
      42280,
      42336,
      42290,
      42294,
      42308,
      42326,
      42312,
      42304,
      42322,
      42249,
      42259,
      42263,
      42273,
      42277,
      42281,
      42291,
      42295,
      42241,
      42245
   };

   public static String internalMethod01705(int localValue0) {
      return internalField0543.get(localValue0);
   }

   public static boolean internalMethod05046(int localValue0) {
      for (int localValue4 : internalField0618) {
         if (localValue4 == localValue0) {
            return true;
         }
      }

      return false;
   }

   public static String internalMethod03838(String localValue0) {
      return internalField1197.getOrDefault(localValue0, localValue0);
   }

   public static void internalMethod05027(String localValue0, String localValue1) {
      internalField1197.put(localValue0, localValue1);
   }

   public static boolean internalMethod00097(int localValue0, Style localValue1) {
      if (localValue0 == 97 && localValue1 != null && localValue1.getFont() != null) {
         String localValue2 = localValue1.getFont().toString();
         return internalField1197.containsKey(localValue2);
      } else {
         return false;
      }
   }

   public static String internalMethod04684(Style localValue0) {
      if (localValue0 != null && localValue0.getFont() != null) {
         String localValue1 = localValue0.getFont().toString();
         return internalField1197.get(localValue1);
      } else {
         return null;
      }
   }

   public static int internalMethod02778(String localValue0) {
      return internalField1196.getOrDefault(localValue0, -1);
   }

   public static int internalMethod03766(String localValue0, float localValue1) {
      int localValue2 = internalMethod02778(localValue0);
      return localValue2 == -1 ? -1 : ColorRGBA.applyOpacity(localValue2, localValue1).getRGB();
   }

   public static int internalMethod03418(int localValue0, int localValue1, int localValue2, float localValue3, int localValue4) {
      boolean localValue5 = false;

      for (int localValue9 : internalField0618) {
         if (localValue9 == localValue0) {
            localValue5 = true;
            break;
         }
      }

      Integer localValue10 = localValue5 ? internalField0544.get(localValue0) : null;
      if (localValue10 == null) {
         return ColorRGBA.applyOpacity(localValue4, localValue3).getRGB();
      } else {
         float localValue11 = localValue2 > 1 ? (float)localValue1 / (localValue2 - 1) : 0.0F;
         ColorRGBA localValue12 = ColorRGBA.fromInt(localValue10).mix(ColorRGBA.darken(localValue10, 0.8F), localValue11);
         return ColorRGBA.applyOpacity(localValue12.getRGB(), localValue3).getRGB();
      }
   }

   static {
      internalField0543.put(9889, "");
      internalField0543.put(9733, "");
      internalField0543.put(42240, "PLAYER");
      internalField0543.put(42244, "HERO");
      internalField0543.put(42248, "TITAN");
      internalField0543.put(42258, "AVENGER");
      internalField0543.put(42262, "OVERLORD");
      internalField0543.put(42272, "MAGISTER");
      internalField0543.put(42276, "IMPERATOR");
      internalField0543.put(42280, "DRAGON");
      internalField0543.put(42336, "D.HELPER");
      internalField0543.put(42290, "BULL");
      internalField0543.put(42294, "TIGER");
      internalField0543.put(42308, "DRACULA");
      internalField0543.put(42326, "BUNNY");
      internalField0543.put(42312, "COBRA");
      internalField0543.put(42304, "HYDRA");
      internalField0543.put(42322, "RABBIT");
      internalField0543.put(42249, "HELPER");
      internalField0543.put(42259, "ML.MODER");
      internalField0543.put(42263, "MODER");
      internalField0543.put(42273, "MODER+");
      internalField0543.put(42277, "ST.MODER");
      internalField0543.put(42281, "GL.MODER");
      internalField0543.put(42291, "ML.ADMIN");
      internalField0543.put(42295, "ADMIN");
      internalField0543.put(42241, "MEDIA");
      internalField0543.put(42245, "YT");
      internalField0543.put(1171, "F");
      internalField0543.put(1109, "S");
      internalField0543.put(42927, "Q");
      internalField0543.put(7424, "A");
      internalField0543.put(665, "B");
      internalField0543.put(7428, "C");
      internalField0543.put(7429, "D");
      internalField0543.put(7431, "E");
      internalField0543.put(42800, "F");
      internalField0543.put(610, "G");
      internalField0543.put(668, "H");
      internalField0543.put(618, "I");
      internalField0543.put(7434, "J");
      internalField0543.put(7435, "K");
      internalField0543.put(671, "L");
      internalField0543.put(7437, "M");
      internalField0543.put(628, "N");
      internalField0543.put(7439, "O");
      internalField0543.put(7448, "P");
      internalField0543.put(491, "Q");
      internalField0543.put(640, "R");
      internalField0543.put(7451, "T");
      internalField0543.put(7452, "U");
      internalField0543.put(42801, "S");
      internalField0543.put(7456, "V");
      internalField0543.put(7457, "W");
      internalField0543.put(7521, "X");
      internalField0543.put(655, "Y");
      internalField0543.put(7458, "Z");
      internalField1197.put("custom:groups/hydra", "\u0413\u0438\u0434\u0440\u0430");
      internalField1197.put("custom:groups/cerberus", "\u0426\u0435\u0440\u0431\u0435\u0440");
      internalField1197.put("custom:groups/triton", "\u0422\u0440\u0438\u0442\u043e\u043d");
      internalField1197.put("custom:groups/phoenix", "\u0424\u0435\u043d\u0438\u043a\u0441");
      internalField1197.put("custom:groups/pandar", "\u041f\u0430\u043d\u0434\u0430\u0440");
      internalField1197.put("custom:groups/heat", "\u0416\u0430\u0440\u0430");
      internalField1197.put("custom:groups/cold", "\u0425\u043e\u043b\u043e\u0434");
      internalField1197.put("custom:groups/kronos", "\u041a\u0440\u043e\u043d\u043e\u0441");
      internalField1197.put("custom:groups/summer", "\u041b\u0435\u0442\u043e");
      internalField1197.put("custom:groups/winter", "\u0417\u0438\u043c\u0430");
      internalField1197.put("custom:groups/phobos", "\u0424\u043e\u0431\u043e\u0441");
      internalField1197.put("custom:groups/ares", "\u0410\u0440\u0435\u0441");
      internalField1197.put("custom:groups/aristocrat", "\u0410\u0440\u0438\u0441\u0442\u043e\u043a\u0440\u0430\u0442");
      internalField1197.put("custom:groups/youtuber", "\u042e\u0442\u0443\u0431\u0435\u0440");
      internalField1197.put("custom:groups/helper", "\u0425\u0435\u043b\u043f\u0435\u0440");
      internalField1197.put("custom:groups/shelper", "\u0421\u0442 \u0425\u0435\u043b\u043f\u0435\u0440");
      internalField1197.put("custom:groups/moder", "\u041c\u043e\u0434\u0435\u0440");
      internalField1197.put("custom:groups/smoder", "\u0421\u0442 \u041c\u043e\u0434\u0435\u0440");
      internalField1197.put("custom:groups/admin", "\u0410\u0434\u043c\u0438\u043d");
      internalField1197.put("custom:groups/default", "\u0418\u0433\u0440\u043e\u043a");
      internalField1196.put("custom:groups/aristocrat", new ColorRGBA(100.0F, 149.0F, 237.0F).getRGB());
      internalField1196.put("custom:groups/ares", new ColorRGBA(255.0F, 215.0F, 0.0F).getRGB());
      internalField1196.put("custom:groups/phobos", new ColorRGBA(255.0F, 165.0F, 0.0F).getRGB());
      internalField1196.put("custom:groups/kronos", new ColorRGBA(139.0F, 0.0F, 139.0F).getRGB());
      internalField1196.put("custom:groups/pandar", new ColorRGBA(255.0F, 0.0F, 0.0F).getRGB());
      internalField1196.put("custom:groups/phoenix", new ColorRGBA(187.0F, 0.0F, 0.0F).getRGB());
      internalField1196.put("custom:groups/triton", new ColorRGBA(173.0F, 216.0F, 230.0F).getRGB());
      internalField1196.put("custom:groups/cerberus", new ColorRGBA(0.0F, 255.0F, 0.0F).getRGB());
      internalField1196.put("custom:groups/hydra", new ColorRGBA(144.0F, 238.0F, 144.0F).getRGB());
      internalField1196.put("custom:groups/admin", new ColorRGBA(255.0F, 0.0F, 0.0F).getRGB());
      internalField1196.put("custom:groups/helper", new ColorRGBA(184.0F, 134.0F, 11.0F).getRGB());
      internalField1196.put("custom:groups/shelper", new ColorRGBA(255.0F, 215.0F, 0.0F).getRGB());
      internalField1196.put("custom:groups/moder", new ColorRGBA(0.0F, 0.0F, 255.0F).getRGB());
      internalField1196.put("custom:groups/smoder", new ColorRGBA(65.0F, 105.0F, 225.0F).getRGB());
      internalField1196.put("custom:groups/heat", new ColorRGBA(255.0F, 69.0F, 0.0F).getRGB());
      internalField1196.put("custom:groups/cold", new ColorRGBA(135.0F, 206.0F, 250.0F).getRGB());
      internalField1196.put("custom:groups/summer", new ColorRGBA(255.0F, 215.0F, 0.0F).getRGB());
      internalField1196.put("custom:groups/winter", new ColorRGBA(240.0F, 248.0F, 255.0F).getRGB());
      internalField1196.put("custom:groups/youtuber", new ColorRGBA(255.0F, 0.0F, 0.0F).getRGB());
      internalField1196.put("custom:groups/default", new ColorRGBA(255.0F, 255.0F, 255.0F).getRGB());
      internalField0544.put(42240, new ColorRGBA(120.0F, 120.0F, 120.0F).getRGB());
      internalField0544.put(42244, new ColorRGBA(100.0F, 113.0F, 251.0F).getRGB());
      internalField0544.put(42248, new ColorRGBA(214.0F, 200.0F, 42.0F).getRGB());
      internalField0544.put(42258, new ColorRGBA(101.0F, 189.0F, 56.0F).getRGB());
      internalField0544.put(42262, new ColorRGBA(64.0F, 151.0F, 214.0F).getRGB());
      internalField0544.put(42272, new ColorRGBA(202.0F, 130.0F, 60.0F).getRGB());
      internalField0544.put(42276, new ColorRGBA(202.0F, 60.0F, 60.0F).getRGB());
      internalField0544.put(42280, new ColorRGBA(245.0F, 51.0F, 238.0F).getRGB());
      internalField0544.put(42336, new ColorRGBA(214.0F, 200.0F, 42.0F).getRGB());
      internalField0544.put(42290, new ColorRGBA(121.0F, 81.0F, 202.0F).getRGB());
      internalField0544.put(42294, new ColorRGBA(202.0F, 130.0F, 60.0F).getRGB());
      internalField0544.put(42308, new ColorRGBA(202.0F, 60.0F, 60.0F).getRGB());
      internalField0544.put(42326, new ColorRGBA(68.0F, 65.0F, 66.0F).getRGB());
      internalField0544.put(42312, new ColorRGBA(127.0F, 214.0F, 86.0F).getRGB());
      internalField0544.put(42304, new ColorRGBA(92.0F, 120.0F, 7.0F).getRGB());
      internalField0544.put(42322, new ColorRGBA(120.0F, 120.0F, 120.0F).getRGB());
      internalField0544.put(42249, new ColorRGBA(214.0F, 200.0F, 42.0F).getRGB());
      internalField0544.put(42259, new ColorRGBA(100.0F, 113.0F, 251.0F).getRGB());
      internalField0544.put(42263, new ColorRGBA(100.0F, 113.0F, 251.0F).getRGB());
      internalField0544.put(42273, new ColorRGBA(121.0F, 81.0F, 202.0F).getRGB());
      internalField0544.put(42277, new ColorRGBA(100.0F, 113.0F, 251.0F).getRGB());
      internalField0544.put(42281, new ColorRGBA(121.0F, 81.0F, 202.0F).getRGB());
      internalField0544.put(42291, new ColorRGBA(64.0F, 151.0F, 214.0F).getRGB());
      internalField0544.put(42295, new ColorRGBA(202.0F, 60.0F, 60.0F).getRGB());
      internalField0544.put(42241, new ColorRGBA(121.0F, 81.0F, 202.0F).getRGB());
      internalField0544.put(42245, new ColorRGBA(255.0F, 255.0F, 255.0F).getRGB());
   }
}
