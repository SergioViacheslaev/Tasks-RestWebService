package ru.home.taskswebservice.service.resthandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.home.taskswebservice.dao.TaskDaoJDBC;
import ru.home.taskswebservice.model.Task;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * PUT handler
 *
 * @author Sergei Viacheslaev
 */
@Getter
@Setter
@AllArgsConstructor
public class RestApiPutHandlerService implements RestApiHandler {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final ObjectMapper objectMapper = new ObjectMapper();
    private TaskDaoJDBC taskDao;

    @Override
    public Optional<String> handleRestRequest(String requestPath) throws SQLException, JsonProcessingException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long handleRestRequest(String requestPath, HttpServletRequest req) throws SQLException, IOException {
        long updatedRows = 0;
        if (requestPath.matches("^/tasks/\\d+$")) {
            String[] parts = requestPath.split("/");
            String taskIdParam = parts[2];
            int task_id = Integer.parseInt(taskIdParam);
            String bodyParams = req.getReader().lines().collect(Collectors.joining());

            //update only Task executor
            if (bodyParams.contains("executor_username")) {
                Map<String, String> map = objectMapper.readValue(bodyParams, new TypeReference<Map<String, String>>() {
                });
                Task task = new Task();
                task.setId(task_id);
                return taskDao.updateTaskExecutor(task, map.get("executor_username"));
            }

            Task task = objectMapper.readValue(bodyParams, Task.class);
            task.setId(task_id);

            //update Task without Executor
            updatedRows = taskDao.update(task);
        }

        return updatedRows;
    }
}
