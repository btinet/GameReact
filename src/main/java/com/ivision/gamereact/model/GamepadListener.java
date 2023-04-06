package ivisionfx.interaction.gamepad;

import TUIO.*;
import ivisionfx.controller.MainController;

import java.util.ArrayList;

public class GamepadListener implements TuioListener {

    private boolean verbose = false; // Konsolenausgabe ein- oder ausschalten

    ArrayList<TuioObject> gamepads = new ArrayList<TuioObject>();
    ArrayList<TuioCursor> fingers = new ArrayList<TuioCursor>();
    ArrayList<TuioBlob> blobs = new ArrayList<TuioBlob>();

    protected MainController controller;

    public GamepadListener () {
    }

    public GamepadListener (boolean verbose) {
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

    public void setController (MainController controller) {
        this.controller = controller;
    }

    @Override
    public void addTuioObject(TuioObject tobj) {
        gamepads.add(tobj);

        if(tobj.getSymbolID() == 1) controller.playerOneIsPresent = true;
        if(tobj.getSymbolID() == 2) controller.playerTwoIsPresent = true;


        if(verbose) {
            System.out.printf("Objekt mit Symbol-ID %s hinzugefügt.%n",tobj.getSymbolID());
        }
    }

    @Override
    public void removeTuioObject(TuioObject tobj) {
        gamepads.remove(tobj);

        if(tobj.getSymbolID() == 1) controller.playerOneIsPresent = false;
        if(tobj.getSymbolID() == 2) controller.playerTwoIsPresent = false;

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
