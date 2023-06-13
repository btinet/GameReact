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
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

import java.net.URL;
import java.util.*;

import static com.ivision.gamereact.ReactApplication.stage;

public class TestController extends TuioClient implements Initializable {

    @FXML
    private BorderPane root;
    private final KeyPolling keys = KeyPolling.getInstance();
    private MarkerListener listener;

    Rectangle horizontalLine = new Rectangle();
    Rectangle verticalLine = new Rectangle();
    private Group fiduciary = new Group();
    private Group alignment = new Group();
    private Boolean setAlignment = false;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.listener =  new MarkerListener();

        addTuioListener(this.listener);
        connect();

        GameLoopTimer timer = new GameLoopTimer() {
            @Override
            public void tick(float secondsSinceLastFrame) {

                if(!setAlignment)
                {
                    horizontalLine.setWidth(stage.getWidth());
                    horizontalLine.setHeight(1);
                    horizontalLine.setTranslateY(stage.getHeight()/2);
                    horizontalLine.setFill(Color.RED);

                    verticalLine.setWidth(1);
                    verticalLine.setHeight(stage.getHeight());
                    verticalLine.setTranslateX(stage.getWidth()/2);
                    verticalLine.setFill(Color.RED);

                    alignment.getChildren().add(horizontalLine);
                    alignment.getChildren().add(verticalLine);

                    root.getChildren().addAll(alignment,fiduciary);
                    toggleCalibrationGrid();
                    setAlignment = true;
                }


                updateObjects();
                getUserInput();
            }
        };
        root.setStyle("-fx-background-color: #CDCDCD");

        timer.start();

    }

    public void toggleCalibrationGrid () {
        if(alignment.getOpacity() != 0)
        {
            alignment.setOpacity(0);
        } else
        {
            alignment.setOpacity(1);
        }
    }

    public void updateObjects ()
    {

        for (Map.Entry<TuioObject, Group> entry :  this.listener.getObjectShapes().entrySet())
        {
            TuioObject marker = entry.getKey();
            Group group = entry.getValue();

            /*
            for (Object object:
                 group.getChildren()) {
                if (object instanceof Rectangle)
                {
                    ((Rectangle)object).setRotate(marker.getAngleDegrees());
                }
            }
             */


            //group.getTransforms().add(Transform.rotate(marker.getAngle(), 0,0));

            group.setRotate(marker.getAngleDegrees());
            group.setTranslateX(root.getWidth()*marker.getX());
            group.setTranslateY(root.getHeight()*marker.getY());

            if (!this.fiduciary.getChildren().contains(group))
            {
                this.fiduciary.getChildren().add(group);
            }

        }

        if(this.fiduciary.getChildren().size() != this.listener.getObjectShapes().size())
        {
            this.fiduciary.getChildren().retainAll(this.listener.getObjectShapes().values());
        }

    }

    public void getUserInput () {

        // Periodische Tastenabfragen
        // z.B. keys.isDown(KeyCode)


        // einmalige Tastenabfragen (innerhalb Anschlagverzögerung)
        if (keys.isPressed(ButtonConfig.toggleFullscreen)) toggleFullscreen();
        if (keys.isPressed(ButtonConfig.toggleCalibrationGrid)) toggleCalibrationGrid();

    }

    @FXML
    protected void toggleFullscreen() {

        stage.setFullScreen(!stage.isFullScreen());
        horizontalLine.setWidth(stage.getWidth());
        horizontalLine.setTranslateY(stage.getHeight()/2);

        verticalLine.setHeight(stage.getHeight());
        verticalLine.setTranslateX(stage.getWidth()/2);

    }

}
