module ToySocialApp.main {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires java.sql;

    opens gui to javafx.fxml;
    exports gui;
}