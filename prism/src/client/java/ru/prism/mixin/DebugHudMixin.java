package ru.prism.mixin;

import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.prism.module.impl.utils.StreamerMode;

import java.util.Collection;

@Mixin(targets = "net.minecraft.client.gui.hud.DebugHud$1")
public class DebugHudMixin {

    @Inject(method = "addLine", at = @At("HEAD"), cancellable = true)
    private void prism$hideLine(String line, CallbackInfo ci) {
        if (prism$hideDebug() && prism$sensitiveLine(line)) {
            ci.cancel();
        }
    }

    @Inject(method = "addPriorityLine", at = @At("HEAD"), cancellable = true)
    private void prism$hidePriorityLine(String line, CallbackInfo ci) {
        if (prism$hideDebug() && prism$sensitiveLine(line)) {
            ci.cancel();
        }
    }

    @Inject(method = "addLinesToSection", at = @At("HEAD"), cancellable = true)
    private void prism$hideSection(Identifier section, Collection<String> lines, CallbackInfo ci) {
        if (prism$hideDebug() && prism$sensitiveSection(section)) {
            ci.cancel();
        }
    }

    @Inject(method = "addLineToSection", at = @At("HEAD"), cancellable = true)
    private void prism$hideSectionLine(Identifier section, String line, CallbackInfo ci) {
        if (prism$hideDebug() && prism$sensitiveSection(section)) {
            ci.cancel();
        }
    }

    private boolean prism$hideDebug() {
        StreamerMode streamerMode = StreamerMode.get();
        return streamerMode != null && streamerMode.isEnabled() && streamerMode.hideDebug.getValue();
    }

    private boolean prism$sensitiveLine(String line) {
        if (line == null) return false;
        return line.contains("XYZ:")
                || line.contains("Block:")
                || line.contains("Chunk:")
                || line.contains("Facing:")
                || line.contains("Section-relative:")
                || line.contains("Targeted Block")
                || line.contains("Targeted Fluid")
                || line.contains("Targeted Entity");
    }

    private boolean prism$sensitiveSection(Identifier section) {
        if (section == null) return false;
        String path = section.getPath();
        return path.equals("position")
                || path.equals("looking_at_block")
                || path.equals("looking_at_fluid")
                || path.equals("looking_at_entity");
    }
}
