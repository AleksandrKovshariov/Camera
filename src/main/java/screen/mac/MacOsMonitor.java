package screen.mac;

import screen.Screen;

import javax.naming.OperationNotSupportedException;

public enum MacOsMonitor implements Screen {
    INSTANCE;
    private Runtime runtime = Runtime.getRuntime();
    @Override
    public void turnOn() {

    }

    @Override
    public void turnOff() {
        try {
            runtime.exec("brightness 0.3");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void setBrightness(double brightness) {

    }

    @Override
    public boolean isOn() throws OperationNotSupportedException {
        return false;
    }
}
