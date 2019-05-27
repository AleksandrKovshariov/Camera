package screen;

import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;

public enum WindowsMonitor implements Screen
{
    INSTANCE;
    private User32 user = User32.INSTANCE;
    private boolean isOn = true;

    public void turnOn(){
        if(!isOn()){
            user.SendMessageA(WinUser.HWND_BROADCAST, WinUser.WM_SYSCOMMAND,
                    User32.SC_MONITORPOWER, new WinDef.LPARAM(User32.SC_MONITOR_ON));
            isOn = true;
        }
    }
    public void turnOff(){
        if(isOn()){
            user.SendMessageA(WinUser.HWND_BROADCAST, WinUser.WM_SYSCOMMAND,
                    User32.SC_MONITORPOWER, new WinDef.LPARAM(User32.SC_MONITOR_OFF));
            isOn = false;
        }
    }

    @Override
    public void setBrightness() {

    }


    @Override
    public boolean isOn() {
        return isOn;
    }

}
