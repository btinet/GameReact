package com.ivision.gamereact.entity.pong;

import com.ivision.engine.ImageFX;
import com.ivision.engine.ImageFiles;
import com.ivision.engine.PaddleManipulation;

import java.util.Objects;

import static com.ivision.gamereact.ReactApplication.verbose;

public class ShufflePowerUp extends PowerUpItem {

    public ShufflePowerUp () {
        super(ImageFX.getImage(Objects.requireNonNull(ImageFiles.shuffle)));
    }

    @Override
    public boolean doAction(Paddle affectedPlayer) {
        affectedPlayer.setManipulation(PaddleManipulation.CONFUSE);
        affectedPlayer.setInverter(-1);
        if(verbose) System.out.println("Verwirrt!");
        return true;
    }

}
