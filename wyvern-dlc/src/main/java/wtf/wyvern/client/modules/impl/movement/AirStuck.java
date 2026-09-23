package wtf.wyvern.client.modules.impl.movement;

import wtf.wyvern.core.eventbus.EventTarget;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;
import wtf.wyvern.core.events.impl.other.EventGameUpdate;
import wtf.wyvern.core.events.impl.other.EventTick;
import wtf.wyvern.core.events.impl.player.EventAttack;
import wtf.wyvern.core.events.impl.render.EventRender3D;
import wtf.wyvern.core.events.impl.server.EventPacket;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;

import static wtf.wyvern.utility.interfaces.IMinecraft.mc;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(
        name = "AirStuck",
        category = Category.MOVEMENT,
        description = "Зависает в воздухе"
)
@FastNative
public class AirStuck extends Module {
    public static final AirStuck INSTANCE = new AirStuck();

    public boolean frozen;
    private Vec3d frozenPos;
    private boolean sending;
    private Vec3d velocityBeforeFreeze = Vec3d.ZERO;
    private static volatile long releaseGraceUntil;

    private AirStuck() {
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.frozen = false;
        this.frozenPos = null;
        this.sending = false;
        this.velocityBeforeFreeze = Vec3d.ZERO;
    }

    @Override
    public void onDisable() {
        super.onDisable();

        if (mc.player != null && this.frozenPos != null) {
            mc.player.setVelocity(this.velocityBeforeFreeze);
            releaseGraceUntil = System.currentTimeMillis() + 350L;
        }

        this.frozenPos = null;
        this.frozen = false;
    }

    @EventTarget
    public void onTick(EventTick event) {
        if (mc.player == null) return;
        if (this.frozen && this.frozenPos != null) {
            mc.player.setPosition(this.frozenPos.x, this.frozenPos.y, this.frozenPos.z);
            mc.player.setVelocity(Vec3d.ZERO);
            mc.player.fallDistance = 0.0F;
        }
    }

    @EventTarget
    public void onGameUpdate(EventGameUpdate event) {
        if (mc.player == null || mc.world == null || this.frozen) return;

        if (mc.player.isOnGround()
                || mc.player.isGliding()
                || mc.player.isTouchingWater()
                || mc.player.hasVehicle()
                || mc.player.getAbilities().flying
                || mc.player.getVelocity().y >= -0.08D
                || mc.player.fallDistance < 0.5F) return;

        this.frozenPos = mc.player.getPos();
        this.velocityBeforeFreeze = mc.player.getVelocity();
        this.frozen = true;
    }

    @EventTarget
    public void onPacket(EventPacket event) {
        if (mc.player == null || !this.frozen || this.frozenPos == null) return;
        if (!event.isSent() || this.sending) return;

        if (event.getPacket() instanceof PlayerMoveC2SPacket movePacket) {
            // Keep look packets flowing while frozen. Cancelling them makes the
            // server use a stale yaw and rejects the following attack packet.
            if (movePacket instanceof PlayerMoveC2SPacket.PositionAndOnGround
                    || movePacket instanceof PlayerMoveC2SPacket.Full) {
                event.cancel();
            }
        }
    }

    @EventTarget
    public void onAttack(EventAttack event) {
        if (!this.frozen || mc.player == null || this.frozenPos == null || mc.getNetworkHandler() == null) return;

        this.sending = true;
        // A look-only packet is enough to refresh server-side reach/rotation and
        // avoids a second position correction when the client attacks in place.
        mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(
                mc.player.getYaw(), mc.player.getPitch(), false, mc.player.horizontalCollision));
        this.sending = false;
    }

    public static boolean isReleaseGraceActive() {
        return System.currentTimeMillis() < releaseGraceUntil;
    }

    @EventTarget
    public void onRender3D(EventRender3D event) {
        if (mc.player == null || !this.frozen) return;
        mc.player.setVelocity(Vec3d.ZERO);
    }

}
