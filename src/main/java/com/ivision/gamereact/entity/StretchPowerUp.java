package com.ivision.gamereact.entity;

import com.ivision.engine.ImageFX;
import com.ivision.engine.ImageFiles;

import java.util.Objects;

public class StretchPowerUp extends PowerUpItem {

    public StretchPowerUp () {
        super(ImageFX.getImage(Objects.requireNonNull(ImageFiles.stretch)));
    }

    @Override
    public void doAction(Paddle affectedPlayer) {

    }

}
