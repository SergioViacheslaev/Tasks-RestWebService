package ru.home.taskswebservice.service;

/**
 * @author Sergei Viacheslaev
 */
public interface Authentication {
    boolean isUsernamePresent(String username);

    boolean isAuthenticated(String username, String inputPassword);
}
