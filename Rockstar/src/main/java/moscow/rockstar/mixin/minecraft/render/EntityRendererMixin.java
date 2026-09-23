package moscow.rockstar.mixin.minecraft.render;


import rockstar.client.internal.game.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rockstar.client.internal.game.EntityRenderStateAccessor;

@Mixin(value={EntityRenderer.class})
public abstract class EntityRendererMixin<T extends Entity, S extends EntityRenderState> {
    @Inject(method={"updateRenderState"}, at={@At(value="HEAD")})
    private void updateRenderingEntity(T t, S s, float f, CallbackInfo callbackInfo) {
        ((EntityRenderStateAccessor)s).rockstar$setEntity((Entity)t);
    }
}

