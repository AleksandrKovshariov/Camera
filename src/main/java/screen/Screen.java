package screen;

import javax.naming.OperationNotSupportedException;

public interface Screen {
    void turnOn();
    void turnOff();
    void setBrightness();
    default boolean isOn() throws OperationNotSupportedException{
        throw new OperationNotSupportedException();
    }
}
