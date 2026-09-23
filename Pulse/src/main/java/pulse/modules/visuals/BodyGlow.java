package pulse.modules.visuals;

import java.awt.Color;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import org.joml.Matrix4f;
import pulse.events.WorldRenderEvent;
import pulse.module.ClientModule;
import pulse.module.ModuleCategory;
import pulse.module.ModuleInfo;
import pulse.module.ModuleRegistry;
import pulse.render.system.ClientPipelines;
import pulse.settings.BooleanSetting;
import pulse.settings.ColorSetting;
import pulse.settings.SettingGroup;
import pulse.settings.SliderSetting;

@ModuleInfo(a = "Body Glow", b = "Рисует мягкое свечение ауры вокруг игроков", c = ModuleCategory.VISUALS)
public class BodyGlow extends ClientModule {
   private final SliderSetting size = new SliderSetting("Размер", 2.3F, 0.5F, 5.0F, 0.1F);
   private final SliderSetting transparency = new SliderSetting("Прозрачность", 1.0F, 0.0F, 1.0F, 0.05F);
   private final SettingGroup colorGroup = new SettingGroup("Цвет");
   private final BooleanSetting useClientColor = new BooleanSetting("Цвет клиента", true);
   private final ColorSetting customColor = new ColorSetting("Цвет", new Color(255, 255, 255)).a(() -> !this.useClientColor.a());

   private Color getColor() {
      return this.useClientColor.a() ? new Color(ModuleRegistry.CLIENT_COLOR.o()) : this.customColor.getColor();
   }

   @EventHandler
   public void onRenderWorld(WorldRenderEvent event) {
      if (c.player != null && c.world != null && c.gameRenderer != null) {
         Immediate bufferSource = event.bufferSource();
         if (bufferSource != null) {
            MatrixStack stack = event.matrices();
            Vec3d cam = c.gameRenderer.getCamera().getCameraPos();
            float tickDelta = event.tickDelta();
            Color baseColor = this.getColor();
            float opacity = MathHelper.clamp(this.transparency.a(), 0.0F, 1.0F);
            if (!(opacity <= 0.01F)) {
               float radiusVal = this.size.a() * 0.35F;
               VertexConsumer fill = bufferSource.getBuffer(ClientPipelines.FILL);
               int SEGMENTS = 36;
               double PI2 = Math.PI * 2;

               for (AbstractClientPlayerEntity player : c.world.getPlayers()) {
                  if (player != null && !player.isInvisible() && (player != c.player || !c.options.getPerspective().isFirstPerson())) {
                     double px = MathHelper.lerp(tickDelta, player.lastRenderX, player.getX()) - cam.x;
                     double py = MathHelper.lerp(tickDelta, player.lastRenderY, player.getY()) - cam.y;
                     double pz = MathHelper.lerp(tickDelta, player.lastRenderZ, player.getZ()) - cam.z;
                     float pHeight = player.getHeight();
                     if (player.isSneaking()) {
                        pHeight -= 0.18F;
                     }

                     stack.push();
                     try {
                        stack.translate(px, py + pHeight * 0.5F, pz);
                        stack.multiply(c.gameRenderer.getCamera().getRotation());
                        Matrix4f mat = stack.peek().getPositionMatrix();
                        float auraRadius = 0.8F + this.size.a() * 0.35F;
                        int centerAlpha = (int)(opacity * 200.0F);
                        int midAlpha = (int)(opacity * 100.0F);
                        int edgeAlpha = 0;
                        int centerArgb = centerAlpha << 24 | baseColor.getRed() << 16 | baseColor.getGreen() << 8 | baseColor.getBlue();
                        int midArgb = midAlpha << 24 | baseColor.getRed() << 16 | baseColor.getGreen() << 8 | baseColor.getBlue();
                        int edgeArgb = edgeAlpha << 24 | baseColor.getRed() << 16 | baseColor.getGreen() << 8 | baseColor.getBlue();
                        float rInner = auraRadius * 0.45F;
                        float rOuter = auraRadius;

                        for (int i = 0; i < SEGMENTS; i++) {
                           float a1 = (float)(i * PI2 / SEGMENTS);
                           float a2 = (float)((i + 1) * PI2 / SEGMENTS);
                           float x1In = -MathHelper.sin(a1) * rInner;
                           float y1In = MathHelper.cos(a1) * rInner;
                           float x2In = -MathHelper.sin(a2) * rInner;
                           float y2In = MathHelper.cos(a2) * rInner;
                           fill.vertex(mat, 0.0F, 0.0F, 0.0F).color(centerArgb);
                           fill.vertex(mat, x1In, y1In, 0.0F).color(midArgb);
                           fill.vertex(mat, x2In, y2In, 0.0F).color(midArgb);
                           fill.vertex(mat, 0.0F, 0.0F, 0.0F).color(centerArgb);
                        }

                        for (int i = 0; i < SEGMENTS; i++) {
                           float a1 = (float)(i * PI2 / SEGMENTS);
                           float a2 = (float)((i + 1) * PI2 / SEGMENTS);
                           float x1In = -MathHelper.sin(a1) * rInner;
                           float y1In = MathHelper.cos(a1) * rInner;
                           float x2In = -MathHelper.sin(a2) * rInner;
                           float y2In = MathHelper.cos(a2) * rInner;
                           float x1Out = -MathHelper.sin(a1) * rOuter;
                           float y1Out = MathHelper.cos(a1) * rOuter;
                           float x2Out = -MathHelper.sin(a2) * rOuter;
                           float y2Out = MathHelper.cos(a2) * rOuter;
                           fill.vertex(mat, x1In, y1In, 0.0F).color(midArgb);
                           fill.vertex(mat, x1Out, y1Out, 0.0F).color(edgeArgb);
                           fill.vertex(mat, x2Out, y2Out, 0.0F).color(edgeArgb);
                           fill.vertex(mat, x2In, y2In, 0.0F).color(midArgb);
                        }
                     } finally {
                        stack.pop();
                     }
                  }
               }
            }
         }
      }
   }
}
