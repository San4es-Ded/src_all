package rockstar.client.internal.core;



import rockstar.client.util.*;
import rockstar.client.*;
import net.minecraft.text.Text;
import rockstar.client.util.ClientMessages;
import rockstar.client.internal.core.NewtonGlobalState;

public final class ChatMessageHelper {
    private ChatMessageHelper() {
    }

    public static void internalMethod06832(Text text) {
        ClientMessages.internalMethod01809(text);
    }

    public static void internalMethod00196(String string) {
        ClientMessages.internalMethod01809((Text)Text.literal((String)string));
    }

    public static void internalMethod07572(Text text) {
        ClientMessages.internalMethod09025(text);
    }

    public static void internalMethod06835(String string) {
        ClientMessages.internalMethod09025((Text)Text.literal((String)string));
    }

    public static void internalMethod09026(String string) {
        if (!NewtonGlobalState.internalField1099) {
            return;
        }
        ClientMessages.internalMethod01809((Text)Text.literal((String)string));
    }
}

