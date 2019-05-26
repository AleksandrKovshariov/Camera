import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Main extends Application {

    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    private VideoCapture capture = new VideoCapture();
    private int cameraId = 0;
    private ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
    private CascadeClassifier eye = new CascadeClassifier(Paths.get("haarcascade_eye.xml").toString());
    private CascadeClassifier face = new CascadeClassifier(Paths.get("haarcascade_frontalface_default.xml").toString());
    private int cvt = Imgproc.COLOR_BGR2GRAY;
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
                drawRects(eyesRects.toArray(), frame);
                drawRects(faceRects.toArray(), frame);
                Image imageToShow = mat2Image(frame);
                Platform.runLater(() -> view.imageProperty().set(imageToShow));
            };

            timer.scheduleAtFixedRate(frameGrabber, 0, 120, TimeUnit.MILLISECONDS);

        }

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private boolean isInRect(Rect outer, Rect inner){
        return outer.contains(new Point(inner.x, inner.y)) && inner.x + outer.x + inner.width < outer.width
                && inner.y + outer.y + inner.height < outer.height;
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

    private void drawRects(Rect[] rects, Mat frame){
        for(Rect rect : rects)
            Imgproc.rectangle(frame, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(50, 255, 50),2);
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
                    Imgproc.cvtColor(frame, frame, cvt);
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

    public static Image mat2Image(Mat frame)
    {
        try
        {
            return SwingFXUtils.toFXImage(matToBufferedImage(frame), null);
        }
        catch (Exception e)
        {
            System.err.println("Cannot convert the Mat obejct: " + e);
            return null;
        }
    }

    private static BufferedImage matToBufferedImage(Mat original)
    {
        BufferedImage image = null;
        int width = original.width(), height = original.height(), channels = original.channels();
        byte[] sourcePixels = new byte[width * height * channels];
        original.get(0, 0, sourcePixels);

        if (original.channels() > 1)
        {
            image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        }
        else
        {
            image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        }
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(sourcePixels, 0, targetPixels, 0, sourcePixels.length);

        return image;
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        timer.shutdownNow();
    }
}
