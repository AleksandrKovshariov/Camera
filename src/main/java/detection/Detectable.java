package detection;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public interface Detectable {
//    //No need for method
//    default MatOfRect detect(Mat frame, MatOfRect areas){
//        MatOfRect inners = detect(frame);
//        List<Rect> result = new ArrayList<>();
//
//        for (Rect inner : inners.toArray()) {
//            for (Rect outer : areas.toArray()) {
//                if (Utils.isInRect(outer, inner)) {
//                    result.add(inner);
//                }
//            }
//        }
//
//        MatOfRect res = new MatOfRect();
//        res.fromList(result);
//        return res;
//
//    }

    MatOfRect detect(Mat frame);
}
