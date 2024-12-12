package util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static final String CONFIG_FILE = "DatabaseConfig.properties";
    private static String URL;
    private static String USER;
    private static String PASSWORD;

    static {
        try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new RuntimeException("Impossibile trovare il file di configurazione: " + CONFIG_FILE);
            }

            Properties props = new Properties();
            props.load(input);
            URL = props.getProperty("db.ip-server-mysql");
            USER = props.getProperty("db.username");
            PASSWORD = props.getProperty("db.password");
        } catch (IOException e) {
            throw new RuntimeException("Errore durante il caricamento del file di configurazione: " + CONFIG_FILE, e);
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Errore nella connessione al database: " + e.getMessage());
            throw new RuntimeException("Impossibile connettersi al database. Verifica le impostazioni di configurazione.", e);
        }
    }
}