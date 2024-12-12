package persistence;

import model.Persona;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PersonaPersistence {
    public static void saveToFile(String fileName, List<Persona> personas) {
        try (PrintStream ps = new PrintStream(new FileOutputStream(fileName))) {
            for (Persona persona : personas) {
                ps.printf("%s;%s;%s;%s;%d%n",
                        persona.getNome(),
                        persona.getCognome(),
                        persona.getIndirizzo(),
                        persona.getTelefono(),
                        persona.getEta());
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    public static List<Persona> loadFromFile(String fileName) {
        List<Persona> personas = new ArrayList<>();
        File file = new File(fileName);
        System.out.println("Looking for file at: " + file.getAbsolutePath());

        if (!file.exists()) {
            System.out.println("File does not exist.");
            return personas;
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                System.out.println("Reading line: " + line);
                String[] parts = line.split(";");
                if (parts.length == 5) {
                    String firstName = parts[0];
                    String lastName = parts[1];
                    String address = parts[2];
                    String phoneNumber = parts[3];
                    int age = Integer.parseInt(parts[4]);

                    personas.add(new Persona(firstName, lastName, address, phoneNumber, age));
                } else {
                    System.out.println("Skipping invalid line: " + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
        System.out.println("Loaded personas: " + personas.size());
        return personas;
    }
}