package screen;

import javax.naming.OperationNotSupportedException;

public interface Screen {
    void turnOn();
    void turnOff();
    void setBrightness(double brightness);
}
