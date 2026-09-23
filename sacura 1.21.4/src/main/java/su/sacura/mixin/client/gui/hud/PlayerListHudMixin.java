package su.sacura.mixin.client.gui.hud;

import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import su.sacura.Sacura;
import su.sacura.features.modules.impl.render.BetterMinecraftModule;
import su.sacura.util.type.MinecraftWrapper;

@Mixin({PlayerListHud.class})
public abstract class PlayerListHudMixin implements MinecraftWrapper {
    @Shadow @Final private MinecraftClient field_2155;

    @Shadow @Nullable private Text field_2153;

    @Shadow @Nullable private Text field_2154;

    // Исправлено: getPlayerList() -> collectPlayerEntries()
    @Shadow
    protected abstract List<PlayerListEntry> collectPlayerEntries();

    @Inject(method = {"render"}, at = {@At("HEAD")})
    private void onRenderHead(DrawContext context, int scaledWindowWidth, Scoreboard scoreboard, ScoreboardObjective objective, CallbackInfo ci) {
        BetterMinecraftModule module = Sacura.getInstance().getModuleManager().getModule(BetterMinecraftModule.class);
        if (module != null && module.enable && ((Boolean)module.smoothTab.get()).booleanValue()) {
            context.getMatrices().push();

            List<PlayerListEntry> list = collectPlayerEntries();
            int n = list.size();
            int o = n;

            for (int p = 1; o > 20; o = (n + p - 1) / p)
                p++;

            List<OrderedText> list3 = null;
            if (this.field_2153 != null)
                list3 = this.field_2155.textRenderer.wrapLines((StringVisitable)this.field_2153, scaledWindowWidth - 50);

            List<OrderedText> list4 = null;
            if (this.field_2154 != null)
                list4 = this.field_2155.textRenderer.wrapLines((StringVisitable)this.field_2154, scaledWindowWidth - 50);

            int headerHeight = 0;
            if (list3 != null)
                headerHeight = list3.size() * 9 + 1;

            int footerHeight = 0;
            if (list4 != null)
                footerHeight = list4.size() * 9 + 1;

            int playerListHeight = o * 9;
            int totalHeight = headerHeight + playerListHeight + footerHeight;

            double animation = module.getTabOpenAnimation().getOutput();
            double hiddenY = (-totalHeight - 20);
            double shownY = 0.0D;

            double yOffset = hiddenY + (shownY - hiddenY) * animation;
            context.getMatrices().translate(0.0D, yOffset, 0.0D);
        }
    }

    @Inject(method = {"render"}, at = {@At("RETURN")})
    private void onRenderReturn(DrawContext context, int scaledWindowWidth, Scoreboard scoreboard, ScoreboardObjective objective, CallbackInfo ci) {
        BetterMinecraftModule module = Sacura.getInstance().getModuleManager().getModule(BetterMinecraftModule.class);
        if (module != null && module.enable && ((Boolean)module.smoothTab.get()).booleanValue())
            context.getMatrices().pop();
    }
}