package moscow.rockstar.mixin.minecraft.client.gui.screen.multiplayer;



import rockstar.client.ui.*;
import rockstar.client.internal.network.*;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.network.ServerInfo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rockstar.client.RockstarClient;
import rockstar.client.ui.ThemeColors;
import rockstar.client.internal.network.PinnedServerManager;

@Mixin(value={MultiplayerServerListWidget.ServerEntry.class})
public abstract class MultiplayerServerListWidgetServerEntryMixin
extends MultiplayerServerListWidget.Entry {
    @Shadow
    @Final
    private ServerInfo server;

    protected MultiplayerServerListWidgetServerEntryMixin() {
    }

    @Inject(method={"swapEntries"}, at={@At(value="HEAD")}, cancellable=true)
    private void rockstar$preventPinnedMove(int n, int n2, CallbackInfo callbackInfo) {
        if (PinnedServerManager.internalMethod04101(this.server)) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"render"}, at={@At(value="TAIL")})
    private void rockstar$drawPinnedOutline(DrawContext drawContext, int mouseX, int mouseY, boolean hovered, float deltaTicks, CallbackInfo callbackInfo) {
        if (PinnedServerManager.internalMethod04101(this.server) && !RockstarClient.getInstance().internalMethod06896()) {
            int color = ThemeColors.internalField1616.mix(ThemeColors.internalMethod02531(), hovered ? 0.9f : 0.85f).getRGB();
            int left = this.getContentX() - 1;
            int top = this.getContentY() - 1;
            int right = this.getContentX() + this.getContentWidth() + 1;
            int bottom = this.getContentY() + this.getContentHeight() + 1;
            drawContext.fill(left, top, right, top + 1, color);
            drawContext.fill(left, bottom - 1, right, bottom, color);
            drawContext.fill(left, top + 1, left + 1, bottom - 1, color);
            drawContext.fill(right - 1, top + 1, right, bottom - 1, color);
        }
    }
}
