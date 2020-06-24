package ru.home.taskswebservice.service;

import ru.home.taskswebservice.service.exceptions.EmailAlreadyRegisteredException;
import ru.home.taskswebservice.service.exceptions.UserAlreadyRegisteredException;

import java.sql.SQLException;

/**
 * @author Sergei Viacheslaev
 */
public interface UserRegistration {
    int registerUser(String username, String password, String name, String surname, String email)
            throws SQLException, UserAlreadyRegisteredException, EmailAlreadyRegisteredException;
}
