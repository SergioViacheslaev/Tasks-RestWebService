package ru.home.taskswebservice.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface DAO<Entity, Id> {
    long insertRecord(Entity model) throws SQLException;

    Optional<Entity> findById(Id id) throws SQLException;

    boolean update(Entity model);

    boolean delete(Entity model);

    List<Entity> getAllById(Object id) throws SQLException;
}
