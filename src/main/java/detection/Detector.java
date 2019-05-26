package detection;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.objdetect.CascadeClassifier;

public class Detector implements Detectable {
    private CascadeClassifier classifier;

    public Detector(CascadeClassifier eyeClassifier){
        this.classifier = eyeClassifier;
    }

    @Override
    public MatOfRect detect(Mat frame) {
        MatOfRect matOfRect = new MatOfRect();
        classifier.detectMultiScale(frame, matOfRect);
        return matOfRect;
    }
}
