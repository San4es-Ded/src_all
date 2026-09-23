package ru.haron.mixin;

import haron.events.KeyInputEvent;
import haron.events.MouseScrollEvent;
import haron.events.MouseButtonEvent;
import haron.events.EventDispatcher;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value={Mouse.class})
public class MouseMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method={"onCursorPos"}, at={@At(value="HEAD")})
    private void onCursorPos(long window, double x, double y, CallbackInfo ci) {
    }

    @Inject(method={"onMouseButton"}, at={@At(value="HEAD")}, cancellable=true)
    private void onMouseButton(long j, int i, int i2, int i3, CallbackInfo callbackInfo) {
        if (j == this.client.getWindow().getHandle()) {
            MouseButtonEvent mouseButtonEvent = new MouseButtonEvent(i, i2, i3, this.client.mouse.getX() * (double)this.client.getWindow().getScaledWidth() / (double)this.client.getWindow().getWidth(), this.client.mouse.getY() * (double)this.client.getWindow().getScaledHeight() / (double)this.client.getWindow().getHeight());
            EventDispatcher.EVENT_BUS.post((Object)mouseButtonEvent);
            if (mouseButtonEvent.c()) {
                callbackInfo.cancel();
            }
        }
    }

    @Inject(method={"onMouseScroll"}, at={@At(value="HEAD")}, cancellable=true)
    private void onMouseScroll(long j, double d, double d2, CallbackInfo callbackInfo) {
        if (j == this.client.getWindow().getHandle()) {
            MouseScrollEvent mouseScrollEvent = new MouseScrollEvent(d, d2);
            EventDispatcher.EVENT_BUS.post((Object)mouseScrollEvent);
            if (mouseScrollEvent.c()) {
                callbackInfo.cancel();
            }
        }
    }

    @Inject(method={"onMouseButton"}, at={@At(value="TAIL")}, locals=LocalCapture.CAPTURE_FAILSOFT)
    private void onMouseButton1(long j, int i, int i2, int i3, CallbackInfo callbackInfo) {
        if (i > 1) {
            EventDispatcher.EVENT_BUS.post((Object)new KeyInputEvent(1450 + i, 0, i2, i3));
        }
    }
}

