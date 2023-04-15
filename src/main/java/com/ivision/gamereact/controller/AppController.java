package com.ivision.gamereact.controller;

import com.ivision.engine.*;
import com.ivision.gamereact.entity.Ball;
import com.ivision.gamereact.entity.Curt;
import com.ivision.gamereact.entity.Paddle;
import com.ivision.engine.PowerUpSystem;
import com.ivision.gamereact.model.GamepadListener;
import com.ivision.gamereact.view.GameBoardDecoration;
import com.ivision.gamereact.view.PauseScreen;
import com.ivision.gamereact.view.Transitions;
import com.tuio.TuioClient;
import com.tuio.TuioCursor;
import com.tuio.TuioObject;
import javafx.animation.FillTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;

import static com.ivision.gamereact.ReactApplication.*;

public class AppController implements Initializable {

    @FXML
    private Pane root;

    // Spielattribute
    GameLoopTimer gameLoop;
    GamepadListener gamepadListener;
    protected double stageWidth;
    protected double stageHeight;
    ArrayList<TuioObject> gamepads = new ArrayList<>();
    ArrayList<TuioCursor> cursors = new ArrayList<>();
    public boolean playerOneIsPresent = false;
    public boolean playerTwoIsPresent = false;
    public boolean playerOneHasKeyboard = false;
    public ImageView p1kbdID;
    public ImageView p2kbdID;
    public ImageView fidBlue;
    public ImageView fidRed;

    boolean markerOneAdded = false;
    boolean markerTwoAdded = false;

    {
        assert ImageFiles.fidBlue != null;
        fidBlue = ImageFX.getImage(ImageFiles.fidBlue);
        fidRed = ImageFX.getImage(ImageFiles.fidBlue);
    }

    public AudioFX confirm = AudioFX.confirm;
    public AudioFX cancel = AudioFX.cancel;
    public AudioFX click = AudioFX.click;
    public boolean playerTwoHasKeyboard = false;
    public Paddle currentPlayer;
    public boolean isGameOver = false;
    boolean intro = true;
    int currentMiddleCirclePosition = 0;
    PowerUpSystem powerUpSystem;
    private final TuioClient client = new TuioClient();
    private final KeyPolling keys = KeyPolling.getInstance();

    // Spielobjekte
    Paddle playerOne = new Paddle(PaddlePosition.LEFT, 5,GameColor.RED);
    Paddle playerTwo = new Paddle(PaddlePosition.RIGHT, 5, GameColor.BLUE);
    private final Ball ball = new Ball(10);
    Curt curt = new Curt();
    GameBoardDecoration gbd;

    // Spielfeldkamera
    Camera camera = new ParallelCamera();
    TranslateTransition cameraTranslateTransition = Transitions.createTranslateTransition(200,camera);

    // Zusätzliches View
    PauseScreen pauseScreen;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ball.setBallSpeed(10);
        playerOne.setSpeedFactor(ball.getBallSpeed());
        playerTwo.setSpeedFactor(ball.getBallSpeed());
        root.setCursor(Cursor.NONE);

        currentPlayer = playerOne;

        Rectangle background = new Rectangle(2500,2500,GameColor.VIOLETT);

        // Spielfeld auf Bühne platzieren
        root.getChildren().addAll(
                background,
                curt
        );

        // Spielfeld dekorieren
        gbd = new GameBoardDecoration(root, playerOne, playerTwo);

        // Spielfiguren, Banden und Anzeigen platzieren
        root.getChildren().addAll(
                playerOne.getPointsText(),
                playerTwo.getPointsText(),
                playerOne,
                playerTwo,
                ball,
                curt.getNorthBorder(),
                curt.getSouthBorder(),
                playerOne.getHealthPointGroup(),
                playerTwo.getHealthPointGroup(),
                playerOne.getTimerIndicator(),
                playerTwo.getTimerIndicator()
        );

        powerUpSystem = new PowerUpSystem(root,ball, gbd);
        pauseScreen =  new PauseScreen(root);

        // Verarbeitung der Benutzereingabe über Reactable-Marker.
        gamepadListener = new GamepadListener(true);
        gamepadListener.setController(this);

        // UDP-Client, der Datenpakete von Port 3333 abfängt und weiterleitet.
        client.addTuioListener(gamepadListener);
        // Verbindung herstellen und Abfrage starten.
        client.connect();

        gameLoop = new GameLoopTimer() {

            @Override
            public void handle(long now) {
                super.handle(now);

                getUserInput();
                getFingerInput();
                getEntityInteractions();



                if(playerOneIsPresent) {
                    addMarkerIconRed();
                    confirm.setBalance(-0.75);
                    if(!markerOneAdded) confirm.play();
                    markerOneAdded = true;
                } else {
                    removeMarkerIconRed();
                    cancel.setBalance(-0.75);
                    if(markerOneAdded) cancel.play();
                    markerOneAdded = false;
                }

                if(playerTwoIsPresent) {
                    addMarkerIconBlue();
                    confirm.setBalance(0.75);
                    if(!markerTwoAdded) confirm.play();
                    markerTwoAdded = true;
                } else {
                    removeMarkerIconBlue();
                    cancel.setBalance(0.75);
                    if(markerTwoAdded) cancel.play();
                    markerTwoAdded = false;
                }

            }

            // Spielzeit generieren
            @Override
            public void tick(float secondsSinceLastFrame) {

                // Intro Audio abspielen, danach nicht mehr!
                if(intro) {
                    if (!isGameOver) AudioFX.pongReactSFX.play();
                    intro = false;
                    togglePause();
                }

                if(playerOne.getManipulation() != null) {
                    powerUpSystem.runPowerUpTimer(playerOne);
                }

                if(playerTwo.getManipulation() != null) {
                    powerUpSystem.runPowerUpTimer(playerTwo);
                }

                // START: Power Ups
                powerUpSystem.run(currentPlayer);

                if(ball.triggerByBallHits()) {
                    powerUpSystem.setPowerUp();
                }
                // ENDE: Power Ups

                // Position der Spielfiguren aktualisieren
                updatePlayer();
            }
        };

        // Hintergrundfarbe der Szene
        root.setStyle("-fx-background-color: #883c77");

        // Spiel starten
        gameLoop.start();
    }

    private void gameOver(Paddle player) {
        gameLoop.stop();
        gameLoop.start();
        isGameOver = true;
        intro = true;
        powerUpSystem.removeCollectedPowerUps();
        player.increaseMatchPoints();
        playerOne.reset();
        playerTwo.reset();

    }

    /**
     * Transition setzen und einmalig sofort abspielen.
     * @param shape Form, der eine FillTransition zugeordnet werden soll.
     */
    public void setAndPlayFillTransition(Shape shape) {
        FillTransition fingerTransition = Transitions.createFillTransition(200,shape,GameColor.YELLOW,GameColor.YELLOW_ALPHA_0,1);
        fingerTransition.setAutoReverse(false);
        fingerTransition.play();
    }

    public void getFingerInput () {
        cursors = gamepadListener.getFingers();
        Iterator<TuioCursor> cursorIterator = cursors.iterator();
        while (cursorIterator.hasNext()) {
            TuioCursor cursor = cursorIterator.next();
            // Fingereingabe
            Circle fingerCircle = new Circle(25, Color.CYAN);
            fingerCircle.setTranslateX((int)(cursor.getScreenX(width)-width/2));
            fingerCircle.setTranslateY((int)(cursor.getScreenY(height)-height/2));
            if(!root.getChildren().contains(fingerCircle)) root.getChildren().add(fingerCircle);
            setAndPlayFillTransition(fingerCircle);
            click.play();
            cursorIterator.remove();
            togglePause();
        }
    }

    /**
     * Konfiguration der Tastatursteuerung
     */
    public void getUserInput () {

        // Periodische Tastenabfragen
        // z.B. keys.down(KeyCode)


        // einmalige Tastenabfragen (innerhalb Anschlagverzögerung)
        if (keys.isPressed(ButtonConfig.toggleFullscreen))         toggleFullscreen();
        if (keys.isPressed(ButtonConfig.actionTertiary)) {
            click.play();
            togglePause();
        }
        if (keys.isPressed(ButtonConfig.toggleP1)) {
            toggleKeyboardControl(1);
            assert ImageFiles.kbd != null;
            if(null == p1kbdID) p1kbdID = ImageFX.getImage(ImageFiles.kbd);
            p1kbdID.setTranslateX(-660);
            p1kbdID.setTranslateY(30);
            p1kbdID.setRotate(90);
            if(!root.getChildren().contains(p1kbdID)) {
                root.getChildren().add(p1kbdID);
                confirm.setBalance(-0.75);
                confirm.play();
            } else {
                root.getChildren().remove(p1kbdID);
                cancel.setBalance(-0.75);
                cancel.play();
            }
        }
        if (keys.isPressed(ButtonConfig.toggleP2)) {
            toggleKeyboardControl(2);
            assert ImageFiles.kbd != null;
            if(null == p2kbdID) p2kbdID = ImageFX.getImage(ImageFiles.kbd);
            p2kbdID.setTranslateX(660);
            p2kbdID.setTranslateY(-30);
            p2kbdID.setRotate(-90);
            if(!root.getChildren().contains(p2kbdID)) {
                root.getChildren().add(p2kbdID);
                confirm.setBalance(0.75);
                confirm.play();
            } else {
                root.getChildren().remove(p2kbdID);
                cancel.setBalance(0.75);
                cancel.play();
            }
        }
    }


    public void toggleKeyboardControl (int player) {
        switch (player) {
            case 1:
                playerOneHasKeyboard = !playerOneHasKeyboard;
                break;
            case 2:
                playerTwoHasKeyboard = !playerTwoHasKeyboard;
                break;
        }
    }

    /**
     * Zwischen Pausen- und Spielmodus wechseln.
     */
    public void togglePause() {
        if(gameLoop.isPaused()) {
            TranslateTransition middleCircleAnimation = gbd.getMoveCircleTransition();

            middleCircleAnimation.setToX(0);
            middleCircleAnimation.setFromX(currentMiddleCirclePosition);
            middleCircleAnimation.play();
            currentMiddleCirclePosition = 0;
            root.getScene().setCamera(camera);
            gbd.getMiddleCircleScaleDown().play();
            pauseScreen.hideHelpText();
            pauseScreen.hidePauseText();
            pauseScreen.hideText();
            gameLoop.play();
            MusicFX.THE_GRID.stop();
            MusicFX.MAZE.play();
            if(isGameOver) {
                isGameOver = false;
            }
        } else {
            gameLoop.pause();
            MusicFX.THE_GRID.play();
            MusicFX.MAZE.pause();
            gbd.getMiddleCircleScaleUp().play();
            gbd.getMiddleCircleScaleUp().setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    pauseScreen.showHelpText();
                    if(isGameOver) {
                        pauseScreen.showPauseText("Game over");
                    } else {
                        //pauseScreen.showPauseText("Pause");
                    }
                }
            });


        }
    }

    public void getEntityInteractions () {



        // Ball geht ins Aus:
        if(!ball.intersects(curt)) {

            powerUpSystem.shutDown();
            TranslateTransition middleCircleAnimation = gbd.getMoveCircleTransition();

            // TODO: PauseScreen Text hier setzen und erst in togglePause() anzeigen lassen!

            if (currentPlayer.equals(playerOne)) {
                currentMiddleCirclePosition = 285;
                middleCircleAnimation.setFromX(0);
                middleCircleAnimation.setToX(285);

                playerTwo.decreaseHealthPoints();
                if (playerTwo.getCurrentHealthPoints() >= 1) {
                    //pauseScreen.showText("Rot trifft!", playerOne.getPrimaryColor());

                    AudioFX.rHitSFX.play();
                } else {
                    //pauseScreen.showText("Rot gewinnt!", playerOne.getPrimaryColor());
                    AudioFX.rWinSFX.play();
                    gameOver(playerOne);
                }
                ball.setStartPosition(PaddlePosition.RIGHT);
                currentPlayer = playerTwo;
                gbd.playStrafeAnimation(PaddlePosition.LEFT);
                cameraTranslateTransition.setFromX(-5);
                cameraTranslateTransition.setToX(0);
            } else if (currentPlayer.equals(playerTwo)) {
                currentMiddleCirclePosition = -285;
                middleCircleAnimation.setFromX(0);
                middleCircleAnimation.setToX(-285);

                playerOne.decreaseHealthPoints();
                if (playerOne.getCurrentHealthPoints() >= 1) {
                    //pauseScreen.showText("Blau trifft!", playerTwo.getPrimaryColor());
                    AudioFX.bHitSFX.play();
                } else {
                    //pauseScreen.showText("Blau gewinnt!", playerTwo.getPrimaryColor());
                    AudioFX.bWinSFX.play();
                    gameOver(playerTwo);
                }
                ball.setStartPosition(PaddlePosition.LEFT);
                currentPlayer = playerOne;
                gbd.playStrafeAnimation(PaddlePosition.RIGHT);
                cameraTranslateTransition.setFromX(5);
                cameraTranslateTransition.setToX(0);
            }
            togglePause();
            cameraTranslateTransition.play();
            if(ball.getBallAngle() > 0) {
                ball.setBallAngle(-1.5);
            } else {
                ball.setBallAngle(1.5);
            }
            middleCircleAnimation.play();
        }

        // TODO: Ball-Interaktion mit Spielern in eigene Klasse oder Methode übertragen
        // Zufallsfaktor generieren
        double random;
        if(Math.random() < .5) {
            random = -(Math.random()*50+5);
        } else {
            random = (Math.random()*50+5);
        }
        // Ballinteraktion mit Spielern
        if(ball.intersects(playerOne)) {
            currentPlayer = playerOne;
            playerOne.getPrimarySound().play();
            ball.increaseBallHits();
            ball.setBallAngle(-Math.sin(Math.toRadians(playerOne.getTranslateY()-ball.getTranslateY()+random))*5);
            ball.setBallSpeed(currentPlayer);
        }
        else if (ball.intersects(playerTwo)) {
            currentPlayer = playerTwo;
            playerTwo.getPrimarySound().play();
            ball.increaseBallHits();
            ball.setBallAngle(-Math.sin(Math.toRadians(playerTwo.getTranslateY()-ball.getTranslateY()+random))*5);
            ball.setBallSpeed(currentPlayer);
        }
        // Ballinteraktion mit Seitenbanden
        if(ball.intersects(curt.getNorthBorder())) {
            TranslateTransition shakeNorth = new TranslateTransition(new Duration(100),camera);
            shakeNorth.setFromY(5);
            shakeNorth.setToY(0);
            shakeNorth.setCycleCount(1);
            shakeNorth.setAutoReverse(true);
            shakeNorth.play();
            AudioFX.SFX3.play();
            ball.reflect();
        }
        else if (ball.intersects(curt.getSouthBorder())) {
            TranslateTransition shakeSouth = new TranslateTransition(new Duration(100),camera);
            shakeSouth.setFromY(-5);
            shakeSouth.setToY(0);
            shakeSouth.setCycleCount(1);
            shakeSouth.setAutoReverse(true);
            shakeSouth.play();
            AudioFX.SFX3.play();
            ball.reflect();
        }

    }

    public void updatePlayer() {

        ball.move();
        // Aktive Marker ermitteln.
        gamepads = gamepadListener.getGamepads();


        if(!playerOneIsPresent) {
            if(!playerOneHasKeyboard) {
                // CPU-Steuerung für Spieler 1
                if(!playerOne.intersects(curt.getNorthBorder())) {
                    if (playerOne.getTranslateY()-ball.getTranslateY() > 100) {
                        playerOne.setTranslateY(playerOne.getTranslateY()-10);
                    } else
                    if (playerOne.getTranslateY()-ball.getTranslateY() > 50) {
                        playerOne.setTranslateY(playerOne.getTranslateY()-4.5);
                    }  else
                    if (playerOne.getTranslateY()-ball.getTranslateY() > 10)  {
                        playerOne.setTranslateY(playerOne.getTranslateY()-.5);
                    }  else
                    if (playerOne.getTranslateY()-ball.getTranslateY() > 1) {
                        playerOne.setTranslateY(playerOne.getTranslateY() - .1);
                    } else {
                        playerOne.setTranslateY(playerOne.getTranslateY());
                    }
                } else {
                    playerOne.setTranslateY(playerOne.getTranslateY());
                }
                if(!playerOne.intersects(curt.getSouthBorder())) {
                    if (playerOne.getTranslateY()-ball.getTranslateY() < -100) {
                        playerOne.setTranslateY(playerOne.getTranslateY()+10);
                    } else
                    if (playerOne.getTranslateY()-ball.getTranslateY() < -50) {
                        playerOne.setTranslateY(playerOne.getTranslateY()+4.7);
                    }  else
                    if (playerOne.getTranslateY()-ball.getTranslateY() < -10)  {
                        playerOne.setTranslateY(playerOne.getTranslateY()+.5);
                    }  else
                    if (playerOne.getTranslateY()-ball.getTranslateY() < -1) {
                        playerOne.setTranslateY(playerOne.getTranslateY() + .1);
                    } else {
                        playerOne.setTranslateY(playerOne.getTranslateY());
                    }
                } else {
                    playerOne.setTranslateY(playerOne.getTranslateY());
                }
            } else {
                // Manuelle Keyboard-Steuerung
                if (playerOne.getInverter() > 0) {
                    if (keys.isDown(ButtonConfig.north2)) {
                        if (!playerOne.intersects(curt.getNorthBorder())) {
                            playerOne.setTranslateY(playerOne.getInverter() * playerOne.getTranslateY() - 6);
                        }
                    }
                    if (keys.isDown(ButtonConfig.south2)) {
                        if (!playerOne.intersects(curt.getSouthBorder())) {
                            playerOne.setTranslateY(playerOne.getInverter() * playerOne.getTranslateY() + 6);
                        }
                    }
                } else {
                    if (keys.isDown(ButtonConfig.south2)) {
                        if (!playerOne.intersects(curt.getNorthBorder())) {
                            playerOne.setTranslateY(playerOne.getInverter() * playerOne.getTranslateY() - 6);
                        }
                    }
                    if (keys.isDown(ButtonConfig.north2)) {
                        if (!playerOne.intersects(curt.getSouthBorder())) {
                            playerOne.setTranslateY(playerOne.getInverter() * playerOne.getTranslateY() + 6);
                        }
                    }
                }
            }
        }

        if(!playerTwoIsPresent) {
            if(!playerTwoHasKeyboard) {
                // CPU-Steuerung für Spieler 2
                if (!playerTwo.intersects(curt.getNorthBorder())) {
                    if (playerTwo.getTranslateY() - ball.getTranslateY() > 100) {
                        playerTwo.setTranslateY(playerTwo.getTranslateY() - 7.4);
                    } else if (playerTwo.getTranslateY() - ball.getTranslateY() > 50) {
                        playerTwo.setTranslateY(playerTwo.getTranslateY() - 5.2);
                    } else if (playerTwo.getTranslateY() - ball.getTranslateY() > 10) {
                        playerTwo.setTranslateY(playerTwo.getTranslateY() - 2);
                    } else if (playerTwo.getTranslateY() - ball.getTranslateY() > 1) {
                        playerTwo.setTranslateY(playerTwo.getTranslateY() - .1);
                    } else {
                        playerTwo.setTranslateY(playerTwo.getTranslateY());
                    }
                } else {
                    playerTwo.setTranslateY(playerTwo.getTranslateY());
                }
                if (!playerTwo.intersects(curt.getSouthBorder())) {
                    if (playerTwo.getTranslateY() - ball.getTranslateY() < -100) {
                        playerTwo.setTranslateY(playerTwo.getTranslateY() + 8);
                    } else if (playerTwo.getTranslateY() - ball.getTranslateY() < -50) {
                        playerTwo.setTranslateY(playerTwo.getTranslateY() + 4.4);
                    } else if (playerTwo.getTranslateY() - ball.getTranslateY() < -10) {
                        playerTwo.setTranslateY(playerTwo.getTranslateY() + 2);
                    } else if (playerTwo.getTranslateY() - ball.getTranslateY() < -1) {
                        playerTwo.setTranslateY(playerTwo.getTranslateY() + .1);
                    } else {
                        playerTwo.setTranslateY(playerTwo.getTranslateY());
                    }
                } else {
                    playerTwo.setTranslateY(playerTwo.getTranslateY());
                }
            } else {
                // Manuelle Keyboard-Steuerung
                if(playerTwo.getInverter() > 0) {
                    if(keys.isDown(ButtonConfig.north)) {
                        if(!playerTwo.intersects(curt.getNorthBorder())) {
                            playerTwo.setTranslateY(playerTwo.getTranslateY()-6);
                        }
                    }
                    if(keys.isDown(ButtonConfig.south)) {
                        if(!playerTwo.intersects(curt.getSouthBorder())) {
                            playerTwo.setTranslateY(playerTwo.getTranslateY()+6);
                        }
                    }
                } else {
                    if(keys.isDown(ButtonConfig.south)) {
                        if(!playerTwo.intersects(curt.getNorthBorder())) {
                            playerTwo.setTranslateY(playerTwo.getTranslateY()-6);
                        }
                    }
                    if(keys.isDown(ButtonConfig.north)) {
                        if(!playerTwo.intersects(curt.getSouthBorder())) {
                            playerTwo.setTranslateY(playerTwo.getTranslateY()+6);
                        }
                    }
                }

            }
        }

        // Manuelle Steuerung für jeden aktiven Spieler
        for (TuioObject gamepad : gamepads) {
            switch (gamepad.getSymbolID()) {
                case 1:
                    double figure = playerOne.getTranslateY();
                    if(playerOne.intersects(curt.getNorthBorder())) {
                        if(getSpeed(gamepad.getAngleDegrees(),playerOne) > 0) {
                            playerOne.setTranslateY( figure + getSpeed(gamepad.getAngleDegrees(),playerOne) );
                        }
                    } else if(playerOne.intersects(curt.getSouthBorder())) {
                        if(getSpeed(gamepad.getAngleDegrees(),playerOne) < 0) {
                            playerOne.setTranslateY( figure + getSpeed(gamepad.getAngleDegrees(),playerOne) );
                        }
                    } else {
                        playerOne.setTranslateY( figure + getSpeed(gamepad.getAngleDegrees(),playerOne)  );
                    }
                    break;
                case 2:
                    double figure2 = playerTwo.getTranslateY();
                    if(playerTwo.intersects(curt.getNorthBorder())) {
                        if(getSpeed(gamepad.getAngleDegrees(),playerTwo) > 0) {
                            playerTwo.setTranslateY( figure2 + getSpeed(gamepad.getAngleDegrees(),playerTwo));
                        }
                    } else if(playerTwo.intersects(curt.getSouthBorder())) {
                        if(getSpeed(gamepad.getAngleDegrees(),playerTwo) < 0) {
                            playerTwo.setTranslateY( figure2 + getSpeed(gamepad.getAngleDegrees(),playerTwo));
                        }
                    } else {
                        playerTwo.setTranslateY( figure2 + getSpeed(gamepad.getAngleDegrees(),playerTwo));
                    }
                    break;
                default:
                    System.out.println("Es sind nur Marker der IDs 1 und 2 erlaubt!");
            }
        }

    }

    public void addMarkerIconBlue () {
        fidBlue.setTranslateX(660);
        fidBlue.setTranslateY(30);
        if(!root.getChildren().contains(fidBlue)) root.getChildren().add(fidBlue);
    }

    public void removeMarkerIconBlue () {
        root.getChildren().remove(fidBlue);
    }

    public void addMarkerIconRed () {
        fidRed.setTranslateX(-660);
        fidRed.setTranslateY(-30);
        if(!root.getChildren().contains(fidRed)) root.getChildren().add(fidRed);
    }

    public void removeMarkerIconRed () {
        root.getChildren().remove(fidRed);
    }

    /**
     *
     * @param angleDegrees Winkel in Grad
     * @return Sinus eines Winkels
     */
    private double getSpeed(float angleDegrees, Paddle currentPlayer) {

        double x = Math.sin(Math.toRadians(angleDegrees-90));
        return currentPlayer.getInverter() * x * 12;
    }

    /**
     * Zwischen Fenster- und Vollbildmodus wechseln.
     */
    @FXML
    protected void toggleFullscreen() {

        stageWidth = root.getWidth();
        stageHeight = root.getHeight();

        stage.setFullScreen(!stage.isFullScreen());
    }

}