package com.ivision.engine;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;

public class ImageFX {

    public static ImageView getImage(URL imageUrl) {
        try {
            assert imageUrl != null;
            Image image = new Image(imageUrl.openStream());
            ImageView imageView = new ImageView();
            imageView.setImage(image);
            imageView.setScaleX(-1);
            return imageView;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
