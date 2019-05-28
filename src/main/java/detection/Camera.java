package detection;

import javafx.scene.image.Image;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import utils.Utils;


public class Camera{
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

    public Image drawRects(Mat frame, MatOfRect faceRects, MatOfRect eyesRects){

        Utils.drawRects(eyesRects.toArray(), frame);
        Utils.drawRects(faceRects.toArray(), frame, 8);

        return Utils.mat2Image(frame);
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
                    Imgproc.cvtColor(frame, frame, color);
                    Imgproc.equalizeHist(frame, frame);
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
