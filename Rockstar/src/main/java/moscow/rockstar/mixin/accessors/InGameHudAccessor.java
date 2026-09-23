package moscow.rockstar.mixin.accessors;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={InGameHud.class})
public interface InGameHudAccessor {
    @Invoker(value="renderOverlay")
    public void rockstar$renderOverlay(DrawContext localValue1, Identifier localValue2, float localValue3);
}

