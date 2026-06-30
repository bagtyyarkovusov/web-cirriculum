package com.bookstore.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Minimal JDBC connection helper. Reads {@code db.properties} from the classpath.
 * Uses {@link DriverManager} (no pool) per the course's plain-JDBC requirement.
 */
public final class Db {

    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;

    static {
        Properties p = new Properties();
        try (InputStream in = Db.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in == null) {
                throw new IllegalStateException("db.properties not found on classpath");
            }
            p.load(in);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
        URL = p.getProperty("db.url");
        USER = p.getProperty("db.user");
        PASSWORD = p.getProperty("db.password", "");
        // Ensure the driver registers in this class loader's context (embedded/webapp loader).
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ignored) {
            // Driver is auto-registered via SPI when present; this is only a safety net.
        }
    }

    private Db() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
