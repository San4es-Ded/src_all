package moscow.rockstar.mixin.minecraft.client;


import rockstar.client.core.*;
import net.minecraft.client.Mouse;
import net.minecraft.client.input.MouseInput;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pyrock.events.window.MouseButtonEvent;
import pyrock.events.window.MouseEvent;
import pyrock.events.window.MouseMoveEvent;
import pyrock.events.window.MouseScrollEvent;
import pyrock.events.window.ScrollEvent;
import rockstar.client.RockstarClient;
import rockstar.client.core.CursorType;
import rockstar.client.core.CursorManager;
import rockstar.client.MinecraftClientAccess;

@Mixin(value={Mouse.class})
public class MouseMixin
implements MinecraftClientAccess {
    @Unique
    private double rockstar$lastCursorX;
    @Unique
    private double rockstar$lastCursorY;

    @Inject(method={"tick()V"}, at={@At(value="RETURN")})
    private void tick(CallbackInfo callbackInfo) {
        if (CursorManager.internalMethod02850() != CursorManager.internalMethod03537()) {
            GLFW.glfwSetCursor((long)internalField0149.getWindow().getHandle(), (long)CursorManager.internalMethod02850().internalMethod04926());
        }
        CursorManager.internalMethod03769(CursorManager.internalMethod02850());
        CursorManager.internalMethod06882(CursorType.internalField0566);
    }

    @Inject(method={"onMouseButton"}, at={@At(value="HEAD")}, cancellable=true)
    private void onMouseButton(long l, MouseInput input, int action, CallbackInfo callbackInfo) {
        int button = input.button();
        int modifiers = input.modifiers();
        if (action == 1) {
            if (GLFW.glfwGetKey((long)l, (int)292) == 1) {
                return;
            }
            RockstarClient.getInstance().internalMethod03317().internalMethod06883(new MouseEvent(button, action));
        }
        MouseButtonEvent mouseButtonEvent = new MouseButtonEvent(button, action, modifiers);
        RockstarClient.getInstance().internalMethod03317().internalMethod06883(mouseButtonEvent);
        if (mouseButtonEvent.isCancelled()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"onMouseScroll"}, at={@At(value="HEAD")}, cancellable=true)
    private void onMouseScroll(long l, double d, double d2, CallbackInfo callbackInfo) {
        if (d2 != 0.0) {
            RockstarClient.getInstance().internalMethod03317().internalMethod06883(new MouseScrollEvent(d2));
        }
        ScrollEvent scrollEvent = new ScrollEvent(d, d2);
        RockstarClient.getInstance().internalMethod03317().internalMethod06883(scrollEvent);
        if (scrollEvent.isCancelled()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"onCursorPos"}, at={@At(value="HEAD")})
    private void onCursorPos(long l, double d, double d2, CallbackInfo callbackInfo) {
        if (l != internalField0149.getWindow().getHandle()) {
            return;
        }
        double d3 = d - this.rockstar$lastCursorX;
        double d4 = d2 - this.rockstar$lastCursorY;
        this.rockstar$lastCursorX = d;
        this.rockstar$lastCursorY = d2;
        RockstarClient.getInstance().internalMethod03317().internalMethod06883(new MouseMoveEvent(d, d2, d3, d4));
    }
}
