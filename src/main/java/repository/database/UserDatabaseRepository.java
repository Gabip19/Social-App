package repository.database;

import domain.User;
import domain.validators.Validator;
import repository.Repository;

import java.sql.*;
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
        return null;
    }

    @Override
    public Iterable<User> findAll() {
        Set<User> entities = new HashSet<>();
        String sql = "SELECT * FROM users";
        try (
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                UUID id = resultSet.getObject("id", UUID.class);
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                LocalDate birthdate = resultSet.getDate("birthdate").toLocalDate();

                User user = new User(id, firstName, lastName, email, birthdate);
                entities.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entities;
    }

    @Override
    public User save(User entity) {
        return null;
    }

    @Override
    public User delete(UUID uuid) {
        return null;
    }

    @Override
    public User update(User entity) {
        return null;
    }
}
