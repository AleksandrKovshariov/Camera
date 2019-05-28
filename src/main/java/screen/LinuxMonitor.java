package screen;


public enum LinuxMonitor implements Screen{
    INSTANCE;
    private Runtime runtime = Runtime.getRuntime();
    @Override
    public void turnOn() {
        try {
            runtime.exec("xset dpms force on");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void turnOff() {
        try {
            runtime.exec("xset dpms force off");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void setBrightness(double value) {
        try {
            runtime.exec("xrandr --output eDP-1-1 --brightness " + value);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
