package wtf.wyvern.client.modules.impl.combat;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import wtf.wyvern.Wyvern;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.BooleanSetting;
import wtf.wyvern.client.modules.api.setting.impl.MultiBooleanSetting;
import wtf.wyvern.client.modules.api.setting.impl.SliderSetting;
import wtf.wyvern.core.eventbus.EventTarget;
import wtf.wyvern.core.events.impl.other.EventTick;
import wtf.wyvern.utility.math.Timer;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(
        name = "TriggerBot",
        category = Category.COMBAT,
        description = "Автоматически бьет при наведении на цель"
)
@FastNative
public final class TriggerBot extends Module {

    public static final TriggerBot INSTANCE = new TriggerBot();

    public final SliderSetting distance = new SliderSetting("Дистанция удара", 3.0F, 1.0F, 6.0F, 0.1F);
    public final BooleanSetting weaponOnly = new BooleanSetting("Только с оружием", true);
    public final BooleanSetting smartCriticals = new BooleanSetting("Умные Криты", false);
    public final BooleanSetting tpsSync = new BooleanSetting("Синхронизация TPS", false);
    private final MultiBooleanSetting targetTypeSetting = MultiBooleanSetting.create(
            "Атаковать",
            List.of("Игроков", "Мобов", "Животных", "Друзей", "Голых", "Невидимых")
    );

    private final Timer attackTimer = new Timer();

    private TriggerBot() {
    }

    @FastNative
    @EventTarget
    public void onTick(EventTick e) {
        if (mc.player == null || mc.world == null || !isEnabled()) return;

        if (weaponOnly.isEnabled()) {
            var mainStack = mc.player.getMainHandStack();
            if (mainStack.isEmpty() || !(mainStack.getItem() instanceof SwordItem || mainStack.getItem() instanceof AxeItem)) {
                return;
            }
        }

        HitResult hit = mc.crosshairTarget;
        if (hit == null || hit.getType() != HitResult.Type.ENTITY) {
            return;
        }

        Entity targetEntity = ((EntityHitResult) hit).getEntity();
        if (!(targetEntity instanceof LivingEntity target) || !isValid(target)) {
            return;
        }

        if (mc.player.getAttackCooldownProgress(0.5F) < 0.9F) {
            return;
        }

        long baseDelay = 458L;
        long adjustedDelay = tpsSync.isEnabled() ? TpsSync.getAdjustedCooldown(baseDelay, true) : baseDelay;
        if (!attackTimer.finished(adjustedDelay)) {
            return;
        }

        if (smartCriticals.isEnabled() && !isFallingCriticalWindow() && !mc.player.isOnGround()) {
            return;
        }

        mc.interactionManager.attackEntity(mc.player, target);
        mc.player.swingHand(Hand.MAIN_HAND);
        attackTimer.reset();
    }

    private boolean isFallingCriticalWindow() {
        return !mc.player.isOnGround()
                && mc.player.fallDistance > 0.0F
                && mc.player.getVelocity().y < -1.0E-4D
                && !mc.player.isTouchingWater()
                && !mc.player.isInLava()
                && !mc.player.isClimbing()
                && !mc.player.hasVehicle()
                && !mc.player.getAbilities().flying;
    }

    public boolean isValid(LivingEntity entity) {
        if (entity == mc.player || !entity.isAlive() || entity.getHealth() <= 0.0F) {
            return false;
        }
        if (!mc.player.isAlive() || mc.player.getHealth() <= 0.0F) {
            return false;
        }
        if (entity instanceof ArmorStandEntity) {
            return false;
        }

        if (entity instanceof PlayerEntity player) {
            boolean invisible = player.isInvisible();
            boolean naked = isNaked(player);

            if (invisible) {
                if (!this.targetTypeSetting.isEnable("Невидимых")) return false;
                if (naked && !this.targetTypeSetting.isEnable("Голых")) return false;
            } else if (naked) {
                if (!this.targetTypeSetting.isEnable("Голых")) return false;
            } else if (!this.targetTypeSetting.isEnable("Игроков")) {
                return false;
            }

            if (!this.targetTypeSetting.isEnable("Друзей")
                    && Wyvern.INSTANCE.getFriendManager().isFriend(entity.getName().getString())) {
                return false;
            }

            if (AntiBot.INSTANCE.isBot(player)) {
                return false;
            }
        } else if (entity instanceof PassiveEntity || entity instanceof FishEntity) {
            if (!this.targetTypeSetting.isEnable("Животных") || Wyvern.INSTANCE.getServerHandler().isPvp()) {
                return false;
            }
        } else if (entity instanceof HostileEntity || entity instanceof AmbientEntity) {
            if (!this.targetTypeSetting.isEnable("Мобов") || Wyvern.INSTANCE.getServerHandler().isPvp()) {
                return false;
            }
        } else {
            return false;
        }

        double dist = mc.player.getEyePos().distanceTo(entity.getBoundingBox().getCenter());
        return !(dist > this.distance.getCurrent());
    }

    private boolean isNaked(PlayerEntity player) {
        for (ItemStack stack : player.getArmorItems()) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
