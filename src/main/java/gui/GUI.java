package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class GUI extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/signin.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1146, 810);

        stage.setResizable(false);
        stage.setTitle("Colors App");
//        stage.getIcons().add(new Image("gui/testgui/styles/images/colorslogo.png"));
//        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}