package view;

import controller.PersonaController;
import model.Persona;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PersonaView extends JFrame {
    private final PersonaController controller;

    private JButton btnAdd, btnUpdate, btnDelete;
    private JTable table;
    private DefaultTableModel tableModel;

    public PersonaView() {
        controller = new PersonaController();
        initialize();
        refreshTable();
    }

    private void initialize() {
        setTitle("Rubrica");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[]{"Nome", "Cognome", "Telefono"}, 0);
        table = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table);

        add(tableScrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        btnAdd = new JButton("Nuovo");
        btnUpdate = new JButton("Modifica");
        btnDelete = new JButton("Elimina");
        bottomPanel.add(btnAdd);
        bottomPanel.add(btnUpdate);
        bottomPanel.add(btnDelete);
        add(bottomPanel, BorderLayout.SOUTH);

        attachEventHandlers();

        setVisible(true);
    }

    private void attachEventHandlers() {
        btnAdd.addActionListener(e -> openNewPersonaWindow());
        btnUpdate.addActionListener(e -> openUpdatePersonaWindow());
        btnDelete.addActionListener(e -> deletePersona());
    }

    private void openNewPersonaWindow() {
        JFrame newPersonaFrame = new JFrame("editor-persona");
        newPersonaFrame.setSize(400, 400);
        newPersonaFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel newPersonaPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        JTextField newTxtNome = new JTextField(10);
        JTextField newTxtCognome = new JTextField(10);
        JTextField newTxtTelefono = new JTextField(10);
        JTextField newTxtIndirizzo = new JTextField(20);
        JTextField newTxtEta = new JTextField(5);

        JButton btnSalva = new JButton("Salva");
        btnSalva.addActionListener(e -> salvaNewPersona(newTxtNome, newTxtCognome, newTxtTelefono, newTxtIndirizzo, newTxtEta, newPersonaFrame));
        JButton btnAnnulla = new JButton("Annulla");
        btnAnnulla.addActionListener(e -> newPersonaFrame.dispose());

        newPersonaPanel.add(new JLabel("Nome:"));
        newPersonaPanel.add(newTxtNome);
        newPersonaPanel.add(new JLabel("Cognome:"));
        newPersonaPanel.add(newTxtCognome);
        newPersonaPanel.add(new JLabel("Telefono:"));
        newPersonaPanel.add(newTxtTelefono);
        newPersonaPanel.add(new JLabel("Indirizzo:"));
        newPersonaPanel.add(newTxtIndirizzo);
        newPersonaPanel.add(new JLabel("Eta:"));
        newPersonaPanel.add(newTxtEta);
        newPersonaPanel.add(btnSalva);
        newPersonaPanel.add(btnAnnulla);

        newPersonaFrame.add(newPersonaPanel);
        newPersonaFrame.setVisible(true);
    }

    private void salvaNewPersona(JTextField nomeField, JTextField cognomeField, JTextField telefonoField, JTextField indirizzoField, JTextField etaField, JFrame frame) {
        try {
            String nome = nomeField.getText().trim();
            String cognome = cognomeField.getText().trim();
            String telefono = telefonoField.getText().trim();
            String indirizzo = indirizzoField.getText().trim();
            int eta = Integer.parseInt(etaField.getText().trim());

            Persona persona = new Persona(nome, cognome, indirizzo, telefono, eta);
            controller.addPersona(persona);
            JOptionPane.showMessageDialog(this, "Persona aggiunta con successo!");
            refreshTable();
            frame.dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Errore: l'età deve essere un numero intero.");
        }
    }

    private void openUpdatePersonaWindow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String nome = (String) tableModel.getValueAt(selectedRow, 0);
            Persona persona = controller.getPersonaByNome(nome);

            JFrame updateFrame = new JFrame("editor-persona");
            updateFrame.setSize(400, 400);
            updateFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel updatePanel = new JPanel(new GridLayout(6, 2, 10, 10));
            JTextField txtNome = new JTextField(persona.getNome());
            JTextField txtCognome = new JTextField(persona.getCognome());
            JTextField txtTelefono = new JTextField(persona.getTelefono());
            JTextField txtIndirizzo = new JTextField(persona.getIndirizzo());
            JTextField txtEta = new JTextField(String.valueOf(persona.getEta()));

            JButton btnSalva = new JButton("Salva");
            btnSalva.addActionListener(e -> salvaUpdatedPersona(txtNome, txtCognome, txtTelefono, txtIndirizzo, txtEta, persona, updateFrame));
            JButton btnAnnulla = new JButton("Annulla");
            btnAnnulla.addActionListener(e -> updateFrame.dispose());

            updatePanel.add(new JLabel("Nome:"));
            updatePanel.add(txtNome);
            updatePanel.add(new JLabel("Cognome:"));
            updatePanel.add(txtCognome);
            updatePanel.add(new JLabel("Telefono:"));
            updatePanel.add(txtTelefono);
            updatePanel.add(new JLabel("Indirizzo:"));
            updatePanel.add(txtIndirizzo);
            updatePanel.add(new JLabel("Eta:"));
            updatePanel.add(txtEta);
            updatePanel.add(btnSalva);
            updatePanel.add(btnAnnulla);

            updateFrame.add(updatePanel);
            updateFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Per favore seleziona una persona da modificare!");
        }
    }

    private void salvaUpdatedPersona(JTextField nomeField, JTextField cognomeField, JTextField telefonoField, JTextField indirizzoField, JTextField etaField, Persona persona, JFrame frame) {
        try {
            String nome = nomeField.getText().trim();
            String cognome = cognomeField.getText().trim();
            String telefono = telefonoField.getText().trim();
            String indirizzo = indirizzoField.getText().trim();
            int eta = Integer.parseInt(etaField.getText().trim());

            Persona updatedPersona = new Persona(nome, cognome, indirizzo, telefono, eta);
            controller.updatePersona(persona.getNome(), updatedPersona);
            JOptionPane.showMessageDialog(this, "Persona aggiornata con successo!");
            refreshTable();
            frame.dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Errore: l'età deve essere un numero intero.");
        }
    }

    private void deletePersona() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String nome = (String) tableModel.getValueAt(selectedRow, 0);
            int confirmation = JOptionPane.showConfirmDialog(this, "Eliminare la persona " + nome + "?", "Conferma eliminazione", JOptionPane.YES_NO_OPTION);
            if (confirmation == JOptionPane.YES_OPTION) {
                if (controller.deletePersona(nome)) {
                    JOptionPane.showMessageDialog(this, "Persona eliminata con successo!");
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Errore durante l'eliminazione.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Per favore seleziona una persona da eliminare!");
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Persona> personas = controller.getAllPersonas();
        for (Persona persona : personas) {
            tableModel.addRow(new Object[]{persona.getNome(), persona.getCognome(), persona.getTelefono()});
        }
    }
}
