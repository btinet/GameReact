package com.ivision.gamereact.model;

import com.ivision.gamereact.entity.module.GuiModel;
import com.tuio.*;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.HashMap;

public class MarkerListener implements TuioListener {

    private boolean verbose = false;
    private final GuiModel model = new GuiModel();
    private final HashMap<TuioObject, Group> objectShapes = new HashMap<>();

    public MarkerListener() {
        new MarkerListener(false);
    }

    public MarkerListener(boolean verbose) {
        this.verbose = verbose;
    }

    public HashMap<TuioObject, Group> getObjectShapes() {
        return objectShapes;
    }

    @Override
    public void updateTuioObject(TuioObject tobj) {

    }

    @Override
    public void updateTuioCursor(TuioCursor tcur) {

    }

    @Override
    public void updateTuioBlob(TuioBlob tblb) {

    }

    @Override
    public void refresh(TuioTime ftime) {

    }

    @Override
    public void addTuioObject(TuioObject tobj) {
        Group rGroup = this.model.getModule(tobj.getSymbolID());
        Rectangle rectangle = new Rectangle(100,100, Color.RED);
        rectangle.setArcWidth(40);
        rectangle.setArcHeight(40);
        rectangle.setX(-50);
        rectangle.setY(-50);

        Text symbolID = new Text("Symbol-ID: " + tobj.getSymbolID());
        Text sessionID = new Text("Session-ID: " + tobj.getSessionID());

        symbolID.setFont(new Font(24));
        symbolID.setY(50);
        symbolID.setX(100);
        symbolID.setFill(Color.GREEN);

        sessionID.setFont(new Font(24));
        sessionID.setY(25);
        sessionID.setX(100);
        sessionID.setFill(Color.BLUE);

        rGroup.getChildren().add(rectangle);

        if (!this.objectShapes.containsKey(tobj)) this.objectShapes.put(tobj,rGroup);

        if(verbose) {
            System.out.printf("Objekt mit Symbol-ID %s hinzugefügt.%n",tobj.getSymbolID());
        }
    }

    @Override
    public void removeTuioObject(TuioObject tobj) {
        this.objectShapes.remove(tobj);
        if(verbose) {
            System.out.printf("Objekt mit Symbol-ID %s entfernt.%n", tobj.getSymbolID());
        }
    }

    @Override
    public void addTuioCursor(TuioCursor tcur) {

        if(verbose) {
            System.out.printf("Cursor mit Finger-ID %s hinzugefügt.%n", tcur.getCursorID());
        }
    }

    @Override
    public void removeTuioCursor(TuioCursor tcur) {

        if(verbose) {
            System.out.printf("Cursor mit Finger-ID %s entfernt.%n", tcur.getCursorID());
        }
    }

    @Override
    public void addTuioBlob(TuioBlob tblb) {


        if(verbose) {
            System.out.printf("Cursor mit Blob-ID %s hinzugefügt.%n", tblb.getBlobID());
        }
    }

    @Override
    public void removeTuioBlob(TuioBlob tblb) {

        if(verbose) {
            System.out.printf("Cursor mit Blob-ID %s entfernt.%n", tblb.getBlobID());
        }
    }

}
