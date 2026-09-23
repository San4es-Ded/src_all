package ru.haron.mixin;

import haron.module.ModuleManager;
import haron.modules.utilities.PvpSafe;
import haron.player.lrsc12;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.MessageScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.realms.gui.screen.RealmsMainScreen;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GameMenuScreen.class})
public abstract class GameMenuScreenMixin
extends Screen {
    @Shadow
    @Nullable
    private ButtonWidget exitButton;

    protected GameMenuScreenMixin(Text TextVar) {
        super(TextVar);
    }

    @Inject(method={"initWidgets"}, at={@At(value="TAIL")})
    private void onInitWidgets(CallbackInfo callbackInfo) {
        if (this.exitButton != null) {
            Text TextVarGetMessage = this.exitButton.getMessage();
            int iGetX = this.exitButton.getX();
            int iGetY = this.exitButton.getY();
            int iGetWidth = this.exitButton.getWidth();
            int iGetHeight = this.exitButton.getHeight();
            this.remove((Element)this.exitButton);
            this.exitButton = ButtonWidget.builder((Text)TextVarGetMessage, ButtonWidgetVar -> {
                ButtonWidgetVar.active = false;
                PvpSafe pvpSafe = ModuleManager.PVP_SAFE;
                if (pvpSafe.k() && lrsc12.a().a()) {
                    this.client.setScreen((Screen)new ConfirmScreen(z -> {
                        if (z) {
                            this.performDisconnect();
                        } else {
                            this.client.setScreen((Screen)this);
                        }
                    }, (Text)Text.literal((String)"Leave the server while PVP Safe is active?"), (Text)Text.empty()));
                } else {
                    this.performDisconnect();
                }
            }).dimensions(iGetX, iGetY, iGetWidth, iGetHeight).build();
            this.addDrawableChild(this.exitButton);
        }
    }

    private void performDisconnect() {
        boolean zIsInSingleplayer = this.client.isInSingleplayer();
        ServerInfo ServerInfoVarGetCurrentServerEntry = this.client.getCurrentServerEntry();
        if (this.client.world != null) {
            this.client.world.disconnect();
        }
        if (zIsInSingleplayer) {
            this.client.disconnect((Screen)new MessageScreen((Text)Text.translatable((String)"menu.savingLevel")));
        } else {
            this.client.disconnect();
        }
        TitleScreen TitleScreenVar = new TitleScreen();
        if (zIsInSingleplayer) {
            this.client.setScreen((Screen)TitleScreenVar);
        } else if (ServerInfoVarGetCurrentServerEntry == null || !ServerInfoVarGetCurrentServerEntry.isRealm()) {
            this.client.setScreen((Screen)new MultiplayerScreen((Screen)TitleScreenVar));
        } else {
            this.client.setScreen((Screen)new RealmsMainScreen((Screen)TitleScreenVar));
        }
    }
}
