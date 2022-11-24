package repository.database;

import domain.Friendship;
import domain.validators.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.UUID;

public class FriendshipDatabaseRepo extends AbstractDatabaseRepository<UUID, Friendship> {

    private final Validator<Friendship> validator;

    public FriendshipDatabaseRepo(String tableName, String url, String username, String password, Validator<Friendship> validator) {
        super(tableName, url, username, password);
        this.validator = validator;
    }

    @Override
    public Friendship save(Friendship entity) {
        if (entity == null)
            throw new IllegalArgumentException("Entity must not be null.\n");

        validator.validate(entity);

        String sql = "INSERT INTO friendships VALUES (?, ?, ?, ?)";
        try (
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, entity.getId());
            statement.setObject(2, entity.getUser1ID());
            statement.setObject(3, entity.getUser2ID());
            statement.setTimestamp(4, Timestamp.valueOf(entity.getFriendsFrom()));

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entity;
    }

    @Override
    public Friendship update(Friendship entity) {
        if (entity == null)
            throw new IllegalArgumentException("Entity must not be null.\n");

        validator.validate(entity);

        if (findOne(entity.getId()) != null) {
            String sql = "UPDATE friendships SET friends_from = ? WHERE id = ?";
            try (
                    PreparedStatement statement = connection.prepareStatement(sql)
            ) {
                statement.setTimestamp(1, Timestamp.valueOf(entity.getFriendsFrom()));
                statement.setObject(2, entity.getId());
                statement.executeUpdate();

                return entity;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        throw new IllegalArgumentException("The entity to be updated does not exist.\n");
    }

    @Override
    protected Friendship createEntity(ResultSet resultSet) throws SQLException {
        UUID id = resultSet.getObject("id", UUID.class);
        UUID user1ID = resultSet.getObject("user1_id", UUID.class);
        UUID user2ID = resultSet.getObject("user2_id", UUID.class);
        LocalDateTime friendsFrom = resultSet.getTimestamp("friends_from").toLocalDateTime();

        return new Friendship(id, user1ID, user2ID, friendsFrom.toString());
    }
}
