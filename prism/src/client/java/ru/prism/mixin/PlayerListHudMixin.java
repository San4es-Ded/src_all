package ru.prism.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.network.ClientConnection;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.prism.Client;
import ru.prism.module.impl.render.TabCustomizer;
import ru.prism.theme.ThemeColor;
import ru.prism.utils.other.Instance;

import java.util.ArrayList;
import java.util.List;

@Mixin(PlayerListHud.class)
public class PlayerListHudMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    public Text header;

    @Shadow
    public Text footer;

    @Inject(method = "getPlayerName", at = @At("RETURN"), cancellable = true)
    private void prism$colorPlayerName(PlayerListEntry entry, CallbackInfoReturnable<Text> cir) {
        TabCustomizer tab = Instance.get(TabCustomizer.class);
        if (tab == null || !tab.isEnabled() || entry == null) return;

        int color = prism$highlightColor(tab, entry);
        if (color == 0) return;

        Text name = cir.getReturnValue();
        if (name == null) return;

        MutableText colored = name.copy();
        colored.setStyle(colored.getStyle().withColor(color));
        cir.setReturnValue(colored);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/PlayerListHud;collectPlayerEntries()Ljava/util/List;"))
    private List<PlayerListEntry> prism$friendsOnTop(PlayerListHud hud) {
        List<PlayerListEntry> entries = hud.collectPlayerEntries();
        TabCustomizer tab = Instance.get(TabCustomizer.class);
        if (tab == null || !tab.isEnabled() || !tab.friendsOnTop.getValue() || entries.size() < 2) return entries;

        List<PlayerListEntry> sorted = new ArrayList<>(entries);
        sorted.sort((first, second) -> Boolean.compare(!prism$isFriend(first), !prism$isFriend(second)));
        return sorted;
    }

    @Redirect(method = "render", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/hud/PlayerListHud;header:Lnet/minecraft/text/Text;"))
    private Text prism$hideHeader(PlayerListHud hud) {
        TabCustomizer tab = Instance.get(TabCustomizer.class);
        return tab != null && tab.isEnabled() && tab.hideHeader.getValue() ? null : this.header;
    }

    @Redirect(method = "render", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/hud/PlayerListHud;footer:Lnet/minecraft/text/Text;"))
    private Text prism$hideFooter(PlayerListHud hud) {
        TabCustomizer tab = Instance.get(TabCustomizer.class);
        return tab != null && tab.isEnabled() && tab.hideFooter.getValue() ? null : this.footer;
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/ClientConnection;isEncrypted()Z"))
    private boolean prism$playerHeads(ClientConnection connection) {
        TabCustomizer tab = Instance.get(TabCustomizer.class);
        if (tab != null && tab.isEnabled()) return tab.showHeads.getValue();
        return connection != null && connection.isEncrypted();
    }

    @Inject(method = "renderLatencyIcon", at = @At("HEAD"), cancellable = true)
    private void prism$latencyIcon(DrawContext context, int width, int x, int y, PlayerListEntry entry, CallbackInfo ci) {
        TabCustomizer tab = Instance.get(TabCustomizer.class);
        if (tab == null || !tab.isEnabled()) return;

        String mode = tab.pingDisplay.getValue();
        if ("Скрыть".equals(mode)) {
            ci.cancel();
            return;
        }
        if (!"Цифры".equals(mode)) return;

        int latency = entry != null ? entry.getLatency() : 0;
        String ping = String.valueOf(latency);
        int color = 65407;
        if (latency > 150) {
            color = 16729344;
        } else if (latency > 80) {
            color = 16776960;
        }
        TextRenderer textRenderer = this.client.textRenderer;
        context.drawTextWithShadow(textRenderer, ping, x + width - textRenderer.getWidth(ping), y + 1, color);
        ci.cancel();
    }

    @Unique
    private int prism$highlightColor(TabCustomizer tab, PlayerListEntry entry) {
        if (entry.getProfile() == null) return 0;
        if (tab.selfHighlight.getValue() && this.client.player != null
                && entry.getProfile().id().equals(this.client.player.getUuid())) {
            return ThemeColor.getVisualColor() & 0xFFFFFF;
        }
        if (tab.friendHighlight.getValue() && prism$isFriend(entry)) {
            return tab.friendColor.getValue() & 0xFFFFFF;
        }
        if (tab.partyHighlight.getValue() && prism$sameTeam(entry)) {
            return 0x55FFFF;
        }
        return 0;
    }

    @Unique
    private boolean prism$isFriend(PlayerListEntry entry) {
        if (entry.getProfile() == null || entry.getProfile().name() == null) return false;
        Client client = Client.get();
        return client != null && client.friendManager() != null && client.friendManager().isFriend(entry.getProfile().name());
    }

    @Unique
    private boolean prism$sameTeam(PlayerListEntry entry) {
        if (this.client.player == null) return false;
        Team own = this.client.player.getScoreboardTeam();
        Team other = entry.getScoreboardTeam();
        return own != null && own == other;
    }
}
