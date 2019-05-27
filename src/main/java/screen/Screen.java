package screen;

import javax.naming.OperationNotSupportedException;

public interface Screen {
    void turnOn();
    void turnOff();
    void setBrightness(double brightness);
    default boolean isOn() throws OperationNotSupportedException{
        throw new OperationNotSupportedException();
    }
}
