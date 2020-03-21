package screen.mac;

import screen.Screen;

import javax.naming.OperationNotSupportedException;
import java.io.IOException;

public enum MacOsScreen implements Screen {
    INSTANCE;
    private Runtime runtime = Runtime.getRuntime();

    @Override
    public void turnOn() {

    }

    @Override
    public void turnOff() {
        try {
            runtime.exec("brightness 0.0");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setBrightness(double brightness) {
        try {
            runtime.exec(String.format("brightness %f", brightness));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
