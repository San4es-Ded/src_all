package ru.haron.mixin;

import haron.module.ModuleManager;
import haron.modules.utilities.StreamerMode;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.client.gui.hud.DebugHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={DebugHud.class})
public class DebugHudMixin {
    @Inject(method={"getLeftText"}, at={@At(value="RETURN")}, cancellable=true)
    private void hideLeftCoordinates(CallbackInfoReturnable<List<String>> callbackInfoReturnable) {
        StreamerMode streamerMode = ModuleManager.STREAMER_MODE;
        if (streamerMode.k() && streamerMode.hideDebugInfo.a()) {
            callbackInfoReturnable.setReturnValue(callbackInfoReturnable.getReturnValue().stream().filter(str -> {
                return !str.contains("XYZ:") && !str.contains("Block:") && !str.contains("Chunk:") && !str.contains("Facing:");
            }).collect(Collectors.toList()));
        }
    }

    @Inject(method={"getRightText"}, at={@At(value="RETURN")}, cancellable=true)
    private void hideRightCoordinates(CallbackInfoReturnable<List<String>> callbackInfoReturnable) {
        StreamerMode streamerMode = ModuleManager.STREAMER_MODE;
        if (streamerMode.k() && streamerMode.hideDebugInfo.a()) {
            callbackInfoReturnable.setReturnValue(callbackInfoReturnable.getReturnValue().stream().filter(str -> {
                return !str.contains("Targeted Block:") && !str.contains("Targeted Fluid:") && !str.contains("Targeted Entity");
            }).collect(Collectors.toList()));
        }
    }
}
