package rockstar.client.internal.core;


import rockstar.client.*;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;
import java.util.Map;

public interface DwmApi
extends StdCallLibrary {
    public static final DwmApi INSTANCE = (DwmApi)Native.load((String)"dwmapi", DwmApi.class, (Map)W32APIOptions.DEFAULT_OPTIONS);

    public void DwmSetWindowAttribute(WinDef.HWND localValue1, WinDef.DWORD localValue2, WinDef.LPVOID localValue3, WinDef.DWORD localValue4);
}

