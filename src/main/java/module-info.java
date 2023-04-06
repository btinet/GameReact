module com.ivision.gamereact {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens com.ivision.gamereact to javafx.fxml;
    exports com.ivision.gamereact;
}