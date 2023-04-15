package com.ivision.gamereact.view;

import com.ivision.engine.GameColor;
import com.ivision.engine.PaddlePosition;
import com.ivision.gamereact.ReactApplication;
import com.ivision.gamereact.entity.Paddle;
import javafx.animation.FillTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import static com.ivision.gamereact.ReactApplication.height;
import static com.ivision.gamereact.ReactApplication.width;

public class GameBoardDecoration {

    Pane root;

    Circle strafeLeft = new Circle(ReactApplication.height);
    Circle strafeLeftStroke = new Circle(ReactApplication.height+200);
    Circle strafeRight = new Circle(ReactApplication.height);
    Circle strafeRightStroke = new Circle(ReactApplication.height+200);

    Circle leftSide = new Circle(height+200);
    Circle rightSide = new Circle(height+200);
    Circle middleCircleBig = new Circle(50);
    Line middleLine = new Line();

    FillTransition leftTransition;
    FillTransition rightTransition;
    FillTransition leftPowerUpTransition;
    FillTransition rightPowerupTransition;
    ScaleTransition middleCircleScaleUp;
    ScaleTransition middleCircleScaleDown;

    TranslateTransition moveCircle;

    public GameBoardDecoration (Pane root, Paddle playerOne, Paddle playerTwo) {
        this.root = root;
        leftTransition =  Transitions.createFillTransition(150,strafeLeft,GameColor.VIOLETT.darker(),playerTwo.getPrimaryColor(),4);
        rightTransition =  Transitions.createFillTransition(150,strafeRight,GameColor.VIOLETT.darker(),playerOne.getPrimaryColor(),4);
        leftPowerUpTransition =  Transitions.createFillTransition(150,strafeLeft,GameColor.VIOLETT.darker(),GameColor.YELLOW,4);
        rightPowerupTransition =  Transitions.createFillTransition(150,strafeRight,GameColor.VIOLETT.darker(),GameColor.YELLOW,4);
        middleCircleScaleUp = Transitions.createScaleTransition(200,middleCircleBig,1,2);
        middleCircleScaleDown = Transitions.createScaleTransition(200,middleCircleBig,2,1);
        moveCircle = Transitions.createTranslateTransition(200, middleCircleBig);
        paint();
        addToStage();
    }

    private void paint () {
        strafeLeft.setTranslateX(-width);
        strafeLeft.setFill(GameColor.VIOLETT.darker());
        strafeRight.setTranslateX(width);
        strafeRight.setFill(GameColor.VIOLETT.darker());

        strafeLeftStroke.setTranslateX(-width-100);
        strafeLeftStroke.setStrokeWidth(2);
        strafeLeftStroke.setStroke(GameColor.YELLOW);
        strafeLeftStroke.setFill(Color.TRANSPARENT);

        strafeRightStroke.setTranslateX(width+100);
        strafeRightStroke.setStrokeWidth(2);
        strafeRightStroke.setStroke(GameColor.YELLOW);
        strafeRightStroke.setFill(Color.TRANSPARENT);

        middleLine.setStroke(GameColor.VIOLETT);
        middleLine.setStrokeWidth(4);
        middleLine.setStartY(0);
        middleLine.setEndY(height);

        middleCircleBig.setFill(GameColor.VIOLETT.darker());


        leftSide.setTranslateX(-width-88);
        leftSide.setStrokeWidth(20);
        leftSide.setStroke(GameColor.VIOLETT);
        leftSide.setFill(Color.TRANSPARENT);


        rightSide.setTranslateX(width+88);
        rightSide.setStrokeWidth(20);
        rightSide.setStroke(GameColor.VIOLETT);
        rightSide.setFill(Color.TRANSPARENT);
    }

    private void addToStage () {
        root.getChildren().addAll(
                strafeLeft,
                strafeRight,
                strafeLeftStroke,
                strafeRightStroke,
                middleCircleBig,
                middleLine,
                leftSide,
                rightSide
        );
    }

    public Circle getMiddleCircleBig () {
        return middleCircleBig;
    }

    public TranslateTransition getMoveCircleTransition() {
        return moveCircle;
    }

    public ScaleTransition getMiddleCircleScaleUp() {
        return middleCircleScaleUp;
    }

    public ScaleTransition getMiddleCircleScaleDown() {
        return middleCircleScaleDown;
    }

    public void playPowerUpAnimation (PaddlePosition position) {
        switch (position) {
            case LEFT:
                leftPowerUpTransition.play();
                break;
            case RIGHT:
                rightPowerupTransition.play();
                break;
        }
    }

    public void playStrafeAnimation (PaddlePosition position) {
        switch (position) {
            case LEFT:
                rightTransition.play();
                break;
            case RIGHT:
                leftTransition.play();
                break;
        }
    }

}
