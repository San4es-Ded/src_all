package rockstar.client.internal.render;




import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.*;
import rockstar.client.compat.RenderSystem;
import rockstar.client.compat.ShaderProgram;
import net.minecraft.client.render.VertexFormats;
import org.lwjgl.opengl.GL11;

public class AdaptiveStateRenderer implements MinecraftClientAccess, WindowAccess {
   private ManagedFramebuffer internalField0769;
   private ManagedFramebuffer internalField0770;
   private ShaderProgramBase internalField0104;
   private long internalField0229;
   private boolean internalField0277;

   public void internalMethod02780() {
      this.internalField0104 = new ShaderProgramBase(RockstarClient.id("adaptive_state/data"), VertexFormats.POSITION_TEXTURE_COLOR);
   }

   public void internalMethod00769(float localValue1, float localValue2, float localValue3) {
      if (this.internalField0104 != null && !RockstarClient.getInstance().internalMethod06896()) {
         if (this.internalField0769 == null) {
            this.internalField0769 = new ManagedFramebuffer(false);
            this.internalField0770 = new ManagedFramebuffer(false);
         }

         this.internalField0769.internalMethod03472(0.25F);
         this.internalField0770.internalMethod03472(0.25F);
         if (this.internalField0769.getColorAttachment() == null) {
            this.internalField0769.setClearColor(0.0F, 0.0F, 0.0F, 1.0F);
            this.internalField0769.internalMethod02227(true);
            this.internalField0769.internalMethod03248();
         }

         long localValue4 = System.currentTimeMillis();
         float localValue6 = Math.min(64.0F, this.internalField0229 == 0L ? 64.0F : (float)(localValue4 - this.internalField0229));
         if (!this.internalField0277 || !(localValue6 <= 0.0F)) {
            this.internalField0229 = localValue4;
            boolean localValue7 = !this.internalField0277 || this.internalField0769.textureWidth != this.internalField0770.textureWidth;
            float localValue8 = localValue7 ? 1.0F : (float)(1.0 - Math.exp(-localValue6 / 100.0F));
            float localValue9 = internalField0149.getWindow().getFramebufferWidth();
            float localValue10 = internalField0149.getWindow().getFramebufferHeight();
            boolean localValue11 = GL11.glIsEnabled(3089);
            if (localValue11) {
               GL11.glDisable(3089);
            }

            try {
               RenderSystem.disableBlend();
               ShaderProgram localValue12 = this.internalField0104.internalMethod01220();
               localValue12.getUniform("Threshold").set(localValue1, localValue2);
               localValue12.getUniform("Probe").set(localValue9 > 0.0F ? localValue3 / localValue9 : 0.0F, localValue10 > 0.0F ? localValue3 / localValue10 : 0.0F);
               localValue12.getUniform("Rate").set(localValue8);
               this.internalField0770.internalMethod02227(true);
               RenderSystem.setShaderTexture(0, InterfaceBlurRenderer.internalMethod05153());
               RenderSystem.setShaderTexture(1, this.internalField0769.getColorAttachmentView());
               RenderPipeline.internalMethod01737(0.0F, 0.0F, internalField0267.getScaledWidth(), internalField0267.getScaledHeight());
               this.internalField0770.internalMethod03248();
               RenderSystem.setShaderTexture(0, 0);
               RenderSystem.setShaderTexture(1, 0);
            } finally {
               if (localValue11) {
                  GL11.glEnable(3089);
               }
            }

            ManagedFramebuffer localValue16 = this.internalField0770;
            this.internalField0770 = this.internalField0769;
            this.internalField0769 = localValue16;
            this.internalField0277 = true;
         }
      }
   }

   public int internalMethod02779() {
      return this.internalField0769 == null ? 0 : rockstar.client.render.FramebufferCompat.glId(this.internalField0769.getColorAttachment());
   }
}
