package moscow.rockstar.mixin.minecraft.client.render;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import net.minecraft.client.render.FrameGraphBuilder;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rockstar.modules.visual.RemovalsModule;
import rockstar.client.RockstarClient;
import rockstar.client.MinecraftClientAccess;

@Mixin(value={WorldRenderer.class})
public abstract class WeatherRendererMixin
implements MinecraftClientAccess {
    @Inject(method={"renderWeather"}, at={@At(value="HEAD")})
    private void onRenderWeather(FrameGraphBuilder frameGraphBuilder, GpuBufferSlice fog, CallbackInfo callbackInfo) {
        RemovalsModule typedValue322 = RockstarClient.getInstance().getModuleManager().getModule(RemovalsModule.class);
        if (typedValue322.isEnabled() && typedValue322.internalMethod09965().isSelected() && WeatherRendererMixin.internalField0149.world != null) {
            WeatherRendererMixin.internalField0149.world.setRainGradient(0.0f);
            WeatherRendererMixin.internalField0149.world.setThunderGradient(0.0f);
        }
    }
}
