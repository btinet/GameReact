package com.ivision.gamereact.entity.module;

import com.ivision.engine.Fonts;
import com.ivision.engine.GameColor;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class TextModule extends Group {

    private final int symbolId;

    public TextModule (int symbolId, String text) {
        this.symbolId = symbolId;



        Text textElement = new Text(text);
        textElement.setFont(Fonts.BOLD_18.getFont());
        textElement.setFill(Color.WHITE);
        textElement.setWrappingWidth(350);
        textElement.setTranslateX(100);
        textElement.setTextAlignment(TextAlignment.JUSTIFY);
        textElement.setLineSpacing(1);


        Integer padding = 60;
        Double cWidth = textElement.getBoundsInLocal().getWidth();
        Double cHeight = textElement.getBoundsInLocal().getHeight();

        Rectangle background = new Rectangle(cWidth+padding,cHeight+padding,GameColor.DARKEN);
        background.setArcHeight(10);
        background.setArcWidth(10);
        background.setTranslateX(80);
        textElement.setTranslateY(-cHeight/2);
        background.setTranslateY(-(cHeight+padding)/2);

        getChildren().addAll(
                background,
                textElement
        );
    }

    public int getSymbolId() {
        return symbolId;
    }

}
