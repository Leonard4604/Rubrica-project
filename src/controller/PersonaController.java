package controller;

import persistence.PersonaPersistence;
import model.Persona;

import java.util.ArrayList;
import java.util.List;

public class PersonaController {
    private static final String FILE_NAME = "informazioni.txt";

    private List<Persona> personas;

    public PersonaController() {
        this.personas = PersonaPersistence.loadFromFile(FILE_NAME);
        if (this.personas == null) {
            this.personas = new ArrayList<>();
        }
    }

    public void addPersona(Persona persona) {
        personas.add(persona);
        System.out.println("Persona added: " + persona);
        saveData();
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
                personas.set(i, updatedPersona);
                System.out.println("Persona updated: " + updatedPersona);
                saveData();
                return true;
            }
        }
        return false;
    }

    public boolean deletePersona(String nome) {
        for (int i = 0; i < personas.size(); i++) {
            if (personas.get(i).getNome().equalsIgnoreCase(nome)) {
                personas.remove(i);
                System.out.println("Persona deleted: " + nome);
                saveData();
                return true;
            }
        }
        return false;
    }

    private void saveData() {
        PersonaPersistence.saveToFile(FILE_NAME, personas);
    }
}