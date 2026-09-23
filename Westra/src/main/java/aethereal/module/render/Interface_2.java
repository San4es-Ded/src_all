package aethereal.module.render;

import aethereal.config.ThemeInfo;
import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.GlobalEvent;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.BackendEvent;
import aethereal.event.DrawEvent;
import aethereal.event.PacketEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.ColorSetting;
import aethereal.setting.ModeSetting;
import aethereal.setting.MultiModeSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import aethereal.ui.widget.ArmorWidget;
import aethereal.ui.widget.CooldownsWidget;
import aethereal.ui.widget.EnvironmentWidget;
import aethereal.ui.widget.HotkeysWidget;
import aethereal.ui.widget.HudScale;
import aethereal.ui.widget.ItemsWidget;
import aethereal.ui.widget.NotificationWidget;
import aethereal.ui.widget.PotionWidget;
import aethereal.ui.widget.StaffWidget;
import aethereal.ui.widget.TargetWidget;
import aethereal.ui.widget.WatermarkWidget;
import aethereal.ui.widget.WestraEnvironmentWidget;
import aethereal.ui.widget.Widget;
import aethereal.ui.widget.kimiko.KimikoArmorWidget;
import aethereal.ui.widget.kimiko.KimikoHotkeysWidget;
import aethereal.ui.widget.kimiko.KimikoKeyStrokesWidget;
import aethereal.ui.widget.kimiko.KimikoPotionsWidget;
import aethereal.ui.widget.kimiko.KimikoTargetWidget;
import aethereal.ui.widget.kimiko.KimikoWatermarkWidget;
import aethereal.ui.widget.pulse.PulseArmorWidget;
import aethereal.ui.widget.pulse.PulseHotkeysWidget;
import aethereal.ui.widget.pulse.PulsePotionWidget;
import aethereal.ui.widget.pulse.PulseStaffWidget;
import aethereal.ui.widget.pulse.PulseTargetWidget;
import aethereal.ui.widget.pulse.PulseWatermarkWidget;
import aethereal.ui.widget.system.SystemArmorWidget;
import aethereal.ui.widget.system.SystemHotkeysWidget;
import aethereal.ui.widget.system.SystemPotionWidget;
import aethereal.ui.widget.system.SystemStaffWidget;
import aethereal.ui.widget.system.SystemTargetWidget;
import aethereal.ui.widget.system.SystemWatermarkWidget;
import aethereal.ui.widget.westra.WestraArmorWidget;
import aethereal.ui.widget.westra.WestraCooldownsWidget;
import aethereal.ui.widget.westra.WestraHotkeysWidget;
import aethereal.ui.widget.westra.WestraItemsWidget;
import aethereal.ui.widget.westra.WestraNotificationWidget;
import aethereal.ui.widget.westra.WestraPotionWidget;
import aethereal.ui.widget.westra.WestraStaffWidget;
import aethereal.ui.widget.westra.WestraTargetWidget;
import aethereal.ui.widget.westra.WestraWatermarkWidget;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;

@ModuleRegister(
   a = "Interface",
   b = "Отображает выбранные элементы интерфейса на экране",
   c = Category.Render
)
public class Interface_2 extends Module {
   private final ColorSetting b = new ColorSetting("Глобальный цвет интерфейса", Westra.h().d().o().a(ThemeInfo.PRIMARY).a())
      .a(color -> Westra.h().d().o().a(ThemeInfo.PRIMARY).a(color));
   private final ModeSetting e = new ModeSetting("Стиль", "Recode", "Recode", "Классика", "System", "Pulse", "Kimiko");
   private final SliderSetting D = new SliderSetting("Размер интерфейса", 1.0F, 0.5F, 2.0F, 0.05F);
   private final BooleanSetting E = new BooleanSetting("Следовать масштабу игры", false);
   private final MultiModeSetting c = new MultiModeSetting(
      "Элементы интерфейса",
      new BooleanSetting("Клавиши", true),
      new BooleanSetting("Таргет-худ", true),
      new BooleanSetting("Задержки", true),
      new BooleanSetting("Инфо-панель", true),
      new BooleanSetting("Уведомления", true),
      new BooleanSetting("Зелья", true),
      new BooleanSetting("Предметы", true),
      new BooleanSetting("Броня", true),
      new BooleanSetting("Стафф", true),
      new BooleanSetting("Окружение", true)
   );
   private final List<Widget> d = new ArrayList<>();
   private final List<Widget> f = new ArrayList<>();
   private final WatermarkWidget g = new WatermarkWidget();
   private final HotkeysWidget h = new HotkeysWidget();
   private final ArmorWidget k = new ArmorWidget();
   private final TargetWidget l = new TargetWidget();
   private final PotionWidget n = new PotionWidget();
   private final StaffWidget o = new StaffWidget();
   private final SystemWatermarkWidget i = new SystemWatermarkWidget();
   private final SystemHotkeysWidget j = new SystemHotkeysWidget();
   private final SystemArmorWidget p = new SystemArmorWidget();
   private final SystemTargetWidget s = new SystemTargetWidget();
   private final SystemPotionWidget t = new SystemPotionWidget();
   private final SystemStaffWidget u = new SystemStaffWidget();
   private final List<Widget> v = new ArrayList<>();
   private final PulseWatermarkWidget w = new PulseWatermarkWidget();
   private final PulseHotkeysWidget y = new PulseHotkeysWidget();
   private final PulseArmorWidget z = new PulseArmorWidget();
   private final PulseTargetWidget A = new PulseTargetWidget();
   private final PulsePotionWidget B = new PulsePotionWidget();
   private final PulseStaffWidget C = new PulseStaffWidget();
   private final List<Widget> F = new ArrayList<>();
   private final WestraWatermarkWidget G = new WestraWatermarkWidget();
   private final WestraHotkeysWidget H = new WestraHotkeysWidget();
   private final WestraArmorWidget I = new WestraArmorWidget();
   private final WestraTargetWidget J = new WestraTargetWidget();
   private final WestraPotionWidget K = new WestraPotionWidget();
   private final WestraStaffWidget L = new WestraStaffWidget();
   private final WestraCooldownsWidget M = new WestraCooldownsWidget();
   private final WestraItemsWidget N = new WestraItemsWidget();
   private final WestraNotificationWidget O = new WestraNotificationWidget();
   private final WestraEnvironmentWidget P = new WestraEnvironmentWidget();
   private final CooldownsWidget Q = new CooldownsWidget();
   private final ItemsWidget R = new ItemsWidget();
   private final NotificationWidget S = new NotificationWidget();
   private final EnvironmentWidget T = new EnvironmentWidget();
   private final List<Widget> U = new ArrayList<>();
   private final KimikoWatermarkWidget V2 = new KimikoWatermarkWidget();
   private final KimikoHotkeysWidget W2 = new KimikoHotkeysWidget();
   private final KimikoTargetWidget X2 = new KimikoTargetWidget();
   private final KimikoKeyStrokesWidget Y2 = new KimikoKeyStrokesWidget();
   private final KimikoArmorWidget Z2 = new KimikoArmorWidget();
   private final KimikoPotionsWidget a2 = new KimikoPotionsWidget();

   @Generated
   public List<Widget> q() {
      return this.r();
   }

   public Interface_2() {
      this.a(new Setting[]{this.b, this.e, this.D, this.E, this.c});
      List<Widget> shared = new ArrayList<>();
      shared.add(this.Q);
      shared.add(this.R);
      shared.add(this.S);
      shared.add(this.T);
      this.d.addAll(shared);
      this.d.add(this.g);
      this.d.add(this.h);
      this.d.add(this.k);
      this.d.add(this.l);
      this.d.add(this.n);
      this.d.add(this.o);
      this.f.addAll(shared);
      this.f.add(this.i);
      this.f.add(this.j);
      this.f.add(this.p);
      this.f.add(this.s);
      this.f.add(this.t);
      this.f.add(this.u);
      this.v.addAll(shared);
      this.v.add(this.w);
      this.v.add(this.y);
      this.v.add(this.z);
      this.v.add(this.A);
      this.v.add(this.B);
      this.v.add(this.C);
      this.F.add(this.G);
      this.F.add(this.H);
      this.F.add(this.I);
      this.F.add(this.J);
      this.F.add(this.K);
      this.F.add(this.L);
      this.F.add(this.M);
      this.F.add(this.N);
      this.F.add(this.O);
      this.F.add(this.P);
      this.U.addAll(shared);
      this.U.add(this.V2);
      this.U.add(this.W2);
      this.U.add(this.X2);
      this.U.add(this.Y2);
      this.U.add(this.Z2);
      this.U.add(this.a2);
   }

   public void q(int color) {
      this.b.a(color);
      Westra.h().d().o().a(ThemeInfo.PRIMARY).a(color);
   }

   private List<Widget> r() {
      if (this.e.l("System")) {
         return this.f;
      } else if (this.e.l("Pulse")) {
         return this.v;
      } else if (this.e.l("Kimiko")) {
         return this.U;
      } else {
         return this.e.l("Классика") ? this.d : this.F;
      }
   }

   private void s() {
      List<Widget> active = this.r();

      for (Widget widget : new Widget[]{
         this.g,
         this.h,
         this.k,
         this.l,
         this.n,
         this.o,
         this.i,
         this.j,
         this.p,
         this.s,
         this.t,
         this.u,
         this.w,
         this.y,
         this.z,
         this.A,
         this.B,
         this.C,
         this.G,
         this.H,
         this.I,
         this.J,
         this.K,
         this.L,
         this.M,
         this.N,
         this.O,
         this.P,
         this.Q,
         this.R,
         this.S,
         this.T,
         this.V2,
         this.W2,
         this.X2,
         this.Y2,
         this.Z2,
         this.a2
      }) {
         if (!active.contains(widget)) {
            widget.j().c(0.0F);
            widget.j().d(0.0F);
         }
      }
   }

   private boolean a(Widget widget) {
      BooleanSetting setting = this.c.a(widget.j().j());
      return setting == null || setting.c();
   }

   @EventTarget
   public void a(DrawEvent event) {
      if (event.b()) {
         Westra.h().d().o().a(ThemeInfo.PRIMARY).a(this.b.c());
         float scale = this.D.c() * (this.E.c() ? HudScale.b() : 1.0F);
         HudScale.a(scale);
         this.s();
         event.h().method_22903();
         event.h().method_22905(scale, scale, 1.0F);

         for (Widget widget : this.r()) {
            if (this.a(widget)) {
               widget.a(event);
            }
         }

         event.h().method_22909();
      }
   }

   @EventTarget
   public void a(GlobalEvent event) {
      for (Widget widget : this.r()) {
         if (this.a(widget)) {
            widget.a(event);
         }
      }
   }

   @EventTarget
   public void a(PacketEvent event) {
      for (Widget widget : this.r()) {
         if (this.a(widget)) {
            widget.a(event);
         }
      }
   }

   @EventTarget
   public void a(BackendEvent event) {
      for (Widget widget : this.r()) {
         if (this.a(widget)) {
            widget.a(event);
         }
      }
   }
}
