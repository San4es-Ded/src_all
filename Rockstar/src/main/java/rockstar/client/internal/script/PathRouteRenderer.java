package rockstar.client.internal.script;








import rockstar.client.ui.*;
import rockstar.client.rotation.*;
import rockstar.client.event.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.internal.game.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;
import rockstar.client.compat.RenderSystem;
import java.util.List;
import rockstar.client.compat.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import rockstar.client.compat.BufferRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexFormats;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import pyrock.events.render.Render3DEvent;
import pyrock.utility.render.ColorRGBA;

public final class PathRouteRenderer {
   private static final Identifier internalField0354 = Identifier.of("rockstar", "textures/pathfinder/bloom.png");
   private final EventListener<Render3DEvent> internalField0157 = localValue1 -> this.internalMethod00872(localValue1.getMatrices(), localValue1.getCamera());

   public static PathRouteRenderer internalMethod01422(NewtonCoreManager localValue0) {
      PathRouteRenderer localValue1 = new PathRouteRenderer();
      localValue0.internalMethod05035().internalMethod00647(localValue1);
      return localValue1;
   }

   private void internalMethod00872(MatrixStack localValue1, Camera localValue2) {
      PathExecutor localValue3 = NewtonCoreManager.internalMethod00114().internalMethod01484();
      ElytraFlightTask localValue4 = NewtonCoreManager.internalMethod00114()
         .internalMethod06401()
         .internalMethod03684()
         .filter(ElytraFlightTask.class::isInstance)
         .map(ElytraFlightTask.class::cast)
         .orElse(null);
      ComputedPath localValue5 = localValue3 != null ? localValue3.internalMethod00712() : null;
      PathGoal localValue6 = localValue3 != null ? localValue3.internalMethod00771() : null;
      boolean localValue7 = localValue5 != null && localValue5.internalMethod05363().size() > 1;
      Vec3d localValue8 = localValue6 != null ? localValue6.internalMethod07298() : null;
      List localValue9 = localValue4 != null ? localValue4.internalMethod02052() : List.of();
      Vec3d localValue10 = localValue4 != null ? localValue4.internalMethod01030() : null;
      if (localValue7 || localValue8 != null || localValue9.size() >= 2 || localValue10 != null) {
         Vec3d localValue11 = localValue2.getCameraPos();
         RenderSystem.enableBlend();
         RenderSystem.disableDepthTest();
         RenderSystem.disableCull();
         RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
         RenderSystem.setShaderTexture(0, internalField0354);
         RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
         BufferBuilder localValue12 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
         localValue1.push();
         localValue1.translate(-localValue11.x, -localValue11.y, -localValue11.z);
         if (localValue9.size() >= 2) {
            ColorRGBA localValue13 = ThemeColors.internalMethod02531();

            for (int localValue14 = 0; localValue14 < localValue9.size() - 1; localValue14++) {
               Vec3d localValue15 = (Vec3d)localValue9.get(localValue14);
               Vec3d localValue16 = (Vec3d)localValue9.get(localValue14 + 1);

               for (int localValue17 = 0; localValue17 <= 10; localValue17++) {
                  float localValue18 = localValue17 / 10.0F;
                  Vec3d localValue19 = localValue15.add(localValue16.subtract(localValue15).multiply(localValue18));
                  this.internalMethod01135(localValue1, localValue2, localValue12, localValue19, 0.35F, 1.0F, localValue13);
                  this.internalMethod01135(localValue1, localValue2, localValue12, localValue19, 1.6F, 0.06F, localValue13);
               }
            }
         }

         if (localValue10 != null) {
            this.internalMethod01135(localValue1, localValue2, localValue12, localValue10, 0.8F, 1.0F, ThemeColors.internalMethod02531());
            this.internalMethod01135(localValue1, localValue2, localValue12, localValue10, 3.2F, 0.12F, ThemeColors.internalMethod02531());
         }

         if (localValue7) {
            int localValue24 = localValue3.internalMethod03485();

            for (int localValue26 = 0; localValue26 < localValue5.internalMethod05363().size() - 1; localValue26++) {
               PathNode localValue27 = localValue5.internalMethod05363().get(localValue26);
               PathNode localValue28 = localValue5.internalMethod05363().get(localValue26 + 1);
               Vec3d localValue29 = new Vec3d(localValue27.internalMethod02945() + 0.5, localValue27.internalMethod02949() + 0.5, localValue27.internalMethod07945() + 0.5);
               Vec3d localValue30 = new Vec3d(localValue28.internalMethod02945() + 0.5, localValue28.internalMethod02949() + 0.5, localValue28.internalMethod07945() + 0.5);
               float localValue31 = (float)localValue29.distanceTo(localValue30);
               ColorRGBA localValue20 = ThemeColors.internalMethod02531();

               for (int localValue21 = 0; localValue21 <= 10; localValue21++) {
                  float localValue22 = localValue21 / 10.0F;
                  Vec3d localValue23 = localValue29.add(localValue30.subtract(localValue29).multiply(localValue22));
                  this.internalMethod01135(localValue1, localValue2, localValue12, localValue23, localValue31 / 3.0F, 1.0F, localValue20);
                  this.internalMethod01135(localValue1, localValue2, localValue12, localValue23, localValue31 * 2.0F, 0.05F, localValue20);
               }
            }
         }

         if (localValue8 != null) {
            Vec3d localValue25 = new Vec3d(localValue8.x, localValue8.y + 0.5, localValue8.z);
            this.internalMethod01135(localValue1, localValue2, localValue12, localValue25, 0.7F, 1.0F, ThemeColors.internalMethod02531());
            this.internalMethod01135(localValue1, localValue2, localValue12, localValue25, 3.0F, 0.12F, ThemeColors.internalMethod02531());
         }

         localValue1.pop();
         BufferRenderer.drawWithGlobalProgram(localValue12.end());
         RenderSystem.enableCull();
         RenderSystem.enableDepthTest();
         RenderSystem.disableBlend();
      }
   }

   private void internalMethod01135(MatrixStack localValue1, Camera localValue2, BufferBuilder localValue3, Vec3d localValue4, float localValue5, float localValue6, ColorRGBA localValue7) {
      localValue1.push();
      localValue1.translate(localValue4.x, localValue4.y, localValue4.z);
      localValue1.multiply(localValue2.getRotation());
      int localValue8 = localValue7.withAlpha(255.0F * localValue6).getRGB();
      float localValue9 = localValue5 / 2.0F;
      Matrix4f localValue10 = localValue1.peek().getPositionMatrix();
      localValue3.vertex(localValue10, -localValue9, localValue9, 0.0F).texture(0.0F, 1.0F).color(localValue8);
      localValue3.vertex(localValue10, localValue9, localValue9, 0.0F).texture(1.0F, 1.0F).color(localValue8);
      localValue3.vertex(localValue10, localValue9, -localValue9, 0.0F).texture(1.0F, 0.0F).color(localValue8);
      localValue3.vertex(localValue10, -localValue9, -localValue9, 0.0F).texture(0.0F, 0.0F).color(localValue8);
      localValue1.pop();
   }
}
