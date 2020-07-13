package ru.home.taskswebservice.service.resthandlers;

import com.fasterxml.jackson.core.JsonProcessingException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Обработчик REST запросов (GET, POST, PUT, DELETE)
 *
 * @author Sergei Viacheslaev
 */
public interface RestApiHandler {
    Optional<String> handleRestRequest(String requestPath) throws SQLException, JsonProcessingException;

    long handleRestRequest(String requestPath, HttpServletRequest request) throws SQLException, IOException;

}
