package rockstar.client.internal.ui;


import rockstar.client.*;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import moscow.rockstar.mixin.accessors.MinecraftClientAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.gui.render.GuiRenderer;
import net.minecraft.client.gui.render.state.GuiRenderState;
import net.minecraft.client.render.OutlineVertexConsumerProvider;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import net.minecraft.client.render.command.OrderedRenderCommandQueueImpl;
import net.minecraft.client.render.command.RenderDispatcher;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.BufferAllocator;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import java.util.List;

public final class InventoryScreenshotRenderer {
   private static final int internalField0227 = 176;
   private static final int internalField0228 = 166;
   private InventoryScreenshotRenderer() {
   }

   public static byte[] internalMethod05830(ClientPlayerEntity localValue0) throws IOException {
      MinecraftClient localValue1 = MinecraftClient.getInstance();
      short localValue2 = 528;
      short localValue3 = 498;
      SimpleFramebuffer localValue4 = new SimpleFramebuffer("Rockstar inventory capture", localValue2, localValue3, true);
      NativeImage localValue5 = null;

      byte[] localValue25;
      try {
         rockstar.client.render.FramebufferCompat.clear(
            localValue4,
            rockstar.client.render.FramebufferCompat.color(0.0F, 0.0F, 0.0F, 1.0F)
         );
         rockstar.client.render.FramebufferCompat.beginWrite(localValue4, true);
         Framebuffer localValue7 = localValue1.getFramebuffer();
         ((MinecraftClientAccessor)(Object)localValue1).setFramebuffer(localValue4);

         try {
            BufferAllocator localValue8 = new BufferAllocator(786432);

            try {
               Immediate localValue9 = VertexConsumerProvider.immediate(localValue8);
               GuiRenderState localValue10 = new GuiRenderState();
               OrderedRenderCommandQueueImpl localValue11 = new OrderedRenderCommandQueueImpl();
               RenderDispatcher localValue12 = new RenderDispatcher(
                  localValue11,
                  localValue1.getBlockRenderManager(),
                  localValue9,
                  localValue1.getAtlasManager(),
                  new OutlineVertexConsumerProvider(),
                  localValue9,
                  localValue1.textRenderer
               );

               try (GuiRenderer localValue13 = new GuiRenderer(localValue10, localValue9, localValue11, localValue12, List.of())) {
                  DrawContext localValue14 = new DrawContext(localValue1, localValue10, 0, 0);
                  localValue14.getMatrices().pushMatrix();
                  localValue14.getMatrices().scale(
                     (float)localValue1.getWindow().getScaledWidth() / internalField0227,
                     (float)localValue1.getWindow().getScaledHeight() / internalField0228
                  );
                  internalMethod06237(localValue14, localValue1, localValue0.getInventory());
                  localValue14.getMatrices().popMatrix();
                  localValue13.render(RenderSystem.getShaderFog());
                  localValue11.onNextFrame();
                  localValue12.endLayeredCustoms();
               } finally {
                  localValue12.close();
               }
            } catch (Throwable localValue22) {
               try {
                  localValue8.close();
               } catch (Throwable localValue21) {
                  localValue22.addSuppressed(localValue21);
               }

               throw localValue22;
            }

            localValue8.close();
         } finally {
            ((MinecraftClientAccessor)(Object)localValue1).setFramebuffer(localValue7);
            rockstar.client.render.FramebufferCompat.beginWrite(localValue7, false);
         }

         localValue5 = internalMethod09272(localValue4);
         localValue25 = internalMethod01008(localValue5);
      } finally {
         if (localValue5 != null) {
            localValue5.close();
         }

         localValue4.delete();
      }

      return localValue25;
   }

   /** Reads the GPU framebuffer before it is released; the screenshot API is asynchronous in 1.21.11. */
   private static NativeImage internalMethod09272(Framebuffer localValue0) {
      GpuTexture localValue1 = localValue0.getColorAttachment();
      if (localValue1 == null) {
         throw new IllegalStateException("Tried to capture an incomplete Rockstar inventory framebuffer");
      }

      int localValue2 = localValue0.textureWidth;
      int localValue3 = localValue0.textureHeight;
      int localValue4 = localValue1.getFormat().pixelSize();
      CommandEncoder localValue5 = RenderSystem.getDevice().createCommandEncoder();
      try (GpuBuffer localValue6 = RenderSystem.getDevice().createBuffer(
         () -> "Rockstar inventory screenshot buffer",
         GpuBuffer.USAGE_MAP_READ | GpuBuffer.USAGE_COPY_DST,
         (long)localValue2 * localValue3 * localValue4
      )) {
         localValue5.copyTextureToBuffer(localValue1, localValue6, 0L, () -> {}, 0);
         try (GpuBuffer.MappedView localValue7 = localValue5.mapBuffer(localValue6, true, false)) {
            NativeImage localValue8 = new NativeImage(localValue2, localValue3, false);
            for (int localValue9 = 0; localValue9 < localValue3; localValue9++) {
               for (int localValue10 = 0; localValue10 < localValue2; localValue10++) {
                  int localValue11 = localValue7.data().getInt((localValue10 + localValue9 * localValue2) * localValue4);
                  localValue8.setColor(localValue10, localValue3 - localValue9 - 1, localValue11 | 0xFF000000);
               }
            }
            return localValue8;
         }
      }
   }

   private static void internalMethod06237(DrawContext localValue0, MinecraftClient localValue1, PlayerInventory localValue2) {
      localValue0.drawTexture(net.minecraft.client.gl.RenderPipelines.GUI_TEXTURED, HandledScreen.BACKGROUND_TEXTURE, 0, 0, 0.0F, 0.0F, 176, 166, 256, 256);

      for (int localValue3 = 0; localValue3 < localValue2.getMainStacks().size(); localValue3++) {
         boolean localValue4 = localValue3 < 9;
         int localValue5 = localValue4 ? localValue3 : (localValue3 - 9) % 9;
         int localValue6 = localValue4 ? 0 : (localValue3 - 9) / 9;
         int localValue7 = 8 + localValue5 * 18;
         int localValue8 = localValue4 ? 142 : 84 + localValue6 * 18;
         internalMethod05231(localValue0, localValue1, (ItemStack)localValue2.getMainStacks().get(localValue3), localValue7, localValue8);
      }

      for (int localValue9 = 0; localValue9 < rockstar.client.util.LegacyItemTypes.armorItems(localValue1.player).size(); localValue9++) {
         ItemStack localValue10 = rockstar.client.util.LegacyItemTypes.armorItems(localValue1.player).get(3 - localValue9);
         internalMethod05231(localValue0, localValue1, localValue10, 8, 8 + localValue9 * 18);
      }

      if (!localValue1.player.getOffHandStack().isEmpty()) {
         internalMethod05231(localValue0, localValue1, localValue1.player.getOffHandStack(), 77, 62);
      }
   }

   private static void internalMethod05231(DrawContext localValue0, MinecraftClient localValue1, ItemStack localValue2, int localValue3, int localValue4) {
      if (localValue2 != null && !localValue2.isEmpty()) {
         localValue0.drawItem(localValue2, localValue3, localValue4);
         localValue0.drawStackOverlay(localValue1.textRenderer, localValue2, localValue3, localValue4);
      }
   }

   private static byte[] internalMethod01008(NativeImage localValue0) throws IOException {
      BufferedImage localValue1 = new BufferedImage(localValue0.getWidth(), localValue0.getHeight(), 1);

      for (int localValue2 = 0; localValue2 < localValue0.getHeight(); localValue2++) {
         for (int localValue3 = 0; localValue3 < localValue0.getWidth(); localValue3++) {
            localValue1.setRGB(localValue3, localValue2, localValue0.getColorArgb(localValue3, localValue2));
         }
      }

      ByteArrayOutputStream localValue4 = new ByteArrayOutputStream(65536);
      if (!ImageIO.write(localValue1, "png", localValue4)) {
         throw new IOException("PNG encoder not available");
      } else {
         return localValue4.toByteArray();
      }
   }
}
