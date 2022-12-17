package controller;

import javafx.scene.Node;
import javafx.stage.Stage;
import service.Network;

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

    public void closeWindow() {
        currentStage.close();
    }

    public void minimizeWindow() {
        currentStage.setIconified(true);
    }

    public void maximizeWindow() {
        currentStage.setMaximized(!currentStage.isMaximized());
    }

    public void defineDraggableNode(Node node) {    // TODO: 12/16/22 define for signin and signup too
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
