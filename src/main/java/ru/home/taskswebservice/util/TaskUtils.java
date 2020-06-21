package ru.home.taskswebservice.util;


import ru.home.taskswebservice.model.Task;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


public class TaskUtils {


    public static boolean idIsNumber(HttpServletRequest request) {
        final String id = request.getParameter("id");
        return id != null &&
                (id.length() > 0) &&
                id.matches("^\\d*$");
    }

    public static boolean requestIsValid(HttpServletRequest request) {
        final String title = request.getParameter("title");
        final String description = request.getParameter("description");

        return title != null && title.length() > 0 &&
                description != null && description.length() > 0;
    }


    public static boolean idIsInvalid(final String id, Map<Integer, Task> repo) {
        return !(id != null &&
                id.matches("^\\d*$") &&
                repo.get(Integer.parseInt(id)) != null);
    }

}
