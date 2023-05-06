package com.ivision.gamereact.entity.pong;

import com.ivision.engine.GameColor;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class Curt extends Rectangle {

    Line northBorder = new Line();
    Line southBorder = new Line();

    public Curt () {
        northBorder.setStartX(0);
        northBorder.setStartX(900);
        northBorder.setStroke(Color.TRANSPARENT);
        northBorder.setStrokeWidth(1);
        northBorder.setTranslateY(-360);

        southBorder.setStartX(0);
        southBorder.setStartX(900);
        southBorder.setStroke(Color.TRANSPARENT);
        southBorder.setStrokeWidth(1);
        southBorder.setTranslateY(360);

        setFill(GameColor.DARKEN);
        setArcHeight(40);
        setArcWidth(40);
        setWidth(880);
        setHeight(720);
    }

    public Line getNorthBorder() {
        return northBorder;
    }

    public Line getSouthBorder() {
        return southBorder;
    }

}
