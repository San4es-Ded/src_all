package rockstar.client.internal.script;








import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.animation.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.internal.game.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import rockstar.client.compat.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Predicate;
import pyrock.utility.render.ColorRGBA;

public class ItemPickerRenderer {
   public ItemPickerRenderer.InternalType0395 internalMethod07117(
      UiRenderContext localValue1,
      float localValue2,
      float localValue3,
      float localValue4,
      float localValue5,
      AnimatedValue localValue6,
      AnimatedValue localValue7,
      AnimatedValue localValue8,
      CustomTextField localValue9
   ) {
      SizedFont localValue10 = Fonts.internalField1154.internalMethod01432(7.0F);
      float localValue11 = 14.0F;
      float localValue12 = 176.0F;
      float localValue13 = localValue2 + 7.0F;
      float localValue14 = localValue3 + 7.0F;
      float localValue15 = 4.0F;
      float localValue16 = 6.0F;
      float localValue17 = localValue13 + localValue15;
      localValue7.internalMethod07062(localValue9.internalMethod00342());
      float localValue18 = (0.55F + 0.35F * localValue7.internalMethod02881()) * localValue6.internalMethod02881() * localValue5;
      localValue1.drawRoundedRect(localValue13, localValue14, localValue12, localValue11, CornerRadii.internalMethod03908(4.0F), ThemeColors.internalMethod08573().mulAlpha(localValue18));
      float localValue19 = localValue6.internalMethod02881() * localValue5;
      localValue1.drawIcon("search", localValue17, localValue14 + localValue15, localValue16, ColorRGBA.WHITE.withAlpha(255.0F * localValue19));
      localValue9.internalMethod05191(localValue13 + 9.0F, localValue14, localValue12 - 5.0F, localValue11);
      localValue9.internalMethod00143(ThemeColors.internalMethod08459().mulAlpha(localValue6.internalMethod02881() * localValue5));
      localValue9.internalMethod08627(localValue5);
      localValue9.internalMethod03398(localValue1);
      String localValue20 = "\u041e\u0442\u043c\u0435\u043d\u0430";
      float localValue21 = localValue10.internalMethod00965(localValue20) + 16.0F;
      float localValue22 = 14.0F;
      float localValue23 = localValue2 + localValue4 - 7.0F - localValue21;
      boolean localValue25 = UiUtils.internalMethod06450(localValue23, localValue14, localValue21, localValue22, localValue1);
      localValue8.internalMethod07062(localValue25);
      float localValue26 = (0.6F + 0.25F * localValue8.internalMethod02881()) * localValue6.internalMethod02881() * localValue5;
      localValue1.drawRoundedRect(localValue23, localValue14, localValue21, localValue22, CornerRadii.internalMethod03908(3.0F), ThemeColors.internalMethod08573().mulAlpha(localValue26));
      localValue1.drawText(
         localValue10,
         localValue20,
         localValue23 + 8.0F,
         localValue14 + (localValue22 - localValue10.internalMethod04890()) / 2.0F,
         ThemeColors.internalMethod08459().mulAlpha(0.9F * localValue6.internalMethod02881() * localValue5)
      );
      return new ItemPickerRenderer.InternalType0395(localValue23, localValue14, localValue21, localValue22);
   }

   public final void internalMethod00057(
      UiRenderContext localValue1,
      float localValue2,
      float localValue3,
      float localValue4,
      float localValue5,
      float localValue6,
      AnimatedValue localValue7,
      List<AssistCategoryGroup> localValue8,
      SmoothScrollHandler localValue9,
      Map<AssistItemPickerEntry, AnimatedValue> localValue10,
      CustomTextField localValue11,
      Predicate<AssistItemPickerEntry> localValue12
   ) {
      SizedFont localValue13 = Fonts.internalField0449.internalMethod01432(8.0F);
      SizedFont localValue14 = Fonts.internalField1154.internalMethod01432(7.0F);
      float localValue15 = localValue2 + 7.0F;
      float localValue16 = localValue3 + 30.0F;
      float localValue17 = localValue4 - 14.0F;
      float localValue18 = localValue5 - 30.0F - 3.0F;
      float localValue19 = 4.0F;
      float localValue20 = 20.0F;
      float localValue21 = 4.0F;
      float localValue22 = (localValue17 - localValue19) / 2.0F;
      ScissorStack.internalMethod06303(localValue1.getMatrices(), localValue15, localValue16 - 2.0F, localValue17, localValue18);
      float localValue23 = localValue16 - (float)localValue9.internalMethod02321();
      String localValue24 = localValue11.internalMethod06202().trim().toLowerCase(Locale.ROOT);

      for (AssistCategoryGroup localValue26 : localValue8) {
         ArrayList localValue27 = new ArrayList();

         for (AssistItemPickerEntry localValue29 : localValue26.internalMethod03025()) {
            if ((localValue24.isEmpty() || localValue29.internalMethod06189().toLowerCase(Locale.ROOT).contains(localValue24)) && !localValue12.test(localValue29)) {
               localValue27.add(localValue29);
            }
         }

         if (!localValue27.isEmpty()) {
            localValue1.drawText(
               localValue13, localValue26.internalMethod00302(), localValue15, localValue23, ThemeColors.internalMethod08459().mulAlpha(0.95F * localValue7.internalMethod02881() * localValue6)
            );
            localValue23 += localValue13.internalMethod04890() + 8.0F;

            for (int localValue52 = 0; localValue52 < localValue27.size(); localValue52++) {
               int localValue56 = localValue52 / 2;
               int localValue30 = localValue52 % 2;
               float localValue31 = localValue15 + localValue30 * (localValue22 + localValue19);
               float localValue32 = localValue23 + localValue56 * (localValue20 + localValue21);
               AssistItemPickerEntry localValue33 = (AssistItemPickerEntry)localValue27.get(localValue52);
               boolean localValue34 = UiUtils.internalMethod06450(localValue31, localValue32, localValue22, localValue20, localValue1);
               AnimatedValue localValue35 = localValue10.computeIfAbsent(localValue33, localValue0 -> new AnimatedValue(200L, 0.0F, Easing.internalField1626));
               localValue35.internalMethod07062(localValue34);
               float localValue36 = (0.55F + 0.25F * localValue35.internalMethod02881()) * localValue7.internalMethod02881() * localValue6;
               localValue1.drawRoundedRect(localValue31, localValue32, localValue22, localValue20, CornerRadii.internalMethod03908(5.0F), ThemeColors.internalMethod08573().mulAlpha(localValue36));
               float localValue37 = 0.75F;
               float localValue38 = 14.0F;
               float localValue39 = 16.0F * localValue37;
               float localValue40 = localValue31 + 5.0F;
               float localValue41 = localValue32 + (localValue20 - localValue38) / 2.0F;
               float localValue42 = localValue40 + (localValue38 - localValue39) / 2.0F;
               float localValue43 = localValue41 + (localValue38 - localValue39) / 2.0F;
               float localValue44 = localValue7.internalMethod02881() * localValue6;
               RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, localValue44);
               localValue1.drawItem(localValue33.internalMethod02770().getItem(), localValue42, localValue43, localValue37);
               RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
               localValue1.drawText(
                  localValue14,
                  localValue33.internalMethod06189(),
                  localValue40 + localValue38 + 5.0F,
                  localValue32 + (localValue20 - localValue14.internalMethod04890()) / 2.0F,
                  ThemeColors.internalMethod08459().mulAlpha(0.92F * localValue7.internalMethod02881() * localValue6)
               );
            }

            int localValue53 = (int)Math.ceil(localValue27.size() / 2.0F);
            localValue23 += localValue53 * localValue20 + Math.max(0, localValue53 - 1) * localValue21 + 14.0F;
         }
      }

      if (!localValue24.isEmpty()) {
         boolean localValue46 = false;

         for (AssistCategoryGroup localValue50 : localValue8) {
            for (AssistItemPickerEntry localValue57 : localValue50.internalMethod03025()) {
               if (localValue57.internalMethod06189().toLowerCase(Locale.ROOT).contains(localValue24) && !localValue12.test(localValue57)) {
                  localValue46 = true;
                  break;
               }
            }

            if (localValue46) {
               break;
            }
         }

         if (!localValue46) {
            SizedFont localValue49 = Fonts.internalField1154.internalMethod01432(7.0F);
            String localValue51 = "\u041d\u0438\u0447\u0435\u0433\u043e \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d\u043e";
            float localValue55 = localValue49.internalMethod00965(localValue51);
            localValue1.drawText(
               localValue49,
               localValue51,
               localValue2 + (localValue4 - localValue55) / 2.0F,
               localValue3 + localValue5 / 2.0F,
               ThemeColors.internalMethod08459().mulAlpha(0.45F * localValue7.internalMethod02881() * localValue6)
            );
         }
      }

      ScissorStack.internalMethod07643();
      float localValue47 = this.internalMethod05219(localValue24, localValue13, localValue20, localValue21, localValue8, localValue12);
      localValue9.internalMethod04308(Math.min(0.0F, localValue18 - localValue47));
   }

   public final AssistItemTemplate internalMethod00363(
      float localValue1,
      float localValue2,
      float localValue3,
      float localValue4,
      float localValue5,
      float localValue6,
      SmoothScrollHandler localValue7,
      List<AssistCategoryGroup> localValue8,
      CustomTextField localValue9,
      Predicate<AssistItemPickerEntry> localValue10
   ) {
      float localValue11 = localValue3 + 7.0F;
      float localValue12 = localValue4 + 30.0F;
      float localValue13 = localValue5 - 14.0F;
      float localValue14 = localValue6 - 30.0F - 3.0F;
      if (!UiUtils.internalMethod05786(localValue11, localValue12, localValue13, localValue14, (int)localValue1, (int)localValue2)) {
         return null;
      } else {
         float localValue15 = 4.0F;
         float localValue16 = 20.0F;
         float localValue17 = 4.0F;
         float localValue18 = (localValue13 - localValue15) / 2.0F;
         float localValue19 = localValue12 - (float)localValue7.internalMethod02321();
         String localValue20 = localValue9.internalMethod06202().trim().toLowerCase(Locale.ROOT);

         for (AssistCategoryGroup localValue22 : localValue8) {
            ArrayList localValue23 = new ArrayList();

            for (AssistItemPickerEntry localValue25 : localValue22.internalMethod03025()) {
               if ((localValue20.isEmpty() || localValue25.internalMethod06189().toLowerCase(Locale.ROOT).contains(localValue20)) && !localValue10.test(localValue25)) {
                  localValue23.add(localValue25);
               }
            }

            if (!localValue23.isEmpty()) {
               localValue19 += Fonts.internalField0449.internalMethod01432(8.0F).internalMethod04890() + 8.0F;

               for (int localValue30 = 0; localValue30 < localValue23.size(); localValue30++) {
                  int localValue32 = localValue30 / 2;
                  int localValue26 = localValue30 % 2;
                  float localValue27 = localValue11 + localValue26 * (localValue18 + localValue15);
                  float localValue28 = localValue19 + localValue32 * (localValue16 + localValue17);
                  if (UiUtils.internalMethod05786(localValue27, localValue28, localValue18, localValue16, (int)localValue1, (int)localValue2)) {
                     return new AssistItemTemplate((AssistItemPickerEntry)localValue23.get(localValue30));
                  }
               }

               int localValue31 = (int)Math.ceil(localValue23.size() / 2.0F);
               localValue19 += localValue31 * localValue16 + Math.max(0, localValue31 - 1) * localValue17 + 14.0F;
            }
         }

         return null;
      }
   }

   private float internalMethod05219(String localValue1, SizedFont localValue2, float localValue3, float localValue4, List<AssistCategoryGroup> localValue5, Predicate<AssistItemPickerEntry> localValue6) {
      float localValue7 = 0.0F;
      String localValue8 = localValue1 == null ? "" : localValue1;

      for (AssistCategoryGroup localValue10 : localValue5) {
         ArrayList localValue11 = new ArrayList();

         for (AssistItemPickerEntry localValue13 : localValue10.internalMethod03025()) {
            if ((localValue8.isEmpty() || localValue13.internalMethod06189().toLowerCase(Locale.ROOT).contains(localValue8)) && !localValue6.test(localValue13)) {
               localValue11.add(localValue13);
            }
         }

         if (!localValue11.isEmpty()) {
            localValue7 += localValue2.internalMethod04890() + 8.0F;
            int localValue15 = (int)Math.ceil(localValue11.size() / 2.0F);
            localValue7 += localValue15 * localValue3 + Math.max(0, localValue15 - 1) * localValue4 + 14.0F;
         }
      }

      return localValue7;
   }

   public static final class InternalType0395 {
      private final float internalField0205;
      private final float internalField0206;
      private final float internalField1048;
      private final float internalField1047;

      public InternalType0395(float localValue1, float localValue2, float localValue3, float localValue4) {
         this.internalField0205 = localValue1;
         this.internalField0206 = localValue2;
         this.internalField1048 = localValue3;
         this.internalField1047 = localValue4;
      }

      @Override
      public final String toString() {
         return "InternalType0395[x=" + this.internalField0205 + ", y=" + this.internalField0206 + ", w=" + this.internalField1048 + ", h=" + this.internalField1047 + "]";
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
         ItemPickerRenderer.InternalType0395 other = (ItemPickerRenderer.InternalType0395) localValue1;
         return java.util.Objects.equals(this.internalField0205, other.internalField0205)
            && java.util.Objects.equals(this.internalField0206, other.internalField0206)
            && java.util.Objects.equals(this.internalField1048, other.internalField1048)
            && java.util.Objects.equals(this.internalField1047, other.internalField1047);
      }

      public float internalMethod03523() {
         return this.internalField0205;
      }

      public float internalMethod03525() {
         return this.internalField0206;
      }

      public float internalMethod08114() {
         return this.internalField1048;
      }

      public float internalMethod08115() {
         return this.internalField1047;
      }
   }
}
