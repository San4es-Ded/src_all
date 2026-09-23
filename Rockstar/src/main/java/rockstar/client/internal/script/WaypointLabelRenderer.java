package rockstar.client.internal.script;






import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.i18n.*;
import rockstar.client.*;
import rockstar.modules.combat.*;
import rockstar.modules.movement.*;
import rockstar.modules.visual.*;
import rockstar.modules.player.*;
import rockstar.modules.other.*;

import net.minecraft.util.math.MathHelper;
import pyrock.utility.render.ColorRGBA;
import pyrock.utility.render.CustomDrawContext;

public final class WaypointLabelRenderer {
   private WaypointLabelRenderer() {
   }

   public static void internalMethod03782(CustomDrawContext localValue0, String localValue1, double localValue2, ColorRGBA localValue4, float localValue5) {
      float localValue6 = MathHelper.clamp(localValue5, 0.0F, 1.0F);
      if (!(localValue6 <= 0.01F)) {
         String localValue7 = internalMethod05012(localValue2);
         float localValue8 = localValue6 * InterfaceModule.internalMethod07584();
         float localValue9 = localValue6 * InterfaceModule.internalMethod07585();
         if (localValue8 > 0.01F) {
            internalMethod02705(localValue0, localValue1, localValue7, localValue4, localValue8);
         }

         if (localValue9 > 0.01F) {
            internalMethod04272(localValue0, localValue1, localValue7, localValue4, localValue9);
         }
      }
   }

   public static WaypointLabelRenderer.InternalType0471 internalMethod06029(String localValue0, double localValue1) {
      String localValue3 = internalMethod05012(localValue1);
      float localValue4 = 0.0F;
      float localValue5 = 0.0F;
      float localValue6 = 0.0F;
      float localValue7 = 0.0F;
      boolean localValue8 = false;
      if (InterfaceModule.internalMethod07584() > 0.01F) {
         SizedFont localValue9 = Fonts.internalField1157.internalMethod01432(10.0F);
         SizedFont localValue10 = Fonts.internalField1157.internalMethod01432(9.0F);
         float localValue11 = 6.0F;
         float localValue12 = 21.0F;
         float localValue13 = 18.0F;
         float localValue14 = 8.0F;
         float localValue15 = Math.max(localValue14 * 2.0F + localValue11 + 8.0F + localValue9.internalMethod00965(localValue0), 52.0F) - 1.0F;
         float localValue16 = Math.max(localValue10.internalMethod00965(localValue3) + 16.0F, 32.0F) + 1.0F;
         float localValue17 = 4.0F;
         float localValue18 = localValue17 + localValue12 + 3.0F;
         float localValue19 = Math.max(localValue15, localValue16) / 2.0F;
         localValue4 = -localValue19;
         localValue5 = localValue19;
         localValue6 = localValue17;
         localValue7 = localValue18 + localValue13;
         localValue8 = true;
      }

      if (InterfaceModule.internalMethod07585() > 0.01F) {
         SizedFont localValue24 = Fonts.internalField1157.internalMethod01432(9.0F);
         SizedFont localValue25 = Fonts.internalField1157.internalMethod01432(8.0F);
         float localValue26 = 21.0F;
         float localValue27 = 6.0F;
         float localValue28 = 8.0F;
         float localValue29 = 6.0F;
         float localValue30 = 6.0F;
         float localValue31 = 7.0F;
         float localValue32 = internalMethod04587(localValue24.internalMethod00965(localValue0));
         float localValue33 = internalMethod04587(localValue25.internalMethod00965(localValue3));
         float localValue34 = internalMethod04587(localValue33 + localValue31 * 2.0F);
         float localValue20 = localValue27 + localValue29 + localValue32 + localValue30 + localValue34;
         float localValue21 = internalMethod04587(localValue20 + localValue28 * 2.0F) - 7.0F;
         float localValue22 = localValue21 / 2.0F;
         float localValue23 = 5.5F;
         if (!localValue8) {
            localValue4 = -localValue22;
            localValue5 = localValue22;
            localValue6 = localValue23;
            localValue7 = localValue23 + localValue26;
            localValue8 = true;
         } else {
            localValue4 = Math.min(localValue4, -localValue22);
            localValue5 = Math.max(localValue5, localValue22);
            localValue6 = Math.min(localValue6, localValue23);
            localValue7 = Math.max(localValue7, localValue23 + localValue26);
         }
      }

      if (!localValue8) {
         localValue4 = -26.0F;
         localValue5 = 26.0F;
         localValue6 = 0.0F;
         localValue7 = 27.0F;
      }

      return new WaypointLabelRenderer.InternalType0471(localValue4, localValue6, localValue5 - localValue4, localValue7 - localValue6);
   }

   public static ColorRGBA internalMethod00779(String localValue0) {
      int localValue1 = localValue0 == null ? 0 : localValue0.hashCode();
      float localValue2 = (localValue1 & 2147483647) % 360 / 360.0F;
      return ColorRGBA.fromHSB(localValue2, 0.48F, 0.95F);
   }

   private static void internalMethod02705(CustomDrawContext localValue0, String localValue1, String localValue2, ColorRGBA localValue3, float localValue4) {
      SizedFont localValue5 = Fonts.internalField1157.internalMethod01432(10.0F);
      SizedFont localValue6 = Fonts.internalField1157.internalMethod01432(9.0F);
      float localValue7 = 6.0F;
      float localValue8 = 21.0F;
      float localValue9 = 18.0F;
      float localValue10 = 8.0F;
      float localValue11 = Math.max(localValue10 * 2.0F + localValue7 + 8.0F + localValue5.internalMethod00965(localValue1), 52.0F) - 1.0F;
      float localValue12 = Math.max(localValue6.internalMethod00965(localValue2) + 16.0F, 32.0F) + 1.0F;
      float localValue13 = -localValue11 / 2.0F;
      float localValue14 = 4.0F;
      float localValue15 = -localValue12 / 2.0F + 0.5F;
      float localValue16 = localValue14 + localValue8 + 3.0F;
      CornerRadii localValue17 = CornerRadii.internalMethod03908(localValue8 / 2.0F);
      CornerRadii localValue18 = CornerRadii.internalMethod03908(localValue9 / 2.0F);
      ColorRGBA localValue19 = ThemeColors.internalMethod08573().withAlpha(60.0F * localValue4);
      ColorRGBA localValue20 = ThemeColors.internalMethod08459().withAlpha(45.0F * localValue4);
      ColorRGBA localValue21 = ThemeColors.internalMethod08459().withAlpha(255.0F * localValue4);
      localValue0.drawLiquidGlass(localValue13, localValue14, localValue11, localValue8, 2.0F, 0.08F, localValue17, ColorRGBA.WHITE.withAlpha(191.0F));
      localValue0.drawRoundedRect(localValue13, localValue14, localValue11, localValue8, localValue17, localValue19);
      localValue0.drawLiquidGlass(localValue15, localValue16, localValue12, localValue9, 2.0F, 0.08F, localValue18, ColorRGBA.WHITE.withAlpha(191.0F));
      localValue0.drawRoundedRect(localValue15, localValue16, localValue12, localValue9, localValue18, localValue19.mulAlpha(0.82F));
      float localValue22 = localValue13 + localValue10;
      float localValue23 = localValue14 + (localValue8 - localValue7) / 2.0F;
      localValue0.drawRoundedRect(localValue22, localValue23, localValue7, localValue7, CornerRadii.internalMethod03908(localValue7 / 2.0F), localValue3.withAlpha(245.0F * localValue4));
      localValue0.drawText(localValue5, localValue1, localValue22 + localValue7 + 6.0F, localValue14 + (localValue8 - localValue5.internalMethod04890()) / 2.0F, localValue21);
      localValue0.drawCenteredText(localValue6, localValue2, 1.0F, localValue16 + (localValue9 - localValue6.internalMethod04890()) / 2.0F, localValue21.mulAlpha(0.88F));
   }

   private static void internalMethod04272(CustomDrawContext localValue0, String localValue1, String localValue2, ColorRGBA localValue3, float localValue4) {
      SizedFont localValue5 = Fonts.internalField1157.internalMethod01432(9.0F);
      SizedFont localValue6 = Fonts.internalField1157.internalMethod01432(8.0F);
      float localValue7 = 21.0F;
      float localValue8 = 6.0F;
      float localValue9 = 8.0F;
      float localValue10 = 6.0F;
      float localValue11 = 6.0F;
      float localValue12 = 14.0F;
      float localValue13 = 7.0F;
      float localValue14 = internalMethod04587(localValue5.internalMethod00965(localValue1));
      float localValue15 = internalMethod04587(localValue6.internalMethod00965(localValue2));
      float localValue16 = internalMethod04587(localValue15 + localValue13 * 2.0F);
      float localValue17 = localValue8 + localValue10 + localValue14 + localValue11 + localValue16;
      float localValue18 = internalMethod04587(localValue17 + localValue9 * 2.0F) - 7.0F;
      float localValue19 = internalMethod04587(-localValue18 / 2.0F);
      float localValue20 = 5.5F;
      CornerRadii localValue21 = CornerRadii.internalMethod03908(localValue7 / 2.0F);
      ColorRGBA localValue22 = new ColorRGBA(13.0F, 18.0F, 20.0F, 238.0F * localValue4);
      ColorRGBA localValue23 = localValue3.mix(ColorRGBA.BLACK, 0.68F).withAlpha(130.0F * localValue4);
      localValue0.drawRoundedRect(localValue19, localValue20, localValue18, localValue7, localValue21, localValue22);
      float localValue24 = localValue19 + localValue9;
      float localValue25 = internalMethod05118(localValue20, localValue7, localValue8);
      localValue0.drawRoundedRect(localValue24, localValue25, localValue8, localValue8, CornerRadii.internalMethod03908(localValue8 / 2.0F), localValue3.withAlpha(245.0F * localValue4));
      float localValue26 = localValue24 + localValue8 + localValue10;
      float localValue27 = internalMethod04587(localValue20 + (localValue7 - localValue5.internalMethod04890()) / 2.0F - 0.5F);
      localValue0.drawText(localValue5, localValue1, localValue26, localValue27, ColorRGBA.WHITE.withAlpha(250.0F * localValue4));
      float localValue28 = localValue26 + localValue14 + localValue10;
      float localValue29 = internalMethod05118(localValue20, localValue7, localValue12);
      localValue0.drawRoundedRect(localValue28, localValue29, localValue16 - 4.0F, localValue12, CornerRadii.internalMethod03908(localValue12 / 2.0F), localValue23);
      localValue0.drawCenteredText(
         localValue6, localValue2, localValue28 + localValue16 / 2.0F - 1.0F, internalMethod04587(localValue20 + (localValue7 - localValue6.internalMethod04890()) / 2.0F), localValue3.withAlpha(255.0F * localValue4)
      );
   }

   private static float internalMethod05118(float localValue0, float localValue1, float localValue2) {
      return internalMethod04587(localValue0 + (localValue1 - localValue2) / 2.0F);
   }

   private static float internalMethod04587(float localValue0) {
      return Math.round(localValue0 * 2.0F) / 2.0F;
   }

   private static String internalMethod05012(double localValue0) {
      String localValue2 = TextUtils.internalMethod07254(localValue0);
      Language localValue3 = LanguageManager.internalMethod00625();
      if (localValue3 != Language.internalField0164 && localValue3 != Language.internalField1031) {
         return localValue2 + " m";
      } else {
         localValue2 = localValue2.replace(".", ",");
         return localValue2 + " \u043c";
      }
   }

   public static final class InternalType0471 {
      private final float internalField0205;
      private final float internalField0206;
      private final float internalField1048;
      private final float internalField1047;

      public InternalType0471(float localValue1, float localValue2, float localValue3, float localValue4) {
         this.internalField0205 = localValue1;
         this.internalField0206 = localValue2;
         this.internalField1048 = localValue3;
         this.internalField1047 = localValue4;
      }

      @Override
      public final String toString() {
         return "InternalType0471[x=" + this.internalField0205 + ", y=" + this.internalField0206 + ", w=" + this.internalField1048 + ", h=" + this.internalField1047 + "]";
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
         WaypointLabelRenderer.InternalType0471 other = (WaypointLabelRenderer.InternalType0471) localValue1;
         return java.util.Objects.equals(this.internalField0205, other.internalField0205)
            && java.util.Objects.equals(this.internalField0206, other.internalField0206)
            && java.util.Objects.equals(this.internalField1048, other.internalField1048)
            && java.util.Objects.equals(this.internalField1047, other.internalField1047);
      }

      public float internalMethod06498() {
         return this.internalField0205;
      }

      public float internalMethod06546() {
         return this.internalField0206;
      }

      public float internalMethod08636() {
         return this.internalField1048;
      }

      public float internalMethod08637() {
         return this.internalField1047;
      }
   }
}
