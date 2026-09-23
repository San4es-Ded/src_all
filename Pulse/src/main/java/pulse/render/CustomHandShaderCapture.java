package pulse.render;

import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.SimpleFramebuffer;
import org.lwjgl.opengl.GL30;
import pulse.module.ModuleRegistry;
import pulse.modules.visuals.CustomHand;
import pulse.render.shader.PulseShaderProgram;
import pulse.render.shader.ShaderLibrary;

public final class CustomHandShaderCapture {
   private static final MinecraftClient a = MinecraftClient.getInstance();
   private static Framebuffer b = null;
   private static int c = 0;
   private static int d = 0;
   private static boolean e = false;

   private CustomHandShaderCapture() {
   }

   private static int getFbo(Framebuffer fb) {
      if (fb == null) {
         return 0;
      }

      try {
         for (Field f : Framebuffer.class.getDeclaredFields()) {
            if (f.getType() == int.class && (f.getName().equals("fbo") || f.getName().contains("fbo") || f.getName().contains("Fbo"))) {
               f.setAccessible(true);
               return f.getInt(fb);
            }
         }
      } catch (Throwable var5) {
      }

      return 0;
   }

   private static int getColorAttachmentId(Framebuffer fb) {
      if (fb == null) {
         return 0;
      }

      try {
         Object attachment = fb.getColorAttachment();
         if (attachment instanceof Integer) {
            return (Integer)attachment;
         }

         if (attachment != null) {
            for (Method m : attachment.getClass().getMethods()) {
               if ((m.getName().equalsIgnoreCase("getGlId") || m.getName().equalsIgnoreCase("getGlTextureId") || m.getName().equalsIgnoreCase("getTextureId"))
                  && m.getParameterCount() == 0
                  && m.getReturnType() == int.class) {
                  return (Integer)m.invoke(attachment);
               }
            }

            for (Field f : attachment.getClass().getDeclaredFields()) {
               if (f.getType() == int.class) {
                  f.setAccessible(true);
                  int val = f.getInt(attachment);
                  if (val > 0) {
                     return val;
                  }
               }
            }
         }
      } catch (Throwable var7) {
      }

      return 0;
   }

   private static void beginWrite(Framebuffer fb, boolean setViewport) {
      if (fb != null) {
         try {
            for (Method m : fb.getClass().getMethods()) {
               if ((m.getName().equals("beginWrite") || m.getName().equals("bindWrite")) && m.getParameterCount() == 1) {
                  m.invoke(fb, setViewport);
                  return;
               }
            }
         } catch (Throwable var6) {
         }
      }
   }

   private static void setClearColor(Framebuffer fb, float r, float g, float b, float a) {
      if (fb != null) {
         try {
            for (Method m : fb.getClass().getMethods()) {
               if (m.getName().equals("setClearColor") && m.getParameterCount() == 4) {
                  m.invoke(fb, r, g, b, a);
                  return;
               }
            }
         } catch (Throwable var9) {
         }
      }
   }

   public static void beginCapture() {
      if (!guiOpen() && !e) {
         CustomHand customHand = ModuleRegistry.CUSTOM_HAND;
         if (ModuleRegistry.CUSTOM_HAND != null && customHand.k() && customHand.shaderEnabled.a()) {
            int iGetFramebufferWidth = a.getWindow().getFramebufferWidth();
            int iGetFramebufferHeight = a.getWindow().getFramebufferHeight();
            if (b == null || c != iGetFramebufferWidth || d != iGetFramebufferHeight) {
               if (b != null) {
                  b.delete();
               }

               b = new SimpleFramebuffer("hand_capture", iGetFramebufferWidth, iGetFramebufferHeight, true);
               setClearColor(b, 0.0F, 0.0F, 0.0F, 0.0F);
               c = iGetFramebufferWidth;
               d = iGetFramebufferHeight;
            }

            int mainFbo = getFbo(a.getFramebuffer());
            int customFbo = getFbo(b);
            if (mainFbo > 0 && customFbo > 0) {
               GL30.glBindFramebuffer(36008, mainFbo);
               GL30.glBindFramebuffer(36009, customFbo);
               GL30.glBlitFramebuffer(0, 0, iGetFramebufferWidth, iGetFramebufferHeight, 0, 0, iGetFramebufferWidth, iGetFramebufferHeight, 256, 9728);
            }

            beginWrite(b, false);
            GL30.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
            GL30.glClear(16384);
            e = true;
            return;
         }
      }
   }

   public static void endCapture() {
      if (guiOpen()) {
         resetForGui();
      } else if (e && b != null) {
         e = false;
         beginWrite(a.getFramebuffer(), true);
         CustomHand customHand = ModuleRegistry.CUSTOM_HAND;
         if (customHand != null && customHand.k() && customHand.shaderEnabled.a()) {
            a(customHand);
         }
      }
   }

   private static void a(CustomHand customHand) {
      Optional<PulseShaderProgram> optionalFind = ShaderLibrary.getRegistry().find("hand_shader");
      if (!optionalFind.isEmpty() && optionalFind.get().b()) {
         Color colorHandShaderColor = customHand.handShaderColor();
         float fCurrentTimeMillis = (float)(System.currentTimeMillis() % 100000L) / 1000.0F * customHand.shaderSpeed.a();
         float fA = customHand.shaderOpacity.a();
         int iGetFramebufferWidth = a.getWindow().getFramebufferWidth();
         int iGetFramebufferHeight = a.getWindow().getFramebufferHeight();
         int texId = getColorAttachmentId(b);
         if (texId > 0) {
            RenderSystem.getModelViewStack().pushMatrix();
            RenderSystem.getModelViewStack().identity();
            RenderSystemHelper.disableDepthTest();
            RenderSystemHelper.enableBlend();
            RenderSystemHelper.defaultBlendFunc();
            GlStateManager._activeTexture(33984);
            GlStateManager._bindTexture(texId);
            PulseShaderProgram pulseShaderProgram = optionalFind.get();
            pulseShaderProgram.d();
            pulseShaderProgram.a("time", fCurrentTimeMillis);
            pulseShaderProgram.a("screenSize", iGetFramebufferWidth, iGetFramebufferHeight);
            pulseShaderProgram.a(
               "baseColor", colorHandShaderColor.getRed() / 255.0F, colorHandShaderColor.getGreen() / 255.0F, colorHandShaderColor.getBlue() / 255.0F, 1.0F
            );
            pulseShaderProgram.a("alpha", fA);
            pulseShaderProgram.b("handTexture", 0);
            pulseShaderProgram.b("shaderMode", customHand.shaderModeIndex());
            pulseShaderProgram.b("shaderOnlyMode", customHand.shaderOnly.a() ? 1 : 0);
            PulseShaderProgram.a(0.0F, 0.0F, iGetFramebufferWidth, iGetFramebufferHeight);
            pulseShaderProgram.e();
            RenderSystem.getModelViewStack().popMatrix();
            GlStateManager._bindTexture(0);
            RenderSystemHelper.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystemHelper.enableDepthTest();
            RenderSystemHelper.disableBlend();
         }
      }
   }

   public static boolean isActive() {
      return !e ? false : !guiOpen();
   }

   public static Framebuffer getFramebuffer() {
      return b;
   }

   public static void dispose() {
      if (b != null) {
         b.delete();
         b = null;
      }

      e = false;
   }

   public static boolean guiOpen() {
      return a.currentScreen != null;
   }

   public static void resetForGui() {
      if (e) {
         e = false;
         MinecraftClient client = MinecraftClient.getInstance();
         if (client == null) {
            return;
         }

         Framebuffer framebuffer = client.getFramebuffer();
         if (framebuffer != null) {
            beginWrite(framebuffer, true);
         }

         RenderSystemHelper.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         RenderSystemHelper.enableDepthTest();
         RenderSystemHelper.disableBlend();
      }
   }

   public static void beginGuiRender() {
      resetForGui();
      RenderSystemHelper.enableBlend();
      RenderSystemHelper.defaultBlendFunc();
      RenderSystemHelper.disableDepthTest();
      RenderSystemHelper.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public static void endGuiRender() {
      RenderSystemHelper.enableDepthTest();
      RenderSystemHelper.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
   }
}
