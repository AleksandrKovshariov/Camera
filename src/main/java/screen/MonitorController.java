package screen;

import org.opencv.core.MatOfRect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MonitorController {
    private static final Logger log = LoggerFactory.getLogger(MonitorController.class);

    private long counter = 0;
    private Screen monitor;

    public MonitorController(Screen monitor){
        this.monitor = monitor;
    }

    public void check(MatOfRect rects){
        monitor.turnOff();
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
