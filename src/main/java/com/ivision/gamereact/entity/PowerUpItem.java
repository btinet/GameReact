package com.ivision.gamereact.entity;

import com.ivision.engine.PowerUp;
import javafx.scene.image.ImageView;

public abstract class PowerUpItem implements PowerUp {

    ImageView icon;

    boolean isActive = true;

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

    @Override
    public boolean isActive() {
        return isActive;
    }

    protected void setIsActive (boolean value) {
        this.isActive = value;
    }

    @Override
    public boolean doAction(Paddle affectedPlayer) {
        System.out.println("Standard Power Up ausgeführt!");
        return true;
    }
}
