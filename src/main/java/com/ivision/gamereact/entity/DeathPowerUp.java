package com.ivision.gamereact.entity;

import com.ivision.engine.ImageFX;
import com.ivision.engine.ImageFiles;

import java.util.Objects;

public class DeathPowerUp extends PowerUpItem {

    public DeathPowerUp () {
        super(ImageFX.getImage(Objects.requireNonNull(ImageFiles.death)));
    }

    @Override
    public void doAction(Paddle affectedPlayer) {

    }

}
