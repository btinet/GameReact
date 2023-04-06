package com.ivision.gamereact;

import com.ivision.engine.KeyPolling;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class ReactApplication extends Application {

    // Attribute
    public static Stage stage;
    public static boolean fullscreen = false;
    public static boolean resizable = false;
    public static int width = 1400;
    public static int height = 860;
    public static String title = "GameReact";

    @Override
    public void start(Stage stage) throws IOException {

        setStage(stage);

        FXMLLoader fxmlLoader = new FXMLLoader(ReactApplication.class.getResource("react-main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), width, height);
        KeyPolling.getInstance().pollScene(scene);

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.exit();
                System.exit(0);
            }
        });

        stage.setTitle(title);
        stage.setScene(scene);
        stage.setResizable(resizable);
        stage.setFullScreen(fullscreen);
        stage.show();
    }



    public static Stage getStage() {
        return stage;
    }

    public static void setStage(Stage currentStage) {
        stage = currentStage;
    }

    public static void main(String[] args) {
        launch();
    }
}