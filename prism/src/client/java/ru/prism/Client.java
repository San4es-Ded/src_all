package ru.prism;

import net.fabricmc.api.ClientModInitializer;
import ru.prism.command.CommandManager;
import ru.prism.config.ConfigManager;
import ru.prism.friend.FriendManager;
import ru.prism.inventorypreset.InventoryPresetManager;
import ru.prism.manager.GuiManager;
import ru.prism.manager.events.orbit.EventBus;
import ru.prism.manager.rotation.ComponentManager;
import ru.prism.module.api.ModuleManager;
import ru.prism.rpc.RPC;
import ru.prism.screen.Menu;
import ru.prism.utils.render.Render2D;
import ru.prism.utils.render.font.FontInitializer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;


import java.lang.invoke.MethodHandles;

@Getter
@Accessors(fluent = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Client implements ClientModInitializer {

    public static Client get;

    public static Client get() {
        return Client.get;
    }

    @Getter
    private static final EventBus eventHandler = EventBus.threadSafe();

    
    public void start() {
        eventHandler.registerLambdaFactory("", (lookupInMethod,
                                                klass) -> (MethodHandles.Lookup) lookupInMethod.invoke(null, klass, MethodHandles.lookup()));

        FontInitializer.register();
    }

    private ModuleManager moduleManager;
    private Render2D render2D;
    private Menu clickGuiScreen;
    private ComponentManager componentManager;
    private CommandManager commandManager;
    private ConfigManager configManager;
    private FriendManager friendManager;
    private InventoryPresetManager inventoryPresetManager;
    final RPC rpc = new RPC();
    public static String build = "5.0";
    private GuiManager guiManager;

    
    @Override
    public void onInitializeClient() {

        System.out.print("Вход");

        get = this;

        ru.prism.lang.Lang.init();

        start();
        rpc.startRpc();

        this.configManager = new ConfigManager();
        this.configManager.setup();

        this.friendManager = new FriendManager();
        this.friendManager.init();

        this.inventoryPresetManager = new InventoryPresetManager();
        this.inventoryPresetManager.init();

        this.moduleManager = new ModuleManager();
        this.moduleManager.init();

        this.componentManager = new ComponentManager();
        this.componentManager.init();

        this.commandManager = new CommandManager();
        this.commandManager.init();

        this.configManager.init();


        this.guiManager = new GuiManager();
        this.guiManager.init();


        this.render2D = new Render2D();
        this.clickGuiScreen = new Menu();

        Menu.selectedTheme = guiManager.getCurrentTheme();
        Menu.preSelectedTheme = guiManager.getCurrentTheme();

        Runtime.getRuntime().addShutdownHook(new Thread(this::unload));
    }

    public void unload() {
        if (configManager != null) {
            configManager.autoSave();
        }
    }

}
