package rockstar.client.internal.config;


import rockstar.client.*;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import rockstar.client.RockstarClient;

public final class CoreResourceReader {
    private static final ResourceManager internalField0060 = MinecraftClient.getInstance().getResourceManager();
    private static final Gson internalField0931 = new Gson();

    public static Identifier internalMethod07486(String string) {
        return RockstarClient.id("core/" + string);
    }

    public static <T> T internalMethod07456(Identifier identifier, Class<T> clazz) {
        return (T)internalField0931.fromJson(CoreResourceReader.internalMethod00816(identifier), clazz);
    }

    public static String internalMethod00816(Identifier identifier) {
        return CoreResourceReader.internalMethod06438(identifier, "\n");
    }

    /*
     * Enabled aggressive exception aggregation
     */
    public static String internalMethod06438(Identifier identifier, String string) {
        try (InputStream inputStream = internalField0060.open(identifier);){
            String string2;
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));){
                string2 = bufferedReader.lines().collect(Collectors.joining(string));
            }
            return string2;
        }
        catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
    }
}

