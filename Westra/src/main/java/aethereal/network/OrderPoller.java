package aethereal.network;

import aethereal.lib.jsoup.Connection_2;
import aethereal.lib.jsoup.Element;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Generated;

public class OrderPoller {
   private final List<String> a = new ArrayList<>();
   private List<OrderModel> b;
   private final FunPay c;

   @Generated
   public List<OrderModel> b() {
      return this.b;
   }

   public OrderPoller(FunPay funPay) {
      this.c = Objects.requireNonNull(funPay);
   }

   public void a() {
      List<OrderModel> current = this.c();
      if (current != null) {
         List<OrderModel> previous = this.b;
         this.b = current;
         if (previous == null) {
            current.forEach(order -> this.a.add(order.a()));
         } else if (this.c.d().b() != null) {
            for (OrderModel order2 : previous) {
               if (current.stream().noneMatch(active -> active.a().equals(order2.a()))) {
                  this.c
                     .d()
                     .b()
                     .stream()
                     .filter(model -> model.d().equalsIgnoreCase(order2.d()))
                     .filter(model2 -> model2.f() != null)
                     .findFirst()
                     .ifPresent(model3 -> model3.a((ChatModel.a)null));
               }
            }

            for (OrderModel order3 : current) {
               if (!this.a.contains(order3.a())) {
                  this.a.add(order3.a());
                  this.c
                     .d()
                     .b()
                     .stream()
                     .filter(model4 -> model4.d().equalsIgnoreCase(order3.d()))
                     .filter(model5 -> model5.f() == null)
                     .findFirst()
                     .ifPresent(
                        model6 -> {
                           if (order3.g() < this.c.f().s().c()) {
                              this.c.b(order3.a());
                              this.c
                                 .b(
                                    model6.b(),
                                    "❌ Минимальная сумма заказа — " + this.c.f().s().c().intValue() + "кк. Средства возвращены, приносим извинения!"
                                 );
                           } else {
                              this.c
                                 .b(
                                    model6.b(),
                                    "\ud83d\udc4b Здравствуйте! Это бот выдачи валюты от — westra client!\n\n\ud83e\uddf8 Для начала выдачи, пожалуйста, укажите ваш игровой никнейм:\n\nℹ️ Если заказ был создан по ошибке, напишите «Отмена» для возврата средств."
                                 );
                              model6.a(ChatModel.a.NICKNAME);
                           }
                        }
                     );
               }
            }
         }
      }
   }

   public OrderModel a(String buyerName) {
      return this.b == null ? null : this.b.stream().filter(order -> order.d().equalsIgnoreCase(buyerName)).findFirst().orElse(null);
   }

   private List<OrderModel> c() {
      List<OrderModel> result = new ArrayList<>();

      try {
         Connection_2.e response = this.c.a("https://funpay.com/orders/trade");

         for (Element order : response.j().j(".tc-item")) {
            try {
               String status = Objects.requireNonNull(order.k(".tc-status")).ac();
               String category = Objects.requireNonNull(order.k(".order-desc .text-muted")).ac();
               if (status.equalsIgnoreCase("Оплачен") && category.contains("Валюта")) {
                  String id = Objects.requireNonNull(order.k(".tc-order")).ac();
                  String name = Objects.requireNonNull(order.k(".order-desc > div")).ac();
                  String buyerName = Objects.requireNonNull(order.k(".media-user-name > span")).ac();
                  double price = Double.parseDouble(Objects.requireNonNull(order.k(".tc-price")).af().trim());
                  String unit = Objects.requireNonNull(order.k(".tc-price > span")).ac();
                  int count = 1;
                  String[] sections = name.split(",");
                  if (sections.length > 1) {
                     String section = sections[sections.length - 1].trim();
                     if (section.contains("шт.")) {
                        count = Integer.parseInt(section.split("шт.")[0].trim());
                     }
                  }

                  if (Math.abs(price / count - this.c.f().r().c().floatValue()) <= 0.010000003747573418) {
                     result.add(new OrderModel(id, category, name, buyerName, price, unit, count));
                  }
               }
            } catch (Exception var16) {
            }
         }

         return result;
      } catch (Exception var17) {
         return null;
      }
   }
}
