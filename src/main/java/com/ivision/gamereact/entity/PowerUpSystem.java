package com.ivision.gamereact.entity;


import com.ivision.engine.AudioFX;
import com.ivision.engine.GameColor;
import com.ivision.gamereact.view.GameBoardDecoration;
import com.ivision.gamereact.view.Transitions;
import javafx.animation.FadeTransition;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.util.concurrent.ThreadLocalRandom;

public class PowerUpSystem extends Group {

    Pane root;
    Ball ball;
    GameBoardDecoration gbd;
    PowerUpItem powerUp;
    Rectangle powerUpCircle = new Rectangle(36, 36, GameColor.VIOLETT.darker());
    AudioFX powerUpFX = AudioFX.collect;
    FadeTransition powerUpVanish;
    int powerUpTimer = 1000;
    int startTime = 0;
    int powerUpTime = 0;
    boolean powerUpSpawned = false;
    boolean powerUpCollected = false;

    public PowerUpSystem (Pane root, Ball ball, GameBoardDecoration gbd) {

        this.root = root;
        this.ball = ball;
        this.gbd = gbd;
        powerUpCircle.setArcWidth(16);
        powerUpCircle.setArcHeight(16);
        powerUpCircle.setStroke(GameColor.VIOLETT);
        powerUpCircle.setStrokeWidth(2);
        getChildren().add(powerUpCircle);
        updatePosition();
    }

    public void setPowerUp () {
        if (!powerUpSpawned && !powerUpCollected) {
            System.out.println("Power Up landet auf dem Spielfeld");
            ball.resetBallHits();

            if(powerUp != null) getChildren().remove(powerUp.icon);

            switch (ThreadLocalRandom.current().nextInt(1, 5 + 1)) {
                case 1:
                    powerUp = new PotionPowerUp();
                    break;
                case 2:
                    powerUp = new DeathPowerUp();
                    break;
                case 3:
                    powerUp = new ShrinkPowerUp();
                    break;
                case 4:
                    powerUp = new StretchPowerUp();
                    break;
                case 5:
                    powerUp = new ShufflePowerUp();
                    break;
            }

            getChildren().add(powerUp.icon);
            addPowerUp();
        }
    }

    private void addPowerUp () {
        powerUpVanish = Transitions.createFadeTransition(200,powerUp.icon,1,.4);
        powerUpVanish.stop();
        if(!root.getChildren().contains(this)) root.getChildren().add(this);
        powerUpSpawned = true;
    }

    private void updatePosition () {
        setTranslateX(ThreadLocalRandom.current().nextInt(-140, 140 + 1));
        setTranslateY(ThreadLocalRandom.current().nextInt(-270, 270 + 1));
    }

    public void shutDown() {
        ball.resetBallHits();
        this.setOpacity(1);
        powerUpVanish = null;
        powerUpSpawned = false;
        startTime = 0;
        root.getChildren().remove(this);
        System.out.println("Power-Up-System zurückgesetzt!");

    }

    public void run (Paddle currentPlayer) {
        if(powerUpSpawned && startTime < powerUpTimer) {
            startTime++;
            if(powerUpTimer-startTime == 200) powerUpVanish.play();
            if(ball.intersects(this)) {
                shutDown();
                powerUpFX.play();
                powerUpCollected = true;

                // TODO: Timer für eingesammelten Power Up starten und doAction() vom PowerUp ausführen!
                // Ende

                gbd.playPowerUpAnimation(currentPlayer.getPosition());
                System.out.println("Power Up aufgesammelt!");
                doAction(currentPlayer);
            }
        } else {
            if (powerUpSpawned) {
                System.out.println("Power Up verschwunden!");
                shutDown();
            }

        }
    }

    public void doAction (Paddle affectedPlayer) {
        while (true) {
            if (powerUp.doAction(affectedPlayer)) break;
        }
    }

    public void runPowerUpTimer (Paddle affectedPlayer) {
        if(powerUpTime < 1000) {
            powerUpTime++;
        } else {
            this.powerUpCollected = false;
            this.powerUpTime = 0;
            System.out.println("PowerUp-Wirkung hat nachgelassen.");
            switch (affectedPlayer.getManipulation()) {
                case WIDTH:
                    affectedPlayer.setEndY(70);
                    break;
                case CONFUSE:
                    // TODO: Umkehrung der Steuerung aufheben.
                    break;
            }
            affectedPlayer.setManipulation(null);
        }

    }

}
