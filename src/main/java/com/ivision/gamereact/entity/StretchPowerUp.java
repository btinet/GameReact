package com.ivision.gamereact.entity;

import com.ivision.engine.ImageFX;
import com.ivision.engine.ImageFiles;
import com.ivision.engine.PaddleManipulation;

import java.util.Objects;

public class StretchPowerUp extends PowerUpItem {

    public StretchPowerUp () {
        super(ImageFX.getImage(Objects.requireNonNull(ImageFiles.stretch)));
    }

    @Override
    public boolean doAction(Paddle affectedPlayer) {
        affectedPlayer.setEndY(100);
        affectedPlayer.setManipulation(PaddleManipulation.WIDTH);
        System.out.println("Gewachsen!");
        return true;
    }

}
