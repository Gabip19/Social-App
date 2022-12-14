package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import service.Network;

public abstract class GuiController {
    protected static Network srv;
    protected static Stage currentStage;

    public static void setSrv(Network srv) {
        GuiController.srv = srv;
    }

    public static void setCurrentStage(Stage stage) {
        GuiController.currentStage = stage;
    }

    public void closeWindow(ActionEvent event) {
        currentStage.close();
    }

    public void minimizeWindow(ActionEvent event) {
        currentStage.setIconified(true);
    }

    public void maximizeWindow(ActionEvent event) {
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

    private class Delta { double x, y; }
}
