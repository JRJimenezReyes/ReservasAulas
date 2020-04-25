DROP SCHEMA IF EXISTS reservasaulas;

CREATE SCHEMA IF NOT EXISTS reservasaulas DEFAULT CHARACTER SET utf8 COLLATE utf8_spanish_ci;
USE reservasaulas;

DROP USER reservasaulas@'%';
CREATE USER reservasaulas@'%' IDENTIFIED BY 'reservasaulas-2020';
GRANT ALL PRIVILEGES ON reservasaulas.* TO reservasaulas;

DROP TABLE IF EXISTS Profesores;
CREATE TABLE IF NOT EXISTS Profesores (
  idProfesor INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(45) NOT NULL,
  correo VARCHAR(30) NOT NULL,
  telefono VARCHAR(9) NULL,
  PRIMARY KEY (idProfesor),
  UNIQUE INDEX correoUnico (correo ASC) VISIBLE);

DROP TABLE IF EXISTS Aulas;
CREATE TABLE IF NOT EXISTS Aulas (
  idAula INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(45) NOT NULL,
  puestos INT NOT NULL,
  PRIMARY KEY (idAula),
  UNIQUE INDEX nombreUnico (nombre ASC) VISIBLE);

DROP TABLE IF EXISTS Permanencias;
CREATE TABLE IF NOT EXISTS Permanencias (
  idPermanencia INT NOT NULL AUTO_INCREMENT,
  dia DATE NOT NULL,
  tipo ENUM("TRAMO", "HORA") NOT NULL,
  PRIMARY KEY (idPermanencia));

DROP TABLE IF EXISTS PorTramos;
CREATE TABLE IF NOT EXISTS PorTramos (
  idPorTramo INT NOT NULL AUTO_INCREMENT,
  tramo ENUM("MAÑANA", "TARDE") NOT NULL,
  idPermanencia INT NOT NULL,
  PRIMARY KEY (idPorTramo),
  CONSTRAINT porTramosPermanencias
    FOREIGN KEY (idPermanencia)
    REFERENCES Permanencias (idPermanencia));

DROP TABLE IF EXISTS PorHoras;
CREATE TABLE IF NOT EXISTS PorHoras (
  idPorHora INT NOT NULL AUTO_INCREMENT,
  hora TIME NULL,
  idPermanencia INT NOT NULL,
  PRIMARY KEY (idPorHora),
  CONSTRAINT porHorasPermanencias
    FOREIGN KEY (idPermanencia)
    REFERENCES Permanencias (idPermanencia));

DROP TABLE IF EXISTS Reservas;
CREATE TABLE IF NOT EXISTS Reservas (
  idProfesor INT NOT NULL,
  idAula INT NOT NULL,
  idPermanencia INT NOT NULL,
  PRIMARY KEY (idProfesor, idAula, idPermanencia),
  CONSTRAINT profesoresReservas
    FOREIGN KEY (idProfesor)
    REFERENCES Profesores (idProfesor),
  CONSTRAINT aulasReservas
    FOREIGN KEY (idAula)
    REFERENCES Aulas (idAula),
  CONSTRAINT permanenciasReservas
    FOREIGN KEY (idPermanencia)
    REFERENCES Permanencias (idPermanencia));

