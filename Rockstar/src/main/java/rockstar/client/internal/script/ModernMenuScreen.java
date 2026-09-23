package rockstar.client.internal.script;













import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.render.*;
import rockstar.client.module.*;
import rockstar.client.i18n.*;
import rockstar.client.event.*;
import rockstar.client.core.*;
import rockstar.client.animation.*;
import rockstar.client.internal.ui.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import rockstar.modules.combat.*;
import rockstar.modules.movement.*;
import rockstar.modules.visual.*;
import rockstar.modules.player.*;
import rockstar.modules.other.*;

import rockstar.client.compat.RenderSystem;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;
import net.minecraft.client.MinecraftClient;
import rockstar.client.compat.ShaderProgramKeys;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.BufferBuilder;
import rockstar.client.compat.BufferRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexFormats;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import pyrock.events.render.HudRenderEvent;
import pyrock.events.render.Render3DEvent;
import pyrock.utility.render.ColorRGBA;
import rockstar.profile.Profile;

public class ModernMenuScreen extends AbstractMenuScreen implements MenuLayoutProvider {
   private static final Motion internalField0913 = Motion.internalMethod01328(160L, Easing.internalField1822);
   private final Map<ModuleEntry, UiContainer> internalField0543 = new IdentityHashMap<>();
   private final Map<Setting, UiContainer> internalField0544 = new IdentityHashMap<>();
   private ModuleCategory internalField0405;
   private ModuleEntry internalField0403;
   private ModuleEntry internalField0402;
   private UiContainer internalField0634;
   private float internalField1856;
   private UiContainer internalField0635;
   private UiContainer internalField1257;
   private UiContainer internalField1254;
   private UiContainer internalField1255;
   private UiElement internalField0626;
   private UiElement internalField0625;
   private CustomTextField internalField0936;
   private KeybindButton internalField0928;
   private ModuleSettingsPanel internalField0096;
   private UiNode internalField0633;
   private UiContainer internalField1256;
   private long internalField0229;
   private float internalField1859;
   private float internalField1857;
   private float internalField1858;
   private float internalField1852;
   private List<ModuleEntry> internalField0416;
   private List<Setting> internalField0417;
   private int internalField0227;
   private ModuleEntry internalField1144;
   private String internalField0248;
   private float internalField1855;
   private long internalField0230;
   private long internalField1059;
   private long internalField1058;
   Vec3d internalField0283;
   private Vec3d internalField0282;
   Vec3d internalField1104;
   Vec3d internalField1106;
   private float internalField1850;
   private float internalField1849;
   private float internalField1848;
   private float internalField1842;
   private float internalField1851;
   private float internalField1847;
   private float internalField1846;
   private float internalField1853;
   static final ManagedFramebuffer internalField0769 = new ManagedFramebuffer(false).internalMethod06013();
   static boolean internalField0277;
   static boolean internalField0276;
   static ModernMenuScreen internalField0550;

   @Override
   public boolean lowDrawBatching() {
      return true;
   }

   public ModernMenuScreen() {
      this.internalField0405 = ModuleCategory.COMBAT;
      this.internalField1857 = Float.NaN;
      this.internalField1858 = Float.NaN;
      this.internalField1852 = Float.NaN;
      this.internalField0416 = List.of();
      this.internalField0417 = List.of();
      this.internalField0227 = -1;
      this.internalField0248 = "";
   }

   @Override
   public void init() {
      super.init();
      this.closing = false;
      this.contentAlpha = 1.0F;
      this.internalField0402 = null;
      this.internalField1855 = 0.0F;
      this.internalField0230 = 0L;
      this.internalField1059 = System.currentTimeMillis();
      this.internalField1857 = Float.NaN;
      RenderPipeline.internalField0312.internalMethod02988();
      this.clearRoots();
      this.internalField1848 = Math.min(488.0F, Math.max(360.0F, this.width - 12.0F));
      this.internalField1842 = Math.min(318.0F, Math.max(235.0F, this.height - 12.0F));
      this.internalField1850 = Math.round((this.width - this.internalField1848) / 2.0F);
      this.internalField1849 = Math.round((this.height - this.internalField1842) / 2.0F);
      this.internalField1851 = this.internalField1848 * 33.0F / 488.0F;
      this.internalField1847 = this.internalField1848 * 101.0F / 488.0F;
      this.internalField1846 = this.internalField1848 - this.internalField1851 - this.internalField1847;
      this.internalField1856 = Math.max(80.0F, (this.internalField1846 - 22.0F - 5.0F - 5.0F) / 2.0F);
      this.internalField1853 = this.internalField1842 * 24.0F / 318.0F;
      if (this.internalField0936 == null) {
         this.internalField0936 = new CustomTextField(Fonts.internalField0449.internalMethod01432(7.0F));
         this.internalField0936.internalMethod09000(LanguageManager.internalMethod07214("search"));
      }

      UiContainer localValue1 = (new UiContainer() {
            @Override
            protected void drawChildren(UiRenderContext localValue1, float localValue2) {
               ScissorStack.internalMethod06303(localValue1.getMatrices(), this.x(), this.y(), this.w(), this.h());
               super.drawChildren(localValue1, localValue2);
               ScissorStack.internalMethod07643();
            }
         })
         .internalMethod05895()
         .internalMethod03754(Motion.internalField1384)
         .internalMethod03995(this.internalField1848, this.internalField1842)
         .internalMethod07178((localValue1x, localValue2) -> this.internalMethod05826(localValue1x, localValue2));
      localValue1.internalMethod09801();
      localValue1.snapSize();
      localValue1.snapAt(this.internalField1850, this.internalField1849);
      localValue1.internalMethod03907(this.internalMethod03911());
      localValue1.internalMethod03907(this.internalMethod04380());
      this.internalField1255 = this.internalMethod07724();
      localValue1.internalMethod03907(this.internalField1255);
      this.internalField1254 = localValue1;
      this.add(localValue1);
      this.internalField0096 = new ModuleSettingsPanel(localValue1, this::internalMethod03200)
         .internalMethod02427(this.internalField0625)
         .internalMethod02280(this.internalField0626, Fonts.internalField0449.internalMethod01432(7.0F));
      this.add(this.internalField0096);
      this.add(this.internalField0096.internalMethod10099());
      this.add(this.internalField0096.internalMethod09983());
      this.internalField0416 = List.of();
      this.internalField0417 = List.of();
      this.internalField0248 = "\u0000";
      this.internalMethod07359(true);
      this.internalMethod07421(true);
   }

   private UiContainer internalMethod03911() {
      UiContainer localValue2 = new UiContainer()
         .internalMethod01863()
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod07607(LayoutAlignment.internalField0912)
         .internalMethod03062(3.0F)
         .internalMethod09339(this.internalField1851)
         .internalMethod09186();

      for (ModuleCategory localValue6 : ModuleCategory.values()) {
         UiElement localValue7 = new UiElement()
            .size(17.0F, 17.0F)
            .padding(4.0F)
            .icon(
               "category/" + localValue6.internalMethod05277().toLowerCase(),
               9.0F,
               localValue1x -> this.internalMethod05164()
                  .mix(ThemeColors.internalField1310, localValue1x.sig("selected"))
                  .mulAlpha(0.62F + 0.38F * localValue1x.sig("selected"))
            )
            .background(localValue1x -> internalMethod07226(this.internalMethod01353(), ThemeColors.internalField1310, 0.025F * localValue1x.hover()))
            .radius(4.0F)
            .bind("selected", () -> this.internalField0405 == localValue6, internalField0913)
            .cursor(CursorType.internalField0567)
            .onClick(() -> this.internalMethod07451(localValue6));
         localValue2.internalMethod03907(localValue7);
      }

      this.internalField0625 = new UiElement()
         .size(17.0F, 17.0F)
         .padding(4.0F)
         .icon("setting", 9.0F, localValue1x -> this.internalMethod05164().mulAlpha(0.58F + 0.35F * localValue1x.hover()))
         .background(localValue1x -> internalMethod07226(this.internalMethod01353(), ThemeColors.internalField1310, 0.025F * localValue1x.hover()))
         .radius(4.0F)
         .cursor(CursorType.internalField0567)
         .onClick(this::internalMethod04264);
      return new UiContainer()
         .internalMethod01863()
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod07607(LayoutAlignment.internalField1377)
         .internalMethod03514(Insets.internalMethod00105(0.0F, 0.0F, 8.0F, 0.0F))
         .internalMethod09339(this.internalField1851)
         .internalMethod09186()
         .internalMethod03715(DragConstraint.internalField0631)
         .internalMethod03907(localValue2)
         .internalMethod03907(this.internalField0625);
   }

   private UiContainer internalMethod04380() {
      UiElement localValue1 = new UiElement()
         .height(this.internalField1853)
         .fillWidth()
         .padding(Insets.internalMethod00105(5.0F, 8.0F, 1.0F, 8.0F))
         .text(Fonts.internalField0449.internalMethod01432(9.0F), () -> this.internalField0405.internalMethod05277(), localValue1x -> this.internalMethod05164())
         .draggable(DragConstraint.internalField0631);
      this.internalField0634 = new UiContainer()
         .internalMethod01863()
         .internalMethod03062(2.0F)
         .internalMethod03514(Insets.internalMethod00105(1.0F, 5.0F, 5.0F, 4.0F))
         .internalMethod09339(this.internalField1847)
         .internalMethod09266(Math.max(0.0F, this.internalField1842 - this.internalField1853))
         .internalMethod08755()
         .internalMethod01416(ScrollPolicy.internalField0916)
         .internalMethod05391(this::internalMethod02726);
      return new UiContainer()
         .internalMethod01863()
         .internalMethod09339(this.internalField1847)
         .internalMethod09266(this.internalField1842)
         .internalMethod03907(localValue1)
         .internalMethod03907(this.internalField0634);
   }

   private UiContainer internalMethod07724() {
      this.internalField0626 = new UiElement()
         .size(81.0F, 12.0F)
         .radius(3.0F)
         .cursor(CursorType.internalField1206)
         .onClick((localValue1x, localValue2x, localValue3x) -> {
            if (localValue1x == MouseButton.internalField0102 && this.internalField0096 != null) {
               if (this.internalField0096.internalMethod08140()) {
                  this.internalField0096.internalMethod06652().internalMethod01643(localValue2x, localValue3x, localValue1x);
               } else {
                  this.internalField0096.internalMethod08139();
               }
            }
         })
         .paint(
            (localValue1x, localValue2x) -> {
               localValue1x.drawRoundedRect(
                  localValue2x.x(),
                  localValue2x.y(),
                  localValue2x.w(),
                  12.0F,
                  CornerRadii.internalMethod03908(3.0F),
                  internalMethod07226(this.internalMethod07666().withAlpha(173.40001F), this.internalMethod05164(), 0.035F + 0.035F * localValue2x.hover())
               );
               localValue1x.drawIcon("search", localValue2x.x() + 3.5F, localValue2x.y() + 3.5F, 5.0F, this.internalMethod05164().mulAlpha(0.48F));
               if (this.internalField0096 != null) {
                  CustomTextField localValue3x = this.internalField0096.internalMethod06652();
                  localValue3x.internalMethod05191(localValue2x.x() + 8.5F, localValue2x.y(), localValue2x.w() - 10.5F, 12.0F);
                  localValue3x.internalMethod09000(LanguageManager.internalMethod07214("search"));
                  localValue3x.internalMethod00143(this.internalMethod05164().mulAlpha(0.72F));
                  localValue3x.internalMethod08627(1.0F);
                  localValue3x.internalMethod03398(localValue1x);
               }
            }
         );
      UiContainer localValue1 = (new UiContainer() {
            @Override
            protected void drawChildren(UiRenderContext localValue1, float localValue2) {
               ScissorStack.internalMethod06303(localValue1.getMatrices(), this.x(), this.y() - 1.0F, this.w(), this.h() + 2.0F);
               super.drawChildren(localValue1, localValue2);
               ScissorStack.internalMethod07643();
            }
         })
         .internalMethod01863()
         .internalMethod01855(TextAlignment.internalField1242)
         .internalMethod03062(1.0F)
         .internalMethod03995(72.0F, 11.0F)
         .internalMethod03907(
            new UiElement()
               .height(5.0F)
               .fillWidth()
               .text(Fonts.internalField0449.internalMethod01432(6.0F), this::internalMethod08918, localValue1x -> this.internalMethod05164())
               .textAlign(TextAlignment.internalField1242)
               .interactive(false)
         );
      UiContainer localValue3 = new UiContainer()
         .internalMethod05895()
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod03062(3.0F)
         .internalMethod03907(localValue1);
      UiContainer localValue4 = new UiContainer()
         .internalMethod05895()
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod07607(LayoutAlignment.internalField1377)
         .internalMethod03514(Insets.internalMethod00105(0.0F, 7.0F, 0.0F, 5.0F))
         .internalMethod09266(this.internalField1853)
         .internalMethod09609()
         .internalMethod03715(DragConstraint.internalField0631)
         .internalMethod03907(this.internalField0626)
         .internalMethod03907(localValue3);
      localValue4.snapSize();
      UiElement localValue5 = new UiElement()
         .height(12.0F)
         .text(Fonts.internalField0449.internalMethod01432(12.0F), this::internalMethod04863, localValue1x -> this.internalMethod05164())
         .interactive(false);
      this.internalField0928 = new KeybindButton(Fonts.internalField1154.internalMethod01432(7.0F), this::internalMethod04261, this::internalMethod07355);
      UiContainer localValue6 = new UiContainer()
         .internalMethod05895()
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod09266(12.0F)
         .internalMethod03855(() -> this.internalField0403 != null)
         .internalMethod04332(CursorType.internalField0567)
         .internalMethod03907(this.internalField0928)
         .internalMethod02525(this.internalField0928::internalMethod00007);
      UiContainer localValue7 = new UiContainer()
         .internalMethod05895()
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod03062(3.0F)
         .internalMethod09213()
         .internalMethod03907(localValue5)
         .internalMethod03907(localValue6);
      UiElement localValue8 = new UiElement()
         .size(16.0F, 16.0F)
         .padding(3.5F)
         .icon("xmark", 9.0F, localValue1x -> this.internalMethod05164().mulAlpha(0.8F + 0.2F * localValue1x.hover()))
         .radius(4.0F)
         .cursor(CursorType.internalField0567)
         .visibleWhen(() -> this.internalField0403 != null)
         .onClick(() -> this.internalMethod05756(null));
      UiContainer localValue9 = new UiContainer()
         .internalMethod05895()
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod07607(LayoutAlignment.internalField1377)
         .internalMethod09266(12.0F)
         .internalMethod09609()
         .internalMethod03907(localValue7)
         .internalMethod03907(localValue8);
      TextLabel localValue10 = new TextLabel(Fonts.internalField1154.internalMethod01432(7.0F), this::internalMethod08036)
         .internalMethod02959(localValue1x -> this.internalMethod05164().mulAlpha(0.58F))
         .internalMethod02902()
         .internalMethod08062();
      localValue10.interactive(false);
      UiContainer localValue11 = new UiContainer()
         .internalMethod01863()
         .internalMethod03062(1.0F)
         .internalMethod09266(22.0F)
         .internalMethod09609()
         .internalMethod03907(localValue9)
         .internalMethod03907(localValue10);
      this.internalField0635 = new UiContainer()
         .internalMethod01863()
         .internalMethod03062(5.0F)
         .internalMethod03514(Insets.internalMethod00105(27.5F, 0.0F, 2.0F, 0.0F))
         .internalMethod09609();
      this.internalField0635.snapSize();
      float localValue12 = Math.max(0.0F, this.internalField1842 - this.internalField1853);
      float localValue13 = Math.max(80.0F, localValue12 - 10.5F - 2.0F);
      this.internalField1257 = new UiContainer()
         .internalMethod01863()
         .internalMethod03514(Insets.internalMethod00105(0.0F, 5.0F, 0.0F, 0.0F))
         .internalMethod09339(this.internalField1846 - 22.0F)
         .internalMethod09266(localValue13)
         .internalMethod08755()
         .internalMethod08881(80.0F)
         .internalMethod01416(ScrollPolicy.internalField0916)
         .internalMethod05391(localValue1x -> {
            this.internalMethod02726(localValue1x);
            localValue1x.internalMethod02712(-9.0F).internalMethod02066(2.0F, 5.0F);
         })
         .internalMethod03907(this.internalField0635);
      this.internalField1257.snapSize();
      UiContainer localValue14 = new UiContainer()
         .internalMethod08791()
         .internalMethod03514(Insets.internalMethod00105(10.5F, 11.0F, 2.0F, 11.0F))
         .internalMethod03995(this.internalField1846, localValue12)
         .internalMethod03907(this.internalField1257)
         .internalMethod03907(this.internalMethod08522())
         .internalMethod03907(this.internalMethod08554())
         .internalMethod03907(localValue11);
      localValue14.snapSize();
      UiContainer localValue15 = new UiContainer()
         .internalMethod01863()
         .internalMethod03995(this.internalField1846, this.internalField1842)
         .internalMethod03907(localValue4)
         .internalMethod03907(localValue14);
      localValue15.snapSize();
      return localValue15;
   }

   private UiContainer internalMethod08522() {
      UiContainer localValue1 = new UiContainer()
         .internalMethod01863()
         .internalMethod02070(2)
         .internalMethod03062(5.0F)
         .internalMethod03907(this.internalMethod01280("menu/swing", () -> "Swing Animations", SwingPresetEditorScreen::new))
         .internalMethod03907(this.internalMethod01280("menu/builder", () -> "Inventory Builder", InventoryBuilderScreen::new))
         .internalMethod03907(this.internalMethod01280("menu/esp", () -> "ESP", EspEditorScreen::new))
         .internalMethod03907(
            this.internalMethod01280("menu/assist", () -> LanguageManager.internalMethod07214("menu.modern.shortcuts.item_binds"), AssistScreen::new)
         )
         .internalMethod03907(this.internalMethod01280("menu/autobuy", () -> "Auto Buy", AutoBuyScreen::new))
         .internalMethod03907(
            this.internalMethod01280(
               "menu/messager",
               () -> LanguageManager.internalMethod07214("menu.modern.shortcuts.messenger"),
               () -> RockstarClient.getInstance().internalMethod01773()
            )
         );
      localValue1.snapSize();
      SizedFont localValue2 = Fonts.internalField1154.internalMethod01432(9.0F);
      UiElement localValue3 = new UiElement()
         .text(localValue2, () -> LanguageManager.internalMethod07214("menu.modern.shortcuts"), localValue1x -> this.internalMethod05164().mulAlpha(0.9F))
         .interactive(false);
      return new UiContainer()
         .internalMethod01863()
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod07607(LayoutAlignment.internalField0912)
         .internalMethod03062(6.0F)
         .internalMethod03514(Insets.internalMethod00105(0.0F, 0.0F, this.internalField1853 + 10.5F - 2.0F + localValue2.internalMethod04890() + 6.0F, 0.0F))
         .internalMethod09213()
         .internalMethod07853(false)
         .internalMethod06712(() -> this.internalField0403 == null, Easing.internalField1828, 220L)
         .internalMethod03907(localValue3)
         .internalMethod03907(localValue1);
   }

   private UiContainer internalMethod08554() {
      UiElement localValue1 = new UiElement()
         .text(
            Fonts.internalField1154.internalMethod01432(9.0F),
            () -> LanguageManager.internalMethod07214("menu.modern.no_settings"),
            localValue1x -> this.internalMethod05164().mulAlpha(0.55F)
         )
         .interactive(false);
      return new UiContainer()
         .internalMethod01863()
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod07607(LayoutAlignment.internalField0912)
         .internalMethod09213()
         .internalMethod07853(false)
         .internalMethod06712(this::internalMethod04265, Easing.internalField1828, 220L)
         .internalMethod03907(localValue1);
   }

   private boolean internalMethod04265() {
      if (this.internalField0403 == null) {
         return false;
      } else {
         for (Setting localValue2 : this.internalField0403.getSettings()) {
            if (localValue2.isVisible()) {
               return false;
            }
         }

         return true;
      }
   }

   private UiContainer internalMethod01280(String localValue1, Supplier<String> localValue2, Supplier<Screen> localValue3) {
      UiContainer localValue4 = new UiContainer();
      UiElement localValue5 = new UiElement()
         .fill()
         .fade()
         .text(Fonts.internalField1154.internalMethod01432(7.0F), localValue2, localValue2x -> this.internalMethod05164().mulAlpha(0.72F + 0.28F * localValue4.hover()))
         .interactive(false);
      UiElement localValue6 = new UiElement()
         .size(7.0F, 7.0F)
         .icon(localValue1, 7.0F, localValue2x -> this.internalMethod05164().mulAlpha(0.62F + 0.38F * localValue4.hover()))
         .interactive(false);
      return localValue4.internalMethod05895()
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod03995(90.0F, 17.0F)
         .internalMethod03514(Insets.internalMethod00105(0.0F, 5.0F, 0.0F, 6.0F))
         .internalMethod09018(4.0F)
         .internalMethod06812(
            localValue0 -> internalMethod07226(ThemeColors.internalMethod08573().mulAlpha(0.4F), ThemeColors.internalField1310, 0.05F * localValue0.hover())
         )
         .internalMethod04332(CursorType.internalField0567)
         .internalMethod03907(localValue5)
         .internalMethod03907(localValue6)
         .internalMethod05690(() -> this.internalMethod00728((Screen)localValue3.get()));
   }

   private void internalMethod00728(Screen localValue1) {
      if (localValue1 != null) {
         if (this.internalField0928 != null) {
            this.internalField0928.internalMethod06271();
         }

         MinecraftClient.getInstance().setScreen(localValue1);
      }
   }

   private void internalMethod05826(UiRenderContext localValue1, UiContainer localValue2) {
      float localValue3 = localValue2.x();
      float localValue4 = localValue2.y();
      float localValue5 = localValue2.w();
      float localValue6 = localValue2.h();
      ColorRGBA localValue7 = this.internalMethod08545();
      localValue1.drawClientRect(localValue3, localValue4, localValue5, localValue6, 1.0F, 0.0F, 2.0F, 12.0F, false, true);
      float localValue8 = localValue3 + this.internalField1851 - 1.0F;
      float localValue9 = localValue3 + this.internalField1851 + this.internalField1847 - 1.0F;
      localValue1.drawRect(localValue8, localValue4 + 1.0F, 1.0F, localValue6 - 2.0F, localValue7);
      localValue1.drawRect(localValue9, localValue4 + 1.0F, 1.0F, localValue6 - 2.0F, localValue7);
      localValue1.drawRect(localValue9 + 1.0F, localValue4 + this.internalField1853 - 1.0F, localValue5 - this.internalField1851 - this.internalField1847 - 1.0F, 1.0F, localValue7);
      if (!this.closing) {
         localValue1.drawRoundedBorder(localValue3, localValue4, localValue5, localValue6, 0.5F, CornerRadii.internalMethod03908(12.0F), localValue7);
      }
   }

   private void internalMethod07451(ModuleCategory localValue1) {
      if (this.internalField0405 != localValue1) {
         if (this.internalField0928 != null) {
            this.internalField0928.internalMethod06271();
         }

         this.internalField0405 = localValue1;
         this.internalField0402 = null;
         this.internalField0416 = List.of();
         this.internalMethod07359(true);
      }
   }

   private void internalMethod04264() {
      if (this.internalField0096 != null) {
         this.internalField0096.internalMethod08155();
      }
   }

   private void internalMethod03200(ModuleEntry localValue1, Setting localValue2) {
      if (localValue1 != null) {
         if (this.internalField0928 != null) {
            this.internalField0928.internalMethod06271();
         }

         this.internalField0405 = localValue1.getCategory();
         this.internalField0403 = localValue1;
         this.internalField0416 = List.of();
         this.internalField0417 = List.of();
         this.internalMethod07359(true);
         this.internalMethod07421(true);
         if (this.internalField1257 != null) {
            this.internalField1257.internalMethod03631();
         }

         UiNode localValue3 = localValue2 != null ? this.internalField0544.get(localValue2) : this.internalField0543.get(localValue1);
         this.internalField0633 = localValue3;
         this.internalField1256 = localValue2 != null ? this.internalField1257 : this.internalField0634;
         this.internalField1859 = localValue2 != null ? 7.0F : 3.0F;
         this.internalField0229 = localValue3 == null ? 0L : System.currentTimeMillis() + 1600L;
      }
   }

   @Override
   public void afterRender(UiRenderContext localValue1) {
      this.internalMethod07167(localValue1);
   }

   private void internalMethod07167(UiRenderContext localValue1) {
      if (this.internalField0633 != null) {
         long localValue2 = this.internalField0229 - System.currentTimeMillis();
         if (localValue2 > 0L && this.internalField0633.inFlow()) {
            float localValue4 = this.internalField1256 == null ? 0.0F : this.internalField1256.internalMethod09429();
            float localValue5 = this.internalField0633.x();
            float localValue6 = this.internalField0633.y() - localValue4;
            float localValue7 = this.internalField0633.w();
            float localValue8 = this.internalField0633.h();
            if (this.internalField1256 != null) {
               float localValue9 = this.internalField1256.y();
               float localValue10 = this.internalField1256.y() + this.internalField1256.h();
               if (localValue6 < localValue9) {
                  localValue8 -= localValue9 - localValue6;
                  localValue6 = localValue9;
               }

               if (localValue6 + localValue8 > localValue10) {
                  localValue8 = localValue10 - localValue6;
               }
            }

            if (!(localValue7 <= 0.0F) && !(localValue8 <= 0.0F)) {
               float localValue11 = Math.min(1.0F, (float)localValue2 / 400.0F);
               localValue1.drawRoundedRect(
                  localValue5, localValue6, localValue7, localValue8, CornerRadii.internalMethod03908(this.internalField1859), ThemeColors.internalField1310.mulAlpha(0.18F * localValue11)
               );
            }
         } else {
            this.internalField0633 = null;
         }
      }
   }

   private List<ModuleEntry> internalMethod01520(ModuleCategory localValue1) {
      return this.internalMethod05840().stream().filter(localValue1x -> localValue1x.getCategory() == localValue1).toList();
   }

   private List<ModuleEntry> internalMethod05840() {
      return RockstarClient.getInstance()
         .getModuleManager()
         .getModules()
         .stream()
         .filter(ModuleEntry::isAvailable)
         .sorted(Comparator.comparing(ModuleEntry::getName, String.CASE_INSENSITIVE_ORDER))
         .toList();
   }

   private void internalMethod07359(boolean localValue1) {
      if (this.internalField0634 != null) {
         this.internalMethod09100();
         String localValue2 = FuzzySearchUtils.internalMethod00096(this.internalField0936 == null ? "" : this.internalField0936.internalMethod06202());
         List localValue3 = localValue2.isEmpty() ? this.internalMethod01520(this.internalField0405) : this.internalMethod05840();
         if (localValue1 || !internalMethod03293(this.internalField0416, localValue3) || !localValue2.equals(this.internalField0248)) {
            ArrayList localValue4 = new ArrayList(localValue3);
            if (!localValue2.isEmpty()) {
               IdentityHashMap localValue5 = new IdentityHashMap();

               for (ModuleEntry localValue7 : (Iterable<ModuleEntry>)(Iterable<?>)localValue4) {
                  localValue5.put(localValue7, FuzzySearchUtils.internalMethod05951(FuzzySearchUtils.internalMethod00096(localValue7.getName()), localValue2));
               }

               localValue4.removeIf(localValue1x -> (Integer)localValue5.get(localValue1x) == Integer.MAX_VALUE);
               localValue4.sort(
                  Comparator.<ModuleEntry>comparingInt(localValue1x -> (Integer)localValue5.get(localValue1x))
                     .thenComparing(ModuleEntry::getName, String.CASE_INSENSITIVE_ORDER)
               );
            }

            this.internalMethod04495(localValue2, localValue4);
            ArrayList localValue8 = new ArrayList();

            for (ModuleEntry localValue10 : (Iterable<ModuleEntry>)(Iterable<?>)localValue4) {
               localValue8.add(this.internalField0543.computeIfAbsent(localValue10, this::internalMethod05653));
            }

            this.internalField0634.internalMethod07849(localValue8);
            this.internalField0416 = new ArrayList<>(localValue3);
            this.internalField0248 = localValue2;
         }
      }
   }

   private void internalMethod04495(String localValue1, List<ModuleEntry> localValue2) {
      boolean localValue3 = !this.internalField0248.isEmpty() && !this.internalField0248.equals("\u0000");
      if (localValue1.isEmpty()) {
         if (localValue3 && this.internalField1144 != null && this.internalField0403 != this.internalField1144) {
            this.internalMethod05756(this.internalField1144);
         }

         this.internalField1144 = null;
      } else {
         if (!localValue3) {
            this.internalField1144 = this.internalField0403;
         }

         ModuleEntry localValue4 = localValue2.isEmpty() ? null : (ModuleEntry)localValue2.getFirst();
         if (this.internalField0403 != localValue4) {
            this.internalMethod05756(localValue4);
         }
      }
   }

   private UiContainer internalMethod05653(ModuleEntry localValue1) {
      UiElement localValue2 = new UiElement()
         .fill()
         .fade()
         .text(
            Fonts.internalField0449.internalMethod01432(7.0F),
            () -> this.internalMethod07444(localValue1),
            localValue1x -> this.internalMethod05164()
               .mix(ThemeColors.internalField1310, 0.5F * localValue1x.sig("enabled"))
               .mulAlpha(0.6F + 0.3F * localValue1x.sig("enabled") + 0.1F * localValue1x.sig("selected"))
         )
         .bind("enabled", localValue1::isEnabled, internalField0913)
         .bind("selected", () -> this.internalField0403 == localValue1, internalField0913)
         .interactive(false);
      return new UiContainer()
         .internalMethod05895()
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod09266(15.0F)
         .internalMethod09609()
         .internalMethod03514(Insets.internalMethod05266(0.0F, 5.0F))
         .internalMethod09018(3.0F)
         .internalMethod06812(
            localValue0 -> internalMethod07226(
               ThemeColors.internalMethod08573().mulAlpha(0.4F),
               ThemeColors.internalField1310,
               0.08F * localValue0.sig("enabled") + 0.018F * localValue0.sig("selected") + 0.025F * localValue0.hover()
            )
         )
         .internalMethod01258("enabled", localValue1::isEnabled, internalField0913)
         .internalMethod01258("selected", () -> this.internalField0403 == localValue1, internalField0913)
         .internalMethod04332(CursorType.internalField0567)
         .internalMethod03907(localValue2)
         .internalMethod05382((localValue2x, localValue3, localValue4) -> this.internalMethod06038(localValue1, localValue2x));
   }

   private String internalMethod07444(ModuleEntry localValue1) {
      if (this.internalField0402 != localValue1) {
         return localValue1.getName();
      } else {
         int localValue2 = KeybindUtils.internalMethod06867();
         if (localValue2 != 0) {
            return LanguageManager.internalMethod07214("key") + ": " + KeybindUtils.internalMethod07434(localValue2) + "...";
         } else {
            return localValue1.getKeybind() == -1
               ? LanguageManager.internalMethod07214("menu.binding")
               : LanguageManager.internalMethod07214("key") + ": " + TextUtils.internalMethod04982(localValue1.getKeybind());
         }
      }
   }

   private void internalMethod06038(ModuleEntry localValue1, MouseButton localValue2) {
      switch (localValue2) {
         case internalField0102:
            localValue1.toggle();
            break;
         case internalField0101:
            if (this.internalField0403 != localValue1) {
               this.internalMethod05756(localValue1);
            }
            break;
         case internalField0990:
            this.internalField0402 = this.internalField0402 == localValue1 ? null : localValue1;
      }
   }

   private void internalMethod05756(ModuleEntry localValue1) {
      if (this.internalField0928 != null) {
         this.internalField0928.internalMethod06271();
      }

      this.internalField0403 = localValue1;
      this.internalField0402 = null;
      this.internalField0417 = List.of();
      this.internalMethod07421(true);
      if (this.internalField1257 != null) {
         this.internalField1257.internalMethod03631();
      }
   }

   private void internalMethod09100() {
      int localValue1 = ModuleManager.internalMethod03045();
      if (localValue1 != this.internalField0227) {
         this.internalField0227 = localValue1;
         List localValue2 = RockstarClient.getInstance().getModuleManager().getModules();
         if (this.internalField0402 != null && !internalMethod03850(localValue2, this.internalField0402)) {
            this.internalField0402 = null;
         }

         if (this.internalField1144 != null && !internalMethod03850(localValue2, this.internalField1144)) {
            this.internalField1144 = internalMethod07553(localValue2, this.internalField1144);
         }

         IdentityHashMap localValue3 = new IdentityHashMap();

         for (ModuleEntry localValue5 : (Iterable<ModuleEntry>)(Iterable<?>)localValue2) {
            localValue3.put(localValue5, Boolean.TRUE);
         }

         this.internalField0543.keySet().removeIf(localValue1x -> !localValue3.containsKey(localValue1x));
         if (this.internalField0403 != null && !localValue3.containsKey(this.internalField0403)) {
            for (Setting localValue7 : this.internalField0417) {
               this.internalField0544.remove(localValue7);
            }

            this.internalMethod05756(internalMethod07553(localValue2, this.internalField0403));
         }
      }
   }

   private static boolean internalMethod03850(List<ModuleEntry> localValue0, ModuleEntry localValue1) {
      for (ModuleEntry localValue3 : localValue0) {
         if (localValue3 == localValue1) {
            return true;
         }
      }

      return false;
   }

   private static ModuleEntry internalMethod07553(List<ModuleEntry> localValue0, ModuleEntry localValue1) {
      for (ModuleEntry localValue3 : localValue0) {
         if (localValue3.getCategory() == localValue1.getCategory() && localValue3.getName().equals(localValue1.getName())) {
            return localValue3;
         }
      }

      return null;
   }

   private void internalMethod07421(boolean localValue1) {
      if (this.internalField0635 != null) {
         List localValue2 = this.internalField0403 == null ? List.of() : this.internalField0403.getSettings();
         if (localValue1 || !internalMethod03293(this.internalField0417, localValue2)) {
            if (!this.internalField0417.isEmpty()) {
               IdentityHashMap localValue3 = new IdentityHashMap();

               for (Setting localValue5 : (Iterable<Setting>)(Iterable<?>)localValue2) {
                  localValue3.put(localValue5, Boolean.TRUE);
               }

               for (Setting localValue13 : this.internalField0417) {
                  if (!localValue3.containsKey(localValue13)) {
                     this.internalField0544.remove(localValue13);
                  }
               }
            }

            ArrayList localValue10 = new ArrayList();
            ArrayList localValue12 = new ArrayList();
            ArrayList localValue14 = new ArrayList();
            ArrayList localValue6 = new ArrayList();
            int localValue7 = 0;

            for (Setting localValue9 : (Iterable<Setting>)(Iterable<?>)localValue2) {
               if (localValue9 instanceof SectionSetting) {
                  this.internalMethod00888(localValue10, localValue12, localValue14, localValue6);
                  localValue7 = 0;
                  localValue10.add(this.internalField0544.computeIfAbsent(localValue9, this::internalMethod00689));
               } else {
                  (localValue7++ % 2 == 0 ? localValue12 : localValue14).add(this.internalField0544.computeIfAbsent(localValue9, this::internalMethod00689));
                  localValue6.add(localValue9);
               }
            }

            this.internalMethod00888(localValue10, localValue12, localValue14, localValue6);
            this.internalField0635.internalMethod06213(localValue10);
            this.internalField0417 = new ArrayList<>(localValue2);
            RenderPipeline.internalField0312.internalMethod02988();
         }
      }
   }

   private void internalMethod00888(List<UiNode> localValue1, List<UiNode> localValue2, List<UiNode> localValue3, List<Setting> localValue4) {
      if (!localValue2.isEmpty() || !localValue3.isEmpty()) {
         List localValue5 = List.copyOf(localValue4);
         UiContainer localValue6 = new UiContainer().internalMethod01863().internalMethod03062(5.0F).internalMethod09339(this.internalField1856).internalMethod07108(localValue2);
         UiContainer localValue7 = new UiContainer().internalMethod01863().internalMethod03062(5.0F).internalMethod09339(this.internalField1856).internalMethod07108(localValue3);
         localValue6.snapSize();
         localValue7.snapSize();
         UiContainer localValue8 = new UiContainer()
            .internalMethod05895()
            .internalMethod03062(5.0F)
            .internalMethod01855(TextAlignment.internalField0622)
            .internalMethod09609()
            .internalMethod06712(() -> internalMethod07248(localValue5), Easing.internalField1828, 220L)
            .internalMethod09936()
            .internalMethod03907(localValue6)
            .internalMethod03907(localValue7);
         localValue8.snapSize();
         localValue1.add(localValue8);
         localValue2.clear();
         localValue3.clear();
         localValue4.clear();
      }
   }

   private static boolean internalMethod07248(List<Setting> localValue0) {
      for (Setting localValue2 : localValue0) {
         if (localValue2.isVisible()) {
            return true;
         }
      }

      return false;
   }

   private UiContainer internalMethod00689(Setting localValue1) {
      return localValue1 instanceof SectionSetting ? this.internalMethod04284(localValue1) : this.internalMethod08311(localValue1);
   }

   private UiContainer internalMethod04284(Setting localValue1) {
      UiContainer localValue2 = SettingComponentFactory.internalMethod07309(localValue1);
      localValue2.internalMethod03514(Insets.internalField0910);
      localValue2.snapSize();
      UiContainer localValue3 = new UiContainer()
         .internalMethod01863()
         .internalMethod09609()
         .internalMethod06712(localValue1::isVisible, Easing.internalField1828, 220L)
         .internalMethod09936()
         .internalMethod03907(localValue2);
      localValue3.snapSize();
      return localValue3;
   }

   private UiContainer internalMethod08311(Setting localValue1) {
      UiContainer localValue2 = SettingComponentFactory.internalMethod07309(localValue1);
      localValue2.snapSize();
      UiContainer localValue3 = new UiContainer()
         .internalMethod01863()
         .internalMethod09609()
         .internalMethod03514(Insets.internalMethod08300(4.0F))
         .internalMethod09018(7.0F)
         .internalMethod06812(localValue0 -> ThemeColors.internalMethod08573().mulAlpha(0.4F))
         .internalMethod06712(localValue1::isVisible, Easing.internalField1828, 220L)
         .internalMethod09936()
         .internalMethod03907(localValue2);
      localValue3.snapSize();
      return localValue3;
   }

   private String internalMethod04863() {
      return this.internalField0403 == null ? "" : LanguageManager.internalMethod00160("menu.modern.settings", this.internalField0403.getName());
   }

   private String internalMethod08036() {
      return this.internalField0403 == null ? "" : this.internalField0403.internalMethod05655();
   }

   private String internalMethod08918() {
      String localValue1 = Profile.getUsername();
      return localValue1 == null ? "" : localValue1;
   }

   private int internalMethod04261() {
      return this.internalField0403 == null ? -1 : this.internalField0403.getKeybind();
   }

   private void internalMethod07355(int localValue1) {
      if (this.internalField0403 != null) {
         this.internalField0403.setKeybind(localValue1);
         this.internalMethod09824();
      }
   }

   @Override
   public void render(UiRenderContext localValue1) {
      this.internalMethod09101();
      this.internalMethod09111();
      GuiMoveModule.internalMethod09597();
      if (!this.closing) {
         this.internalMethod07359(false);
         this.internalMethod07421(false);
      }

      if (this.internalField1257 != null) {
         float localValue2 = this.internalField1257.internalMethod09429();
         if (localValue2 != this.internalField1857) {
            this.internalField1857 = localValue2;
            RenderPipeline.internalField0312.internalMethod02988();
         }
      }

      if (System.currentTimeMillis() - this.internalField1059 < 400L) {
         RenderPipeline.internalField0312.internalMethod02988();
      }

      if (this.internalField1254 != null) {
         this.internalField1850 = this.internalField1254.x();
         this.internalField1849 = this.internalField1254.y();
         if (this.internalField1850 != this.internalField1858 || this.internalField1849 != this.internalField1852) {
            this.internalField1858 = this.internalField1850;
            this.internalField1852 = this.internalField1849;
            RenderPipeline.internalField0312.internalMethod02988();
         }
      }

      float localValue4 = this.closing ? 1.0F : 0.7F + 0.3F * this.internalMethod09112();
      boolean localValue3 = Math.abs(localValue4 - 1.0F) > 1.0E-4F;
      if (localValue3) {
         localValue1.getMatrices().pushMatrix();
         localValue1.getMatrices().translate(this.width / 2.0F, this.height / 2.0F);
         localValue1.getMatrices().scale(localValue4, localValue4);
         localValue1.getMatrices().translate(-this.width / 2.0F, -this.height / 2.0F);
      }

      MenuRenderDispatcher.internalMethod05609(this, localValue1);
      super.render(localValue1);
      MenuRenderDispatcher.internalMethod03319(this, localValue1);
      if (localValue3) {
         localValue1.getMatrices().popMatrix();
      }
   }

   @Override
   public String internalMethod03999() {
      return "modern";
   }

   @Override
   public float internalMethod04389() {
      return this.internalMethod09112();
   }

   @Override
   public float internalMethod04391() {
      return this.closing ? this.internalMethod09823() : 0.0F;
   }

   @Override
   public boolean internalMethod04390() {
      return this.closing;
   }

   @Override
   public float internalMethod08283() {
      return this.contentAlpha;
   }

   @Override
   public float internalMethod08285() {
      return this.closing ? 1.0F : 0.7F + 0.3F * this.internalMethod09112();
   }

   @Override
   public List<MenuLayoutProvider.InternalType0257> internalMethod05973() {
      return List.of(new MenuLayoutProvider.InternalType0257("window", this.internalField1850, this.internalField1849, this.internalField1848, this.internalField1842));
   }

   public static ModernMenuScreen internalMethod03027() {
      return internalField0550;
   }

   private float internalMethod09112() {
      float localValue1 = Math.min(1.0F, Math.max(0.0F, (float)(System.currentTimeMillis() - this.internalField1059) / 300.0F));
      return Easing.internalField0812.ease(localValue1, 0.0F, 1.0F, 1.0F);
   }

   private void internalMethod09101() {
      long localValue1 = System.currentTimeMillis();
      if (this.closing) {
         this.internalField1855 = 0.0F;
         this.internalField0230 = localValue1;
         this.contentAlpha = 1.0F;
      } else {
         float localValue3 = this.internalField0230 == 0L ? 16.0F : Math.min(64.0F, (float)(localValue1 - this.internalField0230));
         this.internalField0230 = localValue1;
         MenuModule localValue4 = RockstarClient.getInstance().getModuleManager().getModule(MenuModule.class);
         int localValue5 = localValue4 != null && localValue4.internalMethod00598() != null ? localValue4.internalMethod00598().internalMethod07477() : -1;
         float localValue6 = internalMethod07356(localValue5) ? 1.0F : 0.0F;
         float localValue7 = localValue3 / 300.0F;
         if (this.internalField1855 < localValue6) {
            this.internalField1855 = Math.min(localValue6, this.internalField1855 + localValue7);
         } else if (this.internalField1855 > localValue6) {
            this.internalField1855 = Math.max(localValue6, this.internalField1855 - localValue7);
         }

         this.contentAlpha = 1.0F - Easing.internalField1626.ease(this.internalField1855, 0.0F, 1.0F, 1.0F);
      }
   }

   private void internalMethod09111() {
      if (!this.closing && !(this.contentAlpha >= 0.999F)) {
         long localValue1 = MinecraftClient.getInstance().getWindow().getHandle();
         boolean localValue3 = GLFW.glfwGetMouseButton(localValue1, 0) == 1;
         if (!localValue3 || UiNode.spotlight() == null) {
            UiContainer localValue4 = null;

            for (Entry localValue6 : this.internalField0544.entrySet()) {
               UiContainer localValue7 = (UiContainer)localValue6.getValue();
               if (localValue7.inFlow() && localValue7.hovered() && SettingComponentFactory.internalMethod00080((Setting)localValue6.getKey())) {
                  localValue4 = localValue7;
                  break;
               }
            }

            UiNode.spotlight(localValue4);
         }
      } else {
         UiNode.spotlight(null);
      }
   }

   private static boolean internalMethod07356(int localValue0) {
      return KeybindUtils.internalMethod08521(localValue0);
   }

   public void tick() {
      GuiMoveModule.internalMethod09597();
      super.tick();
   }

   @Override
   public void onMouseClicked(double localValue1, double localValue3, MouseButton localValue5) {
      if (KeybindButton.internalMethod01436(localValue5)) {
         this.internalMethod09824();
      } else {
         boolean localValue6 = this.overlays.stream().anyMatch(localValue4 -> localValue4.alive() && localValue4.contains((float)localValue1, (float)localValue3));
         if (this.internalField0096 != null && !localValue6) {
            this.internalField0096.internalMethod04148((float)localValue1, (float)localValue3);
         }

         if (this.internalField0402 != null) {
            if (localValue5 == MouseButton.internalField0102) {
               this.internalField0402 = null;
               return;
            }

            if (localValue5 != MouseButton.internalField0990) {
               this.internalField0402.setKeybind(KeybindUtils.internalMethod08541(localValue5.internalMethod02957()));
               this.internalField0402 = null;
               this.internalMethod09824();
               return;
            }
         }

         super.onMouseClicked(localValue1, localValue3, localValue5);
      }
   }

   @Override
   public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
      if (this.internalField0402 != null) {
         int localValue4 = KeybindUtils.internalMethod08281(keyCode, modifiers);
         if (localValue4 != Integer.MIN_VALUE) {
            this.internalField0402.setKeybind(localValue4);
            this.internalField0402 = null;
            this.internalMethod09824();
            return true;
         }
      }

      return super.keyReleased(keyCode, scanCode, modifiers);
   }

   @Override
   public void onMouseReleased(double localValue1, double localValue3, MouseButton localValue5) {
      boolean localValue6 = localValue5 == MouseButton.internalField0102 && this.internalField1254 != null && this.internalField1254.dragging();
      if (this.internalField0936 != null) {
         this.internalField0936.internalMethod02863(localValue1, localValue3, localValue5);
      }

      super.onMouseReleased(localValue1, localValue3, localValue5);
      if (localValue6) {
         this.internalMethod09113();
      }
   }

   private void internalMethod09113() {
      float localValue1 = this.internalField1254.x();
      float localValue2 = this.internalField1254.y();
      float localValue3 = this.internalField1254.w();
      float localValue4 = this.internalField1254.h();
      float localValue5 = Math.max(0.0F, localValue1);
      float localValue6 = Math.max(0.0F, localValue2);
      float localValue7 = Math.min((float)this.width, localValue1 + localValue3);
      float localValue8 = Math.min((float)this.height, localValue2 + localValue4);
      float localValue9 = Math.max(0.0F, localValue7 - localValue5);
      float localValue10 = Math.max(0.0F, localValue8 - localValue6);
      float localValue11 = Math.max(1.0F, localValue3 * localValue4);
      float localValue12 = 1.0F - localValue9 * localValue10 / localValue11;
      if (!(localValue12 < 0.35F)) {
         float localValue13 = Math.round((this.width - localValue3) / 2.0F);
         float localValue14 = Math.round((this.height - localValue4) / 2.0F);
         this.internalField1254.internalMethod08296(localValue13, localValue14);
      }
   }

   @Override
   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      if (this.internalField0402 == null && !KeybindButton.internalMethod06277()) {
         if (rockstar.client.compat.InputCompat.hasControlDown() && keyCode == 90 && SettingHistoryManager.internalMethod07603()) {
            return true;
         }

         if (rockstar.client.compat.InputCompat.hasControlDown() && keyCode == 89 && SettingHistoryManager.internalMethod07605()) {
            return true;
         }
      }

      if (this.internalField0402 != null) {
         if (keyCode != 256 && keyCode != 261) {
            int localValue4 = KeybindUtils.internalMethod06041(keyCode, modifiers);
            if (localValue4 == Integer.MIN_VALUE) {
               return true;
            } else {
               this.internalField0402.setKeybind(localValue4);
               this.internalField0402 = null;
               this.internalMethod09824();
               return true;
            }
         } else {
            this.internalField0402.setKeybind(-1);
            this.internalField0402 = null;
            this.internalMethod09824();
            return true;
         }
      } else if (this.internalField0096 != null && !this.internalField0096.internalMethod08140() && rockstar.client.compat.InputCompat.hasControlDown() && keyCode == 70) {
         this.internalField0096.internalMethod08139();
         return true;
      } else if (super.keyPressed(keyCode, scanCode, modifiers)) {
         return true;
      } else if (MenuModule.internalMethod01859(keyCode)) {
         this.close();
         return true;
      } else {
         return false;
      }
   }

   public void close() {
      if (!this.closing) {
         if (this.internalField0936 != null) {
            this.internalField0936.internalMethod07508(false);
         }

         SoundsModule localValue1 = RockstarClient.getInstance().getModuleManager().getModule(SoundsModule.class);
         if (localValue1 != null && localValue1.isEnabled()) {
            ClientSoundEvents.internalField0130.internalMethod03132(localValue1.internalMethod01798(), 1.0F);
         }

         MenuModule localValue2 = RockstarClient.getInstance().getModuleManager().getModule(MenuModule.class);
         if (localValue2 != null && localValue2.isEnabled()) {
            localValue2.disable();
         }

         super.close();
      }
   }

   @Override
   public void removed() {
      this.internalMethod04262();
      super.removed();
   }

   public void internalMethod04262() {
      if (!this.closing) {
         this.closing = true;
         UiNode.spotlight(null);
         this.internalMethod09824();
         this.internalField1058 = System.currentTimeMillis();
         internalField0277 = false;
         internalField0276 = false;
         MinecraftClient localValue1 = MinecraftClient.getInstance();
         Camera localValue2 = localValue1.gameRenderer.getCamera();
         if (localValue2 != null && localValue1.player != null) {
            double localValue3 = Math.toRadians(localValue2.getYaw());
            double localValue5 = Math.toRadians(localValue2.getPitch());
            Vec3d localValue7 = new Vec3d(-Math.sin(localValue3) * Math.cos(localValue5), -Math.sin(localValue5), Math.cos(localValue3) * Math.cos(localValue5)).normalize();
            this.internalField0282 = localValue7;
            this.internalField1104 = localValue7.crossProduct(new Vec3d(0.0, 1.0, 0.0)).normalize();
            this.internalField1106 = this.internalField1104.crossProduct(localValue7).normalize();
            this.internalField0283 = localValue2.getCameraPos().add(localValue7.multiply(1.5));
         }

         internalField0550 = this;
      }
   }

   public float internalMethod09823() {
      return Math.min(1.0F, Math.max(0.0F, (float)(System.currentTimeMillis() - this.internalField1058) / 1600.0F));
   }

   public static void internalMethod03971(HudRenderEvent localValue0) {
      MinecraftClient localValue1 = MinecraftClient.getInstance();
      float localValue2 = localValue1.getWindow().getScaledWidth();
      float localValue3 = localValue1.getWindow().getScaledHeight();
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
      RenderSystem.setShaderTexture(0, internalField0769.getColorAttachmentView());
      Matrix4f localValue4 = rockstar.client.render.GuiMatrixCompat.toMatrix4f(localValue0.getContext().getMatrices());
      int localValue5 = ColorRGBA.WHITE.getRGB();
      BufferBuilder localValue6 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
      localValue6.vertex(localValue4, 0.0F, 0.0F, 0.0F).texture(0.0F, 1.0F).color(localValue5);
      localValue6.vertex(localValue4, 0.0F, localValue3, 0.0F).texture(0.0F, 0.0F).color(localValue5);
      localValue6.vertex(localValue4, localValue2, localValue3, 0.0F).texture(1.0F, 0.0F).color(localValue5);
      localValue6.vertex(localValue4, localValue2, 0.0F, 0.0F).texture(1.0F, 1.0F).color(localValue5);
      BufferRenderer.drawWithGlobalProgram(localValue6.end());
      RenderSystem.setShaderTexture(0, 0);
      RenderSystem.disableBlend();
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
   }

   private void internalMethod09824() {
      RockstarClient.getInstance().internalMethod02152().internalMethod07804();
      RockstarClient.getInstance().internalMethod03371().internalMethod08923();
   }

   private ColorRGBA internalMethod05164() {
      return ThemeColors.internalMethod08459();
   }

   private ColorRGBA internalMethod01353() {
      return ThemeColors.internalMethod07738().withAlpha(102.0F);
   }

   private ColorRGBA internalMethod07666() {
      return this.internalMethod01353();
   }

   private ColorRGBA internalMethod08545() {
      return ThemeColors.internalField1616.withAlpha(89.25F);
   }

   private void internalMethod02726(ScrollController localValue1) {
      localValue1.internalMethod02712(-3.0F)
         .internalMethod09005(2.0F)
         .internalMethod00894(2.0F)
         .internalMethod08056(18.0F)
         .internalMethod07954(1.0F)
         .internalMethod08313(1100.0F)
         .internalMethod04404(
            localValue1x -> this.internalMethod05164().withAlpha(255.0F * (0.28F + 0.24F * localValue1x.internalMethod05170() + 0.28F * localValue1x.internalMethod05173()))
         );
   }

   private static ColorRGBA internalMethod07226(ColorRGBA localValue0, ColorRGBA localValue1, float localValue2) {
      return localValue0.mix(localValue1.withAlpha(localValue0.getAlpha()), localValue2);
   }

   private static boolean internalMethod03293(List<?> localValue0, List<?> localValue1) {
      if (localValue0.size() != localValue1.size()) {
         return false;
      } else {
         for (int localValue2 = 0; localValue2 < localValue0.size(); localValue2++) {
            if (localValue0.get(localValue2) != localValue1.get(localValue2)) {
               return false;
            }
         }

         return true;
      }
   }

   @Override
   public boolean shouldPause() {
      return false;
   }

   @Override
   public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
   }

   static {
      RockstarClient.getInstance().internalMethod03317().internalMethod00647(new ModernMenuScreen.InternalType0265());
   }

   static final class InternalType0265 {
      final EventListener<HudRenderEvent> internalField0157 = localValue0 -> {
         ModernMenuScreen localValue1 = ModernMenuScreen.internalField0550;
         if (localValue1 != null) {
            MinecraftClient localValue2 = MinecraftClient.getInstance();
            if (localValue1.internalMethod09823() >= 1.0F) {
               ModernMenuScreen.internalField0550 = null;
               ModernMenuScreen.internalField0277 = false;
               ModernMenuScreen.internalField0276 = false;
            } else if (localValue2.currentScreen == null) {
               if (!ModernMenuScreen.internalField0277) {
                  ModernMenuScreen.internalField0769.setClearColor(0.0F, 0.0F, 0.0F, 0.0F);
                  ModernMenuScreen.internalField0769.internalMethod02227(true);
                  UiBatchRenderer.internalField0277 = true;

                  try {
                     localValue1.render(UiRenderContext.internalMethod02316(localValue0.getContext(), -1, -1, localValue0.getTickDelta()));
                  } finally {
                     UiBatchRenderer.internalField0277 = false;
                     ModernMenuScreen.internalField0769.internalMethod03248();
                  }

                  ModernMenuScreen.internalField0277 = true;
               }

               if (!ModernMenuScreen.internalField0276) {
                  ModernMenuScreen.internalMethod03971(localValue0);
               }
            }
         }
      };
      final EventListener<Render3DEvent> internalField0158 = EventListener.internalMethod05136(
         Integer.MIN_VALUE,
         localValue0 -> {
            ModernMenuScreen localValue1 = ModernMenuScreen.internalField0550;
            if (localValue1 != null && ModernMenuScreen.internalField0277 && localValue1.internalField0283 != null) {
               MinecraftClient localValue2 = MinecraftClient.getInstance();
               float localValue3 = localValue1.internalMethod09823();
               float localValue4 = Math.min(1.0F, localValue3 / 0.4F);
               float localValue5 = localValue3 <= 0.4F ? 0.0F : (localValue3 - 0.4F) / 0.6F;
               float localValue6 = Easing.internalField1328.ease(localValue4, 0.0F, 1.0F, 1.0F);
               float localValue7 = 1.0F - localValue6;
               float localValue8 = Math.min(1.0F, Math.max(0.0F, (localValue5 - 0.15F) / 0.85F));
               float localValue10 = (float)(
                  Math.tan(Math.toRadians(((Integer)localValue2.options.getFov().getValue()).intValue()) / 2.0) / Math.tan(Math.toRadians(110.0) / 2.0)
               );
               float localValue11 = (3.3F + localValue7) * localValue10;
               float localValue12 = localValue11 * ((float)localValue2.getWindow().getFramebufferWidth() / localValue2.getWindow().getFramebufferHeight());
               Vec3d localValue13 = localValue2.gameRenderer.getCamera().getCameraPos();
               Vec3d localValue14 = localValue1.internalField0283;
               Vec3d localValue15 = localValue1.internalField1104.multiply(localValue12 / 2.0);
               Vec3d localValue16 = localValue1.internalField1106.multiply(localValue11 / 2.0);
               Vec3d localValue17 = localValue14.subtract(localValue15).add(localValue16).subtract(localValue13);
               Vec3d localValue18 = localValue14.subtract(localValue15).subtract(localValue16).subtract(localValue13);
               Vec3d localValue19 = localValue14.add(localValue15).subtract(localValue16).subtract(localValue13);
               Vec3d localValue20 = localValue14.add(localValue15).add(localValue16).subtract(localValue13);
               RenderSystem.enableBlend();
               RenderSystem.defaultBlendFunc();
               RenderSystem.disableDepthTest();
               RenderSystem.disableCull();
               RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
               RenderSystem.setShaderTexture(0, ModernMenuScreen.internalField0769.getColorAttachmentView());
               Matrix4f localValue21 = localValue0.getMatrices().peek().getPositionMatrix();
               int localValue22 = ColorRGBA.WHITE.withAlpha(255.0F * localValue7).getRGB();
               BufferBuilder localValue23 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
               localValue23.vertex(localValue21, (float)localValue17.x, (float)localValue17.y, (float)localValue17.z).texture(0.0F, 1.0F).color(localValue22);
               localValue23.vertex(localValue21, (float)localValue18.x, (float)localValue18.y, (float)localValue18.z).texture(0.0F, 0.0F).color(localValue22);
               localValue23.vertex(localValue21, (float)localValue19.x, (float)localValue19.y, (float)localValue19.z).texture(1.0F, 0.0F).color(localValue22);
               localValue23.vertex(localValue21, (float)localValue20.x, (float)localValue20.y, (float)localValue20.z).texture(1.0F, 1.0F).color(localValue22);
               BufferRenderer.drawWithGlobalProgram(localValue23.end());
               ModernMenuScreen.internalField0276 = true;
               RenderSystem.enableDepthTest();
               RenderSystem.enableCull();
               RenderSystem.disableBlend();
               RenderSystem.setShaderTexture(0, 0);
               RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
               if (localValue3 >= 1.0F) {
                  ModernMenuScreen.internalField0550 = null;
                  ModernMenuScreen.internalField0277 = false;
               }
            }
         }
      );
   }
}
