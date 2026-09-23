package moscow.rockstar.mixin.minecraft.client.gui.screen.multiplayer;


import rockstar.client.internal.network.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.option.ServerList;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rockstar.client.RockstarClient;
import rockstar.client.internal.network.PinnedServerManager;

@Mixin(value={MultiplayerScreen.class})
public abstract class MultiplayerScreenMixin
extends Screen {
    @Shadow
    protected MultiplayerServerListWidget serverListWidget;
    @Shadow
    private ButtonWidget buttonEdit;
    @Shadow
    private ButtonWidget buttonDelete;

    @Shadow
    public abstract ServerList getServerList();

    @Shadow
    protected abstract void updateButtonActivationStates();

    protected MultiplayerScreenMixin(Text text) {
        super(text);
    }

    @Inject(method={"init"}, at={@At(value="TAIL")})
    private void rockstar$syncPinnedServer(CallbackInfo callbackInfo) {
        if (this.client == null || RockstarClient.internalField0240.internalMethod06896()) {
            return;
        }
        PinnedServerManager.internalMethod01284(this.client, this.getServerList(), this::rockstar$reloadServerListWidget);
    }

    @Inject(method={"updateButtonActivationStates"}, at={@At(value="TAIL")})
    private void rockstar$lockPinnedButtons(CallbackInfo callbackInfo) {
        if (!this.rockstar$isPinnedServerSelected()) {
            return;
        }
        this.buttonEdit.active = false;
        this.buttonDelete.active = false;
    }

    @Inject(method={"removeEntry"}, at={@At(value="HEAD")}, cancellable=true)
    private void rockstar$preventPinnedRemoval(boolean bl, CallbackInfo callbackInfo) {
        if (!bl || this.client == null || !this.rockstar$isPinnedServerSelected() || RockstarClient.internalField0240.internalMethod06896()) {
            return;
        }
        this.client.setScreen((Screen)((MultiplayerScreen)(Object)this));
        callbackInfo.cancel();
    }

    @Inject(method={"editEntry"}, at={@At(value="HEAD")}, cancellable=true)
    private void rockstar$preventPinnedEdit(boolean bl, CallbackInfo callbackInfo) {
        if (!bl || this.client == null || !this.rockstar$isPinnedServerSelected()) {
            return;
        }
        this.client.setScreen((Screen)((MultiplayerScreen)(Object)this));
        callbackInfo.cancel();
    }

    @Unique
    private void rockstar$reloadServerListWidget() {
        if (this.client == null || this.client.currentScreen != this || this.serverListWidget == null || RockstarClient.internalField0240.internalMethod06896()) {
            return;
        }
        this.serverListWidget.setServers(this.getServerList());
        this.updateButtonActivationStates();
    }

    @Unique
    private boolean rockstar$isPinnedServerSelected() {
        if (this.serverListWidget == null) {
            return false;
        }
        MultiplayerServerListWidget.Entry entry = (MultiplayerServerListWidget.Entry)(Object)this.serverListWidget.getSelectedOrNull();
        if (!(entry instanceof MultiplayerServerListWidget.ServerEntry)) {
            return false;
        }
        MultiplayerServerListWidget.ServerEntry serverEntry = (MultiplayerServerListWidget.ServerEntry)entry;
        ServerInfo serverInfo = serverEntry.getServer();
        return PinnedServerManager.internalMethod04101(serverInfo);
    }
}

