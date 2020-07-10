package ru.home.taskswebservice.service.resthandlers;

import com.fasterxml.jackson.core.JsonProcessingException;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Обработчик REST запросов
 *
 * @author Sergei Viacheslaev
 */
public interface RestApiHandler {
    Optional<String> handleRestRequest(String requestPath) throws SQLException, JsonProcessingException;

    long handleRestRequest(String requestPath, HttpServletRequest request) throws SQLException, JsonProcessingException;

}
