CREATE DATABASE IF NOT EXISTS rubrica;

USE rubrica;

CREATE TABLE IF NOT EXISTS persona (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(50) NOT NULL,
    cognome VARCHAR(50) NOT NULL,
    indirizzo VARCHAR(100) NOT NULL,
    telefono VARCHAR(20) UNIQUE NOT NULL,
    eta INT NOT NULL
);

CREATE TABLE IF NOT EXISTS utente (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL
);

INSERT INTO utente (username, password)
SELECT 'admin', 'admin'
    WHERE NOT EXISTS (
    SELECT 1 FROM utente WHERE username = 'admin' and password = 'admin'
);