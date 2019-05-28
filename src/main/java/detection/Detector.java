package detection;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Size;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

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

    public MatOfRect detect(Mat frame, double scale, int minNeighbors, int flags, Size minSize, Size maxSize){
        MatOfRect matOfRect = new MatOfRect();
        classifier.detectMultiScale(frame, matOfRect, scale, minNeighbors, flags, minSize, maxSize);
        return matOfRect;
    }
}
