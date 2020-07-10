package ru.home.taskswebservice.service.resthandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.home.taskswebservice.dao.TaskDaoJDBC;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * DELETE handler
 *
 * @author Sergei Viacheslaev
 */
@Getter
@Setter
@AllArgsConstructor
public class RestApiDeleteHandlerService implements RestApiHandler {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final ObjectMapper objectMapper = new ObjectMapper();
    private TaskDaoJDBC taskDao;

    @Override
    public Optional<String> handleRestRequest(String requestPath) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long handleRestRequest(String requestPath, HttpServletRequest req) throws SQLException {
        long updated_rows = 0;
        if (requestPath.matches("^/tasks/\\d+$")) {
            String[] parts = requestPath.split("/");
            String taskIdParam = parts[2];

            long taskId = Long.parseLong(taskIdParam);
            updated_rows = taskDao.deleteTaskById(taskId);


        }
        return updated_rows;
    }
}
