package rockstar.client.internal.render;





import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.esp.*;
import rockstar.client.*;
import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;
import rockstar.client.compat.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.BlockState;
import rockstar.client.compat.ShaderProgramKeys;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;

public final class EspGlowCaptureRenderer implements MinecraftClientAccess, WindowAccess {
   public static boolean internalField0277;
   public static boolean internalField0276;
   public static boolean internalField1099;
   private static final ManagedFramebuffer internalField0769 = new ManagedFramebuffer(true).internalMethod06013().internalMethod03472(1.0F);
   private static final int internalField0227 = 15728880;
   private static final List<EspGlowCaptureRenderer.InternalType0252> internalField0416 = new ArrayList<>();
   private static final List<EspGlowCaptureRenderer.InternalType0077> internalField0417 = new ArrayList<>();
   private static final List<EspGlowCaptureRenderer.InternalType0076> internalField1145 = new ArrayList<>();
   private static boolean internalField1100;

   private EspGlowCaptureRenderer() {
   }

   public static boolean internalMethod05096() {
      EspManager localValue0 = EspManager.internalMethod06726();
      return localValue0 == null
         ? false
         : internalMethod04248(localValue0.internalMethod05464(GlowEspFeature.class))
            || internalMethod04248(localValue0.internalMethod05464(FlameEspFeature.class))
            || internalMethod04248(localValue0.internalMethod05464(FillEspFeature.class));
   }

   private static boolean internalMethod04248(EspFeature localValue0) {
      return localValue0 != null && localValue0.internalMethod02927(ItemTargetType.internalField0012);
   }

   public static void internalMethod05095() {
      internalField0276 = true;
      internalField1099 = false;
      internalField1100 = false;
      internalField0416.clear();
      internalField0417.clear();
      internalField1145.clear();
   }

   public static void internalMethod05102() {
      internalField0276 = false;
   }

   public static void internalMethod00596(ItemStack localValue0, ItemDisplayContext localValue1, boolean localValue2, MatrixStack localValue3) {
      if (internalMethod05096() && localValue0 != null && !localValue0.isEmpty()) {
         internalField0416.add(new EspGlowCaptureRenderer.InternalType0252(localValue0.copy(), localValue1, localValue2, new Matrix4f(localValue3.peek().getPositionMatrix())));
      }
   }

   public static void internalMethod02731(BlockState localValue0, int localValue1, MatrixStack localValue2) {
      if (internalMethod05096() && localValue0 != null) {
         internalField0417.add(new EspGlowCaptureRenderer.InternalType0077(localValue0, localValue1, new Matrix4f(localValue2.peek().getPositionMatrix())));
      }
   }

   public static void internalMethod01707(Arm localValue0, MatrixStack localValue1) {
      if (internalMethod05096()) {
         internalField1145.add(new EspGlowCaptureRenderer.InternalType0076(localValue0, new Matrix4f(localValue1.peek().getPositionMatrix())));
      }
   }

   public static void internalMethod08038() {
      if (!internalField1145.isEmpty() && internalMethod05096()) {
         ClientPlayerEntity localValue0 = internalField0149.player;
         if (localValue0 != null) {
            if (internalField0149.getEntityRenderDispatcher().getRenderer(localValue0) instanceof PlayerEntityRenderer localValue2) {
               internalField0277 = true;
               internalField0769.internalMethod02227(true);

               try {
                  Immediate localValue3 = internalField0149.getBufferBuilders().getEntityVertexConsumers();

                  for (EspGlowCaptureRenderer.InternalType0252 localValue5 : internalField0416) {
                     MatrixStack localValue6 = new MatrixStack();
                     localValue6.multiplyPositionMatrix(localValue5.internalMethod05938());
                     LegacyRenderCompat.renderItem(
                           localValue0,
                           localValue5.internalMethod04909(),
                           localValue5.internalMethod06336(),
                           localValue5.internalMethod01777(),
                           localValue6,
                           localValue3,
                           localValue0.getEntityWorld(),
                           15728880,
                           OverlayTexture.DEFAULT_UV,
                           localValue0.getId() + localValue5.internalMethod06336().ordinal()
                        );
                  }

                  for (EspGlowCaptureRenderer.InternalType0077 localValue11 : internalField0417) {
                     MatrixStack localValue13 = new MatrixStack();
                     localValue13.multiplyPositionMatrix(localValue11.internalMethod05044());
                     internalField0149.getBlockRenderManager().renderBlockAsEntity(localValue11.internalMethod04951(), localValue13, localValue3, 15728880, localValue11.internalMethod05033());
                  }

                  localValue3.draw();
                  RenderSystem.clearColor(0.0F, 0.0F, 0.0F, 0.0F);
                  RenderSystem.clear(16384);
                  Identifier localValue10 = localValue0.getSkin().body().texturePath();

                  for (EspGlowCaptureRenderer.InternalType0076 localValue14 : internalField1145) {
                     MatrixStack localValue7 = new MatrixStack();
                     localValue7.multiplyPositionMatrix(localValue14.internalMethod00935());
                     if (localValue14.internalMethod00626() == Arm.LEFT) {
                        LegacyRenderCompat.renderArm(localValue2, true, localValue7, localValue3, 15728880, localValue10, localValue0.isModelPartVisible(PlayerModelPart.LEFT_SLEEVE));
                     } else {
                        LegacyRenderCompat.renderArm(localValue2, false, localValue7, localValue3, 15728880, localValue10, localValue0.isModelPartVisible(PlayerModelPart.RIGHT_SLEEVE));
                     }
                  }

                  localValue3.draw();
                  internalField1100 = true;
               } catch (Exception localValue8) {
               }

               internalField0769.internalMethod03248();
               internalField0277 = false;
               internalField0416.clear();
               internalField0417.clear();
               internalField1145.clear();
            }
         }
      }
   }

   public static void internalMethod04936(ManagedFramebuffer localValue0) {
      if (internalField1100 && localValue0 != null) {
         localValue0.beginWrite(true);
         RenderSystem.enableBlend();
         RenderSystem.blendFuncSeparate(SourceFactor.ZERO, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ZERO, DestFactor.ONE_MINUS_SRC_ALPHA);
         RenderSystem.disableDepthTest();
         RenderSystem.disableCull();
         RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         RenderSystem.setShaderTexture(0, internalField0769.getColorAttachmentView());
         RenderPipeline.internalMethod01737(0.0F, 0.0F, internalField0267.getScaledWidth(), internalField0267.getScaledHeight());
         RenderSystem.setShaderTexture(0, 0);
         RenderSystem.defaultBlendFunc();
         RenderSystem.enableCull();
         rockstar.client.render.FramebufferCompat.beginWrite(internalField0149.getFramebuffer(), true);
      }
   }

   static final class InternalType0076 {
      private final Arm internalField0112;
      private final Matrix4f internalField0788;

      InternalType0076(Arm localValue1, Matrix4f localValue2) {
         this.internalField0112 = localValue1;
         this.internalField0788 = localValue2;
      }

      @Override
      public final String toString() {
         return "InternalType0076[arm=" + this.internalField0112 + ", pose=" + this.internalField0788 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0112);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0788);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         EspGlowCaptureRenderer.InternalType0076 other = (EspGlowCaptureRenderer.InternalType0076) localValue1;
         return java.util.Objects.equals(this.internalField0112, other.internalField0112)
            && java.util.Objects.equals(this.internalField0788, other.internalField0788);
      }

      public Arm internalMethod00626() {
         return this.internalField0112;
      }

      public Matrix4f internalMethod00935() {
         return this.internalField0788;
      }
   }

   static final class InternalType0077 {
      private final BlockState internalField0934;
      private final int internalField0227;
      private final Matrix4f internalField0788;

      InternalType0077(BlockState localValue1, int localValue2, Matrix4f localValue3) {
         this.internalField0934 = localValue1;
         this.internalField0227 = localValue2;
         this.internalField0788 = localValue3;
      }

      @Override
      public final String toString() {
         return "InternalType0077[state=" + this.internalField0934 + ", overlay=" + this.internalField0227 + ", pose=" + this.internalField0788 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0934);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0788);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         EspGlowCaptureRenderer.InternalType0077 other = (EspGlowCaptureRenderer.InternalType0077) localValue1;
         return java.util.Objects.equals(this.internalField0934, other.internalField0934)
            && java.util.Objects.equals(this.internalField0227, other.internalField0227)
            && java.util.Objects.equals(this.internalField0788, other.internalField0788);
      }

      public BlockState internalMethod04951() {
         return this.internalField0934;
      }

      public int internalMethod05033() {
         return this.internalField0227;
      }

      public Matrix4f internalMethod05044() {
         return this.internalField0788;
      }
   }

   static final class InternalType0252 {
      private final ItemStack internalField0878;
      private final ItemDisplayContext internalField0372;
      private final boolean internalField0277;
      private final Matrix4f internalField0788;

      InternalType0252(ItemStack localValue1, ItemDisplayContext localValue2, boolean localValue3, Matrix4f localValue4) {
         this.internalField0878 = localValue1;
         this.internalField0372 = localValue2;
         this.internalField0277 = localValue3;
         this.internalField0788 = localValue4;
      }

      @Override
      public final String toString() {
         return "InternalType0252[stack=" + this.internalField0878 + ", mode=" + this.internalField0372 + ", leftHanded=" + this.internalField0277 + ", pose=" + this.internalField0788 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0878);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0372);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0277);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0788);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         EspGlowCaptureRenderer.InternalType0252 other = (EspGlowCaptureRenderer.InternalType0252) localValue1;
         return java.util.Objects.equals(this.internalField0878, other.internalField0878)
            && java.util.Objects.equals(this.internalField0372, other.internalField0372)
            && java.util.Objects.equals(this.internalField0277, other.internalField0277)
            && java.util.Objects.equals(this.internalField0788, other.internalField0788);
      }

      public ItemStack internalMethod04909() {
         return this.internalField0878;
      }

      public ItemDisplayContext internalMethod06336() {
         return this.internalField0372;
      }

      public boolean internalMethod01777() {
         return this.internalField0277;
      }

      public Matrix4f internalMethod05938() {
         return this.internalField0788;
      }
   }
}
