package com.ivision.gamereact.entity;

import com.ivision.engine.PowerUp;
import javafx.scene.image.ImageView;

public abstract class PowerUpItem implements PowerUp {

    ImageView icon;

    public PowerUpItem (ImageView icon) {
        setIcon(icon);
    }

    @Override
    public void setIcon(ImageView imageFile) {
        this.icon = imageFile;
        this.icon.setTranslateX(2);
        this.icon.setTranslateY(2);
    }

    @Override
    public void setLabel(String label) {

    }

}
