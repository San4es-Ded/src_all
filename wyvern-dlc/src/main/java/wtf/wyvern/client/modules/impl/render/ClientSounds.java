package wtf.wyvern.client.modules.impl.render;

import wtf.wyvern.core.eventbus.EventTarget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import wtf.wyvern.core.events.impl.other.EventModuleToggle;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.SliderSetting;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(
        name = "ClientSounds",
        category = Category.RENDER,
        description = "Звуки при включении/выключении модулей"
)
@FastNative
public class ClientSounds extends Module {
    public static final ClientSounds INSTANCE = new ClientSounds();
    private static final SoundEvent ENABLE_SOUND = SoundEvent.of(Identifier.of("wyvern", "enable"));
    private static final SoundEvent DISABLE_SOUND = SoundEvent.of(Identifier.of("wyvern", "disable"));

    private final SliderSetting volume = new SliderSetting("Громкость", 1F, 0.1F, 2.0F, 0.1F);
    private final SliderSetting pitch = new SliderSetting("Высота звука", 1.0F, 0.5F, 2.0F, 0.1F);

    @EventTarget
    private void onToggle(EventModuleToggle event) {
        if (mc.player == null || mc.world == null) return;
        if (event.getModule() == this) return;

        SoundEvent sound = event.isEnabled() ? ENABLE_SOUND : DISABLE_SOUND;
        mc.getSoundManager().play(
                PositionedSoundInstance.master(sound, pitch.getCurrent(), volume.getCurrent())
        );
    }
}
