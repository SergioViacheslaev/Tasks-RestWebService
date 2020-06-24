package ru.home.taskswebservice.dao.jdbc;

import ru.home.taskswebservice.model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * @author Sergei Viacheslaev
 */
public interface UserDao {
    int insertUser(User user) throws SQLException;

    Optional<User> findById(int id) throws SQLException;

    Optional<User> findByUsername(String username) throws SQLException;

    Optional<User> findByEmail(String email) throws SQLException;

    boolean update(User user) throws SQLException;

    boolean deleteEntityByID(int id) throws SQLException;

    List<User> findAllByPrimaryKey(Object pkey) throws SQLException;

    List<User> findAll() throws SQLException;

}
