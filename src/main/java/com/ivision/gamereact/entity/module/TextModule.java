package com.ivision.gamereact.entity.module;

import javafx.scene.Group;
import javafx.scene.text.Text;

public class TextModule extends Group {

    private final int symbolId;

    public TextModule (int symbolId) {
        this.symbolId = symbolId;
        getChildren().add(new Text("Ich bin ein Modul, das Text ausgeben kann."));
    }

    public int getSymbolId() {
        return symbolId;
    }

}
