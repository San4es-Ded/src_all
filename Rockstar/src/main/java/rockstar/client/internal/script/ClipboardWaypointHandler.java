package rockstar.client.internal.script;






import rockstar.client.util.*;
import rockstar.client.i18n.*;
import rockstar.client.event.*;
import rockstar.client.internal.game.*;
import rockstar.client.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import pyrock.events.window.KeyPressEvent;
import rockstar.client.event.EventListener;
import rockstar.client.internal.game.WaypointStore;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.RockstarClient;
import rockstar.client.util.ClientMessages;
import rockstar.client.MinecraftClientAccess;

public class ClipboardWaypointHandler
implements MinecraftClientAccess {
    private static final Pattern internalField0293 = Pattern.compile("(-?\\d{1,8})(?:\\s*,\\s*|\\s+)(-?\\d{1,8})(?:\\s*,\\s*|\\s+)(-?\\d{1,8})");
    private static final Pattern internalField0294 = Pattern.compile("(-?\\d{1,8})(?:\\s*,\\s*|\\s+)(-?\\d{1,8})");
    private static final String internalField0248 = "ClipboardWaypoint";
    private final EventListener<KeyPressEvent> internalField0157 = this::internalMethod01340;

    public ClipboardWaypointHandler() {
        RockstarClient.getInstance().internalMethod03317().internalMethod00647(this);
    }

    private void internalMethod01340(KeyPressEvent keyPressEvent) {
        if (keyPressEvent.getAction() != 1) {
            return;
        }
        if (keyPressEvent.getKey() != 86) {
            return;
        }
        if (ClipboardWaypointHandler.internalField0149.currentScreen != null || !rockstar.client.compat.InputCompat.hasControlDown()) {
            return;
        }
        String string = ClipboardWaypointHandler.internalField0149.keyboard.getClipboard();
        if (string == null || string.isBlank()) {
            return;
        }
        Matcher matcher = internalField0293.matcher(string);
        if (matcher.find()) {
            this.internalMethod01140(matcher.group(1), matcher.group(2), matcher.group(3));
            return;
        }
        Matcher matcher2 = internalField0294.matcher(string);
        if (matcher2.find()) {
            this.internalMethod01140(matcher2.group(1), String.valueOf(70), matcher2.group(2));
        }
    }

    private void internalMethod01140(String string, String string2, String string3) {
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
}

