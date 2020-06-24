package ru.home.taskswebservice.dao;

import lombok.extern.slf4j.Slf4j;
import ru.home.taskswebservice.dao.jdbc.UserDao;
import ru.home.taskswebservice.dao.jdbc.sessionmanager.SessionManager;
import ru.home.taskswebservice.model.User;

import java.sql.*;
import java.util.List;
import java.util.Optional;

/**
 * UserDAO
 *
 * @author Sergei Viacheslaev
 */
@Slf4j
public class UserDaoJDBC implements UserDao {


    private final SessionManager sessionManager;

    public UserDaoJDBC(final SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public Optional<User> findById(int id) throws SQLException {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) throws SQLException {
        User dbUser = null;
        sessionManager.beginSession();

        try (Connection connection = sessionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SQLTask.GET_USER_BY_USERNAME.QUERY)) {
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
    public int insertUser(User user) throws SQLException {
        sessionManager.beginSession();

        try (Connection connection = sessionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SQLTask.INSERT_USER.QUERY, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, user.getUsername());
            pst.setString(2, user.getPassword_hash());
            pst.setString(3, user.getName());
            pst.setString(4, user.getSurname());
            pst.setString(5, user.getEmail());

            pst.executeUpdate();
            try (ResultSet rs = pst.getGeneratedKeys()) {
                rs.next();
                int id = rs.getInt(1);
                sessionManager.commitSession();
                return id;
            }

        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            sessionManager.rollbackSession();
            throw ex;
        }

    }

    @Override
    public Optional<User> findByEmail(String email) throws SQLException {
        User dbUser = null;
        sessionManager.beginSession();

        try (Connection connection = sessionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SQLTask.GET_USER_BY_EMAIL.QUERY)) {
            pst.setString(1, email);

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
    public boolean update(User user) throws SQLException {
        return false;
    }

    @Override
    public boolean deleteEntityByID(int id) throws SQLException {
        return false;
    }

    @Override
    public List<User> findAllByPrimaryKey(Object pkey) throws SQLException {
        return null;
    }

    @Override
    public List<User> findAll() throws SQLException {
        return null;
    }

    /**
     * SQL queries for users table.
     */
    enum SQLTask {
        GET_USER_BY_USERNAME("SELECT * FROM users WHERE username = (?)"),
        GET_USER_BY_EMAIL("SELECT * FROM users WHERE email = (?)"),
        INSERT_USER("INSERT into users (username, password_hash, name, surname, email) VALUES ((?), (?),(?),(?),(?))");

        String QUERY;

        SQLTask(String QUERY) {
            this.QUERY = QUERY;
        }
    }
}
