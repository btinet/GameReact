package com.ivision.gamereact.entity;

import com.ivision.engine.ImageFX;
import com.ivision.engine.ImageFiles;
import com.ivision.engine.PaddleManipulation;

import java.util.Objects;

public class ShufflePowerUp extends PowerUpItem {

    public ShufflePowerUp () {
        super(ImageFX.getImage(Objects.requireNonNull(ImageFiles.shuffle)));
    }

    @Override
    public boolean doAction(Paddle affectedPlayer) {
        affectedPlayer.setManipulation(PaddleManipulation.CONFUSE);
        affectedPlayer.setInverter(-1);
        System.out.println("Verwirrt!");
        return true;
    }

}
