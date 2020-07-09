package ru.home.taskswebservice.service.resthandlers;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.sql.SQLException;
import java.util.Optional;

/**
 * Обработчик REST запросов
 *
 * @author Sergei Viacheslaev
 */
public interface RestApiHandler {
    Optional<String> handleRestRequest(String requestPath) throws SQLException,JsonProcessingException;
}
