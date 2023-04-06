package ivisionfx.interaction;

import javafx.scene.input.KeyCode;

public interface ButtonConfig {

    KeyCode toggleFullscreen = KeyCode.F11;
    KeyCode gameMenu = KeyCode.F1;
    KeyCode saveGame = KeyCode.F5;
    KeyCode loadGame = KeyCode.F6;
    KeyCode actionPrimary = KeyCode.CONTROL;
    KeyCode actionSecondary = KeyCode.ALT;
    KeyCode actionTertiary = KeyCode.SPACE;
    KeyCode north = KeyCode.UP;
    KeyCode south = KeyCode.DOWN;
    KeyCode east = KeyCode.RIGHT;
    KeyCode west = KeyCode.LEFT;
    KeyCode enter = KeyCode.ENTER;
    KeyCode exit = KeyCode.ESCAPE;

}
