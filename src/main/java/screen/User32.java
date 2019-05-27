package screen;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.win32.StdCallLibrary;

public interface User32 extends StdCallLibrary {
    public static final int SC_MONITORPOWER = 0xF170;
    public static final int SC_MONITOR_OFF = 2;
    public static final int SC_MONITOR_ON = -1;
    public static final int SC_MONITOR_LOW_POWER = 1;


    User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class);

    WinDef.LRESULT SendMessageA(WinDef.HWND paramHWND, int paramInt, WinDef.WPARAM paramWPARAM,
                                WinDef.LPARAM paramLPARAM);

    WinDef.LRESULT SendMessageA(WinDef.HWND paramHWND, int paramInt, int paramInt2,
                                WinDef.LPARAM paramLPARAM);
}