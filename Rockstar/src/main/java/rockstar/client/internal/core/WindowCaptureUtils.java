package rockstar.client.internal.core;


import rockstar.client.*;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;
import java.util.Map;

public interface WindowCaptureUtils
extends StdCallLibrary {
    public static final WindowCaptureUtils INSTANCE = (WindowCaptureUtils)Native.load((String)"user32", WindowCaptureUtils.class, (Map)W32APIOptions.DEFAULT_OPTIONS);
    public static final int WDA_NONE = 0;
    public static final int WDA_EXCLUDEFROMCAPTURE = 17;
    public static final int WS_EX_TRANSPARENT = 32;
    public static final int WS_EX_TOOLWINDOW = 128;
    public static final int WS_EX_NOACTIVATE = 0x8000000;

    public boolean SetWindowDisplayAffinity(WinDef.HWND localValue1, WinDef.DWORD localValue2);

    public static WinDef.HWND handle(long l) {
        return new WinDef.HWND(Pointer.createConstant((long)l));
    }

    public static boolean excludeFromCapture(long l) {
        try {
            return INSTANCE.SetWindowDisplayAffinity(WindowCaptureUtils.handle(l), new WinDef.DWORD(17L));
        }
        catch (Throwable throwable) {
            return false;
        }
    }

    public static void makeGhost(long l) {
        try {
            WinDef.HWND hWND = WindowCaptureUtils.handle(l);
            int n = User32.INSTANCE.GetWindowLong(hWND, -20);
            User32.INSTANCE.SetWindowLong(hWND, -20, n | 0x80 | 0x8000000 | 0x20);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }
}

