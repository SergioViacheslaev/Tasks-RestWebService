package ru.home.taskswebservice.dao;

import ru.home.taskswebservice.model.User;

import java.sql.SQLException;
import java.util.Optional;


public interface UserDao {
    int insertUser(User user) throws SQLException;

    Optional<User> findByUsername(String username) throws SQLException;

    Optional<User> findByEmail(String email) throws SQLException;

}
