package aethereal.autobuy;

import aethereal.api.Compile;
import aethereal.config.Condition;
import aethereal.config.ConfigProcessor;
import aethereal.config.DescriptionProcessor;
import aethereal.config.EnchantmentProcessor;
import aethereal.config.PotionCondition;
import aethereal.config.PotionProcessor;
import aethereal.lib.json.JSONArray;
import aethereal.lib.json.JSONObject;
import aethereal.module.misc.Collector_2;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.class_1294;
import net.minecraft.class_1802;
import net.minecraft.class_1893;

public class CollectorProcessor extends ConfigProcessor<Collector_2.b> {
   public CollectorProcessor() {
      this.d.addAll(this.a());
   }

   @Compile
   @Override
   protected List<Collector_2.b> a(String str) {
      if (this.d.isEmpty()) {
         this.d.addAll(this.a());
      }

      JSONArray jSONArray = new JSONArray(str);

      for (int i = 0; i < jSONArray.a(); i++) {
         JSONObject jSONObjectJ = jSONArray.j(i);
         this.d.stream().filter(obj -> obj.j().equals(jSONObjectJ.l("name"))).findFirst().ifPresent(obj -> this.b(jSONObjectJ, obj));
      }

      return new ArrayList<>(this.d);
   }

   @Compile
   @Override
   protected String a(List<Collector_2.b> data) {
      JSONArray jSONArray = new JSONArray();

      for (Collector_2.b bVar : data) {
         JSONObject jSONObject = new JSONObject();
         if (!(bVar instanceof Collector_2.b)) {
            throw new ClassCastException();
         }

         jSONObject.c("name", bVar.j());
         jSONObject.b("active", bVar.k());
         jSONObject.b("count", bVar.m());
         bVar.f();
         if (bVar.f() != null) {
            this.a(jSONObject, "enchantments", bVar.f().b());
         }

         bVar.e();
         if (bVar.e() != null) {
            this.a(jSONObject, "descriptions", bVar.e().b());
         }

         jSONArray.a(jSONObject);
      }

      return jSONArray.E(2);
   }

   @Compile
   public void b(JSONObject obj, Collector_2.b info) {
      if (obj.m("active")) {
         info.a(obj.b("active"));
      }

      if (obj.m("count")) {
         info.b(obj.h("count"));
      }

      info.f();
      if (info.f() != null) {
         this.b(obj, "enchantments", info.f().b());
      }

      info.e();
      if (info.e() != null) {
         this.b(obj, "descriptions", info.e().b());
      }
   }

   @Compile
   private void a(JSONObject obj, String key, List<? extends Condition> conditions) {
      if (!conditions.isEmpty()) {
         JSONObject jSONObject = new JSONObject();

         for (Condition condition : conditions) {
            if (!(condition instanceof Condition)) {
               throw new ClassCastException();
            }

            if (!condition.d()) {
               JSONObject jSONObject2 = new JSONObject();
               jSONObject2.b("level", condition.g());
               jSONObject2.c("type", condition.h().name());
               jSONObject.c(condition.f(), jSONObject2);
            }
         }

         obj.c(key, jSONObject);
      }
   }

   @Compile
   private void b(JSONObject obj, String key, List<? extends Condition> conditions) {
      if (obj.m(key)) {
         JSONObject jSONObjectJ = obj.j(key);
         if (jSONObjectJ == null) {
            return;
         }

         for (Condition condition : conditions) {
            if (!(condition instanceof Condition)) {
               throw new ClassCastException();
            }

            if (!condition.d() && jSONObjectJ.m(condition.f())) {
               JSONObject jSONObjectJ2 = jSONObjectJ.j(condition.f());
               if (jSONObjectJ2 != null) {
                  if (jSONObjectJ2.m("level")) {
                     condition.a(jSONObjectJ2.h("level"));
                  }

                  if (jSONObjectJ2.m("type")) {
                     if (ItemType.valueOf(jSONObjectJ2.l("type")) == ItemType.OFF) {
                        condition.a(ItemType.OFF);
                     } else {
                        condition.a(ItemType.ON);
                     }
                  }

                  if (jSONObjectJ2.m("enabled")) {
                     if (jSONObjectJ2.b("enabled")) {
                        condition.a(ItemType.ON);
                     } else {
                        condition.a(ItemType.OFF);
                     }
                  }
               }
            }
         }
      }
   }

   @Override
   protected String b() {
      return "collect.json";
   }

   private List<Collector_2.b> a() {
      return Arrays.asList(
         Collector_2.b.a(class_1802.field_22022, 1, "Незеритовый меч")
            .a(true)
            .b(true)
            .a(new EnchantmentProcessor().a().a(class_1893.field_9118, 7).a(class_1893.field_9124, 2))
            .a(new DescriptionProcessor().a().a("Яд", 3).a("Вампиризм", 2).a("Окисление", 2).a("Опытный", 3, false).a("Детекция", 3)),
         Collector_2.b.a(class_1802.field_49814, 1, "Булава")
            .a(true)
            .b(true)
            .a(new EnchantmentProcessor().a().a(class_1893.field_9118, 7).a(class_1893.field_50158, 3).a(class_1893.field_50157, 5))
            .a(new DescriptionProcessor().a()),
         Collector_2.b.a(class_1802.field_8547, 1, "Трезубец")
            .a(true)
            .b(true)
            .a(new DescriptionProcessor().a().a("Ступор", 3).a("Притяжение", 2).a("Скаут", 3).a("Возвращение").a("Подрывник")),
         Collector_2.b.a(class_1802.field_22027, 1, "Незеритовый шлем")
            .a(true)
            .b(true)
            .a(new EnchantmentProcessor().a().a(class_1893.field_9111, 5).a(class_1893.field_9119, 5).a(class_1893.field_9127, 3).a(class_1893.field_9101)),
         Collector_2.b.a(class_1802.field_22028, 1, "Незеритовый нагрудник")
            .a(true)
            .b(true)
            .a(new EnchantmentProcessor().a().a(class_1893.field_9111, 5).a(class_1893.field_9119, 5).a(class_1893.field_9101)),
         Collector_2.b.a(class_1802.field_22029, 1, "Незеритовые поножи")
            .a(true)
            .b(true)
            .a(new EnchantmentProcessor().a().a(class_1893.field_9111, 5).a(class_1893.field_9119, 5).a(class_1893.field_9101)),
         Collector_2.b.a(class_1802.field_22030, 1, "Незеритовые ботинки")
            .a(true)
            .b(true)
            .a(new EnchantmentProcessor().a().a(class_1893.field_9111, 5).a(class_1893.field_9119, 5).a(class_1893.field_9128, 3).a(class_1893.field_9101)),
         Collector_2.b.a(class_1802.field_22021, 8, "Трапка").a(true).a(new DescriptionProcessor().a().a("Каст: Нерушимая клетка")),
         Collector_2.b.a(class_1802.field_8479, 12, "Явная пыль").a(true).a(new DescriptionProcessor().a().a("Каст: Световая вспышка")),
         Collector_2.b.a(class_1802.field_8614, 4, "Божья аура").a(true).a(new DescriptionProcessor().a().a("Каст: Божественная аура")),
         Collector_2.b.a(class_1802.field_8449, 16, "Дезориентация").a(true).a(new DescriptionProcessor().a().a("Каст: Звуковая волна")),
         Collector_2.b.a(class_1802.field_49098, 32, "Заряд ветра").a(true),
         Collector_2.b.a(class_1802.field_8551, 16, "Пласт").a(true).a(new DescriptionProcessor().a().a("Каст: Нерушимая стена")),
         Collector_2.b.a(class_1802.field_8543, 4, "Снежок заморозка").a(true).a(new DescriptionProcessor().a().a("Каст: Ледяная сфера")),
         Collector_2.b.a(class_1802.field_8634, 16, "Перка").a(true),
         Collector_2.b.a(class_1802.field_8288, 1, "Тотем бессмертия").a(true),
         Collector_2.b.a(class_1802.field_8399, 1, "Арбалет")
            .a(true)
            .a(new EnchantmentProcessor().a().a(class_1893.field_9098, 3).a(class_1893.field_9101).a(class_1893.field_9108)),
         Collector_2.b.a(class_1802.field_8463, 16, "Золотое яблоко").a(true),
         Collector_2.b.a(class_1802.field_8367, 8, "Зачарованное золотое яб").a(true),
         Collector_2.b.a(class_1802.field_8071, 64, "Золотая морковь").a(true),
         Collector_2.b.a(class_1802.field_8233, 64, "Хорус").a(true),
         Collector_2.b.a(class_1802.field_8833, 1, "Элитры").a(true),
         Collector_2.b.a(class_1802.field_8639, 64, "Фейерверк").a(true),
         Collector_2.b.a(class_1802.field_8436, 1, "Хлопушка")
            .a(true)
            .a(16711680)
            .a(
               new PotionProcessor()
                  .a(new PotionCondition(class_1294.field_5909, 10, 200))
                  .a(new PotionCondition(class_1294.field_5904, 5, 300))
                  .a(new PotionCondition(class_1294.field_5919, 10, 100))
                  .a(new PotionCondition(class_1294.field_5912, 1, 3600))
            ),
         Collector_2.b.a(class_1802.field_8436, 1, "Святая вода")
            .a(true)
            .a(16777215)
            .a(
               new PotionProcessor()
                  .a(new PotionCondition(class_1294.field_5924, 2, 900))
                  .a(new PotionCondition(class_1294.field_5905, 2, 12000))
                  .a(new PotionCondition(class_1294.field_5915, 2, 0))
            ),
         Collector_2.b.a(class_1802.field_8436, 1, "Зелье Гнева")
            .a(true)
            .b(true)
            .a(10040115)
            .a(new PotionProcessor().a(new PotionCondition(class_1294.field_5910, 5, 600)).a(new PotionCondition(class_1294.field_5909, 4, 600))),
         Collector_2.b.a(class_1802.field_8436, 1, "Зелье Палладина")
            .a(true)
            .a(65535)
            .a(
               new PotionProcessor()
                  .a(new PotionCondition(class_1294.field_5907, 1, 12000))
                  .a(new PotionCondition(class_1294.field_5918, 1, 12000))
                  .a(new PotionCondition(class_1294.field_5914, 3, 1200))
                  .a(new PotionCondition(class_1294.field_5905, 1, 18000))
            ),
         Collector_2.b.a(class_1802.field_8436, 1, "Зелье Ассасина")
            .a(true)
            .a(3355443)
            .a(
               new PotionProcessor()
                  .a(new PotionCondition(class_1294.field_5910, 4, 1200))
                  .a(new PotionCondition(class_1294.field_5904, 3, 6000))
                  .a(new PotionCondition(class_1294.field_5917, 1, 1200))
                  .a(new PotionCondition(class_1294.field_5921, 2, 0))
            ),
         Collector_2.b.a(class_1802.field_8436, 1, "Зелье Радиации")
            .a(true)
            .a(3329330)
            .a(
               new PotionProcessor()
                  .a(new PotionCondition(class_1294.field_5899, 2, 300))
                  .a(new PotionCondition(class_1294.field_5920, 2, 300))
                  .a(new PotionCondition(class_1294.field_5909, 3, 300))
                  .a(new PotionCondition(class_1294.field_5903, 5, 300))
                  .a(new PotionCondition(class_1294.field_5912, 1, 300))
            ),
         Collector_2.b.a(class_1802.field_8436, 1, "Снотворное")
            .a(true)
            .b(false)
            .a(4737096)
            .a(
               new PotionProcessor()
                  .a(new PotionCondition(class_1294.field_5911, 2, 1800))
                  .a(new PotionCondition(class_1294.field_5901, 2, 200))
                  .a(new PotionCondition(class_1294.field_5920, 3, 1800))
                  .a(new PotionCondition(class_1294.field_5919, 1, 200))
            ),
         Collector_2.b.a(class_1802.field_8574, 1, "Зелье")
            .a(true)
            .a(new PotionProcessor().a(new PotionCondition(class_1294.field_5910, 3, 3600)).a(new PotionCondition(class_1294.field_5904, 3, 3600))),
         Collector_2.b.a(class_1802.field_8574, 1, "Зелье регенерации")
            .a(true)
            .a(new PotionProcessor().a(new PotionCondition(class_1294.field_5915, 2, 0)).a(new PotionCondition(class_1294.field_5924, 1, 900))),
         Collector_2.b.a(class_1802.field_8087, 32, "Кровавая стрела")
            .a(true)
            .a(
               new PotionProcessor()
                  .a(new PotionCondition(class_1294.field_5911, 3, 60))
                  .a(new PotionCondition(class_1294.field_5919, 1, 40))
                  .a(new PotionCondition(class_1294.field_5901, 1, 40))
                  .a(new PotionCondition(class_1294.field_5916, 1, 100))
            ),
         Collector_2.b.a(class_1802.field_8087, 64, "Стрела обледенения")
            .a(false)
            .a(new PotionProcessor().a(new PotionCondition(class_1294.field_5909, 10, 100)).a(new PotionCondition(class_1294.field_5901, 3, 40))),
         Collector_2.b.a(class_1802.field_8087, 64, "Мучительная стрела")
            .a(false)
            .a(
               new PotionProcessor()
                  .a(new PotionCondition(class_1294.field_5909, 3, 100))
                  .a(new PotionCondition(class_1294.field_5920, 3, 100))
                  .a(new PotionCondition(class_1294.field_5899, 3, 100))
            )
      );
   }
}
