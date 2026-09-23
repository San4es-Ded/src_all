package ru.prism.module.impl.render.killeffect;

import net.minecraft.sound.SoundEvent;

public record ScheduledKillSound(SoundEvent sound, float volume, float pitch, long playAt) {
}
