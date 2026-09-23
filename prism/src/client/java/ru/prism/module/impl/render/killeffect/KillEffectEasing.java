package ru.prism.module.impl.render.killeffect;

import net.minecraft.util.math.MathHelper;

public final class KillEffectEasing {

    private KillEffectEasing() {
    }

    public static float smooth(float t) {
        t = MathHelper.clamp(t, 0F, 1F);
        return t * t * (3F - 2F * t);
    }
}
