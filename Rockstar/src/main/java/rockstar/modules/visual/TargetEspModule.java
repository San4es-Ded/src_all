package rockstar.modules.visual;










import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.render.*;
import rockstar.client.module.*;
import rockstar.client.event.*;
import rockstar.client.animation.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.render.*;
import rockstar.client.*;
import rockstar.client.module.Module;

import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;
import rockstar.client.compat.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import rockstar.client.compat.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import rockstar.client.compat.BufferRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexFormats;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;
import pyrock.events.render.Render3DEvent;
import pyrock.utility.render.ColorRGBA;

@ModuleInfo(
   name = "Target ESP",
   category = ModuleCategory.VISUALS,
   internalMethod08049 = true,
   internalMethod09633 = "modules.descriptions.target_esp"
)
public class TargetEspModule extends Module {
   private ModeSetting internalField0668;
   private ModeSetting.InternalType0088 internalField0237;
   private ModeSetting.InternalType0088 internalField0238;
   private ModeSetting.InternalType0088 internalField1066;
   private ModeSetting.InternalType0088 internalField1067;
   private BooleanSetting internalField0650;
   private BooleanSetting internalField0651;
   private SliderSetting internalField0383;
   private BooleanSetting internalField1261;
   private ColorSetting internalField0665;
   private BooleanSetting internalField1263;
   private final AnimatedValue internalField0808 = new AnimatedValue(300L, 0.0F, Easing.internalField1631);
   private final AnimatedValue internalField0809 = new AnimatedValue(70L, 0.0F, Easing.internalField1627);
   private final AnimatedValue internalField1321 = new AnimatedValue(100L, 0.0F, Easing.internalField1818);
   private final AnimatedValue internalField1322 = new AnimatedValue(250L, 0.0F, Easing.internalField1631);
   private float internalField1047 = 1.0F;
   private LivingEntity internalField0505;
   private final TargetLensRenderer internalField0440 = new TargetLensRenderer();
   private final EventListener<Render3DEvent> internalField0157 = localValue1 -> {
      if (GameUtils.internalMethod00471()) {
         Entity localValue2 = RockstarClient.getInstance().internalMethod04463().internalMethod04526();
         LivingEntity localValue3 = this.internalField0650.internalMethod04496() && internalField0149.targetedEntity instanceof LivingEntity localValue5
            ? localValue5
            : (localValue2 instanceof LivingEntity localValue4 ? localValue4 : null);
         this.internalField1322.internalMethod06645(Easing.internalField1631);
         this.internalField1322.internalMethod07061(350L);
         this.internalField0808.internalMethod06645(Easing.internalField1626);
         this.internalField0808.internalMethod07062(localValue3 != null);
         this.internalField0809.internalMethod07059(this.internalField0809.internalMethod02881() + 10.0F + 50.0F);
         if (this.internalField0809.internalMethod02881() > Float.MAX_VALUE || Float.isNaN(this.internalField0809.internalMethod02881())) {
            this.internalField0809.internalMethod07060(0.0F);
         }

         if (localValue3 != null) {
            this.internalField0505 = localValue3;
         }

         if (this.internalField0505 != null && this.internalField0808.internalMethod02881() != 0.0F) {
            this.internalField1321.internalMethod07062(this.internalField0505.hurtTime > 0);
            this.internalMethod05954(localValue1);
            MatrixStack localValue7 = localValue1.getMatrices();
            localValue7.push();
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
            boolean localValue8 = internalField0149.world
                     .raycast(
                        new RaycastContext(
                           internalField0149.gameRenderer.getCamera().getCameraPos(),
                           this.internalField0505.getEyePos(),
                           ShapeType.COLLIDER,
                           FluidHandling.NONE,
                           internalField0149.player
                        )
                     )
                     .getType()
                  != Type.MISS
               || this.internalField0505.isTouchingWater();
            this.internalField1322.internalMethod07062(localValue8);
            RenderSystem.disableCull();
            RenderSystem.depthMask(false);
            RenderSystem.enableDepthTest();
            RenderSystem.depthFunc(515);
            this.internalField1047 = 1.0F;
            localValue7.push();
            this.internalMethod01810(localValue7, this.internalField0505);
            localValue7.pop();
            float localValue9 = this.internalField1322.internalMethod02881();
            if (localValue9 > 0.01F) {
               RenderSystem.depthFunc(516);
               this.internalField1047 = localValue9;
               localValue7.push();
               this.internalMethod01810(localValue7, this.internalField0505);
               localValue7.pop();
               RenderSystem.depthFunc(515);
            }

            this.internalField1047 = 1.0F;
            RenderSystem.depthMask(true);
            RenderSystem.setShaderTexture(0, 0);
            RenderSystem.disableBlend();
            RenderSystem.enableCull();
            RenderSystem.disableDepthTest();
            localValue7.pop();
            this.internalMethod06847(localValue1);
         }
      }
   };

   private void internalMethod09630() {
      this.internalField0668 = new ModeSetting(this, "modules.settings.target_esp.mode");
      this.internalField0237 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.target_esp.mode.souls");
      this.internalField0238 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.target_esp.mode.crystals").select();
      this.internalField1066 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.target_esp.mode.circle");
      this.internalField1067 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.target_esp.mode.jello");
      this.internalField0650 = new BooleanSetting(this, "modules.settings.target_esp.ray_trace");
      this.internalField1263 = new BooleanSetting(this, "modules.settings.target_esp.lighting");
      this.internalField0651 = new BooleanSetting(this, "modules.settings.target_esp.distortion", () -> !this.internalField0238.isSelected())
         .internalMethod06630();
      this.internalField0383 = new SliderSetting(
            this, "modules.settings.target_esp.distortion_strength", () -> !this.internalField0238.isSelected() || !this.internalField0651.internalMethod04496()
         )
         .internalMethod05900(0.05F)
         .internalMethod02732(0.15F)
         .internalMethod08673(0.01F)
         .internalMethod08074(0.05F);
      this.internalField1261 = new BooleanSetting(this, "theme.sync").internalMethod06630();
      this.internalField0665 = new ColorSetting(this, "modules.settings.target_esp.color", this.internalField1261::internalMethod04496)
         .internalMethod04886(ThemeColors.internalMethod02531());
   }

   public TargetEspModule() {
      this.internalMethod09630();
      this.internalField0440.internalMethod06763();
   }

   private void internalMethod06847(Render3DEvent localValue1) {
      if (this.internalField0651.internalMethod04496()) {
         if (this.internalField0238.isSelected()) {
            float localValue2 = this.internalField0808.internalMethod02881();
            if (!(localValue2 <= 0.01F)) {
               float localValue3 = this.internalField0383.internalMethod08576() * localValue2;
               if (!(localValue3 <= 0.001F)) {
                  Vec3d localValue4 = localValue1.getCamera().getCameraPos();
                  Vec3d localValue5 = this.internalMethod05069(this.internalField0505);
                  float localValue6 = this.internalField0505.getWidth() * 1.5F;
                  float localValue7 = this.internalField0505.getHeight();
                  float localValue8 = this.internalField0809.internalMethod02881();
                  Matrix4f localValue9 = localValue1.getProjectionMatrix();
                  Matrix4f localValue10 = new Matrix4f(localValue9).mul(localValue1.getPositionMatrix());
                  float localValue11 = localValue9.m11() / localValue9.m00();
                  ArrayList localValue12 = new ArrayList();

                  for (int localValue13 = 0; localValue13 < 360; localValue13 += 20) {
                     float localValue14 = 1.2F - 0.5F * localValue2;
                     float localValue15 = (float)(MathUtils.internalMethod04857((float)Math.toRadians(localValue13 + localValue8 * 0.3F)) * localValue6 * localValue14);
                     float localValue16 = (float)(MathUtils.internalMethod04929((float)Math.toRadians(localValue13 + localValue8 * 0.3F)) * localValue6 * localValue14);
                     float localValue17 = 0.1F + localValue7 * (float)Math.abs(MathUtils.internalMethod04857(localValue13));
                     this.internalMethod05845(localValue12, localValue10, localValue9, localValue5, localValue4, localValue15, localValue17, localValue16, 0.45F);
                  }

                  if (!localValue12.isEmpty()) {
                     this.internalField0440.internalMethod06535(localValue11, localValue3, localValue12);
                  }
               }
            }
         }
      }
   }

   private void internalMethod05845(
      List<TargetLensRenderer.InternalType0350> localValue1, Matrix4f localValue2, Matrix4f localValue3, Vec3d localValue4, Vec3d localValue5, float localValue6, float localValue7, float localValue8, float localValue9
   ) {
      Vector4f localValue10 = localValue2.transform(new Vector4f((float)(localValue4.x + localValue6 - localValue5.x), (float)(localValue4.y + localValue7 - localValue5.y), (float)(localValue4.z + localValue8 - localValue5.z), 1.0F));
      if (!(localValue10.w <= 0.05F)) {
         float localValue11 = localValue10.x / localValue10.w * 0.5F + 0.5F;
         float localValue12 = localValue10.y / localValue10.w * 0.5F + 0.5F;
         float localValue13 = localValue10.z / localValue10.w * 0.5F + 0.5F;
         float localValue14 = Math.min(localValue9 * localValue3.m11() / localValue10.w * 0.5F, 0.35F);
         if (!(localValue11 < -localValue14 * 2.0F) && !(localValue11 > 1.0F + localValue14 * 2.0F) && !(localValue12 < -localValue14 * 2.0F) && !(localValue12 > 1.0F + localValue14 * 2.0F)) {
            localValue1.add(new TargetLensRenderer.InternalType0350(localValue11, localValue12, localValue13, localValue14));
         }
      }
   }

   private void internalMethod01810(MatrixStack localValue1, LivingEntity localValue2) {
      if (this.internalField1066.isSelected()) {
         this.internalMethod09072(localValue1, localValue2);
      } else if (this.internalField0238.isSelected()) {
         this.internalMethod08966(localValue1, localValue2);
      } else if (this.internalField1067.isSelected()) {
         this.internalMethod01307(localValue1, localValue2);
      } else if (this.internalField0237.isSelected()) {
         this.internalMethod09070(localValue1, localValue2);
      }
   }

   private void internalMethod01307(MatrixStack localValue1, LivingEntity localValue2) {
      Camera localValue3 = internalField0149.gameRenderer.getCamera();
      ColorRGBA localValue4 = this.internalField1261.internalMethod04496() ? ThemeColors.internalMethod02531() : this.internalField0665.internalMethod05620();
      Identifier localValue5 = RockstarClient.id("textures/bloom.png");
      float localValue6 = this.internalField0505.getWidth() * 1.45F;
      localValue1.push();
      HudRenderUtils.internalMethod03474(localValue1, this.internalMethod05069(this.internalField0505));
      RenderSystem.setShaderTexture(0, localValue5);
      RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
      BufferBuilder localValue7 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
      float localValue8 = 0.0F;
      float localValue9 = this.internalField0808.internalMethod02881();

      for (int localValue10 = 0; localValue10 < 360; localValue10 += 2) {
         float localValue11 = Math.max(0.5F, 0.7F - 0.2F * localValue8 + 0.2F - 0.2F * localValue9);
         double localValue12 = (localValue10 + this.internalField0809.internalMethod02881()) * (float) (Math.PI / 180.0);
         float localValue14 = (float)(MathUtils.internalMethod04857(localValue12) * localValue6 * localValue11);
         float localValue15 = (float)(MathUtils.internalMethod04929(localValue12) * localValue6 * localValue11);
         float localValue16 = localValue2.getHeight() / 1.75F
            + (float)(localValue2.getHeight() / 2.0F * MathUtils.internalMethod04857(Math.toRadians(this.internalField0809.internalMethod02881() / 1.5F + 30.0F)));
         float localValue17 = 0.2F;
         float localValue18 = 0.8F;
         float localValue19 = 0.2F;

         for (int localValue20 = 0; localValue20 < 15; localValue20++) {
            localValue16 = localValue2.getHeight() / 1.75F
               + (float)(
                  localValue2.getHeight() / 2.0F
                     * MathUtils.internalMethod04857(Math.toRadians(this.internalField0809.internalMethod02881() / 1.5F + localValue20 * 2.0F))
               );
            localValue1.push();
            localValue1.translate(localValue14, localValue16, localValue15);
            localValue1.multiply(localValue3.getRotation());
            RenderPipeline.internalMethod05007(
               localValue1,
               localValue7,
               -localValue19 / 2.0F,
               -localValue19 / 2.0F,
               -localValue17 / 2.0F,
               localValue19,
               localValue19,
               localValue4.withAlpha(localValue4.getAlpha() * localValue9 * (localValue20 / 15.0F) * 0.05F * this.internalField1047)
            );
            localValue1.pop();
         }
      }

      HudRenderUtils.internalMethod05816(localValue7);
      localValue1.pop();
      localValue1.push();
      HudRenderUtils.internalMethod03474(localValue1, this.internalMethod05069(this.internalField0505));
      RenderSystem.setShaderTexture(0, RockstarClient.id("textures/glowing.png"));
      RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
      localValue7 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

      for (int localValue22 = 0; localValue22 < 360; localValue22 += 2) {
         float localValue23 = Math.max(0.5F, 0.7F - 0.2F * localValue8 + 0.2F - 0.2F * localValue9);
         double localValue24 = (localValue22 + this.internalField0809.internalMethod02881()) * (float) (Math.PI / 180.0);
         float localValue25 = (float)(MathUtils.internalMethod04857(localValue24) * localValue6 * localValue23);
         float localValue26 = (float)(MathUtils.internalMethod04929(localValue24) * localValue6 * localValue23);
         float localValue28 = localValue2.getHeight() / 1.75F
            + (float)(localValue2.getHeight() / 2.0F * MathUtils.internalMethod04857(Math.toRadians(this.internalField0809.internalMethod02881() / 1.5F + 30.0F)));
         float localValue29 = 0.2F;
         float localValue30 = 0.2F;
         float localValue31 = 0.2F;
         localValue1.push();
         localValue1.translate(localValue25, localValue28, localValue26);
         localValue1.multiply(localValue3.getRotation());
         RenderPipeline.internalMethod05007(
            localValue1, localValue7, -localValue30 / 2.0F, -localValue30 / 2.0F, -localValue29 / 2.0F, localValue30, localValue30, localValue4.withAlpha(localValue4.getAlpha() * localValue9 * 0.2F * this.internalField1047)
         );
         localValue1.pop();
      }

      HudRenderUtils.internalMethod05816(localValue7);
      localValue1.pop();
   }

   private void internalMethod09072(MatrixStack localValue1, LivingEntity localValue2) {
      Camera localValue3 = internalField0149.gameRenderer.getCamera();
      ColorRGBA localValue4 = this.internalField1261.internalMethod04496() ? ThemeColors.internalMethod02531() : this.internalField0665.internalMethod05620();
      Identifier localValue5 = RockstarClient.id("textures/glowing.png");
      float localValue6 = this.internalField0505.getWidth() * 1.5F;
      HudRenderUtils.internalMethod03474(localValue1, this.internalMethod05069(this.internalField0505));
      RenderSystem.setShaderTexture(0, localValue5);
      RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
      BufferBuilder localValue7 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
      float localValue8 = this.internalField1321.internalMethod02881();
      float localValue9 = this.internalField0808.internalMethod02881();

      for (int localValue10 = 0; localValue10 < 360; localValue10 += 2) {
         if (localValue10 / 45 % 2 != 0) {
            float localValue11 = Math.max(0.5F, 0.7F - 0.2F * localValue8 + 0.2F - 0.2F * localValue9);
            double localValue12 = (localValue10 + this.internalField0809.internalMethod02881()) * (float) (Math.PI / 180.0);
            float localValue14 = (float)(MathUtils.internalMethod04857(localValue12) * localValue6 * localValue11);
            float localValue15 = (float)(MathUtils.internalMethod04929(localValue12) * localValue6 * localValue11);
            float localValue16 = localValue2.getHeight() / 1.75F
               + (float)(localValue2.getHeight() / 2.0F * MathUtils.internalMethod04857(Math.toRadians(this.internalField0809.internalMethod02881() / 2.0F)));
            float localValue17 = 0.3F;
            float localValue18 = 0.8F;
            localValue1.push();
            localValue1.translate(localValue14, localValue16, localValue15);
            localValue1.multiply(localValue3.getRotation());
            RenderPipeline.internalMethod05007(
               localValue1, localValue7, -localValue18 / 2.0F, -localValue18 / 2.0F, -localValue17 / 2.0F, localValue18, localValue18, localValue4.withAlpha(localValue4.getAlpha() * localValue9 * 0.03F * this.internalField1047)
            );
            RenderPipeline.internalMethod05007(
               localValue1, localValue7, -localValue17 / 2.0F, -localValue17 / 2.0F, -localValue17 / 2.0F, localValue17, localValue17, localValue4.withAlpha(localValue4.getAlpha() * localValue9 * 0.7F * this.internalField1047)
            );
            localValue1.pop();
         }
      }

      HudRenderUtils.internalMethod05816(localValue7);
   }

   private void internalMethod08966(MatrixStack localValue1, LivingEntity localValue2) {
      Camera localValue3 = internalField0149.gameRenderer.getCamera();
      Vec3d localValue4 = localValue3.getCameraPos();
      ColorRGBA localValue5 = this.internalField1261.internalMethod04496() ? ThemeColors.internalMethod02531() : this.internalField0665.internalMethod05620();
      float localValue6 = this.internalField0505.getWidth() * 1.5F;
      HudRenderUtils.internalMethod03474(localValue1, this.internalMethod05069(this.internalField0505));
      BufferBuilder localValue7 = DiamondMarkerRenderer.internalMethod05818();

      for (int localValue8 = 0; localValue8 < 360; localValue8 += 20) {
         float localValue9 = 1.2F - 0.5F * this.internalField0808.internalMethod02881();
         float localValue10 = (float)(
            MathUtils.internalMethod04857((float)Math.toRadians(localValue8 + this.internalField0809.internalMethod02881() * 0.3F)) * localValue6 * localValue9
         );
         float localValue11 = (float)(
            MathUtils.internalMethod04929((float)Math.toRadians(localValue8 + this.internalField0809.internalMethod02881() * 0.3F)) * localValue6 * localValue9
         );
         float localValue12 = 0.1F;
         localValue1.push();
         localValue1.translate(localValue10, 0.1F + localValue2.getHeight() * Math.abs(MathUtils.internalMethod04857(localValue8)), localValue11);
         Vec3d localValue13 = this.internalMethod05069(this.internalField0505).add(localValue10, 1.0, localValue11);
         Vec3d localValue14 = localValue2.getEntityPos().add(0.0, localValue2.getHeight() / 2.0, 0.0);
         Vector3f localValue15 = new Vector3f((float)(localValue14.x - localValue13.x), (float)(localValue14.y - localValue13.y), (float)(localValue14.z - localValue13.z)).normalize();
         Vector3f localValue16 = new Vector3f(0.0F, 1.0F, 0.0F);
         Quaternionf localValue17 = new Quaternionf().rotationTo(localValue16, localValue15);
         localValue1.multiply(localValue17);
         DiamondMarkerRenderer.internalMethod04026(
            localValue1, localValue7, 0.0F, 0.0F, 0.0F, localValue12, localValue5.withAlpha(255.0F * this.internalField0808.internalMethod02881() * this.internalField1047)
         );
         localValue1.pop();
      }

      BufferRenderer.drawWithGlobalProgram(localValue7.end());
      Identifier localValue18 = RockstarClient.id("textures/bloom.png");
      RenderSystem.setShaderTexture(0, localValue18);
      RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
      BufferBuilder localValue19 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
      float localValue20 = 1.0F;

      for (int localValue21 = 0; localValue21 < 360; localValue21 += 20) {
         float localValue22 = 1.2F - 0.5F * this.internalField0808.internalMethod02881();
         float localValue23 = (float)(
            MathUtils.internalMethod04857((float)Math.toRadians(localValue21 + this.internalField0809.internalMethod02881() * 0.3F)) * localValue6 * localValue22
         );
         float localValue24 = (float)(
            MathUtils.internalMethod04929((float)Math.toRadians(localValue21 + this.internalField0809.internalMethod02881() * 0.3F)) * localValue6 * localValue22
         );
         float localValue25 = 0.1F;
         localValue1.push();
         localValue1.translate(localValue23, 0.1F + localValue2.getHeight() * Math.abs(MathUtils.internalMethod04857(localValue21)), localValue24);
         localValue1.multiply(localValue3.getRotation());
         RenderPipeline.internalMethod05007(
            localValue1,
            localValue19,
            -localValue20 / 2.0F,
            -localValue20 / 2.0F,
            0.0,
            localValue20,
            localValue20,
            localValue5.withAlpha(255.0F * this.internalField0808.internalMethod02881() * 0.2F * this.internalField1047)
         );
         localValue1.pop();
      }

      HudRenderUtils.internalMethod05816(localValue19);
   }

   private void internalMethod09070(MatrixStack localValue1, LivingEntity localValue2) {
      Camera localValue3 = internalField0149.gameRenderer.getCamera();
      ColorRGBA localValue4 = this.internalField1261.internalMethod04496() ? ThemeColors.internalMethod02531() : this.internalField0665.internalMethod05620();
      Identifier localValue5 = RockstarClient.id("textures/glowing.png");
      float localValue6 = this.internalField0505.getWidth() * 1.5F;
      RenderSystem.setShaderTexture(0, localValue5);
      RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
      BufferBuilder localValue7 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
      HudRenderUtils.internalMethod03474(localValue1, this.internalMethod05069(this.internalField0505));
      byte localValue8 = 2;
      byte localValue9 = 0;
      byte localValue10 = 0;
      int localValue11 = 0;

      for (int localValue12 = 0; localValue12 < 360; localValue12 += localValue8) {
         float localValue13 = 0.23F + 0.005F * localValue9;
         float localValue14 = 0.7F + 0.005F * localValue9;
         if (localValue10 > 0) {
            localValue10 -= localValue8;
         } else {
            localValue9 += localValue8;
            if (localValue9 > 50) {
               localValue10 = 100;
               localValue9 = 0;
               localValue11++;
            } else {
               float localValue15 = Math.max(0.5F, 1.2F - 0.5F * this.internalField0808.internalMethod02881());
               float localValue16 = (float)(
                  MathUtils.internalMethod04857((float)Math.toRadians(localValue12 + this.internalField0809.internalMethod02881() * 1.0F)) * localValue6 * localValue15
               );
               float localValue17 = (float)(
                  MathUtils.internalMethod04929((float)Math.toRadians(localValue12 + this.internalField0809.internalMethod02881() * 1.0F)) * localValue6 * localValue15
               );
               localValue1.push();
               localValue1.translate(
                  localValue16,
                  this.internalField0505.getHeight() / 1.5F
                     + this.internalField0505.getHeight() / 3.0F
                        * MathUtils.internalMethod04857(Math.toRadians(localValue12 / 2.0F + this.internalField0809.internalMethod02881() / 5.0F)),
                  localValue17
               );
               localValue1.multiply(localValue3.getRotation());
               RenderPipeline.internalMethod05007(
                  localValue1,
                  localValue7,
                  -localValue14 / 2.0F,
                  -localValue14 / 2.0F,
                  -localValue13 / 2.0F,
                  localValue14,
                  localValue14,
                  localValue4.withAlpha(localValue4.getAlpha() * this.internalField0808.internalMethod02881() * 0.02F * this.internalField1047)
               );
               RenderPipeline.internalMethod05007(
                  localValue1,
                  localValue7,
                  -localValue13 / 2.0F,
                  -localValue13 / 2.0F,
                  -localValue13 / 2.0F,
                  localValue13,
                  localValue13,
                  localValue4.withAlpha(localValue4.getAlpha() * this.internalField0808.internalMethod02881() * 0.5F * this.internalField1047)
               );
               localValue1.pop();
            }
         }
      }

      HudRenderUtils.internalMethod05816(localValue7);
   }

   private void internalMethod05954(Render3DEvent localValue1) {
      if (this.internalField1263.internalMethod04496()) {
         if (RenderPipeline.internalField0344 != null) {
            float localValue2 = 1.0F * this.internalField0808.internalMethod02881();
            if (!(localValue2 <= 0.001F)) {
               List localValue3 = this.internalMethod03194(this.internalField0505);
               if (!localValue3.isEmpty()) {
                  ColorRGBA localValue4 = this.internalField1261.internalMethod04496()
                     ? ThemeColors.internalMethod02531()
                     : this.internalField0665.internalMethod05620();
                  float localValue5 = localValue4.getRed() / 255.0F;
                  float localValue6 = localValue4.getGreen() / 255.0F;
                  float localValue7 = localValue4.getBlue() / 255.0F;
                  float localValue8 = localValue2 / localValue3.size();
                  Vec3d localValue9 = localValue1.getCamera().getCameraPos();
                  Vec3d localValue10 = this.internalMethod05069(this.internalField0505);
                  ArrayList localValue11 = new ArrayList(localValue3.size());

                  for (Vec3d localValue13 : (Iterable<Vec3d>)(Iterable<?>)localValue3) {
                     localValue11.add(
                        new ParticleLightShader.InternalType0228(
                           (float)(localValue10.x + localValue13.x - localValue9.x),
                           (float)(localValue10.y + localValue13.y - localValue9.y),
                           (float)(localValue10.z + localValue13.z - localValue9.z),
                           3.5F,
                           localValue5,
                           localValue6,
                           localValue7,
                           localValue8
                        )
                     );
                  }

                  Matrix4f localValue14 = new Matrix4f(localValue1.getProjectionMatrix()).mul(localValue1.getPositionMatrix()).invert();
                  RenderPipeline.internalField0344.internalMethod02901(localValue14, 1.0F, localValue11);
               }
            }
         }
      }
   }

   private List<Vec3d> internalMethod03194(LivingEntity localValue1) {
      float localValue2 = this.internalField0808.internalMethod02881();
      float localValue3 = this.internalField0809.internalMethod02881();
      double localValue4 = Math.max(0.1, (double)localValue1.getHeight());
      ArrayList localValue6 = new ArrayList(6);
      if (this.internalField0238.isSelected()) {
         double localValue21 = localValue1.getWidth() * 1.5 * (1.2 - 0.5 * localValue2);

         for (int localValue22 = 0; localValue22 < 360; localValue22 += 60) {
            double localValue24 = Math.toRadians(localValue22 + localValue3 * 0.3);
            localValue6.add(
               new Vec3d(
                  MathUtils.internalMethod04857(localValue24) * localValue21,
                  0.1 + localValue4 * Math.abs(MathUtils.internalMethod04857(localValue22)),
                  MathUtils.internalMethod04929(localValue24) * localValue21
               )
            );
         }

         return localValue6;
      } else if (this.internalField0237.isSelected()) {
         double localValue20 = localValue1.getWidth() * 1.5 * Math.max(0.5, 1.2 - 0.5 * localValue2);

         for (int localValue9 = 0; localValue9 < 360; localValue9 += 60) {
            double localValue23 = Math.toRadians(localValue9 + localValue3);
            localValue6.add(
               new Vec3d(
                  MathUtils.internalMethod04857(localValue23) * localValue20,
                  localValue4 / 1.5 + localValue4 / 3.0 * MathUtils.internalMethod04857(Math.toRadians(localValue9 / 2.0 + localValue3 / 5.0)),
                  MathUtils.internalMethod04929(localValue23) * localValue20
               )
            );
         }

         return localValue6;
      } else {
         boolean localValue7 = this.internalField1067.isSelected();
         double localValue8 = localValue7 ? 0.0 : this.internalField1321.internalMethod02881();
         double localValue10 = localValue1.getWidth() * (localValue7 ? 1.45 : 1.5) * Math.max(0.5, 0.7 - 0.2 * localValue8 + 0.2 - 0.2 * localValue2);
         double localValue12 = localValue7 ? localValue3 / 1.5 + 30.0 : localValue3 / 2.0;
         double localValue14 = localValue4 / 1.75 + localValue4 / 2.0 * MathUtils.internalMethod04857(Math.toRadians(localValue12));
         int localValue16 = localValue7 ? 30 : 15;

         for (int localValue17 = 0; localValue17 < 360; localValue17 += localValue16) {
            if (localValue7 || localValue17 / 45 % 2 != 0) {
               double localValue18 = Math.toRadians(localValue17 + localValue3);
               localValue6.add(new Vec3d(MathUtils.internalMethod04857(localValue18) * localValue10, localValue14, MathUtils.internalMethod04929(localValue18) * localValue10));
            }
         }

         return localValue6;
      }
   }

   private Vec3d internalMethod05069(LivingEntity localValue1) {
      float localValue2 = MinecraftClient.getInstance().getRenderTickCounter().getTickProgress(false);
      return new Vec3d(
         MathHelper.lerp(localValue2, localValue1.lastX, localValue1.getX()), MathHelper.lerp(localValue2, localValue1.lastY, localValue1.getY()), MathHelper.lerp(localValue2, localValue1.lastZ, localValue1.getZ())
      );
   }

   @Override
   public void internalMethod08229() {
      super.internalMethod08229();
   }
}
