package haron.modules.utilities;

import com.mojang.authlib.GameProfile;
import haron.events.WorldChangedEvent;
import haron.events.ClientTickEvent;
import haron.events.MouseButtonEvent;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.settings.NumberSetting;
import haron.settings.StringSetting;
import haron.settings.BooleanSetting;
import java.util.UUID;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;

@ModuleInfo(a="FakePlayer", b="Спавнит копию игрока на клиенте для тренировки.", c=ModuleCategory.UTILITIES)
public final class FakePlayer
extends HaronModule {
    public final StringSetting name = new StringSetting("Name", "FakePlayer");
    public final BooleanSetting lookAtPlayer = new BooleanSetting("Смотреть на игрока", true);
    public final BooleanSetting totem = new BooleanSetting("Тотем", true);
    public final BooleanSetting copyEquip = new BooleanSetting("Копировать снаряжение", true);
    public final NumberSetting health = new NumberSetting("Здоровье", 20.0f, 1.0f, 40.0f, 1.0f);
    private OtherClientPlayerEntity fakePlayer;
    private float currentHp;
    private static final byte STATUS_HURT = 2;
    private static final byte STATUS_TOTEM = 35;
    private static final int FAKE_ID = -42069;

    @EventHandler
    public void onTick(ClientTickEvent q8krcw2) {
        if (FakePlayer.c.player == null || FakePlayer.c.world == null) {
            return;
        }
        if (this.fakePlayer == null) {
            this.spawn();
            return;
        }
        if (this.fakePlayer.hurtTime > 0) {
            --this.fakePlayer.hurtTime;
        }
        this.fakePlayer.setHealth(this.currentHp);
        if (this.lookAtPlayer.get()) {
            double d = FakePlayer.c.player.getX() - this.fakePlayer.getX();
            double d2 = FakePlayer.c.player.getZ() - this.fakePlayer.getZ();
            double d3 = FakePlayer.c.player.getEyeY() - this.fakePlayer.getEyeY();
            double d4 = Math.sqrt(d * d + d2 * d2);
            float f = (float)Math.toDegrees(Math.atan2(d2, d)) - 90.0f;
            float f2 = (float)(-Math.toDegrees(Math.atan2(d3, d4)));
            this.fakePlayer.setYaw(f);
            this.fakePlayer.setPitch(f2);
            this.fakePlayer.headYaw = f;
            this.fakePlayer.bodyYaw = f;
        }
    }

    @Override
    public void onDisable() {
        this.remove();
    }

    private void onFatalHit() {
        if (this.fakePlayer == null || FakePlayer.c.world == null) {
            return;
        }
        if (!this.totem.get() || this.fakePlayer.getEquippedStack(EquipmentSlot.OFFHAND).getItem() != Items.TOTEM_OF_UNDYING && this.fakePlayer.getEquippedStack(EquipmentSlot.MAINHAND).getItem() != Items.TOTEM_OF_UNDYING) {
            FakePlayer.c.world.playSound(this.fakePlayer.getX(), this.fakePlayer.getY(), this.fakePlayer.getZ(), SoundEvents.ENTITY_PLAYER_DEATH, SoundCategory.PLAYERS, 1.0f, 1.0f, false);
            this.remove();
            this.currentHp = this.health.get();
            this.spawn();
            return;
        }
        this.currentHp = 1.0f;
        this.fakePlayer.setHealth(this.currentHp);
        this.fakePlayer.handleStatus((byte)35);
        if (this.fakePlayer.getEquippedStack(EquipmentSlot.OFFHAND).getItem() == Items.TOTEM_OF_UNDYING) {
            this.fakePlayer.equipStack(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
        } else {
            this.fakePlayer.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        }
        FakePlayer.c.world.playSound(this.fakePlayer.getX(), this.fakePlayer.getY(), this.fakePlayer.getZ(), SoundEvents.ITEM_TOTEM_USE, SoundCategory.PLAYERS, 1.0f, 1.0f, false);
    }

    private void applyHit() {
        if (FakePlayer.c.player == null || FakePlayer.c.world == null || this.fakePlayer == null) {
            return;
        }
        float f = FakePlayer.c.player.getAttackCooldownProgress(0.5f);
        if (f < 0.2f) {
            return;
        }
        float f2 = (float)FakePlayer.c.player.getAttributeValue(EntityAttributes.ATTACK_DAMAGE) * (0.2f + f * f * 0.8f);
        if (!(FakePlayer.c.player.isOnGround() || FakePlayer.c.player.fallDistance <= 0.0f || FakePlayer.c.player.isSprinting() || FakePlayer.c.player.isClimbing() || FakePlayer.c.player.isTouchingWater() || FakePlayer.c.player.getVehicle() != null || FakePlayer.c.player.hasStatusEffect(StatusEffects.BLINDNESS) || f <= 0.9f)) {
            f2 *= 1.5f;
            FakePlayer.c.player.addCritParticles((Entity)(Object)this.fakePlayer);
            FakePlayer.c.world.playSound(this.fakePlayer.getX(), this.fakePlayer.getY(), this.fakePlayer.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, SoundCategory.PLAYERS, 1.0f, 1.0f, false);
        } else if (f > 0.9f) {
            FakePlayer.c.world.playSound(this.fakePlayer.getX(), this.fakePlayer.getY(), this.fakePlayer.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, SoundCategory.PLAYERS, 1.0f, 1.0f, false);
        } else {
            FakePlayer.c.world.playSound(this.fakePlayer.getX(), this.fakePlayer.getY(), this.fakePlayer.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_WEAK, SoundCategory.PLAYERS, 1.0f, 1.0f, false);
        }
        float f3 = (float)this.fakePlayer.getAttributeValue(EntityAttributes.ARMOR);
        float f4 = Math.max(0.0f, f2 * (1.0f - Math.min(20.0f, Math.max(f3 * 0.2f, Math.min(f3, f3 - f2 / (2.0f + (float)this.fakePlayer.getAttributeValue(EntityAttributes.ARMOR_TOUGHNESS) / 4.0f)))) / 25.0f));
        this.fakePlayer.hurtTime = 10;
        this.fakePlayer.maxHurtTime = 10;
        this.fakePlayer.handleStatus((byte)2);
        FakePlayer.c.world.playSound(this.fakePlayer.getX(), this.fakePlayer.getY(), this.fakePlayer.getZ(), SoundEvents.ENTITY_PLAYER_HURT, SoundCategory.PLAYERS, 1.0f, 1.0f, false);
        FakePlayer.c.player.resetLastAttackedTicks();
        this.currentHp = Math.max(0.0f, this.currentHp - f4);
        this.fakePlayer.setHealth(this.currentHp);
        if (this.currentHp <= 0.0f) {
            this.onFatalHit();
        }
    }

    private void spawn() {
        if (FakePlayer.c.player == null || FakePlayer.c.world == null) {
            return;
        }
        this.remove();
        this.currentHp = this.health.get();
        ClientWorld clientWorld = FakePlayer.c.world;
        String string = this.name.get();
        if (string == null || string.isBlank()) {
            string = "FakePlayer";
        }
        OtherClientPlayerEntity otherClientPlayerEntity = new OtherClientPlayerEntity(clientWorld, new GameProfile(UUID.randomUUID(), string));
        otherClientPlayerEntity.copyFrom((Entity)FakePlayer.c.player);
        otherClientPlayerEntity.setId(-42069);
        double d = Math.toRadians(FakePlayer.c.player.getYaw());
        otherClientPlayerEntity.refreshPositionAndAngles(FakePlayer.c.player.getX() - Math.sin(d) * 2.0, FakePlayer.c.player.getY(), FakePlayer.c.player.getZ() + Math.cos(d) * 2.0, FakePlayer.c.player.getYaw() + 180.0f, 0.0f);
        otherClientPlayerEntity.headYaw = otherClientPlayerEntity.getYaw();
        otherClientPlayerEntity.bodyYaw = otherClientPlayerEntity.getYaw();
        otherClientPlayerEntity.setHealth(this.currentHp);
        if (this.copyEquip.get()) {
            for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
                otherClientPlayerEntity.equipStack(equipmentSlot, FakePlayer.c.player.getEquippedStack(equipmentSlot).copy());
            }
        }
        if (this.totem.get()) {
            otherClientPlayerEntity.equipStack(EquipmentSlot.OFFHAND, new ItemStack((ItemConvertible)Items.TOTEM_OF_UNDYING));
        }
        clientWorld.addEntity((Entity)otherClientPlayerEntity);
        this.fakePlayer = otherClientPlayerEntity;
    }

    @EventHandler
    public void onWorldChange(WorldChangedEvent m7z9q12) {
        this.remove();
        if (this.k()) {
            this.currentHp = this.health.get();
            this.spawn();
        }
    }

    @EventHandler
    public void onMouseButton(MouseButtonEvent tp78k22) {
        EntityHitResult entityHitResult;
        if (FakePlayer.c.player == null || FakePlayer.c.world == null || this.fakePlayer == null || tp78k22.button() != 0 || tp78k22.action() != 1 || FakePlayer.c.currentScreen != null) {
            return;
        }
        if (FakePlayer.c.crosshairTarget instanceof EntityHitResult && (entityHitResult = (EntityHitResult)FakePlayer.c.crosshairTarget).getEntity() == this.fakePlayer) {
            tp78k22.cancel();
            this.applyHit();
        }
    }

    public FakePlayer() {
        this.collectSettings();
    }

    private void remove() {
        if (this.fakePlayer == null) {
            return;
        }
        if (FakePlayer.c.world != null) {
            FakePlayer.c.world.removeEntity(this.fakePlayer.getId(), Entity.RemovalReason.DISCARDED);
        }
        this.fakePlayer = null;
    }

    @Override
    public void onEnable() {
        this.spawn();
    }
}

