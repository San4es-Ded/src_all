package ru.haron.mixin;

import haron.client.MinecraftClientAccess;
import haron.module.ModuleManager;
import haron.modules.utilities.StreamerMode;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.text.PlainTextContent;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Style;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={PlainTextContent.Literal.class})
public class TextMixin
implements MinecraftClientAccess {
    @Shadow
    @Final
    private String string;
    @Unique
    private static final String FAKE_NICK = "Haron";
    @Unique
    private static final Pattern holyWorldPattern = Pattern.compile("(СолоЛайт|ДуоЛайт|ТриоЛайт|КланЛайт)\\s*#(\\d{1,2})");
    @Unique
    private static final Pattern funTimePattern = Pattern.compile("Анархия-(\\d+)");
    @Unique
    private static volatile boolean cachedReplaceNick = false;
    @Unique
    private static volatile boolean cachedHideServer = false;
    @Unique
    private static volatile String cachedUsername = null;
    @Unique
    private static volatile long cachedFlagsTime = 0L;
    @Unique
    private static final long FLAGS_TTL_MS = 500L;
    @Unique
    private static final ConcurrentHashMap<String, String> RESULT_CACHE = new ConcurrentHashMap();
    @Unique
    private static volatile long resultCacheTime = 0L;
    @Unique
    private static final long RESULT_CACHE_TTL_MS = 2000L;
    @Unique
    private static final int RESULT_CACHE_MAX = 2048;

    @Redirect(method={"visit(Lnet/minecraft/text/StringVisitable$Visitor;)Ljava/util/Optional;"}, at=@At(value="INVOKE", target="Lnet/minecraft/text/StringVisitable$Visitor;accept(Ljava/lang/String;)Ljava/util/Optional;"))
    private <ConfigTextDialog> Optional<ConfigTextDialog> redirectVisitor(StringVisitable.Visitor<ConfigTextDialog> class_5245Var, String str) {
        return class_5245Var.accept(this.getProcessedText(str));
    }

    @Redirect(method={"visit(Lnet/minecraft/text/StringVisitable$StyledVisitor;Lnet/minecraft/text/Style;)Ljava/util/Optional;"}, at=@At(value="INVOKE", target="Lnet/minecraft/text/StringVisitable$StyledVisitor;accept(Lnet/minecraft/text/Style;Ljava/lang/String;)Ljava/util/Optional;"))
    private <ConfigTextDialog> Optional<ConfigTextDialog> redirectStyledVisitor(StringVisitable.StyledVisitor<ConfigTextDialog> class_5246Var, Style StyleVar, String str) {
        return class_5246Var.accept(StyleVar, this.getProcessedText(str));
    }

    @Unique
    private void refreshFlags() {
        long now = System.currentTimeMillis();
        if (now - cachedFlagsTime < 500L) {
            return;
        }
        cachedFlagsTime = now;
        boolean replaceNick = false;
        boolean hideServer = false;
        String username = null;
        try {
            StreamerMode streamerMode = ModuleManager.STREAMER_MODE;
            if (streamerMode.k()) {
                if (((Boolean)streamerMode.hidePlayerName.k()).booleanValue() && c.getSession() != null) {
                    username = c.getSession().getUsername();
                    boolean bl = replaceNick = username != null && !username.isEmpty();
                }
                if (((Boolean)streamerMode.hideServerNumber.k()).booleanValue() && c.getCurrentServerEntry() != null) {
                    String lowerCase = TextMixin.c.getCurrentServerEntry().address.toLowerCase();
                    hideServer = lowerCase.contains("holyworld") || lowerCase.contains("funtime");
                }
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        cachedReplaceNick = replaceNick;
        cachedHideServer = hideServer;
        cachedUsername = username;
        RESULT_CACHE.clear();
        resultCacheTime = now;
    }

    @Unique
    private String getProcessedText(String str) {
        String cached;
        if (str == null || str.isEmpty()) {
            return str;
        }
        this.refreshFlags();
        if (!cachedReplaceNick && !cachedHideServer) {
            return str;
        }
        long now = System.currentTimeMillis();
        if (now - resultCacheTime > 2000L) {
            resultCacheTime = now;
            RESULT_CACHE.clear();
        }
        if ((cached = RESULT_CACHE.get(str)) != null) {
            return cached;
        }
        String result = str;
        try {
            if (cachedReplaceNick) {
                result = result.replace(cachedUsername, FAKE_NICK);
            }
            if (cachedHideServer) {
                result = this.hideServerNumber(result);
            }
        }
        catch (Throwable t) {
            result = str;
        }
        if (RESULT_CACHE.size() < 2048) {
            RESULT_CACHE.put(str, result);
        }
        return result;
    }

    @Unique
    private String hideServerNumber(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        if (!str.contains("Анархия") && !str.contains("Лайт")) {
            return str;
        }
        Matcher matcher = holyWorldPattern.matcher(str);
        if (matcher.find()) {
            return matcher.replaceAll("@nativevm");
        }
        Matcher matcher2 = funTimePattern.matcher(str);
        return matcher2.find() ? matcher2.replaceAll("@nativevm") : str;
    }
}

