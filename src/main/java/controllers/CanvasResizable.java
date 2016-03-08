package controllers;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Region;

/**
 * Created by astrg_000 on 07.03.2016.
 */
public class CanvasResizable extends Canvas {

    public CanvasResizable() {

        parentProperty().addListener((ov, oldParent, parent) ->
                this.widthProperty().bind(((Region) parent).widthProperty()));
        parentProperty().addListener((ov, oldParent, parent) ->
                this.heightProperty().bind(((Region) parent).heightProperty()));
    }

}
