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
import utils.Utils;

import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Main extends Application {

    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    private VideoCapture capture = new VideoCapture();
    private static final int cameraId = 0;
    private static final int color = Imgproc.COLOR_BGR2GRAY;

    private ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
    private CascadeClassifier eye = new CascadeClassifier(Paths.get("haarcascade_eye.xml").toString());
    private CascadeClassifier face = new CascadeClassifier(Paths.get("haarcascade_frontalface_default.xml").toString());
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
        this.capture.open(cameraId);
        System.out.println("Camera is open");

        pane.getChildren().add(view);

        if(this.capture.isOpened()){

            Runnable frameGrabber = () -> {
                Mat frame = grabFrame();
                MatOfRect faceRects = detect(frame, face);
                MatOfRect eyesRects = detectEyes(frame, faceRects);
                Utils.drawRects(eyesRects.toArray(), frame);
                Utils.drawRects(faceRects.toArray(), frame);
                Image imageToShow = Utils.mat2Image(frame);
                Platform.runLater(() -> view.imageProperty().set(imageToShow));
            };

            timer.scheduleAtFixedRate(frameGrabber, 0, 120, TimeUnit.MILLISECONDS);

        }

        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private MatOfRect detectEyes(Mat frame, MatOfRect rect){
        MatOfRect eyes = new MatOfRect();
        eye.detectMultiScale(frame, eyes);
        List<Rect> eyesList = eyes.toList();

        for(Rect r : rect.toArray()){
            eyesList = eyesList.stream().filter(x -> r.contains(new Point(x.x, x.y))).collect(Collectors.toList());
        }

        MatOfRect result = new MatOfRect();
        result.fromList(eyesList);
        return  result;
    }

    private MatOfRect detect(Mat frame, CascadeClassifier classifier){
        MatOfRect eyes = new MatOfRect();
        classifier.detectMultiScale(frame, eyes);
        return eyes;
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
                    Imgproc.cvtColor(frame, frame, color);
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
