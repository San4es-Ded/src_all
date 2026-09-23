package rockstar.client.internal.render;




import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.*;
import com.mojang.blaze3d.systems.ProjectionType;
import rockstar.client.compat.RenderSystem;
import java.util.List;
import net.minecraft.client.gl.Framebuffer;
import rockstar.client.compat.ShaderProgramKeys;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;

public class TargetLensRenderer implements MinecraftClientAccess, WindowAccess {
   private final ManagedFramebuffer internalField0769 = new ManagedFramebuffer(false).internalMethod06013();
   private final ManagedFramebuffer internalField0770 = new ManagedFramebuffer(false).internalMethod06013();
   private TargetLensShader internalField0441;

   public void internalMethod06763() {
      if (this.internalField0441 == null) {
         this.internalField0441 = new TargetLensShader(RockstarClient.id("target_lens/data"));
      }
   }

   public void internalMethod06535(float localValue1, float localValue2, List<TargetLensRenderer.InternalType0350> localValue3) {
      if (this.internalField0441 != null && localValue3 != null && !localValue3.isEmpty() && !(localValue2 <= 0.001F)) {
         Framebuffer localValue4 = internalField0149.getFramebuffer();
         if (localValue4 != null && localValue4.getDepthAttachment() != null) {
            int localValue5 = internalField0267.getScaledWidth();
            int localValue6 = internalField0267.getScaledHeight();
            if (localValue5 > 0 && localValue6 > 0) {
               int localValue7 = rockstar.client.render.FramebufferCompat.glId(localValue4.getDepthAttachment());
               RenderSystem.disableBlend();
               RenderSystem.depthMask(false);
               RenderSystem.backupProjectionMatrix();
               Matrix4f localValue8 = new Matrix4f().setOrtho(0.0F, localValue5, localValue6, 0.0F, 1000.0F, 21000.0F);
               RenderSystem.setProjectionMatrix(localValue8, ProjectionType.ORTHOGRAPHIC);
               Matrix4fStack localValue9 = RenderSystem.getModelViewStack();
               localValue9.pushMatrix();
               localValue9.identity().translate(0.0F, 0.0F, -11000.0F);
               this.internalField0769.internalMethod03245();
               this.internalField0441.internalMethod01220();
               this.internalField0441.internalMethod02500(localValue1, localValue2, List.of());
               RenderSystem.setShaderTexture(0, localValue4.getColorAttachmentView());
               RenderSystem.setShaderTexture(1, localValue7);
               RenderPipeline.internalMethod01737(0.0F, 0.0F, localValue5, localValue6);
               this.internalField0769.internalMethod03248();
               this.internalField0770.internalMethod03245();
               this.internalField0441.internalMethod01220();
               this.internalField0441.internalMethod02500(localValue1, localValue2, localValue3);
               RenderSystem.setShaderTexture(0, this.internalField0769.getColorAttachmentView());
               RenderSystem.setShaderTexture(1, localValue7);
               RenderPipeline.internalMethod01737(0.0F, 0.0F, localValue5, localValue6);
               this.internalField0770.internalMethod03248();
               RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
               RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
               RenderSystem.setShaderTexture(0, this.internalField0770.getColorAttachmentView());
               RenderPipeline.internalMethod01737(0.0F, 0.0F, localValue5, localValue6);
               RenderSystem.setShaderTexture(1, 0);
               RenderSystem.setShaderTexture(0, 0);
               localValue9.popMatrix();
               RenderSystem.restoreProjectionMatrix();
               RenderSystem.depthMask(true);
               RenderSystem.enableBlend();
            }
         }
      }
   }

   public static final class InternalType0350 {
      private final float internalField0205;
      private final float internalField0206;
      private final float internalField1048;
      private final float internalField1047;

      public InternalType0350(float localValue1, float localValue2, float localValue3, float localValue4) {
         this.internalField0205 = localValue1;
         this.internalField0206 = localValue2;
         this.internalField1048 = localValue3;
         this.internalField1047 = localValue4;
      }

      @Override
      public final String toString() {
         return "InternalType0350[u=" + this.internalField0205 + ", v=" + this.internalField0206 + ", depth=" + this.internalField1048 + ", radius=" + this.internalField1047 + "]";
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
         TargetLensRenderer.InternalType0350 other = (TargetLensRenderer.InternalType0350) localValue1;
         return java.util.Objects.equals(this.internalField0205, other.internalField0205)
            && java.util.Objects.equals(this.internalField0206, other.internalField0206)
            && java.util.Objects.equals(this.internalField1048, other.internalField1048)
            && java.util.Objects.equals(this.internalField1047, other.internalField1047);
      }

      public float internalMethod00366() {
         return this.internalField0205;
      }

      public float internalMethod00369() {
         return this.internalField0206;
      }

      public float internalMethod07721() {
         return this.internalField1048;
      }

      public float internalMethod07723() {
         return this.internalField1047;
      }
   }
}
