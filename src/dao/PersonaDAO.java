package dao;

import model.Persona;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonaDAO {
    private List<Persona> personas;

    public PersonaDAO() {
        this.personas = new ArrayList<>();
    }

    public boolean addPersona(Persona persona) {
        String query = "INSERT INTO persona (nome, cognome, indirizzo, telefono, eta) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, persona.getNome());
            stmt.setString(2, persona.getCognome());
            stmt.setString(3, persona.getIndirizzo());
            stmt.setString(4, persona.getTelefono());
            stmt.setInt(5, persona.getEta());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        persona.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Persona> getAllPersonas() {
        List<Persona> personas = new ArrayList<>();
        String query = "SELECT * FROM persona";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Persona persona = new Persona(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("indirizzo"),
                        rs.getString("telefono"),
                        rs.getInt("eta")
                );
                personas.add(persona);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.personas = personas;

        return personas;
    }

    public boolean updatePersona(int id, Persona updatedPersona) {
        String query = "UPDATE persona SET nome = ?, cognome = ?, indirizzo = ?, telefono = ?, eta = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, updatedPersona.getNome());
            stmt.setString(2, updatedPersona.getCognome());
            stmt.setString(3, updatedPersona.getIndirizzo());
            stmt.setString(4, updatedPersona.getTelefono());
            stmt.setInt(5, updatedPersona.getEta());
            stmt.setInt(6, id);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePersona(int id) {
        String query = "DELETE FROM persona WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Persona getPersonaById(int id) {
        String query = "SELECT * FROM persona WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Persona(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("cognome"),
                            rs.getString("indirizzo"),
                            rs.getString("telefono"),
                            rs.getInt("eta")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}