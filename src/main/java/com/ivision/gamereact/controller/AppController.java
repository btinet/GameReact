package com.ivision.gamereact.controller;

import com.github.strikerx3.jxinput.*;
import com.github.strikerx3.jxinput.enums.XInputButton;
import com.github.strikerx3.jxinput.exceptions.XInputNotLoadedException;
import com.ivision.engine.*;
import com.ivision.gamereact.entity.AudioPlayer;
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
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Screen;
import javafx.util.Duration;
import java.net.URL;
import java.util.*;

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
    public boolean playerTwoHasKeyboard = false;
    public boolean playerOneHasGamepad = false;
    public boolean playerTwoHasGamepad = false;
    public boolean gamepadOneIsPresent = false;
    public boolean gamepadTwoIsPresent = false;

    public boolean a1IsPresent = false;

    // Test Music Media
    AudioPlayer a1 = new AudioPlayer(MusicFX.HACKING,"Tron Legacy - Hacking",this);

    public ImageView p1kbdID;
    public ImageView p2kbdID;
    public ImageView fidBlue;
    public ImageView fidRed;

    boolean markerOneAdded = false;
    boolean markerTwoAdded = false;

    int shortTimer = 0;
    int playerRumbleTimer = 0;

    {
        assert ImageFiles.fidBlue != null;
        assert ImageFiles.xbox != null;
        fidBlue = ImageFX.getImage(ImageFiles.fidBlue);
        fidRed = ImageFX.getImage(ImageFiles.fidBlue);
    }


    ImageView p1GamepadIcon = ImageFX.getImage(ImageFiles.Y);
    ImageView p1GamepadPause = ImageFX.getImage(ImageFiles.A);
    ImageView p2GamepadIcon = ImageFX.getImage(ImageFiles.Y);

    ImageView p2GamepadPause = ImageFX.getImage(ImageFiles.A);

    public AudioFX confirm = AudioFX.confirm;
    public AudioFX cancel = AudioFX.cancel;
    public AudioFX click = AudioFX.click;
    public Paddle currentPlayer;
    public boolean isGameOver = false;
    boolean intro = true;
    int currentMiddleCirclePosition = 0;
    PowerUpSystem powerUpSystem;
    private final TuioClient client = new TuioClient();
    private final KeyPolling keys = KeyPolling.getInstance();

    // Spielobjekte
    public HashMap<ImageView,TuioObject> imageObjects = new HashMap<>();
    Paddle playerOne = new Paddle(PaddlePosition.LEFT, 5,GameColor.RED);
    XInputDevice finalDevice;
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

        ball.setBallSpeed(9);
        playerOne.setSpeedFactor(ball.getBallSpeed());
        playerTwo.setSpeedFactor(ball.getBallSpeed());
        root.setCursor(Cursor.NONE);

        currentPlayer = playerOne;

        // Spielfeld auf Bühne platzieren

        // Spielfeld dekorieren
        gbd = new GameBoardDecoration(root, playerOne, playerTwo);

        powerUpSystem = new PowerUpSystem(root,ball, gbd);

        // Verarbeitung der Benutzereingabe über Reactable-Marker.
        gamepadListener = new GamepadListener(root,verbose);
        gamepadListener.setController(this);

        // UDP-Client, der Datenpakete von Port 3333 abfängt und weiterleitet.
        client.addTuioListener(gamepadListener);
        // Verbindung herstellen und Abfrage starten.
        client.connect();
        XInputDevice device = null;
        try {
            device = XInputDevice.getDeviceFor(0);
        } catch (XInputNotLoadedException e) {
            throw new RuntimeException(e);
        }
        finalDevice = device;
        gameLoop = new GameLoopTimer() {

            @Override
            public void handle(long now) {
                super.handle(now);

                if(a1IsPresent) {
                    if(!root.getChildren().contains(a1)) root.getChildren().add(a1);
                } else {
                    root.getChildren().remove(a1);
                }

                updateTools();
                getUserInput();
                getFingerInput();
                toggleGamepadOneIcon();

                if (finalDevice.poll()) {

                    gamepadOneIsPresent = true;
                    // Retrieve the components
                    XInputComponents components = finalDevice.getComponents();

                    XInputButtons buttons = components.getButtons();
                    XInputAxes axes = components.getAxes();

                    // Buttons and axes have public fields (although this is not idiomatic Java)
                    XInputComponentsDelta delta = finalDevice.getDelta();

                    XInputButtonsDelta dButtons = delta.getButtons();
                    XInputAxesDelta dAxes = delta.getAxes();

                    // Retrieve button state change

                    if (dButtons.isPressed(XInputButton.A)) togglePause();
                    if (dButtons.isPressed(XInputButton.Y)) togglePlayerOneHasGamepad();


                    // Retrieve button state


                    // Retrieve axis state
                    float lyAxis = axes.ly;





                    if(!gameLoop.isPaused() && playerOneHasGamepad) {
                        double figure = playerOne.getTranslateY();
                        if(playerOne.intersects(curt.getNorthBorder())) {
                            if(lyAxis < -0.2) {
                                playerOne.setTranslateY( figure + getAnalogSpeed(-lyAxis,playerOne) );
                            }
                        } else if(playerOne.intersects(curt.getSouthBorder())) {
                            if(lyAxis > 0.2) {
                                playerOne.setTranslateY( figure + getAnalogSpeed(-lyAxis,playerOne) );
                            }
                        } else if(lyAxis > 0.2 || lyAxis < -0.2) {
                            playerOne.setTranslateY( figure + getAnalogSpeed(-lyAxis,playerOne)  );
                        }
                    }


                } else {
                    // Controller is not connected; display a message
                    playerOneHasGamepad = false;
                    gamepadOneIsPresent = false;
                    assert ImageFiles.Y != null;
                    p1GamepadIcon.setImage(ImageFX.getImage(ImageFiles.Y).getImage());

                }

                // This is exactly the same as above
                finalDevice.poll();
                if (finalDevice.isConnected()) {
                    // ...
                } else {
                    // ...
                }



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

                if(!gameLoop.isPaused()) getEntityInteractions();

                // START: Power Ups
                if(playerOne.getManipulation() != null) {
                    powerUpSystem.runPowerUpTimer(playerOne);
                }

                if(playerTwo.getManipulation() != null) {
                    powerUpSystem.runPowerUpTimer(playerTwo);
                }

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

    public void toggleGamepadOneIcon() {

        if(gamepadOneIsPresent) {
            if(!root.getChildren().contains(p1GamepadIcon)) {
                p1GamepadIcon.setTranslateX(-620);
                p1GamepadIcon.setScaleX(.15);
                p1GamepadIcon.setScaleY(.15);
                p1GamepadIcon.setTranslateY(120);
                p1GamepadIcon.setRotate(90);
                p1GamepadIcon.setOpacity(1);
                root.getChildren().add(p1GamepadIcon);
            }
        } else {
            root.getChildren().remove(p1GamepadIcon);
        }
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
    public FillTransition setAndPlayFillTransition(Shape shape) {
        FillTransition fingerTransition = Transitions.createFillTransition(200,shape,GameColor.YELLOW,GameColor.YELLOW_ALPHA_0,1);
        fingerTransition.setAutoReverse(false);
        return fingerTransition;
    }

    public void getFingerInput () {
        cursors = gamepadListener.getFingers();
        Iterator<TuioCursor> cursorIterator = cursors.iterator();
        while (cursorIterator.hasNext()) {
            TuioCursor cursor = cursorIterator.next();
            // Fingereingabe
            Circle fingerCircle = new Circle(25, Color.CYAN);
            fingerCircle.setTranslateX((int)(cursor.getScreenX((int) (Screen.getPrimary().getBounds().getWidth()))-(Screen.getPrimary().getBounds().getWidth()/2)));
            fingerCircle.setTranslateY((int)(cursor.getScreenY((int) (Screen.getPrimary().getBounds().getHeight()))-(Screen.getPrimary().getBounds().getHeight()/2)));
            root.getChildren().add(fingerCircle);

            a1.touch(fingerCircle,stageWidth,stageHeight);

            FillTransition ft = setAndPlayFillTransition(fingerCircle);
            ft.setOnFinished(event -> root.getChildren().remove(fingerCircle));
            ft.play();
            click.play();
            //root.getChildren().remove(fingerCircle);
            cursorIterator.remove();
            //togglePause();
        }
    }

    /**
     * Konfiguration der Tastatursteuerung
     */
    public void getUserInput () {

        // Periodische Tastenabfragen
        // z.B. keys.isDown(KeyCode)


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

    public void togglePlayerOneHasGamepad () {
        playerOneHasGamepad = !playerOneHasGamepad;
        if(playerOneHasGamepad) {
            assert ImageFiles.Y3 != null;
            p1GamepadIcon.setImage(ImageFX.getImage(ImageFiles.Y3).getImage());
        } else {
            assert ImageFiles.Y != null;
            p1GamepadIcon.setImage(ImageFX.getImage(ImageFiles.Y).getImage());
        }
    }

    public void startPong () {

        Rectangle background = new Rectangle(2500,2500,GameColor.VIOLETT);

        root.getChildren().clear();
        root.getChildren().addAll(
                background,
                curt
        );

        gbd.addToStage();



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


        pauseScreen =  new PauseScreen(root);
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
            MusicFX.THE_GRID.pause();
            MusicFX.MAZE.pause();
            gbd.getMiddleCircleScaleUp().play();
            gbd.getMiddleCircleScaleUp().setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

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
        double random = (Math.random()*2+6);
        // Ballinteraktion mit Spielern
        if(ball.intersects(playerOne)) {
            playerRumbleTimer = 5;
            finalDevice.setVibration(0, 40000);
            currentPlayer = playerOne;
            playerOne.getPrimarySound().play();
            ball.increaseBallHits();
            ball.setBallAngle(-Math.sin(Math.toRadians(playerOne.getTranslateY()-ball.getTranslateY()))*random);
            ball.setBallSpeed(currentPlayer);
        }
        else if (ball.intersects(playerTwo)) {
            playerRumbleTimer = 5;
            finalDevice.setVibration(0, 40000);
            currentPlayer = playerTwo;
            playerTwo.getPrimarySound().play();
            ball.increaseBallHits();
            ball.setBallAngle(-Math.sin(Math.toRadians(playerTwo.getTranslateY()-ball.getTranslateY()))*random);
            ball.setBallSpeed(currentPlayer);
        }
        // Ballinteraktion mit Seitenbanden
        if(ball.intersects(curt.getNorthBorder())) {
            shortTimer = 15;
            finalDevice.setVibration(10000, 20000);
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
            shortTimer = 15;
            finalDevice.setVibration(10000, 20000);
            TranslateTransition shakeSouth = new TranslateTransition(new Duration(100),camera);
            shakeSouth.setFromY(-5);
            shakeSouth.setToY(0);
            shakeSouth.setCycleCount(1);
            shakeSouth.setAutoReverse(true);
            shakeSouth.play();
            AudioFX.SFX3.play();
            ball.reflect();
        }
        if(shortTimer > 0)
        {
            shortTimer--;
        } else if (playerRumbleTimer > 0)
        {
            playerRumbleTimer--;
        } else {
            finalDevice.setVibration(0, 0);
        }
    }

    public void updateImageObjects () {
        for (Map.Entry<ImageView,TuioObject> imageView : imageObjects.entrySet()) {

        }
    }

    public void updateTools() {
        for (TuioObject marker : gamepadListener.getGamepads()) {
            switch (marker.getSymbolID()) {
                case 12:
                    a1.setTranslateX((int)(marker.getScreenX((int) (Screen.getPrimary().getBounds().getWidth()))-(Screen.getPrimary().getBounds().getWidth()/2))+100);
                    a1.setTranslateY((int)(marker.getScreenY((int) (Screen.getPrimary().getBounds().getHeight()))-(Screen.getPrimary().getBounds().getHeight()/2)));
                    a1.setRotate(marker.getAngleDegrees());
                    break;
            }
        }
    }

    public void updatePlayer() {

        ball.move();
        // Aktive Marker ermitteln.
        gamepads = gamepadListener.getGamepads();

        if(!playerOneIsPresent) {
            if(!playerOneHasKeyboard && !playerOneHasGamepad) {
                // CPU-Steuerung für Spieler 1
                if(!playerOne.intersects(curt.getNorthBorder())) {
                    if (playerOne.getTranslateY()-ball.getTranslateY() > 100) {
                        playerOne.setTranslateY(playerOne.getTranslateY()-10);
                    } else if (playerOne.getTranslateY()-ball.getTranslateY() > 50) {
                        playerOne.setTranslateY(playerOne.getTranslateY()-4.5);
                    }  else if (playerOne.getTranslateY()-ball.getTranslateY() > 10)  {
                        playerOne.setTranslateY(playerOne.getTranslateY()-.5);
                    }  else if (playerOne.getTranslateY()-ball.getTranslateY() > 1) {
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
                    } else if (playerOne.getTranslateY()-ball.getTranslateY() < -50) {
                        playerOne.setTranslateY(playerOne.getTranslateY()+4.7);
                    }  else if (playerOne.getTranslateY()-ball.getTranslateY() < -10)  {
                        playerOne.setTranslateY(playerOne.getTranslateY()+.5);
                    }  else if (playerOne.getTranslateY()-ball.getTranslateY() < -1) {
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
                            playerOne.setTranslateY(playerOne.getInverter() * playerOne.getTranslateY() - 7);
                        }
                    }
                    if (keys.isDown(ButtonConfig.south2)) {
                        if (!playerOne.intersects(curt.getSouthBorder())) {
                            playerOne.setTranslateY(playerOne.getInverter() * playerOne.getTranslateY() + 7);
                        }
                    }
                } else {
                    if (keys.isDown(ButtonConfig.south2)) {
                        if (!playerOne.intersects(curt.getNorthBorder())) {
                            playerOne.setTranslateY(playerOne.getInverter() * playerOne.getTranslateY() - 7);
                        }
                    }
                    if (keys.isDown(ButtonConfig.north2)) {
                        if (!playerOne.intersects(curt.getSouthBorder())) {
                            playerOne.setTranslateY(playerOne.getInverter() * playerOne.getTranslateY() + 7);
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
                            playerTwo.setTranslateY(playerTwo.getTranslateY()-7);
                        }
                    }
                    if(keys.isDown(ButtonConfig.south)) {
                        if(!playerTwo.intersects(curt.getSouthBorder())) {
                            playerTwo.setTranslateY(playerTwo.getTranslateY()+7);
                        }
                    }
                } else {
                    if(keys.isDown(ButtonConfig.south)) {
                        if(!playerTwo.intersects(curt.getNorthBorder())) {
                            playerTwo.setTranslateY(playerTwo.getTranslateY()-7);
                        }
                    }
                    if(keys.isDown(ButtonConfig.north)) {
                        if(!playerTwo.intersects(curt.getSouthBorder())) {
                            playerTwo.setTranslateY(playerTwo.getTranslateY()+7);
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
                        if(getSpeed(gamepad.getAngleDegrees(),playerOne) > 0.4) {
                            playerOne.setTranslateY( figure + getSpeed(gamepad.getAngleDegrees(),playerOne) );
                        }
                    } else if(playerOne.intersects(curt.getSouthBorder())) {
                        if(getSpeed(gamepad.getAngleDegrees(),playerOne) < -0.4) {
                            playerOne.setTranslateY( figure + getSpeed(gamepad.getAngleDegrees(),playerOne) );
                        }
                    } else if(getSpeed(gamepad.getAngleDegrees(),playerOne) < -0.4 || getSpeed(gamepad.getAngleDegrees(),playerOne) > 0.4){
                        playerOne.setTranslateY( figure + getSpeed(gamepad.getAngleDegrees(),playerOne)  );
                    }
                    break;
                case 2:
                    double figure2 = playerTwo.getTranslateY();
                    if(playerTwo.intersects(curt.getNorthBorder())) {
                        if(getSpeed(gamepad.getAngleDegrees(),playerTwo) > 0.4) {
                            playerTwo.setTranslateY( figure2 + getSpeed(gamepad.getAngleDegrees(),playerTwo));
                        }
                    } else if(playerTwo.intersects(curt.getSouthBorder())) {
                        if(getSpeed(gamepad.getAngleDegrees(),playerTwo) < -0.4) {
                            playerTwo.setTranslateY( figure2 + getSpeed(gamepad.getAngleDegrees(),playerTwo));
                        }
                    } else if(getSpeed(gamepad.getAngleDegrees(),playerTwo) < -0.4 || getSpeed(gamepad.getAngleDegrees(),playerTwo) > 0.4){
                        playerTwo.setTranslateY( figure2 + getSpeed(gamepad.getAngleDegrees(),playerTwo));
                    }
                    break;
                default:
                    if(verbose) System.out.println("Es sind nur Marker der IDs 1 und 2 erlaubt!");
                    break;
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

        double x = 2*Math.sin(Math.toRadians(angleDegrees-90));
        return currentPlayer.getInverter() * x * 12;
    }

    private double getAnalogSpeed(float axis, Paddle currentPlayer) {
        return currentPlayer.getInverter() * axis * 12;
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