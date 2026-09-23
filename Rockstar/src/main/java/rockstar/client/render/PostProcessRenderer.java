package rockstar.client.render;


import rockstar.client.*;
import rockstar.client.internal.ui.*;
import rockstar.modules.combat.*;
import rockstar.modules.movement.*;
import rockstar.modules.visual.*;
import rockstar.modules.player.*;
import rockstar.modules.other.*;

import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.systems.ProjectionType;
import rockstar.client.compat.RenderSystem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import net.minecraft.client.util.BufferAllocator;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public final class PostProcessRenderer implements MinecraftClientAccess {
   private static final CaptureShieldOverlay internalField0415 = new CaptureShieldOverlay();
   static final ManagedFramebuffer internalField0769 = new ManagedFramebuffer(true);
   public static final int internalField0227 = 0;
   public static final int internalField0228 = 1;
   public static final int internalField1053 = 2;
   static final int[] internalField0618 = new int[1024];
   static final int[] internalField0617 = new int[256];
   static int internalField1054;
   static int internalField1464 = 2;
   private static final List<PostProcessRenderer.InternalType0211> internalField0416 = new ArrayList<>();
   private static Immediate internalField0729;
   private static boolean internalField0277;
   static boolean internalField0276;
   static boolean internalField1099;
   private static boolean internalField1100;
   private static boolean internalField1102;
   private static boolean internalField1101;
   private static int internalField1470;
   static int internalField1463;

   private PostProcessRenderer() {
   }

   public static boolean internalMethod07188() {
      return internalField0277;
   }

   public static boolean internalMethod07190() {
      return internalField1099;
   }

   public static boolean internalMethod08007() {
      return internalField0277 && internalField0276 && !internalField1099 && !internalField1102;
   }

   public static void internalMethod01673(boolean localValue0) {
      internalField1101 = localValue0;
   }

   public static void internalMethod02021(Runnable localValue0) {
      internalField1102 = true;

      try {
         localValue0.run();
      } finally {
         internalField1102 = false;
      }
   }

   public static void internalMethod07187() {
      internalField0415.internalMethod02342();
      internalField0277 = false;
      internalField0276 = false;
      internalField1054 = 0;
   }

   public static void internalMethod07189() {
      internalField1054 = 0;
      internalField0416.clear();
      internalField0276 = false;
      NameProtectModule localValue0 = internalMethod03771();
      if (localValue0 != null) {
         localValue0.internalMethod09832();
      }

      internalMethod08009();
      if (internalField0277) {
         Framebuffer localValue1 = internalField0149.getFramebuffer();
         if (localValue1 != null && localValue1.textureWidth > 0 && localValue1.textureHeight > 0) {
            try {
               internalField0769.setClearColor(0.0F, 0.0F, 0.0F, 0.0F);
               internalField0769.internalMethod02227(true);
               rockstar.client.render.FramebufferCompat.beginWrite(localValue1, false);
               internalField0276 = true;
            } catch (Throwable localValue3) {
               RockstarClient.internalField0572
                  .error(
                     "[CaptureBypass] \u043d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u043f\u043e\u0434\u0433\u043e\u0442\u043e\u0432\u0438\u0442\u044c \u0441\u043b\u043e\u0439 \u0437\u0430\u043f\u043b\u0430\u0442\u043e\u043a",
                     localValue3
                  );
               internalField0276 = false;
            }
         }
      }
   }

   private static void internalMethod08009() {
      NameProtectModule localValue0 = internalMethod03771();
      boolean localValue1 = localValue0 != null && localValue0.isEnabled() && localValue0.internalMethod07675().internalMethod04496();
      boolean localValue2 = localValue1
         && !internalField0149.getWindow().isFullscreen()
         && !internalField0415.internalMethod02343()
         && (internalField0415.internalMethod02338() || internalField0415.internalMethod08566());
      if (localValue1 && !localValue2 && !internalField1100) {
         internalField1100 = true;
         RockstarClient.internalField0572
            .warn(
               "[CaptureBypass] \u043f\u0440\u0438\u0432\u0430\u0442\u043d\u044b\u0439 \u0441\u043b\u043e\u0439 \u043d\u0435\u0434\u043e\u0441\u0442\u0443\u043f\u0435\u043d: {}",
               internalField0149.getWindow().isFullscreen()
                  ? "\u0438\u0433\u0440\u0430 \u0432 \u043f\u043e\u043b\u043d\u043e\u044d\u043a\u0440\u0430\u043d\u043d\u043e\u043c \u0440\u0435\u0436\u0438\u043c\u0435"
                  : "\u0441\u0438\u0441\u0442\u0435\u043c\u0430 \u043d\u0435 \u0443\u043c\u0435\u0435\u0442 \u0432\u044b\u0440\u0435\u0437\u0430\u0442\u044c \u043e\u043a\u043d\u0430 \u0438\u0437 \u0437\u0430\u0445\u0432\u0430\u0442\u0430 (\u043d\u0443\u0436\u043d\u0430 Windows 10 2004+)"
            );
      }

      if (localValue2) {
         internalField1100 = false;
      }

      if (localValue2 != internalField0277) {
         internalField0277 = localValue2;
         if (!localValue2) {
            internalField0415.internalMethod02337();
         }

         if (internalField0149.inGameHud != null) {
            internalField0149.inGameHud.getChatHud().reset();
         }
      }
   }

   public static void internalMethod08006() {
      if (!internalField0276) {
         if (!internalField0415.internalMethod02343()) {
            internalField0415.internalMethod02337();
         }
      } else {
         internalField0276 = false;

         try {
            internalMethod08020();
            if (internalField1054 == 0) {
               if (++internalField1470 > 30) {
                  internalField0415.internalMethod02337();
                  return;
               }
            } else {
               internalField1470 = 0;
               internalMethod08022();
            }

            Framebuffer localValue0 = internalField0149.getFramebuffer();
            internalField0415.internalMethod05460(FramebufferCompat.glId(internalField0769.getColorAttachment()), internalField0618, internalField1054, localValue0.textureWidth, localValue0.textureHeight);
         } finally {
            GlStateManager._glBindFramebuffer(36160, 0);
         }
      }
   }

   public static Immediate internalMethod04244() {
      if (internalField0729 == null) {
         internalField0729 = VertexConsumerProvider.immediate(new BufferAllocator(1536));
      }

      return internalField0729;
   }

   public static int[] internalMethod01605(Matrix4f localValue0, float localValue1, float localValue2, float localValue3) {
      return internalMethod04696(localValue0, localValue1 - 1.0F, localValue2 - 1.0F, localValue1 + localValue3 + 2.0F, localValue2 + 10.0F);
   }

   public static boolean internalMethod04612(PostProcessRenderer.InternalType0212 localValue0) {
      if (internalMethod08007() && internalField0416.size() < 256) {
         internalField0416.add(new PostProcessRenderer.InternalType0211(localValue0, internalMethod00950()));
         return true;
      } else {
         return false;
      }
   }

   public static void internalMethod01672(int localValue0) {
      if (!internalField0416.isEmpty()) {
         ArrayList localValue1 = new ArrayList<>(internalField0416);
         internalField0416.clear();
         internalField1464 = localValue0;
         internalMethod07412(localValue1, PostProcessRenderer.InternalType0211::internalMethod06017);
         internalMethod07412(localValue1, PostProcessRenderer.InternalType0211::internalMethod06021);
         internalMethod07412(localValue1, PostProcessRenderer.InternalType0211::internalMethod08777);
      }
   }

   private static void internalMethod07412(List<PostProcessRenderer.InternalType0211> localValue0, Consumer<PostProcessRenderer.InternalType0211> localValue1) {
      for (PostProcessRenderer.InternalType0211 localValue3 : localValue0) {
         try {
            localValue1.accept(localValue3);
         } catch (Throwable localValue5) {
            RockstarClient.internalField0572
               .error(
                  "[CaptureBypass] \u043e\u0442\u043b\u043e\u0436\u0435\u043d\u043d\u0430\u044f \u0437\u0430\u043f\u043b\u0430\u0442\u043a\u0430 \u043d\u0435 \u043b\u0435\u0433\u043b\u0430",
                  localValue5
               );
         }
      }
   }

   private static void internalMethod08020() {
      boolean localValue0 = internalField0149.getOverlay() != null || internalField0149.currentScreen != null && !(internalField0149.currentScreen instanceof ChatScreen);
      if (localValue0) {
         int localValue1 = 0;

         for (int localValue2 = 0; localValue2 < internalField1054; localValue2++) {
            if (internalField0617[localValue2] == 2) {
               System.arraycopy(internalField0618, localValue2 * 4, internalField0618, localValue1 * 4, 4);
               internalField0617[localValue1] = internalField0617[localValue2];
               localValue1++;
            }
         }

         internalField1054 = localValue1;
      }
   }

   public static void internalMethod01602(int[] localValue0, Runnable localValue1) {
      if (localValue0 != null && internalMethod08007() && internalField1054 < 256) {
         Framebuffer localValue2 = internalField0149.getFramebuffer();
         boolean localValue3 = GL11.glIsEnabled(3089);
         int[] localValue4 = new int[4];
         GL11.glGetIntegerv(3088, localValue4);
         internalField1099 = true;

         try {
            UiBatchRenderer.internalMethod02576();
            internalMethod06560(localValue0);
            internalField0769.beginWrite(false);
            GlStateManager._enableScissorTest();
            GlStateManager._scissorBox(localValue0[0], localValue0[1], localValue0[2], localValue0[3]);
            localValue1.run();
            int localValue5 = internalField1054 * 4;
            internalField0618[localValue5] = localValue0[0];
            internalField0618[localValue5 + 1] = localValue0[1];
            internalField0618[localValue5 + 2] = localValue0[2];
            internalField0618[localValue5 + 3] = localValue0[3];
            internalField0617[internalField1054] = internalField1101 ? 2 : 1;
            internalField1054++;
         } catch (Throwable localValue9) {
            RockstarClient.internalField0572
               .error("[CaptureBypass] \u0437\u0430\u043f\u043b\u0430\u0442\u043a\u0430 \u043d\u0435 \u043b\u0435\u0433\u043b\u0430", localValue9);
         } finally {
            rockstar.client.render.FramebufferCompat.beginWrite(localValue2, false);
            if (localValue3) {
               GlStateManager._enableScissorTest();
               GlStateManager._scissorBox(localValue4[0], localValue4[1], localValue4[2], localValue4[3]);
            } else {
               GlStateManager._disableScissorTest();
            }

            internalField1099 = false;
         }
      }
   }

   static void internalMethod06560(int[] localValue0) {
      Framebuffer localValue1 = internalField0149.getFramebuffer();
      var localValue2 = com.mojang.blaze3d.systems.RenderSystem.getDevice().createCommandEncoder();
      localValue2.copyTextureToTexture(
         localValue1.getColorAttachment(), internalField0769.getColorAttachment(), 0,
         localValue0[0], localValue0[1], localValue0[0], localValue0[1], localValue0[2], localValue0[3]
      );
      if (localValue1.getDepthAttachment() != null && internalField0769.getDepthAttachment() != null) {
         localValue2.copyTextureToTexture(
            localValue1.getDepthAttachment(), internalField0769.getDepthAttachment(), 0,
            localValue0[0], localValue0[1], localValue0[0], localValue0[1], localValue0[2], localValue0[3]
         );
      }
   }

   public static String internalMethod03546(String localValue0) {
      if (internalMethod08007() && localValue0 != null && !localValue0.isEmpty()) {
         NameProtectModule localValue1 = internalMethod03771();
         if (localValue1 != null && localValue1.internalMethod02621(localValue0)) {
            String localValue2 = localValue1.internalMethod04954(localValue0);
            return localValue2.equals(localValue0) ? localValue0 : localValue2;
         } else {
            return localValue0;
         }
      } else {
         return localValue0;
      }
   }

   public static int[] internalMethod06487(int[] localValue0, int[] localValue1) {
      if (localValue0 == null) {
         return localValue1;
      } else if (localValue1 == null) {
         return localValue0;
      } else {
         int localValue2 = Math.min(localValue0[0], localValue1[0]);
         int localValue3 = Math.min(localValue0[1], localValue1[1]);
         int localValue4 = Math.max(localValue0[0] + localValue0[2], localValue1[0] + localValue1[2]);
         int localValue5 = Math.max(localValue0[1] + localValue0[3], localValue1[1] + localValue1[3]);
         return new int[]{localValue2, localValue3, localValue4 - localValue2, localValue5 - localValue3};
      }
   }

   private static void internalMethod08022() {
      internalField0769.beginWrite(false);
      RenderSystem.colorMask(false, false, false, true);
      RenderSystem.clearColor(0.0F, 0.0F, 0.0F, 1.0F);

      for (int localValue0 = 0; localValue0 < internalField1054; localValue0++) {
         int localValue1 = localValue0 * 4;
         RenderSystem.enableScissor(internalField0618[localValue1], internalField0618[localValue1 + 1], internalField0618[localValue1 + 2], internalField0618[localValue1 + 3]);
         RenderSystem.clear(16384);
      }

      RenderSystem.disableScissor();
      RenderSystem.colorMask(true, true, true, true);
      rockstar.client.render.FramebufferCompat.beginWrite(internalField0149.getFramebuffer(), false);
   }

   public static int[] internalMethod04696(Matrix4f localValue0, float localValue1, float localValue2, float localValue3, float localValue4) {
      Framebuffer localValue5 = internalField0149.getFramebuffer();
      int localValue6 = localValue5.textureWidth;
      int localValue7 = localValue5.textureHeight;
      if (localValue6 > 0 && localValue7 > 0) {
         Matrix4f localValue8 = RenderSystem.getModelViewMatrix();
         Matrix4f localValue9 = RenderSystem.getProjectionMatrix();
         float localValue10 = Float.MAX_VALUE;
         float localValue11 = Float.MAX_VALUE;
         float localValue12 = -Float.MAX_VALUE;
         float localValue13 = -Float.MAX_VALUE;

         for (int localValue14 = 0; localValue14 < 4; localValue14++) {
            Vector4f localValue15 = new Vector4f((localValue14 & 1) == 0 ? localValue1 : localValue3, (localValue14 & 2) == 0 ? localValue2 : localValue4, 0.0F, 1.0F);
            localValue0.transform(localValue15);
            localValue8.transform(localValue15);
            localValue9.transform(localValue15);
            if (localValue15.w <= 1.0E-5F) {
               return null;
            }

            float localValue16 = (localValue15.x / localValue15.w * 0.5F + 0.5F) * localValue6;
            float localValue17 = (localValue15.y / localValue15.w * 0.5F + 0.5F) * localValue7;
            localValue10 = Math.min(localValue10, localValue16);
            localValue11 = Math.min(localValue11, localValue17);
            localValue12 = Math.max(localValue12, localValue16);
            localValue13 = Math.max(localValue13, localValue17);
         }

         int localValue19 = Math.max(0, (int)Math.floor(localValue10));
         int localValue20 = Math.max(0, (int)Math.floor(localValue11));
         int localValue21 = Math.min(localValue6, (int)Math.ceil(localValue12));
         int localValue22 = Math.min(localValue7, (int)Math.ceil(localValue13));
         if (GL11.glIsEnabled(3089)) {
            int[] localValue18 = new int[4];
            GL11.glGetIntegerv(3088, localValue18);
            localValue19 = Math.max(localValue19, localValue18[0]);
            localValue20 = Math.max(localValue20, localValue18[1]);
            localValue21 = Math.min(localValue21, localValue18[0] + localValue18[2]);
            localValue22 = Math.min(localValue22, localValue18[1] + localValue18[3]);
         }

         return localValue21 > localValue19 && localValue22 > localValue20 ? new int[]{localValue19, localValue20, localValue21 - localValue19, localValue22 - localValue20} : null;
      } else {
         return null;
      }
   }

   private static NameProtectModule internalMethod03771() {
      try {
         return RockstarClient.getInstance().getModuleManager().getModule(NameProtectModule.class);
      } catch (Throwable localValue1) {
         return null;
      }
   }

   private static PostProcessRenderer.InternalType0014 internalMethod00950() {
      boolean localValue0 = GL11.glIsEnabled(3089);
      int[] localValue1 = new int[4];
      GL11.glGetIntegerv(3088, localValue1);
      int[] localValue2 = new int[4];
      GL11.glGetIntegerv(2978, localValue2);
      return new PostProcessRenderer.InternalType0014(
         new Matrix4f(RenderSystem.getProjectionMatrix()), RenderSystem.getProjectionType(), new Matrix4f(RenderSystem.getModelViewMatrix()), localValue0, localValue1, localValue2
      );
   }

   static final class InternalType0014 {
      private final Matrix4f internalField0788;
      private final ProjectionType internalField0356;
      private final Matrix4f internalField0787;
      private final boolean internalField0277;
      private final int[] internalField0618;
      private final int[] internalField0617;

      InternalType0014(Matrix4f localValue1, ProjectionType localValue2, Matrix4f localValue3, boolean localValue4, int[] localValue5, int[] localValue6) {
         this.internalField0788 = localValue1;
         this.internalField0356 = localValue2;
         this.internalField0787 = localValue3;
         this.internalField0277 = localValue4;
         this.internalField0618 = localValue5;
         this.internalField0617 = localValue6;
      }

      @Override
      public final String toString() {
         return "InternalType0014[projection=" + this.internalField0788 + ", projectionType=" + this.internalField0356 + ", modelView=" + this.internalField0787 + ", scissorEnabled=" + this.internalField0277 + ", scissorBox=" + this.internalField0618 + ", viewport=" + this.internalField0617 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0788);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0356);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0787);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0277);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0618);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0617);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         PostProcessRenderer.InternalType0014 other = (PostProcessRenderer.InternalType0014) localValue1;
         return java.util.Objects.equals(this.internalField0788, other.internalField0788)
            && java.util.Objects.equals(this.internalField0356, other.internalField0356)
            && java.util.Objects.equals(this.internalField0787, other.internalField0787)
            && java.util.Objects.equals(this.internalField0277, other.internalField0277)
            && java.util.Objects.equals(this.internalField0618, other.internalField0618)
            && java.util.Objects.equals(this.internalField0617, other.internalField0617);
      }

      public Matrix4f internalMethod01091() {
         return this.internalField0788;
      }

      public ProjectionType internalMethod05230() {
         return this.internalField0356;
      }

      public Matrix4f internalMethod06233() {
         return this.internalField0787;
      }

      public boolean internalMethod06975() {
         return this.internalField0277;
      }

      public int[] internalMethod02998() {
         return this.internalField0618;
      }

      public int[] internalMethod03050() {
         return this.internalField0617;
      }
   }

   static final class InternalType0211 {
      private final PostProcessRenderer.InternalType0212 internalField0521;
      private final PostProcessRenderer.InternalType0014 internalField0503;
      private int[] internalField0618;
      private Matrix4f internalField0788;
      private ProjectionType internalField0356;
      private final int[] internalField0617 = new int[4];

      InternalType0211(PostProcessRenderer.InternalType0212 localValue1, PostProcessRenderer.InternalType0014 localValue2) {
         this.internalField0521 = localValue1;
         this.internalField0503 = localValue2;
      }

      void internalMethod06017() {
         if (PostProcessRenderer.internalField0276) {
            this.internalMethod08778();

            try {
               this.internalField0618 = this.internalField0521.internalMethod02439();
               if (this.internalField0618 != null && PostProcessRenderer.internalField1054 < 256) {
                  PostProcessRenderer.internalMethod06560(this.internalField0618);
                  int localValue1 = PostProcessRenderer.internalField1054 * 4;
                  PostProcessRenderer.internalField0618[localValue1] = this.internalField0618[0];
                  PostProcessRenderer.internalField0618[localValue1 + 1] = this.internalField0618[1];
                  PostProcessRenderer.internalField0618[localValue1 + 2] = this.internalField0618[2];
                  PostProcessRenderer.internalField0618[localValue1 + 3] = this.internalField0618[3];
                  PostProcessRenderer.internalField0617[PostProcessRenderer.internalField1054] = PostProcessRenderer.internalField1464;
                  PostProcessRenderer.internalField1054++;
                  return;
               }

               this.internalField0618 = null;
            } finally {
               this.internalMethod08786();
            }
         }
      }

      void internalMethod06021() {
         if (this.internalField0618 != null) {
            this.internalMethod08778();
            PostProcessRenderer.internalField1099 = true;

            try {
               PostProcessRenderer.internalField0769.beginWrite(false);
               GlStateManager._enableScissorTest();
               GlStateManager._scissorBox(this.internalField0618[0], this.internalField0618[1], this.internalField0618[2], this.internalField0618[3]);
               this.internalField0521.internalMethod01050();
            } finally {
               rockstar.client.render.FramebufferCompat.beginWrite(MinecraftClientAccess.internalField0149.getFramebuffer(), false);
               PostProcessRenderer.internalField1099 = false;
               this.internalMethod08786();
            }
         }
      }

      void internalMethod08777() {
         this.internalMethod08778();

         try {
            this.internalField0521.internalMethod01085();
         } finally {
            this.internalMethod08786();
         }
      }

      private void internalMethod08778() {
         this.internalField0788 = RenderSystem.getProjectionMatrix();
         this.internalField0356 = RenderSystem.getProjectionType();
         RenderSystem.setProjectionMatrix(this.internalField0503.internalMethod01091(), this.internalField0503.internalMethod05230());
         GL11.glGetIntegerv(2978, this.internalField0617);
         int[] localValue1 = this.internalField0503.internalMethod03050();
         if (!Arrays.equals(this.internalField0617, localValue1)) {
            RenderSystem.viewport(localValue1[0], localValue1[1], localValue1[2], localValue1[3]);
            if (PostProcessRenderer.internalField1463 < 3) {
               PostProcessRenderer.internalField1463++;
               RockstarClient.internalField0572
                  .warn(
                     "[CaptureBypass] \u043e\u0431\u043b\u0430\u0441\u0442\u044c \u0432\u044b\u0432\u043e\u0434\u0430 \u0440\u0430\u0437\u044a\u0435\u0445\u0430\u043b\u0430\u0441\u044c: \u0431\u044b\u043b\u043e {}, \u0441\u0442\u0430\u043b\u043e {}",
                     Arrays.toString(localValue1),
                     Arrays.toString(this.internalField0617)
                  );
            }
         }

         Matrix4fStack localValue2 = RenderSystem.getModelViewStack();
         localValue2.pushMatrix();
         localValue2.set(this.internalField0503.internalMethod06233());
         if (this.internalField0503.internalMethod06975()) {
            GlStateManager._enableScissorTest();
            GlStateManager._scissorBox(
               this.internalField0503.internalMethod02998()[0],
               this.internalField0503.internalMethod02998()[1],
               this.internalField0503.internalMethod02998()[2],
               this.internalField0503.internalMethod02998()[3]
            );
         } else {
            GlStateManager._disableScissorTest();
         }
      }

      private void internalMethod08786() {
         GlStateManager._disableScissorTest();
         RenderSystem.getModelViewStack().popMatrix();
         RenderSystem.setProjectionMatrix(this.internalField0788, this.internalField0356);
         RenderSystem.viewport(this.internalField0617[0], this.internalField0617[1], this.internalField0617[2], this.internalField0617[3]);
      }
   }

   public interface InternalType0212 {
      int[] internalMethod02439();

      void internalMethod01050();

      void internalMethod01085();
   }
}
