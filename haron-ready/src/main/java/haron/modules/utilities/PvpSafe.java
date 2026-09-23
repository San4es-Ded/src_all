package haron.modules.utilities;

import haron.events.ChatSendEvent;
import haron.events.ClientTickEvent;
import haron.media.chat.w53bpe;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.player.lrsc12;
import haron.util.jeooat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import meteordevelopment.orbit.EventHandler;

@ModuleInfo(a="PVP Safe", b="Blocks risky commands while PvP mode is active.", c=ModuleCategory.UTILITIES)
public class PvpSafe
extends HaronModule {
    private static final long WARNING_COOLDOWN_MS = 3000L;
    private final Pattern dangerousCommandPattern = Pattern.compile("^/(spawn|home|hub|lobby|rtp|warp|back|logout|server|suicide|kill|auction|trade|duel|pay|msg|tell|w)(\\s+.*)?$", 2);
    private final Map<String, jeooat> warningTimers = new ConcurrentHashMap<String, jeooat>();

    @EventHandler
    private void a(ClientTickEvent q8krcw2) {
        this.warningTimers.values().removeIf(jeooat2 -> {
            return jeooat2.a(3000L);
        });
    }

    @EventHandler
    private void a(ChatSendEvent cjbc7b2) {
        Matcher matcher;
        String string = cjbc7b2.d();
        if (string != null && string.startsWith("/") && lrsc12.a().a() && (matcher = this.dangerousCommandPattern.matcher(string.trim())).matches()) {
            String string2 = matcher.group(1).toLowerCase();
            jeooat jeooat2 = this.warningTimers.remove(string2);
            if (jeooat2 != null && !jeooat2.a(3000L)) {
                cjbc7b2.b();
                return;
            }
            this.warningTimers.put(string2, new jeooat());
            w53bpe.a((Object)PvpSafe.$sf$0(string2));
            cjbc7b2.b();
        }
    }

    private static /* synthetic */ String $sf$0(String string) {
        return "PVP Safe blocked /" + string + " while PvP mode is active.";
    }
}

