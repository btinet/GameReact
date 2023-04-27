package com.ivision.gamereact.entity;

import com.ivision.engine.ImageFX;
import com.ivision.engine.ImageFiles;
import com.ivision.engine.PaddleManipulation;

import java.util.Objects;

import static com.ivision.gamereact.ReactApplication.verbose;

public class PotionPowerUp extends PowerUpItem {

    public PotionPowerUp () {
        super(ImageFX.getImage(Objects.requireNonNull(ImageFiles.heart)));
    }

    @Override
    public boolean doAction(Paddle affectedPlayer) {
        affectedPlayer.resetCurrentHealthPoints();
        affectedPlayer.setManipulation(PaddleManipulation.LIFE);
        if(verbose) System.out.println("Lebenspunkte aufgefrischt!");
        return true;
    }
}
