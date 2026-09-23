package haron.util.sound;

import net.minecraft.client.sound.Sound;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.sound.WeightedSoundSet;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;

public class k9fqgt
implements SoundInstance {
    private final SoundInstance c;
    private final float d;
    public static int a;

    public float getPitch() {
        return this.c.getPitch();
    }

    public float getVolume() {
        return this.c.getVolume() * this.d;
    }

    public double getZ() {
        return this.c.getZ();
    }

    public Sound getSound() {
        return this.c.getSound();
    }

    public int getRepeatDelay() {
        int n = 203;
        return this.c.getRepeatDelay();
    }

    public boolean isRepeatable() {
        return this.c.isRepeatable();
    }

    public SoundCategory getCategory() {
        return this.c.getCategory();
    }

    public boolean isRelative() {
        return this.c.isRelative();
    }

    public boolean canPlay() {
        return this.c.canPlay();
    }

    public double getY() {
        return this.c.getY();
    }

    public double getX() {
        return this.c.getX();
    }

    public boolean shouldAlwaysPlay() {
        return this.c.shouldAlwaysPlay();
    }

    public SoundInstance.AttenuationType getAttenuationType() {
        return this.c.getAttenuationType();
    }

    public WeightedSoundSet getSoundSet(SoundManager soundManager) {
        return this.c.getSoundSet(soundManager);
    }

    public Identifier getId() {
        return this.c.getId();
    }

    public k9fqgt(SoundInstance soundInstance, float f) {
        this.c = soundInstance;
        this.d = f;
    }

}

