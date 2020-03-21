import detection.Camera;
import detection.EyesInFaceDetector;
import javafx.scene.layout.AnchorPane;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.opencv.core.*;
import screen.Brightness;
import screen.mac.MacOsScreen;
import screen.ScreenController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ImageUtils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Main extends Application {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    static{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private final ImageView view = new ImageView();
    private static final ScheduledExecutorService TIMER = Executors.newSingleThreadScheduledExecutor();
    private static final Camera CAMERA = new Camera();
    private static final EyesInFaceDetector DETECTOR = new EyesInFaceDetector();
    private ScreenController screenController = new ScreenController(new Brightness(0.8, 0.05), MacOsScreen.INSTANCE);

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Test");
        AnchorPane pane = new AnchorPane();
        Scene scene = new Scene(pane, 1350, 800);
        log.debug("Opening camera");
        CAMERA.open();

        if(CAMERA.isOpen()) {
            TIMER.scheduleAtFixedRate(this::processFrame, 0, 120, TimeUnit.MILLISECONDS);
        }

        pane.getChildren().add(view);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void processFrame(){
        Mat frame = CAMERA.grabFrame();
        MatOfRect faceRects = DETECTOR.detectFace(frame);
        MatOfRect eyesRects = DETECTOR.detectEyes(frame, faceRects);
        screenController.check(eyesRects);

        ImageUtils.drawRects(faceRects.toArray(), frame, 10);
        ImageUtils.drawRects(eyesRects.toArray(), frame, 2);

        Image image = ImageUtils.mat2Image(frame);

        Platform.runLater(() -> view.imageProperty().set(image));
    }


    @Override
    public void stop() throws Exception {
        TIMER.shutdownNow();
        super.stop();
    }
}
