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
import screen.LinuxMonitor;
import screen.Screen;
import screen.WindowsMonitor;
import utils.Utils;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Main extends Application {

    static{ nu.pattern.OpenCV.loadShared();}

    private VideoCapture capture = new VideoCapture();
    private static final int CAMERA_ID = 0;
    private static final int COLOR = Imgproc.COLOR_BGR2GRAY;
    private static final CascadeClassifier eye = new CascadeClassifier(Paths.get("haarcascade_eye.xml").toString());
    private static final CascadeClassifier face = new CascadeClassifier(Paths.get("haarcascade_frontalface_default.xml").toString());
    private static final ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
    private static final Detector eyeDetector = new Detector(eye);
    private static final Detector faceDetector = new Detector(face);
    private static final Screen monitor = LinuxMonitor.INSTANCE;

    private ImageView view = new ImageView();

    public static void main(String[] args) {
        launch(args);
    }

    private static int counter = 0;

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

                if(eyesRects.toArray().length < 2){
                    counter++;
                    System.out.println(counter);
                    if(counter == 8) {
                        monitor.turnOff();
                    }
                }else{
                    monitor.setBrightness(0.7);
                    counter = 0;
                    monitor.turnOn();
                }

                Utils.drawRects(eyesRects.toArray(), frame);
                Utils.drawRects(faceRects.toArray(), frame, 8);

                Image imageToShow = Utils.mat2Image(frame);
                Platform.runLater(() -> view.imageProperty().set(imageToShow));
            };

            timer.scheduleAtFixedRate(frameGrabber, 0, 50, TimeUnit.MILLISECONDS);

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
