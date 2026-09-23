package moscow.rockstar.mixin.minecraft.client.gui.screen;


import rockstar.client.internal.script.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rockstar.client.RockstarClient;
import rockstar.client.internal.script.MainMenuScreen;
import rockstar.client.MinecraftClientAccess;

@Mixin(value={TitleScreen.class})
public abstract class TitleScreenMixin extends Screen
implements MinecraftClientAccess {
    protected TitleScreenMixin() {
        super(Text.empty());
    }

    @Inject(method={"init"}, at={@At(value="HEAD")}, cancellable=true)
    public void setCustomScreen(CallbackInfo callbackInfo) {
        if (RockstarClient.internalField0240.internalMethod06896()) {
            return;
        }
        callbackInfo.cancel();
        internalField0149.setScreen((Screen)new MainMenuScreen());
    }
}
