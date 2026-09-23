package moscow.rockstar.mixin.minecraft.client.input;

import moscow.rockstar.mixin.minecraft.client.input.InputAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.math.Vec2f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pyrock.events.player.InputEvent;
import rockstar.client.RockstarClient;

@Environment(value=EnvType.CLIENT)
@Mixin(value={KeyboardInput.class})
public abstract class KeyboardInputMixin {
    @Inject(method={"tick"}, at={@At(value="TAIL")})
    private void onTick(CallbackInfo callbackInfo) {
        Input input = (Input)(Object)this;
        InputAccessor inputAccessor = (InputAccessor)(Object)input;
        PlayerInput playerInput = inputAccessor.getInput();
        Vec2f movement = inputAccessor.getMovementVector();
        float f = movement.y;
        float f2 = movement.x;
        boolean bl = inputAccessor.getInput().jump();
        boolean bl2 = inputAccessor.getInput().sneak();
        boolean bl3 = inputAccessor.getInput().sprint();
        InputEvent inputEvent = new InputEvent(f, f2, bl, bl2, bl3);
        RockstarClient.getInstance().internalMethod03317().internalMethod06883(inputEvent);
        inputAccessor.setMovementVector(new Vec2f(inputEvent.getStrafe(), inputEvent.getForward()));
        boolean bl4 = inputEvent.getForward() > 0.0f;
        boolean bl5 = inputEvent.getForward() < 0.0f;
        boolean bl6 = inputEvent.getStrafe() > 0.0f;
        boolean bl7 = inputEvent.getStrafe() < 0.0f;
        inputAccessor.setInput(new PlayerInput(bl4, bl5, bl6, bl7, inputEvent.isJump(), inputEvent.isSneak(), inputEvent.isSprint()));
    }
}
