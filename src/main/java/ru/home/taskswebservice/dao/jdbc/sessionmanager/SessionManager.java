package ru.home.taskswebservice.dao.jdbc.sessionmanager;

import java.sql.Connection;

public interface SessionManager extends AutoCloseable {
    void beginSession();

    void commitSession();

    void rollbackSession();

    void close();

    Connection getCurrentSession();

}
