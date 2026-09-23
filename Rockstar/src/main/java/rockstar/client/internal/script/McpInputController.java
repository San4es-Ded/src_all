package rockstar.client.internal.script;




import rockstar.client.internal.ui.*;
import rockstar.client.internal.network.*;
import rockstar.client.*;
import com.google.gson.JsonObject;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.input.MouseInput;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.client.util.InputUtil.Key;
import net.minecraft.client.util.InputUtil.Type;
import org.lwjgl.glfw.GLFW;
import pyrock.events.window.CharTypedEvent;
import pyrock.events.window.KeyEvent;
import pyrock.events.window.KeyPressEvent;
import pyrock.events.window.MouseButtonEvent;
import pyrock.events.window.MouseEvent;
import pyrock.events.window.MouseScrollEvent;
import pyrock.events.window.ScrollEvent;

public final class McpInputController {
   private static final ScheduledExecutorService internalField0220 = Executors.newSingleThreadScheduledExecutor(localValue0 -> {
      Thread localValue1 = new Thread(localValue0, "Rockstar-MCP-Input");
      localValue1.setDaemon(true);
      return localValue1;
   });
   private static final ConcurrentLinkedQueue<CompletableFuture<NativeImage>> internalField0920 = new ConcurrentLinkedQueue<>();
   private static volatile int internalField0227;
   private static volatile int internalField0228;

   private McpInputController() {
   }

   public static void internalMethod03757() {
      CompletableFuture localValue0;
      while ((localValue0 = internalField0920.poll()) != null) {
         try {
            CompletableFuture<NativeImage> localValue1 = localValue0;
            ScreenshotRecorder.takeScreenshot(MinecraftClientAccess.internalField0149.getFramebuffer(), localValue1::complete);
         } catch (Throwable localValue2) {
            localValue0.completeExceptionally(localValue2);
         }
      }
   }

   public static McpInputController.InternalType0428 internalMethod01136(int localValue0, String localValue1, float localValue2) {
      CompletableFuture localValue3 = new CompletableFuture();
      internalField0920.add(localValue3);

      NativeImage localValue4;
      try {
         localValue4 = (NativeImage)localValue3.get(5L, TimeUnit.SECONDS);
      } catch (Exception localValue14) {
         internalField0920.remove(localValue3);
         throw new McpHttpServer.InternalType0086(
            -32603,
            "\u0438\u0433\u0440\u0430 \u043d\u0435 \u043e\u0442\u0434\u0430\u043b\u0430 \u043a\u0430\u0434\u0440 \u2014 \u043e\u043a\u043d\u043e \u0441\u0432\u0451\u0440\u043d\u0443\u0442\u043e \u0438\u043b\u0438 \u0438\u0433\u0440\u0430 \u043d\u0435 \u0440\u0438\u0441\u0443\u0435\u0442"
         );
      }

      McpInputController.InternalType0428 localValue19;
      try {
         int localValue5 = localValue4.getWidth();
         int localValue6 = localValue4.getHeight();
         BufferedImage localValue7 = new BufferedImage(localValue5, localValue6, 1);

         for (int localValue8 = 0; localValue8 < localValue6; localValue8++) {
            for (int localValue9 = 0; localValue9 < localValue5; localValue9++) {
               localValue7.setRGB(localValue9, localValue8, localValue4.getColorArgb(localValue9, localValue8));
            }
         }

         BufferedImage localValue16 = localValue7;
         if (localValue0 > 0 && localValue5 > localValue0) {
            int localValue17 = Math.max(1, Math.round(localValue6 * ((float)localValue0 / localValue5)));
            localValue16 = new BufferedImage(localValue0, localValue17, 1);
            Graphics2D localValue10 = localValue16.createGraphics();
            localValue10.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            localValue10.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            localValue10.drawImage(localValue7, 0, 0, localValue0, localValue17, null);
            localValue10.dispose();
         }

         internalField0227 = localValue16.getWidth();
         internalField0228 = localValue16.getHeight();
         boolean localValue18 = "png".equalsIgnoreCase(localValue1);
         localValue19 = new McpInputController.InternalType0428(
            localValue18 ? internalMethod06800(localValue16) : internalMethod05323(localValue16, localValue2),
            localValue18 ? "image/png" : "image/jpeg",
            localValue16.getWidth(),
            localValue16.getHeight(),
            localValue5,
            localValue6
         );
      } finally {
         localValue4.close();
      }

      return localValue19;
   }

   private static byte[] internalMethod06800(BufferedImage localValue0) {
      try {
         ByteArrayOutputStream localValue1 = new ByteArrayOutputStream(262144);
         if (!ImageIO.write(localValue0, "png", localValue1)) {
            throw new McpHttpServer.InternalType0086(-32603, "\u043d\u0435\u0442 PNG-\u043a\u043e\u0434\u0438\u0440\u043e\u0432\u0449\u0438\u043a\u0430");
         } else {
            return localValue1.toByteArray();
         }
      } catch (Exception localValue2) {
         throw new McpHttpServer.InternalType0086(
            -32603, "\u043d\u0435 \u0437\u0430\u043a\u043e\u0434\u0438\u0440\u043e\u0432\u0430\u043b PNG: " + localValue2.getMessage()
         );
      }
   }

   private static byte[] internalMethod05323(BufferedImage localValue0, float localValue1) {
      Iterator localValue2 = ImageIO.getImageWritersByFormatName("jpeg");
      if (!localValue2.hasNext()) {
         return internalMethod06800(localValue0);
      } else {
         ImageWriter localValue3 = (ImageWriter)localValue2.next();

         byte[] localValue18;
         try {
            ByteArrayOutputStream localValue4 = new ByteArrayOutputStream(131072);
            ImageWriteParam localValue5 = localValue3.getDefaultWriteParam();
            if (localValue5.canWriteCompressed()) {
               localValue5.setCompressionMode(2);
               localValue5.setCompressionQuality(Math.max(0.1F, Math.min(1.0F, localValue1)));
            }

            try (MemoryCacheImageOutputStream localValue6 = new MemoryCacheImageOutputStream(localValue4)) {
               localValue3.setOutput(localValue6);
               localValue3.write(null, new IIOImage(localValue0, null, null), localValue5);
            }

            localValue18 = localValue4.toByteArray();
         } catch (Exception localValue16) {
            throw new McpHttpServer.InternalType0086(
               -32603, "\u043d\u0435 \u0437\u0430\u043a\u043e\u0434\u0438\u0440\u043e\u0432\u0430\u043b JPEG: " + localValue16.getMessage()
            );
         } finally {
            localValue3.dispose();
         }

         return localValue18;
      }
   }

   public static JsonObject internalMethod00871(
      String localValue0, double localValue1, double localValue3, String localValue5, int localValue6, double localValue7, int localValue9, Double localValue10, Double localValue11, String localValue12
   ) {
      JsonObject localValue13 = new JsonObject();
      int localValue14 = internalMethod00001(localValue5);
      double[] localValue15 = internalMethod05525(localValue1, localValue3, localValue12);
      localValue13.addProperty("guiX", internalMethod06366(localValue15[0]));
      localValue13.addProperty("guiY", internalMethod06366(localValue15[1]));
      String localValue16 = localValue0 == null ? "click" : localValue0.toLowerCase(Locale.ROOT);
      switch (localValue16) {
         case "move":
            internalMethod05134(localValue13, internalMethod04704(localValue15[0], localValue15[1]));
            localValue13.addProperty("done", "\u043a\u0443\u0440\u0441\u043e\u0440 \u043f\u0435\u0440\u0435\u0434\u0432\u0438\u043d\u0443\u0442");
            break;
         case "click":
            internalMethod05134(localValue13, internalMethod04704(localValue15[0], localValue15[1]));

            for (int localValue19 = 0; localValue19 < Math.max(1, localValue6); localValue19++) {
               internalMethod02472(localValue15[0], localValue15[1], localValue14, localValue9);
            }

            localValue13.addProperty("done", "\u043a\u043b\u0438\u043a " + internalMethod05070(localValue14) + " \u00d7" + Math.max(1, localValue6));
            break;
         case "drag":
            if (localValue10 == null || localValue11 == null) {
               throw new McpHttpServer.InternalType0086("\u0434\u043b\u044f drag \u043d\u0443\u0436\u043d\u044b to_x \u0438 to_y");
            }

            double[] localValue18 = internalMethod05525(localValue10, localValue11, localValue12);
            internalMethod00461(localValue15[0], localValue15[1], localValue18[0], localValue18[1], localValue14);
            localValue13.addProperty(
               "done", "\u043f\u0435\u0440\u0435\u0442\u0430\u0449\u0435\u043d\u043e \u0432 " + internalMethod06366(localValue18[0]) + ", " + internalMethod06366(localValue18[1])
            );
            break;
         case "scroll":
            internalMethod03833(localValue15[0], localValue15[1], localValue7);
            localValue13.addProperty("done", "\u043f\u0440\u043e\u043a\u0440\u0443\u0442\u043a\u0430 " + localValue7);
            break;
         default:
            throw new McpHttpServer.InternalType0086(
               "\u0434\u0435\u0439\u0441\u0442\u0432\u0438\u0435 \u043c\u044b\u0448\u0438 \u0431\u044b\u0432\u0430\u0435\u0442 move, click, drag, scroll"
            );
      }

      localValue13.add("screen", GameStateSnapshotter.internalMethod08175());
      return localValue13;
   }

   private static void internalMethod05134(JsonObject localValue0, boolean localValue1) {
      localValue0.addProperty("cursorMoved", localValue1);
      if (!localValue1) {
         localValue0.addProperty(
            "cursorNote",
            "\u043e\u043a\u043d\u043e \u0438\u0433\u0440\u044b \u043d\u0435 \u0432 \u0444\u043e\u043a\u0443\u0441\u0435 \u0438\u043b\u0438 \u043a\u0443\u0440\u0441\u043e\u0440 \u0437\u0430\u0445\u0432\u0430\u0447\u0435\u043d: \u043a\u043b\u0438\u043a \u0434\u043e\u0441\u0442\u0430\u0432\u043b\u0435\u043d \u043d\u0430\u043f\u0440\u044f\u043c\u0443\u044e, \u043d\u043e \u043d\u0430\u0432\u0435\u0434\u0435\u043d\u0438\u0435 (\u043f\u043e\u0434\u0441\u0432\u0435\u0442\u043a\u0430 \u043f\u043e\u0434 \u043a\u0443\u0440\u0441\u043e\u0440\u043e\u043c) \u043d\u0435 \u0441\u0440\u0430\u0431\u043e\u0442\u0430\u0435\u0442"
         );
      }
   }

   private static void internalMethod02472(double localValue0, double localValue2, int localValue4, int localValue5) {
      Screen localValue6 = MinecraftClientAccess.internalField0149.currentScreen;
      RockstarClient.getInstance().internalMethod03317().internalMethod06883(new MouseEvent(localValue4, 1));
      RockstarClient.getInstance().internalMethod03317().internalMethod06883(new MouseButtonEvent(localValue4, 1, 0));
      if (localValue6 != null) {
         localValue6.mouseMoved(localValue0, localValue2);
         Click localValue8 = new Click(localValue0, localValue2, new MouseInput(localValue4, 0));
         localValue6.mouseClicked(localValue8, false);
         if (localValue5 > 0) {
            internalMethod06250(localValue5, () -> localValue6.mouseReleased(localValue8));
         } else {
            localValue6.mouseReleased(localValue8);
         }

         RockstarClient.getInstance().internalMethod03317().internalMethod06883(new MouseButtonEvent(localValue4, 0, 0));
      } else {
         Key localValue7 = Type.MOUSE.createFromCode(localValue4);
         KeyBinding.setKeyPressed(localValue7, true);
         KeyBinding.onKeyPressed(localValue7);
         if (localValue5 > 0) {
            internalMethod06250(localValue5, () -> {
               KeyBinding.setKeyPressed(localValue7, false);
               RockstarClient.getInstance().internalMethod03317().internalMethod06883(new MouseButtonEvent(localValue4, 0, 0));
            });
         } else {
            KeyBinding.setKeyPressed(localValue7, false);
            RockstarClient.getInstance().internalMethod03317().internalMethod06883(new MouseButtonEvent(localValue4, 0, 0));
         }
      }
   }

   private static void internalMethod00461(double localValue0, double localValue2, double localValue4, double localValue6, int localValue8) {
      Screen localValue9 = MinecraftClientAccess.internalField0149.currentScreen;
      if (localValue9 == null) {
         throw new McpHttpServer.InternalType0086(
            "\u043f\u0435\u0440\u0435\u0442\u0430\u0441\u043a\u0438\u0432\u0430\u0442\u044c \u043d\u0435\u0447\u0435\u0433\u043e: \u043e\u0442\u043a\u0440\u044b\u0442\u043e\u0433\u043e \u044d\u043a\u0440\u0430\u043d\u0430 \u043d\u0435\u0442"
         );
      } else {
         internalMethod04704(localValue0, localValue2);
         localValue9.mouseMoved(localValue0, localValue2);
         localValue9.mouseClicked(new Click(localValue0, localValue2, new MouseInput(localValue8, 0)), false);
         byte localValue10 = 8;

         for (int localValue11 = 1; localValue11 <= localValue10; localValue11++) {
            double localValue12 = localValue0 + (localValue4 - localValue0) * localValue11 / localValue10;
            double localValue14 = localValue2 + (localValue6 - localValue2) * localValue11 / localValue10;
            localValue9.mouseDragged(new Click(localValue12, localValue14, new MouseInput(localValue8, 0)), localValue12 - localValue0, localValue14 - localValue2);
            localValue9.mouseMoved(localValue12, localValue14);
         }

         internalMethod04704(localValue4, localValue6);
         localValue9.mouseReleased(new Click(localValue4, localValue6, new MouseInput(localValue8, 0)));
      }
   }

   private static void internalMethod03833(double localValue0, double localValue2, double localValue4) {
      RockstarClient.getInstance().internalMethod03317().internalMethod06883(new MouseScrollEvent(localValue4));
      RockstarClient.getInstance().internalMethod03317().internalMethod06883(new ScrollEvent(0.0, localValue4));
      if (MinecraftClientAccess.internalField0149.currentScreen != null) {
         MinecraftClientAccess.internalField0149.currentScreen.mouseScrolled(localValue0, localValue2, 0.0, localValue4);
      }
   }

   private static boolean internalMethod04704(double localValue0, double localValue2) {
      if (MinecraftClientAccess.internalField0149.mouse.isCursorLocked()) {
         return false;
      } else if (GLFW.glfwGetWindowAttrib(MinecraftClientAccess.internalField0149.getWindow().getHandle(), 131073) != 1) {
         return false;
      } else {
         double localValue4 = localValue0 * MinecraftClientAccess.internalField0149.getWindow().getWidth() / MinecraftClientAccess.internalField0149.getWindow().getScaledWidth();
         double localValue6 = localValue2 * MinecraftClientAccess.internalField0149.getWindow().getHeight() / MinecraftClientAccess.internalField0149.getWindow().getScaledHeight();
         GLFW.glfwSetCursorPos(MinecraftClientAccess.internalField0149.getWindow().getHandle(), localValue4, localValue6);
         if (MinecraftClientAccess.internalField0149.currentScreen != null) {
            MinecraftClientAccess.internalField0149.currentScreen.mouseMoved(localValue0, localValue2);
         }

         return true;
      }
   }

   public static JsonObject internalMethod00576(String localValue0, String localValue1, String localValue2, List<String> localValue3, int localValue4) {
      JsonObject localValue5 = new JsonObject();
      String localValue6 = localValue0 == null ? "press" : localValue0.toLowerCase(Locale.ROOT);
      switch (localValue6) {
         case "press":
            int localValue8 = KeybindParser.internalMethod06543(localValue1);
            if (localValue8 == -1) {
               throw new McpHttpServer.InternalType0086("\u043d\u0435 \u0437\u043d\u0430\u044e \u043a\u043b\u0430\u0432\u0438\u0448\u0443 \"" + localValue1 + "\"");
            }

            internalMethod03843(localValue8, internalMethod01891(localValue3), localValue4);
            localValue5.addProperty("done", "\u043d\u0430\u0436\u0430\u0442\u0430 " + localValue1);
            break;
         case "type":
            if (localValue2 == null || localValue2.isEmpty()) {
               throw new McpHttpServer.InternalType0086("\u043d\u0435\u0447\u0435\u0433\u043e \u043f\u0435\u0447\u0430\u0442\u0430\u0442\u044c");
            }

            internalMethod00002(localValue2);
            localValue5.addProperty(
               "done", "\u043d\u0430\u043f\u0435\u0447\u0430\u0442\u0430\u043d\u043e " + localValue2.length() + " \u0441\u0438\u043c\u0432\u043e\u043b\u043e\u0432"
            );
            break;
         default:
            throw new McpHttpServer.InternalType0086(
               "\u0434\u0435\u0439\u0441\u0442\u0432\u0438\u0435 \u043a\u043b\u0430\u0432\u0438\u0430\u0442\u0443\u0440\u044b \u0431\u044b\u0432\u0430\u0435\u0442 press \u0438\u043b\u0438 type"
            );
      }

      localValue5.add("screen", GameStateSnapshotter.internalMethod08175());
      return localValue5;
   }

   private static void internalMethod03843(int localValue0, int localValue1, int localValue2) {
      RockstarClient.getInstance().internalMethod03317().internalMethod06883(new KeyPressEvent(1, localValue0));
      RockstarClient.getInstance().internalMethod03317().internalMethod06883(new KeyEvent(localValue0, 0, 1, localValue1));
      Screen localValue3 = MinecraftClientAccess.internalField0149.currentScreen;
      if (localValue3 != null) {
         KeyInput localValue7 = new KeyInput(localValue0, 0, localValue1);
         localValue3.keyPressed(localValue7);
         Runnable localValue6 = () -> {
            localValue3.keyReleased(localValue7);
            RockstarClient.getInstance().internalMethod03317().internalMethod06883(new KeyPressEvent(0, localValue0));
         };
         if (localValue2 > 0) {
            internalMethod06250(localValue2, localValue6);
         } else {
            localValue6.run();
         }
      } else {
         Key localValue4 = InputUtil.fromKeyCode(new KeyInput(localValue0, 0, localValue1));
         KeyBinding.setKeyPressed(localValue4, true);
         KeyBinding.onKeyPressed(localValue4);
         Runnable localValue5 = () -> {
            KeyBinding.setKeyPressed(localValue4, false);
            RockstarClient.getInstance().internalMethod03317().internalMethod06883(new KeyPressEvent(0, localValue0));
         };
         if (localValue2 > 0) {
            internalMethod06250(localValue2, localValue5);
         } else {
            localValue5.run();
         }
      }
   }

   private static void internalMethod00002(String localValue0) {
      Screen localValue1 = MinecraftClientAccess.internalField0149.currentScreen;

      for (int localValue2 = 0; localValue2 < localValue0.length(); localValue2++) {
         char localValue3 = localValue0.charAt(localValue2);
         RockstarClient.getInstance().internalMethod03317().internalMethod06883(new CharTypedEvent(localValue3, 0));
         if (localValue1 != null) {
            localValue1.charTyped(new CharInput(localValue3, 0));
         }
      }
   }

   private static double[] internalMethod05525(double localValue0, double localValue2, String localValue4) {
      int localValue5 = MinecraftClientAccess.internalField0149.getWindow().getFramebufferWidth();
      int localValue6 = MinecraftClientAccess.internalField0149.getWindow().getFramebufferHeight();
      double localValue7 = MinecraftClientAccess.internalField0149.getWindow().getScaledWidth();
      double localValue9 = MinecraftClientAccess.internalField0149.getWindow().getScaledHeight();
      String localValue11 = localValue4 != null && !localValue4.isBlank() ? localValue4.toLowerCase(Locale.ROOT) : "image";

      return switch (localValue11) {
         case "gui" -> new double[]{localValue0, localValue2};
         case "pixels", "frame" -> new double[]{localValue0 * localValue7 / localValue5, localValue2 * localValue9 / localValue6};
         default -> {
            int localValue14 = internalField0227 > 0 ? internalField0227 : localValue5;
            int localValue15 = internalField0228 > 0 ? internalField0228 : localValue6;
            yield new double[]{localValue0 * localValue7 / localValue14, localValue2 * localValue9 / localValue15};
         }
      };
   }

   private static int internalMethod00001(String localValue0) {
      if (localValue0 != null && !localValue0.isBlank()) {
         String localValue1 = localValue0.toLowerCase(Locale.ROOT);

         return switch (localValue1) {
            case "right", "rmb", "2" -> 1;
            case "middle", "mmb", "3" -> 2;
            case "left", "lmb", "1", "0" -> 0;
            default -> throw new McpHttpServer.InternalType0086(
               "\u043a\u043d\u043e\u043f\u043a\u0430 \u0431\u044b\u0432\u0430\u0435\u0442 left, right \u0438\u043b\u0438 middle"
            );
         };
      } else {
         return 0;
      }
   }

   private static String internalMethod05070(int localValue0) {
      return switch (localValue0) {
         case 1 -> "right";
         case 2 -> "middle";
         default -> "left";
      };
   }

   private static int internalMethod01891(List<String> localValue0) {
      byte localValue1 = 0;
      if (localValue0 == null) {
         return localValue1;
      } else {
         for (String localValue3 : localValue0) {
            String localValue4 = localValue3.toLowerCase(Locale.ROOT);

            localValue1 |= switch (localValue4) {
               case "shift" -> 1;
               case "ctrl", "control" -> 2;
               case "alt" -> 4;
               case "super", "win", "cmd" -> 8;
               default -> 0;
            };
         }

         return localValue1;
      }
   }

   private static void internalMethod06250(int localValue0, Runnable localValue1) {
      internalField0220.schedule(() -> MinecraftClientAccess.internalField0149.execute(localValue1), (long)Math.min(localValue0, 5000), TimeUnit.MILLISECONDS);
   }

   private static double internalMethod06366(double localValue0) {
      return Math.round(localValue0 * 10.0) / 10.0;
   }

   public static final class InternalType0428 {
      private final byte[] internalField0609;
      private final String internalField0248;
      private final int internalField0227;
      private final int internalField0228;
      private final int internalField1053;
      private final int internalField1055;

      public InternalType0428(byte[] localValue1, String localValue2, int localValue3, int localValue4, int localValue5, int localValue6) {
         this.internalField0609 = localValue1;
         this.internalField0248 = localValue2;
         this.internalField0227 = localValue3;
         this.internalField0228 = localValue4;
         this.internalField1053 = localValue5;
         this.internalField1055 = localValue6;
      }

      @Override
      public final String toString() {
         return "InternalType0428[bytes=" + this.internalField0609 + ", mime=" + this.internalField0248 + ", width=" + this.internalField0227 + ", height=" + this.internalField0228 + ", frameWidth=" + this.internalField1053 + ", frameHeight=" + this.internalField1055 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0609);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0248);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0228);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1053);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1055);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         McpInputController.InternalType0428 other = (McpInputController.InternalType0428) localValue1;
         return java.util.Objects.equals(this.internalField0609, other.internalField0609)
            && java.util.Objects.equals(this.internalField0248, other.internalField0248)
            && java.util.Objects.equals(this.internalField0227, other.internalField0227)
            && java.util.Objects.equals(this.internalField0228, other.internalField0228)
            && java.util.Objects.equals(this.internalField1053, other.internalField1053)
            && java.util.Objects.equals(this.internalField1055, other.internalField1055);
      }

      public byte[] internalMethod03687() {
         return this.internalField0609;
      }

      public String internalMethod04674() {
         return this.internalField0248;
      }

      public int internalMethod04133() {
         return this.internalField0227;
      }

      public int internalMethod04135() {
         return this.internalField0228;
      }

      public int internalMethod08399() {
         return this.internalField1053;
      }

      public int internalMethod08400() {
         return this.internalField1055;
      }
   }
}
