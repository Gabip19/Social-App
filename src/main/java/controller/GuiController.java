package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import service.Network;

import java.io.IOException;

public abstract class GuiController {
    protected static Network srv;
    protected static Stage currentStage;
    private static class Delta { double x, y; }

    public static void setSrv(Network srv) {
        GuiController.srv = srv;
    }

    public static void setCurrentStage(Stage stage) {
        GuiController.currentStage = stage;
    }

    public static void switchToMainPage() throws IOException {
        GuiController.setSrv(srv);
        Stage stage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(GuiController.class.getResource("/gui/main-window.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1146, 810);

        GuiController.setCurrentStage(stage);

        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    public void closeWindow() {
        currentStage.close();
    }

    public void minimizeWindow() {
        currentStage.setIconified(true);
    }

    public void maximizeWindow() {
        currentStage.setMaximized(!currentStage.isMaximized());
    }

    public void defineDraggableNode(Node node) {
        // allow the clock background to be used to drag the clock around.
        final Delta dragDelta = new Delta();
        node.setOnMousePressed(mouseEvent -> {
            // record a delta distance for the drag and drop operation.
            dragDelta.x = currentStage.getX() - mouseEvent.getScreenX();
            dragDelta.y = currentStage.getY() - mouseEvent.getScreenY();
        });
        node.setOnMouseDragged(mouseEvent -> {
            currentStage.setX(mouseEvent.getScreenX() + dragDelta.x);
            currentStage.setY(mouseEvent.getScreenY() + dragDelta.y);
        });

    }


}
