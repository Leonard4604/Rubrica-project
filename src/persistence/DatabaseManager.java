package persistence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DatabaseManager {
    public static Connection getConnection() {
        try {
            Properties properties = new Properties();

            String jarDir = System.getProperty("user.dir");
            File propertiesFile = new File(jarDir, "DatabaseConfig.properties");

            InputStream inputStream;

            if (propertiesFile.exists()) {
                inputStream = new FileInputStream(propertiesFile);
            } else {
                inputStream = DatabaseManager.class.getClassLoader().getResourceAsStream("DatabaseConfig.properties");
                if (inputStream == null) {
                    throw new IOException("Property file 'DatabaseConfig.properties' not found in the same directory as the JAR or in the resources folder");
                }
            }

            try {
                properties.load(inputStream);
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }

            String url = properties.getProperty("db.ip-server-mysql");
            String username = properties.getProperty("db.username");
            String password = properties.getProperty("db.password");

            return DriverManager.getConnection(url, username, password);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database connection error", e);
        }
    }

    public static void executeScript(String resourceFileName) {
        try {
            String jarDir = System.getProperty("user.dir");
            File scriptFile = new File(jarDir, resourceFileName);

            InputStream inputStream;

            if (scriptFile.exists()) {
                inputStream = new FileInputStream(scriptFile);
            } else {
                inputStream = DatabaseManager.class.getClassLoader().getResourceAsStream(resourceFileName);
                if (inputStream == null) {
                    throw new IOException("SQL file '" + resourceFileName + "' not found in the same directory as the JAR or in the resources folder");
                }
            }

            StringBuilder script = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    script.append(line).append("\n");
                }
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }

            String[] commands = script.toString().split(";");

            try (Connection connection = getConnection();
                 Statement statement = connection.createStatement()) {
                for (String command : commands) {
                    command = command.trim();
                    if (!command.isEmpty()) {
                        statement.execute(command);
                    }
                }
                System.out.println("Database and tables created successfully.");
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error executing SQL script", e);
        }
    }
}