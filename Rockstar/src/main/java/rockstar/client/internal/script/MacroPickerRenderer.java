package rockstar.client.internal.script;










import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.i18n.*;
import rockstar.client.animation.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.internal.game.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import rockstar.client.compat.RenderSystem;
import java.util.List;
import java.util.Map;
import java.util.function.ToDoubleFunction;
import pyrock.utility.render.ColorRGBA;

public class MacroPickerRenderer {
   public MacroPickerRenderer.InternalType0450 internalMethod02409(
      UiRenderContext localValue1,
      float localValue2,
      float localValue3,
      float localValue4,
      float localValue5,
      AnimatedValue localValue6,
      AssistItemCategory localValue7,
      Map<AssistItemCategory, AnimatedValue> localValue8,
      AnimatedValue localValue9
   ) {
      SizedFont localValue10 = Fonts.internalField1154.internalMethod01432(7.0F);
      float localValue11 = localValue2 + 7.0F;
      float localValue12 = localValue3 + 24.0F;
      localValue1.drawText(
         Fonts.internalField0449.internalMethod01432(8.0F),
         "\u041c\u0430\u043a\u0440\u043e\u0441\u044b",
         localValue11 + 1.0F,
         localValue12 - 14.0F,
         ThemeColors.internalMethod08459().mulAlpha(0.95F * localValue6.internalMethod02881() * localValue5)
      );

      for (AssistItemCategory localValue16 : AssistItemCategory.values()) {
         String localValue17 = localValue16.internalMethod06548();
         float localValue18 = localValue10.internalMethod00965(localValue17);
         float localValue19 = localValue18 + 8.0F;
         boolean localValue20 = localValue7 == localValue16;
         boolean localValue21 = UiUtils.internalMethod06450(localValue11, localValue12, localValue19, 13.0, localValue1);
         AnimatedValue localValue22 = localValue8.computeIfAbsent(localValue16, localValue0 -> new AnimatedValue(200L, 0.0F, Easing.internalField1626));
         localValue22.internalMethod07059(localValue20 ? 1.0F : (localValue21 ? 0.67F : 0.0F));
         float localValue23 = 0.4F + 0.6F * localValue22.internalMethod02881();
         float localValue24 = localValue20 ? 1.0F : 0.75F;
         localValue1.drawRoundedRect(
            localValue11,
            localValue12,
            localValue19,
            13.0F,
            CornerRadii.internalMethod03908(3.0F),
            ThemeColors.internalMethod08573().mulAlpha(localValue23 * localValue6.internalMethod02881() * localValue5)
         );
         localValue1.drawText(
            localValue10,
            localValue17,
            localValue11 + 4.0F,
            localValue12 + (13.0F - localValue10.internalMethod04890()) / 2.0F,
            ThemeColors.internalMethod08459().mulAlpha(localValue24 * localValue6.internalMethod02881() * localValue5)
         );
         localValue11 += localValue19 + 4.0F;
      }

      String localValue25 = "\u0414\u043e\u0431\u0430\u0432\u0438\u0442\u044c";
      float localValue26 = localValue10.internalMethod00965(localValue25);
      float localValue27 = localValue26 + 8.0F;
      float localValue28 = 13.0F;
      float localValue29 = localValue2 + localValue4 - localValue27 - 7.0F;
      float localValue30 = localValue3 + 24.0F;
      boolean localValue31 = UiUtils.internalMethod06450(localValue29, localValue30, localValue27, localValue28, localValue1);
      localValue9.internalMethod07062(localValue31);
      float localValue32 = 0.4F + 0.6F * localValue9.internalMethod02881();
      localValue1.drawRoundedRect(
         localValue29,
         localValue30,
         localValue27,
         localValue28,
         CornerRadii.internalMethod03908(3.0F),
         ThemeColors.internalMethod08573().mulAlpha(localValue32 * localValue6.internalMethod02881() * localValue5)
      );
      localValue1.drawText(
         localValue10,
         localValue25,
         localValue29 + 4.0F,
         localValue30 + (localValue28 - localValue10.internalMethod04890()) / 2.0F,
         ThemeColors.internalMethod08459().mulAlpha((0.75F + 0.25F * localValue9.internalMethod02881()) * localValue6.internalMethod02881() * localValue5)
      );
      return new MacroPickerRenderer.InternalType0450(localValue29, localValue30, localValue27, localValue28);
   }

   public void internalMethod01678(
      UiRenderContext localValue1,
      float localValue2,
      float localValue3,
      float localValue4,
      float localValue5,
      float localValue6,
      AnimatedValue localValue7,
      SmoothScrollHandler localValue8,
      AssistItemCategory localValue9,
      List<AssistSwapItem> localValue10,
      AssistSwapItem localValue11,
      AssistSwapItem localValue12,
      int localValue13,
      int localValue14,
      Map<AssistSwapItem, ItemSlotAnimation> localValue15,
      Map<AssistSwapItem, AnimatedValue> localValue16,
      Map<AssistSwapItem, AnimatedValue> localValue17,
      ToDoubleFunction<AssistSwapItem> localValue18
   ) {
      SizedFont localValue19 = Fonts.internalField0449.internalMethod01432(8.0F);
      SizedFont localValue20 = Fonts.internalField1154.internalMethod01432(7.0F);
      SizedFont localValue21 = Fonts.internalField1154.internalMethod01432(6.0F);
      float localValue22 = localValue2 + 7.0F;
      float localValue23 = localValue3 + 44.0F;
      float localValue24 = localValue4 - 14.0F;
      float localValue25 = localValue5 - 30.0F - 10.0F;
      if (localValue10.isEmpty()) {
         String localValue47 = "\u041f\u0443\u0441\u0442\u043e";
         float localValue48 = localValue20.internalMethod00965(localValue47);
         localValue1.drawText(
            localValue20,
            localValue47,
            localValue22 + (localValue24 - localValue48) / 2.0F,
            localValue23 + localValue25 / 2.0F - localValue20.internalMethod04890() / 2.0F,
            ThemeColors.internalMethod08459().mulAlpha(0.35F * localValue6)
         );
      } else {
         float localValue26 = 4.0F;
         float localValue27 = 20.0F;
         float localValue28 = 4.0F;
         float localValue29 = (localValue24 - localValue26) / 2.0F;
         float localValue30 = 2.0F;
         ScissorStack.internalMethod06303(localValue1.getMatrices(), localValue22, localValue23 - localValue30, localValue24, localValue25 - localValue30 * 2.0F);
         float localValue31 = localValue23 - (float)localValue8.internalMethod02321();
         if (localValue9 == AssistItemCategory.internalField0459) {
            for (AssistItemCategory localValue35 : AssistItemCategory.values()) {
               if (localValue35 != AssistItemCategory.internalField0459) {
                  List localValue36 = localValue10.stream().filter(localValue1x -> localValue1x.internalMethod06886() == localValue35).toList();
                  if (!localValue36.isEmpty()) {
                     localValue1.drawText(
                        localValue19,
                        localValue35.internalMethod06548(),
                        localValue22 + 1.0F,
                        localValue31 + 2.0F,
                        ThemeColors.internalMethod08459().mulAlpha(0.95F * localValue7.internalMethod02881() * localValue6)
                     );
                     localValue31 += localValue19.internalMethod04890() + 10.0F;

                     for (int localValue37 = 0; localValue37 < localValue36.size(); localValue37++) {
                        int localValue38 = localValue37 / 2;
                        int localValue39 = localValue37 % 2;
                        float localValue40 = localValue22 + localValue39 * (localValue29 + localValue26);
                        float localValue41 = localValue31 + localValue38 * (localValue27 + localValue28);
                        if (localValue41 + localValue27 >= localValue23 - 10.0F && localValue41 <= localValue23 + localValue25 + 10.0F) {
                           AssistSwapItem localValue42 = (AssistSwapItem)localValue36.get(localValue37);
                           boolean localValue43 = UiUtils.internalMethod06450(localValue40, localValue41, localValue29, localValue27, localValue1);
                           boolean localValue44 = localValue42 == localValue11;
                           float localValue45 = (float)localValue18.applyAsDouble(localValue42);
                           AnimatedValue localValue46 = localValue16.computeIfAbsent(
                              localValue42, localValue0 -> new AnimatedValue(200L, 0.0F, Easing.internalField1626)
                           );
                           localValue46.internalMethod07062(localValue43 || localValue44);
                           this.internalMethod01074(
                              localValue1,
                              localValue40,
                              localValue41,
                              localValue29,
                              localValue27,
                              localValue42,
                              localValue46.internalMethod02881(),
                              localValue20,
                              localValue21,
                              localValue44,
                              localValue45,
                              localValue6,
                              localValue7,
                              localValue12,
                              localValue13,
                              localValue14,
                              localValue15,
                              localValue17
                           );
                        }
                     }

                     int localValue58 = (int)Math.ceil(localValue36.size() / 2.0F);
                     localValue31 += localValue58 * localValue27 + Math.max(0, localValue58 - 1) * localValue28 + 14.0F;
                  }
               }
            }
         } else {
            List localValue50 = localValue10.stream().filter(localValue1x -> localValue1x.internalMethod06886() == localValue9).toList();
            if (localValue50.isEmpty()) {
               String localValue52 = "\u041f\u0443\u0441\u0442\u043e";
               float localValue54 = localValue20.internalMethod00965(localValue52);
               localValue1.drawText(
                  localValue20,
                  localValue52,
                  localValue22 + (localValue24 - localValue54) / 2.0F,
                  localValue23 + localValue25 / 2.0F - localValue20.internalMethod04890() / 2.0F,
                  ThemeColors.internalMethod08459().mulAlpha(0.35F * localValue6)
               );
            } else {
               for (int localValue53 = 0; localValue53 < localValue50.size(); localValue53++) {
                  int localValue55 = localValue53 / 2;
                  int localValue56 = localValue53 % 2;
                  float localValue57 = localValue22 + localValue56 * (localValue29 + localValue26);
                  float localValue59 = localValue31 + localValue55 * (localValue27 + localValue28);
                  if (localValue59 + localValue27 >= localValue23 - 10.0F && localValue59 <= localValue23 + localValue25 + 10.0F) {
                     AssistSwapItem localValue60 = (AssistSwapItem)localValue50.get(localValue53);
                     float localValue61 = (float)localValue18.applyAsDouble(localValue60);
                     boolean localValue62 = UiUtils.internalMethod06450(localValue57, localValue59, localValue29, localValue27, localValue1);
                     boolean localValue63 = localValue60 == localValue11;
                     AnimatedValue localValue64 = localValue16.computeIfAbsent(localValue60, localValue0 -> new AnimatedValue(200L, 0.0F, Easing.internalField1626));
                     localValue64.internalMethod07062(localValue62 || localValue63);
                     this.internalMethod01074(
                        localValue1,
                        localValue57,
                        localValue59,
                        localValue29,
                        localValue27,
                        localValue60,
                        localValue64.internalMethod02881(),
                        localValue20,
                        localValue21,
                        localValue63,
                        localValue61,
                        localValue6,
                        localValue7,
                        localValue12,
                        localValue13,
                        localValue14,
                        localValue15,
                        localValue17
                     );
                  }
               }
            }
         }

         ScissorStack.internalMethod07643();
         float localValue51 = this.internalMethod03504(localValue9, localValue10, localValue19, localValue27, localValue28);
         localValue8.internalMethod04308(Math.min(0.0F, localValue25 - localValue51));
      }
   }

   public AssistSlotLayout internalMethod06716(
      float localValue1, float localValue2, float localValue3, float localValue4, float localValue5, float localValue6, SmoothScrollHandler localValue7, AssistItemCategory localValue8, List<AssistSwapItem> localValue9
   ) {
      float localValue10 = localValue3 + 7.0F;
      float localValue11 = localValue4 + 44.0F;
      float localValue12 = localValue5 - 14.0F;
      float localValue13 = localValue6 - 30.0F - 10.0F;
      if (!UiUtils.internalMethod05786(localValue10, localValue11, localValue12, localValue13, (int)localValue1, (int)localValue2)) {
         return null;
      } else {
         SizedFont localValue14 = Fonts.internalField0449.internalMethod01432(8.0F);
         SizedFont localValue15 = Fonts.internalField1154.internalMethod01432(6.0F);
         float localValue16 = 4.0F;
         float localValue17 = 20.0F;
         float localValue18 = 4.0F;
         float localValue19 = (localValue12 - localValue16) / 2.0F;
         float localValue20 = localValue11 - (float)localValue7.internalMethod02321();
         if (localValue8 == AssistItemCategory.internalField0459) {
            for (AssistItemCategory localValue24 : AssistItemCategory.values()) {
               if (localValue24 != AssistItemCategory.internalField0459) {
                  List localValue25 = localValue9.stream().filter(localValue1x -> localValue1x.internalMethod06886() == localValue24).toList();
                  if (!localValue25.isEmpty()) {
                     localValue20 += localValue14.internalMethod04890() + 10.0F;

                     for (int localValue26 = 0; localValue26 < localValue25.size(); localValue26++) {
                        int localValue27 = localValue26 / 2;
                        int localValue28 = localValue26 % 2;
                        float localValue29 = localValue10 + localValue28 * (localValue19 + localValue16);
                        float localValue30 = localValue20 + localValue27 * (localValue17 + localValue18);
                        if (UiUtils.internalMethod05786(localValue29, localValue30, localValue19, localValue17, (int)localValue1, (int)localValue2)) {
                           AssistSwapItem localValue31 = (AssistSwapItem)localValue25.get(localValue26);
                           String localValue32 = TextUtils.internalMethod04982(localValue31.internalMethod03234());
                           float localValue33 = 12.0F;
                           float localValue34 = Math.max(16.0F, localValue15.internalMethod00965(localValue32) + 8.0F);
                           float localValue35 = localValue29 + localValue19 - localValue34 - 5.0F;
                           float localValue36 = localValue30 + (localValue17 - localValue33) / 2.0F;
                           return new AssistSlotLayout(localValue31, localValue29, localValue30, localValue19, localValue17, localValue35, localValue36, localValue34, localValue33);
                        }
                     }

                     int localValue43 = (int)Math.ceil(localValue25.size() / 2.0F);
                     localValue20 += localValue43 * localValue17 + Math.max(0, localValue43 - 1) * localValue18 + 14.0F;
                  }
               }
            }
         } else {
            List localValue38 = localValue9.stream().filter(localValue1x -> localValue1x.internalMethod06886() == localValue8).toList();

            for (int localValue39 = 0; localValue39 < localValue38.size(); localValue39++) {
               int localValue40 = localValue39 / 2;
               int localValue41 = localValue39 % 2;
               float localValue42 = localValue10 + localValue41 * (localValue19 + localValue16);
               float localValue44 = localValue20 + localValue40 * (localValue17 + localValue18);
               if (UiUtils.internalMethod05786(localValue42, localValue44, localValue19, localValue17, (int)localValue1, (int)localValue2)) {
                  AssistSwapItem localValue45 = (AssistSwapItem)localValue38.get(localValue39);
                  String localValue46 = TextUtils.internalMethod04982(localValue45.internalMethod03234());
                  float localValue47 = 12.0F;
                  float localValue48 = Math.max(16.0F, localValue15.internalMethod00965(localValue46) + 8.0F);
                  float localValue49 = localValue42 + localValue19 - localValue48 - 5.0F;
                  float localValue50 = localValue44 + (localValue17 - localValue47) / 2.0F;
                  return new AssistSlotLayout(localValue45, localValue42, localValue44, localValue19, localValue17, localValue49, localValue50, localValue48, localValue47);
               }
            }
         }

         return null;
      }
   }

   private void internalMethod01074(
      UiRenderContext localValue1,
      float localValue2,
      float localValue3,
      float localValue4,
      float localValue5,
      AssistSwapItem localValue6,
      float localValue7,
      SizedFont localValue8,
      SizedFont localValue9,
      boolean localValue10,
      float localValue11,
      float localValue12,
      AnimatedValue localValue13,
      AssistSwapItem localValue14,
      int localValue15,
      int localValue16,
      Map<AssistSwapItem, ItemSlotAnimation> localValue17,
      Map<AssistSwapItem, AnimatedValue> localValue18
   ) {
      float localValue19 = (localValue10 ? 0.85F : 0.55F + 0.3F * localValue7) * localValue13.internalMethod02881() * localValue11 * localValue12;
      ColorRGBA localValue20 = ThemeColors.internalMethod08573().mulAlpha(localValue19);
      HudRenderUtils.internalMethod08976(localValue1.getMatrices(), localValue2 + localValue4 / 2.0F, localValue3 + localValue5 / 2.0F, 0.85F + 0.15F * localValue11);
      localValue1.drawRoundedRect(localValue2, localValue3, localValue4, localValue5, CornerRadii.internalMethod03908(5.0F), localValue20);
      float localValue21 = 14.0F;
      float localValue22 = 0.75F;
      float localValue23 = 16.0F * localValue22;
      float localValue24 = localValue2 + 4.0F;
      float localValue25 = localValue3 + (localValue5 - localValue21) / 2.0F;
      float localValue26 = localValue24 + (localValue21 - localValue23) / 2.0F;
      float localValue27 = localValue25 + (localValue21 - localValue23) / 2.0F;
      float localValue28 = localValue13.internalMethod02881() * localValue11 * localValue12;
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, localValue28);
      localValue1.drawItem(localValue6.internalMethod06489().getItem(), localValue26, localValue27, localValue22);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      boolean localValue29 = localValue14 == localValue6;
      String localValue30 = localValue29
         ? KeybindUtils.internalMethod07434(KeybindUtils.internalMethod06867()) + "..."
         : TextUtils.internalMethod04982(localValue6.internalMethod03234());
      float localValue31 = 12.0F;
      float localValue32 = Math.max(16.0F, localValue9.internalMethod00965(localValue30) + 8.0F);
      float localValue33 = localValue2 + localValue4 - localValue32 - 5.0F;
      float localValue34 = localValue3 + (localValue5 - localValue31) / 2.0F;
      boolean localValue35 = UiUtils.internalMethod05786(localValue33, localValue34, localValue32, localValue31, localValue15, localValue16);
      AnimatedValue localValue36 = localValue18.computeIfAbsent(localValue6, localValue0 -> new AnimatedValue(200L, 0.0F, Easing.internalField1626));
      localValue36.internalMethod07062(localValue35 || localValue29);
      float localValue37 = (0.6F + 0.4F * localValue36.internalMethod02881()) * localValue13.internalMethod02881() * localValue11 * localValue12;
      localValue1.drawRoundedRect(localValue33, localValue34, localValue32, localValue31, CornerRadii.internalMethod03908(4.0F), ThemeColors.internalMethod08573().mulAlpha(localValue37));
      localValue1.drawText(
         localValue9,
         localValue30,
         localValue33 + (localValue32 - localValue9.internalMethod00965(localValue30)) / 2.0F,
         localValue34 + (localValue31 - localValue9.internalMethod04890()) / 2.0F,
         ThemeColors.internalMethod08459().mulAlpha(0.95F * localValue13.internalMethod02881() * localValue11 * localValue12)
      );
      float localValue38 = localValue24 + localValue21 + 2.0F;
      float localValue39 = localValue33 - 4.0F - localValue38;
      this.internalMethod05247(
         localValue1,
         localValue17,
         localValue6,
         localValue8,
         LanguageManager.internalMethod07214(localValue6.internalMethod06026()),
         localValue38,
         localValue3 + (localValue5 - localValue8.internalMethod04890()) / 2.0F,
         localValue39,
         localValue5,
         ThemeColors.internalMethod08459().mulAlpha((0.75F + 0.25F * localValue7) * localValue13.internalMethod02881() * localValue11 * localValue12),
         localValue7 > 0.05F
      );
      HudRenderUtils.internalMethod00012(localValue1.getMatrices());
   }

   private void internalMethod05247(
      UiRenderContext localValue1,
      Map<AssistSwapItem, ItemSlotAnimation> localValue2,
      AssistSwapItem localValue3,
      SizedFont localValue4,
      String localValue5,
      float localValue6,
      float localValue7,
      float localValue8,
      float localValue9,
      ColorRGBA localValue10,
      boolean localValue11
   ) {
      float localValue12 = Math.max(1.0F, localValue8);
      float localValue13 = localValue4.internalMethod00965(localValue5);
      long localValue14 = System.currentTimeMillis();
      ItemSlotAnimation localValue16 = localValue2.computeIfAbsent(localValue3, localValue0 -> new ItemSlotAnimation());
      float localValue17 = Math.max(0.0F, localValue13 - localValue12);
      float localValue18 = (float)(localValue14 - localValue16.internalField0229) / 1000.0F;
      localValue16.internalField0229 = localValue14;
      if (localValue17 <= 0.0F) {
         localValue16.internalMethod03475(localValue14);
      } else if (localValue11) {
         localValue16.internalField0205 = Math.min(localValue16.internalField0205, localValue17);
         if (localValue14 >= localValue16.internalField0230) {
            float localValue19 = localValue18 * 35.0F;
            if (localValue16.internalField0277) {
               localValue16.internalField0205 = Math.min(localValue16.internalField0205 + localValue19, localValue17);
               if (localValue16.internalField0205 >= localValue17) {
                  localValue16.internalField0277 = false;
                  localValue16.internalField0230 = localValue14 + 600L;
               }
            } else {
               localValue16.internalField0205 = Math.max(localValue16.internalField0205 - localValue19, 0.0F);
               if (localValue16.internalField0205 <= 0.0F) {
                  localValue16.internalField0277 = true;
                  localValue16.internalField0230 = localValue14 + 600L;
               }
            }
         }
      } else if (localValue16.internalField0205 > 0.0F) {
         localValue16.internalField0205 = Math.max(0.0F, localValue16.internalField0205 - localValue18 * 35.0F);
         if (localValue16.internalField0205 == 0.0F) {
            localValue16.internalField0277 = true;
            localValue16.internalField0230 = localValue14;
         }
      }

      ScissorStack.internalMethod06303(localValue1.getMatrices(), localValue6 - 3.0F, localValue7 - 3.0F, localValue12 + 6.0F, localValue9 + 6.0F);
      localValue1.pushMatrix();
      localValue1.getMatrices().translate(-localValue16.internalField0205, 0.0F);
      localValue1.drawFadeoutText(localValue4, localValue5, localValue6, localValue7, localValue10, 0.95F, 1.0F, localValue12 + 10.0F);
      localValue1.popMatrix();
      ScissorStack.internalMethod07643();
   }

   private float internalMethod03504(AssistItemCategory localValue1, List<AssistSwapItem> localValue2, SizedFont localValue3, float localValue4, float localValue5) {
      float localValue6 = 0.0F;
      if (localValue1 == AssistItemCategory.internalField0459) {
         for (AssistItemCategory localValue10 : AssistItemCategory.values()) {
            if (localValue10 != AssistItemCategory.internalField0459) {
               List localValue11 = localValue2.stream().filter(localValue1x -> localValue1x.internalMethod06886() == localValue10).toList();
               if (!localValue11.isEmpty()) {
                  localValue6 += localValue3.internalMethod04890() + 8.0F;
                  int localValue12 = (int)Math.ceil(localValue11.size() / 2.0F);
                  localValue6 += localValue12 * localValue4 + Math.max(0, localValue12 - 1) * localValue5 + 14.0F;
               }
            }
         }
      } else {
         List localValue14 = localValue2.stream().filter(localValue1x -> localValue1x.internalMethod06886() == localValue1).toList();
         int localValue15 = (int)Math.ceil(localValue14.size() / 2.0F);
         localValue6 += localValue15 * localValue4 + Math.max(0, localValue15 - 1) * localValue5;
      }

      return localValue6;
   }

   public static final class InternalType0450 {
      private final float internalField0205;
      private final float internalField0206;
      private final float internalField1048;
      private final float internalField1047;

      public InternalType0450(float localValue1, float localValue2, float localValue3, float localValue4) {
         this.internalField0205 = localValue1;
         this.internalField0206 = localValue2;
         this.internalField1048 = localValue3;
         this.internalField1047 = localValue4;
      }

      @Override
      public final String toString() {
         return "InternalType0450[x=" + this.internalField0205 + ", y=" + this.internalField0206 + ", w=" + this.internalField1048 + ", h=" + this.internalField1047 + "]";
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
         MacroPickerRenderer.InternalType0450 other = (MacroPickerRenderer.InternalType0450) localValue1;
         return java.util.Objects.equals(this.internalField0205, other.internalField0205)
            && java.util.Objects.equals(this.internalField0206, other.internalField0206)
            && java.util.Objects.equals(this.internalField1048, other.internalField1048)
            && java.util.Objects.equals(this.internalField1047, other.internalField1047);
      }

      public float internalMethod03446() {
         return this.internalField0205;
      }

      public float internalMethod03467() {
         return this.internalField0206;
      }

      public float internalMethod09069() {
         return this.internalField1048;
      }

      public float internalMethod09071() {
         return this.internalField1047;
      }
   }
}
