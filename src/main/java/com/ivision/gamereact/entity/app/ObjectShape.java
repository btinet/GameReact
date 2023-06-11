package com.ivision.gamereact.entity.app;

import com.tuio.TuioPoint;
import javafx.scene.shape.Rectangle;

public class ObjectShape extends Rectangle {

    private final TuioPoint object;

    public ObjectShape (TuioPoint object) {
        this.object = object;
    }

    public TuioPoint getObject() {
        return object;
    }

}
