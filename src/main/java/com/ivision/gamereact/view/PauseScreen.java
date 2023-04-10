package com.ivision.gamereact.view;

import com.ivision.engine.Fonts;
import javafx.animation.FadeTransition;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class PauseScreen {

    Group textGroup = new Group();
    Text leftText = new Text();
    Text rightText = new Text();
    Group pauseTextGroup = new Group();
    FadeTransition pauseTextTransition;
    Text leftPauseText = new Text();
    Text rightPauseText = new Text();

    Pane root;

    public PauseScreen (Pane root) {

        this.root = root;

        leftText.setFont(Fonts.REGULAR_24.getFont());
        leftText.setRotate(90);
        leftText.setTranslateY(-5);
        leftText.setScaleX(-1);
        leftText.setTranslateX(0);


        rightText.setFont(Fonts.REGULAR_24.getFont());
        rightText.setRotate(-90);
        rightText.setTranslateY(5);
        rightText.setScaleX(-1);

        rightText.setTranslateX(400);

        leftPauseText.setTranslateX(leftText.getTranslateX()+100);
        leftPauseText.setFont(Fonts.REGULAR_46.getFont());
        leftPauseText.setFill(Color.WHITE);
        leftPauseText.setRotate(90);
        leftPauseText.setTranslateY(-5);
        leftPauseText.setScaleX(-1);

        rightPauseText.setTranslateX(rightText.getTranslateX()-100);
        rightPauseText.setFont(Fonts.REGULAR_46.getFont());
        rightPauseText.setFill(Color.WHITE);
        rightPauseText.setRotate(-90);
        rightPauseText.setTranslateY(5);
        rightPauseText.setScaleX(-1);


        setTextOpacity(0);
        pauseTextGroup.getChildren().addAll(leftPauseText,rightPauseText);
        textGroup.getChildren().addAll(leftText,rightText);
        pauseTextTransition = Transitions.createFadeTransition(500,pauseTextGroup,1,.5);
        root.getChildren().addAll(textGroup,pauseTextGroup);
        hidePauseText();
        hideText();
    }

    public void showText(String text, Color textFill) {
        setTextOpacity(1);
        leftText.setFill(textFill);
        leftText.setText(text);
        rightText.setFill(textFill);
        rightText.setText(text);
    }

    public void hideText () {
        setTextOpacity(0);
    }

    public void showPauseText(String text) {
        leftPauseText.setText(text);
        rightPauseText.setText(text);
        setPauseTextOpacity(1);
        pauseTextTransition.play();
    }

    public void showPauseText() {
        setPauseTextOpacity(1);
        pauseTextTransition.play();
    }

    public void hidePauseText () {
        setPauseTextOpacity(0);
        pauseTextTransition.stop();
    }

    public void setPauseText (String text) {
        leftPauseText.setText(text);
        rightPauseText.setText(text);
    }

    public void setText (String text) {
        leftText.setText(text);
        rightText.setText(text);
    }

    public void setTextFill (Color color) {
        leftText.setFill(color);
        rightText.setFill(color);
    }

    public void setTextOpacity (double opacity) {
        textGroup.setOpacity(opacity);
    }

    public void setPauseTextOpacity (double opacity) {
        pauseTextGroup.setOpacity(opacity);
    }

}
