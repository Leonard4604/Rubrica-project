package controller;

import persistence.PersonaPersistence;
import model.Persona;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PersonaController {
    private static final String DIRECTORY_NAME = "informazioni";

    private List<Persona> personas;

    public PersonaController() {
        File dir = new File(DIRECTORY_NAME);
        if (!dir.exists()) {
            dir.mkdir();
        }
        this.personas = PersonaPersistence.loadFromDirectory(DIRECTORY_NAME);
    }

    public void addPersona(Persona persona) {
        personas.add(persona);
        System.out.println("Persona added: " + persona);
        PersonaPersistence.saveToFile(DIRECTORY_NAME, persona);
    }

    public List<Persona> getAllPersonas() {
        return new ArrayList<>(personas);
    }

    public Persona getPersonaByNome(String nome) {
        for (Persona persona : personas) {
            if (persona.getNome().equalsIgnoreCase(nome)) {
                return persona;
            }
        }
        return null;
    }

    public boolean updatePersona(String nome, Persona updatedPersona) {
        for (int i = 0; i < personas.size(); i++) {
            if (personas.get(i).getNome().equalsIgnoreCase(nome)) {
                Persona oldPersona = personas.set(i, updatedPersona);
                PersonaPersistence.deleteFile(DIRECTORY_NAME, oldPersona);
                PersonaPersistence.saveToFile(DIRECTORY_NAME, updatedPersona);
                System.out.println("Persona updated: " + updatedPersona);
                return true;
            }
        }
        return false;
    }

    public boolean deletePersona(String nome) {
        for (int i = 0; i < personas.size(); i++) {
            if (personas.get(i).getNome().equalsIgnoreCase(nome)) {
                Persona persona = personas.remove(i);
                PersonaPersistence.deleteFile(DIRECTORY_NAME, persona);
                System.out.println("Persona deleted: " + nome);
                return true;
            }
        }
        return false;
    }
}