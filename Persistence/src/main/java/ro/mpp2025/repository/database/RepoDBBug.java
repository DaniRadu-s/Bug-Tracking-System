package ro.mpp2025.repository.database;

import ro.mpp2025.domain.Bug;
import ro.mpp2025.domain.BugStatus;
import ro.mpp2025.domain.Programmer;
import ro.mpp2025.domain.Tester;
import ro.mpp2025.repository.interfaces.RepoInterfaceBug;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class RepoDBBug implements RepoInterfaceBug {
    protected String url;
    protected String usernameDB;
    protected String passwordDB;
    public RepoDBBug(String url, String usernameDB, String passwordDB) {
        this.url = url;
        this.usernameDB = usernameDB;
        this.passwordDB = passwordDB;
    }
    @Override
    public Optional<Bug> findOne(Long bugID) {
        try (Connection connection = DriverManager.getConnection(url, usernameDB, passwordDB);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Bugs " +
                     "WHERE id_bug = ?");
             PreparedStatement testerStatement = connection.prepareStatement("SELECT * FROM Testers " +
                     "WHERE id_tester = ?");
             PreparedStatement programmerStatement = connection.prepareStatement("SELECT * FROM Programmers " +
                     "WHERE id_programmer = ?");
        ) {
            statement.setInt(1, Math.toIntExact(bugID));
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                Long reportedById = resultSet.getLong("testerID");
                Long assignedToId = resultSet.getLong("programmerID");
                String statusStr = resultSet.getString("status");
                BugStatus status = BugStatus.valueOf(statusStr);
                Timestamp timestamp = resultSet.getTimestamp("date");
                LocalDateTime date = timestamp.toLocalDateTime();

                testerStatement.setLong(1, reportedById);
                ResultSet testerResultSet = testerStatement.executeQuery();
                Tester tester = null;
                if (testerResultSet.next()) {
                    String testerUsername = testerResultSet.getString("username");
                    String testerPassword = testerResultSet.getString("password");
                    String testerEmail = testerResultSet.getString("email");
                    String testerName = testerResultSet.getString("name");
                    tester = new Tester(testerUsername, testerPassword, testerEmail, testerName);
                }

                programmerStatement.setLong(1, assignedToId);
                ResultSet programmerResultSet = programmerStatement.executeQuery();
                Programmer programmer = null;
                if (programmerResultSet.next()) {
                    String programmerUsername = programmerResultSet.getString("username");
                    String programmerPassword = programmerResultSet.getString("password");
                    String programmerEmail = programmerResultSet.getString("email");
                    String programmerName = programmerResultSet.getString("name");
                    programmer = new Programmer(programmerUsername, programmerPassword, programmerEmail, programmerName);
                }

                Bug bug = new Bug(name, description, tester, programmer,date);
                bug.setStatus(status);
                bug.setDate(date);
                bug.setId(bugID);
                return Optional.of(bug);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Bug> findAll() {
        Set<Bug> bugs = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, usernameDB, passwordDB);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Bugs");
             PreparedStatement testerStatement = connection.prepareStatement("SELECT * FROM Testers " +
                     "WHERE id_tester = ?");
             PreparedStatement programmerStatement = connection.prepareStatement("SELECT * FROM Programmers " +
                     "WHERE id_programmer = ?");
        ) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long bugID = resultSet.getLong("id_bug");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                Long reportedById = resultSet.getLong("testerID");
                Long assignedToId = resultSet.getLong("programmerID");
                String statusStr = resultSet.getString("status");
                BugStatus status = BugStatus.valueOf(statusStr);
                Timestamp timestamp = resultSet.getTimestamp("date");
                LocalDateTime date = timestamp.toLocalDateTime();

                testerStatement.setLong(1, reportedById);
                ResultSet testerResultSet = testerStatement.executeQuery();
                Tester tester = null;
                if (testerResultSet.next()) {
                    String testerUsername = testerResultSet.getString("username");
                    String testerPassword = testerResultSet.getString("password");
                    String testerEmail = testerResultSet.getString("email");
                    String testerName = testerResultSet.getString("name");
                    tester = new Tester(testerUsername, testerPassword, testerEmail, testerName);
                }

                programmerStatement.setLong(1, assignedToId);
                ResultSet programmerResultSet = programmerStatement.executeQuery();
                Programmer programmer = null;
                if (programmerResultSet.next()) {
                    String programmerUsername = programmerResultSet.getString("username");
                    String programmerPassword = programmerResultSet.getString("password");
                    String programmerEmail = programmerResultSet.getString("email");
                    String programmerName = programmerResultSet.getString("name");
                    programmer = new Programmer(programmerUsername, programmerPassword, programmerEmail, programmerName);
                }

                if (tester != null && programmer != null) {
                    Bug bug = new Bug(name, description, tester, programmer, date);
                    bug.setStatus(status);
                    bug.setId(bugID);
                    bugs.add(bug);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return bugs;
    }

    @Override
    public Optional<Bug> save(Bug entity) {
        try (Connection connection = DriverManager.getConnection(url, usernameDB, passwordDB);
             PreparedStatement statement = connection.prepareStatement("INSERT INTO Bugs(name, description, testerID, programmerID, status, date) " +
                     "VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        ) {
            statement.setString(1, entity.getName());
            statement.setString(2, entity.getDescription());
            statement.setLong(3, entity.getTester().getId());
            statement.setLong(4, entity.getProgrammer().getId());
            statement.setString(5, entity.getStatus().toString());
            statement.setTimestamp(6, Timestamp.valueOf(entity.getDate()));
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                entity.setId(generatedKeys.getLong(1));
                return Optional.of(entity);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Bug> delete(Long aLong) {
        try (Connection connection = DriverManager.getConnection(url, usernameDB, passwordDB);
             PreparedStatement statement = connection.prepareStatement("DELETE FROM Bugs " +
                     "WHERE id_bug = ?");
        ) {
            Optional<Bug> toDelete = findOne(aLong);
            if (toDelete.isPresent()) {
                statement.setLong(1, aLong);
                int result = statement.executeUpdate();
                if (result > 0) {
                    return toDelete;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Bug> update(Bug entity) {
        try (Connection connection = DriverManager.getConnection(url, usernameDB, passwordDB);
             PreparedStatement statement = connection.prepareStatement("UPDATE Bugs " +
                     "SET name = ?, description = ?, testerID = ?, programmerID = ?, status = ?, date = ? " +
                     "WHERE id_bug = ?");
        ) {
            statement.setString(1, entity.getName());
            statement.setString(2, entity.getDescription());
            statement.setLong(3, entity.getTester().getId());
            statement.setLong(4, entity.getProgrammer().getId());
            statement.setString(5, entity.getStatus().toString());
            statement.setTimestamp(6, Timestamp.valueOf(entity.getDate()));
            statement.setLong(7, entity.getId());
            int result = statement.executeUpdate();
            if (result > 0) {
                return Optional.of(entity);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    public Optional<Bug> findBugByName(String name) {
        try (Connection connection = DriverManager.getConnection(url, usernameDB, passwordDB);
             PreparedStatement bugStatement = connection.prepareStatement("SELECT * FROM Bugs WHERE name = ?");
             PreparedStatement testerStatement = connection.prepareStatement("SELECT * FROM Testers WHERE id_tester = ?");
             PreparedStatement programmerStatement = connection.prepareStatement("SELECT * FROM Programmers WHERE id_programmer = ?");
        ) {
            bugStatement.setString(1, name);
            ResultSet resultSet = bugStatement.executeQuery();

            if (resultSet.next()) {
                Long bugID = resultSet.getLong("id_bug");
                String description = resultSet.getString("description");
                Long testerId = resultSet.getLong("testerID");
                Long programmerId = resultSet.getLong("programmerID");
                String statusStr = resultSet.getString("status");
                Timestamp timestamp = resultSet.getTimestamp("date");
                LocalDateTime date = timestamp.toLocalDateTime();

                testerStatement.setLong(1, testerId);
                ResultSet testerResult = testerStatement.executeQuery();
                Tester tester = null;
                if (testerResult.next()) {
                    tester = new Tester(
                            testerResult.getString("username"),
                            testerResult.getString("password"),
                            testerResult.getString("email"),
                            testerResult.getString("name")
                    );
                    tester.setId(testerId);
                }

                programmerStatement.setLong(1, programmerId);
                ResultSet programmerResult = programmerStatement.executeQuery();
                Programmer programmer = null;
                if (programmerResult.next()) {
                    programmer = new Programmer(
                            programmerResult.getString("username"),
                            programmerResult.getString("password"),
                            programmerResult.getString("email"),
                            programmerResult.getString("name")
                    );
                    programmer.setId(programmerId);
                }

                if (tester != null && programmer != null) {
                    Bug bug = new Bug(name, description, tester, programmer, date);
                    bug.setStatus(BugStatus.valueOf(statusStr));
                    bug.setId(bugID);
                    return Optional.of(bug);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

}
