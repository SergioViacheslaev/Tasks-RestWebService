package ru.home.taskswebservice.service.exceptions;

/**
 * @author Sergei Viacheslaev
 */
public class EmailAlreadyRegisteredException extends RuntimeException {
    public EmailAlreadyRegisteredException(String message) {
        super(message);
    }

    public EmailAlreadyRegisteredException(Exception ex) {
        super(ex);
    }
}
