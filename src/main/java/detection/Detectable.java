package detection;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;

public interface Detectable {
    MatOfRect detect(Mat frame);
}
