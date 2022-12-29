package repository.database;

import domain.TextMessage;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TextMessageDatabaseRepo extends AbstractDatabaseRepository<UUID, TextMessage>{

    public TextMessageDatabaseRepo(String tableName, String url, String username, String password) {
        super(tableName, url, username, password);
    }

    @Override
    protected TextMessage createEntity(ResultSet resultSet) throws SQLException {
        UUID id = resultSet.getObject("id", UUID.class);
        UUID senderId = resultSet.getObject("sender_id", UUID.class);
        UUID receiverId = resultSet.getObject("receiver_id", UUID.class);
        String text = resultSet.getString("text_message");
        LocalDateTime sentDate = resultSet.getTimestamp("sent_date").toLocalDateTime();

        return new TextMessage(id, senderId, receiverId, text, sentDate);
    }

    @Override
    public TextMessage save(TextMessage entity) {
        if (entity == null)
            throw new IllegalArgumentException("Entity must not be null.\n");

        if (entity.getText().length() > 511) {
            throw new IllegalArgumentException("Text is too long.\n");
        }

        String sql = "INSERT INTO messages VALUES (?, ?, ?, ?, ?)";
        try (
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, entity.getId());
            statement.setObject(2, entity.getSenderID());
            statement.setObject(3, entity.getReceiverID());
            statement.setString(4, entity.getText());
            statement.setTimestamp(5, Timestamp.valueOf(entity.getSentDate()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return entity;
    }

    @Override
    public TextMessage update(TextMessage entity) {
        if (entity == null)
            throw new IllegalArgumentException("Entity must not be null.\n");

        // validation

        if (findOne(entity.getId()) != null) {
            String sql = "UPDATE messages SET text_message = ? WHERE id = ?";
            try (
                    PreparedStatement statement = connection.prepareStatement(sql)
            ) {
                statement.setString(1, entity.getText());
                statement.setObject(2, entity.getId());
                statement.executeUpdate();

                return entity;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        throw new IllegalArgumentException("The entity to be updated does not exist.\n");
    }

    public Iterable<TextMessage> getMessagesBetweenUsers(UUID id1, UUID id2) {
        List<TextMessage> entities = new ArrayList<>();
        String sql = "SELECT * FROM messages WHERE (sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?) ORDER BY sent_date";
        try (
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, id1);
            statement.setObject(2, id2);
            statement.setObject(3, id2);
            statement.setObject(4, id1);

            var rez = statement.executeQuery();
            entities = (List<TextMessage>) getEntitiesFromResult(rez);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entities;
    }
}
