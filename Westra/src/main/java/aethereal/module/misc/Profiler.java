package aethereal.module.misc;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.TickEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import aethereal.util.ChatUtil;
import java.io.IOException;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.minecraft.class_1297;

@ModuleRegister(
   a = "Profiler",
   b = "Собирает причины просадок кадров и сохраняет отчёт при выключении",
   c = Category.Misc
)
public class Profiler extends Module implements Interface {
   private static final DateTimeFormatter b = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
   private static final int c = 20000;
   private static final int d2 = 40;
   private final SliderSetting e = new SliderSetting("Порог просадки", 2.5F, 1.5F, 8.0F, 0.1F);
   private final BooleanSetting f2 = new BooleanSetting("Сохранять отчёт", true);
   private final BooleanSetting g2 = new BooleanSetting("Сообщать о просадках", false);
   private final List<Float> h2 = new ArrayList<>();
   private final List<Profiler.a> i2 = new ArrayList<>();
   private long j2;
   private long k2;
   private long l2;
   private long m2;
   private long n2;
   private long o2;
   private float p2 = 16.7F;

   public Profiler() {
      this.a(new Setting[]{this.e, this.f2, this.g2});
   }

   @Override
   public void b() {
      this.h2.clear();
      this.i2.clear();
      this.j2 = System.nanoTime();
      this.k2 = System.currentTimeMillis();
      this.l2 = q();
      this.m2 = r();
      this.n2 = s();
      this.o2 = System.currentTimeMillis();
      this.p2 = 16.7F;
      super.b();
   }

   @Override
   public void c() {
      if (this.f2.c() && !this.h2.isEmpty()) {
         this.F();
      }

      super.c();
   }

   @EventTarget
   public void a(TickEvent event) {
      long now = System.nanoTime();
      float frame = (float)(now - this.j2) / 1000000.0F;
      this.j2 = now;
      if (!(frame <= 0.0F) && !(frame > 5000.0F)) {
         if (this.h2.size() < 20000) {
            this.h2.add(frame);
         }

         this.p2 = this.p2 + (frame - this.p2) * 0.02F;
         long gcCount = q();
         long gcTime = r();
         long heap = s();
         long wall = System.currentTimeMillis();
         long elapsed = Math.max(1L, wall - this.o2);
         long allocated = Math.max(0L, heap - this.n2);
         long allocRate = allocated * 1000L / elapsed;
         long gcPause = gcTime - this.m2;
         if (frame > this.p2 * this.e.c() && frame > 8.0F) {
            Profiler.a spike = new Profiler.a(
               frame, this.p2, this.t(gcCount - this.l2, gcPause, allocRate), this.u(), gcPause, allocRate, wall, this.v(), this.w(), this.x(), this.k2
            );
            this.i2.add(spike);
            if (this.i2.size() > 400) {
               this.i2.removeFirst();
            }

            if (this.g2.c()) {
               ChatUtil.a(String.format(Locale.ROOT, "Просадка %.1f мс (норма %.1f мс): %s.", frame, this.p2, spike.c()));
            }
         }

         this.l2 = gcCount;
         this.m2 = gcTime;
         this.n2 = heap;
         this.o2 = wall;
      }
   }

   private static long q() {
      long total = 0L;

      for (GarbageCollectorMXBean bean : ManagementFactory.getGarbageCollectorMXBeans()) {
         long count = bean.getCollectionCount();
         if (count > 0L) {
            total += count;
         }
      }

      return total;
   }

   private static long r() {
      long total = 0L;

      for (GarbageCollectorMXBean bean : ManagementFactory.getGarbageCollectorMXBeans()) {
         long time = bean.getCollectionTime();
         if (time > 0L) {
            total += time;
         }
      }

      return total;
   }

   private static long s() {
      Runtime runtime = Runtime.getRuntime();
      return runtime.totalMemory() - runtime.freeMemory();
   }

   private String t(long collections, long pause, long allocRate) {
      if (collections > 0L || pause > 4L) {
         return String.format(Locale.ROOT, "пауза сборщика ~%d мс, аллокации %s/с", pause, y(allocRate));
      } else if (allocRate > 209715200L) {
         return "высокая скорость аллокаций";
      } else {
         return this.v() > 400 ? "много сущностей в мире" : "нагрузка отрисовки";
      }
   }

   private String u() {
      return String.format(Locale.ROOT, "куча %s из %s", y(s()), y(Runtime.getRuntime().maxMemory()));
   }

   private int v() {
      if (aM_.field_1687 == null) {
         return 0;
      } else {
         int count = 0;

         for (class_1297 ignored : aM_.field_1687.method_18112()) {
            count++;
         }

         return count;
      }
   }

   private int w() {
      return aM_.field_1687 == null ? 0 : aM_.field_1687.method_2935().method_14151();
   }

   private int x() {
      try {
         for (Field field : aM_.field_1713.getClass().getDeclaredFields()) {
            if (Map.class.isAssignableFrom(field.getType())) {
               field.setAccessible(true);
               if (field.get(aM_.field_1713) instanceof Map<?, ?> map) {
                  int total = 0;

                  for (Object bucket : map.values()) {
                     if (bucket instanceof Collection<?> collection) {
                        total += collection.size();
                     }
                  }

                  return total;
               }
            }
         }

         return -1;
      } catch (Throwable var11) {
         return -1;
      }
   }

   private static String y(long bytes) {
      double mib = bytes / 1048576.0;
      return mib >= 1024.0 ? String.format(Locale.ROOT, "%.2f ГиБ", mib / 1024.0) : String.format(Locale.ROOT, "%.0f МиБ", mib);
   }

   private void F() {
      List<Float> sorted = new ArrayList<>(this.h2);
      sorted.sort(Comparator.naturalOrder());
      double sum = 0.0;

      for (Float frame : sorted) {
         sum += frame.floatValue();
      }

      int size = sorted.size();
      double average = sum / size;
      double median = sorted.get(size / 2).doubleValue();
      double low1 = sorted.get(Math.min(size - 1, (int)(size * 0.99))).doubleValue();
      double low01 = sorted.get(Math.min(size - 1, (int)(size * 0.999))).doubleValue();
      List<String> lines = new ArrayList<>();
      lines.add("Отчёт профилировщика Westra");
      lines.add("Собран: " + LocalDateTime.now());
      lines.add("");
      lines.add(
         String.format(
            Locale.ROOT,
            "Выборка: %d; среднее %.1f мс (%.0f FPS); мин. %.1f мс; макс. %.1f мс",
            size,
            average,
            1000.0 / average,
            sorted.getFirst(),
            sorted.getLast()
         )
      );
      lines.add(
         String.format(
            Locale.ROOT, "1%% low: %.0f FPS (%.1f мс); 0.1%% low: %.0f FPS (%.1f мс); медиана %.1f мс", 1000.0 / low1, low1, 1000.0 / low01, low01, median
         )
      );
      lines.add("");
      lines.add("Крупнейшие просадки:");
      List<Profiler.a> spikes = new ArrayList<>(this.i2);
      spikes.sort((first, second) -> Float.compare(second.a(), first.a()));
      int shown = Math.min(40, spikes.size());
      if (shown == 0) {
         lines.add("- просадок не зафиксировано");
      }

      for (int index = 0; index < shown; index++) {
         Profiler.a spike = spikes.get(index);
         long secondsBefore = Math.max(0L, (System.currentTimeMillis() - spike.g()) / 1000L);
         lines.add(String.format(Locale.ROOT, "- %.1f мс (норма %.1f мс), %s, за %d с до выключения", spike.a(), spike.b(), spike.c(), secondsBefore));
         lines.add(
            String.format(
               Locale.ROOT,
               "  %s; пауза сборщика %d мс; аллокации %s/с; сущности %d; чанки %d; частицы %s",
               spike.d(),
               spike.e(),
               y(spike.f()),
               spike.h(),
               spike.i(),
               spike.j() < 0 ? "недоступно" : String.valueOf(spike.j())
            )
         );
      }

      try {
         Path path = Paths.get("westra-profiler-" + LocalDateTime.now().format(b) + ".txt");
         Files.writeString(path, String.join(System.lineSeparator(), lines) + System.lineSeparator(), StandardCharsets.UTF_8);
         ChatUtil.a("Отчёт профилировщика сохранён: " + path.toAbsolutePath() + ".");
      } catch (IOException var20) {
         ChatUtil.a("Не удалось сохранить отчёт профилировщика: " + var20.getMessage() + ".");
      }
   }

   private record a(float a, float b, String c, String d, long e, long f, long g, int h, int i, int j, long k) {
   }
}
