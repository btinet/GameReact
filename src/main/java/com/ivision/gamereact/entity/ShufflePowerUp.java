package com.ivision.gamereact.entity;

import com.ivision.engine.ImageFX;
import com.ivision.engine.ImageFiles;

import java.util.Objects;

public class ShufflePowerUp extends PowerUpItem {

    public ShufflePowerUp () {
        super(ImageFX.getImage(Objects.requireNonNull(ImageFiles.shuffle)));
    }

    @Override
    public void doAction(Paddle affectedPlayer) {

    }

}
