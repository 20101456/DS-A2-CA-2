module route.cityroutefinder {
    requires javafx.controls;
    requires javafx.fxml;
    requires jmh.core;


    opens Application to javafx.fxml;
    exports Application;
}