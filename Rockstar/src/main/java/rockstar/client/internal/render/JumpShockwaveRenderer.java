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

public class JumpShockwaveRenderer implements MinecraftClientAccess, WindowAccess {
   private final ManagedFramebuffer internalField0769 = new ManagedFramebuffer(false).internalMethod06013();
   private final ManagedFramebuffer internalField0770 = new ManagedFramebuffer(false).internalMethod06013();
   private ShockwaveShader internalField0339;

   public void internalMethod04237() {
      if (this.internalField0339 == null) {
         this.internalField0339 = new ShockwaveShader(RockstarClient.id("jump_shockwave/data"));
      }
   }

   public void internalMethod01211(Matrix4f localValue1, List<JumpShockwaveRenderer.InternalType0218> localValue2) {
      if (this.internalField0339 != null && localValue2 != null && !localValue2.isEmpty()) {
         Framebuffer localValue3 = internalField0149.getFramebuffer();
         if (localValue3 != null && localValue3.getDepthAttachment() != null) {
            int localValue4 = internalField0267.getScaledWidth();
            int localValue5 = internalField0267.getScaledHeight();
            if (localValue4 > 0 && localValue5 > 0) {
               int localValue6 = rockstar.client.render.FramebufferCompat.glId(localValue3.getDepthAttachment());
               int localValue7 = Math.min(localValue2.size(), 12);
               RenderSystem.disableBlend();
               RenderSystem.depthMask(false);
               RenderSystem.backupProjectionMatrix();
               Matrix4f localValue8 = new Matrix4f().setOrtho(0.0F, localValue4, localValue5, 0.0F, 1000.0F, 21000.0F);
               RenderSystem.setProjectionMatrix(localValue8, ProjectionType.ORTHOGRAPHIC);
               Matrix4fStack localValue9 = RenderSystem.getModelViewStack();
               localValue9.pushMatrix();
               localValue9.identity().translate(0.0F, 0.0F, -11000.0F);
               ManagedFramebuffer[] localValue10 = new ManagedFramebuffer[]{this.internalField0769, this.internalField0770};
               Object localValue11 = localValue3;
               byte localValue12 = 0;

               for (int localValue13 = 0; localValue13 < localValue7; localValue13++) {
                  ManagedFramebuffer localValue14 = localValue10[localValue12];
                  localValue14.internalMethod03245();
                  this.internalField0339.internalMethod01220();
                  this.internalField0339.internalMethod04470(localValue1, (JumpShockwaveRenderer.InternalType0218)localValue2.get(localValue13));
                  RenderSystem.setShaderTexture(0, ((net.minecraft.client.gl.Framebuffer)localValue11).getColorAttachmentView());
                  RenderSystem.setShaderTexture(1, localValue6);
                  RenderPipeline.internalMethod01737(0.0F, 0.0F, localValue4, localValue5);
                  localValue14.internalMethod03248();
                  localValue11 = localValue14;
                  localValue12 ^= 1;
               }

               RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
               RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
               RenderSystem.setShaderTexture(0, ((net.minecraft.client.gl.Framebuffer)localValue11).getColorAttachmentView());
               RenderPipeline.internalMethod01737(0.0F, 0.0F, localValue4, localValue5);
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

   public static final class InternalType0218 {
      private final float internalField0205;
      private final float internalField0206;
      private final float internalField1048;
      private final float internalField1047;
      private final float internalField1049;
      private final float internalField1046;
      private final float internalField1456;
      private final float internalField1457;
      private final float internalField1458;
      private final float internalField1459;

      public InternalType0218(float localValue1, float localValue2, float localValue3, float localValue4, float localValue5, float localValue6, float localValue7, float localValue8, float localValue9, float localValue10) {
         this.internalField0205 = localValue1;
         this.internalField0206 = localValue2;
         this.internalField1048 = localValue3;
         this.internalField1047 = localValue4;
         this.internalField1049 = localValue5;
         this.internalField1046 = localValue6;
         this.internalField1456 = localValue7;
         this.internalField1457 = localValue8;
         this.internalField1458 = localValue9;
         this.internalField1459 = localValue10;
      }

      @Override
      public final String toString() {
         return "InternalType0218[cx=" + this.internalField0205 + ", cy=" + this.internalField0206 + ", cz=" + this.internalField1048 + ", worldRadius=" + this.internalField1047 + ", thickness=" + this.internalField1049 + ", r=" + this.internalField1046 + ", g=" + this.internalField1456 + ", b=" + this.internalField1457 + ", intensity=" + this.internalField1458 + ", strength=" + this.internalField1459 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0205);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0206);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1048);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1047);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1049);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1046);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1456);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1457);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1458);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1459);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         JumpShockwaveRenderer.InternalType0218 other = (JumpShockwaveRenderer.InternalType0218) localValue1;
         return java.util.Objects.equals(this.internalField0205, other.internalField0205)
            && java.util.Objects.equals(this.internalField0206, other.internalField0206)
            && java.util.Objects.equals(this.internalField1048, other.internalField1048)
            && java.util.Objects.equals(this.internalField1047, other.internalField1047)
            && java.util.Objects.equals(this.internalField1049, other.internalField1049)
            && java.util.Objects.equals(this.internalField1046, other.internalField1046)
            && java.util.Objects.equals(this.internalField1456, other.internalField1456)
            && java.util.Objects.equals(this.internalField1457, other.internalField1457)
            && java.util.Objects.equals(this.internalField1458, other.internalField1458)
            && java.util.Objects.equals(this.internalField1459, other.internalField1459);
      }

      public float internalMethod04651() {
         return this.internalField0205;
      }

      public float internalMethod04654() {
         return this.internalField0206;
      }

      public float internalMethod08125() {
         return this.internalField1048;
      }

      public float internalMethod08127() {
         return this.internalField1047;
      }

      public float internalMethod08136() {
         return this.internalField1049;
      }

      public float internalMethod08137() {
         return this.internalField1046;
      }

      public float internalMethod09362() {
         return this.internalField1456;
      }

      public float internalMethod09363() {
         return this.internalField1457;
      }

      public float internalMethod09371() {
         return this.internalField1458;
      }

      public float internalMethod09372() {
         return this.internalField1459;
      }
   }
}
