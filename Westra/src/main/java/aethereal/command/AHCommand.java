package aethereal.command;

import aethereal.autobuy.AutoBuyEntry;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.event.PacketEvent;
import aethereal.event.TickEvent;
import aethereal.util.ChatUtil;
import aethereal.util.CounterUtil;
import aethereal.util.ServerUtil;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_1078;
import net.minecraft.class_1799;
import net.minecraft.class_1836;
import net.minecraft.class_2172;
import net.minecraft.class_2649;
import net.minecraft.class_1792.class_9635;

@Command(
   a = "ah"
)
public class AHCommand extends BaseCommand implements Interface {
   private final List<AHCommand.a> c = new ArrayList<>();
   private class_1078 d;
   private String e;
   private AHCommand.b f;

   @Override
   public void a(LiteralArgumentBuilder<class_2172> builder) {
      ((LiteralArgumentBuilder)builder.executes(context -> {
         class_1799 stack = aM_.field_1724.method_6047();
         if (!stack.method_7960()) {
            aM_.field_1724.field_3944.method_45730("ah search " + this.a(stack));
            return 1;
         } else {
            return 1;
         }
      })).then(((LiteralArgumentBuilder)this.a("sell").executes(context2 -> {
         this.a(0.0F);
         return 1;
      })).then(this.f("процент").executes(context3 -> {
         this.a(this.c(context3, "процент"));
         return 1;
      })));
   }

   @EventTarget
   public void a(PacketEvent event) {
      if (this.f != null && this.e == null && event.c() && event.d() instanceof class_2649 class_2649VarD && class_2649VarD.method_11440() != 0) {
         List<class_1799> contents = class_2649VarD.method_11441();
         List<Integer> prices = contents.subList(0, Math.max(0, contents.size() - 36))
            .stream()
            .filter(
               stack -> this.f.a().a(stack)
                  && stack.method_7950(class_9635.field_51353, aM_.field_1724, class_1836.field_41070)
                     .stream()
                     .noneMatch(line -> line.getString().contains("Нажмите, чтобы забрать"))
            )
            .mapToInt(ServerUtil.a::a)
            .filter(price -> price > 0)
            .sorted()
            .boxed()
            .toList();
         if (!prices.isEmpty()) {
            int reference = prices.get(Math.min(2, prices.size() - 1));
            int cheapest = prices.stream().filter(price2 -> price2.intValue() >= reference * 0.75).findFirst().orElse(reference);
            this.c.removeIf(entry -> entry.a() == this.f.a());
            this.c.add(new AHCommand.a(this.f.a(), cheapest, new CounterUtil()));
            this.a(this.f, cheapest);
         }

         this.f = null;
      }
   }

   @EventTarget
   public void a(TickEvent event) {
      if (this.e != null) {
         if (aM_.field_1755 != null) {
            aM_.field_1724.method_7346();
         } else {
            aM_.field_1724.field_3944.method_45731(this.e);
            this.e = null;
         }
      }
   }

   private void a(float percent) {
      class_1799 stack = aM_.field_1724.method_6047();
      AutoBuyEntry item = Westra.h().d().q().e().stream().filter(info -> info.a(stack)).findFirst().orElse(null);
      if (item == null) {
         ChatUtil.a("Авто-продажа недоступна для обычных предметов — только для донатных");
      } else {
         AHCommand.b request = new AHCommand.b(item, stack.method_7947(), percent);
         AHCommand.a cached = this.c.stream().filter(entry -> entry.a() == item && !entry.c().a(15000L)).findFirst().orElse(null);
         if (cached != null) {
            this.a(request, cached.b());
         } else {
            aM_.field_1724.field_3944.method_45730("ah search " + this.a(stack));
            this.f = request;
         }
      }
   }

   private void a(AHCommand.b sell, int cheapest) {
      long price = Math.max(1L, Math.round(cheapest * (1.0 - sell.c() / 100.0) * Math.max(1, sell.b())));
      String strB = sell.a().b();
      ChatUtil.a("Выставляю &c" + strB + " &7за &c" + price + " &7(-" + strB + "% от " + (int)sell.c() + ")");
      this.e = "ah sell " + price;
   }

   private String a(class_1799 stack) {
      this.d = this.d == null ? class_1078.method_4675(aM_.method_1478(), List.of("ru_ru"), false) : this.d;
      String name = Westra.h()
         .d()
         .q()
         .e()
         .stream()
         .filter(item -> item.a(stack))
         .findFirst()
         .map(v0 -> v0.b())
         .orElse(this.d.method_48307(stack.method_7909().method_7876()));
      return cleanSearchQuery(name);
   }

   public static String cleanSearchQuery(String name) {
      return name == null
         ? ""
         : name.replaceAll("\\[\\d+x\\d+]", "")
            .replace("⚡", "")
            .replace("xxx", "")
            .replace("[", "")
            .replace("]", "")
            .replace("★", "")
            .trim()
            .replaceAll("\\s+", "");
   }

   static final class a {
      private final AutoBuyEntry a;
      private final int b;
      private final CounterUtil c;

      a(AutoBuyEntry item, int price, CounterUtil timer) {
         this.a = item;
         this.b = price;
         this.c = timer;
      }

      public AutoBuyEntry a() {
         return this.a;
      }

      public int b() {
         return this.b;
      }

      public CounterUtil c() {
         return this.c;
      }
   }

   static final class b {
      private final AutoBuyEntry a;
      private final int b;
      private final float c;

      b(AutoBuyEntry item, int count, float percent) {
         this.a = item;
         this.b = count;
         this.c = percent;
      }

      public AutoBuyEntry a() {
         return this.a;
      }

      public int b() {
         return this.b;
      }

      public float c() {
         return this.c;
      }
   }
}
