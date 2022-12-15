package gui.components;

import domain.User;
import javafx.scene.control.ListCell;
import service.Network;

public class UserListCell extends ListCell<User> {
    private final Network srv;

    public UserListCell(Network srv) {
        super();
        this.srv = srv;
        setStyle("-fx-padding: 0px; -fx-background-color: rgba(0, 0, 0, 0);");
        setPrefWidth(0);
    }

    @Override
    protected void updateItem(User item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setGraphic(null);
            setText(null);
        } else {
            UserCell userCell = new UserCell();
            userCell.setNameLabelText(item.getFirstName() + " " + item.getLastName());



            setText(null);
            setGraphic(userCell.getAnchorRoot());
        }
    }
}
