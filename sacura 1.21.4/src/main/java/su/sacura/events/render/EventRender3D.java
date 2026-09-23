 package su.sacura.events.render;
 
 import net.minecraft.client.render.RenderTickCounter;
 import net.minecraft.client.util.math.MatrixStack;
 
 public class EventRender3D {
   private RenderTickCounter deltatick;
   
   private MatrixStack matrixStack;
   
   public EventRender3D(MatrixStack matrixStack, RenderTickCounter deltatick) {
     this.matrixStack = matrixStack;
     this.deltatick = deltatick;
   }
   
   public MatrixStack getMatrixStack() {
     return this.matrixStack;
   }
   
   public RenderTickCounter getDeltatick() {
     return this.deltatick;
   }
 }


