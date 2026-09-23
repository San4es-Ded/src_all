package wtf.wyvern.client.gui.screens.autoswap;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix4f;
import wtf.wyvern.Wyvern;
import wtf.wyvern.client.modules.impl.combat.AutoSwap;

/** Minimal three-sector wheel used by AutoSwap's triple mode. */
public final class AutoSwapWheelScreen extends Screen {
   private static final float INNER_RADIUS = 68.0F;
   private static final float OUTER_RADIUS = 104.0F;
   private static final float SECTOR_SIZE = 120.0F;
   private static final long OPEN_ANIMATION_MS = 170L;
   private final AutoSwap autoSwap;
   private final int holdKey;
   private final long openedAt = System.currentTimeMillis();
   private int hoveredSector = -1;
   private boolean completed;

   public AutoSwapWheelScreen(AutoSwap autoSwap, int holdKey) {
      super(Text.empty());
      this.autoSwap = autoSwap;
      this.holdKey = holdKey;
   }

   @Override
   public boolean shouldPause() { return false; }

   @Override
   public void tick() {
      if (!completed && (client == null || !InputUtil.isKeyPressed(client.getWindow().getHandle(), holdKey))) {
         selectHoveredSlot();
      }
   }

   @Override
   public void render(DrawContext context, int mouseX, int mouseY, float delta) {
      float centerX = this.width / 2.0F;
      float centerY = this.height / 2.0F;
      float progress = openProgress();
      float innerRadius = INNER_RADIUS * progress;
      float outerRadius = OUTER_RADIUS * progress;
      hoveredSector = sectorAt(mouseX, mouseY, centerX, centerY, innerRadius, outerRadius);
      context.fill(0, 0, width, height, ((int)(115.0F * progress) << 24));

      drawDisk(context, centerX, centerY, innerRadius, ((int)(112.0F * progress) << 24) | 0x101318);
      drawRingSector(context, centerX, centerY, innerRadius, outerRadius, -150.0F, 210.0F,
         ((int)(142.0F * progress) << 24) | 0x181A20);

      if (hoveredSector >= 0) {
         float start = -150.0F + hoveredSector * SECTOR_SIZE;
         int color = Wyvern.getInstance().getThemeManager().getCurrentTheme().getColor()
            .withAlpha((int)(195.0F * progress)).getRGB();
         drawRingSector(context, centerX, centerY, innerRadius, outerRadius, start, start + SECTOR_SIZE, color);
      }

      float iconRadius = (innerRadius + outerRadius) * 0.5F;
      for (int i = 0; i < 3; i++) {
         float angle = -90.0F + i * SECTOR_SIZE;
         int iconX = Math.round(centerX + MathHelper.cos((float)Math.toRadians(angle)) * iconRadius) - 8;
         int iconY = Math.round(centerY + MathHelper.sin((float)Math.toRadians(angle)) * iconRadius) - 8;
         ItemStack stack = autoSwap.getWheelSlotStack(i);
         if (!stack.isEmpty()) context.drawItem(stack, iconX, iconY);
      }
   }

   @Override
   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      float progress = openProgress();
      int slot = sectorAt(mouseX, mouseY, this.width / 2.0F, this.height / 2.0F, INNER_RADIUS * progress, OUTER_RADIUS * progress);
      if (slot < 0) return super.mouseClicked(mouseX, mouseY, button);
      if (button == 1) {
         client.setScreen(new AutoSwapItemSelectScreen(autoSwap, slot, holdKey));
      }
      return true;
   }

   /** Called by AutoSwap when the bound key is released. */
   public void selectHoveredSlot() {
      if (completed) return;
      completed = true;
      int slot = hoveredSector;
      close();
      if (slot >= 0) autoSwap.swapToWheelSlot(slot);
   }

   private static int sectorAt(double mouseX, double mouseY, float centerX, float centerY, float innerRadius, float outerRadius) {
      float dx = (float)mouseX - centerX;
      float dy = (float)mouseY - centerY;
      float distance = MathHelper.sqrt(dx * dx + dy * dy);
      if (distance < innerRadius || distance > outerRadius) return -1;
      float angle = (float)Math.toDegrees(Math.atan2(dy, dx));
      float relative = angle + 150.0F;
      while (relative < 0.0F) relative += 360.0F;
      while (relative >= 360.0F) relative -= 360.0F;
      int sector = (int)(relative / SECTOR_SIZE);
      return sector >= 0 && sector < 3 ? sector : -1;
   }

   private float openProgress() {
      float t = MathHelper.clamp((System.currentTimeMillis() - openedAt) / (float)OPEN_ANIMATION_MS, 0.0F, 1.0F);
      return 1.0F - (float)Math.pow(1.0F - t, 3.0F);
   }

   private static void drawRingSector(DrawContext context, float centerX, float centerY, float innerRadius, float outerRadius, float startDegrees, float endDegrees, int color) {
      Matrix4f matrix = context.getMatrices().peek().getPositionMatrix();
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.disableCull();
      RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
      BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.TRIANGLES, VertexFormats.POSITION_COLOR);
      float start = (float)Math.toRadians(startDegrees);
      float step = (float)Math.toRadians(endDegrees - startDegrees) / 48.0F;
      for (int i = 0; i < 48; i++) {
         float a = start + step * i;
         float b = a + step;
         float outerAX = centerX + MathHelper.cos(a) * outerRadius;
         float outerAY = centerY + MathHelper.sin(a) * outerRadius;
         float outerBX = centerX + MathHelper.cos(b) * outerRadius;
         float outerBY = centerY + MathHelper.sin(b) * outerRadius;
         float innerAX = centerX + MathHelper.cos(a) * innerRadius;
         float innerAY = centerY + MathHelper.sin(a) * innerRadius;
         float innerBX = centerX + MathHelper.cos(b) * innerRadius;
         float innerBY = centerY + MathHelper.sin(b) * innerRadius;
         buffer.vertex(matrix, outerAX, outerAY, 0).color(color);
         buffer.vertex(matrix, outerBX, outerBY, 0).color(color);
         buffer.vertex(matrix, innerBX, innerBY, 0).color(color);
         buffer.vertex(matrix, outerAX, outerAY, 0).color(color);
         buffer.vertex(matrix, innerBX, innerBY, 0).color(color);
         buffer.vertex(matrix, innerAX, innerAY, 0).color(color);
      }
      BufferRenderer.drawWithGlobalProgram(buffer.end());
      RenderSystem.enableCull();
      RenderSystem.disableBlend();
   }

   private static void drawDisk(DrawContext context, float centerX, float centerY, float radius, int color) {
      Matrix4f matrix = context.getMatrices().peek().getPositionMatrix();
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.disableCull();
      RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
      BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.TRIANGLES, VertexFormats.POSITION_COLOR);
      for (int i = 0; i < 40; i++) {
         float a = (float)(Math.PI * 2.0 * i / 40.0);
         float b = (float)(Math.PI * 2.0 * (i + 1) / 40.0);
         buffer.vertex(matrix, centerX, centerY, 0).color(color);
         buffer.vertex(matrix, centerX + MathHelper.cos(a) * radius, centerY + MathHelper.sin(a) * radius, 0).color(color);
         buffer.vertex(matrix, centerX + MathHelper.cos(b) * radius, centerY + MathHelper.sin(b) * radius, 0).color(color);
      }
      BufferRenderer.drawWithGlobalProgram(buffer.end());
      RenderSystem.enableCull();
      RenderSystem.disableBlend();
   }

}
