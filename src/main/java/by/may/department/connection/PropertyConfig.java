package by.may.department.connection;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyConfig {

    private static final Properties properties = new Properties();

    static {
        try(InputStream is = PropertyConfig.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (is == null) {
                throw new RuntimeException("application.properties not found");
            }

            properties.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Error loading database configuration", e);
        }
    }

    public static String getUrl() {
        return properties.getProperty("db.url");
    }

    public static String getUsername() {
        return properties.getProperty("db.username");
    }

    public static String getPassword() {
        return properties.getProperty("db.password");
    }

    public static String getDriver() {
        return properties.getProperty("db.driver");
    }
}