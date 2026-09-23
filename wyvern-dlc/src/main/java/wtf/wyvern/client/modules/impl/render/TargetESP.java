package wtf.wyvern.client.modules.impl.render;

import wtf.wyvern.core.eventbus.EventTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import wtf.wyvern.core.animations.base.Animation;
import wtf.wyvern.core.animations.base.Easing;
import wtf.wyvern.core.events.impl.render.EventRender3D;
import wtf.wyvern.core.theme.Theme;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.impl.combat.Aura;
import wtf.wyvern.client.modules.api.setting.impl.ModeSetting;
import wtf.wyvern.render.display.base.color.ColorRGBA;
import wtf.wyvern.render.display.shader.DrawUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.*;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.texture.ResourceTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import wtf.wyvern.Wyvern;
import wtf.wyvern.render.level.Render3DUtil;
import wtf.wyvern.render.level.CrystalRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(name = "TargetESP", category = Category.RENDER, description = "Подсвечивает текущую цель атаки")
@FastNative
public class TargetESP extends Module {
   public static final TargetESP INSTANCE = new TargetESP();
private final ModeSetting mode = new ModeSetting("Мод", new String[]{
            "Маркер", "Призраки", "Призраки 2", "Гост Райдер", "Призрачные орбиты", "Кристаллы", "Кристаллы2", "Кубики", "Кольцо"
    });
   private final ModeSetting cubeMode = new ModeSetting(
           "Режим кубиков", () -> mode.is("Кубики"), "Новый", "Старый");

   private final Animation chainTargetAnim = new Animation(400L, Easing.CUBIC_OUT);
   private final Animation chainTarget2Anim = new Animation(400L, Easing.CUBIC_OUT);
private final Animation animation = new Animation(400L, Easing.CUBIC_OUT);
    private final Animation animation2 = new Animation(250L, Easing.CUBIC_OUT);
    private final Animation celestialFade = new Animation(500L, Easing.CUBIC_OUT);

    private static final int CELESTIAL_ESP_ARMS = 4;
    private static final int CELESTIAL_ESP_SEGMENTS = 14;
    private static final double CELESTIAL_ESP_ARC = Math.toRadians(60.0);
    private static final double CELESTIAL_ESP_VERTICAL_AMPLITUDE = 0.7;
   private Entity lastTarget = null;
   private boolean textureLoaded = false;
   private float rotationAngle = 0.0F;
   private float rotationSpeed = 0.0F;
   private List<Vector3f> crystall2Points;
   private int crystall2TargetId;
   private float crystall2Width;
   private float crystall2Height;
   private boolean isReversing = false;
   private float animationNurik = 0.0F;
   private long currentTime;
   private final List<OldCubeParticle> oldCubeParticles = new ArrayList<>();
   private long oldCubeLastSpawn;
   private int oldCubeTargetId = Integer.MIN_VALUE;

   private static final float SPIRIT_ANGULAR_SPEED_PER_MS = 0.00755F;
   private static final float SPIRIT_SIZE_MULTIPLIER = 0.50F;
   private static final float SPIRIT_ORBIT_RADIUS = 0.725F;


   private static final float MARKER_SIZE = 8.0F;


   private long timestamp5 = System.nanoTime();


   private static final int ORBIT_PARTICLE_COUNT = 3;
   private static final float ORBIT_BASE_RADIUS = 0.4f;
   private static final float ORBIT_BASE_MUL = 0.1f;
   private static final float ORBIT_SPEED = 15.0f;
   private static final int ORBIT_TRAIL_LENGTH = 40;

   private static final float[] SCALE_CACHE = new float[101];
   static {
      for (int k = 0; k <= 100; k++) {
         SCALE_CACHE[k] = Math.max(0.28f * (k / 100f), 0.15f);
      }
   }

   private final Vec3d[] orbitPositions = new Vec3d[ORBIT_PARTICLE_COUNT];
   private final Vec3d[] orbitMotions = new Vec3d[ORBIT_PARTICLE_COUNT];
   @SuppressWarnings("unchecked")
   private final List<Vec3d>[] orbitTrails = new List[ORBIT_PARTICLE_COUNT];
   private float movingAngle = 0;
   private long lastOrbitTime = 0;
   private final Animation orbitShrinkAnim = new Animation(300L, Easing.CUBIC_OUT);


   private float crystalMoving = 0;

   public TargetESP() {
      for (int i = 0; i < ORBIT_PARTICLE_COUNT; i++) {
         this.orbitTrails[i] = new ArrayList<>();
         this.orbitMotions[i] = Vec3d.ZERO;
      }
   }

   public void onEnable() {
      super.onEnable();
   }

   @Override
   public void onDisable() {
      this.resetOldCubes();
      super.onDisable();
   }

   @EventTarget
   private void onRenderWorldLast(EventRender3D e) {
      if (this.mode.is("Призраки")) {
         this.drawSpiritsTrack(e);
      }

      if (this.mode.is("Призраки 2")) {
         Entity celestialTarget = Aura.INSTANCE.isEnabled() ? Aura.INSTANCE.getTarget() : null;
         if (celestialTarget != null) {
            this.lastTarget = celestialTarget;
            this.celestialFade.update(true);
         } else {
            this.celestialFade.update(false);
            if (this.celestialFade.getValue() == 0.0F) {
               this.lastTarget = null;
            }
         }
         if (this.lastTarget instanceof LivingEntity living && this.celestialFade.getValue() > 0.01F) {
            this.drawCelestialSpirals(e, living, this.celestialFade.getValue());
         }
      }


      if (this.mode.is("Гост Райдер")) {
         this.updateTargetAnimations();
         if (this.lastTarget != null && this.animation.getValue() > 0.01F) {
            this.drawGhosts3(e);
         }
      }

      if (this.mode.is("Призрачные орбиты")) {
         this.updateTargetAnimations();
         if (this.lastTarget != null && this.animation.getValue() > 0.01F) {
            this.drawGhostOrbits(e);
         }
      }

      if (this.mode.is("Кристаллы")) {
         this.updateTargetAnimations();
         if (this.lastTarget != null && this.animation.getValue() > 0.01F) {
            this.renderCrystals(e);
         }
      }

      if (this.mode.is("Кристаллы2")) {
         this.updateTargetAnimations();
         if (this.lastTarget instanceof LivingEntity living && this.animation2.getValue() > 0.01F) {
            this.renderCrystall2(e, living);
         }
      }

      if (this.mode.is("Кубики")) {
         this.updateTargetAnimations();
         if (this.lastTarget instanceof LivingEntity living && this.animation2.getValue() > 0.01F) {
            if (this.cubeMode.is("Новый")) {
               this.resetOldCubes();
               this.renderCubesNew(e, living);
            } else {
               this.renderCubesOld(e, living);
            }
         } else {
            this.resetOldCubes();
         }
      } else if (!this.oldCubeParticles.isEmpty() || this.oldCubeTargetId != Integer.MIN_VALUE) {
         this.resetOldCubes();
      }

      if (this.mode.is("Маркер")) {
         if (!this.textureLoaded) {
            MinecraftClient.getInstance().getTextureManager().registerTexture(Wyvern.id("marker.png"), new ResourceTexture(Wyvern.id("hud/marker.png")));
            this.textureLoaded = true;
         }

         Vec3d camPos = mc.gameRenderer.getCamera().getPos();
         Entity target = Aura.INSTANCE.getTarget();
         if (target != null) {
            this.lastTarget = target;
            this.animation.update(true);
         } else {
            this.animation.update(false);
            if (this.animation.getValue() == 0.0F) {
               this.lastTarget = null;
            }
         }

         if (this.lastTarget != null) {
            double tickDelta = (double) e.getPartialTicks();
            MatrixStack matrices = e.getMatrix();
            double x = MathHelper.lerp(tickDelta, this.lastTarget.lastRenderX, this.lastTarget.getX());
            double y = MathHelper.lerp(tickDelta, this.lastTarget.lastRenderY, this.lastTarget.getY()) + (double) this.lastTarget.getHeight() / 2.0D;
            double z = MathHelper.lerp(tickDelta, this.lastTarget.lastRenderZ, this.lastTarget.getZ());
            matrices.push();
            matrices.translate(x - camPos.x, y - camPos.y, z - camPos.z);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-mc.gameRenderer.getCamera().getYaw()));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(mc.gameRenderer.getCamera().getPitch()));
            float scale = 0.15F * this.animation.getValue();
            matrices.scale(-scale, -scale, scale);
            this.updateRotation();
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(this.rotationAngle));
            if (this.isBlockedByWall(camPos, new Vec3d(x, y, z))) {
               RenderSystem.disableDepthTest();
            }
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            Identifier textureId = Wyvern.id( "icons/marker.png");
            float alpha = this.animation.getValue();
            float size = MARKER_SIZE;
            ColorRGBA color = getColor(Aura.INSTANCE.getTarget()).withAlpha((int) (alpha * 255.0F));
            DrawUtil.drawTexture(matrices, textureId, 0.0F - size / 2.0F, 0.0F - size / 2.0F, size, size, color);
            RenderSystem.enableDepthTest();
            RenderSystem.disableBlend();
            matrices.pop();
         }
      }
   }


   private void updateTargetAnimations() {
      Entity target = Aura.INSTANCE.getTarget();
      if (target != null) {
         if (this.lastTarget != target) {
            for (int i = 0; i < ORBIT_PARTICLE_COUNT; i++) {
               orbitPositions[i] = null;
               orbitMotions[i] = Vec3d.ZERO;
               orbitTrails[i].clear();
            }
         }
         this.lastTarget = target;
         this.animation.update(true);
         this.animation2.update(true);
      } else {
         this.animation.update(false);
         this.animation2.update(false);
         if (this.animation.getValue() == 0.0F) {
            this.lastTarget = null;
         }
      }
   }

   private void renderCubesNew(EventRender3D event, LivingEntity target) {
      float alpha = this.animation2.getValue();
      if (alpha <= 0.01F) return;

      MatrixStack matrices = event.getMatrix();
      Vec3d cameraPos = mc.gameRenderer.getCamera().getPos();
      Vec3d targetPos = new Vec3d(
              MathHelper.lerp(event.getPartialTicks(), target.lastRenderX, target.getX()),
              MathHelper.lerp(event.getPartialTicks(), target.lastRenderY, target.getY()),
              MathHelper.lerp(event.getPartialTicks(), target.lastRenderZ, target.getZ())
      );
      long time = System.currentTimeMillis();
      int count = 24;
      double radius = 0.75D + target.getWidth() * 0.5D - 0.35D * alpha;
      float hurt = MathHelper.sin(target.hurtTime * MathHelper.PI / 20.0F);
      ColorRGBA color = getTargetColor().mix(new ColorRGBA(200, 70, 70), hurt);
      int fillColor = color.withAlpha((int) (70.0F * alpha)).getRGB();
      int lineColor = color.withAlpha((int) (220.0F * alpha)).getRGB();
      int glowColor = color.withAlpha((int) (120.0F * alpha)).getRGB();

      RenderSystem.enableBlend();
      RenderSystem.blendFunc(770, 1);
      RenderSystem.disableCull();
      RenderSystem.enableDepthTest();
      RenderSystem.depthMask(false);

      List<CubeFrame> cubes = new ArrayList<>(count);
      for (int i = 0; i < count; i++) {
         double randomHeight = Math.sin(i * 132.12D + 4.12D);
         double randomRotation = Math.sin(i * 789.34D + 9.87D);
         double angleOffset = MathHelper.TAU / count * i;
         double angle = time / 6000.0D * MathHelper.TAU + angleOffset;
         double ySpeed = 1.0D + randomHeight * 0.2D;
         double yOffset = Math.sin(time / 9000.0D * MathHelper.TAU * ySpeed
                 + angleOffset + randomRotation * 2.0D) * 0.45D + 0.55D;
         double pushOut = hurt * (0.5D + 0.5D * Math.sin(i * 123.45D)) * 0.4D;
         double x = Math.cos(angle) * (radius + pushOut);
         double z = Math.sin(angle) * (radius + pushOut);
         double y = yOffset * target.getHeight();
         float pulse = 1.0F + 0.15F * MathHelper.sin(time / 400.0F + i * 1.5F);
         float cubeSize = 0.19F * pulse * (1.0F - hurt * 0.1F);
         float selfRotation = (float) (time % Math.max(1000L,
                 (long) Math.abs(12000.0D + randomRotation * 2000.0D)))
                 / (float) Math.abs(12000.0D + randomRotation * 2000.0D) * 360.0F;
         cubes.add(new CubeFrame(targetPos.add(x, y, z), cubeSize, selfRotation, i, 1.0F));
      }

      RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
      BufferBuilder fill = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
      for (CubeFrame cube : cubes) {
         emitCube(matrices, cube, cameraPos, fill, fillColor, false);
      }
      BufferRenderer.drawWithGlobalProgram(fill.end());

      BufferBuilder lines = Tessellator.getInstance().begin(DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
      for (CubeFrame cube : cubes) {
         emitCube(matrices, cube, cameraPos, lines, lineColor, true);
      }
      BufferRenderer.drawWithGlobalProgram(lines.end());

      RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
      RenderSystem.setShaderTexture(0, Wyvern.id("trail/dashtrail/dashbloom.png"));
      BufferBuilder glow = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
      for (CubeFrame cube : cubes) {
         addCubeGlow(matrices, glow, cube.position, cameraPos, cube.size * 1.5F, glowColor);
      }
      BufferRenderer.drawWithGlobalProgram(glow.end());
      restoreCubeRenderState();
   }

   private void renderCubesOld(EventRender3D event, LivingEntity target) {
      float moduleAlpha = this.animation2.getValue();
      long now = System.currentTimeMillis();
      if (oldCubeTargetId != target.getId()) {
         oldCubeParticles.clear();
         oldCubeTargetId = target.getId();
         oldCubeLastSpawn = now;
      }

      while (now - oldCubeLastSpawn >= 20L && oldCubeParticles.size() < 50) {
         oldCubeLastSpawn += 20L;
         double angle = Math.random() * MathHelper.TAU;
         oldCubeParticles.add(new OldCubeParticle(
                 Math.cos(angle) * 0.7D,
                 0.04D + Math.random() * 0.16D,
                 Math.sin(angle) * 0.7D,
                 0.01D + Math.random() * 0.03D,
                 now,
                 (float) (Math.random() * 360.0D)
         ));
      }
      oldCubeParticles.removeIf(particle -> now - particle.bornAt > 1000L);
      if (oldCubeParticles.isEmpty()) return;

      Vec3d cameraPos = mc.gameRenderer.getCamera().getPos();
      Vec3d targetPos = new Vec3d(
              MathHelper.lerp(event.getPartialTicks(), target.lastRenderX, target.getX()),
              MathHelper.lerp(event.getPartialTicks(), target.lastRenderY, target.getY()),
              MathHelper.lerp(event.getPartialTicks(), target.lastRenderZ, target.getZ())
      );
      float hurt = MathHelper.sin(target.hurtTime * MathHelper.PI / 20.0F);
      ColorRGBA color = getTargetColor().mix(new ColorRGBA(200, 70, 70), hurt);

      RenderSystem.enableBlend();
      RenderSystem.blendFunc(770, 1);
      RenderSystem.disableCull();
      RenderSystem.enableDepthTest();
      RenderSystem.depthMask(false);
      RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
      List<CubeFrame> cubes = new ArrayList<>(oldCubeParticles.size());
      for (OldCubeParticle particle : oldCubeParticles) {
         float lifeAlpha = particle.alpha(now) * moduleAlpha;
         float ageSeconds = (now - particle.bornAt) / 1000.0F;
         Vec3d position = targetPos.add(particle.x, particle.y + particle.velocityY * ageSeconds * 60.0D, particle.z);
         cubes.add(new CubeFrame(position, 0.12F + 0.04F * lifeAlpha,
                 particle.rotation + (now - particle.bornAt) / 10.0F, -1, lifeAlpha));
      }

      BufferBuilder fill = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
      for (CubeFrame cube : cubes) {
         emitCube(event.getMatrix(), cube, cameraPos, fill,
                 color.withAlpha((int) (75.0F * cube.alpha)).getRGB(), false);
      }
      BufferRenderer.drawWithGlobalProgram(fill.end());

      BufferBuilder lines = Tessellator.getInstance().begin(DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
      for (CubeFrame cube : cubes) {
         emitCube(event.getMatrix(), cube, cameraPos, lines,
                 color.withAlpha((int) (230.0F * cube.alpha)).getRGB(), true);
      }
      BufferRenderer.drawWithGlobalProgram(lines.end());

      RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
      RenderSystem.setShaderTexture(0, Wyvern.id("trail/dashtrail/dashbloom.png"));
      BufferBuilder glow = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
      for (CubeFrame cube : cubes) {
         addCubeGlow(event.getMatrix(), glow, cube.position, cameraPos,
                 cube.size * 1.5F, color.withAlpha((int) (125.0F * cube.alpha)).getRGB());
      }
      BufferRenderer.drawWithGlobalProgram(glow.end());
      restoreCubeRenderState();
   }

   private void resetOldCubes() {
      this.oldCubeParticles.clear();
      this.oldCubeTargetId = Integer.MIN_VALUE;
      this.oldCubeLastSpawn = 0L;
   }

   private static void emitCube(MatrixStack matrices, CubeFrame cube, Vec3d camera,
                                BufferBuilder buffer, int color, boolean outline) {
      matrices.push();
      matrices.translate(cube.position.x - camera.x, cube.position.y - camera.y, cube.position.z - camera.z);
      applyCubeRotation(matrices, cube.rotationMode, cube.rotation);
      Matrix4f matrix = matrices.peek().getPositionMatrix();
      if (outline) drawCubeLines(buffer, matrix, color, cube.size);
      else drawColorCube(buffer, matrix, color, cube.size);
      matrices.pop();
   }

   private static void applyCubeRotation(MatrixStack matrices, int index, float rotation) {
      if (index < 0) {
         matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(rotation));
         matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotation));
         matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotation));
      } else if (index % 3 == 0) {
         matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotation));
         matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(rotation));
      } else if (index % 3 == 1) {
         matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotation));
         matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotation));
      } else {
         matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(rotation));
         matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotation));
      }
   }

   private static void addCubeGlow(MatrixStack matrices, BufferBuilder buffer, Vec3d position,
                                   Vec3d cameraPos, float size, int color) {
      matrices.push();
      matrices.translate(position.x - cameraPos.x, position.y - cameraPos.y, position.z - cameraPos.z);
      matrices.multiply(mc.gameRenderer.getCamera().getRotation());
      Matrix4f matrix = matrices.peek().getPositionMatrix();
      buffer.vertex(matrix, -size, size, 0.0F).texture(0.0F, 1.0F).color(color);
      buffer.vertex(matrix, size, size, 0.0F).texture(1.0F, 1.0F).color(color);
      buffer.vertex(matrix, size, -size, 0.0F).texture(1.0F, 0.0F).color(color);
      buffer.vertex(matrix, -size, -size, 0.0F).texture(0.0F, 0.0F).color(color);
      matrices.pop();
   }

   private static void drawColorCube(BufferBuilder buffer, Matrix4f matrix, int color, float size) {
      float s = size * 0.5F;
      buffer.vertex(matrix, -s, s, -s).color(color);
      buffer.vertex(matrix, -s, s, s).color(color);
      buffer.vertex(matrix, s, s, s).color(color);
      buffer.vertex(matrix, s, s, -s).color(color);
      buffer.vertex(matrix, -s, -s, -s).color(color);
      buffer.vertex(matrix, s, -s, -s).color(color);
      buffer.vertex(matrix, s, -s, s).color(color);
      buffer.vertex(matrix, -s, -s, s).color(color);
      buffer.vertex(matrix, -s, s, s).color(color);
      buffer.vertex(matrix, -s, -s, s).color(color);
      buffer.vertex(matrix, s, -s, s).color(color);
      buffer.vertex(matrix, s, s, s).color(color);
      buffer.vertex(matrix, -s, s, -s).color(color);
      buffer.vertex(matrix, s, s, -s).color(color);
      buffer.vertex(matrix, s, -s, -s).color(color);
      buffer.vertex(matrix, -s, -s, -s).color(color);
      buffer.vertex(matrix, -s, s, -s).color(color);
      buffer.vertex(matrix, -s, -s, -s).color(color);
      buffer.vertex(matrix, -s, -s, s).color(color);
      buffer.vertex(matrix, -s, s, s).color(color);
      buffer.vertex(matrix, s, s, -s).color(color);
      buffer.vertex(matrix, s, s, s).color(color);
      buffer.vertex(matrix, s, -s, s).color(color);
      buffer.vertex(matrix, s, -s, -s).color(color);
   }

   private static void drawCubeLines(BufferBuilder buffer, Matrix4f matrix, int color, float size) {
      float s = size * 0.5F;
      cubeLine(buffer, matrix, -s, -s, -s, s, -s, -s, color);
      cubeLine(buffer, matrix, s, -s, -s, s, -s, s, color);
      cubeLine(buffer, matrix, s, -s, s, -s, -s, s, color);
      cubeLine(buffer, matrix, -s, -s, s, -s, -s, -s, color);
      cubeLine(buffer, matrix, -s, s, -s, s, s, -s, color);
      cubeLine(buffer, matrix, s, s, -s, s, s, s, color);
      cubeLine(buffer, matrix, s, s, s, -s, s, s, color);
      cubeLine(buffer, matrix, -s, s, s, -s, s, -s, color);
      cubeLine(buffer, matrix, -s, -s, -s, -s, s, -s, color);
      cubeLine(buffer, matrix, s, -s, -s, s, s, -s, color);
      cubeLine(buffer, matrix, s, -s, s, s, s, s, color);
      cubeLine(buffer, matrix, -s, -s, s, -s, s, s, color);
   }

   private static void cubeLine(BufferBuilder buffer, Matrix4f matrix, float x1, float y1, float z1,
                                float x2, float y2, float z2, int color) {
      buffer.vertex(matrix, x1, y1, z1).color(color);
      buffer.vertex(matrix, x2, y2, z2).color(color);
   }

   private static void restoreCubeRenderState() {
      RenderSystem.depthMask(true);
      RenderSystem.enableDepthTest();
      RenderSystem.enableCull();
      RenderSystem.defaultBlendFunc();
      RenderSystem.disableBlend();
   }

   private record OldCubeParticle(double x, double y, double z, double velocityY,
                                  long bornAt, float rotation) {
      private float alpha(long now) {
         long age = now - bornAt;
         if (age <= 500L) return MathHelper.clamp(age / 500.0F, 0.0F, 1.0F);
         if (age <= 800L) return 1.0F;
         return MathHelper.clamp((1000L - age) / 200.0F, 0.0F, 1.0F);
      }
   }

   private record CubeFrame(Vec3d position, float size, float rotation, int rotationMode, float alpha) {
   }


   private ColorRGBA getTargetColor() {
      Theme theme = Wyvern.INSTANCE.getThemeManager().getCurrentTheme();
      return theme.getColor();
   }

   private ColorRGBA getTargetSecondColor() {
      Theme theme = Wyvern.INSTANCE.getThemeManager().getCurrentTheme();
      return theme.getSecondColor();
   }

   private boolean isBlockedByWall(Vec3d from, Vec3d to) {
      if (mc.world == null || mc.player == null) return false;
      return mc.world.raycast(new RaycastContext(
              from, to,
              RaycastContext.ShapeType.COLLIDER,
              RaycastContext.FluidHandling.NONE,
              mc.player
      )).getType() != HitResult.Type.MISS;
   }

   private void updateRotation() {
      if (!this.isReversing) {
         this.rotationSpeed += 0.01F;
         if ((double) this.rotationSpeed > 2.3D) {
            this.rotationSpeed = 2.3F;
            this.isReversing = true;
         }
      } else {
         this.rotationSpeed -= 0.01F;
         if ((double) this.rotationSpeed < -2.3D) {
            this.rotationSpeed = -2.3F;
            this.isReversing = false;
         }
      }

      this.rotationAngle += this.rotationSpeed;
      this.rotationAngle %= 360.0F;
   }

   private void renderNimbus(EventRender3D e) {
      if (this.lastTarget == null) return;

      MatrixStack matrices = e.getMatrix();
      Camera camera = mc.gameRenderer.getCamera();
      Vec3d camPos = camera.getPos();
      float tickDelta = e.getPartialTicks();
      float alpha = this.animation2.getValue();
      if (alpha <= 0.0F) return;

      double x = interpolate(this.lastTarget.getX(), this.lastTarget.lastRenderX, tickDelta);
      double y = interpolate(this.lastTarget.getY(), this.lastTarget.lastRenderY, tickDelta) + this.lastTarget.getHeight() + 0.22D;
      double z = interpolate(this.lastTarget.getZ(), this.lastTarget.lastRenderZ, tickDelta);
      float time = ((mc.player != null ? mc.player.age : 0) + tickDelta) * 0.08F;
      float radius = Math.max(0.42F, this.lastTarget.getWidth() * 0.9F);

      ColorRGBA first = getTargetColor();
      ColorRGBA second = getTargetSecondColor();

      RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
      RenderSystem.setShaderTexture(0, Wyvern.id("icons/glow.png"));
      RenderSystem.enableBlend();
      RenderSystem.blendFuncSeparate(770, 1, 0, 1);
      RenderSystem.disableCull();
      RenderSystem.disableDepthTest();
      RenderSystem.depthMask(false);

      BufferBuilder buffer = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

      for (int i = 0; i < 48; i++) {
         float prc = i / 48.0F;
         float angle = prc * 6.2831855F + time;
         float pulse = 0.65F + 0.35F * MathHelper.sin(time * 2.0F + prc * 6.2831855F);
         float px = MathHelper.cos(angle) * radius;
         float pz = MathHelper.sin(angle) * radius;
         float py = MathHelper.sin(angle * 2.0F + time) * 0.035F;
         ColorRGBA color = first.mix(second, prc).withAlpha((int) (alpha * 150.0F * pulse));
         float size = (0.08F + 0.04F * pulse) * alpha;

         matrices.push();
         matrices.translate(x + px - camPos.x, y + py - camPos.y, z + pz - camPos.z);
         matrices.multiply(camera.getRotation());
         addTexturedQuad(buffer, matrices.peek().getPositionMatrix(), size, color.getRGB());
         matrices.pop();
      }

      for (int i = 0; i < 8; i++) {
         float angle = i * 0.7853982F - time * 1.4F;
         float px = MathHelper.cos(angle) * radius * 0.82F;
         float pz = MathHelper.sin(angle) * radius * 0.82F;
         ColorRGBA color = second.mix(first, i / 8.0F).withAlpha((int) (alpha * 210.0F));
         float size = 0.18F * alpha;

         matrices.push();
         matrices.translate(x + px - camPos.x, y + 0.02F - camPos.y, z + pz - camPos.z);
         matrices.multiply(camera.getRotation());
         addTexturedQuad(buffer, matrices.peek().getPositionMatrix(), size, color.getRGB());
         matrices.pop();
      }

      BufferRenderer.drawWithGlobalProgram(buffer.end());
      RenderSystem.enableDepthTest();
      RenderSystem.depthMask(true);
      RenderSystem.disableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.enableCull();
   }

   private void renderRunes(EventRender3D e) {
      if (this.lastTarget == null) return;

      MatrixStack matrices = e.getMatrix();
      Camera camera = mc.gameRenderer.getCamera();
      Vec3d camPos = camera.getPos();
      float tickDelta = e.getPartialTicks();
      float alpha = this.animation2.getValue();
      if (alpha <= 0.0F) return;

      double x = interpolate(this.lastTarget.getX(), this.lastTarget.lastRenderX, tickDelta);
      double y = interpolate(this.lastTarget.getY(), this.lastTarget.lastRenderY, tickDelta);
      double z = interpolate(this.lastTarget.getZ(), this.lastTarget.lastRenderZ, tickDelta);
      float height = this.lastTarget.getHeight();
      float radius = Math.max(0.45F, this.lastTarget.getWidth() * 1.15F);
      float time = ((mc.player != null ? mc.player.age : 0) + tickDelta) * 0.12F;

      ColorRGBA first = getTargetColor();
      ColorRGBA second = getTargetSecondColor();

      RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
      RenderSystem.setShaderTexture(0, Wyvern.id("icons/glow.png"));
      RenderSystem.enableBlend();
      RenderSystem.blendFuncSeparate(770, 1, 0, 1);
      RenderSystem.disableCull();
      RenderSystem.disableDepthTest();
      RenderSystem.depthMask(false);

      BufferBuilder buffer = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

      for (int strand = 0; strand < 2; strand++) {
         float strandOffset = strand * 3.1415927F;
         for (int i = 0; i < 30; i++) {
            float prc = i / 29.0F;
            float vertical = prc * height;
            float angle = prc * 10.995575F + time + strandOffset;
            float waveRadius = radius * (0.86F + 0.12F * MathHelper.sin(time * 1.7F + prc * 6.2831855F));
            float px = MathHelper.cos(angle) * waveRadius;
            float pz = MathHelper.sin(angle) * waveRadius;
            float fade = MathHelper.sin(prc * 3.1415927F);
            ColorRGBA color = first.mix(second, strand == 0 ? prc : 1.0F - prc).withAlpha((int) (alpha * fade * 190.0F));
            float size = (0.09F + fade * 0.055F) * alpha;

            matrices.push();
            matrices.translate(x + px - camPos.x, y + vertical - camPos.y, z + pz - camPos.z);
            matrices.multiply(camera.getRotation());
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((time * 90.0F + i * 23.0F) % 360.0F));
            addTexturedQuad(buffer, matrices.peek().getPositionMatrix(), size, color.getRGB());
            matrices.pop();
         }
      }

      BufferRenderer.drawWithGlobalProgram(buffer.end());
      RenderSystem.enableDepthTest();
      RenderSystem.depthMask(true);
      RenderSystem.disableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.enableCull();
   }


   private void drawGhosts3(EventRender3D e) {
      if (this.lastTarget == null) return;

      float fadeAnim = this.animation.getValue();
      if (fadeAnim <= 0.0f) return;

      MatrixStack ms = e.getMatrix();
      Camera camera = mc.gameRenderer.getCamera();
      float tickDelta = e.getPartialTicks();
      float moving = ((mc.player != null ? mc.player.age : 0) + tickDelta) * 13.0f;

      Vec3d targetPos = new Vec3d(
              MathHelper.lerp(tickDelta, this.lastTarget.lastRenderX, this.lastTarget.getX()),
              MathHelper.lerp(tickDelta, this.lastTarget.lastRenderY, this.lastTarget.getY()),
              MathHelper.lerp(tickDelta, this.lastTarget.lastRenderZ, this.lastTarget.getZ())
      );

      float width = this.lastTarget.getWidth() * 1.5f;
      float entityHeight = this.lastTarget.getHeight();
      ColorRGBA themeColor = getTargetColor();

      RenderSystem.enableBlend();
      RenderSystem.blendFuncSeparate(770, 1, 0, 1);
      RenderSystem.enableDepthTest();
      if (mc.world != null && mc.player != null) {
         Vec3d targetEye = targetPos.add(0.0, entityHeight * 0.85, 0.0);
         if (mc.world.raycast(new RaycastContext(
                 camera.getPos(),
                 targetEye,
                 RaycastContext.ShapeType.COLLIDER,
                 RaycastContext.FluidHandling.NONE,
                 mc.player
         )).getType() != HitResult.Type.MISS) {
            RenderSystem.disableDepthTest();
         }
      }
      RenderSystem.disableCull();
      RenderSystem.depthMask(false);

      RenderSystem.setShaderTexture(0, Wyvern.id("icons/glow.png"));
      RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);

      BufferBuilder builder = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

      int step = 2;
      int wormTick = 0;
      int wormCD = 0;

      for (int i = 0; i < 360; i += step) {
         float size = 0.13f + 0.005f * wormTick;
         float bigSize = 0.7f + 0.005f * wormTick;

         if (wormCD > 0) {
            wormCD -= step;
            continue;
         }

         if ((wormTick += step) > 50) {
            wormCD = 100;
            wormTick = 0;
            continue;
         }

         float val = Math.max(0.5f, 1.2f - 0.5f * fadeAnim);
         float sin = (float) (Math.sin(Math.toRadians(i + moving)) * width * val);
         float cos = (float) (Math.cos(Math.toRadians(i + moving)) * width * val);
         float yAnim = (float) Math.sin(Math.toRadians(i / 2.0f + moving / 5.0f));
         float whiteMix = 0.18F + 0.62F * MathHelper.clamp(wormTick / 50.0F, 0.0F, 1.0F);
         ColorRGBA ghostColor = themeColor.mix(ColorRGBA.WHITE, whiteMix);
         int glowArgb = ghostColor.withAlpha((int) (fadeAnim * 255.0F * 0.08F)).getRGB();
         int coreArgb = ghostColor.mix(ColorRGBA.WHITE, 0.28F)
                 .withAlpha((int) (fadeAnim * 255.0F)).getRGB();

         ms.push();
         ms.translate(
                 targetPos.x + sin - camera.getPos().x,
                 targetPos.y + (entityHeight / 1.5f) + (entityHeight / 3.0f) * yAnim - camera.getPos().y,
                 targetPos.z + cos - camera.getPos().z
         );
         ms.multiply(camera.getRotation());

         Matrix4f matrix = ms.peek().getPositionMatrix();
         float glowHalf = bigSize / 2.0f;
         this.addTexturedQuad(builder, matrix, glowHalf, glowArgb);
         float coreHalf = size / 2.0f;
         this.addTexturedQuad(builder, matrix, coreHalf, coreArgb);

         ms.pop();
      }

      BufferRenderer.drawWithGlobalProgram(builder.end());

      RenderSystem.enableCull();
      RenderSystem.depthMask(true);
      RenderSystem.enableDepthTest();
      RenderSystem.defaultBlendFunc();
      RenderSystem.disableBlend();
   }

   private void addTexturedQuad(BufferBuilder builder, Matrix4f matrix, float half, int argb) {
      int a = (argb >> 24) & 0xFF;
      int r = (argb >> 16) & 0xFF;
      int g = (argb >> 8) & 0xFF;
      int b = argb & 0xFF;
      builder.vertex(matrix, -half, -half, 0.0F).texture(0.0F, 1.0F).color(r, g, b, a);
      builder.vertex(matrix, half, -half, 0.0F).texture(1.0F, 1.0F).color(r, g, b, a);
      builder.vertex(matrix, half, half, 0.0F).texture(1.0F, 0.0F).color(r, g, b, a);
      builder.vertex(matrix, -half, half, 0.0F).texture(0.0F, 0.0F).color(r, g, b, a);
   }


   private void drawGhostOrbits(EventRender3D e) {
      if (this.lastTarget == null) return;

      MatrixStack matrices = e.getMatrix();
      Vec3d camPos = mc.gameRenderer.getCamera().getPos();
      float delta = e.getPartialTicks();
      Camera camera = mc.gameRenderer.getCamera();

      double tx = interpolate(this.lastTarget.getX(), this.lastTarget.lastRenderX, delta);
      double ty = interpolate(this.lastTarget.getY(), this.lastTarget.lastRenderY, delta);
      double tz = interpolate(this.lastTarget.getZ(), this.lastTarget.lastRenderZ, delta);
      Vec3d targetCenter = new Vec3d(tx, ty + this.lastTarget.getHeight() / 2.0, tz);

      long now = System.currentTimeMillis();
      if (lastOrbitTime == 0) lastOrbitTime = now;
      float dtMs = now - lastOrbitTime;
      lastOrbitTime = now;

      float fpsFactor = 500 / Math.max(mc.getCurrentFps(), 10);
      movingAngle += (20.0f * dtMs / 16.667f) * (ORBIT_SPEED / 55.0f);

      boolean isHurt = false;
      if (this.lastTarget instanceof LivingEntity living) isHurt = living.hurtTime > 7;
      orbitShrinkAnim.update(isHurt);
      float shrinkValue = orbitShrinkAnim.getValue();

      RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
      RenderSystem.setShaderTexture(0, Wyvern.id("icons/glow.png"));
      RenderSystem.enableBlend();
      RenderSystem.blendFunc(770, 1);
      RenderSystem.disableCull();
      if (this.isBlockedByWall(camPos, targetCenter)) {
         RenderSystem.disableDepthTest();
      }
      RenderSystem.depthMask(false);

      BufferBuilder buffer = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

      ColorRGBA baseColor = getTargetColor();

      for (int i = 0; i < ORBIT_PARTICLE_COUNT; i++) {
         float angleOffset = i * 360f / ORBIT_PARTICLE_COUNT;
         float currentAngle = movingAngle + angleOffset;
         double radian = Math.toRadians(currentAngle);

         float orbitRadius = ORBIT_BASE_RADIUS - shrinkValue * ORBIT_BASE_RADIUS;
         float ox = (float) Math.sin(radian) * orbitRadius;
         float oz = (float) Math.cos(radian) * orbitRadius;
         double oy = 0.3 * Math.sin(Math.toRadians(movingAngle / (i + 1.0f)));

         Vec3d targetGhostPos = targetCenter.add(ox, oy, oz);

         if (orbitPositions[i] == null || orbitPositions[i].distanceTo(targetGhostPos) > 10) {
            orbitPositions[i] = targetGhostPos;
            orbitMotions[i] = Vec3d.ZERO;
         }

         float mul = ORBIT_BASE_MUL * fpsFactor;
         Vec3d diff = targetGhostPos.subtract(orbitPositions[i]);
         orbitMotions[i] = diff.multiply(mul, mul, mul);
         orbitPositions[i] = orbitPositions[i].add(orbitMotions[i]);

         if (orbitTrails[i].isEmpty() || orbitTrails[i].get(0).distanceTo(orbitPositions[i]) > 0.01) {
            orbitTrails[i].add(0, orbitPositions[i]);
            while (orbitTrails[i].size() > ORBIT_TRAIL_LENGTH) orbitTrails[i].remove(orbitTrails[i].size() - 1);
         }

         for (int j = 0; j < orbitTrails[i].size(); j++) {
            Vec3d p = orbitTrails[i].get(j);
            float offset = 1.0f - (float) j / ORBIT_TRAIL_LENGTH;

            matrices.push();
            matrices.translate(p.x - camPos.x, p.y - camPos.y, p.z - camPos.z);
            matrices.multiply(camera.getRotation());
            Matrix4f matrix = matrices.peek().getPositionMatrix();

            float opacity = (float) Math.pow(offset, 1.8) * this.animation2.getValue() * 0.7f;
            float whiteMix = 0.20F + 0.62F * offset;
            int color = baseColor.mix(ColorRGBA.WHITE, whiteMix)
                    .withAlpha((int)(opacity * 255)).getRGB();
            float scale = SCALE_CACHE[Math.min((int) (offset * 100), 100)] * 0.8f;

            buffer.vertex(matrix, -scale, scale, 0).texture(0f, 1f).color(color);
            buffer.vertex(matrix, scale, scale, 0).texture(1f, 1f).color(color);
            buffer.vertex(matrix, scale, -scale, 0).texture(1f, 0f).color(color);
            buffer.vertex(matrix, -scale, -scale, 0).texture(0f, 0f).color(color);
            matrices.pop();
         }

         if (!orbitTrails[i].isEmpty()) {
            Vec3d head = orbitTrails[i].get(0);
            matrices.push();
            matrices.translate(head.x - camPos.x, head.y - camPos.y, head.z - camPos.z);
            matrices.multiply(camera.getRotation());
            Matrix4f matrix = matrices.peek().getPositionMatrix();

            float headScale = 0.35f * this.animation2.getValue();
            int headColor = baseColor.mix(ColorRGBA.WHITE, 0.86F)
                    .withAlpha((int)(120 * this.animation2.getValue())).getRGB();

            buffer.vertex(matrix, -headScale, headScale, 0).texture(0f, 1f).color(headColor);
            buffer.vertex(matrix, headScale, headScale, 0).texture(1f, 1f).color(headColor);
            buffer.vertex(matrix, headScale, -headScale, 0).texture(1f, 0f).color(headColor);
            buffer.vertex(matrix, -headScale, -headScale, 0).texture(0f, 0f).color(headColor);
            matrices.pop();
         }
      }

      BufferRenderer.drawWithGlobalProgram(buffer.end());
      RenderSystem.enableDepthTest();
      RenderSystem.depthMask(true);
      RenderSystem.disableBlend();
      RenderSystem.blendFunc(770, 771);
      RenderSystem.enableCull();
   }


   private void renderCrystals(EventRender3D e) {
      float alpha = this.animation2.getValue();
      if (alpha <= 0.0F) return;
      if (this.lastTarget == null) return;
      if (mc.player == null) return;

      MatrixStack matrices = e.getMatrix();
      Vec3d camPos = mc.gameRenderer.getCamera().getPos();
      float tickDelta = e.getPartialTicks();

      double tx = interpolate(this.lastTarget.getX(), this.lastTarget.lastRenderX, tickDelta);
      double ty = interpolate(this.lastTarget.getY(), this.lastTarget.lastRenderY, tickDelta);
      double tz = interpolate(this.lastTarget.getZ(), this.lastTarget.lastRenderZ, tickDelta);

      crystalMoving += 1.0f;

      float entityHeight = this.lastTarget.getHeight();
      float entityWidth = this.lastTarget.getWidth();
      float width = entityWidth * 1.5f;

      ColorRGBA themeColor = getTargetColor();

      matrices.push();
      matrices.translate(tx - camPos.x, ty - camPos.y, tz - camPos.z);

      RenderSystem.disableBlend();
      RenderSystem.disableCull();
      if (this.isBlockedByWall(camPos, new Vec3d(tx, ty + entityHeight * 0.5D, tz))) {
         RenderSystem.disableDepthTest();
      }
      RenderSystem.depthMask(false);

      RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);

      BufferBuilder crystalBuffer = Tessellator.getInstance().begin(
              DrawMode.TRIANGLES, VertexFormats.POSITION_COLOR);

      int color = themeColor.withAlpha(255).getRGB();

      int cTop = color;
      int cSide1 = color;
      int cSide2 = color;
      int cBot = color;

      float cw = 0.085f;
      float ch = 0.22f;

      for (int i = 0; i < 360; i += 19) {
         float val = 1.2f - 0.5f * alpha;
         float angleDeg = i + crystalMoving * 0.3f;
         float angleRad = (float) Math.toRadians(angleDeg);
         float sin = (float) (Math.sin(angleRad) * width * val);
         float cos = (float) (Math.cos(angleRad) * width * val);

         float heightPrc = ((i / 20.0f) * 0.6180339f) % 1.0f;
         float crystalY = entityHeight * heightPrc;

         matrices.push();
         matrices.translate(sin, crystalY, cos);

         Vector3f dir = new Vector3f(-sin, 0, -cos).normalize();
         Quaternionf rotation = new Quaternionf().rotationTo(new Vector3f(0, 1, 0), dir);
         matrices.multiply(rotation);

         Matrix4f matrix = matrices.peek().getPositionMatrix();

         float[] ex = {cw, 0, -cw, 0};
         float[] ez = {0, cw, 0, -cw};

         for (int j = 0; j < 4; j++) {
            int next = (j + 1) % 4;
            int fc = (j % 2 == 0) ? cTop : cSide1;
            crystalBuffer.vertex(matrix, 0, ch, 0).color(fc);
            crystalBuffer.vertex(matrix, ex[j], 0, ez[j]).color(fc);
            crystalBuffer.vertex(matrix, ex[next], 0, ez[next]).color(fc);
         }

         for (int j = 0; j < 4; j++) {
            int next = (j + 1) % 4;
            int fc = (j % 2 == 0) ? cBot : cSide2;
            crystalBuffer.vertex(matrix, 0, -ch, 0).color(fc);
            crystalBuffer.vertex(matrix, ex[next], 0, ez[next]).color(fc);
            crystalBuffer.vertex(matrix, ex[j], 0, ez[j]).color(fc);
         }

         matrices.pop();
      }

      BufferRenderer.drawWithGlobalProgram(crystalBuffer.end());

      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
      RenderSystem.setShaderTexture(0, Wyvern.id("icons/glow.png"));

      BufferBuilder glowBuffer = Tessellator.getInstance().begin(
              DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
      Camera camera = mc.gameRenderer.getCamera();
      int glowColor = themeColor.withAlpha(Math.min(55, (int) (55.0F * alpha))).getRGB();
      float glowSize = 0.3F;

      for (int i = 0; i < 360; i += 19) {
         float val = 1.2F - 0.5F * alpha;
         float angleRad = (float) Math.toRadians(i + crystalMoving * 0.3F);
         float sin = (float) (Math.sin(angleRad) * width * val);
         float cos = (float) (Math.cos(angleRad) * width * val);
         float crystalY = entityHeight * (((i / 20.0F) * 0.6180339F) % 1.0F);

         matrices.push();
         matrices.translate(sin, crystalY, cos);
         matrices.multiply(camera.getRotation());
         Matrix4f matrix = matrices.peek().getPositionMatrix();

         glowBuffer.vertex(matrix, -glowSize, glowSize, 0.0F).texture(0.0F, 1.0F).color(glowColor);
         glowBuffer.vertex(matrix, glowSize, glowSize, 0.0F).texture(1.0F, 1.0F).color(glowColor);
         glowBuffer.vertex(matrix, glowSize, -glowSize, 0.0F).texture(1.0F, 0.0F).color(glowColor);
         glowBuffer.vertex(matrix, -glowSize, -glowSize, 0.0F).texture(0.0F, 0.0F).color(glowColor);
         matrices.pop();
      }

      BufferRenderer.drawWithGlobalProgram(glowBuffer.end());

      matrices.pop();

      RenderSystem.enableCull();
      RenderSystem.depthMask(true);
      RenderSystem.enableDepthTest();
      RenderSystem.defaultBlendFunc();
      RenderSystem.disableBlend();
   }

   private void renderCrystall2(EventRender3D event, LivingEntity target) {
      float alpha = this.animation2.getValue();
      if (alpha <= 0.0F || mc.player == null) return;

      MatrixStack matrices = event.getMatrix();
      Camera camera = mc.gameRenderer.getCamera();
      Vec3d cameraPos = camera.getPos();
      float tickDelta = event.getPartialTicks();
      Vec3d renderPos = new Vec3d(
              MathHelper.lerp(tickDelta, target.lastRenderX, target.getX()),
              MathHelper.lerp(tickDelta, target.lastRenderY, target.getY()),
              MathHelper.lerp(tickDelta, target.lastRenderZ, target.getZ())
      );

      float width = target.getWidth() * 1.5F;
      float height = target.getHeight();
      ColorRGBA renderColor = getTargetColor();
      float timeSeconds = System.currentTimeMillis() * 0.001F;
      float rotationRadians = (float) Math.toRadians((System.currentTimeMillis() % 360000L) / 7.25F);
      float sinRotation = MathHelper.sin(rotationRadians);
      float cosRotation = MathHelper.cos(rotationRadians);
      float radiusFactor = 1.25F - 0.5F * alpha;

      List<Vector3f> positions = new ArrayList<>();
      List<Vector3f> points = buildCrystall2Points(target, width, height);
      for (int i = 0; i < points.size(); i++) {
         Vector3f point = points.get(i);
         float x = (point.x * cosRotation - point.z * sinRotation) * radiusFactor;
         float z = (point.x * sinRotation + point.z * cosRotation) * radiusFactor;
         float y = point.y + 0.05F * MathHelper.sin(timeSeconds * 2.0F + i * 1337.0F);
         positions.add(new Vector3f(x, y, z));
      }

      matrices.push();
      matrices.translate(renderPos.x - cameraPos.x, renderPos.y - cameraPos.y, renderPos.z - cameraPos.z);
      RenderSystem.enableBlend();
      RenderSystem.blendFunc(770, 1);
      RenderSystem.disableCull();
      if (this.isBlockedByWall(cameraPos, renderPos.add(0.0D, height * 0.5D, 0.0D))) {
         RenderSystem.disableDepthTest();
      }
      RenderSystem.depthMask(false);

      try {
         BufferBuilder crystals = CrystalRenderer.createBuffer();
         ColorRGBA crystalColor = renderColor.withAlpha((int) (255.0F * alpha));
         for (Vector3f position : positions) {
            matrices.push();
            matrices.translate(position.x, position.y, position.z);
            Vector3f direction = new Vector3f(
                    -position.x,
                    height * 0.5F - position.y,
                    -position.z
            );
            if (direction.lengthSquared() > 0.0001F) {
               direction.normalize();
               matrices.multiply(new Quaternionf().rotationTo(new Vector3f(0.0F, 1.0F, 0.0F), direction));
            }
            CrystalRenderer.render(matrices, crystals, 0.0F, 0.0F, 0.0F, 0.1F, crystalColor);
            matrices.pop();
         }
         BufferRenderer.drawWithGlobalProgram(crystals.end());

         RenderSystem.setShaderTexture(0, Wyvern.id("icons/bloom.png"));
         RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
         BufferBuilder glow = RenderSystem.renderThreadTesselator().begin(
                 DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
         int glowColor = renderColor.withAlpha((int) (255.0F * alpha * 0.2F)).getRGB();
         float glowSize = 1.2F;
         for (Vector3f position : positions) {
            matrices.push();
            matrices.translate(position.x, position.y, position.z);
            matrices.multiply(camera.getRotation());
            Matrix4f matrix = matrices.peek().getPositionMatrix();
            float half = glowSize * 0.5F;
            glow.vertex(matrix, -half, half, 0.0F).texture(0.0F, 1.0F).color(glowColor);
            glow.vertex(matrix, half, half, 0.0F).texture(1.0F, 1.0F).color(glowColor);
            glow.vertex(matrix, half, -half, 0.0F).texture(1.0F, 0.0F).color(glowColor);
            glow.vertex(matrix, -half, -half, 0.0F).texture(0.0F, 0.0F).color(glowColor);
            matrices.pop();
         }
         BufferRenderer.drawWithGlobalProgram(glow.end());
      } finally {
         matrices.pop();
         RenderSystem.depthMask(true);
         RenderSystem.enableDepthTest();
         RenderSystem.enableCull();
         RenderSystem.defaultBlendFunc();
         RenderSystem.disableBlend();
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      }
   }

   private List<Vector3f> buildCrystall2Points(LivingEntity target, float width, float height) {
      // The sample set is fully determined by the target id and its size (seeded Random),
      // so regenerating it every frame produced the exact same points. Cache it and only
      // rebuild when the target or its dimensions change.
      if (this.crystall2Points != null && this.crystall2TargetId == target.getId()
            && this.crystall2Width == width && this.crystall2Height == height) {
         return this.crystall2Points;
      }

      int count = Math.max(8, (int) (width + height * 12.0F));
      Random random = new Random((long) target.getId() * 133769420L);
      List<Vector3f> points = new ArrayList<>(count);

      for (int i = 0; i < count; i++) {
         Vector3f best = null;
         float bestDistanceSquared = -1.0F;
         for (int attempt = 0; attempt < 15; attempt++) {
            float angle = random.nextFloat() * MathHelper.TAU;
            float x = MathHelper.sin(angle) * width;
            float z = MathHelper.cos(angle) * width;
            float y = random.nextFloat() * height;

            if (points.isEmpty()) {
               best = new Vector3f(x, y, z);
               break;
            }

            float nearest = Float.MAX_VALUE;
            for (Vector3f existing : points) {
               float dx = existing.x - x;
               float dy = existing.y - y;
               float dz = existing.z - z;
               nearest = Math.min(nearest, dx * dx + dy * dy + dz * dz);
            }
            if (nearest > bestDistanceSquared) {
               bestDistanceSquared = nearest;
               best = new Vector3f(x, y, z);
            }
         }
         if (best != null) points.add(best);
      }

      this.crystall2Points = points;
      this.crystall2TargetId = target.getId();
      this.crystall2Width = width;
      this.crystall2Height = height;
      return points;
   }

   private void drawSpiritsTrack(EventRender3D event3D) {
      if (!this.mode.is("Призраки")) {
         return;
      }

      Aura aura = Aura.INSTANCE;
      this.animation2.update(aura.getTarget() != null && aura.isEnabled());
      if ((double) this.animation2.getValue() == 0.0D) {
         return;
      }

      if (aura.getTarget() != null) {
         if (this.lastTarget != aura.getTarget()) {
            this.currentTime = 0L;
         }

         this.lastTarget = aura.getTarget();
      }

      if (this.lastTarget == null) {
         return;
      }

      long now = System.currentTimeMillis();
      if (this.currentTime == 0L) {
         this.currentTime = now;
      }
      // Clamp gaps caused by pausing or switching modes so the spirits do not
      // jump around the target when rendering resumes.
      long frameTime = Math.max(0L, Math.min(50L, now - this.currentTime));
      this.animationNurik = (this.animationNurik
              + frameTime * SPIRIT_ANGULAR_SPEED_PER_MS) % MathHelper.TAU;
      this.currentTime = now;

      MatrixStack e = event3D.getMatrix();
      Camera camera = mc.gameRenderer.getCamera();
      RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
      RenderSystem.setShaderTexture(0, Wyvern.id("icons/glow.png"));
      RenderSystem.enableBlend();
      RenderSystem.blendFuncSeparate(770, 1, 0, 1);
      RenderSystem.disableCull();
      // Always visible: rendered after entities with depth test off, so the
      // spirits show through walls and through the target model alike.
      RenderSystem.disableDepthTest();
      RenderSystem.depthMask(false);
      Vec3d camPos = camera.getPos();
      double x = interpolate(this.lastTarget.getX(), this.lastTarget.lastRenderX, (double) event3D.getPartialTicks()) - camPos.getX();
      double y = interpolate(this.lastTarget.getY(), this.lastTarget.lastRenderY, (double) event3D.getPartialTicks()) - camPos.getY();
      double z = interpolate(this.lastTarget.getZ(), this.lastTarget.lastRenderZ, (double) event3D.getPartialTicks()) - camPos.getZ();
      int n2 = 3;
      int n3 = 12;
      int n4 = 3 * n2;
      e.push();
      BufferBuilder buffer = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

      for (int i = 0; i < n4; i += n2) {
         for (int j = 0; j < n3; ++j) {
            ColorRGBA color = getColor(aura.getTarget());
            float f2 = this.animationNurik + (float) j * 0.1F;
            float f3 = SPIRIT_ORBIT_RADIUS;
            float f4 = 0.5F;
            int n5 = (int) Math.pow((double) i, 2.0D);
            e.push();
            e.translate(x + (double) (f3 * MathHelper.sin(f2 + (float) n5)), y + (double) f4 + (double) (0.3F * MathHelper.sin(this.animationNurik + (float) j * 0.2F)) + (double) (0.2F * (float) i), z + (double) (f3 * MathHelper.cos(f2 - (float) n5)));
            float spiritScale = this.animation2.getValue()
                    * (0.005F + (float) j / 2000.0F)
                    * SPIRIT_SIZE_MULTIPLIER;
            e.scale(spiritScale, spiritScale, spiritScale);
            e.multiply(camera.getRotation());
            int n7 = -25;
            int n8 = 60;
            int alpha = (int) (this.animation2.getValue() * 225.0F);
            buffer.vertex(e.peek().getPositionMatrix(), (float) n7, (float) (n7 + n8), 0.0F).texture(0.0F, 1.0F).color(color.withAlpha(alpha).getRGB());
            buffer.vertex(e.peek().getPositionMatrix(), (float) (n7 + n8), (float) (n7 + n8), 0.0F).texture(1.0F, 1.0F).color(color.withAlpha(alpha).getRGB());
            buffer.vertex(e.peek().getPositionMatrix(), (float) (n7 + n8), (float) n7, 0.0F).texture(1.0F, 0.0F).color(color.withAlpha(alpha).getRGB());
            buffer.vertex(e.peek().getPositionMatrix(), (float) n7, (float) n7, 0.0F).texture(0.0F, 0.0F).color(color.withAlpha(alpha).getRGB());
            // Wide faint halo around the core: additive light now spreads beyond
            // the quad, which reads as a stronger glow instead of a filled circle.
            e.push();
            e.scale(2.0F, 2.0F, 2.0F);
            int haloAlpha = (int) (this.animation2.getValue() * 140.0F);
            buffer.vertex(e.peek().getPositionMatrix(), (float) n7, (float) (n7 + n8), 0.0F).texture(0.0F, 1.0F).color(color.withAlpha(haloAlpha).getRGB());
            buffer.vertex(e.peek().getPositionMatrix(), (float) (n7 + n8), (float) (n7 + n8), 0.0F).texture(1.0F, 1.0F).color(color.withAlpha(haloAlpha).getRGB());
            buffer.vertex(e.peek().getPositionMatrix(), (float) (n7 + n8), (float) n7, 0.0F).texture(1.0F, 0.0F).color(color.withAlpha(haloAlpha).getRGB());
            buffer.vertex(e.peek().getPositionMatrix(), (float) n7, (float) n7, 0.0F).texture(0.0F, 0.0F).color(color.withAlpha(haloAlpha).getRGB());
            e.pop();
            e.pop();
         }
      }

      BufferRenderer.drawWithGlobalProgram(buffer.end());
      e.pop();
      RenderSystem.enableDepthTest();
      RenderSystem.depthMask(true);
      RenderSystem.disableBlend();
      RenderSystem.blendFunc(770, 771);
      RenderSystem.enableCull();
   }

   private void drawCelestialSpirals(EventRender3D event, LivingEntity target, float fade) {
      MatrixStack matrices = event.getMatrix();
      Camera camera = mc.gameRenderer.getCamera();
      Vec3d camPos = camera.getPos();
      float tickDelta = event.getPartialTicks();

      Vec3d targetPos = new Vec3d(
              MathHelper.lerp(tickDelta, target.lastRenderX, target.getX()),
              MathHelper.lerp(tickDelta, target.lastRenderY, target.getY()),
              MathHelper.lerp(tickDelta, target.lastRenderZ, target.getZ())
      );

      float height = target.getHeight();
      float radius = target.getWidth();
      float timeSeconds = ((mc.world != null ? (float) mc.world.getTime() : 0.0F) + tickDelta) / 20.0F;
      float centerY = height / 2.0F + 0.2F;

      ColorRGBA colorA = getTargetColor();
      ColorRGBA colorB = getTargetSecondColor();

      RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
      RenderSystem.setShaderTexture(0, Wyvern.id("icons/glow.png"));
      RenderSystem.enableBlend();
      RenderSystem.blendFuncSeparate(770, 1, 0, 1);
      RenderSystem.disableCull();
      RenderSystem.disableDepthTest();
      RenderSystem.depthMask(false);

      BufferBuilder buffer = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

      double time = timeSeconds;
      double spin = time * 3.0 % (MathHelper.TAU);
      double arcStep = CELESTIAL_ESP_ARC / CELESTIAL_ESP_SEGMENTS;

      for (int arm = 0; arm < CELESTIAL_ESP_ARMS; arm++) {
         double verticalTime = time + arm * 15.0;
         double armPhase = arm * (Math.PI / 2.0);

         for (int seg = 0; seg <= CELESTIAL_ESP_SEGMENTS; seg++) {
            double arc = seg * arcStep;
            double angle = arc + spin + armPhase;
            double px = Math.cos(angle) * radius;
            double pz = Math.sin(angle) * radius;
            double py = Math.sin(verticalTime + arc + arm) * CELESTIAL_ESP_VERTICAL_AMPLITUDE + centerY;

            double originalIndex = seg * CELESTIAL_ESP_SEGMENTS;
            double phase = time * 1.5 + originalIndex * 0.035;
            double mix = 0.5 + 0.5 * Math.sin(phase);
            ColorRGBA color = colorA.mix(colorB, (float) mix).withAlpha((int) (255.0 * fade));
            float size = 0.4F * (0.5F + (seg / (float) CELESTIAL_ESP_SEGMENTS));
            float half = size * 0.5F;

            matrices.push();
            matrices.translate(targetPos.x + px - camPos.x, targetPos.y + py - camPos.y, targetPos.z + pz - camPos.z);
            matrices.multiply(camera.getRotation());
            Matrix4f matrix = matrices.peek().getPositionMatrix();
            buffer.vertex(matrix, -half, -half, 0.0F).texture(0.0F, 1.0F).color(color.getRGB());
            buffer.vertex(matrix, half, -half, 0.0F).texture(1.0F, 1.0F).color(color.getRGB());
            buffer.vertex(matrix, half, half, 0.0F).texture(1.0F, 0.0F).color(color.getRGB());
            buffer.vertex(matrix, -half, half, 0.0F).texture(0.0F, 0.0F).color(color.getRGB());
            matrices.pop();
         }
      }

      BufferRenderer.drawWithGlobalProgram(buffer.end());

      RenderSystem.enableDepthTest();
      RenderSystem.depthMask(true);
      RenderSystem.disableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.enableCull();
   }

   @EventTarget
   private void onRenderExtraTargetModes(EventRender3D e) {
      if (this.mode.is("Кольцо")) {
         Entity auraTarget = Aura.INSTANCE.isEnabled() ? Aura.INSTANCE.getTarget() : null;
         if (auraTarget == null) {
            this.lastTarget = null;
            return;
         }

         this.lastTarget = auraTarget;
         this.renderTargetRing(e);
         return;
      }

       return;
   }

   private void renderTargetRing(EventRender3D e) {
      if (this.lastTarget == null) return;

      double tickDelta = e.getPartialTicks();
      Vec3d camera = mc.gameRenderer.getCamera().getPos();
      double x = interpolate(this.lastTarget.getX(), this.lastTarget.lastRenderX, tickDelta) - camera.x;
      double y = interpolate(this.lastTarget.getY(), this.lastTarget.lastRenderY, tickDelta) - camera.y;
      double z = interpolate(this.lastTarget.getZ(), this.lastTarget.lastRenderZ, tickDelta) - camera.z;
      float height = this.lastTarget.getHeight();

      double duration = 2000.0D;
      double elapsed = System.currentTimeMillis() % duration;
      boolean side = elapsed > duration / 2.0D;
      double progress = elapsed / (duration / 2.0D);
      progress = side ? progress - 1.0D : 1.0D - progress;
      progress = progress < 0.5D
              ? 2.0D * progress * progress
              : 1.0D - Math.pow(-2.0D * progress + 2.0D, 2.0D) / 2.0D;
      double eased = height / 2.0D
              * (progress > 0.5D ? 1.0D - progress : progress)
              * (side ? -1.0D : 1.0D);

      double ringY = y + height * progress;
      double trailY = ringY + eased;
      double radius = this.lastTarget.getWidth() * 0.8D;
      ColorRGBA themeColor = this.getTargetColor();
      int ringColor = themeColor.withAlpha(255).getRGB();
      int trailEndColor = themeColor.withAlpha(0).getRGB();

      MatrixStack matrices = e.getMatrix();
      matrices.push();
      Matrix4f matrix = matrices.peek().getPositionMatrix();

      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.disableCull();
      if (this.isBlockedByWall(camera, new Vec3d(x + camera.x, y + camera.y, z + camera.z))) {
         RenderSystem.disableDepthTest();
      }
      RenderSystem.depthMask(false);
      RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
      RenderSystem.lineWidth(1.5F);
      GL11.glEnable(GL11.GL_LINE_SMOOTH);
      GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);

      BufferBuilder curtain = Tessellator.getInstance().begin(
              DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);
      for (int i = 0; i <= 360; i++) {
         double angle = Math.toRadians(i);
         float px = (float) (x + Math.cos(angle) * radius);
         float pz = (float) (z + Math.sin(angle) * radius);
         curtain.vertex(matrix, px, (float) ringY, pz).color(ringColor);
         curtain.vertex(matrix, px, (float) trailY, pz).color(trailEndColor);
      }
      BufferRenderer.drawWithGlobalProgram(curtain.end());



      RenderSystem.defaultBlendFunc();
      RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
      double glowWidth = Math.min(0.055D, radius * 0.16D);
      double innerRadius = Math.max(0.0D, radius - glowWidth);
      int glowEdgeColor = themeColor.withAlpha(165).getRGB();
      int glowInnerColor = themeColor.withAlpha(0).getRGB();
      BufferBuilder innerGlow = Tessellator.getInstance().begin(
              DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);

      for (int i = 0; i <= 360; i++) {
         double angle = Math.toRadians(i);
         double cos = Math.cos(angle);
         double sin = Math.sin(angle);
         innerGlow.vertex(matrix,
                 (float) (x + cos * radius),
                 (float) ringY,
                 (float) (z + sin * radius)).color(glowEdgeColor);
         innerGlow.vertex(matrix,
                 (float) (x + cos * innerRadius),
                 (float) ringY,
                 (float) (z + sin * innerRadius)).color(glowInnerColor);
      }
      BufferRenderer.drawWithGlobalProgram(innerGlow.end());

      RenderSystem.defaultBlendFunc();
      RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
      BufferBuilder line = Tessellator.getInstance().begin(
              DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION_COLOR);
      for (int i = 0; i <= 360; i++) {
         double angle = Math.toRadians(i);
         float px = (float) (x + Math.cos(angle) * radius);
         float pz = (float) (z + Math.sin(angle) * radius);
         line.vertex(matrix, px, (float) ringY, pz).color(ringColor);
      }
      BufferRenderer.drawWithGlobalProgram(line.end());
      matrices.pop();

      RenderSystem.depthMask(true);
      RenderSystem.enableDepthTest();
      RenderSystem.enableCull();
      GL11.glDisable(GL11.GL_LINE_SMOOTH);
      RenderSystem.lineWidth(1.0F);
      RenderSystem.defaultBlendFunc();
      RenderSystem.disableBlend();
   }

   private ColorRGBA getColor(LivingEntity entity) {
      Theme theme = Wyvern.INSTANCE.getThemeManager().getCurrentTheme();
      return theme.getColor();
   }

   public static double interpolate(double current, double old, double scale) {
      return old + (current - old) * scale;
   }
}
