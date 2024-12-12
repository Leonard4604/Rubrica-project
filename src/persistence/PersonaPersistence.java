package persistence;

import model.Persona;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PersonaPersistence {
    public static void saveToFile(String directory, Persona persona) {
        String fileName = generateFileName(directory, persona);
        try (PrintStream ps = new PrintStream(new FileOutputStream(fileName))) {
            ps.printf("%s;%s;%s;%s;%d%n",
                    persona.getNome(),
                    persona.getCognome(),
                    persona.getIndirizzo(),
                    persona.getTelefono(),
                    persona.getEta());
        } catch (FileNotFoundException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    public static List<Persona> loadFromDirectory(String directory) {
        List<Persona> personas = new ArrayList<>();
        File dir = new File(directory);
        File[] files = dir.listFiles((d, name) -> name.endsWith(".txt"));

        if (files != null) {
            for (File file : files) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line = reader.readLine();
                    if (line != null) {
                        String[] parts = line.split(";");
                        if (parts.length == 5) {
                            String nome = parts[0];
                            String cognome = parts[1];
                            String indirizzo = parts[2];
                            String telefono = parts[3];
                            int eta = Integer.parseInt(parts[4]);
                            personas.add(new Persona(nome, cognome, indirizzo, telefono, eta));
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Error reading file: " + file.getName() + " - " + e.getMessage());
                }
            }
        }
        return personas;
    }

    public static void deleteFile(String directory, Persona persona) {
        File dir = new File(directory);
        File[] files = dir.listFiles((d, name) -> name.startsWith(generateFileNamePrefix(persona)));

        if (files != null) {
            for (File file : files) {
                if (!file.delete()) {
                    System.err.println("Error deleting file: " + file.getName());
                }
            }
        }
    }

    private static String generateFileName(String directory, Persona persona) {
        String sanitizedNome = persona.getNome().replaceAll("\\s+", "_");
        String sanitizedCognome = persona.getCognome().replaceAll("\\s+", "_");
        String uniqueId = UUID.randomUUID().toString();
        return directory + File.separator + sanitizedNome + "-" + sanitizedCognome + "-" + uniqueId + ".txt";
    }

    private static String generateFileNamePrefix(Persona persona) {
        String sanitizedNome = persona.getNome().replaceAll("\\s+", "_");
        String sanitizedCognome = persona.getCognome().replaceAll("\\s+", "_");
        return sanitizedNome + "-" + sanitizedCognome + "-";
    }
}