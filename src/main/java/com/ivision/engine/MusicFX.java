package com.ivision.engine;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.util.Objects;

public enum MusicFX {

    MAZE ("/wav/tmp_maze2.stm.mp3"),
    THE_GRID ("/wav/tmp_lc_grid.stm.mp3");

    private final MediaPlayer mediaPlayer;

    MusicFX(String audioFile) {
        Media music = new Media(Objects.requireNonNull(getClass().getResource(audioFile)).toExternalForm());
        mediaPlayer = new MediaPlayer(music);
        mediaPlayer.setCycleCount(-1);
    }

    public void setCycleCount(int duration) {
        mediaPlayer.setCycleCount(duration);
    }

    public void play() {
        mediaPlayer.play();
    }

    public void pause() {mediaPlayer.pause();}

    public void stop() {mediaPlayer.stop();}

}
