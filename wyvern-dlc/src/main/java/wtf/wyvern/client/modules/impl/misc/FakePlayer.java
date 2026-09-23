package wtf.wyvern.client.modules.impl.misc;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.core.eventbus.EventTarget;
import wtf.wyvern.core.events.impl.other.EventGameUpdate;
import wtf.wyvern.core.events.impl.player.EventAttack;

import java.util.UUID;

@ModuleAnnotation(
        name = "FakePlayer",
        description = "Создаёт локальную копию игрока",
        category = Category.MISC
)
public final class FakePlayer extends Module {
    public static final FakePlayer INSTANCE = new FakePlayer();

    private OtherClientPlayerEntity fakePlayer;
    private ClientWorld fakeWorld;
    private Placement placement = Placement.HERE;
    private boolean trainingActive;
    private boolean enabledBeforeTraining;
    private Placement placementBeforeTraining = Placement.HERE;
    private int deadSinceAge = Integer.MIN_VALUE;
    private int lastFeedbackAge = Integer.MIN_VALUE;

    private FakePlayer() {
    }

    @Override
    public void onEnable() {
        super.onEnable();
        spawn();
    }

    @Override
    public void onDisable() {
        remove();
        super.onDisable();
    }

    @EventTarget
    private void onGameUpdate(EventGameUpdate event) {
        if (mc.player == null || mc.world == null) {
            remove();
        } else if (fakePlayer == null || fakeWorld != mc.world || fakePlayer.isRemoved()) {
            spawn();
        } else if (!fakePlayer.isAlive()) {
            if (deadSinceAge == Integer.MIN_VALUE) {
                deadSinceAge = mc.player.age;
            } else if (mc.player.age - deadSinceAge >= 12) {
                spawn();
            }
        } else {
            deadSinceAge = Integer.MIN_VALUE;
        }
    }

    @EventTarget
    private void onAttack(EventAttack event) {
        if (isFakePlayer(event.getTarget())) {
            // The entity only exists in ClientWorld. Never send its negative
            // entity id to the server, but keep vanilla weapon cooldown useful
            // for local practice and Neuro's timing recorder.
            event.cancel();
            // During Neuro recording the manager owns feedback so it can read
            // the pre-reset cooldown. Outside training this listener does it.
            if (!trainingActive) {
                simulateAttackFeedback();
            }
            if (mc.player != null) {
                mc.player.resetLastAttackedTicks();
            }
        }
    }

    private void spawn() {
        if (mc.player == null || mc.world == null) {
            return;
        }

        remove();
        GameProfile profile = new GameProfile(UUID.randomUUID(), "FakePlayer");
        profile.getProperties().putAll(mc.player.getGameProfile().getProperties());

        OtherClientPlayerEntity entity = new OtherClientPlayerEntity(mc.world, profile);
        entity.copyPositionAndRotation(mc.player);
        if (placement == Placement.NEAR) {
            Vec3d forward = Vec3d.fromPolar(0.0F, mc.player.getYaw());
            entity.setPosition(mc.player.getPos().add(forward.x * 2.5D, 0.0D, forward.z * 2.5D));
        }
        entity.setPose(mc.player.getPose());
        entity.setYaw(mc.player.getYaw());
        entity.prevYaw = mc.player.prevYaw;
        entity.setPitch(mc.player.getPitch());
        entity.prevPitch = mc.player.prevPitch;
        entity.setBodyYaw(mc.player.getBodyYaw());
        entity.prevBodyYaw = mc.player.prevBodyYaw;
        entity.setHeadYaw(mc.player.getHeadYaw());
        entity.prevHeadYaw = mc.player.prevHeadYaw;
        entity.setHealth(entity.getMaxHealth());
        entity.setSprinting(mc.player.isSprinting());
        entity.setSneaking(mc.player.isSneaking());
        entity.setSwimming(mc.player.isSwimming());
        entity.setOnGround(mc.player.isOnGround());
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            entity.equipStack(slot, mc.player.getEquippedStack(slot).copy());
        }

        int entityId = -1337;
        while (mc.world.getEntityById(entityId) != null) {
            entityId--;
        }
        entity.setId(entityId);
        mc.world.addEntity(entity);
        fakePlayer = entity;
        fakeWorld = mc.world;
        deadSinceAge = Integer.MIN_VALUE;
        lastFeedbackAge = Integer.MIN_VALUE;
    }

    private void remove() {
        if (fakePlayer != null && fakeWorld != null && !fakePlayer.isRemoved()) {
            fakeWorld.removeEntity(fakePlayer.getId(), Entity.RemovalReason.DISCARDED);
        }
        fakePlayer = null;
        fakeWorld = null;
        deadSinceAge = Integer.MIN_VALUE;
        lastFeedbackAge = Integer.MIN_VALUE;
    }

    public OtherClientPlayerEntity place(Placement placement) {
        this.placement = placement == null ? Placement.NEAR : placement;
        if (!isEnabled()) {
            setToggled(true);
        } else {
            spawn();
        }
        return fakePlayer;
    }

    public void dismiss() {
        if (isEnabled()) {
            setToggled(false);
        } else {
            remove();
        }
    }

    public OtherClientPlayerEntity beginTraining(Placement placement) {
        if (!trainingActive) {
            enabledBeforeTraining = isEnabled();
            placementBeforeTraining = this.placement;
        }
        trainingActive = true;
        this.placement = placement == null ? Placement.NEAR : placement;
        if (!isEnabled()) {
            setToggled(true);
        } else {
            spawn();
        }
        return fakePlayer;
    }

    public void endTraining() {
        if (!trainingActive) {
            return;
        }
        trainingActive = false;
        if (!enabledBeforeTraining) {
            dismiss();
        } else {
            this.placement = placementBeforeTraining;
            if (isEnabled()) {
                spawn();
            } else {
                setToggled(true);
            }
        }
    }

    public OtherClientPlayerEntity ensureTrainingTarget() {
        if (!trainingActive || mc.player == null || mc.world == null) {
            return null;
        }
        if (!isEnabled()) {
            setToggled(true);
        } else if (fakePlayer == null || fakeWorld != mc.world || fakePlayer.isRemoved()) {
            spawn();
        }
        return fakePlayer != null && fakePlayer.isAlive() ? fakePlayer : null;
    }

    /**
     * Recreates the vanilla client feedback which is lost when the attack on
     * this client-only entity is cancelled. No packet containing the dummy's
     * negative entity id is sent to the server.
     */
    public void simulateAttackFeedback() {
        OtherClientPlayerEntity target = fakePlayer;
        if (mc.player == null || mc.world == null || target == null || !target.isAlive()
                || lastFeedbackAge == mc.player.age) {
            return;
        }
        lastFeedbackAge = mc.player.age;

        float cooldown = mc.player.getAttackCooldownProgress(0.5F);
        boolean strong = cooldown >= 0.9F;
        boolean critical = strong
                && !mc.player.isSprinting()
                && !mc.player.isOnGround()
                && mc.player.fallDistance > 0.0F
                && mc.player.getVelocity().y < -1.0E-4D
                && !mc.player.isClimbing()
                && !mc.player.isTouchingWater()
                && !mc.player.hasVehicle()
                && !mc.player.hasStatusEffect(StatusEffects.BLINDNESS);

        double x = target.getX();
        double y = target.getY() + target.getHeight() * 0.5D;
        double z = target.getZ();
        SoundEvent attackSound = critical
                ? SoundEvents.ENTITY_PLAYER_ATTACK_CRIT
                : strong
                ? (mc.player.isSprinting()
                ? SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK
                : SoundEvents.ENTITY_PLAYER_ATTACK_STRONG)
                : SoundEvents.ENTITY_PLAYER_ATTACK_WEAK;
        mc.world.playSound(x, y, z, attackSound,
                SoundCategory.PLAYERS, 1.0F, 1.0F, false);
        mc.world.playSound(x, y, z, SoundEvents.ENTITY_PLAYER_HURT,
                SoundCategory.PLAYERS, 0.85F,
                0.9F + mc.world.getRandom().nextFloat() * 0.2F, false);

        // animateDamage supplies the directional tilt; hurtTime produces the
        // red damage overlay even though no server damage packet exists.
        target.animateDamage(mc.player.getYaw());
        target.maxHurtTime = 10;
        target.hurtTime = 10;

        if (critical) {
            for (int index = 0; index < 12; index++) {
                double offsetX = (mc.world.getRandom().nextDouble() - 0.5D) * target.getWidth();
                double offsetY = mc.world.getRandom().nextDouble() * target.getHeight();
                double offsetZ = (mc.world.getRandom().nextDouble() - 0.5D) * target.getWidth();
                mc.world.addParticle(ParticleTypes.CRIT,
                        x + offsetX, target.getY() + offsetY, z + offsetZ,
                        offsetX * 0.35D, 0.08D + offsetY * 0.08D, offsetZ * 0.35D);
            }
        }

        double baseDamage = mc.player.getAttributeValue(EntityAttributes.ATTACK_DAMAGE);
        double cooldownDamage = 0.2D + cooldown * cooldown * 0.8D;
        float damage = (float) (baseDamage * cooldownDamage * (critical ? 1.5D : 1.0D));
        target.setHealth(Math.max(0.0F, target.getHealth() - Math.max(0.1F, damage)));
        if (!target.isAlive()) {
            deadSinceAge = mc.player.age;
        }
    }

    public boolean isFakePlayer(Entity entity) {
        return entity != null && entity == fakePlayer;
    }

    public OtherClientPlayerEntity getFakePlayer() {
        return fakePlayer;
    }

    public enum Placement {
        HERE,
        NEAR
    }
}
