package com.ivision.engine;

import javafx.scene.media.AudioClip;

import java.util.Objects;

public enum AudioFX {

    SFX1 ("/wav/b1.wav"),
    SFX2 ("/wav/b2.wav"),
    SFX3 ("/wav/b3.wav"),
    bWinSFX ( "/wav/bWin.wav"),
    rWinSFX ("/wav/rWin.wav"),
    bHitSFX ("/wav/bHit.wav"),
    rHitSFX ("/wav/rHit.wav"),
    pongReactSFX ("/wav/pongReact.wav"),
    confirm ("/wav/confirm.wav"),
    cancel ("/wav/cancel.wav"),
    click ("/wav/click.wav");

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
