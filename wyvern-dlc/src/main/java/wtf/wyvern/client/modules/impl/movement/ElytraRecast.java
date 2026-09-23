package wtf.wyvern.client.modules.impl.movement;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import wtf.wyvern.Wyvern;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.core.eventbus.EventTarget;
import wtf.wyvern.core.events.impl.player.EventMoveInput;
import wtf.wyvern.utility.game.player.MovingUtil;
import wtf.wyvern.utility.game.player.PlayerIntersectionUtil;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(
    name = "ElytraRecast",
    description = "Позволяет выше прыгать на элитрах",
    category = Category.MOVEMENT
)
@FastNative
public final class ElytraRecast extends Module {
    public static final ElytraRecast INSTANCE = new ElytraRecast();
    private int groundTick;
    private boolean changed;

    private ElytraRecast() {
    }

    @EventTarget
    public void update(EventMoveInput event) {
        if (mc.player == null) return;

        if (mc.player.isUsingItem()) {
            if (Wyvern.getInstance().getServerHandler().isServerSprint()) {
                mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, Mode.STOP_SPRINTING));
                mc.player.setSprinting(false);
            }
            groundTick = 5;
        } else if (groundTick > 0) {
            groundTick--;
            return;
        }

        if (!mc.player.isUsingItem()
            && !mc.player.isTouchingWater()
            && mc.player.getEquippedStack(EquipmentSlot.CHEST).isOf(Items.ELYTRA)
            && MovingUtil.hasPlayerMovement()) {
            if (mc.player.isOnGround() && mc.player.isWalking()) {
                if (!mc.player.canSprint() || !mc.player.isWalking() || mc.player.isBlind()
                    || mc.player.isUsingItem()
                    || mc.player.shouldSlowDown() && !mc.player.isSubmergedInWater()) {
                    if (Wyvern.getInstance().getServerHandler().isServerSprint()) {
                        mc.player.lastSprinting = true;
                        mc.player.setSprinting(false);
                    }
                    mc.player.setSprinting(false);
                } else {
                    if (!mc.player.isSprinting() && Wyvern.getInstance().getServerHandler().isServerSprint()) {
                        mc.player.setSprinting(true);
                    }
                    if (!Wyvern.getInstance().getServerHandler().isServerSprint()) {
                        mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, Mode.START_SPRINTING));
                        mc.player.setSprinting(true);
                        changed = true;
                    }
                }
                mc.player.jump();
            } else if (!mc.player.isGliding()) {
                PlayerIntersectionUtil.startFallFlying();
            }
        } else if (changed && Wyvern.getInstance().getServerHandler().isServerSprint()) {
            mc.player.lastSprinting = true;
            mc.player.setSprinting(false);
            changed = false;
        }

        if (groundTick > 0) groundTick--;
    }

    @Override
    public void onDisable() {
        if (mc.player != null && Wyvern.getInstance().getServerHandler().isServerSprint() && changed) {
            mc.player.lastSprinting = true;
            mc.player.setSprinting(false);
        }
        changed = false;
        groundTick = 0;
        super.onDisable();
    }
}
