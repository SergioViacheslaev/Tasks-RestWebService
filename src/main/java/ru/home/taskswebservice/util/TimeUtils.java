package ru.home.taskswebservice.util;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * @author Sergei Viacheslaev
 */
public class TimeUtils {
    public static LocalDate convertToLocalDateViaSqlDate(Date sqlDate) {
        return new java.sql.Date(sqlDate.getTime()).toLocalDate();
    }

    public static LocalDate convertToLocalDateViaDate(java.util.Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

}
