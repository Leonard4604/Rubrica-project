package view;

import controller.UtenteController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class LoginView extends JFrame {
    private final UtenteController controller;

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public LoginView() {
        controller = new UtenteController();
        initialize();
    }

    private void initialize() {
        setTitle("Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        ImageIcon loginIcon = resizeIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/login.png"))));

        btnLogin = new JButton("Login", loginIcon);
        toolBar.add(btnLogin);

        add(toolBar, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.add(new JLabel("Username:"));
        txtUsername = new JTextField(20);
        formPanel.add(txtUsername);

        formPanel.add(new JLabel("Password:"));
        txtPassword = new JPasswordField(20);
        formPanel.add(txtPassword);

        add(formPanel, BorderLayout.CENTER);

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        setVisible(true);
    }

    private void login() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (controller.validateLogin(username, password)) {
            JOptionPane.showMessageDialog(this, "Login avvenuto con successo!");
            new PersonaView();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid login credentials.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private ImageIcon resizeIcon(ImageIcon icon) {
        Image img = icon.getImage();
        Image resizedImage = img.getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }
}