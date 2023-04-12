package com.ivision.gamereact.entity;

import com.ivision.engine.ImageFX;
import com.ivision.engine.ImageFiles;
import com.ivision.engine.PaddleManipulation;

import java.util.Objects;

public class ShrinkPowerUp extends PowerUpItem {

    public ShrinkPowerUp () {
        super(ImageFX.getImage(Objects.requireNonNull(ImageFiles.shrink)));
    }

    @Override
    public boolean doAction(Paddle affectedPlayer) {
        affectedPlayer.setEndY(50);
        affectedPlayer.setManipulation(PaddleManipulation.WIDTH);
        System.out.println("geschrumpft!");
        return true;
    }

}
