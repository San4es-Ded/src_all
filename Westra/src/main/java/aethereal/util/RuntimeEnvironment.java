package aethereal.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class RuntimeEnvironment {
   private static boolean b(String path) {
      return Files.exists(Paths.get(path));
   }

   public static Boolean a() {
      return a("");
   }

   static boolean a(String dirPrefix) {
      String value = a(dirPrefix + "/proc/1/environ", "container");
      return value != null ? !value.isEmpty() : b(dirPrefix + "/.dockerenv") || b(dirPrefix + "/run/.containerenv");
   }

   private static String a(String envVarFile, String key) {
      try {
         byte[] bytes = Files.readAllBytes(Paths.get(envVarFile));
         String content = new String(bytes, Charset.defaultCharset());
         String[] lines = content.split(String.valueOf('\u0000'));
         String prefix = key + "=";
         return Arrays.stream(lines)
            .filter(line -> line.startsWith(prefix))
            .map(line2 -> line2.split("=", 2))
            .map(keyValue -> (String)keyValue[1])
            .findFirst()
            .orElse(null);
      } catch (IOException var6) {
         return null;
      }
   }
}
