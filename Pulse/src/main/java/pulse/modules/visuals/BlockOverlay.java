package pulse.modules.visuals;

import java.awt.Color;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.block.BlockState;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.BufferAllocator;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import pulse.events.BlockOutlineEvent;
import pulse.events.WorldRenderEvent;
import pulse.module.ClientModule;
import pulse.module.ModuleCategory;
import pulse.module.ModuleInfo;
import pulse.module.ModuleRegistry;
import pulse.render.RenderSystemHelper;
import pulse.render.shader.PulseShaderProgram;
import pulse.render.shader.ShaderLibrary;
import pulse.render.system.ClientPipelines;
import pulse.settings.BooleanSetting;
import pulse.settings.ColorSetting;
import pulse.settings.ModeSetting;
import pulse.settings.SettingGroup;
import pulse.settings.SliderSetting;

@ModuleInfo(a = "Block Overlay", b = "Красиво выделяет блок, на который наведен игрок", c = ModuleCategory.VISUALS)
public class BlockOverlay extends ClientModule {
   private static final String FILL_NORMAL = "Обычная";
   private static final String FILL_SHADER = "Шейдер";
   private static final String ANIMATION_NONE = "Нет";
   private static final String ANIMATION_PULSE = "Пульсация";
   private static final String ANIMATION_WAVE = "Волна";
   private static final String SHADER_NEBULA = "Небула";
   private static final String SHADER_STARS = "Звёзды";
   private static final String SHADER_WEB = "Паутина";
   private static final String SHADER_PLASMA = "Плазма";
   private final SettingGroup outlineGroup = new SettingGroup("Обводка");
   private final BooleanSetting outlineEnabled = new BooleanSetting("Обводка", true);
   private final SliderSetting lineWidth;
   private final SettingGroup fillGroup;
   private final BooleanSetting fillEnabled;
   private final ModeSetting fillType;
   private final SliderSetting fillAlpha;
   private final ModeSetting shaderType;
   private final SliderSetting shaderSpeed;
   private final SliderSetting shaderAlpha;
   private final SettingGroup animationGroup;
   private final ModeSetting animationMode;
   private final SettingGroup colorGroup;
   private final BooleanSetting useClientColor;
   private final ColorSetting customColor;
   private Box smoothedBox;
   private long lastRenderNanos;
   private float frameShaderTime;
   private Color frameShaderTint = Color.WHITE;

   public BlockOverlay() {
      SliderSetting sliderSetting = new SliderSetting("Толщина линий", 2.0F, 1.0F, 5.0F, 0.5F);
      BooleanSetting booleanSetting = this.outlineEnabled;
      this.lineWidth = sliderSetting.a(booleanSetting::a);
      this.fillGroup = new SettingGroup("Толщина линий");
      this.fillEnabled = new BooleanSetting("Заливка", true);
      ModeSetting modeSetting = new ModeSetting("Тип заливки", new String[]{"Обычная", "Шейдер"}, "Обычная");
      BooleanSetting booleanSetting2 = this.fillEnabled;
      this.fillType = modeSetting.a(booleanSetting2::a);
      this.fillAlpha = new SliderSetting("Прозрачность заливки", 0.3F, 0.1F, 1.0F, 0.05F).a(() -> this.fillEnabled.a() && this.fillType.b("Обычная"));
      this.shaderType = new ModeSetting("Шейдер", new String[]{"Небула", "Звёзды", "Паутина", "Плазма"}, "Небула")
         .a(() -> this.fillEnabled.a() && this.fillType.b("Шейдер"));
      this.shaderSpeed = new SliderSetting("Скорость анимации", 1.0F, 0.1F, 3.0F, 0.1F).a(() -> this.fillEnabled.a() && this.fillType.b("Шейдер"));
      this.shaderAlpha = new SliderSetting("Прозрачность", 1.0F, 0.1F, 1.0F, 0.05F).a(() -> this.fillEnabled.a() && this.fillType.b("Шейдер"));
      this.animationGroup = new SettingGroup("Анимация");
      this.animationMode = new ModeSetting("Тип заливки", new String[]{"Нет", "Пульсация", "Волна"}, "Нет");
      this.colorGroup = new SettingGroup("Цвет");
      this.useClientColor = new BooleanSetting("Цвет клиента", true);
      this.customColor = new ColorSetting("Кастомный цвет", Color.WHITE).a(() -> !this.useClientColor.a());
   }

   @EventHandler
   public void onBlockOutline(BlockOutlineEvent blockOutlineEvent) {
      blockOutlineEvent.a();
   }

   @EventHandler
   public void onWorldRender(WorldRenderEvent event) {
      HitResult hitResult = c.crosshairTarget;
      if (hitResult != null && hitResult.getType() == Type.BLOCK) {
         BlockPos blockPos = ((BlockHitResult)hitResult).getBlockPos();
         BlockState BlockStateVarBlockState = c.world.getBlockState(blockPos);
         if (BlockStateVarBlockState == null || BlockStateVarBlockState.isAir()) {
            return;
         }

         Camera camera = c.gameRenderer.getCamera();
         double cameraX = camera.getCameraPos().x;
         double cameraY = camera.getCameraPos().y;
         double cameraZ = camera.getCameraPos().z;
         Box BoxVarOffset = BlockStateVarBlockState.getOutlineShape(c.world, blockPos)
            .getBoundingBox()
            .offset(blockPos.getX() - cameraX, blockPos.getY() - cameraY, blockPos.getZ() - cameraZ);
         BoxVarOffset = this.smoothSelectionBox(BoxVarOffset);
         this.renderOverlay(event.matrices(), this.applyPrimaryAnimation(BoxVarOffset), this.overlayColor(), cameraX, cameraY, cameraZ);
         if (this.animationMode.b("Волна")) {
            this.renderWave(event.matrices(), BoxVarOffset, this.overlayColor(), cameraX, cameraY, cameraZ);
         }
      }
   }

   private Box smoothSelectionBox(Box target) {
      long now = System.nanoTime();
      if (this.smoothedBox != null && this.lastRenderNanos != 0L && !(this.smoothedBox.getCenter().squaredDistanceTo(target.getCenter()) > 9.0)) {
         double dt = Math.min(0.05, (now - this.lastRenderNanos) / 1.0E9);
         this.lastRenderNanos = now;
         double k = 1.0 - Math.exp(-dt * 13.0);
         this.smoothedBox = new Box(
            lerpD(this.smoothedBox.minX, target.minX, k),
            lerpD(this.smoothedBox.minY, target.minY, k),
            lerpD(this.smoothedBox.minZ, target.minZ, k),
            lerpD(this.smoothedBox.maxX, target.maxX, k),
            lerpD(this.smoothedBox.maxY, target.maxY, k),
            lerpD(this.smoothedBox.maxZ, target.maxZ, k)
         );
         return this.smoothedBox;
      } else {
         this.smoothedBox = target;
         this.lastRenderNanos = now;
         return target;
      }
   }

   private static double lerpD(double a, double b, double t) {
      return a + (b - a) * t;
   }

   private Box applyPrimaryAnimation(Box BoxVar) {
      return !this.animationMode.b("Пульсация") ? BoxVar : scaleBox(BoxVar, 1.0F + (float)((Math.sin(System.currentTimeMillis() / 260.0) + 1.0) * 0.5) * 0.08F);
   }

   private void renderOverlay(MatrixStack MatrixStackVar, Box BoxVar, Color color, double cameraX, double cameraY, double cameraZ) {
      MatrixStackVar.push();
      BufferAllocator allocator = new BufferAllocator(524288);
      Immediate imm = VertexConsumerProvider.immediate(allocator);
      if (this.fillEnabled.a()) {
         this.drawFill(MatrixStackVar, imm, BoxVar, color, cameraX, cameraY, cameraZ);
      }

      if (this.outlineEnabled.a() && !"Шейдер".equals(this.fillType.d())) {
         this.drawOutline(MatrixStackVar, imm, BoxVar, color, 1.0F, this.lineWidth.a());
      }

      imm.draw();
      MatrixStackVar.pop();
      allocator.close();
   }

   private void renderWave(MatrixStack MatrixStackVar, Box BoxVar, Color color, double cameraX, double cameraY, double cameraZ) {
      float fCurrentTimeMillis = (float)(System.currentTimeMillis() % 1000L) / 1000.0F;
      Box BoxVarScaleBox = scaleBox(BoxVar, 1.0F + fCurrentTimeMillis * 0.3F);
      MatrixStackVar.push();
      BufferAllocator allocator = new BufferAllocator(65536);
      Immediate imm = VertexConsumerProvider.immediate(allocator);
      this.drawOutline(MatrixStackVar, imm, BoxVarScaleBox, color, 1.0F - fCurrentTimeMillis, Math.max(1.0F, this.lineWidth.a() - 0.5F));
      imm.draw();
      MatrixStackVar.pop();
      allocator.close();
   }

   private void drawOutline(MatrixStack MatrixStackVar, Immediate imm, Box BoxVar, Color color, float f, float f2) {
      RenderSystemHelper.lineWidth(f2);
      VertexConsumer buf = imm.getBuffer(ClientPipelines.OUTLINE_THROUGH);
      addBoxLines(buf, MatrixStackVar.peek().getPositionMatrix(), BoxVar, color, f);
   }

   private void renderGpuShaderBox(MatrixStack matrices, Box box, Color color) {
      String selected = this.shaderType.d();
      String programName;
      if ("Небула".equals(selected)) {
         programName = "block_nebula";
      } else if ("Звёзды".equals(selected)) {
         programName = "block_starfield";
      } else if ("Паутина".equals(selected)) {
         programName = "block_cobweb";
      } else {
         if (!"Плазма".equals(selected)) {
            return;
         }

         programName = "block_plasma";
      }

      ShaderLibrary.getRegistry().find(programName).ifPresent(program -> {
         float fov = ((Number)c.options.getFov().getValue()).floatValue();
         Matrix4f projection = c.gameRenderer.getBasicProjectionMatrix(fov);
         Matrix4f clipTransform = new Matrix4f(projection).mul(matrices.peek().getPositionMatrix());
         program.d(new Matrix4f(), new Matrix4f());
         float seconds = (float)(System.currentTimeMillis() % 1000000L) / 1000.0F;
         program.a("time", seconds * this.shaderSpeed.a() * 0.22F);
         program.a("screenSize", c.getWindow().getFramebufferWidth(), c.getWindow().getFramebufferHeight());
         program.a("baseColor", color);
         program.a("alpha", this.shaderAlpha.a());
         float x0 = (float)box.minX;
         float y0 = (float)box.minY;
         float z0 = (float)box.minZ;
         float x1 = (float)box.maxX;
         float y1 = (float)box.maxY;
         float z1 = (float)box.maxZ;
         drawShaderFace(clipTransform, x0, y0, z1, x1, y0, z1, x1, y1, z1, x0, y1, z1);
         drawShaderFace(clipTransform, x1, y0, z0, x0, y0, z0, x0, y1, z0, x1, y1, z0);
         drawShaderFace(clipTransform, x0, y0, z0, x0, y0, z1, x0, y1, z1, x0, y1, z0);
         drawShaderFace(clipTransform, x1, y0, z1, x1, y0, z0, x1, y1, z0, x1, y1, z1);
         drawShaderFace(clipTransform, x0, y1, z1, x1, y1, z1, x1, y1, z0, x0, y1, z0);
         drawShaderFace(clipTransform, x0, y0, z0, x1, y0, z0, x1, y0, z1, x0, y0, z1);
         program.e();
      });
   }

   private static void drawShaderFace(
      Matrix4f clip, float ax, float ay, float az, float bx, float by, float bz, float cx, float cy, float cz, float dx, float dy, float dz
   ) {
      Vector4f a = clip.transform(new Vector4f(ax, ay, az, 1.0F));
      Vector4f b = clip.transform(new Vector4f(bx, by, bz, 1.0F));
      Vector4f c = clip.transform(new Vector4f(cx, cy, cz, 1.0F));
      Vector4f d = clip.transform(new Vector4f(dx, dy, dz, 1.0F));
      if (!(a.w <= 0.001F) && !(b.w <= 0.001F) && !(c.w <= 0.001F) && !(d.w <= 0.001F)) {
         a.div(a.w);
         b.div(b.w);
         c.div(c.w);
         d.div(d.w);
         float[] vertices = new float[]{a.x, a.y, a.z, 0.0F, 0.0F, b.x, b.y, b.z, 1.0F, 0.0F, c.x, c.y, c.z, 1.0F, 1.0F, d.x, d.y, d.z, 0.0F, 1.0F};
         PulseShaderProgram.a(vertices, 6);
      }
   }

   private void drawFill(MatrixStack MatrixStackVar, Immediate imm, Box BoxVar, Color color, double cameraX, double cameraY, double cameraZ) {
      Matrix4f matrix = MatrixStackVar.peek().getPositionMatrix();
      if (this.fillType.b("Шейдер")) {
         if ("Звёзды".equals(this.shaderType.d())) {
            VertexConsumer shaderBuffer = imm.getBuffer(ClientPipelines.BLOCK_STARFIELD);
            this.addFshBoxQuads(shaderBuffer, matrix, BoxVar, this.shaderAlpha.a());
         } else {
            VertexConsumer shaderBuffer = imm.getBuffer(ClientPipelines.QUAD_THROUGH);
            this.addShaderBoxQuads(shaderBuffer, matrix, BoxVar, this.shaderAlpha.a(), cameraX, cameraY, cameraZ);
         }
      } else {
         VertexConsumer buf = imm.getBuffer(ClientPipelines.QUAD_THROUGH);
         addBoxQuads(buf, matrix, BoxVar, color, this.fillAlpha.a());
      }
   }

   private Color overlayColor() {
      return this.useClientColor.a() ? ModuleRegistry.CLIENT_COLOR.n() : this.customColor.a();
   }

   private Color shaderColor(float f, float f2, float f3, float f4) {
      float t = this.frameShaderTime;
      String strD = this.shaderType.d();
      if ("Небула".equals(strD)) {
         return this.shaderNebula(f, f2, f3, t, f4);
      } else if ("Звёзды".equals(strD)) {
         return this.shaderStars(f, f2, f3, t, f4);
      } else if ("Паутина".equals(strD)) {
         return this.shaderWeb(f, f2, f3, t, f4);
      } else {
         return "Плазма".equals(strD) ? this.shaderPlasma(f, f2, f3, t, f4) : new Color(128, 128, 128, Math.round(f4 * 255.0F));
      }
   }

   private float noise2d(float x, float y) {
      float n = (float)Math.sin(x * 12.9898F + y * 78.233F) * 43758.547F;
      return n - (float)Math.floor(n);
   }

   private float smoothNoise(float x, float y) {
      float ix = (float)Math.floor(x);
      float iy = (float)Math.floor(y);
      float fx = x - ix;
      float fy = y - iy;
      fx = fx * fx * (3.0F - 2.0F * fx);
      fy = fy * fy * (3.0F - 2.0F * fy);
      float a = this.noise2d(ix, iy);
      float b = this.noise2d(ix + 1.0F, iy);
      float c = this.noise2d(ix, iy + 1.0F);
      float d = this.noise2d(ix + 1.0F, iy + 1.0F);
      return a + (b - a) * fx + (c - a) * fy + (a - b - c + d) * fx * fy;
   }

   private float fbm(float x, float y, int octaves) {
      float value = 0.0F;
      float amplitude = 0.5F;

      for (int i = 0; i < octaves; i++) {
         value += amplitude * this.smoothNoise(x, y);
         x *= 2.0F;
         y *= 2.0F;
         amplitude *= 0.5F;
      }

      return value;
   }

   private Color shaderNebula(float x, float y, float z, float t, float alpha) {
      float u = x * 2.15F + z * 1.35F;
      float v = y * 2.25F - z * 0.85F;
      float time = t * 0.22F;
      float warpX = this.fbm(u * 0.55F + time * 0.24F, v * 0.55F - time * 0.13F, 5);
      float warpY = this.fbm(u * 0.55F - time * 0.16F + 17.3F, v * 0.55F + time * 0.2F + 9.1F, 5);
      float clouds = this.fbm(u * 0.72F + warpX * 3.1F + time * 0.12F, v * 0.72F + warpY * 3.1F - time * 0.09F, 5);
      float wisps = this.fbm(u * 1.28F - warpY * 1.8F - time * 0.18F + 4.7F, v * 1.28F + warpX * 1.8F + time * 0.14F + 12.4F, 4);
      float violet = clampF((clouds - 0.24F) * 1.85F, 0.0F, 1.0F);
      float mint = clampF((warpX + wisps - 0.67F) * 1.55F, 0.0F, 1.0F);
      float pearl = clampF((wisps - 0.48F) * 2.5F, 0.0F, 1.0F);
      float r = 0.31F + violet * 0.37F + pearl * 0.24F;
      float g = 0.12F + violet * 0.18F + mint * 0.2F + pearl * 0.27F;
      float b = 0.58F + violet * 0.34F + mint * 0.1F + pearl * 0.18F;
      Color tint = this.frameShaderTint;
      if (!this.useClientColor.a()) {
         r *= 0.65F + tint.getRed() / 255.0F * 0.35F;
         g *= 0.65F + tint.getGreen() / 255.0F * 0.35F;
         b *= 0.65F + tint.getBlue() / 255.0F * 0.35F;
      }

      float filament = clampF((clouds + wisps - 0.82F) * 1.65F, 0.0F, 1.0F);
      filament *= filament;
      float cloudAlpha = 0.135F + filament * 0.22F + pearl * 0.1F + mint * 0.06F;
      int ai = Math.round(clampF(alpha * cloudAlpha, 0.09F, 0.48F) * 255.0F);
      return new Color(Math.round(clampF(r, 0.0F, 1.0F) * 255.0F), Math.round(clampF(g, 0.0F, 1.0F) * 255.0F), Math.round(clampF(b, 0.0F, 1.0F) * 255.0F), ai);
   }

   private Color shaderStars(float u, float v, float faceSeed, float t, float alpha) {
      float slow = t * 0.035F;
      float wave = (float)Math.sin((u * 0.78F - v * 0.46F) * Math.PI + slow);
      float wave2 = (float)Math.sin((u + v) * 1.35F * Math.PI - slow * 0.57F + faceSeed * 0.03F);
      float purple = clampF(wave * 0.5F + 0.5F, 0.0F, 1.0F);
      purple = purple * purple * (0.48F + (wave2 * 0.5F + 0.5F) * 0.52F);
      float r = 0.03F + purple * 0.42F;
      float g = 0.035F + purple * 0.075F;
      float b = 0.055F + purple * 0.46F;
      float gx = (float)Math.floor(u * 32.0F + 0.5F);
      float gy = (float)Math.floor(v * 32.0F + 0.5F);
      float h = this.noise2d(gx + faceSeed * 2.17F, gy - faceSeed * 1.31F);
      float h2 = this.noise2d(gx * 2.73F - faceSeed, gy * 3.19F + faceSeed);
      float fine = clampF((h - 0.7F) / 0.3F, 0.0F, 1.0F);
      fine = fine * fine * 0.58F;
      float bright = clampF((h2 - 0.935F) / 0.065F, 0.0F, 1.0F);
      bright *= bright * (0.86F + 0.14F * (float)Math.sin(slow * 2.1F + h2 * 24.0F));
      float warm = this.noise2d(gx * 5.7F + 4.0F, gy * 7.1F - 9.0F);
      float outAlpha = 0.15F + purple * 0.075F;
      return new Color(
         Math.round(clampF(r, 0.0F, 1.0F) * 255.0F),
         Math.round(clampF(g, 0.0F, 1.0F) * 255.0F),
         Math.round(clampF(b, 0.0F, 1.0F) * 255.0F),
         Math.round(clampF(alpha * outAlpha, 0.025F, 0.64F) * 255.0F)
      );
   }

   private Color shaderWeb(float x, float y, float z, float t, float alpha) {
      float u = (x + z * 0.72F) * 1.75F;
      float v = (y - z * 0.38F) * 1.75F;
      float slow = t * 0.075F;
      float warpX = this.fbm(u * 0.45F + slow, v * 0.45F - slow * 0.7F, 4) - 0.5F;
      float warpY = this.fbm(u * 0.45F - slow * 0.6F + 8.3F, v * 0.45F + slow + 3.7F, 4) - 0.5F;
      float pu = u + warpX * 1.05F;
      float pv = v + warpY * 1.05F;
      float d1 = (float)Math.sqrt((pu - 0.35F) * (pu - 0.35F) + (pv + 0.15F) * (pv + 0.15F));
      float d2 = (float)Math.sqrt((pu + 1.15F) * (pu + 1.15F) + (pv - 0.65F) * (pv - 0.65F));
      float ring1 = 1.0F - clampF(Math.abs((float)Math.sin(d1 * 4.1F - slow * 0.8F)) * 3.2F, 0.0F, 1.0F);
      float ring2 = 1.0F - clampF(Math.abs((float)Math.sin(d2 * 3.5F + slow * 0.55F)) * 3.5F, 0.0F, 1.0F);
      ring1 *= ring1;
      ring2 *= ring2;
      float strand = clampF(ring1 * 0.82F + ring2 * 0.62F, 0.0F, 1.0F);
      float haze = this.fbm(pu * 0.52F + 4.0F, pv * 0.52F - 7.0F, 5);
      float glow = clampF((haze - 0.38F) * 1.55F, 0.0F, 1.0F);
      float warmZone = clampF((this.fbm(pu * 0.31F + slow * 0.25F + 13.0F, pv * 0.31F - slow * 0.18F + 5.0F, 5) - 0.34F) * 2.0F, 0.0F, 1.0F);
      float coolZone = clampF((this.fbm(pu * 0.38F - 9.0F, pv * 0.38F + 16.0F, 4) - 0.4F) * 1.8F, 0.0F, 1.0F);
      float r = 0.16F + warmZone * 0.52F + glow * 0.16F + strand * 0.16F;
      float g = 0.14F + warmZone * 0.29F + coolZone * 0.13F + strand * 0.13F;
      float b = 0.17F + warmZone * 0.25F + coolZone * 0.22F + strand * 0.17F;
      float outAlpha = alpha * (0.34F + glow * 0.22F + strand * 0.27F);
      return new Color(
         Math.round(clampF(r, 0.0F, 1.0F) * 255.0F),
         Math.round(clampF(g, 0.0F, 1.0F) * 255.0F),
         Math.round(clampF(b, 0.0F, 1.0F) * 255.0F),
         Math.round(clampF(outAlpha, 0.26F, 0.82F) * 255.0F)
      );
   }

   private Color shaderPlasma(float x, float y, float z, float t, float alpha) {
      float u = (x + z * 0.68F) * 1.85F;
      float v = (y - z * 0.32F) * 1.85F;
      float slow = t * 0.085F;
      float fold1 = (float)Math.sin(v * 2.35F + slow + Math.sin(u * 1.15F - slow * 0.45F) * 0.72F);
      float fold2 = (float)Math.sin(v * 4.1F - slow * 0.62F + Math.cos(u * 0.82F + slow * 0.3F) * 0.48F);
      float fold3 = (float)Math.cos((u + v * 0.42F) * 1.34F + slow * 0.38F);
      float noise = this.fbm(u * 0.42F + slow * 0.18F, v * 0.42F - slow * 0.13F, 4);
      float plasma = clampF(0.48F + fold1 * 0.2F + fold2 * 0.1F + fold3 * 0.07F + (noise - 0.5F) * 0.2F, 0.0F, 1.0F);
      float ribbon = clampF(1.0F - Math.abs(fold1) * 1.65F, 0.0F, 1.0F);
      ribbon *= ribbon;
      float hot = clampF((plasma - 0.64F) * 2.75F, 0.0F, 1.0F);
      float colorCycle = (float)Math.sin(slow * 0.72F + u * 0.24F) * 0.5F + 0.5F;
      float core = clampF(1.0F - plasma * 1.32F, 0.0F, 1.0F);
      float edgeHeat = clampF((plasma - 0.46F) * 2.35F, 0.0F, 1.0F);
      float shine = clampF(1.0F - Math.abs((float)Math.sin(v * 3.15F + slow * 0.9F + Math.sin(u * 0.9F) * 0.62F)) * 4.2F, 0.0F, 1.0F);
      shine *= shine;
      float r = 0.3F + edgeHeat * 0.62F + colorCycle * 0.18F + shine * 0.34F;
      float g = 0.075F + edgeHeat * (0.08F + colorCycle * 0.2F) + shine * 0.26F;
      float b = 0.24F + core * 0.34F + ribbon * 0.22F + shine * 0.3F;
      float outAlpha = alpha * (0.36F + edgeHeat * 0.23F + ribbon * 0.1F + shine * 0.08F);
      return new Color(
         Math.round(clampF(r, 0.0F, 1.0F) * 255.0F),
         Math.round(clampF(g, 0.0F, 1.0F) * 255.0F),
         Math.round(clampF(b, 0.0F, 1.0F) * 255.0F),
         Math.round(clampF(outAlpha, 0.3F, 0.77F) * 255.0F)
      );
   }

   private static float clampF(float val, float min, float max) {
      return Math.max(min, Math.min(max, val));
   }

   private static Box scaleBox(Box BoxVar, float f) {
      Vec3d Vec3dVarGetCenter = BoxVar.getCenter();
      double d = (BoxVar.maxX - BoxVar.minX) * 0.5 * f;
      double d2 = (BoxVar.maxY - BoxVar.minY) * 0.5 * f;
      double d3 = (BoxVar.maxZ - BoxVar.minZ) * 0.5 * f;
      return new Box(
         Vec3dVarGetCenter.x - d,
         Vec3dVarGetCenter.y - d2,
         Vec3dVarGetCenter.z - d3,
         Vec3dVarGetCenter.x + d,
         Vec3dVarGetCenter.y + d2,
         Vec3dVarGetCenter.z + d3
      );
   }

   private static void addBoxLines(VertexConsumer buf, Matrix4f matrix4f, Box BoxVar, Color color, float f) {
      float f2 = (float)BoxVar.minX;
      float f3 = (float)BoxVar.minY;
      float f4 = (float)BoxVar.minZ;
      float f5 = (float)BoxVar.maxX;
      float f6 = (float)BoxVar.maxY;
      float f7 = (float)BoxVar.maxZ;
      line(buf, matrix4f, f2, f3, f4, f5, f3, f4, color, f);
      line(buf, matrix4f, f2, f3, f4, f2, f6, f4, color, f);
      line(buf, matrix4f, f2, f3, f4, f2, f3, f7, color, f);
      line(buf, matrix4f, f5, f6, f7, f2, f6, f7, color, f);
      line(buf, matrix4f, f5, f6, f7, f5, f3, f7, color, f);
      line(buf, matrix4f, f5, f6, f7, f5, f6, f4, color, f);
      line(buf, matrix4f, f2, f6, f4, f5, f6, f4, color, f);
      line(buf, matrix4f, f5, f3, f4, f5, f3, f7, color, f);
      line(buf, matrix4f, f2, f3, f7, f5, f3, f7, color, f);
      line(buf, matrix4f, f2, f6, f7, f2, f6, f4, color, f);
      line(buf, matrix4f, f2, f6, f7, f2, f3, f7, color, f);
      line(buf, matrix4f, f5, f6, f4, f5, f3, f4, color, f);
   }

   private static void addBoxQuads(VertexConsumer buf, Matrix4f matrix4f, Box BoxVar, Color color, float f) {
      float f2 = (float)BoxVar.minX;
      float f3 = (float)BoxVar.minY;
      float f4 = (float)BoxVar.minZ;
      float f5 = (float)BoxVar.maxX;
      float f6 = (float)BoxVar.maxY;
      float f7 = (float)BoxVar.maxZ;
      quad(buf, matrix4f, color, f, f2, f3, f7, f5, f3, f7, f5, f6, f7, f2, f6, f7);
      quad(buf, matrix4f, color, f, f2, f6, f4, f5, f6, f4, f5, f3, f4, f2, f3, f4);
      quad(buf, matrix4f, color, f, f2, f3, f4, f2, f3, f7, f2, f6, f7, f2, f6, f4);
      quad(buf, matrix4f, color, f, f5, f6, f4, f5, f6, f7, f5, f3, f7, f5, f3, f4);
      quad(buf, matrix4f, color, f, f2, f6, f4, f2, f6, f7, f5, f6, f7, f5, f6, f4);
      quad(buf, matrix4f, color, f, f5, f3, f4, f5, f3, f7, f2, f3, f7, f2, f3, f4);
   }

   private void addFshBoxQuads(VertexConsumer buf, Matrix4f mat, Box box, float alpha) {
      float x0 = (float)box.minX;
      float y0 = (float)box.minY;
      float z0 = (float)box.minZ;
      float x1 = (float)box.maxX;
      float y1 = (float)box.maxY;
      float z1 = (float)box.maxZ;
      float seconds = (float)(System.currentTimeMillis() % 256000L) / 1000.0F * this.shaderSpeed.a() * 0.22F;
      int packed = Math.max(0, Math.min(65535, Math.round(seconds / 256.0F * 65535.0F)));
      int hi = packed >> 8 & 0xFF;
      int lo = packed & 0xFF;
      int a = Math.round(clampF(alpha, 0.0F, 1.0F) * 255.0F);
      fshQuad(buf, mat, hi, lo, a, x0, y0, z1, x1, y0, z1, x1, y1, z1, x0, y1, z1);
      fshQuad(buf, mat, hi, lo, a, x1, y0, z0, x0, y0, z0, x0, y1, z0, x1, y1, z0);
      fshQuad(buf, mat, hi, lo, a, x0, y0, z0, x0, y0, z1, x0, y1, z1, x0, y1, z0);
      fshQuad(buf, mat, hi, lo, a, x1, y0, z1, x1, y0, z0, x1, y1, z0, x1, y1, z1);
      fshQuad(buf, mat, hi, lo, a, x0, y1, z1, x1, y1, z1, x1, y1, z0, x0, y1, z0);
      fshQuad(buf, mat, hi, lo, a, x0, y0, z0, x1, y0, z0, x1, y0, z1, x0, y0, z1);
   }

   private static void fshQuad(
      VertexConsumer b,
      Matrix4f m,
      int hi,
      int lo,
      int alpha,
      float ax,
      float ay,
      float az,
      float bx,
      float by,
      float bz,
      float cx,
      float cy,
      float cz,
      float dx,
      float dy,
      float dz
   ) {
      b.vertex(m, ax, ay, az).texture(0.0F, 0.0F).color(hi, lo, 0, alpha);
      b.vertex(m, bx, by, bz).texture(1.0F, 0.0F).color(hi, lo, 0, alpha);
      b.vertex(m, cx, cy, cz).texture(1.0F, 1.0F).color(hi, lo, 0, alpha);
      b.vertex(m, dx, dy, dz).texture(0.0F, 1.0F).color(hi, lo, 0, alpha);
   }

   private void addShaderBoxQuads(VertexConsumer buf, Matrix4f matrix4f, Box BoxVar, float f, double cameraX, double cameraY, double cameraZ) {
      float x0 = (float)BoxVar.minX;
      float y0 = (float)BoxVar.minY;
      float z0 = (float)BoxVar.minZ;
      float x1 = (float)BoxVar.maxX;
      float y1 = (float)BoxVar.maxY;
      float z1 = (float)BoxVar.maxZ;
      int res = 16;
      this.frameShaderTime = (float)(System.currentTimeMillis() % 100000L) / 1000.0F * this.shaderSpeed.a();
      this.frameShaderTint = this.overlayColor();
      this.tessShaderFace(buf, matrix4f, f, cameraX, cameraY, cameraZ, res, x0, y0, z1, x1, y0, z1, x1, y1, z1, x0, y1, z1);
      this.tessShaderFace(buf, matrix4f, f, cameraX, cameraY, cameraZ, res, x0, y1, z0, x1, y1, z0, x1, y0, z0, x0, y0, z0);
      this.tessShaderFace(buf, matrix4f, f, cameraX, cameraY, cameraZ, res, x0, y0, z0, x0, y0, z1, x0, y1, z1, x0, y1, z0);
      this.tessShaderFace(buf, matrix4f, f, cameraX, cameraY, cameraZ, res, x1, y1, z0, x1, y1, z1, x1, y0, z1, x1, y0, z0);
      this.tessShaderFace(buf, matrix4f, f, cameraX, cameraY, cameraZ, res, x0, y1, z0, x0, y1, z1, x1, y1, z1, x1, y1, z0);
      this.tessShaderFace(buf, matrix4f, f, cameraX, cameraY, cameraZ, res, x1, y0, z0, x1, y0, z1, x0, y0, z1, x0, y0, z0);
   }

   private Color shaderVertexColor(float px, float py, float pz, float u, float v, float faceSeed, double cx, double cy, double cz, float alpha) {
      Color result;
      if ("Звёзды".equals(this.shaderType.d())) {
         float time = (float)(System.currentTimeMillis() % 100000L) / 1000.0F * this.shaderSpeed.a();
         result = this.shaderStars(u, v, faceSeed, time, alpha);
      } else {
         result = this.shaderColor((float)(px + cx), (float)(py + cy), (float)(pz + cz), alpha);
      }

      float edge = Math.min(Math.min(u, 1.0F - u), Math.min(v, 1.0F - v));
      float edgeFade = clampF(edge * 28.0F, 0.0F, 1.0F);
      return new Color(result.getRed(), result.getGreen(), result.getBlue(), Math.round(result.getAlpha() * edgeFade));
   }

   private void tessShaderFace(
      VertexConsumer buf,
      Matrix4f mat,
      float alpha,
      double cx,
      double cy,
      double cz,
      int res,
      float ax,
      float ay,
      float az,
      float bx,
      float by,
      float bz,
      float dx,
      float dy,
      float dz,
      float ex,
      float ey,
      float ez
   ) {
      int n = res + 1;
      float[] xs = new float[n * n];
      float[] ys = new float[n * n];
      float[] zs = new float[n * n];
      Color[] colors = new Color[n * n];
      float faceSeed = ax * 17.0F + ay * 31.0F + az * 47.0F + bx * 7.0F + by * 11.0F + bz * 13.0F;

      for (int j = 0; j <= res; j++) {
         float v = (float)j / res;

         for (int i = 0; i <= res; i++) {
            float u = (float)i / res;
            int index = j * n + i;
            float px = bilerp(ax, bx, dx, ex, u, v);
            float py = bilerp(ay, by, dy, ey, u, v);
            float pz = bilerp(az, bz, dz, ez, u, v);
            xs[index] = px;
            ys[index] = py;
            zs[index] = pz;
            colors[index] = this.shaderVertexColor(px, py, pz, u, v, faceSeed, cx, cy, cz, alpha);
         }
      }

      for (int j = 0; j < res; j++) {
         for (int i = 0; i < res; i++) {
            int i0 = j * n + i;
            int i1 = i0 + 1;
            int i3 = (j + 1) * n + i;
            int i2 = i3 + 1;
            vertex(buf, mat, xs[i0], ys[i0], zs[i0], colors[i0], alpha);
            vertex(buf, mat, xs[i1], ys[i1], zs[i1], colors[i1], alpha);
            vertex(buf, mat, xs[i2], ys[i2], zs[i2], colors[i2], alpha);
            vertex(buf, mat, xs[i3], ys[i3], zs[i3], colors[i3], alpha);
         }
      }
   }

   private void addStarSprites(
      VertexConsumer buf,
      Matrix4f mat,
      float alpha,
      float ax,
      float ay,
      float az,
      float bx,
      float by,
      float bz,
      float dx,
      float dy,
      float dz,
      float ex,
      float ey,
      float ez
   ) {
      float faceSeed = ax * 17.0F + ay * 31.0F + az * 47.0F + bx * 7.0F + by * 11.0F + bz * 13.0F;
      float time = (float)(System.currentTimeMillis() % 10000000L) / 1000.0F * this.shaderSpeed.a();

      for (int i = 0; i < 22; i++) {
         float seed = this.noise2d(i * 23.17F + faceSeed, i * 61.73F - faceSeed);
         float rawPhase = seed * 5.0F + time * (0.004F + seed * 0.0035F);
         float cycle = (float)Math.floor(rawPhase);
         float phase = wrap(rawPhase);
         float life = 0.52F + this.noise2d(i * 9.31F, cycle + faceSeed) * 0.43F;
         if (!(phase >= life)) {
            float travel = phase / life;
            float su = 0.02F + travel * 0.96F;
            float sv = 0.025F + this.noise2d(i * 79.1F + cycle * 17.0F, seed * 37.9F - faceSeed) * 0.95F;
            float edgeFade = clampF(Math.min(travel, 1.0F - travel) * 9.0F, 0.0F, 1.0F);
            float twinkle = 0.92F + 0.08F * (float)Math.sin(time * 0.055F + seed * 19.0F);
            float sizeClass = this.noise2d(i * 31.7F, cycle * 5.3F + faceSeed);
            float size = 0.0024F + sizeClass * sizeClass * 0.0058F;
            if (sizeClass > 0.91F) {
               size += 0.009F;
            }

            float warm = this.noise2d(i * 8.3F, cycle * 2.7F + faceSeed);
            Color halo = new Color(
               255,
               Math.round(185.0F + warm * 70.0F),
               Math.round(170.0F + (1.0F - warm) * 85.0F),
               Math.round(clampF(alpha * (0.045F + sizeClass * 0.065F) * twinkle * edgeFade, 0.0F, 0.14F) * 255.0F)
            );
            emitFaceQuad(buf, mat, halo, su, sv, size * (2.2F + sizeClass), ax, ay, az, bx, by, bz, dx, dy, dz, ex, ey, ez);
            Color core = new Color(
               255,
               Math.round(205.0F + warm * 50.0F),
               Math.round(185.0F + (1.0F - warm) * 70.0F),
               Math.round(clampF(alpha * (0.25F + sizeClass * 0.35F) * twinkle * edgeFade, 0.0F, 0.64F) * 255.0F)
            );
            emitFaceQuad(buf, mat, core, su, sv, size, ax, ay, az, bx, by, bz, dx, dy, dz, ex, ey, ez);
         }
      }
   }

   private static void emitFaceQuad(
      VertexConsumer buf,
      Matrix4f mat,
      Color color,
      float u,
      float v,
      float size,
      float ax,
      float ay,
      float az,
      float bx,
      float by,
      float bz,
      float dx,
      float dy,
      float dz,
      float ex,
      float ey,
      float ez
   ) {
      float u0 = clampF(u - size, 0.002F, 0.998F);
      float u1 = clampF(u + size, 0.002F, 0.998F);
      float v0 = clampF(v - size, 0.002F, 0.998F);
      float v1 = clampF(v + size, 0.002F, 0.998F);
      float p0x = bilerp(ax, bx, dx, ex, u0, v0);
      float p0y = bilerp(ay, by, dy, ey, u0, v0);
      float p0z = bilerp(az, bz, dz, ez, u0, v0);
      float p1x = bilerp(ax, bx, dx, ex, u1, v0);
      float p1y = bilerp(ay, by, dy, ey, u1, v0);
      float p1z = bilerp(az, bz, dz, ez, u1, v0);
      float p2x = bilerp(ax, bx, dx, ex, u1, v1);
      float p2y = bilerp(ay, by, dy, ey, u1, v1);
      float p2z = bilerp(az, bz, dz, ez, u1, v1);
      float p3x = bilerp(ax, bx, dx, ex, u0, v1);
      float p3y = bilerp(ay, by, dy, ey, u0, v1);
      float p3z = bilerp(az, bz, dz, ez, u0, v1);
      vertex(buf, mat, p0x, p0y, p0z, color, 1.0F);
      vertex(buf, mat, p1x, p1y, p1z, color, 1.0F);
      vertex(buf, mat, p2x, p2y, p2z, color, 1.0F);
      vertex(buf, mat, p3x, p3y, p3z, color, 1.0F);
   }

   private static void line(VertexConsumer buf, Matrix4f matrix4f, float f, float f2, float f3, float f4, float f5, float f6, Color color, float f7) {
      buf.vertex(matrix4f, f, f2, f3).color(color.getRed(), color.getGreen(), color.getBlue(), (int)(color.getAlpha() * f7));
      buf.vertex(matrix4f, f4, f5, f6).color(color.getRed(), color.getGreen(), color.getBlue(), (int)(color.getAlpha() * f7));
   }

   private static void quad(
      VertexConsumer buf,
      Matrix4f matrix4f,
      Color color,
      float f,
      float f2,
      float f3,
      float f4,
      float f5,
      float f6,
      float f7,
      float f8,
      float f9,
      float f10,
      float f11,
      float f12,
      float f13
   ) {
      vertex(buf, matrix4f, f2, f3, f4, color, f);
      vertex(buf, matrix4f, f5, f6, f7, color, f);
      vertex(buf, matrix4f, f8, f9, f10, color, f);
      vertex(buf, matrix4f, f11, f12, f13, color, f);
   }

   private static float bilerp(float a, float b, float c, float d, float u, float v) {
      return a * (1.0F - u) * (1.0F - v) + b * u * (1.0F - v) + c * u * v + d * (1.0F - u) * v;
   }

   private static void vertex(VertexConsumer buf, Matrix4f matrix4f, float f, float f2, float f3, Color color, float f4) {
      buf.vertex(matrix4f, f, f2, f3).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
   }

   private static Color withAlpha(Color color, float f) {
      return new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.round(f * 255.0F));
   }

   private static float wrap(float f) {
      float f2 = f % 1.0F;
      return f2 < 0.0F ? f2 + 1.0F : f2;
   }
}
