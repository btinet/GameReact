package com.ivision.engine;

import javafx.scene.text.Font;

public enum Fonts {

    REGULAR_24 ("/fonts/ErbosDraco1StOpenNbpRegular-l5wX.ttf",24),
    REGULAR_46 ("/fonts/ErbosDraco1StOpenNbpRegular-l5wX.ttf",46);


    private final Font font;

    Fonts (String font, int size) {
        this.font = Font.loadFont(this.getClass().getResourceAsStream(font), size);
    }

    public Font getFont() {
        return font;
    }

}
