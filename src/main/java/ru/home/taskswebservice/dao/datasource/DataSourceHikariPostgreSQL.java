package ru.home.taskswebservice.dao.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Connection pool
 *
 * @author Sergei Viacheslaev
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DataSourceHikariPostgreSQL {
    final static String USER = "postgres";
//    final static String USER = "kfgzgqbrhqpjvd";
    final static String PASSWORD = "postgres";
//    final static String PASSWORD = "216be4352e2eb18afb691e0806bfe5567321cd8d01f66bdc59775571c6fb4168";

    final static String URL = "jdbc:postgresql://localhost:5432/restserviceDB";
//    final static String URL = "jdbc:postgresql://ec2-54-246-115-40.eu-west-1.compute.amazonaws.com:5432/dfbd0helbmkatr?user=kfgzgqbrhqpjvd&password=216be4352e2eb18afb691e0806bfe5567321cd8d01f66bdc59775571c6fb4168";

    private static final HikariConfig config = new HikariConfig();
    private static final HikariDataSource dataSource;

    static {
        config.setJdbcUrl(URL);
        config.setUsername(USER);
        config.setPassword(PASSWORD);


        config.setDriverClassName(org.postgresql.Driver.class.getName());
        config.setConnectionTimeout(15000); //ms
        config.setIdleTimeout(60000); //ms
        config.setMaxLifetime(600000);//ms
        config.setAutoCommit(false);
        config.setMinimumIdle(5);
        config.setMaximumPoolSize(10);
        config.setPoolName("restServiceDbPool");
        config.setRegisterMbeans(true);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        dataSource = new HikariDataSource(config);
    }

    private DataSourceHikariPostgreSQL() {
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static DataSource getHikariDataSource() {
        return dataSource;
    }

}
