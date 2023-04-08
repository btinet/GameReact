package com.ivision.gamereact.entity;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.Collection;

public class Paddle extends Line {

    // Klassenattribute

    protected Integer ObjectId;
    protected Color primaryColor;
    protected Color secondaryColor;
    protected Integer px;
    protected Integer py;
    protected Double currentDirectionY;
    protected Integer healthPoints;
    protected ArrayList<Circle> healthPointCircles = new ArrayList<>();
    protected Group healthPointGroup;
    protected Integer matchPoints;
    protected AudioClip primarySound;
    protected AudioClip secondarySound;


    // Constructors

    public Paddle(int healthPoints) {
        this.healthPoints = healthPoints;

        healthPointGroup = new Group();

        for (int i = 0;i < healthPoints;i++) {
            healthPointCircles.add(new Circle(100,Color.WHITE));
            healthPointGroup.getChildren().add(healthPointCircles.get(i));
        }

        healthPointCircles.get(4).setFill(Color.GREEN);

        System.out.printf("Dem Spieler wurden %s Punkte zugewiesen. ",healthPoints);
        System.out.printf("Deshalb gibt es %s Kreise in der Sammlung.%n",healthPointGroup.getChildren().size());
    }

    // Getter und Setter für die Klassenattribute


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

    public Double getCurrentDirectionY() {
        return currentDirectionY;
    }

    public void setCurrentDirectionY(Double currentDirectionY) {
        this.currentDirectionY = currentDirectionY;
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

    public AudioClip getPrimarySound() {
        return primarySound;
    }

    public void setPrimarySound(AudioClip primarySound) {
        this.primarySound = primarySound;
    }

    public AudioClip getSecondarySound() {
        return secondarySound;
    }

    public void setSecondarySound(AudioClip secondarySound) {
        this.secondarySound = secondarySound;
    }
}
