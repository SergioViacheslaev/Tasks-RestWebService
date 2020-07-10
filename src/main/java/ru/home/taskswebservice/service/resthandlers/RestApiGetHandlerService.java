package ru.home.taskswebservice.service.resthandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.home.taskswebservice.dao.TaskDaoJDBC;
import ru.home.taskswebservice.model.Task;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * GET handler
 *
 * @author Sergei Viacheslaev
 */
@Getter
@Setter
@AllArgsConstructor
public class RestApiGetHandlerService implements RestApiHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private TaskDaoJDBC taskDao;

    @Override
    public Optional<String> handleRestRequest(String requestPath) throws SQLException, JsonProcessingException {
        if (requestPath.matches("^/tasks/\\d+$")) {
            String[] parts = requestPath.split("/");
            String taskIdParam = parts[2];

            long taskId = Long.parseLong(taskIdParam);
            Task task = taskDao.findById(taskId).orElseThrow(SQLException::new);
            final String jsonTask = objectMapper.writeValueAsString(task);

            return Optional.ofNullable(jsonTask);


        } else if (requestPath.matches("^/tasks/$")) {
            final List<Task> allTasks = taskDao.findAll();
            return Optional.ofNullable(objectMapper.writeValueAsString(allTasks));
        }

        return Optional.empty();
    }

    @Override
    public long handleRestRequest(String requestPath, HttpServletRequest request) {
        throw new UnsupportedOperationException();
    }
}
