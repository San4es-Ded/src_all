package rockstar.client.internal.media;


import rockstar.client.*;
import lombok.Generated;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import rockstar.client.RockstarClient;

public class ModSoundInstance
extends PositionedSoundInstance {
    private final String internalField0248;

    public ModSoundInstance(String string, float f) {
        super(Identifier.of((String)(RockstarClient.internalField1077 + ":" + string)), SoundCategory.MASTER, f, 1.0f, SoundInstance.createRandom(), false, 0, SoundInstance.AttenuationType.NONE, 0.0, 0.0, 0.0, true);
        this.internalField0248 = string;
    }

    public ModSoundInstance(String string, float f, float f2) {
        super(Identifier.of((String)(RockstarClient.internalField1077 + ":" + string)), SoundCategory.MASTER, f, f2, SoundInstance.createRandom(), false, 0, SoundInstance.AttenuationType.NONE, 0.0, 0.0, 0.0, true);
        this.internalField0248 = string;
    }

    public void internalMethod03864(float f) {
        MinecraftClient.getInstance().getSoundManager().play((SoundInstance)new ModSoundInstance(this.internalField0248, f));
    }

    public void internalMethod03132(float f, float f2) {
        MinecraftClient.getInstance().getSoundManager().play((SoundInstance)new ModSoundInstance(this.internalField0248, f, f2));
    }

    @Generated
    public String internalMethod03064() {
        return this.internalField0248;
    }
}

