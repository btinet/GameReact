package com.ivision.gamereact.entity;

import com.ivision.engine.AudioFX;
import com.ivision.engine.PaddlePosition;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.Objects;

public class Paddle extends Line {

    // Klassenattribute

    protected Integer ObjectId;
    protected Color primaryColor;
    protected Color secondaryColor;
    protected Integer px;
    protected Integer py;
    protected Integer healthPoints;
    protected Integer currentHealthPoints;
    protected ArrayList<Circle> healthPointCircles = new ArrayList<>();
    protected Group healthPointGroup;
    protected Integer matchPoints = 0;
    protected AudioFX primarySound;
    protected AudioFX secondarySound;


    // Constructors

    public Paddle(PaddlePosition position, int healthPoints, Color primaryColor) {

        this.healthPoints = healthPoints;
        this.currentHealthPoints = healthPoints;
        this.primaryColor = primaryColor;
        this.secondaryColor = Color.BLACK;

        healthPointGroup = new Group();

        addHealthPointCircles();

        switch (position) {
            case LEFT:
                setTranslateX(-400);
                setId("ROT");
                primarySound = AudioFX.SFX1;
                primarySound.setBalance(-0.75);
                healthPointGroup.setTranslateX(-600);
                healthPointGroup.setScaleX(-1);
                break;
            case RIGHT:
                setTranslateX(400);
                setId("BLAU");
                primarySound = AudioFX.SFX2;
                primarySound.setBalance(0.75);
                healthPointGroup.setTranslateX(600);
                break;
        }

    }

    // Methoden

    private void addHealthPointCircles() {

        int y = 0;
        double x = 0;
        for (int i = 0;i < healthPoints;i++) {

            healthPointCircles.add(new Circle(10,primaryColor));
            healthPointGroup.getChildren().add(healthPointCircles.get(i));

            if(i % 2 != 0) {
                y = y + 25;
                x = 4*Math.log(1+i*0.125)+i;
                System.out.println(x);
            }

            healthPointCircles.get(i).setTranslateY(y);
            healthPointCircles.get(i).setTranslateX(x);

            if(i % 2 == 0 && i > 0) {
                healthPointCircles.get(i).setTranslateY(-healthPointCircles.get(i-1).getTranslateY());
                healthPointCircles.get(i).setTranslateX(healthPointCircles.get(i-1).getTranslateX());
            }

        }

        updateHealthPointCircles();

    }

    private void updateHealthPointCircles () {

        if(!Objects.equals(currentHealthPoints, healthPoints)) {
            for (int i = healthPoints-1; i >= currentHealthPoints; i--) {
                this.healthPointGroup.getChildren().get(i).setOpacity(.2);
            }
        } else {
            for (int i = 0; i < healthPoints; i++) {
                this.healthPointGroup.getChildren().get(i).setOpacity(1);
            }
        }

    }

    public void increaseHealthPoints () {
        if(currentHealthPoints < healthPoints) {
            currentHealthPoints++;
            updateHealthPointCircles();
        }
    }

    public void decreaseHealthPoints () {
        if(currentHealthPoints > 0) {
            currentHealthPoints--;
            updateHealthPointCircles();
        }
    }

    public void increaseMatchPoints () {
            matchPoints++;
    }

    public void decreaseMatchPoints () {
        if(matchPoints > 0) {
            matchPoints--;
        }
    }

    public void resetCurrentHealthPoints() {
        this.currentHealthPoints = healthPoints;
        updateHealthPointCircles();
    }


    // Getter und Setter für die Klassenattribute

    public Integer getCurrentHealthPoints() {
        return currentHealthPoints;
    }

    public void setCurrentHealthPoints(Integer currentHealthPoints) {
        this.currentHealthPoints = currentHealthPoints;
    }

    public ArrayList<Circle> getHealthPointCircles() {
        return healthPointCircles;
    }

    public void setHealthPointCircles(ArrayList<Circle> healthPointCircles) {
        this.healthPointCircles = healthPointCircles;
    }

    public Group getHealthPointGroup() {
        return healthPointGroup;
    }

    public void setHealthPointGroup(Group healthPointGroup) {
        this.healthPointGroup = healthPointGroup;
    }

    public Integer getObjectId() {
        return ObjectId;
    }

    public void setObjectId(Integer objectId) {
        ObjectId = objectId;
    }

    public Color getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(Color primaryColor) {
        this.primaryColor = primaryColor;
    }

    public Color getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(Color secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public Integer getPx() {
        return px;
    }

    public void setPx(Integer px) {
        this.px = px;
    }

    public Integer getPy() {
        return py;
    }

    public void setPy(Integer py) {
        this.py = py;
    }

    public Integer getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(Integer healthPoints) {
        this.healthPoints = healthPoints;
    }

    public Integer getMatchPoints() {
        return matchPoints;
    }

    public void setMatchPoints(Integer matchPoints) {
        this.matchPoints = matchPoints;
    }

    public AudioFX getPrimarySound() {
        return primarySound;
    }

    public void setPrimarySound(AudioFX primarySound) {
        this.primarySound = primarySound;
    }

    public AudioFX getSecondarySound() {
        return secondarySound;
    }

    public void setSecondarySound(AudioFX secondarySound) {
        this.secondarySound = secondarySound;
    }
}
