package rockstar.client.internal.script;




import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import pyrock.events.render.HudLayerRenderEvent;
import pyrock.events.render.PostHudLayerRenderEvent;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.RockstarClient;
import rockstar.client.render.UiBatchRenderer;

public final class HudLayerEvents {
    private HudLayerEvents() {
    }

    public static void internalMethod01097(UiRenderContext iII, int n) {
        UiBatchRenderer.internalMethod02576();
        RockstarClient.getInstance().internalMethod03317().internalMethod06883(new HudLayerRenderEvent(iII, iII.internalMethod05258(), n, HudLayerEvents.internalMethod01954()));
        UiBatchRenderer.internalMethod02576();
    }

    public static void internalMethod02900(UiRenderContext iII, int n) {
        UiBatchRenderer.internalMethod02576();
        RockstarClient.getInstance().internalMethod03317().internalMethod06883(new PostHudLayerRenderEvent(iII, iII.internalMethod05258(), n, HudLayerEvents.internalMethod01954()));
        UiBatchRenderer.internalMethod02576();
    }

    private static boolean internalMethod01954() {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        return minecraftClient != null && minecraftClient.currentScreen instanceof ChatScreen;
    }
}

