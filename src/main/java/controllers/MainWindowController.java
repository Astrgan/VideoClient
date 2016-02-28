package controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import model.Player;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;


public class MainWindowController {

    @FXML
    private GridPane videoGrid;

    @FXML
    public void initialize(){
        new NativeDiscovery().discover();

        for(final Node label : videoGrid.getChildren()){
            label.setOnMouseEntered(e -> ((Label) label).setTextFill(Color.BLUE));
            label.setOnMouseExited(e -> ((Label) label).setTextFill(Color.ORANGE));
        }

        Player player = new Player("D:\\RaM.mkv");
        videoGrid.getChildren().remove(0);
        videoGrid.add(player.getBorderPane(),0,0);

        videoGrid.getChildren().remove(1);
        Player player2 = new Player("D:\\u.mp4");
        videoGrid.add(player2.getBorderPane(),0,0);
    }

}
