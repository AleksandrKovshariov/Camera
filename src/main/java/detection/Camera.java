package detection;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import screen.ScreenController;


public class Camera{
    private static final Logger log = LoggerFactory.getLogger(ScreenController.class);
    private int camera_id;
    private int color;

    public Camera(int camera_id, int color){
        this.camera_id = camera_id;
        this.color = color;
    }

    public Camera(int camera_id){
        this(camera_id, Imgproc.COLOR_BGR2GRAY);
    }

    public Camera(){
        this(0);
    }

    private VideoCapture capture = new VideoCapture();

    public void open(){
        this.capture.open(camera_id);
    }

    public boolean isOpen(){
        return capture.isOpened();
    }

    public Mat grabFrame()
    {
        Mat frame = new Mat();

        if (this.capture.isOpened())
        {
            try {
                this.capture.read(frame);

                if (!frame.empty())
                {
                    Imgproc.cvtColor(frame, frame, color);
//                    Imgproc.equalizeHist(frame, frame);
                }
                else {
                    log.error("Frame is empty");
                }

            }
            catch (Exception e) {
                log.error("Exception during the image elaboration: " + e);
            }
        }

        return frame;
    }
}
