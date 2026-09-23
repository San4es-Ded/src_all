package ru.haron.mixin;

import haron.client.MinecraftClientAccess;
import haron.module.ModuleManager;
import haron.modules.utilities.StreamerMode;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={InGameHud.class})
public class ScoreboardMixin
implements MinecraftClientAccess {
    @Unique
    private static final Text FAKE_TITLE = Text.literal((String)"@nativevm");
    @Unique
    private static volatile boolean cachedHideTitle = false;
    @Unique
    private static volatile long cachedTime = 0L;
    @Unique
    private static final long CACHE_TTL_MS = 500L;

    @Unique
    private void refreshHideTitle() {
        long now = System.currentTimeMillis();
        if (now - cachedTime < 500L) {
            return;
        }
        cachedTime = now;
        boolean hide = false;
        try {
            StreamerMode streamerMode = ModuleManager.STREAMER_MODE;
            if (streamerMode.k() && ((Boolean)streamerMode.hideServerNumber.k()).booleanValue()) {
                if (c.getCurrentServerEntry() == null) {
                    hide = true;
                } else if (!ScoreboardMixin.c.getCurrentServerEntry().address.toLowerCase().contains("holyworld")) {
                    hide = true;
                }
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        cachedHideTitle = hide;
    }

    @Redirect(method={"renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V"}, at=@At(value="INVOKE", target="Lnet/minecraft/scoreboard/ScoreboardObjective;getDisplayName()Lnet/minecraft/text/Text;"))
    private Text hideScoreboardTitle(ScoreboardObjective ScoreboardObjectiveVar) {
        this.refreshHideTitle();
        return cachedHideTitle ? FAKE_TITLE : ScoreboardObjectiveVar.getDisplayName();
    }
}

