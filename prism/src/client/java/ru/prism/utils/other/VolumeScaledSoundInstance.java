package ru.prism.utils.other;

import net.minecraft.client.sound.Sound;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundInstance.AttenuationType;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.sound.WeightedSoundSet;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;

public class VolumeScaledSoundInstance implements SoundInstance {
    private final SoundInstance wrapped;
    private final float factor;

    public VolumeScaledSoundInstance(SoundInstance wrapped, float factor) {
        this.wrapped = wrapped;
        this.factor = factor;
    }

    @Override
    public Identifier getId() {
        return this.wrapped.getId();
    }

    @Override
    public WeightedSoundSet getSoundSet(SoundManager soundManager) {
        return this.wrapped.getSoundSet(soundManager);
    }

    @Override
    public Sound getSound() {
        return this.wrapped.getSound();
    }

    @Override
    public SoundCategory getCategory() {
        return this.wrapped.getCategory();
    }

    @Override
    public boolean isRepeatable() {
        return this.wrapped.isRepeatable();
    }

    @Override
    public boolean isRelative() {
        return this.wrapped.isRelative();
    }

    @Override
    public int getRepeatDelay() {
        return this.wrapped.getRepeatDelay();
    }

    @Override
    public float getVolume() {
        return this.wrapped.getVolume() * this.factor;
    }

    @Override
    public float getPitch() {
        return this.wrapped.getPitch();
    }

    @Override
    public double getX() {
        return this.wrapped.getX();
    }

    @Override
    public double getY() {
        return this.wrapped.getY();
    }

    @Override
    public double getZ() {
        return this.wrapped.getZ();
    }

    @Override
    public AttenuationType getAttenuationType() {
        return this.wrapped.getAttenuationType();
    }

    @Override
    public boolean shouldAlwaysPlay() {
        return this.wrapped.shouldAlwaysPlay();
    }

    @Override
    public boolean canPlay() {
        return this.wrapped.canPlay();
    }
}
