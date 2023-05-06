package com.ivision.gamereact.entity.pong;

import com.ivision.engine.ImageFX;
import com.ivision.engine.ImageFiles;
import com.ivision.engine.PaddleManipulation;

import java.util.Objects;

import static com.ivision.gamereact.ReactApplication.verbose;

public class ShrinkPowerUp extends PowerUpItem {

    public ShrinkPowerUp () {
        super(ImageFX.getImage(Objects.requireNonNull(ImageFiles.shrink)));
    }

    @Override
    public boolean doAction(Paddle affectedPlayer) {
        affectedPlayer.setEndY(50);
        affectedPlayer.setManipulation(PaddleManipulation.WIDTH);
        if(verbose) System.out.println("geschrumpft!");
        return true;
    }

}
