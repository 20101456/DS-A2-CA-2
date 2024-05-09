module route.cityroutefinder {
    requires javafx.controls;
    requires javafx.fxml;
    requires jmh.core;
    requires java.desktop;
    requires org.apache.commons.csv;


    opens Application to javafx.fxml;
    exports Application;
}