package moscow.rockstar.mixin.minecraft.client.gui.overlay;




import rockstar.client.server.*;
import rockstar.client.render.*;
import rockstar.client.internal.core.*;
import globals.client.Information;
import globals.shared.proto.Packets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pyrock.utility.render.ColorRGBA;
import rockstar.modules.other.NameProtectModule;
import rockstar.modules.visual.BeautifullyModule;
import rockstar.client.RockstarClient;
import rockstar.client.server.ServerUtils;
import rockstar.client.internal.core.DrawCallCounter;
import rockstar.client.render.RenderPipeline;

@Mixin(value={PlayerListHud.class})
public class PlayerListHudMixin {
    @Unique
    private static int entryCount;

    @Inject(method={"collectPlayerEntries"}, at={@At(value="RETURN")}, cancellable=true)
    private void filterStreamerTabEntries(CallbackInfoReturnable<List<PlayerListEntry>> callbackInfoReturnable) {
        entryCount = ((List)callbackInfoReturnable.getReturnValue()).size();
        NameProtectModule typedValue207 = RockstarClient.getInstance().getModuleManager().getModule(NameProtectModule.class);
        if (typedValue207 == null || !typedValue207.isEnabled() || !typedValue207.internalMethod04251().internalMethod04496()) {
            return;
        }
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (minecraftClient.getSession() == null) {
            return;
        }
        ArrayList<PlayerListEntry> arrayList = new ArrayList<PlayerListEntry>();
        for (PlayerListEntry playerListEntry : (Iterable<PlayerListEntry>)(Iterable<?>)(List)callbackInfoReturnable.getReturnValue()) {
            String string = playerListEntry.getProfile().name();
            if (!string.equalsIgnoreCase(minecraftClient.getSession().getUsername()) && this.shouldHideStreamerTabEntry(playerListEntry)) continue;
            arrayList.add(playerListEntry);
        }
        callbackInfoReturnable.setReturnValue(arrayList);
        entryCount = arrayList.size();
    }

    @ModifyConstant(method={"render"}, constant={@Constant(intValue=20)})
    private int rockstar$rowsPerColumn(int n) {
        int n2 = BeautifullyModule.internalMethod08757();
        if (n2 <= 0 || entryCount <= 0) {
            return n;
        }
        return Math.max(1, MathHelper.ceil((float)((float)entryCount / (float)n2)));
    }

    @Inject(method={"getPlayerName"}, at={@At(value="RETURN")}, cancellable=true)
    private void cleanStreamerTabName(PlayerListEntry playerListEntry, CallbackInfoReturnable<Text> callbackInfoReturnable) {
        NameProtectModule typedValue207 = RockstarClient.getInstance().getModuleManager().getModule(NameProtectModule.class);
        if (typedValue207 == null || !typedValue207.isEnabled() || !typedValue207.internalMethod04251().internalMethod04496()) {
            return;
        }
        callbackInfoReturnable.setReturnValue(Text.literal((String)typedValue207.internalMethod08287(playerListEntry.getProfile().name())));
    }

    @Unique
    private boolean shouldHideStreamerTabEntry(PlayerListEntry playerListEntry) {
        String string = playerListEntry.getProfile().name();
        int n = Objects.hash(string.toLowerCase(Locale.ROOT), playerListEntry.getProfile().id(), ServerUtils.internalMethod00929());
        return Math.floorMod(n, 4) == 0;
    }
}

