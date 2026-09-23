package rockstar.client.internal.script;





import rockstar.client.ui.*;
import rockstar.client.event.*;
import rockstar.client.animation.*;
import rockstar.client.*;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.Identifier;
import pyrock.events.render.PreHudRenderEvent;
import pyrock.events.window.KeyPressEvent;
import rockstar.client.event.EventListener;
import rockstar.client.RockstarClient;
import rockstar.client.animation.AnimatedValue;
import rockstar.client.animation.Easing;
import rockstar.client.ui.ThemeColors;
import rockstar.client.MinecraftClientAccess;

public class KeyComboAnimationListener
implements MinecraftClientAccess {
    private static final AnimatedValue internalField0808 = new AnimatedValue(1000L, Easing.internalField1814);
    private static boolean internalField0277 = true;
    private static boolean internalField0276 = false;
    private static boolean internalField1099 = false;
    private final EventListener<KeyPressEvent> internalField0157 = keyPressEvent -> {
        int n = keyPressEvent.getKey();
        int n2 = keyPressEvent.getAction();
        if (n == 90) {
            internalField0276 = n2 != 0;
        } else if (n == 86) {
            boolean bl = internalField1099 = n2 != 0;
        }
        if (internalField0276 && internalField1099) {
            this.internalMethod03088();
        }
    };
    public KeyComboAnimationListener() {
        RockstarClient.getInstance().internalMethod03317().internalMethod00647(this);
    }

    private void internalMethod03088() {
        internalField0277 = false;
        internalField0808.internalMethod07059(1.0f);
    }
}

