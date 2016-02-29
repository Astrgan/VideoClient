package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import model.Camera;
import org.opencv.core.Core;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class MainWindowController {

    @FXML
    private GridPane videoGrid;
    ArrayList<model.Camera> listCamera = new ArrayList<>();
    Timer timer = new Timer();
    @FXML
    private TextField videoPath;

    @FXML
    public void initialize(){

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        for(final Node label : videoGrid.getChildren()){
            label.setOnMouseEntered(e -> ((Label) label).setTextFill(Color.BLUE));
            label.setOnMouseExited(e -> ((Label) label).setTextFill(Color.ORANGE));
        }

        videoGrid.getChildren().remove(0);//delete

    }

    @FXML
    void addCamera(ActionEvent event) {

        listCamera.add(new Camera(videoPath.getText()));
        videoGrid.add(listCamera.get(listCamera.size()-1).getCanvas(),0,0);
        listCamera.get(listCamera.size()-1).process();
    }



}
