package ru.haron;

import haron.config.LocalConfigManager;
import haron.gui.core.ClickGuiKeybind;
import haron.gui.core.ClickGuiScreen;
import haron.hud.core.HudServices;
import haron.module.ModuleManager;
import haron.player.HaronPlayerTracker;
import haron.render.icons.HaronIcons;
import haron.render.ShapeRenderer;
import haron.render.shader.DefaultShaders;
import haron.render.ShaderShapeRenderer;
import java.util.concurrent.ThreadFactory;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.gui.screen.Screen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Haron
implements Runnable,
ThreadFactory,
ClientModInitializer {
    public static final String CLIENT_VERSION = "2.0.1";
    private static final String TELEGRAM_BOT_URL = "https://t.me/nativevm";
    private static final String TELEGRAM_CHANNEL_URL = "https://t.me/nativevm";
    public static final String CLIENT_NAME = "haron";
    private static final Logger LOGGER = LogManager.getLogger((String)"haron");
    private static final Haron instance = new Haron();
    private ShapeRenderer render;
    private Screen clickGui;
    private boolean init = false;

    public void onInitializeClient() {
        HaronPlayerTracker.get().init();
        ClickGuiKeybind.register();
        ClientPlayConnectionEvents.DISCONNECT.register((clientPlayNetworkHandler, minecraftClient) -> {
            LOGGER.info("[Haron] Disconnect — saving config");
            LocalConfigManager.get().flushSaveNow("disconnect");
        });
        ClientLifecycleEvents.CLIENT_STOPPING.register(minecraftClient -> {
            LOGGER.info("[Haron] Client stop — saving config");
            LocalConfigManager.get().flushSaveNow("client-stop");
        });
        LOGGER.info("Haron initialized");
    }

    public static String getBackendUrl() {
        return "https://t.me/nativevm";
    }

    public static String getDirectApiUrl() {
        return "https://t.me/nativevm";
    }

    public ShapeRenderer getRender() {
        int n = 110;
        return this.render;
    }

    public static String getMainUrl() {
        return "https://t.me/nativevm";
    }

    public static Logger getLOGGER() {
        return LOGGER;
    }

    public Screen getClickGui() {
        return this.clickGui;
    }

    @Override
    public void run() {
    }

    public static Haron getInstance() {
        return instance;
    }

    public void init() {
        HaronIcons.load();
        DefaultShaders.loadDefaultShaders();
        this.render = new ShaderShapeRenderer();
        HudServices.initialize();
        ModuleManager.init();
        LOGGER.info("[Haron] Loading config (dir: {})", (Object)LocalConfigManager.get().configDirectory());
        try {
            LocalConfigManager.get().init();
            LOGGER.info("[Haron] Config load finished");
        }
        catch (Exception exception) {
            LOGGER.warn("[Haron] Config load failed", (Throwable)exception);
        }
        this.clickGui = new ClickGuiScreen();
        this.init = true;
        LOGGER.info("Haron Java GUI initialized");
    }

    @Override
    public Thread newThread(Runnable runnable) {
        return null;
    }
}
