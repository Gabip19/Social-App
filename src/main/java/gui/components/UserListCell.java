package gui.components;

import domain.User;
import javafx.scene.control.ListCell;

public class UserListCell extends ListCell<User> {
    public UserListCell() {
        super();
        setPrefWidth(0);
    }

    @Override
    protected void updateItem(User item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
            UserCell userCell = new UserCell();
            userCell.setNameLabelText(item.getFirstName() + " " + item.getLastName());

            setGraphic(userCell.getAnchorRoot());
        }
    }
}
