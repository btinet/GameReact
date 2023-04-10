package com.ivision.gamereact.controller;

import com.ivision.engine.*;
import com.ivision.gamereact.entity.Ball;
import com.ivision.gamereact.entity.Paddle;
import com.ivision.gamereact.model.GamepadListener;
import com.ivision.gamereact.view.GameBoardDecoration;
import com.ivision.gamereact.view.PauseScreen;
import com.ivision.gamereact.view.Transitions;
import com.tuio.TuioClient;
import com.tuio.TuioCursor;
import com.tuio.TuioObject;
import javafx.animation.FillTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.*;
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
    public Line currentPlayer;
    public boolean isGameOver = false;
    boolean intro = true;
    private final TuioClient client = new TuioClient();
    private final KeyPolling keys = KeyPolling.getInstance();

    /**
     * Paddle 1
     */
    Paddle playerOne = new Paddle(PaddlePosition.LEFT, 5,new Color(0.988,0.266,0.392,1));

    /**
     * Paddle 2
     */
    Paddle playerTwo = new Paddle(PaddlePosition.RIGHT, 5, new Color(0.392,0.567,0.988,1));

    /**
     * Ball
     */
    private final Ball ball = new Ball(10);

    // Spielfeld
    Rectangle playground = new Rectangle(880,720);
    Line northBorder = new Line();
    Line southBorder = new Line();

    // Spielfeldkamera
    Camera camera = new ParallelCamera();
    TranslateTransition cameraTranslateTransition = Transitions.createTranslateTransition(200,camera);

    PauseScreen pauseScreen;

    // Fingereingabe
    private Circle fingerCircle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ball.setBallSpeed(9);
        root.setCursor(Cursor.NONE);

        currentPlayer = playerOne;

        // funktionales Spielfeld
        northBorder.setStartX(0);
        northBorder.setStartX(900);
        northBorder.setStroke(Color.TRANSPARENT);
        northBorder.setStrokeWidth(1);
        northBorder.setTranslateY(-360);

        southBorder.setStartX(0);
        southBorder.setStartX(900);
        southBorder.setStroke(Color.TRANSPARENT);
        southBorder.setStrokeWidth(1);
        southBorder.setTranslateY(360);

        playground.setFill(new Color(0,0,0,.1));
        playground.setArcHeight(40);
        playground.setArcWidth(40);

        Rectangle background = new Rectangle(2500,2500,new Color(0.533,0.235,0.466,1));

        root.getChildren().addAll(
                background,
                playground
        );

        GameBoardDecoration gbd = new GameBoardDecoration(root, playerOne, playerTwo);

        root.getChildren().addAll(
                playerOne.getPointsText(),
                playerTwo.getPointsText(),
                playerOne,
                playerTwo,
                ball,
                northBorder,
                southBorder,
                playerOne.getHealthPointGroup(),
                playerTwo.getHealthPointGroup()
        );

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
                getEntityInteractions(gbd);

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
        isGameOver = true;
        intro = true;
        player.increaseMatchPoints();
        playerOne.reset();
        playerTwo.reset();
        gameLoop.stop();
        gameLoop.start();
    }

    /**
     * Tastaturabfrage
     * @param currentScene Szene (Fenster), in der die Keyboardabfrage stattfinden soll.
     */
    public void setPolling(Scene currentScene) {
        KeyPolling.getInstance().pollScene(currentScene);
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
            int i = root.getChildren().size();
            TuioCursor cursor = cursorIterator.next();
            fingerCircle = new Circle(25,Color.CYAN);
            fingerCircle.setTranslateX((int)(cursor.getScreenX(width)-width/2));
            fingerCircle.setTranslateY((int)(cursor.getScreenY(height)-height/2));
            if(!root.getChildren().contains(fingerCircle)) root.getChildren().add(fingerCircle);
            setAndPlayFillTransition(fingerCircle);
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
        if (keys.isPressed(ButtonConfig.toggleFullscreen))          toggleFullscreen();
        if (keys.isPressed(ButtonConfig.actionTertiary))                 togglePause();
    }


    // TODO: Methoden für GameOver auslagern.
    /**
     * Zwischen Pausen- und Spielmodus wechseln.
     */
    public void togglePause() {
        if(gameLoop.isPaused()) {
            root.getScene().setCamera(camera);
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
            if(isGameOver) {
                pauseScreen.showPauseText("Game over");
            } else {
                pauseScreen.showPauseText("Pause");
            }
            MusicFX.THE_GRID.play();
            MusicFX.MAZE.pause();
        }
    }

    public void getEntityInteractions (GameBoardDecoration gbd) {

        // Ball geht ins Aus:
        if(!playground.getBoundsInParent().intersects(ball.getBoundsInParent())) {

            if (currentPlayer.equals(playerOne)) {
                playerTwo.decreaseHealthPoints();
                if (playerTwo.getCurrentHealthPoints() > 1) {
                    pauseScreen.showText("Rot trifft!", playerOne.getPrimaryColor());
                    AudioFX.rHitSFX.play();
                } else {
                    pauseScreen.showText("Rot gewinnt!", playerOne.getPrimaryColor());
                    AudioFX.rWinSFX.play();
                    gameOver(playerOne);
                }
                ball.setStartPosition(PaddlePosition.RIGHT);
                currentPlayer = playerTwo;
                gbd.playStrafeAnimation(PaddlePosition.LEFT);
                cameraTranslateTransition.setFromX(-5);
                cameraTranslateTransition.setToX(0);
            } else if (currentPlayer.equals(playerTwo)) {
                playerOne.decreaseHealthPoints();
                if (playerOne.getCurrentHealthPoints() > 1) {
                    pauseScreen.showText("Blau trifft!", playerTwo.getPrimaryColor());
                    AudioFX.bHitSFX.play();
                } else {
                    pauseScreen.showText("Blau gewinnt!", playerTwo.getPrimaryColor());
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
            ball.reflect();
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
        if(playerOne.getBoundsInParent().intersects(ball.getBoundsInParent())) {
            currentPlayer = playerOne;
            playerOne.getPrimarySound().play();

            ball.setBallAngle(-Math.sin(Math.toRadians(playerOne.getTranslateY()-ball.getTranslateY()+random))*5);
            ball.setBallSpeed(PaddlePosition.LEFT);
        }
        else if (playerTwo.getBoundsInParent().intersects(ball.getBoundsInParent())) {
            currentPlayer = playerTwo;
            playerTwo.getPrimarySound().play();
            ball.setBallAngle(-Math.sin(Math.toRadians(playerTwo.getTranslateY()-ball.getTranslateY()+random))*5);
            ball.setBallSpeed(PaddlePosition.RIGHT);
        }
        // Ballinteraktion mit Seitenbanden
        if(northBorder.getBoundsInParent().intersects(ball.getBoundsInParent())) {
            TranslateTransition shakeNorth = new TranslateTransition(new Duration(100),camera);
            shakeNorth.setFromY(5);
            shakeNorth.setToY(0);
            shakeNorth.setCycleCount(1);
            shakeNorth.setAutoReverse(true);
            shakeNorth.play();
            AudioFX.SFX3.play();
            ball.reflect();
        }
        else if (southBorder.getBoundsInParent().intersects(ball.getBoundsInParent())) {
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

        // CPU-Steuerung für Spieler 1
        if(!playerOneIsPresent) {
            if(!northBorder.getBoundsInParent().intersects(playerOne.getBoundsInParent())) {
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
            if(!southBorder.getBoundsInParent().intersects(playerOne.getBoundsInParent())) {
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
        }

        // CPU-Steuerung für Spieler 2
        if(!playerTwoIsPresent) {
            if(!northBorder.getBoundsInParent().intersects(playerTwo.getBoundsInParent())) {
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
            if(!southBorder.getBoundsInParent().intersects(playerTwo.getBoundsInParent())) {
                if (playerTwo.getTranslateY()-ball.getTranslateY() < -100) {
                    playerTwo.setTranslateY(playerTwo.getTranslateY()+8);
                } else
                if (playerTwo.getTranslateY()-ball.getTranslateY() < -50) {
                    playerTwo.setTranslateY(playerTwo.getTranslateY()+4.4);
                }  else
                if (playerTwo.getTranslateY()-ball.getTranslateY() < -10)  {
                    playerTwo.setTranslateY(playerTwo.getTranslateY()+2);
                }  else
                if (playerTwo.getTranslateY()-ball.getTranslateY() < -1) {
                    playerTwo.setTranslateY(playerTwo.getTranslateY() + .1);
                } else {
                    playerTwo.setTranslateY(playerTwo.getTranslateY());
                }
            } else {
                playerTwo.setTranslateY(playerTwo.getTranslateY());
            }
        }

        // Manuelle Steuerung für jeden aktiven Spieler
        for (TuioObject gamepad : gamepads) {
            switch (gamepad.getSymbolID()) {
                case 1:
                    double figure = playerOne.getTranslateY();
                    if(northBorder.getBoundsInParent().intersects(playerOne.getBoundsInParent()))
                    {
                        playerOne.setTranslateY(figure+Math.abs(getSpeed(gamepad.getAngleDegrees())));
                    } else if(southBorder.getBoundsInParent().intersects(playerOne.getBoundsInParent()))
                    {
                        playerOne.setTranslateY(figure-Math.abs(getSpeed(gamepad.getAngleDegrees())));
                    } else {
                        playerOne.setTranslateY(figure+getSpeed(gamepad.getAngleDegrees()));
                    }

                    break;
                case 2:
                    double figure2 = playerTwo.getTranslateY();
                    if(northBorder.getBoundsInParent().intersects(playerTwo.getBoundsInParent()))
                    {
                        playerTwo.setTranslateY(figure2+Math.abs(getSpeed(gamepad.getAngleDegrees())));
                    } else if(southBorder.getBoundsInParent().intersects(playerTwo.getBoundsInParent()))
                    {
                        playerTwo.setTranslateY(figure2-Math.abs(getSpeed(gamepad.getAngleDegrees())));
                    } else {
                        playerTwo.setTranslateY(figure2+getSpeed(gamepad.getAngleDegrees()));
                    }
                    break;
                default:
                    System.out.println("Es sind nur Marker der IDs 1 und 2 erlaubt!");
            }
        }

    }

    /**
     *
     * @param angleDegrees Winkel in Grad
     * @return Sinus eines Winkels
     */
    private double getSpeed(float angleDegrees) {
        double x = Math.sin(Math.toRadians(angleDegrees-90));
        return x*7;
    }

    /**
     * Zwischen Fenster- und Vollbildmodus wechseln.
     */
    @FXML
    protected void toggleFullscreen() {

        stageWidth = root.getWidth();
        stageHeight = root.getHeight();

        if(stage.isFullScreen()) {
            stage.setFullScreen(false);
        } else {
            stage.setFullScreen(true);
        }
    }

}