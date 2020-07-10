package ru.home.taskswebservice.dao;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.home.taskswebservice.dao.jdbc.sessionmanager.SessionManager;
import ru.home.taskswebservice.model.Goal;
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
                    task = parseTaskFromResultSet(rs);
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
    public long insertTaskWithoutGoal(Task task, String executor_username) throws SQLException {
        sessionManager.beginSession();

        try (Connection connection = sessionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SQLTask.INSERT_TASK_WITHOUT_GOAL.QUERY, Statement.RETURN_GENERATED_KEYS)) {
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
    public long insertTaskWithGoal(Task task, String executor_username) throws SQLException {
        long generated_taskId;
        sessionManager.beginSession();

        try (Connection connection = sessionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SQLTask.INSERT_TASK_WITHOUT_GOAL.QUERY, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, task.getTitle());
            pst.setString(2, task.getDescription());
            pst.setDate(3, Date.valueOf(task.getDeadline_date()));
            pst.setBoolean(4, task.isDone());
            pst.setString(5, executor_username);

            pst.executeUpdate();
            try (ResultSet rs = pst.getGeneratedKeys()) {
                rs.next();
                generated_taskId = rs.getLong(1);
                sessionManager.commitSession();
            }

        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        }

        sessionManager.beginSession();
        try (Connection connection = sessionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SQLTask.INSERT_TASKS_GOALS.QUERY)) {
            pst.setString(1, task.getGoal().getName());
            pst.setString(2, task.getGoal().getDescription());
            pst.setLong(3, generated_taskId);

            pst.executeUpdate();
            sessionManager.commitSession();

        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        }

        return generated_taskId;
    }

    @Override
    public int update(Task task) throws SQLException {
        int rowsUpdated = 0;
        sessionManager.beginSession();

        try (Connection connection = sessionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SQLTask.UPDATE_TASK_BY_ID.QUERY)) {
            pst.setString(1, task.getTitle());
            pst.setString(2, task.getDescription());
            pst.setDate(3, Date.valueOf(task.getDeadline_date()));
            pst.setBoolean(4, task.isDone());
            pst.setInt(5, task.getId());

            rowsUpdated = pst.executeUpdate();
            sessionManager.commitSession();

        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            sessionManager.rollbackSession();
            throw ex;
        }
        return rowsUpdated;
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
    public int deleteTaskById(Long task_id) throws SQLException {
        int updated_rows;

        sessionManager.beginSession();
        try (Connection connection = sessionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SQLTask.DELETE_TASK_BY_ID.QUERY)) {
            pst.setLong(1, task_id);
            updated_rows = pst.executeUpdate();
            sessionManager.commitSession();

        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            sessionManager.rollbackSession();
            throw ex;
        }
        return updated_rows;
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
                    Task task = parseTaskFromResultSet(rs);
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
             PreparedStatement pst = connection.prepareStatement(SQLTask.GET_ALL_TASKS.QUERY)) {

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    userTasks.add(parseTaskFromResultSet(rs));
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
        GET_TASKS_BY_EMAIL("select tasks.id, tasks.title, tasks.description, tasks.deadline_date, tasks.done, users.username," +
                " users.name, users.surname, users.email," +
                " goals.id AS goal_id, goals.name AS goal_name, goals.description AS goal_description" +
                " from tasks INNER JOIN tasks_users ON tasks.id = tasks_users.task_id" +
                " INNER JOIN users ON users.id = tasks_users.user_id" +
                " LEFT OUTER JOIN tasks_goals ON tasks.id = tasks_goals.task_id" +
                " LEFT OUTER JOIN goals ON goals.id = tasks_goals.goal_id" +
                " WHERE users.email = (?)"),
        GET_TASK_BY_ID("select tasks.id, tasks.title, tasks.description, tasks.deadline_date, tasks.done, users.username," +
                " users.name, users.surname, users.email," +
                " goals.id AS goal_id, goals.name AS goal_name, goals.description AS goal_description" +
                " from tasks INNER JOIN tasks_users ON tasks.id = tasks_users.task_id" +
                " INNER JOIN users ON users.id = tasks_users.user_id" +
                " LEFT OUTER JOIN tasks_goals ON tasks.id = tasks_goals.task_id" +
                " LEFT OUTER JOIN goals ON goals.id = tasks_goals.goal_id" +
                " WHERE tasks.id = (?)"),
        GET_ALL_TASKS("select tasks.id, tasks.title, tasks.description, tasks.deadline_date, tasks.done, users.username," +
                " users.name, users.surname, users.email," +
                " goals.id AS goal_id, goals.name AS goal_name, goals.description AS goal_description" +
                " from tasks INNER JOIN tasks_users ON tasks.id = tasks_users.task_id" +
                " INNER JOIN users ON users.id = tasks_users.user_id" +
                " LEFT OUTER JOIN tasks_goals ON tasks.id = tasks_goals.task_id" +
                " LEFT OUTER JOIN goals ON goals.id = tasks_goals.goal_id"),
        INSERT_TASK_WITHOUT_GOAL("with task_insert as (INSERT INTO tasks (title, description, deadline_date, done) " +
                "VALUES ((?), (?),(?), (?)) RETURNING id) " +
                "INSERT into tasks_users(task_id,user_id) " +
                "VALUES " +
                "( (SELECT id from task_insert), (SELECT users.id from users where username=(?)))"),
        DELETE_TASK_BY_ID("DELETE from tasks where id=(?)"),
        UPDATE_TASK_BY_ID("UPDATE tasks set title=(?), description=(?), deadline_date=(?), done=(?) where id=(?)"),
        UPDATE_TASK_EXECUTOR("UPDATE tasks_users" +
                " set user_id=(select users.id from users where users.username =(?) )" +
                " where task_id=(?)"),
        INSERT_TASKS_GOALS("with goals_insert as (INSERT INTO goals (name, description)" +
                " VALUES ((?), (?)) RETURNING id)" +
                " INSERT into tasks_goals(task_id,goal_id)" +
                " VALUES" +
                " ((?),(SELECT id from goals_insert))");

        String QUERY;

        SQLTask(String QUERY) {
            this.QUERY = QUERY;
        }

    }

    private Task parseTaskFromResultSet(ResultSet rs) throws SQLException {
        Task task = new Task();
        task.setId(Integer.parseInt(rs.getString("id")));
        task.setTitle(rs.getString("title"));
        task.setDescription(rs.getString("description"));
        task.setDeadline_date(TimeUtils.convertToLocalDateViaSqlDate(rs.getDate("deadline_date")));
        task.setDone(rs.getBoolean("done"));
        User user = new User();
        user.setUsername(rs.getString("username"));
        user.setName(rs.getString("name"));
        user.setSurname(rs.getString("surname"));
        user.setEmail(rs.getString("email"));
        task.setExecutor(user);

        String goalId = rs.getString("goal_id");
        if (goalId != null) {
            Goal goal = new Goal();
            goal.setId(Integer.parseInt(goalId));
            goal.setName(rs.getString("goal_name"));
            goal.setDescription(rs.getString("goal_description"));
            task.setGoal(goal);
        }
        return task;
    }
}
