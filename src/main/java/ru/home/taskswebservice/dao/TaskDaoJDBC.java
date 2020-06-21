package ru.home.taskswebservice.dao;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.home.taskswebservice.dao.jdbc.sessionmanager.SessionManager;
import ru.home.taskswebservice.model.Task;
import ru.home.taskswebservice.util.TimeUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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

    @Override
    public List<Task> getAllById(Object id) throws SQLException {
        String username = (String) id;
        List<Task> userTasks = new ArrayList<>();

        sessionManager.beginSession();
        try (Connection connection = sessionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SQLTask.GET_TASKS_BY_USERNAME.QUERY)) {
            pst.setString(1, username);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Task task = new Task();
                    task.setId(Integer.parseInt(rs.getString("id")));
                    task.setTitle(rs.getString("title"));
                    task.setDescription(rs.getString("description"));
                    task.setDeadline_date(TimeUtils.convertToLocalDateViaSqlDate(rs.getDate("deadline_date")));
                    task.setDone(rs.getBoolean("done"));
                    userTasks.add(task);
                }
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            sessionManager.rollbackSession();
            throw ex;
        }

        return userTasks;

    }

    /**
     * SQL queries for users table.
     */
    enum SQLTask {
        INSERT("INSERT INTO task (title, description) VALUES ((?), (?))"),
        GET_TASKS_BY_USERNAME("SELECT tasks.id, tasks.title, tasks.description," +
                " tasks.deadline_date, tasks.done" +
                " from users" +
                " INNER JOIN tasks_users on users.id = tasks_users.user_id" +
                " INNER JOIN tasks on tasks.id = tasks_users.task_id" +
                " WHERE users.username = (?)");

        String QUERY;

        SQLTask(String QUERY) {
            this.QUERY = QUERY;
        }
    }
}
