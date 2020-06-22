package ru.home.taskswebservice.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface DAO<Entity, Id> {
    long insertRecord(Entity model) throws SQLException;

    long insertRecordMultipleTables(Entity model, Object arg) throws SQLException;

    Optional<Entity> findById(Id id) throws SQLException;

    boolean update(Entity model) throws SQLException;

    boolean delete(Entity model);

    boolean deleteEntityByID(Id id) throws SQLException;

    List<Entity> findAllByPrimaryKey(Object pkey) throws SQLException;

    List<Entity> findAll() throws SQLException;
}
