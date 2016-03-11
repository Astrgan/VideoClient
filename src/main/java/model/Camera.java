package model;

import controllers.CanvasResizable;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import java.nio.ByteBuffer;

/**
 * Created by astrg_000 on 07.03.2016.
 */
public class Camera {

    CanvasResizable canvas;
    PixelWriter pixelWriter;
    Mat frame;
    VideoCapture camera;
    Size size;
    final PixelFormat<ByteBuffer> pixelFormat = PixelFormat.getByteRgbInstance();
    byte[] byteArray;
    int width, height, channels;

    public Camera(String videoPath) {
        canvas = new CanvasResizable();
        frame = new Mat();
        pixelWriter = canvas.getGraphicsContext2D().getPixelWriter();
        camera = new VideoCapture(videoPath);
        canvas.heightProperty().addListener(e -> newSize());
        canvas.widthProperty().addListener(e -> newSize());
        camera.read(frame);
        newSize();
    }

    void newSize(){
        width = (int) canvas.getWidth();
        height = (int) canvas.getHeight();
        channels = frame.channels();
        byteArray = null;
        size = null;
        byteArray = new byte[width * height * channels];
        size = new Size(width, height);
        System.out.println("width: " + width + " height: " + height);
    }

    public void Process(){
        try {
            if (camera.isOpened()){
                camera.read(frame);
                Imgproc.resize(frame, frame, size);
                frame.get(0, 0, byteArray);
                pixelWriter.setPixels(0, 0, frame.width(), frame.height(), pixelFormat, byteArray, 0, frame.width() * frame.channels());
            }else {
                System.out.println("Error");
            }
        }catch (Exception e){
            System.out.println("ErrorException: " + e);
        }
    }

    public Canvas getCanvas() {
        return canvas;
    }

}
