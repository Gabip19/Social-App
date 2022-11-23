package repository.database;

import domain.Entity;
import repository.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDatabaseRepository<UUID, E extends Entity<UUID>> implements Repository<UUID, E> {
    protected final String url;
    protected final String username;
    protected final String password;
    private final String tableName;

    public AbstractDatabaseRepository(String tableName, String url, String username, String password) {
        this.tableName = tableName;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public E findOne(UUID uuid) {
        if (uuid == null)
            throw new IllegalArgumentException("Id must not be null.\n");

        String strSql = "SELECT * FROM $table WHERE id = ?";
        String sql = strSql.replace("$table", tableName);
        try (
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, uuid);
            ResultSet resultSet = statement.executeQuery();

            List<E> entities = (List<E>) getEntitiesFromResult(resultSet);
            if (!entities.isEmpty()) {
                return entities.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Iterable<E> findAll() {
        List<E> entities = new ArrayList<>();
        String strSql = "SELECT * FROM $table";
        String sql = strSql.replace("$table", tableName);
        try (
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            entities = (List<E>) getEntitiesFromResult(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entities;
    }

    @Override
    public E delete(UUID uuid) {
        E entity = findOne(uuid);

        if (entity != null) {
            String strSql = "DELETE FROM $table WHERE id = ?";
            String sql = strSql.replace("$table", tableName);
            try (
                    Connection connection = DriverManager.getConnection(url, username, password);
                    PreparedStatement statement = connection.prepareStatement(sql)
            ) {
                statement.setObject(1, uuid);
                statement.executeUpdate();

                return entity;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        throw new IllegalArgumentException("Entity with the given ID does not exist.\n");
    }

    private Iterable<E> getEntitiesFromResult(ResultSet resultSet) throws SQLException {
        List<E> entities = new ArrayList<>();
        while (resultSet.next()) {
            E entity = createEntity(resultSet);
            entities.add(entity);
        }
        return entities;
    }

    protected abstract E createEntity(ResultSet resultSet) throws SQLException;
}
