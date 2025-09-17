package ro.mpp2025.repository.database;

import ro.mpp2025.domain.Programmer;
import ro.mpp2025.domain.Tester;
import ro.mpp2025.repository.interfaces.RepoInterfaceProgrammer;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class RepoDBProgrammer implements RepoInterfaceProgrammer {
    protected String url;
    protected String usernameDB;
    protected String passwordDB;
    public RepoDBProgrammer(String url, String usernameDB, String passwordDB) {
        this.url = url;
        this.usernameDB = usernameDB;
        this.passwordDB = passwordDB;
    }
    @Override
    public Optional<Programmer> findOne(Long programmerID) {
        try (Connection connection = DriverManager.getConnection(url, usernameDB, passwordDB);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Programmers " +
                     "WHERE id_programmer = ?");
        ) {
            statement.setInt(1, Math.toIntExact(programmerID));
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");
                String name = resultSet.getString("name");
                Programmer programmer = new Programmer(username, password, email, name);
                programmer.setId(programmerID);
                return Optional.of(programmer);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Programmer> findAll() {
        Set<Programmer> programmers = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, usernameDB, passwordDB);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Programmers");
             ResultSet resultSet = statement.executeQuery();
        ) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("id_programmer");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");
                String name = resultSet.getString("name");
                Programmer programmer = new Programmer(username, password, email, name);
                programmer.setId(id);
                programmers.add(programmer);
            }
            return programmers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Programmer> save(Programmer entity) {
        try (Connection connection = DriverManager.getConnection(url, usernameDB, passwordDB);
             PreparedStatement statement = connection.prepareStatement("INSERT INTO Programmers(username, password, email, name) " +
                     "VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        ) {
            statement.setString(1, entity.getUsername());
            statement.setString(2, entity.getPassword());
            statement.setString(3, entity.getEmail());
            statement.setString(4, entity.getName());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                entity.setId(resultSet.getLong(1));
                return Optional.of(entity);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    public Optional<Programmer> findProgrammerByEmail(String email) {
        String query = "SELECT * FROM Programmers WHERE email = ?";
        try (Connection connection = DriverManager.getConnection(url, usernameDB, passwordDB);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Programmer programmer = new Programmer();
                programmer.setId(resultSet.getLong("id_programmer"));
                programmer.setUsername(resultSet.getString("username"));
                programmer.setPassword(resultSet.getString("password"));
                programmer.setEmail(resultSet.getString("email"));
                programmer.setName(resultSet.getString("name"));
                return Optional.of(programmer);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error while finding programmer by email", e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Programmer> delete(Long aLong) {
        Optional<Programmer> programmer = findOne(aLong);
        if (programmer.isEmpty()) {
            return Optional.empty();
        }
        try (Connection connection = DriverManager.getConnection(url, usernameDB, passwordDB);
             PreparedStatement statement = connection.prepareStatement("DELETE FROM Programmers " +
                     "WHERE id_programmer = ?");
        ) {
            statement.setInt(1, Math.toIntExact(aLong));
            statement.executeUpdate();
            return programmer;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Programmer> update(Programmer entity) {
        try (Connection connection = DriverManager.getConnection(url, usernameDB, passwordDB);
             PreparedStatement statement = connection.prepareStatement("UPDATE Programmers " +
                     "SET username = ?, password = ?, email = ?, name = ? " +
                     "WHERE id_programmer = ?");
        ) {
            statement.setString(1, entity.getUsername());
            statement.setString(2, entity.getPassword());
            statement.setString(3, entity.getEmail());
            statement.setString(4, entity.getName());
            statement.setLong(5, entity.getId());
            statement.executeUpdate();
            return Optional.of(entity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
