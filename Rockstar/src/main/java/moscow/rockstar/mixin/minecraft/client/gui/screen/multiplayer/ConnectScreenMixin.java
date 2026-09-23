package moscow.rockstar.mixin.minecraft.client.gui.screen.multiplayer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.network.CookieStorage;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pyrock.events.network.ServerConnectionEvent;
import rockstar.client.RockstarClient;

@Mixin(value={ConnectScreen.class})
public class ConnectScreenMixin {
    @Inject(method={"connect(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/network/ServerAddress;Lnet/minecraft/client/network/ServerInfo;Lnet/minecraft/client/network/CookieStorage;)V"}, at={@At(value="HEAD")})
    private void onNewConnection(MinecraftClient minecraftClient, ServerAddress serverAddress, ServerInfo serverInfo, CookieStorage cookieStorage, CallbackInfo callbackInfo) {
        RockstarClient.getInstance().internalMethod03317().internalMethod06883(new ServerConnectionEvent(serverAddress, serverInfo, cookieStorage));
    }
}

