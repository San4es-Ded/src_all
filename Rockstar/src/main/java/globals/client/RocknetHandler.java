package globals.client;





import rockstar.client.ui.*;
import rockstar.client.server.*;
import rockstar.client.event.*;
import rockstar.client.internal.game.*;
import globals.client.Activities;
import globals.client.Information;
import globals.client.WorldKey;
import globals.client.ui.RocknetMenu;
import globals.shared.proto.Packets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.minecraft.client.MinecraftClient;
import pyrock.events.game.GameTickEvent;
import pyrock.events.game.WorldChangeEvent;
import pyrock.events.render.HudRenderEvent;
import rockstar.client.ui.UiRenderContext;
import rockstar.modules.other.GlobalsMenuModule;
import rockstar.client.event.EventListener;
import rockstar.client.RockstarClient;
import rockstar.client.internal.game.EntityPositionTracker;
import rockstar.client.server.ServerUtils;
import rockstar.client.MinecraftClientAccess;
import rockstar.profile.Profile;

public class RocknetHandler
implements MinecraftClientAccess {
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private String lastVisibility = "all";
    private static volatile String lastJoinedServer;
    private static volatile long lastOnServerAt;
    private static volatile String lastWorldStamp;
    private final EventListener<HudRenderEvent> handleGlobals = hudRenderEvent -> {
        RocknetMenu rocknetMenu = RockstarClient.getInstance().internalMethod01773();
        if (rocknetMenu == null) {
            RockstarClient.getInstance().internalMethod06176(new RocknetMenu());
            return;
        }
        rocknetMenu.getMenuAnimation().internalMethod07059(rocknetMenu.isClosing() ? 0.0f : 1.0f);
        if (!(RocknetHandler.internalField0149.currentScreen instanceof RocknetMenu)) {
            RockstarClient.getInstance().getModuleManager().getModule(GlobalsMenuModule.class).disable();
        }
        if (rocknetMenu.getMenuAnimation().internalMethod02881() > 0.1f && !(RocknetHandler.internalField0149.currentScreen instanceof RocknetMenu) && rocknetMenu.isClosing()) {
            UiRenderContext iII = UiRenderContext.internalMethod02316(hudRenderEvent.getContext(), -1, -1, MinecraftClient.getInstance().getRenderTickCounter().getTickProgress(false));
            rocknetMenu.render(iII);
        }
        this.checkVisibilityChange();
    };
    private final EventListener<WorldChangeEvent> worldChange = worldChangeEvent -> {
        EntityPositionTracker.internalMethod07263();
        RocknetHandler.send();
    };
    private final EventListener<GameTickEvent> worldStamp = gameTickEvent -> {
        if (RocknetHandler.internalField0149.player == null || RocknetHandler.internalField0149.player.age % 20 != 0) {
            return;
        }
        if (!internalField0149.isInSingleplayer()) {
            lastOnServerAt = System.currentTimeMillis();
        }
        if (!WorldKey.local().stamp().equals(lastWorldStamp)) {
            RocknetHandler.send();
        }
    };
    public RocknetHandler() {
        RockstarClient.getInstance().internalMethod03317().internalMethod00647(this);
    }

    private String getVisibilityValue(GlobalsMenuModule typedValue184) {
        if (typedValue184 == null || typedValue184.internalMethod06435() == null) {
            return "all";
        }
        if (typedValue184.internalMethod06435().internalMethod06103(typedValue184.internalMethod02794())) {
            return "all";
        }
        if (typedValue184.internalMethod06435().internalMethod06103(typedValue184.internalMethod02984())) {
            return "friends";
        }
        if (typedValue184.internalMethod06435().internalMethod06103(typedValue184.internalMethod08860())) {
            return "none";
        }
        return "all";
    }

    private void checkVisibilityChange() {
        GlobalsMenuModule typedValue184 = RockstarClient.getInstance().getModuleManager().getModule(GlobalsMenuModule.class);
        if (typedValue184 == null) {
            return;
        }
        String string = this.getVisibilityValue(typedValue184);
        if (!string.equals(this.lastVisibility)) {
            this.lastVisibility = string;
            RockstarClient.getInstance().internalMethod06050().updateVisibility(string);
        }
    }

    public static void send() {
        executor.submit(() -> {
            String string;
            WorldKey worldKey = WorldKey.local();
            lastWorldStamp = worldKey.stamp();
            GlobalsMenuModule typedValue184 = RockstarClient.getInstance().getModuleManager().getModule(GlobalsMenuModule.class);
            String string2 = "all";
            if (typedValue184 != null) {
                if (typedValue184.internalMethod06435().internalMethod06103(typedValue184.internalMethod02794())) {
                    string2 = "all";
                } else if (typedValue184.internalMethod06435().internalMethod06103(typedValue184.internalMethod02984())) {
                    string2 = "friends";
                } else if (typedValue184.internalMethod06435().internalMethod06103(typedValue184.internalMethod08860())) {
                    string2 = "none";
                }
            }
            RockstarClient.getInstance().internalMethod06050().info(internalField0149.getSession().getUsername(), String.valueOf(worldKey.anarchy()), worldKey.server(), ServerUtils.internalMethod05631(), worldKey.world(), string2, Profile.getUsername());
            String string3 = ServerUtils.internalMethod00929();
            if (!internalField0149.isInSingleplayer() && string3 != null && !string3.equals("single")) {
                boolean bl;
                string = ServerUtils.internalMethod06458(false);
                boolean bl2 = bl = System.currentTimeMillis() - lastOnServerAt > 60000L;
                if (!(string == null || string.isBlank() || !bl && string.equals(lastJoinedServer))) {
                    lastJoinedServer = string;
                    lastOnServerAt = System.currentTimeMillis();
                    RockstarClient.getInstance().internalMethod06050().send(new Packets.InternalType0060(string, ServerUtils.internalMethod05631(), string3, internalField0149.getSession().getUsername(), "Rockstar".toLowerCase()));
                }
            } else {
                lastJoinedServer = null;
            }
            string = "main_menu";
            if (RocknetHandler.internalField0149.world != null) {
                string = internalField0149.isInSingleplayer() ? "singleplayer" : Activities.multiplayer(ServerUtils.internalMethod06458(false));
            }
            RockstarClient.getInstance().internalMethod06050().update(string);
        });
    }

    static {
        lastWorldStamp = "";
    }
}
