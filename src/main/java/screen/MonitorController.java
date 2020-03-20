package screen;

import org.opencv.core.MatOfRect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MonitorController {
    private static final Logger log = LoggerFactory.getLogger(MonitorController.class);
    private Brightness brightness;

    private long counter = 0;
    private Screen monitor;

    public MonitorController(Brightness brightness, Screen monitor){
        this.brightness = brightness;
        this.monitor = monitor;
    }

    public void check(MatOfRect rects){
        if(rects.toArray().length < 2){
            log.debug("found " + counter);
            counter++;
            if(counter >= 10) {
                log.debug("Decreasing brightness to " + brightness.decrease());
                monitor.setBrightness(brightness.getBrightness());
                counter = 0;
            }
        }else{
            counter = 0;
            log.debug("Increasing brightness to " + brightness.increase());
            monitor.setBrightness(brightness.getBrightness());
        }

    }

}
