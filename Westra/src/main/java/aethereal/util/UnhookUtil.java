package aethereal.util;

import aethereal.core.Interface;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.ProcessBuilder.Redirect;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import net.fabricmc.loader.api.FabricLoader;

public final class UnhookUtil {
   private static final SecureRandom RANDOM = new SecureRandom();

   private UnhookUtil() {
   }

   public static List<Path> a() {
      List<Path> paths = new ArrayList<>();
      FabricLoader.getInstance().getModContainer("westra").ifPresent(container -> {
         for (Path path : container.getOrigin().getPaths()) {
            if (Files.isRegularFile(path) && path.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".jar")) {
               paths.add(path.toAbsolutePath());
            }
         }
      });
      return paths;
   }

   public static List<Path> b() {
      Set<Path> targets = new LinkedHashSet<>();
      File root = Interface.aM_.field_1697;
      if (root == null) {
         return new ArrayList<>(targets);
      } else {
         Path base = root.toPath().toAbsolutePath();
         c(targets, base.resolve("logs"));
         return new ArrayList<>(targets);
      }
   }

   private static void c(Set<Path> targets, Path path) {
      if (Files.exists(path)) {
         targets.add(path);
      }
   }

   public static boolean a(Path path) {
      path.toFile().deleteOnExit();
      return false;
   }

   private static void d(Path path) {
      try (RandomAccessFile file = new RandomAccessFile(path.toFile(), "rws")) {
         long length = file.length();
         byte[] buffer = new byte[65536];
         long written = 0L;

         while (written < length) {
            RANDOM.nextBytes(buffer);
            int chunk = (int)Math.min((long)buffer.length, length - written);
            file.write(buffer, 0, chunk);
            written += chunk;
         }

         file.getFD().sync();
      } catch (IOException var10) {
      }
   }

   public static void a(List<Path> targets) {
      if (!targets.isEmpty()) {
         boolean windows = System.getProperty("os.name", "").toLowerCase(Locale.ROOT).contains("win");

         try {
            File script = File.createTempFile("wu", windows ? ".ps1" : ".sh");
            script.deleteOnExit();
            Files.writeString(script.toPath(), windows ? a(targets, script) : b(targets, script), StandardCharsets.UTF_8);
            ProcessBuilder builder = windows
               ? new ProcessBuilder("powershell", "-NoProfile", "-ExecutionPolicy", "Bypass", "-WindowStyle", "Hidden", "-File", script.getAbsolutePath())
               : new ProcessBuilder("sh", script.getAbsolutePath());
            builder.redirectErrorStream(true);
            builder.redirectOutput(Redirect.DISCARD);
            builder.start();
         } catch (IOException var4) {
         }
      }
   }

   private static String a(List<Path> targets, File script) {
      StringBuilder list = new StringBuilder();

      for (Path target : targets) {
         if (list.length() > 0) {
            list.append(',');
         }

         list.append('\'').append(target.toString().replace("'", "''")).append('\'');
      }

      return "$ErrorActionPreference='SilentlyContinue'\n$targets=@("
         + list
         + ")\nfor($i=0;$i -lt 240;$i++){\n  $left=$false\n  foreach($t in $targets){\n    if(-not (Test-Path -LiteralPath $t)){continue}\n    try{\n      $files=@()\n      if(Test-Path -LiteralPath $t -PathType Container){$files=Get-ChildItem -LiteralPath $t -Recurse -File -Force}\n      else{$files=@(Get-Item -LiteralPath $t -Force)}\n      foreach($f in $files){\n        try{\n          $fs=[System.IO.File]::Open($f.FullName,'Open','Write','None')\n          $len=$fs.Length\n          if($len -gt 0){\n            $buf=New-Object byte[] 65536\n            $rng=[System.Security.Cryptography.RandomNumberGenerator]::Create()\n            $w=0\n            while($w -lt $len){$rng.GetBytes($buf);$c=[Math]::Min($buf.Length,$len-$w);$fs.Write($buf,0,$c);$w+=$c}\n          }\n          $fs.Flush($true);$fs.Close()\n        }catch{}\n      }\n      Remove-Item -LiteralPath $t -Recurse -Force\n    }catch{}\n    if(Test-Path -LiteralPath $t){$left=$true}\n  }\n  if(-not $left){break}\n  Start-Sleep -Milliseconds 500\n}\nRemove-Item -LiteralPath '"
         + script.getAbsolutePath().replace("'", "''")
         + "' -Force\n";
   }

   private static String b(List<Path> targets, File script) {
      StringBuilder body = new StringBuilder("#!/bin/sh\nfor i in $(seq 1 240); do\n  left=0\n");

      for (Path target : targets) {
         String path = target.toString();
         body.append("  if [ -e \"")
            .append(path)
            .append("\" ]; then\n")
            .append("    find \"")
            .append(path)
            .append("\" -type f -exec sh -c 'dd if=/dev/urandom of=\"$1\" bs=65536 conv=notrunc 2>/dev/null' _ {} \\;\n")
            .append("    rm -rf \"")
            .append(path)
            .append("\"\n")
            .append("    [ -e \"")
            .append(path)
            .append("\" ] && left=1\n")
            .append("  fi\n");
      }

      body.append("  [ $left -eq 0 ] && break\n  sleep 1\ndone\nrm -f \"").append(script.getAbsolutePath()).append("\"\n");
      return body.toString();
   }
}
