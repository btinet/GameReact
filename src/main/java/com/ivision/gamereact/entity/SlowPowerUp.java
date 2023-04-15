package com.ivision.gamereact.entity;

import com.ivision.engine.ImageFX;
import com.ivision.engine.ImageFiles;
import com.ivision.engine.PaddleManipulation;

import java.util.Objects;

public class SlowPowerUp extends PowerUpItem {

    public SlowPowerUp() {
        super(ImageFX.getImage(Objects.requireNonNull(ImageFiles.snail)));
    }

    @Override
    public boolean doAction(Paddle affectedPlayer) {
        affectedPlayer.setSpeedFactor(.5);
        affectedPlayer.setManipulation(PaddleManipulation.SPEED);
        System.out.println("Ball fliegt langsamer!");
        return true;
    }
}
