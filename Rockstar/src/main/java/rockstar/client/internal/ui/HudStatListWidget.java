package rockstar.client.internal.ui;







import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.render.*;
import rockstar.client.internal.framework.*;
import rockstar.client.*;
import java.util.ArrayList;
import java.util.List;

public class HudStatListWidget extends AbstractHudWidget {
   public final MultiSelectSetting internalField0675 = new MultiSelectSetting(this, "elements").internalMethod05559().internalMethod03035(1);

   public HudStatListWidget(String localValue1, String localValue2) {
      super(localValue1, localValue2);
      this.height = 18.0F;
   }

   @Override
   public UiContainer build() {
      ArrayList localValue1 = new ArrayList();

      for (MultiSelectSetting.InternalType0091 localValue3 : this.internalField0675.internalMethod01792()) {
         localValue1.add((AnimatedStatusEntry)localValue3);
      }

      UiContainer localValue6 = new UiContainer().internalMethod05895().internalMethod01855(TextAlignment.internalField0621).internalMethod03062(0.0F);

      for (int localValue7 = 0; localValue7 < localValue1.size(); localValue7++) {
         AnimatedStatusEntry localValue4 = (AnimatedStatusEntry)localValue1.get(localValue7);
         int localValue5 = localValue7;
         localValue6.internalMethod03907(
            new UiElement()
               .size(10.0F, 2.0F)
               .interactive(false)
               .visibleWhen(() -> localValue4.isSelected() && internalMethod00318(localValue1, localValue5))
               .paint(
                  (localValue0, localValue1x) -> localValue0.drawRoundedRect(
                     localValue1x.x() + localValue1x.w() / 2.0F - 1.0F,
                     localValue1x.y() + localValue1x.h() / 2.0F - 1.0F,
                     2.0F,
                     2.0F,
                     CornerRadii.internalMethod03908(1.0F),
                     ThemeColors.internalField1310.mulAlpha(0.5F)
                  )
               )
         );
         localValue6.internalMethod03907(this.internalMethod07110(localValue4).visibleWhen(localValue4::isSelected));
      }

      return new UiContainer()
         .internalMethod05895()
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod09266(15.0F)
         .internalMethod03062(3.0F)
         .internalMethod03514(Insets.internalMethod00105(0.0F, 5.0F, 0.0F, 4.0F))
         .internalMethod07178(
            (localValue1x, localValue2) -> {
               localValue1x.drawClientRect(localValue2.x(), localValue2.y(), localValue2.w(), localValue2.h(), this.animation.internalMethod02881(), this.dragAnim.internalMethod02881(), 3.0F, 5.0F);
               localValue1x.drawSquircle(
                  localValue2.x(),
                  localValue2.y(),
                  23.0F,
                  localValue2.h(),
                  3.0F,
                  CornerRadii.internalMethod03908(5.0F),
                  new VerticalColorGradient(ThemeColors.internalField1610.mulAlpha(0.1F), ThemeColors.internalField1610.mulAlpha(0.0F))
               );
            }
         )
         .internalMethod03907(
            new UiElement().size(7.0F, 7.0F).interactive(false).icon(this.icon, 7.0F, localValue0 -> ThemeColors.internalField1310.withAlpha(255.0F))
         )
         .internalMethod03907(localValue6);
   }

   private UiNode internalMethod07110(final AnimatedStatusEntry localValue1) {
      final SizedFont localValue2 = Fonts.internalField0449.internalMethod01432(7.0F);
      final SizedFont localValue3 = Fonts.internalField0449.internalMethod01432(7.0F);
      return (new UiNode() {
            @Override
            protected void measure() {
               float localValue1x = localValue1.internalMethod03430().isEmpty() ? 0.0F : 8.0F * this.hover();
               float localValue2x = localValue1.internalMethod07883().isEmpty() ? 0.0F : localValue3.internalMethod00965(localValue1.internalMethod07883());
               this.prefW = localValue1x + localValue2.internalMethod00965(localValue1.internalMethod06899()) + localValue2x;
               this.prefH = localValue2.internalMethod04890();
            }

            @Override
            protected void onTick(float localValue1x, float localValue2x, float localValue3x) {
               if (!this.hovered() || localValue1.internalMethod06474().internalMethod02365(1000L)) {
                  localValue1.internalMethod06767(false);
               }

               localValue1.internalMethod02715().internalMethod07062(localValue1.internalMethod05290());
            }

            @Override
            protected void drawSelf(UiRenderContext localValue1x, float localValue2x) {
               float localValue3x = localValue1.internalMethod03430().isEmpty() ? 0.0F : this.hover();
               float localValue4 = 8.0F * localValue3x;
               float localValue5 = localValue1.internalMethod02715().internalMethod02881();
               float localValue6 = this.x();
               float localValue7 = this.y() + this.h() / 2.0F - 3.0F;
               float localValue8 = localValue3x * (1.0F - localValue5);
               if (localValue8 > 0.001F) {
                  HudRenderUtils.internalMethod02865(localValue1x.getMatrices(), localValue6 + 3.0F, localValue7 + 3.0F, 90.0F * localValue5);
                  localValue1x.drawIcon("copy", localValue6, localValue7, 6.0F, ThemeColors.internalMethod08459().mulAlpha(localValue8));
                  HudRenderUtils.internalMethod00012(localValue1x.getMatrices());
               }

               float localValue9 = localValue3x * localValue5;
               if (localValue9 > 0.001F) {
                  HudRenderUtils.internalMethod02865(localValue1x.getMatrices(), localValue6 + 3.0F, localValue7 + 3.0F, -90.0F + 90.0F * localValue5);
                  localValue1x.drawIcon("check", localValue6, localValue7, 6.0F, ThemeColors.internalField0776.mulAlpha(localValue9));
                  HudRenderUtils.internalMethod00012(localValue1x.getMatrices());
               }

               float localValue10 = this.x() + localValue4;
               float localValue11 = this.y() + this.h() / 2.0F - localValue2.internalMethod04890() / 2.0F;
               localValue1x.drawText(localValue2, localValue1.internalMethod06899(), localValue10, localValue11, ThemeColors.internalMethod08459());
               if (!localValue1.internalMethod07883().isEmpty()) {
                  localValue1x.drawText(
                     localValue3,
                     localValue1.internalMethod07883(),
                     localValue10 + localValue2.internalMethod00965(localValue1.internalMethod06899()),
                     localValue11,
                     ThemeColors.internalMethod08459().mulAlpha(0.5F)
                  );
               }
            }

            @Override
            public boolean mouseClicked(float localValue1x, float localValue2x, MouseButton localValue3x) {
               if (!this.interactive || !this.inFlow() || !this.contains(localValue1x, localValue2x)) {
                  return false;
               } else if (localValue3x == MouseButton.internalField0102 && !localValue1.internalMethod03430().isEmpty()) {
                  TextUtils.internalMethod05864(localValue1.internalMethod03430());
                  localValue1.internalMethod06474().internalMethod00701();
                  localValue1.internalMethod06767(true);
                  return true;
               } else {
                  return false;
               }
            }
         })
         .snapSize();
   }

   private static boolean internalMethod00318(List<AnimatedStatusEntry> localValue0, int localValue1) {
      for (int localValue2 = 0; localValue2 < localValue1; localValue2++) {
         if (((AnimatedStatusEntry)localValue0.get(localValue2)).isSelected()) {
            return true;
         }
      }

      return false;
   }

   @Override
   public void onMouseClicked(double localValue1, double localValue3, MouseButton localValue5) {
      if (this.flow == null || !this.isShowing() || localValue5 != MouseButton.internalField0102 || !this.flow.mouseClicked((float)localValue1, (float)localValue3, localValue5)) {
         super.onMouseClicked(localValue1, localValue3, localValue5);
      }
   }

   @Override
   public void onMouseReleased(double localValue1, double localValue3, MouseButton localValue5) {
      if (this.flow != null) {
         this.flow.mouseReleased((float)localValue1, (float)localValue3, localValue5);
      }

      super.onMouseReleased(localValue1, localValue3, localValue5);
   }
}
