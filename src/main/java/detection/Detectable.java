package detection;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public interface Detectable {
    default MatOfRect detect(Mat frame, MatOfRect areas){
        MatOfRect inners = detect(frame);
        List<Rect> result = inners.toList();

        for(Rect outer : areas.toArray()) {
            result = result.stream().filter(x -> Utils.isInRect(outer, x)).collect(Collectors.toList());
        }

        MatOfRect res = new MatOfRect();
        res.fromList(result);
        return res;

    }

    MatOfRect detect(Mat frame);
}
