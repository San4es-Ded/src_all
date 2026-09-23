package moscow.rockstar.mixin.minecraft.client;


import rockstar.client.ui.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rockstar.modules.visual.AmbienceModule;
import rockstar.client.RockstarClient;
import rockstar.client.ui.ThemeColors;

@Mixin(value={ClientWorld.class})
public class ClientWorldMixin {
    @Inject(method={"getSkyColor"}, at={@At(value="HEAD")}, cancellable=true)
    private void onGetSkyColor(Vec3d vec3d, float f, CallbackInfoReturnable<Integer> callbackInfoReturnable) {
        AmbienceModule typedValue317 = RockstarClient.getInstance().getModuleManager().getModule(AmbienceModule.class);
        if (typedValue317.isEnabled() && typedValue317.internalMethod06409().isSelected()) {
            callbackInfoReturnable.setReturnValue((typedValue317.internalMethod00796().internalMethod04496() ? ThemeColors.internalMethod02531().getRGB() : typedValue317.internalMethod00850().internalMethod05620().getRGB()));
        }
    }

    @Inject(method={"getCloudsColor"}, at={@At(value="HEAD")}, cancellable=true)
    private void onGetCloudsColor(float f, CallbackInfoReturnable<Integer> callbackInfoReturnable) {
        AmbienceModule typedValue317 = RockstarClient.getInstance().getModuleManager().getModule(AmbienceModule.class);
        if (typedValue317.isEnabled() && typedValue317.internalMethod06628().isSelected()) {
            callbackInfoReturnable.setReturnValue((typedValue317.internalMethod00796().internalMethod04496() ? ThemeColors.internalMethod02531().getRGB() : typedValue317.internalMethod01575().internalMethod05620().getRGB()));
        }
    }
}
