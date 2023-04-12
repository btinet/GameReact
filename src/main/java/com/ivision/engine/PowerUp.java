package com.ivision.engine;

import com.ivision.gamereact.entity.Paddle;
import javafx.scene.image.ImageView;

public interface PowerUp {

    /**
     *
     * @param imageFile Icon des PowerUps.
     */
    void setIcon (ImageView imageFile);

    /**
     *
     * @param label Bezeichnung des PowerUps.
     */
    void setLabel (String label);

    /**
     * Wendet PowerUp auf den Spieler an, der es einsammelte.
     * @param affectedPlayer Vom PowerUp betroffener Spieler.
     */
    void doAction (Paddle affectedPlayer);
}
