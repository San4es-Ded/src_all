package rockstar.modules.visual;








import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.render.*;
import rockstar.client.module.*;
import rockstar.client.event.*;
import rockstar.client.animation.*;
import rockstar.client.*;
import rockstar.client.module.Module;

import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;
import rockstar.client.compat.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import rockstar.client.compat.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import rockstar.client.compat.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexFormats;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import pyrock.events.game.EntityDeathEvent;
import pyrock.events.game.WorldChangeEvent;
import pyrock.events.render.Render3DEvent;
import pyrock.utility.render.ColorRGBA;

@ModuleInfo(
   name = "Kill Effects",
   category = ModuleCategory.VISUALS,
   internalMethod08049 = true,
   internalMethod09633 = "modules.descriptions.kill_effects"
)
public class KillEffectsModule extends Module {
   private final List<KillEffectsModule.InternalType0318> internalField0416 = new CopyOnWriteArrayList<>();
   private final List<KillEffectsModule.InternalType0317> internalField0417 = new CopyOnWriteArrayList<>();
   static final Random internalField0362 = new Random();
   private ModeSetting internalField0668;
   private ModeSetting.InternalType0088 internalField0237;
   private ModeSetting.InternalType0088 internalField0238;
   private ModeSetting internalField0669;
   private ModeSetting.InternalType0088 internalField1066;
   private ModeSetting.InternalType0088 internalField1067;
   private BooleanSetting internalField0650;
   private ColorSetting internalField0665;
   private final EventListener<EntityDeathEvent> internalField0157 = localValue1 -> {
      if (!localValue1.getEntity().isRemoved() && !(localValue1.getEntity() instanceof ArmorStandEntity)) {
         ColorRGBA localValue2 = this.internalField0650.internalMethod04496() ? ThemeColors.internalMethod02531() : this.internalField0665.internalMethod05620();
         if (this.internalField0668.internalMethod06103(this.internalField0237)) {
            this.internalField0416.add(new KillEffectsModule.InternalType0318(localValue1.getEntity().getEntityPos(), localValue2));
         } else if (this.internalField0668.internalMethod06103(this.internalField0238)) {
            this.internalMethod04423(localValue1.getEntity(), localValue2);
         }
      }
   };
   private final EventListener<Render3DEvent> internalField0158 = localValue1 -> {
      MatrixStack localValue2 = localValue1.getMatrices();
      Camera localValue3 = internalField0149.gameRenderer.getCamera();
      localValue2.push();
      RenderSystem.enableBlend();
      RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
      RenderSystem.enableDepthTest();
      RenderSystem.disableCull();
      RenderSystem.depthMask(false);
      Identifier localValue4 = RockstarClient.id("textures/bloom.png");
      RenderSystem.setShaderTexture(0, localValue4);
      RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
      BufferBuilder localValue5 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

      for (KillEffectsModule.InternalType0318 localValue7 : this.internalField0416) {
         localValue7.internalMethod05206(localValue5, localValue1.getMatrices(), localValue3);
         if (localValue7.internalField0808.internalMethod02881() == 1.0F) {
            localValue7.internalField0277 = false;
         }
      }

      for (KillEffectsModule.InternalType0317 localValue10 : this.internalField0417) {
         if (!localValue10.internalMethod05356()) {
            localValue10.internalMethod05355();
            localValue10.internalMethod05810(localValue1, localValue5);
         }
      }

      this.internalField0417.removeIf(KillEffectsModule.InternalType0317::internalMethod05356);
      BuiltBuffer localValue9 = localValue5.endNullable();
      if (localValue9 != null) {
         BufferRenderer.drawWithGlobalProgram(localValue9);
      }

      RenderSystem.depthMask(true);
      RenderSystem.setShaderTexture(0, 0);
      RenderSystem.disableBlend();
      RenderSystem.enableCull();
      RenderSystem.disableDepthTest();
      localValue2.pop();
      this.internalField0416.removeIf(localValue0 -> !localValue0.internalField0277 && localValue0.internalField0808.internalMethod02881() == 0.0F);
   };
   private final EventListener<WorldChangeEvent> internalField1028 = localValue1 -> {
      this.internalField0416.clear();
      this.internalField0417.clear();
   };

   public KillEffectsModule() {
      this.internalMethod09741();
   }

   private void internalMethod09741() {
      this.internalField0668 = new ModeSetting(this, "modules.settings.kill_effects.mode");
      this.internalField0237 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.kill_effects.mode.lightning");
      this.internalField0238 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.kill_effects.mode.particles").select();
      this.internalField0669 = new ModeSetting(
         this, "modules.settings.kill_effects.particlePhysics", () -> !this.internalField0668.internalMethod06103(this.internalField0238)
      );
      this.internalField1066 = new ModeSetting.InternalType0088(this.internalField0669, "modules.settings.kill_effects.particlePhysics.gravity");
      this.internalField1067 = new ModeSetting.InternalType0088(this.internalField0669, "modules.settings.kill_effects.particlePhysics.scatter").select();
      this.internalField0650 = new BooleanSetting(this, "theme.sync").internalMethod06630();
      this.internalField0665 = new ColorSetting(this, "modules.settings.kill_effects.color", this.internalField0650::internalMethod04496)
         .internalMethod04886(ThemeColors.internalMethod02531());
   }

   private void internalMethod04423(LivingEntity localValue1, ColorRGBA localValue2) {
      Vec3d localValue3 = localValue1.getEntityPos();
      float localValue4 = localValue1.getHeight();
      float localValue5 = localValue1.getWidth();
      float localValue6 = (float)Math.toRadians(-localValue1.bodyYaw + 90.0F);
      boolean localValue7 = this.internalField0669.internalMethod06103(this.internalField1066);
      short localValue8 = 250;
      float localValue9 = localValue4 - 0.2F;
      float localValue10 = localValue5 * 0.4F;
      this.internalMethod03253(localValue3.add(0.0, localValue9, 0.0), localValue10, localValue8 / 10, localValue6, localValue2, localValue7);
      float localValue11 = localValue4 * 0.85F;
      float localValue12 = localValue4 * 0.4F;
      float localValue13 = localValue5 * 0.4F;
      float localValue14 = localValue5 * 0.2F;
      this.internalMethod01997(localValue3, localValue12, localValue11, localValue13, localValue14, localValue6, localValue8 / 4, localValue2, localValue7);
      float localValue15 = localValue4 * 0.4F;
      float localValue16 = localValue5 * 0.15F;

      for (int localValue17 = -1; localValue17 <= 1; localValue17 += 2) {
         Vec3d localValue18 = new Vec3d(Math.sin(localValue6) * localValue5 * 0.5 * localValue17, localValue4 * 0.75F, Math.cos(localValue6) * localValue5 * 0.5 * localValue17);
         this.internalMethod04615(localValue3.add(localValue18), localValue15, localValue16, localValue6, localValue8 / 8, localValue2, localValue7);
      }

      float localValue21 = localValue4 * 0.45F;
      float localValue22 = localValue5 * 0.15F;

      for (int localValue19 = -1; localValue19 <= 1; localValue19 += 2) {
         Vec3d localValue20 = new Vec3d(Math.sin(localValue6) * localValue5 * 0.15F * localValue19, localValue4 * 0.4F, Math.cos(localValue6) * localValue5 * 0.15F * localValue19);
         this.internalMethod04615(localValue3.add(localValue20), localValue21, localValue22, localValue6, localValue8 / 6, localValue2, localValue7);
      }
   }

   private void internalMethod03253(Vec3d localValue1, float localValue2, int localValue3, float localValue4, ColorRGBA localValue5, boolean localValue6) {
      for (int localValue7 = 0; localValue7 < localValue3; localValue7++) {
         float localValue8 = internalField0362.nextFloat() * (float) Math.PI * 2.0F;
         float localValue9 = (float)Math.acos(2.0F * internalField0362.nextFloat() - 1.0F);
         float localValue10 = localValue2 * (float)Math.cbrt(internalField0362.nextFloat());
         float localValue11 = localValue10 * (float)(Math.sin(localValue9) * Math.cos(localValue8));
         float localValue12 = localValue10 * (float)(Math.sin(localValue9) * Math.sin(localValue8));
         float localValue13 = localValue10 * (float)Math.cos(localValue9);
         float localValue14 = localValue6 ? 0.02F : 0.008F;
         float localValue15 = (internalField0362.nextFloat() - 0.5F) * localValue14;
         float localValue16 = localValue6 ? 0.03F + internalField0362.nextFloat() * 0.04F : (internalField0362.nextFloat() - 0.5F) * 0.008F;
         float localValue17 = (internalField0362.nextFloat() - 0.5F) * localValue14;
         this.internalField0417.add(new KillEffectsModule.InternalType0317(localValue1, localValue11, localValue12, localValue13, localValue15, localValue16, localValue17, localValue5, localValue6));
      }
   }

   private void internalMethod01997(Vec3d localValue1, float localValue2, float localValue3, float localValue4, float localValue5, float localValue6, int localValue7, ColorRGBA localValue8, boolean localValue9) {
      for (int localValue10 = 0; localValue10 < localValue7; localValue10++) {
         float localValue11 = (internalField0362.nextFloat() - 0.5F) * localValue4 * 2.0F;
         float localValue12 = localValue2 + internalField0362.nextFloat() * (localValue3 - localValue2);
         float localValue13 = (internalField0362.nextFloat() - 0.5F) * localValue5 * 2.0F;
         float localValue14 = (float)(localValue11 * Math.cos(localValue6) - localValue13 * Math.sin(localValue6));
         float localValue15 = (float)(localValue11 * Math.sin(localValue6) + localValue13 * Math.cos(localValue6));
         float localValue16 = localValue9 ? 0.025F : 0.01F;
         float localValue17 = (internalField0362.nextFloat() - 0.5F) * localValue16;
         float localValue18 = localValue9 ? 0.04F + internalField0362.nextFloat() * 0.05F : (internalField0362.nextFloat() - 0.5F) * 0.01F;
         float localValue19 = (internalField0362.nextFloat() - 0.5F) * localValue16;
         this.internalField0417.add(new KillEffectsModule.InternalType0317(localValue1, localValue14, localValue12, localValue15, localValue17, localValue18, localValue19, localValue8, localValue9));
      }
   }

   private void internalMethod04615(Vec3d localValue1, float localValue2, float localValue3, float localValue4, int localValue5, ColorRGBA localValue6, boolean localValue7) {
      for (int localValue8 = 0; localValue8 < localValue5; localValue8++) {
         float localValue9 = internalField0362.nextFloat();
         float localValue10 = (internalField0362.nextFloat() - 0.5F) * localValue3 * 2.0F;
         float localValue11 = -localValue9 * localValue2;
         float localValue12 = (internalField0362.nextFloat() - 0.5F) * localValue3 * 2.0F;
         float localValue13 = localValue7 ? 0.018F : 0.006F;
         float localValue14 = (internalField0362.nextFloat() - 0.5F) * localValue13;
         float localValue15 = localValue7 ? 0.025F + internalField0362.nextFloat() * 0.035F : (internalField0362.nextFloat() - 0.5F) * 0.006F;
         float localValue16 = (internalField0362.nextFloat() - 0.5F) * localValue13;
         this.internalField0417.add(new KillEffectsModule.InternalType0317(localValue1, localValue10, localValue11, localValue12, localValue14, localValue15, localValue16, localValue6, localValue7));
      }
   }

   class InternalType0317 {
      double internalField0194;
      double internalField0193;
      double internalField1045;
      float internalField0205;
      float internalField0206;
      float internalField1048;
      long internalField0229;
      long internalField0230;
      float internalField1047;
      long internalField1059;
      boolean internalField0277;
      float internalField1049;
      ColorRGBA internalField0777;
      static final float internalField1046 = 0.5F;

      InternalType0317(Vec3d localValue2, float localValue3, float localValue4, float localValue5, float localValue6, float localValue7, float localValue8, ColorRGBA localValue9, boolean localValue10) {
         this.internalField0194 = localValue2.x + localValue3;
         this.internalField0193 = localValue2.y + localValue4;
         this.internalField1045 = localValue2.z + localValue5;
         this.internalField0777 = localValue9;
         this.internalField0229 = System.currentTimeMillis();
         this.internalField0230 = this.internalField0229;
         this.internalField0277 = localValue10;
         this.internalField1047 = 0.005F + KillEffectsModule.internalField0362.nextFloat() * 0.005F;
         this.internalField1059 = 1500 + KillEffectsModule.internalField0362.nextInt(1500);
         this.internalField1049 = localValue10 ? 0.999F : 0.995F;
         float localValue11 = localValue10 ? 0.04F : 0.03F;
         this.internalField0205 = (KillEffectsModule.internalField0362.nextFloat() - 0.5F) * localValue11;
         this.internalField0206 = localValue10
            ? 0.025F + KillEffectsModule.internalField0362.nextFloat() * 0.035F
            : (KillEffectsModule.internalField0362.nextFloat() - 0.5F) * 0.03F;
         this.internalField1048 = (KillEffectsModule.internalField0362.nextFloat() - 0.5F) * localValue11;
      }

      void internalMethod05355() {
         float localValue1 = 0.5F;
         long localValue2 = System.currentTimeMillis();
         float localValue4 = (float)(localValue2 - this.internalField0230) / 16.67F;
         this.internalField0230 = localValue2;
         if (localValue4 > 5.0F) {
            localValue4 = 5.0F;
         }

         if (this.internalField0277) {
            this.internalField0206 = this.internalField0206 - this.internalField1047 * localValue4;
         }

         float localValue5 = (float)Math.pow(this.internalField1049, localValue4);
         this.internalField0205 *= localValue5;
         this.internalField0206 *= localValue5;
         this.internalField1048 *= localValue5;
         double localValue6 = this.internalField0194 + this.internalField0205 * localValue4;
         double localValue8 = this.internalField0193 + this.internalField0206 * localValue4;
         double localValue10 = this.internalField1045 + this.internalField1048 * localValue4;
         if (this.internalField0277 && MinecraftClientAccess.internalField0149.world != null) {
            BlockPos localValue12 = BlockPos.ofFloored(this.internalField0194, localValue8 - 0.05F, this.internalField1045);
            if (!MinecraftClientAccess.internalField0149.world.getBlockState(localValue12).isAir()) {
               this.internalField0206 = -this.internalField0206 * localValue1;
               localValue8 = this.internalField0193;
            }

            BlockPos localValue13 = BlockPos.ofFloored(localValue6, this.internalField0193, this.internalField1045);
            if (!MinecraftClientAccess.internalField0149.world.getBlockState(localValue13).isAir()) {
               this.internalField0205 = -this.internalField0205 * localValue1;
               localValue6 = this.internalField0194;
            }

            BlockPos localValue14 = BlockPos.ofFloored(this.internalField0194, this.internalField0193, localValue10);
            if (!MinecraftClientAccess.internalField0149.world.getBlockState(localValue14).isAir()) {
               this.internalField1048 = -this.internalField1048 * localValue1;
               localValue10 = this.internalField1045;
            }
         }

         if (Math.abs(this.internalField0206) <= 1.0E-4F) {
            this.internalField0205 = 0.0F;
            this.internalField1048 = 0.0F;
         }

         this.internalField0194 = localValue6;
         this.internalField0193 = localValue8;
         this.internalField1045 = localValue10;
      }

      float internalMethod05354() {
         return MathHelper.clamp((float)(System.currentTimeMillis() - this.internalField0229) / (float)this.internalField1059, 0.0F, 1.0F);
      }

      float internalMethod05358() {
         return 1.0F - this.internalMethod05354();
      }

      boolean internalMethod05356() {
         return System.currentTimeMillis() - this.internalField0229 > this.internalField1059;
      }

      void internalMethod05810(Render3DEvent localValue1, BufferBuilder localValue2) {
         float localValue3 = 0.1F;
         float localValue4 = 0.5F;
         MatrixStack localValue5 = localValue1.getMatrices();
         Camera localValue6 = MinecraftClientAccess.internalField0149.gameRenderer.getCamera();
         float localValue7 = this.internalMethod05358();
         localValue5.push();
         HudRenderUtils.internalMethod03474(localValue5, new Vec3d(this.internalField0194, this.internalField0193, this.internalField1045));
         localValue5.multiply(localValue6.getRotation());
         RenderPipeline.internalMethod05007(localValue5, localValue2, -localValue3 / 2.0F, -localValue3 / 2.0F, 0.0, localValue3, localValue3, this.internalField0777.mulAlpha(0.9F * localValue7));
         RenderPipeline.internalMethod05007(localValue5, localValue2, -localValue4 / 2.0F, -localValue4 / 2.0F, 0.0, localValue4, localValue4, this.internalField0777.mulAlpha(0.15F * localValue7));
         localValue5.pop();
      }
   }

   static class InternalType0318 {
      final Vec3d internalField0283;
      final ColorRGBA internalField0777;
      boolean internalField0277 = true;
      final AnimatedValue internalField0808 = new AnimatedValue(300L, 0.0F, Easing.internalField1626);
      final List<Vec3d> internalField0416 = new ArrayList<>();

      public InternalType0318(Vec3d localValue1, ColorRGBA localValue2) {
         this.internalField0283 = localValue1;
         this.internalField0777 = localValue2;
         Vec3d localValue3 = localValue1;

         for (int localValue4 = 0; localValue4 < 200; localValue4++) {
            this.internalField0416.add(localValue3 = localValue3.add(MathUtils.internalMethod05368(-0.4F, 0.4F), 0.25, MathUtils.internalMethod05368(-0.4F, 0.4F)));
         }
      }

      void internalMethod05206(BufferBuilder localValue1, MatrixStack localValue2, Camera localValue3) {
         this.internalField0808.internalMethod06645(Easing.internalField1925);
         this.internalField0808.internalMethod07061(500L);
         this.internalField0808.internalMethod07062(this.internalField0277);

         for (Vec3d localValue5 : this.internalField0416) {
            float localValue6 = (float)(2.0 + 5.0 * (localValue5.y - this.internalField0283.y) / 50.0);
            localValue2.push();
            HudRenderUtils.internalMethod03474(localValue2, localValue5);
            localValue2.multiply(localValue3.getRotation());
            RenderPipeline.internalMethod05007(
               localValue2,
               localValue1,
               -localValue6 / 2.0F,
               -localValue6 / 2.0F,
               0.0,
               localValue6,
               localValue6,
               this.internalField0777.withAlpha(255.0F * this.internalField0808.internalMethod02881() * 0.4F)
            );
            localValue2.pop();
         }
      }
   }
}
