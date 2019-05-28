package detection;

import javafx.scene.image.Image;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import screen.MonitorController;
import utils.Utils;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class Camera implements Detectable {
    public static final int CAMERA_ID = 0;
    public static final int COLOR = Imgproc.COLOR_BGR2GRAY;

    private static final CascadeClassifier eye = new CascadeClassifier(Paths.get("haarcascade_eye.xml").toString());
    private static final CascadeClassifier face = new CascadeClassifier(Paths.get("haarcascade_frontalface_default.xml").toString());
    private static final Detector eyeDetector = new Detector(eye);
    private static final Detector faceDetector = new Detector(face);

    private VideoCapture capture = new VideoCapture();

    public void open(){
        this.capture.open(CAMERA_ID);
    }

    public boolean isOpen(){
        return capture.isOpened();
    }

    public Image draw(Mat frame, MatOfRect faceRects, MatOfRect eyesRects){

        Utils.drawRects(eyesRects.toArray(), frame);
        Utils.drawRects(faceRects.toArray(), frame, 8);

        return Utils.mat2Image(frame);
    }

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

    public Mat grabFrame()
    {
        Mat frame = new Mat();

        if (this.capture.isOpened())
        {
            try
            {
                this.capture.read(frame);

                if (!frame.empty())
                {
                    Imgproc.cvtColor(frame, frame, COLOR);
                }
                else {
                    System.out.println("Frame empty");
                }

            }
            catch (Exception e)
            {
                System.err.println("Exception during the image elaboration: " + e);
            }
        }

        return frame;
    }
}
