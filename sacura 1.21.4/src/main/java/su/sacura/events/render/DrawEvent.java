 package su.sacura.events.render;
 
 import net.minecraft.client.gui.DrawContext;
 import su.sacura.util.impl.render.engine.DrawEngine;
 
 public class DrawEvent {
   private DrawContext drawContext;
   
   private DrawEngine drawEngine;
   
   private float partialTicks;
   
   public DrawEvent(DrawContext drawContext, DrawEngine drawEngine, float partialTicks) {
     this.drawContext = drawContext;
     this.drawEngine = drawEngine;
     this.partialTicks = partialTicks;
   }
   
   public DrawContext getDrawContext() {
     return this.drawContext;
   }
   
   public DrawEngine getDrawEngine() {
     return this.drawEngine;
   }
   
   public float getPartialTicks() {
     return this.partialTicks;
   }
 }


