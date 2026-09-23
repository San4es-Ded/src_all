package moscow.rockstar.mixin.minecraft.client.gui.overlay;



import rockstar.client.server.*;
import rockstar.client.internal.script.*;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.gui.hud.ClientBossBar;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rockstar.modules.visual.RemovalsModule;
import rockstar.client.internal.script.DynamicIslandHud;
import rockstar.client.RockstarClient;
import rockstar.client.internal.script.PvpModeHud;
import rockstar.client.server.KnownServer;
import rockstar.client.server.ServerUtils;
import rockstar.client.MinecraftClientAccess;

@Mixin(value={BossBarHud.class})
public class BossBarHudMixin
implements MinecraftClientAccess {
    @Shadow
    @Final
    private Map<UUID, ClientBossBar> bossBars;
    @Unique
    private static final Pattern PVP_TIME_PATTERN = Pattern.compile("(\\d+)\\s*(?:[\u0441c][\u0435e][\u043ak]\\.?|[\u0441c][\u0435e][\u043ak][\u0443y]?[\u043dnh][\u0434d]?)(?=$|\\s|\\p{Punct})", 322);
    @Unique
    private static final Pattern ROCKSTAR_PVP_TIME_LOOSE = Pattern.compile("(\\d{1,4})[^\\d]{0,4}?[\u0441c][\u0435e][\u043ak]", 66);
    @Unique
    private static final Pattern ROCKSTAR_NUMBER = Pattern.compile("\\d{1,4}");
    @Unique
    private static final String ROCKSTAR_CYRILLIC = "\u0430\u0441\u0435\u043e\u0440\u0445\u0443\u043a\u043d\u0432\u0442\u043c";
    @Unique
    private static final String ROCKSTAR_LATIN = "aceopxykhbtm";
    private static final String FILTERED_TEXT = "\ub445\ua223\ua203\ub444\ua223\ua205";

    @Inject(method={"render"}, at={@At(value="HEAD")})
    private void onRenderHead(DrawContext drawContext, CallbackInfo callbackInfo) {
        int n = 0;
        for (ClientBossBar object : this.bossBars.values()) {
            int n2;
            String bl;
            if (object.getName() == null || !BossBarHudMixin.rockstar$isPvpBar(bl = BossBarHudMixin.rockstar$plain(object.getName().getString())) || (n2 = BossBarHudMixin.rockstar$seconds(bl)) <= n) continue;
            n = n2;
        }
        ServerUtils.internalMethod05301(n > 0);
        ServerUtils.internalMethod05300(n);
        RemovalsModule typedValue322 = RockstarClient.getInstance().getModuleManager().getModule(RemovalsModule.class);
        if (typedValue322.isEnabled() && typedValue322.internalMethod08477().isSelected()) {
            return;
        }
        if (!(RockstarClient.getInstance().internalMethod06896() || !RockstarClient.getInstance().internalMethod01271().internalMethod01259().isShowing() || this.bossBars.isEmpty() || typedValue322.isEnabled() && typedValue322.internalMethod08477().isSelected() || ServerUtils.internalMethod01786(KnownServer.internalField1219))) {
            boolean bl;
            DynamicIslandHud typedValue201 = RockstarClient.getInstance().internalMethod01271().internalMethod01259();
            boolean bl2 = bl = typedValue201.isShowing() && typedValue201.internalMethod05688().stream().anyMatch(typedValue202 -> typedValue202 instanceof PvpModeHud);
            if (typedValue322.isEnabled() && typedValue322.internalMethod08477().isSelected() || ServerUtils.internalField0277 && bl) {
                return;
            }
            drawContext.getMatrices().pushMatrix();
            drawContext.getMatrices().translate(0.0f, RockstarClient.getInstance().internalMethod01271().internalMethod01259().internalMethod07527().internalField0206 + 7.0f);
        }
    }

    @Inject(method={"render"}, at={@At(value="HEAD")}, cancellable=true)
    private void render(CallbackInfo callbackInfo) {
        boolean bl;
        RemovalsModule typedValue322 = RockstarClient.getInstance().getModuleManager().getModule(RemovalsModule.class);
        DynamicIslandHud typedValue201 = RockstarClient.getInstance().internalMethod01271().internalMethod01259();
        boolean bl2 = bl = typedValue201.isShowing() && typedValue201.internalMethod05688().stream().anyMatch(typedValue202 -> typedValue202 instanceof PvpModeHud);
        if (typedValue322.isEnabled() && typedValue322.internalMethod08477().isSelected() || ServerUtils.internalField0277 && bl) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"render"}, at={@At(value="RETURN")})
    private void onRenderReturn(DrawContext drawContext, CallbackInfo callbackInfo) {
        int n = 19 * this.bossBars.size();
        RemovalsModule typedValue322 = RockstarClient.getInstance().getModuleManager().getModule(RemovalsModule.class);
        if (typedValue322.isEnabled() && typedValue322.internalMethod08477().isSelected()) {
            return;
        }
        if (!(RockstarClient.getInstance().internalMethod06896() || !RockstarClient.getInstance().internalMethod01271().internalMethod01259().isShowing() || this.bossBars.isEmpty() || typedValue322.isEnabled() && typedValue322.internalMethod08477().isSelected() || ServerUtils.internalMethod01786(KnownServer.internalField1219))) {
            drawContext.getMatrices().popMatrix();
        }
    }

    @Unique
    private static String rockstar$plain(String string) {
        StringBuilder stringBuilder = new StringBuilder(string.length());
        for (int i = 0; i < string.length(); ++i) {
            char c = string.charAt(i);
            if (c >= '\ue000' && c <= '\uf8ff' || Character.getType(c) == 16) continue;
            stringBuilder.append(Character.isSpaceChar(c) ? (char)' ' : (char)c);
        }
        return stringBuilder.toString();
    }

    @Unique
    private static boolean rockstar$isPvpBar(String string) {
        String string2 = string.toLowerCase(Locale.ROOT);
        if (string2.contains("\u0431\u043e\u0439") || string2.contains("\u0431\u043e\u044e") || string2.contains("\u043f\u0432\u043f")) {
            return true;
        }
        StringBuilder stringBuilder = new StringBuilder(string2.length());
        for (int i = 0; i < string2.length(); ++i) {
            char c = string2.charAt(i);
            int n = ROCKSTAR_CYRILLIC.indexOf(c);
            stringBuilder.append(n < 0 ? c : ROCKSTAR_LATIN.charAt(n));
        }
        return stringBuilder.indexOf("pvp") >= 0;
    }

    @Unique
    private static int rockstar$seconds(String string) {
        Matcher matcher = PVP_TIME_PATTERN.matcher(string);
        if (matcher.find()) {
            return BossBarHudMixin.rockstar$parse(matcher.group(1));
        }
        Matcher matcher2 = ROCKSTAR_PVP_TIME_LOOSE.matcher(string);
        if (matcher2.find()) {
            return BossBarHudMixin.rockstar$parse(matcher2.group(1));
        }
        Matcher matcher3 = ROCKSTAR_NUMBER.matcher(string);
        if (!matcher3.find()) {
            return -1;
        }
        String string2 = matcher3.group();
        return matcher3.find() ? -1 : BossBarHudMixin.rockstar$parse(string2);
    }

    @Unique
    private static int rockstar$parse(String string) {
        try {
            return Integer.parseInt(string);
        }
        catch (NumberFormatException numberFormatException) {
            return -1;
        }
    }
}
