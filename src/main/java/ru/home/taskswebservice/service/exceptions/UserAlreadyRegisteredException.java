package ru.home.taskswebservice.service.exceptions;

/**
 * @author Sergei Viacheslaev
 */
public class UserAlreadyRegisteredException extends RuntimeException {
    public UserAlreadyRegisteredException(String message) {
        super(message);
    }

    public UserAlreadyRegisteredException(Exception ex) {
        super(ex);
    }
}
