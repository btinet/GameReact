module com.ivision.gamereact {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.ivision.gamereact to javafx.fxml;
    exports com.ivision.gamereact;
    exports com.ivision.gamereact.controller;
    opens com.ivision.gamereact.controller to javafx.fxml;
}