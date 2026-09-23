package moscow.rockstar.mixin.minecraft.client.gui.screen;


import rockstar.client.util.*;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import java.util.Collection;
import net.minecraft.client.gui.screen.ingame.StatusEffectsDisplay;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import rockstar.client.util.GameUtils;

@Mixin(value={StatusEffectsDisplay.class})
public class StatusEffectsDisplayMixin {
    @ModifyExpressionValue(
        method="render",
        at=@At(
            value="INVOKE",
            target="Lnet/minecraft/client/network/ClientPlayerEntity;getStatusEffects()Ljava/util/Collection;"
        )
    )
    private Collection<StatusEffectInstance> rockstar$filterStatusEffects(Collection<StatusEffectInstance> collection) {
        return this.filterCollection(collection);
    }

    @Unique
    private Collection<StatusEffectInstance> filterCollection(Collection<StatusEffectInstance> collection) {
        return collection.stream().filter(statusEffectInstance -> !GameUtils.internalMethod06278(statusEffectInstance)).toList();
    }
}
