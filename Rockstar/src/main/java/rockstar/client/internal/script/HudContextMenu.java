package rockstar.client.internal.script;











import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.render.*;
import rockstar.client.i18n.*;
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
import globals.client.ui.RocknetMenu;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import lombok.Generated;
import pyrock.utility.render.ColorRGBA;

public class HudContextMenu extends LegacyUiElement {
   private final AnimatedValue internalField0808 = new AnimatedValue(300L, 0.0F, Easing.internalField1327);
   private boolean internalField0276;
   private final float internalField1049;
   private Runnable internalField0659 = () -> {};
   private boolean internalField1099;
   private boolean internalField1100 = true;
   private boolean internalField1102;
   private boolean internalField1101;
   private float internalField1046 = -1.0F;
   private long internalField0229;
   private final Set<SettingOwner> internalField0546 = new LinkedHashSet<>();
   private final UiContainer internalField0634 = new UiContainer()
      .internalMethod01863()
      .internalMethod03514(Insets.internalMethod05266(4.0F, 0.0F))
      .internalMethod08755()
      .internalMethod05391(
         localValue0 -> localValue0.internalMethod02712(2.0F)
            .internalMethod02066(1.0F, 4.0F)
            .internalMethod00894(2.5F)
            .internalMethod04404(
               localValue0x -> ColorRGBA.BLACK
                  .mix(ColorRGBA.WHITE, 0.3F)
                  .withAlpha(255.0F * (0.32F + 0.28F * localValue0x.internalMethod05170() + 0.3F * localValue0x.internalMethod05173()))
            )
      );
   public static final List<UiNode> internalField0416 = new LinkedList<>();
   public static boolean internalField0277;

   public static <T extends UiNode> T internalMethod03683(T localValue0) {
      internalField0416.add(localValue0);
      return (T)localValue0;
   }

   public HudContextMenu(float localValue1, float localValue2) {
      this(localValue1, localValue2, 90.0F);
   }

   public HudContextMenu(float localValue1, float localValue2, float localValue3) {
      this(localValue1, localValue2, localValue3, 2.0F);
   }

   public HudContextMenu(float localValue1, float localValue2, float localValue3, float localValue4) {
      this.internalField0205 = localValue1;
      this.internalField0206 = localValue2;
      this.internalField1048 = localValue3;
      this.internalField1049 = localValue4;
      this.internalField0276 = true;
   }

   @Override
   public void internalMethod05619(UiRenderContext localValue1) {
      this.internalField0808.internalMethod06645(this.internalField0276 ? Easing.internalField0812 : Easing.internalField1328);
      this.internalField0808.internalMethod07062(this.internalField0276);
      long localValue2 = System.currentTimeMillis();
      float localValue4 = this.internalField0229 == 0L ? 16.0F : Math.min(64.0F, (float)(localValue2 - this.internalField0229));
      this.internalField0229 = localValue2;
      this.internalField0634.internalMethod09339(this.internalField1048);
      if (this.internalField1102) {
         this.internalField0634.internalMethod08392(this.internalField1048, this.internalField1047);
      }

      this.internalField0634.prepareRoot();
      if (this.internalField1046 >= 0.0F) {
         float localValue5 = Math.max(this.internalField1046, internalField0149.getWindow().getScaledWidth() - this.internalField1046 - this.internalField1048);
         float localValue6 = Math.max(this.internalField1046, internalField0149.getWindow().getScaledHeight() - this.internalField1046 - this.internalField0634.h());
         this.internalField0205 = Math.clamp(this.internalField0205, this.internalField1046, localValue5);
         this.internalField0206 = Math.clamp(this.internalField0206, this.internalField1046, localValue6);
      }

      this.internalField0634.snapAt(this.internalField0205, this.internalField0206);
      if (this.internalField0808.internalMethod02881() < 0.999F) {
         this.internalField0634.snapSubtree();
      }

      this.internalField0634.tick(localValue4, localValue1.internalMethod05259(), localValue1.internalMethod05261());
      this.internalField1047 = this.internalField0634.h();
      boolean localValue9 = true;

      for (UiNode localValue7 : this.internalField0634.internalMethod01401()) {
         if (localValue7.inFlow()) {
            localValue9 = false;
            break;
         }
      }

      float localValue11 = Math.min(1.0F, this.internalField0808.internalMethod02881());
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, localValue11);
      HudRenderUtils.internalMethod08976(
         localValue1.getMatrices(),
         this.internalField0205 + this.internalField1048 / this.internalField1049,
         this.internalField0206 + this.internalField1047 / this.internalField1049,
         0.5F + this.internalField0808.internalMethod02881() * 0.5F
      );
      if (this.internalField1100 && !localValue9) {
         if (this.internalField1101) {
            localValue1.drawShadow(
               this.internalField0205,
               this.internalField0206,
               this.internalField1048,
               this.internalField1047,
               25.0F,
               CornerRadii.internalMethod03908(11.0F),
               ThemeColors.internalField1309.mulAlpha(0.5F)
            );
            localValue1.drawBlurredRect(
               this.internalField0205,
               this.internalField0206,
               this.internalField1048,
               this.internalField1047,
               5.0F,
               3.0F,
               CornerRadii.internalMethod03908(11.0F),
               ThemeColors.internalField1312
            );
            localValue1.drawSquircle(
               this.internalField0205,
               this.internalField0206,
               this.internalField1048,
               this.internalField1047,
               3.0F,
               CornerRadii.internalMethod03908(11.0F),
               ThemeColors.internalField1612
            );
            localValue1.drawSquircleBorder(
               this.internalField0205,
               this.internalField0206,
               this.internalField1048,
               this.internalField1047,
               0.5F,
               3.0F,
               CornerRadii.internalMethod03908(11.0F),
               ThemeColors.internalField1616
            );
         } else {
            localValue1.drawShadow(
               this.internalField0205,
               this.internalField0206,
               this.internalField1048,
               this.internalField1047,
               15.0F,
               CornerRadii.internalMethod03908(6.0F),
               ColorRGBA.BLACK.withAlpha(127.5F)
            );
            if (internalField0149.currentScreen instanceof RocknetMenu) {
               localValue1.drawSquircle(
                  this.internalField0205,
                  this.internalField0206,
                  this.internalField1048,
                  this.internalField1047,
                  7.0F,
                  CornerRadii.internalMethod03908(6.0F),
                  ThemeColors.internalMethod07738()
               );
            } else {
               if (InterfaceModule.internalMethod09917()) {
                  localValue1.drawBlurredRect(
                     this.internalField0205,
                     this.internalField0206,
                     this.internalField1048,
                     this.internalField1047,
                     45.0F,
                     7.0F,
                     CornerRadii.internalMethod03908(6.0F),
                     ColorRGBA.WHITE.withAlpha(255.0F * this.internalField0808.internalMethod02881() * InterfaceModule.internalMethod07585())
                  );
               }

               if (InterfaceModule.internalMethod09719()) {
                  localValue1.drawLiquidGlass(
                     this.internalField0205,
                     this.internalField0206,
                     this.internalField1048,
                     this.internalField1047,
                     7.0F,
                     0.08F,
                     CornerRadii.internalMethod03908(6.0F),
                     ColorRGBA.WHITE.withAlpha(255.0F * this.internalField0808.internalMethod02881() * InterfaceModule.internalMethod07584())
                  );
               }

               localValue1.drawSquircle(
                  this.internalField0205,
                  this.internalField0206,
                  this.internalField1048,
                  this.internalField1047,
                  7.0F,
                  CornerRadii.internalMethod03908(6.0F),
                  ThemeColors.internalMethod07738()
                     .withAlpha(
                        255.0F
                           * MathUtils.internalMethod02587(
                              ThemeColors.internalMethod02435().internalMethod08704(),
                              ThemeColors.internalMethod02435().internalMethod08705(),
                              InterfaceModule.internalMethod07584()
                           )
                     )
               );
            }
         }
      }

      if (!localValue9) {
         this.internalField0634.draw(localValue1, localValue11);
      }

      HudRenderUtils.internalMethod00012(localValue1.getMatrices());
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      if (!internalField0277) {
         internalField0416.removeIf(localValue0 -> !localValue0.alive());

         for (UiNode localValue8 : internalField0416) {
            localValue8.prepareRoot();
            localValue8.tick(localValue4, localValue1.internalMethod05259(), localValue1.internalMethod05261());
         }

         for (UiNode localValue14 : internalField0416) {
            localValue14.draw(localValue1, 1.0F);
         }

         internalField0277 = true;
      }
   }

   private HudContextMenu internalMethod03617(UiNode localValue1) {
      this.internalField0634.internalMethod03907(localValue1);
      return this;
   }

   public HudContextMenu internalMethod05995(Setting localValue1) {
      if (localValue1 instanceof AbstractSetting localValue2) {
         this.internalField0546.add(localValue2.internalMethod01453());
      }

      return this.internalMethod03617(SettingComponentFactory.internalMethod03724(localValue1));
   }

   public boolean internalMethod02626() {
      return SettingHistoryManager.internalMethod03741(this.internalField0546);
   }

   public boolean internalMethod02629() {
      return SettingHistoryManager.internalMethod05652(this.internalField0546);
   }

   public HudContextMenu internalMethod03869(final LegacyUiElement localValue1) {
      return this.internalMethod03617((new UiContainer() {
         @Override
         public float desiredH() {
            return localValue1.internalMethod07809();
         }

         @Override
         protected void onTick(float localValue1x, float localValue2, float localValue3) {
         }

         @Override
         protected void drawSelf(UiRenderContext localValue1x, float localValue2) {
            localValue1.internalMethod05191(this.x(), this.y(), this.w(), localValue1.internalMethod07809());
            if (localValue1 instanceof SettingComponent localValue3) {
               localValue3.internalMethod07890(this.y());
               localValue3.internalMethod07901(localValue1.internalMethod07809());
            }

            localValue1.internalMethod03398(localValue1x);
            if (localValue1 instanceof SettingComponent localValue4) {
               localValue4.internalMethod08256(localValue1x);
            }
         }

         @Override
         public boolean mouseClicked(float localValue1x, float localValue2, MouseButton localValue3) {
            if (!this.contains(localValue1x, localValue2)) {
               return false;
            } else {
               localValue1.internalMethod01643(localValue1x, localValue2, localValue3);
               return true;
            }
         }

         @Override
         public void mouseReleased(float localValue1x, float localValue2, MouseButton localValue3) {
            localValue1.internalMethod02863(localValue1x, localValue2, localValue3);
            super.mouseReleased(localValue1x, localValue2, localValue3);
         }

         @Override
         public boolean mouseScrolled(float localValue1x, float localValue2, float localValue3, float localValue4) {
            if (!this.contains(localValue1x, localValue2)) {
               return false;
            } else {
               localValue1.internalMethod02890(localValue1x, localValue2, localValue3, localValue4);
               return false;
            }
         }

         @Override
         public boolean keyPressed(int localValue1x, int localValue2, int localValue3) {
            localValue1.internalMethod05727(localValue1x, localValue2, localValue3);
            return false;
         }

         @Override
         public boolean charTyped(char localValue1x, int localValue2) {
            return localValue1.internalMethod05413(localValue1x, localValue2);
         }
      }).internalMethod09609());
   }

   public HudContextMenu internalMethod02825(String localValue1) {
      return this.internalMethod03617(
         new UiContainer()
            .internalMethod09266(14.0F)
            .internalMethod09609()
            .internalMethod01192(FlexDirection.internalField1246)
            .internalMethod01855(TextAlignment.internalField0621)
            .internalMethod03514(Insets.internalMethod05266(0.0F, 9.0F))
            .internalMethod03907(
               new TextLabel(Fonts.internalField1154.internalMethod01432(8.0F), () -> LanguageManager.internalMethod07214(localValue1))
                  .internalMethod02959(localValue0 -> ThemeColors.internalField1613.mulAlpha(0.75F))
                  .internalMethod05903(0.75F)
                  .fill()
            )
      );
   }

   public HudContextMenu internalMethod04738(String localValue1) {
      return this.internalMethod05789(localValue1, 7, false);
   }

   public HudContextMenu internalMethod05789(String localValue1, int localValue2, boolean localValue3) {
      UiContainer localValue4 = new UiContainer()
         .internalMethod09266(16.0F)
         .internalMethod09609()
         .internalMethod01192(FlexDirection.internalField1246)
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod03514(Insets.internalMethod05266(0.0F, 9.0F))
         .internalMethod03907(
            new TextLabel(Fonts.internalField1157.internalMethod01432(localValue2), () -> LanguageManager.internalMethod07214(localValue1))
               .internalMethod02959(localValue0 -> ThemeColors.internalField1613)
               .internalMethod05903(0.75F)
               .fill()
         );
      if (localValue3) {
         localValue4.internalMethod09932();
      }

      return this.internalMethod03617(localValue4);
   }

   public HudContextMenu internalMethod03873() {
      return this.internalMethod03617(
         new UiElement()
            .fillWidth()
            .height(4.0F)
            .paint(
               (localValue0, localValue1) -> localValue0.drawRect(localValue1.x() + 6.0F, localValue1.y() + localValue1.h() / 2.0F - 0.25F, localValue1.w() - 12.0F, 0.5F, ThemeColors.internalField1616)
            )
      );
   }

   public HudContextMenu internalMethod06727(String localValue1, boolean localValue2) {
      return this.internalMethod05004(localValue1, localValue2, null);
   }

   public HudContextMenu internalMethod05004(String localValue1, boolean localValue2, HudBooleanAction localValue3) {
      boolean[] localValue4 = new boolean[]{localValue2};
      return this.internalMethod03617(
         new UiContainer()
            .internalMethod09266(17.0F)
            .internalMethod09609()
            .internalMethod03062(5.0F)
            .internalMethod01192(FlexDirection.internalField1246)
            .internalMethod07607(LayoutAlignment.internalField1377)
            .internalMethod01855(TextAlignment.internalField0621)
            .internalMethod03514(Insets.internalMethod05266(0.0F, 9.0F))
            .internalMethod03907(
               new TextLabel(Fonts.internalField1154.internalMethod01432(8.0F), () -> localValue1)
                  .internalMethod02959(localValue0 -> ThemeColors.internalField1613.mulAlpha(0.75F + 0.25F * localValue0.hover()))
                  .internalMethod05903(0.75F)
                  .fill()
            )
            .internalMethod03907(new ToggleSwitchWidget(() -> localValue4[0]).internalMethod05792(() -> ThemeColors.internalField1614).size(13.0F, 8.0F))
            .internalMethod05690(() -> {
               localValue4[0] = !localValue4[0];
               if (localValue3 != null) {
                  localValue3.handleAction(localValue4[0]);
               }
            })
            .internalMethod04332(CursorType.internalField0567)
      );
   }

   public HudContextMenu internalMethod03323(String localValue1, String localValue2, HudMenuAction localValue3) {
      boolean localValue4 = localValue1.equals(LanguageManager.internalMethod07214("remove"));
      ColorRGBA localValue5 = localValue4 ? ColorRGBA.RED.mix(ColorRGBA.WHITE, 0.3F) : ThemeColors.internalField1613;
      return this.internalMethod03617(
         new UiContainer()
            .internalMethod09266(17.0F)
            .internalMethod09609()
            .internalMethod03062(5.0F)
            .internalMethod01192(FlexDirection.internalField1246)
            .internalMethod07607(LayoutAlignment.internalField1377)
            .internalMethod01855(TextAlignment.internalField0621)
            .internalMethod03514(Insets.internalMethod05266(0.0F, 9.0F))
            .internalMethod03907(
               new TextLabel(Fonts.internalField1154.internalMethod01432(8.0F), () -> localValue1)
                  .internalMethod02959(localValue1x -> localValue5.mulAlpha(0.75F + 0.25F * localValue1x.hover()))
                  .internalMethod05903(0.75F)
                  .fill()
            )
            .internalMethod03907(
               new UiElement()
                  .size(8.0F, 8.0F)
                  .interactive(false)
                  .paint((localValue2x, localValue3x) -> localValue2x.drawIcon(localValue2, localValue3x.x(), localValue3x.y(), localValue3x.w(), localValue5.mulAlpha(0.75F + 0.25F * localValue3x.hover())))
            )
            .internalMethod05690(() -> localValue3.run(this))
            .internalMethod04332(CursorType.internalField0567)
      );
   }

   public HudContextMenu internalMethod02093(String localValue1, int localValue2) {
      return this.internalMethod06512(localValue1, localValue2, null);
   }

   public HudContextMenu internalMethod06512(String localValue1, int localValue2, HudIntAction localValue3) {
      int[] localValue4 = new int[]{localValue2};
      KeybindButton localValue5 = new KeybindButton(Fonts.internalField1154.internalMethod01432(7.0F), () -> localValue4[0], localValue2x -> {
         localValue4[0] = localValue2x;
         if (localValue3 != null) {
            localValue3.handleAction(localValue2x);
         }
      });
      return this.internalMethod03617(
         new UiContainer()
            .internalMethod09266(17.0F)
            .internalMethod09609()
            .internalMethod03062(5.0F)
            .internalMethod01192(FlexDirection.internalField1246)
            .internalMethod07607(LayoutAlignment.internalField1377)
            .internalMethod01855(TextAlignment.internalField0621)
            .internalMethod03514(Insets.internalMethod05266(0.0F, 9.0F))
            .internalMethod03907(
               new TextLabel(Fonts.internalField1154.internalMethod01432(8.0F), () -> localValue1)
                  .internalMethod02959(localValue0 -> ThemeColors.internalField1613.mulAlpha(0.75F + 0.25F * localValue0.hover()))
                  .internalMethod05903(0.75F)
                  .fill()
            )
            .internalMethod03907(localValue5)
            .internalMethod02525(localValue5::internalMethod00007)
            .internalMethod04332(CursorType.internalField0567)
      );
   }

   public HudContextMenu internalMethod06866(KeybindSetting localValue1) {
      return this.internalMethod06512(LanguageManager.internalMethod07214(localValue1.getName()), localValue1.internalMethod07477(), localValue1::internalMethod02164);
   }

   public HudContextMenu internalMethod05831(String localValue1, float localValue2, float localValue3, float localValue4, float localValue5) {
      return this.internalMethod01406(localValue1, localValue2, localValue3, localValue4, localValue5, null);
   }

   public HudContextMenu internalMethod01406(String localValue1, float localValue2, float localValue3, float localValue4, float localValue5, HudFloatAction localValue6) {
      float[] localValue7 = new float[]{localValue4};
      UiContainer localValue8 = new UiContainer()
         .internalMethod03062(6.0F)
         .internalMethod01192(FlexDirection.internalField1246)
         .internalMethod07607(LayoutAlignment.internalField1377)
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod09609()
         .internalMethod03907(
            new TextLabel(Fonts.internalField1154.internalMethod01432(8.0F), () -> localValue1)
               .internalMethod02959(localValue0 -> ThemeColors.internalField1613.mulAlpha(0.75F + 0.25F * localValue0.hover()))
               .internalMethod05903(0.75F)
               .fill()
         )
         .internalMethod03907(new NumberInputField(Fonts.internalField1154.internalMethod01432(7.0F), () -> localValue7[0], localValue2x -> {
            localValue7[0] = localValue2x;
            if (localValue6 != null) {
               localValue6.internalMethod05176(localValue2x);
            }
         }, localValue2, localValue3).internalMethod05772(() -> TextUtils.internalMethod00670(localValue7[0])).internalMethod01821(localValue0 -> ThemeColors.internalField1310));
      return this.internalMethod03617(
         new UiContainer()
            .internalMethod01192(FlexDirection.internalField0629)
            .internalMethod09609()
            .internalMethod03062(5.0F)
            .internalMethod03514(Insets.internalMethod05266(0.0F, 9.0F))
            .internalMethod03907(localValue8)
            .internalMethod03907(
               new SingleValueSlider(() -> localValue7[0], localValue2x -> {
                     localValue7[0] = localValue2x;
                     if (localValue6 != null) {
                        localValue6.internalMethod05176(localValue2x);
                     }
                  }, localValue2, localValue3)
                  .internalMethod07013(localValue5)
                  .internalMethod06319()
                  .internalMethod08206(6.0F)
                  .internalMethod00229(3.0F)
                  .internalMethod08796(3.0F)
                  .internalMethod09045(1.5F)
                  .internalMethod00551(localValue0 -> ThemeColors.internalField1614)
                  .internalMethod01909(localValue0 -> ThemeColors.internalField1310.mulAlpha(1.0F - 0.25F * localValue0.hover()))
                  .internalMethod07361(Motion.internalMethod01328(300L, Easing.internalField1325))
            )
      );
   }

   public HudContextMenu internalMethod04092(Runnable localValue1) {
      this.internalField0659 = localValue1;
      return this;
   }

   public HudContextMenu internalMethod06528(boolean localValue1) {
      this.internalField1100 = localValue1;
      return this;
   }

   public HudContextMenu internalMethod04574() {
      this.internalField1101 = true;
      return this;
   }

   public HudContextMenu internalMethod04739(float localValue1) {
      this.internalField1046 = Math.max(0.0F, localValue1);
      return this;
   }

   public HudContextMenu internalMethod01608(float localValue1) {
      this.internalField1047 = localValue1;
      this.internalField1102 = true;
      return this;
   }

   public void internalMethod05781(boolean localValue1) {
      this.internalField0276 = localValue1;
      if (!localValue1 && !this.internalField1099) {
         this.internalField0659.run();
         this.internalField1099 = true;
      }
   }

   @Override
   public void internalMethod01643(double localValue1, double localValue3, MouseButton localValue5) {
      for (int localValue6 = internalField0416.size() - 1; localValue6 >= 0; localValue6--) {
         if (internalField0416.get(localValue6).mouseClicked((float)localValue1, (float)localValue3, localValue5)) {
            return;
         }
      }

      this.internalField0634.mouseClicked((float)localValue1, (float)localValue3, localValue5);
      super.internalMethod01643(localValue1, localValue3, localValue5);
   }

   @Override
   public void internalMethod02863(double localValue1, double localValue3, MouseButton localValue5) {
      for (UiNode localValue7 : internalField0416) {
         localValue7.mouseReleased((float)localValue1, (float)localValue3, localValue5);
      }

      this.internalField0634.mouseReleased((float)localValue1, (float)localValue3, localValue5);
      super.internalMethod02863(localValue1, localValue3, localValue5);
   }

   @Override
   public void internalMethod02890(double localValue1, double localValue3, double localValue5, double localValue7) {
      for (int localValue9 = internalField0416.size() - 1; localValue9 >= 0; localValue9--) {
         if (internalField0416.get(localValue9).mouseScrolled((float)localValue1, (float)localValue3, (float)localValue5, (float)localValue7)) {
            return;
         }
      }

      this.internalField0634.mouseScrolled((float)localValue1, (float)localValue3, (float)localValue5, (float)localValue7);
      super.internalMethod02890(localValue1, localValue3, localValue5, localValue7);
   }

   @Override
   public void internalMethod05727(int localValue1, int localValue2, int localValue3) {
      if (!this.internalMethod00980(localValue1, localValue2, localValue3)) {
         super.internalMethod05727(localValue1, localValue2, localValue3);
      }
   }

   public boolean internalMethod00980(int localValue1, int localValue2, int localValue3) {
      for (int localValue4 = internalField0416.size() - 1; localValue4 >= 0; localValue4--) {
         if (internalField0416.get(localValue4).keyPressed(localValue1, localValue2, localValue3)) {
            return true;
         }
      }

      return this.internalField0634.keyPressed(localValue1, localValue2, localValue3);
   }

   @Override
   public boolean internalMethod05413(char localValue1, int localValue2) {
      for (int localValue3 = internalField0416.size() - 1; localValue3 >= 0; localValue3--) {
         if (internalField0416.get(localValue3).charTyped(localValue1, localValue2)) {
            return true;
         }
      }

      return this.internalField0634.charTyped(localValue1, localValue2) ? true : super.internalMethod05413(localValue1, localValue2);
   }

   @Generated
   public AnimatedValue internalMethod06960() {
      return this.internalField0808;
   }

   @Generated
   public boolean internalMethod08805() {
      return this.internalField0276;
   }
}
