package wtf.wyvern.client.modules.impl.movement;

import wtf.wyvern.core.eventbus.EventTarget;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import wtf.wyvern.core.events.impl.server.EventPacket;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.ModeSetting;
import wtf.wyvern.client.modules.api.setting.impl.SliderSetting;

@ModuleAnnotation(
        name = "Velocity",
        category = Category.COMBAT,
        description = "Редактор получаемого отбрасывания"
)
public class Velocity extends Module {
    public static final Velocity INSTANCE = new Velocity();

    private final ModeSetting mode = new ModeSetting("Mode", "Отмена", "Настраиваемый");
    private final SliderSetting horizontal = new SliderSetting("Горизонталь", 0, 0, 100, 1, () -> mode.is("Настраиваемый"));
    private final SliderSetting vertical = new SliderSetting("Вертикаль", 0, 0, 100, 1, () -> mode.is("Настраиваемый"));

    private Velocity() {
    }

    @EventTarget
    private void onPacket(EventPacket event) {
        if (!event.isReceive() || mc.player == null || mc.world == null) return;

        if (event.getPacket() instanceof EntityVelocityUpdateS2CPacket packet) {
            if (packet.getEntityId() != mc.player.getId()) return;

            // AirStuck deliberately freezes outgoing movement. When it is
            // disabled the first server correction must be accepted, otherwise
            // cancelling it leaves the client/server positions divergent and
            // the next tick is flagged by Matrix/Grim.
            if (AirStuck.isReleaseGraceActive()) {
                return;
            }

            if (mode.is("Отмена")) {
                event.setCancelled(true);
                return;
            }

            if (mode.is("Настраиваемый")) {
                event.setCancelled(true);
                double vx = packet.getVelocityX() * (horizontal.getCurrent() / 100.0);
                double vy = packet.getVelocityY() * (vertical.getCurrent() / 100.0);
                double vz = packet.getVelocityZ() * (horizontal.getCurrent() / 100.0);
                mc.player.setVelocity(vx, vy, vz);
            }
        }

        if (event.getPacket() instanceof net.minecraft.network.packet.s2c.play.ExplosionS2CPacket packet) {
            if (AirStuck.isReleaseGraceActive()) {
                return;
            }
            if (mode.is("Отмена")) {
                event.setCancelled(true);
                return;
            }

            if (mode.is("Настраиваемый")) {
                double h = horizontal.getCurrent() / 100.0;
                double v = vertical.getCurrent() / 100.0;
                packet.playerKnockback().ifPresent(kb -> {
                    event.setCancelled(true);
                    mc.player.setVelocity(kb.x * h, kb.y * v, kb.z * h);
                });
            }
        }
    }
}
