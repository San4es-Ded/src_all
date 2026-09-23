package wtf.wyvern.client.modules.impl.render;

import wtf.wyvern.core.eventbus.EventTarget;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.OutlineVertexConsumerProvider;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import wtf.wyvern.Wyvern;
import wtf.wyvern.core.events.impl.render.EventRender3D;
import wtf.wyvern.core.events.impl.server.EventPacket;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.SliderSetting;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(name = "GhostPlayer", category = Category.RENDER, description = "Показывает призрак игрока при тотеме")
public final class GhostPlayer extends Module {
   public static final GhostPlayer INSTANCE = new GhostPlayer();
   private final SliderSetting duration = new SliderSetting("Длительность", 2000F, 250F, 5000F, 50F);
   private final SliderSetting rise = new SliderSetting("Подъём", 2F, 0F, 6F, 0.1F);
   private final SliderSetting chamsOpacity = new SliderSetting("Прозрачность чамса", 0.34F, 0.05F, 0.85F, 0.05F);
   private final List<Entry> ghosts = new ArrayList<>();

   @FastNative
   @EventTarget
   public void onPacket(EventPacket event) {
      if (!event.isReceive() || mc.world == null || !(event.getPacket() instanceof EntityStatusS2CPacket packet) || packet.getStatus() != 35) return;
      if (packet.getEntity(mc.world) instanceof PlayerEntity player && player != mc.player) ghosts.add(new Entry(player, mc.world));
   }

   @EventTarget
   public void onRender(EventRender3D event) {
      if (ghosts.isEmpty()) return;
      long now = System.currentTimeMillis();
      long life = (long)duration.getCurrent();
      Vec3d camera = mc.gameRenderer.getCamera().getPos();
      EntityRenderDispatcher dispatcher = mc.getEntityRenderDispatcher();
      VertexConsumerProvider.Immediate buffers = mc.getBufferBuilders().getEntityVertexConsumers();
      OutlineVertexConsumerProvider outlineBuffers = new OutlineVertexConsumerProvider(buffers);
      BufferBuilder glowBuffer = Tessellator.getInstance().begin(
              VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
      int themeColor = Wyvern.getInstance().getThemeManager().getCurrentTheme().getColor().getRGB();
      int red = themeColor >> 16 & 255;
      int green = themeColor >> 8 & 255;
      int blue = themeColor & 255;

      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.disableDepthTest();
      RenderSystem.depthMask(false);
      Iterator<Entry> it = ghosts.iterator();
      while (it.hasNext()) {
         Entry entry = it.next();
         float progress = (now - entry.createdAt) / (float)life;
         if (progress >= 1F) { it.remove(); continue; }
         float easedProgress = 1.0F - (1.0F - progress) * (1.0F - progress);
         // Keep the rising chams subtle from the first frame, then fade it out completely.
         float alpha = chamsOpacity.getCurrent() * (1.0F - progress * progress);
         double x = entry.x - camera.x;
         double y = entry.y + rise.getCurrent() * easedProgress - camera.y;
         double z = entry.z - camera.z;

         // Первый проход оставляет полупрозрачный силуэт, второй добавляет цветной контур chams.
         // Three fading passes create a compact multi-image after-trail.
         for (int copyIndex = 0; copyIndex < 3; copyIndex++) {
            float copyAlpha = alpha * (1.0F - copyIndex * 0.24F);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, copyAlpha);
            dispatcher.render(entry.player, x, y - copyIndex * 0.075D, z,
                    event.getPartialTicks(), event.getMatrix(), buffers, 0xF000F0);
            outlineBuffers.setColor(red, green, blue, Math.max(12, (int)(220.0F * copyAlpha)));
            dispatcher.render(entry.player, x, y - copyIndex * 0.075D, z,
                    event.getPartialTicks(), event.getMatrix(), outlineBuffers, 0xF000F0);
            addGlowQuad(glowBuffer, event.getMatrix(), camera, new Vec3d(entry.x - camera.x,
                            entry.y + rise.getCurrent() * easedProgress + 0.9D - camera.y, entry.z - camera.z),
                    0.82F + copyAlpha * 0.32F, themeColor, Math.min(0.9F, copyAlpha * 1.85F));
         }
      }
      buffers.draw();
      outlineBuffers.draw();
      RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
      RenderSystem.setShaderTexture(0, Identifier.of("wyvern", "icons/glow.png"));
      BufferRenderer.drawWithGlobalProgram(glowBuffer.end());
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.depthMask(true);
      RenderSystem.enableDepthTest();
      RenderSystem.disableBlend();
   }

   @FastNative
   @Override
   public void onDisable() { ghosts.clear(); super.onDisable(); }

   private static void addGlowQuad(BufferBuilder buffer, MatrixStack matrices, Vec3d camera,
                                   Vec3d position, float size, int color, float alpha) {
      int a = Math.max(0, Math.min(255, (int) (alpha * 255.0F)));
      int argb = (a << 24) | (color & 0x00FFFFFF);
      matrices.push();
      matrices.translate(position.x, position.y, position.z);
      matrices.multiply(MinecraftClient.getInstance().gameRenderer.getCamera().getRotation());
      Matrix4f matrix = matrices.peek().getPositionMatrix();
      float h = size * 0.5F;
      buffer.vertex(matrix, -h, h, 0).texture(0, 1).color(argb);
      buffer.vertex(matrix, h, h, 0).texture(1, 1).color(argb);
      buffer.vertex(matrix, h, -h, 0).texture(1, 0).color(argb);
      buffer.vertex(matrix, -h, -h, 0).texture(0, 0).color(argb);
      matrices.pop();
   }

   private static final class Entry {
      private final OtherClientPlayerEntity player;
      private final double x, y, z;
      private final long createdAt = System.currentTimeMillis();

      private Entry(PlayerEntity source, ClientWorld world) {
         this.player = new OtherClientPlayerEntity(world, source.getGameProfile()) {
            @Override
            public boolean isPartVisible(PlayerModelPart modelPart) {
               // No hat, jacket, sleeves, trouser overlays or cape on the ghost copy.
               return false;
            }
         };
         this.x = source.getX();
         this.y = source.getY();
         this.z = source.getZ();

         // Это отдельная копия игрока: она никогда не тикается, поэтому поза,
         // направление взгляда и конечности остаются такими, какими были при попе.
         player.copyPositionAndRotation(source);
         player.setPose(source.getPose());
         player.setYaw(source.getYaw());
         player.prevYaw = source.getYaw();
         player.setPitch(source.getPitch());
         player.prevPitch = source.getPitch();
         player.setBodyYaw(source.getBodyYaw());
         player.prevBodyYaw = source.getBodyYaw();
         player.setHeadYaw(source.getHeadYaw());
         player.prevHeadYaw = source.getHeadYaw();
         player.limbAnimator.reset();
         player.handSwinging = false;
         player.handSwingProgress = 0.0F;
         player.lastHandSwingProgress = 0.0F;
         // The ghost is a bare player silhouette: no armor/elytra and nothing in either hand.
         for (EquipmentSlot slot : EquipmentSlot.values()) {
            player.equipStack(slot, ItemStack.EMPTY);
         }
      }
   }
}
