package controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import model.Player;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;

import java.util.Timer;
import java.util.TimerTask;


public class MainWindowController {

    @FXML
    private GridPane videoGrid;

    Player player1;
    Player player2;
    Timer timer = new Timer();
    TimerTask task = new MyTaskextends();
    boolean flagPlayer = true;

    @FXML
    public void initialize(){
        new NativeDiscovery().discover();

        for(final Node label : videoGrid.getChildren()){
            label.setOnMouseEntered(e -> ((Label) label).setTextFill(Color.BLUE));
            label.setOnMouseExited(e -> ((Label) label).setTextFill(Color.ORANGE));
        }

        player1 = new Player("D:\\RaM.mkv");
        videoGrid.getChildren().remove(0);
        videoGrid.add(player1.getBorderPane(),0,0);

        videoGrid.getChildren().remove(2);
        player2 = new Player("D:\\u.mp4");
        videoGrid.add(player2.getBorderPane(),2,0);
        player2.pause();

        timer.schedule( task,5000,10);
    }



    class MyTaskextends extends TimerTask{

        public void run(){
            if(flagPlayer){
                player1.pause();
                player2.play();
            }else{
                player1.play();
                player2.pause();
            }

        }
    }
}
