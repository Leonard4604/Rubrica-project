package controller;

import dao.UtenteDAO;

public class UtenteController {

    private UtenteDAO userDAO;

    public UtenteController() {
        userDAO = new UtenteDAO();
    }

    public boolean validateLogin(String username, String password) {
        return userDAO.validateUser(username, password);
    }
}