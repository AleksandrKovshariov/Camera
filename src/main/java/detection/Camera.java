package detection;

import javafx.scene.image.Image;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import screen.LinuxMonitor;
import screen.Screen;
import utils.Utils;

import java.nio.file.Paths;


public class Camera {
    public static final int CAMERA_ID = 0;
    public static final int COLOR = Imgproc.COLOR_BGR2GRAY;

    private static final CascadeClassifier eye = new CascadeClassifier(Paths.get("haarcascade_eye.xml").toString());
    private static final CascadeClassifier face = new CascadeClassifier(Paths.get("haarcascade_frontalface_default.xml").toString());
    private static final Detector eyeDetector = new Detector(eye);
    private static final Detector faceDetector = new Detector(face);
    private static final Screen monitor = LinuxMonitor.INSTANCE;

    private int counter = 0;
    private VideoCapture capture = new VideoCapture();

    public void open(){
        this.capture.open(CAMERA_ID);
    }

    public boolean isOpen(){
        return capture.isOpened();
    }

    public Image processFrame(){

        Mat frame = grabFrame();
        MatOfRect faceRects = faceDetector.detect(frame);
        MatOfRect eyesRects = eyeDetector.detect(frame, faceRects);

        if(eyesRects.toArray().length < 2){
            counter++;
            System.out.println(counter);
            if(counter == 8) {
                monitor.turnOff();
            }
        }else{
            counter = 0;
            monitor.turnOn();
        }

        Utils.drawRects(eyesRects.toArray(), frame);
        Utils.drawRects(faceRects.toArray(), frame, 8);

        return Utils.mat2Image(frame);
    }

    private Mat grabFrame()
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
