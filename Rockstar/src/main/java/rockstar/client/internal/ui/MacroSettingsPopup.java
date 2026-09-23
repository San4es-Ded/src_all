package rockstar.client.internal.ui;








import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.i18n.*;
import rockstar.client.animation.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.*;
import rockstar.client.compat.RenderSystem;

public class MacroSettingsPopup implements ScreenMetricsAccess {
   public MacroSettingsPopup.InternalType0094 internalMethod05692(
      UiRenderContext localValue1,
      float localValue2,
      float localValue3,
      float localValue4,
      float localValue5,
      float localValue6,
      float localValue7,
      float localValue8,
      AnimatedValue localValue9,
      AnimatedValue localValue10,
      AnimatedValue localValue11,
      MacroConfigPanel localValue12,
      AssistSwapItem localValue13,
      float localValue14
   ) {
      float localValue16 = localValue12.internalMethod02556();
      float localValue17 = localValue7 + localValue8 * 2.0F + localValue16 - 2.0F;
      float localValue18 = localValue2 + localValue4 + localValue5;
      boolean localValue19 = localValue18 + localValue6 <= internalField0389.internalMethod03585() - 6.0F;
      float localValue20 = localValue19 ? localValue18 : localValue2 - localValue6 - localValue5;
      float localValue21 = Math.max(6.0F, Math.min(localValue3, internalField0389.internalMethod03589() - localValue17 - 6.0F));
      localValue12.internalMethod01250().internalMethod05191(localValue20, localValue21, localValue6, localValue17);
      float localValue22 = localValue9.internalMethod02881() * localValue14 * localValue10.internalMethod02881();
      float localValue23;
      float localValue24;
      if (localValue19) {
         localValue23 = localValue2 + localValue4;
         localValue24 = internalField0389.internalMethod03585() - localValue23;
      } else {
         localValue23 = 0.0F;
         localValue24 = localValue2;
      }

      ScissorStack.internalMethod06303(localValue1.getMatrices(), localValue23, 0.0F, localValue24, internalField0389.internalMethod03589());
      localValue1.drawShadow(localValue20, localValue21, localValue6, localValue17, 25.0F, CornerRadii.internalMethod03908(11.0F), ThemeColors.internalField1309.mulAlpha(0.5F * localValue22));
      localValue1.drawBlurredRect(localValue20, localValue21, localValue6, localValue17, 5.0F, 3.0F, CornerRadii.internalMethod03908(11.0F), ThemeColors.internalField1312.mulAlpha(localValue22));
      localValue1.drawSquircle(localValue20, localValue21, localValue6, localValue17, 3.0F, CornerRadii.internalMethod03908(11.0F), ThemeColors.internalField1612.mulAlpha(localValue22));
      localValue1.drawSquircleBorder(
         localValue20, localValue21, localValue6, localValue17, 0.5F, 3.0F, CornerRadii.internalMethod03908(11.0F), ThemeColors.internalField1616.mulAlpha(localValue22)
      );
      MacroSettingsPopup.InternalType0093 localValue25 = this.internalMethod03439(localValue1, localValue12, localValue13, localValue22, localValue20, localValue21, localValue6, localValue7, localValue8, localValue11);
      float localValue26 = localValue20;
      float localValue27 = localValue21 + localValue7 + localValue8 + 1.0F;
      float localValue28 = localValue6;
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, localValue22);

      for (SettingComponent localValue30 : localValue12.internalMethod01283()) {
         localValue30.internalMethod05191(localValue26, localValue27, localValue28, localValue30.internalMethod07809());
         localValue30.internalMethod07890(localValue21);
         localValue30.internalMethod07901(localValue17);
         localValue30.internalMethod03398(localValue1);
         localValue27 += localValue30.internalMethod07809();
      }

      for (SettingComponent localValue32 : localValue12.internalMethod01283()) {
         localValue32.internalMethod08256(localValue1);
      }

      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      ScissorStack.internalMethod07643();
      return new MacroSettingsPopup.InternalType0094(
         localValue20, localValue21, localValue6, localValue17, localValue25.internalMethod00540(), localValue25.internalMethod00542(), localValue25.internalMethod08792(), localValue25.internalMethod08793()
      );
   }

   private MacroSettingsPopup.InternalType0093 internalMethod03439(
      UiRenderContext localValue1,
      MacroConfigPanel localValue2,
      AssistSwapItem localValue3,
      float localValue4,
      float localValue5,
      float localValue6,
      float localValue7,
      float localValue8,
      float localValue9,
      AnimatedValue localValue10
   ) {
      SizedFont localValue11 = Fonts.internalField1154.internalMethod01432(7.0F);
      SizedFont localValue12 = Fonts.internalField1154.internalMethod01432(6.0F);
      float localValue13 = localValue5 + localValue9;
      float localValue14 = localValue6 + localValue9;
      float localValue15 = localValue7 - localValue9 * 2.0F;
      localValue1.drawRoundedRect(localValue13, localValue14, localValue15, localValue8, CornerRadii.internalMethod03908(5.0F), ThemeColors.internalMethod08573().mulAlpha(0.4F * localValue4));
      float localValue17 = 10.0F;
      float localValue18 = 0.6875F;
      float localValue19 = 16.0F * localValue18;
      float localValue20 = localValue13 + 4.0F;
      float localValue21 = localValue14 + (localValue8 - localValue17) / 2.0F;
      float localValue22 = localValue20 + (localValue17 - localValue19) / 2.0F;
      float localValue23 = localValue21 + (localValue17 - localValue19) / 2.0F;
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, localValue4);
      localValue1.drawItem(localValue3.internalMethod06489().getItem(), localValue22, localValue23, localValue18);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      String localValue24 = localValue2.internalMethod02558()
         ? KeybindUtils.internalMethod07434(KeybindUtils.internalMethod06867()) + "..."
         : TextUtils.internalMethod04982(localValue2.internalMethod02557());
      if (localValue24 == null || localValue24.isEmpty()) {
         localValue24 = "-";
      }

      float localValue25 = 4.0F;
      float localValue26 = 2.0F;
      float localValue27 = localValue12.internalMethod00965(localValue24);
      float localValue28 = Math.max(12.0F, localValue27 + localValue25 * 2.0F);
      float localValue29 = localValue12.internalMethod04890() + localValue26 * 2.0F;
      float localValue30 = localValue13 + localValue15 - 5.0F - localValue28;
      float localValue31 = localValue14 + (localValue8 - localValue29) / 2.0F;
      boolean localValue32 = UiUtils.internalMethod06450(localValue30, localValue31, localValue28, localValue29, localValue1);
      localValue10.internalMethod07062(localValue32 || localValue2.internalMethod02558());
      localValue1.drawRoundedRect(
         localValue30,
         localValue31,
         localValue28,
         localValue29,
         CornerRadii.internalMethod03908(3.0F),
         ThemeColors.internalMethod08573().mulAlpha((0.45F + 0.25F * localValue10.internalMethod02881()) * localValue4)
      );
      localValue1.drawText(
         localValue12, localValue24, localValue30 + localValue25, localValue31 + (localValue29 - localValue12.internalMethod04890()) / 2.0F, ThemeColors.internalMethod08459().mulAlpha(0.75F * localValue4)
      );
      String localValue33 = LanguageManager.internalMethod07214(localValue3.internalMethod06026());
      float localValue34 = localValue20 + localValue17 + 6.0F;
      float localValue35 = localValue14 + (localValue8 - localValue11.internalMethod04890()) / 2.0F;
      float localValue36 = Math.max(10.0F, localValue30 - 6.0F - localValue34);
      localValue1.drawFadeoutText(localValue11, localValue33, localValue34, localValue35, ThemeColors.internalMethod08459().mulAlpha(0.9F * localValue4), 0.85F, 1.0F, localValue36);
      return new MacroSettingsPopup.InternalType0093(localValue30, localValue31, localValue28, localValue29);
   }

   static final class InternalType0093 {
      private final float internalField0205;
      private final float internalField0206;
      private final float internalField1048;
      private final float internalField1047;

      InternalType0093(float localValue1, float localValue2, float localValue3, float localValue4) {
         this.internalField0205 = localValue1;
         this.internalField0206 = localValue2;
         this.internalField1048 = localValue3;
         this.internalField1047 = localValue4;
      }

      @Override
      public final String toString() {
         return "InternalType0093[x=" + this.internalField0205 + ", y=" + this.internalField0206 + ", w=" + this.internalField1048 + ", h=" + this.internalField1047 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0205);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0206);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1048);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1047);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         MacroSettingsPopup.InternalType0093 other = (MacroSettingsPopup.InternalType0093) localValue1;
         return java.util.Objects.equals(this.internalField0205, other.internalField0205)
            && java.util.Objects.equals(this.internalField0206, other.internalField0206)
            && java.util.Objects.equals(this.internalField1048, other.internalField1048)
            && java.util.Objects.equals(this.internalField1047, other.internalField1047);
      }

      public float internalMethod00540() {
         return this.internalField0205;
      }

      public float internalMethod00542() {
         return this.internalField0206;
      }

      public float internalMethod08792() {
         return this.internalField1048;
      }

      public float internalMethod08793() {
         return this.internalField1047;
      }
   }

   public static final class InternalType0094 {
      private final float internalField0205;
      private final float internalField0206;
      private final float internalField1048;
      private final float internalField1047;
      private final float internalField1049;
      private final float internalField1046;
      private final float internalField1456;
      private final float internalField1457;

      public InternalType0094(float localValue1, float localValue2, float localValue3, float localValue4, float localValue5, float localValue6, float localValue7, float localValue8) {
         this.internalField0205 = localValue1;
         this.internalField0206 = localValue2;
         this.internalField1048 = localValue3;
         this.internalField1047 = localValue4;
         this.internalField1049 = localValue5;
         this.internalField1046 = localValue6;
         this.internalField1456 = localValue7;
         this.internalField1457 = localValue8;
      }

      @Override
      public final String toString() {
         return "InternalType0094[x=" + this.internalField0205 + ", y=" + this.internalField0206 + ", w=" + this.internalField1048 + ", h=" + this.internalField1047 + ", bindX=" + this.internalField1049 + ", bindY=" + this.internalField1046 + ", bindW=" + this.internalField1456 + ", bindH=" + this.internalField1457 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0205);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0206);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1048);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1047);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1049);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1046);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1456);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1457);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         MacroSettingsPopup.InternalType0094 other = (MacroSettingsPopup.InternalType0094) localValue1;
         return java.util.Objects.equals(this.internalField0205, other.internalField0205)
            && java.util.Objects.equals(this.internalField0206, other.internalField0206)
            && java.util.Objects.equals(this.internalField1048, other.internalField1048)
            && java.util.Objects.equals(this.internalField1047, other.internalField1047)
            && java.util.Objects.equals(this.internalField1049, other.internalField1049)
            && java.util.Objects.equals(this.internalField1046, other.internalField1046)
            && java.util.Objects.equals(this.internalField1456, other.internalField1456)
            && java.util.Objects.equals(this.internalField1457, other.internalField1457);
      }

      public float internalMethod02269() {
         return this.internalField0205;
      }

      public float internalMethod02270() {
         return this.internalField0206;
      }

      public float internalMethod08178() {
         return this.internalField1048;
      }

      public float internalMethod08179() {
         return this.internalField1047;
      }

      public float internalMethod08186() {
         return this.internalField1049;
      }

      public float internalMethod08189() {
         return this.internalField1046;
      }

      public float internalMethod09456() {
         return this.internalField1456;
      }

      public float internalMethod09458() {
         return this.internalField1457;
      }
   }
}
