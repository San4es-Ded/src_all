package moscow.rockstar.mixin.minecraft.client.render;


import rockstar.client.util.*;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rockstar.client.util.GameUtils;

@Mixin(value={RenderTickCounter.Dynamic.class})
public class DynamicMixin {
    @Shadow
    private float dynamicDeltaTicks;
    @Shadow
    private float tickProgress;
    @Shadow
    private long lastTimeMillis;
    @Final
    @Shadow
    private float tickTime;

    @Inject(at={@At(value="FIELD", target="Lnet/minecraft/client/render/RenderTickCounter$Dynamic;lastTimeMillis:J", opcode=181, ordinal=0)}, method={"beginRenderTick(J)I"}, cancellable=true)
    public void onBeginRenderTick(long l, CallbackInfoReturnable<Integer> callbackInfoReturnable) {
        if (GameUtils.internalMethod00467() == 1.0f) {
            return;
        }
        this.dynamicDeltaTicks = (float)(l - this.lastTimeMillis) / this.tickTime * GameUtils.internalMethod00467();
        this.lastTimeMillis = l;
        this.tickProgress += this.dynamicDeltaTicks;
        int n = (int)this.tickProgress;
        this.tickProgress -= (float)n;
        callbackInfoReturnable.setReturnValue(n);
    }
}
