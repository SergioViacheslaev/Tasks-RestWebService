package ru.home.taskswebservice.dao;

import ru.home.taskswebservice.model.Task;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface TaskDao {
    long insertTask(Task task) throws SQLException;

    Optional<Task> findById(Long id) throws SQLException;

    int updateTaskExecutor(Task model, String executor_username) throws SQLException;

    int update(Task task) throws SQLException;

    List<Task> findAllByPrimaryKey(Object pkey) throws SQLException;

    List<Task> findAll() throws SQLException;

    long insertTaskWithoutGoal(Task task, String executor_username) throws SQLException;

    long insertTaskWithGoal(Task task, String executor_username) throws SQLException;

    int deleteTaskById(Long task_id) throws SQLException;
}
