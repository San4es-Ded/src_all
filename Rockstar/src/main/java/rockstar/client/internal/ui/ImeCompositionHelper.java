package rockstar.client.internal.ui;


import rockstar.client.*;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;
import java.util.Map;
import org.lwjgl.glfw.GLFWNativeWin32;

public final class ImeCompositionHelper {
    private static long internalField0229;
    private static long internalField0230;

    private ImeCompositionHelper() {
    }

    public static void internalMethod05309(long l) {
        if (!Platform.isWindows()) {
            return;
        }
        internalField0229 = l;
        internalField0230 = System.nanoTime() + 60000000000L;
    }

    public static void internalMethod04762() {
        if (!Platform.isWindows() || internalField0229 == 0L) {
            return;
        }
        if (System.nanoTime() > internalField0230) {
            internalField0229 = 0L;
            return;
        }
        try {
            Pointer pointer = Pointer.createConstant((long)GLFWNativeWin32.glfwGetWin32Window((long)internalField0229));
            Pointer pointer2 = InternalType0385.INSTANCE.ImmGetContext(pointer);
            if (pointer2 == null) {
                return;
            }
            try {
                if (InternalType0385.INSTANCE.ImmGetCompositionStringW(pointer2, 8, Pointer.NULL, 0) > 0) {
                    InternalType0385.INSTANCE.ImmNotifyIME(pointer2, 21, 1, 0);
                }
            }
            finally {
                InternalType0385.INSTANCE.ImmReleaseContext(pointer, pointer2);
            }
        }
        catch (LinkageError | RuntimeException throwable) {
            internalField0229 = 0L;
        }
    }

    static interface InternalType0385
    extends StdCallLibrary {
        public static final InternalType0385 INSTANCE = (InternalType0385)Native.load((String)"imm32", InternalType0385.class, (Map)W32APIOptions.DEFAULT_OPTIONS);

        public Pointer ImmGetContext(Pointer localValue1);

        public boolean ImmReleaseContext(Pointer localValue1, Pointer localValue2);

        public boolean ImmNotifyIME(Pointer localValue1, int localValue2, int localValue3, int localValue4);

        public int ImmGetCompositionStringW(Pointer localValue1, int localValue2, Pointer localValue3, int localValue4);
    }
}

