package com.ivision.engine;

import javafx.scene.text.Font;

public enum Fonts {

    BOLD_24 ("/fonts/stormfaze.otf",46),
    REGULAR_46 ("/fonts/ErbosDraco1StOpenNbpRegular-l5wX.ttf",46),
    BOLD_18 ("/fonts/OpenSans-Bold.ttf",18),
    BOLD_12 ("/fonts/OpenSans-Bold.ttf",12);


    private final Font font;

    Fonts (String font, int size) {
        this.font = Font.loadFont(this.getClass().getResourceAsStream(font), size);
    }

    public Font getFont() {
        return font;
    }

}
