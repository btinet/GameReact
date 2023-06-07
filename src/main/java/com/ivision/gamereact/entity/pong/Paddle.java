package com.ivision.gamereact.entity.pong;

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
    protected Color primaryColor;
    protected Color secondaryColor;
    protected int inverter = 1;
    protected double speedFactor = 0;
    protected Integer healthPoints;
    protected Integer currentHealthPoints;
    protected ArrayList<Circle> healthPointCircles = new ArrayList<>();
    protected Group timerIndicator = new Group();
    protected ArrayList<Rectangle> timerIndicatorLeds = new ArrayList<>();
    protected Group healthPointGroup;
    protected Integer matchPoints = 0;
    protected AudioFX primarySound;
    PaddleManipulation manipulation;
    PaddlePosition position;
    Text pointsText = new Text(String.valueOf(matchPoints));


    // Constructors

    public Paddle(PaddlePosition position, int healthPoints, Color primaryColor) {

        this.position = position;
        this.healthPoints = healthPoints;
        this.currentHealthPoints = healthPoints;
        this.primaryColor = primaryColor;
        this.secondaryColor = Color.BLACK;

        setStartY(0);
        setEndY(70);
        setStroke(Color.WHITE);
        setStrokeWidth(8);
        strokeLineCapProperty().setValue(StrokeLineCap.ROUND);

        initTimerGroup();

        healthPointGroup = new Group();

        addHealthPointCircles();

        switch (this.position) {
            case LEFT:
                setTranslateX(-400);
                setId("ROT");
                primarySound = AudioFX.SFX1;
                primarySound.setBalance(-0.75);
                healthPointGroup.setTranslateX(-600);
                this.timerIndicator.setTranslateX(-524);
                timerIndicator.setScaleY(1);
                healthPointGroup.setScaleX(1);
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
                this.timerIndicator.setTranslateX(524);
                timerIndicator.setScaleX(1);
                pointsText.setTranslateX(485);
                pointsText.setTranslateY(7);
                pointsText.setRotate(-90);
                break;
        }


        pointsText.setTextAlignment(TextAlignment.CENTER);
        pointsText.setFont(Fonts.REGULAR_46.getFont());
        pointsText.setFill(Color.WHITE);
        pointsText.setScaleX(1);

    }

    // Methoden
    public int getInverter() {
        return inverter;
    }

    public void setInverter(int value) {
        this.inverter = value;
    }

    public double getSpeedFactor() {
        return speedFactor;
    }

    public void setSpeedFactor(double speedFactor) {
        this.speedFactor = speedFactor;
    }

    public Boolean intersects(Node node) {
        return this.getBoundsInParent().intersects(node.getBoundsInParent());
    }

    public Group getTimerIndicator() {
        return timerIndicator;
    }

    public void initTimerGroup() {
        timerIndicator.getChildren().removeAll(timerIndicatorLeds);
        timerIndicatorLeds = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Rectangle rectangle = new Rectangle(8, 16, getPrimaryColor());
            rectangle.setTranslateY(i * 18);
            rectangle.setTranslateX(-50);
            rectangle.setArcHeight(4);
            rectangle.setArcWidth(4);
            timerIndicatorLeds.add(rectangle);
        }
        timerIndicator.setOpacity(0);
        timerIndicator.getChildren().addAll(timerIndicatorLeds);
    }

    public void increaseTimerIndicator(int length) {
        if (length != -1 && manipulation != PaddleManipulation.LIFE) {
            timerIndicator.setOpacity(1);
            if (length < timerIndicatorLeds.size()) {
                AudioFX.second.play();
                this.timerIndicatorLeds.get(length).setFill(GameColor.DARKEN);
            }
        } else {
            initTimerGroup();
        }
    }

    private void addHealthPointCircles() {

        int y = 0;
        double x = 0;
        for (int i = 0; i < healthPoints; i++) {

            healthPointCircles.add(new Circle(10, primaryColor));
            healthPointGroup.getChildren().add(healthPointCircles.get(i));

            if (i % 2 != 0) {
                y = y + 25;
                x = 4 * Math.log(1 + i * 0.125) + i;
            }

            healthPointCircles.get(i).setTranslateY(y);
            healthPointCircles.get(i).setTranslateX(x);

            if (i % 2 == 0 && i > 0) {
                healthPointCircles.get(i).setTranslateY(-healthPointCircles.get(i - 1).getTranslateY());
                healthPointCircles.get(i).setTranslateX(healthPointCircles.get(i - 1).getTranslateX());
            }

        }

        updateHealthPointCircles();

    }

    private void updateHealthPointCircles() {

        if (!Objects.equals(currentHealthPoints, healthPoints)) {
            for (int i = healthPoints - 1; i >= currentHealthPoints; i--) {
                this.healthPointGroup.getChildren().get(i).setOpacity(.2);
            }
        } else {
            for (int i = 0; i < healthPoints; i++) {
                this.healthPointGroup.getChildren().get(i).setOpacity(1);
            }
        }

    }

    public void decreaseHealthPoints() {
        if (currentHealthPoints > 0) {
            currentHealthPoints--;
            updateHealthPointCircles();
        }
    }

    public void increaseMatchPoints() {
        matchPoints++;
        setPointsText(matchPoints);
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

    public Integer getCurrentHealthPoints() {
        return currentHealthPoints;
    }

    public void setCurrentHealthPoints(Integer currentHealthPoints) {
        this.currentHealthPoints = currentHealthPoints;
        updateHealthPointCircles();
    }

    public Group getHealthPointGroup() {
        return healthPointGroup;
    }

    public Color getPrimaryColor() {
        return primaryColor;
    }

    public void reset() {
        setEndY(70);
        setTranslateY(0);
        setInverter(1);
        resetCurrentHealthPoints();
        setManipulation(null);
        initTimerGroup();
    }

    public AudioFX getPrimarySound() {
        return primarySound;
    }

    public Text getPointsText() {
        return pointsText;
    }

    public void setPointsText(int value) {
        this.pointsText.setText(String.valueOf(value));
    }
}
