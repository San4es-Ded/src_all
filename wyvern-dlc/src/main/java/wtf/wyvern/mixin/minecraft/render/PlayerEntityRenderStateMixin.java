package wtf.wyvern.mixin.minecraft.render;

import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import wtf.wyvern.utility.interfaces.ILocalPlayerRenderState;

@Mixin(PlayerEntityRenderState.class)
public abstract class PlayerEntityRenderStateMixin implements ILocalPlayerRenderState {
    @Unique
    private boolean wyvern$localPlayer;

    @Override
    public boolean wyvern$isLocalPlayer() {
        return wyvern$localPlayer;
    }

    @Override
    public void wyvern$setLocalPlayer(boolean localPlayer) {
        wyvern$localPlayer = localPlayer;
    }
}
