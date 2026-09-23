package aethereal.module.render;

import aethereal.command.BlockESPCommand;
import aethereal.config.ThemeInfo;
import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.DrawEvent;
import aethereal.event.TickEvent;
import aethereal.render.ColorUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_5562;
import net.minecraft.class_2338.class_2339;
import platform.inject.accessors.WorldAccessor;

@ModuleRegister(
   a = "Block ESP",
   b = "Подсвечивает добавленные вами блоки через .blockesp",
   c = Category.Render
)
public class BlockESP extends Module {
   private final List<class_2338> b = new CopyOnWriteArrayList<>();
   private ExecutorService c;
   private int d;

   @Override
   public void b() {
      super.b();
      this.b.clear();
      this.c = Executors.newSingleThreadExecutor();
      this.d = 0;
   }

   @Override
   public void c() {
      super.c();
      this.b.clear();
      if (this.c != null) {
         this.c.shutdownNow();
      }
   }

   @EventTarget
   public void a(TickEvent event) {
      int i = this.d + 1;
      this.d = i;
      if (i % 12 == 0) {
         List<BlockESPCommand.a> list = Westra.h().d().u().g().c();
         if (list.isEmpty()) {
            this.b.clear();
         } else {
            this.c.submit(() -> this.a(list.stream().map(v0 -> v0.a()).collect(Collectors.toSet())));
         }
      }
   }

   @EventTarget
   public void a(DrawEvent event) {
      if (event.c()) {
         List<BlockESPCommand.a> entries = Westra.h().d().u().g().c();
         if (!entries.isEmpty()) {
            Map<class_2248, Integer> colors = entries.stream().collect(Collectors.toMap(v0 -> v0.a(), v0 -> v0.b(), (first, second) -> second));

            for (class_5562 ticker : ((WorldAccessor)aM_.field_1687).getBlockEntityTickers()) {
               if (!ticker.method_31704()) {
                  this.a(event, ticker.method_31705(), colors);
               }
            }

            for (class_2338 pos : this.b) {
               this.a(event, pos, colors);
            }
         }
      }
   }

   private void a(DrawEvent event, class_2338 pos, Map<class_2248, Integer> colors) {
      Integer color = colors.get(aM_.field_1687.method_8320(pos).method_26204());
      if (color != null) {
         event.e().a(event.h(), new class_238(pos), color != -1 ? color : ColorUtil.a(Westra.h().d().o().a(ThemeInfo.PRIMARY).a(), 140), 1.5F);
      }
   }

   private void a(Set<class_2248> targets) {
      List<class_2338> found = new ArrayList<>();
      class_2338 center = aM_.field_1724.method_24515();
      class_2339 mutable = new class_2339();
      int maxY = aM_.field_1687.method_31600();

      for (int x = center.method_10263() - 70; x <= center.method_10263() + 70; x++) {
         for (int z = center.method_10260() - 70; z <= center.method_10260() + 70; z++) {
            if (aM_.field_1687.method_2935().method_12123(x >> 4, z >> 4)) {
               for (int y = aM_.field_1687.method_31607(); y < maxY; y++) {
                  if (targets.contains(aM_.field_1687.method_8320(mutable.method_10103(x, y, z)).method_26204())) {
                     found.add(mutable.method_10062());
                  }
               }
            }
         }
      }

      this.b.clear();
      this.b.addAll(found);
   }
}
