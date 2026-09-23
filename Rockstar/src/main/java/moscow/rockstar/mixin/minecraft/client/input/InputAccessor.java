package moscow.rockstar.mixin.minecraft.client.input;

import net.minecraft.client.input.Input;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.math.Vec2f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={Input.class})
public interface InputAccessor {
    @Accessor(value="movementVector")
    Vec2f getMovementVector();

    @Accessor(value="movementVector")
    void setMovementVector(Vec2f value);

    @Accessor(value="playerInput")
    public PlayerInput getInput();

    @Accessor(value="playerInput")
    public void setInput(PlayerInput localValue1);
}
