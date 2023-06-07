package com.ivision.gamereact.entity.app;

import com.ivision.engine.*;
import com.ivision.gamereact.controller.AppController;
import javafx.scene.Group;
import javafx.scene.effect.Blend;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

import static com.ivision.gamereact.ReactApplication.height;
import static com.ivision.gamereact.ReactApplication.width;

public class AudioPlayer extends Group {

    MusicFX audio;
    Circle anchor = new Circle(60,GameColor.YELLOW);

    Group mediaBarGroup = new Group();
    ImageView backwards = ImageFX.getImage(ImageFiles.back);
    ImageView fastBackwards = ImageFX.getImage(ImageFiles.back);
    ImageView stop = ImageFX.getImage(ImageFiles.stop);
    ImageView pausePlay = ImageFX.getImage(ImageFiles.play);
    ImageView fastForwards = ImageFX.getImage(ImageFiles.next);
    ImageView forwards = ImageFX.getImage(ImageFiles.next);
    ImageView muteAudio = ImageFX.getImage(ImageFiles.stop);

    AppController controller;

    Circle playCircle = new Circle(20,GameColor.YELLOW);
    Rectangle board = new Rectangle(800,400, Color.TRANSPARENT);
    Rectangle panel = new Rectangle(400,220,GameColor.DARKEN);

    Text titel = new Text();

    public AudioPlayer (MusicFX audio, String titel, AppController controller) {
        this.controller = controller;
        this.titel.setText(titel);
        this.titel.setFont(Fonts.BOLD_18.getFont());
        this.titel.setFill(Color.WHITE);
        this.titel.setTranslateX(100);
        this.titel.setTranslateY(-50);
        fastForwards.setTranslateX(70);
        stop.setTranslateX(140);
        pausePlay.setTranslateX(210);
        playCircle.setTranslateX(210);
        pausePlay.setScaleX(1);
        fastBackwards.setTranslateX(280);

        mediaBarGroup.getChildren().addAll(
                fastBackwards,
                stop,
                pausePlay,
                fastForwards,
                this.titel
        );
        anchor.setTranslateX(400);
        anchor.setTranslateY(200);
        panel.setTranslateX(400);
        panel.setTranslateY(90);
        panel.setArcHeight(40);
        panel.setArcWidth(40);
        panel.setEffect(new Blend());
        this.audio = audio;
        mediaBarGroup.setTranslateY(220);
        mediaBarGroup.setTranslateX(420);
        mediaBarGroup.setScaleX(-1);
        getChildren().addAll(
                board,
                panel,
                mediaBarGroup,
                anchor
        );
        //getAudio().play();
    }

    public ImageView getBackwards() {
        return backwards;
    }

    public ImageView getFastBackwards() {
        return fastBackwards;
    }

    public ImageView getStop() {
        return stop;
    }

    public ImageView getPausePlay() {
        return pausePlay;
    }

    public void touch(Shape shape,double sx, double sy) {

        // TODO: Für jedes aktive Feld Koordinaten ermitteln und vergleichen.
        // TODO: foreach in eigene Methode auslagern.

        double fx = shape.getBoundsInParent().getCenterX()+width/2;
        double fy = shape.getBoundsInParent().getCenterY()+height/2;
        double px = getPausePlay().localToScene(getPausePlay().getBoundsInLocal()).getCenterX()-(sx);
        double py = getPausePlay().localToScene(getPausePlay().getBoundsInLocal()).getCenterY()-(sy);

        System.out.printf("Finger (%s|%s) und Play(%s|%s)%n",fx,fy,px,py);

        if(Math.abs(fx-px) < 25 && Math.abs(fy-py) < 25) {
            System.out.println("Knopf getroffen!");
            if(getAudio().isPlaying()) {
                getAudio().stop();
                controller.startPong();
            } else {
                getAudio().play();
            }
        }

    }

    public ImageView getFastForwards() {
        return fastForwards;
    }

    public ImageView getForwards() {
        return forwards;
    }

    public ImageView getMuteAudio() {
        return muteAudio;
    }

    public MusicFX getAudio() {
        return audio;
    }

    public void setAudio(MusicFX audio) {
        this.audio = audio;
    }
}
