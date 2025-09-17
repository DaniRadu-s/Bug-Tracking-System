package ro.mpp2025.repository.database;

import ro.mpp2025.domain.Tester;
import ro.mpp2025.repository.interfaces.RepoInterfaceTester;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class RepoDBTester implements RepoInterfaceTester {
    protected String url;
    protected String usernameDB;
    protected String passwordDB;
    public RepoDBTester(String url, String usernameDB, String passwordDB) {
        this.url = url;
        this.usernameDB = usernameDB;
        this.passwordDB = passwordDB;
    }
    @Override
    public Optional<Tester> findOne(Long testerID) {
        try (Connection connection = DriverManager.getConnection(url, usernameDB, passwordDB);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Testers " +
                     "WHERE id_tester = ?");
        ) {
            statement.setInt(1, Math.toIntExact(testerID));
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");
                String name = resultSet.getString("name");
                Tester tester = new Tester(username, password, email, name);
                tester.setId(testerID);
                return Optional.of(tester);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Tester> findAll() {
        Set<Tester> testers = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, usernameDB, passwordDB);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Testers");
             ResultSet resultSet = statement.executeQuery();
        ) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("id_tester");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");
                String name = resultSet.getString("name");
                Tester tester = new Tester(username, password, email, name);
                tester.setId(id);
                testers.add(tester);
            }
            return testers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Tester> save(Tester entity) {
        try (Connection connection = DriverManager.getConnection(url, usernameDB, passwordDB);
             PreparedStatement statement = connection.prepareStatement("INSERT INTO Testers(username, password, email, name) " +
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

    public Optional<Tester> findTesterByEmail(String email) {
        String query = "SELECT * FROM Testers WHERE email = ?";
        try (Connection connection = DriverManager.getConnection(url, usernameDB, passwordDB);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Tester tester = new Tester();
                tester.setId(resultSet.getLong("id_tester"));
                tester.setUsername(resultSet.getString("username"));
                tester.setPassword(resultSet.getString("password"));
                tester.setEmail(resultSet.getString("email"));
                tester.setName(resultSet.getString("name"));
                return Optional.of(tester);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error while finding tester by email", e);
        }

        return Optional.empty();
    }


    @Override
    public Optional<Tester> delete(Long testerID) {
        Optional<Tester> testerOpt = findOne(testerID);
        if (testerOpt.isEmpty()) {
            return Optional.empty();
        }

        try (Connection connection = DriverManager.getConnection(url, usernameDB, passwordDB);
             PreparedStatement statement = connection.prepareStatement("DELETE FROM Testers WHERE id_tester = ?")
        ) {
            statement.setInt(1, Math.toIntExact(testerID));
            statement.executeUpdate();
            return testerOpt;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Optional<Tester> update(Tester entity) {
        try (Connection connection = DriverManager.getConnection(url, usernameDB, passwordDB);
             PreparedStatement statement = connection.prepareStatement("UPDATE Testers " +
                     "SET username = ?, password = ?, email = ?, name = ? " +
                     "WHERE id_tester = ?");
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
