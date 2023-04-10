package com.ivision.gamereact.controller;

import com.ivision.engine.*;
import com.ivision.gamereact.ReactApplication;
import com.ivision.gamereact.entity.Ball;
import com.ivision.gamereact.entity.Paddle;
import com.ivision.gamereact.model.GamepadListener;
import com.tuio.TuioClient;
import com.tuio.TuioCursor;
import com.tuio.TuioObject;
import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.io.IOException;
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

    // Dekoration
    Circle strafeLeft = new Circle(ReactApplication.height);
    Circle strafeLeftStroke = new Circle(ReactApplication.height+200);
    Circle strafeRight = new Circle(ReactApplication.height);
    Circle strafeRightStroke = new Circle(ReactApplication.height+200);
    Circle middleCircleBig = new Circle(50);
    Line middleLine = new Line();

    // Spielfeld
    Rectangle playground = new Rectangle(880,720);
    Line northBorder = new Line();
    Line southBorder = new Line();

    // Spielfeldkamera
    Camera camera = new ParallelCamera();

    // Infotext für Pausenmodus
    Group helpImageGroup = new Group();
    Text leftText = new Text();
    Text rightText = new Text();
    Text leftPauseText = new Text();
    Text rightPauseText = new Text();

    // Fingereingabe
    private Circle fingerCircle;

    // Effekte
    FadeTransition p1t;
    FadeTransition p2t;

    // Schriften
    Font font = Font.loadFont(this.getClass().getResourceAsStream("/fonts/ErbosDraco1StOpenNbpRegular-l5wX.ttf"), 46);
    Font font2 = Font.loadFont(this.getClass().getResourceAsStream("/fonts/ErbosDraco1StOpenNbpRegular-l5wX.ttf"), 24);

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ball.setBallSpeed(9);
        root.setCursor(Cursor.NONE);

        URL f1Image = getClass().getResource("/png/1x/f1.png");
        URL f5Image = getClass().getResource("/png/1x/f5.png");
        URL f11Image = getClass().getResource("/png/1x/f11.png");

        try {
            assert f1Image != null;
            Image f1HelpImage = new Image(f1Image.openStream());
            ImageView f1ImageView = new ImageView();
            f1ImageView.setImage(f1HelpImage);
            f1ImageView.setTranslateX(-200);
            f1ImageView.setScaleX(-1);

            assert f5Image != null;
            Image f5HelpImage = new Image(f5Image.openStream());
            ImageView f5ImageView = new ImageView();
            f5ImageView.setImage(f5HelpImage);
            f5ImageView.setScaleX(-1);

            assert f11Image != null;
            Image f11HelpImage = new Image(f11Image.openStream());
            ImageView f11ImageView = new ImageView();
            f11ImageView.setImage(f11HelpImage);
            f11ImageView.setTranslateX(200);
            f11ImageView.setScaleX(-1);

            helpImageGroup.getChildren().addAll(f1ImageView,f11ImageView,f5ImageView);
            helpImageGroup.setTranslateY(395);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        FillTransition strafeLeftTransition = new FillTransition(Duration.millis(100),strafeLeft);
        FillTransition strafeRightTransition = new FillTransition(Duration.millis(100),strafeRight);

        strafeLeftTransition.setToValue(new Color(0.392,0.567,0.988,.6));
        strafeLeftTransition.setFromValue(new Color(0,0,0,.1));
        strafeLeftTransition.setCycleCount(4);
        strafeLeftTransition.setAutoReverse(true);

        strafeRightTransition.setToValue(new Color(0.988,0.266,0.392,.6));
        strafeRightTransition.setFromValue(new Color(0,0,0,.1));
        strafeRightTransition.setCycleCount(4);
        strafeRightTransition.setAutoReverse(true);

        leftText.setFont(font2);
        leftText.setRotate(90);
        leftText.setTranslateX(-200);
        leftText.setTranslateY(-5);
        leftText.setScaleX(-1);

        rightText.setFont(font2);
        rightText.setRotate(-90);
        rightText.setTranslateX(200);
        rightText.setTranslateY(5);
        rightText.setScaleX(-1);

        leftPauseText.setFont(font);
        leftPauseText.setRotate(90);
        leftPauseText.setTranslateX(-100);
        leftPauseText.setTranslateY(-5);
        leftPauseText.setScaleX(-1);

        rightPauseText.setFont(font);
        rightPauseText.setRotate(-90);
        rightPauseText.setTranslateX(100);
        rightPauseText.setTranslateY(5);
        rightPauseText.setScaleX(-1);

        leftPauseText.setFill(new Color(1,1,1,1));
        rightPauseText.setFill(new Color(1,1,1,1));

        p1t = new FadeTransition(Duration.millis(500), leftPauseText);
        p2t = new FadeTransition(Duration.millis(500), rightPauseText);

        currentPlayer = playerOne;

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

        strafeLeft.setTranslateX(-width);
        strafeLeft.setFill(new Color(0,0,0,.1));

        strafeLeftStroke.setTranslateX(-width-100);
        strafeLeftStroke.setStrokeWidth(2);
        strafeLeftStroke.setStroke(new Color(1.0,0.67,0.26,.6));
        strafeLeftStroke.setFill(Color.TRANSPARENT);

        Circle leftSide = new Circle(height+200);
        leftSide.setTranslateX(-width-88);
        leftSide.setStrokeWidth(20);
        leftSide.setStroke(new Color(0.533,0.235,0.466,1));
        leftSide.setFill(Color.TRANSPARENT);

        strafeRight.setTranslateX(width);
        strafeRight.setFill(new Color(0,0,0,.1));

        strafeRightStroke.setTranslateX(width+100);
        strafeRightStroke.setStrokeWidth(2);
        strafeRightStroke.setStroke(new Color(1.0,0.67,0.26,.6));
        strafeRightStroke.setFill(Color.TRANSPARENT);

        Circle rightSide = new Circle(height+200);
        rightSide.setTranslateX(width+88);
        rightSide.setStrokeWidth(20);
        rightSide.setStroke(new Color(0.533,0.235,0.466,1));
        rightSide.setFill(Color.TRANSPARENT);

        middleLine.setStroke(new Color(0.533,0.235,0.466,1));
        middleLine.setStrokeWidth(4);
        middleLine.setStartY(0);
        middleLine.setEndY(height);

        middleCircleBig.setFill(new Color(0,0,0,.1));

        Rectangle background = new Rectangle(2500,2500,new Color(0.533,0.235,0.466,1));

        root.getChildren().addAll(
                background,
                strafeLeftStroke,
                strafeRightStroke,
                playground,
                leftSide,
                rightSide,
                playerOne.getPointsText(),
                playerTwo.getPointsText(),
                middleCircleBig,
                middleLine,
                playerOne,
                playerTwo,
                ball,
                strafeLeft,
                strafeRight,
                northBorder,
                southBorder,
                playerOne.getHealthPointGroup(),
                playerTwo.getHealthPointGroup()
        );


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

                // TODO: Tastatur generell in dieser Methode abfragen. Nicht in tick();
                // Im Pausenmodus Tastatur abfragen
                if(isPaused()) {
                        getUserInput();
                }

                // TODO: Ball-Handling in eigene Klasse übertragen
                // Ball geht ins Aus:
                if(!playground.getBoundsInParent().intersects(ball.getBoundsInParent())) {
                    togglePause();

                    System.out.println("Tor für " + currentPlayer.getId());

                    TranslateTransition cameraShake = new TranslateTransition(new Duration(100),camera);

                    cameraShake.setCycleCount(1);
                    cameraShake.setAutoReverse(false);

                    switch (currentPlayer.getId()) {
                        case "ROT":
                            playerTwo.decreaseHealthPoints();
                            if(playerTwo.getCurrentHealthPoints() > 1) {
                                leftText.setText("Rot trifft!");
                                leftText.setFill(new Color(0.988,0.266,0.392,1));
                                rightText.setText("Rot trifft!");
                                rightText.setFill(new Color(0.988,0.266,0.392,1));
                                AudioFX.rHitSFX.play();
                            } else {
                                leftText.setText("Rot gewinnt!");
                                leftText.setFill(new Color(0.988,0.266,0.392,1));
                                rightText.setText("Rot gewinnt!");
                                rightText.setFill(new Color(0.988,0.266,0.392,1));
                                AudioFX.rWinSFX.play();
                                System.out.println("ROT GEWINNT!");
                                gameOver(playerOne);
                            }
                            ball.setStartPosition(PaddlePosition.RIGHT);
                            currentPlayer = playerTwo;
                            strafeRightTransition.play();
                            cameraShake.setFromX(-5);
                            cameraShake.setToX(0);
                            break;
                        case "BLAU":
                            playerOne.decreaseHealthPoints();
                            if(playerOne.getCurrentHealthPoints() > 1) {
                                leftText.setText("Blau trifft!");
                                leftText.setFill(new Color(0.392,0.567,0.988,1));
                                rightText.setText("Blau trifft!");
                                rightText.setFill(new Color(0.392,0.567,0.988,1));
                                AudioFX.bHitSFX.play();
                            } else {
                                leftText.setText("Blau gewinnt!");
                                leftText.setFill(new Color(0.392,0.567,0.988,1));
                                rightText.setText("Blau gewinnt!");
                                rightText.setFill(new Color(0.392,0.567,0.988,1));
                                AudioFX.bWinSFX.play();
                                System.out.println("BLAU GEWINNT!");
                                gameOver(playerTwo);
                            }
                            ball.setStartPosition(PaddlePosition.LEFT);
                            currentPlayer = playerOne;
                            strafeLeftTransition.play();
                            cameraShake.setFromX(5);
                            cameraShake.setToX(0);
                            break;
                    }


                    cameraShake.play();

                    FadeTransition ft = new FadeTransition(Duration.millis(500), leftText);
                    ft.setFromValue(0);
                    ft.setToValue(1);

                    FadeTransition ft2 = new FadeTransition(Duration.millis(500), rightText);
                    ft2.setFromValue(0);
                    ft2.setToValue(1);

                    ft.play();
                    ft2.play();
                    if(!root.getChildren().contains(leftText)) root.getChildren().add(leftText);
                    if(!root.getChildren().contains(rightText)) root.getChildren().add(rightText);

                    if (ball.getBallAngle() < 0) {
                        ball.setBallAngle(1.5);
                    } else {
                        ball.setBallAngle(-1.5);
                    }
                }

                // Fingereingabe verarbeiten:
                cursors = gamepadListener.getFingers();
                Iterator<TuioCursor> cursorIterator = cursors.iterator();
                while (cursorIterator.hasNext()) {
                    int i = root.getChildren().size();
                    root.getChildren().remove(i-1);
                    TuioCursor cursor = cursorIterator.next();
                    fingerCircle = new Circle(25,Color.CYAN);
                    double dx = cursor.getX();
                    double dy = cursor.getY();
                    fingerCircle.setTranslateX((int)(cursor.getScreenX(width)-width/2));
                    fingerCircle.setTranslateY((int)(cursor.getScreenY(height)-height/2));
                    root.getChildren().add(fingerCircle);
                    setAndPlayColorTransition(fingerCircle);
                    cursorIterator.remove();
                    togglePause();
                }


            }

            // Spielzeit generieren
            @Override
            public void tick(float secondsSinceLastFrame) {

                // TODO: Abfrage nach handle(); übertragen
                // Tastaturabfrage
                getUserInput();

                // Intro Audio abspielen, danach nicht mehr!
                if(intro) {
                    if (!isGameOver) AudioFX.pongReactSFX.play();
                    intro = false;
                    togglePause();
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
    public void setAndPlayColorTransition(Shape shape) {
        FillTransition fingerTransition = new FillTransition(Duration.millis(300),shape);
        fingerTransition.setFromValue(new Color(0.992,0.967,0.3,.6));
        fingerTransition.setToValue(new Color(0.992,0.967,0.3,0));
        fingerTransition.setCycleCount(1);
        fingerTransition.setAutoReverse(false);
        fingerTransition.play();
    }

    /**
     * Konfiguration der Tastatursteuerung
     */
    public void getUserInput () {

        // Periodische Tastenabfragen
        // z.B. keys.down(KeyCode)

        // einmalige Tastenabfragen (innerhalb Anschlagverzögerung)
        if (keys.isPressed(ButtonConfig.toggleFullscreen))          toggleFullscreen();
        if (keys.isPressed(ButtonConfig.gameMenu))                  togglePause();
    }


    // TODO: Methoden für GameOver auslagern.
    /**
     * Zwischen Pausen- und Spielmodus wechseln.
     */
    public void togglePause() {
        if(gameLoop.isPaused()) {
            root.getChildren().remove(helpImageGroup);
            root.getScene().setCamera(camera);
            root.getChildren().remove(leftPauseText);
            root.getChildren().remove(rightPauseText);
            root.getChildren().remove(leftText);
            root.getChildren().remove(rightText);
            gameLoop.play();
            MusicFX.THE_GRID.pause();
            MusicFX.MAZE.play();
            p1t.stop();
            p2t.stop();
            if(isGameOver) {
                isGameOver = false;
            }

        } else {
            if(!root.getChildren().contains(helpImageGroup)) root.getChildren().add(helpImageGroup);
            gameLoop.pause();
            if(playerOne.getHealthPoints() > 0 || playerTwo.getHealthPoints() > 0) {
                if(isGameOver) {
                    leftPauseText.setText("Game over");
                    rightPauseText.setText("Game over");
                } else {
                    leftPauseText.setText("Pause");
                    rightPauseText.setText("Pause");
                }

                p1t.setFromValue(.5);
                p1t.setToValue(1);
                p1t.setCycleCount(-1);
                p1t.setAutoReverse(true);

                p2t.setFromValue(.5);
                p2t.setToValue(1);
                p2t.setCycleCount(-1);
                p2t.setAutoReverse(true);

                p1t.play();
                p2t.play();
                if(!root.getChildren().contains(leftPauseText)) root.getChildren().add(leftPauseText);
                if(!root.getChildren().contains(rightPauseText)) root.getChildren().add(rightPauseText);
            }

            MusicFX.THE_GRID.play();
            MusicFX.MAZE.stop();

        }
    }

    public void updatePlayer() {

        ball.move();

        // Aktive Marker ermitteln.
        gamepads = gamepadListener.getGamepads();

        // Fingereingabe ermitteln.
        cursors = gamepadListener.getFingers();

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

        // Steuerung für jede Fingereingabe
        Iterator<TuioCursor> cursorIterator = cursors.iterator();
        while (cursorIterator.hasNext()) {
            int i = root.getChildren().size();
            root.getChildren().remove(i-1);
            TuioCursor cursor = cursorIterator.next();
            Circle circle = new Circle(25,Color.CYAN);
            circle.setTranslateX((int)(cursor.getScreenX(width)-width/2));
            circle.setTranslateY((int)(cursor.getScreenY(height)-height/2));
            root.getChildren().add(circle);
            setAndPlayColorTransition(circle);
            togglePause();
            cursorIterator.remove();
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