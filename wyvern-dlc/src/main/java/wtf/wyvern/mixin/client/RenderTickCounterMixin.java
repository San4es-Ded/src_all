package wtf.wyvern.mixin.client;

import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import wtf.wyvern.utility.game.client.TimerService;

/**
 * Перехватывает вычисление {@code targetMillisPerTick.apply(tickTime)} внутри
 * {@link RenderTickCounter.Dynamic#beginRenderTick(long)} и масштабирует его
 * на множитель из {@link TimerService}.
 * <p>
 * В 1.21.4 ванильная логика делит прошедшее время на millis-per-tick, поэтому
 * чтобы ускорить игру — millis-per-tick нужно уменьшить (разделить на множитель).
 */
@Mixin(RenderTickCounter.Dynamic.class)
public abstract class RenderTickCounterMixin {

    @ModifyExpressionValue(
            method = "beginRenderTick(J)I",
            at = @At(
                    value = "INVOKE",
                    target = "Lit/unimi/dsi/fastutil/floats/FloatUnaryOperator;apply(F)F"
            )
    )
    private float wyvern$scaleMillisPerTick(float original) {
        return TimerService.applyMultiplier(original);
    }
}
