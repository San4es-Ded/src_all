 package su.sacura.events.render;
 
 import net.minecraft.client.util.math.MatrixStack;
 
 public class WorldRenderEvent {
   private MatrixStack stack;
   
   private float partialTicks;
   
   public WorldRenderEvent(MatrixStack stack, float partialTicks) {
     this.stack = stack;
     this.partialTicks = partialTicks;
   }
   
   public MatrixStack getStack() {
     return this.stack;
   }
   
   public float getPartialTicks() {
     return this.partialTicks;
   }
 }


