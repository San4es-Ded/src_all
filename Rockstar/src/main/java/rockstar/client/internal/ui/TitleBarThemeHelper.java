package rockstar.client.internal.ui;



import rockstar.client.internal.core.*;
import rockstar.client.*;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef.DWORD;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.LPVOID;
import com.sun.jna.ptr.IntByReference;
import lombok.Generated;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFWNativeWin32;

public final class TitleBarThemeHelper {
   private static final int internalField0227 = 20;
   private static final boolean internalField0277 = internalMethod07869();
   private static HWND internalField0042;
   private static TitleBarThemeHelper.InternalType0376 internalField0137 = null;

   private static boolean internalMethod07869() {
      String localValue0 = System.getProperty("os.name");
      return localValue0 != null && localValue0.toLowerCase().contains("windows");
   }

   public static void internalMethod01735() {
      internalMethod05782(TitleBarThemeHelper.InternalType0376.internalField0138);
   }

   public static void internalMethod01739() {
      internalMethod05782(TitleBarThemeHelper.InternalType0376.internalField0137);
   }

   public static void internalMethod07865() {
      TitleBarThemeHelper.InternalType0376 localValue0 = internalField0137 == TitleBarThemeHelper.InternalType0376.internalField0138
         ? TitleBarThemeHelper.InternalType0376.internalField0137
         : TitleBarThemeHelper.InternalType0376.internalField0138;
      internalMethod05782(localValue0);
   }

   public static boolean internalMethod01736() {
      return internalField0277;
   }

   public static boolean internalMethod01740() {
      return internalField0137 == TitleBarThemeHelper.InternalType0376.internalField0138;
   }

   public static boolean internalMethod07866() {
      return internalField0137 == TitleBarThemeHelper.InternalType0376.internalField0137;
   }

   private static void internalMethod05782(TitleBarThemeHelper.InternalType0376 localValue0) {
      if (internalField0277 && localValue0 != null) {
         try {
            internalMethod07868();
            IntByReference localValue1 = new IntByReference(localValue0.internalMethod04920());
            LPVOID localValue2 = new LPVOID(localValue1.getPointer());
            DwmApi.INSTANCE.DwmSetWindowAttribute(internalField0042, new DWORD(20L), localValue2, new DWORD(4L));
            internalField0137 = localValue0;
         } catch (Exception localValue3) {
            throw new RuntimeException("Failed to apply title bar theme: " + localValue0, localValue3);
         }
      }
   }

   private static void internalMethod07868() {
      if (internalField0042 == null) {
         MinecraftClient localValue0 = MinecraftClient.getInstance();
         if (localValue0 != null && localValue0.getWindow() != null) {
            long localValue1 = localValue0.getWindow().getHandle();
            long localValue3 = GLFWNativeWin32.glfwGetWin32Window(localValue1);
            internalField0042 = new HWND(Pointer.createConstant(localValue3));
         } else {
            throw new IllegalStateException("MinecraftClient window is not available");
         }
      }
   }

   @Generated
   private TitleBarThemeHelper() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static enum InternalType0376 {
      internalField0137(0),
      internalField0138(1);

      private final int internalField0227;

      @Generated
      public int internalMethod04920() {
         return this.internalField0227;
      }

      @Generated
      private InternalType0376(int localValue3) {
         this.internalField0227 = localValue3;
      }
   }
}
