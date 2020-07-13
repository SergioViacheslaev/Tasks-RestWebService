package ru.home.taskswebservice.dao;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.home.taskswebservice.dao.jdbc.sessionmanager.SessionManager;
import ru.home.taskswebservice.model.Goal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Getter
public class GoalDaoJDBC implements GoalDao {
    private final SessionManager sessionManager;

    public GoalDaoJDBC(final SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public long insertGoal(final Goal goal) throws SQLException {
        sessionManager.beginSession();

        try (Connection connection = sessionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SQLTask.INSERT_GOAL.QUERY, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, goal.getName());
            pst.setString(2, goal.getDescription());

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
    public Optional<Goal> findById(Long id) throws SQLException {
        Goal goal = null;
        sessionManager.beginSession();
        try (Connection connection = sessionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SQLTask.GET_GOAL_BY_ID.QUERY)) {
            pst.setLong(1, id);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    goal = parseGoalFromResultSet(rs);
                }
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            sessionManager.rollbackSession();
            throw ex;
        }

        return Optional.ofNullable(goal);

    }


    @Override
    public int deleteGoal(Long goal_id) throws SQLException {
        int updated_rows;

        sessionManager.beginSession();
        try (Connection connection = sessionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SQLTask.DELETE_GOAL_BY_ID.QUERY)) {
            pst.setLong(1, goal_id);
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
    public List<Goal> findAll() throws SQLException {
        List<Goal> goals = new ArrayList<>();

        sessionManager.beginSession();
        try (Connection connection = sessionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SQLTask.GET_ALL_GOALS.QUERY)) {

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    goals.add(parseGoalFromResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            sessionManager.rollbackSession();
            throw ex;
        }

        return goals;
    }

    /**
     * SQL queries for goals table.
     */
    enum SQLTask {
        INSERT_GOAL("INSERT INTO goals (name, description) VALUES ((?), (?))"),
        GET_GOAL_BY_ID("SELECT  goals.id, goals.name , goals.description from goals where id = (?)"),
        DELETE_GOAL_BY_ID("DELETE from goals where id=(?)"),
        UPDATE_GOAL_BY_ID("UPDATE goals set name=(?), description=(?) where id=(?)"),
        GET_ALL_GOALS("SELECT goals.id, goals.name , goals.description from goals");

        String QUERY;

        SQLTask(String QUERY) {
            this.QUERY = QUERY;
        }

    }

    private Goal parseGoalFromResultSet(ResultSet rs) throws SQLException {
        Goal goal = new Goal();

        goal.setId(rs.getInt("id"));
        goal.setName(rs.getString("name"));
        goal.setDescription(rs.getString("description"));

        return goal;
    }


}
