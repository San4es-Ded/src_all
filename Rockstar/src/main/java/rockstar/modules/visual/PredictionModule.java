package rockstar.modules.visual;












import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.rotation.*;
import rockstar.client.render.*;
import rockstar.client.module.*;
import rockstar.client.i18n.*;
import rockstar.client.event.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.internal.framework.*;
import rockstar.client.*;
import rockstar.client.module.Module;

import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;
import rockstar.client.compat.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.Generated;
import rockstar.client.compat.ShaderProgramKeys;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexFormats;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SnowballItem;
import net.minecraft.item.TridentItem;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL11;
import moscow.rockstar.mixin.accessors.AbstractTextureAccessor;
import pyrock.events.network.SendPacketEvent;
import pyrock.events.render.PreHudRenderEvent;
import pyrock.events.render.Render3DEvent;
import pyrock.utility.render.ColorRGBA;
import pyrock.utility.render.CustomDrawContext;

@ModuleInfo(
   name = "Prediction",
   category = ModuleCategory.VISUALS,
   internalMethod08049 = true
)
public class PredictionModule extends Module {
   private final List<PredictionModule.InternalType0333> internalField0416 = new ArrayList<>();
   private final List<PredictionModule.InternalType0439> internalField0417 = new ArrayList<>();
   private final List<PredictionModule.InternalType0438> internalField1145 = new ArrayList<>();
   private MultiSelectSetting internalField0675;
   private ModeSetting internalField0668;
   private ModeSetting.InternalType0088 internalField0237;
   private ModeSetting.InternalType0088 internalField0238;
   private BooleanSetting internalField0650;
   private BooleanSetting internalField0651;
   private BooleanSetting internalField1261;
   private BooleanSetting internalField1263;
   private BooleanSetting internalField1262;
   private BooleanSetting internalField1264;
   private ColorSetting internalField0665;
   private Rotation internalField0118;
   private Vec3d internalField0283;
   private static final int internalField0227 = 4;
   private int internalField0228 = -1;
   private final EventListener<SendPacketEvent> internalField0157 = localValue1 -> {
      if (localValue1.getPacket() instanceof PlayerInteractItemC2SPacket localValue2) {
         if (internalField0149.player != null && this.internalField0118 != null) {
            ItemStack localValue9 = internalField0149.player.getMainHandStack();
            if (this.internalMethod03316(localValue9.getItem())) {
               RotationManager localValue4 = RockstarClient.getInstance().internalMethod02368();
               if (localValue4 != null && !localValue4.internalMethod01525()) {
                  Rotation localValue5 = localValue4.internalMethod00024();
                  float localValue6 = Math.abs(RotationUtils.internalMethod08495(localValue5.internalMethod00169(), this.internalField0118.internalMethod00169()));
                  float localValue7 = Math.abs(localValue5.internalMethod00171() - this.internalField0118.internalMethod00171());
                  float localValue8 = 35.0F;
                  if (!(localValue6 > localValue8) && !(localValue7 > localValue8)) {
                     if (this.internalField1263.internalMethod04496() && !this.internalField1262.internalMethod04496()) {
                        localValue4.internalMethod00418(
                           this.internalField0118, RotationBehavior.internalField1003, 180.0F, 180.0F, 180.0F, RotationPriority.internalField1012
                        );
                     }
                  }
               }
            }
         }
      }
   };
   private final EventListener<PreHudRenderEvent> internalField0158 = localValue1 -> {
      CustomDrawContext localValue2 = localValue1.getContext();
      MatrixStack localValue3 = rockstar.client.render.GuiMatrixCompat.toLegacyStack(localValue2.getMatrices());

      for (PredictionModule.InternalType0333 localValue5 : this.internalField0416) {
         if (localValue5.internalField0227 > 0 || localValue5.internalField0411 != null) {
            Vec2f localValue6 = ScreenProjectionUtils.internalMethod00612(localValue5.internalField0416.getLast());
            if (localValue6 != null) {
               float localValue7 = localValue6.x;
               float localValue8 = localValue6.y;
               SizedFont localValue9 = Fonts.internalField0449.internalMethod01432(13.0F);
               float localValue10 = localValue9.internalMethod04890() + 6.0F;
               float localValue11 = -localValue10;
               String localValue12 = this.internalMethod03959(localValue5.internalField0410);
               localValue12 = this.internalMethod03672(localValue12.replace("] ", "").replace("[", ""), localValue5.internalField0227);

               ItemStack localValue13 = switch (localValue5.internalField0410) {
                  case ThrownItemEntity localValue16 -> localValue16.getStack();
                  case PersistentProjectileEntity localValue17 -> localValue17.getItemStack();
                  case ItemEntity localValue18 -> localValue18.getStack();
                  default -> Items.ARROW.getDefaultStack();
               };
               float localValue56 = (float)localValue5.internalField0416.getLast().distanceTo(internalField0149.player.getEyePos());
               float localValue58 = MathHelper.clamp(1.0F - localValue56 / 20.0F, 0.5F, 1.0F);
               localValue3.push();
               localValue3.translate(localValue7, localValue8, 0.0F);
               localValue3.scale(localValue58, localValue58, 1.0F);
               float localValue60 = localValue9.internalMethod00965(localValue12) + 20.0F;
               localValue2.drawRect(-localValue60 / 2.0F, localValue11, localValue60, localValue10, new ColorRGBA(0.0F, 0.0F, 0.0F, 100.0F));
               localValue2.drawItem(localValue13, -localValue60 / 2.0F, localValue11, 1.0F);
               localValue2.drawText(localValue9, localValue12, -localValue60 / 2.0F + 17.0F, localValue11 + 3.0F, ThemeColors.internalField1312);
               localValue11 += localValue10;
               if (localValue5.internalField0410 instanceof ProjectileEntity localValue62 && localValue62.getOwner() instanceof AbstractClientPlayerEntity localValue65) {
                  String localValue70 = LanguageManager.internalMethod07214("modules.prediction.from")
                     + " "
                     + (
                        localValue62.getOwner() == internalField0149.player
                           ? LanguageManager.internalMethod07214("modules.prediction.you")
                           : localValue62.getOwner().getName().getString()
                     );
                  float localValue20 = localValue9.internalMethod00965(localValue70) + 22.0F;
                  localValue2.drawRect(-localValue20 / 2.0F, localValue11, localValue20, localValue10, new ColorRGBA(0.0F, 0.0F, 0.0F, 100.0F));
                  localValue2.drawHead(localValue65, -localValue20 / 2.0F, localValue11, localValue10, CornerRadii.internalField0098, ThemeColors.internalField1312);
                  localValue2.drawText(localValue9, localValue70, -localValue20 / 2.0F + 19.0F, localValue11 + 3.0F, ThemeColors.internalField1312);
                  localValue11 += localValue10;
               }

               if (localValue5.internalField0410 instanceof PotionEntity localValue63) {
                  for (StatusEffectInstance localValue71 : PotionItemUtils.internalMethod00984(localValue63.getStack())) {
                     String localValue73 = ((StatusEffect)localValue71.getEffectType().value()).getName().getString();
                     int localValue21 = localValue71.getAmplifier();
                     int localValue22 = localValue71.getDuration();
                     String localValue23 = localValue21 > 0 ? " " + (localValue21 + 1) : "";
                     String localValue24 = this.internalMethod04778(localValue22);
                     String localValue25 = localValue73 + localValue23 + " (" + localValue24 + ")";
                     float localValue26 = localValue9.internalMethod00965(localValue25) + 6.0F;
                     localValue2.drawRect(-localValue26 / 2.0F, localValue11 + 5.0F, localValue26, localValue10, new ColorRGBA(0.0F, 0.0F, 0.0F, 100.0F));
                     localValue2.drawText(
                        localValue9,
                        localValue25,
                        -localValue26 / 2.0F + 3.0F,
                        localValue11 + 8.0F,
                        ColorRGBA.fromInt(((StatusEffect)localValue71.getEffectType().value()).getColor()).withAlpha(255.0F)
                     );
                     localValue11 += localValue10;
                  }
               }

               localValue3.pop();
            }
         }
      }

      if (this.internalField1263.internalMethod04496()
         && this.internalField0283 != null
         && this.internalMethod03316(internalField0149.player.getMainHandStack().getItem())) {
         Vec3d localValue27 = internalField0149.player.getCameraPosVec(localValue1.getTickDelta());
         Rotation localValue30 = this.internalMethod03673(localValue27, this.internalField0283, internalField0149.player.getMainHandStack().getItem());
         Vec2f localValue33 = localValue30 == null
            ? null
            : ScreenProjectionUtils.internalMethod06420(internalField0149.player.getRotationVector(localValue30.internalMethod00171(), localValue30.internalMethod00169()));
         if (localValue33 != null) {
            RotationManager localValue36 = RockstarClient.getInstance().internalMethod02368();
            Rotation localValue39 = localValue36 != null
               ? localValue36.internalMethod09074()
               : new Rotation(internalField0149.player.getYaw(), internalField0149.player.getPitch());
            float localValue42 = Math.abs(RotationUtils.internalMethod08495(localValue39.internalMethod00169(), localValue30.internalMethod00169()));
            float localValue45 = Math.abs(localValue39.internalMethod00171() - localValue30.internalMethod00171());
            ColorRGBA localValue48 = (localValue42 < 10.0F && localValue45 < 10.0F ? ThemeColors.internalField0776 : ThemeColors.internalField1312).withAlpha(200.0F);
            localValue3.push();
            localValue3.translate(localValue33.x, localValue33.y, 0.0F);
            float localValue51 = (float)(System.currentTimeMillis() % 2000L) / 2000.0F * 360.0F;
            localValue3.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(localValue51));
            float localValue54 = 14.0F;
            localValue2.drawRect(-localValue54 / 2.0F, -0.75F, localValue54, 1.5F, localValue48);
            localValue2.drawRect(-0.75F, -localValue54 / 2.0F, 1.5F, localValue54, localValue48);
            localValue3.pop();
         }
      }

      for (PredictionModule.InternalType0438 localValue31 : this.internalField1145) {
         Vec3d localValue34 = localValue31.internalMethod04202();
         Vec2f localValue37 = ScreenProjectionUtils.internalMethod00612(localValue34);
         if (localValue37 != null) {
            float localValue40 = localValue37.x;
            float localValue43 = localValue37.y;
            SizedFont localValue46 = Fonts.internalField0449.internalMethod01432(13.0F);
            float localValue49 = localValue46.internalMethod04890() + 6.0F;
            float localValue52 = -localValue49;
            String localValue55 = localValue31.internalMethod03017();
            ItemStack localValue57 = localValue31.internalMethod04149();
            float localValue59 = (float)localValue34.distanceTo(internalField0149.player.getEyePos());
            float localValue61 = MathHelper.clamp(1.0F - localValue59 / 20.0F, 0.5F, 1.0F);
            localValue3.push();
            localValue3.translate(localValue40, localValue43, 0.0F);
            localValue3.scale(localValue61, localValue61, 1.0F);
            float localValue64 = localValue46.internalMethod00965(localValue55) + 20.0F;
            localValue2.drawRect(-localValue64 / 2.0F, localValue52, localValue64, localValue49, new ColorRGBA(0.0F, 0.0F, 0.0F, 100.0F));
            localValue2.drawItem(localValue57, -localValue64 / 2.0F, localValue52, 1.0F);
            localValue2.drawText(localValue46, localValue55, -localValue64 / 2.0F + 17.0F, localValue52 + 3.0F, ThemeColors.internalField1312);
            localValue52 += localValue49;

            for (StatusEffectInstance localValue72 : PotionItemUtils.internalMethod00984(localValue57)) {
               String localValue74 = ((StatusEffect)localValue72.getEffectType().value()).getName().getString();
               int localValue75 = localValue72.getAmplifier();
               int localValue76 = localValue72.getDuration();
               String localValue77 = localValue75 > 0 ? " " + (localValue75 + 1) : "";
               String localValue78 = this.internalMethod04778(localValue76);
               String localValue79 = localValue74 + localValue77 + " (" + localValue78 + ")";
               float localValue80 = localValue46.internalMethod00965(localValue79) + 6.0F;
               localValue2.drawRect(-localValue80 / 2.0F, localValue52 + 5.0F, localValue80, localValue49, new ColorRGBA(0.0F, 0.0F, 0.0F, 100.0F));
               localValue2.drawText(
                  localValue46,
                  localValue79,
                  -localValue80 / 2.0F + 3.0F,
                  localValue52 + 8.0F,
                  ColorRGBA.fromInt(((StatusEffect)localValue72.getEffectType().value()).getColor()).withAlpha(255.0F)
               );
               localValue52 += localValue49;
            }

            localValue3.pop();
         }
      }

      if (this.internalField1261.internalMethod04496()) {
         SizedFont localValue29 = Fonts.internalField0449.internalMethod01432(10.0F);
         float localValue32 = 0.0F;

         for (PredictionModule.InternalType0333 localValue38 : this.internalField0416) {
            if (localValue38.internalField0411 == internalField0149.player && !(localValue38.internalField0410 instanceof EnderPearlEntity)) {
               String localValue41 = this.internalMethod03672(this.internalMethod03959(localValue38.internalField0410), localValue38.internalField0227);
               String localValue44 = LanguageManager.internalMethod00160("modules.prediction.warning", localValue41);
               localValue2.drawCenteredText(
                  localValue29,
                  localValue44,
                  internalField0389.internalMethod03585() / 2.0F,
                  internalField0389.internalMethod03589() / 2.0F + 20.0F + localValue32,
                  ThemeColors.internalField1312
               );
               localValue32 += localValue29.internalMethod04890() + 3.0F;
            }
         }
      }
   };
   private final EventListener<Render3DEvent> internalField1028 = localValue1 -> {
      ColorRGBA localValue2 = this.internalField1264.internalMethod04496() ? ThemeColors.internalMethod02531() : this.internalField0665.internalMethod05620();
      this.internalMethod06220(localValue1.getTickDelta());
      MatrixStack localValue3 = localValue1.getMatrices();
      localValue3.push();
      HudRenderUtils.internalMethod02691(true);
      HudRenderUtils.internalMethod01900(localValue3);
      RenderSystem.enableDepthTest();
      if (this.internalField0651.internalMethod04496()) {
         RenderSystem.disableDepthTest();
      }

      if (this.internalField0237.isSelected()) {
         RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
         BufferBuilder localValue4 = RenderSystem.renderThreadTesselator().begin(DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);

         for (PredictionModule.InternalType0333 localValue6 : this.internalField0416) {
            Vec3d localValue7 = localValue6.internalField0416.getFirst();
            Render3DUtils.internalMethod06311(localValue3, localValue4, ScreenProjectionUtils.internalMethod02822(localValue6.internalField0410, localValue1.getTickDelta()), localValue7, localValue2);

            for (Vec3d localValue9 : localValue6.internalField0416) {
               Render3DUtils.internalMethod06311(localValue3, localValue4, localValue7, localValue9, localValue2);
               localValue7 = localValue9;
            }
         }

         HudRenderUtils.internalMethod05816(localValue4);
      } else {
         Identifier localValue15 = RockstarClient.id("textures/bloom.png");
         RenderSystem.setShaderTexture(0, localValue15);
         RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
         BufferBuilder localValue17 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

         for (PredictionModule.InternalType0333 localValue21 : this.internalField0416) {
            Vec3d localValue24 = localValue21.internalField0416.getFirst();
            Vec3d localValue27 = ScreenProjectionUtils.internalMethod02822(localValue21.internalField0410, localValue1.getTickDelta());
            if (localValue27.distanceTo(internalField0149.player.getEyePos()) > 2.0) {
               for (int localValue10 = 0; localValue10 < 10; localValue10++) {
                  float localValue11 = localValue10 / 10.0F;
                  Vec3d localValue12 = localValue27.add(localValue24.subtract(localValue27).multiply(localValue11));
                  this.internalMethod02521(localValue3, localValue12, localValue17, (float)localValue24.distanceTo(localValue27) / 3.0F, 1.0F);
                  this.internalMethod02521(localValue3, localValue12, localValue17, (float)localValue24.distanceTo(localValue27) * 2.0F, 0.05F);
               }
            }

            for (Vec3d localValue33 : localValue21.internalField0416) {
               if (localValue33.distanceTo(internalField0149.player.getEyePos()) > 2.0) {
                  for (int localValue36 = 0; localValue36 < 10; localValue36++) {
                     float localValue13 = localValue36 / 10.0F;
                     Vec3d localValue14 = localValue24.add(localValue33.subtract(localValue24).multiply(localValue13));
                     this.internalMethod02521(localValue3, localValue14, localValue17, (float)localValue33.distanceTo(localValue24) / 3.0F, 1.0F);
                     this.internalMethod02521(localValue3, localValue14, localValue17, (float)localValue33.distanceTo(localValue24) * 2.0F, 0.05F);
                  }
               }

               localValue24 = localValue33;
            }

            float localValue30 = 9.0F;
            if (localValue21.internalField0410 instanceof PotionEntity) {
               localValue3.push();
               localValue3.translate(localValue21.internalField0416.getLast());
               localValue3.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(-90.0F));
               RenderPipeline.internalMethod05007(localValue3, localValue17, -localValue30 / 2.0F, -localValue30 / 2.0F, 0.0, localValue30, localValue30, localValue2.withAlpha(255.0F));
               localValue3.pop();
            }
         }

         HudRenderUtils.internalMethod05816(localValue17);
      }

      float localValue16 = 1.0F;
      Identifier localValue18 = RockstarClient.id("textures/hit.png");
      this.internalMethod03308(localValue18);
      RenderSystem.setShaderTexture(0, localValue18);
      RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
      BufferBuilder localValue20 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

      for (PredictionModule.InternalType0439 localValue25 : this.internalField0417) {
         if (localValue25.internalField0411 == null) {
            localValue3.push();
            localValue3.translate(localValue25.internalField0246.getPos());
            localValue3.multiply(localValue25.internalField0246.getSide().getRotationQuaternion());
            localValue3.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(-90.0F));
            RenderPipeline.internalMethod05007(localValue3, localValue20, -localValue16 / 2.0F, -localValue16 / 2.0F, 0.0, localValue16, localValue16, localValue2.withAlpha(255.0F));
            localValue3.pop();
         }
      }

      HudRenderUtils.internalMethod05816(localValue20);
      RenderSystem.enableBlend();
      RenderSystem.disableDepthTest();
      RenderSystem.disableCull();
      RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
      RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
      Camera localValue23 = internalField0149.gameRenderer.getCamera();
      Vec3d localValue26 = localValue23.getCameraPos();
      BufferBuilder localValue28 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);

      for (PredictionModule.InternalType0439 localValue34 : this.internalField0417) {
         if (localValue34.internalField0411 != null) {
            Box localValue37 = localValue34.internalField0411.getBoundingBox().offset(-localValue26.getX(), -localValue26.getY(), -localValue26.getZ());
            localValue3.push();
            localValue3.translate(localValue26.getX(), localValue26.getY(), localValue26.getZ());
            Render3DUtils.internalMethod02535(localValue3, localValue28, localValue37, localValue2.mulAlpha(0.5F));
            localValue3.pop();
         }
      }

      HudRenderUtils.internalMethod05816(localValue28);
      BufferBuilder localValue32 = RenderSystem.renderThreadTesselator().begin(DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);

      for (PredictionModule.InternalType0439 localValue38 : this.internalField0417) {
         if (localValue38.internalField0411 != null) {
            Box localValue39 = localValue38.internalField0411.getBoundingBox().offset(-localValue26.getX(), -localValue26.getY(), -localValue26.getZ());
            localValue3.push();
            localValue3.translate(localValue26.getX(), localValue26.getY(), localValue26.getZ());
            Render3DUtils.internalMethod08795(localValue3, localValue32, localValue39, localValue2);
            localValue3.pop();
         }
      }

      HudRenderUtils.internalMethod05816(localValue32);
      HudRenderUtils.internalMethod04670();
      localValue3.pop();
   };

   public PredictionModule() {
      this.internalMethod09586();
   }

   private void internalMethod09586() {
      this.internalField0675 = new MultiSelectSetting(this, "modules.settings.prediction.entities");
      this.internalField0668 = new ModeSetting(this, "modules.settings.prediction.render_mode");
      this.internalField0237 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.prediction.render_mode.default");
      this.internalField0238 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.prediction.render_mode.glow").select();
      this.internalField0650 = new BooleanSetting(this, "modules.settings.prediction.hand").internalMethod06630();
      this.internalField0651 = new BooleanSetting(this, "modules.settings.prediction.walls").internalMethod06630();
      this.internalField1261 = new BooleanSetting(this, "modules.settings.prediction.hud").internalMethod06630();
      this.internalField1263 = new BooleanSetting(this, "modules.settings.prediction.helper");
      this.internalField1262 = new BooleanSetting(this, "modules.settings.prediction.assist", () -> !this.internalField1263.internalMethod04496());
      this.internalField1264 = new BooleanSetting(this, "theme.sync").internalMethod06630();
      this.internalField0665 = new ColorSetting(this, "modules.settings.chams.color", this.internalField1264::internalMethod04496)
         .internalMethod04886(ThemeColors.internalMethod02531());
      new PredicateSelectOption<Entity>(this.internalField0675, "modules.settings.prediction.entities.pearls", localValue0 -> localValue0 instanceof EnderPearlEntity).select();
      new PredicateSelectOption<Entity>(this.internalField0675, "modules.settings.prediction.entities.tridents", localValue0 -> localValue0 instanceof TridentEntity).select();
      new PredicateSelectOption<Entity>(this.internalField0675, "modules.settings.prediction.entities.snowballs", localValue0 -> localValue0 instanceof SnowballEntity).select();
      new PredicateSelectOption<Entity>(this.internalField0675, "modules.settings.prediction.entities.arrows", localValue0 -> localValue0 instanceof ArrowEntity).select();
      new PredicateSelectOption<Entity>(this.internalField0675, "modules.settings.prediction.entities.potions", localValue0 -> localValue0 instanceof PotionEntity).select();
      new PredicateSelectOption<>(this.internalField0675, "modules.settings.prediction.entities.items", localValue0 -> localValue0 instanceof ItemEntity);
   }

   @Override
   public void internalMethod08229() {
      this.internalField0416.clear();
      long localValue1 = System.currentTimeMillis();
      this.internalField1145.removeIf(localValue2 -> localValue2.internalMethod05274(localValue1));

      for (Entity localValue4 : internalField0149.world.getEntities()) {
         this.internalMethod01890(localValue4, false);
      }

      this.internalMethod09587();
   }

   private void internalMethod06220(float localValue1) {
      this.internalField0417.clear();
      if (this.internalField0650.internalMethod04496() && internalField0149.player != null && internalField0149.world != null) {
         Rotation localValue2 = this.internalMethod01425(localValue1);
         ItemStack localValue3 = internalField0149.player.getMainHandStack();
         ArrayList localValue4 = new ArrayList();
         Object localValue5 = null;
         if (localValue3.getItem() instanceof EnderPearlItem) {
            localValue5 = new EnderPearlEntity(internalField0149.world, internalField0149.player, localValue3);
         } else if (localValue3.getItem() instanceof TridentItem && internalField0149.player.isUsingItem()) {
            localValue5 = new TridentEntity(internalField0149.world, internalField0149.player, localValue3);
         } else if (localValue3.getItem() instanceof SnowballItem) {
            localValue5 = new SnowballEntity(internalField0149.world, internalField0149.player, localValue3);
         } else if (localValue3.getItem() instanceof BowItem && internalField0149.player.isUsingItem()) {
            ItemStack localValue11 = new ItemStack(Items.ARROW);
            localValue5 = new ArrowEntity(internalField0149.world, internalField0149.player, localValue11, localValue3);
         } else if (localValue3.getItem() instanceof CrossbowItem && CrossbowItem.isCharged(localValue3)) {
            boolean localValue6 = EnchantmentUtils.internalMethod03526(localValue3, Enchantments.MULTISHOT) > 0;
            ItemStack localValue7 = new ItemStack(Items.ARROW);
            if (localValue6) {
               for (int localValue8 = 0; localValue8 < 3; localValue8++) {
                  ArrowEntity localValue9 = new ArrowEntity(internalField0149.world, internalField0149.player, localValue7, localValue3);
                  localValue4.add(localValue9);
               }
            } else {
               localValue5 = new ArrowEntity(internalField0149.world, internalField0149.player, localValue7, localValue3);
            }
         }

         if (localValue5 instanceof ProjectileEntity localValue12) {
            float localValue14 = 1.5F;
            if (localValue5 instanceof TridentEntity) {
               localValue14 = 2.5F;
            } else if (localValue5 instanceof ArrowEntity) {
               localValue14 = 3.0F;
            }

            this.internalMethod05486(localValue12, internalField0149.player, localValue2.internalMethod00171(), localValue2.internalMethod00169(), 0.0F, localValue14, 1.0F);
            this.internalMethod03742(localValue12, localValue1);
            this.internalMethod01890(localValue12, true);
         }

         if (!localValue4.isEmpty()) {
            float localValue13 = 3.15F;
            float localValue15 = 10.0F;

            for (int localValue16 = 0; localValue16 < localValue4.size(); localValue16++) {
               ProjectileEntity localValue17 = (ProjectileEntity)localValue4.get(localValue16);
               float localValue10 = 0.0F;
               if (localValue16 == 0) {
                  localValue10 = -localValue15;
               } else if (localValue16 == 2) {
                  localValue10 = localValue15;
               }

               this.internalMethod05486(localValue17, internalField0149.player, localValue2.internalMethod00171(), localValue2.internalMethod00169() + localValue10, 0.0F, localValue13, 1.0F);
               this.internalMethod03742(localValue17, localValue1);
               this.internalMethod01890(localValue17, true);
            }
         }
      }
   }

   private Rotation internalMethod01425(float localValue1) {
      RotationManager localValue2 = RockstarClient.getInstance().internalMethod02368();
      return localValue2 != null && !localValue2.internalMethod01525()
         ? localValue2.internalMethod08582()
         : new Rotation(internalField0149.player.getYaw(localValue1), internalField0149.player.getPitch(localValue1));
   }

   private void internalMethod03742(ProjectileEntity localValue1, float localValue2) {
      Vec3d localValue3 = internalField0149.player.getCameraPosVec(localValue2);
      localValue1.setPosition(localValue3.x, localValue3.y - 0.1, localValue3.z);
   }

   private void internalMethod09587() {
      this.internalField0118 = null;
      this.internalField0283 = null;
      if (this.internalField1263.internalMethod04496() && internalField0149.player != null && internalField0149.world != null) {
         Item localValue1 = internalField0149.player.getMainHandStack().getItem();
         if (this.internalMethod03316(localValue1)) {
            RotationManager localValue2 = RockstarClient.getInstance().internalMethod02368();
            Rotation localValue3 = localValue2.internalMethod00024();
            Vec3d localValue4 = internalField0149.player.getEyePos();
            float localValue5 = Float.MAX_VALUE;
            Rotation localValue6 = null;

            for (PredictionModule.InternalType0333 localValue8 : this.internalField0416) {
               if (localValue8.internalMethod06390() instanceof EnderPearlEntity) {
                  Vec3d localValue9 = this.internalMethod05433(localValue8.internalMethod07501().getLast(), localValue1);
                  Rotation localValue10 = this.internalMethod03673(localValue4, localValue9, localValue1);
                  if (localValue10 != null) {
                     float localValue11 = localValue10.internalMethod00735(localValue3);
                     if (localValue11 < localValue5) {
                        localValue5 = localValue11;
                        localValue6 = localValue10;
                        this.internalField0283 = localValue9;
                     }
                  }
               }
            }

            for (PredictionModule.InternalType0439 localValue14 : this.internalField0417) {
               if (!localValue14.internalMethod05448() && localValue14.internalMethod02183() instanceof EnderPearlEntity) {
                  Vec3d localValue16 = this.internalMethod05433(localValue14.internalMethod00398(), localValue1);
                  Rotation localValue18 = this.internalMethod03673(localValue4, localValue16, localValue1);
                  if (localValue18 != null) {
                     float localValue19 = localValue18.internalMethod00735(localValue3);
                     if (localValue19 < localValue5) {
                        localValue5 = localValue19;
                        localValue6 = localValue18;
                        this.internalField0283 = localValue16;
                     }
                  }
               }
            }

            if (localValue6 != null) {
               this.internalField0118 = RotationUtils.internalMethod02284(localValue2.internalMethod07496(), localValue6);
               if (this.internalField1262.internalMethod04496()) {
                  Rotation localValue13 = localValue2.internalMethod09074();
                  float localValue15 = Math.abs(RotationUtils.internalMethod08495(localValue13.internalMethod00169(), this.internalField0118.internalMethod00169()));
                  float localValue17 = Math.abs(localValue13.internalMethod00171() - this.internalField0118.internalMethod00171());
                  if (localValue15 < 10.0F && localValue17 < 10.0F) {
                     localValue2.internalMethod00418(
                        this.internalField0118, RotationBehavior.internalField1003, 180.0F, 180.0F, 180.0F, RotationPriority.internalField1012
                     );
                  }
               }
            }
         }
      }
   }

   private Vec3d internalMethod05433(Vec3d localValue1, Item localValue2) {
      if (!(localValue2 instanceof EnderPearlItem)) {
         return localValue1;
      } else {
         BlockPos localValue3 = BlockPos.ofFloored(localValue1);
         return new Vec3d(localValue3.getX() + 0.5, localValue3.getY() + 1.0, localValue3.getZ() + 0.5);
      }
   }

   private Rotation internalMethod03673(Vec3d localValue1, Vec3d localValue2, Item localValue3) {
      Rotation localValue4 = RotationUtils.internalMethod03921(localValue1, localValue2);
      if (localValue3 instanceof EnderPearlItem) {
         Float localValue10 = this.internalMethod02768(localValue1, localValue2);
         return localValue10 != null && !Float.isNaN(localValue10) ? new Rotation(localValue4.internalMethod00169(), localValue10) : null;
      } else if (localValue3 instanceof TridentItem) {
         double localValue5 = localValue1.distanceTo(localValue2);
         double localValue7 = internalField0149.player.getVelocity().y;
         float localValue9 = localValue4.internalMethod00171() - (float)(localValue5 * 0.22F) + (float)(localValue7 * localValue5 * (localValue7 > 0.0 ? 0.5F : 1.0F));
         return new Rotation(localValue4.internalMethod00169(), localValue9);
      } else {
         return null;
      }
   }

   private Float internalMethod02768(Vec3d localValue1, Vec3d localValue2) {
      double localValue3 = localValue2.x - localValue1.x;
      double localValue5 = localValue2.z - localValue1.z;
      double localValue7 = Math.hypot(localValue3, localValue5);
      if (localValue7 < 0.001) {
         return null;
      } else {
         double localValue9 = 6.125 * (localValue2.y - localValue1.y);
         double localValue11 = 0.05F * (0.05F * (localValue7 * localValue7) + localValue9);
         double localValue13 = 9.378906F - localValue11;
         if (localValue13 < 0.0) {
            return null;
         } else {
            double localValue15 = Math.sqrt(localValue13);
            double localValue17 = 3.0625 - localValue15;
            double localValue19 = Math.atan2(localValue17 * localValue17 + localValue15, 0.05F * localValue7);
            double localValue21 = Math.atan2(localValue17, 0.05F * localValue7);
            double localValue23 = Math.min(localValue19, localValue21);
            return (float)(-Math.toDegrees(localValue23));
         }
      }
   }

   private boolean internalMethod03316(Item localValue1) {
      return localValue1 instanceof EnderPearlItem || localValue1 instanceof TridentItem;
   }

   private void internalMethod01890(Entity localValue1, boolean localValue2) {
      if (this.internalMethod04942(localValue1)) {
         if (localValue1 instanceof ProjectileEntity localValue3 && localValue3.getOwner() == null) {
            List localValue4 = internalField0149.world.getPlayers();
            if (!localValue4.isEmpty()) {
               localValue4.sort(Comparator.comparingDouble(localValue1x -> ((net.minecraft.entity.Entity)localValue1x).distanceTo(localValue3)));
               localValue3.setOwner((Entity)localValue4.getFirst());
            }
         }

         ArrayList localValue13 = new ArrayList();
         Vec3d localValue14 = localValue1.getEntityPos();
         Vec3d localValue5 = localValue1.getVelocity();
         Entity localValue6 = null;
         int localValue7 = 0;
         BlockHitResult localValue8 = null;

         for (int localValue9 = 0; localValue9 < 150; localValue9++) {
            Vec3d localValue10 = this.internalMethod06612(localValue1, localValue5);
            Vec3d localValue11 = localValue14.add(localValue10);
            localValue7 = localValue9;
            localValue8 = internalField0149.world.raycast(new RaycastContext(localValue14, localValue11, ShapeType.COLLIDER, FluidHandling.NONE, localValue1));
            Entity localValue12 = this.internalMethod06006(localValue1, localValue11);
            if (localValue12 != null) {
               localValue13.add(localValue11);
               localValue6 = localValue12;
               break;
            }

            if (localValue8.getType() != Type.MISS) {
               localValue13.add(localValue8.getPos());
               break;
            }

            localValue13.add(localValue11);
            localValue14 = localValue11;
            localValue5 = localValue10;
         }

         if (!localValue13.isEmpty()) {
            if (localValue2) {
               this.internalField0417.add(new PredictionModule.InternalType0439(localValue1, (Vec3d)localValue13.getLast(), localValue7, localValue6, localValue8, localValue2));
            } else {
               this.internalField0416.add(new PredictionModule.InternalType0333(localValue1, localValue13, localValue7, localValue6));
               if (localValue6 != null && localValue1 instanceof PotionEntity localValue15) {
                  this.internalMethod07206(localValue15, localValue6, (Vec3d)localValue13.getLast());
               }
            }
         }
      }
   }

   public Vec3d internalMethod01554() {
      if (internalField0149.player != null && internalField0149.world != null) {
         Rotation localValue1 = RockstarClient.getInstance().internalMethod02368().internalMethod00024();
         float localValue2 = localValue1 != null ? localValue1.internalMethod00171() : internalField0149.player.getPitch();
         float localValue3 = localValue1 != null ? localValue1.internalMethod00169() : internalField0149.player.getYaw();
         SnowballEntity localValue4 = new SnowballEntity(internalField0149.world, internalField0149.player, Items.SNOWBALL.getDefaultStack());
         this.internalMethod05486(localValue4, internalField0149.player, localValue2, localValue3, 0.0F, 1.5F, 1.0F);
         return this.internalMethod02963(localValue4);
      } else {
         return null;
      }
   }

   public Vec3d internalMethod07524(Entity localValue1) {
      return localValue1 != null && internalField0149.world != null ? this.internalMethod02963(localValue1) : null;
   }

   private Vec3d internalMethod02963(Entity localValue1) {
      Vec3d localValue2 = localValue1.getEntityPos();
      Vec3d localValue3 = localValue1.getVelocity();

      for (int localValue4 = 0; localValue4 < 150; localValue4++) {
         Vec3d localValue5 = this.internalMethod06612(localValue1, localValue3);
         Vec3d localValue6 = localValue2.add(localValue5);
         BlockHitResult localValue7 = internalField0149.world.raycast(new RaycastContext(localValue2, localValue6, ShapeType.COLLIDER, FluidHandling.NONE, localValue1));
         Entity localValue8 = this.internalMethod06006(localValue1, localValue6);
         if (localValue8 != null) {
            return localValue6;
         }

         if (localValue7.getType() != Type.MISS) {
            return localValue7.getPos();
         }

         localValue2 = localValue6;
         localValue3 = localValue5;
      }

      return localValue2;
   }

   private void internalMethod03308(Identifier localValue1) {
      AbstractTexture localValue2 = internalField0149.getTextureManager().getTexture(localValue1);
      if (localValue2 != null) {
         if (rockstar.client.render.FramebufferCompat.glId(localValue2.getGlTexture()) != this.internalField0228) {
            this.internalField0228 = rockstar.client.render.FramebufferCompat.glId(localValue2.getGlTexture());
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.internalField0228);
            GlStateManager._texParameter(3553, 33085, 4);
            GL30.glGenerateMipmap(3553);
         }

         ((AbstractTextureAccessor)localValue2).rockstar$setSampler(
            com.mojang.blaze3d.systems.RenderSystem.getSamplerCache().getRepeated(FilterMode.LINEAR, true)
         );
      }
   }

   private void internalMethod02521(MatrixStack localValue1, Vec3d localValue2, BufferBuilder localValue3, float localValue4, float localValue5) {
      ColorRGBA localValue6 = this.internalField1264.internalMethod04496() ? ThemeColors.internalMethod02531() : this.internalField0665.internalMethod05620();
      localValue1.push();
      localValue1.translate(localValue2);
      localValue1.multiply(internalField0149.gameRenderer.getCamera().getRotation());
      RenderPipeline.internalMethod05007(localValue1, localValue3, -localValue4 / 2.0F, -localValue4 / 2.0F, 0.0, localValue4, localValue4, localValue6.withAlpha(255.0F * localValue5));
      localValue1.pop();
   }

   private boolean internalMethod04942(Entity localValue1) {
      boolean localValue2 = false;

      for (MultiSelectSetting.InternalType0091 localValue4 : this.internalField0675.internalMethod07492()) {
         PredicateSelectOption localValue5 = (PredicateSelectOption)localValue4;
         if (localValue5.internalMethod06699(localValue1)) {
            localValue2 = true;
         }
      }

      return localValue1 instanceof TridentEntity localValue6 && localValue6.returnTimer > 0
         ? false
         : localValue2 && (Math.abs(localValue1.getVelocity().x + localValue1.getVelocity().z) > 0.01F || Math.abs(localValue1.getVelocity().y) > 0.2F);
   }

   private void internalMethod07206(PotionEntity localValue1, Entity localValue2, Vec3d localValue3) {
      long localValue4 = System.currentTimeMillis() + 2000L;

      for (PredictionModule.InternalType0438 localValue7 : this.internalField1145) {
         if (localValue7.internalField0227 == localValue1.getId()) {
            localValue7.internalMethod01420(localValue2, localValue3, localValue4);
            return;
         }
      }

      String localValue8 = this.internalMethod03959(localValue1);
      PredictionModule.InternalType0438 localValue9 = new PredictionModule.InternalType0438(localValue1.getId(), localValue1.getStack().copy(), localValue8, localValue2, localValue3, localValue4);
      this.internalField1145.add(localValue9);
   }

   private Entity internalMethod06006(Entity localValue1, Vec3d localValue2) {
      Vec3d localValue3 = localValue1.getEntityPos();
      Vec3d localValue4 = localValue2.subtract(localValue3);
      if (localValue4.lengthSquared() == 0.0) {
         return null;
      } else {
         EntityHitResult localValue5 = ProjectileUtil.raycast(
            localValue1,
            localValue3,
            localValue2,
            localValue1.getBoundingBox().stretch(localValue4).expand(0.5),
            localValue1x -> internalField0149.player != localValue1x
               && localValue1x.isAlive()
               && !(localValue1x instanceof ItemEntity)
               && !(localValue1x instanceof SnowballEntity)
               && !(localValue1x instanceof ExperienceOrbEntity)
               && localValue1x != localValue1,
            localValue4.lengthSquared()
         );
         return localValue5 != null ? localValue5.getEntity() : null;
      }
   }

   private String internalMethod03959(Entity localValue1) {
      if (localValue1 instanceof EnderPearlEntity) {
         return Items.ENDER_PEARL.getName().getString();
      } else {
         return localValue1 instanceof PotionEntity localValue2 ? localValue2.getStack().getFormattedName().getString() : localValue1.getName().getString();
      }
   }

   private void internalMethod01400(ProjectileEntity localValue1, double localValue2, double localValue4, double localValue6, float localValue8) {
      Vec3d localValue9 = this.internalMethod04366(localValue1, localValue2, localValue4, localValue6, localValue8);
      localValue1.setVelocity(localValue9);
      localValue1.velocityDirty = true;
      double localValue10 = localValue9.horizontalLength();
      localValue1.setYaw((float)(MathHelper.atan2(localValue9.x, localValue9.z) * 180.0F / (float)Math.PI));
      localValue1.setPitch((float)(MathHelper.atan2(localValue9.y, localValue10) * 180.0F / (float)Math.PI));
      localValue1.lastYaw = localValue1.getYaw();
      localValue1.lastPitch = localValue1.getPitch();
   }

   private void internalMethod05486(ProjectileEntity localValue1, Entity localValue2, float localValue3, float localValue4, float localValue5, float localValue6, float localValue7) {
      float localValue8 = -MathHelper.sin(localValue4 * (float) (Math.PI / 180.0)) * MathHelper.cos(localValue3 * (float) (Math.PI / 180.0));
      float localValue9 = -MathHelper.sin((localValue3 + localValue5) * (float) (Math.PI / 180.0));
      float localValue10 = MathHelper.cos(localValue4 * (float) (Math.PI / 180.0)) * MathHelper.cos(localValue3 * (float) (Math.PI / 180.0));
      this.internalMethod01400(localValue1, localValue8, localValue9, localValue10, localValue6);
      Vec3d localValue11 = localValue2.getMovement();
      localValue1.setVelocity(localValue1.getVelocity().add(localValue11.x, localValue2.isOnGround() ? 0.0 : localValue11.y, localValue11.z));
   }

   private Vec3d internalMethod04366(ProjectileEntity localValue1, double localValue2, double localValue4, double localValue6, float localValue8) {
      return new Vec3d(localValue2, localValue4, localValue6).normalize().multiply(localValue8);
   }

   private Vec3d internalMethod06612(Entity localValue1, Vec3d localValue2) {
      return localValue2.multiply(0.99).add(0.0, -localValue1.getFinalGravity(), 0.0);
   }

   private String internalMethod03672(String localValue1, int localValue2) {
      return localValue2 <= 0 ? localValue1 : localValue1 + "(" + TextUtils.internalMethod07254(localValue2 / 20.0F) + " " + LanguageManager.internalMethod07214("sec") + ")";
   }

   private String internalMethod04778(int localValue1) {
      int localValue2 = localValue1 / 20;
      int localValue3 = localValue2 / 60;
      int localValue4 = localValue2 % 60;
      return localValue3 > 0 ? String.format("%d:%02d", localValue3, localValue4) : String.format("0:%02d", localValue4);
   }

   @Generated
   public List<PredictionModule.InternalType0333> internalMethod03152() {
      return this.internalField0416;
   }

   public static final class InternalType0333 {
      final Entity internalField0410;
      final List<Vec3d> internalField0416;
      final int internalField0227;
      final Entity internalField0411;

      public InternalType0333(Entity localValue1, List<Vec3d> localValue2, int localValue3, Entity localValue4) {
         this.internalField0410 = localValue1;
         this.internalField0416 = localValue2;
         this.internalField0227 = localValue3;
         this.internalField0411 = localValue4;
      }

      @Override
      public final String toString() {
         return "InternalType0333[entity=" + this.internalField0410 + ", vectors=" + this.internalField0416 + ", ticks=" + this.internalField0227 + ", collidedEntity=" + this.internalField0411 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0410);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0416);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0411);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         PredictionModule.InternalType0333 other = (PredictionModule.InternalType0333) localValue1;
         return java.util.Objects.equals(this.internalField0410, other.internalField0410)
            && java.util.Objects.equals(this.internalField0416, other.internalField0416)
            && java.util.Objects.equals(this.internalField0227, other.internalField0227)
            && java.util.Objects.equals(this.internalField0411, other.internalField0411);
      }

      public Entity internalMethod06390() {
         return this.internalField0410;
      }

      public List<Vec3d> internalMethod07501() {
         return this.internalField0416;
      }

      public int internalMethod06631() {
         return this.internalField0227;
      }

      public Entity internalMethod06609() {
         return this.internalField0411;
      }
   }

   static class InternalType0438 {
      final int internalField0227;
      private final ItemStack internalField0878;
      private final String internalField0248;
      private Entity internalField0410;
      private String internalField0247;
      private Vec3d internalField0283;
      private long internalField0229;

      InternalType0438(int localValue1, ItemStack localValue2, String localValue3, Entity localValue4, Vec3d localValue5, long localValue6) {
         this.internalField0227 = localValue1;
         this.internalField0878 = localValue2;
         this.internalField0248 = localValue3;
         this.internalField0410 = localValue4;
         this.internalField0247 = localValue4.getName().getString();
         this.internalField0283 = localValue5;
         this.internalField0229 = localValue6;
      }

      void internalMethod01420(Entity localValue1, Vec3d localValue2, long localValue3) {
         this.internalField0410 = localValue1;
         this.internalField0247 = localValue1.getName().getString();
         this.internalField0283 = localValue2;
         this.internalField0229 = localValue3;
      }

      boolean internalMethod05274(long localValue1) {
         return localValue1 > this.internalField0229;
      }

      Vec3d internalMethod04202() {
         return this.internalField0410 != null && this.internalField0410.isAlive()
            ? this.internalField0410.getBoundingBox().getCenter().add(0.0, this.internalField0410.getHeight() * 0.5, 0.0)
            : this.internalField0283;
      }

      ItemStack internalMethod04149() {
         return this.internalField0878;
      }

      String internalMethod03017() {
         return this.internalField0248 + " -> " + this.internalField0247;
      }
   }

   static final class InternalType0439 {
      private final Entity internalField0410;
      private final Vec3d internalField0283;
      private final int internalField0227;
      final Entity internalField0411;
      final BlockHitResult internalField0246;
      private final boolean internalField0277;

      InternalType0439(Entity localValue1, Vec3d localValue2, int localValue3, Entity localValue4, BlockHitResult localValue5, boolean localValue6) {
         this.internalField0410 = localValue1;
         this.internalField0283 = localValue2;
         this.internalField0227 = localValue3;
         this.internalField0411 = localValue4;
         this.internalField0246 = localValue5;
         this.internalField0277 = localValue6;
      }

      @Override
      public final String toString() {
         return "InternalType0439[entity=" + this.internalField0410 + ", pos=" + this.internalField0283 + ", ticks=" + this.internalField0227 + ", collidedEntity=" + this.internalField0411 + ", hitResult=" + this.internalField0246 + ", fromHand=" + this.internalField0277 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0410);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0283);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0411);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0246);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0277);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         PredictionModule.InternalType0439 other = (PredictionModule.InternalType0439) localValue1;
         return java.util.Objects.equals(this.internalField0410, other.internalField0410)
            && java.util.Objects.equals(this.internalField0283, other.internalField0283)
            && java.util.Objects.equals(this.internalField0227, other.internalField0227)
            && java.util.Objects.equals(this.internalField0411, other.internalField0411)
            && java.util.Objects.equals(this.internalField0246, other.internalField0246)
            && java.util.Objects.equals(this.internalField0277, other.internalField0277);
      }

      public Entity internalMethod02183() {
         return this.internalField0410;
      }

      public Vec3d internalMethod00398() {
         return this.internalField0283;
      }

      public int internalMethod05447() {
         return this.internalField0227;
      }

      public Entity internalMethod02370() {
         return this.internalField0411;
      }

      public BlockHitResult internalMethod04503() {
         return this.internalField0246;
      }

      public boolean internalMethod05448() {
         return this.internalField0277;
      }
   }
}
