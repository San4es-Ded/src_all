package rockstar.client.internal.script;







import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.render.*;
import rockstar.client.i18n.*;
import rockstar.client.*;
import rockstar.modules.combat.*;
import rockstar.modules.movement.*;
import rockstar.modules.visual.*;
import rockstar.modules.player.*;
import rockstar.modules.other.*;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import pyrock.utility.render.ColorRGBA;

public class ModuleStatusHud extends DynamicIslandStatus implements MinecraftClientAccess {
   private static final ColorRGBA internalField0777 = new ColorRGBA(34.0F, 187.0F, 94.0F);
   private static final ColorRGBA internalField0776 = new ColorRGBA(239.0F, 68.0F, 68.0F);
   private static final ColorRGBA internalField1311 = ThemeColors.internalField1309.mix(ThemeColors.internalField0777, 0.5F);
   private static final ColorRGBA internalField1312 = new ColorRGBA(228.0F, 52.0F, 52.0F);
   private ModuleStatusHud.InternalType0506 internalField0491;
   private int internalField0227;
   private ColorRGBA internalField1309 = internalField0777;
   private int internalField0228;
   private int internalField1053;
   private float internalField0205 = 1.0F;
   private UiNode internalField0633;
   private UiNode internalField0632;
   private UiNode internalField1253;
   private UiNode internalField1252;

   public ModuleStatusHud(MultiSelectSetting localValue1) {
      super(localValue1, "modules");
   }

   @Override
   public void prepare(DynamicIslandHud localValue1) {
      this.internalField0491 = this.internalMethod03717();
      if (this.internalField0491 != null) {
         switch (this.internalField0491) {
            case internalField0491:
               this.internalMethod08900();
               break;
            case internalField0490:
               this.internalMethod03114();
               break;
            case internalField1180:
               this.internalMethod03119();
               break;
            case internalField1179:
               this.internalMethod08898();
         }
      }
   }

   @Override
   public UiNode content(DynamicIslandHud localValue1) {
      if (this.internalField0491 == null) {
         return null;
      } else {
         return switch (this.internalField0491) {
            case internalField0491 -> this.internalMethod08736();
            case internalField0490 -> this.internalMethod01071();
            case internalField1180 -> this.internalMethod05177();
            case internalField1179 -> this.internalMethod07947();
         };
      }
   }

   @Override
   public boolean canShow() {
      return this.internalMethod03717() != null;
   }

   private void internalMethod03114() {
      FreeCameraModule localValue1 = this.internalMethod02873();
      Vec3d localValue2 = localValue1.internalMethod04686();
      Vec3d localValue3 = localValue1.internalMethod01774();
      this.internalField0227 = (int)localValue2.y - (int)localValue3.y;
      float localValue4 = MathHelper.clamp(Math.abs(this.internalField0227) / 35.0F, 0.0F, 1.0F);
      this.internalField1309 = internalField0777.mix(internalField0776, localValue4);
   }

   private void internalMethod03119() {
   }

   private void internalMethod08898() {
   }

   private void internalMethod08900() {
      AuraDatasetRecorder localValue1 = this.internalMethod02916();
      int localValue2 = localValue1 == null ? 0 : localValue1.internalMethod08258() / 20;
      this.internalField0228 = localValue2 / 60;
      this.internalField1053 = localValue2 % 60;
      this.internalField0205 = 0.8F + 0.2F * (float)Math.sin(System.currentTimeMillis() / 260.0);
   }

   private UiNode internalMethod01071() {
      if (this.internalField0633 == null) {
         UiContainer localValue1 = UiElementFactory.internalMethod01948(15.0F, Insets.internalMethod00105(0.0F, 4.5F, 0.0F, 6.0F), 2.0F)
            .internalMethod01855(TextAlignment.internalField0621);
         UiContainer localValue2 = UiElementFactory.internalMethod04233(() -> -14.0F * (1.0F - this.animation.internalMethod02881()))
            .internalMethod05895()
            .internalMethod09266(8.0F)
            .internalMethod03514(Insets.internalMethod00105(0.0F, 2.5F, 0.0F, 3.0F))
            .internalMethod01855(TextAlignment.internalField0621)
            .internalMethod07178(
               (localValue1x, localValue2x) -> localValue1x.drawRoundedRect(
                  localValue2x.x(),
                  localValue2x.y(),
                  localValue2x.w(),
                  localValue2x.h(),
                  CornerRadii.internalMethod03908(3.0F),
                  this.internalField1309.withAlpha(255.0F * this.animation.internalMethod02881())
               )
            );
         localValue2.internalMethod03907(
            UiElementFactory.internalMethod03257(
               Fonts.internalField0449.internalMethod01432(6.0F),
               () -> this.internalField0227 < 0 ? "-" : "",
               () -> ThemeColors.internalField1312.withAlpha(255.0F * this.animation.internalMethod02881())
            )
         );
         localValue2.internalMethod03907(
            new AnimatedNumberLabel(Fonts.internalField0449.internalMethod01432(6.0F), () -> Math.abs(this.internalField0227))
               .internalMethod02983(5.0F)
               .internalMethod00089()
               .internalMethod05034(() -> ThemeColors.internalField1312.withAlpha(255.0F * this.animation.internalMethod02881()))
               .interactive(false)
         );
         localValue1.internalMethod03907(
            UiElementFactory.internalMethod04195(
               Fonts.internalField0449.internalMethod01432(7.0F),
               () -> LanguageManager.internalMethod07214("hud.dynamic_island.modules.freecam_height"),
               () -> ThemeColors.internalMethod08459().withAlpha(255.0F * this.animation.internalMethod02881()),
               () -> -6.0F * (1.0F - this.animation.internalMethod02881())
            )
         );
         localValue1.internalMethod03907(localValue2);
         this.internalField0633 = localValue1;
      }

      return this.internalField0633;
   }

   private UiNode internalMethod05177() {
      if (this.internalField0632 == null) {
         UiContainer localValue1 = UiElementFactory.internalMethod01948(15.0F, Insets.internalMethod00105(0.0F, 5.0F, 0.0F, 6.0F), 3.0F)
            .internalMethod09339(80.0F)
            .internalMethod01855(TextAlignment.internalField0621);
         localValue1.internalMethod03907(
            UiElementFactory.internalMethod04195(
               Fonts.internalField0449.internalMethod01432(7.0F),
               () -> "Blink",
               () -> ThemeColors.internalMethod08459().withAlpha(255.0F * this.animation.internalMethod02881()),
               () -> -10.0F * (1.0F - this.animation.internalMethod02881())
            )
         );
         localValue1.internalMethod03907(new ModuleStatusHud.InternalType0507().fillWidth());
         this.internalField0632 = localValue1;
      }

      return this.internalField0632;
   }

   private UiNode internalMethod07947() {
      if (this.internalField1253 == null) {
         UiContainer localValue1 = UiElementFactory.internalMethod01948(15.0F, Insets.internalMethod00105(0.0F, 5.0F, 0.0F, 4.0F), 4.0F)
            .internalMethod01855(TextAlignment.internalField0621);
         localValue1.internalMethod03907(
            UiElementFactory.internalMethod00122(
               7.0F,
               7.0F,
               (localValue1x, localValue2, localValue3) -> {
                  float localValue4 = this.animation.internalMethod02881();
                  ColorRGBA localValue5 = internalField1311.mix(ThemeColors.internalField1312, 0.25F);
                  ColorRGBA localValue6 = internalField1311.mix(ThemeColors.internalField1312, 0.5F);
                  localValue1x.drawRoundedRect(
                     localValue2.x() - 10.0F * (1.0F - localValue4),
                     localValue2.y(),
                     localValue2.w(),
                     localValue2.h(),
                     CornerRadii.internalMethod03908(3.0F),
                     new QuadColorGradient(internalField1311, localValue6, localValue6, localValue5)
                  );
               }
            )
         );
         localValue1.internalMethod03907(
            UiElementFactory.internalMethod04195(
               Fonts.internalField0449.internalMethod01432(7.0F),
               () -> LanguageManager.internalMethod07214("modules.settings.auto_swap.auto_cerber"),
               () -> ThemeColors.internalMethod08459().withAlpha(255.0F * this.animation.internalMethod02881()),
               () -> 10.0F * (1.0F - this.animation.internalMethod02881())
            )
         );
         this.internalField1253 = localValue1;
      }

      return this.internalField1253;
   }

   private UiNode internalMethod08736() {
      if (this.internalField1252 == null) {
         UiContainer localValue1 = UiElementFactory.internalMethod01948(15.0F, Insets.internalMethod00105(0.0F, 4.0F, 0.0F, 4.0F), 3.5F)
            .internalMethod01855(TextAlignment.internalField0621);
         UiContainer localValue2 = UiElementFactory.internalMethod04233(() -> -20.0F * (1.0F - this.animation.internalMethod02881()))
            .internalMethod05895()
            .internalMethod09266(8.0F)
            .internalMethod03514(Insets.internalMethod00105(0.0F, 2.5F, 0.0F, 3.0F))
            .internalMethod01855(TextAlignment.internalField0621)
            .internalMethod07178(
               (localValue1x, localValue2x) -> localValue1x.drawRoundedRect(
                  localValue2x.x(),
                  localValue2x.y(),
                  localValue2x.w(),
                  localValue2x.h(),
                  CornerRadii.internalMethod03908(3.0F),
                  internalField1312.withAlpha(255.0F * this.animation.internalMethod02881() * this.internalField0205)
               )
            );
         localValue2.internalMethod03907(
            UiElementFactory.internalMethod03257(
               Fonts.internalField0449.internalMethod01432(6.0F),
               () -> this.internalField0228 + ":",
               () -> ThemeColors.internalMethod01303(internalField1312).withAlpha(255.0F * this.animation.internalMethod02881())
            )
         );
         localValue2.internalMethod03907(
            new AnimatedNumberLabel(Fonts.internalField0449.internalMethod01432(6.0F), () -> this.internalField1053)
               .internalMethod02983(5.0F)
               .internalMethod00089()
               .internalMethod05034(() -> ThemeColors.internalMethod01303(internalField1312).withAlpha(255.0F * this.animation.internalMethod02881()))
               .interactive(false)
         );
         localValue1.internalMethod03907(localValue2);
         localValue1.internalMethod03907(
            UiElementFactory.internalMethod04195(
               Fonts.internalField0449.internalMethod01432(7.0F),
               () -> LanguageManager.internalMethod07214("hud.dynamic_island.modules.neuro_record"),
               () -> ThemeColors.internalMethod08459().withAlpha(255.0F * this.animation.internalMethod02881()),
               () -> 10.0F * (1.0F - this.animation.internalMethod02881())
            )
         );
         this.internalField1252 = localValue1;
      }

      return this.internalField1252;
   }

   private ModuleStatusHud.InternalType0506 internalMethod03717() {
      if (this.internalMethod08901()) {
         return ModuleStatusHud.InternalType0506.internalField0491;
      } else if (this.internalMethod03115()) {
         return ModuleStatusHud.InternalType0506.internalField0490;
      } else if (this.internalMethod03120()) {
         return ModuleStatusHud.InternalType0506.internalField1180;
      } else {
         return this.internalMethod08899() ? ModuleStatusHud.InternalType0506.internalField1179 : null;
      }
   }

   private boolean internalMethod03115() {
      return internalField0149.player != null && this.internalMethod02873().isEnabled();
   }

   private boolean internalMethod03120() {
      return this.internalMethod02869().isEnabled() && GameUtils.internalMethod00471();
   }

   private boolean internalMethod08899() {
      return this.internalMethod02868().internalMethod09277() && GameUtils.internalMethod00471();
   }

   private boolean internalMethod08901() {
      AuraDatasetRecorder localValue1 = this.internalMethod02916();
      return localValue1 != null && localValue1.internalMethod05629() && GameUtils.internalMethod00471();
   }

   private AuraDatasetRecorder internalMethod02916() {
      AuraModule localValue1 = RockstarClient.getInstance().getModuleManager().getModule(AuraModule.class);
      return localValue1 != null && localValue1.internalMethod05950() != null ? localValue1.internalMethod05950().internalMethod03449() : null;
   }

   private FreeCameraModule internalMethod02873() {
      return RockstarClient.getInstance().getModuleManager().getModule(FreeCameraModule.class);
   }

   public BlinkModule internalMethod02869() {
      return RockstarClient.getInstance().getModuleManager().getModule(BlinkModule.class);
   }

   private AutoSwapModule internalMethod02868() {
      return RockstarClient.getInstance().getModuleManager().getModule(AutoSwapModule.class);
   }

   static enum InternalType0506 {
      internalField0491,
      internalField0490,
      internalField1180,
      internalField1179;
   }

   final class InternalType0507 extends UiNode {
      InternalType0507() {
         this.height(6.0F);
         this.interactive(false);
      }

      @Override
      protected void measure() {
         this.prefW = 0.0F;
         this.prefH = 6.0F;
      }

      @Override
      protected void drawSelf(UiRenderContext localValue1, float localValue2) {
         BlinkModule localValue3 = ModuleStatusHud.this.internalMethod02869();
         SizedFont localValue4 = Fonts.internalField0449.internalMethod01432(7.0F);
         float localValue5 = ModuleStatusHud.this.animation.internalMethod02881();
         if (!localValue3.internalMethod03674().internalMethod04496()) {
            String localValue7 = TextUtils.internalMethod07254((float)localValue3.internalMethod02640().internalMethod00700() / 1000.0F)
               + " "
               + LanguageManager.internalMethod07214("sec");
            localValue1.drawRightText(
               localValue4,
               localValue7,
               this.x() + this.w() - 1.0F,
               this.y() + this.h() / 2.0F - localValue4.internalMethod04890() / 2.0F,
               ThemeColors.internalMethod08459().withAlpha(255.0F * localValue5)
            );
         } else {
            float localValue6 = this.w()
               * (
                  (localValue3.internalMethod01477().internalMethod08576() * 50.0F - (float)localValue3.internalMethod02640().internalMethod00700())
                     / (localValue3.internalMethod01477().internalMethod08576() * 50.0F)
               );
            localValue1.drawRoundedRect(
               this.x(), this.y(), this.w(), this.h(), CornerRadii.internalMethod03908(2.5F), ThemeColors.internalMethod08573().withAlpha(255.0F * localValue5)
            );
            localValue1.drawRoundedRect(
               this.x() + this.w() - localValue6,
               this.y(),
               localValue6,
               this.h(),
               CornerRadii.internalMethod03908(2.5F),
               ThemeColors.internalMethod02531().withAlpha(255.0F * localValue5)
            );
         }
      }
   }
}
