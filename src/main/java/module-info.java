module com.ivision.gamereact {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.ivision.gamereact to javafx.fxml;
    exports com.ivision.gamereact;
    exports com.ivision.gamereact.controller;
    exports com.ivision.engine;
    exports com.ivision.gamereact.entity;
    exports com.ivision.gamereact.view;
    opens com.ivision.gamereact.controller to javafx.fxml;
}