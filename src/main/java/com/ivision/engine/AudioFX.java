package com.ivision.engine;

import javafx.scene.media.AudioClip;

import java.util.Objects;

public enum AudioFX {

    SFX1 ("/wav/b1.wav"),
    SFX2 ("/wav/b2.wav"),
    SFX3 ("/wav/b3.wav");

    private final AudioClip sfx;

    AudioFX(String audioFile) {
        sfx = new AudioClip(Objects.requireNonNull(getClass().getResource(audioFile)).toExternalForm());
    }

    public void setBalance (double value) {
        sfx.setBalance(value);
    }

    public void play() {
        sfx.play();
    }

}
