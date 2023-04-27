package com.ivision.gamereact.entity;

import com.ivision.engine.*;
import javafx.scene.Group;
import javafx.scene.effect.Blend;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

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
    Rectangle board = new Rectangle(800,400, Color.TRANSPARENT);
    Rectangle panel = new Rectangle(400,220,GameColor.DARKEN);

    public AudioPlayer (MusicFX audio) {
        fastForwards.setTranslateX(70);
        pausePlay.setTranslateX(140);
        stop.setTranslateX(210);
        fastBackwards.setTranslateX(280);

        mediaBarGroup.getChildren().addAll(
                fastBackwards,
                pausePlay,
                stop,
                fastForwards
        );
        anchor.setTranslateX(400);
        anchor.setTranslateY(200);
        panel.setTranslateY(90);
        panel.setArcHeight(40);
        panel.setArcWidth(40);
        panel.setEffect(new Blend());
        this.audio = audio;
        mediaBarGroup.setTranslateY(120);
        mediaBarGroup.setTranslateX(-20);
        mediaBarGroup.setScaleX(-1);
        getChildren().addAll(
                board,
                panel,
                mediaBarGroup,
                anchor
        );
    }

    public MusicFX getAudio() {
        return audio;
    }

    public void setAudio(MusicFX audio) {
        this.audio = audio;
    }
}
