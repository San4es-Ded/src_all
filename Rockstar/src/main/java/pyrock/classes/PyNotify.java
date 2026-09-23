package pyrock.classes;



import rockstar.client.notification.*;
import rockstar.client.internal.core.*;
import java.util.Locale;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.RockstarClient;
import rockstar.client.internal.core.ClientSoundEvents;
import rockstar.client.notification.NotificationManager;
import rockstar.client.notification.NotificationType;
import rockstar.client.notification.ItemNotification;

public class PyNotify {
    public void island(String string, String string2) {
        PyNotify.manager().internalMethod04075(PyNotify.type(string2), string == null ? "" : string);
    }

    public void crosshair(String string, String string2, String string3) {
        PyNotify.manager().internalMethod00599(PyNotify.type(string3), string == null ? "" : string, string2 == null ? "" : string2);
    }

    public void item(String string, String string2, String string3, @Nullable ColorRGBA colorRGBA) {
        Item item = PyNotify.item(string2);
        ItemNotification typedValue052 = new ItemNotification(string == null ? "" : string, item);
        if (string3 != null && !string3.isEmpty()) {
            typedValue052.internalMethod03390(string3);
            if (colorRGBA != null) {
                typedValue052.internalMethod05942(colorRGBA);
            }
        }
        PyNotify.manager().internalMethod02784(typedValue052);
    }

    public void sound() {
        try {
            ClientSoundEvents.internalField1017.internalMethod03864(1.0f);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public int count() {
        return PyNotify.manager().internalMethod02336().size();
    }

    private static NotificationType type(String string) {
        if (string == null) {
            return NotificationType.internalField1280;
        }
        return switch (string.toLowerCase(Locale.ROOT)) {
            case "success", "ok", "good" -> NotificationType.internalField0704;
            case "error", "fail", "bad" -> NotificationType.internalField0705;
            default -> NotificationType.internalField1280;
        };
    }

    private static Item item(String string) {
        Identifier identifier;
        if (string == null || string.isBlank()) {
            return Items.PAPER;
        }
        Identifier identifier2 = identifier = string.contains(":") ? Identifier.tryParse((String)string) : Identifier.tryParse((String)("minecraft:" + string));
        if (identifier == null) {
            return Items.PAPER;
        }
        Item item = (Item)Registries.ITEM.get(identifier);
        return item == Items.AIR ? Items.PAPER : item;
    }

    private static NotificationManager manager() {
        return RockstarClient.getInstance().internalMethod02503();
    }
}

