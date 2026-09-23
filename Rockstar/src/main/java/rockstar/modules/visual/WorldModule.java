package rockstar.modules.visual;











import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.rotation.*;
import rockstar.client.render.*;
import rockstar.client.module.*;
import rockstar.client.event.*;
import rockstar.client.animation.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.internal.render.*;
import rockstar.client.*;
import rockstar.client.module.Module;

import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;
import rockstar.client.compat.RenderSystem;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.block.BlockState;
import rockstar.client.compat.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import rockstar.client.compat.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexFormats;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import org.joml.FrustumIntersection;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import pyrock.events.render.Render3DEvent;
import pyrock.utility.render.ColorRGBA;

@ModuleInfo(
   name = "World",
   category = ModuleCategory.VISUALS,
   internalMethod09633 = "modules.descriptions.world"
)
public class WorldModule extends Module {
   private final List<WorldModule.InternalType0112> internalField0416 = new ArrayList<>();
   private BooleanSetting internalField0650;
   private ColorSetting internalField0665;
   private BooleanSetting internalField0651;
   private SliderSetting internalField0383;
   private SliderSetting internalField0382;
   private static final float internalField0205 = 20.0F;
   private static final float internalField0206 = 1.6F;
   private static final float internalField1048 = 6.0F;
   private static final double internalField0194 = 1.2;
   private static final float internalField1047 = 1.0F;
   private int internalField1053;
   private final EventListener<Render3DEvent> internalField0157 = localValue1 -> {
      if (!this.internalField0416.isEmpty()) {
         ColorRGBA localValue2 = this.internalField0650.internalMethod04496() ? ThemeColors.internalMethod02531() : this.internalField0665.internalMethod05620();
         MatrixStack localValue3 = localValue1.getMatrices();
         Camera localValue4 = internalField0149.gameRenderer.getCamera();
         Vec3d localValue5 = localValue4.getCameraPos();

         for (WorldModule.InternalType0112 localValue7 : this.internalField0416) {
            localValue7.internalField0808.internalMethod07062(!localValue7.internalMethod06725());
            localValue7.internalField0809.internalMethod07062(!localValue7.internalField0276);
         }

         this.internalMethod01500(localValue1, localValue2, localValue5);
         localValue3.push();
         RenderSystem.enableBlend();
         RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
         RenderSystem.enableDepthTest();
         RenderSystem.disableCull();
         RenderSystem.depthMask(false);
         Identifier localValue14 = RockstarClient.id("textures/bloom.png");
         RenderSystem.setShaderTexture(0, localValue14);
         RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
         BufferBuilder localValue15 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

         for (WorldModule.InternalType0112 localValue9 : this.internalField0416) {
            Vec3d localValue10 = ScreenProjectionUtils.internalMethod00202(localValue9.internalField0283, localValue9.internalField1104, localValue1.getTickDelta());
            float localValue11 = 4.0F * localValue9.internalField0205;
            localValue3.push();
            HudRenderUtils.internalMethod03474(localValue3, localValue10);
            localValue3.multiply(localValue4.getRotation());
            RenderPipeline.internalMethod05007(
               localValue3, localValue15, -localValue11 / 2.0F, -localValue11 / 2.0F, 0.0, localValue11, localValue11, localValue2.withAlpha(255.0F * localValue9.internalField0808.internalMethod02881() * 0.4F)
            );
            localValue3.pop();
         }

         BuiltBuffer localValue16 = localValue15.endNullable();
         if (localValue16 != null) {
            BufferRenderer.drawWithGlobalProgram(localValue16);
         }

         RenderSystem.depthMask(true);
         RenderSystem.setShaderTexture(0, 0);
         RenderSystem.disableBlend();
         RenderSystem.enableCull();
         RenderSystem.disableDepthTest();
         localValue3.pop();
         RenderSystem.enableBlend();
         RenderSystem.disableDepthTest();
         RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
         RenderSystem.enableDepthTest();
         RenderSystem.disableCull();
         RenderSystem.depthMask(false);
         RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
         BufferBuilder localValue17 = RenderSystem.renderThreadTesselator().begin(DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);

         for (WorldModule.InternalType0112 localValue20 : this.internalField0416) {
            Vec3d localValue12 = ScreenProjectionUtils.internalMethod00202(localValue20.internalField0283, localValue20.internalField1104, localValue1.getTickDelta());
            Vec3d localValue13 = ScreenProjectionUtils.internalMethod00202(localValue20.internalField0282, localValue20.internalField1106, localValue1.getTickDelta());
            localValue3.push();
            localValue3.translate(localValue12.add(-localValue5.getX(), -localValue5.getY(), -localValue5.getZ()));
            localValue3.multiply(new Quaternionf().rotationXYZ((float)localValue13.x, (float)localValue13.y, (float)localValue13.z));
            localValue3.scale(localValue20.internalField0205, localValue20.internalField0205, localValue20.internalField0205);
            Render3DUtils.internalMethod09146(
               localValue3, localValue17, new Box(-0.5, -0.5, -0.5, 0.5, 0.5, 0.5), localValue2.withAlpha(255.0F * localValue20.internalField0808.internalMethod02881() * 0.4F)
            );
            Render3DUtils.internalMethod08795(
               localValue3, localValue17, new Box(-0.5, -0.5, -0.5, 0.5, 0.5, 0.5), localValue2.withAlpha(205.0F * localValue20.internalField0808.internalMethod02881())
            );
            localValue3.pop();
         }

         BuiltBuffer localValue19 = localValue17.endNullable();
         if (localValue19 != null) {
            BufferRenderer.drawWithGlobalProgram(localValue19);
         }

         RenderSystem.depthMask(true);
         RenderSystem.defaultBlendFunc();
         RenderSystem.enableCull();
         RenderSystem.enableDepthTest();
         RenderSystem.disableBlend();
      }
   };
   public WorldModule() {
      this.internalMethod09703();
   }

   private void internalMethod09703() {
      this.internalField0650 = new BooleanSetting(this, "theme.sync").internalMethod06630();
      this.internalField0665 = new ColorSetting(this, "modules.settings.world.color", this.internalField0650::internalMethod04496)
         .internalMethod04886(ThemeColors.internalMethod02531());
      this.internalField0651 = new BooleanSetting(this, "modules.settings.world.lighting");
      this.internalField0383 = new SliderSetting(this, "modules.settings.world.lighting.radius", () -> !this.internalField0651.internalMethod04496())
         .internalMethod05900(2.0F)
         .internalMethod02732(7.0F)
         .internalMethod08673(0.5F)
         .internalMethod08074(4.0F)
         .internalMethod06240(" blocks");
      this.internalField0382 = new SliderSetting(this, "modules.settings.world.lighting.strength", () -> !this.internalField0651.internalMethod04496())
         .internalMethod05900(0.0F)
         .internalMethod02732(150.0F)
         .internalMethod08673(10.0F)
         .internalMethod08074(100.0F)
         .internalMethod06240("%");
   }

   @Override
   public void onDisable() {
      this.internalField0416.clear();
   }

   @Override
   public void internalMethod08229() {
      if (internalField0149.player != null && internalField0149.world != null) {
         this.internalField0416.removeIf(localValue0 -> localValue0.internalField0808.internalMethod02881() == 0.0F && localValue0.internalMethod06725());

         for (WorldModule.InternalType0112 localValue2 : this.internalField0416) {
            localValue2.internalMethod06724();
         }

         int localValue5 = 0;

         for (WorldModule.InternalType0112 localValue3 : this.internalField0416) {
            if (!localValue3.internalField0277) {
               localValue5++;
            }
         }

         for (int localValue7 = this.internalField0416.size() - 1; localValue7 >= 0 && localValue5 > 100; localValue7--) {
            WorldModule.InternalType0112 localValue9 = this.internalField0416.get(localValue7);
            if (!localValue9.internalField0277) {
               localValue9.internalField0277 = true;
               localValue5--;
            }
         }

         for (int localValue8 = 0; localValue8 < 4 && localValue5 < 100; localValue8++) {
            Vec3d localValue10 = this.internalMethod01922();
            if (localValue10 == null) {
               break;
            }

            WorldModule.InternalType0112 localValue4 = new WorldModule.InternalType0112(
               localValue10,
               Vec3d.ZERO,
               new Vec3d(
                  MathUtils.internalMethod05368(-1.0, 1.0), MathUtils.internalMethod05368(0.0, 2.0), MathUtils.internalMethod05368(-1.0, 1.0)
               ),
               new Vec3d(
                  MathUtils.internalMethod05368(-1.0, 1.0),
                  MathUtils.internalMethod05368(-1.0, 1.0),
                  MathUtils.internalMethod05368(-1.0, 1.0)
               ),
               (long)MathUtils.internalMethod05368(1500.0, 4500.0),
               MathUtils.internalMethod05368(0.1F, 0.3F)
            );
            this.internalMethod02043(localValue4);
            this.internalField0416.add(localValue4);
            localValue5++;
         }

         this.internalMethod09704();
      } else {
         this.internalField0416.clear();
      }
   }

   private Vec3d internalMethod01922() {
      Vec3d localValue1 = null;

      for (int localValue2 = 0; localValue2 < 8; localValue2++) {
         Vec3d localValue3 = internalField0149.player
            .getEntityPos()
            .add(
               MathUtils.internalMethod05368(-20.0, 20.0),
               MathUtils.internalMethod05368(1.6F, 6.0),
               MathUtils.internalMethod05368(-20.0, 20.0)
            );
         if (!this.internalMethod00437(localValue3)) {
            if (localValue1 == null) {
               localValue1 = localValue3;
            }

            if (!this.internalMethod00437(localValue3.subtract(0.0, 1.2, 0.0))) {
               return localValue3;
            }
         }
      }

      return localValue1;
   }

   private boolean internalMethod00437(Vec3d localValue1) {
      BlockPos localValue2 = BlockPos.ofFloored(localValue1);
      if (internalField0149.world.isOutOfHeightLimit(localValue2.getY())) {
         return true;
      } else {
         BlockState localValue3 = internalField0149.world.getBlockState(localValue2);
         if (localValue3.isAir()) {
            return false;
         } else {
            VoxelShape localValue4 = localValue3.getCollisionShape(internalField0149.world, localValue2);
            if (localValue4.isEmpty()) {
               return false;
            } else {
               Vec3d localValue5 = localValue1.subtract(localValue2.getX(), localValue2.getY(), localValue2.getZ());

               for (Box localValue7 : localValue4.getBoundingBoxes()) {
                  if (localValue7.expand(0.01).contains(localValue5)) {
                     return true;
                  }
               }

               return false;
            }
         }
      }
   }

   private void internalMethod09704() {
      if (!this.internalField0651.internalMethod04496()) {
         for (WorldModule.InternalType0112 localValue9 : this.internalField0416) {
            localValue9.internalField0276 = false;
         }
      } else {
         Camera localValue1 = internalField0149.gameRenderer.getCamera();
         if (localValue1 != null) {
            Vec3d localValue2 = localValue1.getCameraPos();
            int localValue3 = this.internalField0416.size();
            if (localValue3 != 0) {
               int localValue4 = Math.min(localValue3, 40);

               for (WorldModule.InternalType0112 localValue6 : this.internalField0416) {
                  if (localValue6.internalField1099 && localValue4 > 0) {
                     localValue4--;
                     localValue6.internalField0276 = this.internalMethod06066(localValue2, localValue6.internalField1104);
                  }
               }

               int localValue10 = 0;

               for (int localValue11 = 0; localValue11 < localValue3 && localValue4 > 0; localValue11++) {
                  WorldModule.InternalType0112 localValue7 = this.internalField0416.get((this.internalField1053 + localValue11) % localValue3);
                  localValue10++;
                  if (!localValue7.internalField1099) {
                     localValue4--;
                     localValue7.internalField0276 = this.internalMethod06066(localValue2, localValue7.internalField1104);
                  }
               }

               this.internalField1053 = (this.internalField1053 + localValue10) % localValue3;
            }
         }
      }
   }

   private void internalMethod02043(WorldModule.InternalType0112 localValue1) {
      if (this.internalField0651.internalMethod04496()) {
         Camera localValue2 = internalField0149.gameRenderer.getCamera();
         if (localValue2 != null) {
            localValue1.internalField0276 = this.internalMethod06066(localValue2.getCameraPos(), localValue1.internalField1104);
            localValue1.internalField0809.internalMethod07060(localValue1.internalField0276 ? 0.0F : 1.0F);
         }
      }
   }

   private boolean internalMethod06066(Vec3d localValue1, Vec3d localValue2) {
      return localValue2.squaredDistanceTo(localValue1) > 2304.0
         ? true
         : internalField0149.world.raycast(new RaycastContext(localValue1, localValue2, ShapeType.COLLIDER, FluidHandling.NONE, internalField0149.player)).getType()
            != Type.MISS;
   }

   private void internalMethod01500(Render3DEvent localValue1, ColorRGBA localValue2, Vec3d localValue3) {
      if (this.internalField0651.internalMethod04496()) {
         float localValue4 = this.internalField0383.internalMethod08576();
         float localValue5 = this.internalField0382.internalMethod08576() / 100.0F;
         int localValue6 = Math.min(32, 48);
         if (!(localValue4 <= 0.0F) && !(localValue5 <= 0.0F) && localValue6 > 0) {
            Matrix4f localValue7 = new Matrix4f(localValue1.getProjectionMatrix()).mul(localValue1.getPositionMatrix());
            FrustumIntersection localValue8 = new FrustumIntersection(localValue7);
            ArrayList localValue9 = new ArrayList();

            for (WorldModule.InternalType0112 localValue11 : this.internalField0416) {
               float localValue12 = localValue11.internalField0808.internalMethod02881() * localValue11.internalField0809.internalMethod02881();
               if (localValue12 <= 0.01F) {
                  localValue11.internalField1099 = false;
                  localValue11.internalField1321.internalMethod07062(false);
               } else {
                  Vec3d localValue13 = ScreenProjectionUtils.internalMethod00202(localValue11.internalField0283, localValue11.internalField1104, localValue1.getTickDelta());
                  float localValue14 = (float)(localValue13.x - localValue3.x);
                  float localValue15 = (float)(localValue13.y - localValue3.y);
                  float localValue16 = (float)(localValue13.z - localValue3.z);
                  double localValue17 = Math.sqrt(localValue14 * localValue14 + localValue15 * localValue15 + localValue16 * localValue16);
                  if (!(localValue17 - localValue4 > 48.0) && localValue8.testSphere(localValue14, localValue15, localValue16, localValue4)) {
                     localValue9.add(new WorldModule.InternalType0111(localValue11, localValue14, localValue15, localValue16, localValue12 * localValue5, localValue17));
                  } else {
                     localValue11.internalField1099 = false;
                     localValue11.internalField1321.internalMethod07062(false);
                  }
               }
            }

            if (!localValue9.isEmpty()) {
               localValue9.sort(Comparator.comparingDouble(WorldModule.InternalType0111::internalMethod05039));

               for (WorldModule.InternalType0111 localValue22 : (Iterable<WorldModule.InternalType0111>)(Iterable<?>)localValue9) {
                  localValue22.internalMethod05685().internalField1099 = false;
               }

               float localValue21 = localValue4 * 0.35F;
               float localValue23 = localValue5 * 2.0F;
               ArrayList localValue24 = new ArrayList(localValue6);
               ArrayList localValue25 = new ArrayList();

               for (WorldModule.InternalType0111 localValue28 : (Iterable<WorldModule.InternalType0111>)(Iterable<?>)localValue9) {
                  if (localValue24.size() >= localValue6) {
                     localValue25.add(localValue28);
                  } else {
                     int localValue31 = -1;
                     float localValue35 = 0.0F;
                     int localValue18 = 0;

                     while (true) {
                        if (localValue18 < localValue24.size()) {
                           float localValue19 = localValue28.internalMethod05454((WorldModule.InternalType0111)localValue24.get(localValue18));
                           if (!(localValue19 < localValue21)) {
                              localValue18++;
                              continue;
                           }

                           localValue31 = localValue18;
                           localValue35 = localValue19;
                        }

                        if (localValue31 < 0) {
                           localValue24.add(localValue28);
                        } else {
                           WorldModule.InternalType0111 localValue37 = (WorldModule.InternalType0111)localValue24.get(localValue31);
                           if (localValue37.internalMethod08769() < localValue23) {
                              float localValue39 = localValue28.internalMethod08769()
                                 * 0.35F
                                 * (1.0F - localValue35 / localValue21)
                                 * (1.0F - localValue28.internalMethod05685().internalField1321.internalMethod02881());
                              if (localValue39 > 0.0F) {
                                 localValue24.set(localValue31, localValue37.internalMethod03012(Math.min(localValue39, localValue23 - localValue37.internalMethod08769())));
                              }
                           }

                           localValue25.add(localValue28);
                        }
                        break;
                     }
                  }
               }

               ArrayList localValue27 = new ArrayList(localValue24.size() + 16);

               for (WorldModule.InternalType0111 localValue32 : (Iterable<WorldModule.InternalType0111>)(Iterable<?>)localValue24) {
                  localValue32.internalMethod05685().internalField1099 = true;
                  this.internalMethod07443(localValue27, localValue32, localValue2, localValue4, localValue32.internalMethod05685().internalField1321.internalMethod07059(1.0F));
               }

               int localValue30 = 0;

               for (WorldModule.InternalType0111 localValue36 : (Iterable<WorldModule.InternalType0111>)(Iterable<?>)localValue25) {
                  float localValue38 = localValue36.internalMethod05685().internalField1321.internalMethod07059(0.0F);
                  if (!(localValue38 <= 0.001F) && localValue30 < 16 && this.internalMethod07443(localValue27, localValue36, localValue2, localValue4, localValue38)) {
                     localValue30++;
                  }
               }

               if (!localValue27.isEmpty()) {
                  Matrix4f localValue34 = new Matrix4f(localValue7).invert();
                  RenderPipeline.internalField0344.internalMethod02901(localValue34, 1.0F, localValue27);
               }
            }
         }
      }
   }

   private boolean internalMethod07443(List<ParticleLightShader.InternalType0228> localValue1, WorldModule.InternalType0111 localValue2, ColorRGBA localValue3, float localValue4, float localValue5) {
      float localValue6 = localValue2.internalMethod08769() * localValue5;
      if (localValue6 <= 0.001F) {
         return false;
      } else {
         localValue1.add(
            new ParticleLightShader.InternalType0228(
               localValue2.internalMethod05040(),
               localValue2.internalMethod05042(),
               localValue2.internalMethod08768(),
               localValue4,
               localValue3.getRed() / 255.0F,
               localValue3.getGreen() / 255.0F,
               localValue3.getBlue() / 255.0F,
               localValue6
            )
         );
         return true;
      }
   }

   static final class InternalType0111 {
      private final WorldModule.InternalType0112 internalField0750;
      private final float internalField0205;
      private final float internalField0206;
      private final float internalField1048;
      private final float internalField1047;
      private final double internalField0194;

      InternalType0111(WorldModule.InternalType0112 localValue1, float localValue2, float localValue3, float localValue4, float localValue5, double localValue6) {
         this.internalField0750 = localValue1;
         this.internalField0205 = localValue2;
         this.internalField0206 = localValue3;
         this.internalField1048 = localValue4;
         this.internalField1047 = localValue5;
         this.internalField0194 = localValue6;
      }

      double internalMethod05039() {
         return this.internalField0750.internalField1099 ? this.internalField0194 * 0.5 : this.internalField0194;
      }

      float internalMethod05454(WorldModule.InternalType0111 localValue1) {
         float localValue2 = this.internalField0205 - localValue1.internalField0205;
         float localValue3 = this.internalField0206 - localValue1.internalField0206;
         float localValue4 = this.internalField1048 - localValue1.internalField1048;
         return (float)Math.sqrt(localValue2 * localValue2 + localValue3 * localValue3 + localValue4 * localValue4);
      }

      WorldModule.InternalType0111 internalMethod03012(float localValue1) {
         return new WorldModule.InternalType0111(
            this.internalField0750, this.internalField0205, this.internalField0206, this.internalField1048, this.internalField1047 + localValue1, this.internalField0194
         );
      }

      @Override
      public final String toString() {
         return "InternalType0111[particle=" + this.internalField0750 + ", x=" + this.internalField0205 + ", y=" + this.internalField0206 + ", z=" + this.internalField1048 + ", intensity=" + this.internalField1047 + ", distance=" + this.internalField0194 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0750);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0205);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0206);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1048);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1047);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0194);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         WorldModule.InternalType0111 other = (WorldModule.InternalType0111) localValue1;
         return java.util.Objects.equals(this.internalField0750, other.internalField0750)
            && java.util.Objects.equals(this.internalField0205, other.internalField0205)
            && java.util.Objects.equals(this.internalField0206, other.internalField0206)
            && java.util.Objects.equals(this.internalField1048, other.internalField1048)
            && java.util.Objects.equals(this.internalField1047, other.internalField1047)
            && java.util.Objects.equals(this.internalField0194, other.internalField0194);
      }

      public WorldModule.InternalType0112 internalMethod05685() {
         return this.internalField0750;
      }

      public float internalMethod05040() {
         return this.internalField0205;
      }

      public float internalMethod05042() {
         return this.internalField0206;
      }

      public float internalMethod08768() {
         return this.internalField1048;
      }

      public float internalMethod08769() {
         return this.internalField1047;
      }

      public double internalMethod05041() {
         return this.internalField0194;
      }
   }

   static class InternalType0112 {
      Vec3d internalField0283;
      Vec3d internalField0282;
      Vec3d internalField1104;
      Vec3d internalField1106;
      Vec3d internalField1105;
      Vec3d internalField1103;
      final long internalField0229;
      float internalField0205;
      boolean internalField0277;
      boolean internalField0276;
      boolean internalField1099;
      final Stopwatch internalField0519 = new Stopwatch();
      final AnimatedValue internalField0808 = new AnimatedValue(300L, Easing.internalField1626);
      final AnimatedValue internalField0809 = new AnimatedValue(200L, 1.0F, Easing.internalField1626);
      final AnimatedValue internalField1321 = new AnimatedValue(220L, Easing.internalField1626);

      public InternalType0112(Vec3d localValue1, Vec3d localValue2, Vec3d localValue3, Vec3d localValue4, long localValue5, float localValue7) {
         this.internalField1104 = localValue1;
         this.internalField1106 = localValue2;
         this.internalField1105 = localValue3.multiply(0.04F);
         this.internalField1103 = localValue4.multiply(0.04F);
         this.internalField0229 = localValue5;
         this.internalField0205 = localValue7;
         this.internalField0282 = localValue2;
         this.internalField0283 = localValue1;
         this.internalField0808.internalMethod07061(1000L);
      }

      boolean internalMethod06725() {
         return this.internalField0277 || this.internalField0519.internalMethod02365(this.internalField0229);
      }

      void internalMethod06724() {
         this.internalField0283 = this.internalField1104;
         this.internalField0282 = this.internalField1106;
         this.internalField1104 = this.internalField1104.add(this.internalField1105);
         this.internalField1106 = this.internalField1106.add(this.internalField1103);
         this.internalField1105 = this.internalField1105.multiply(0.98);
         this.internalField1103 = this.internalField1103.multiply(0.98);
      }
   }
}
