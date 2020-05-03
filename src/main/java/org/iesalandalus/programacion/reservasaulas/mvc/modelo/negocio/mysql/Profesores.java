package org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.IProfesores;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mysql.utilidades.MySQL;

public class Profesores implements IProfesores {
	
	private static final String ERROR = "ERROR: ";
	
	Connection conexion = null;
	
	public Profesores() {
		//
	}
	
	@Override
	public void comenzar() {
		conexion = MySQL.establecerConexion();
	}

	@Override
	public void terminar() {
		MySQL.cerrarConexion();
	}
	
	@Override
	public List<Profesor> get() {
		List<Profesor> profesores = new ArrayList<>();
		try {
			String sentenciaStr = "select nombre, correo, telefono from Profesores order by correo";
			Statement sentencia = conexion.createStatement();
			ResultSet filas = sentencia.executeQuery(sentenciaStr);
			while (filas.next()) {
				String nombre = filas.getString(1);
				String correo = filas.getString(2);
				String telefono = filas.getString(3);
				Profesor profesor = new Profesor(nombre, correo, telefono);
				profesores.add(profesor);
			}
		} catch (SQLException e) {
			throw new IllegalArgumentException(ERROR + e.getMessage());
		}
		return profesores;

	}
	
	@Override
	public int getTamano() {
		int tamano = 0;
		try {
			String sentenciaStr = "select count(*) from Profesores";
			Statement sentencia = conexion.createStatement();
			ResultSet filas = sentencia.executeQuery(sentenciaStr);
			if (filas.next()) {
				tamano = filas.getInt(1);
			}
		} catch (SQLException e) {
			throw new IllegalArgumentException(ERROR + e.getMessage());
		}
		return tamano;
	}
	
	@Override
	public void insertar(Profesor profesor) throws OperationNotSupportedException {
		if (profesor == null) {
			throw new NullPointerException("ERROR: No se puede insertar un profesor nulo.");
		}
		try {
			String sentenciaStr = "insert into Profesores values (null, ?, ?, ?)";
			PreparedStatement sentencia = conexion.prepareStatement(sentenciaStr);
			sentencia.setString(1, profesor.getNombre());
			sentencia.setString(2, profesor.getCorreo());
			sentencia.setString(3, profesor.getTelefono());
			sentencia.executeUpdate();
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new OperationNotSupportedException("ERROR: Ya existe un profesor con ese correo.");
		} catch (SQLException e) {
			throw new OperationNotSupportedException(ERROR + e.getMessage());
		}
	}

	@Override
	public Profesor buscar(Profesor profesor) {
		if (profesor == null) {
			throw new IllegalArgumentException("ERROR: No se puede buscar un profesor nulo.");
		}
		try {
			String sentenciaStr = "select nombre, correo, telefono from Profesores where correo=?";
			PreparedStatement sentencia = conexion.prepareStatement(sentenciaStr);
			sentencia.setString(1, profesor.getCorreo());
			ResultSet filas = sentencia.executeQuery();
			if (filas.next()) {
				String nombre = filas.getString(1);
				String correo = filas.getNString(2);
				String telefono = filas.getString(3);
				profesor = new Profesor(nombre, correo, telefono);
			}
		} catch (SQLException e) {
			throw new IllegalArgumentException(ERROR + e.getMessage());
		}
		return profesor;
	}
	
	@Override
	public void borrar(Profesor profesor) throws OperationNotSupportedException {
		if (profesor == null) {
			throw new IllegalArgumentException("ERROR: No se puede borrar un profesor nulo.");
		}
		try {
			String sentenciaStr = "delete from Profesores where correo = ?";
			PreparedStatement sentencia = conexion.prepareStatement(sentenciaStr);
			sentencia.setString(1, profesor.getCorreo());
			if (sentencia.executeUpdate() == 0) {
				throw new OperationNotSupportedException("ERROR: No existe ningún profesor con ese correo.");
			}
		} catch (SQLException e) {
			throw new OperationNotSupportedException(ERROR + e.toString());
		}
	}

}
