package rockstar.client.internal.ui;





import rockstar.client.util.*;
import rockstar.client.notification.*;
import rockstar.client.i18n.*;
import rockstar.client.*;
import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import globals.client.Information;
import java.io.File;
import java.nio.file.Files;
import lombok.Generated;
import net.coobird.thumbnailator.Thumbnails;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWDropCallback;
import org.lwjgl.glfw.GLFWDropCallbackI;

public final class ConfigFileDropHandler implements MinecraftClientAccess {
   private static boolean internalField0277;

   public static void internalMethod05313() {
      if (!internalField0277) {
         internalField0277 = true;
         long localValue0 = internalField0149.getWindow().getHandle();
         GLFWDropCallbackI[] localValue2 = new GLFWDropCallbackI[1];
         GLFWDropCallbackI localValue3 = (localValue1, localValue3x, localValue4) -> {
            if (localValue2[0] != null) {
               localValue2[0].invoke(localValue1, localValue3x, localValue4);
            }

            for (int localValue6 = 0; localValue6 < localValue3x; localValue6++) {
               String localValue7 = GLFWDropCallback.getName(localValue4, localValue6);
               internalMethod02049(localValue7);
            }
         };
         localValue2[0] = GLFW.glfwSetDropCallback(localValue0, localValue3);
      }
   }

   private static void internalMethod02049(String localValue0) {
      try {
         File localValue1 = new File(localValue0);
         if (!localValue1.isFile()) {
            return;
         }

         if (localValue1.getName().endsWith(".rock")) {
            JsonElement localValue2 = JsonParser.parseString(Files.readString(localValue1.toPath()));
            if (!localValue2.isJsonObject()) {
               ClientMessages.internalMethod09025(Text.of(LanguageManager.internalMethod07214("config.delete_error")));
               return;
            }

            String localValue3 = localValue1.getName().substring(0, localValue1.getName().lastIndexOf(46));
            RockstarClient.getInstance().internalMethod02152().internalMethod04975(localValue3, localValue2.getAsJsonObject());
            ClientMessages.internalMethod01809(Text.of(LanguageManager.internalMethod00160("config.loaded", localValue3)));
            RockstarClient.getInstance()
               .internalMethod02503()
               .internalMethod04075(NotificationType.internalField0704, Text.translatable("configs.loaded").getString());
         }
      } catch (Exception localValue6) {
         localValue6.printStackTrace();
      }
   }

   @Generated
   private ConfigFileDropHandler() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
