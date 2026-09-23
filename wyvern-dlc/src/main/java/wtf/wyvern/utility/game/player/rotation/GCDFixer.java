package wtf.wyvern.utility.game.player.rotation;

import wtf.wyvern.utility.interfaces.IMinecraft;
import wtf.astroguard.J2C.FastNative;

/**
 * Mouse-sensitivity quantisation used by the original HolyWorld rotation.
 */
@FastNative
public final class GCDFixer implements IMinecraft {
    private GCDFixer() {
    }

    public static float getFixRotate(float rotation) {
        return getDeltaMouse(rotation) * getGCDValue();
    }

    public static float getGCDValue() {
        return (float) (getGCD() * 0.15D);
    }

    public static float getGCD() {
        double sensitivity = (Double) mc.options.getMouseSensitivity().getValue();
        double scaledSensitivity = sensitivity / 0.15D / 8.0D;
        double cubicRoot = Math.cbrt(scaledSensitivity);
        float mouseFactor = (float) ((cubicRoot - 0.2F) / 0.6F * 0.6D + 0.2D);
        return mouseFactor * mouseFactor * mouseFactor * 8.0F;
    }

    public static float getDeltaMouse(float delta) {
        return Math.round(delta / getGCDValue());
    }
}
