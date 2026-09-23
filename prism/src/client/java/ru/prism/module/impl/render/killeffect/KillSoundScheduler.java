package ru.prism.module.impl.render.killeffect;

import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class KillSoundScheduler {

    private final List<ScheduledKillSound> pending = new ArrayList<>();

    public void schedule(SoundEvent sound, float volume, float pitch, long delayMs) {
        if (sound == null) return;
        if (delayMs <= 0L) {
            play(sound, volume, pitch);
            return;
        }
        pending.add(new ScheduledKillSound(sound, volume, pitch, System.currentTimeMillis() + delayMs));
    }

    public void tick() {
        if (pending.isEmpty()) return;

        long now = System.currentTimeMillis();
        Iterator<ScheduledKillSound> iterator = pending.iterator();
        while (iterator.hasNext()) {
            ScheduledKillSound scheduled = iterator.next();
            if (now < scheduled.playAt()) continue;
            play(scheduled.sound(), scheduled.volume(), scheduled.pitch());
            iterator.remove();
        }
    }

    private void play(SoundEvent sound, float volume, float pitch) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player != null) {
            mc.player.playSound(sound, volume, pitch);
        }
    }

    public void clear() {
        pending.clear();
    }
}
