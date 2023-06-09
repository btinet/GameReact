package com.ivision.gamereact.entity.pong;

import com.ivision.engine.PaddlePosition;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.concurrent.ThreadLocalRandom;

public class Ball extends Circle {

    private double ballSpeed = 10;
    private double ballAngle = 3;
    private double startPosition = -360;

    private int ballHits = 0;

    public Ball(double radius) {
        setRadius(radius);
        setStartPosition(PaddlePosition.LEFT);
        setFill(Color.WHITE);
    }

    /**
     * Setzt Startposition und Flugrichtung des Balls in Abhängigkeit des Spielers, der an der Reihe ist.
     * @param currentPlayer Seite des Spielers, der den Aufschlag durchführt.
     */
    public void setStartPosition (PaddlePosition currentPlayer) {
        switch (currentPlayer) {
            case LEFT:
                startPosition = -Math.abs(startPosition);
                ballSpeed = Math.abs(ballSpeed);
                break;
            case RIGHT:
                startPosition = Math.abs(startPosition);
                ballSpeed = -Math.abs(ballSpeed);
                break;
        }
        setTranslateX(startPosition);
        setTranslateY(0);
    }

    public Boolean intersects (Node node) {
        return this.getBoundsInParent().intersects(node.getBoundsInParent());
    }

    public void reflect() {
        if (ballAngle < 0) {
            setBallAngle(PaddlePosition.LEFT);
        } else {
            setBallAngle(PaddlePosition.RIGHT);
        }
    }

    public void increaseBallHits () {
        this.ballHits = ballHits + ThreadLocalRandom.current().nextInt(1, 13 + 1);
    }

    public void resetBallHits () {
        this.ballHits = 0;
    }

    public Boolean triggerByBallHits() {
        return ballHits % 7 == 0 && ballHits > 0;
    }

    public void moveX() {
        setTranslateX(getTranslateX()+ballSpeed);
    }

    public void moveY() {
        setTranslateY(getTranslateY()+ballAngle);
    }

    public void move() {
        moveX();
        moveY();
    }

    public void setBallSpeed(double ballSpeed) {
        this.ballSpeed = ballSpeed;
    }

    public double getBallSpeed() {
        return ballSpeed;
    }

    public void setBallSpeed(PaddlePosition currentPlayer) {
        switch (currentPlayer) {
            case LEFT:
                ballSpeed = Math.abs(ballSpeed);
                break;
            case RIGHT:
                ballSpeed = -Math.abs(ballSpeed);
                break;
        }
    }

    public void setBallSpeed(Paddle currentPlayer) {
        switch (currentPlayer.getPosition()) {
            case LEFT:
                ballSpeed = Math.abs(currentPlayer.getSpeedFactor());
                break;
            case RIGHT:
                ballSpeed = -Math.abs(currentPlayer.getSpeedFactor());
                break;
        }
    }

    public double getBallAngle() {
        return ballAngle;
    }

    public void setBallAngle(double ballAngle) {
        this.ballAngle = ballAngle;
    }

    public void setBallAngle(PaddlePosition currentBorder) {
        switch (currentBorder) {
            case LEFT:
                ballAngle = Math.abs(ballAngle);
                break;
            case RIGHT:
                ballAngle = -Math.abs(ballAngle);
                break;
        }
    }

}
