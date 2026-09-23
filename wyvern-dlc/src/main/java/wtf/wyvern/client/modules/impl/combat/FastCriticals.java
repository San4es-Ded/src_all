package wtf.wyvern.client.modules.impl.combat;

import wtf.wyvern.core.eventbus.EventTarget;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import wtf.wyvern.core.events.impl.player.EventAttack;
import wtf.wyvern.core.events.impl.player.EventPostMotion;
import wtf.wyvern.core.events.impl.server.EventPacket;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.ModeSetting;
import wtf.wyvern.utility.game.other.NetworkUtils;
import wtf.wyvern.utility.game.player.PlayerIntersectionUtil;
import wtf.wyvern.utility.math.MathUtil;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(
        name = "FastCriticals",
        category = Category.COMBAT,
        description = "Быстрые криты через пакет движения"
)
public final class FastCriticals extends Module {
    public static final FastCriticals INSTANCE = new FastCriticals();

    private final ModeSetting mode = new ModeSetting("Режим", "ReallyWorld", "Grim 1.17+", "RW2");
    private boolean preparedCobwebAttack;

    private FastCriticals() {
    }

    @FastNative
    @EventTarget
    public void onPacket(EventPacket event) {
        if (!event.isSent()) return;
        if (!(event.getPacket() instanceof PlayerInteractEntityC2SPacket packet)) return;
        if (mc.player == null || mc.world == null) return;

        if (!isAttackPacket(packet)) return;

        // EventAttack has already supplied the web-safe movement packet. Let
        // the real attack through and do not add a second critical spoof.
        if (preparedCobwebAttack) {
            preparedCobwebAttack = false;
            return;
        }

        if (mc.player.isGliding() || PlayerIntersectionUtil.isPlayerInBlock(Blocks.COBWEB)) return;
        if (mode.is("RW2") && (mc.player.hasStatusEffect(StatusEffects.SLOW_FALLING)
                || mc.player.hasStatusEffect(StatusEffects.LEVITATION)
                || mc.player.hasStatusEffect(StatusEffects.BLINDNESS))) {
            // Never cancel the real hit when a status effect makes the spoofed
            // critical invalid. The normal attack must still reach the server.
            return;
        }

        // Не подменяем настоящий крит во время прыжка. Иначе перед обычным ударом
        // отправлялся ещё один фальшивый пакет движения, на который реагирует античит.
        if (!mc.player.isOnGround()
                || mc.player.fallDistance > 0.0F
                || Math.abs(mc.player.getVelocity().y) > 1.0E-4D
                || mc.player.isTouchingWater()
                || mc.player.isInLava()
                || mc.player.isClimbing()
                || mc.player.hasVehicle()
                || mc.player.getAbilities().flying) {
            return;
        }

        event.setCancelled(true);
        sendCriticalPacket();
        NetworkUtils.sendSilentPacket(packet);
    }

    /**
     * RW2 uses the packet critical from the reference implementation while
     * the player is in cobweb. It is sent from EventAttack, immediately before
     * Minecraft emits the real interaction packet.
     */
    @FastNative
    @EventTarget
    public void onAttack(EventAttack event) {
        if (!isEnabled() || !mode.is("RW2") || mc.player == null || mc.world == null) return;
        if (mc.player.isGliding() || event.getTarget() instanceof EndCrystalEntity) return;
        if (!PlayerIntersectionUtil.isPlayerInBlock(Blocks.COBWEB)) return;

        Entity auraTarget = Aura.INSTANCE.getTarget();
        if (auraTarget == null || auraTarget != event.getTarget()) return;

        float fallSpoof = randomFloat(1.0E-7F, 1.0E-6F);
        mc.player.fallDistance = fallSpoof;
        float yaw = Aura.INSTANCE.lastYaw;
        float pitch = Aura.INSTANCE.lastPitch;
        if (yaw == 0.0F && pitch == 0.0F) {
            yaw = mc.player.getYaw();
            pitch = mc.player.getPitch();
        }
        NetworkUtils.sendSilentPacket(new PlayerMoveC2SPacket.Full(
                mc.player.getX(),
                mc.player.getY() - fallSpoof,
                mc.player.getZ(),
                yaw,
                pitch,
                false,
                mc.player.horizontalCollision
        ));
        preparedCobwebAttack = true;
    }

    @FastNative
    @EventTarget
    public void onPostMotion(EventPostMotion event) {
        if (!shouldDeferAuraAttack()) return;
        Aura.INSTANCE.tryPostMotionAttack();
    }

    @FastNative
    public boolean shouldDeferAuraAttack() {
        return isEnabled() && mode.is("RW2") && mc.player != null && mc.world != null;
    }

    @FastNative
    @Override
    public void onDisable() {
        preparedCobwebAttack = false;
        super.onDisable();
    }

    @FastNative
    private static float randomFloat(float min, float max) {
        return min + ThreadLocalRandom.current().nextFloat() * (max - min);
    }

    @FastNative
    private void sendCriticalPacket() {
        double x = mc.player.getX();
        double y = mc.player.getY();
        double z = mc.player.getZ();

        if (mode.is("RW2")) {
            sendRw2CriticalPackets(x, y, z);
            return;
        }

        if (mode.is("Grim 1.17+")) {
            sendPosition(x, y + 0.0625D, z);
            sendPosition(x, y, z);
            mc.player.fallDistance = 0.0625F;
            return;
        }

        float drop = MathUtil.random(1.0E-7D, 1.0E-6D);
        mc.player.fallDistance = 0.001F;
        sendPosition(x, y - drop, z);
    }

    @FastNative
    private void sendRw2CriticalPackets(double x, double y, double z) {
        // Large enough to survive server-side floating point normalization,
        // but still below a visible movement step.
        double lift = MathUtil.random(8.0E-4D, 1.2E-3D);
        sendPosition(x, y + lift, z);
        sendPosition(x, y, z);
        mc.player.fallDistance = (float) lift;
    }

    private boolean isAttackPacket(PlayerInteractEntityC2SPacket packet) {
        boolean[] attack = {false};
        packet.handle(new PlayerInteractEntityC2SPacket.Handler() {
            @Override
            public void interact(Hand hand) {
            }

            @Override
            public void interactAt(Hand hand, Vec3d pos) {
            }

            @Override
            public void attack() {
                attack[0] = true;
            }
        });
        return attack[0];
    }

    @FastNative
    private void sendPosition(double x, double y, double z) {
        NetworkUtils.sendSilentPacket(new PlayerMoveC2SPacket.Full(
                x,
                y,
                z,
                mc.player.getYaw(),
                mc.player.getPitch(),
                false,
                false
        ));
    }
}
