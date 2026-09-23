package moscow.rockstar.mixin.minecraft.client.gui.screen;


import rockstar.client.render.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.ClickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rockstar.client.render.PostProcessRenderer;
import rockstar.client.RockstarClient;
import rockstar.client.MinecraftClientAccess;

@Mixin(value={Screen.class})
public class ScreenMixin
implements MinecraftClientAccess {
    @Inject(method={"render"}, at={@At(value="HEAD")})
    private void rockstar$markScreenRender(CallbackInfo callbackInfo) {
        PostProcessRenderer.internalMethod01673(true);
    }

    @Inject(method={"render"}, at={@At(value="RETURN")})
    private void rockstar$unmarkScreenRender(CallbackInfo callbackInfo) {
        PostProcessRenderer.internalMethod01673(false);
    }

    @Inject(
        method="handleClickEvent(Lnet/minecraft/text/ClickEvent;Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/gui/screen/Screen;)V",
        at=@At("HEAD"),
        cancellable=true
    )
    private static void rockstar$handleCommandClick(ClickEvent clickEvent, MinecraftClient minecraftClient, Screen screenAfterRun, CallbackInfo callbackInfo) {
        if (clickEvent instanceof ClickEvent.RunCommand runCommand
            && runCommand.command().startsWith(RockstarClient.getInstance().internalMethod05348().internalMethod03606())
            && minecraftClient.player != null) {
            RockstarClient.getInstance().internalMethod05348().internalMethod04610(runCommand.command());
            callbackInfo.cancel();
        }
    }
}
