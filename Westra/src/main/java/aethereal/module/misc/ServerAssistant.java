package aethereal.module.misc;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.PacketEvent;
import aethereal.event.TickEvent;
import aethereal.render.AnimationUtil;
import aethereal.setting.BindSetting;
import aethereal.setting.BooleanSetting;
import aethereal.setting.ModeSetting;
import aethereal.setting.Setting;
import aethereal.util.ChatUtil;
import aethereal.util.CounterUtil;
import aethereal.util.InventoryUtil;
import aethereal.util.ServerUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;
import lombok.Generated;
import net.minecraft.class_1294;
import net.minecraft.class_1792;
import net.minecraft.class_1796;
import net.minecraft.class_1802;
import net.minecraft.class_7439;
import net.minecraft.class_7923;
import platform.inject.accessors.ItemCooldownEntryAccessor;
import platform.inject.accessors.ItemCooldownManagerAccessor;

@ModuleRegister(
   a = "Server Assistant",
   b = "Помощник, упрощающий работу с сервером и игровыми механиками",
   c = Category.Misc
)
public class ServerAssistant extends Module implements Interface {
   private List<ServerAssistant.b> w;
   private final ModeSetting b = new ModeSetting("Целевой сервер помощи", "FunTime", "FunTime", "SpookyTime", "HolyWorld");
   private final BindSetting g = this.a("Трапка", class_1802.field_22021, "FunTime", "SpookyTime");
   private final BindSetting h = this.a("Снежок заморозка", class_1802.field_8543, "FunTime", "SpookyTime");
   private final BindSetting i = this.a("Пласт", class_1802.field_8551, "FunTime", "SpookyTime");
   private final BindSetting j = this.a("Дезориентация", class_1802.field_8449, "FunTime", "SpookyTime");
   private final BindSetting k = this.a("Явная пыль", class_1802.field_8479, "FunTime", "SpookyTime");
   private final BindSetting l = this.a("Заряд ветра", class_1802.field_49098, "FunTime", "SpookyTime");
   private final BindSetting m = this.a("Огненный заряд", class_1802.field_8814, "FunTime", "SpookyTime");
   private final BindSetting n = this.a("Божья аура", class_1802.field_8614, "FunTime", "SpookyTime");
   private final BindSetting o = this.a("Взрывная штучка", class_1802.field_8814, "HolyWorld");
   private final BindSetting p = this.a("Взрывная палочка", class_1802.field_8894, "HolyWorld");
   private final BindSetting q = this.a("Взрывная трапка", class_1802.field_8662, "HolyWorld");
   private final BindSetting r = this.a("Стан", class_1802.field_8137, "HolyWorld");
   private final BindSetting s = this.a("Ком снега", class_1802.field_8543, "HolyWorld");
   private final BooleanSetting t = new BooleanSetting("Авто-божья аура", false).a(() -> this.b.l("FunTime") || this.b.l("SpookyTime"));
   private final CounterUtil v = new CounterUtil();
   private int z = -1;

   @Generated
   public List<ServerAssistant.b> q() {
      return this.w;
   }

   public ServerAssistant() {
      this.a(new Setting[]{this.b, this.g, this.h, this.i, this.j, this.k, this.m, this.l, this.n, this.o, this.p, this.q, this.r, this.s, this.t});
   }

   @EventTarget
   public void a(TickEvent event) {
      if (this.t.c() && (ServerUtil.a.a() || ServerUtil.d.a())) {
         boolean hasBadEffect = aM_.field_1724
            .method_6026()
            .stream()
            .anyMatch(effect -> effect.method_5579() == class_1294.field_5911 && effect.method_5578() >= 1 && effect.method_5584() / 20.0 >= 15.0);
         if (InventoryUtil.b(class_1802.field_8614) != -1
            && hasBadEffect
            && !aM_.field_1724.method_7357().method_7904(class_1802.field_8614.method_7854())
            && this.v.a(5000L)
            && aM_.field_1724.method_6067() <= 3.0F) {
            Westra.h().d().v().b().a(class_1802.field_8614.method_7854());
            this.v.b();
         }
      }
   }

   @EventTarget
   public void a(PacketEvent event) {
      if (event.c()
         && event.d() instanceof class_7439 class_7439VarD
         && ServerUtil.a.a()
         && class_7439VarD.comp_763().getString().equals("На этой анархии этот предмет не работает")) {
         this.z = ServerUtil.a.d();
      }
   }

   private BindSetting a(String name, class_1792 item, String... servers) {
      BindSetting setting = new BindSetting(name, -1)
         .a(
            () -> {
               var itemCooldownManagerAccessorMethod_7357 = (ItemCooldownManagerAccessor & class_1796)aM_.field_1724.method_7357();
               if (((class_1796)itemCooldownManagerAccessorMethod_7357).method_7904(item.method_7854())) {
                  Object entry = itemCooldownManagerAccessorMethod_7357.getEntries().get(class_7923.field_41178.method_10221(item));
                  Locale locale = Locale.US;
                  Object[] objArr = new Object[]{
                     entry != null
                        ? Math.max(((ItemCooldownEntryAccessor)entry).getEndTick() - itemCooldownManagerAccessorMethod_7357.getTick(), 0) / 20.0F
                        : 0.0F
                  };
                  ChatUtil.a("&c" + name + "&7 - имеет задержку &c" + String.format(locale, "%.1fс", objArr));
               } else {
                  if (InventoryUtil.b(item) == -1) {
                     ChatUtil.a("&c" + name + "&7 - нет в инвентаре");
                  } else {
                     Westra.h().d().v().b().a(item.method_7854());
                  }
               }
            }
         )
         .a(() -> {
            Stream stream = Arrays.stream(servers);
            ModeSetting modeSetting = this.b;
            return Arrays.stream(servers).anyMatch(server -> modeSetting.l(server));
         });
      if (this.w == null) {
         this.w = new ArrayList<>();
      }

      this.w.add(new ServerAssistant.b(new AnimationUtil(), setting, item));
      return setting;
   }

   public static final class b {
      private final AnimationUtil a;
      private final BindSetting b;
      private final class_1792 c;

      public b(AnimationUtil animationUtil, BindSetting bind, class_1792 item) {
         this.a = animationUtil;
         this.b = bind;
         this.c = item;
      }

      public AnimationUtil a() {
         return this.a;
      }

      public BindSetting b() {
         return this.b;
      }

      public class_1792 c() {
         return this.c;
      }
   }
}
