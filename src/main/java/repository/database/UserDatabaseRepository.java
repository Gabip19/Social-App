package repository.database;

import domain.User;
import domain.validators.Validator;
import repository.Repository;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class UserDatabaseRepository implements Repository<UUID, User> {

    private final String url;
    private final String username;
    private final String password;
    private final Validator<User> validator;

    public UserDatabaseRepository(String url, String username, String password, Validator<User> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public User findOne(UUID uuid) {
        if (uuid == null)
            throw new IllegalArgumentException("Id must not be null.\n");

        String sql = "SELECT * FROM users WHERE id = ?";
        try (
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, uuid);
            ResultSet resultSet = statement.executeQuery();

            List<User> users = (List<User>) getUsersFromResult(resultSet);
            if (!users.isEmpty()) {
                return users.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Iterable<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            users = (List<User>) getUsersFromResult(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public User save(User entity) {
        if (entity == null)
            throw new IllegalArgumentException("Entity must not be null.\n");

        validator.validate(entity);

        String sql = "INSERT INTO users VALUES (?, ?, ?, ?, ?)";
        try (
                Connection connection = DriverManager.getConnection(url, username, password);
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
    public User delete(UUID uuid) {
        User user = findOne(uuid);

        if (user != null) {
            String sql = "DELETE FROM users WHERE id = ?";
            try (
                    Connection connection = DriverManager.getConnection(url, username, password);
                    PreparedStatement statement = connection.prepareStatement(sql)
            ) {
                statement.setObject(1, uuid);
                statement.executeUpdate();

                return user;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        throw new IllegalArgumentException("Entity with the given ID does not exist.\n");
    }

    @Override
    public User update(User entity) {
        if (entity == null)
            throw new IllegalArgumentException("Entity must not be null.\n");

        validator.validate(entity);

        if (findOne(entity.getId()) != null) {
            String sql = "UPDATE users SET first_name = ?, last_name = ?, birthdate = ? WHERE id = ?";
            try (
                    Connection connection = DriverManager.getConnection(url, username, password);
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

    private Iterable<User> getUsersFromResult(ResultSet resultSet) throws SQLException {
        List<User> userList = new ArrayList<>();
        while (resultSet.next()) {
            User user = createUser(resultSet);
            userList.add(user);
        }
        return userList;
    }

    private static User createUser(ResultSet resultSet) throws SQLException {
        UUID id = resultSet.getObject("id", UUID.class);
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        String email = resultSet.getString("email");
        LocalDate birthdate = resultSet.getDate("birthdate").toLocalDate();

        return new User(id, firstName, lastName, email, birthdate);
    }
}
