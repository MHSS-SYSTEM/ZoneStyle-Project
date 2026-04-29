CREATE DATABASE IF NOT EXISTS cancion_db;
USE cancion_db;

CREATE TABLE IF NOT EXISTS cancion (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    artista VARCHAR(100) NOT NULL,
    genero VARCHAR(50),
    duracion DOUBLE NOT NULL,
    anio INT
) ENGINE=InnoDB;

-- Datos de prueba
INSERT INTO cancion (titulo, artista, genero, duracion, anio) VALUES
('Bohemian Rhapsody', 'Queen', 'Rock', 5.55, 1975),
('Blinding Lights', 'The Weeknd', 'Pop', 3.22, 2019),
('Despacito', 'Luis Fonsi', 'Reggaeton', 3.89, 2017);
