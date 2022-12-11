package config;

import java.util.Properties;

public class ApplicationContext {
    private static final Properties PROPERTIES = Config.getProperties();

//    public static final String REPO_TYPE = PROPERTIES.getProperty("REPO_TYPE");
//    public static final String DATABASE_URL = PROPERTIES.getProperty("DATABASE_URL");
//    public static final String DB_USERNAME = PROPERTIES.getProperty("DB_USERNAME");
//    public static final String DB_PASSWORD = PROPERTIES.getProperty("DB_PASSWORD");
    public static final String REPO_TYPE = "database";
    public static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/ToySocialApp";
    public static final String DB_USERNAME = "postgres";
    public static final String DB_PASSWORD = "postgres";
}
