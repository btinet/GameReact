package com.ivision.gamereact.entity.module;

import javafx.scene.Group;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class TextModule extends Group {

    private final int symbolId;

    public TextModule (int symbolId, String text) {
        this.symbolId = symbolId;
        Text textElement = new Text(text);
        textElement.setFont(new Font(28));
        textElement.setTranslateX(150);

        getChildren().add(textElement);
    }

    public int getSymbolId() {
        return symbolId;
    }

}
