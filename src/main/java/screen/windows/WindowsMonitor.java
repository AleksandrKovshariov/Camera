package screen.windows;

import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import screen.Screen;

public enum WindowsMonitor implements Screen
{
    INSTANCE;
    private User32 user = User32.INSTANCE;
    private boolean isOn = true;

    public void turnOn(){
        user.SendMessageA(WinUser.HWND_BROADCAST, WinUser.WM_SYSCOMMAND,
                User32.SC_MONITORPOWER, new WinDef.LPARAM(User32.SC_MONITOR_ON));
        isOn = true;
    }
    public void turnOff(){
        user.SendMessageA(WinUser.HWND_BROADCAST, WinUser.WM_SYSCOMMAND,
                User32.SC_MONITORPOWER, new WinDef.LPARAM(User32.SC_MONITOR_OFF));
        isOn = false;
    }

    @Override
    public void setBrightness(double value) {

    }


    @Override
    public boolean isOn() {
        return isOn;
    }

}
