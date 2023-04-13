package com.ivision.gamereact.entity;

import com.ivision.engine.*;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.Objects;

public class Paddle extends Line {

    // Klassenattribute

    protected Integer ObjectId;
    protected Color primaryColor;
    protected Color secondaryColor;
    protected Integer px;
    protected Integer py;
    PaddleManipulation manipulation;
    PaddlePosition position;
    protected Integer healthPoints;
    protected Integer currentHealthPoints;
    protected ArrayList<Circle> healthPointCircles = new ArrayList<>();

    protected Arc timerIndicator = new Arc();
    protected Group healthPointGroup;
    protected Integer matchPoints = 0;
    protected AudioFX primarySound;
    protected AudioFX secondarySound;

    Text pointsText = new Text(String.valueOf(matchPoints));


    // Constructors

    public Paddle(PaddlePosition position, int healthPoints, Color primaryColor) {

        this.position = position;
        this.healthPoints = healthPoints;
        this.currentHealthPoints = healthPoints;
        this.primaryColor = primaryColor;
        this.secondaryColor = Color.BLACK;

        this.timerIndicator.setType(ArcType.OPEN);
        this.timerIndicator.setStartAngle(0);
        this.timerIndicator.setRadiusX(10);
        timerIndicator.setCenterX(10);
        this.timerIndicator.setRadiusY(10);
        this.timerIndicator.setStroke(GameColor.YELLOW);
        this.timerIndicator.setFill(null);
        this.timerIndicator.setStrokeWidth(3);
        this.timerIndicator.setStrokeLineCap(StrokeLineCap.ROUND);
        this.timerIndicator.setLength(0);

        setStartY(0);
        setEndY(70);
        setStroke(Color.WHITE);
        setStrokeWidth(8);
        strokeLineCapProperty().setValue(StrokeLineCap.ROUND);

        healthPointGroup = new Group();

        addHealthPointCircles();

        switch (this.position) {
            case LEFT:
                setTranslateX(-400);
                setId("ROT");
                primarySound = AudioFX.SFX1;
                primarySound.setBalance(-0.75);
                healthPointGroup.setTranslateX(-600);
                this.timerIndicator.setTranslateX(-630);
                timerIndicator.setScaleY(-1);
                healthPointGroup.setScaleX(-1);
                pointsText.setTranslateX(-485);
                pointsText.setTranslateY(-7);
                pointsText.setRotate(90);
                break;
            case RIGHT:
                setTranslateX(400);
                setId("BLAU");
                primarySound = AudioFX.SFX2;
                primarySound.setBalance(0.75);
                healthPointGroup.setTranslateX(600);
                this.timerIndicator.setTranslateX(630);
                timerIndicator.setScaleX(-1);
                pointsText.setTranslateX(485);
                pointsText.setTranslateY(7);
                pointsText.setRotate(-90);
                break;
        }


        pointsText.setTextAlignment(TextAlignment.CENTER);
        pointsText.setFont(Fonts.REGULAR_46.getFont());
        pointsText.setFill(Color.WHITE);
        pointsText.setScaleX(-1);

    }

    // Methoden

    public Boolean intersects (Node node) {
        return this.getBoundsInParent().intersects(node.getBoundsInParent());
    }

    public Arc getTimerIndicator () {
        return timerIndicator;
    }

    public void increaseTimerIndicator (double length) {
        if(length >= 0 && length <= 360) this.timerIndicator.setLength(length);
    }

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
            setPointsText(matchPoints);
    }

    public void decreaseMatchPoints () {
        if(matchPoints > 0) {
            matchPoints--;
            setPointsText(matchPoints);
        }
    }

    public void resetCurrentHealthPoints() {
        this.currentHealthPoints = healthPoints;
        updateHealthPointCircles();
    }


    // Getter und Setter für die Klassenattribute

    public PaddleManipulation getManipulation() {
        return manipulation;
    }

    public void setManipulation(PaddleManipulation manipulation) {
        this.manipulation = manipulation;
    }

    public PaddlePosition getPosition() {
        return position;
    }

    public void setPosition(PaddlePosition position) {
        this.position = position;
    }

    public Integer getCurrentHealthPoints() {
        return currentHealthPoints;
    }

    public void setCurrentHealthPoints(Integer currentHealthPoints) {
        this.currentHealthPoints = currentHealthPoints;
        updateHealthPointCircles();
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

    public void reset () {
        setTranslateY(0);
        resetCurrentHealthPoints();
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

    public Text getPointsText() {
        return pointsText;
    }

    public void setPointsText(int value) {
        this.pointsText.setText(String.valueOf(value));
    }
}
