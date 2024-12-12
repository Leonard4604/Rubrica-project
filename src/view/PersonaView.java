package view;

import controller.PersonaController;
import model.Persona;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Objects;

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
        table.setFillsViewportHeight(true);

        table.getColumnModel().getColumn(0).setPreferredWidth(150);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);

        JScrollPane tableScrollPane = new JScrollPane(table);

        add(tableScrollPane, BorderLayout.CENTER);

        JToolBar toolBar = createToolBar();
        toolBar.setFloatable(false);
        add(toolBar, BorderLayout.NORTH);

        attachEventHandlers();

        setVisible(true);
    }

    private JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        ImageIcon addIcon = resizeIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/add.png"))));
        ImageIcon editIcon = resizeIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/edit.png"))));
        ImageIcon deleteIcon = resizeIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/delete.png"))));

        btnAdd = new JButton("Nuovo", addIcon);
        toolBar.add(btnAdd);

        btnUpdate = new JButton("Modifica", editIcon);
        toolBar.add(btnUpdate);

        btnDelete = new JButton("Elimina", deleteIcon);
        toolBar.add(btnDelete);

        return toolBar;
    }

    private ImageIcon resizeIcon(ImageIcon icon) {
        Image img = icon.getImage();
        Image resizedImage = img.getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
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
        newPersonaFrame.setLayout(new BorderLayout());

        // Center panel with text fields
        JPanel newPersonaPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        JTextField newTxtNome = new JTextField(10);
        JTextField newTxtCognome = new JTextField(10);
        JTextField newTxtTelefono = new JTextField(10);
        JTextField newTxtIndirizzo = new JTextField(20);
        JTextField newTxtEta = new JTextField(5);

        newPersonaPanel.add(new JLabel("Nome:"));
        newPersonaPanel.add(newTxtNome);
        newPersonaPanel.add(new JLabel("Cognome:"));
        newPersonaPanel.add(newTxtCognome);
        newPersonaPanel.add(new JLabel("Telefono:"));
        newPersonaPanel.add(newTxtTelefono);
        newPersonaPanel.add(new JLabel("Indirizzo:"));
        newPersonaPanel.add(newTxtIndirizzo);
        newPersonaPanel.add(new JLabel("Età:"));
        newPersonaPanel.add(newTxtEta);

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        ImageIcon saveIcon = resizeIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/save.png"))));
        ImageIcon cancelIcon = resizeIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/cancel.png"))));

        JButton btnSalva = new JButton("Salva", saveIcon);
        JButton btnAnnulla = new JButton("Annulla", cancelIcon);

        btnSalva.addActionListener(e -> salvaNewPersona(newTxtNome, newTxtCognome, newTxtTelefono, newTxtIndirizzo, newTxtEta, newPersonaFrame));
        btnAnnulla.addActionListener(e -> newPersonaFrame.dispose());

        toolBar.add(btnSalva);
        toolBar.add(btnAnnulla);

        newPersonaFrame.add(toolBar, BorderLayout.NORTH);
        newPersonaFrame.add(newPersonaPanel, BorderLayout.CENTER);
        newPersonaFrame.setVisible(true);
    }

    private void salvaNewPersona(JTextField nomeField, JTextField cognomeField, JTextField telefonoField, JTextField indirizzoField, JTextField etaField, JFrame frame) {
        try {
            String customStringMessage = "Per favore inserisci";
            String nome = nomeField.getText().trim();
            if (nome.isEmpty()) {
                customStringMessage = customStringMessage + " nome";
            }
            String cognome = cognomeField.getText().trim();
            if (cognome.isEmpty()) {
                customStringMessage = customStringMessage + " cognome";
            }
            String telefono = telefonoField.getText().trim();
            if (telefono.isEmpty()) {
                customStringMessage = customStringMessage + " telefono";
            }
            String indirizzo = indirizzoField.getText().trim();
            if (indirizzo.isEmpty()) {
                customStringMessage = customStringMessage + " indirizzo";
            }
            int eta = Integer.parseInt(etaField.getText().trim());
            if (eta <= 0 || eta > 100) {
                customStringMessage = customStringMessage + " eta valida (1-100)";
            }

            if (!customStringMessage.equalsIgnoreCase("Per favore inserisci")) {
                JOptionPane.showMessageDialog(this, customStringMessage);
                return;
            }

            Persona persona = new Persona(0, nome, cognome, indirizzo, telefono, eta);
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
            int id = controller.getAllPersonas().get(selectedRow).getId();
            Persona persona = controller.getPersonaById(id);

            if (persona == null) {
                JOptionPane.showMessageDialog(this, "Errore: persona non trovata!");
                return;
            }

            JFrame updateFrame = new JFrame("editor-persona");
            updateFrame.setSize(400, 400);
            updateFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            updateFrame.setLayout(new BorderLayout());

            JPanel updatePanel = new JPanel(new GridLayout(5, 2, 10, 10));
            JTextField txtNome = new JTextField(persona.getNome());
            JTextField txtCognome = new JTextField(persona.getCognome());
            JTextField txtTelefono = new JTextField(persona.getTelefono());
            JTextField txtIndirizzo = new JTextField(persona.getIndirizzo());
            JTextField txtEta = new JTextField(String.valueOf(persona.getEta()));

            updatePanel.add(new JLabel("Nome:"));
            updatePanel.add(txtNome);
            updatePanel.add(new JLabel("Cognome:"));
            updatePanel.add(txtCognome);
            updatePanel.add(new JLabel("Telefono:"));
            updatePanel.add(txtTelefono);
            updatePanel.add(new JLabel("Indirizzo:"));
            updatePanel.add(txtIndirizzo);
            updatePanel.add(new JLabel("Età:"));
            updatePanel.add(txtEta);

            JToolBar toolBar = new JToolBar();
            toolBar.setFloatable(false);

            ImageIcon saveIcon = resizeIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/save.png"))));
            ImageIcon cancelIcon = resizeIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/cancel.png"))));

            JButton btnSalva = new JButton("Salva", saveIcon);
            JButton btnAnnulla = new JButton("Annulla", cancelIcon);

            btnSalva.addActionListener(e -> salvaUpdatedPersona(id, txtNome, txtCognome, txtTelefono, txtIndirizzo, txtEta, updateFrame));
            btnAnnulla.addActionListener(e -> updateFrame.dispose());

            toolBar.add(btnSalva);
            toolBar.add(btnAnnulla);

            updateFrame.add(toolBar, BorderLayout.NORTH);
            updateFrame.add(updatePanel, BorderLayout.CENTER);
            updateFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Per favore seleziona una persona da modificare!");
        }
    }

    private void salvaUpdatedPersona(int id, JTextField nomeField, JTextField cognomeField, JTextField telefonoField, JTextField indirizzoField, JTextField etaField, JFrame frame) {
        try {
            String customStringMessage = "Per favore inserisci";
            String nome = nomeField.getText().trim();
            if (nome.isEmpty()) {
                customStringMessage = customStringMessage + " nome";
            }
            String cognome = cognomeField.getText().trim();
            if (cognome.isEmpty()) {
                customStringMessage = customStringMessage + " cognome";
            }
            String telefono = telefonoField.getText().trim();
            if (telefono.isEmpty()) {
                customStringMessage = customStringMessage + " telefono";
            }
            String indirizzo = indirizzoField.getText().trim();
            if (indirizzo.isEmpty()) {
                customStringMessage = customStringMessage + " indirizzo";
            }
            int eta = Integer.parseInt(etaField.getText().trim());
            if (eta <= 0 || eta > 100) {
                customStringMessage = customStringMessage + " eta valida (1-100)";
            }

            if (!customStringMessage.equalsIgnoreCase("Per favore inserisci")) {
                JOptionPane.showMessageDialog(this, customStringMessage);
                return;
            }


            Persona updatedPersona = new Persona(id, nome, cognome, indirizzo, telefono, eta);
            controller.updatePersona(id, updatedPersona);
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
            int id = controller.getAllPersonas().get(selectedRow).getId();

            String selectedNome = (String) tableModel.getValueAt(selectedRow, 0);
            String selectedCognome = (String) tableModel.getValueAt(selectedRow, 1);
            int confirmation = JOptionPane.showConfirmDialog(this,
                    "Eliminare la persona " + selectedNome + " " + selectedCognome + "?",
                    "Conferma Eliminazione", JOptionPane.YES_NO_OPTION);
            if (confirmation == JOptionPane.YES_OPTION) {
                if (controller.deletePersona(id)) {
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
