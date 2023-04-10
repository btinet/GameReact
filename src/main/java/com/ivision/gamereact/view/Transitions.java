package com.ivision.gamereact.view;

import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

public class Transitions {

    public static FadeTransition createFadeTransition (int millis, Node node) {
        return createFadeTransition(millis,node,0,1);
    }

    public static FadeTransition createFadeTransition (int millis, Node node, double from, double to) {
        FadeTransition transition = new FadeTransition(Duration.millis(millis), node);
        transition.setFromValue(from);
        transition.setToValue(to);
        transition.setCycleCount(-1);
        transition.setAutoReverse(true);
        return transition;
    }

    public static FillTransition createFillTransition (int millis, Shape shape, Color from, Color to, int cycleCount) {
        FillTransition transition = new FillTransition(Duration.millis(millis),shape);
        transition.setFromValue(from);
        transition.setToValue(to);
        transition.setCycleCount(cycleCount);
        transition.setAutoReverse(true);
        return transition;
    }

    public static TranslateTransition createTranslateTransition (int duration, Node node) {
        TranslateTransition translation = new TranslateTransition(new Duration(duration),node);
        translation.setCycleCount(1);
        translation.setAutoReverse(false);
        return translation;
    }

}
