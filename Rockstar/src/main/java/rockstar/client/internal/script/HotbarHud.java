package rockstar.client.internal.script;









import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.core.*;
import rockstar.client.animation.*;
import rockstar.client.internal.ui.*;
import rockstar.client.internal.render.*;
import rockstar.client.*;
import rockstar.modules.combat.*;
import rockstar.modules.movement.*;
import rockstar.modules.visual.*;
import rockstar.modules.player.*;
import rockstar.modules.other.*;

import rockstar.client.compat.RenderSystem;
import lombok.Generated;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;
import pyrock.utility.render.ColorRGBA;
import pyrock.utility.render.CustomDrawContext;
import rockstar.client.render.compat.ImmediateRenderer;

public class HotbarHud extends AbstractHudWidget implements MinecraftClientAccess, ScreenMetricsAccess {
   private static final Identifier internalField0354 = Identifier.ofVanilla("hud/air");
   private static final Identifier internalField0355 = Identifier.ofVanilla("hud/air_bursting");
   private static final Identifier internalField1133 = Identifier.ofVanilla("hud/air_empty");
   private final Random internalField0721 = Random.create();
   private int internalField0227;
   private long internalField0229 = -1L;
   private int internalField0228;
   private int internalField1053;
   private long internalField0230;
   private long internalField1059;
   private int internalField1055;
   private final AnimatedValue[] internalField0556 = new AnimatedValue[40];
   private final int[] internalField0618 = new int[40];
   private final AnimatedValue[] internalField0557 = new AnimatedValue[9];
   private final AnimatedValue[] internalField1204 = new AnimatedValue[9];
   private static final float internalField0205 = 29.0F;

   public HotbarHud() {
      super("hud.custom_hotbar", "hud/hotbar");
      this.y = -1.0F;

      for (int localValue1 = 0; localValue1 < this.internalField0557.length; localValue1++) {
         this.internalField0557[localValue1] = new AnimatedValue(300L, Easing.internalField0812);
         this.internalField1204[localValue1] = new AnimatedValue(300L, Easing.internalField0812);
      }

      for (int localValue2 = 0; localValue2 < this.internalField0556.length; localValue2++) {
         this.internalField0556[localValue2] = new AnimatedValue(700L, 0.0F, Easing.internalField1630);
      }
   }

   @Override
   public boolean anchorsRightEdge() {
      return false;
   }

   @Override
   public void update(UiRenderContext localValue1) {
      boolean localValue2 = !this.internalMethod06382();
      if (this.y < 0.0F) {
         this.y = internalField0389.internalMethod03589() - 29.0F;
      }

      if (localValue2 && !this.isDragging()) {
         this.internalMethod06381();
      }

      super.update(localValue1);
      if (this.isDragging()) {
         this.x = internalField0389.internalMethod03585() / 2.0F - (localValue2 ? 91 : 100);
         this.y = Math.min(this.y, internalField0389.internalMethod03589() - 29.0F);
      }

      if (localValue2 && this.isHovered(localValue1)) {
         CursorManager.internalMethod06882(CursorType.internalField0567);
      }
   }

   @Override
   public void pos(float localValue1, float localValue2) {
      super.pos(localValue1, localValue2);
      if (!this.isShowing()) {
         this.y = -1.0F;
      }
   }

   @Override
   public void onMouseClicked(double localValue1, double localValue3, MouseButton localValue5) {
      if (this.internalMethod06382()) {
         super.onMouseClicked(localValue1, localValue3, localValue5);
      } else {
         if (localValue5 == MouseButton.internalField0102 && this.isHovered(localValue1, localValue3)) {
            this.beginDrag(localValue1, localValue3);
         }
      }
   }

   private boolean internalMethod06382() {
      return this.isShowing() && this.show();
   }

   private void internalMethod06381() {
      this.width = 182.0F;
      this.height = 29.0F;
      this.x = internalField0389.internalMethod03585() / 2.0F - 91.0F;
   }

   public static float internalMethod06380() {
      HudManager localValue0 = RockstarClient.getInstance() == null ? null : RockstarClient.getInstance().internalMethod01271();
      HotbarHud localValue1 = localValue0 == null ? null : localValue0.internalMethod01440();
      return localValue1 != null && localValue1.isShowing() && localValue1.show() && !(localValue1.y < 0.0F)
         ? internalField0389.internalMethod03589() - 29.0F - localValue1.y
         : 0.0F;
   }

   @Override
   public void renderComponent(UiRenderContext localValue1) {
      this.width = 200.0F;
      this.height = 27.0F;
      ItemStack localValue2 = internalField0149.player.getOffHandStack();
      Arm localValue3 = internalField0149.player.getMainArm().getOpposite();
      boolean localValue4 = false;
      boolean localValue5 = false;
      if (!localValue2.isEmpty()) {
         if (localValue3 == Arm.LEFT) {
            localValue4 = true;
         } else {
            localValue5 = true;
         }
      }

      this.x = internalField0389.internalMethod03585() / 2.0F - 100.0F;
      if (internalField0149.player != null) {
         if (internalField0149.world != null) {
            long localValue6 = internalField0149.world.getTime();
            if (localValue6 != this.internalField0229) {
               this.internalField0229 = localValue6;
               this.internalField0227++;
            }
         }

         int localValue25 = (int)this.x;
         int localValue7 = (int)this.y;
         int localValue8 = internalField0149.player.getInventory().getSelectedSlot();
         if (internalField0149.interactionManager.hasStatusBars()) {
            this.internalMethod04473(localValue1);
         }

         int localValue9 = (int)(internalField0389.internalMethod03585() / 2.0F - 91.0F);
         this.internalMethod03311(localValue1, internalField0149.getRenderTickCounter());
         localValue1.drawClientRect(localValue25 - (localValue4 ? 30.0F : 0.0F), localValue7, !localValue5 && !localValue4 ? 200.0F : 230.0F, 27.0F, 1.0F, 0.0F, 7.0F, 8.0F);
         float localValue10 = RenderSystem.getShaderColor()[3];
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         HotbarHud.InternalType0286[] localValue11 = new HotbarHud.InternalType0286[9];

         try (
            SmoothItemLayerHelper.InternalType0310 localValue12 = SmoothItemLayerHelper.internalMethod04678();
            CustomDrawContext.InternalType0486 localValue13 = localValue1.beginItemBatch();
         ) {
            for (int localValue14 = 0; localValue14 < 9; localValue14++) {
               AnimatedValue localValue15 = this.internalField0557[localValue14];
               AnimatedValue localValue16 = this.internalField1204[localValue14];
               localValue15.internalMethod07062(localValue8 == localValue14);
               localValue16.internalMethod07062(Math.abs(localValue8 - localValue14) <= 1);
               ItemStack localValue17 = (ItemStack)internalField0149.player.getInventory().getMainStacks().get(localValue14);
               float localValue18 = 0.1F * localValue16.internalMethod02881() + 0.25F * localValue15.internalMethod02881();
               float localValue19 = localValue25 + 6 + localValue14 * 21.5F;
               float localValue20 = localValue7 + 5.5F - 12.0F * localValue18;
               localValue11[localValue14] = new HotbarHud.InternalType0286(localValue17, localValue19, localValue20, localValue18);
               if (!localValue17.isEmpty()) {
                  HudRenderUtils.internalMethod08976(localValue1.getMatrices(), localValue19 + 8.0F, localValue20 + 8.0F, 1.0F + localValue18);
                  localValue1.drawBatchItem(localValue17, localValue19, localValue20, 1);
                  HudRenderUtils.internalMethod00012(localValue1.getMatrices());
               }
            }

            if (localValue4) {
               float localValue27 = localValue25 + 6 - 30.0F;
               float localValue29 = localValue7 + 5.5F;
               localValue1.drawBatchItem(localValue2, localValue27, localValue29, 1);
            } else if (localValue5) {
               float localValue28 = localValue25 + 6 + 172.0F + 30.0F;
               float localValue30 = localValue7 + 5.5F;
               localValue1.drawBatchItem(localValue2, localValue28, localValue30, 1);
            }
         }

         HotbarHud.InternalType0286 localValue26 = null;
         if (localValue4) {
            localValue26 = new HotbarHud.InternalType0286(localValue2, localValue25 + 6 - 30.0F, localValue7 + 5.5F, 0.0F);
         } else if (localValue5) {
            localValue26 = new HotbarHud.InternalType0286(localValue2, localValue25 + 6 + 172.0F + 30.0F, localValue7 + 5.5F, 0.0F);
         }

         this.internalMethod07489(localValue1, localValue11);
         Matrix3x2f localValue31 = new Matrix3x2f(localValue1.getMatrices());
         HotbarHud.InternalType0286 localValue32 = localValue26;
         ImmediateRenderer.deferGuiOverlay(() -> {
            localValue1.getMatrices().pushMatrix();
            try {
               localValue1.getMatrices().set(localValue31);
               this.internalMethod02960(localValue1, localValue11, localValue32);
               this.internalMethod04474(localValue1, localValue11, localValue32);
               this.internalMethod08096(localValue1, localValue11, localValue32);
            } finally {
               localValue1.getMatrices().popMatrix();
            }
         });
         this.internalMethod03055(localValue1, localValue11);
         if (localValue4 && localValue26 != null) {
            localValue1.drawRoundedRect(
               localValue26.internalMethod01040() + 22.5F,
               localValue26.internalMethod01044(),
               0.5F,
               16.0F,
               CornerRadii.internalField0098,
               ThemeColors.internalMethod08459().mulAlpha(0.5F)
            );
         } else if (localValue5 && localValue26 != null) {
            localValue1.drawRoundedRect(
               localValue26.internalMethod01040() - 7.5F,
               localValue26.internalMethod01044(),
               0.5F,
               16.0F,
               CornerRadii.internalField0098,
               ThemeColors.internalMethod08459().mulAlpha(0.5F)
            );
         }

         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, localValue10);
      }
   }

   private void internalMethod07489(CustomDrawContext localValue1, HotbarHud.InternalType0286[] localValue2) {
      boolean localValue3 = false;

      for (HotbarHud.InternalType0286 localValue7 : localValue2) {
         if (localValue7.internalMethod02530().isEmpty()) {
            localValue3 = true;
            break;
         }
      }

      if (localValue3) {
         PlainGlyphBatch localValue9 = new PlainGlyphBatch(VertexFormats.POSITION_TEXTURE_COLOR, Fonts.internalField1156);

         for (int localValue10 = 0; localValue10 < localValue2.length; localValue10++) {
            HotbarHud.InternalType0286 localValue11 = localValue2[localValue10];
            if (localValue11.internalMethod02530().isEmpty()) {
               float localValue12 = 1.0F + localValue11.internalMethod08617();
               String localValue8 = String.valueOf(localValue10 + 1);
               HudRenderUtils.internalMethod08976(localValue1.getMatrices(), localValue11.internalMethod08618(), localValue11.internalMethod08631(), localValue12);
               localValue1.drawText(
                  Fonts.internalField1156.internalMethod01432(10.0F),
                  localValue8,
                  localValue11.internalMethod01040() + 8.5F - Fonts.internalField1156.internalMethod01432(10.0F).internalMethod00965(localValue8) / 2.0F,
                  localValue11.internalMethod01044() + 4.5F,
                  ThemeColors.internalMethod08459().mulAlpha(0.5F + localValue11.internalMethod08617() * 1.5F)
               );
               HudRenderUtils.internalMethod00012(localValue1.getMatrices());
            }
         }

         localValue9.internalMethod09053();
      }
   }

   private void internalMethod02960(CustomDrawContext localValue1, HotbarHud.InternalType0286[] localValue2, @Nullable HotbarHud.InternalType0286 localValue3) {
      for (HotbarHud.InternalType0286 localValue7 : localValue2) {
         if (!localValue7.internalMethod02530().isEmpty()) {
            HudRenderUtils.internalMethod08976(localValue1.getMatrices(), localValue7.internalMethod08618(), localValue7.internalMethod08631(), 1.0F + localValue7.internalMethod08617());
            this.internalMethod01355(localValue1, localValue7.internalMethod02530(), localValue7.internalMethod01040(), localValue7.internalMethod01044());
            HudRenderUtils.internalMethod00012(localValue1.getMatrices());
         }
      }

      if (localValue3 != null) {
         this.internalMethod01355(localValue1, localValue3.internalMethod02530(), localValue3.internalMethod01040(), localValue3.internalMethod01044());
      }
   }

   private void internalMethod04474(CustomDrawContext localValue1, HotbarHud.InternalType0286[] localValue2, @Nullable HotbarHud.InternalType0286 localValue3) {
      boolean localValue4 = false;

      for (HotbarHud.InternalType0286 localValue8 : localValue2) {
         if (this.internalMethod05706(localValue8.internalMethod02530())) {
            localValue4 = true;
            break;
         }
      }

      if (localValue4 || localValue3 != null && this.internalMethod05706(localValue3.internalMethod02530())) {
         PlainGlyphBatch localValue10 = new PlainGlyphBatch(VertexFormats.POSITION_TEXTURE_COLOR, Fonts.internalField1156);

         for (HotbarHud.InternalType0286 localValue9 : localValue2) {
            if (this.internalMethod05706(localValue9.internalMethod02530())) {
               HudRenderUtils.internalMethod08976(localValue1.getMatrices(), localValue9.internalMethod08618(), localValue9.internalMethod08631(), 1.0F + localValue9.internalMethod08617());
               this.internalMethod08298(localValue1, localValue9.internalMethod02530(), localValue9.internalMethod01040(), localValue9.internalMethod01044());
               HudRenderUtils.internalMethod00012(localValue1.getMatrices());
            }
         }

         if (localValue3 != null && this.internalMethod05706(localValue3.internalMethod02530())) {
            this.internalMethod08298(localValue1, localValue3.internalMethod02530(), localValue3.internalMethod01040(), localValue3.internalMethod01044());
         }

         localValue10.internalMethod09053();
      }
   }

   private void internalMethod08096(CustomDrawContext localValue1, HotbarHud.InternalType0286[] localValue2, @Nullable HotbarHud.InternalType0286 localValue3) {
      for (HotbarHud.InternalType0286 localValue7 : localValue2) {
         if (!localValue7.internalMethod02530().isEmpty()) {
            HudRenderUtils.internalMethod08976(localValue1.getMatrices(), localValue7.internalMethod08618(), localValue7.internalMethod08631(), 1.0F + localValue7.internalMethod08617());
            this.internalMethod08435(localValue1, localValue7.internalMethod02530(), localValue7.internalMethod01040(), localValue7.internalMethod01044());
            HudRenderUtils.internalMethod00012(localValue1.getMatrices());
         }
      }

      if (localValue3 != null) {
         this.internalMethod08435(localValue1, localValue3.internalMethod02530(), localValue3.internalMethod01040(), localValue3.internalMethod01044());
      }
   }

   private void internalMethod03055(CustomDrawContext localValue1, HotbarHud.InternalType0286[] localValue2) {
      boolean localValue3 = false;

      for (AnimatedValue localValue7 : this.internalField0557) {
         if (localValue7.internalMethod02881() > 0.001F) {
            localValue3 = true;
            break;
         }
      }

      if (localValue3) {
         RoundedRectBatch localValue8 = new RoundedRectBatch();

         for (int localValue9 = 0; localValue9 < localValue2.length; localValue9++) {
            HotbarHud.InternalType0286 localValue10 = localValue2[localValue9];
            float localValue11 = this.internalField0557[localValue9].internalMethod02881();
            localValue1.drawRoundedRect(
               localValue10.internalMethod01040() + 7.5F,
               localValue10.internalMethod01044() + 21.0F - localValue11,
               2.0F,
               2.0F,
               CornerRadii.internalMethod03908(0.5F),
               ThemeColors.internalMethod08459().mulAlpha(localValue11)
            );
         }

         localValue8.internalMethod09053();
      }
   }

   private boolean internalMethod05706(ItemStack localValue1) {
      return !localValue1.isEmpty() && localValue1.getCount() != 1;
   }

   public void internalMethod00493(CustomDrawContext localValue1, ItemStack localValue2, float localValue3, float localValue4) {
      if (!localValue2.isEmpty()) {
         localValue1.getMatrices().pushMatrix();
         this.internalMethod01355(localValue1, localValue2, localValue3, localValue4);
         this.internalMethod08298(localValue1, localValue2, localValue3, localValue4);
         this.internalMethod08435(localValue1, localValue2, localValue3, localValue4);
         localValue1.getMatrices().popMatrix();
      }
   }

   private void internalMethod01355(CustomDrawContext localValue1, ItemStack localValue2, float localValue3, float localValue4) {
      if (localValue2.isItemBarVisible()) {
         float localValue5 = localValue3 + 2.0F;
         float localValue6 = localValue4 + 13.0F;
         localValue1.drawRoundedRect(localValue5, localValue6, 13.0F, 1.5F, CornerRadii.internalMethod03908(0.25F), ColorRGBA.WHITE.mulAlpha(0.25F));
         localValue1.drawRoundedRect(localValue5, localValue6, (float)localValue2.getItemBarStep(), 1.5F, CornerRadii.internalMethod03908(0.25F), ThemeColors.internalMethod02531());
      }
   }

   private void internalMethod08298(CustomDrawContext localValue1, ItemStack localValue2, float localValue3, float localValue4) {
      if (localValue2.getCount() != 1) {
         String localValue5 = String.valueOf(localValue2.getCount());
         localValue1.getMatrices().pushMatrix();
         localValue1.getMatrices().translate(0.0F, 0.0F);
         localValue1.drawText(
            Fonts.internalField1156.internalMethod01432(8.0F),
            localValue5,
            localValue3 + 19.0F + 0.5F - 2.0F - Fonts.internalField1156.internalMethod01432(8.0F).internalMethod00965(localValue5),
            localValue4 + 6.0F + 3.5F,
            ThemeColors.internalField1309.mulAlpha(0.4F)
         );
         localValue1.drawText(
            Fonts.internalField1156.internalMethod01432(8.0F),
            localValue5,
            localValue3 + 19.0F - 2.0F - Fonts.internalField1156.internalMethod01432(8.0F).internalMethod00965(localValue5),
            localValue4 + 6.0F + 3.0F,
            ThemeColors.internalField1312
         );
         localValue1.getMatrices().popMatrix();
      }
   }

   private void internalMethod08435(CustomDrawContext localValue1, ItemStack localValue2, float localValue3, float localValue4) {
      ClientPlayerEntity localValue5 = internalField0149.player;
      float localValue6 = localValue5 == null ? 0.0F : localValue5.getItemCooldownManager().getCooldownProgress(localValue2, internalField0149.getRenderTickCounter().getTickProgress(true));
      if (localValue6 > 0.0F) {
         float localValue7 = localValue4 + MathHelper.floor(16.0F * (1.0F - localValue6));
         float localValue8 = MathHelper.ceil(16.0F * localValue6);
         localValue1.drawRoundedRect(localValue3, localValue7, 16.0F, localValue8, CornerRadii.internalMethod03908(0.5F), ColorRGBA.WHITE.mulAlpha(0.35F));
      }
   }

   private void internalMethod03311(CustomDrawContext localValue1, RenderTickCounter localValue2) {
      int localValue3 = internalField0149.player.experienceLevel;
      if (this.internalMethod06383() && localValue3 > 0) {
         String localValue4 = localValue3 + "";
         float localValue5 = (localValue1.getScaledWindowWidth() - Fonts.internalField1154.internalMethod01432(8.0F).internalMethod00965(localValue4)) / 2.0F;
         int localValue6 = (int)this.y - 8;
         PlainGlyphBatch localValue7 = new PlainGlyphBatch(VertexFormats.POSITION_TEXTURE_COLOR, Fonts.internalField1154);
         localValue1.drawText(Fonts.internalField1154.internalMethod01432(8.0F), localValue4, localValue5 + 1.0F, localValue6, ThemeColors.internalField1309.mulAlpha(0.5F));
         localValue1.drawText(Fonts.internalField1154.internalMethod01432(8.0F), localValue4, localValue5 - 1.0F, localValue6, ThemeColors.internalField1309.mulAlpha(0.5F));
         localValue1.drawText(Fonts.internalField1154.internalMethod01432(8.0F), localValue4, localValue5, localValue6 + 1, ThemeColors.internalField1309.mulAlpha(0.5F));
         localValue1.drawText(Fonts.internalField1154.internalMethod01432(8.0F), localValue4, localValue5, localValue6 - 1, ThemeColors.internalField1309.mulAlpha(0.5F));
         localValue1.drawText(Fonts.internalField1154.internalMethod01432(8.0F), localValue4, localValue5, localValue6, new ColorRGBA(126.0F, 252.0F, 32.0F));
         localValue7.internalMethod09053();
      }
   }

   private boolean internalMethod06383() {
      return internalField0149.player.getJumpingMount() == null && internalField0149.interactionManager.hasExperienceBar();
   }

   private void internalMethod04473(UiRenderContext localValue1) {
      PlayerEntity localValue2 = this.internalMethod03874();
      if (localValue2 != null) {
         int localValue3 = MathHelper.ceil(localValue2.getHealth());
         boolean localValue4 = this.internalField1059 > this.internalField0227 && (this.internalField1059 - this.internalField0227) / 3L % 2L == 1L;
         long localValue5 = Util.getMeasuringTimeMs();
         if (localValue3 < this.internalField0228 && localValue2.timeUntilRegen > 0) {
            this.internalField0230 = localValue5;
            this.internalField1059 = this.internalField0227 + 20;
         } else if (localValue3 > this.internalField0228 && localValue2.timeUntilRegen > 0) {
            this.internalField0230 = localValue5;
            this.internalField1059 = this.internalField0227 + 10;
         }

         if (localValue5 - this.internalField0230 > 1000L) {
            this.internalField1053 = localValue3;
            this.internalField0230 = localValue5;
         }

         if (localValue3 < this.internalField0228) {
            for (int localValue7 = 0; localValue7 < 40 && localValue7 * 2 < this.internalField0228; localValue7++) {
               int localValue8 = MathHelper.clamp(this.internalField0228 - localValue7 * 2, 0, 2);
               int localValue9 = MathHelper.clamp(localValue3 - localValue7 * 2, 0, 2);
               if (localValue8 > localValue9) {
                  this.internalField0618[localValue7] = localValue8 - localValue9 == 2 ? 3 : (localValue8 == 2 ? 2 : 1);
                  this.internalField0556[localValue7].internalMethod07060(1.0F);
                  this.internalField0556[localValue7].internalMethod07059(0.0F);
               }
            }
         } else if (localValue3 > this.internalField0228) {
            for (int localValue19 = 0; localValue19 < 40 && localValue19 * 2 < localValue3; localValue19++) {
               this.internalField0556[localValue19].internalMethod02883();
               this.internalField0618[localValue19] = 0;
            }
         }

         this.internalField0228 = localValue3;
         int localValue20 = this.internalField1053;
         this.internalField0721.setSeed(this.internalField0227 * 312871);
         int localValue21 = localValue1.getScaledWindowWidth() / 2 - 100;
         int localValue22 = localValue1.getScaledWindowWidth() / 2 + 100;
         int localValue10 = (int)this.y - 11;
         float localValue11 = Math.max((float)localValue2.getAttributeValue(EntityAttributes.MAX_HEALTH), (float)Math.max(localValue20, localValue3));
         int localValue12 = MathHelper.ceil(localValue2.getAbsorptionAmount());
         int localValue13 = MathHelper.ceil((localValue11 + localValue12) / 2.0F / 10.0F);
         int localValue14 = Math.max(10 - (localValue13 - 2), 3);
         int localValue15 = localValue10 - 10;
         int localValue16 = -1;
         if (localValue2.hasStatusEffect(StatusEffects.REGENERATION)) {
            localValue16 = this.internalField0227 % MathHelper.ceil(localValue11 + 5.0F);
         }

         internalMethod02071(localValue1, localValue2, localValue10, localValue13, localValue14, localValue21);
         this.internalMethod03714(localValue1, localValue2, localValue21, localValue10, localValue14, localValue16, localValue11, localValue3, localValue20, localValue12, localValue4);
         LivingEntity localValue17 = this.internalMethod04421();
         int localValue18 = this.internalMethod05438(localValue17);
         if (localValue18 == 0) {
            this.internalMethod00037(localValue1, localValue2, localValue10, localValue22);
            localValue15 -= 10;
         }

         this.internalMethod05073(localValue1, localValue2, localValue18, localValue15, localValue22);
      }
   }

   private static void internalMethod02071(UiRenderContext localValue0, PlayerEntity localValue1, int localValue2, int localValue3, int localValue4, int localValue5) {
      ColorRGBA localValue6 = new ColorRGBA(223.0F, 223.0F, 223.0F);
      int localValue7 = localValue1.getArmor();
      if (localValue7 > 0) {
         int localValue8 = localValue2 - (localValue3 - 1) * localValue4 - 10;

         for (int localValue9 = 0; localValue9 < 10; localValue9++) {
            int localValue10 = localValue5 + localValue9 * 9;
            if (localValue9 * 2 + 1 < localValue7) {
               localValue0.drawRoundedRect(localValue10, localValue8, 8.0F, 8.0F, CornerRadii.internalMethod03908(1.5F), localValue6);
            }

            if (localValue9 * 2 + 1 > localValue7) {
               localValue0.drawRoundedRect(
                  localValue10,
                  localValue8,
                  8.0F,
                  8.0F,
                  CornerRadii.internalMethod03908(1.5F),
                  ThemeColors.internalMethod07738()
                     .mulAlpha(
                        MathUtils.internalMethod02587(
                           ThemeColors.internalMethod02435().internalMethod08704(),
                           ThemeColors.internalMethod02435().internalMethod08705(),
                           InterfaceModule.internalMethod07584()
                        )
                     )
               );
            }
         }

         for (int localValue11 = 0; localValue11 < 10; localValue11++) {
            int localValue12 = localValue5 + localValue11 * 9;
            if (localValue11 * 2 + 1 == localValue7) {
               localValue0.drawRoundedRect(
                  localValue12 + 4,
                  localValue8,
                  4.0F,
                  8.0F,
                  new CornerRadii(0.0F, 1.5F, 1.5F, 0.0F),
                  ThemeColors.internalMethod07738()
                     .mulAlpha(
                        MathUtils.internalMethod02587(
                           ThemeColors.internalMethod02435().internalMethod08704(),
                           ThemeColors.internalMethod02435().internalMethod08705(),
                           InterfaceModule.internalMethod07584()
                        )
                     )
               );
               localValue0.drawRoundedRect(localValue12, localValue8, 4.0F, 8.0F, new CornerRadii(1.5F, 0.0F, 0.0F, 1.5F), localValue6);
            }
         }
      }
   }

   private void internalMethod03714(UiRenderContext localValue1, PlayerEntity localValue2, int localValue3, int localValue4, int localValue5, int localValue6, float localValue7, int localValue8, int localValue9, int localValue10, boolean localValue11) {
      HotbarHud.InternalType0285 localValue12 = HotbarHud.InternalType0285.internalMethod05872(localValue2);
      boolean localValue13 = localValue2.getEntityWorld().getLevelProperties().isHardcore();
      int localValue14 = MathHelper.ceil(localValue7 / 2.0);
      int localValue15 = MathHelper.ceil(localValue10 / 2.0);
      int localValue16 = localValue14 * 2;

      for (int localValue17 = localValue14 + localValue15 - 1; localValue17 >= 0; localValue17--) {
         int localValue18 = localValue17 / 10;
         int localValue19 = localValue17 % 10;
         int localValue20 = localValue3 + localValue19 * 9;
         int localValue21 = localValue4 - localValue18 * localValue5;
         if (localValue8 + localValue10 <= 4) {
            localValue21 += this.internalField0721.nextInt(2);
         }

         if (localValue17 < localValue14 && localValue17 == localValue6) {
            localValue21 -= 2;
         }

         this.internalMethod07283(localValue1, HotbarHud.InternalType0285.internalField0108, localValue20, localValue21, false);
         int localValue22 = localValue17 * 2;
         boolean localValue23 = localValue17 >= localValue14;
         if (localValue23) {
            int localValue24 = localValue22 - localValue16;
            if (localValue24 < localValue10) {
               boolean localValue25 = localValue24 + 1 == localValue10;
               this.internalMethod07283(
                  localValue1,
                  localValue12 == HotbarHud.InternalType0285.internalField1000 ? localValue12 : HotbarHud.InternalType0285.internalField0998,
                  localValue20,
                  localValue21,
                  localValue25
               );
            }
         }

         if (localValue22 < localValue8) {
            this.internalMethod07283(localValue1, localValue12, localValue20, localValue21, localValue22 + 1 == localValue8);
         }

         if (localValue17 < 40) {
            float localValue27 = this.internalField0556[localValue17].internalMethod07059(0.0F);
            if (localValue27 > 0.001F) {
               float localValue28 = 1.0F + (1.0F - localValue27);
               HudRenderUtils.internalMethod08976(localValue1.getMatrices(), localValue20 + 4.0F, localValue21 + 4.0F, localValue28);
               ColorRGBA localValue26 = ColorRGBA.WHITE.mulAlpha(localValue27);
               if (this.internalField0618[localValue17] == 1) {
                  localValue1.drawRoundedRect(localValue20, localValue21, 4.0F, 8.0F, new CornerRadii(2.0F, 0.0F, 0.0F, 2.0F), localValue26);
               } else if (this.internalField0618[localValue17] == 2) {
                  localValue1.drawRoundedRect(localValue20 + 4, localValue21, 4.0F, 8.0F, new CornerRadii(0.0F, 2.0F, 2.0F, 0.0F), localValue26);
               } else {
                  localValue1.drawRoundedRect(localValue20, localValue21, 8.0F, 8.0F, CornerRadii.internalMethod03908(2.0F), localValue26);
               }

               HudRenderUtils.internalMethod00012(localValue1.getMatrices());
            }
         }
      }
   }

   private void internalMethod07283(UiRenderContext localValue1, HotbarHud.InternalType0285 localValue2, int localValue3, int localValue4, boolean localValue5) {
      localValue2.internalMethod06973(localValue1, localValue3, localValue4, 8.0F, 8.0F, localValue5);
   }

   private void internalMethod05073(UiRenderContext localValue1, PlayerEntity localValue2, int localValue3, int localValue4, int localValue5) {
      int localValue6 = localValue2.getMaxAir();
      int localValue7 = Math.clamp((long)localValue2.getAir(), 0, localValue6);
      boolean localValue8 = localValue2.isSubmergedIn(FluidTags.WATER);
      if (localValue8 || localValue7 < localValue6) {
         localValue4 = this.internalMethod03982(localValue3, localValue4);
         int localValue9 = internalMethod07239(localValue7, localValue6, -2);
         int localValue10 = internalMethod07239(localValue7, localValue6, 0);
         int localValue11 = 10 - internalMethod07239(localValue7, localValue6, internalMethod03983(localValue7, localValue8));
         boolean localValue12 = localValue9 != localValue10;
         if (!localValue8) {
            this.internalField1055 = 0;
         }

         for (int localValue13 = 1; localValue13 <= 10; localValue13++) {
            int localValue14 = localValue5 - (localValue13 - 1) * 9 - 10;
            if (localValue13 <= localValue9) {
               localValue1.drawGuiTexture(net.minecraft.client.gl.RenderPipelines.GUI_TEXTURED, internalField0354, localValue14, localValue4, 9, 9);
            } else if (localValue12 && localValue13 == localValue10 && localValue8) {
               localValue1.drawGuiTexture(net.minecraft.client.gl.RenderPipelines.GUI_TEXTURED, internalField0355, localValue14, localValue4, 9, 9);
               this.internalMethod07071(localValue13, localValue2, localValue11);
            } else if (localValue13 > 10 - localValue11) {
               int localValue15 = localValue11 == 10 && this.internalField0227 % 2 == 0 ? this.internalField0721.nextInt(2) : 0;
               localValue1.drawGuiTexture(net.minecraft.client.gl.RenderPipelines.GUI_TEXTURED, internalField1133, localValue14, localValue4 + localValue15, 9, 9);
            }
         }
      }
   }

   private int internalMethod03982(int localValue1, int localValue2) {
      return localValue2 - (this.internalMethod00616(localValue1) - 1) * 10;
   }

   private static int internalMethod07239(int localValue0, int localValue1, int localValue2) {
      return MathHelper.ceil((float)((localValue0 + localValue2) * 10) / localValue1);
   }

   private static int internalMethod03983(int localValue0, boolean localValue1) {
      return localValue0 != 0 && localValue1 ? 1 : 0;
   }

   private void internalMethod07071(int localValue1, PlayerEntity localValue2, int localValue3) {
      if (this.internalField1055 != localValue1) {
         float localValue4 = 0.5F + 0.1F * Math.max(0, localValue3 - 3 + 1);
         float localValue5 = 1.0F + 0.1F * Math.max(0, localValue3 - 5 + 1);
         localValue2.playSound(SoundEvents.UI_HUD_BUBBLE_POP, localValue4, localValue5);
         this.internalField1055 = localValue1;
      }
   }

   private void internalMethod00037(UiRenderContext localValue1, PlayerEntity localValue2, int localValue3, int localValue4) {
      ColorRGBA localValue5 = new ColorRGBA(184.0F, 132.0F, 88.0F);
      HungerManager localValue6 = localValue2.getHungerManager();
      int localValue7 = localValue6.getFoodLevel();
      int[] localValue8 = new int[10];

      for (int localValue9 = 0; localValue9 < 10; localValue9++) {
         int localValue10 = localValue3;
         if (localValue6.getSaturationLevel() <= 0.0F && this.internalField0227 % (localValue7 * 3 + 1) == 0) {
            localValue10 = localValue3 + (this.internalField0721.nextInt(3) - 1);
         }

         localValue8[localValue9] = localValue10;
      }

      for (int localValue14 = 0; localValue14 < 10; localValue14++) {
         int localValue18 = localValue4 - localValue14 * 9 - 10;
         localValue1.drawRoundedRect(
            localValue18,
            localValue8[localValue14],
            8.0F,
            8.0F,
            CornerRadii.internalMethod03908(1.5F),
            ThemeColors.internalMethod07738()
               .mulAlpha(
                  MathUtils.internalMethod02587(
                     ThemeColors.internalMethod02435().internalMethod08704(),
                     ThemeColors.internalMethod02435().internalMethod08705(),
                     InterfaceModule.internalMethod07584()
                  )
               )
         );
      }

      for (int localValue15 = 0; localValue15 < 10; localValue15++) {
         if (localValue15 * 2 + 1 < localValue7) {
            int localValue19 = localValue4 - localValue15 * 9 - 10;
            localValue1.drawRoundedRect(localValue19, localValue8[localValue15], 8.0F, 8.0F, CornerRadii.internalMethod03908(1.5F), localValue5);
         }
      }

      for (int localValue16 = 0; localValue16 < 10; localValue16++) {
         if (localValue16 * 2 + 1 == localValue7) {
            int localValue20 = localValue4 - localValue16 * 9 - 10;
            localValue1.drawRoundedRect(localValue20 + 4, localValue8[localValue16], 4.0F, 8.0F, new CornerRadii(0.0F, 1.5F, 1.5F, 0.0F), localValue5);
         }
      }

      localValue5 = new ColorRGBA(251.0F, 170.0F, 56.0F);
      int localValue17 = (int)localValue6.getSaturationLevel();
      int[] localValue21 = new int[10];

      for (int localValue11 = 0; localValue11 < 10; localValue11++) {
         int localValue12 = localValue3;
         if (localValue6.getSaturationLevel() <= 0.0F && this.internalField0227 % (localValue17 * 3 + 1) == 0) {
            localValue12 = localValue3 + (this.internalField0721.nextInt(3) - 1);
         }

         localValue21[localValue11] = localValue12;
      }

      for (int localValue22 = 0; localValue22 < 10; localValue22++) {
         int localValue24 = localValue4 - localValue22 * 9 - 10;
         if (localValue22 * 2 + 1 < localValue17) {
            localValue1.drawRoundedRect(localValue24, localValue21[localValue22], 8.0F, 8.0F, CornerRadii.internalMethod03908(1.5F), localValue5);
         }
      }

      for (int localValue23 = 0; localValue23 < 10; localValue23++) {
         if (localValue23 * 2 + 1 == localValue17) {
            int localValue25 = localValue4 - localValue23 * 9 - 10;
            localValue1.drawRoundedRect(localValue25 + 4, localValue21[localValue23], 4.0F, 8.0F, new CornerRadii(0.0F, 1.5F, 1.5F, 0.0F), localValue5);
         }
      }
   }

   @Nullable
   private PlayerEntity internalMethod03874() {
      return internalField0149.getCameraEntity() instanceof PlayerEntity localValue2 ? localValue2 : null;
   }

   @Nullable
   private LivingEntity internalMethod04421() {
      PlayerEntity localValue1 = this.internalMethod03874();
      if (localValue1 == null) {
         return null;
      } else {
         return localValue1.getVehicle() instanceof LivingEntity localValue3 ? localValue3 : null;
      }
   }

   private int internalMethod05438(@Nullable LivingEntity localValue1) {
      if (localValue1 != null && localValue1.isLiving()) {
         int localValue2 = (int)(localValue1.getMaxHealth() + 0.5F) / 2;
         return Math.min(localValue2, 30);
      } else {
         return 0;
      }
   }

   private int internalMethod00616(int localValue1) {
      return (int)Math.ceil(localValue1 / 10.0);
   }

   static enum InternalType0285 {
      internalField0108(new ColorRGBA(200.0F, 200.0F, 200.0F, 200.0F)),
      internalField0109(new ColorRGBA(255.0F, 81.0F, 81.0F)),
      internalField0999(new ColorRGBA(169.0F, 202.0F, 23.0F)),
      internalField1000(new ColorRGBA(66.0F, 66.0F, 66.0F)),
      internalField0998(new ColorRGBA(251.0F, 170.0F, 56.0F)),
      internalField0997(new ColorRGBA(163.0F, 246.0F, 255.0F));

      private final ColorRGBA internalField0777;

      public void internalMethod06973(CustomDrawContext localValue1, float localValue2, float localValue3, float localValue4, float localValue5, boolean localValue6) {
         if (localValue6) {
            localValue1.drawRoundedRect(
               localValue2,
               localValue3,
               localValue4 / 2.0F,
               localValue5,
               new CornerRadii(2.0F, 0.0F, 0.0F, 2.0F),
               this == internalField0108
                  ? ThemeColors.internalMethod07738()
                     .mulAlpha(
                        MathUtils.internalMethod02587(
                           ThemeColors.internalMethod02435().internalMethod08704(),
                           ThemeColors.internalMethod02435().internalMethod08705(),
                           InterfaceModule.internalMethod07584()
                        )
                     )
                  : this.internalField0777
            );
         } else {
            localValue1.drawRoundedRect(
               localValue2,
               localValue3,
               localValue4,
               localValue5,
               CornerRadii.internalMethod03908(2.0F),
               this == internalField0108
                  ? ThemeColors.internalMethod07738()
                     .mulAlpha(
                        MathUtils.internalMethod02587(
                           ThemeColors.internalMethod02435().internalMethod08704(),
                           ThemeColors.internalMethod02435().internalMethod08705(),
                           InterfaceModule.internalMethod07584()
                        )
                     )
                  : this.internalField0777
            );
         }
      }

      static HotbarHud.InternalType0285 internalMethod05872(PlayerEntity localValue0) {
         if (localValue0.hasStatusEffect(StatusEffects.POISON)) {
            return internalField0999;
         } else if (localValue0.hasStatusEffect(StatusEffects.WITHER)) {
            return internalField1000;
         } else {
            return localValue0.isFrozen() ? internalField0997 : internalField0109;
         }
      }

      @Generated
      private InternalType0285(ColorRGBA localValue3) {
         this.internalField0777 = localValue3;
      }
   }

   static final class InternalType0286 {
      private final ItemStack internalField0878;
      private final float internalField0205;
      private final float internalField0206;
      private final float internalField1048;

      InternalType0286(ItemStack localValue1, float localValue2, float localValue3, float localValue4) {
         this.internalField0878 = localValue1;
         this.internalField0205 = localValue2;
         this.internalField0206 = localValue3;
         this.internalField1048 = localValue4;
      }

      float internalMethod08618() {
         return this.internalField0205 + 8.0F;
      }

      float internalMethod08631() {
         return this.internalField0206 + 8.0F;
      }

      @Override
      public final String toString() {
         return "InternalType0286[stack=" + this.internalField0878 + ", x=" + this.internalField0205 + ", y=" + this.internalField0206 + ", scaleFactor=" + this.internalField1048 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0878);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0205);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0206);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1048);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         HotbarHud.InternalType0286 other = (HotbarHud.InternalType0286) localValue1;
         return java.util.Objects.equals(this.internalField0878, other.internalField0878)
            && java.util.Objects.equals(this.internalField0205, other.internalField0205)
            && java.util.Objects.equals(this.internalField0206, other.internalField0206)
            && java.util.Objects.equals(this.internalField1048, other.internalField1048);
      }

      public ItemStack internalMethod02530() {
         return this.internalField0878;
      }

      public float internalMethod01040() {
         return this.internalField0205;
      }

      public float internalMethod01044() {
         return this.internalField0206;
      }

      public float internalMethod08617() {
         return this.internalField1048;
      }
   }
}
