package moscow.rockstar.mixin.minecraft.client.gui.screen;



import rockstar.client.server.*;
import rockstar.client.i18n.*;
import javax.annotation.Nullable;
import moscow.rockstar.mixin.accessors.ScreenAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.realms.gui.screen.RealmsMainScreen;
import net.minecraft.text.Text;
import net.minecraft.world.Difficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.RockstarClient;
import rockstar.client.server.KnownServer;
import rockstar.client.server.ServerUtils;
import rockstar.client.MinecraftClientAccess;

@Mixin(value={GameMenuScreen.class})
public class GameMenuScreenMixin
extends Screen
implements MinecraftClientAccess {
    @Shadow
    @Nullable
    private ButtonWidget exitButton;

    protected GameMenuScreenMixin(Text text) {
        super(text);
    }

    @Inject(method={"initWidgets"}, at={@At(value="TAIL")})
    private void reconnectButton(CallbackInfo callbackInfo) {
        if (RockstarClient.internalField0240.internalMethod06896()) {
            return;
        }
        if (internalField0149.isInSingleplayer()) {
            return;
        }
        if (this.exitButton == null) {
            return;
        }
        Text text = Text.of((String)LanguageManager.internalMethod07214("inventory.button.reconnect"));
        int n = 204;
        int n2 = this.exitButton.getX() + this.exitButton.getWidth() / 2 - n / 2;
        int n3 = this.exitButton.getY() + this.exitButton.getHeight() + (ServerUtils.internalMethod06501("aresmine") ? 44 : 4);
        ButtonWidget buttonWidget2 = ButtonWidget.builder((Text)text, buttonWidget -> this.reconnect()).dimensions(n2, n3, n, 20).build();
        ((ScreenAccessor)((Object)this)).invokeAddDrawableChild(buttonWidget2);
    }

    @Unique
    private void reconnect() {
        ServerInfo serverInfo;
        if (ServerUtils.internalMethod01786(KnownServer.internalField0578) && GameMenuScreenMixin.internalField0149.world.getDifficulty() == Difficulty.HARD) {
            try {
                GameMenuScreenMixin.internalField0149.player.networkHandler.sendChatCommand(".rct");
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        if ((serverInfo = internalField0149.getCurrentServerEntry()) == null) {
            return;
        }
        ServerAddress serverAddress = ServerAddress.parse((String)serverInfo.address);
        internalField0149.getAbuseReportContext().tryShowDraftScreen(internalField0149, (Screen)(Object)this, this::disconnect, true);
        new Thread(() -> {
            try {
                Thread.sleep(1200L);
            }
            catch (Exception exception) {
                // empty catch block
            }
            internalField0149.execute(() -> ConnectScreen.connect((Screen)new MultiplayerScreen((Screen)new TitleScreen()), (MinecraftClient)internalField0149, (ServerAddress)serverAddress, (ServerInfo)serverInfo, (boolean)false, null));
        }).start();
    }

    @Unique
    private void disconnect() {
        ServerInfo serverInfo = internalField0149.getCurrentServerEntry();
        internalField0149.disconnect(net.minecraft.client.world.ClientWorld.QUITTING_MULTIPLAYER_TEXT);
        TitleScreen titleScreen = new TitleScreen();
        if (serverInfo != null && serverInfo.isRealm()) {
            internalField0149.setScreen((Screen)new RealmsMainScreen((Screen)titleScreen));
        } else {
            internalField0149.setScreen((Screen)new MultiplayerScreen((Screen)titleScreen));
        }
    }
}
