package haron.modules.visuals;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import haron.events.AttackTargetEvent;
import haron.events.WorldRenderPostEvent;
import haron.module.ModuleManager;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.modules.visuals.HitGhostSnapshot;
import haron.settings.NumberSetting;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;

@ModuleInfo(a="Hit Ghost", b="Shader ghost with orbiting orbs on hit", c=ModuleCategory.VISUALS)
public class HitGhost
extends HaronModule {
    private static final Identifier WHITE = Identifier.of((String)"minecraft", (String)"textures/misc/white.png");
    private final NumberSetting lifeTime = new NumberSetting("Время жизни", 1500.0f, 550.0f, 3000.0f, 50.0f);
    private final NumberSetting riseHeight = new NumberSetting("Высота подъёма", 1.9f, 0.2f, 4.0f, 0.05f);
    private final NumberSetting drift = new NumberSetting("Дрейф", 1.7f, 0.2f, 3.5f, 0.05f);
    private final NumberSetting ghostAlpha = new NumberSetting("Прозрачность", 0.85f, 0.15f, 1.0f, 0.01f);
    private final NumberSetting maxGhosts = new NumberSetting("Макс. призраков", 3.0f, 1.0f, 12.0f, 1.0f);
    private final NumberSetting orbSize = new NumberSetting("Размер орбы", 0.34f, 0.12f, 0.9f, 0.01f);
    private final List<HitGhostSnapshot> ghosts = new ArrayList<HitGhostSnapshot>();
    private final Map<UUID, Long> lastSpawn = new HashMap<UUID, Long>();
    private PlayerEntityModel wideModel;
    private PlayerEntityModel slimModel;
    private final PlayerEntityRenderState animState = new PlayerEntityRenderState();

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @EventHandler
    public void onRender(WorldRenderPostEvent kvprd92) {
        if (this.ghosts.isEmpty()) {
            return;
        }
        this.ensureModels();
        if (this.wideModel == null) {
            return;
        }
        long l = System.currentTimeMillis();
        float f = this.lifeTime.get();
        Vec3d vec3d = HitGhost.c.gameRenderer.getCamera().getPos();
        MatrixStack matrixStack = kvprd92.a();
        VertexConsumerProvider.Immediate immediate = c.getBufferBuilders().getEntityVertexConsumers();
        Color color = ModuleManager.CLIENT_COLOR.n();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        try {
            Iterator<HitGhostSnapshot> iterator = this.ghosts.iterator();
            while (iterator.hasNext()) {
                Vec3d vec3d2;
                float f2;
                HitGhostSnapshot u7kn5e2 = iterator.next();
                float f3 = (float)(l - u7kn5e2.spawnAt()) / f;
                if (f3 >= 1.0f) {
                    iterator.remove();
                    continue;
                }
                float f4 = this.smooth(Math.min(1.0f, f3 / 0.08f));
                float f5 = f4 * (f2 = 1.0f - this.smooth(MathHelper.clamp((float)((f3 - 0.5f) / 0.5f), (float)0.0f, (float)1.0f)));
                if (f5 <= 0.01f || vec3d.squaredDistanceTo(vec3d2 = this.ghostWorldPos(u7kn5e2, f3)) < 1.3) continue;
                this.renderGhost(matrixStack, immediate, u7kn5e2, vec3d, vec3d2, f3, f5, color);
            }
            immediate.draw();
        }
        catch (Exception exception) {
            this.ghosts.clear();
        }
        finally {
            RenderSystem.enableCull();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableBlend();
            RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        }
    }

    public void onAttack(LivingEntity livingEntity) {
        if (HitGhost.c.player == null || livingEntity == null) {
            return;
        }
        long l = System.currentTimeMillis();
        Long l2 = this.lastSpawn.get(livingEntity.getUuid());
        if (l2 != null && l - l2 < 450L) {
            return;
        }
        this.lastSpawn.put(livingEntity.getUuid(), l);
        float f = c.getRenderTickCounter().getTickDelta(false);
        Vec3d vec3d = livingEntity.getLerpedPos(f);
        boolean bl = false;
        if (livingEntity instanceof AbstractClientPlayerEntity) {
            AbstractClientPlayerEntity abstractClientPlayerEntity = (AbstractClientPlayerEntity)livingEntity;
            bl = abstractClientPlayerEntity.getSkinTextures().model() == SkinTextures.Model.SLIM;
        }
        float f2 = MathHelper.lerpAngleDegrees((float)f, (float)livingEntity.prevBodyYaw, (float)livingEntity.bodyYaw);
        float f3 = MathHelper.lerpAngleDegrees((float)f, (float)livingEntity.prevHeadYaw, (float)livingEntity.headYaw) - f2;
        float f4 = MathHelper.lerp((float)f, (float)livingEntity.prevPitch, (float)livingEntity.getPitch());
        Vec3d vec3d2 = vec3d.subtract(HitGhost.c.player.getLerpedPos(f));
        if (vec3d2.lengthSquared() < 1.0E-4) {
            float f5 = (float)Math.toRadians(f2);
            vec3d2 = new Vec3d((double)(-MathHelper.sin((float)f5)), 0.0, (double)MathHelper.cos((float)f5));
        }
        Vec3d vec3d3 = vec3d2.normalize().add(0.0, 0.42, 0.0).normalize();
        this.ghosts.add(new HitGhostSnapshot(vec3d, vec3d3, bl, livingEntity.isInSneakingPose(), livingEntity.isBaby(), f2, f3, f4, livingEntity.limbAnimator.getPos(f), Math.min(livingEntity.limbAnimator.getSpeed(f), 1.0f), livingEntity.getHandSwingProgress(f), l));
        while (this.ghosts.size() > (int)this.maxGhosts.get()) {
            this.ghosts.removeFirst();
        }
    }

    private float smooth(float f) {
        f = MathHelper.clamp((float)f, (float)0.0f, (float)1.0f);
        return f * f * (3.0f - 2.0f * f);
    }

    private void applyPose(PlayerEntityModel playerEntityModel, HitGhostSnapshot u7kn5e2, float f) {
        float f2 = this.smooth(f);
        this.animState.bodyYaw = u7kn5e2.bodyYaw();
        this.animState.yawDegrees = u7kn5e2.headYaw();
        this.animState.pitch = u7kn5e2.pitch() - f2 * 12.0f;
        this.animState.limbFrequency = u7kn5e2.limbSwing();
        this.animState.limbAmplitudeMultiplier = u7kn5e2.limbSwingAmount() * (1.0f - f * 0.7f);
        this.animState.handSwingProgress = u7kn5e2.swingProgress() * (1.0f - f);
        this.animState.isInSneakingPose = u7kn5e2.sneaking();
        this.animState.baby = u7kn5e2.baby();
        this.animState.hatVisible = false;
        this.animState.jacketVisible = false;
        this.animState.leftSleeveVisible = false;
        this.animState.rightSleeveVisible = false;
        this.animState.leftPantsLegVisible = false;
        this.animState.rightPantsLegVisible = false;
        playerEntityModel.setAngles(this.animState);
        playerEntityModel.hat.visible = false;
        playerEntityModel.jacket.visible = false;
        playerEntityModel.leftSleeve.visible = false;
        playerEntityModel.rightSleeve.visible = false;
        playerEntityModel.leftPants.visible = false;
        playerEntityModel.rightPants.visible = false;
        playerEntityModel.head.visible = true;
        playerEntityModel.body.visible = true;
        playerEntityModel.leftArm.visible = true;
        playerEntityModel.rightArm.visible = true;
        playerEntityModel.leftLeg.visible = true;
        playerEntityModel.rightLeg.visible = true;
    }

    private void ensureModels() {
        if (this.wideModel == null && c.getLoadedEntityModels() != null) {
            this.wideModel = new PlayerEntityModel(c.getLoadedEntityModels().getModelPart(EntityModelLayers.PLAYER), false);
            this.slimModel = new PlayerEntityModel(c.getLoadedEntityModels().getModelPart(EntityModelLayers.PLAYER_SLIM), true);
        }
    }

    private void setupGhostMatrix(MatrixStack matrixStack, double d, double d2, double d3, float f, float f2, float f3) {
        matrixStack.translate(d, d2, d3);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(f));
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-f2));
        matrixStack.scale(-f3, -f3, f3);
        matrixStack.translate(0.0f, -1.501f, 0.0f);
    }

    private Vec3d ghostWorldPos(HitGhostSnapshot u7kn5e2, float f) {
        float f2 = 1.0f - (float)Math.pow(1.0f - f, 1.45);
        float f3 = this.smooth(f);
        double d = this.riseHeight.get() * f2;
        double d2 = this.drift.get() * f3;
        return u7kn5e2.pos().add(u7kn5e2.drift().x * d2, d + u7kn5e2.drift().y * d2 * 0.65, u7kn5e2.drift().z * d2);
    }

    @EventHandler
    public void onAttackEvent(AttackTargetEvent dt813s2) {
        Entity entity = dt813s2.getEntity();
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)entity;
            this.onAttack(livingEntity);
        }
    }

    private void renderGhost(MatrixStack matrixStack, VertexConsumerProvider.Immediate immediate, HitGhostSnapshot u7kn5e2, Vec3d vec3d, Vec3d vec3d2, float f, float f2, Color color) {
        float f3 = this.ghostAlpha.get() * f2;
        if (f3 <= 0.01f) {
            return;
        }
        float f4 = 1.0f - f * 0.18f;
        float f5 = 0.9375f * (u7kn5e2.baby() ? 0.5f : 1.0f) * f4;
        float f6 = MathHelper.sin((float)(f * (float)Math.PI)) * 10.0f + f * 22.0f;
        float f7 = f * 16.0f;
        PlayerEntityModel playerEntityModel = u7kn5e2.slim() ? this.slimModel : this.wideModel;
        this.applyPose(playerEntityModel, u7kn5e2, f);
        double d = vec3d2.x - vec3d.x;
        double d2 = vec3d2.y - vec3d.y;
        double d3 = vec3d2.z - vec3d.z;
        float f8 = 180.0f - u7kn5e2.bodyYaw() + f6;
        int n = 0xF000F0;
        VertexConsumer vertexConsumer = immediate.getBuffer(RenderLayer.getEntityTranslucent((Identifier)WHITE));
        RenderSystem.blendFunc((GlStateManager.SrcFactor)GlStateManager.SrcFactor.SRC_ALPHA, (GlStateManager.DstFactor)GlStateManager.DstFactor.ONE);
        for (int i = 2; i >= 1; --i) {
            float f9 = 1.0f + (float)i * 0.035f * (1.0f + f * 0.4f);
            float f10 = 0.22f / (float)i * f3;
            matrixStack.push();
            this.setupGhostMatrix(matrixStack, d, d2, d3, f8, f7, f5 * f9);
            int n2 = ColorHelper.getArgb((int)((int)(f10 * 255.0f)), (int)Math.min(255, 140 + color.getRed() / 3), (int)Math.min(255, 190 + color.getGreen() / 5), (int)255);
            playerEntityModel.render(matrixStack, vertexConsumer, 0xF000F0, OverlayTexture.DEFAULT_UV, n2);
            matrixStack.pop();
        }
        RenderSystem.defaultBlendFunc();
        matrixStack.push();
        this.setupGhostMatrix(matrixStack, d, d2, d3, f8, f7, f5);
        float f11 = 0.55f + 0.45f * MathHelper.sin((float)(f * 6.0f));
        int n3 = (int)(f3 * (150.0f + f11 * 50.0f));
        int n4 = ColorHelper.getArgb((int)MathHelper.clamp((int)n3, (int)0, (int)255), (int)Math.min(255, color.getRed() + 20), (int)Math.min(255, color.getGreen() + 20), (int)Math.min(255, color.getBlue() + 20));
        playerEntityModel.render(matrixStack, vertexConsumer, 0xF000F0, OverlayTexture.DEFAULT_UV, n4);
        matrixStack.pop();
    }

    @Override
    public void f() {
        super.f();
        this.ghosts.clear();
        this.lastSpawn.clear();
    }
}
