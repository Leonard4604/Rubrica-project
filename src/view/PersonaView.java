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

        JPanel topPanel = new JPanel(new GridLayout(3, 1, 10, 10));

        tableModel = new DefaultTableModel(new String[]{"Nome", "Cognome", "Telefono"}, 0);
        table = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table);

        add(topPanel, BorderLayout.NORTH);
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

        JPanel newPersonaPanel = new JPanel(new GridLayout(7, 2, 10, 10));

        JTextField newTxtNome = new JTextField(10);
        JTextField newTxtCognome = new JTextField(10);
        JTextField newTxtEta = new JTextField(5);
        JTextField newTxtTelefono = new JTextField(10);
        JTextField newTxtIndirizzo = new JTextField(20);

        JButton btnSalva = new JButton("Salva");
        btnSalva.addActionListener(e -> salvaNewPersona(newTxtNome, newTxtCognome, newTxtEta, newTxtTelefono, newTxtIndirizzo, newPersonaFrame));

        JButton btnAnnulla = new JButton("Annulla");
        btnAnnulla.addActionListener(e -> newPersonaFrame.dispose());

        newPersonaPanel.add(new JLabel("Nome:"));
        newPersonaPanel.add(newTxtNome);
        newPersonaPanel.add(new JLabel("Cognome:"));
        newPersonaPanel.add(newTxtCognome);
        newPersonaPanel.add(new JLabel("Eta:"));
        newPersonaPanel.add(newTxtEta);
        newPersonaPanel.add(new JLabel("Telefono:"));
        newPersonaPanel.add(newTxtTelefono);
        newPersonaPanel.add(new JLabel("Indirizzo:"));
        newPersonaPanel.add(newTxtIndirizzo);
        newPersonaPanel.add(btnSalva);
        newPersonaPanel.add(btnAnnulla);

        newPersonaFrame.add(newPersonaPanel);
        newPersonaFrame.setVisible(true);
    }

    private void salvaNewPersona(JTextField newTxtNome, JTextField newTxtCognome, JTextField newTxtEta, JTextField newTxtTelefono, JTextField newTxtIndirizzo, JFrame newPersonaFrame) {
        try {
            String nome = newTxtNome.getText().trim();
            String cognome = newTxtCognome.getText().trim();
            String telefono = newTxtTelefono.getText().trim();
            String indirizzo = newTxtIndirizzo.getText().trim();
            int eta = Integer.parseInt(newTxtEta.getText().trim());
            Persona persona = new Persona(nome, cognome, indirizzo, telefono, eta);
            controller.addPersona(persona);
            JOptionPane.showMessageDialog(this, "Persona aggiunta con successo!");
            refreshTable();
            newPersonaFrame.dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Eta invidalida. Per favore inserisci un eta.");
        }
    }

    private void openUpdatePersonaWindow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String selectedNome = (String) tableModel.getValueAt(selectedRow, 0);
            Persona selectedPersona = controller.getPersonaByNome(selectedNome);

            JFrame updatePersonaFrame = new JFrame("editor-persona");
            updatePersonaFrame.setSize(400, 400);
            updatePersonaFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel updatePersonaPanel = new JPanel(new GridLayout(7, 2, 10, 10));

            JTextField txtUpdateNome = new JTextField(selectedPersona.getNome(), 10);
            JTextField txtUpdateCognome = new JTextField(selectedPersona.getCognome(), 10);
            JTextField txtUpdateEta = new JTextField(String.valueOf(selectedPersona.getEta()), 5);
            JTextField txtUpdateTelefono = new JTextField(selectedPersona.getTelefono(), 10);
            JTextField txtUpdateIndirizzo = new JTextField(selectedPersona.getIndirizzo(), 20);

            JButton btnSalvaUpdate = new JButton("Salva");
            btnSalvaUpdate.addActionListener(e -> salvaUpdatedPersona(txtUpdateNome, txtUpdateCognome, txtUpdateEta, txtUpdateTelefono, txtUpdateIndirizzo, selectedPersona, updatePersonaFrame));

            JButton btnAnnullaUpdate = new JButton("Annulla");
            btnAnnullaUpdate.addActionListener(e -> updatePersonaFrame.dispose());

            updatePersonaPanel.add(new JLabel("Nome:"));
            updatePersonaPanel.add(txtUpdateNome);
            updatePersonaPanel.add(new JLabel("Cognome:"));
            updatePersonaPanel.add(txtUpdateCognome);
            updatePersonaPanel.add(new JLabel("Eta:"));
            updatePersonaPanel.add(txtUpdateEta);
            updatePersonaPanel.add(new JLabel("Telefono:"));
            updatePersonaPanel.add(txtUpdateTelefono);
            updatePersonaPanel.add(new JLabel("Indirizzo:"));
            updatePersonaPanel.add(txtUpdateIndirizzo);
            updatePersonaPanel.add(btnSalvaUpdate);
            updatePersonaPanel.add(btnAnnullaUpdate);

            updatePersonaFrame.add(updatePersonaPanel);
            updatePersonaFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Per favore selezionare una persona da modificare!");
        }
    }

    private void salvaUpdatedPersona(JTextField txtNome, JTextField txtCognome, JTextField txtEta, JTextField txtTelefono, JTextField txtIndirizzo, Persona oldPersona, JFrame updatePersonaFrame) {
        try {
            String nome = txtNome.getText().trim();
            String cognome = txtCognome.getText().trim();
            String telefono = txtTelefono.getText().trim();
            String indirizzo = txtIndirizzo.getText().trim();
            int eta = Integer.parseInt(txtEta.getText().trim());
            Persona updatedPersona = new Persona(nome, cognome, indirizzo, telefono, eta);

            controller.updatePersona(oldPersona.getNome(), updatedPersona);
            JOptionPane.showMessageDialog(this, "Persona modificata con successo!");
            refreshTable();
            updatePersonaFrame.dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid eta. Please enter a valid number.");
        }
    }

    private void deletePersona() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Per favore seleziona una persona da eliminare!");
        } else {
            String selectedNome = (String) tableModel.getValueAt(selectedRow, 0);
            String selectedCognome = (String) tableModel.getValueAt(selectedRow, 1);
            int confirmation = JOptionPane.showConfirmDialog(this,
                    "Eliminare la persona " + selectedNome + " " + selectedCognome + "?",
                    "Conferma Eliminazione", JOptionPane.YES_NO_OPTION);

            if (confirmation == JOptionPane.YES_OPTION) {
                boolean success = controller.deletePersona(selectedNome);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Persona eliminata con successo!");
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Errore durante l'eliminazione della persona.");
                }
            }
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