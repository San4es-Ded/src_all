package wtf.wyvern;

import java.io.File;
import lombok.Generated;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import wtf.wyvern.core.command.CommandManager;
import wtf.wyvern.core.config.ConfigManager;
import wtf.wyvern.core.discord.DiscordManager;
import wtf.wyvern.core.filemanager.impl.FriendManager;
import wtf.wyvern.core.filemanager.impl.StaffManager;
import wtf.wyvern.core.filemanager.impl.TargetManager;
import wtf.wyvern.core.macro.MacroManager;
import wtf.wyvern.core.modules.ModuleManager;
import wtf.wyvern.core.notify.NotifyManager;
import wtf.wyvern.core.neuro.NeuroManager;
import wtf.wyvern.core.repository.RCTRepository;
import wtf.wyvern.core.request.ScriptManager;
import wtf.wyvern.core.theme.ThemeManager;
import wtf.wyvern.core.waypoint.WaypointManager;
import wtf.wyvern.client.ui.interfaces.ToggleNotify;
import wtf.wyvern.client.gui.screens.menu.MenuScreen;
import wtf.wyvern.utility.game.server.ServerHandler;
import wtf.wyvern.render.display.shader.DrawUtil;
import wtf.wyvern.render.display.shader.GlProgram;
import wtf.wyvern.integration.figura.FiguraAvatarLibrary;

public enum Wyvern implements ClientModInitializer {
    INSTANCE;

    public static final String NAME = "Wyvern";
    public static final String VER = "1.21.4";
    public static final String TYPE = "Developer";
    private static final String MOD_ID = "Wyvern".toLowerCase();
    public static File DIRECTORY;
    private ModuleManager moduleManager;
    private ThemeManager themeManager;
    private MenuScreen menuScreen;
    private ScriptManager scriptManager;
    private ServerHandler serverHandler;
    private FriendManager friendManager;
    private TargetManager targetManager;
    private MacroManager macroManager;
    private StaffManager staffManager;
    private WaypointManager waypointManager;
    private NotifyManager notifyManager;
    private CommandManager commandManager;
    private ConfigManager configManager;
    private RCTRepository rctRepository;
    private DiscordManager discordManager;
    private ToggleNotify toggleNotify;
    private NeuroManager neuroManager;
    private boolean initialized = false;

    @Override
    public void onInitializeClient() {
        try {
            init();
        } catch (Exception e) {

            throw e;
        }
    }

    public void init() {
        if (initialized) {
            return;
        }
        initialized = true;
        try {
            // In-game license heartbeat (AstroGuard). Стартуем первым делом, до
            // остальной инициализации: поток фоновый, гасит клиент при подписанном
            // отзыве лицензии. Слушатель тика дёргает guard() ниже.
            wtf.wyvern.security.AstroGuardAuth.boot();

            DIRECTORY = new File(MinecraftClient.getInstance().runDirectory, "Wyvern");
            if (!DIRECTORY.exists()) {
                DIRECTORY.mkdirs();
            }

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                getInstance().shutdown();
            }));

            this.friendManager = new FriendManager();
            this.targetManager = new TargetManager();
            this.macroManager = new MacroManager();
            this.staffManager = new StaffManager();
            this.notifyManager = new NotifyManager();
            this.serverHandler = new ServerHandler();
            this.rctRepository = new RCTRepository();
            this.themeManager = new ThemeManager();
            this.neuroManager = new NeuroManager();
            this.moduleManager = new ModuleManager();
            this.configManager = new ConfigManager();
            this.commandManager = new CommandManager();
            this.scriptManager = new ScriptManager();
            try {
                this.discordManager = new DiscordManager();
            } catch (Throwable e) {
                this.discordManager = null;
            }
            this.toggleNotify = new ToggleNotify();
            this.waypointManager = new WaypointManager();
            // Регистрируем per-tick мостик к AstroGuardAuth.guard() в шине событий.
            new wtf.wyvern.security.AstroGuardGuardListener();
            this.menuScreen = new MenuScreen();
            // Install only the lightweight local index on startup. Figura models
            // are loaded lazily by the browser instead of all at once.
            FiguraAvatarLibrary.ensureInstalled();
            ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
                @Override
                public Identifier getFabricId() {
                    return Wyvern.id("after_shader_load");
                }

                @Override
                public void reload(ResourceManager manager) {
                    GlProgram.loadAndSetupPrograms();
                }
            });
            DrawUtil.initializeShaders();
            wtf.wyvern.render.shader.HandsRenderer.initializeShaders();
            wtf.wyvern.render.shader.AmbienceRenderer.initializeShaders();
            wtf.wyvern.render.shader.MotionBlurRenderer.initializeShaders();
        } catch (Exception e) {

            throw new RuntimeException("Wyvern initialization failed", e);
        }
    }

    public void shutdown() {
        this.friendManager.save();
        this.targetManager.save();
        this.staffManager.save();
        this.configManager.save();
        this.macroManager.save();
        if (this.waypointManager != null) {
            this.waypointManager.save();
        }
        if (this.discordManager != null) {
            this.discordManager.stopRPC();
        }

    }

    public static Identifier id(String path) {
        return Identifier.of("wyvern", path);
    }

    public static Wyvern getInstance() {
        return INSTANCE;
    }

    public RCTRepository getRCTRepository() {
        return this.rctRepository;
    }

    @Generated
    public ModuleManager getModuleManager() {
        return this.moduleManager;
    }

    @Generated
    public ThemeManager getThemeManager() {
        return this.themeManager;
    }

    @Generated
    public MenuScreen getMenuScreen() {
        return this.menuScreen;
    }

    @Generated
    public ScriptManager getScriptManager() {
        return this.scriptManager;
    }

    @Generated
    public ServerHandler getServerHandler() {
        return this.serverHandler;
    }

    @Generated
    public FriendManager getFriendManager() {
        return this.friendManager;
    }

    @Generated
    public TargetManager getTargetManager() {
        return this.targetManager;
    }

    @Generated
    public MacroManager getMacroManager() {
        return this.macroManager;
    }

    @Generated
    public StaffManager getStaffManager() {
        return this.staffManager;
    }

    @Generated
    public WaypointManager getWaypointManager() {
        return this.waypointManager;
    }

    @Generated
    public NotifyManager getNotifyManager() {
        return this.notifyManager;
    }

    @Generated
    public CommandManager getCommandManager() {
        return this.commandManager;
    }

    @Generated
    public ConfigManager getConfigManager() {
        return this.configManager;
    }

    @Generated
    public DiscordManager getDiscordManager() {
        return this.discordManager;
    }

    @Generated
    public ToggleNotify getToggleNotify() {
        return this.toggleNotify;
    }

    @Generated
    public NeuroManager getNeuroManager() {
        return this.neuroManager;
    }

    private static Wyvern[] $values() {
        return new Wyvern[]{INSTANCE};
    }
}
