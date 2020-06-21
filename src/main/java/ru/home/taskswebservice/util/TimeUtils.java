package ru.home.taskswebservice.util;

import java.sql.Date;
import java.time.LocalDate;

/**
 * @author Sergei Viacheslaev
 */
public class TimeUtils {
    public static LocalDate convertToLocalDateViaSqlDate(Date sqlDate) {
        return new java.sql.Date(sqlDate.getTime()).toLocalDate();
    }
}
