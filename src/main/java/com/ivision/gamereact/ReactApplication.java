package com.ivision.gamereact;

import com.github.strikerx3.jxinput.XInputDevice14;
import com.ivision.engine.KeyPolling;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import com.github.strikerx3.jxinput.XInputDevice;

import java.io.IOException;

public class ReactApplication extends Application {

    private com.github.strikerx3.jxinput.XInputDevice XInputDevice;

    // Attribute
    public static Stage stage;
    public static boolean fullscreen = false;
    public static boolean resizable = false;
    public static int width = 1400;
    public static int height = 860;
    public static String title = "GameReact";
    public static boolean verbose = true;

    @Override
    public void start(Stage stage) throws IOException {

        // Check if XInput 1.3 is available
        if (XInputDevice.isAvailable() && verbose) {
            System.out.println("XInput 1.3 is available on this platform");
        }

        // Check if XInput 1.4 is available
        if (XInputDevice14.isAvailable() && verbose) {
            System.out.println("XInput 1.4 is available on this platform");
        }

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

    public static void setStage(Stage currentStage) {
        stage = currentStage;
    }

    public static void main(String[] args) {
        launch();
    }
}