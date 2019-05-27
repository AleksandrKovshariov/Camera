package screen;

import jdk.internal.util.xml.impl.Input;

import java.io.InputStream;

public enum LinuxMonitor implements Screen{
    INSTANCE;
    private Runtime runtime = Runtime.getRuntime();
    @Override
    public void turnOn() {
        try {
            runtime.exec("xset dpms force on");
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @Override
    public void turnOff() {
        try {
            runtime.exec("xset dpms force off");
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
