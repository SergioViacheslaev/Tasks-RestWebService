package ru.home.taskswebservice.service;

/**
 * @author Sergei Viacheslaev
 */
public interface Authentication {
    boolean isAuthenticated(String username, String inputPassword);
}
