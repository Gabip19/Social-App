module ToySocialApp.main {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires java.sql;

    opens gui to javafx.fxml;
    exports gui;
    opens controller to javafx.fxml;
    exports controller;
    exports gui.components;
    opens gui.components to javafx.fxml;
    exports gui.components.message;
    opens gui.components.message to javafx.fxml;
}