package com.ivision.gamereact.entity;

import com.ivision.engine.ImageFX;
import com.ivision.engine.ImageFiles;
import com.ivision.engine.PaddleManipulation;

import java.util.Objects;

public class PotionPowerUp extends PowerUpItem {

    public PotionPowerUp () {
        super(ImageFX.getImage(Objects.requireNonNull(ImageFiles.heart)));
    }

    @Override
    public boolean doAction(Paddle affectedPlayer) {
        affectedPlayer.resetCurrentHealthPoints();
        affectedPlayer.setManipulation(PaddleManipulation.LIFE);
        System.out.println("Lebenspunkte aufgefrischt!");
        return true;
    }
}
