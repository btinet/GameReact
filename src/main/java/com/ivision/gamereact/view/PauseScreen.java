package com.ivision.gamereact.view;

import com.ivision.engine.Fonts;
import javafx.animation.FadeTransition;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class PauseScreen {

    Group textGroup = new Group();
    Text leftText = new Text();
    Text rightText = new Text();
    Group pauseTextGroup = new Group();
    FadeTransition pauseTextTransition;
    Text leftPauseText = new Text();
    Text rightPauseText = new Text();
    Group hilfeText = new Group();

    ArrayList<Text> hilfeTextListe = new ArrayList<>();

    Pane root;

    public PauseScreen (Pane root) {

        this.root = root;

        Text f1 = new Text("F1: Keyboard P1 aktivieren/deaktivieren");
        Text f2 = new Text("F2: Keyboard P2 aktivieren/deaktivieren");
        Text f11 = new Text("F11: Vollbild ein/aus");
        Text space = new Text("Leertaste: pausieren/fortsetzen");
        f1.setFill(Color.WHITE);
        f2.setFill(Color.WHITE);
        f11.setFill(Color.WHITE);
        space.setFill(Color.WHITE);

        f1.setFont(Fonts.BOLD_12.getFont());
        f2.setFont(Fonts.BOLD_12.getFont());
        f11.setFont(Fonts.BOLD_12.getFont());
        space.setFont(Fonts.BOLD_12.getFont());

        f1.setTranslateX(0);
        f2.setTranslateX(250);
        f11.setTranslateX(500);
        space.setTranslateX(630);

        hilfeTextListe.add(f1);
        hilfeTextListe.add(f2);
        hilfeTextListe.add(f11);
        hilfeTextListe.add(space);

        hilfeText.getChildren().addAll(hilfeTextListe);
        hilfeText.setTranslateY(400);
        hilfeText.setScaleX(-1);
        this.root.getChildren().add(hilfeText);

        leftText.setFont(Fonts.BOLD_24.getFont());
        leftText.setRotate(90);
        leftText.setTranslateY(0);
        leftText.setScaleX(-1);
        leftText.setTranslateX(0);


        rightText.setFont(Fonts.BOLD_24.getFont());
        rightText.setRotate(-90);
        rightText.setTranslateY(0);
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

    public void showHelpText () {
        hilfeText.setOpacity(1);
    }

    public void hideHelpText () {
        hilfeText.setOpacity(0);
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

    public void hidePauseText () {
        setPauseTextOpacity(0);
        pauseTextTransition.stop();
    }

    public void setTextOpacity (double opacity) {
        textGroup.setOpacity(opacity);
    }

    public void setPauseTextOpacity (double opacity) {
        pauseTextGroup.setOpacity(opacity);
    }

}
