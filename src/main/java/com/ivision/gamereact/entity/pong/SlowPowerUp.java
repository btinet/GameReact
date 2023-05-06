package com.ivision.gamereact.entity.pong;

import com.ivision.engine.ImageFX;
import com.ivision.engine.ImageFiles;
import com.ivision.engine.PaddleManipulation;

import java.util.Objects;

import static com.ivision.gamereact.ReactApplication.verbose;

public class SlowPowerUp extends PowerUpItem {

    public SlowPowerUp() {
        super(ImageFX.getImage(Objects.requireNonNull(ImageFiles.snail)));
    }

    @Override
    public boolean doAction(Paddle affectedPlayer) {
        // TODO: Bug, wenn Power Up abgelaufen. Ball wird nochmals langsamer.
        affectedPlayer.setSpeedFactor(affectedPlayer.getSpeedFactor()/2);
        affectedPlayer.setManipulation(PaddleManipulation.SPEED);
        if(verbose) System.out.println("Ball fliegt langsamer!");
        return true;
    }
}
