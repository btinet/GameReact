package com.ivision.gamereact.model;

import com.ivision.engine.ImageFX;
import com.ivision.engine.ImageFiles;
import com.ivision.gamereact.controller.AppController;
import com.tuio.TuioBlob;
import com.tuio.TuioCursor;
import com.tuio.TuioListener;
import com.tuio.TuioObject;
import com.tuio.TuioTime;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import static com.ivision.gamereact.ReactApplication.height;
import static com.ivision.gamereact.ReactApplication.width;

public class GamepadListener implements TuioListener {

    private boolean verbose = false; // Konsolenausgabe ein- oder ausschalten

    Pane root;
    ArrayList<TuioObject> gamepads = new ArrayList<TuioObject>();
    ArrayList<TuioCursor> fingers = new ArrayList<TuioCursor>();
    ArrayList<TuioBlob> blobs = new ArrayList<TuioBlob>();

    protected AppController controller;

    public GamepadListener (Pane rootPane) {
        this.root = rootPane;
    }

    public GamepadListener (Pane rootPane, boolean verbose) {
        this.root = rootPane;
        this.verbose = verbose;
    }

    public ArrayList<TuioObject> getGamepads() {
        return gamepads;
    }

    public ArrayList<TuioCursor> getFingers() {
        return fingers;
    }

    public ArrayList<TuioBlob> getBlobs() {
        return blobs;
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

    public void setController (AppController controller) {
        this.controller = controller;
    }

    @Override
    public void addTuioObject(TuioObject tobj) {
        gamepads.add(tobj);


        if(tobj.getSymbolID() == 1) controller.playerOneIsPresent = true;
        if(tobj.getSymbolID() == 2) controller.playerTwoIsPresent = true;
        if(tobj.getSymbolID() > 2) {
            controller.imageObjects.put(tobj.getImageView(),tobj);
        }
        if(verbose) {
            System.out.printf("Objekt mit Symbol-ID %s hinzugefügt.%n",tobj.getSymbolID());
        }
    }

    @Override
    public void removeTuioObject(TuioObject tobj) {
        gamepads.remove(tobj);

        if(tobj.getSymbolID() == 1) controller.playerOneIsPresent = false;
        if(tobj.getSymbolID() == 2) controller.playerTwoIsPresent = false;
        if(tobj.getSymbolID() > 2) {
            controller.imageObjects.remove(tobj.getImageView());
        }
        if(verbose) {
            System.out.printf("Objekt mit Symbol-ID %s entfernt.%n", tobj.getSymbolID());
        }
    }

    @Override
    public void addTuioCursor(TuioCursor tcur) {
        fingers.add(tcur);

        if(verbose) {
            System.out.printf("Cursor mit Finger-ID %s hinzugefügt.%n", tcur.getCursorID());
        }
    }

    @Override
    public void removeTuioCursor(TuioCursor tcur) {
        fingers.remove(tcur);

        if(verbose) {
            System.out.printf("Cursor mit Finger-ID %s entfernt.%n", tcur.getCursorID());
        }
    }

    @Override
    public void addTuioBlob(TuioBlob tblb) {
        blobs.add(tblb);

        if(verbose) {
            System.out.printf("Cursor mit Blob-ID %s hinzugefügt.%n", tblb.getBlobID());
        }
    }

    @Override
    public void removeTuioBlob(TuioBlob tblb) {
        blobs.remove(tblb);

        if(verbose) {
            System.out.printf("Cursor mit Blob-ID %s entfernt.%n", tblb.getBlobID());
        }
    }

}
