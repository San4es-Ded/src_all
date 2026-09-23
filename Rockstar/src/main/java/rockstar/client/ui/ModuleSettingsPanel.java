package rockstar.client.ui;








import rockstar.client.setting.*;
import rockstar.client.render.*;
import rockstar.client.module.*;
import rockstar.client.i18n.*;
import rockstar.client.core.*;
import rockstar.client.animation.*;
import rockstar.client.*;
import rockstar.client.internal.ui.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.internal.core.*;
import rockstar.modules.combat.*;
import rockstar.modules.movement.*;
import rockstar.modules.visual.*;
import rockstar.modules.player.*;
import rockstar.modules.other.*;

import globals.client.Information;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import pyrock.utility.render.ColorRGBA;
import rockstar.profile.Profile;

public class ModuleSettingsPanel extends UiContainer implements SettingOwner {
   private static final float internalField0205 = 94.0F;
   private static final float internalField0206 = 24.0F;
   private static final float internalField1048 = 207.0F;
   private static final float internalField1047 = 24.0F;
   private static final SizedFont internalField0447 = Fonts.internalField0449.internalMethod01432(9.0F);
   private final AnimatedFloat internalField0623 = new AnimatedFloat(Motion.internalMethod07185(300.0F, 28.0F));
   private final UiNode internalField0633;
   private final BiConsumer<ModuleEntry, Setting> internalField0048;
   private final UiNode internalField0632;
   private final UiNode internalField1253;
   private final UiNode internalField1252;
   private final UiNode internalField1250;
   private final UiNode internalField1251;
   private final UiContainer internalField0634;
   private final List<Setting> internalField0416 = new ArrayList<>();
   private final ModeSetting internalField0668;
   private final ColorSetting internalField0665;
   private final BooleanSetting internalField0650;
   private final UiContainer internalField0635;
   private boolean internalField0277;
   private boolean internalField0276;
   private UiNode internalField1581;
   private int internalField0227;
   private long internalField0230;
   private long internalField1059;
   private static final UiTransition internalField0918 = (localValue0, localValue1, localValue2) -> {
      localValue2.internalField0205 = Math.min(1.0F, Math.max(0.0F, localValue0));
      localValue2.internalField1047 = 0.7F + 0.3F * localValue0;
   };
   private final UiNode internalField1582;
   private final UiNode internalField1584;
   private final UiContainer internalField1257;
   private final UiContainer internalField1254;
   private boolean internalField1099;
   private final List<ModuleSettingsPanel.InternalType0342> internalField0417 = new ArrayList<>();
   private CustomTextField internalField0936;
   private boolean internalField1100;
   private long internalField1060;
   private boolean internalField1102;
   private String internalField0248 = null;
   private int internalField0228;
   private UiNode internalField1583;
   private SizedFont internalField0448;
   private final List<ModuleSettingsPanel.InternalType0343> internalField1145 = new ArrayList<>();
   public ModuleSettingsPanel(UiNode localValue1, BiConsumer<ModuleEntry, Setting> localValue2) {
      this.internalField0633 = localValue1;
      this.internalField0048 = localValue2;
      UiContainer localValue4 = new UiContainer()
         .internalMethod01863()
         .internalMethod03062(2.0F)
         .internalMethod01855(TextAlignment.internalField0622)
         .internalMethod07853(false)
         .internalMethod03907(
            new UiElement()
               .text(Fonts.internalField0449.internalMethod01432(7.0F), Profile::getUsername, localValue0 -> ThemeColors.internalField1613)
               .interactive(false)
         );
      this.internalField0632 = new UiContainer()
         .internalMethod05895()
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod03062(4.0F)
         .internalMethod09609()
          .internalMethod03907(localValue4);
      this.internalField1253 = this.internalMethod06086("setting", this::internalMethod09583);
      this.internalField1252 = this.internalMethod06086("search", () -> this.internalMethod08478(true));
      this.internalField1251 = this.internalMethod06086("xmark", () -> this.internalMethod08478(false));
      this.internalField1250 = new UiElement()
         .fillWidth()
         .height(24.0F)
         .cursor(CursorType.internalField1206)
         .onClick((localValue1x, localValue2x, localValue3x) -> this.internalMethod07310().internalMethod01643(localValue2x, localValue3x, localValue1x))
         .paint((localValue1x, localValue2x) -> {
            CustomTextField localValue3x = this.internalMethod07310();
            localValue3x.internalMethod05191(localValue2x.x() - 4.0F, localValue2x.y(), localValue2x.w() + 4.0F, localValue2x.h());
            localValue3x.internalMethod00143(ThemeColors.internalField1613);
            localValue3x.internalMethod08627(1.0F);
            localValue3x.internalMethod03398(localValue1x);
         });
      this.internalMethod05895()
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod07607(LayoutAlignment.internalField0911)
         .internalMethod03062(6.0F)
         .internalMethod03514(Insets.internalMethod00105(0.0F, 8.0F, 0.0F, 6.0F))
         .internalMethod09339(94.0F)
         .internalMethod09266(24.0F)
         .internalMethod05690(() -> {})
         .internalMethod03907(this.internalField0632)
         .internalMethod03907(this.internalField1253)
         .internalMethod03907(this.internalField1252)
         .internalMethod07178(
            (localValue1x, localValue2x) -> {
               this.internalMethod09580();
               if (!this.internalField0276) {
                  localValue1x.drawShadow(
                     localValue2x.x(),
                     localValue2x.y(),
                     localValue2x.w(),
                     localValue2x.h(),
                     10.0F,
                     CornerRadii.internalMethod03908(5.0F),
                     ThemeColors.internalField1309.mulAlpha(0.15F)
                  );
                  this.internalMethod06772(localValue1x, localValue2x, 8.0F);
               }
            }
         );
      internalMethod00742(this);
      this.internalField0632.snapPosition();
      this.internalField1253.snapPosition();
      this.internalField1252.snapPosition(() -> !this.internalField1100);
      this.internalField1250.exit(UiTransition.internalField0919).lifeMotion(Motion.internalMethod01328(160L, Easing.internalField1822));
      this.internalField1251.exit(UiTransition.internalField0919).lifeMotion(Motion.internalMethod01328(160L, Easing.internalField1822));
      this.internalField0634 = (new UiContainer() {
            @Override
            public boolean mouseScrolled(float localValue1, float localValue2x, float localValue3x, float localValue4x) {
               return super.mouseScrolled(localValue1, localValue2x, localValue3x, localValue4x) || this.inFlow() && this.contains(localValue1, localValue2x);
            }
         })
         .internalMethod01863()
         .internalMethod09339(207.0F)
         .internalMethod08392(207.0F, 150.0F)
         .internalMethod05690(() -> {})
         .internalMethod08755()
         .internalMethod05391(
            localValue0 -> localValue0.internalMethod02712(3.0F)
               .internalMethod02066(7.0F, 6.0F)
               .internalMethod00894(2.5F)
               .internalMethod04404(
                  localValue0x -> ColorRGBA.BLACK
                     .mix(ColorRGBA.WHITE, 0.3F)
                     .withAlpha(255.0F * (0.32F + 0.28F * localValue0x.internalMethod05170() + 0.3F * localValue0x.internalMethod05173()))
               )
         )
         .internalMethod06712(() -> this.internalField1100 && this.internalField1102 && !this.internalField1145.isEmpty(), Easing.internalField1828, 240L)
         .internalMethod09936()
         .internalMethod07178(
            (localValue1x, localValue2x) -> {
               if (this.internalField0276) {
                  localValue1x.drawShadow(
                     localValue2x.x(),
                     localValue2x.y(),
                     localValue2x.w(),
                     localValue2x.h(),
                     10.0F,
                     CornerRadii.internalMethod03908(11.0F),
                     ThemeColors.internalField1309.mulAlpha(0.5F)
                  );
                  this.internalMethod04217(localValue1x, localValue2x, 11.0F);
               } else {
                  localValue1x.drawShadow(
                     localValue2x.x(),
                     localValue2x.y(),
                     localValue2x.w(),
                     localValue2x.h(),
                     10.0F,
                     CornerRadii.internalMethod03908(11.0F),
                     ThemeColors.internalField1309.mulAlpha(0.5F)
                  );
                  this.internalMethod06772(localValue1x, localValue2x, 11.0F);
               }
            }
         );
      this.internalField0650 = new BooleanSetting(this, "configs.autosave").internalMethod04836(internalMethod03894().internalMethod03463());
      this.internalField0668 = new ModeSetting(this, "modules.settings.interface.language");
      new ModeSetting.InternalType0088(this.internalField0668, "\u0420\u0443\u0441\u0441\u043a\u0438\u0439");
      new ModeSetting.InternalType0088(this.internalField0668, "English");
      new ModeSetting.InternalType0088(this.internalField0668, "\u0423\u043a\u0440\u0430\u0457\u043d\u0441\u044c\u043a\u0430");
      new ModeSetting.InternalType0088(this.internalField0668, "polski");
      this.internalField0227 = internalMethod01233(LanguageManager.internalMethod00625());
      this.internalField0668.internalMethod03917(this.internalField0668.internalMethod06723().get(this.internalField0227));
      this.internalField0665 = new ColorSetting(this, "theme.colors.accent")
         .internalMethod05166(false)
         .internalMethod04886(ThemeColors.internalMethod03506());
      UiTransition localValue5 = (localValue0, localValue1x, localValue2x) -> {
         localValue2x.internalField0205 = 1.0F;
         float localValue3x = Math.max(0.0F, localValue0);
         localValue2x.internalField1047 = localValue3x;
         localValue2x.internalField0206 = -(1.0F - localValue3x) * localValue1x.w() / 2.0F;
         localValue2x.internalField1048 = (1.0F - localValue3x) * localValue1x.h() / 2.0F;
      };
      this.internalField0635 = (new UiContainer() {
            @Override
            public boolean mouseScrolled(float localValue1, float localValue2, float localValue3, float localValue4) {
               return super.mouseScrolled(localValue1, localValue2, localValue3, localValue4) || this.inFlow() && this.contains(localValue1, localValue2);
            }

            @Override
            protected void drawChildren(UiRenderContext localValue1, float localValue2x) {
               ScissorStack.internalMethod06303(localValue1.getMatrices(), this.x(), this.y(), this.w(), this.h());
               super.drawChildren(localValue1, localValue2x);
               ScissorStack.internalMethod07643();
            }
         })
         .internalMethod01863()
         .internalMethod09339(115.0F)
         .internalMethod03514(Insets.internalMethod05266(6.0F, 0.0F))
         .internalMethod05690(() -> {})
         .internalMethod03855(() -> this.internalField0277 && !this.internalField1100)
         .internalMethod07914(localValue5)
         .internalMethod05305(Motion.internalMethod01328(300L, Easing.internalField0812))
         .internalMethod03907(this.internalMethod10011())
         .internalMethod03907(SettingComponentFactory.internalMethod03724(this.internalField0650))
         .internalMethod03907(SettingComponentFactory.internalMethod03724(this.internalField0665))
         .internalMethod03907(SettingComponentFactory.internalMethod03724(this.internalField0668))
         .internalMethod07178(
            (localValue1x, localValue2x) -> {
               localValue1x.drawShadow(
                  localValue2x.x(), localValue2x.y(), localValue2x.w(), localValue2x.h(), 10.0F, CornerRadii.internalMethod03908(11.0F), ThemeColors.internalField1309.mulAlpha(0.5F)
               );
               this.internalMethod06772(localValue1x, localValue2x, 11.0F);
            }
         );
      this.internalField1582 = new UiElement()
         .text(
            Fonts.internalField0449.internalMethod01432(8.0F),
            () -> LanguageManager.internalMethod07214("profile.binds.title"),
            localValue0 -> ThemeColors.internalField1613
         )
         .interactive(false);
      this.internalField1584 = this.internalMethod06086("xmark", () -> this.internalMethod02382(false));
      UiContainer localValue6 = new UiContainer()
         .internalMethod05895()
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod07607(LayoutAlignment.internalField1377)
         .internalMethod09609()
         .internalMethod09266(14.0F)
         .internalMethod03514(Insets.internalMethod05266(0.0F, 9.0F))
         .internalMethod03907(this.internalField1582)
         .internalMethod03907(this.internalField1584);
      this.internalField1254 = (new UiContainer() {
            @Override
            public boolean mouseScrolled(float localValue1, float localValue2, float localValue3, float localValue4) {
               return super.mouseScrolled(localValue1, localValue2, localValue3, localValue4) || this.inFlow() && this.contains(localValue1, localValue2);
            }
         })
         .internalMethod01863()
         .internalMethod09609()
         .internalMethod08392(207.0F, 150.0F)
         .internalMethod05690(() -> {})
         .internalMethod08755()
         .internalMethod05391(
            localValue0 -> localValue0.internalMethod02712(3.0F)
               .internalMethod02066(1.0F, 6.0F)
               .internalMethod00894(2.5F)
               .internalMethod04404(
                  localValue0x -> ColorRGBA.BLACK
                     .mix(ColorRGBA.WHITE, 0.3F)
                     .withAlpha(255.0F * (0.32F + 0.28F * localValue0x.internalMethod05170() + 0.3F * localValue0x.internalMethod05173()))
               )
         );
      this.internalField1257 = (new UiContainer() {
            @Override
            protected void drawChildren(UiRenderContext localValue1, float localValue2) {
               ScissorStack.internalMethod06303(localValue1.getMatrices(), this.x(), this.y(), this.w(), this.h());
               super.drawChildren(localValue1, localValue2);
               ScissorStack.internalMethod07643();
            }
         })
         .internalMethod01863()
         .internalMethod09609()
         .internalMethod03514(Insets.internalMethod00105(7.0F, 0.0F, 0.0F, 0.0F))
         .internalMethod03062(2.0F)
         .internalMethod01619(UiTransition.internalField1389)
         .internalMethod02991(UiTransition.internalField0919)
         .internalMethod05305(Motion.internalMethod01328(220L, Easing.internalField1822))
         .internalMethod03907(localValue6)
         .internalMethod03907(this.internalField1254);
      this.internalField1257
         .internalMethod07178(
            (localValue1x, localValue2x) -> {
               if (this.internalField0276) {
                  boolean localValue3x = RockstarClient.getInstance().internalMethod04467().internalMethod05065() == ThemeVariant.internalField0395;
                  ColorRGBA localValue4x = (localValue3x ? ThemeColors.internalMethod07738() : ThemeColors.internalMethod08573()).withAlpha(255.0F);
                  localValue1x.drawShadow(
                     localValue2x.x(),
                     localValue2x.y(),
                     localValue2x.w(),
                     localValue2x.h(),
                     10.0F,
                     CornerRadii.internalMethod03908(8.0F),
                     ThemeColors.internalField1309.mulAlpha(0.15F)
                  );
                  localValue1x.drawRoundedRect(localValue2x.x(), localValue2x.y(), localValue2x.w(), localValue2x.h(), CornerRadii.internalMethod03908(8.0F), localValue4x);
                  localValue1x.drawRoundedBorder(
                     localValue2x.x(),
                     localValue2x.y(),
                     localValue2x.w(),
                     localValue2x.h(),
                     0.5F,
                     CornerRadii.internalMethod03908(8.0F),
                     ThemeColors.internalField1616.withAlpha(89.25F)
                  );
               }
            }
         );
      internalMethod05032(this.internalField1257);
   }

   private UiContainer internalMethod10011() {
      UiContainer localValue1 = new UiContainer()
         .internalMethod05895()
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod03062(6.0F)
         .internalMethod09609()
         .internalMethod09266(20.0F)
         .internalMethod03514(Insets.internalMethod05266(0.0F, 9.0F))
         .internalMethod04332(CursorType.internalField0567);
      UiElement localValue2 = new UiElement()
         .size(8.0F, 8.0F)
         .icon("keyboard", 8.0F, localValue1x -> ThemeColors.internalField1613.mulAlpha(0.5F + 0.4F * localValue1.hover()))
         .interactive(false);
      UiElement localValue3 = new UiElement()
         .text(
            Fonts.internalField1154.internalMethod01432(8.0F),
            () -> LanguageManager.internalMethod07214("profile.binds.title"),
            localValue1x -> ThemeColors.internalField1613.mulAlpha(0.75F + 0.25F * localValue1.hover())
         )
         .fill()
         .interactive(false);
      return localValue1.internalMethod03907(localValue2).internalMethod03907(localValue3).internalMethod05690(() -> {
         this.internalMethod02319(false);
         this.internalMethod02382(true);
      });
   }

   public UiContainer internalMethod10099() {
      return this.internalField0634;
   }

   public UiContainer internalMethod09983() {
      return this.internalField0635;
   }

   public ModuleSettingsPanel internalMethod02427(UiNode localValue1) {
      this.internalField0276 = true;
      this.internalField1581 = localValue1;
      this.internalMethod06213(List.of());
      this.internalField1257.internalMethod07914(internalField0918);
      return this;
   }

   public ModuleSettingsPanel internalMethod02280(UiNode localValue1, SizedFont localValue2) {
      this.internalField1583 = localValue1;
      this.internalField0448 = localValue2;
      return this;
   }

   public CustomTextField internalMethod06652() {
      return this.internalMethod07310();
   }

   private static ColorRGBA internalMethod06765() {
      ColorRGBA localValue0 = ThemeColors.internalMethod07738().withAlpha(173.40001F);
      return localValue0.mix(ThemeColors.internalField1613.withAlpha(localValue0.getAlpha()), 0.035F);
   }

   private void internalMethod09580() {
      if (this.internalField0633 != null && this.internalMethod08156()) {
         UiBatchRenderer.internalMethod02576();
         RenderPipeline.internalField0312.internalMethod03955(1, 1.0F);
      }
   }

   private boolean internalMethod08156() {
      boolean localValue1 = this.internalField1100 || this.internalField0277 || !this.internalField0276 && this.internalField1099;
      long localValue2 = System.currentTimeMillis();
      if (localValue1) {
         this.internalField1059 = localValue2 + 400L;
      }

      return localValue2 < this.internalField1059;
   }

   private void internalMethod06772(UiRenderContext localValue1, UiNode localValue2, float localValue3) {
      CornerRadii localValue4 = CornerRadii.internalMethod03908(localValue3);
      boolean localValue5 = this.internalMethod08156();
      if (localValue5) {
         RenderPipeline.internalMethod07025(1);
      }

      try {
         localValue1.drawBlurredRect(localValue2.x(), localValue2.y(), localValue2.w(), localValue2.h(), 5.0F, 3.0F, localValue4, ThemeColors.internalField1312);
         localValue1.drawClientRect(localValue2.x(), localValue2.y(), localValue2.w(), localValue2.h(), 1.0F, 0.0F, 3.0F, localValue3, true);
      } finally {
         if (localValue5) {
            RenderPipeline.internalMethod01905();
         }
      }
   }

   private void internalMethod04217(UiRenderContext localValue1, UiNode localValue2, float localValue3) {
      CornerRadii localValue4 = CornerRadii.internalMethod03908(localValue3);
      boolean localValue5 = this.internalField0633 != null
         && this.internalMethod08156()
         && !UiBatchRenderer.internalField0277
         && RenderPipeline.internalField0312.internalMethod02312(1);
      if (localValue5) {
         UiBatchRenderer.internalMethod02576();
         float localValue6 = 16.0F;
         localValue1.internalMethod05234(
            1,
            localValue2.x(),
            localValue2.y(),
            localValue2.w(),
            localValue2.h(),
            1.5F,
            1.0F,
            1.2F,
            this.internalField0633.x() + localValue6,
            this.internalField0633.y() + localValue6,
            this.internalField0633.w() - 2.0F * localValue6,
            this.internalField0633.h() - 2.0F * localValue6,
            localValue4,
            ThemeColors.internalField1312
         );
      }

      localValue1.drawRoundedRect(localValue2.x(), localValue2.y(), localValue2.w(), localValue2.h(), localValue4, localValue5 ? internalMethod06765() : internalMethod06765().withAlpha(255.0F));
      localValue1.drawRoundedBorder(localValue2.x(), localValue2.y(), localValue2.w(), localValue2.h(), 0.5F, localValue4, ThemeColors.internalField1616.withAlpha(89.25F));
   }

   public void internalMethod08139() {
      this.internalMethod08478(true);
   }

   public void internalMethod08141() {
      this.internalMethod08478(false);
   }

   public void internalMethod08155() {
      this.internalMethod09583();
   }

   @Override
   public List<Setting> getSettings() {
      return this.internalField0416;
   }

   private static int internalMethod01233(Language localValue0) {
      return switch (localValue0) {
         case internalField0165 -> 1;
         case internalField1031 -> 2;
         case internalField1032 -> 3;
         default -> 0;
      };
   }

   private void internalMethod09583() {
      this.internalMethod02319(!this.internalField0277);
   }

   private void internalMethod02319(boolean localValue1) {
      if (this.internalField0277 != localValue1) {
         this.internalField0635
            .internalMethod05305(Motion.internalMethod01328(300L, localValue1 ? Easing.internalField0812 : Easing.internalField1328));
         this.internalField0277 = localValue1;
         if (!localValue1) {
            this.internalField0665.internalMethod03824();
         } else {
            this.internalField0665.internalMethod07054(ThemeColors.internalMethod03506());
            this.internalField0227 = internalMethod01233(LanguageManager.internalMethod00625());
            this.internalField0668.internalMethod03917(this.internalField0668.internalMethod06723().get(this.internalField0227));
            this.internalField0650.internalMethod02034(internalMethod03894().internalMethod03463());
         }
      }
   }

   public void internalMethod04148(float localValue1, float localValue2) {
      boolean localValue3 = !this.internalField0276 && this.contains(localValue1, localValue2)
         || this.internalField0276 && this.internalField1581 != null && this.internalField1581.contains(localValue1, localValue2);
      if (this.internalField0277 && !localValue3 && !this.internalField0635.contains(localValue1, localValue2)) {
         this.internalMethod02319(false);
      }

      if (this.internalField1099 && !this.contains(localValue1, localValue2)) {
         this.internalMethod02382(false);
      }

      if (this.internalField1100
         && this.internalField1583 != null
         && !this.internalField1583.contains(localValue1, localValue2)
         && (!this.internalField0634.inFlow() || !this.internalField0634.contains(localValue1, localValue2))) {
         this.internalMethod08478(false);
      }
   }

   public void internalMethod08158() {
      this.internalMethod02382(true);
   }

   public boolean internalMethod00442() {
      return this.internalField1099 || this.internalMethod08159();
   }

   private boolean internalMethod08159() {
      return this.internalField0276 && this.internalField1257.phase() == UiNode.InternalType0146.internalField1091;
   }

   private void internalMethod02382(boolean localValue1) {
      if (this.internalField1099 != localValue1) {
         this.internalField1099 = localValue1;
         Motion localValue2 = localValue1 ? Motion.internalField0914 : Motion.internalMethod01328(320L, Easing.internalField1828);
         this.internalMethod03754(localValue2);
         if (localValue1) {
            this.internalMethod02319(false);
            this.internalMethod08478(false);
            this.internalMethod09584();
            float localValue3 = Math.min(150.0F, Math.max(1, this.internalField0417.size()) * 18.0F + 6.0F);
            float localValue4 = 23.0F + localValue3;
            this.internalMethod09339(207.0F).internalMethod09266(localValue4).internalMethod03514(Insets.internalField0910);
            if (this.internalField0276) {
               Window localValue5 = MinecraftClient.getInstance().getWindow();
               this.snapToSize(207.0F, localValue4);
               this.snapAt((localValue5.getScaledWidth() - 207.0F) / 2.0F, (localValue5.getScaledHeight() - localValue4) / 2.0F);
               this.internalField1257.internalMethod05305(Motion.internalMethod01328(300L, Easing.internalField0812));
            }

            this.internalMethod07849(List.of(this.internalField1257));
         } else if (this.internalField0276) {
            this.internalField1257.internalMethod05305(Motion.internalMethod01328(300L, Easing.internalField1328));
            this.internalMethod07849(List.of());
         } else {
            this.internalField0632.enter(UiTransition.internalField1389).lifeMotion(Motion.internalMethod01328(220L, Easing.internalField1822));
            this.internalField1253.enter(UiTransition.internalField1389).lifeMotion(Motion.internalMethod01328(220L, Easing.internalField1822));
            this.internalField1252.enter(UiTransition.internalField1389).lifeMotion(Motion.internalMethod01328(220L, Easing.internalField1822));
            this.internalMethod09339(94.0F).internalMethod09266(24.0F).internalMethod03514(Insets.internalMethod00105(0.0F, 8.0F, 0.0F, 6.0F));
            this.internalMethod07849(List.of(this.internalField0632, this.internalField1253, this.internalField1252));
         }
      }
   }

   private void internalMethod09584() {
      this.internalField0417.clear();

      for (ModuleEntry localValue3 : RockstarClient.getInstance()
         .getModuleManager()
         .getModules()
         .stream()
         .filter(ModuleEntry::isAvailable)
         .sorted(Comparator.comparing(ModuleEntry::getName))
         .toList()) {
         if (localValue3.getKeybind() != -1) {
            this.internalField0417.add(new ModuleSettingsPanel.InternalType0342(List.of(localValue3.getName()), localValue3::getKeybind, localValue1 -> {
               localValue3.setKeybind(localValue1);
               internalMethod09742();
            }));
         }

         for (Setting localValue5 : localValue3.getSettings()) {
            if (localValue5 instanceof KeybindSetting localValue6 && localValue6.isVisible() && localValue6.internalMethod07477() != -1) {
               this.internalField0417
                  .add(
                     new ModuleSettingsPanel.InternalType0342(
                        List.of(localValue3.getName(), LanguageManager.internalMethod07214(localValue5.getName())), localValue6::internalMethod07477, localValue1 -> {
                           localValue6.internalMethod02164(localValue1);
                           internalMethod09742();
                        }
                     )
                  );
            }
         }
      }

      AssistModule localValue7 = RockstarClient.getInstance().getModuleManager().getModule(AssistModule.class);
      if (localValue7 != null) {
         for (AssistSwapItem localValue10 : localValue7.internalMethod03313()) {
            if (localValue10.internalMethod03234() != -1) {
               this.internalField0417
                  .add(
                     new ModuleSettingsPanel.InternalType0342(
                        List.of("Assist", "\u041c\u0430\u043a\u0440\u043e\u0441\u044b", LanguageManager.internalMethod07214(localValue10.internalMethod06026())),
                        localValue10::internalMethod03234,
                        localValue1 -> {
                           localValue10.internalMethod01910(localValue1);
                           internalMethod09742();
                        }
                     )
                  );
            }
         }
      }

      ChatMacroManager localValue9 = RockstarClient.getInstance().internalMethod05155();
      if (localValue9 != null) {
         for (ChatMacroEntry localValue13 : localValue9.internalMethod06721()) {
            if (localValue13.internalMethod03890() != -1) {
               String localValue16 = localValue13.internalMethod07169();
               this.internalField0417
                  .add(new ModuleSettingsPanel.InternalType0342(List.of(localValue16), () -> internalMethod05603(localValue16), localValue1 -> internalMethod07129(localValue16, localValue1)));
            }
         }
      }

      ArrayList localValue12 = new ArrayList(this.internalField0417.size() + 1);
      if (this.internalField0417.isEmpty()) {
         localValue12.add(this.internalMethod10013());
      }

      for (ModuleSettingsPanel.InternalType0342 localValue17 : this.internalField0417) {
         localValue12.add(this.internalMethod02356(localValue17));
      }

      UiElement localValue15 = new UiElement().fillWidth().height(6.0F).interactive(false);
      internalMethod05032(localValue15);
      localValue12.add(localValue15);
      this.internalField1254.internalMethod07849(localValue12);
   }

   private static LocalConfigStore internalMethod03894() {
      return RockstarClient.getInstance().internalMethod02152();
   }

   private static void internalMethod09742() {
      internalMethod03894().internalMethod07804();
   }

   private static void internalMethod09743() {
      RockstarClient.getInstance().internalMethod03371().internalMethod05165("client");
   }

   private static int internalMethod05603(String localValue0) {
      ChatMacroManager localValue1 = RockstarClient.getInstance().internalMethod05155();
      if (localValue1 != null) {
         for (ChatMacroEntry localValue3 : localValue1.internalMethod06721()) {
            if (localValue3.internalMethod07169().equalsIgnoreCase(localValue0)) {
               return localValue3.internalMethod03890();
            }
         }
      }

      return -1;
   }

   private static void internalMethod07129(String localValue0, int localValue1) {
      ChatMacroManager localValue2 = RockstarClient.getInstance().internalMethod05155();
      if (localValue2 != null) {
         localValue2.internalMethod04540(localValue0);
         if (localValue1 != -1) {
            localValue2.internalMethod01066(localValue0, localValue1);
         }

         internalMethod09743();
      }
   }

   private UiContainer internalMethod10013() {
      UiElement localValue1 = new UiElement()
         .fill()
         .interactive(false)
         .text(
            Fonts.internalField1154.internalMethod01432(8.0F),
            () -> LanguageManager.internalMethod07214("commands.bind.list_empty"),
            localValue0 -> ThemeColors.internalField1613.mulAlpha(0.5F)
         );
      UiContainer localValue2 = new UiContainer()
         .internalMethod05895()
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod09266(18.0F)
         .internalMethod09609()
         .internalMethod03514(Insets.internalMethod05266(0.0F, 9.0F))
         .internalMethod03907(localValue1);
      internalMethod05032(localValue2);
      return localValue2;
   }

   private UiContainer internalMethod02356(ModuleSettingsPanel.InternalType0342 localValue1) {
      KeybindButton localValue2 = new KeybindButton(Fonts.internalField1154.internalMethod01432(7.0F), localValue1.internalMethod00967(), localValue1.internalMethod00180());
      UiElement localValue3 = new UiElement().fill().interactive(false).paint((localValue2x, localValue3x) -> this.internalMethod04106(localValue2x, localValue3x, localValue1.internalMethod04552()));
      UiContainer localValue4 = new UiContainer()
         .internalMethod09266(18.0F)
         .internalMethod09609()
         .internalMethod03514(Insets.internalMethod05266(0.0F, 9.0F))
         .internalMethod03907(localValue3)
         .internalMethod03907(localValue2)
         .internalMethod03062(5.0F)
         .internalMethod01192(FlexDirection.internalField1246)
         .internalMethod07607(LayoutAlignment.internalField1377)
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod02525(localValue2::internalMethod00007)
         .internalMethod04332(CursorType.internalField0567);
      internalMethod05032(localValue4);
      return localValue4;
   }

   private void internalMethod04106(UiRenderContext localValue1, UiElement localValue2, List<String> localValue3) {
      SizedFont localValue4 = Fonts.internalField1154.internalMethod01432(8.0F);
      float localValue5 = localValue2.y() + localValue2.h() / 2.0F - localValue4.internalMethod04890() / 2.0F;
      float localValue6 = localValue2.x();

      for (int localValue7 = 0; localValue7 < localValue3.size(); localValue7++) {
         boolean localValue8 = localValue7 == localValue3.size() - 1;
         String localValue9 = (String)localValue3.get(localValue7);
         localValue1.drawText(localValue4, localValue9, localValue6, localValue5, ThemeColors.internalField1613.mulAlpha(localValue8 ? 1.0F : 0.5F));
         localValue6 += localValue4.internalMethod00965(localValue9);
         if (!localValue8) {
            float localValue10 = localValue6 + 3.0F;
            localValue1.drawRoundedRect(
               localValue10, localValue2.y() + localValue2.h() / 2.0F - 1.0F, 2.0F, 2.0F, CornerRadii.internalMethod03908(1.0F), ThemeColors.internalField1613.mulAlpha(0.5F)
            );
            localValue6 = localValue10 + 2.0F + 4.0F;
         }
      }
   }

   public boolean internalMethod08140() {
      return this.internalField1100;
   }

   private boolean internalMethod09579() {
      return this.internalField1583 != null && this.internalField0634.phase() == UiNode.InternalType0146.internalField1091;
   }

   private UiElement internalMethod06086(String localValue1, Runnable localValue2) {
      return new UiElement()
         .size(8.0F, 8.0F)
         .textInset(1.0F)
         .icon(localValue1, 8.0F, localValue0 -> ThemeColors.internalField1613.mulAlpha(0.5F + 0.4F * localValue0.hover()))
         .cursor(CursorType.internalField0567)
         .onClick(localValue2);
   }

   private static void internalMethod00742(UiNode localValue0) {
      localValue0.enter(UiTransition.internalField0918);
      if (localValue0 instanceof UiContainer localValue1) {
         for (UiNode localValue3 : localValue1.internalMethod01401()) {
            internalMethod00742(localValue3);
         }
      }
   }

   private static void internalMethod05032(UiNode localValue0) {
      localValue0.snapPosition().snapSize();
      if (localValue0 instanceof UiContainer localValue1) {
         for (UiNode localValue3 : localValue1.internalMethod01401()) {
            internalMethod05032(localValue3);
         }
      }
   }

   @Override
   protected void drawChildren(UiRenderContext localValue1, float localValue2) {
      if (!this.internalField0276 || this.internalField1099 || this.internalField1100 || this.internalMethod08159() || this.internalMethod09579()) {
         if (!this.internalField0276 && this.internalField1099) {
            org.joml.Matrix3x2fStack localValue3 = localValue1.getMatrices();
            ScissorStack.internalMethod06303(localValue3, this.x(), this.y(), this.w(), this.h());
            super.drawChildren(localValue1, localValue2);
            ScissorStack.internalMethod07643();
         } else {
            super.drawChildren(localValue1, localValue2);
         }
      }
   }

   @Override
   public boolean mouseClicked(float localValue1, float localValue2, MouseButton localValue3) {
      return this.internalField0276 && !this.internalField1099 ? false : super.mouseClicked(localValue1, localValue2, localValue3);
   }

   private void internalMethod08478(boolean localValue1) {
      if (this.internalField1100 != localValue1) {
         this.internalField1100 = localValue1;
         Motion localValue2 = localValue1 ? Motion.internalField0914 : Motion.internalMethod01328(320L, Easing.internalField1828);
         if (this.internalField1583 == null) {
            this.internalMethod03754(localValue2);
         }

         this.internalField1252.motion(localValue2);
         this.internalField0634
            .internalMethod05305(
               localValue1
                  ? Motion.internalMethod01328(180L, Easing.internalField1828)
                  : Motion.internalMethod01328(260L, Easing.internalField1828)
            );
         if (localValue1) {
            this.internalMethod02319(false);
            this.internalMethod02382(false);
            this.internalField1060 = System.currentTimeMillis();
            if (this.internalField1583 == null) {
               this.internalMethod09339(207.0F).internalMethod09266(24.0F).internalMethod03514(Insets.internalMethod05266(0.0F, 10.0F));
               this.internalMethod07849(List.of(this.internalField1252, this.internalField1250, this.internalField1251));
            }

            this.internalMethod07310().internalMethod09126();
            this.internalMethod07310().internalMethod00484("");
            this.internalMethod07310().internalMethod09000("\u041f\u043e\u0438\u0441\u043a");
            this.internalMethod07310().internalMethod07508(true);
            this.internalField0228 = 0;
            this.internalMethod09746();
            this.internalField0248 = "";
         } else if (this.internalField1583 != null) {
            this.internalMethod07310().internalMethod09126();
            this.internalMethod07310().internalMethod00484("");
            this.internalMethod07310().internalMethod07508(false);
         } else {
            this.internalField0632.enter(UiTransition.internalField1389).lifeMotion(Motion.internalMethod01328(220L, Easing.internalField1822));
            this.internalField1253.enter(UiTransition.internalField1389).lifeMotion(Motion.internalMethod01328(220L, Easing.internalField1822));
            this.internalMethod09339(94.0F).internalMethod09266(24.0F).internalMethod03514(Insets.internalMethod00105(0.0F, 8.0F, 0.0F, 6.0F));
            this.internalMethod07849(List.of(this.internalField0632, this.internalField1253, this.internalField1252));
            this.internalMethod07310().internalMethod07508(false);
         }
      }
   }

   private CustomTextField internalMethod07310() {
      if (this.internalField0936 == null) {
         this.internalField0936 = new CustomTextField(this.internalField0448 != null ? this.internalField0448 : internalField0447);
      }

      return this.internalField0936;
   }

   private String internalMethod07365() {
      String localValue1 = this.internalMethod07310().internalMethod06202();
      return localValue1 == null ? "" : localValue1.trim();
   }

   private static String internalMethod05318(String localValue0) {
      StringBuilder localValue1 = new StringBuilder(localValue0.length());

      for (char localValue5 : localValue0.toCharArray()) {
         int localValue6 = "\u0439\u0446\u0443\u043a\u0435\u043d\u0433\u0448\u0449\u0437\u0445\u044a\u0444\u044b\u0432\u0430\u043f\u0440\u043e\u043b\u0434\u0436\u044d\u044f\u0447\u0441\u043c\u0438\u0442\u044c\u0431\u044e."
            .indexOf(localValue5);
         if (localValue6 >= 0) {
            localValue1.append("qwertyuiop[]asdfghjkl;'zxcvbnm,./".charAt(localValue6));
         } else {
            localValue6 = "qwertyuiop[]asdfghjkl;'zxcvbnm,./".indexOf(localValue5);
            localValue1.append(
               localValue6 >= 0
                  ? "\u0439\u0446\u0443\u043a\u0435\u043d\u0433\u0448\u0449\u0437\u0445\u044a\u0444\u044b\u0432\u0430\u043f\u0440\u043e\u043b\u0434\u0436\u044d\u044f\u0447\u0441\u043c\u0438\u0442\u044c\u0431\u044e."
                     .charAt(localValue6)
                  : localValue5
            );
         }
      }

      return localValue1.toString();
   }

   private static int internalMethod02191(String localValue0, String localValue1, String localValue2) {
      String localValue3 = FuzzySearchUtils.internalMethod00096(localValue0);
      return Math.min(FuzzySearchUtils.internalMethod05951(localValue3, localValue1), FuzzySearchUtils.internalMethod05951(localValue3, localValue2));
   }

   private void internalMethod09746() {
      String localValue1 = this.internalMethod07365();
      String localValue2 = FuzzySearchUtils.internalMethod00096(localValue1);
      String localValue3 = FuzzySearchUtils.internalMethod00096(internalMethod05318(localValue1.toLowerCase()));
      this.internalField1145.clear();
      List localValue4 = RockstarClient.getInstance()
         .getModuleManager()
         .getModules()
         .stream()
         .filter(ModuleEntry::isAvailable)
         .sorted(Comparator.comparing(ModuleEntry::getName, String.CASE_INSENSITIVE_ORDER))
         .toList();
      if (localValue2.isEmpty()) {
         for (ModuleEntry localValue17 : (Iterable<ModuleEntry>)(Iterable<?>)localValue4) {
            this.internalField1145.add(new ModuleSettingsPanel.InternalType0343(localValue17, null, localValue17.getName(), 0));
         }

         this.internalMethod09747();
      } else {
         ModuleEntry localValue5 = null;
         int localValue6 = Integer.MAX_VALUE;

         for (ModuleEntry localValue8 : (Iterable<ModuleEntry>)(Iterable<?>)localValue4) {
            int localValue9 = internalMethod02191(localValue8.getName(), localValue2, localValue3);
            if (localValue9 <= 10 && localValue9 < localValue6) {
               localValue5 = localValue8;
               localValue6 = localValue9;
            }
         }

         if (localValue5 != null) {
            this.internalField1145.add(new ModuleSettingsPanel.InternalType0343(localValue5, null, localValue5.getName(), localValue6));
            int localValue19 = 0;

            for (Setting localValue23 : localValue5.getSettings()) {
               if (localValue23.isVisible()) {
                  this.internalField1145
                     .add(new ModuleSettingsPanel.InternalType0343(localValue5, localValue23, LanguageManager.internalMethod07214(localValue23.getName()), 100 + localValue19++));
               }
            }

            this.internalMethod09747();
         } else {
            for (ModuleEntry localValue20 : (Iterable<ModuleEntry>)(Iterable<?>)localValue4) {
               int localValue22 = internalMethod02191(localValue20.getName(), localValue2, localValue3);
               boolean localValue10 = localValue22 != Integer.MAX_VALUE;
               if (localValue10) {
                  this.internalField1145.add(new ModuleSettingsPanel.InternalType0343(localValue20, null, localValue20.getName(), localValue22));
               }

               int localValue11 = 0;

               for (Setting localValue13 : localValue20.getSettings()) {
                  if (localValue13.isVisible()) {
                     String localValue14 = LanguageManager.internalMethod07214(localValue13.getName());
                     int localValue15 = internalMethod02191(localValue14, localValue2, localValue3);
                     if (localValue15 != Integer.MAX_VALUE) {
                        this.internalField1145.add(new ModuleSettingsPanel.InternalType0343(localValue20, localValue13, localValue14, 1000 + localValue15));
                     } else if (localValue10) {
                        this.internalField1145.add(new ModuleSettingsPanel.InternalType0343(localValue20, localValue13, localValue14, 2000 + localValue22 + localValue11));
                     }

                     localValue11++;
                  }
               }
            }

            this.internalField1145
               .sort(
                  Comparator.comparingInt(ModuleSettingsPanel.InternalType0343::internalMethod05410)
                     .thenComparing(localValue0 -> localValue0.internalMethod06786().getName(), String.CASE_INSENSITIVE_ORDER)
                     .thenComparing(ModuleSettingsPanel.InternalType0343::internalMethod06821, String.CASE_INSENSITIVE_ORDER)
               );
            this.internalMethod09747();
         }
      }
   }

   private void internalMethod09747() {
      if (this.internalField0228 >= this.internalField1145.size()) {
         this.internalField0228 = Math.max(0, this.internalField1145.size() - 1);
      }

      ArrayList localValue1 = new ArrayList(this.internalField1145.size() + 2);
      localValue1.add(internalMethod04439());

      for (int localValue2 = 0; localValue2 < this.internalField1145.size(); localValue2++) {
         localValue1.add(this.internalMethod05570(this.internalField1145.get(localValue2), localValue2));
      }

      localValue1.add(internalMethod04439());
      this.internalField0634.internalMethod07849(localValue1);
   }

   private static UiElement internalMethod04439() {
      return new UiElement().fillWidth().height(6.0F).interactive(false);
   }

   private UiElement internalMethod05570(ModuleSettingsPanel.InternalType0343 localValue1, int localValue2) {
      return new UiElement()
         .fillWidth()
         .height(18.0F)
         .cursor(CursorType.internalField0567)
         .hoverMotion(Motion.internalMethod01870(70L))
         .bind("sel", () -> localValue2 == this.internalField0228, Motion.internalMethod01328(160L, Easing.internalField1828))
         .onClick(() -> this.internalMethod01763(localValue1, true))
         .paint((localValue3, localValue4) -> this.internalMethod06745(localValue3, localValue4, localValue1, localValue2));
   }

   private void internalMethod06745(UiRenderContext localValue1, UiElement localValue2, ModuleSettingsPanel.InternalType0343 localValue3, int localValue4) {
      if (localValue2.hover() > 0.5F) {
         this.internalField0228 = localValue4;
      }

      SizedFont localValue5 = Fonts.internalField1154.internalMethod01432(8.0F);
      SizedFont localValue6 = Fonts.internalField1154.internalMethod01432(7.0F);
      String localValue7 = "TAB - \u041e\u0442\u043a\u0440\u044b\u0442\u044c";
      float localValue8 = 5.0F;
      float localValue9 = localValue2.x() + localValue2.w() - 10.0F - localValue8;
      float localValue10 = localValue9 - 3.0F - localValue6.internalMethod00965(localValue7);
      float localValue11 = localValue2.y() + localValue2.h() / 2.0F - localValue5.internalMethod04890() / 2.0F;
      float localValue12 = localValue2.x() + 10.0F;
      float localValue13;
      if (localValue3.internalMethod05411()) {
         String localValue14 = localValue3.internalMethod06786().getName();
         localValue1.drawText(localValue5, localValue14, localValue12, localValue11, ThemeColors.internalField1613.mulAlpha(0.5F));
         float localValue15 = localValue12 + localValue5.internalMethod00965(localValue14) + 2.0F;
         localValue1.drawRoundedRect(
            localValue15, localValue2.y() + localValue2.h() / 2.0F - 1.0F, 2.0F, 2.0F, CornerRadii.internalMethod03908(1.0F), ThemeColors.internalField1613.mulAlpha(0.5F)
         );
         localValue13 = localValue15 + 2.0F + 4.0F;
      } else {
         localValue13 = localValue12;
      }

      String localValue21 = localValue3.internalMethod06821();
      float localValue22 = localValue2.sig("sel");
      float localValue16 = localValue2.x() + localValue2.w() - 10.0F - localValue13;
      float localValue17 = localValue10 - 6.0F - localValue13;
      float localValue18 = localValue16 + (localValue17 - localValue16) * localValue22;
      if (localValue5.internalMethod00965(localValue21) > localValue18) {
         localValue1.drawFadeText(localValue5, localValue21, localValue13, localValue11, ThemeColors.internalField1613, 0.0F, 8.0F, localValue18);
      } else {
         localValue1.drawText(localValue5, localValue21, localValue13, localValue11, ThemeColors.internalField1613);
      }

      if (localValue4 == this.internalField0228) {
         this.internalField0623.internalMethod03690(localValue2.y());
         float localValue19 = this.internalField0623.internalMethod02046();
         ColorRGBA localValue20 = ThemeColors.internalField1613.mulAlpha(0.5F);
         localValue1.drawText(localValue6, localValue7, localValue10, localValue19 + localValue2.h() / 2.0F - localValue6.internalMethod04890() / 2.0F, localValue20);
         localValue1.drawIcon("arrows", localValue9, localValue19 + localValue2.h() / 2.0F - localValue8 / 2.0F, localValue8, localValue20);
      }
   }

   private void internalMethod02318(int localValue1) {
      if (!this.internalField1145.isEmpty()) {
         this.internalField0228 = Math.max(0, Math.min(this.internalField1145.size() - 1, this.internalField0228 + localValue1));
      }
   }

   private void internalMethod01763(ModuleSettingsPanel.InternalType0343 localValue1, boolean localValue2) {
      if (localValue2) {
         if (this.internalField0048 != null) {
            this.internalField0048.accept(localValue1.internalMethod06786(), localValue1.internalMethod00131());
         }

         this.internalMethod08478(false);
      } else if (localValue1.internalMethod00131() == null) {
         localValue1.internalMethod06786().toggle();
      } else if (localValue1.internalMethod00131() instanceof BooleanSetting localValue3) {
         localValue3.toggle();
      }
   }

   private void internalMethod08488(boolean localValue1) {
      if (!this.internalField1145.isEmpty()) {
         this.internalMethod01763(this.internalField1145.get(this.internalField0228), localValue1);
      }
   }

   @Override
   public boolean keyPressed(int localValue1, int localValue2, int localValue3) {
      if (this.internalField1099) {
         if (super.keyPressed(localValue1, localValue2, localValue3)) {
            return true;
         } else {
            if (localValue1 == 256) {
               this.internalMethod02382(false);
            }

            return true;
         }
      } else if (!this.internalField1100) {
         if (localValue1 == 70 && (localValue3 & 2) != 0 && !KeybindButton.internalMethod06277()) {
            this.internalMethod08478(true);
            return true;
         } else if (this.internalField0277 && localValue1 == 256) {
            this.internalMethod02319(false);
            return true;
         } else {
            return false;
         }
      } else {
         switch (localValue1) {
            case 256:
               this.internalMethod08478(false);
               break;
            case 257:
            case 335:
               this.internalMethod08488(false);
               break;
            case 258:
               this.internalMethod08488(true);
               break;
            case 264:
               this.internalMethod02318(1);
               break;
            case 265:
               this.internalMethod02318(-1);
               break;
            default:
               this.internalMethod07310().internalMethod05727(localValue1, localValue2, localValue3);
         }

         return true;
      }
   }

   @Override
   public boolean charTyped(char localValue1, int localValue2) {
      if (!this.internalField1100) {
         return false;
      } else {
         this.internalMethod07310().internalMethod05413(localValue1, localValue2);
         return true;
      }
   }

   @Override
   public boolean mouseScrolled(float localValue1, float localValue2, float localValue3, float localValue4) {
      return super.mouseScrolled(localValue1, localValue2, localValue3, localValue4) || this.contains(localValue1, localValue2);
   }

   @Override
   public void mouseReleased(float localValue1, float localValue2, MouseButton localValue3) {
      super.mouseReleased(localValue1, localValue2, localValue3);
      if (this.internalField0936 != null) {
         this.internalField0936.internalMethod02863(localValue1, localValue2, localValue3);
      }
   }

   @Override
   protected void onTick(float localValue1, float localValue2, float localValue3) {
      super.onTick(localValue1, localValue2, localValue3);
      this.internalField0623.internalMethod08946(localValue1);
      float localValue4 = this.desiredW();
      float localValue5 = this.desiredH();
      this.internalField1102 = this.internalField1100 && System.currentTimeMillis() - this.internalField1060 > 140L;
      if ((this.internalField1100 || this.internalMethod09579()) && this.internalField1583 == null) {
         Window localValue9 = MinecraftClient.getInstance().getWindow();
         float localValue7 = this.internalField1145.isEmpty() ? 0.0F : Math.min(150.0F, this.internalField1145.size() * 18.0F + 12.0F);
         float localValue8 = localValue5 + (localValue7 > 0.0F ? 6.0F + localValue7 : 0.0F);
         this.internalMethod08296((localValue9.getScaledWidth() - localValue4) / 2.0F, (localValue9.getScaledHeight() - localValue8) / 2.0F);
      } else if (!this.internalField1099 && !this.internalMethod08159()) {
         this.internalMethod08296(
            this.internalField0633.x() + (this.internalField0633.w() - localValue4) / 2.0F, this.internalField0633.y() + this.internalField0633.h() + 10.0F
         );
      } else {
         Window localValue6 = MinecraftClient.getInstance().getWindow();
         this.internalMethod08296((localValue6.getScaledWidth() - localValue4) / 2.0F, (localValue6.getScaledHeight() - localValue5) / 2.0F);
      }

      if (this.internalField1583 != null) {
         this.internalField0634
            .snapAt(
               this.internalField0633.x() + (this.internalField0633.w() - 207.0F) / 2.0F,
               this.internalField0633.y() + (this.internalField0633.h() - this.internalField0634.h()) / 2.0F
            );
      } else {
         this.internalField0634.snapAt(this.x(), this.y() + this.h() + 6.0F);
      }

      if (this.internalField0277) {
         UiNode localValue10 = this.internalField1581 != null ? this.internalField1581 : this.internalField1253;
         if (this.internalField0276 && this.internalField1581 != null) {
            this.internalField0635.snapAt(localValue10.x() + localValue10.w() + 5.0F, localValue10.y() + localValue10.h() - this.internalField0635.h());
         } else {
            this.internalField0635.snapAt(localValue10.x(), localValue10.y() + localValue10.h() / 2.0F - this.internalField0635.h() / 2.0F);
         }
      }

      this.internalMethod10082();
      if (this.internalField1100) {
         if (!this.internalMethod07310().internalMethod00342()) {
            this.internalMethod07310().internalMethod07508(true);
         }

         String localValue11 = this.internalMethod07310().internalMethod06202();
         if (!localValue11.equals(this.internalField0248)) {
            this.internalField0248 = localValue11;
            this.internalMethod09746();
         }

         if (this.internalField0228 >= this.internalField1145.size()) {
            this.internalField0228 = Math.max(0, this.internalField1145.size() - 1);
         }
      }
   }

   private void internalMethod10082() {
      int localValue1 = this.internalField0668.internalMethod06723().indexOf(this.internalField0668.internalMethod07418());
      if (localValue1 != this.internalField0227 && localValue1 >= 0) {
         this.internalField0227 = localValue1;

         LanguageManager.internalMethod04491(switch (localValue1) {
            case 1 -> Language.internalField0165;
            case 2 -> Language.internalField1031;
            case 3 -> Language.internalField1032;
            default -> Language.internalField0164;
         });
         RockstarClient.getInstance().internalMethod03371().internalMethod05165("client");
      }

      if (this.internalField0650.internalMethod04496() != internalMethod03894().internalMethod03463()) {
         internalMethod03894().internalMethod00563(this.internalField0650.internalMethod04496());
         internalMethod09743();
      }

      ColorRGBA localValue2 = this.internalField0665.internalMethod05620() == null ? null : this.internalField0665.internalMethod05620().withAlpha(255.0F);
      if (localValue2 != null && !localValue2.equals(ThemeColors.internalMethod03506())) {
         ThemeColors.internalMethod01095(localValue2);
         this.internalField0230 = System.currentTimeMillis();
      }

      if (this.internalField0230 != 0L && System.currentTimeMillis() - this.internalField0230 > 600L) {
         this.internalField0230 = 0L;
         RockstarClient.getInstance().internalMethod03371().internalMethod05165("client");
      }
   }

   static final class InternalType0342 {
      private final List<String> internalField0416;
      private final IntSupplier internalField0657;
      private final IntConsumer internalField0540;

      InternalType0342(List<String> localValue1, IntSupplier localValue2, IntConsumer localValue3) {
         this.internalField0416 = localValue1;
         this.internalField0657 = localValue2;
         this.internalField0540 = localValue3;
      }

      @Override
      public final String toString() {
         return "InternalType0342[segments=" + this.internalField0416 + ", get=" + this.internalField0657 + ", set=" + this.internalField0540 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0416);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0657);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0540);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         ModuleSettingsPanel.InternalType0342 other = (ModuleSettingsPanel.InternalType0342) localValue1;
         return java.util.Objects.equals(this.internalField0416, other.internalField0416)
            && java.util.Objects.equals(this.internalField0657, other.internalField0657)
            && java.util.Objects.equals(this.internalField0540, other.internalField0540);
      }

      public List<String> internalMethod04552() {
         return this.internalField0416;
      }

      public IntSupplier internalMethod00967() {
         return this.internalField0657;
      }

      public IntConsumer internalMethod00180() {
         return this.internalField0540;
      }
   }

   static final class InternalType0343 {
      private final ModuleEntry internalField0403;
      private final Setting internalField0644;
      private final String internalField0248;
      private final int internalField0227;

      InternalType0343(ModuleEntry localValue1, Setting localValue2, String localValue3, int localValue4) {
         this.internalField0403 = localValue1;
         this.internalField0644 = localValue2;
         this.internalField0248 = localValue3;
         this.internalField0227 = localValue4;
      }

      boolean internalMethod05411() {
         return this.internalField0644 != null;
      }

      @Override
      public final String toString() {
         return "InternalType0343[module=" + this.internalField0403 + ", setting=" + this.internalField0644 + ", display=" + this.internalField0248 + ", score=" + this.internalField0227 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0403);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0644);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0248);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         ModuleSettingsPanel.InternalType0343 other = (ModuleSettingsPanel.InternalType0343) localValue1;
         return java.util.Objects.equals(this.internalField0403, other.internalField0403)
            && java.util.Objects.equals(this.internalField0644, other.internalField0644)
            && java.util.Objects.equals(this.internalField0248, other.internalField0248)
            && java.util.Objects.equals(this.internalField0227, other.internalField0227);
      }

      public ModuleEntry internalMethod06786() {
         return this.internalField0403;
      }

      public Setting internalMethod00131() {
         return this.internalField0644;
      }

      public String internalMethod06821() {
         return this.internalField0248;
      }

      public int internalMethod05410() {
         return this.internalField0227;
      }
   }
}
