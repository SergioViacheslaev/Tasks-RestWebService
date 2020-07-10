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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
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
    public long handleRestRequest(String requestPath, HttpServletRequest req) throws SQLException, JsonProcessingException {
        long generated_id = 0;
        if (requestPath.matches("^/tasks/$")) {

            final String title = req.getParameter("title");
            final String description = req.getParameter("description");
            final LocalDate deadline_date = LocalDate.parse(req.getParameter("deadline_date"), formatter);
            final boolean done = Boolean.parseBoolean(req.getParameter("done"));
            String executorUsername = req.getParameter("executor_username");

            final Task task = new Task();
            task.setTitle(title);
            task.setDescription(description);
            task.setDeadline_date(deadline_date);
            task.setDone(done);


            generated_id = taskDao.insertTaskForUser(task, executorUsername);

        }

        return generated_id;
    }
}
