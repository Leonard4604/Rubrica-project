package org.rubrica;

import view.LoginView;
import persistence.DatabaseManager;

public class Main {
    public static void main(String[] args) {
        try {
            DatabaseManager.executeScript("schema_database.sql");

            new LoginView();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Application failed to start due to a database error.");
        }
    }
}