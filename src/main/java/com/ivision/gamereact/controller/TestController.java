package com.ivision.gamereact.controller;

import com.ivision.engine.ButtonConfig;
import com.ivision.engine.GameLoopTimer;
import com.ivision.engine.KeyPolling;
import com.ivision.gamereact.model.MarkerListener;
import com.tuio.TuioClient;
import com.tuio.TuioObject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.*;

import static com.ivision.gamereact.ReactApplication.stage;

public class TestController extends TuioClient implements Initializable {

    @FXML
    private Pane root;
    private final KeyPolling keys = KeyPolling.getInstance();
    private MarkerListener listener;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.listener =  new MarkerListener();

        addTuioListener(this.listener);
        connect();

        GameLoopTimer timer = new GameLoopTimer() {
            @Override
            public void tick(float secondsSinceLastFrame) {
                updateObjects();
                getUserInput();
            }
        };
        root.setStyle("-fx-background-color: #2F4F4F");
        timer.start();

    }

    public void updateObjects ()
    {
        for (Map.Entry<TuioObject, Group> entry :  this.listener.getObjectShapes().entrySet())
        {
            TuioObject marker = entry.getKey();
            Group group = entry.getValue();
            Rectangle rectangle = (Rectangle) group.getChildren().get(0);
            rectangle.setRotate(marker.getAngleDegrees());
            group.setTranslateX(root.getWidth()*marker.getX());
            group.setTranslateY(root.getHeight()*marker.getY());

            if (!this.root.getChildren().contains(group))
            {
                this.root.getChildren().add(group);
            }

        }

        if(this.root.getChildren().size() != this.listener.getObjectShapes().size())
        {
            this.root.getChildren().retainAll(this.listener.getObjectShapes().values());
        }

    }

    public void getUserInput () {

        // Periodische Tastenabfragen
        // z.B. keys.isDown(KeyCode)


        // einmalige Tastenabfragen (innerhalb Anschlagverzögerung)
        if (keys.isPressed(ButtonConfig.toggleFullscreen)) toggleFullscreen();

    }

    @FXML
    protected void toggleFullscreen() {

        stage.setFullScreen(!stage.isFullScreen());
    }

}
