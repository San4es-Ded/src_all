package rockstar.client.internal.script;






import rockstar.client.util.*;
import rockstar.client.i18n.*;
import rockstar.client.event.*;
import rockstar.client.internal.game.*;
import rockstar.client.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import moscow.rockstar.mixin.accessors.ChatHudAccessor;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.network.message.ChatVisibility;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import pyrock.events.render.ChatRenderEvent;
import pyrock.events.window.ChatClickEvent;
import rockstar.client.event.EventListener;
import rockstar.client.internal.game.WaypointStore;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.RockstarClient;
import rockstar.client.util.ClientMessages;
import rockstar.client.MinecraftClientAccess;

public class ChatWaypointHandler
implements MinecraftClientAccess {
    private static final Pattern internalField0293 = Pattern.compile("(-?\\d{1,8})(?:\\s*,\\s*|\\s+)(-?\\d{1,8})(?:\\s*,\\s*|\\s+)(-?\\d{1,8})");
    private static final Pattern internalField0294 = Pattern.compile("(-?\\d{1,8})(?:\\s*,\\s*|\\s+)(-?\\d{1,8})");
    private static final String internalField0248 = "ChatWaypoint";
    private static final MutableText internalField0703 = Text.literal((String)LanguageManager.internalMethod07214("chat_waypoint.hover"));
    private final EventListener<ChatRenderEvent> internalField0157 = this::internalMethod02172;
    private final EventListener<ChatClickEvent> internalField0158 = this::internalMethod02567;

    public ChatWaypointHandler() {
        RockstarClient.getInstance().internalMethod03317().internalMethod00647(this);
    }

    private void internalMethod02172(ChatRenderEvent chatRenderEvent) {
        if (!(ChatWaypointHandler.internalField0149.currentScreen instanceof ChatScreen) || ChatWaypointHandler.internalField0149.inGameHud == null) {
            return;
        }
        double d = ChatWaypointHandler.internalField0149.mouse.getX() * (double)internalField0149.getWindow().getScaledWidth() / (double)internalField0149.getWindow().getWidth();
        double d2 = ChatWaypointHandler.internalField0149.mouse.getY() * (double)internalField0149.getWindow().getScaledHeight() / (double)internalField0149.getWindow().getHeight();
        ChatHud chatHud = ChatWaypointHandler.internalField0149.inGameHud.getChatHud();
        ChatHudAccessor chatHudAccessor = (ChatHudAccessor)(Object)chatHud;
        double d3 = this.rockstar$toChatLineX(d);
        double d4 = this.rockstar$toChatLineY(d2);
        if (d3 < 0.0 || d4 < 0.0) {
            return;
        }
        int n = this.rockstar$getMessageLineIndex(chatHud, chatHudAccessor, d3, d4);
        List<ChatHudLine.Visible> list = chatHudAccessor.getVisibleMessages();
        if (n < 0 || n >= list.size()) {
            return;
        }
        ChatHudLine.Visible visible = list.get(n);
        String string = this.internalMethod01846(visible.content());
        if (string.isBlank()) {
            return;
        }
        if (this.internalMethod07400(string, d3)) {
            chatRenderEvent.getContext().drawTooltip(ChatWaypointHandler.internalField0149.textRenderer, List.of(internalField0703), (int)d, (int)d2);
        }
    }

    private void internalMethod02567(ChatClickEvent chatClickEvent) {
        if (chatClickEvent.getButton() != 0) {
            return;
        }
        if (!(ChatWaypointHandler.internalField0149.currentScreen instanceof ChatScreen) || ChatWaypointHandler.internalField0149.inGameHud == null) {
            return;
        }
        ChatHud chatHud = ChatWaypointHandler.internalField0149.inGameHud.getChatHud();
        ChatHudAccessor chatHudAccessor = (ChatHudAccessor)(Object)chatHud;
        double d = this.rockstar$toChatLineX(chatClickEvent.getX());
        double d2 = this.rockstar$toChatLineY(chatClickEvent.getY());
        if (d < 0.0 || d2 < 0.0) {
            return;
        }
        int n = this.rockstar$getMessageLineIndex(chatHud, chatHudAccessor, d, d2);
        List<ChatHudLine.Visible> list = chatHudAccessor.getVisibleMessages();
        if (n < 0 || n >= list.size()) {
            return;
        }
        ChatHudLine.Visible visible = list.get(n);
        String string = this.internalMethod01846(visible.content());
        if (string.isBlank()) {
            return;
        }
        Matcher matcher = internalField0293.matcher(string);
        while (matcher.find()) {
            if (!this.internalMethod06758(d, string, matcher.start(), matcher.end())) continue;
            this.internalMethod01714(matcher.group(1), matcher.group(2), matcher.group(3));
            return;
        }
        Matcher matcher2 = internalField0294.matcher(string);
        while (matcher2.find()) {
            if (!this.internalMethod06758(d, string, matcher2.start(), matcher2.end())) continue;
            this.internalMethod01714(matcher2.group(1), String.valueOf(70), matcher2.group(2));
            return;
        }
    }

    private boolean internalMethod07400(String string, double d) {
        Matcher matcher = internalField0293.matcher(string);
        while (matcher.find()) {
            if (!this.internalMethod06758(d, string, matcher.start(), matcher.end())) continue;
            return true;
        }
        Matcher matcher2 = internalField0294.matcher(string);
        while (matcher2.find()) {
            if (!this.internalMethod06758(d, string, matcher2.start(), matcher2.end())) continue;
            return true;
        }
        return false;
    }

    private double rockstar$toChatLineX(double x) {
        return x / internalField0149.options.getChatScale().getValue() - 4.0;
    }

    private double rockstar$toChatLineY(double y) {
        double scale = internalField0149.options.getChatScale().getValue();
        double lineHeight = 9.0 * (internalField0149.options.getChatLineSpacing().getValue() + 1.0);
        return (internalField0149.getWindow().getScaledHeight() - y - 40.0) / (scale * lineHeight);
    }

    private int rockstar$getMessageLineIndex(ChatHud chatHud, ChatHudAccessor accessor, double x, double y) {
        if (!(internalField0149.currentScreen instanceof ChatScreen)
            || internalField0149.options.getChatVisibility().getValue() == ChatVisibility.HIDDEN) {
            return -1;
        }

        double scale = internalField0149.options.getChatScale().getValue();
        int width = MathHelper.floor(ChatHud.getWidth(internalField0149.options.getChatWidth().getValue()) / scale);
        if (x < -4.0 || x > width) {
            return -1;
        }

        List<ChatHudLine.Visible> visibleMessages = accessor.getVisibleMessages();
        int visibleLineCount = Math.min(chatHud.getVisibleLineCount(), visibleMessages.size());
        if (y < 0.0 || y >= visibleLineCount) {
            return -1;
        }

        int line = MathHelper.floor(y + accessor.getScrolledLines());
        return line >= 0 && line < visibleMessages.size() ? line : -1;
    }

    private boolean internalMethod06758(double d, String string, int n, int n2) {
        int n3 = ChatWaypointHandler.internalField0149.textRenderer.getWidth(string.substring(0, n));
        int n4 = ChatWaypointHandler.internalField0149.textRenderer.getWidth(string.substring(0, n2));
        return d >= (double)n3 && d <= (double)n4;
    }

    private void internalMethod01714(String string, String string2, String string3) {
        int n;
        int n2;
        int n3;
        try {
            n3 = Integer.parseInt(string);
            n2 = Integer.parseInt(string2);
            n = Integer.parseInt(string3);
        }
        catch (NumberFormatException numberFormatException) {
            n = 0;
            n2 = 0;
            n3 = 0;
            ClientMessages.internalMethod09025(Text.of((String)LanguageManager.internalMethod07214("waypoints.error_coords")));
        }
        WaypointStore typedValue188 = RockstarClient.getInstance().internalMethod06121();
        if (typedValue188.internalMethod06387(internalField0248)) {
            typedValue188.internalMethod06386(internalField0248);
        }
        typedValue188.internalMethod05412(internalField0248, n3, n2, n);
    }

    private String internalMethod01846(OrderedText orderedText) {
        StringBuilder stringBuilder = new StringBuilder();
        orderedText.accept((n, style, n2) -> {
            stringBuilder.appendCodePoint(n2);
            return true;
        });
        return stringBuilder.toString();
    }
}
