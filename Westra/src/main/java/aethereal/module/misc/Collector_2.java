package aethereal.module.misc;

import aethereal.config.DescriptionProcessor;
import aethereal.config.EnchantmentProcessor;
import aethereal.config.PotionProcessor;
import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.ContainerEvent;
import aethereal.event.InputEvent;
import aethereal.event.KeyEvent;
import aethereal.event.PacketEvent;
import aethereal.event.TickEvent;
import aethereal.render.AnimationUtil;
import aethereal.setting.ButtonSetting;
import aethereal.setting.Setting;
import aethereal.ui.screen.StationScreen;
import aethereal.util.ChatUtil;
import aethereal.util.CounterUtil;
import aethereal.util.MathUtil;
import aethereal.util.ServerUtil;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import lombok.Generated;
import net.minecraft.class_1293;
import net.minecraft.class_1661;
import net.minecraft.class_1713;
import net.minecraft.class_1735;
import net.minecraft.class_1738;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1812;
import net.minecraft.class_1828;
import net.minecraft.class_1836;
import net.minecraft.class_1844;
import net.minecraft.class_1893;
import net.minecraft.class_2561;
import net.minecraft.class_2815;
import net.minecraft.class_3944;
import net.minecraft.class_465;
import net.minecraft.class_476;
import net.minecraft.class_7439;
import net.minecraft.class_9334;
import net.minecraft.class_1792.class_9635;

@ModuleRegister(
   a = "Collector",
   b = "Автоматически собирает нужный инвентарь на FunTime",
   c = Category.Misc
)
public class Collector_2 extends Module {
   private final ButtonSetting b = new ButtonSetting("Запустить работу", () -> {
      if (!this.m()) {
         this.a();
      }

      this.r();
   });
   private final ButtonSetting c = new ButtonSetting("Открыть редактор", () -> aM_.method_1507(new StationScreen(class_2561.method_43470(""), 0)));
   private final List<Collector_2.b> d = Westra.h().d().p().e();
   private final List<Collector_2.a> e = new ArrayList<>();
   private final CounterUtil f = new CounterUtil();
   private final CounterUtil g = new CounterUtil();
   private Collector_2.b h;
   private Collector_2.a i;
   private int j;

   @Generated
   public List<Collector_2.b> q() {
      return this.d;
   }

   public Collector_2() {
      this.a(new Setting[]{this.b, this.c});
   }

   @Override
   public void b() {
      super.b();
      ChatUtil.a("Модуль ищет самые дешевые лоты среди тех которые есть, имейте это ввиду, и будьте осторожны!");
   }

   private void r() {
      int start = this.h == null ? 0 : this.d.indexOf(this.h) + 1;
      this.i = null;
      this.e.clear();
      IntStream intStreamRange = IntStream.range(start, this.d.size());
      List<Collector_2.b> list = this.d;
      this.h = intStreamRange.mapToObj(list::get).filter(v0 -> v0.k()).filter(slot -> this.a(slot) < this.a(slot, false)).findFirst().orElse(null);
      if (this.h != null) {
         ChatUtil.a("Переходим к сбору предмета: " + this.h.j());
      } else {
         ChatUtil.a("Все предметы собраны, работа завершена");
      }
   }

   @Override
   public void c() {
      super.c();
      this.h = null;
   }

   @EventTarget
   public void a(TickEvent event) {
      if (this.h != null) {
         if (aM_.field_1724.method_31548().method_7376() == -1) {
            ChatUtil.a("Автоматическое отключение: нет свободных слотов в инвентаре, освободите место");
            this.s();
            this.a();
            return;
         }

         this.j++;
         if (!(aM_.field_1755 instanceof class_476) && aM_.field_1724.field_6012 >= 200 && this.f.a(1000L, 300L)) {
            aM_.field_1724.field_3944.method_45731("ah search " + this.h.j());
            this.f.b();
            this.j = 0;
         }
      }
   }

   @EventTarget
   public void a(ContainerEvent event) {
      if (this.h != null && event.h() != ContainerEvent.Phase.PRE) {
         String title = event.b().method_25440().getString().replaceAll("§.", "").toLowerCase().trim();
         if (title.contains(this.h.j().toLowerCase())) {
            if (this.f.a(300L, 80L) && this.a(this.h) >= this.a(this.h, false)) {
               ChatUtil.a("Предмет " + this.h.j() + " приобретен, перехожу к следующему");
               this.r();
               this.s();
               return;
            }

            if (this.g.a(1000L, 150L)) {
               if (this.f.a(this.h.l() ? 400L : 550L, 120L)) {
                  if (this.h.l()) {
                     Matcher matcher = Pattern.compile("(\\d+)/(\\d+)").matcher(title);
                     if (matcher.find()) {
                        int currentPage = Integer.parseInt(matcher.group(1));
                        int lastScanPage = Math.min(4, Integer.parseInt(matcher.group(2)));
                        if (this.i == null) {
                           if (this.e.stream().noneMatch(offer -> offer.a() == currentPage)) {
                              event.e()
                                 .stream()
                                 .filter(slot -> this.a(slot.method_7677()))
                                 .forEach(slot2 -> this.e.add(new Collector_2.a(currentPage, slot2.field_7874, ServerUtil.a.a(slot2.method_7677()))));
                           }

                           if (currentPage < lastScanPage) {
                              this.a(event, "следующая страница");
                           } else {
                              this.i = this.e.stream().min(Comparator.comparingInt(v0 -> v0.c())).orElse(null);
                              if (this.i == null) {
                                 this.s();
                              }
                           }
                        } else if (currentPage == this.i.a()) {
                           class_1735 offer2 = event.e()
                              .stream()
                              .filter(slot3 -> this.a(slot3.method_7677()) && ServerUtil.a.a(slot3.method_7677()) == this.i.c())
                              .findFirst()
                              .orElse(null);
                           if (offer2 == null) {
                              offer2 = event.e()
                                 .stream()
                                 .filter(slot4 -> this.a(slot4.method_7677()))
                                 .min(Comparator.comparingInt(slot5 -> ServerUtil.a.a(slot5.method_7677())))
                                 .orElse(null);
                           }

                           if (offer2 != null) {
                              aM_.field_1761.method_2906(event.c().field_7763, offer2.field_7874, 0, class_1713.field_7794, aM_.field_1724);
                              this.f.b();
                           } else {
                              ChatUtil.a("Оффер пропал, пересканирую");
                              this.i = null;
                              this.e.clear();
                              this.s();
                           }
                        } else {
                           this.a(event, currentPage < this.i.a() ? "следующая страница" : "предыдущая страница");
                        }
                     }
                  } else {
                     List<class_1735> buyable = event.e().stream().filter(slot6 -> this.a(slot6.method_7677())).toList();
                     int minPrice = buyable.stream().mapToInt(slot7 -> ServerUtil.a.a(slot7.method_7677())).min().orElse(0);
                     List<class_1735> affordable = buyable.stream()
                        .filter(slot8 -> ServerUtil.a.a(slot8.method_7677()) <= Math.round((float)(minPrice * 2)))
                        .filter(slot9 -> slot9.method_7677().method_7947() <= this.a(this.h, true) - this.a(this.h))
                        .toList();
                     class_1735 cheapest = affordable.stream()
                        .filter(slot10 -> slot10.method_7677().method_7947() >= this.a(this.h, false) - this.a(this.h))
                        .min(Comparator.comparingInt(slot11 -> ServerUtil.a.a(slot11.method_7677())))
                        .orElse(null);
                     if (cheapest == null) {
                        cheapest = affordable.stream().min(Comparator.comparingInt(slot12 -> ServerUtil.a.a(slot12.method_7677()))).orElse(null);
                     }

                     if (cheapest != null) {
                        aM_.field_1761.method_2906(event.c().field_7763, cheapest.field_7874, 0, class_1713.field_7794, aM_.field_1724);
                        this.f.b();
                     } else {
                        Matcher matcher2 = Pattern.compile("(\\d+)/(\\d+)").matcher(title);
                        if (matcher2.find()) {
                           int currentPage2 = Integer.parseInt(matcher2.group(1));
                           int totalPages = Integer.parseInt(matcher2.group(2));
                           if (totalPages == 1) {
                              ChatUtil.a("Пропускаем предмет " + this.h.j() + ", ибо нету подходящего");
                              this.r();
                              this.s();
                              return;
                           }

                           this.a(event, currentPage2 == 1 ? "следующая страница" : "предыдущая страница");
                        }
                     }
                  }

                  this.f.b();
                  return;
               }

               return;
            }

            return;
         }

         if (title.contains("подтверждение покупки") || title.contains("подозрительная цена!") || title.contains("подозрительная цена: ")) {
            if (this.f.a(200L, 90L)) {
               this.a(event, "[Кyпить]");
               this.f.b();
               return;
            }

            return;
         }

         this.s();
      }
   }

   @EventTarget
   public void a(PacketEvent event) {
      if (this.h != null && event.c()) {
         if (event.d() instanceof class_7439 class_7439VarD) {
            String message = class_7439VarD.comp_763().getString();
            if (message.toLowerCase().contains("[✘] Ошибка! Этот товар уже Купили!")) {
               this.i = null;
               this.e.clear();
               return;
            }

            if (message.contains("[✘] Ошибка! У Вас не хватает Монет!")) {
               ChatUtil.a("Автоматическое отключение из-за нехватки баланса на аккаунте");
               this.a();
               return;
            }

            if (message.contains("Данная команда недоступна в режиме AFK")) {
               Westra.h().d().v().g().a(10);
            }
         }

         if (event.d() instanceof class_3944 && !(aM_.field_1755 instanceof class_476)) {
            if (this.j >= 8) {
               int anarchy = (int)(MathUtil.a(0.0F, 100.0F) <= 50.0F ? MathUtil.a(205.0F, 231.0F) : MathUtil.a(305.0F, 325.0F));
               aM_.field_1724.field_3944.method_45729("/an" + anarchy);
               ChatUtil.a("Обнаружили замедление аукциона, переходим на " + anarchy + " анархию");
            }

            this.g.b();
         }
      }
   }

   @EventTarget
   public void a(InputEvent event) {
      if (this.h != null && aM_.field_1755 instanceof class_465) {
         event.a(0.0F);
         event.b(0.0F);
      }
   }

   @EventTarget
   public void a(KeyEvent event) {
      if (this.h != null && aM_.field_1755 instanceof class_465) {
         event.a(true);
      }

      if (event.b() == 256 && this.h != null) {
         this.h = null;
         ChatUtil.a("Работа модуля была принудительно завершена");
      }
   }

   private void a(ContainerEvent event, String name) {
      event.e()
         .stream()
         .filter(slot -> slot.method_7677().method_7964().getString().toLowerCase().contains(name.toLowerCase()))
         .findFirst()
         .ifPresent(slot2 -> aM_.field_1761.method_2906(event.c().field_7763, slot2.field_7874, 0, class_1713.field_7794, aM_.field_1724));
   }

   private void s() {
      if (aM_.field_1755 instanceof class_476) {
         aM_.field_1724.field_3944.method_52787(new class_2815(aM_.field_1724.field_7512.field_7763));
         aM_.field_1724.method_3137();
      }
   }

   private boolean a(class_1799 stack) {
      if (!stack.method_7960() && this.h.a(stack)) {
         IntStream intStreamRange = IntStream.range(0, 40);
         class_1661 class_1661VarMethod_31548 = aM_.field_1724.method_31548();
         class_1799 inventory = intStreamRange.<class_1799>mapToObj(class_1661VarMethod_31548::method_5438)
            .filter(s -> !s.method_7960() && this.h.a(s))
            .findFirst()
            .orElse(class_1799.field_8037);
         if ((
               inventory.method_7960()
                  || (
                        !(this.h.i() instanceof class_1812)
                           || Objects.equals(stack.method_57824(class_9334.field_49651), inventory.method_57824(class_9334.field_49651))
                     )
                     && stack.method_7964().getString().trim().equalsIgnoreCase(inventory.method_7964().getString().trim())
            )
            && !stack.method_7950(class_9635.field_51353, aM_.field_1724, class_1836.field_41070)
               .stream()
               .anyMatch(line -> line.getString().contains("➥ Нажмите, чтобы забрать"))
            && ServerUtil.a.a(stack) > 0) {
            if (this.h.i() == class_1802.field_8288 && stack.method_7958()) {
               return false;
            } else if (this.h.i() == class_1802.field_8833 && stack.method_57824(class_9334.field_49629) != null) {
               return (432 - (Integer)stack.method_57824(class_9334.field_49629)) / 432.0F >= 0.5F;
            } else {
               return this.h.i() instanceof class_1738 && stack.method_57824(class_9334.field_49629) != null && stack.method_7936() > 0
                  ? this.h.f() != null
                        && !this.h.f().b().stream().noneMatch(condition -> condition.b() && !condition.d() && condition.i().equals(class_1893.field_9101))
                     || (float)(stack.method_7936() - (Integer)stack.method_57824(class_9334.field_49629)) / stack.method_7936() >= 0.7F
                  : true;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private int a(Collector_2.b info, boolean upper) {
      return Math.max(1, upper ? info.m() + Math.round(info.m() * 0.2F) : info.m());
   }

   private int a(Collector_2.b slot) {
      IntStream intStreamRange = IntStream.range(0, 40);
      class_1661 class_1661VarMethod_31548 = aM_.field_1724.method_31548();
      return intStreamRange.<class_1799>mapToObj(class_1661VarMethod_31548::method_5438)
         .filter(stack -> !stack.method_7960() && slot.a(stack))
         .mapToInt(v0 -> v0.method_7947())
         .sum();
   }

   static final class a {
      private final int a;
      private final int b;
      private final int c;

      a(int page, int slotId, int price) {
         this.a = page;
         this.b = slotId;
         this.c = price;
      }

      public int a() {
         return this.a;
      }

      public int b() {
         return this.b;
      }

      public int c() {
         return this.c;
      }
   }

   public static class b {
      private DescriptionProcessor a;
      private EnchantmentProcessor b;
      private PotionProcessor c;
      private final AnimationUtil d = new AnimationUtil();
      private final class_1792 e;
      private final String f;
      private boolean g;
      private boolean h;
      private int i;
      private int j;

      @Generated
      public void b(int count) {
         this.i = count;
      }

      @Generated
      public DescriptionProcessor e() {
         return this.a;
      }

      @Generated
      public EnchantmentProcessor f() {
         return this.b;
      }

      @Generated
      public PotionProcessor g() {
         return this.c;
      }

      @Generated
      public AnimationUtil h() {
         return this.d;
      }

      @Generated
      public class_1792 i() {
         return this.e;
      }

      @Generated
      public String j() {
         return this.f;
      }

      @Generated
      public boolean k() {
         return this.g;
      }

      @Generated
      public boolean l() {
         return this.h;
      }

      @Generated
      public int m() {
         return this.i;
      }

      @Generated
      public int n() {
         return this.j;
      }

      private b(class_1792 item, int count, String name) {
         this.e = item;
         this.f = name;
         this.i = count;
      }

      public static Collector_2.b a(class_1792 item, int count, String name) {
         return new Collector_2.b(item, count, name);
      }

      public boolean a() {
         return this.e.method_7882() > 1 || this.e == class_1802.field_8288 || this.e instanceof class_1828 || this.e instanceof class_1812;
      }

      public int b() {
         if (this.e instanceof class_1812) {
            return 16;
         } else {
            return this.e != class_1802.field_8288 && !(this.e instanceof class_1828) ? this.e.method_7882() : 6;
         }
      }

      public Collector_2.b a(EnchantmentProcessor processor) {
         this.b = processor;
         return this;
      }

      public Collector_2.b a(DescriptionProcessor processor) {
         this.a = processor;
         return this;
      }

      public Collector_2.b a(PotionProcessor processor) {
         this.c = processor;
         return this;
      }

      public Collector_2.b a(boolean active) {
         this.g = active;
         return this;
      }

      public Collector_2.b b(boolean scan) {
         this.h = scan;
         return this;
      }

      public Collector_2.b a(int color) {
         this.j = color;
         return this;
      }

      public boolean a(class_1799 stack) {
         return stack.method_31574(this.e) && (this.a == null || this.a.a(stack)) && (this.b == null || this.b.a(stack)) && (this.c == null || this.c.a(stack));
      }

      public class_1799 c() {
         class_1799 stack = new class_1799(this.e, this.i);
         if (this.j != 0) {
            List<class_1293> effects = this.c == null ? List.of() : this.c.a().stream().map(c -> new class_1293(c.a(), c.c(), Math.max(0, c.b() - 1))).toList();
            stack.method_57379(class_9334.field_49651, new class_1844(Optional.empty(), Optional.of(this.j), effects, Optional.empty()));
         }

         return stack;
      }

      public Collector_2.b d() {
         return a(this.e, this.i, this.f).a(this.a).a(this.b).a(this.c).a(this.g).b(this.h).a(this.j);
      }
   }
}
