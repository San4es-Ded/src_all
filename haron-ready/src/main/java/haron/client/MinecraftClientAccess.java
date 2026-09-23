package haron.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;

public interface MinecraftClientAccess {
    public static final MinecraftClient CLIENT = MinecraftClient.getInstance();
    public static final Window WINDOW = CLIENT.getWindow();
    public static final MinecraftClient c = CLIENT;
    public static final Window d = WINDOW;
}

