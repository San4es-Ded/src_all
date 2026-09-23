 package su.sacura.util.type;
 
 import net.minecraft.client.MinecraftClient;
 import net.minecraft.client.render.RenderTickCounter;
 import net.minecraft.client.render.Tessellator;
 import net.minecraft.client.util.Window;
 import su.sacura.util.impl.render.engine.DrawEngine;
 import su.sacura.util.impl.render.engine.DrawEngineImpl;
 
 public interface MinecraftWrapper {
   public static final MinecraftClient mc = MinecraftClient.getInstance();
   
   public static final RenderTickCounter tickCounter = mc.getRenderTickCounter();
   
   public static final Window sr = mc.getWindow();
   
   public static final DrawEngine drawEngine = (DrawEngine)new DrawEngineImpl();
   
   public static final Tessellator tessellator = Tessellator.getInstance();
 }


