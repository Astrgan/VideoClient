package model;


import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import java.nio.ByteBuffer;

/**
 * Created by astrg_000 on 27.02.2016.
 */
public class Camera {

    VideoCapture camera;
    Mat frame;
    String videoPath;

    private Canvas canvas;
    private PixelWriter pixelWriter;
    final PixelFormat<ByteBuffer> pixelformat = PixelFormat.getByteRgbInstance();
    byte[] byteArray;

    public Camera(String videoPath) {
        this.videoPath = videoPath;
        pixelWriter = canvas.getGraphicsContext2D().getPixelWriter();
        camera = new VideoCapture(0);
        camera.open(0);
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void process(){
//        while (true) {
            camera.read(frame);
            Imgproc.resize(frame, frame, new Size(canvas.getWidth(), canvas.getHeight()));
            byteArray = new byte[(int) frame.total() * frame.channels()];
            frame.get(0, 0, byteArray);
            pixelWriter.setPixels(0, 0, frame.width(), frame.height(), pixelformat, byteArray, 0, frame.width() * frame.channels());
//        }
    }


}//END MAIN CLASS: "Camera"
