package ru.prism.cosmetic.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import ru.prism.module.impl.render.Cosmetics;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CosmeticFeatureRenderer extends FeatureRenderer<PlayerEntityRenderState, PlayerEntityModel> {
   private static final Map<Integer, double[]> CAPE_TRACKER = new ConcurrentHashMap<>();

   public CosmeticFeatureRenderer(FeatureRendererContext<PlayerEntityRenderState, PlayerEntityModel> context) {
      super(context);
   }

   @Override
   public void render(MatrixStack matrices, OrderedRenderCommandQueue queue, int light, PlayerEntityRenderState state, float limbAngle, float tickDelta) {
      Cosmetics module = Cosmetics.getInstance();
      if (module == null || !module.isEnabled()) {
         return;
      }

      MinecraftClient mc = MinecraftClient.getInstance();
      if (mc.player == null || state == null || state.spectator) {
         return;
      }

      if (state.id != mc.player.getId()) {
         return;
      }

      Entity wearer = mc.player;

      module.renderCosmetics(matrices, light, this.getContextModel());
      this.renderCape(matrices, light, this.getContextModel(), wearer, tickDelta);
   }

   private void renderCape(MatrixStack matrices, int light, PlayerEntityModel model, Entity wearer, float tickDelta) {
      Identifier capeTexture = Cosmetics.getInstance().getSelectedCapeTexture();
      if (capeTexture == null) {
         return;
      }

      boolean skinLayout = Cosmetics.getInstance().isSelectedCapeSkinLayout();

      float frontU0 = skinLayout ? 1.0F / 64.0F : 0.0F;
      float frontU1 = skinLayout ? 11.0F / 64.0F : 1.0F;
      float v0 = skinLayout ? 1.0F / 32.0F : 0.0F;
      float v1 = skinLayout ? 17.0F / 32.0F : 1.0F;
      float backU0 = skinLayout ? 12.0F / 64.0F : 0.0F;
      float backU1 = skinLayout ? 22.0F / 64.0F : 1.0F;
      float topV0 = skinLayout ? 0.0F : 0.0F;
      float topV1 = skinLayout ? 1.0F / 32.0F : 1.0F;
      float topU0 = skinLayout ? 1.0F / 64.0F : 0.0F;
      float topU1 = skinLayout ? 11.0F / 64.0F : 1.0F;
      float bottomU0 = skinLayout ? 11.0F / 64.0F : 0.0F;
      float bottomU1 = skinLayout ? 21.0F / 64.0F : 1.0F;
      float sideU0 = skinLayout ? 0.0F : 0.0F;
      float sideU1 = skinLayout ? 1.0F / 64.0F : 1.0F;
      float sideU2 = skinLayout ? 11.0F / 64.0F : 0.0F;
      float sideU3 = skinLayout ? 12.0F / 64.0F : 1.0F;

      Quaternionf capeRotation = new Quaternionf();
      if (wearer != null) {
         Vec3d velocity = wearer.getVelocity();
         float wx = (float) (-velocity.x);
         float wy = (float) (-velocity.y);
         float wz = (float) (-velocity.z);
         float bodyYaw;
         if (wearer instanceof LivingEntity living) {
            bodyYaw = MathHelper.lerpAngleDegrees(tickDelta, living.lastBodyYaw, living.bodyYaw);
         } else {
            bodyYaw = wearer.getYaw(tickDelta);
         }
         double sin = Math.sin((double) bodyYaw * 0.017453292519943295D);
         double negCos = -Math.cos((double) bodyYaw * 0.017453292519943295D);
         float glide = wearer instanceof LivingEntity living && living.isGliding() ? 1.0F : 0.0F;
         float targetZ = MathHelper.clamp((float) wy * 10.0F, -6.0F, 32.0F);
         float targetY = MathHelper.clamp((float) ((double) wx * sin + (double) wz * negCos) * 45.0F * (1.0F - glide), 0.0F, 90.0F);
         float targetX = MathHelper.clamp((float) ((double) wx * negCos - (double) wz * sin) * 45.0F, -12.0F, 12.0F);
         float horizontal = (float) Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z);

         Vec3d smoothPos = wearer.getLerpedPos(tickDelta);
         double[] tracker = CAPE_TRACKER.get(wearer.getId());
         if (tracker == null) {
            tracker = new double[] {smoothPos.x, smoothPos.y, smoothPos.z, 0.0D, (double) targetX, (double) targetY, (double) targetZ, (double) tickDelta, 0.0D, 0.0D, 0.0D};
            CAPE_TRACKER.put(wearer.getId(), tracker);
         }
         double dx = smoothPos.x - tracker[0];
         double dz = smoothPos.z - tracker[2];
         tracker[0] = smoothPos.x;
         tracker[1] = smoothPos.y;
         tracker[2] = smoothPos.z;
         double moved = Math.sqrt(dx * dx + dz * dz);
         if (moved > 1.0D) {
            moved = 0.0D;
         } else if (moved > 0.35D) {
            moved = 0.35D;
         }
         tracker[3] += moved;
         targetZ += MathHelper.sin((float) tracker[3] * 3.0F) * 8.0F * Math.min(1.0F, horizontal * 6.0F);

         double frameTicks = (double) tickDelta - tracker[7];
         if (frameTicks < 0.0D) {
            frameTicks += 1.0D;
         }
         tracker[7] = (double) tickDelta;
         double dt = MathHelper.clamp(frameTicks / 20.0D, 0.001D, 0.05D);

         double omega = 21.0D;
         double stiffness = omega * omega;
         double damping = 2.0D * omega;

         int steps = (int) Math.ceil(dt / 0.0167D);
         double sub = dt / (double) steps;
         for (int i = 0; i < steps; i++) {
            tracker[8] += (stiffness * ((double) targetX - tracker[4]) - damping * tracker[8]) * sub;
            tracker[4] += tracker[8] * sub;
            tracker[9] += (stiffness * ((double) targetY - tracker[5]) - damping * tracker[9]) * sub;
            tracker[5] += tracker[9] * sub;
            tracker[10] += (stiffness * ((double) targetZ - tracker[6]) - damping * tracker[10]) * sub;
            tracker[6] += tracker[10] * sub;
         }

         tracker[4] = MathHelper.clamp(tracker[4], -14.0D, 14.0D);
         tracker[5] = MathHelper.clamp(tracker[5], -10.0D, 100.0D);
         tracker[6] = MathHelper.clamp(tracker[6], -18.0D, 48.0D);

         float capeX = (float) tracker[4];
         float capeY = (float) tracker[5];
         float capeZ = (float) tracker[6];

         capeRotation.rotateY(-(float) Math.PI);
         capeRotation.rotateX((6.0F + capeY / 2.0F + capeZ) * 0.017453292F);
         capeRotation.rotateZ(capeX / 2.0F * 0.017453292F);
         capeRotation.rotateY((180.0F - capeX / 2.0F) * 0.017453292F);
      }

      matrices.push();
      model.body.applyTransform(matrices);
      matrices.translate(0.0F, 0.0F, 0.125F);
      matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
      matrices.multiply(capeRotation);

      Matrix4f matrix = matrices.peek().getPositionMatrix();
      VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
      VertexConsumer consumer = immediate.getBuffer(RenderLayers.entityCutoutNoCull(capeTexture));

      float hw = 0.3125F;
      float yTop = 0.0F;
      float yBottom = 1.0F;
      float zFront = -0.0625F;
      float zBack = 0.0F;

      quad(consumer, matrix, light, -hw, yTop, zFront, -hw, yBottom, zFront, hw, yBottom, zFront, hw, yTop, zFront, frontU0, v0, frontU1, v1, 0.0F, 0.0F, -1.0F);
      quad(consumer, matrix, light, hw, yTop, zBack, hw, yBottom, zBack, -hw, yBottom, zBack, -hw, yTop, zBack, backU0, v0, backU1, v1, 0.0F, 0.0F, 1.0F);
      quad(consumer, matrix, light, -hw, yTop, zBack, -hw, yBottom, zBack, -hw, yBottom, zFront, -hw, yTop, zFront, sideU0, v0, sideU1, v1, -1.0F, 0.0F, 0.0F);
      quad(consumer, matrix, light, hw, yTop, zFront, hw, yBottom, zFront, hw, yBottom, zBack, hw, yTop, zBack, sideU2, v0, sideU3, v1, 1.0F, 0.0F, 0.0F);
      quad(consumer, matrix, light, -hw, yTop, zBack, -hw, yTop, zFront, hw, yTop, zFront, hw, yTop, zBack, topU0, topV0, topU1, topV1, 0.0F, -1.0F, 0.0F);
      quad(consumer, matrix, light, -hw, yBottom, zFront, -hw, yBottom, zBack, hw, yBottom, zBack, hw, yBottom, zFront, bottomU0, v0, bottomU1, v1, 0.0F, 1.0F, 0.0F);

      immediate.draw();
      matrices.pop();
   }

   private static void quad(VertexConsumer consumer, Matrix4f matrix, int light,
         float x0, float y0, float z0, float x1, float y1, float z1,
         float x2, float y2, float z2, float x3, float y3, float z3,
         float u0, float v0, float u1, float v1, float nx, float ny, float nz) {
      consumer.vertex(matrix, x0, y0, z0).color(1.0F, 1.0F, 1.0F, 1.0F).texture(u0, v0).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(nx, ny, nz);
      consumer.vertex(matrix, x1, y1, z1).color(1.0F, 1.0F, 1.0F, 1.0F).texture(u0, v1).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(nx, ny, nz);
      consumer.vertex(matrix, x2, y2, z2).color(1.0F, 1.0F, 1.0F, 1.0F).texture(u1, v1).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(nx, ny, nz);
      consumer.vertex(matrix, x3, y3, z3).color(1.0F, 1.0F, 1.0F, 1.0F).texture(u1, v0).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(nx, ny, nz);
   }
}
