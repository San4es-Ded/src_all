package wtf.wyvern.client.modules.impl.movement;

import net.minecraft.entity.effect.StatusEffects;
import wtf.wyvern.client.modules.impl.combat.Aura;
import wtf.wyvern.core.events.impl.other.EventTick;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.BooleanSetting;
import wtf.wyvern.utility.game.player.MovingUtil;
import wtf.wyvern.core.eventbus.EventTarget;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(
        name = "AutoSprint",
        category = Category.MOVEMENT,
        description = "Автоматически включает спринт"
)
@FastNative
public final class AutoSprint extends Module {
    public static final AutoSprint INSTANCE = new AutoSprint();

    private final BooleanSetting sprintInWater = new BooleanSetting("Спринт в воде", true);

    private AutoSprint() {
    }

    public boolean shouldKeepSprintInWater() {
        return isEnabled() && sprintInWater.isEnabled();
    }

    @Override
    public void onDisable() {
        if (mc.options != null) {
            mc.options.sprintKey.setPressed(false);
        }
        if (mc.player != null) {
            mc.player.setSprinting(false);
        }
        super.onDisable();
    }

    @EventTarget
    public void onUpdate(EventTick event) {
        if (mc.player == null) {
            return;
        }

        if (Aura.INSTANCE.shouldHoldSprintForCritical()) {
            mc.player.setSprinting(false);
            return;
        }

        boolean inWater = mc.player.isTouchingWater() || mc.player.isSubmergedInWater();
        boolean hasMovement = MovingUtil.hasPlayerMovement();

        if (inWater) {
            // Let vanilla swimming handle the sprint transition. Forcing
            // setSprinting(true) here fights its water checks every tick and
            // repeatedly sends START/STOP sprint packets.
            boolean waterSprint = sprintInWater.isEnabled()
                    && hasMovement
                    && !mc.player.isSneaking()
                    && !mc.player.isUsingItem()
                    && !mc.player.hasVehicle();
            mc.options.sprintKey.setPressed(waterSprint);
            if (!waterSprint && mc.player.isSprinting()) {
                mc.player.setSprinting(false);
            }
            return;
        }

        mc.options.sprintKey.setPressed(false);

        boolean canSprint = hasMovement
                && mc.player.canSprint()
                && !mc.player.isSneaking()
                && !mc.player.isUsingItem()
                && !mc.player.isGliding()
                && !mc.player.hasStatusEffect(StatusEffects.BLINDNESS)
                && !mc.player.horizontalCollision;

        if (mc.player.isSprinting() != canSprint) {
            mc.player.setSprinting(canSprint);
        }
    }
}
