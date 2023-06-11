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
    public static boolean verbose = false;

    @Override
    public void start(Stage stage) throws IOException {

        if(System.getProperty("os.name").contains("Windows")) {
            System.out.println("Xinput wird unterstützt.");
            // Check if XInput 1.4 is available
            if (XInputDevice14.isAvailable()) {
                System.out.println("XInput 1.4 ist verfügbar.");
                // TODO: Input Listener instanziieren (nicht mehr im AppController).
            }
        }


        setStage(stage);

        // FXMLLoader fxmlLoader = new FXMLLoader(ReactApplication.class.getResource("react-main-view.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(ReactApplication.class.getResource("react-test-view.fxml"));
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