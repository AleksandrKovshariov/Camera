import detection.Camera;
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

    private ImageView view = new ImageView();
    private static final ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
    private static Camera camera = new Camera();

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Hello World!");

        AnchorPane pane = new AnchorPane();
        Scene scene = new Scene(pane, 650, 600);
        System.out.println("Opening camera");
        camera.open();

        if(camera.isOpen()) {
            timer.scheduleAtFixedRate(this::renderFrame, 0, 60, TimeUnit.MILLISECONDS);
        }

        pane.getChildren().add(view);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void renderFrame(){
        Image image = camera.processFrame();
        Platform.runLater(() -> view.imageProperty().set(image));
    }


    @Override
    public void stop() throws Exception {
        timer.shutdownNow();
        super.stop();
    }
}
