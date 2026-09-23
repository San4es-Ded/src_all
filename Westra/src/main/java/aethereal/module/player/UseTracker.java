package aethereal.module.player;

import aethereal.config.ThemeInfo;
import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.PacketEvent;
import aethereal.event.PotionEvent;
import aethereal.event.TickEvent;
import aethereal.module.render.EntityESP;
import aethereal.notification.Notification;
import aethereal.setting.BooleanSetting;
import aethereal.setting.MultiModeSetting;
import aethereal.setting.Setting;
import aethereal.util.ChatUtil;
import aethereal.util.MathUtil;
import aethereal.util.ServerUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import lombok.Generated;
import net.minecraft.class_1291;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1322;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1812;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2561;
import net.minecraft.class_2583;
import net.minecraft.class_2663;
import net.minecraft.class_2781;
import net.minecraft.class_3532;
import net.minecraft.class_5250;
import net.minecraft.class_6880;
import net.minecraft.class_746;
import net.minecraft.class_9334;
import net.minecraft.class_1322.class_1323;
import net.minecraft.class_2781.class_2782;

@ModuleRegister(
   a = "Use Tracker",
   b = "Отслеживает выбранные использования и уведомляет о них",
   c = Category.Player
)
public class UseTracker extends Module {
   private final MultiModeSetting b = new MultiModeSetting(
      "Отслеживать использования", new BooleanSetting("Тотема", true), new BooleanSetting("Зелья", true), new BooleanSetting("Предмета", true)
   );

   public UseTracker() {
      this.a(new Setting[]{this.b});
   }

   @EventTarget
   public void a(TickEvent event) {
      if (this.b.a("Предмета").c()) {
         for (class_1297 _e : aM_.field_1687.method_18112()) {
            if (_e instanceof class_1657 class_746Var2 && class_746Var2 != aM_.field_1724) {
               class_1799 active = class_746Var2.method_6030();
               if ((
                     active.method_7909() instanceof class_1812
                        || active.method_57824(class_9334.field_50075) != null
                        || active.method_7909() == class_1802.field_8103
                  )
                  && class_746Var2.method_6014() == 1) {
                  String color = active.method_7909() instanceof class_1812 ? "&a" : "&c";
                  if (active.method_31574(class_1802.field_8103)) {
                     Westra.h().d().t().aa().q().removeIf(info -> info.b() == class_746Var2.method_5628());
                  }

                  ChatUtil.a(
                     "[" + this.j() + "]",
                     class_746Var2.method_5477().getString() + " использовал \"" + color + active.method_7909().method_63680().getString() + "&7\""
                  );
                  Westra.h()
                     .d()
                     .m()
                     .a(
                        new Notification(
                           active.method_7972(),
                           class_746Var2.method_5477().getString() + " использовал " + active.method_7909().method_63680().getString(),
                           1500
                        )
                     );
               }
            }
         }
      }
   }

   @EventTarget
   public void a(PotionEvent event) {
      if (this.b.a("Зелья").c() && event.b() == PotionEvent.a.PARTICLES && aM_.field_1687 != null) {
         for (UseTracker.a type : UseTracker.a.values()) {
            for (int color : type.d()) {
               if ((color & 16777215) == (event.c() & 16777215)) {
                  class_2338 pos = event.d();
                  class_243 splash = pos.method_46558();
                  class_238 box = new class_238(
                     pos.method_10263() - 4,
                     pos.method_10264() - 4,
                     pos.method_10260() - 4,
                     pos.method_10263() + 5,
                     pos.method_10264() + 5,
                     pos.method_10260() + 5
                  );

                  for (class_1657 class_746Var : aM_.field_1687.method_8390(class_1657.class, box, v0 -> v0.method_5805())) {
                     class_238 boundingBox = class_746Var.method_5829();
                     double factor = 1.0
                        - Math.sqrt(
                              Math.pow(splash.field_1352 - class_3532.method_15350(splash.field_1352, boundingBox.field_1323, boundingBox.field_1320), 2.0)
                                 + Math.pow(splash.field_1351 - class_3532.method_15350(splash.field_1351, boundingBox.field_1322, boundingBox.field_1325), 2.0)
                                 + Math.pow(
                                    splash.method_10215() - class_3532.method_15350(splash.method_10215(), boundingBox.field_1321, boundingBox.field_1324), 2.0
                                 )
                           )
                           / 4.0;
                     if (factor > 0.0) {
                        List<class_1293> effects = new ArrayList<>();
                        if (class_746Var != aM_.field_1724) {
                           ChatUtil.a(
                              (Object)("[" + this.j() + "]"),
                              (class_2561)ChatUtil.b(class_746Var.method_5477().getString() + " получил эффекты от \"")
                                 .method_10852(type.a())
                                 .method_10852(ChatUtil.b("\""))
                           );
                           ChatUtil.a("[" + this.j() + "]", "- Успешность: &a" + (int)(factor * 100.0) + "%");
                        }

                        class_5250 class_5250VarMethod_10852;
                        if (class_746Var == aM_.field_1724) {
                           class_5250VarMethod_10852 = class_2561.method_43470("Вы получили эффекты от ")
                              .method_27694(style -> style.method_36139(Westra.h().d().o().a(ThemeInfo.PRIMARY).a()))
                              .method_10852(type.a())
                              .method_10852(ChatUtil.b(" &7(" + (int)(factor * 100.0) + "%)"));
                        } else {
                           class_5250VarMethod_10852 = ChatUtil.b(class_746Var.method_5477().getString() + " получил эффекты от ")
                              .method_10852(type.a())
                              .method_10852(ChatUtil.b(" &7(" + (int)(factor * 100.0) + "%)"));
                        }

                        Westra.h().d().m().a(new Notification("o", class_5250VarMethod_10852, 2000));

                        for (Entry<class_6880<class_1291>, int[]> entry : type.b()) {
                           int duration = Math.max(0, class_3532.method_15357(entry.getValue()[0] * factor + 0.5));
                           int amplifier = entry.getValue()[1];
                           if (duration > 20) {
                              int sec = duration / 20;
                              if (class_746Var != aM_.field_1724) {
                                 ChatUtil.a(
                                    "[" + this.j() + "]",
                                    "- &c"
                                       + ((class_1291)entry.getKey().comp_349()).method_5560().getString()
                                       + " "
                                       + MathUtil.a(amplifier)
                                       + " &7("
                                       + sec / 60
                                       + ":"
                                       + String.format("%02d", sec % 60)
                                       + ")"
                                 );
                              }

                              if (entry.getKey().equals(class_1294.field_5919)
                                 || entry.getKey().equals(class_1294.field_5910)
                                 || entry.getKey().equals(class_1294.field_5909)
                                 || entry.getKey().equals(class_1294.field_5920)
                                 || entry.getKey().equals(class_1294.field_5899)
                                 || entry.getKey().equals(class_1294.field_5911)
                                 || entry.getKey().equals(class_1294.field_5924)
                                 || entry.getKey().equals(class_1294.field_5914)
                                 || entry.getKey().equals(class_1294.field_5907)) {
                                 effects.add(new class_1293(entry.getKey(), duration, amplifier));
                              }
                           }
                        }

                        if (!effects.isEmpty()) {
                           Westra.h().d().t().aa().q().add(new EntityESP.a(List.copyOf(effects), class_746Var.method_5628(), class_746Var.field_6012));
                        }
                     }
                  }

                  return;
               }
            }
         }
      }
   }

   @EventTarget
   public void a(PacketEvent event) {
      if (event.c()) {
         if (event.d() instanceof class_2781 class_2781VarD) {
            class_2781 packet = class_2781VarD;

            for (class_2782 entry : class_2781VarD.method_11938()) {
               if (entry.comp_2177().method_40230().toString().contains("minecraft:movement_speed")) {
                  for (class_1322 modifier : entry.comp_2179()) {
                     if (aM_.field_1687.method_8469(packet.method_11937()) instanceof class_1657
                        && modifier.comp_2447().toString().equals("minecraft:effect.speed")
                        && modifier.comp_2449() <= 0.40000001199465773
                        && modifier.comp_2450() == class_1323.field_6331) {
                        Westra.h().d().t().aa().q().removeIf(info -> info.b() == packet.method_11937());
                     }
                  }
               }
            }
         }

         if (event.d() instanceof class_2663 class_2663VarD && class_2663VarD.method_11469(aM_.field_1687) instanceof class_1309 class_746VarMethod_11469) {
            class_746 class_746Var = (class_746)class_746VarMethod_11469;
            if (class_2663VarD.method_11470() == 35) {
               Westra.h().d().t().aa().q().removeIf(info2 -> info2.b() == class_746Var.method_5628());
               if (this.b.a("Тотема").c()) {
                  class_1799 totem = class_746Var.method_6047().method_7909() == class_1802.field_8288
                     ? class_746Var.method_6047()
                     : (class_746Var.method_6079().method_7909() == class_1802.field_8288 ? class_746Var.method_6079() : null);
                  if (totem != null) {
                     String name = ServerUtil.a.a() ? ServerUtil.a.b(totem) : totem.method_7964().getString();
                     ChatUtil.a(
                        "[" + this.j() + "]",
                        (class_746Var == aM_.field_1724 ? "Вы потеряли " : class_746Var.method_5477().getString() + " потерял ")
                           + name
                           + ", зачарован: "
                           + (!name.startsWith("Талисман") && !totem.method_7958() ? "&c●&7" : "&a●&7")
                     );
                  }
               }
            }
         }
      }
   }

   public static enum a {
      POPPER_POTION(
         List.of(
            Map.entry(class_1294.field_5909, new int[]{200, 9}),
            Map.entry(class_1294.field_5904, new int[]{300, 4}),
            Map.entry(class_1294.field_5919, new int[]{100, 9}),
            Map.entry(class_1294.field_5912, new int[]{3600, 0})
         ),
         "[★] Хлопушка",
         new int[]{16738740},
         new int[]{16711765, 16727869, 16743972, 16760076, 14410269, 9628759, 4846994, 65484}
      ),
      HOLY_WATER(
         List.of(
            Map.entry(class_1294.field_5924, new int[]{900, 1}),
            Map.entry(class_1294.field_5905, new int[]{12000, 1}),
            Map.entry(class_1294.field_5915, new int[]{0, 2})
         ),
         "[★] Святая вода",
         new int[]{16777215},
         new int[]{16777163, 16777148, 16777132, 16777117, 16777102, 16777087, 16776815, 16776800, 16776785, 16776769, 16776754}
      ),
      RAGE_POTION(
         List.of(Map.entry(class_1294.field_5910, new int[]{600, 4}), Map.entry(class_1294.field_5909, new int[]{600, 3})),
         "[★] Зелье Гнева",
         new int[]{10040115},
         new int[]{9109504, 10620416, 12131328, 13707520, 15218432, 16729344, 16732928, 16736512, 16740352, 16743936, 16747520}
      ),
      PALLADIN_POTION(
         List.of(
            Map.entry(class_1294.field_5907, new int[]{12000, 0}),
            Map.entry(class_1294.field_5918, new int[]{12000, 0}),
            Map.entry(class_1294.field_5914, new int[]{1200, 2}),
            Map.entry(class_1294.field_5905, new int[]{18000, 2})
         ),
         "[★] Зелье Палладина",
         new int[]{65535},
         new int[]{
            13762395,
            14090092,
            14417789,
            14745486,
            15007648,
            15335345,
            15663042,
            15990739,
            15663042,
            15335345,
            15007648,
            14745486,
            14417789,
            14090092,
            13762395
         }
      ),
      ASSASSIN_POTION(
         List.of(
            Map.entry(class_1294.field_5910, new int[]{1200, 3}),
            Map.entry(class_1294.field_5904, new int[]{6000, 2}),
            Map.entry(class_1294.field_5917, new int[]{1200, 0}),
            Map.entry(class_1294.field_5921, new int[]{0, 1})
         ),
         "[★] Зелье Ассасина",
         new int[]{3355443},
         new int[]{4277061, 4603456, 4929850, 5256245, 5516848, 5843242, 6169637, 6496032, 6822427, 7148821, 7409424, 7735819, 8062213, 8388608}
      ),
      RADIATION_POTION(
         List.of(
            Map.entry(class_1294.field_5899, new int[]{1200, 1}),
            Map.entry(class_1294.field_5920, new int[]{1200, 1}),
            Map.entry(class_1294.field_5909, new int[]{1800, 2}),
            Map.entry(class_1294.field_5903, new int[]{1200, 4}),
            Map.entry(class_1294.field_5912, new int[]{2400, 0})
         ),
         "[★] Зелье Радиации",
         new int[]{3329330},
         new int[]{16774970, 16250192, 15659878, 15135100, 14545043, 14020265, 13429951, 12905919, 12382378, 11858836, 11269759, 10746217, 10222676, 9699134}
      ),
      SLEEPING_PILL(
         List.of(
            Map.entry(class_1294.field_5911, new int[]{1800, 1}),
            Map.entry(class_1294.field_5901, new int[]{200, 1}),
            Map.entry(class_1294.field_5920, new int[]{1800, 2}),
            Map.entry(class_1294.field_5919, new int[]{200, 0})
         ),
         "[★] Снотворное",
         new int[]{255, 4737096},
         new int[]{4132250, 3219615, 2306725, 1394090, 481455, 812728, 2322884, 3833041, 5408733, 6918889}
      );

      private final List<Entry<class_6880<class_1291>, int[]>> h;
      private final String i;
      private final int[] j;
      private final int[] k;

      @Generated
      private a(final List effects, final String displayName, final int[] throwColor, final int[] nameColors) {
         this.h = effects;
         this.i = displayName;
         this.j = throwColor;
         this.k = nameColors;
      }

      @Generated
      public List<Entry<class_6880<class_1291>, int[]>> b() {
         return this.h;
      }

      @Generated
      public String c() {
         return this.i;
      }

      @Generated
      public int[] d() {
         return this.j;
      }

      @Generated
      public int[] e() {
         return this.k;
      }

      public class_5250 a() {
         int start = this.i.indexOf(32) + 1;
         class_5250 text = class_2561.method_43470("");

         for (int i = 0; i < this.i.length(); i++) {
            int color = i < start ? this.k[0] : this.k[Math.min(i - start, this.k.length - 1)];
            text.method_10852(
               class_2561.method_43470(String.valueOf(this.i.charAt(i))).method_10862(class_2583.field_24360.method_36139(color).method_10982(true))
            );
         }

         return text;
      }
   }
}
