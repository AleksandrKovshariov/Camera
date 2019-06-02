package screen;

import org.opencv.core.MatOfRect;

public class MonitorController {
    private long counter = 0;
    private Screen monitor;

    public MonitorController(Screen monitor){
        this.monitor = monitor;
    }

    public void check(MatOfRect rects){
        if(rects.toArray().length < 2){
            counter++;
            System.out.println(counter);
            if(counter == 8) {
//                monitor.turnOff();
            }
        }else{
            counter = 0;
            monitor.turnOn();
        }

    }

}
