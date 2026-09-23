package moscow.rockstar.mixin.minecraft.client.network;


import rockstar.client.internal.core.*;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.SkinTextures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rockstar.client.internal.core.SkinOverrideStore;

@Mixin(value={PlayerListEntry.class})
public class PlayerListEntryMixin {
    @Inject(method={"getSkinTextures"}, at={@At(value="RETURN")}, cancellable=true)
    private void rockstar$swapSkin(CallbackInfoReturnable<SkinTextures> callbackInfoReturnable) {
        if (SkinOverrideStore.internalMethod00189()) {
            return;
        }
        PlayerListEntry playerListEntry = (PlayerListEntry)(Object)this;
        SkinTextures skinTextures = SkinOverrideStore.internalMethod04550(playerListEntry.getProfile().name(), (SkinTextures)callbackInfoReturnable.getReturnValue());
        if (skinTextures != callbackInfoReturnable.getReturnValue()) {
            callbackInfoReturnable.setReturnValue(skinTextures);
        }
    }
}

