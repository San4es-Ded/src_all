package pulse.effects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.Identifier;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import pulse.client.MinecraftContext;
import pulse.events.WorldRenderEvent;
import pulse.events.WorldRenderStartEvent;
import pulse.hud.core.HudService;
import pulse.hud.core.HudServiceInfo;

@HudServiceInfo(enabledByDefault = true)
public class ParticleRenderService extends HudService implements MinecraftContext {
   private final List<ParticleRenderService.ParticleInstance> particles = new ArrayList<>();
   private long lastTickTime = -1L;
   private static final int MAX_PARTICLES = 200;

   public void a(Vec3d pos, Vec3d vel, int lifetime, float scale, Identifier texture, int colorArgb, double gravity, String physicsMode) {
      if (this.particles.size() >= 200) {
         this.particles.remove(0);
      }

      this.particles.add(new ParticleRenderService.ParticleInstance(this, pos, vel, lifetime, scale, texture, colorArgb, gravity, physicsMode));
   }

   public void a(Vec3d pos, Vec3d vel, int lifetime, float scale, Identifier texture, int colorArgb, String physicsMode) {
      this.a(pos, vel, lifetime, scale, texture, colorArgb, 0.02, physicsMode);
   }

   @EventHandler
   public void onStart(WorldRenderStartEvent event) {
      if (c.world != null) {
         long time = c.world.getTime();
         if (time != this.lastTickTime) {
            this.particles.removeIf(p -> !p.tick());
            this.lastTickTime = time;
         }
      }
   }

   @EventHandler
   public void a(WorldRenderEvent worldRenderEvent) {
      if (c.gameRenderer != null && c.gameRenderer.getCamera() != null && !this.particles.isEmpty()) {
         Immediate bufferSource = worldRenderEvent.bufferSource();
         if (bufferSource != null) {
            Vec3d camPos = c.gameRenderer.getCamera().getCameraPos();
            Map<Identifier, List<ParticleRenderService.ParticleVertex>> map = new HashMap<>();

            for (int i = 0; i < this.particles.size(); i++) {
               ParticleRenderService.ParticleInstance p = this.particles.get(i);
               ParticleRenderService.ParticleVertex v = p.getVertex();
               if (v != null) {
                  map.computeIfAbsent(p.texture, k -> new ArrayList<>()).add(v);
               }
            }

            if (!map.isEmpty()) {
               Quaternionf cameraRot = c.gameRenderer.getCamera().getRotation();
               Vector3f right = new Vector3f(1.0F, 0.0F, 0.0F).rotate(cameraRot);
               Vector3f up = new Vector3f(0.0F, 1.0F, 0.0F).rotate(cameraRot);
               Matrix4f identityMatrix = new Matrix4f();

               for (Map.Entry<Identifier, List<ParticleRenderService.ParticleVertex>> entry : map.entrySet()) {
                  Identifier texture = entry.getKey();
                  List<ParticleRenderService.ParticleVertex> list = entry.getValue();
                  if (!list.isEmpty()) {
                     VertexConsumer consumer = bufferSource.getBuffer(RenderLayers.itemEntityTranslucentCull(texture));

                     for (int i = 0; i < list.size(); i++) {
                        ParticleRenderService.ParticleVertex vertex = list.get(i);
                        float hs = vertex.scale * 0.5F;
                        float rx = right.x * hs;
                        float ry = right.y * hs;
                        float rz = right.z * hs;
                        float ux = up.x * hs;
                        float uy = up.y * hs;
                        float uz = up.z * hs;
                        float px = (float)(vertex.pos.x - camPos.x);
                        float py = (float)(vertex.pos.y - camPos.y);
                        float pz = (float)(vertex.pos.z - camPos.z);
                        consumer.vertex(identityMatrix, px - rx - ux, py - ry - uy, pz - rz - uz)
                           .color(vertex.r, vertex.g, vertex.b, vertex.a)
                           .texture(0.0F, 0.0F)
                           .overlay(OverlayTexture.DEFAULT_UV)
                           .light(15728880)
                           .normal(0.0F, 1.0F, 0.0F);
                        consumer.vertex(identityMatrix, px + rx - ux, py + ry - uy, pz + rz - uz)
                           .color(vertex.r, vertex.g, vertex.b, vertex.a)
                           .texture(1.0F, 0.0F)
                           .overlay(OverlayTexture.DEFAULT_UV)
                           .light(15728880)
                           .normal(0.0F, 1.0F, 0.0F);
                        consumer.vertex(identityMatrix, px + rx + ux, py + ry + uy, pz + rz + uz)
                           .color(vertex.r, vertex.g, vertex.b, vertex.a)
                           .texture(1.0F, 1.0F)
                           .overlay(OverlayTexture.DEFAULT_UV)
                           .light(15728880)
                           .normal(0.0F, 1.0F, 0.0F);
                        consumer.vertex(identityMatrix, px - rx + ux, py - ry + uy, pz - rz + uz)
                           .color(vertex.r, vertex.g, vertex.b, vertex.a)
                           .texture(0.0F, 1.0F)
                           .overlay(OverlayTexture.DEFAULT_UV)
                           .light(15728880)
                           .normal(0.0F, 1.0F, 0.0F);
                     }
                  }
               }

               bufferSource.draw();
            }
         }
      }
   }

   public class ParticleInstance {
      final int lifetime;
      Vec3d lastPos;
      Vec3d pos;
      Vec3d velocity;
      final float scale;
      final Identifier texture;
      final double gravity;
      final String physicsMode;
      final int colorArgb;
      final float rotation = ThreadLocalRandom.current().nextFloat() * 360.0F;
      int age = 0;

      ParticleInstance(
         ParticleRenderService service,
         Vec3d pos,
         Vec3d vel,
         int lifetime,
         float scale,
         Identifier texture,
         int colorArgb,
         double gravity,
         String physicsMode
      ) {
         this.lifetime = lifetime;
         this.pos = pos;
         this.lastPos = pos;
         this.velocity = vel;
         this.scale = scale;
         this.texture = texture;
         this.colorArgb = colorArgb;
         this.gravity = gravity;
         this.physicsMode = physicsMode;
      }

      public boolean tick() {
         this.age++;
         if (this.age >= this.lifetime) {
            return false;
         }

         this.lastPos = this.pos;
         if ("Р РµР°Р»РёСЃС‚РёС‡РЅР°СЏ".equals(this.physicsMode)) {
            this.velocity = this.velocity.multiply(0.98);
            this.velocity = this.velocity.add(0.0, -this.gravity, 0.0);
            this.pos = this.pos.add(this.velocity);
         } else if ("РџСЂРёС‚СЏР¶РµРЅРёРµ".equals(this.physicsMode)) {
            if (MinecraftContext.c.player != null) {
               Vec3d dir = new Vec3d(
                     MinecraftContext.c.player.getX(),
                     MinecraftContext.c.player.getY() + 1.0,
                     MinecraftContext.c.player.getZ()
                  )
                  .subtract(this.pos);
               if (dir.lengthSquared() > 0.01) {
                  this.velocity = this.velocity.add(dir.normalize().multiply(0.03));
               }
            }

            this.pos = this.pos.add(this.velocity);
         } else {
            this.pos = this.pos.add(this.velocity);
         }

         return true;
      }

      public ParticleRenderService.ParticleVertex getVertex() {
         float progress = (float)this.age / this.lifetime;
         int alpha = (int)((1.0F - progress) * (this.colorArgb >> 24 & 0xFF));
         if (alpha <= 0) {
            return null;
         }

         int r = this.colorArgb >> 16 & 0xFF;
         int g = this.colorArgb >> 8 & 0xFF;
         int b = this.colorArgb & 0xFF;
         return new ParticleRenderService.ParticleVertex(this.pos, this.scale * (1.0F - progress * 0.3F), this.rotation, r, g, b, alpha);
      }
   }

   public static class ParticleVertex {
      Vec3d pos;
      float scale;
      float rotation;
      int r;
      int g;
      int b;
      int a;

      ParticleVertex(Vec3d pos, float scale, float rotation, int r, int g, int b, int a) {
         this.pos = pos;
         this.scale = scale;
         this.rotation = rotation;
         this.r = r;
         this.g = g;
         this.b = b;
         this.a = a;
      }
   }
}
