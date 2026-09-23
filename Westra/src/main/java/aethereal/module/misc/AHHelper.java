package aethereal.module.misc;

import aethereal.config.DescriptionProcessor;
import aethereal.config.EnchantmentProcessor;
import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.ContainerEvent;
import aethereal.event.PacketEvent;
import aethereal.event.TooltipEvent;
import aethereal.render.ColorUtil;
import aethereal.render.Fonts;
import aethereal.setting.BooleanSetting;
import aethereal.setting.ModeSetting;
import aethereal.setting.MultiModeSetting;
import aethereal.setting.Setting;
import aethereal.util.ChatUtil;
import aethereal.util.ServerUtil;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;
import net.minecraft.class_10192;
import net.minecraft.class_124;
import net.minecraft.class_1304;
import net.minecraft.class_1735;
import net.minecraft.class_1738;
import net.minecraft.class_1799;
import net.minecraft.class_1810;
import net.minecraft.class_1829;
import net.minecraft.class_1893;
import net.minecraft.class_2561;
import net.minecraft.class_2583;
import net.minecraft.class_2649;
import net.minecraft.class_2653;
import net.minecraft.class_2813;
import net.minecraft.class_476;
import net.minecraft.class_5250;
import net.minecraft.class_9334;
import platform.inject.accessors.ClickSlotC2SPacketAccessor;
import platform.inject.accessors.HandledScreenAccessor;
import platform.inject.accessors.ScreenHandlerSlotUpdateS2CPacketAccessor;

@ModuleRegister(
   a = "AH Helper",
   b = "Сортирует лоты по цене за штуку — сам аукцион умеет сортировать только по цене лота",
   c = Category.Misc
)
public class AHHelper extends Module {
   private final BooleanSetting b = new BooleanSetting("Сортировать по цене", true);
   private final BooleanSetting c = new BooleanSetting("Цена за штуку в подсказке", true);
   private final BooleanSetting d = new BooleanSetting("Стоимость хранилища в заголовке", true);
   private final BooleanSetting e = new BooleanSetting("Фильтровать лоты", true);
   private final ModeSetting k = new ModeSetting("Цена для сортировки", "За штуку", "За штуку", "За лот");
   private final BooleanSetting l = new BooleanSetting("Подсвечивать лучший лот", false);
   private final BooleanSetting m = new BooleanSetting("Показывать цену на лоте", true);
   private boolean n;
   final MultiModeSetting f = new MultiModeSetting(
         "Фильтр брони по",
         new BooleanSetting("Защите", false),
         new BooleanSetting("Аншип", true),
         new BooleanSetting("Починке", true),
         new BooleanSetting("Подводной ходьбе", true)
      )
      .a(() -> this.e.c());
   final MultiModeSetting g = new MultiModeSetting(
         "Фильтр меча по",
         new BooleanSetting("Остроте", false),
         new BooleanSetting("Детекции", true),
         new BooleanSetting("Вампиризму", true),
         new BooleanSetting("Окислению", true),
         new BooleanSetting("Яду", true)
      )
      .a(() -> this.e.c());
   final MultiModeSetting h = new MultiModeSetting(
         "Фильтр кирки по",
         new BooleanSetting("Эффективности", false),
         new BooleanSetting("Удаче", true),
         new BooleanSetting("Магнит", true),
         new BooleanSetting("Починке", true)
      )
      .a(() -> this.e.c());
   private final AHHelper.a i = new AHHelper.a();
   private int[] j = null;

   public AHHelper() {
      this.a(new Setting[]{this.b, this.k, this.l, this.m, this.c, this.d, this.e, this.f, this.g, this.h});
   }

   private static boolean r() {
      return ServerUtil.a.a() || ServerUtil.d.a();
   }

   private static ToIntFunction<class_1799> s() {
      return ServerUtil.a.a() ? ServerUtil.a::a : ServerUtil.d::a;
   }

   @EventTarget
   public void a(TooltipEvent event) {
      if (this.c.c()) {
         class_1799 stack = event.b();
         int price = ServerUtil.a.a(stack);
         if (price > 0 && stack.method_7947() > 1) {
            List<class_2561> lines = event.c();

            for (int line = 0; line < lines.size(); line++) {
               if (lines.get(line).getString().contains("$ Ценa: ")) {
                  lines.add(
                     line + 1,
                     class_2561.method_43470("$")
                        .method_27692(class_124.field_1060)
                        .method_10852(class_2561.method_43470(" Цена за штуку: ").method_27692(class_124.field_1068))
                        .method_10852(class_2561.method_43470(String.format(Locale.US, "%,d", price)).method_27692(class_124.field_1060))
                  );
                  return;
               }
            }
         }
      }
   }

   @EventTarget
   public void a(ContainerEvent event) {
      if (event.h() == ContainerEvent.Phase.POST) {
         this.p(event);
      } else if (this.d.c() && event.h() == ContainerEvent.Phase.TITLE) {
         Object screen = event.b();
         if (screen instanceof class_476 && event.i().getString().contains("Хранилище")) {
            ToIntFunction<class_1799> price = s();
            long storage = ((HandledScreenAccessor)screen)
               .getScreenHandler()
               .field_7761
               .stream()
               .mapToLong(slot -> (long)Math.max(price.applyAsInt(slot.method_7677()), 0) * Math.max(slot.method_7677().method_7947(), 1))
               .sum();
            String suffix = " - "
               + Stream.of(Map.entry(1000000000L, "ккк"), Map.entry(1000000L, "кк"), Map.entry(1000L, "к"))
                  .filter(unit -> storage >= (Long)unit.getKey())
                  .map(unit -> String.format(Locale.US, "%.0f%s", (double)(storage / (Long)unit.getKey()), unit.getValue()))
                  .findFirst()
                  .orElseGet(() -> String.valueOf(storage));
            class_5250 title = class_2561.method_43473();
            event.i().method_27658((style, part) -> {
               title.method_10852(class_2561.method_43470(part).method_10862(style));
               if (part.contains("Хранилище")) {
                  title.method_10852(class_2561.method_43470(suffix).method_10862(style));
               }

               return Optional.empty();
            }, class_2583.field_24360);
            event.a(title);
         }
      }
   }

   @EventTarget
   public void a(PacketEvent event) {
      if (event.c()) {
         Object incoming = event.d();
         if (incoming instanceof class_2649 packet) {
            this.q(packet);
         }

         if (incoming instanceof class_2653 update) {
            ScreenHandlerSlotUpdateS2CPacketAccessor accessor = (ScreenHandlerSlotUpdateS2CPacketAccessor)update;
            if (this.j != null
               && accessor.getSyncId() != 0
               && aM_.field_1724 != null
               && accessor.getSyncId() == aM_.field_1724.field_7512.field_7763
               && accessor.getSlot() >= 0
               && accessor.getSlot() < this.j.length) {
               for (int display = 0; display < this.j.length; display++) {
                  if (this.j[display] == accessor.getSlot()) {
                     accessor.setSlot(display);
                     break;
                  }
               }
            }
         }
      }

      if (event.b() && event.d() instanceof class_2813 click) {
         ClickSlotC2SPacketAccessor accessor = (ClickSlotC2SPacketAccessor)click;
         if (this.j != null
            && accessor.getSyncId() != 0
            && aM_.field_1724 != null
            && accessor.getSyncId() == aM_.field_1724.field_7512.field_7763
            && accessor.getSlot() >= 0
            && accessor.getSlot() < this.j.length) {
            accessor.setSlot(this.j[accessor.getSlot()]);
         }
      }
   }

   private void p(ContainerEvent event) {
      if (this.l.c() && r() && aM_.field_1724 != null) {
         Object screen = event.b();
         if (screen instanceof class_476) {
            List<class_1735> slots = event.e();
            if (slots.size() >= 45) {
               HandledScreenAccessor accessor = (HandledScreenAccessor)screen;
               ToIntFunction<class_1799> price = s();
               boolean perItem = this.k.l("За штуку");
               boolean filtering = this.e.c();
               class_1735 best = null;
               long bestValue = Long.MAX_VALUE;

               for (int index = 0; index < slots.size() - 36; index++) {
                  class_1735 slot = slots.get(index);
                  class_1799 stack = slot.method_7677();
                  int value = stack.method_7960() ? -1 : price.applyAsInt(stack);
                  if (value >= 0 && (!filtering || this.i.a(stack))) {
                     long total = perItem ? value : (long)value * Math.max(1, stack.method_7947());
                     if (total < bestValue) {
                        bestValue = total;
                        best = slot;
                     }
                  }
               }

               if (this.m.c()) {
                  for (int indexx = 0; indexx < slots.size() - 36; indexx++) {
                     class_1735 slot = slots.get(indexx);
                     class_1799 stack = slot.method_7677();
                     int value = stack.method_7960() ? -1 : price.applyAsInt(stack);
                     if (value >= 0) {
                        long shown = perItem ? value : (long)value * Math.max(1, stack.method_7947());
                        String label = o(shown);
                        float lx = accessor.getX() + slot.field_7873 + 16.0F - Fonts.e.a(label, 6.0F);
                        float ly = accessor.getY() + slot.field_7872 + 16.0F - 6.0F;
                        Fonts.e.a(event.d().method_51448(), label, lx + 0.5F, ly + 0.5F, 6.0F, ColorUtil.a(0, 0, 0, 200));
                        Fonts.e.a(event.d().method_51448(), label, lx, ly, 6.0F, ColorUtil.a(120, 255, 170, 255));
                     }
                  }
               }

               if (best != null) {
                  float pulse = (float)(Math.sin(System.currentTimeMillis() / 250.0) * 0.5 + 0.5);
                  Westra.h()
                     .d()
                     .i()
                     .a(
                        event.d(),
                        accessor.getX() + best.field_7873,
                        accessor.getY() + best.field_7872,
                        16.0F,
                        16.0F,
                        ColorUtil.a(70, 225, 130, (int)(60.0F + 120.0F * pulse))
                     );
               }
            }
         }
      }
   }

   private static String o(long value) {
      if (value >= 1000000000L) {
         return String.format(Locale.US, "%.1fккк", value / 1.0E9);
      } else if (value >= 1000000L) {
         return String.format(Locale.US, "%.1fкк", value / 1000000.0);
      } else {
         return value >= 1000L ? String.format(Locale.US, "%.0fк", value / 1000.0) : String.valueOf(value);
      }
   }

   private void q(class_2649 packet) {
      try {
         this.n(packet);
      } catch (Throwable var3) {
         if (!this.n) {
            this.n = true;
            ChatUtil.a("Сортировка аукциона недоступна: &c" + var3);
         }

         this.j = null;
      }
   }

   private void n(class_2649 packet) {
      this.j = null;
      if (this.b.c() && r()) {
         List<class_1799> contents = packet.method_11441();
         int chestSlots = contents.size() > 36 ? contents.size() - 36 : contents.size();
         if (chestSlots >= 9 && packet.method_11440() != 0) {
            ToIntFunction<class_1799> price = s();
            boolean perItem = this.k.l("За штуку");
            boolean filtering = this.e.c();
            List<Integer> lots = new ArrayList<>();
            int[] weight = new int[chestSlots];

            for (int slot = 0; slot < chestSlots; slot++) {
               class_1799 stack = contents.get(slot);
               int value = stack.method_7960() ? -1 : price.applyAsInt(stack);
               if (value < 0) {
                  weight[slot] = Integer.MAX_VALUE;
               } else {
                  lots.add(slot);
                  long total = perItem ? value : (long)value * Math.max(1, stack.method_7947());
                  weight[slot] = filtering && !this.i.a(stack) ? 2147483646 : (int)Math.min(total, 2147483645L);
               }
            }

            if (lots.size() >= 2) {
               List<Integer> ordered = new ArrayList<>(lots);
               ordered.sort(Comparator.comparingInt(slotx -> weight[slotx]));
               List<class_1799> original = new ArrayList<>(contents);
               int[] map = new int[chestSlots];
               int slotx = 0;

               while (slotx < chestSlots) {
                  map[slotx] = slotx++;
               }

               for (int index = 0; index < lots.size(); index++) {
                  int display = lots.get(index);
                  int real = ordered.get(index);
                  map[display] = real;
                  contents.set(display, original.get(real));
               }

               this.j = map;
            }
         }
      }
   }

   @Override
   public void c() {
      this.j = null;
      super.c();
   }

   public class a {
      public boolean a(class_1799 stack) {
         if (stack.method_7909() instanceof class_1738) {
            return this.b(stack);
         } else if (stack.method_7909() instanceof class_1829) {
            return this.c(stack);
         } else {
            return stack.method_7909() instanceof class_1810 ? this.d(stack) : true;
         }
      }

      private boolean b(class_1799 stack) {
         EnchantmentProcessor enchantment = new EnchantmentProcessor();
         if (AHHelper.this.f.a("Защите").c()) {
            enchantment.a(class_1893.field_9119, 4);
            enchantment.a(class_1893.field_9111, 5);
         }

         if (AHHelper.this.f.a("Аншип").c()) {
            enchantment.b(class_1893.field_9097);
         }

         if (AHHelper.this.f.a("Починке").c()) {
            enchantment.a(class_1893.field_9101, 1);
         }

         if (AHHelper.this.f.a("Подводной ходьбе").c()
            && stack.method_57824(class_9334.field_54196) != null
            && ((class_10192)stack.method_57824(class_9334.field_54196)).comp_3174() == class_1304.field_6166) {
            enchantment.a(class_1893.field_9128, 1);
         }

         return enchantment.a(stack);
      }

      private boolean c(class_1799 stack) {
         EnchantmentProcessor enchantment = new EnchantmentProcessor().b(class_1893.field_9121, 2);
         if (AHHelper.this.g.a("Остроте").c()) {
            enchantment.a(class_1893.field_9118, 6);
         }

         enchantment.b(class_1893.field_9121, 1);
         if (!enchantment.a(stack)) {
            return false;
         } else {
            DescriptionProcessor description = new DescriptionProcessor();
            description.b("Нестабильность ");
            description.b("Нестабильный ");
            if (AHHelper.this.g.a("Детекции").c()) {
               description.a("Детекция", 2);
            }

            if (AHHelper.this.g.a("Вампиризму").c()) {
               description.a("Вампиризм", 2);
            }

            if (AHHelper.this.g.a("Окислению").c()) {
               description.a("Окисление", 2);
            }

            if (AHHelper.this.g.a("Яду").c()) {
               description.a("Яд", 3);
            }

            return description.a(stack);
         }
      }

      private boolean d(class_1799 stack) {
         if (AHHelper.this.h.a("Починке").c() && !new EnchantmentProcessor().a(class_1893.field_9101, 1).a(stack)) {
            return false;
         } else {
            DescriptionProcessor description = new DescriptionProcessor();
            if (AHHelper.this.h.a("Удаче").c()) {
               description.a("Удача", 5);
            }

            if (AHHelper.this.h.a("Эффективности").c()) {
               description.a("Эффективность", 4);
            }

            if (AHHelper.this.h.a("Магнит").c()) {
               description.a("Магнит");
            }

            return description.a(stack);
         }
      }
   }
}
