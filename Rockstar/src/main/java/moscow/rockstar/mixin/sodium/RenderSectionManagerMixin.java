package moscow.rockstar.mixin.sodium;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import rockstar.modules.visual.CustomFogModule;
import rockstar.client.RockstarClient;
import rockstar.client.MinecraftClientAccess;

@Pseudo
@Mixin(targets={"net.caffeinemc.mods.sodium.client.render.chunk.RenderSectionManager"}, remap=false)
public class RenderSectionManagerMixin
implements MinecraftClientAccess {
    @ModifyReturnValue(method={"getEffectiveRenderDistance(Lnet/minecraft/client/render/Fog;)F"}, at={@At(value="RETURN")}, require=0)
    private float keepRenderDistance(float f) {
        if (RenderSectionManagerMixin.internalField0149.options == null || RenderSectionManagerMixin.internalField0149.gameRenderer == null) {
            return f;
        }
        CustomFogModule typedValue319 = RockstarClient.getInstance().getModuleManager().getModule(CustomFogModule.class);
        if (typedValue319 == null || !typedValue319.internalMethod00459(RenderSectionManagerMixin.internalField0149.gameRenderer.getCamera())) {
            return f;
        }
        return Math.max(f, (float)RenderSectionManagerMixin.internalField0149.options.getClampedViewDistance() * 16.0f);
    }
}
