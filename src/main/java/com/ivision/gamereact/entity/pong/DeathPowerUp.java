package com.ivision.gamereact.entity.pong;

import com.ivision.engine.ImageFX;
import com.ivision.engine.ImageFiles;
import com.ivision.engine.PaddleManipulation;

import java.util.Objects;

public class DeathPowerUp extends PowerUpItem {

    public DeathPowerUp () {
        super(ImageFX.getImage(Objects.requireNonNull(ImageFiles.death)));
    }

    @Override
    public boolean doAction(Paddle affectedPlayer) {
        affectedPlayer.setCurrentHealthPoints(0);
        affectedPlayer.setManipulation(PaddleManipulation.LIFE);
        System.out.println("Leben verloren!");
        return true;
    }

}
