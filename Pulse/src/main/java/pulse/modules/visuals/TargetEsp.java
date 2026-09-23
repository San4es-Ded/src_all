package pulse.modules.visuals;

import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.Identifier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.gl.ShaderProgramKeys;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import pulse.animation.AnimationState;
import pulse.animation.Easing;
import pulse.core.Bool;
import pulse.entity.EntityUtils;
import pulse.events.WorldRenderEvent;
import pulse.hud.core.HudServiceRegistry;
import pulse.module.ClientModule;
import pulse.module.ModuleCategory;
import pulse.module.ModuleInfo;
import pulse.module.ModuleRegistry;
import pulse.modules.hud.ClientColor;
import pulse.render.RenderSystemHelper;
import pulse.render.shader.PulseShaderProgram;
import pulse.render.shader.ShaderLibrary;
import pulse.render.system.ClientPipelines;
import pulse.render.world.WorldRenderUtils;
import pulse.settings.BooleanSetting;
import pulse.settings.ColorSetting;
import pulse.settings.ModeSetting;
import pulse.settings.SettingGroup;
import pulse.settings.SliderSetting;
import pulse.util.ColorUtils;

@ModuleInfo(a = "Target ESP", b = "Shows ESP around the target", c = ModuleCategory.VISUALS)
public class TargetEsp extends ClientModule {
   public static int a;
   public static boolean b;
   private final ModeSetting e = new ModeSetting("Mode", new String[]{"Ghosts", "Circle", "Square", "Orbit", "Rings"}, "Rings");
   private final SettingGroup f = new SettingGroup("Ghosts").a(() -> this.e.b("Ghosts"));
   private final SliderSetting g = new SliderSetting("Animation speed", 1.5F, 0.5F, 5.0F, 0.1F).a(() -> this.e.b("Ghosts"));
   private final SliderSetting h = new SliderSetting("Particle size", 0.25F, 0.05F, 0.5F, 0.01F).a(() -> this.e.b("Ghosts"));
   private final SliderSetting i = new SliderSetting("Ghost count", 4.0F, 2.0F, 6.0F, 1.0F).a(() -> this.e.b("Ghosts"));
   private final SettingGroup j = new SettingGroup("Circle").a(() -> this.e.b("Circle"));
   private final SliderSetting k = new SliderSetting("Animation speed", 1.5F, 0.5F, 5.0F, 0.1F).a(() -> this.e.b("Circle"));
   private final SettingGroup l = new SettingGroup("Square").a(() -> this.e.b("Square"));
   private final SliderSetting m = new SliderSetting("Animation speed", 2.5F, 0.5F, 5.0F, 0.1F).a(() -> this.e.b("Square"));
   private final SliderSetting n = new SliderSetting("Square size", 1.4F, 0.5F, 2.0F, 0.1F).a(() -> this.e.b("Square"));
   private final SettingGroup o = new SettingGroup("Orbit").a(() -> this.e.b("Orbit"));
   private final ModeSetting p = new ModeSetting("Shape", new String[]{"Arrows", "Diamonds", "Cubes"}, "Arrows").a(() -> this.e.b("Orbit"));
   private final SliderSetting q = new SliderSetting("Animation speed", 1.5F, 0.5F, 5.0F, 0.1F).a(() -> this.e.b("Orbit"));
   private final SliderSetting r = new SliderSetting("Figures per ring", 3.0F, 2.0F, 8.0F, 1.0F).a(() -> this.e.b("Orbit"));
   private final SliderSetting s = new SliderSetting("Height layers", 3.0F, 2.0F, 5.0F, 1.0F).a(() -> this.e.b("Orbit"));
   private final SliderSetting t = new SliderSetting("Layer spacing", 1.0F, 0.3F, 2.0F, 0.05F).a(() -> this.e.b("Orbit"));
   private final SliderSetting u = new SliderSetting("Distance", 0.9F, 0.5F, 2.0F, 0.05F).a(() -> this.e.b("Orbit"));
   private final SliderSetting v = new SliderSetting("Figure size", 0.2F, 0.08F, 0.4F, 0.01F).a(() -> this.e.b("Orbit"));
   private final BooleanSetting w = new BooleanSetting("Rotation", true).a(() -> this.e.b("Orbit"));
   private final BooleanSetting x = new BooleanSetting("Shader", true).a(() -> this.e.b("Orbit"));
   private final ModeSetting y = new ModeSetting("Shader type", new String[]{"Nebula", "Stars", "Web", "Plasma"}, "Nebula")
      .a(() -> this.e.b("Orbit") && this.x.a() ? true : false);
   private final SettingGroup C = new SettingGroup("Color");
   private final BooleanSetting D = new BooleanSetting("Client color", true);
   private final ColorSetting E = new ColorSetting("Custom color", Color.WHITE).a(() -> Bool.from(this.D.a() ? 0 : 1));
   private final SettingGroup F = new SettingGroup("On hit");
   private final BooleanSetting G = new BooleanSetting("Enable", Bool.from(-1673835725));
   private final BooleanSetting H = new BooleanSetting("Change color", true).a(() -> this.G.a());
   private final ColorSetting I = new ColorSetting("Damage color", new Color(255, 50, 50)).a(() -> Bool.from(this.G.a() && this.H.a() ? 1 : 0));
   private final BooleanSetting J = new BooleanSetting("Speed up animation", true).a(() -> this.G.a());
   private final SliderSetting K = new SliderSetting("Speed multiplier", 3.0F, 1.2F, 5.0F, 0.1F).a(() -> Bool.from(this.G.a() && this.J.a() ? 1 : 0));
   private final SliderSetting L = new SliderSetting("Effect duration", 0.8F, 0.3F, 2.0F, 0.1F).a(() -> Bool.from(this.G.a() && this.J.a() ? 1 : 0));
   private final Map<LivingEntity, AnimationState> M = new HashMap<>();
   private final Identifier N = Identifier.of("pulse", "textures/target.png");
   private final BooleanSetting targetPlayers = new BooleanSetting("Players", true);
   private final BooleanSetting targetMobs = new BooleanSetting("Mobs", true);
   private final Map<LivingEntity, Integer> O = new HashMap<>();
   private final Map<LivingEntity, Long> P = new HashMap<>();
   private final Map<LivingEntity, Double> Q = new HashMap<>();
   private final Map<LivingEntity, Double> R = new HashMap<>();
   private long S = System.currentTimeMillis();

   @EventHandler
   public void a(WorldRenderEvent worldRenderEvent) {
      long lastTime = this.S;
      this.S = System.currentTimeMillis();
      float fMin = Math.min((float)(this.S - lastTime) / 1000.0F, 0.1F);
      LivingEntity LivingEntityVarP = this.p();
      if (LivingEntityVarP != null && this.a(LivingEntityVarP)) {
         this.M.computeIfAbsent(LivingEntityVarP, LivingEntityVar -> new AnimationState()).a(1.0, 0.5, Easing.f, true);
      }

      HashSet<LivingEntity> hashSet = new HashSet<>();

      for (Map.Entry<LivingEntity, AnimationState> entry : this.M.entrySet()) {
         LivingEntity key = entry.getKey();
         if (this.a(key)) {
            AnimationState value = entry.getValue();
            if (key != LivingEntityVarP) {
               value.a(0.0, 0.5, Easing.f, true);
            }

            value.a();
            if (value.d() && value.i() == 0.0) {
               hashSet.add(key);
            } else {
               this.a(key, fMin);
               switch (this.e.d()) {
                  case "Ghosts":
                     this.b(worldRenderEvent, key, (float)value.j());
                     break;
                  case "Circle":
                     this.renderCircle(worldRenderEvent, key, (float)value.j());
                     break;
                  case "Square":
                     this.a(worldRenderEvent, key, (float)value.j());
                     break;
                  case "Orbit":
                     this.d(worldRenderEvent, key, (float)value.j());
                     break;
                  case "Rings":
                     this.c(worldRenderEvent, key, (float)value.j());
               }
            }
         } else {
            hashSet.add(key);
         }
      }

      for (LivingEntity LivingEntityVar2 : hashSet) {
         this.M.remove(LivingEntityVar2);
         this.Q.remove(LivingEntityVar2);
         this.R.remove(LivingEntityVar2);
      }
   }

   private void a(LivingEntity LivingEntityVar, float f) {
      float fC = this.c(LivingEntityVar);
      this.Q.put(LivingEntityVar, (this.Q.getOrDefault(LivingEntityVar, 0.0) + f * fC * 50.0) % 360.0);
      this.R.put(LivingEntityVar, this.R.getOrDefault(LivingEntityVar, 0.0) + f * fC * 2.5);
   }

   private boolean a(LivingEntity LivingEntityVar) {
      if (LivingEntityVar == null) {
         return false;
      } else if (!this.matchesTargetType(LivingEntityVar)) {
         return false;
      } else if (LivingEntityVar.isAlive() && LivingEntityVar.getEntityWorld() == c.world) {
         return !(LivingEntityVar.getWidth() <= 0.0F) && !(LivingEntityVar.getHeight() <= 0.0F)
            ? LivingEntityVar.squaredDistanceTo(c.player) <= 10000.0
            : false;
      } else {
         return false;
      }
   }

   private LivingEntity p() {
      if (c.player != null && c.world != null) {
         LivingEntity LivingEntityVarH = HudServiceRegistry.TARGETS.h();
         if (this.a(LivingEntityVarH)) {
            return LivingEntityVarH;
         } else if (c.targetedEntity instanceof LivingEntity LivingEntityVar && this.a(LivingEntityVar)) {
            return LivingEntityVar;
         } else {
            LivingEntity LivingEntityVar2 = null;
            double d = 4096.0;

            for (LivingEntity LivingEntityVar3 : c.world
               .getEntitiesByClass(LivingEntity.class, c.player.getBoundingBox().expand(64.0), LivingEntityVar4 -> this.a(LivingEntityVar4))) {
               double dSquaredDistanceTo = LivingEntityVar3.squaredDistanceTo(c.player);
               if (dSquaredDistanceTo < d) {
                  d = dSquaredDistanceTo;
                  LivingEntityVar2 = LivingEntityVar3;
               }
            }

            return LivingEntityVar2;
         }
      } else {
         return null;
      }
   }

   private boolean matchesTargetType(LivingEntity LivingEntityVar) {
      if (LivingEntityVar != null && LivingEntityVar != c.player) {
         return LivingEntityVar instanceof PlayerEntity ? this.targetPlayers.a() : this.targetMobs.a();
      } else {
         return false;
      }
   }

   private void a(WorldRenderEvent worldRenderEvent, LivingEntity LivingEntityVar, float f) {
      if (!(f <= 0.0F) && LivingEntityVar != null && this.a(LivingEntityVar)) {
         Vec3d Vec3dVarB = EntityUtils.a(LivingEntityVar, worldRenderEvent.b());
         if (Vec3dVarB == null) {
            Vec3dVarB = LivingEntityVar.getEntityPos();
         }

         if (Vec3dVarB != null) {
            double dDoubleValue = this.Q.getOrDefault(LivingEntityVar, 0.0);
            double dDoubleValue2 = this.R.getOrDefault(LivingEntityVar, 0.0);
            float fA = (float)(this.n.a() * (1.0 + 0.05 * Math.sin(dDoubleValue2))) * f;
            Vec3d center = new Vec3d(Vec3dVarB.x, Vec3dVarB.y + LivingEntityVar.getHeight() / 2.0F, Vec3dVarB.z);
            Vec3d cameraPos = c.gameRenderer.getCamera().getCameraPos();
            Vec3d dir = cameraPos.subtract(center).normalize().multiply(LivingEntityVar.getWidth());
            center = center.add(dir);
            WorldRenderUtils.a(
               worldRenderEvent.a(),
               center,
               fA,
               ColorUtils.a(ColorUtils.a(this.b(LivingEntityVar).getRGB()), Math.max(1, (int)(f * 255.0F))),
               this.N,
               (float)dDoubleValue,
               false
            );
         }
      }
   }

   private void b(WorldRenderEvent worldRenderEvent, LivingEntity LivingEntityVar, float f) {
      if (this.a(LivingEntityVar) && !(f <= 0.0F)) {
         Vec3d Vec3dVarB = EntityUtils.a(LivingEntityVar, worldRenderEvent.b());
         if (Vec3dVarB == null) {
            Vec3dVarB = LivingEntityVar.getEntityPos();
         }

         if (Vec3dVarB != null) {
            double radians = Math.toRadians(50.0) / 15.0;
            int iB = this.i.b();
            double dDoubleValue = this.R.getOrDefault(LivingEntityVar, 0.0);
            double dMax = Math.max(LivingEntityVar.getWidth(), 0.5F) + 0.3;
            double dMax2 = Vec3dVarB.y + Math.max(LivingEntityVar.getHeight(), 0.5F) / 2.0F;
            double d = f;
            Vec3d[] Vec3dVarArr = new Vec3d[]{
               new Vec3d(1.0, 1.0, 1.0),
               new Vec3d(-1.0, 1.0, -1.0),
               new Vec3d(1.0, -1.0, 1.0),
               new Vec3d(-1.0, -1.0, 1.0),
               new Vec3d(1.0, 1.0, -1.0),
               new Vec3d(-1.0, -1.0, -1.0)
            };

            for (int i = 0; i < iB; i++) {
               double d2 = dDoubleValue + i * Math.PI / 2.0;
               Vec3d Vec3dVar = Vec3dVarArr[i];
               double dSqrt = Math.sqrt(
                  Vec3dVar.x * Vec3dVar.x + Vec3dVar.y * Vec3dVar.y + Vec3dVar.z * Vec3dVar.z
               );
               Vec3d Vec3dVar2 = new Vec3d(Vec3dVar.x / dSqrt, Vec3dVar.y / dSqrt, Vec3dVar.z / dSqrt);
               Vec3d Vec3dVar3 = new Vec3d(0.0, 1.0, 0.0);
               if (Math.abs(
                     Vec3dVar2.x * Vec3dVar3.x + Vec3dVar2.y * Vec3dVar3.y + Vec3dVar2.z * Vec3dVar3.z
                  )
                  > 0.99) {
                  Vec3dVar3 = new Vec3d(1.0, 0.0, 0.0);
               }

               Vec3d Vec3dVar4 = new Vec3d(
                  Vec3dVar2.y * Vec3dVar3.z - Vec3dVar2.z * Vec3dVar3.y,
                  Vec3dVar2.z * Vec3dVar3.x - Vec3dVar2.x * Vec3dVar3.z,
                  Vec3dVar2.x * Vec3dVar3.y - Vec3dVar2.y * Vec3dVar3.x
               );
               double dSqrt2 = Math.sqrt(
                  Vec3dVar4.x * Vec3dVar4.x + Vec3dVar4.y * Vec3dVar4.y + Vec3dVar4.z * Vec3dVar4.z
               );
               Vec3d Vec3dVar5 = new Vec3d(Vec3dVar4.x / dSqrt2, Vec3dVar4.y / dSqrt2, Vec3dVar4.z / dSqrt2);
               Vec3d Vec3dVar6 = new Vec3d(
                  Vec3dVar2.y * Vec3dVar5.z - Vec3dVar2.z * Vec3dVar5.y,
                  Vec3dVar2.z * Vec3dVar5.x - Vec3dVar2.x * Vec3dVar5.z,
                  Vec3dVar2.x * Vec3dVar5.y - Vec3dVar2.y * Vec3dVar5.x
               );
               double dSqrt3 = Math.sqrt(
                  Vec3dVar6.x * Vec3dVar6.x + Vec3dVar6.y * Vec3dVar6.y + Vec3dVar6.z * Vec3dVar6.z
               );
               Vec3d Vec3dVar7 = new Vec3d(Vec3dVar6.x / dSqrt3, Vec3dVar6.y / dSqrt3, Vec3dVar6.z / dSqrt3);

               for (int i2 = 0; i2 < 15; i2++) {
                  double d3 = i2 * radians + d2;
                  double dCos = Math.cos(d3);
                  double dSin = Math.sin(d3);
                  Vec3d Vec3dVar8 = new Vec3d(
                     (Vec3dVar5.x * dCos + Vec3dVar7.x * dSin) * dMax,
                     (Vec3dVar5.y * dCos + Vec3dVar7.y * dSin) * dMax,
                     (Vec3dVar5.z * dCos + Vec3dVar7.z * dSin) * dMax
                  );
                  double d4 = Vec3dVarB.x + Vec3dVar8.x;
                  double d5 = dMax2 + Vec3dVar8.y;
                  double d6 = Vec3dVarB.z + Vec3dVar8.z;

                  try {
                     WorldRenderUtils.a(
                        worldRenderEvent.a(),
                        new Vec3d(d4, d5, d6),
                        this.h.a() * (1.0F + i2 / 15.0F),
                        ColorUtils.a(ColorUtils.a(this.b(LivingEntityVar).getRGB()), Math.max(1, (int)(d * 150.0)))
                     );
                  } catch (Exception var48) {
                  }
               }
            }
         }
      }
   }

   private void renderCircle(WorldRenderEvent event, LivingEntity target, float visibility) {
      if (!(visibility <= 0.0F) && target != null && this.a(target)) {
         Vec3d pos = EntityUtils.a(target, event.b());
         if (pos == null) {
            pos = target.getEntityPos();
         }

         if (pos != null) {
            Vec3d camera = c.gameRenderer.getCamera().getCameraPos();
            MatrixStack matrices = event.a();
            Color color = this.b(target);
            float height = Math.max(0.5F, target.getHeight());
            float radius = Math.max(0.42F, target.getWidth() * 0.72F) + 0.12F;
            double phase = this.R.getOrDefault(target, 0.0);
            float scan = (float)((Math.sin(phase * 0.72) + 1.0) * 0.5);
            float centerY = (float)(pos.y + 0.06 + scan * (height - 0.12F));
            matrices.push();
            RenderSystemHelper.enableBlend();
            RenderSystemHelper.defaultBlendFunc();
            RenderSystemHelper.disableCull();
            RenderSystemHelper.depthMask(false);
            RenderSystemHelper.setShader(ShaderProgramKeys.POSITION_COLOR);
            RenderSystemHelper.lineWidth(2.2F);
            Matrix4f matrix = matrices.peek().getPositionMatrix();
            VertexConsumer out = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers().getBuffer(ClientPipelines.OUTLINE_NO);
            int segments = 96;
            int layers = 9;

            for (int layer = 0; layer < 9; layer++) {
               float offset = (layer - 4.0F) * 0.035F;
               float fade = 1.0F - Math.abs(layer - 4.0F) / 5.0F;
               int alpha = Math.max(2, (int)(visibility * fade * (layer == 4 ? 235 : 95)));
               float y = centerY + offset;

               for (int i = 0; i < 96; i++) {
                  double a0 = (Math.PI * 2) * i / 96.0;
                  double a1 = (Math.PI * 2) * (i + 1) / 96.0;
                  float x0 = (float)(pos.x + Math.cos(a0) * radius - camera.x);
                  float z0 = (float)(pos.z + Math.sin(a0) * radius - camera.z);
                  float x1 = (float)(pos.x + Math.cos(a1) * radius - camera.x);
                  float z1 = (float)(pos.z + Math.sin(a1) * radius - camera.z);
                  float ry = (float)(y - camera.y);
                  out.vertex(matrix, x0, ry, z0).color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
                  out.vertex(matrix, x1, ry, z1).color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
               }
            }

            RenderSystemHelper.depthMask(true);
            RenderSystemHelper.enableCull();
            RenderSystemHelper.disableBlend();
            matrices.pop();
         }
      }
   }

   private void c(WorldRenderEvent worldRenderEvent, LivingEntity LivingEntityVar, float f) {
      if (!(f <= 0.0F) && LivingEntityVar != null && this.a(LivingEntityVar)) {
         Vec3d Vec3dVarB = EntityUtils.a(LivingEntityVar, worldRenderEvent.b());
         if (Vec3dVarB == null) {
            Vec3dVarB = LivingEntityVar.getEntityPos();
         }

         if (Vec3dVarB != null) {
            MatrixStack MatrixStackVarA = worldRenderEvent.a();
            Vec3d Vec3dVarGetPos = c.gameRenderer.getCamera().getCameraPos();
            Color colorB = this.b(LivingEntityVar);
            float width = LivingEntityVar.getWidth();
            float height = LivingEntityVar.getHeight();
            MatrixStackVarA.push();
            RenderSystemHelper.enableBlend();
            RenderSystemHelper.defaultBlendFunc();
            RenderSystemHelper.disableCull();
            RenderSystemHelper.depthMask(false);
            RenderSystemHelper.setShader(ShaderProgramKeys.POSITION_COLOR);
            RenderSystemHelper.lineWidth(2.5F);
            Matrix4f matrix4fGetPositionMatrix = MatrixStackVarA.peek().getPositionMatrix();
            VertexConsumer consumer = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers().getBuffer(ClientPipelines.OUTLINE_NO);
            long time = System.currentTimeMillis();
            float pulseProgress = (float)(time % 1200L) / 1200.0F;

            for (int wave = 0; wave < 3; wave++) {
               float progress = (pulseProgress + wave / 3.0F) % 1.0F;
               float expand = progress * 0.35F;
               float waveAlpha = (1.0F - progress) * f;
               if (!(waveAlpha <= 0.01F)) {
                  int alpha = (int)(waveAlpha * 240.0F);
                  int red = colorB.getRed();
                  int green = colorB.getGreen();
                  int blue = colorB.getBlue();
                  float minX = (float)(Vec3dVarB.x - width / 2.0F - expand - Vec3dVarGetPos.x);
                  float maxX = (float)(Vec3dVarB.x + width / 2.0F + expand - Vec3dVarGetPos.x);
                  float minY = (float)(Vec3dVarB.y - expand - Vec3dVarGetPos.y);
                  float maxY = (float)(Vec3dVarB.y + height + expand - Vec3dVarGetPos.y);
                  float minZ = (float)(Vec3dVarB.z - width / 2.0F - expand - Vec3dVarGetPos.z);
                  float maxZ = (float)(Vec3dVarB.z + width / 2.0F + expand - Vec3dVarGetPos.z);
                  consumer.vertex(matrix4fGetPositionMatrix, minX, minY, minZ).color(red, green, blue, alpha);
                  consumer.vertex(matrix4fGetPositionMatrix, maxX, minY, minZ).color(red, green, blue, alpha);
                  consumer.vertex(matrix4fGetPositionMatrix, maxX, minY, minZ).color(red, green, blue, alpha);
                  consumer.vertex(matrix4fGetPositionMatrix, maxX, minY, maxZ).color(red, green, blue, alpha);
                  consumer.vertex(matrix4fGetPositionMatrix, maxX, minY, maxZ).color(red, green, blue, alpha);
                  consumer.vertex(matrix4fGetPositionMatrix, minX, minY, maxZ).color(red, green, blue, alpha);
                  consumer.vertex(matrix4fGetPositionMatrix, minX, minY, maxZ).color(red, green, blue, alpha);
                  consumer.vertex(matrix4fGetPositionMatrix, minX, minY, minZ).color(red, green, blue, alpha);
                  consumer.vertex(matrix4fGetPositionMatrix, minX, maxY, minZ).color(red, green, blue, alpha);
                  consumer.vertex(matrix4fGetPositionMatrix, maxX, maxY, minZ).color(red, green, blue, alpha);
                  consumer.vertex(matrix4fGetPositionMatrix, maxX, maxY, minZ).color(red, green, blue, alpha);
                  consumer.vertex(matrix4fGetPositionMatrix, maxX, maxY, maxZ).color(red, green, blue, alpha);
                  consumer.vertex(matrix4fGetPositionMatrix, maxX, maxY, maxZ).color(red, green, blue, alpha);
                  consumer.vertex(matrix4fGetPositionMatrix, minX, maxY, maxZ).color(red, green, blue, alpha);
                  consumer.vertex(matrix4fGetPositionMatrix, minX, maxY, maxZ).color(red, green, blue, alpha);
                  consumer.vertex(matrix4fGetPositionMatrix, minX, maxY, minZ).color(red, green, blue, alpha);
                  consumer.vertex(matrix4fGetPositionMatrix, minX, minY, minZ).color(red, green, blue, alpha);
                  consumer.vertex(matrix4fGetPositionMatrix, minX, maxY, minZ).color(red, green, blue, alpha);
                  consumer.vertex(matrix4fGetPositionMatrix, maxX, minY, minZ).color(red, green, blue, alpha);
                  consumer.vertex(matrix4fGetPositionMatrix, maxX, maxY, minZ).color(red, green, blue, alpha);
                  consumer.vertex(matrix4fGetPositionMatrix, maxX, minY, maxZ).color(red, green, blue, alpha);
                  consumer.vertex(matrix4fGetPositionMatrix, maxX, maxY, maxZ).color(red, green, blue, alpha);
                  consumer.vertex(matrix4fGetPositionMatrix, minX, minY, maxZ).color(red, green, blue, alpha);
                  consumer.vertex(matrix4fGetPositionMatrix, minX, maxY, maxZ).color(red, green, blue, alpha);
               }
            }

            RenderSystemHelper.depthMask(true);
            RenderSystemHelper.enableCull();
            RenderSystemHelper.disableBlend();
            MatrixStackVarA.pop();
         }
      }
   }

   private String a(ModeSetting modeSetting) {
      switch (modeSetting.d()) {
         case "Stars":
            return "block_starfield";
         case "Web":
            return "block_cobweb";
         case "Plasma":
            return "block_plasma";
         default:
            return "block_nebula";
      }
   }

   private void d(WorldRenderEvent worldRenderEvent, LivingEntity LivingEntityVar, float f) {
      if (f > 0.0F && this.a(LivingEntityVar)) {
         Vec3d Vec3dVarB = EntityUtils.a(LivingEntityVar, worldRenderEvent.b());
         if (Vec3dVarB == null) {
            Vec3dVarB = LivingEntityVar.getEntityPos();
         }

         if (Vec3dVarB == null) {
            return;
         }

         MatrixStack MatrixStackVarA = worldRenderEvent.a();
         Vec3d Vec3dVarGetPos = c.gameRenderer.getCamera().getCameraPos();
         Color colorB = this.b(LivingEntityVar);
         int iB = this.r.b();
         int iB2 = this.s.b();
         float fA = this.u.a() + LivingEntityVar.getWidth() / 2.0F;
         float fA2 = this.v.a() * f;
         float fGetHeight = LivingEntityVar.getHeight();
         Vec3d Vec3dVar = new Vec3d(Vec3dVarB.x, Vec3dVarB.y + fGetHeight / 2.0F, Vec3dVarB.z);
         double dDoubleValue = this.w.a() ? this.Q.getOrDefault(LivingEntityVar, 0.0) : 0.0;
         double dDoubleValue2 = this.R.getOrDefault(LivingEntityVar, 0.0);
         float fSin = (float)(fA + Math.sin(dDoubleValue2) * 0.08);
         Optional<PulseShaderProgram> optionalEmpty = Optional.empty();
         if (this.x.a()) {
            optionalEmpty = ShaderLibrary.getRegistry().find(this.a(this.y));
            if (optionalEmpty.isPresent() && !optionalEmpty.get().b()) {
               optionalEmpty = Optional.empty();
            }
         }

         String strD = this.p.d();
         float f2 = (iB2 - 2 + 1) / 2.0F;
         float fA3 = fGetHeight * this.t.a();
         float f3 = (fGetHeight - fA3) / 2.0F;
         MatrixStackVarA.push();
         RenderSystemHelper.enableBlend();
         RenderSystemHelper.defaultBlendFunc();
         RenderSystemHelper.disableCull();
         RenderSystemHelper.depthMask(false);
         RenderSystemHelper.setShader(ShaderProgramKeys.POSITION_COLOR);
         Matrix4f matrix4fGetPositionMatrix = MatrixStackVarA.peek().getPositionMatrix();
         byte b2 = -1;
         if (strD.equals("Arrows")) {
            b2 = 0;
         } else if (strD.equals("Diamonds")) {
            b2 = 1;
         } else if (strD.equals("Cubes")) {
            b2 = 2;
         }

         int totalPasses = optionalEmpty.isPresent() ? 2 : 1;

         for (int pass = 0; pass < totalPasses; pass++) {
            boolean shaderPass = pass == 1;
            if (shaderPass) {
               PulseShaderProgram pulseShaderProgram = optionalEmpty.get();
               pulseShaderProgram.d();
               pulseShaderProgram.a("time", (float)(dDoubleValue2 * 0.4));
               pulseShaderProgram.a("screenSize", c.getWindow().getFramebufferWidth(), c.getWindow().getFramebufferHeight());
               pulseShaderProgram.a("baseColor", colorB.getRed() / 255.0F, colorB.getGreen() / 255.0F, colorB.getBlue() / 255.0F, 1.0F);
               pulseShaderProgram.a("alpha", f * 1.5F);
               RenderSystemHelper.blendFunc(770, 1);
            }

            for (int i3 = 0; i3 < iB2; i3++) {
               float f9 = (float)(Vec3dVarB.y + (f3 + fA3 * i3 / (2 * (iB2 & -2) - (iB2 ^ 1))));
               float fAbs2 = fSin * (1.0F - Math.abs(i3 - f2) / Math.max(f2, 0.001F) * 0.3F);
               double d5 = 360.0 / iB / 2.0 * (i3 % 2);

               for (int i4 = 0; i4 < iB; i4++) {
                  double radians2 = Math.toRadians(dDoubleValue + d5 + 360.0 * i4 / iB);
                  float fCos2 = (float)(Vec3dVar.x + fAbs2 * Math.cos(radians2));
                  float fSin3 = (float)(Vec3dVar.z + fAbs2 * Math.sin(radians2));
                  double d6 = Vec3dVar.x - fCos2;
                  double d7 = Vec3dVar.y - f9;
                  double d8 = Vec3dVar.z - fSin3;
                  double dSqrt2 = Math.sqrt(d6 * d6 + d7 * d7 + d8 * d8);
                  double d9 = d6 / dSqrt2;
                  double d10 = d7 / dSqrt2;
                  double d11 = d8 / dSqrt2;
                  double d12 = -d11;
                  double d13 = 0.0;
                  double d14 = d9;
                  double dSqrt3 = Math.sqrt(d12 * d12 + d13 * d13 + d14 * d14);
                  if (dSqrt3 > 0.001) {
                     d12 /= dSqrt3;
                     d13 /= dSqrt3;
                     d14 /= dSqrt3;
                  }

                  double d15 = d13 * d11 - d14 * d10;
                  double d16 = d14 * d9 - d12 * d11;
                  double d17 = d12 * d10 - d13 * d9;
                  int i5 = (int)(f * 220.0F);
                  switch (b2) {
                     case 0:
                        this.a(
                           matrix4fGetPositionMatrix, Vec3dVarGetPos, colorB, shaderPass, fCos2, f9, fSin3, d9, d10, d11, d15, d16, d17, d12, d13, d14, fA2, i5
                        );
                        break;
                     case 1:
                        this.b(
                           matrix4fGetPositionMatrix, Vec3dVarGetPos, colorB, shaderPass, fCos2, f9, fSin3, d9, d10, d11, d15, d16, d17, d12, d13, d14, fA2, i5
                        );
                        break;
                     case 2:
                        this.a(matrix4fGetPositionMatrix, Vec3dVarGetPos, colorB, shaderPass, fCos2, f9, fSin3, d15, d16, d17, d12, d13, d14, fA2, i5);
                  }
               }
            }

            if (shaderPass) {
               optionalEmpty.get().e();
               RenderSystemHelper.defaultBlendFunc();
            }
         }

         RenderSystemHelper.depthMask(true);
         RenderSystemHelper.enableCull();
         RenderSystemHelper.disableBlend();
         MatrixStackVarA.pop();
      }
   }

   private void a(
      Matrix4f matrix4f,
      Vec3d Vec3dVar,
      Color color,
      boolean z,
      float f,
      float f2,
      float f3,
      double d,
      double d2,
      double d3,
      double d4,
      double d5,
      double d6,
      double d7,
      double d8,
      double d9,
      float f4,
      int i
   ) {
      float f5 = f4 * 0.5F;
      this.a(
         matrix4f,
         color,
         z,
         (float)(f + d * f4 * 1.5 - Vec3dVar.x),
         (float)(f2 + d2 * f4 * 1.5 - Vec3dVar.y),
         (float)(f3 + d3 * f4 * 1.5 - Vec3dVar.z),
         (float)(f + d4 * f5 - Vec3dVar.x),
         (float)(f2 + d5 * f5 - Vec3dVar.y),
         (float)(f3 + d6 * f5 - Vec3dVar.z),
         (float)(f - d4 * f5 - Vec3dVar.x),
         (float)(f2 - d5 * f5 - Vec3dVar.y),
         (float)(f3 - d6 * f5 - Vec3dVar.z),
         (float)(f + d7 * f5 - Vec3dVar.x),
         (float)(f2 + d8 * f5 - Vec3dVar.y),
         (float)(f3 + d9 * f5 - Vec3dVar.z),
         (float)(f - d7 * f5 - Vec3dVar.x),
         (float)(f2 - d8 * f5 - Vec3dVar.y),
         (float)(f3 - d9 * f5 - Vec3dVar.z),
         i
      );
   }

   private void b(
      Matrix4f matrix4f,
      Vec3d Vec3dVar,
      Color color,
      boolean z,
      float f,
      float f2,
      float f3,
      double d,
      double d2,
      double d3,
      double d4,
      double d5,
      double d6,
      double d7,
      double d8,
      double d9,
      float f4,
      int i
   ) {
      double d10 = d5 * d9 - d6 * d8;
      double d11 = d6 * d7 - d4 * d9;
      double d12 = d4 * d8 - d5 * d7;
      float f5 = (float)(f + d4 * f4 * 1.2 - Vec3dVar.x);
      float f6 = (float)(f2 + d5 * f4 * 1.2 - Vec3dVar.y);
      float f7 = (float)(f3 + d6 * f4 * 1.2 - Vec3dVar.z);
      float f8 = (float)(f - d4 * f4 * 1.2 - Vec3dVar.x);
      float f9 = (float)(f2 - d5 * f4 * 1.2 - Vec3dVar.y);
      float f10 = (float)(f3 - d6 * f4 * 1.2 - Vec3dVar.z);
      float f11 = f4 * 0.6F;
      float f12 = (float)(f + d7 * f11 + d10 * f11 - Vec3dVar.x);
      float f13 = (float)(f2 + d8 * f11 + d11 * f11 - Vec3dVar.y);
      float f14 = (float)(f3 + d9 * f11 + d12 * f11 - Vec3dVar.z);
      float f15 = (float)(f + d7 * f11 - d10 * f11 - Vec3dVar.x);
      float f16 = (float)(f2 + d8 * f11 - d11 * f11 - Vec3dVar.y);
      float f17 = (float)(f3 + d9 * f11 - d12 * f11 - Vec3dVar.z);
      float f18 = (float)(f - d7 * f11 - d10 * f11 - Vec3dVar.x);
      float f19 = (float)(f2 - d8 * f11 - d11 * f11 - Vec3dVar.y);
      float f20 = (float)(f3 - d9 * f11 - d12 * f11 - Vec3dVar.z);
      float f21 = (float)(f - d7 * f11 + d10 * f11 - Vec3dVar.x);
      float f22 = (float)(f2 - d8 * f11 + d11 * f11 - Vec3dVar.y);
      float f23 = (float)(f3 - d9 * f11 + d12 * f11 - Vec3dVar.z);
      this.a(matrix4f, color, z, f5, f6, f7, f12, f13, f14, f15, f16, f17, i);
      this.a(matrix4f, color, z, f5, f6, f7, f15, f16, f17, f18, f19, f20, i);
      this.a(matrix4f, color, z, f5, f6, f7, f18, f19, f20, f21, f22, f23, i);
      this.a(matrix4f, color, z, f5, f6, f7, f21, f22, f23, f12, f13, f14, i);
      this.a(matrix4f, color, z, f8, f9, f10, f15, f16, f17, f12, f13, f14, i);
      this.a(matrix4f, color, z, f8, f9, f10, f18, f19, f20, f15, f16, f17, i);
      this.a(matrix4f, color, z, f8, f9, f10, f21, f22, f23, f18, f19, f20, i);
      this.a(matrix4f, color, z, f8, f9, f10, f12, f13, f14, f21, f22, f23, i);
   }

   private void a(
      Matrix4f matrix4f,
      Vec3d Vec3dVar,
      Color color,
      boolean z,
      float f,
      float f2,
      float f3,
      double d,
      double d2,
      double d3,
      double d4,
      double d5,
      double d6,
      float f4,
      int i
   ) {
      float f5 = f4 * 0.5F;
      double d7 = d2 * d6 - d3 * d5;
      double d8 = d3 * d4 - d * d6;
      double d9 = d * d5 - d2 * d4;
      float[][] fArr = new float[8][3];
      int i2 = 0;

      for (int i3 = -1; i3 <= 1; i3 += 2) {
         for (int i4 = -1; i4 <= 1; i4 += 2) {
            for (int i5 = -1; i5 <= 1; i5 += 2) {
               fArr[i2][0] = (float)(f + d * f5 * i3 + d4 * f5 * i4 + d7 * f5 * i5 - Vec3dVar.x);
               fArr[i2][1] = (float)(f2 + d2 * f5 * i3 + d5 * f5 * i4 + d8 * f5 * i5 - Vec3dVar.y);
               fArr[i2][2] = (float)(f3 + d3 * f5 * i3 + d6 * f5 * i4 + d9 * f5 * i5 - Vec3dVar.z);
               i2++;
            }
         }
      }

      for (int[] objArr : new int[][]{{0, 1, 3, 2}, {4, 6, 7, 5}, {0, 4, 5, 1}, {2, 3, 7, 6}, {0, 2, 6, 4}, {1, 5, 7, 3}}) {
         this.a(
            matrix4f,
            color,
            z,
            fArr[objArr[0]][0],
            fArr[objArr[0]][1],
            fArr[objArr[0]][2],
            fArr[objArr[1]][0],
            fArr[objArr[1]][1],
            fArr[objArr[1]][2],
            fArr[objArr[2]][0],
            fArr[objArr[2]][1],
            fArr[objArr[2]][2],
            i
         );
         this.a(
            matrix4f,
            color,
            z,
            fArr[objArr[0]][0],
            fArr[objArr[0]][1],
            fArr[objArr[0]][2],
            fArr[objArr[2]][0],
            fArr[objArr[2]][1],
            fArr[objArr[2]][2],
            fArr[objArr[3]][0],
            fArr[objArr[3]][1],
            fArr[objArr[3]][2],
            i
         );
      }
   }

   private void a(Matrix4f matrix4f, Color color, boolean z, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, int i) {
      if (!z) {
         VertexConsumer consumer = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers().getBuffer(ClientPipelines.QUAD);
         consumer.vertex(matrix4f, f, f2, f3).color(color.getRed(), color.getGreen(), color.getBlue(), i);
         consumer.vertex(matrix4f, f4, f5, f6).color(color.getRed(), color.getGreen(), color.getBlue(), i * 3 / 4);
         consumer.vertex(matrix4f, f7, f8, f9).color(color.getRed(), color.getGreen(), color.getBlue(), i * 3 / 4);
         consumer.vertex(matrix4f, f7, f8, f9).color(color.getRed(), color.getGreen(), color.getBlue(), i * 3 / 4);
      } else {
         Vector4f v1 = matrix4f.transform(new Vector4f(f, f2, f3, 1.0F));
         Vector4f v2 = matrix4f.transform(new Vector4f(f4, f5, f6, 1.0F));
         Vector4f v3 = matrix4f.transform(new Vector4f(f7, f8, f9, 1.0F));
         float[] fArr = new float[]{v1.x, v1.y, v1.z, 0.5F, 0.0F, v2.x, v2.y, v2.z, 0.0F, 1.0F, v3.x, v3.y, v3.z, 1.0F, 1.0F};
         PulseShaderProgram.a(fArr, 3);
      }
   }

   private void a(
      Matrix4f matrix4f,
      Color color,
      boolean z,
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
      float f13,
      float f14,
      float f15,
      int i
   ) {
      this.a(matrix4f, color, z, f, f2, f3, f4, f5, f6, f10, f11, f12, i);
      this.a(matrix4f, color, z, f, f2, f3, f10, f11, f12, f7, f8, f9, i);
      this.a(matrix4f, color, z, f, f2, f3, f7, f8, f9, f13, f14, f15, i);
      this.a(matrix4f, color, z, f, f2, f3, f13, f14, f15, f4, f5, f6, i);
   }

   private Color b(LivingEntity LivingEntityVar) {
      if (this.G.a() && this.H.a() && LivingEntityVar != null && LivingEntityVar.hurtTime > 0) {
         float f = LivingEntityVar.hurtTime / 10.0F;
         Color colorN = this.n();
         Color colorA = this.I.a();
         int red = (int)(colorN.getRed() + (colorA.getRed() - colorN.getRed()) * f);
         float green = colorN.getGreen();
         int green2 = colorA.getGreen();
         int green3 = colorN.getGreen();
         return new Color(
            Math.max(0, Math.min(255, red)),
            Math.max(0, Math.min(255, (int)(green + (2 * (green2 & ~green3) - (green2 ^ green3)) * f))),
            Math.max(0, Math.min(255, (int)(colorN.getBlue() + (colorA.getBlue() - colorN.getBlue()) * f)))
         );
      } else {
         return this.n();
      }
   }

   private Color n() {
      if (this.D.a()) {
         ClientColor clientColor = ModuleRegistry.CLIENT_COLOR;
         if (clientColor != null) {
            return clientColor.n();
         }
      }

      return this.E.a();
   }

   private float o() {
      switch (this.e.d()) {
         case "Ghosts":
            return this.g.a();
         case "Circle":
            return this.k.a();
         case "Square":
            return this.m.a();
         case "Orbit":
            return this.q.a();
         default:
            return 1.5F;
      }
   }

   private float c(LivingEntity LivingEntityVar) {
      float fO = this.o();
      if (this.G.a() && this.J.a() && LivingEntityVar != null) {
         int i = LivingEntityVar.hurtTime;
         int iIntValue = this.O.getOrDefault(LivingEntityVar, 0);
         this.O.put(LivingEntityVar, i);
         if (i >= 9 && iIntValue < 9) {
            this.P.put(LivingEntityVar, System.currentTimeMillis());
         }

         Long lastSeen = this.P.get(LivingEntityVar);
         if (lastSeen == null) {
            return fO;
         }

         float fCurrentTimeMillis = (float)(System.currentTimeMillis() - lastSeen) / 1000.0F;
         float fA = this.L.a();
         if (fCurrentTimeMillis > fA) {
            return fO;
         }

         float f2 = fCurrentTimeMillis / fA;
         float f;
         if (f2 >= 0.15F) {
            float f3 = (f2 - 0.15F) / 0.85F;
            f = 1.0F - f3 * f3 * (3.0F - 2.0F * f3);
         } else {
            float f4 = f2 / 0.15F;
            f = f4 * f4 * (3.0F - 2.0F * f4);
         }

         return fO * (1.0F + (this.K.a() - 1.0F) * f);
      } else {
         return fO;
      }
   }

   @Override
   public void f() {
      super.f();
      this.M.clear();
      this.O.clear();
      this.P.clear();
      this.Q.clear();
      this.R.clear();
   }

   public static String c(String str, String str2, int i, int i2, int i3, int i4) {
      return null;
   }
}
