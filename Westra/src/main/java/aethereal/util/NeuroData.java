package aethereal.util;

import aethereal.core.Interface;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;

public class NeuroData implements Interface {
   private static final NeuroData a = new NeuroData();
   private final List<NeuroData.a> b = new ArrayList<>();
   private boolean c;
   private boolean d;

   @Generated
   public static NeuroData a() {
      return a;
   }

   @Generated
   public boolean b() {
      return this.c;
   }

   @Generated
   public int c() {
      return this.b.size();
   }

   public void d() {
      this.e();
      this.c = true;
   }

   public void f() {
      this.c = false;
      this.g();
   }

   public void h() {
      this.b.clear();
      this.i();
   }

   public void a(float deltaYaw, float deltaPitch, float distance, float yawStep, float pitchStep) {
      if (this.c) {
         if (!(Math.abs(yawStep) < 0.01F) || !(Math.abs(pitchStep) < 0.01F)) {
            if (this.b.size() < 20000) {
               this.b.add(new NeuroData.a(deltaYaw, deltaPitch, distance, yawStep, pitchStep));
            }
         }
      }
   }

   public float[] a(float deltaYaw, float deltaPitch, float distance) {
      this.e();
      if (this.b.isEmpty()) {
         return null;
      } else {
         NeuroData.a best = null;
         float bestScore = Float.MAX_VALUE;

         for (NeuroData.a sample : this.b) {
            float dy = (sample.a - deltaYaw) * 1.0F;
            float dp = (sample.b - deltaPitch) * 0.7F;
            float dd = (sample.c - distance) * 0.4F;
            float score = dy * dy + dp * dp + dd * dd;
            if (score < bestScore) {
               bestScore = score;
               best = sample;
            }
         }

         return best == null ? null : new float[]{best.d, best.e};
      }
   }

   private void e() {
      if (!this.d) {
         this.d = true;
         File file = j();
         if (file.exists()) {
            try {
               for (String line : Files.readAllLines(file.toPath(), StandardCharsets.UTF_8)) {
                  String[] parts = line.split(";");
                  if (parts.length == 5) {
                     this.b
                        .add(
                           new NeuroData.a(
                              Float.parseFloat(parts[0]),
                              Float.parseFloat(parts[1]),
                              Float.parseFloat(parts[2]),
                              Float.parseFloat(parts[3]),
                              Float.parseFloat(parts[4])
                           )
                        );
                  }
               }
            } catch (NumberFormatException | IOException var5) {
               this.b.clear();
            }
         }
      }
   }

   private void g() {
      StringBuilder builder = new StringBuilder();

      for (NeuroData.a sample : this.b) {
         builder.append(sample.a)
            .append(';')
            .append(sample.b)
            .append(';')
            .append(sample.c)
            .append(';')
            .append(sample.d)
            .append(';')
            .append(sample.e)
            .append('\n');
      }

      try {
         Path path = j().toPath();
         Files.createDirectories(path.getParent());
         Files.writeString(path, builder.toString(), StandardCharsets.UTF_8);
      } catch (IOException var4) {
      }
   }

   private void i() {
      File file = j();
      if (file.exists()) {
         file.delete();
      }
   }

   private static File j() {
      return new File(new File(aM_.field_1697, "configs\\general"), "neuro.csv");
   }

   private static final class a {
      private final float a;
      private final float b;
      private final float c;
      private final float d;
      private final float e;

      a(float deltaYaw, float deltaPitch, float distance, float yawStep, float pitchStep) {
         this.a = deltaYaw;
         this.b = deltaPitch;
         this.c = distance;
         this.d = yawStep;
         this.e = pitchStep;
      }
   }
}
