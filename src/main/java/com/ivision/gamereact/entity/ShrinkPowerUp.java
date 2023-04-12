package com.ivision.gamereact.entity;

import com.ivision.engine.ImageFX;
import com.ivision.engine.ImageFiles;

import java.util.Objects;

public class ShrinkPowerUp extends PowerUpItem {

    public ShrinkPowerUp () {
        super(ImageFX.getImage(Objects.requireNonNull(ImageFiles.shrink)));
    }

    @Override
    public void doAction(Paddle affectedPlayer) {

    }

}
