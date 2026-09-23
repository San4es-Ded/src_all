package su.sacura.features.modules.impl.render;

import com.google.common.eventbus.Subscribe;
import com.mojang.authlib.GameProfile;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import su.sacura.events.player.EntityDeathEvent;
import su.sacura.events.render.WorldRenderEvent;
import su.sacura.features.modules.api.core.Module;
import su.sacura.features.modules.api.core.ModuleAnnotations;
import su.sacura.features.modules.impl.Category;
import su.sacura.features.modules.settings.impl.BooleanSetting;
import su.sacura.features.modules.settings.impl.ModeSetting;
import su.sacura.util.impl.render.RenderWorld;
import su.sacura.util.type.ISetting;

@ModuleAnnotations(name = "Death Effect", category = Category.RENDER)
public class DeathEffectModule extends Module {
    BooleanSetting mobs = (new BooleanSetting("На мобов", Boolean.valueOf(false))).setDescription("Анимация работает на мобов");
    ModeSetting effectType = (new ModeSetting("Тип эффекта", "Крест", new String[] { "Крест", "Душа" })).setDescription("Устанавливает тип эффекта");
    private final Map<Entity, EntityRenderData> renderEntities = new ConcurrentHashMap<>();

    public DeathEffectModule() {
        addSettings(new ISetting[] { (ISetting)this.effectType, (ISetting)this.mobs });
    }

    @Subscribe
    public void onEntityDeath(EntityDeathEvent event) {
        if (mc.world == null || mc.player == null)
            return;
        Entity entity = event.getEntity();
        if (!(entity instanceof LivingEntity))
            return;
        if (!((Boolean)this.mobs.get()).booleanValue() && !(entity instanceof PlayerEntity))
            return;
        if (entity == mc.player || this.renderEntities.containsKey(entity))
            return;

        OtherClientPlayerEntity fakePlayer = null;
        if (entity instanceof PlayerEntity) {
            try {
                GameProfile profile = ((PlayerEntity)entity).getGameProfile();
                fakePlayer = new OtherClientPlayerEntity(mc.world, profile);
                fakePlayer.setPitch(-30.0F);
                fakePlayer.setYaw(entity.getYaw());
                fakePlayer.setHeadYaw(entity.getYaw());
                fakePlayer.setBodyYaw(entity.getYaw());
                fakePlayer.setInvisible(false);
                fakePlayer.setNoGravity(true);
                fakePlayer.setCustomName(Text.literal("Ghost_" + profile.getId()));
                // fakePlayer не добавляем в мир — иначе его тикает игровой цикл
            } catch (Throwable t) {
                System.err.println("[DeathEffect] failed to create fake player:");
                t.printStackTrace();
                fakePlayer = null;
            }
        }

        this.renderEntities.put(entity, new EntityRenderData(
                System.currentTimeMillis(),
                entity.getYaw(),
                entity.getPos(),
                entity,
                fakePlayer
        ));
    }

    @Subscribe
    public void onWorldRender(WorldRenderEvent e) {
        if (mc.world == null || mc.player == null)
            return;
        MatrixStack stack = e.getStack();
        float tickDelta = e.getPartialTicks();
        List<Entity> entitiesToRemove = new ArrayList<>();

        this.renderEntities.forEach((entity, data) -> {
            if (System.currentTimeMillis() - data.getTimestamp() > 3000L) {
                entitiesToRemove.add(entity);
            } else {
                float timeProgress = (float)(System.currentTimeMillis() - data.getTimestamp()) / 3000.0F;
                int alpha = (int)(255.0F * (1.0F - timeProgress));

                if (this.effectType.is("Крест")) {
                    int color = (new Color(255, 255, 255, alpha)).getRGB();
                    float yaw = (float)Math.toRadians((data.getYaw() + 95.0F));
                    Vec3d pos = data.getStartPos();
                    RenderWorld.drawLine(pos, pos.add(0.0D, 3.0D, 0.0D), color, 5.0F, true);
                    float armLength = 1.0F;
                    float yOffset = 2.3F;
                    Vec3d start = pos.add(-armLength * Math.sin(yaw), yOffset, armLength * Math.cos(yaw));
                    Vec3d end = pos.add(armLength * Math.sin(yaw), yOffset, -armLength * Math.cos(yaw));
                    RenderWorld.drawLine(start, end, color, 5.0F, true);
                } else if (this.effectType.is("Душа")) {
                    float yOffset = timeProgress * 3.0F;
                    Vec3d soulPos = data.getStartPos().add(0.0D, yOffset, 0.0D);

                    OtherClientPlayerEntity fakePlayer = data.getFakePlayer();
                    if (fakePlayer != null) {
                        fakePlayer.setPos(soulPos.x, soulPos.y, soulPos.z);
                        fakePlayer.prevX = soulPos.x;
                        fakePlayer.prevY = soulPos.y;
                        fakePlayer.prevZ = soulPos.z;
                        fakePlayer.lastRenderX = soulPos.x;
                        fakePlayer.lastRenderY = soulPos.y;
                        fakePlayer.lastRenderZ = soulPos.z;

                        fakePlayer.setYaw(data.getYaw());
                        fakePlayer.setHeadYaw(data.getYaw());
                        fakePlayer.setBodyYaw(data.getYaw());
                        fakePlayer.prevYaw = data.getYaw();

                        RenderWorld.drawEntity(fakePlayer, soulPos, data.getYaw(), alpha, stack, tickDelta);
                    } else {
                        Entity original = data.getEntity();
                        if (original instanceof LivingEntity && !original.isRemoved()) {
                            RenderWorld.drawEntity(original, soulPos, data.getYaw(), alpha, stack, tickDelta);
                        }
                    }
                }
            }
        });

        entitiesToRemove.forEach(this.renderEntities::remove);
    }

    private static class EntityRenderData {
        private final long timestamp;
        private final float yaw;
        private final Vec3d startPos;
        private final Entity entity;
        private final GameProfile gameProfile;
        private final EntityPose pose;
        private final OtherClientPlayerEntity fakePlayer;

        public EntityRenderData(long timestamp, float yaw, Vec3d startPos, Entity entity, OtherClientPlayerEntity fakePlayer) {
            this.timestamp = timestamp;
            this.yaw = yaw;
            this.startPos = startPos;
            this.entity = entity;
            this.gameProfile = (entity instanceof PlayerEntity) ? ((PlayerEntity)entity).getGameProfile() : null;
            this.pose = entity.getPose();
            this.fakePlayer = fakePlayer;
        }

        public long getTimestamp() { return this.timestamp; }
        public float getYaw() { return this.yaw; }
        public Vec3d getStartPos() { return this.startPos; }
        public Entity getEntity() { return this.entity; }
        public GameProfile getGameProfile() { return this.gameProfile; }
        public EntityPose getPose() { return this.pose; }
        public OtherClientPlayerEntity getFakePlayer() { return this.fakePlayer; }
    }
}