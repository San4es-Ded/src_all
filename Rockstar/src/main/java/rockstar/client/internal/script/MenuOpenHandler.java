package rockstar.client.internal.script;





import rockstar.client.ui.*;
import rockstar.client.event.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import net.minecraft.client.MinecraftClient;
import pyrock.events.render.HudRenderEvent;
import rockstar.client.ui.UiRenderContext;
import rockstar.modules.visual.MenuModule;
import rockstar.client.event.EventListener;
import rockstar.client.RockstarClient;
import rockstar.client.internal.core.AbstractMenuScreen;
import rockstar.client.internal.script.ModernMenuScreen;
import rockstar.client.internal.script.WorldPanelScreen;
import rockstar.client.MinecraftClientAccess;

public class MenuOpenHandler
implements MinecraftClientAccess {
    private final EventListener<HudRenderEvent> internalField0157 = hudRenderEvent -> {
        boolean bl;
        AbstractMenuScreen typedValue204 = RockstarClient.getInstance().internalMethod04334();
        if (MenuOpenHandler.internalField0149.currentScreen == null && RockstarClient.getInstance().getModuleManager().getModule(MenuModule.class).internalMethod01968().isSelected() && !(typedValue204 instanceof ModernMenuScreen)) {
            RockstarClient.getInstance().internalMethod02331(new ModernMenuScreen());
        }
        boolean bl2 = bl = MenuOpenHandler.internalField0149.currentScreen instanceof AbstractMenuScreen || MenuOpenHandler.internalField0149.currentScreen instanceof WorldPanelScreen;
        if (!bl && RockstarClient.getInstance().getModuleManager().getModule(MenuModule.class).isEnabled()) {
            RockstarClient.getInstance().getModuleManager().getModule(MenuModule.class).internalMethod05084(false);
        }
        if (typedValue204 == null) {
            return;
        }
        typedValue204.getMenuAnimation().internalMethod07059(typedValue204.isClosing() ? 0.0f : 1.0f);
        if (!(typedValue204 instanceof ModernMenuScreen) && typedValue204.getMenuAnimation().internalMethod02881() > 0.1f && !(MenuOpenHandler.internalField0149.currentScreen instanceof AbstractMenuScreen) && typedValue204.isClosing()) {
            UiRenderContext iII = UiRenderContext.internalMethod02316(hudRenderEvent.getContext(), -1, -1, MinecraftClient.getInstance().getRenderTickCounter().getTickProgress(false));
            typedValue204.render(iII);
        }
    };

    public MenuOpenHandler() {
        RockstarClient.getInstance().internalMethod03317().internalMethod00647(this);
    }
}
