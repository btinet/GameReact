module com.ivision.gamereact {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires JXInput;


    opens com.ivision.gamereact to javafx.fxml;
    exports com.ivision.gamereact;
    exports com.ivision.gamereact.controller;
    exports com.ivision.engine;
    exports com.ivision.gamereact.view;
    exports com.tuio;
    opens com.ivision.gamereact.controller to javafx.fxml;
    exports com.ivision.gamereact.entity.app;
    exports com.ivision.gamereact.entity.pong;
    exports com.ivision.gamereact.view.pong;
}