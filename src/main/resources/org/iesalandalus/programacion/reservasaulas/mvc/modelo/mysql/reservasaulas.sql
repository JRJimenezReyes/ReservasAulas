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
  correo VARCHAR(45) NOT NULL,
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

DROP TABLE IF EXISTS Reservas;
CREATE TABLE IF NOT EXISTS `Reservas` (
  `idProfesor` INT NOT NULL,
  `idAula` INT NOT NULL,
  `dia` DATE NOT NULL,
  `tipo` ENUM("TRAMO", "HORA") NOT NULL,
  `tramo` ENUM("MANANA", "TARDE") NOT NULL,
  `hora` TIME NOT NULL,
  INDEX `fk_Profesores_has_Aulas_Aulas1_idx` (`idAula` ASC) VISIBLE,
  INDEX `fk_Profesores_has_Aulas_Profesores1_idx` (`idProfesor` ASC) VISIBLE,
  PRIMARY KEY (`idProfesor`, `idAula`, `dia`, `tipo`, `tramo`, `hora`),
  CONSTRAINT `fk_Profesores_has_Aulas_Profesores1`
    FOREIGN KEY (`idProfesor`)
    REFERENCES `Profesores` (`idProfesor`),
  CONSTRAINT `fk_Profesores_has_Aulas_Aulas1`
    FOREIGN KEY (`idAula`)
    REFERENCES `Aulas` (`idAula`));

