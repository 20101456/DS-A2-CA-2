module route.cityroutefinder {
    requires javafx.controls;
    requires javafx.fxml;
    requires jmh.core;
    requires java.desktop;


    opens Application to javafx.fxml;
    exports Application;
}