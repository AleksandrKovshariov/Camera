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
import screen.mac.MacOsMonitor;
import screen.MonitorController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Main extends Application {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    static{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private final ImageView view = new ImageView();
    private static final ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
    private static final Camera camera = new Camera();
    private static final EyesInFaceDetector detector = new EyesInFaceDetector();
    private MonitorController monitorController = new MonitorController(MacOsMonitor.INSTANCE);

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Hello World!");
        AnchorPane pane = new AnchorPane();
        Scene scene = new Scene(pane, 650, 600);
        log.debug("Opening camera");
        camera.open();

        if(camera.isOpen()) {
            timer.scheduleAtFixedRate(this::processFrame, 0, 120, TimeUnit.MILLISECONDS);
        }

        pane.getChildren().add(view);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void processFrame(){
        Mat frame = camera.grabFrame();
        MatOfRect faceRects = detector.detectFace(frame);
        MatOfRect eyesRects = detector.detectEyes(frame, faceRects);
        monitorController.check(eyesRects);

        Image image = camera.drawRects(frame, faceRects, eyesRects);
        Platform.runLater(() -> view.imageProperty().set(image));
    }


    @Override
    public void stop() throws Exception {
        timer.shutdownNow();
        super.stop();
    }
}
