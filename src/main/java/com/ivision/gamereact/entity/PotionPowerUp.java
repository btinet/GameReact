package com.ivision.gamereact.entity;

import com.ivision.engine.ImageFX;
import com.ivision.engine.ImageFiles;

import java.util.Objects;

public class PotionPowerUp extends PowerUpItem {

    public PotionPowerUp () {
        super(ImageFX.getImage(Objects.requireNonNull(ImageFiles.heart)));
    }

    @Override
    public void doAction(Paddle affectedPlayer) {

    }

}
