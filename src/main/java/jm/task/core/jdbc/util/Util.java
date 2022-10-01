package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class Util {
    public static final String CREATE_TABLE_SQL = """
            CREATE TABLE IF NOT EXISTS user(
            id SERIAL NOT NULL AUTO_INCREMENT PRIMARY KEY ,
            name VARCHAR(50) NOT NULL ,
            lastname VARCHAR(50) NOT NULL ,
            age TINYINT NOT NULL
            );
            """;
    public static final String DELETE_TABLE_SQL = """
            DROP TABLE IF EXISTS user;
            """;
    public static final String SAVE_SQL = """
            INSERT INTO user(name, lastname, age) VALUES (?, ?, ?);
            """;
    public static final String DELETE_SQL = """
            DELETE FROM user
            WHERE id = ?
            """;
    public static final String GET_ALL_SQL = """
            SELECT * FROM user
            """;
    public static final String CLEAN_TABLE_SQL = """
            TRUNCATE TABLE user
            """;
    // реализуйте настройку соеденения с БД
    private static final String DRIVER_KEY = "db.driver";
    private static final String URL_KEY = "db.url";
    private static final String USERNAME_KEY = "db.username";
    private static final String PASSWORD_KEY = "db.password";
    private static final String DIALECT_KEY = "hibernate.dialect";
    private static final String HBM2DDL_KEY = "hibernate.hbm2ddl.auto";
    private static final String SHOW_SQL_KEY = "hibernate.show_sql";

    static {
        loadDriver();
    }

    private Util() {
    }

    private static void loadDriver() {
        try {
            Class.forName(PropertiesUtil.get(DRIVER_KEY));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    PropertiesUtil.get(URL_KEY),
                    PropertiesUtil.get(USERNAME_KEY),
                    PropertiesUtil.get(PASSWORD_KEY));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        Configuration configuration = new Configuration();

        Properties settings = new Properties();
        settings.put(Environment.DRIVER, PropertiesUtil.get(DRIVER_KEY));
        settings.put(Environment.URL, PropertiesUtil.get(URL_KEY));
        settings.put(Environment.USER, PropertiesUtil.get(USERNAME_KEY));
        settings.put(Environment.PASS, PropertiesUtil.get(PASSWORD_KEY));
        settings.put(Environment.DIALECT, PropertiesUtil.get(DIALECT_KEY));
        settings.put(Environment.SHOW_SQL, PropertiesUtil.get(SHOW_SQL_KEY));
        settings.put(Environment.HBM2DDL_AUTO, PropertiesUtil.get(HBM2DDL_KEY));

        configuration.setProperties(settings);

        configuration.addAnnotatedClass(User.class);

        return configuration.buildSessionFactory();
    }
}
