 package su.sacura.display.csgui.components.module;
 
 import net.minecraft.client.gui.DrawContext;
 import su.sacura.util.type.MinecraftWrapper;
 
 public abstract class Component implements MinecraftWrapper {
   public float x;
   
   public float y;
   
   public float width;
   
   public float height;
   
   public Component(float width, float height) {
     this.width = width;
     this.height = height;
   }
   
   public abstract void render(DrawContext paramDrawContext, float paramFloat1, float paramFloat2, int paramInt1, int paramInt2, int paramInt3);
   
   public abstract boolean mouseClicked(double paramDouble1, double paramDouble2, int paramInt);
   
   public void mouseReleased(int button) {}
   
   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
     return false;
   }
   
   public boolean charTyped(char chr, int modifiers) {
     return false;
   }
   
   public float getHeight() {
     return this.height;
   }
 }


