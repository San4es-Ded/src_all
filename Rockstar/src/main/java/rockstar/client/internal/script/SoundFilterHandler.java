package rockstar.client.internal.script;


import rockstar.client.*;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundEvents;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pyrock.events.game.SoundEvent;
import rockstar.modules.visual.RemovalsModule;
import rockstar.client.RockstarClient;

public class SoundFilterHandler {
    public static void internalMethod01687(SoundInstance soundInstance, CallbackInfo callbackInfo) {
        String string;
        RemovalsModule typedValue322 = RockstarClient.getInstance().getModuleManager().getModule(RemovalsModule.class);
        if (typedValue322.isEnabled() && typedValue322.internalMethod10128().isSelected() && (soundInstance.getId().equals((Object)SoundEvents.BLOCK_BEACON_ACTIVATE.id()) || soundInstance.getId().equals((Object)SoundEvents.BLOCK_BEACON_AMBIENT.id()) || soundInstance.getId().equals((Object)SoundEvents.BLOCK_BEACON_POWER_SELECT.id()) || soundInstance.getId().equals((Object)SoundEvents.BLOCK_BEACON_DEACTIVATE.id()))) {
            callbackInfo.cancel();
        }
        if (typedValue322.isEnabled() && typedValue322.internalMethod10103().isSelected() && (soundInstance.getId().equals((Object)SoundEvents.WEATHER_RAIN.id()) || soundInstance.getId().equals((Object)SoundEvents.WEATHER_RAIN_ABOVE.id()) || soundInstance.getId().equals((Object)SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER.id()))) {
            callbackInfo.cancel();
        }
        if (typedValue322.isEnabled() && typedValue322.internalMethod10129().isSelected() && (soundInstance.getId().equals((Object)SoundEvents.ENTITY_PARROT_IMITATE_PHANTOM.id()) || soundInstance.getId().equals((Object)SoundEvents.ENTITY_PHANTOM_AMBIENT.id()) || soundInstance.getId().equals((Object)SoundEvents.ENTITY_PHANTOM_BITE.id()) || soundInstance.getId().equals((Object)SoundEvents.ENTITY_PHANTOM_FLAP.id()) || soundInstance.getId().equals((Object)SoundEvents.ENTITY_PHANTOM_DEATH.id()) || soundInstance.getId().equals((Object)SoundEvents.ENTITY_PHANTOM_HURT.id()) || soundInstance.getId().equals((Object)SoundEvents.ENTITY_PHANTOM_SWOOP.id()))) {
            callbackInfo.cancel();
        }
        if (typedValue322.isEnabled() && typedValue322.internalMethod10104().isSelected() && (string = soundInstance.getId().getPath()).contains("water")) {
            callbackInfo.cancel();
        }
        if (typedValue322.isEnabled() && typedValue322.internalMethod10036().isSelected() && (string = soundInstance.getId().getPath()).contains("lava")) {
            callbackInfo.cancel();
        }
        RockstarClient.getInstance().internalMethod03317().internalMethod06883(new SoundEvent(soundInstance));
    }
}
