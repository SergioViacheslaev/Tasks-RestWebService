package ru.home.taskswebservice.dao;

import lombok.extern.slf4j.Slf4j;
import ru.home.taskswebservice.dao.jdbc.sessionmanager.SessionManager;
import ru.home.taskswebservice.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * UserDAO
 *
 * @author Sergei Viacheslaev
 */
@Slf4j
public class UserDaoJDBC implements DAO<User, String> {

    private final SessionManager sessionManager;

    public UserDaoJDBC(final SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }


    @Override
    public long insertRecord(User user) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<User> findById(String username) throws SQLException {
        User dbUser = null;
        sessionManager.beginSession();

        try (Connection connection = sessionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SQLTask.GET.QUERY)) {
            pst.setString(1, username);

            try (ResultSet rs = pst.executeQuery()) {

                if (rs.next()) {
                    dbUser = new User();
                    dbUser.setId(Integer.parseInt(rs.getString("id")));
                    dbUser.setUsername(rs.getString("username"));
                    dbUser.setPassword_hash(rs.getString("password_hash"));
                }
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            sessionManager.rollbackSession();
            throw ex;
        }

        return Optional.ofNullable(dbUser);
    }

    @Override
    public boolean update(User model) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean delete(User model) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<User> getAllById(Object id) throws SQLException {
        return null;
    }

    /**
     * SQL queries for users table.
     */
    enum SQLTask {
        GET("SELECT * FROM users WHERE username = (?)");

        String QUERY;

        SQLTask(String QUERY) {
            this.QUERY = QUERY;
        }
    }
}
