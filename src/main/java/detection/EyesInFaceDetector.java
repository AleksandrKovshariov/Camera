package detection;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class EyesInFaceDetector implements Detectable {

    private static final CascadeClassifier eye = new CascadeClassifier(Paths.get("haarcascade_eye.xml").toString());
    private static final CascadeClassifier face = new CascadeClassifier(Paths.get("haarcascade_frontalface_default.xml").toString());
    private static final Detector eyeDetector = new Detector(eye);
    private static final Detector faceDetector = new Detector(face);


    public MatOfRect detectFace(Mat frame){
        return faceDetector.detect(frame);
    }

    @Override
    public MatOfRect detect(Mat frame){
        return detectEyes(frame, detectFace(frame));
    }

    public MatOfRect detectEyes(Mat frame, MatOfRect face){
        MatOfRect eyesRects = new MatOfRect();
        List<Rect> eyeList = new ArrayList<>();

        for(Rect r : face.toArray()){
            MatOfRect rects = eyeDetector.detect(frame.submat(r));
            List<Rect> temp = rects.toList();
            temp.forEach(x -> {
                x.x += r.x;
                x.y += r.y;
            });
            eyeList.addAll(temp);
        }
        eyesRects.fromList(eyeList);
        return eyesRects;
    }
}
