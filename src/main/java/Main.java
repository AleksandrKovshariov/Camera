import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import detection.Detector;
import javafx.scene.layout.AnchorPane;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import screen.Monitor;
import screen.User32;
import utils.Utils;

import java.nio.file.Paths;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Main extends Application {

    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    private VideoCapture capture = new VideoCapture();
    private static final int CAMERA_ID = 0;
    private static final int COLOR = Imgproc.COLOR_BGR2GRAY;
    private static final CascadeClassifier eye = new CascadeClassifier(Paths.get("haarcascade_eye.xml").toString());
    private static final CascadeClassifier face = new CascadeClassifier(Paths.get("haarcascade_frontalface_default.xml").toString());
    private static final ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
    private static final Detector eyeDetector = new Detector(eye);
    private static final Detector faceDetector = new Detector(face);
    private static final Monitor monitor = Monitor.INSTANCE;

    private ImageView view = new ImageView();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Hello World!");

        AnchorPane pane = new AnchorPane();
        Scene scene = new Scene(pane, 650, 600);
        System.out.println("Opening camera");

        this.capture.open(CAMERA_ID);
        System.out.println("Camera is open");

        pane.getChildren().add(view);

        if(this.capture.isOpened()){

            Runnable frameGrabber = () -> {
                Mat frame = grabFrame();
                MatOfRect faceRects = faceDetector.detect(frame);
                MatOfRect eyesRects = eyeDetector.detect(frame, faceRects);

                if(eyesRects.empty()){
                    System.out.println("EMPTY");
                }else{
                }

                Utils.drawRects(eyesRects.toArray(), frame);
                Utils.drawRects(faceRects.toArray(), frame, 8);

                Image imageToShow = Utils.mat2Image(frame);
                Platform.runLater(() -> view.imageProperty().set(imageToShow));
            };

            timer.scheduleAtFixedRate(frameGrabber, 0, 300, TimeUnit.MILLISECONDS);

        }

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Mat grabFrame()
    {
        // init everything
        Mat frame = new Mat();

        // check if the capture is open
        if (this.capture.isOpened())
        {
            try
            {
                // read the current frame
                this.capture.read(frame);


                // if the frame is not empty, process it
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
                // log the error
                System.err.println("Exception during the image elaboration: " + e);
            }
        }

        return frame;
    }


    @Override
    public void stop() throws Exception {
        super.stop();
        timer.shutdownNow();
    }
}
