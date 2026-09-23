package moscow.rockstar.mixin.minecraft.client;





import rockstar.client.render.*;
import rockstar.client.animation.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.core.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rockstar.client.render.Fonts;
import rockstar.client.internal.script.RollingDigitLabel;
import rockstar.client.animation.AnimatedValue;
import rockstar.client.animation.Easing;
import rockstar.client.internal.core.PotionEffectAnimationAccess;

@Mixin(value={StatusEffectInstance.class})
public class StatusEffectInstanceMixin
implements PotionEffectAnimationAccess {
    @Unique
    private AnimatedValue potionStatusAnimation;
    @Unique
    private RollingDigitLabel timeAnimation;

    @Inject(method={"<init>(Lnet/minecraft/registry/entry/RegistryEntry;IIZZZLnet/minecraft/entity/effect/StatusEffectInstance;)V"}, at={@At(value="TAIL")})
    public void onInit(RegistryEntry<?> registryEntry, int n, int n2, boolean bl, boolean bl2, boolean bl3, StatusEffectInstance statusEffectInstance, CallbackInfo callbackInfo) {
        if (MinecraftClient.getInstance() == null || MinecraftClient.getInstance().player == null) {
            return;
        }
        this.timeAnimation = new RollingDigitLabel(Fonts.internalField1154.internalMethod01432(7.0f), 3.0f, 300L, Easing.internalField1626);
    }

    @Override
    public AnimatedValue rockstar$getAnimPotion() {
        if (this.potionStatusAnimation == null) {
            this.potionStatusAnimation = new AnimatedValue(300L, 0.0f, Easing.internalField1626);
        }
        return this.potionStatusAnimation;
    }

    @Override
    public RollingDigitLabel rockstar$getTimeAnimation() {
        return this.timeAnimation;
    }
}

