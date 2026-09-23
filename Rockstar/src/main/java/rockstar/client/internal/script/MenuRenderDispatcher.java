package rockstar.client.internal.script;





import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import net.minecraft.client.MinecraftClient;
import pyrock.events.render.MenuRenderEvent;
import pyrock.events.render.PostMenuRenderEvent;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.RockstarClient;
import rockstar.client.internal.core.MenuLayoutProvider;
import rockstar.client.render.UiBatchRenderer;

public final class MenuRenderDispatcher {
    private MenuRenderDispatcher() {
    }

    public static void internalMethod05609(MenuLayoutProvider typedValue208, UiRenderContext iII) {
        UiBatchRenderer.internalMethod02576();
        RockstarClient.getInstance().internalMethod03317().internalMethod06883(new MenuRenderEvent(iII, iII.internalMethod05258(), typedValue208.internalMethod03999(), typedValue208.internalMethod08297(), MenuRenderDispatcher.internalMethod04666(typedValue208)));
        UiBatchRenderer.internalMethod02576();
    }

    public static void internalMethod03319(MenuLayoutProvider typedValue208, UiRenderContext iII) {
        UiBatchRenderer.internalMethod02576();
        RockstarClient.getInstance().internalMethod03317().internalMethod06883(new PostMenuRenderEvent(iII, iII.internalMethod05258(), typedValue208.internalMethod03999(), typedValue208.internalMethod08297(), MenuRenderDispatcher.internalMethod04666(typedValue208)));
        UiBatchRenderer.internalMethod02576();
    }

    private static boolean internalMethod04666(MenuLayoutProvider typedValue208) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        return typedValue208.internalMethod04390() && (minecraftClient == null || minecraftClient.currentScreen == null);
    }
}

