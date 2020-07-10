package ru.home.taskswebservice.service.resthandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import java.util.Optional;
import java.util.stream.Collectors;

/** POST handler
 *
 * @author Sergei Viacheslaev
 */
@Getter
@Setter
@AllArgsConstructor
public class RestApiPostHandlerService implements RestApiHandler {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final ObjectMapper objectMapper = new ObjectMapper();
    private TaskDaoJDBC taskDao;

    @Override
    public Optional<String> handleRestRequest(String requestPath) throws SQLException, JsonProcessingException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long handleRestRequest(String requestPath, HttpServletRequest req) throws SQLException, IOException {
        long generated_id = 0;
        if (requestPath.matches("^/tasks/$")) {

            String bodyParams = req.getReader().lines().collect(Collectors.joining());

            Task task = objectMapper.readValue(bodyParams, Task.class);

            generated_id = taskDao.insertTaskForUser(task, task.getExecutor().getUsername());

        }

        return generated_id;
    }
}
