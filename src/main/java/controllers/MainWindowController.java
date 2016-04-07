package controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import model.Camera;
import model.VideoSocket;
import org.opencv.core.Core;

import java.io.IOException;
import java.util.ArrayList;

public class MainWindowController {

    @FXML
    private AnchorPane root;
    @FXML
    private TextField videoPath;
    @FXML
    private GridPane videoGrid;
    @FXML
    private ListView<String> listView;
    ObservableList<String> listData = FXCollections.observableArrayList();
    ArrayList<model.Camera> listCamera = new ArrayList<>();
    public Thread threadCameraProcess;
    VideoSocket videoSocket;


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

        try {
            videoSocket = new VideoSocket("127.0.0.1", 1234) {
                @Override
                public void callback(String path) {
//                    createCamera(path);
//                    System.out.println(path);
                    if(path.charAt(0) == '1'){
                        System.out.println(path.substring(1));
                        Platform.runLater(() -> createCamera(path.substring(1)));
                        System.out.println("Hello callback");
                    }
                }
            };
        } catch (IOException e) {
            System.out.println("ошибка подключения к серверу");
        }
        videoSocket.getPaths();
        listView.setItems(listData);

    }

    void serviceRUN(){
        threadCameraProcess = new Thread(() -> {
            while (!Thread.interrupted()) {
                listCamera.forEach(Camera::Process);
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e ) {
                    break;
                }
            }
        });
        threadCameraProcess.start();
    }

    void resizePause(){
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void addCamera(ActionEvent event) {
        videoSocket.pushPath(videoPath.getText());

        }

    void createCamera(String path){
        System.out.println("createCamera::" + path);
        threadCameraProcess.interrupt();
        try {
            threadCameraProcess.join();
            listCamera.add(new Camera(path));
            videoPath.clear();
            int index = listCamera.size()-1;
            Label label = ((Label)videoGrid.getChildren().get(index));
            label.setGraphic(listCamera.get(index).getCanvas());
            label.setText(" ");
            System.out.println("index: " + index);
            listData.add(path);
            serviceRUN();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stop(){
        threadCameraProcess.interrupt();
        videoSocket.stop();
    }

    @Override
    protected void finalize() throws Throwable {
        System.out.println("finalize");
        threadCameraProcess.interrupt();
        super.finalize();
    }

}
