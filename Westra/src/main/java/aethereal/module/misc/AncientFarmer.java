package aethereal.module.misc;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.DrawEvent;
import aethereal.event.TickEvent;
import aethereal.handler.BaritoneBridge;
import aethereal.handler.InteractHandler;
import aethereal.render.ColorUtil;
import aethereal.setting.ModeSetting;
import aethereal.setting.Setting;
import aethereal.util.ChatUtil;
import aethereal.util.CounterUtil;
import aethereal.util.InventoryUtil;
import aethereal.util.Rotation;
import aethereal.util.ServerUtil;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;
import lombok.Generated;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1541;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1810;
import net.minecraft.class_1844;
import net.minecraft.class_1937;
import net.minecraft.class_1972;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2784;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_9334;
import net.minecraft.class_2338.class_2339;
import net.minecraft.class_239.class_240;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;
import platform.inject.invokers.MinecraftClientInvoker;

@ModuleRegister(
   a = "Ancient Farmer",
   b = "Автоматически фармит древние обломки в режиме полета",
   c = Category.Misc
)
public class AncientFarmer extends Module {
   private final ModeSetting b = new ModeSetting("Режим поиска территории", "Поиск сверху", "Поиск сверху", "Поиск снизу");
   private final ExecutorService c = Executors.newSingleThreadExecutor();
   private final CounterUtil d = new CounterUtil();
   private AncientFarmer.Phase e = AncientFarmer.Phase.SEARCH;
   private class_2338 f;
   private class_238 g;

   @Override
   public void b() {
      super.b();
      this.d(true);
   }

   @Override
   public void c() {
      super.c();
      this.d(false);
   }

   public AncientFarmer() {
      this.a(new Setting[]{this.b});
   }

   @EventTarget
   public void a(TickEvent event) {
      AncientFarmer.a missing = Arrays.stream(AncientFarmer.a.values()).filter(requirement -> !requirement.a()).findFirst().orElse(null);
      XRay xray = Westra.h().d().t().E();
      boolean work = missing == AncientFarmer.a.TNT && this.e != AncientFarmer.Phase.SEARCH;
      if (ServerUtil.a.d() == -1) {
      }

      if (missing != null && !work) {
         ChatUtil.a("Для работы модуля " + missing.c() + "!");
         this.a();
      } else {
         InteractHandler eat = Westra.h().d().v().k();
         int foodSlot = IntStream.range(0, 9)
            .filter(slot -> aM_.field_1724.method_31548().method_5438(slot).method_57826(class_9334.field_50075))
            .findFirst()
            .orElse(-1);
         if (!eat.a() && aM_.field_1724.method_7344().method_7586() <= 17 && foodSlot != -1) {
            eat.a(foodSlot);
         }

         int potionSlot = IntStream.range(0, 9)
            .filter(
               slot2 -> StreamSupport.<class_1293>stream(
                     ((class_1844)aM_.field_1724.method_31548().method_5438(slot2).method_57825(class_9334.field_49651, class_1844.field_49274))
                        .method_57397()
                        .spliterator(),
                     false
                  )
                  .anyMatch(effect -> effect.method_5579() == class_1294.field_5918)
            )
            .findFirst()
            .orElse(-1);
         if (!eat.a()
            && potionSlot != -1
            && (!aM_.field_1724.method_6059(class_1294.field_5918) || aM_.field_1724.method_6112(class_1294.field_5918).method_5584() <= 100)) {
            eat.a(potionSlot);
         }

         if (eat.a()) {
            BaritoneBridge.d();
         } else if (!xray.m()) {
            xray.a();
         } else {
            boolean near = this.g != null && aM_.field_1724.method_24515().method_19771(class_2338.method_49638(this.g.method_1005()), 2.5);
            boolean primed = !aM_.field_1687.method_8390(class_1541.class, aM_.field_1724.method_5829().method_1014(8.0), t -> true).isEmpty();
            this.f = null;
            switch (this.e) {
               case SEARCH:
                  if (!xray.s().isEmpty()) {
                     ChatUtil.a("Вскапываем обломки найденные по пути");
                     this.e = AncientFarmer.Phase.MINE;
                  } else if (this.g == null) {
                     this.c.execute(() -> {
                        if (this.g == null && aM_.field_1724.field_6012 > 20) {
                           ChatUtil.a("Переходим к поиску новой территории.");
                           this.g = this.q();
                        }
                     });
                  } else if (near) {
                     BaritoneBridge.d();
                     this.e = AncientFarmer.Phase.TNT;
                  } else if (!BaritoneBridge.e()) {
                     BaritoneBridge.a(class_2338.method_49638(this.g.method_1005()));
                  }
                  break;
               case TNT:
                  double reach = aM_.field_1724.method_55754();
                  class_2338 tnt = class_2338.method_25998(aM_.field_1724.method_24515(), (int)reach, (int)reach, (int)reach)
                     .filter(pos2 -> aM_.field_1687.method_8320(pos2).method_27852(class_2246.field_10375))
                     .map(v0 -> v0.method_10062())
                     .findFirst()
                     .orElse(null);
                  if (primed) {
                     this.e = AncientFarmer.Phase.RETREAT;
                  } else if (tnt != null) {
                     this.f = tnt;
                     int flint = InventoryUtil.a(class_1802.field_8884, true);
                     if (flint != -1) {
                        class_243 eye = aM_.field_1724.method_33571();
                        class_243 center = class_243.method_24953(tnt);
                        class_243 aim = Arrays.stream(class_2350.values())
                           .map(
                              side -> center.method_1031(
                                 side.method_10148() * 0.4499999185117763, side.method_10164() * 0.4499999185117763, side.method_10165() * 0.4499999185117763
                              )
                           )
                           .filter(point -> eye.method_1022(point) <= reach)
                           .filter(
                              point2 -> {
                                 class_3965 trace = aM_.field_1687
                                    .method_17742(new class_3959(eye, point2, class_3960.field_17558, class_242.field_1348, aM_.field_1724));
                                 return trace.method_17783() == class_240.field_1332 && trace.method_17777().equals(tnt);
                              }
                           )
                           .min((a2, b2) -> Double.compare(eye.method_1025(a2), eye.method_1025(b2)))
                           .orElse(null);
                        if (aim != null) {
                           Westra.h().d().k().a(Rotation.a(eye, aim), 180.0F, 0, 1);
                           if (new Rotation(aM_.field_1724).a(Rotation.b()) < 1.0 && aM_.field_1724.field_6012 % 5 == 0) {
                              aM_.field_1724.method_31548().field_7545 = flint;
                              if (aM_.field_1765 instanceof class_3965 class_3965Var
                                 && class_3965Var.method_17783() == class_240.field_1332
                                 && class_3965Var.method_17777().equals(tnt)) {
                                 ((MinecraftClientInvoker)aM_).invokeDoItemUse();
                              }
                           }
                        }
                     }
                  } else {
                     class_243 eye2 = aM_.field_1724.method_33571();
                     class_2338 target = class_2338.method_25998(aM_.field_1724.method_24515(), (int)reach, (int)reach, (int)reach)
                        .filter(pos3 -> aM_.field_1687.method_8320(pos3).method_45474())
                        .filter(pos4 -> aM_.field_1687.method_8320(pos4.method_10074()).method_26212(aM_.field_1687, pos4.method_10074()))
                        .filter(
                           pos5 -> !aM_.field_1724
                              .method_5829()
                              .method_18804(aM_.field_1724.method_18798())
                              .method_1014(0.10000001882007822)
                              .method_994(new class_238(pos5))
                        )
                        .filter(
                           pos6 -> {
                              class_243 hitVec = new class_243(pos6.method_10263() + 0.5, pos6.method_10264(), pos6.method_10260() + 0.5);
                              if (!(eye2.method_1022(hitVec) > reach) && !(eye2.method_1020(hitVec).method_1029().field_1351 <= 0.0)) {
                                 class_3965 hit2 = aM_.field_1687
                                    .method_17742(new class_3959(eye2, hitVec, class_3960.field_17558, class_242.field_1348, aM_.field_1724));
                                 return hit2.method_17783() != class_240.field_1332 || hit2.method_17777().equals(pos6.method_10074());
                              } else {
                                 return false;
                              }
                           }
                        )
                        .map(v0 -> v0.method_10062())
                        .min((a3, b3) -> Double.compare(eye2.method_1025(a3.method_46558()), eye2.method_1025(b3.method_46558())))
                        .orElse(null);
                     this.f = target;
                     int slot3 = InventoryUtil.a(class_1802.field_8626, true);
                     if (target != null) {
                        if (slot3 != -1) {
                           class_2338 support = target.method_10074();
                           Westra.h()
                              .d()
                              .k()
                              .a(
                                 Rotation.a(eye2, new class_243(support.method_10263() + 0.5, support.method_10264() + 1, support.method_10260() + 0.5)),
                                 180.0F,
                                 0,
                                 1
                              );
                           if (new Rotation(aM_.field_1724).a(Rotation.b()) < 1.0 && aM_.field_1724.field_6012 % 5 == 0) {
                              if (aM_.field_1724.method_31548().field_7545 != slot3) {
                                 aM_.field_1724.method_31548().field_7545 = slot3;
                              }

                              if (aM_.field_1765 instanceof class_3965 class_3965Var2
                                 && class_3965Var2.method_17783() == class_240.field_1332
                                 && class_3965Var2.method_17777().equals(support)
                                 && class_3965Var2.method_17780() == class_2350.field_11036) {
                                 ((MinecraftClientInvoker)aM_).invokeDoItemUse();
                              }
                           }
                        }
                     } else if (!BaritoneBridge.e()) {
                        class_2338 feet = aM_.field_1724.method_24515();
                        class_2338 stand = class_2338.method_25998(feet, 16, 8, 16)
                           .filter(pos7 -> !pos7.equals(feet))
                           .filter(pos8 -> aM_.field_1687.method_8320(pos8.method_10074()).method_26212(aM_.field_1687, pos8.method_10074()))
                           .filter(pos9 -> aM_.field_1687.method_8320(pos9).method_45474() && aM_.field_1687.method_8320(pos9.method_10084()).method_45474())
                           .filter(
                              pos10 -> aM_.field_1687.method_8320(pos10).method_26227().method_15769()
                                 && aM_.field_1687.method_8320(pos10.method_10084()).method_26227().method_15769()
                           )
                           .map(v0 -> v0.method_10062())
                           .min((a4, b4) -> Double.compare(feet.method_10262(a4), feet.method_10262(b4)))
                           .orElse(null);
                        if (stand != null) {
                           BaritoneBridge.a(stand);
                        } else {
                           this.e = AncientFarmer.Phase.SEARCH;
                        }
                     }
                  }
                  break;
               case RETREAT:
                  List<class_1541> burning = aM_.field_1687.method_8390(class_1541.class, aM_.field_1724.method_5829().method_1014(32.0), t2 -> true);
                  if (burning.isEmpty()) {
                     ChatUtil.a("Ожидаем обломки, и начинаем вскапывать");
                     BaritoneBridge.d();
                     this.g = null;
                     this.e = AncientFarmer.Phase.MINE;
                     this.d.b();
                  } else if (!BaritoneBridge.e()) {
                     BaritoneBridge.a(20.0, burning.stream().map(v0 -> v0.method_24515()).toArray(class_2338[]::new));
                  }
                  break;
               case MINE:
                  if (this.d.a(1000L)) {
                     if (!xray.s().isEmpty()) {
                        if (!BaritoneBridge.b()) {
                           BaritoneBridge.a("ancient_debris");
                           Westra.h()
                              .f()
                              .a(
                                 false,
                                 "telegram",
                                 "message",
                                 "⛏️ AncientFarmer — Найдены древние обломки!\n\n\ud83d\udccd Позиций для добычи: %s\n".formatted(xray.s().size())
                              );
                        }
                     } else if (!BaritoneBridge.b()) {
                        this.e = AncientFarmer.Phase.SEARCH;
                     }
                  }
            }
         }
      }
   }

   @EventTarget
   public void a(DrawEvent draw) {
      if (draw.c() && this.f != null) {
         draw.e().a(draw.h(), new class_238(this.f), ColorUtil.a(230, 90, 70, 150), 1.0F);
      }
   }

   private class_238 q() {
      int reach = (int)((Math.sqrt(aM_.field_1687.method_2935().method_14151()) - 1.0) / 2.0) * 16;
      class_2338 feet = aM_.field_1724.method_24515();
      class_2784 border = aM_.field_1687.method_8621();
      int bottom = this.b.l("Поиск сверху") ? 90 : 20;
      int top = this.b.l("Поиск сверху") ? 120 : 60;
      class_238 best = null;
      double bestScore = -1.0;
      double bestFill = 0.0;
      class_2339 pos = new class_2339();

      for (int x = feet.method_10263() - reach; x <= feet.method_10263() + reach; x += 8) {
         for (int z = feet.method_10260() - reach; z <= feet.method_10260() + reach; z += 8) {
            if (aM_.field_1687.method_2935().method_12123(x >> 4, z >> 4) && border.method_35317(x - 20, z - 20) && border.method_35317(x + 20, z + 20)) {
               for (int y = bottom; y <= top; y += 8) {
                  int solid = 0;
                  int total = 0;
                  boolean badBiome = false;

                  for (int dx = -20; dx <= 20 && !badBiome; dx += 4) {
                     for (int dy = -20; dy <= 20 && !badBiome; dy += 4) {
                        for (int dz = -20; dz <= 20; dz += 4) {
                           pos.method_10103(x + dx, y + dy, z + dz);
                           if (aM_.field_1687.method_23753(pos).method_40225(class_1972.field_23859)
                              || aM_.field_1687.method_23753(pos).method_40225(class_1972.field_22075)) {
                              badBiome = true;
                              break;
                           }

                           total++;
                           if (!aM_.field_1687.method_8320(pos).method_26215() && aM_.field_1687.method_8320(pos).method_26227().method_15769()) {
                              solid++;
                           }
                        }
                     }
                  }

                  if (!badBiome && total > 0) {
                     double score = (double)solid / total - feet.method_40081(x, y, z) * 9.99999555911002E-10;
                     class_2338 anchor;
                     if (score > bestScore
                        && (
                              anchor = class_2338.method_25998(new class_2338(x, y, z), 20, 20, 20)
                                 .filter(
                                    candidate -> !aM_.field_1687.method_8320(candidate).method_26215()
                                       && aM_.field_1687.method_8320(candidate).method_26227().method_15769()
                                 )
                                 .map(v0 -> v0.method_10062())
                                 .findFirst()
                                 .orElse(null)
                           )
                           != null) {
                        bestScore = score;
                        bestFill = (double)solid / total;
                        best = new class_238(
                           anchor.method_10263() - 20,
                           anchor.method_10264() - 20,
                           anchor.method_10260() - 20,
                           anchor.method_10263() + 20,
                           anchor.method_10264() + 20,
                           anchor.method_10260() + 20
                        );
                     }
                  }
               }
            }
         }
      }

      if (best != null) {
         long jRound = Math.round(bestFill * 100.0);
         Math.round(Math.sqrt(feet.method_19770(best.method_1005())));
         ChatUtil.a("Успешность: &c" + jRound + "%&7, до неё &c" + jRound + "&7 блоков");
      }

      return best;
   }

   public void d(boolean status) {
      BaritoneBridge.d();
      BaritoneBridge.a("blockFreeLook", Boolean.TRUE);
      BaritoneBridge.a("allowBreak", Boolean.TRUE);
      BaritoneBridge.a("freeLook", status);
      BaritoneBridge.a("randomLooking", 1.0);
      BaritoneBridge.a("randomLooking113", 1.0);
      this.g = null;
      this.e = AncientFarmer.Phase.SEARCH;
   }

   static enum Phase {
      SEARCH,
      TNT,
      RETREAT,
      MINE;
   }

   static enum a {
      FLY("необходимо включить режим полёта (/fly)", () -> Interface.aM_.field_1724.method_31549().field_7478),
      NETHER("необходимо находиться в Незере", () -> Interface.aM_.field_1687.method_27983() == class_1937.field_25180),
      FOOD("в хотбаре должна быть еда", stack -> stack.method_57826(class_9334.field_50075)),
      TNT("в хотбаре должен быть динамит", stack2 -> stack2.method_31574(class_1802.field_8626)),
      FLINT_AND_STEEL("в хотбаре должно быть огниво", stack3 -> stack3.method_31574(class_1802.field_8884)),
      PICKAXE(
         "в хотбаре должна быть кирка с прочностью больше 5%",
         stack4 -> stack4.method_7909() instanceof class_1810 && stack4.method_7936() - stack4.method_7919() > stack4.method_7936() * 0.050000008964116646
      ),
      FIRE_RESISTANCE(
         "в хотбаре должна быть огнестойкость",
         stack5 -> StreamSupport.<class_1293>stream(
               ((class_1844)stack5.method_57825(class_9334.field_49651, class_1844.field_49274)).method_57397().spliterator(), false
            )
            .anyMatch(effect -> effect.method_5579() == class_1294.field_5918)
      );

      private final BooleanSupplier h;
      private final String i;

      @Generated
      public BooleanSupplier b() {
         return this.h;
      }

      @Generated
      public String c() {
         return this.i;
      }

      private a(String description, BooleanSupplier condition) {
         this.i = description;
         this.h = condition;
      }

      private a(String description, Predicate<class_1799> predicate) {
         this(description, () -> IntStream.range(0, 9).anyMatch(slot -> predicate.test(Interface.aM_.field_1724.method_31548().method_5438(slot))));
      }

      public boolean a() {
         return this.h.getAsBoolean();
      }
   }
}
