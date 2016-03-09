package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import model.Camera;
import org.opencv.core.Core;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainWindowController {

    @FXML
    private AnchorPane root;
    @FXML
    private TextField videoPath;
    @FXML
    private GridPane videoGrid;
    ArrayList<model.Camera> listCamera = new ArrayList<>();
    Thread thread;
    boolean flag = true;


    @FXML
    public void initialize(){

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        root.heightProperty().addListener(e-> resizePause());
        root.widthProperty().addListener(e-> resizePause());

        for(final Node label : videoGrid.getChildren()){
            label.setOnMouseEntered(e -> ((Label) label).setTextFill(Color.BLUE));
            label.setOnMouseExited(e -> ((Label) label).setTextFill(Color.ORANGE));
        }
        serviceRUN();


    }

    void serviceRUN(){
        thread = new Thread(() -> {
            while (true) {
                listCamera.forEach(Camera::Process);
                try {
                    Thread.sleep(35);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    void resizePause(){
        try {
            thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();

        }
    }

    @FXML
    void addCamera(ActionEvent event) {

        listCamera.add(new Camera(videoPath.getText()));
        int index = listCamera.size()-1;
        ((Label)videoGrid.getChildren().get(index)).setGraphic(listCamera.get(index).getCanvas());
        System.out.println("index: " + index);
    }
}
