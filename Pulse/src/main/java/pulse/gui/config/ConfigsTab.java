package pulse.gui.config;

import java.awt.Color;
import org.joml.Matrix3x2fStack;
import pulse.animation.AnimationState;
import pulse.animation.Easing;
import pulse.client.MinecraftContext;
import pulse.config.ConfigEntry;
import pulse.config.LocalConfigManager;
import pulse.core.Bool;
import pulse.gui.core.ClickGuiTab;
import pulse.gui.core.ClickGuiTabType;
import pulse.gui.core.GuiInput;
import pulse.gui.core.GuiInteractionState;
import pulse.gui.core.PanelFadeOverlay;
import pulse.gui.core.PulseClickGuiScreen;
import pulse.gui.core.TabHost;
import pulse.gui.core.TabSelector;
import pulse.gui.widgets.SearchBox;
import pulse.hud.notifications.HudNotificationCenter;
import pulse.media.chat.ChatMessages;
import pulse.render.Renderer2D;
import pulse.render.font.FontManager;
import pulse.theme.Theme;
import pulse.util.ColorUtils;

public class ConfigsTab implements TabHost, ClickGuiTab {
   private static final String[] a = new String[]{"Configs"};
   private static final float b = 19.0F;
   private static final float c = 48.0F;
   private static final float d = 117.5F;
   private static final float e = 15.0F;
   private static final float f = 8.0F;
   private static final float g = 8.0F;
   private int i;
   private int j;
   private int h = 0;
   private final AnimationState o = new AnimationState();
   private final AnimationState p = new AnimationState();
   private final AnimationState q = new AnimationState();
   private final AnimationState backdrop = new AnimationState();
   private boolean r = false;
   private boolean s = false;
   private boolean t = false;
   private final TabSelector k = new TabSelector(this);
   private final SearchBox l = new SearchBox(117.5F, 15.0F);
   private final ConfigListPanel m = new ConfigListPanel(this.l);
   private final PanelFadeOverlay n = new PanelFadeOverlay(25, 10.0F, 7.5F);

   public ConfigsTab() {
      this.j();
   }

   private void h() {
      this.m.b();

      try {
         LocalConfigManager localConfigManager = LocalConfigManager.get();
         localConfigManager.refreshProfilesFromDisk();

         for (ConfigEntry configEntry : localConfigManager.toConfigEntries()) {
            configEntry.a(configEntry.a().equals(localConfigManager.activeProfile()));
            this.m.a(configEntry);
         }
      } catch (Throwable ignored) {
      }
   }

   public void e() {
      this.h();
   }

   private void i() {
   }

   private void j() {
      this.m.a(configEntry -> {
         String name = configEntry.a();
         LocalConfigManager.get().loadProfile(name, () -> {
            for (ConfigEntry configEntry2 : this.m.a()) {
               configEntry2.a(configEntry2.a().equals(name));
            }

            HudNotificationCenter.a("Config " + name + " loaded", true);
            ChatMessages.a((Object)("Config " + name + " loaded"));
         }, str -> ChatMessages.a((Object)("Load error: " + str)));
      });
      this.m.b(configEntry2 -> {
         String name = configEntry2.a();
         LocalConfigManager.get().deleteProfile(name, () -> {
            HudNotificationCenter.a("Config " + name + " deleted", false);
            ChatMessages.a((Object)("Config " + name + " deleted"));
            this.e();
         }, str -> ChatMessages.a((Object)("Delete error: " + str)));
      });
      this.m.a(proxy_$3(this));
      this.m.c(configEntry3 -> ChatMessages.a((Object)this.d(configEntry3.a())));
      this.m.setCreateCallback(name -> {
         if (name != null && !name.trim().isEmpty()) {
            try {
               LocalConfigManager.get().createProfile(name, () -> {
                  HudNotificationCenter.a("Config " + name + " created", true);
                  ChatMessages.a((Object)("Config " + name + " created."));
                  this.e();
               }, errStr -> ChatMessages.a((Object)("Create error: " + errStr)));
            } catch (Throwable th) {
               ChatMessages.a((Object)"Enter config name.");
            }
         } else {
            ChatMessages.a((Object)"Enter config name.");
         }
      });
      this.h();
   }

   private static ConfigListPanel.RenameCallback proxy_$3(ConfigsTab configsTab) {
      return (configEntry, str, str2) -> LocalConfigManager.get().renameProfile(configEntry.a(), str, () -> {
         configEntry.a(str);
         HudNotificationCenter.a("Config renamed to " + str, true);
         ChatMessages.a((Object)("Config renamed to " + str));
         configsTab.e();
      }, errStr -> ChatMessages.a((Object)("Rename error: " + errStr)));
   }

   @Override
   public String[] c() {
      return a;
   }

   @Override
   public int d() {
      return this.h;
   }

   @Override
   public void a(int i) {
      if (i >= 0 && i < a.length && this.h != i) {
         this.h = i;
      }
   }

   @Override
   public void a(Matrix3x2fStack MatrixStackVar, Renderer2D renderer2D, float f2, float f3, int i, int i2) {
      this.i = i;
      this.j = i2;
      this.k.a(MatrixStackVar, renderer2D, f2, f3, i, i2);
      this.o.a();
      this.p.a();
      this.q.a();
      float fD = PulseClickGuiScreen.d();
      float fE = PulseClickGuiScreen.e();
      float f4 = f2 + fD - 19.0F - 117.5F;
      float f5 = f3 + 19.0F - 3.0F;
      float f6 = f4 - 8.0F - 8.0F;
      float f7 = f3 + 19.0F + 3.5F - 3.0F;
      int i3 = !GuiInteractionState.a().b() && !this.m.e() ? 0 : 1;
      int i4 = i3 == 0 && GuiInput.a(f6 - 16.0F - 16.0F, f7, 8.0F, 8.0F, i, i2) ? 1 : 0;
      int i5 = i3 == 0 && GuiInput.a(f6 - 8.0F - 8.0F, f7, 8.0F, 8.0F, i, i2) ? 1 : 0;
      int i6 = i3 == 0 && GuiInput.a(f6, f7, 8.0F, 8.0F, i, i2) ? 1 : 0;
      this.a(this.o, Bool.from(i4), this.r, Bool.from(i3));
      this.r = Bool.from(i3 == 0 && i4 != 0 ? 1 : 0);
      this.a(this.p, Bool.from(i5), this.s, Bool.from(i3));
      this.s = Bool.from(i3 == 0 && i5 != 0 ? 1 : 0);
      this.a(this.q, Bool.from(i6), this.t, Bool.from(i3));
      this.t = Bool.from(i3 == 0 && i6 != 0 ? 1 : 0);
      this.a(MatrixStackVar, renderer2D, f6 - 16.0F - 16.0F, f7, 8.0F, (float)this.o.j(), "add");
      this.a(MatrixStackVar, renderer2D, f6 - 8.0F - 8.0F, f7, 8.0F, (float)this.p.j(), "save");
      this.a(MatrixStackVar, renderer2D, f6, f7, 8.0F, (float)this.q.j(), "folder");
      this.l.a(MatrixStackVar, renderer2D, f4, f5, 117.5F, 15.0F, i, i2);
      this.m.a(MatrixStackVar, renderer2D, f2 + 19.0F, f3 + 48.0F, fD - 38.0F, fE - 48.0F - 9.5F - 8.0F, i, i2, 1.0F);
      this.n.a(MatrixStackVar, renderer2D, f2, f3, i, i2);
      this.backdrop.a();
      boolean dialogOpen = this.m.e();
      if (dialogOpen && this.backdrop.i() < 1.0) {
         this.backdrop.a(1.0, 0.25, Easing.h);
      } else if (!dialogOpen && this.backdrop.i() > 0.0) {
         this.backdrop.a(0.0, 0.18, Easing.h);
      }

      float backdropAlpha = (float)this.backdrop.j();
      if (backdropAlpha > 0.01F) {
         float sw = MinecraftContext.getWidth();
         float sh = MinecraftContext.getHeight();
         renderer2D.a(0.0F, 0.0F, sw, sh, 0.0F, new Color(0, 0, 0, (int)(160.0F * backdropAlpha)), MatrixStackVar);
      }

      this.m.a(MatrixStackVar, renderer2D, MinecraftContext.getWidth() / 2.0F, MinecraftContext.getHeight() / 2.0F, i, i2);
      if (i4 != 0 || i5 != 0 || i6 != 0) {
         GuiInput.g();
      }
   }

   private void a(AnimationState animationState, boolean z, boolean z2, boolean z3) {
      if (z3 && z2) {
         animationState.a(0.0, 0.15, Easing.h);
      } else {
         if (z3 || z == z2) {
            return;
         }

         animationState.a(z ? 1.0 : 0.0, 0.15, Easing.h);
      }
   }

   private void a(Matrix3x2fStack MatrixStackVar, Renderer2D renderer2D, float f2, float f3, float f4, float f5, String str) {
      Color colorA = ColorUtils.a(Theme.b, Theme.d, f5);
      String str2;
      switch (str) {
         case "add":
            str2 = "\ue91c";
            break;
         case "save":
            str2 = "\ue920";
            break;
         case "folder":
            str2 = "\ue905";
            break;
         default:
            return;
      }

      FontManager.e[14].a(str2, f2, f3, colorA, MatrixStackVar);
   }

   public void a(Matrix3x2fStack MatrixStackVar, Renderer2D renderer2D) {
      this.m.a(MatrixStackVar, renderer2D, MinecraftContext.getWidth() / 2.0F, MinecraftContext.getHeight() / 2.0F, this.i, this.j);
   }

   @Override
   public void a(float f2, float f3, int i, int i2) {
      if (this.m.e()) {
         this.m.a(f2 + 19.0F, f3 + 48.0F, PulseClickGuiScreen.d() - 38.0F, PulseClickGuiScreen.e() - 48.0F - 9.5F - 8.0F, i, i2);
      } else if (!this.l.a(i, i2)) {
         this.k.a(f2, f3, i, i2);
         float fD = PulseClickGuiScreen.d();
         float fE = PulseClickGuiScreen.e();
         float f4 = f2 + fD - 19.0F - 117.5F - 8.0F - 8.0F;
         float f5 = f3 + 19.0F + 3.5F - 3.0F;
         if (GuiInput.a(f4 - 16.0F - 16.0F, f5, 8.0F, 8.0F, i, i2)) {
            this.m();
         } else if (GuiInput.a(f4 - 8.0F - 8.0F, f5, 8.0F, 8.0F, i, i2)) {
            this.l();
         } else if (GuiInput.a(f4, f5, 8.0F, 8.0F, i, i2)) {
            this.k();
         } else {
            this.m.a(f2 + 19.0F, f3 + 48.0F, fD - 38.0F, fE - 48.0F - 9.5F - 8.0F, i, i2);
         }
      }
   }

   private void k() {
      LocalConfigManager.get().openConfigDirectory(() -> {
         String path = LocalConfigManager.get().configDirectory();
         HudNotificationCenter.a("Config folder opened", true);
         ChatMessages.a((Object)("Config folder: " + path));
         this.e();
      }, errStr -> ChatMessages.a((Object)("Open folder error: " + errStr)));
   }

   private void l() {
      try {
         LocalConfigManager localConfigManager = LocalConfigManager.get();
         String name = localConfigManager.activeProfile();
         localConfigManager.flushSaveNow("config-tab-save");
         HudNotificationCenter.a("Config " + name + " saved", true);
         ChatMessages.a((Object)("Config " + name + " saved"));
         this.e();
      } catch (Throwable th) {
         ChatMessages.a((Object)("Save error: " + th.getMessage()));
      }
   }

   private void m() {
      this.m.openCreate();
   }

   @Override
   public void b(float f2, float f3, int i, int i2) {
      this.a(f2, f3, i, i2);
   }

   @Override
   public void c(float f2, float f3, int i, int i2) {
      this.m.a(i, i2);
   }

   @Override
   public void a(float f2, float f3, int i, int i2, double d2, double d3) {
      this.m.a(i, i2, d2, d3);
   }

   @Override
   public void a(float f2) {
      this.m.a(f2, this.i, this.j);
   }

   @Override
   public boolean a(int i, int i2, int i3) {
      if (this.m.d()) {
         return this.m.a(i, i2, i3);
      } else {
         return this.l.c() ? this.l.a(i, i2, i3) : this.m.a(i, i2, i3);
      }
   }

   @Override
   public boolean b() {
      return Bool.from(!this.l.c() && !this.m.d() ? 0 : 1);
   }

   public boolean a(char c2, int i) {
      if (this.m.d()) {
         return this.m.a(c2, i);
      } else {
         return this.l.c() ? this.l.a(c2, i) : this.m.a(c2, i);
      }
   }

   public boolean f() {
      return this.l.c();
   }

   public boolean g() {
      return this.m.e();
   }

   @Override
   public ClickGuiTabType a() {
      return ClickGuiTabType.CONFIGS;
   }

   private String a(String str) {
      return str == null ? "Could not load config." : "Could not load config: " + str;
   }

   private String b(String str) {
      return str == null ? "Could not delete config." : "Could not delete config: " + str;
   }

   private String c(String str) {
      return str == null ? "Could not rename config." : "Could not rename config: " + str;
   }

   private String d(String str) {
      return str == null ? "Could not create config." : "Could not create config: " + str;
   }

   public static String b(String str, String str2, int i, int i2, int i3, int i4) {
      return null;
   }}


