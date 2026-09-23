package wtf.wyvern.client.modules.impl.movement;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.ModeSetting;
import wtf.wyvern.client.modules.api.setting.impl.SliderSetting;
import wtf.wyvern.client.modules.impl.combat.Aura;
import wtf.wyvern.core.eventbus.EventTarget;
import wtf.wyvern.core.events.impl.player.EventMove;
import wtf.wyvern.core.events.impl.player.EventUpdate;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(
        name = "ElytraMotion",
        category = Category.MOVEMENT,
        description = "Зависает в воздухе на элитрах рядом с целью"
)
@FastNative
public final class ElytraMotion extends Module {
    public static final ElytraMotion INSTANCE = new ElytraMotion();

    private final ModeSetting mode = new ModeSetting("Режим", "Старый", "Новый");
    public final SliderSetting attackDistance = new SliderSetting(
            "Дистанция работы",
            2.4F,
            0.1F,
            5.0F,
            0.01F
    );

    public boolean freeze;
    private boolean waitTarget;

    private ElytraMotion() {
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mc.player == null) {
            return;
        }

        if (mode.is("Старый")) {
            updateOld();
            return;
        }

        updateNew();
    }

    @EventTarget
    public void onMove(EventMove event) {
        if (mode.is("Старый") && freeze) {
            event.setMovePos(new Vec3d(0.0D, 0.0D, 0.0D));
        }
    }

    private void updateOld() {
        waitTarget = false;
        if (mc.player != null) {
            mc.player.setNoGravity(false);
        }

        if (!mc.player.isGliding()) {
            freeze = false;
            return;
        }

        freeze = check(Aura.INSTANCE);
    }

    private void updateNew() {
        freeze = false;
        LivingEntity target = Aura.INSTANCE.getTarget();
        if (target == null) {
            if (!waitTarget) {
                mc.player.setNoGravity(false);
                waitTarget = true;
            }
            return;
        }

        waitTarget = false;
        double distance = attackDistance.getCurrent();
        Vec3d targetCenter = target.getBoundingBox().getCenter().add(
                0.0D,
                (target.getY() - target.prevY) * 2.0D,
                0.0D
        );
        if (mc.player.isGliding()
                && mc.player.getEyePos().distanceTo(targetCenter) < distance
                && !target.isGliding()) {
            mc.player.setVelocity(0.0D, 0.0D, 0.0D);
            mc.player.setNoGravity(true);
        } else {
            mc.player.setNoGravity(false);
        }
    }

    public boolean check(Aura aura) {
        if (!aura.isEnabled()) {
            return false;
        }

        LivingEntity target = aura.getTarget();
        if (target == null || mc.player == null || !mc.player.isGliding()) {
            return false;
        }

        return target.distanceTo(mc.player) < attackDistance.getCurrent();
    }

    @Override
    public void onEnable() {
        freeze = false;
        waitTarget = false;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        freeze = false;
        waitTarget = false;
        if (mc.player != null) {
            mc.player.setNoGravity(false);
        }
        super.onDisable();
    }
}
