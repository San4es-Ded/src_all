package rockstar.client.internal.config;



import rockstar.client.internal.core.*;
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

public class ResourceJsonHelper {
    private static final ResourceManager internalField0060 = MinecraftClient.getInstance().getResourceManager();
    private static final Gson internalField0931 = new Gson();

    public static Identifier internalMethod03182(String string) {
        return RockstarClient.id("core/" + string);
    }

    public static <T> T internalMethod01844(Identifier identifier, Class<T> clazz) {
        try {
            return (T)internalField0931.fromJson(ResourceJsonHelper.internalMethod01212(ResourceJsonHelper.internalMethod04705(identifier)), clazz);
        }
        catch (Exception exception) {
            throw new RuntimeException("\u041d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u043f\u0440\u043e\u0447\u0438\u0442\u0430\u0442\u044c \u0440\u0435\u0441\u0443\u0440\u0441 " + String.valueOf(identifier) + ": " + exception.getMessage(), exception);
        }
    }

    public static String internalMethod01212(String string) {
        return string;
    }

    public static String internalMethod04705(Identifier identifier) {
        return ResourceJsonHelper.internalMethod02113(identifier, "\n");
    }

    /*
     * Enabled aggressive exception aggregation
     */
    public static String internalMethod02113(Identifier identifier, String string) {
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
