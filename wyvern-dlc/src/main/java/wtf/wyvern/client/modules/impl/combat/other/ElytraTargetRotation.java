package wtf.wyvern.client.modules.impl.combat.other;

import wtf.astroguard.J2C.FastNative;
import wtf.wyvern.client.modules.impl.combat.Aura;
import wtf.wyvern.utility.game.player.rotation.Rotation;
import wtf.wyvern.utility.interfaces.IMinecraft;

/**
 * Elytra aiming entry-point: always uses the rotation mode selected in Aura
 * (ReallyWorld, Shard, HolyWorld, ...), never a separate snap path.
 */
@FastNative
public class ElytraTargetRotation implements IMinecraft {

    public static void rotation(Rotation angle) {
        if (mc.player == null || angle == null || !mc.player.isGliding()) {
            return;
        }
        Aura.INSTANCE.applyElytraModeRotation(angle);
    }
}
