package ru.home.taskswebservice.dao;

import ru.home.taskswebservice.model.Goal;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface GoalDao {
    long insertGoal(Goal goal) throws SQLException;

    Optional<Goal> findById(Long id) throws SQLException;

    List<Goal> findAll() throws SQLException;

    int deleteGoal(Long goal_id) throws SQLException;
}
