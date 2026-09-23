package rockstar.client.render;




import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.render.*;
import rockstar.client.internal.game.*;
import rockstar.client.internal.core.*;
import rockstar.modules.combat.*;
import rockstar.modules.movement.*;
import rockstar.modules.visual.*;
import rockstar.modules.player.*;
import rockstar.modules.other.*;

import com.mojang.blaze3d.systems.ProjectionType;
import rockstar.client.compat.RenderSystem;
import java.util.List;
import lombok.Generated;
import net.minecraft.client.gl.Framebuffer;
import rockstar.client.compat.ShaderProgramKeys;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.BlockGhostLayers;
import net.minecraft.client.render.BufferBuilder;
import rockstar.client.compat.BufferRenderer;
import net.minecraft.client.render.GlowRenderLayers;
import net.minecraft.client.render.SmoothItemLayers;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MatrixUtil;
import net.minecraft.util.math.Vec2f;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Vector3f;
import pyrock.utility.render.ColorRGBA;
import pyrock.utility.render.Rect;

public final class RenderPipeline implements MinecraftClientAccess, WindowAccess {
   public static final float internalField0205 = 0.5F;
   public static final FixedTickScheduler internalField0857 = new FixedTickScheduler(true);
   public static ShaderProgramBase internalField0104;
   private static ShaderProgramBase internalField0994;
   private static ShaderProgramBase internalField1419;
   private static ShaderProgramBase internalField1418;
   private static ShaderProgramBase internalField1421;
   private static ShaderProgramBase internalField1422;
   private static ShaderProgramBase internalField1420;
   private static ShaderProgramBase internalField1417;
   private static ShaderProgramBase internalField1415;
   private static ShaderProgramBase internalField1416;
   private static ShaderProgramBase internalField1675;
   private static ShaderProgramBase internalField1674;
   public static InterfaceBlurRenderer internalField0314;
   public static KawaseBlurRenderer internalField0312;
   public static GlowBloomRenderer internalField0320;
   public static GlowEntitySolidShader internalField0319;
   public static AccentTimeShader internalField0439;
   public static AccentTimeShader internalField0438;
   public static AccentTimeShader internalField1153;
   public static AccentTimeShader internalField1152;
   public static ShaderPair internalField0315;
   public static ShaderPair internalField0316;
   public static SaturationIsolationRenderer internalField0437;
   public static WetWorldRenderer internalField0446;
   public static ParticleLightRenderer internalField0344;
   public static MirrorCompositeShader internalField0343;
   public static ItemCenterShader internalField0336;
   public static ItemCenterShader internalField0337;
   public static ItemCenterShader internalField1128;
   private static ShaderProgramBase internalField1673;
   private static ShaderProgramBase internalField1670;
   private static ShaderProgramBase internalField1671;
   public static ShaderProgramBase internalField0103;
   public static ShaderProgramBase internalField0996;
   public static ShaderProgramBase internalField0993;
   public static AdaptiveStateRenderer internalField0105;
   private static ShaderProgramBase internalField1672;
   public static ShaderProgramBase internalField0995;
   private static final ManagedFramebuffer internalField0769 = new ManagedFramebuffer(false);
   private static int internalField0227 = -1;

   public static void internalMethod07025(int localValue0) {
      internalField0227 = localValue0;
   }

   public static void internalMethod01905() {
      internalField0227 = -1;
   }

   private static int internalMethod01904() {
      return internalField0227 >= 0 && !UiBatchRenderer.internalField0277 && internalField0312.internalMethod02312(internalField0227)
         ? internalField0312.internalMethod02310(internalField0227)
         : InterfaceBlurRenderer.internalMethod05153();
   }

   private static boolean internalMethod03563(ColorRGBA localValue0) {
      return localValue0 == null || localValue0.getAlpha() <= 0.5F;
   }

   private static boolean internalMethod05199(float localValue0, float localValue1) {
      return localValue0 <= 0.0F || localValue1 <= 0.0F;
   }

   public static void internalMethod01907() {
      internalField0104 = new ShaderProgramBase(RockstarClient.id("rectangle/data"), VertexFormats.POSITION_COLOR);
      internalField0994 = new ShaderProgramBase(RockstarClient.id("squircle/data"), VertexFormats.POSITION_COLOR);
      internalField1418 = new ShaderProgramBase(RockstarClient.id("squircle_texture/data"), VertexFormats.POSITION_TEXTURE_COLOR);
      internalField1421 = new ShaderProgramBase(RockstarClient.id("proj_squircle/data"), VertexFormats.POSITION_TEXTURE_COLOR);
      internalField1419 = new ShaderProgramBase(RockstarClient.id("texture/data"), VertexFormats.POSITION_TEXTURE_COLOR);
      internalField1422 = new ShaderProgramBase(RockstarClient.id("border/data"), VertexFormats.POSITION_COLOR);
      internalField1420 = new ShaderProgramBase(RockstarClient.id("dashed_border/data"), VertexFormats.POSITION_COLOR);
      internalField1417 = new ShaderProgramBase(RockstarClient.id("bezier/data"), VertexFormats.POSITION_COLOR);
      internalField1415 = new ShaderProgramBase(RockstarClient.id("squircle_border/data"), VertexFormats.POSITION_COLOR);
      internalField1416 = new ShaderProgramBase(RockstarClient.id("loading/data"), VertexFormats.POSITION_COLOR);
      internalField1675 = new ShaderProgramBase(RockstarClient.id("liquidglass/data"), VertexFormats.POSITION_TEXTURE_COLOR);
      internalField1674 = new ShaderProgramBase(RockstarClient.id("gradient_rectangle/data"), VertexFormats.POSITION_COLOR);
      internalField1673 = new ShaderProgramBase(RockstarClient.id("metaball/data"), VertexFormats.POSITION_TEXTURE_COLOR);
      internalField1670 = new ShaderProgramBase(RockstarClient.id("islandblob/data"), VertexFormats.POSITION_COLOR);
      internalField0103 = new ShaderProgramBase(RockstarClient.id("mapped_texture/data"), VertexFormats.POSITION_TEXTURE_COLOR);
      internalField0996 = new ShaderProgramBase(RockstarClient.id("ui_universal/data"), VertexFormats.POSITION_TEXTURE_COLOR_LIGHT);
      internalField0993 = new ShaderProgramBase(RockstarClient.id("adaptive_ui/data"), VertexFormats.POSITION_TEXTURE_COLOR_LIGHT);
      internalField0105 = new AdaptiveStateRenderer();
      internalField0105.internalMethod02780();
      internalField1672 = new ShaderProgramBase(RockstarClient.id("backdrop_blur/data"), VertexFormats.POSITION_TEXTURE_COLOR);
      internalField0995 = new ShaderProgramBase(RockstarClient.id("ui_stream/data"), VertexFormats.POSITION_TEXTURE_COLOR);
      internalField1671 = new ShaderProgramBase(RockstarClient.id("arc/data"), VertexFormats.POSITION_COLOR);
      internalField0314 = new InterfaceBlurRenderer();
      internalField0314.internalMethod05154();
      internalField0312 = new KawaseBlurRenderer();
      internalField0312.internalMethod02986();
      internalField0344 = new ParticleLightRenderer();
      internalField0344.internalMethod04102();
      internalField0320 = new GlowBloomRenderer();
      internalField0320.internalMethod03093();
      internalField0319 = new GlowEntitySolidShader(RockstarClient.id("glow/entity_solid/data"));
      GlowRenderLayers.initShader(internalField0319);
      internalField0439 = new AccentTimeShader(RockstarClient.id("sky_nebula/data"));
      CustomSkyboxRenderer.internalMethod05026(internalField0439);
      internalField0438 = new AccentTimeShader(RockstarClient.id("sky_sunset/data"));
      CustomSkyboxRenderer.internalMethod05026(internalField0438);
      internalField1153 = new AccentTimeShader(RockstarClient.id("sky_radiant/data"));
      CustomSkyboxRenderer.internalMethod05026(internalField1153);
      internalField1152 = new AccentTimeShader(RockstarClient.id("sky_caustic/data"));
      CustomSkyboxRenderer.internalMethod05026(internalField1152);
      internalField0315 = new ShaderPair(RockstarClient.id("sky_galaxy/bake/data"), RockstarClient.id("sky_galaxy/view/data"));
      CustomSkyboxRenderer.internalMethod02273(internalField0315);
      internalField0316 = new ShaderPair(RockstarClient.id("sky_space/bake/data"), RockstarClient.id("sky_space/view/data"));
      CustomSkyboxRenderer.internalMethod02273(internalField0316);
      internalField0437 = new SaturationIsolationRenderer(RockstarClient.id("saturation/data"));
      internalField0446 = new WetWorldRenderer(RockstarClient.id("wet_world/data"));
      internalField0343 = new MirrorCompositeShader(RockstarClient.id("mirror_composite/data"));
      internalField0336 = new ItemCenterShader(RockstarClient.id("item_caustic/data"));
      internalField0337 = new ItemCenterShader(RockstarClient.id("item_plasma/data"));
      internalField1128 = new ItemCenterShader(RockstarClient.id("item_lava/data"));
      SmoothItemLayers.initShaders(
         new ShaderProgramBase(RockstarClient.id("item_smooth/translucent/data"), VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL),
         new ShaderProgramBase(RockstarClient.id("item_smooth/cutout/data"), VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL)
      );
      BlockGhostLayers.initShader(new ShaderProgramBase(RockstarClient.id("block_ghost/data"), VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL));
   }

   public static void internalMethod09050() {
      internalField0769.setClearColor(0.0F, 0.0F, 0.0F, 0.0F);
      internalField0769.internalMethod03245();
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
      RenderSystem.setShaderTexture(0, internalField0149.getFramebuffer().getColorAttachmentView());
      internalMethod00811(0.0F, 0.0F, internalField0267.getScaledWidth(), internalField0267.getScaledHeight(), true);
      RenderSystem.disableBlend();
      rockstar.client.render.FramebufferCompat.beginWrite(internalField0149.getFramebuffer(), true);
      internalField0769.internalMethod03248();
   }

   public static void internalMethod09051() {
      Framebuffer localValue0 = internalField0149.getFramebuffer();
      if (localValue0 != null) {
         int localValue1 = internalField0267.getScaledWidth();
         int localValue2 = internalField0267.getScaledHeight();
         RenderSystem.backupProjectionMatrix();
         Matrix4f localValue3 = new Matrix4f().setOrtho(0.0F, localValue1, localValue2, 0.0F, 1000.0F, 21000.0F);
         RenderSystem.setProjectionMatrix(localValue3, ProjectionType.ORTHOGRAPHIC);
         Matrix4fStack localValue4 = RenderSystem.getModelViewStack();
         localValue4.pushMatrix();
         localValue4.identity().translate(0.0F, 0.0F, -11000.0F);
         internalField0769.internalMethod02227(true);
         RenderSystem.disableBlend();
         RenderSystem.disableDepthTest();
         RenderSystem.disableCull();
         RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         RenderSystem.setShaderTexture(0, localValue0.getColorAttachmentView());
         internalMethod00811(0.0F, 0.0F, localValue1, localValue2, true);
         RenderSystem.setShaderTexture(0, 0);
         internalField0769.internalMethod03248();
         localValue4.popMatrix();
         RenderSystem.restoreProjectionMatrix();
      }
   }

   public static void internalMethod03082(
      MatrixStack localValue0, float localValue1, float localValue2, float localValue3, float localValue4, float localValue5, float localValue6, float localValue7, int localValue8, Runnable localValue9
   ) {
      if (!internalMethod05199(localValue3, localValue4)) {
         internalField0769.internalMethod02227(true);
         localValue9.run();
         internalField0769.internalMethod03248();
         Matrix4f localValue10 = localValue0.peek().getPositionMatrix();
         internalMethod09058();
         RenderSystem.disableCull();
         RenderSystem.setShaderTexture(0, internalField0769.getColorAttachmentView());
         internalField1673.internalMethod01220();
         internalField1673.internalMethod05981("Size").set(localValue3, localValue4);
         internalField1673.internalMethod05981("Threshold").set(localValue5);
         internalField1673.internalMethod05981("Smooth").set(localValue6);
         internalField1673.internalMethod05981("RadiusPx").set(localValue7);
         internalField1673.internalMethod05981("Iterations").set(localValue8);
         int localValue11 = internalField0267.getScaledWidth();
         int localValue12 = internalField0267.getScaledHeight();
         float localValue13 = localValue1 / localValue11;
         float localValue14 = (localValue12 - localValue2 - localValue4) / localValue12;
         float localValue15 = localValue3 / localValue11;
         float localValue16 = localValue4 / localValue12;
         byte localValue17 = -1;
         BufferBuilder localValue18 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
         localValue18.vertex(localValue10, localValue1, localValue2, 0.0F).texture(localValue13, localValue14 + localValue16).color(localValue17);
         localValue18.vertex(localValue10, localValue1, localValue2 + localValue4, 0.0F).texture(localValue13, localValue14).color(localValue17);
         localValue18.vertex(localValue10, localValue1 + localValue3, localValue2 + localValue4, 0.0F).texture(localValue13 + localValue15, localValue14).color(localValue17);
         localValue18.vertex(localValue10, localValue1 + localValue3, localValue2, 0.0F).texture(localValue13 + localValue15, localValue14 + localValue16).color(localValue17);
         BufferRenderer.drawWithGlobalProgram(localValue18.end());
         RenderSystem.setShaderTexture(0, 0);
         RenderSystem.enableCull();
         internalMethod09648();
      }
   }

   private static void internalMethod00811(float localValue0, float localValue1, float localValue2, float localValue3, boolean localValue4) {
      BufferBuilder localValue5 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
      byte localValue6 = -1;
      float localValue7 = localValue4 ? 0.0F : 1.0F;
      float localValue8 = localValue4 ? 1.0F : 0.0F;
      localValue5.vertex(localValue0, localValue1, 0.0F).texture(0.0F, localValue8).color(-1);
      localValue5.vertex(localValue0, localValue1 + localValue3, 0.0F).texture(0.0F, localValue7).color(-1);
      localValue5.vertex(localValue0 + localValue2, localValue1 + localValue3, 0.0F).texture(1.0F, localValue7).color(-1);
      localValue5.vertex(localValue0 + localValue2, localValue1, 0.0F).texture(1.0F, localValue8).color(-1);
      BufferRenderer.drawWithGlobalProgram(localValue5.end());
   }

   public static void internalMethod01986(MatrixStack localValue0, Vec2f localValue1, Vec2f localValue2, ColorRGBA localValue3) {
      localValue0.push();

      try {
         Matrix4f localValue4 = localValue0.peek().getPositionMatrix();
         RenderSystem.enableBlend();
         RenderSystem.defaultBlendFunc();
         RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
         RenderSystem.lineWidth(1.0F);
         internalMethod09058();
         BufferBuilder localValue5 = RenderSystem.renderThreadTesselator().begin(DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION_COLOR);
         localValue5.vertex(localValue4, localValue1.x, localValue1.y, 0.0F).color(localValue3.getRGB());
         localValue5.vertex(localValue4, localValue2.x, localValue2.y, 0.0F).color(localValue3.getRGB());
         BufferRenderer.drawWithGlobalProgram(localValue5.end());
         internalMethod09648();
      } finally {
         RenderSystem.disableBlend();
         RenderSystem.lineWidth(1.0F);
         localValue0.pop();
      }
   }

   public static void internalMethod05338(MatrixStack localValue0, Vec2f localValue1, Vec2f localValue2, Vec2f localValue3, Vec2f localValue4, ColorRGBA localValue5, int localValue6) {
      localValue0.push();

      try {
         Matrix4f localValue7 = localValue0.peek().getPositionMatrix();
         int localValue8 = internalMethod06221(localValue1, localValue2, localValue3, localValue4, localValue6);
         RenderSystem.enableBlend();
         RenderSystem.defaultBlendFunc();
         RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
         RenderSystem.lineWidth(1.0F);
         internalMethod09058();
         BufferBuilder localValue9 = RenderSystem.renderThreadTesselator().begin(DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION_COLOR);

         for (int localValue10 = 0; localValue10 <= localValue8; localValue10++) {
            float localValue11 = (float)localValue10 / localValue8;
            float localValue12 = (float)MathUtils.internalMethod03549(localValue11, localValue1.x, localValue2.x, localValue3.x, localValue4.x);
            float localValue13 = (float)MathUtils.internalMethod03549(localValue11, localValue1.y, localValue2.y, localValue3.y, localValue4.y);
            localValue9.vertex(localValue7, localValue12, localValue13, 0.0F).color(localValue5.getRGB());
         }

         BufferRenderer.drawWithGlobalProgram(localValue9.end());
         internalMethod09648();
      } finally {
         RenderSystem.disableBlend();
         RenderSystem.lineWidth(1.0F);
         localValue0.pop();
      }
   }

   public static void internalMethod03090(
      MatrixStack localValue0, float localValue1, float localValue2, float localValue3, float localValue4, Vec2f localValue5, Vec2f localValue6, Vec2f localValue7, Vec2f localValue8, float localValue9, ColorRGBA localValue10
   ) {
      if (!internalMethod05199(localValue3, localValue4) && !(localValue9 <= 0.0F) && !internalMethod03563(localValue10)) {
         localValue0.push();
         Matrix4f localValue11 = localValue0.peek().getPositionMatrix();
         internalField1417.internalMethod01220();
         internalField1417.internalMethod05981("Size").set(localValue3, localValue4);
         internalField1417.internalMethod05981("P0").set(localValue5.x - localValue1, localValue5.y - localValue2);
         internalField1417.internalMethod05981("P1").set(localValue6.x - localValue1, localValue6.y - localValue2);
         internalField1417.internalMethod05981("P2").set(localValue7.x - localValue1, localValue7.y - localValue2);
         internalField1417.internalMethod05981("P3").set(localValue8.x - localValue1, localValue8.y - localValue2);
         internalField1417.internalMethod05981("Thickness").set(localValue9);
         internalMethod09058();
         BufferBuilder localValue12 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
         localValue12.vertex(localValue11, localValue1, localValue2, 0.0F).color(localValue10.getRGB());
         localValue12.vertex(localValue11, localValue1, localValue2 + localValue4, 0.0F).color(localValue10.getRGB());
         localValue12.vertex(localValue11, localValue1 + localValue3, localValue2 + localValue4, 0.0F).color(localValue10.getRGB());
         localValue12.vertex(localValue11, localValue1 + localValue3, localValue2, 0.0F).color(localValue10.getRGB());
         BufferRenderer.drawWithGlobalProgram(localValue12.end());
         internalMethod09648();
         localValue0.pop();
      }
   }

   public static void internalMethod07554(MatrixStack localValue0, float[] localValue1, float[] localValue2, float localValue3, ColorRGBA localValue4, ColorRGBA localValue5) {
      if (localValue1 != null && localValue2 != null && localValue1.length >= 2 && localValue1.length == localValue2.length) {
         if (!internalMethod03563(localValue4) || !internalMethod03563(localValue5)) {
            UiBatchRenderer.internalMethod02576();
            localValue0.push();

            try {
               Matrix4f localValue6 = localValue0.peek().getPositionMatrix();
               RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
               internalMethod09058();
               BufferBuilder localValue7 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
               int localValue8 = localValue4.getRGB();
               int localValue9 = localValue5.getRGB();

               for (int localValue10 = 0; localValue10 < localValue1.length - 1; localValue10++) {
                  float localValue11 = localValue1[localValue10];
                  float localValue12 = localValue1[localValue10 + 1];
                  float localValue13 = Math.min(localValue2[localValue10], localValue3);
                  float localValue14 = Math.min(localValue2[localValue10 + 1], localValue3);
                  localValue7.vertex(localValue6, localValue11, localValue13, 0.0F).color(localValue8);
                  localValue7.vertex(localValue6, localValue11, localValue3, 0.0F).color(localValue9);
                  localValue7.vertex(localValue6, localValue12, localValue3, 0.0F).color(localValue9);
                  localValue7.vertex(localValue6, localValue12, localValue14, 0.0F).color(localValue8);
               }

               BufferRenderer.drawWithGlobalProgram(localValue7.end());
               internalMethod09648();
            } finally {
               localValue0.pop();
            }
         }
      }
   }

   private static int internalMethod06221(Vec2f localValue0, Vec2f localValue1, Vec2f localValue2, Vec2f localValue3, int localValue4) {
      if (localValue4 <= 1) {
         return 1;
      } else {
         float localValue5 = internalMethod04242(localValue0, localValue1) + internalMethod04242(localValue1, localValue2) + internalMethod04242(localValue2, localValue3);
         if (Float.isFinite(localValue5) && !(localValue5 <= 0.0F)) {
            int localValue6 = Math.max(1, Math.round(localValue5));
            int localValue7 = Math.max(1, internalField0267.getScaledWidth() + internalField0267.getScaledHeight());
            return Math.min(localValue4, Math.min(localValue6, localValue7));
         } else {
            return 1;
         }
      }
   }

   private static float internalMethod04242(Vec2f localValue0, Vec2f localValue1) {
      return (float)Math.hypot(localValue1.x - localValue0.x, localValue1.y - localValue0.y);
   }

   public static void internalMethod02688(MatrixStack localValue0, float localValue1, float localValue2, float localValue3, float localValue4, ColorRGBA localValue5) {
      if (!internalMethod05199(localValue3, localValue4) && !internalMethod03563(localValue5)) {
         if (UiVertexBatch.internalMethod04253() instanceof ColoredQuadBatch localValue9) {
            BufferBuilder localValue11 = localValue9.internalMethod05457();
            Matrix4f localValue12 = localValue9.internalMethod02894().peek().getPositionMatrix();
            localValue11.vertex(localValue12, localValue1, localValue2 + localValue4, 0.0F).color(localValue5.getRGB());
            localValue11.vertex(localValue12, localValue1 + localValue3, localValue2 + localValue4, 0.0F).color(localValue5.getRGB());
            localValue11.vertex(localValue12, localValue1 + localValue3, localValue2, 0.0F).color(localValue5.getRGB());
            localValue11.vertex(localValue12, localValue1, localValue2, 0.0F).color(localValue5.getRGB());
         } else {
            UiBatchRenderer localValue6 = UiBatchRenderer.internalMethod08317();
            if (localValue6 != null) {
               localValue6.internalMethod04843(localValue0.peek().getPositionMatrix(), localValue1, localValue2, localValue3, localValue4, localValue5);
            } else {
               localValue0.push();
               Matrix4f localValue10 = localValue0.peek().getPositionMatrix();
               RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
               internalMethod09058();
               BufferBuilder localValue8 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
               localValue8.vertex(localValue10, localValue1, localValue2 + localValue4, 0.0F).color(localValue5.getRGB());
               localValue8.vertex(localValue10, localValue1 + localValue3, localValue2 + localValue4, 0.0F).color(localValue5.getRGB());
               localValue8.vertex(localValue10, localValue1 + localValue3, localValue2, 0.0F).color(localValue5.getRGB());
               localValue8.vertex(localValue10, localValue1, localValue2, 0.0F).color(localValue5.getRGB());
               BufferRenderer.drawWithGlobalProgram(localValue8.end());
               internalMethod09648();
               localValue0.pop();
            }
         }
      }
   }

   public static void internalMethod04182(MatrixStack localValue0, float localValue1, float localValue2, float localValue3, float localValue4, float localValue5, CornerRadii localValue6, ColorRGBA localValue7) {
      if (!internalMethod05199(localValue3, localValue4) && !internalMethod03563(localValue7)) {
         localValue0.push();
         Matrix4f localValue8 = localValue0.peek().getPositionMatrix();
         float localValue9 = 0.5F;
         if (UiVertexBatch.internalMethod04253() instanceof SmoothCornerRectBatch localValue18) {
            localValue18.internalMethod07440(
               localValue8,
               localValue1,
               localValue2,
               localValue3,
               localValue4,
               localValue6.internalMethod05337() * localValue5 / 2.0F,
               localValue6.internalMethod08942() * localValue5 / 2.0F,
               localValue6.internalMethod05340() * localValue5 / 2.0F,
               localValue6.internalMethod08939() * localValue5 / 2.0F,
               localValue7.getRGB()
            );
            localValue0.pop();
         } else {
            UiBatchRenderer localValue10 = UiBatchRenderer.internalMethod08317();
            if (localValue10 != null) {
               localValue10.internalMethod00028(localValue8, localValue1, localValue2, localValue3, localValue4, localValue5, localValue6, localValue7);
               localValue0.pop();
            } else {
               internalField0994.internalMethod01220();
               internalField0994.internalMethod05981("Size").set(localValue3, localValue4);
               internalField0994.internalMethod05981("Radius")
                  .set(
                     localValue6.internalMethod05337() * localValue5 / 2.0F,
                     localValue6.internalMethod08942() * localValue5 / 2.0F,
                     localValue6.internalMethod05340() * localValue5 / 2.0F,
                     localValue6.internalMethod08939() * localValue5 / 2.0F
                  );
               internalField0994.internalMethod05981("Smoothness").set(localValue9);
               internalField0994.internalMethod05981("CornerSmoothness").set(localValue5);
               internalMethod09058();
               float localValue19 = -localValue9 / 2.0F + localValue9 * 2.0F;
               float localValue12 = localValue9 / 2.0F + localValue9;
               float localValue13 = localValue1 - localValue19 / 2.0F;
               float localValue14 = localValue2 - localValue12 / 2.0F;
               float localValue15 = localValue3 + localValue19;
               float localValue16 = localValue4 + localValue12;
               BufferBuilder localValue17 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
               localValue17.vertex(localValue8, localValue13, localValue14, 0.0F).color(localValue7.getRGB());
               localValue17.vertex(localValue8, localValue13, localValue14 + localValue16, 0.0F).color(localValue7.getRGB());
               localValue17.vertex(localValue8, localValue13 + localValue15, localValue14 + localValue16, 0.0F).color(localValue7.getRGB());
               localValue17.vertex(localValue8, localValue13 + localValue15, localValue14, 0.0F).color(localValue7.getRGB());
               BufferRenderer.drawWithGlobalProgram(localValue17.end());
               internalMethod09648();
               localValue0.pop();
            }
         }
      }
   }

   public static void internalMethod01759(
      MatrixStack localValue0,
      float localValue1,
      float localValue2,
      float localValue3,
      float localValue4,
      float localValue5,
      CornerRadii localValue6,
      ColorRGBA localValue7,
      ColorRGBA localValue8,
      ColorRGBA localValue9,
      ColorRGBA localValue10
   ) {
      if (!internalMethod05199(localValue3, localValue4) && (!internalMethod03563(localValue7) || !internalMethod03563(localValue8) || !internalMethod03563(localValue9) || !internalMethod03563(localValue10))) {
         UiBatchRenderer localValue11 = UiBatchRenderer.internalMethod08317();
         if (localValue11 != null && localValue11.internalMethod08432()) {
            localValue0.push();
            Matrix4f localValue21 = localValue0.peek().getPositionMatrix();
            CornerRadii localValue22 = new CornerRadii(
               localValue6.internalMethod05337() * localValue5 / 2.0F,
               localValue6.internalMethod05340() * localValue5 / 2.0F,
               localValue6.internalMethod08939() * localValue5 / 2.0F,
               localValue6.internalMethod08942() * localValue5 / 2.0F
            );
            localValue11.internalMethod03840(localValue21, localValue1, localValue2, localValue3, localValue4, localValue22, 0.5F, localValue5, localValue7, localValue8, localValue9, localValue10, false);
            localValue0.pop();
         } else {
            UiBatchRenderer.internalMethod02576();
            localValue0.push();
            Matrix4f localValue12 = localValue0.peek().getPositionMatrix();
            float localValue13 = 0.5F;
            internalField0994.internalMethod01220();
            internalField0994.internalMethod05981("Size").set(localValue3, localValue4);
            internalField0994.internalMethod05981("Radius")
               .set(
                  localValue6.internalMethod05337() * localValue5 / 2.0F,
                  localValue6.internalMethod08942() * localValue5 / 2.0F,
                  localValue6.internalMethod05340() * localValue5 / 2.0F,
                  localValue6.internalMethod08939() * localValue5 / 2.0F
               );
            internalField0994.internalMethod05981("Smoothness").set(localValue13);
            internalField0994.internalMethod05981("CornerSmoothness").set(localValue5);
            internalMethod09058();
            float localValue14 = -localValue13 / 2.0F + localValue13 * 2.0F;
            float localValue15 = localValue13 / 2.0F + localValue13;
            float localValue16 = localValue1 - localValue14 / 2.0F;
            float localValue17 = localValue2 - localValue15 / 2.0F;
            float localValue18 = localValue3 + localValue14;
            float localValue19 = localValue4 + localValue15;
            BufferBuilder localValue20 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            localValue20.vertex(localValue12, localValue16, localValue17, 0.0F).color(localValue7.getRGB());
            localValue20.vertex(localValue12, localValue16, localValue17 + localValue19, 0.0F).color(localValue8.getRGB());
            localValue20.vertex(localValue12, localValue16 + localValue18, localValue17 + localValue19, 0.0F).color(localValue9.getRGB());
            localValue20.vertex(localValue12, localValue16 + localValue18, localValue17, 0.0F).color(localValue10.getRGB());
            BufferRenderer.drawWithGlobalProgram(localValue20.end());
            internalMethod09648();
            localValue0.pop();
         }
      }
   }

   public static void internalMethod04108(MatrixStack localValue0, float localValue1, float localValue2, float localValue3, float localValue4, float localValue5, CornerRadii localValue6, QuadColorGradient localValue7) {
      internalMethod01759(
         localValue0, localValue1, localValue2, localValue3, localValue4, localValue5, localValue6, localValue7.internalMethod03279(), localValue7.internalMethod07639(), localValue7.internalMethod08912(), localValue7.internalMethod08085()
      );
   }

   public static void internalMethod05317(MatrixStack localValue0, float localValue1, float localValue2, float localValue3, float localValue4, float localValue5, CornerRadii localValue6, ColorRGBA localValue7) {
      if (!internalMethod05199(localValue3, localValue4) && !internalMethod03563(localValue7)) {
         UiBatchRenderer.internalMethod02576();
         localValue0.push();
         Matrix4f localValue8 = localValue0.peek().getPositionMatrix();
         float localValue9 = 0.5F;
         internalField1416.internalMethod01220();
         internalField1416.internalMethod05981("Size").set(localValue3, localValue4);
         internalField1416.internalMethod05981("Radius")
            .set(localValue6.internalMethod05337(), localValue6.internalMethod08942(), localValue6.internalMethod05340(), localValue6.internalMethod08939());
         internalField1416.internalMethod05981("Smoothness").set(localValue9);
         internalField1416.internalMethod05981("Progress").set(localValue5);
         internalField1416.internalMethod05981("StripeWidth").set(0.0F);
         internalField1416.internalMethod05981("Fade").set(0.5F);
         internalMethod09058();
         float localValue10 = -localValue9 / 2.0F + localValue9 * 2.0F;
         float localValue11 = localValue9 / 2.0F + localValue9;
         float localValue12 = localValue1 - localValue10 / 2.0F;
         float localValue13 = localValue2 - localValue11 / 2.0F;
         float localValue14 = localValue3 + localValue10;
         float localValue15 = localValue4 + localValue11;
         BufferBuilder localValue16 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
         localValue16.vertex(localValue8, localValue12, localValue13, 0.0F).color(localValue7.getRGB());
         localValue16.vertex(localValue8, localValue12, localValue13 + localValue15, 0.0F).color(localValue7.getRGB());
         localValue16.vertex(localValue8, localValue12 + localValue14, localValue13 + localValue15, 0.0F).color(localValue7.getRGB());
         localValue16.vertex(localValue8, localValue12 + localValue14, localValue13, 0.0F).color(localValue7.getRGB());
         BufferRenderer.drawWithGlobalProgram(localValue16.end());
         internalMethod09648();
         localValue0.pop();
      }
   }

   public static void internalMethod03329(
      MatrixStack localValue0,
      float localValue1,
      float localValue2,
      float localValue3,
      float localValue4,
      CornerRadii localValue5,
      ColorRGBA localValue6,
      float localValue7,
      float localValue8,
      ColorRGBA localValue9,
      float localValue10,
      boolean localValue11,
      float localValue12,
      float localValue13,
      float localValue14,
      boolean localValue15
   ) {
      if (!internalMethod05199(localValue3, localValue4) && !internalMethod03563(localValue6) && !(localValue7 <= 0.0F)) {
         UiBatchRenderer.internalMethod02576();
         Matrix4f localValue16 = localValue0.peek().getPositionMatrix();
         internalField1675.internalMethod01220();
         if (localValue15) {
            RenderSystem.setShaderTexture(0, internalField0769.getColorAttachmentView());
         } else {
            RenderSystem.setShaderTexture(0, internalMethod01904());
         }
         internalField1675.internalMethod05981("GlobalAlpha").set(localValue7);
         internalField1675.internalMethod05981("Size").set(localValue3, localValue4);
         internalField1675.internalMethod05981("Radius")
            .set(localValue5.internalMethod05337(), localValue5.internalMethod08942(), localValue5.internalMethod05340(), localValue5.internalMethod08939());
         internalField1675.internalMethod05981("Smoothness").set(0.5F);
         internalField1675.internalMethod05981("FresnelPower").set(localValue8);
         internalField1675.internalMethod05981("FresnelColor").set(PackedColorUtils.internalMethod02646(localValue9.getRGB()));
         internalField1675.internalMethod05981("FresnelAlpha").set(PackedColorUtils.internalMethod07904(localValue9.getRGB()));
         internalField1675.internalMethod05981("BaseAlpha").set(localValue10);
         internalField1675.internalMethod05981("FresnelInvert").set(localValue11 ? 1 : 0);
         internalField1675.internalMethod05981("FresnelMix").set(localValue12);
         internalField1675.internalMethod05981("DistortStrength").set(InterfaceModule.internalMethod08761());
         internalField1675.internalMethod05981("DistortRadius").set(InterfaceModule.internalMethod08764());
         internalField1675.internalMethod05981("Aberration").set(InterfaceModule.internalMethod08774());
         internalField1675.internalMethod05981("Saturation").set(InterfaceModule.internalMethod08775());
         internalField1675.internalMethod05981("CornerSmoothness").set(localValue14);
         internalMethod09058();
         RenderSystem.disableCull();
         int localValue17 = internalField0267.getScaledWidth();
         int localValue18 = internalField0267.getScaledHeight();
         float localValue19;
         float localValue20;
         float localValue21;
         float localValue22;
         if (!MatrixUtil.isIdentity(localValue16)) {
            Vector3f localValue23 = new Vector3f(localValue1, localValue2, 0.0F);
            Vector3f localValue24 = new Vector3f(localValue1 + localValue3, localValue2 + localValue4, 0.0F);
            localValue16.transformPosition(localValue23);
            localValue16.transformPosition(localValue24);
            localValue19 = localValue23.x / localValue17;
            localValue20 = (localValue18 - localValue23.y - (localValue24.y - localValue23.y)) / localValue18;
            localValue21 = (localValue24.x - localValue23.x) / localValue17;
            localValue22 = (localValue24.y - localValue23.y) / localValue18;
         } else {
            localValue19 = localValue1 / localValue17;
            localValue20 = (localValue18 - localValue2 - localValue4) / localValue18;
            localValue21 = localValue3 / localValue17;
            localValue22 = localValue4 / localValue18;
         }

         BufferBuilder localValue25 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
         localValue25.vertex(localValue16, localValue1, localValue2, 0.0F).texture(localValue19, localValue20 + localValue22).color(localValue6.getRGB());
         localValue25.vertex(localValue16, localValue1, localValue2 + localValue4, 0.0F).texture(localValue19, localValue20).color(localValue6.getRGB());
         localValue25.vertex(localValue16, localValue1 + localValue3, localValue2 + localValue4, 0.0F).texture(localValue19 + localValue21, localValue20).color(localValue6.getRGB());
         localValue25.vertex(localValue16, localValue1 + localValue3, localValue2, 0.0F).texture(localValue19 + localValue21, localValue20 + localValue22).color(localValue6.getRGB());
         BufferRenderer.drawWithGlobalProgram(localValue25.end());
         RenderSystem.setShaderTexture(0, 0);
         RenderSystem.enableCull();
         internalMethod09648();
      }
   }

   public static void internalMethod01221(
      Rect localValue0,
      MatrixStack localValue1,
      float localValue2,
      float localValue3,
      float localValue4,
      float localValue5,
      CornerRadii localValue6,
      ColorRGBA localValue7,
      float localValue8,
      float localValue9,
      ColorRGBA localValue10,
      float localValue11,
      boolean localValue12,
      float localValue13,
      float localValue14,
      float localValue15,
      int localValue16
   ) {
      UiBatchRenderer.internalMethod02576();
      Matrix4f localValue17 = localValue1.peek().getPositionMatrix();
      internalField1675.internalMethod01220();
      RenderSystem.setShaderTexture(0, localValue16);
      internalField1675.internalMethod05981("GlobalAlpha").set(localValue8);
      internalField1675.internalMethod05981("Size").set(localValue4, localValue5);
      internalField1675.internalMethod05981("Radius")
         .set(localValue6.internalMethod05337(), localValue6.internalMethod08942(), localValue6.internalMethod05340(), localValue6.internalMethod08939());
      internalField1675.internalMethod05981("Smoothness").set(0.5F);
      internalField1675.internalMethod05981("FresnelPower").set(localValue9);
      internalField1675.internalMethod05981("FresnelColor").set(PackedColorUtils.internalMethod02646(localValue10.getRGB()));
      internalField1675.internalMethod05981("FresnelAlpha").set(PackedColorUtils.internalMethod07904(localValue10.getRGB()));
      internalField1675.internalMethod05981("BaseAlpha").set(localValue11);
      internalField1675.internalMethod05981("FresnelInvert").set(localValue12 ? 1 : 0);
      internalField1675.internalMethod05981("FresnelMix").set(localValue13);
      internalField1675.internalMethod05981("DistortStrength").set(InterfaceModule.internalMethod08761());
      internalField1675.internalMethod05981("DistortRadius").set(InterfaceModule.internalMethod08764());
      internalField1675.internalMethod05981("Aberration").set(InterfaceModule.internalMethod08774());
      internalField1675.internalMethod05981("Saturation").set(InterfaceModule.internalMethod08775());
      internalField1675.internalMethod05981("CornerSmoothness").set(localValue15);
      internalMethod09058();
      RenderSystem.disableCull();
      float localValue18 = localValue0.getWidth();
      float localValue19 = localValue0.getHeight();
      float localValue20;
      float localValue21;
      float localValue22;
      float localValue23;
      if (!MatrixUtil.isIdentity(localValue17)) {
         Vector3f localValue24 = new Vector3f(localValue2, localValue3, 0.0F);
         Vector3f localValue25 = new Vector3f(localValue2 + localValue4, localValue3 + localValue5, 0.0F);
         localValue17.transformPosition(localValue24);
         localValue17.transformPosition(localValue25);
         float localValue26 = localValue24.x;
         float localValue27 = localValue24.y;
         float localValue28 = localValue25.x - localValue24.x;
         float localValue29 = localValue25.y - localValue24.y;
         localValue20 = (localValue26 - localValue0.getX()) / localValue18;
         localValue21 = (localValue27 - localValue0.getY()) / localValue19 + localValue29 / localValue19;
         localValue22 = localValue28 / localValue18;
         localValue23 = -localValue29 / localValue19;
      } else {
         localValue20 = (localValue2 - localValue0.getX()) / localValue18;
         localValue21 = (localValue3 - localValue0.getY()) / localValue19 + localValue5 / localValue19;
         localValue22 = localValue4 / localValue18;
         localValue23 = -localValue5 / localValue19;
      }

      BufferBuilder localValue30 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
      localValue30.vertex(localValue17, localValue2, localValue3, 0.0F).texture(localValue20, localValue21 + localValue23).color(localValue7.getRGB());
      localValue30.vertex(localValue17, localValue2, localValue3 + localValue5, 0.0F).texture(localValue20, localValue21).color(localValue7.getRGB());
      localValue30.vertex(localValue17, localValue2 + localValue4, localValue3 + localValue5, 0.0F).texture(localValue20 + localValue22, localValue21).color(localValue7.getRGB());
      localValue30.vertex(localValue17, localValue2 + localValue4, localValue3, 0.0F).texture(localValue20 + localValue22, localValue21 + localValue23).color(localValue7.getRGB());
      BufferRenderer.drawWithGlobalProgram(localValue30.end());
      RenderSystem.setShaderTexture(0, 0);
      RenderSystem.enableCull();
      internalMethod09648();
   }

   public static void internalMethod02035(MatrixStack localValue0, float localValue1, float localValue2, float localValue3, float localValue4, CornerRadii localValue5, ColorRGBA localValue6) {
      if (!internalMethod05199(localValue3, localValue4) && !internalMethod03563(localValue6)) {
         localValue0.push();
         Matrix4f localValue7 = localValue0.peek().getPositionMatrix();
         float localValue8 = 0.5F;
         UiVertexBatch localValue9 = UiVertexBatch.internalMethod04253();
         if (localValue9 != null && localValue9.internalMethod00818(localValue7, localValue1, localValue2, localValue3, localValue4, localValue5, localValue6.getRGB())) {
            localValue0.pop();
         } else {
            UiBatchRenderer localValue10 = UiBatchRenderer.internalMethod08317();
            if (localValue10 != null) {
               localValue10.internalMethod03494(localValue7, localValue1, localValue2, localValue3, localValue4, localValue5, localValue6);
               localValue0.pop();
            } else {
               internalField0104.internalMethod01220();
               internalField0104.internalMethod05981("Size").set(localValue3, localValue4);
               internalField0104.internalMethod05981("Radius")
                  .set(localValue5.internalMethod05337(), localValue5.internalMethod08942(), localValue5.internalMethod05340(), localValue5.internalMethod08939());
               internalField0104.internalMethod05981("Smoothness").set(localValue8);
               internalMethod09058();
               float localValue11 = -localValue8 / 2.0F + localValue8 * 2.0F;
               float localValue12 = localValue8 / 2.0F + localValue8;
               float localValue13 = localValue1 - localValue11 / 2.0F;
               float localValue14 = localValue2 - localValue12 / 2.0F;
               float localValue15 = localValue3 + localValue11;
               float localValue16 = localValue4 + localValue12;
               BufferBuilder localValue17 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
               localValue17.vertex(localValue7, localValue13, localValue14, 0.0F).color(localValue6.getRGB());
               localValue17.vertex(localValue7, localValue13, localValue14 + localValue16, 0.0F).color(localValue6.getRGB());
               localValue17.vertex(localValue7, localValue13 + localValue15, localValue14 + localValue16, 0.0F).color(localValue6.getRGB());
               localValue17.vertex(localValue7, localValue13 + localValue15, localValue14, 0.0F).color(localValue6.getRGB());
               BufferRenderer.drawWithGlobalProgram(localValue17.end());
               internalMethod09648();
               localValue0.pop();
            }
         }
      }
   }

   public static void internalMethod04255(
      MatrixStack localValue0, float localValue1, float localValue2, float localValue3, float localValue4, CornerRadii localValue5, ColorRGBA localValue6, ColorRGBA localValue7, ColorRGBA localValue8, ColorRGBA localValue9
   ) {
      if (!internalMethod05199(localValue3, localValue4) && (!internalMethod03563(localValue6) || !internalMethod03563(localValue7) || !internalMethod03563(localValue8) || !internalMethod03563(localValue9))) {
         UiBatchRenderer localValue10 = UiBatchRenderer.internalMethod08317();
         if (localValue10 != null && localValue10.internalMethod08432()) {
            localValue0.push();
            Matrix4f localValue20 = localValue0.peek().getPositionMatrix();
            localValue10.internalMethod03840(localValue20, localValue1, localValue2, localValue3, localValue4, localValue5, 0.5F, 2.0F, localValue6, localValue7, localValue8, localValue9, true);
            localValue0.pop();
         } else {
            UiBatchRenderer.internalMethod02576();
            localValue0.push();
            Matrix4f localValue11 = localValue0.peek().getPositionMatrix();
            float localValue12 = 0.5F;
            internalField1674.internalMethod01220();
            internalField1674.internalMethod05981("Size").set(localValue3, localValue4);
            internalField1674.internalMethod05981("Radius")
               .set(localValue5.internalMethod05337(), localValue5.internalMethod08942(), localValue5.internalMethod05340(), localValue5.internalMethod08939());
            internalField1674.internalMethod05981("Smoothness").set(localValue12);
            internalField1674.internalMethod05981("TopLeftColor")
               .set(localValue6.getRed() / 255.0F, localValue6.getGreen() / 255.0F, localValue6.getBlue() / 255.0F, localValue6.getAlpha() / 255.0F);
            internalField1674.internalMethod05981("BottomLeftColor")
               .set(localValue7.getRed() / 255.0F, localValue7.getGreen() / 255.0F, localValue7.getBlue() / 255.0F, localValue7.getAlpha() / 255.0F);
            internalField1674.internalMethod05981("BottomRightColor")
               .set(localValue8.getRed() / 255.0F, localValue8.getGreen() / 255.0F, localValue8.getBlue() / 255.0F, localValue8.getAlpha() / 255.0F);
            internalField1674.internalMethod05981("TopRightColor")
               .set(localValue9.getRed() / 255.0F, localValue9.getGreen() / 255.0F, localValue9.getBlue() / 255.0F, localValue9.getAlpha() / 255.0F);
            internalMethod09058();
            float localValue13 = -localValue12 / 2.0F + localValue12 * 2.0F;
            float localValue14 = localValue12 / 2.0F + localValue12;
            float localValue15 = localValue1 - localValue13 / 2.0F;
            float localValue16 = localValue2 - localValue14 / 2.0F;
            float localValue17 = localValue3 + localValue13;
            float localValue18 = localValue4 + localValue14;
            BufferBuilder localValue19 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            localValue19.vertex(localValue11, localValue15, localValue16, 0.0F).color(localValue6.getRGB());
            localValue19.vertex(localValue11, localValue15, localValue16 + localValue18, 0.0F).color(localValue7.getRGB());
            localValue19.vertex(localValue11, localValue15 + localValue17, localValue16 + localValue18, 0.0F).color(localValue8.getRGB());
            localValue19.vertex(localValue11, localValue15 + localValue17, localValue16, 0.0F).color(localValue9.getRGB());
            BufferRenderer.drawWithGlobalProgram(localValue19.end());
            internalMethod09648();
            localValue0.pop();
         }
      }
   }

   public static void internalMethod03784(MatrixStack localValue0, float localValue1, float localValue2, float localValue3, float localValue4, CornerRadii localValue5, QuadColorGradient localValue6) {
      internalMethod04255(
         localValue0, localValue1, localValue2, localValue3, localValue4, localValue5, localValue6.internalMethod03279(), localValue6.internalMethod07639(), localValue6.internalMethod08912(), localValue6.internalMethod08085()
      );
   }

   public static void internalMethod08588(MatrixStack localValue0, float localValue1, float localValue2, float localValue3, float localValue4, float localValue5, CornerRadii localValue6, ColorRGBA localValue7) {
      if (!internalMethod05199(localValue3, localValue4) && !(localValue5 <= 0.0F) && !internalMethod03563(localValue7)) {
         localValue0.push();
         Matrix4f localValue8 = localValue0.peek().getPositionMatrix();
         float localValue9 = 0.5F;
         float localValue10 = 1.0F;
         UiBatchRenderer localValue11 = UiBatchRenderer.internalMethod08317();
         if (localValue11 != null) {
            localValue11.internalMethod08941(localValue8, localValue1, localValue2, localValue3, localValue4, localValue5, localValue6, localValue7);
            localValue0.pop();
         } else {
            internalField1422.internalMethod01220();
            internalField1422.internalMethod05981("Size").set(localValue3, localValue4);
            internalField1422.internalMethod05981("Radius")
               .set(localValue6.internalMethod05337(), localValue6.internalMethod08942(), localValue6.internalMethod05340(), localValue6.internalMethod08939());
            internalField1422.internalMethod05981("Smoothness").set(localValue9, localValue10);
            internalField1422.internalMethod05981("Thickness").set(localValue5);
            internalMethod09058();
            float localValue12 = -localValue10 / 2.0F + localValue10 * 2.0F;
            float localValue13 = localValue10 / 2.0F + localValue10;
            float localValue14 = localValue1 - localValue12 / 2.0F;
            float localValue15 = localValue2 - localValue13 / 2.0F;
            float localValue16 = localValue3 + localValue12;
            float localValue17 = localValue4 + localValue13;
            BufferBuilder localValue18 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            localValue18.vertex(localValue8, localValue14, localValue15, 0.0F).color(localValue7.getRGB());
            localValue18.vertex(localValue8, localValue14, localValue15 + localValue17, 0.0F).color(localValue7.getRGB());
            localValue18.vertex(localValue8, localValue14 + localValue16, localValue15 + localValue17, 0.0F).color(localValue7.getRGB());
            localValue18.vertex(localValue8, localValue14 + localValue16, localValue15, 0.0F).color(localValue7.getRGB());
            BufferRenderer.drawWithGlobalProgram(localValue18.end());
            internalMethod09648();
            localValue0.pop();
         }
      }
   }

   public static void internalMethod01338(
      MatrixStack localValue0,
      float localValue1,
      float localValue2,
      float localValue3,
      float localValue4,
      float localValue5,
      CornerRadii localValue6,
      float localValue7,
      float localValue8,
      ColorRGBA localValue9,
      float localValue10,
      float localValue11,
      float localValue12,
      float localValue13
   ) {
      if (!internalMethod05199(localValue3, localValue4) && !(localValue5 <= 0.0F) && !internalMethod03563(localValue9)) {
         localValue0.push();
         Matrix4f localValue14 = localValue0.peek().getPositionMatrix();
         float localValue15 = 0.5F;
         float localValue16 = 1.0F;
         internalField1420.internalMethod01220();
         internalField1420.internalMethod05981("Size").set(localValue3, localValue4);
         internalField1420.internalMethod05981("Radius")
            .set(localValue6.internalMethod05337(), localValue6.internalMethod08942(), localValue6.internalMethod05340(), localValue6.internalMethod08939());
         internalField1420.internalMethod05981("Smoothness").set(localValue15, localValue16);
         internalField1420.internalMethod05981("Thickness").set(localValue5);
         internalField1420.internalMethod05981("DashLength").set(localValue7);
         internalField1420.internalMethod05981("GapLength").set(localValue8);
         internalField1420.internalMethod05981("Dashed").set(localValue10, localValue11, localValue12, localValue13);
         internalMethod09058();
         float localValue17 = -localValue16 / 2.0F + localValue16 * 2.0F;
         float localValue18 = localValue16 / 2.0F + localValue16;
         float localValue19 = localValue1 - localValue17 / 2.0F;
         float localValue20 = localValue2 - localValue18 / 2.0F;
         float localValue21 = localValue3 + localValue17;
         float localValue22 = localValue4 + localValue18;
         BufferBuilder localValue23 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
         localValue23.vertex(localValue14, localValue19, localValue20, 0.0F).color(localValue9.getRGB());
         localValue23.vertex(localValue14, localValue19, localValue20 + localValue22, 0.0F).color(localValue9.getRGB());
         localValue23.vertex(localValue14, localValue19 + localValue21, localValue20 + localValue22, 0.0F).color(localValue9.getRGB());
         localValue23.vertex(localValue14, localValue19 + localValue21, localValue20, 0.0F).color(localValue9.getRGB());
         BufferRenderer.drawWithGlobalProgram(localValue23.end());
         internalMethod09648();
         localValue0.pop();
      }
   }

   public static void internalMethod06077(
      MatrixStack localValue0, float localValue1, float localValue2, float localValue3, float localValue4, float localValue5, float localValue6, CornerRadii localValue7, ColorRGBA localValue8
   ) {
      if (!internalMethod05199(localValue3, localValue4) && !(localValue5 <= 0.0F) && !internalMethod03563(localValue8)) {
         localValue0.push();
         Matrix4f localValue9 = localValue0.peek().getPositionMatrix();
         float localValue10 = 0.5F;
         float localValue11 = 0.5F;
         UiBatchRenderer localValue12 = UiBatchRenderer.internalMethod08317();
         if (localValue12 != null) {
            localValue12.internalMethod00726(localValue9, localValue1, localValue2, localValue3, localValue4, localValue5, localValue6, localValue7, localValue8);
            localValue0.pop();
         } else {
            internalField1415.internalMethod01220();
            internalField1415.internalMethod05981("Size").set(localValue3, localValue4);
            internalField1415.internalMethod05981("Radius")
               .set(
                  localValue7.internalMethod05337() * localValue6 / 2.0F,
                  localValue7.internalMethod08942() * localValue6 / 2.0F,
                  localValue7.internalMethod05340() * localValue6 / 2.0F,
                  localValue7.internalMethod08939() * localValue6 / 2.0F
               );
            internalField1415.internalMethod05981("Smoothness").set(localValue10, localValue11);
            internalField1415.internalMethod05981("Thickness").set(localValue5);
            internalField1415.internalMethod05981("CornerSmoothness").set(localValue6);
            internalMethod09058();
            float localValue13 = -localValue11 / 2.0F + localValue11 * 2.0F;
            float localValue14 = localValue11 / 2.0F + localValue11;
            float localValue15 = localValue1 - localValue13 / 2.0F;
            float localValue16 = localValue2 - localValue14 / 2.0F;
            float localValue17 = localValue3 + localValue13;
            float localValue18 = localValue4 + localValue14;
            BufferBuilder localValue19 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            localValue19.vertex(localValue9, localValue15, localValue16, 0.0F).color(localValue8.getRGB());
            localValue19.vertex(localValue9, localValue15, localValue16 + localValue18, 0.0F).color(localValue8.getRGB());
            localValue19.vertex(localValue9, localValue15 + localValue17, localValue16 + localValue18, 0.0F).color(localValue8.getRGB());
            localValue19.vertex(localValue9, localValue15 + localValue17, localValue16, 0.0F).color(localValue8.getRGB());
            BufferRenderer.drawWithGlobalProgram(localValue19.end());
            internalMethod09648();
            localValue0.pop();
         }
      }
   }

   public static void internalMethod00463(MatrixStack localValue0, Identifier localValue1, float localValue2, float localValue3, float localValue4, float localValue5, ColorRGBA localValue6) {
      if (localValue1 != null && !internalMethod05199(localValue4, localValue5) && !internalMethod03563(localValue6)) {
         if (UiVertexBatch.internalMethod04253() instanceof TexturedQuadBatch localValue10) {
            BufferBuilder localValue12 = localValue10.internalMethod05457();
            Matrix4f localValue13 = localValue10.internalMethod03359().peek().getPositionMatrix();
            RenderSystem.setShaderTexture(0, localValue1);
            localValue12.vertex(localValue13, localValue2, localValue3, 0.0F).texture(0.0F, 0.0F).color(localValue6.getRGB());
            localValue12.vertex(localValue13, localValue2, localValue3 + localValue5, 0.0F).texture(0.0F, 1.0F).color(localValue6.getRGB());
            localValue12.vertex(localValue13, localValue2 + localValue4, localValue3 + localValue5, 0.0F).texture(1.0F, 1.0F).color(localValue6.getRGB());
            localValue12.vertex(localValue13, localValue2 + localValue4, localValue3, 0.0F).texture(1.0F, 0.0F).color(localValue6.getRGB());
         } else {
            UiBatchRenderer localValue7 = UiBatchRenderer.internalMethod08317();
            if (localValue7 != null) {
               localValue7.internalMethod02156(
                  rockstar.client.render.FramebufferCompat.glId(internalField0149.getTextureManager().getTexture(localValue1).getGlTexture()),
                  localValue0.peek().getPositionMatrix(),
                  localValue2,
                  localValue3,
                  localValue4,
                  localValue5,
                  0.0F,
                  0.0F,
                  1.0F,
                  1.0F,
                  localValue6
               );
            } else {
               localValue0.push();
               Matrix4f localValue11 = localValue0.peek().getPositionMatrix();
               RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
               RenderSystem.setShaderTexture(0, localValue1);
               internalMethod09058();
               BufferBuilder localValue9 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
               localValue9.vertex(localValue11, localValue2, localValue3, 0.0F).texture(0.0F, 0.0F).color(localValue6.getRGB());
               localValue9.vertex(localValue11, localValue2, localValue3 + localValue5, 0.0F).texture(0.0F, 1.0F).color(localValue6.getRGB());
               localValue9.vertex(localValue11, localValue2 + localValue4, localValue3 + localValue5, 0.0F).texture(1.0F, 1.0F).color(localValue6.getRGB());
               localValue9.vertex(localValue11, localValue2 + localValue4, localValue3, 0.0F).texture(1.0F, 0.0F).color(localValue6.getRGB());
               BufferRenderer.drawWithGlobalProgram(localValue9.end());
               internalMethod09648();
               RenderSystem.setShaderTexture(0, 0);
               localValue0.pop();
            }
         }
      }
   }

   public static void internalMethod01117(
      MatrixStack localValue0, Identifier localValue1, float localValue2, float localValue3, float localValue4, float localValue5, float localValue6, float localValue7, float localValue8, float localValue9, ColorRGBA localValue10
   ) {
      if (localValue1 != null && !internalMethod05199(localValue4, localValue5) && !internalMethod03563(localValue10)) {
         if (UiVertexBatch.internalMethod04253() instanceof TexturedQuadBatch localValue17) {
            BufferBuilder localValue19 = localValue17.internalMethod05457();
            Matrix4f localValue20 = localValue17.internalMethod03359().peek().getPositionMatrix();
            RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
            RenderSystem.setShaderTexture(0, localValue1);
            int localValue21 = localValue10.getRGB();
            float localValue22 = localValue2 + localValue4;
            float localValue23 = localValue3 + localValue5;
            localValue19.vertex(localValue20, localValue2, localValue3, 0.0F).texture(localValue6, localValue8).color(localValue21);
            localValue19.vertex(localValue20, localValue2, localValue23, 0.0F).texture(localValue6, localValue9).color(localValue21);
            localValue19.vertex(localValue20, localValue22, localValue23, 0.0F).texture(localValue7, localValue9).color(localValue21);
            localValue19.vertex(localValue20, localValue22, localValue3, 0.0F).texture(localValue7, localValue8).color(localValue21);
         } else {
            UiBatchRenderer localValue11 = UiBatchRenderer.internalMethod08317();
            if (localValue11 != null) {
               localValue11.internalMethod02156(
                  rockstar.client.render.FramebufferCompat.glId(internalField0149.getTextureManager().getTexture(localValue1).getGlTexture()),
                  localValue0.peek().getPositionMatrix(),
                  localValue2,
                  localValue3,
                  localValue4,
                  localValue5,
                  localValue6,
                  localValue8,
                  localValue7,
                  localValue9,
                  localValue10
               );
            } else {
               RenderSystem.enableBlend();
               RenderSystem.defaultBlendFunc();
               localValue0.push();
               int localValue18 = localValue10.getRGB();
               Matrix4f localValue13 = localValue0.peek().getPositionMatrix();
               float localValue14 = localValue2 + localValue4;
               float localValue15 = localValue3 + localValue5;
               RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
               RenderSystem.setShaderTexture(0, localValue1);
               BufferBuilder localValue16 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
               localValue16.vertex(localValue13, localValue2, localValue3, 0.0F).texture(localValue6, localValue8).color(localValue18);
               localValue16.vertex(localValue13, localValue2, localValue15, 0.0F).texture(localValue6, localValue9).color(localValue18);
               localValue16.vertex(localValue13, localValue14, localValue15, 0.0F).texture(localValue7, localValue9).color(localValue18);
               localValue16.vertex(localValue13, localValue14, localValue3, 0.0F).texture(localValue7, localValue8).color(localValue18);
               BufferRenderer.drawWithGlobalProgram(localValue16.end());
               internalMethod09648();
               RenderSystem.setShaderTexture(0, 0);
               localValue0.pop();
               RenderSystem.disableBlend();
            }
         }
      }
   }

   public static void internalMethod06903(MatrixStack localValue0, TextureRegion localValue1, float localValue2, float localValue3, float localValue4, float localValue5, ColorRGBA localValue6) {
      if (localValue1 != null) {
         internalMethod01117(
            localValue0,
            localValue1.internalMethod05466(),
            localValue2,
            localValue3,
            localValue4,
            localValue5,
            localValue1.internalMethod06427(),
            localValue1.internalMethod08077(),
            localValue1.internalMethod06429(),
            localValue1.internalMethod08079(),
            localValue6
         );
      }
   }

   public static void internalMethod02217(MatrixStack localValue0, IconSprite localValue1, float localValue2, float localValue3, float localValue4, float localValue5, ColorRGBA localValue6) {
      internalMethod01117(
         localValue0,
         RockstarClient.id(localValue1.internalMethod03213().internalMethod00021()),
         localValue2,
         localValue3,
         localValue4,
         localValue5,
         localValue1.internalField0205 / localValue1.internalMethod03213().internalMethod06562(),
         (localValue1.internalField0205 + localValue1.internalMethod03213().internalMethod08819()) / localValue1.internalMethod03213().internalMethod06562(),
         0.0F,
         1.0F,
         localValue6
      );
   }

   public static void internalMethod06642(MatrixStack localValue0, Identifier localValue1, float localValue2, float localValue3, float localValue4, float localValue5, CornerRadii localValue6) {
      internalMethod06728(localValue0, localValue1, localValue2, localValue3, localValue4, localValue5, localValue6, ThemeColors.internalField1312);
   }

   public static void internalMethod06728(MatrixStack localValue0, Identifier localValue1, float localValue2, float localValue3, float localValue4, float localValue5, CornerRadii localValue6, ColorRGBA localValue7) {
      if (localValue1 != null && !internalMethod05199(localValue4, localValue5) && !internalMethod03563(localValue7)) {
         localValue0.push();
         Matrix4f localValue8 = localValue0.peek().getPositionMatrix();
         float localValue9 = 0.5F;
         float localValue10 = -localValue9 / 2.0F + localValue9 * 2.0F;
         float localValue11 = localValue9 / 2.0F + localValue9;
         float localValue12 = localValue2 - localValue10 / 2.0F;
         float localValue13 = localValue3 - localValue11 / 2.0F;
         float localValue14 = localValue4 + localValue10;
         float localValue15 = localValue5 + localValue11;
         UiBatchRenderer localValue16 = UiBatchRenderer.internalMethod08317();
         if (localValue16 != null) {
            localValue16.internalMethod00549(
               rockstar.client.render.FramebufferCompat.glId(internalField0149.getTextureManager().getTexture(localValue1).getGlTexture()),
               localValue8,
               localValue12,
               localValue13,
               localValue14,
               localValue15,
               0.0F,
               0.0F,
               1.0F,
               1.0F,
               localValue4,
               localValue5,
               localValue6.internalMethod05337(),
               localValue6.internalMethod08942(),
               localValue6.internalMethod05340(),
               localValue6.internalMethod08939(),
               localValue9,
               localValue7
            );
            localValue0.pop();
         } else {
            UiBatchRenderer.internalMethod02576();
            internalField1419.internalMethod01220();
            RenderSystem.setShaderTexture(0, localValue1);
            internalField1419.internalMethod05981("Size").set(localValue4, localValue5);
            internalField1419.internalMethod05981("Radius")
               .set(localValue6.internalMethod05337(), localValue6.internalMethod08942(), localValue6.internalMethod05340(), localValue6.internalMethod08939());
            internalField1419.internalMethod05981("Smoothness").set(localValue9);
            internalMethod09058();
            BufferBuilder localValue17 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
            localValue17.vertex(localValue8, localValue12, localValue13, 0.0F).texture(0.0F, 0.0F).color(localValue7.getRGB());
            localValue17.vertex(localValue8, localValue12, localValue13 + localValue15, 0.0F).texture(0.0F, 1.0F).color(localValue7.getRGB());
            localValue17.vertex(localValue8, localValue12 + localValue14, localValue13 + localValue15, 0.0F).texture(1.0F, 1.0F).color(localValue7.getRGB());
            localValue17.vertex(localValue8, localValue12 + localValue14, localValue13, 0.0F).texture(1.0F, 0.0F).color(localValue7.getRGB());
            BufferRenderer.drawWithGlobalProgram(localValue17.end());
            internalMethod09648();
            RenderSystem.setShaderTexture(0, 0);
            localValue0.pop();
         }
      }
   }

   public static void internalMethod08818(MatrixStack localValue0, float localValue1, float localValue2, float localValue3, float localValue4, float localValue5, CornerRadii localValue6, ColorRGBA localValue7) {
      if (!internalMethod05199(localValue3, localValue4) && !(localValue5 <= 0.0F) && !internalMethod03563(localValue7)) {
         localValue0.push();
         Matrix4f localValue8 = localValue0.peek().getPositionMatrix();
         if (UiVertexBatch.internalMethod04253() instanceof TexturedQuadBatch localValue17) {
            BufferBuilder localValue19 = localValue17.internalMethod05457();
            float localValue20 = -localValue5 / 2.0F + localValue5 * 2.0F;
            float localValue21 = localValue5 / 2.0F + localValue5;
            float localValue22 = localValue1 - localValue20 / 2.0F;
            float localValue23 = localValue2 - localValue21 / 2.0F;
            float localValue24 = localValue3 + localValue20;
            float localValue25 = localValue4 + localValue21;
            localValue19.vertex(localValue8, localValue22, localValue23, 0.0F).color(localValue7.getRGB());
            localValue19.vertex(localValue8, localValue22, localValue23 + localValue25, 0.0F).color(localValue7.getRGB());
            localValue19.vertex(localValue8, localValue22 + localValue24, localValue23 + localValue25, 0.0F).color(localValue7.getRGB());
            localValue19.vertex(localValue8, localValue22 + localValue24, localValue23, 0.0F).color(localValue7.getRGB());
            localValue0.pop();
         } else {
            UiBatchRenderer localValue9 = UiBatchRenderer.internalMethod08317();
            if (localValue9 != null) {
               localValue9.internalMethod00020(localValue8, localValue1, localValue2, localValue3, localValue4, localValue5, localValue6, localValue7);
               localValue0.pop();
            } else {
               internalField0104.internalMethod01220();
               internalField0104.internalMethod05981("Size").set(localValue3, localValue4);
               internalField0104.internalMethod05981("Radius")
                  .set(localValue6.internalMethod05337() * 3.0F, localValue6.internalMethod08942() * 3.0F, localValue6.internalMethod05340() * 3.0F, localValue6.internalMethod08939() * 3.0F);
               internalField0104.internalMethod05981("Smoothness").set(localValue5);
               internalMethod09058();
               float localValue18 = -localValue5 / 2.0F + localValue5 * 2.0F;
               float localValue11 = localValue5 / 2.0F + localValue5;
               float localValue12 = localValue1 - localValue18 / 2.0F;
               float localValue13 = localValue2 - localValue11 / 2.0F;
               float localValue14 = localValue3 + localValue18;
               float localValue15 = localValue4 + localValue11;
               BufferBuilder localValue16 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
               localValue16.vertex(localValue8, localValue12, localValue13, 0.0F).color(localValue7.getRGB());
               localValue16.vertex(localValue8, localValue12, localValue13 + localValue15, 0.0F).color(localValue7.getRGB());
               localValue16.vertex(localValue8, localValue12 + localValue14, localValue13 + localValue15, 0.0F).color(localValue7.getRGB());
               localValue16.vertex(localValue8, localValue12 + localValue14, localValue13, 0.0F).color(localValue7.getRGB());
               BufferRenderer.drawWithGlobalProgram(localValue16.end());
               internalMethod09648();
               localValue0.pop();
            }
         }
      }
   }

   public static void internalMethod06000(float localValue0, float localValue1, CornerRadii localValue2) {
      if (!(localValue0 <= 0.0F)) {
         internalField0314.internalMethod02255(2.0F);
         internalField1421.internalMethod01220();
         RenderSystem.setShaderTexture(0, internalMethod01904());
         internalField1421.internalMethod05981("Size")
            .set(internalField0149.getWindow().getScaledWidth() * 2.0F, internalField0149.getWindow().getScaledHeight() * 2.0F);
         internalField1421.internalMethod05981("Radius")
            .set(
               localValue2.internalMethod05337() * localValue1 / 2.0F,
               localValue2.internalMethod08942() * localValue1 / 2.0F,
               localValue2.internalMethod05340() * localValue1 / 2.0F,
               localValue2.internalMethod08939() * localValue1 / 2.0F
            );
         internalField1421.internalMethod05981("Smoothness").set(0.1F);
         internalField1421.internalMethod05981("CornerSmoothness").set(localValue1);
         internalField1421.internalMethod05981("ScreenSize").set(internalField0149.getWindow().getScaledWidth(), internalField0149.getWindow().getScaledHeight());
         internalMethod09058();
         RenderSystem.enableDepthTest();
         RenderSystem.disableCull();
         RenderSystem.depthMask(false);
      }
   }

   public static void internalMethod09057() {
      internalMethod09648();
      RenderSystem.depthMask(true);
      RenderSystem.setShaderTexture(0, 0);
      RenderSystem.disableBlend();
      RenderSystem.setShaderTexture(0, 0);
   }

   public static void internalMethod02643(MatrixStack localValue0, float localValue1, float localValue2, float localValue3, float localValue4, ColorRGBA localValue5) {
      Matrix4f localValue6 = localValue0.peek().getPositionMatrix();
      float localValue7 = 0.03F;
      float localValue8 = -localValue7 / 2.0F + localValue7 * 2.0F;
      float localValue9 = localValue7 / 2.0F + localValue7;
      float localValue10 = localValue1 - localValue8 / 2.0F;
      float localValue11 = localValue2 - localValue9 / 2.0F;
      float localValue12 = localValue3 + localValue8;
      float localValue13 = localValue4 + localValue9;
      int localValue14 = internalField0149.getWindow().getScaledWidth();
      int localValue15 = internalField0149.getWindow().getScaledHeight();
      float localValue16 = localValue10 / localValue14;
      float localValue17 = (localValue15 - localValue11 - localValue13) / localValue15;
      float localValue18 = localValue12 / localValue14;
      float localValue19 = localValue13 / localValue15;
      BufferBuilder localValue20 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
      localValue20.vertex(localValue6, localValue10, localValue11, 0.0F).texture(localValue16, localValue17 + localValue19).color(localValue5.getRGB());
      localValue20.vertex(localValue6, localValue10, localValue11 + localValue13, 0.0F).texture(localValue16, localValue17).color(localValue5.getRGB());
      localValue20.vertex(localValue6, localValue10 + localValue12, localValue11 + localValue13, 0.0F).texture(localValue16 + localValue18, localValue17).color(localValue5.getRGB());
      localValue20.vertex(localValue6, localValue10 + localValue12, localValue11, 0.0F).texture(localValue16 + localValue18, localValue17 + localValue19).color(localValue5.getRGB());
      BufferRenderer.drawWithGlobalProgram(localValue20.end());
   }

   public static void internalMethod01296(UiBatchRenderer.InternalType0472 localValue0, List<UiBatchRenderer.InternalType0473> localValue1) {
      if (localValue0 != null && localValue1 != null && !localValue1.isEmpty()) {
         if (localValue0.internalMethod02687()) {
            internalField1418.internalMethod01220();
            internalField1418.internalMethod05981("Size").set(localValue0.internalMethod02685(), localValue0.internalMethod02689());
            internalField1418.internalMethod05981("Radius")
               .set(localValue0.internalMethod07774(), localValue0.internalMethod07775(), localValue0.internalMethod07788(), localValue0.internalMethod07790());
            internalField1418.internalMethod05981("Smoothness").set(localValue0.internalMethod09561());
            internalField1418.internalMethod05981("CornerSmoothness").set(localValue0.internalMethod09562());
         } else {
            internalField1419.internalMethod01220();
            internalField1419.internalMethod05981("Size").set(localValue0.internalMethod02685(), localValue0.internalMethod02689());
            internalField1419.internalMethod05981("Radius")
               .set(localValue0.internalMethod07774(), localValue0.internalMethod07775(), localValue0.internalMethod07788(), localValue0.internalMethod07790());
            internalField1419.internalMethod05981("Smoothness").set(localValue0.internalMethod09561());
         }

         RenderSystem.setShaderTexture(0, localValue0.internalMethod02686());
         internalMethod09058();
         BufferBuilder localValue2 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

         for (UiBatchRenderer.InternalType0473 localValue4 : localValue1) {
            localValue2.vertex(localValue4.internalMethod05108(), localValue4.internalMethod04475(), localValue4.internalMethod04478(), 0.0F)
               .texture(localValue4.internalMethod08758(), localValue4.internalMethod08759())
               .color(localValue4.internalMethod04476());
            localValue2.vertex(localValue4.internalMethod05108(), localValue4.internalMethod04475(), localValue4.internalMethod04478() + localValue4.internalMethod08747(), 0.0F)
               .texture(localValue4.internalMethod08758(), localValue4.internalMethod09541())
               .color(localValue4.internalMethod04476());
            localValue2.vertex(
                  localValue4.internalMethod05108(), localValue4.internalMethod04475() + localValue4.internalMethod08746(), localValue4.internalMethod04478() + localValue4.internalMethod08747(), 0.0F
               )
               .texture(localValue4.internalMethod09540(), localValue4.internalMethod09541())
               .color(localValue4.internalMethod04476());
            localValue2.vertex(localValue4.internalMethod05108(), localValue4.internalMethod04475() + localValue4.internalMethod08746(), localValue4.internalMethod04478(), 0.0F)
               .texture(localValue4.internalMethod09540(), localValue4.internalMethod08759())
               .color(localValue4.internalMethod04476());
         }

         BufferRenderer.drawWithGlobalProgram(localValue2.end());
         internalMethod09648();
         RenderSystem.setShaderTexture(0, 0);
      }
   }

   public static void internalMethod01599(
      MatrixStack localValue0,
      float localValue1,
      float localValue2,
      float localValue3,
      float localValue4,
      float localValue5,
      float localValue6,
      float localValue7,
      float localValue8,
      float localValue9,
      float localValue10,
      float localValue11,
      CornerRadii localValue12,
      ColorRGBA localValue13
   ) {
      internalMethod02681(localValue0, 0, localValue1, localValue2, localValue3, localValue4, localValue5, localValue6, localValue7, localValue8, localValue9, localValue10, localValue11, localValue12, localValue13);
   }

   public static void internalMethod02681(
      MatrixStack localValue0,
      int localValue1,
      float localValue2,
      float localValue3,
      float localValue4,
      float localValue5,
      float localValue6,
      float localValue7,
      float localValue8,
      float localValue9,
      float localValue10,
      float localValue11,
      float localValue12,
      CornerRadii localValue13,
      ColorRGBA localValue14
   ) {
      if (!internalMethod05199(localValue4, localValue5) && !internalMethod03563(localValue14) && internalField0312.internalMethod02312(localValue1)) {
         if (!UiBatchRenderer.internalField0277) {
            localValue0.push();
            Matrix4f localValue15 = localValue0.peek().getPositionMatrix();
            int localValue16 = internalField0149.getWindow().getScaledWidth();
            int localValue17 = internalField0149.getWindow().getScaledHeight();
            float localValue18;
            float localValue19;
            float localValue20;
            float localValue21;
            if (!MatrixUtil.isIdentity(localValue15)) {
               Vector3f localValue22 = new Vector3f(localValue2, localValue3, 0.0F);
               Vector3f localValue23 = new Vector3f(localValue2 + localValue4, localValue3 + localValue5, 0.0F);
               localValue15.transformPosition(localValue22);
               localValue15.transformPosition(localValue23);
               localValue18 = localValue22.x / localValue16;
               localValue19 = (localValue17 - localValue22.y - (localValue23.y - localValue22.y)) / localValue17;
               localValue20 = (localValue23.x - localValue22.x) / localValue16;
               localValue21 = (localValue23.y - localValue22.y) / localValue17;
            } else {
               localValue18 = localValue2 / localValue16;
               localValue19 = (localValue17 - localValue3 - localValue5) / localValue17;
               localValue20 = localValue4 / localValue16;
               localValue21 = localValue5 / localValue17;
            }

            internalField1672.internalMethod01220();
            RenderSystem.setShaderTexture(0, internalField0312.internalMethod02310(localValue1));
            internalField1672.internalMethod05981("Size").set(localValue4, localValue5);
            internalField1672.internalMethod05981("Radius")
               .set(localValue13.internalMethod05337(), localValue13.internalMethod08942(), localValue13.internalMethod05340(), localValue13.internalMethod08939());
            internalField1672.internalMethod05981("Smoothness").set(localValue6);
            internalField1672.internalMethod05981("FadeStart").set(localValue7);
            internalField1672.internalMethod05981("FadeEnd").set(localValue8);
            internalField1672.internalMethod05981("ClampMin").set(localValue9 / localValue16, (localValue17 - (localValue10 + localValue12)) / localValue17);
            internalField1672.internalMethod05981("ClampMax").set((localValue9 + localValue11) / localValue16, (localValue17 - localValue10) / localValue17);
            internalMethod09058();
            BufferBuilder localValue24 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
            localValue24.vertex(localValue15, localValue2, localValue3, 0.0F).texture(localValue18, localValue19 + localValue21).color(localValue14.getRGB());
            localValue24.vertex(localValue15, localValue2, localValue3 + localValue5, 0.0F).texture(localValue18, localValue19).color(localValue14.getRGB());
            localValue24.vertex(localValue15, localValue2 + localValue4, localValue3 + localValue5, 0.0F).texture(localValue18 + localValue20, localValue19).color(localValue14.getRGB());
            localValue24.vertex(localValue15, localValue2 + localValue4, localValue3, 0.0F).texture(localValue18 + localValue20, localValue19 + localValue21).color(localValue14.getRGB());
            BufferRenderer.drawWithGlobalProgram(localValue24.end());
            internalMethod09648();
            RenderSystem.setShaderTexture(0, 0);
            localValue0.pop();
         }
      }
   }

   public static void internalMethod06570(
      MatrixStack localValue0, float localValue1, float localValue2, float localValue3, float localValue4, float localValue5, float localValue6, CornerRadii localValue7, ColorRGBA localValue8
   ) {
      if (!internalMethod05199(localValue3, localValue4) && !(localValue5 <= 0.0F) && !internalMethod03563(localValue8)) {
         localValue5 /= 22.5F;
         if (!(localValue5 <= 0.0F)) {
            localValue0.push();
            Matrix4f localValue9 = localValue0.peek().getPositionMatrix();
            float localValue10 = 0.03F;
            float localValue11 = -localValue10 / 2.0F + localValue10 * 2.0F;
            float localValue12 = localValue10 / 2.0F + localValue10;
            float localValue13 = localValue1 - localValue11 / 2.0F;
            float localValue14 = localValue2 - localValue12 / 2.0F;
            float localValue15 = localValue3 + localValue11;
            float localValue16 = localValue4 + localValue12;
            int localValue17 = internalField0149.getWindow().getScaledWidth();
            int localValue18 = internalField0149.getWindow().getScaledHeight();
            float localValue19;
            float localValue20;
            float localValue21;
            float localValue22;
            if (!MatrixUtil.isIdentity(localValue9)) {
               Vector3f localValue23 = new Vector3f(localValue13, localValue14, 0.0F);
               Vector3f localValue24 = new Vector3f(localValue13 + localValue15, localValue14 + localValue16, 0.0F);
               localValue9.transformPosition(localValue23);
               localValue9.transformPosition(localValue24);
               localValue19 = localValue23.x / localValue17;
               localValue20 = (localValue18 - localValue23.y - (localValue24.y - localValue23.y)) / localValue18;
               localValue21 = (localValue24.x - localValue23.x) / localValue17;
               localValue22 = (localValue24.y - localValue23.y) / localValue18;
            } else {
               localValue19 = localValue13 / localValue17;
               localValue20 = (localValue18 - localValue14 - localValue16) / localValue18;
               localValue21 = localValue15 / localValue17;
               localValue22 = localValue16 / localValue18;
            }

            internalField0314.internalMethod02255(2.0F);
            UiBatchRenderer localValue26 = UiBatchRenderer.internalMethod08317();
            if (localValue26 != null) {
               localValue26.internalMethod02893(
                  internalMethod01904(),
                  localValue9,
                  localValue13,
                  localValue14,
                  localValue15,
                  localValue16,
                  localValue19,
                  localValue20 + localValue22,
                  localValue19 + localValue21,
                  localValue20,
                  localValue3,
                  localValue4,
                  localValue7.internalMethod05337() * localValue6 / 2.0F,
                  localValue7.internalMethod08942() * localValue6 / 2.0F,
                  localValue7.internalMethod05340() * localValue6 / 2.0F,
                  localValue7.internalMethod08939() * localValue6 / 2.0F,
                  0.1F,
                  localValue6,
                  localValue8
               );
               localValue0.pop();
            } else {
               internalField1418.internalMethod01220();
               RenderSystem.setShaderTexture(0, internalMethod01904());
               internalField1418.internalMethod05981("Size").set(localValue3, localValue4);
               internalField1418.internalMethod05981("Radius")
                  .set(
                     localValue7.internalMethod05337() * localValue6 / 2.0F,
                     localValue7.internalMethod08942() * localValue6 / 2.0F,
                     localValue7.internalMethod05340() * localValue6 / 2.0F,
                     localValue7.internalMethod08939() * localValue6 / 2.0F
                  );
               internalField1418.internalMethod05981("Smoothness").set(0.1F);
               internalField1418.internalMethod05981("CornerSmoothness").set(localValue6);
               internalMethod09058();
               BufferBuilder localValue27 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
               localValue27.vertex(localValue9, localValue13, localValue14, 0.0F).texture(localValue19, localValue20 + localValue22).color(localValue8.getRGB());
               localValue27.vertex(localValue9, localValue13, localValue14 + localValue16, 0.0F).texture(localValue19, localValue20).color(localValue8.getRGB());
               localValue27.vertex(localValue9, localValue13 + localValue15, localValue14 + localValue16, 0.0F).texture(localValue19 + localValue21, localValue20).color(localValue8.getRGB());
               localValue27.vertex(localValue9, localValue13 + localValue15, localValue14, 0.0F).texture(localValue19 + localValue21, localValue20 + localValue22).color(localValue8.getRGB());
               BufferRenderer.drawWithGlobalProgram(localValue27.end());
               internalMethod09648();
               RenderSystem.setShaderTexture(0, 0);
               localValue0.pop();
            }
         }
      }
   }

   public static void internalMethod08703(MatrixStack localValue0, float localValue1, float localValue2, float localValue3, float localValue4, float localValue5, CornerRadii localValue6, ColorRGBA localValue7) {
      if (!internalMethod05199(localValue3, localValue4) && !(localValue5 <= 0.0F) && !internalMethod03563(localValue7)) {
         localValue5 /= 22.5F;
         if (!(localValue5 <= 0.0F)) {
            localValue0.push();
            Matrix4f localValue8 = localValue0.peek().getPositionMatrix();
            int localValue9 = internalField0149.getWindow().getScaledWidth();
            int localValue10 = internalField0149.getWindow().getScaledHeight();
            float localValue11;
            float localValue12;
            float localValue13;
            float localValue14;
            if (!MatrixUtil.isIdentity(localValue8)) {
               Vector3f localValue15 = new Vector3f(localValue1, localValue2, 0.0F);
               Vector3f localValue16 = new Vector3f(localValue1 + localValue3, localValue2 + localValue4, 0.0F);
               localValue8.transformPosition(localValue15);
               localValue8.transformPosition(localValue16);
               localValue11 = localValue15.x / localValue9;
               localValue12 = (localValue10 - localValue15.y - (localValue16.y - localValue15.y)) / localValue10;
               localValue13 = (localValue16.x - localValue15.x) / localValue9;
               localValue14 = (localValue16.y - localValue15.y) / localValue10;
            } else {
               localValue11 = localValue1 / localValue9;
               localValue12 = (localValue10 - localValue2 - localValue4) / localValue10;
               localValue13 = localValue3 / localValue9;
               localValue14 = localValue4 / localValue10;
            }

            internalField0314.internalMethod02255(2.0F);
            UiBatchRenderer localValue18 = UiBatchRenderer.internalMethod08317();
            if (localValue18 != null) {
               localValue18.internalMethod00549(
                  internalMethod01904(),
                  localValue8,
                  localValue1,
                  localValue2,
                  localValue3,
                  localValue4,
                  localValue11,
                  localValue12 + localValue14,
                  localValue11 + localValue13,
                  localValue12,
                  localValue3,
                  localValue4,
                  localValue6.internalMethod05337(),
                  localValue6.internalMethod08942(),
                  localValue6.internalMethod05340(),
                  localValue6.internalMethod08939(),
                  0.01F,
                  localValue7
               );
               localValue0.pop();
            } else {
               internalField1419.internalMethod01220();
               RenderSystem.setShaderTexture(0, internalMethod01904());
               internalField1419.internalMethod05981("Size").set(localValue3, localValue4);
               internalField1419.internalMethod05981("Radius")
                  .set(localValue6.internalMethod05337(), localValue6.internalMethod08942(), localValue6.internalMethod05340(), localValue6.internalMethod08939());
               internalField1419.internalMethod05981("Smoothness").set(0.01F);
               internalMethod09058();
               BufferBuilder localValue19 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
               localValue19.vertex(localValue8, localValue1, localValue2, 0.0F).texture(localValue11, localValue12 + localValue14).color(localValue7.getRGB());
               localValue19.vertex(localValue8, localValue1, localValue2 + localValue4, 0.0F).texture(localValue11, localValue12).color(localValue7.getRGB());
               localValue19.vertex(localValue8, localValue1 + localValue3, localValue2 + localValue4, 0.0F).texture(localValue11 + localValue13, localValue12).color(localValue7.getRGB());
               localValue19.vertex(localValue8, localValue1 + localValue3, localValue2, 0.0F).texture(localValue11 + localValue13, localValue12 + localValue14).color(localValue7.getRGB());
               BufferRenderer.drawWithGlobalProgram(localValue19.end());
               internalMethod09648();
               RenderSystem.setShaderTexture(0, 0);
               localValue0.pop();
            }
         }
      }
   }

   public static void internalMethod05578(MatrixStack localValue0, Rect localValue1, float localValue2, float localValue3, float localValue4, float localValue5, CornerRadii localValue6, float localValue7) {
      if (!internalMethod05199(localValue4, localValue5) && !(localValue7 <= 0.0F)) {
         ColorRGBA localValue8 = ColorRGBA.WHITE.mulAlpha(localValue7);
         localValue0.push();
         Matrix4f localValue9 = localValue0.peek().getPositionMatrix();
         internalField0314.internalMethod02255(2.0F);
         internalField1419.internalMethod01220();
         RenderSystem.setShaderTexture(0, RockstarClient.id("rocknet/blur.png"));
         internalField1419.internalMethod05981("Size").set(localValue4, localValue5);
         internalField1419.internalMethod05981("Radius")
            .set(localValue6.internalMethod05337(), localValue6.internalMethod08942(), localValue6.internalMethod05340(), localValue6.internalMethod08939());
         internalField1419.internalMethod05981("Smoothness").set(0.5F);
         internalMethod09058();
         float localValue10 = localValue1.getWidth();
         float localValue11 = localValue1.getHeight();
         float localValue12;
         float localValue13;
         float localValue14;
         float localValue15;
         if (!MatrixUtil.isIdentity(localValue9)) {
            Vector3f localValue16 = new Vector3f(localValue2, localValue3, 0.0F);
            Vector3f localValue17 = new Vector3f(localValue2 + localValue4, localValue3 + localValue5, 0.0F);
            localValue9.transformPosition(localValue16);
            localValue9.transformPosition(localValue17);
            float localValue18 = localValue16.x;
            float localValue19 = localValue16.y;
            float localValue20 = localValue17.x - localValue16.x;
            float localValue21 = localValue17.y - localValue16.y;
            localValue12 = (localValue18 - localValue1.getX()) / localValue10;
            localValue13 = (localValue19 - localValue1.getY()) / localValue11 + localValue21 / localValue11;
            localValue14 = localValue20 / localValue10;
            localValue15 = -localValue21 / localValue11;
         } else {
            localValue12 = (localValue2 - localValue1.getX()) / localValue10;
            localValue13 = (localValue3 - localValue1.getY()) / localValue11 + localValue5 / localValue11;
            localValue14 = localValue4 / localValue10;
            localValue15 = -localValue5 / localValue11;
         }

         BufferBuilder localValue22 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
         localValue22.vertex(localValue9, localValue2, localValue3, 0.0F).texture(localValue12, localValue13 + localValue15).color(localValue8.getRGB());
         localValue22.vertex(localValue9, localValue2, localValue3 + localValue5, 0.0F).texture(localValue12, localValue13).color(localValue8.getRGB());
         localValue22.vertex(localValue9, localValue2 + localValue4, localValue3 + localValue5, 0.0F).texture(localValue12 + localValue14, localValue13).color(localValue8.getRGB());
         localValue22.vertex(localValue9, localValue2 + localValue4, localValue3, 0.0F).texture(localValue12 + localValue14, localValue13 + localValue15).color(localValue8.getRGB());
         BufferRenderer.drawWithGlobalProgram(localValue22.end());
         internalMethod09648();
         RenderSystem.setShaderTexture(0, 0);
         localValue0.pop();
      }
   }

   public static void internalMethod05007(MatrixStack localValue0, BufferBuilder localValue1, double localValue2, double localValue4, double localValue6, double localValue8, double localValue10, ColorRGBA localValue12) {
      if (!(localValue8 <= 0.0) && !(localValue10 <= 0.0) && !internalMethod03563(localValue12)) {
         Matrix4f localValue13 = localValue0.peek().getPositionMatrix();
         localValue1.vertex(localValue13, (float)localValue2, (float)(localValue4 + localValue10), (float)localValue6).texture(0.0F, 1.0F).color(localValue12.getRGB());
         localValue1.vertex(localValue13, (float)(localValue2 + localValue8), (float)(localValue4 + localValue10), (float)localValue6).texture(1.0F, 1.0F).color(localValue12.getRGB());
         localValue1.vertex(localValue13, (float)(localValue2 + localValue8), (float)localValue4, (float)localValue6).texture(1.0F, 0.0F).color(localValue12.getRGB());
         localValue1.vertex(localValue13, (float)localValue2, (float)localValue4, (float)localValue6).texture(0.0F, 0.0F).color(localValue12.getRGB());
      }
   }

   public static void internalMethod04257(
      MatrixStack localValue0, BufferBuilder localValue1, double localValue2, double localValue4, double localValue6, double localValue8, double localValue10, ColorRGBA localValue12, int localValue13
   ) {
      if (!(localValue8 <= 0.0) && !(localValue10 <= 0.0) && !internalMethod03563(localValue12)) {
         Matrix4f localValue14 = localValue0.peek().getPositionMatrix();
         float localValue15 = (float)localValue2;
         float localValue16 = (float)(localValue2 + localValue8);
         float localValue17 = (float)localValue4;
         float localValue18 = (float)(localValue4 + localValue10);
         float localValue19 = (float)localValue6;
         float localValue20 = 0.0F;
         float localValue21 = 0.0F;
         float localValue22 = 1.0F;
         float localValue23 = 1.0F;
         localValue13 = (localValue13 % 360 + 360) % 360;
         switch (localValue13) {
            case 0:
               localValue1.vertex(localValue14, localValue15, localValue18, localValue19).texture(localValue20, localValue23).color(localValue12.getRGB());
               localValue1.vertex(localValue14, localValue16, localValue18, localValue19).texture(localValue22, localValue23).color(localValue12.getRGB());
               localValue1.vertex(localValue14, localValue16, localValue17, localValue19).texture(localValue22, localValue21).color(localValue12.getRGB());
               localValue1.vertex(localValue14, localValue15, localValue17, localValue19).texture(localValue20, localValue21).color(localValue12.getRGB());
               break;
            case 180:
               localValue1.vertex(localValue14, localValue15, localValue18, localValue19).texture(localValue22, localValue21).color(localValue12.getRGB());
               localValue1.vertex(localValue14, localValue16, localValue18, localValue19).texture(localValue20, localValue21).color(localValue12.getRGB());
               localValue1.vertex(localValue14, localValue16, localValue17, localValue19).texture(localValue20, localValue23).color(localValue12.getRGB());
               localValue1.vertex(localValue14, localValue15, localValue17, localValue19).texture(localValue22, localValue23).color(localValue12.getRGB());
         }
      }
   }

   public static void internalMethod04502(MatrixStack localValue0, Identifier localValue1, double localValue2, double localValue4, double localValue6, double localValue8, double localValue10, ColorRGBA localValue12) {
      if (localValue1 != null && !(localValue8 <= 0.0) && !(localValue10 <= 0.0) && !internalMethod03563(localValue12)) {
         RenderSystem.setShaderTexture(0, localValue1);
         BufferBuilder localValue13 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
         Matrix4f localValue14 = localValue0.peek().getPositionMatrix();
         localValue13.vertex(localValue14, (float)localValue2, (float)(localValue4 + localValue10), (float)localValue6).texture(0.0F, 1.0F).color(localValue12.getRGB());
         localValue13.vertex(localValue14, (float)(localValue2 + localValue8), (float)(localValue4 + localValue10), (float)localValue6).texture(1.0F, 1.0F).color(localValue12.getRGB());
         localValue13.vertex(localValue14, (float)(localValue2 + localValue8), (float)localValue4, (float)localValue6).texture(1.0F, 0.0F).color(localValue12.getRGB());
         localValue13.vertex(localValue14, (float)localValue2, (float)localValue4, (float)localValue6).texture(0.0F, 0.0F).color(localValue12.getRGB());
         BufferRenderer.drawWithGlobalProgram(localValue13.end());
      }
   }

   public static void internalMethod01072(
      MatrixStack localValue0, AbstractClientPlayerEntity localValue1, float localValue2, float localValue3, float localValue4, CornerRadii localValue5, ColorRGBA localValue6
   ) {
      if (localValue1 != null && !(localValue4 <= 0.0F) && !internalMethod03563(localValue6)) {
         Identifier localValue7 = localValue1.getSkin().body().texturePath();
         internalMethod00808(localValue0, localValue7, localValue2, localValue3, localValue4, localValue5, localValue6);
         internalMethod01174(localValue0, localValue7, localValue2, localValue3, localValue4, localValue5, localValue6);
      }
   }

   public static <T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> void internalMethod03763(
      MatrixStack localValue0, T localValue1, float localValue2, float localValue3, float localValue4, CornerRadii localValue5, ColorRGBA localValue6
   ) {
      if (localValue1 != null && !(localValue4 <= 0.0F) && !internalMethod03563(localValue6)) {
         EntityRenderer localValue7 = internalField0149.getEntityRenderDispatcher().getRenderer(localValue1);
         if (localValue7 instanceof LivingEntityRenderer localValue9) {
            LivingEntityRenderer localValue10 = (LivingEntityRenderer)localValue7;
            LivingEntityRenderState localValue11 = (LivingEntityRenderState)localValue10.createRenderState();
            Identifier localValue8 = localValue10.getTexture(localValue11);
            internalMethod00808(localValue0, localValue8, localValue2, localValue3, localValue4, localValue5, localValue6);
            internalMethod01174(localValue0, localValue8, localValue2, localValue3, localValue4, localValue5, localValue6);
         }
      }
   }

   public static void internalMethod00808(MatrixStack localValue0, Identifier localValue1, float localValue2, float localValue3, float localValue4, CornerRadii localValue5, ColorRGBA localValue6) {
      if (localValue1 != null && !(localValue4 <= 0.0F) && !internalMethod03563(localValue6)) {
         internalMethod02676(localValue0, localValue1, localValue2, localValue3, localValue4, localValue4, localValue5, localValue6, 0.125F, 0.125F, 0.25F, 0.25F);
      }
   }

   private static void internalMethod01174(MatrixStack localValue0, Identifier localValue1, float localValue2, float localValue3, float localValue4, CornerRadii localValue5, ColorRGBA localValue6) {
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      internalMethod02676(localValue0, localValue1, localValue2, localValue3, localValue4, localValue4, localValue5, localValue6, 0.625F, 0.125F, 0.75F, 0.25F);
      RenderSystem.disableBlend();
   }

   public static void internalMethod02676(
      MatrixStack localValue0,
      Identifier localValue1,
      float localValue2,
      float localValue3,
      float localValue4,
      float localValue5,
      CornerRadii localValue6,
      ColorRGBA localValue7,
      float localValue8,
      float localValue9,
      float localValue10,
      float localValue11
   ) {
      if (localValue1 != null && !internalMethod05199(localValue4, localValue5) && !internalMethod03563(localValue7)) {
         localValue0.push();
         Matrix4f localValue12 = localValue0.peek().getPositionMatrix();
         float localValue13 = 0.5F;
         float localValue14 = -localValue13 / 2.0F + localValue13 * 2.0F;
         float localValue15 = localValue13 / 2.0F + localValue13;
         float localValue16 = localValue2 - localValue14 / 2.0F;
         float localValue17 = localValue3 - localValue15 / 2.0F;
         float localValue18 = localValue4 + localValue14;
         float localValue19 = localValue5 + localValue15;
         UiBatchRenderer localValue20 = UiBatchRenderer.internalMethod08317();
         if (localValue20 != null) {
            localValue20.internalMethod00549(
               rockstar.client.render.FramebufferCompat.glId(internalField0149.getTextureManager().getTexture(localValue1).getGlTexture()),
               localValue12,
               localValue16,
               localValue17,
               localValue18,
               localValue19,
               localValue8,
               localValue9,
               localValue10,
               localValue11,
               localValue4,
               localValue5,
               localValue6.internalMethod05337(),
               localValue6.internalMethod08942(),
               localValue6.internalMethod05340(),
               localValue6.internalMethod08939(),
               localValue13,
               localValue7
            );
            localValue0.pop();
         } else {
            internalField1419.internalMethod01220();
            RenderSystem.setShaderTexture(0, localValue1);
            internalField1419.internalMethod05981("Size").set(localValue4, localValue5);
            internalField1419.internalMethod05981("Radius")
               .set(localValue6.internalMethod05337(), localValue6.internalMethod08942(), localValue6.internalMethod05340(), localValue6.internalMethod08939());
            internalField1419.internalMethod05981("Smoothness").set(localValue13);
            internalMethod09058();
            BufferBuilder localValue21 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
            localValue21.vertex(localValue12, localValue16, localValue17, 0.0F).texture(localValue8, localValue9).color(localValue7.getRGB());
            localValue21.vertex(localValue12, localValue16, localValue17 + localValue19, 0.0F).texture(localValue8, localValue11).color(localValue7.getRGB());
            localValue21.vertex(localValue12, localValue16 + localValue18, localValue17 + localValue19, 0.0F).texture(localValue10, localValue11).color(localValue7.getRGB());
            localValue21.vertex(localValue12, localValue16 + localValue18, localValue17, 0.0F).texture(localValue10, localValue9).color(localValue7.getRGB());
            BufferRenderer.drawWithGlobalProgram(localValue21.end());
            internalMethod09648();
            RenderSystem.setShaderTexture(0, 0);
            localValue0.pop();
         }
      }
   }

   public static void internalMethod04875(MatrixStack localValue0, float localValue1, float localValue2, float localValue3, float localValue4) {
      if (!internalMethod05199(localValue3, localValue4)) {
         byte localValue5 = -1;
         Matrix4f localValue6 = localValue0.peek().getPositionMatrix();
         BufferBuilder localValue7 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
         localValue7.vertex(localValue6, localValue1, localValue2, 0.0F).texture(0.0F, 1.0F).color(localValue5);
         localValue7.vertex(localValue6, localValue1, localValue2 + localValue4, 0.0F).texture(0.0F, 0.0F).color(localValue5);
         localValue7.vertex(localValue6, localValue1 + localValue3, localValue2 + localValue4, 0.0F).texture(1.0F, 0.0F).color(localValue5);
         localValue7.vertex(localValue6, localValue1 + localValue3, localValue2, 0.0F).texture(1.0F, 1.0F).color(localValue5);
         BufferRenderer.drawWithGlobalProgram(localValue7.end());
      }
   }

   public static void internalMethod01737(float localValue0, float localValue1, float localValue2, float localValue3) {
      if (!internalMethod05199(localValue2, localValue3)) {
         byte localValue4 = -1;
         BufferBuilder localValue5 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
         localValue5.vertex(localValue0, localValue1, 0.0F).texture(0.0F, 1.0F).color(localValue4);
         localValue5.vertex(localValue0, localValue1 + localValue3, 0.0F).texture(0.0F, 0.0F).color(localValue4);
         localValue5.vertex(localValue0 + localValue2, localValue1 + localValue3, 0.0F).texture(1.0F, 0.0F).color(localValue4);
         localValue5.vertex(localValue0 + localValue2, localValue1, 0.0F).texture(1.0F, 1.0F).color(localValue4);
         BufferRenderer.drawWithGlobalProgram(localValue5.end());
      }
   }

   public static void internalMethod09058() {
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
   }

   public static void internalMethod01924(MatrixStack localValue0, float localValue1, float localValue2, float localValue3, float localValue4, float localValue5, float localValue6, ColorRGBA localValue7) {
      if (!(localValue3 <= 0.0F) && !(localValue4 <= 0.0F) && !internalMethod03563(localValue7)) {
         UiBatchRenderer.internalMethod02576();
         localValue0.push();
         Matrix4f localValue8 = localValue0.peek().getPositionMatrix();
         float localValue9 = 0.5F;
         internalField1671.internalMethod01220();
         internalField1671.internalMethod05981("Size").set(localValue3, localValue3);
         internalField1671.internalMethod05981("Thickness").set(localValue4);
         internalField1671.internalMethod05981("StartAngle").set(localValue5);
         internalField1671.internalMethod05981("EndAngle").set(localValue6);
         internalField1671.internalMethod05981("Smoothness").set(localValue9);
         internalMethod09058();
         float localValue10 = localValue4 + localValue9 * 2.0F;
         float localValue11 = localValue1 - localValue10 / 2.0F;
         float localValue12 = localValue2 - localValue10 / 2.0F;
         float localValue13 = localValue3 + localValue10;
         float localValue14 = localValue3 + localValue10;
         BufferBuilder localValue15 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
         localValue15.vertex(localValue8, localValue11, localValue12, 0.0F).color(localValue7.getRGB());
         localValue15.vertex(localValue8, localValue11, localValue12 + localValue14, 0.0F).color(localValue7.getRGB());
         localValue15.vertex(localValue8, localValue11 + localValue13, localValue12 + localValue14, 0.0F).color(localValue7.getRGB());
         localValue15.vertex(localValue8, localValue11 + localValue13, localValue12, 0.0F).color(localValue7.getRGB());
         BufferRenderer.drawWithGlobalProgram(localValue15.end());
         internalMethod09648();
         localValue0.pop();
      }
   }

   public static void internalMethod02355(MatrixStack localValue0, float localValue1, float localValue2, float localValue3, float localValue4, float localValue5, float localValue6, ColorRGBA localValue7, boolean localValue8) {
      if (localValue8) {
         float localValue9 = localValue3 * 2.0F;
         float localValue10 = (float)Math.toRadians(localValue5 - 90.0F);
         float localValue11 = (float)Math.toRadians(localValue6 - 90.0F);
         internalMethod01924(localValue0, localValue1 - localValue3, localValue2 - localValue3, localValue9, localValue4, localValue10, localValue11, localValue7);
      } else {
         float localValue12 = (float)Math.toRadians(localValue5 - 90.0F);
         float localValue13 = (float)Math.toRadians(localValue6 - 90.0F);
         internalMethod01924(localValue0, localValue1, localValue2, localValue3 * 2.0F, localValue4, localValue12, localValue13, localValue7);
      }
   }

   public static void internalMethod02451(MatrixStack localValue0, float localValue1, float localValue2, float localValue3, float localValue4, float localValue5, ColorRGBA localValue6) {
      float localValue7 = 0.0F;
      float localValue8 = localValue5 * 360.0F;
      internalMethod02355(localValue0, localValue1, localValue2, localValue3, localValue4, localValue7, localValue8, localValue6, true);
   }

   public static void internalMethod05386(
      MatrixStack localValue0,
      float localValue1,
      float localValue2,
      float localValue3,
      float localValue4,
      float localValue5,
      float localValue6,
      float localValue7,
      float localValue8,
      float localValue9,
      float localValue10,
      float localValue11,
      float localValue12,
      float localValue13,
      float localValue14,
      ColorRGBA localValue15,
      ColorRGBA localValue16,
      float localValue17
   ) {
      if (!internalMethod05199(localValue3, localValue4)) {
         UiBatchRenderer.internalMethod02576();
         localValue0.push();
         Matrix4f localValue18 = localValue0.peek().getPositionMatrix();
         internalField1670.internalMethod01220();
         internalField1670.internalMethod05981("Size").set(localValue3, localValue4);
         internalField1670.internalMethod05981("RectCenter").set(localValue5, localValue6);
         internalField1670.internalMethod05981("RectHalf").set(localValue7, localValue8);
         internalField1670.internalMethod05981("RectRadius").set(localValue9, localValue9, localValue9, localValue9);
         internalField1670.internalMethod05981("CircleCenter").set(localValue10, localValue11);
         internalField1670.internalMethod05981("CircleRadius").set(localValue12);
         internalField1670.internalMethod05981("Smooth").set(localValue13);
         internalField1670.internalMethod05981("Outline").set(localValue14);
         internalField1670.internalMethod05981("FillColor")
            .set(localValue15.getRed() / 255.0F, localValue15.getGreen() / 255.0F, localValue15.getBlue() / 255.0F, localValue15.getAlpha() / 255.0F);
         internalField1670.internalMethod05981("OutlineColor")
            .set(localValue16.getRed() / 255.0F, localValue16.getGreen() / 255.0F, localValue16.getBlue() / 255.0F, localValue16.getAlpha() / 255.0F);
         internalField1670.internalMethod05981("GlobalAlpha").set(localValue17);
         internalMethod09058();
         byte localValue19 = -1;
         BufferBuilder localValue20 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
         localValue20.vertex(localValue18, localValue1, localValue2, 0.0F).color(localValue19);
         localValue20.vertex(localValue18, localValue1, localValue2 + localValue4, 0.0F).color(localValue19);
         localValue20.vertex(localValue18, localValue1 + localValue3, localValue2 + localValue4, 0.0F).color(localValue19);
         localValue20.vertex(localValue18, localValue1 + localValue3, localValue2, 0.0F).color(localValue19);
         BufferRenderer.drawWithGlobalProgram(localValue20.end());
         internalMethod09648();
         localValue0.pop();
      }
   }

   public static void internalMethod09648() {
      RenderSystem.disableBlend();
   }

   @Generated
   private RenderPipeline() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   @Generated
   public static ShaderProgramBase internalMethod05294() {
      return internalField0994;
   }

   static final class InternalType0372 {
      private final float internalField0205;
      private final float internalField0206;
      private final float internalField1048;
      private final float internalField1047;

      InternalType0372(float localValue1, float localValue2, float localValue3, float localValue4) {
         this.internalField0205 = localValue1;
         this.internalField0206 = localValue2;
         this.internalField1048 = localValue3;
         this.internalField1047 = localValue4;
      }

      @Override
      public final String toString() {
         return "InternalType0372[u1=" + this.internalField0205 + ", v1=" + this.internalField0206 + ", uSize=" + this.internalField1048 + ", vSize=" + this.internalField1047 + "]";
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
         RenderPipeline.InternalType0372 other = (RenderPipeline.InternalType0372) localValue1;
         return java.util.Objects.equals(this.internalField0205, other.internalField0205)
            && java.util.Objects.equals(this.internalField0206, other.internalField0206)
            && java.util.Objects.equals(this.internalField1048, other.internalField1048)
            && java.util.Objects.equals(this.internalField1047, other.internalField1047);
      }

      public float internalMethod04657() {
         return this.internalField0205;
      }

      public float internalMethod04660() {
         return this.internalField0206;
      }

      public float internalMethod08152() {
         return this.internalField1048;
      }

      public float internalMethod08153() {
         return this.internalField1047;
      }
   }
   public static void internalMethod03082(org.joml.Matrix3x2fStack m, float a, float b, float c, float d, float e, float f, float g, int h, Runnable i) {
      internalMethod03082(GuiMatrixCompat.toLegacyStack(m), a, b, c, d, e, f, g, h, i);
   }
   public static void internalMethod01986(org.joml.Matrix3x2fStack m, Vec2f a, Vec2f b, ColorRGBA c) {
      internalMethod01986(GuiMatrixCompat.toLegacyStack(m), a, b, c);
   }
   public static void internalMethod05338(org.joml.Matrix3x2fStack m, Vec2f a, Vec2f b, Vec2f c, Vec2f d, ColorRGBA e, int f) {
      internalMethod05338(GuiMatrixCompat.toLegacyStack(m), a, b, c, d, e, f);
   }
   public static void internalMethod03090(org.joml.Matrix3x2fStack m, float a, float b, float c, float d, Vec2f e, Vec2f f, Vec2f g, Vec2f h, float i, ColorRGBA j) {
      internalMethod03090(GuiMatrixCompat.toLegacyStack(m), a, b, c, d, e, f, g, h, i, j);
   }
   public static void internalMethod07554(org.joml.Matrix3x2fStack m, float[] a, float[] b, float c, ColorRGBA d, ColorRGBA e) {
      internalMethod07554(GuiMatrixCompat.toLegacyStack(m), a, b, c, d, e);
   }
   public static void internalMethod02688(org.joml.Matrix3x2fStack m, float a, float b, float c, float d, ColorRGBA e) {
      internalMethod02688(GuiMatrixCompat.toLegacyStack(m), a, b, c, d, e);
   }
   public static void internalMethod04182(org.joml.Matrix3x2fStack m, float a, float b, float c, float d, float e, CornerRadii f, ColorRGBA g) {
      internalMethod04182(GuiMatrixCompat.toLegacyStack(m), a, b, c, d, e, f, g);
   }
   public static void internalMethod01759(org.joml.Matrix3x2fStack m, float a, float b, float c, float d, float e, CornerRadii f, ColorRGBA g, ColorRGBA h, ColorRGBA i, ColorRGBA j) {
      internalMethod01759(GuiMatrixCompat.toLegacyStack(m), a, b, c, d, e, f, g, h, i, j);
   }
   public static void internalMethod04108(org.joml.Matrix3x2fStack m, float a, float b, float c, float d, float e, CornerRadii f, QuadColorGradient g) {
      internalMethod04108(GuiMatrixCompat.toLegacyStack(m), a, b, c, d, e, f, g);
   }
   public static void internalMethod05317(org.joml.Matrix3x2fStack m, float a, float b, float c, float d, float e, CornerRadii f, ColorRGBA g) {
      internalMethod05317(GuiMatrixCompat.toLegacyStack(m), a, b, c, d, e, f, g);
   }
   public static void internalMethod03329(org.joml.Matrix3x2fStack m, float a, float b, float c, float d, CornerRadii e, ColorRGBA f, float g, float h, ColorRGBA i, float j, boolean k, float l, float n, float o, boolean p) {
      internalMethod03329(GuiMatrixCompat.toLegacyStack(m), a, b, c, d, e, f, g, h, i, j, k, l, n, o, p);
   }
   public static void internalMethod01221(Rect r, org.joml.Matrix3x2fStack m, float a, float b, float c, float d, CornerRadii e, ColorRGBA f, float g, float h, ColorRGBA i, float j, boolean k, float l, float n, float o, int p) {
      internalMethod01221(r, GuiMatrixCompat.toLegacyStack(m), a, b, c, d, e, f, g, h, i, j, k, l, n, o, p);
   }
   public static void internalMethod02035(org.joml.Matrix3x2fStack m, float a, float b, float c, float d, CornerRadii e, ColorRGBA f) {
      internalMethod02035(GuiMatrixCompat.toLegacyStack(m), a, b, c, d, e, f);
   }
   public static void internalMethod04255(org.joml.Matrix3x2fStack m, float a, float b, float c, float d, CornerRadii e, ColorRGBA f, ColorRGBA g, ColorRGBA h, ColorRGBA i) {
      internalMethod04255(GuiMatrixCompat.toLegacyStack(m), a, b, c, d, e, f, g, h, i);
   }
   public static void internalMethod03784(org.joml.Matrix3x2fStack m, float a, float b, float c, float d, CornerRadii e, QuadColorGradient f) {
      internalMethod03784(GuiMatrixCompat.toLegacyStack(m), a, b, c, d, e, f);
   }
   public static void internalMethod08588(org.joml.Matrix3x2fStack m, float a, float b, float c, float d, float e, CornerRadii f, ColorRGBA g) {
      internalMethod08588(GuiMatrixCompat.toLegacyStack(m), a, b, c, d, e, f, g);
   }
   public static void internalMethod01338(org.joml.Matrix3x2fStack m, float a, float b, float c, float d, float e, CornerRadii f, float g, float h, ColorRGBA i, float j, float k, float l, float n) {
      internalMethod01338(GuiMatrixCompat.toLegacyStack(m), a, b, c, d, e, f, g, h, i, j, k, l, n);
   }
   public static void internalMethod06077(org.joml.Matrix3x2fStack m, float a, float b, float c, float d, float e, float f, CornerRadii g, ColorRGBA h) {
      internalMethod06077(GuiMatrixCompat.toLegacyStack(m), a, b, c, d, e, f, g, h);
   }
   public static void internalMethod00463(org.joml.Matrix3x2fStack m, Identifier a, float b, float c, float d, float e, ColorRGBA f) {
      internalMethod00463(GuiMatrixCompat.toLegacyStack(m), a, b, c, d, e, f);
   }
   public static void internalMethod01117(org.joml.Matrix3x2fStack m, Identifier a, float b, float c, float d, float e, float f, float g, float h, float i, ColorRGBA j) {
      internalMethod01117(GuiMatrixCompat.toLegacyStack(m), a, b, c, d, e, f, g, h, i, j);
   }
   public static void internalMethod06903(org.joml.Matrix3x2fStack m, TextureRegion a, float b, float c, float d, float e, ColorRGBA f) {
      internalMethod06903(GuiMatrixCompat.toLegacyStack(m), a, b, c, d, e, f);
   }
   public static void internalMethod02217(org.joml.Matrix3x2fStack m, IconSprite a, float b, float c, float d, float e, ColorRGBA f) {
      internalMethod02217(GuiMatrixCompat.toLegacyStack(m), a, b, c, d, e, f);
   }
   public static void internalMethod06642(org.joml.Matrix3x2fStack m, Identifier a, float b, float c, float d, float e, CornerRadii f) {
      internalMethod06642(GuiMatrixCompat.toLegacyStack(m), a, b, c, d, e, f);
   }
   public static void internalMethod06728(org.joml.Matrix3x2fStack m, Identifier a, float b, float c, float d, float e, CornerRadii f, ColorRGBA g) {
      internalMethod06728(GuiMatrixCompat.toLegacyStack(m), a, b, c, d, e, f, g);
   }
   public static void internalMethod08818(org.joml.Matrix3x2fStack m, float a, float b, float c, float d, float e, CornerRadii f, ColorRGBA g) {
      internalMethod08818(GuiMatrixCompat.toLegacyStack(m), a, b, c, d, e, f, g);
   }
   public static void internalMethod02643(org.joml.Matrix3x2fStack m, float a, float b, float c, float d, ColorRGBA e) {
      internalMethod02643(GuiMatrixCompat.toLegacyStack(m), a, b, c, d, e);
   }
   public static void internalMethod01599(org.joml.Matrix3x2fStack m, float a, float b, float c, float d, float e, float f, float g, float h, float i, float j, float k, CornerRadii l, ColorRGBA n) {
      internalMethod01599(GuiMatrixCompat.toLegacyStack(m), a, b, c, d, e, f, g, h, i, j, k, l, n);
   }
   public static void internalMethod02681(org.joml.Matrix3x2fStack m, int a, float b, float c, float d, float e, float f, float g, float h, float i, float j, float k, float l, CornerRadii n, ColorRGBA o) {
      internalMethod02681(GuiMatrixCompat.toLegacyStack(m), a, b, c, d, e, f, g, h, i, j, k, l, n, o);
   }
   public static void internalMethod06570(org.joml.Matrix3x2fStack m, float a, float b, float c, float d, float e, float f, CornerRadii g, ColorRGBA h) {
      internalMethod06570(GuiMatrixCompat.toLegacyStack(m), a, b, c, d, e, f, g, h);
   }
   public static void internalMethod08703(org.joml.Matrix3x2fStack m, float a, float b, float c, float d, float e, CornerRadii f, ColorRGBA g) {
      internalMethod08703(GuiMatrixCompat.toLegacyStack(m), a, b, c, d, e, f, g);
   }
   public static void internalMethod05578(org.joml.Matrix3x2fStack m, Rect a, float b, float c, float d, float e, CornerRadii f, float g) {
      internalMethod05578(GuiMatrixCompat.toLegacyStack(m), a, b, c, d, e, f, g);
   }
   public static void internalMethod05007(org.joml.Matrix3x2fStack m, BufferBuilder a, double b, double c, double d, double e, double f, ColorRGBA g) {
      internalMethod05007(GuiMatrixCompat.toLegacyStack(m), a, b, c, d, e, f, g);
   }
   public static void internalMethod04257(org.joml.Matrix3x2fStack m, BufferBuilder a, double b, double c, double d, double e, double f, ColorRGBA g, int h) {
      internalMethod04257(GuiMatrixCompat.toLegacyStack(m), a, b, c, d, e, f, g, h);
   }
   public static void internalMethod04502(org.joml.Matrix3x2fStack m, Identifier a, double b, double c, double d, double e, double f, ColorRGBA g) {
      internalMethod04502(GuiMatrixCompat.toLegacyStack(m), a, b, c, d, e, f, g);
   }
   public static void internalMethod01072(org.joml.Matrix3x2fStack m, AbstractClientPlayerEntity a, float b, float c, float d, CornerRadii e, ColorRGBA f) {
      internalMethod01072(GuiMatrixCompat.toLegacyStack(m), a, b, c, d, e, f);
   }
   public static <T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> void internalMethod03763(org.joml.Matrix3x2fStack m, T a, float b, float c, float d, CornerRadii e, ColorRGBA f) {
      internalMethod03763(GuiMatrixCompat.toLegacyStack(m), a, b, c, d, e, f);
   }
   public static void internalMethod00808(org.joml.Matrix3x2fStack m, Identifier a, float b, float c, float d, CornerRadii e, ColorRGBA f) {
      internalMethod00808(GuiMatrixCompat.toLegacyStack(m), a, b, c, d, e, f);
   }
   public static void internalMethod02676(org.joml.Matrix3x2fStack m, Identifier a, float b, float c, float d, float e, CornerRadii f, ColorRGBA g, float h, float i, float j, float k) {
      internalMethod02676(GuiMatrixCompat.toLegacyStack(m), a, b, c, d, e, f, g, h, i, j, k);
   }
   public static void internalMethod04875(org.joml.Matrix3x2fStack m, float a, float b, float c, float d) {
      internalMethod04875(GuiMatrixCompat.toLegacyStack(m), a, b, c, d);
   }
   public static void internalMethod01924(org.joml.Matrix3x2fStack m, float a, float b, float c, float d, float e, float f, ColorRGBA g) {
      internalMethod01924(GuiMatrixCompat.toLegacyStack(m), a, b, c, d, e, f, g);
   }
   public static void internalMethod02355(org.joml.Matrix3x2fStack m, float a, float b, float c, float d, float e, float f, ColorRGBA g, boolean h) {
      internalMethod02355(GuiMatrixCompat.toLegacyStack(m), a, b, c, d, e, f, g, h);
   }
   public static void internalMethod02451(org.joml.Matrix3x2fStack m, float a, float b, float c, float d, float e, ColorRGBA f) {
      internalMethod02451(GuiMatrixCompat.toLegacyStack(m), a, b, c, d, e, f);
   }
   public static void internalMethod05386(org.joml.Matrix3x2fStack m, float a, float b, float c, float d, float e, float f, float g, float h, float i, float j, float k, float l, float n, float o, ColorRGBA p, ColorRGBA q, float r) {
      internalMethod05386(GuiMatrixCompat.toLegacyStack(m), a, b, c, d, e, f, g, h, i, j, k, l, n, o, p, q, r);
   }
}
