package ru.home.taskswebservice.dao;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.home.taskswebservice.dao.jdbc.sessionmanager.SessionManager;
import ru.home.taskswebservice.model.Task;
import ru.home.taskswebservice.model.User;
import ru.home.taskswebservice.util.TimeUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Getter
public class TaskDaoJDBC implements TaskDao {
    private final SessionManager sessionManager;

    public TaskDaoJDBC(final SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }


    @Override
    public long insertTask(final Task task) throws SQLException {
        sessionManager.beginSession();

        try (Connection connection = sessionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SQLTask.INSERT_TASK.QUERY, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, task.getTitle());
            pst.setString(2, task.getDescription());
            pst.setDate(3, Date.valueOf(task.getDeadline_date()));
            pst.setBoolean(4, task.isDone());

            pst.executeUpdate();
            try (ResultSet rs = pst.getGeneratedKeys()) {
                rs.next();
                long id = rs.getLong(1);
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
    public Optional<Task> findById(Long id) throws SQLException {
        Task task = null;
        sessionManager.beginSession();
        try (Connection connection = sessionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SQLTask.GET_TASK_BY_ID.QUERY)) {
            pst.setLong(1, id);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    task = new Task();
                    task.setId(Integer.parseInt(rs.getString("id")));
                    task.setTitle(rs.getString("title"));
                    task.setDescription(rs.getString("description"));
                    task.setDeadline_date(TimeUtils.convertToLocalDateViaSqlDate(rs.getDate("deadline_date")));
                    task.setDone(rs.getBoolean("done"));
                    User user = new User();
                    user.setName(rs.getString("name"));
                    user.setSurname(rs.getString("surname"));
                    task.setExecutor(user);
                }
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            sessionManager.rollbackSession();
            throw ex;
        }

        return Optional.ofNullable(task);

    }

    @Override
    public long insertTaskForUser(Task task, String executor_username) throws SQLException {
        sessionManager.beginSession();

        try (Connection connection = sessionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SQLTask.INSERT_TASK_FOR_USER.QUERY, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, task.getTitle());
            pst.setString(2, task.getDescription());
            pst.setDate(3, Date.valueOf(task.getDeadline_date()));
            pst.setBoolean(4, task.isDone());
            pst.setString(5, executor_username);

            pst.executeUpdate();
            try (ResultSet rs = pst.getGeneratedKeys()) {
                rs.next();
                long id = rs.getLong(1);
                sessionManager.commitSession();

                return id;
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        }

    }

    @Override
    public boolean update(Task task) throws SQLException {
        boolean result;
        sessionManager.beginSession();

        try (Connection connection = sessionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SQLTask.UPDATE_TASK_BY_ID.QUERY)) {
            pst.setString(1, task.getTitle());
            pst.setString(2, task.getDescription());
            pst.setDate(3, Date.valueOf(task.getDeadline_date()));
            pst.setBoolean(4, task.isDone());
            pst.setInt(5, task.getId());


            int rows = pst.executeUpdate();
            result = rows == 1;

            sessionManager.commitSession();


        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            sessionManager.rollbackSession();
            throw ex;
        }

        return result;
    }

    @Override
    public int updateTaskExecutor(Task task, String executor_username) throws SQLException {
        int rowsUpdated;


        sessionManager.beginSession();
        try (Connection connection = sessionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SQLTask.UPDATE_TASK_EXECUTOR.QUERY)) {
            pst.setString(1, executor_username);
            pst.setLong(2, task.getId());

            rowsUpdated = pst.executeUpdate();

            sessionManager.commitSession();


        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        }

        return rowsUpdated;
    }


    @Override
    public boolean deleteTaskById(Long task_id) throws SQLException {
        boolean result;

        sessionManager.beginSession();
        try (Connection connection = sessionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SQLTask.DELETE_TASK_BY_ID.QUERY)) {
            pst.setLong(1, task_id);

            int rows = pst.executeUpdate();
            result = rows == 1;

            sessionManager.commitSession();


        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            sessionManager.rollbackSession();
            throw ex;
        }

        return result;
    }


    @Override
    public List<Task> findAllByPrimaryKey(Object pkey) throws SQLException {
        String email = (String) pkey;
        List<Task> userTasks = new ArrayList<>();

        sessionManager.beginSession();
        try (Connection connection = sessionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SQLTask.GET_TASKS_BY_EMAIL.QUERY)) {
            pst.setString(1, email);

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

    @Override
    public List<Task> findAll() throws SQLException {
        List<Task> userTasks = new ArrayList<>();

        sessionManager.beginSession();
        try (Connection connection = sessionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SQLTask.GET_ALL_TASKS_WITH_EXECUTORS.QUERY)) {

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Task task = new Task();
                    task.setId(Integer.parseInt(rs.getString("id")));
                    task.setTitle(rs.getString("title"));
                    task.setDescription(rs.getString("description"));
                    task.setDeadline_date(TimeUtils.convertToLocalDateViaSqlDate(rs.getDate("deadline_date")));
                    task.setDone(rs.getBoolean("done"));
                    User user = new User();
                    user.setName(rs.getString("name"));
                    user.setSurname(rs.getString("surname"));
                    task.setExecutor(user);
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
        INSERT_TASK("INSERT INTO tasks (title, description, deadline_date, done) VALUES ((?), (?),(?), (?))"),
        GET_TASKS_BY_EMAIL("SELECT tasks.id, tasks.title, tasks.description," +
                " tasks.deadline_date, tasks.done" +
                " from users" +
                " INNER JOIN tasks_users on users.id = tasks_users.user_id" +
                " INNER JOIN tasks on tasks.id = tasks_users.task_id" +
                " WHERE users.email = (?)"),
        GET_TASK_BY_ID("select tasks.id, tasks.title, tasks.description, tasks.deadline_date, tasks.done, users.name, users.surname" +
                " from tasks INNER JOIN tasks_users ON tasks.id = tasks_users.task_id" +
                " INNER JOIN users ON users.id = tasks_users.user_id" +
                " WHERE tasks.id = (?)"),
        GET_ALL_TASKS_WITH_EXECUTORS("select tasks.id, tasks.title, tasks.description,tasks.deadline_date, tasks.done, users.name, users.surname" +
                " from tasks INNER JOIN tasks_users ON tasks.id = tasks_users.task_id" +
                " INNER JOIN users ON users.id = tasks_users.user_id"),
        INSERT_TASK_FOR_USER("with task_insert as (INSERT INTO tasks (title, description, deadline_date, done) " +
                "VALUES ((?), (?),(?), (?)) RETURNING id) " +
                "INSERT into tasks_users(task_id,user_id) " +
                "VALUES " +
                "( (SELECT id from task_insert), (SELECT users.id from users where username=(?)))"),
        DELETE_TASK_BY_ID("DELETE from tasks where id=(?)"),
        UPDATE_TASK_BY_ID("UPDATE tasks set title=(?), description=(?), deadline_date=(?), done=(?) where id=(?)"),
        UPDATE_TASK_EXECUTOR("UPDATE tasks_users" +
                " set user_id=(select users.id from users where users.username =(?) )" +
                " where task_id=(?)");

        String QUERY;

        SQLTask(String QUERY) {
            this.QUERY = QUERY;
        }
    }
}
