package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DBPropertyUtil {

    public static String getConnectionString(String propertyFileName) {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(propertyFileName)) {
            props.load(fis);
            String host = props.getProperty("host");
            String port = props.getProperty("port");
            String dbname = props.getProperty("dbname");
            String username = props.getProperty("username");
            String password = props.getProperty("password");

            // Construct JDBC MySQL URL
            return "jdbc:mysql://" + host + ":" + port + "/" + dbname +
                   "?user=" + username + "&password=" + password;
        } catch (IOException e) {
            System.out.println("Error reading properties file: " + e.getMessage());
        }
        return null;
    }
}
