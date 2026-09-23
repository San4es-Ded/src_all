package ru.prism.module.impl.render.killeffect;

import net.minecraft.client.MinecraftClient;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;

import java.util.concurrent.ThreadLocalRandom;

public final class KillParticleBurstEmitter {

    private KillParticleBurstEmitter() {
    }

    public static void spawnBurst(Vec3d pos, int count, float spread, float upward) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.world == null || pos == null) return;

        int amount = Math.clamp((long) count, 0, 48);
        ThreadLocalRandom random = ThreadLocalRandom.current();
        float strength = spread / Math.max(count / 50.0F, 1.0F) * 1.4F;

        for (int i = 0; i < amount; i++) {
            double vx = random.nextDouble(-strength, strength);
            double vy = random.nextDouble(-strength * 0.25, strength * 0.55) + upward;
            double vz = random.nextDouble(-strength, strength);
            mc.particleManager.addParticle(ParticleTypes.END_ROD, pos.x, pos.y, pos.z, vx, vy, vz);
        }
    }
}
