package controller;

import dao.PersonaDAO;
import model.Persona;

import java.util.List;

public class PersonaController {

    private final PersonaDAO personaDAO;

    public PersonaController() {
        personaDAO = new PersonaDAO();
    }

    public boolean addPersona(Persona persona) {
        return personaDAO.addPersona(persona);
    }

    public List<Persona> getAllPersonas() {
        return personaDAO.getAllPersonas();
    }

    public boolean updatePersona(int id, Persona updatedPersona) {
        return personaDAO.updatePersona(id, updatedPersona);
    }

    public boolean deletePersona(int id) {
        return personaDAO.deletePersona(id);
    }

    public Persona getPersonaById(int id) {
        return personaDAO.getPersonaById(id);
    }
}