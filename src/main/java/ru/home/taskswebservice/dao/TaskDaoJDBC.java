package ru.home.taskswebservice.dao;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.home.taskswebservice.dao.jdbc.sessionmanager.SessionManager;
import ru.home.taskswebservice.model.Task;

import java.sql.*;
import java.util.Optional;

@Slf4j
@Getter
public class TaskDaoJDBC implements DAO<Task, Long> {
    /**
     * Connection of database.
     */
    private final SessionManager sessionManager;

    /**
     * Init database connection.
     *
     * @param sessionManager of database.
     */
    public TaskDaoJDBC(final SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * Create User in database.
     *
     * @param task for create.
     * @return false if User already exist. If creating success true.
     */
    @Override
    public long insertRecord(final Task task) throws SQLException {
        Connection connection = sessionManager.getCurrentSession();

        try (PreparedStatement pst = connection.prepareStatement(SQLTask.INSERT.QUERY, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, task.getTitle());
            pst.setString(2, task.getDescription());
            pst.executeUpdate();
            try (ResultSet rs = pst.getGeneratedKeys()) {
                rs.next();
                return rs.getLong(1);
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        }


    }

    @Override
    public Optional<Task> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean update(Task model) {
        return false;
    }

    @Override
    public boolean delete(Task model) {
        return false;
    }

    /**
     * SQL queries for users table.
     */
    enum SQLTask {
        INSERT("INSERT INTO task (title, description) VALUES ((?), (?))");

        String QUERY;

        SQLTask(String QUERY) {
            this.QUERY = QUERY;
        }
    }
}
