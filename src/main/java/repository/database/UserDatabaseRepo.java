package repository.database;

import domain.HashedPasswordDTO;
import domain.User;
import domain.validators.Validator;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class UserDatabaseRepo extends AbstractDatabaseRepository<UUID, User> {

    private final Validator<User> validator;

    public UserDatabaseRepo(String tableName, String url, String username, String password, Validator<User> validator) {
        super(tableName, url, username, password);
        this.validator = validator;
    }

    @Override
    public User save(User entity) {
        if (entity == null)
            throw new IllegalArgumentException("Entity must not be null.\n");

        validator.validate(entity);

        String sql = "INSERT INTO users VALUES (?, ?, ?, ?, ?)";
        try (
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, entity.getId());
            statement.setString(2, entity.getFirstName());
            statement.setString(3, entity.getLastName());
            statement.setString(4, entity.getEmail());
            statement.setDate(5, Date.valueOf(entity.getBirthdate()));

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entity;
    }

    @Override
    public User update(User entity) {
        if (entity == null)
            throw new IllegalArgumentException("Entity must not be null.\n");

        validator.validate(entity);

        if (findOne(entity.getId()) != null) {
            String sql = "UPDATE users SET first_name = ?, last_name = ?, birthdate = ? WHERE id = ?";
            try (
                    PreparedStatement statement = connection.prepareStatement(sql)
            ) {
                statement.setString(1, entity.getFirstName());
                statement.setString(2, entity.getLastName());
                statement.setDate(3, Date.valueOf(entity.getBirthdate()));
                statement.setObject(4, entity.getId());
                statement.executeUpdate();

                return entity;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        throw new IllegalArgumentException("The entity to be updated does not exist.\n");
    }

    @Override
    protected User createEntity(ResultSet resultSet) throws SQLException {
        UUID id = resultSet.getObject("id", UUID.class);
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        String email = resultSet.getString("email");
        LocalDate birthdate = resultSet.getDate("birthdate").toLocalDate();

        return new User(id, firstName, lastName, email, birthdate);
    }

    public void setUserPassword(String email, HashedPasswordDTO passwordInfo) {
        String sql = "UPDATE users SET salt = ?, password = ? WHERE email = ?";
        try (
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setString(1, passwordInfo.getSalt());
            ps.setString(2, passwordInfo.getHashedPassword());
            ps.setString(3, email);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User findUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setString(1, email);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return createEntity(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HashedPasswordDTO getLoginInfo(UUID id) {
        String sql = "SELECT salt, password FROM users WHERE id = ?";
        HashedPasswordDTO passwordDTO = null;
        try (
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setObject(1, id);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                passwordDTO = new HashedPasswordDTO(
                    resultSet.getString("salt"),
                    resultSet.getString("password")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return passwordDTO;
    }
}
