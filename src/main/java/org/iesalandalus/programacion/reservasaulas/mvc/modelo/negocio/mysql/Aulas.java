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

import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.IAulas;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mysql.utilidades.MySQL;

public class Aulas implements IAulas {
	
	private static final String ERROR = "ERROR: ";
	
	Connection conexion = null;
	
	public Aulas() {
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
	public List<Aula> get() {
		List<Aula> aulas = new ArrayList<>();
		try {
			String sentenciaStr = "select nombre, puestos from Aulas order by nombre";
			Statement sentencia = conexion.createStatement();
			ResultSet filas = sentencia.executeQuery(sentenciaStr);
			while (filas.next()) {
				String nombre = filas.getString(1);
				int puestos = filas.getInt(2);
				Aula aula = new Aula(nombre, puestos);
				aulas.add(aula);
			}
		} catch (SQLException e) {
			throw new IllegalArgumentException(ERROR + e.getMessage());
		}
		return aulas;
	}
	
	@Override
	public int getTamano() {
		int tamano = 0;
		try {
			String sentenciaStr = "select count(*) from Aulas";
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
	public void insertar(Aula aula) throws OperationNotSupportedException {
		if (aula == null) {
			throw new NullPointerException("ERROR: No se puede insertar un aula nula.");
		}
		try {
			String sentenciaStr = "insert into Aulas values (null, ?, ?)";
			PreparedStatement sentencia = conexion.prepareStatement(sentenciaStr);
			sentencia.setString(1, aula.getNombre());
			sentencia.setInt(2, aula.getPuestos());
			sentencia.executeUpdate();
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new OperationNotSupportedException("ERROR: Ya existe un aula con ese nombre.");
		} catch (SQLException e) {
			throw new OperationNotSupportedException(ERROR + e.getMessage());
		}
	}
	
	@Override
	public Aula buscar(Aula aula) {
		if (aula == null) {
			throw new IllegalArgumentException("ERROR: No se puede buscar un aula nula.");
		}
		try {
			String sentenciaStr = "select nombre, puestos from Aulas where nombre=?";
			PreparedStatement sentencia = conexion.prepareStatement(sentenciaStr);
			sentencia.setString(1, aula.getNombre());
			ResultSet filas = sentencia.executeQuery();
			if (filas.next()) {
				String nombre = filas.getString(1);
				int puestos = filas.getInt(2);
				aula = new Aula(nombre, puestos);
			}
		} catch (SQLException e) {
			throw new IllegalArgumentException(ERROR + e.getMessage());
		}
		return aula;
	}
	
	@Override
	public void borrar(Aula aula) throws OperationNotSupportedException {
		if (aula == null) {
			throw new IllegalArgumentException("ERROR: No se puede borrar un aula nula.");
		}
		try {
			String sentenciaStr = "delete from Aulas where nombre = ?";
			PreparedStatement sentencia = conexion.prepareStatement(sentenciaStr);
			sentencia.setString(1, aula.getNombre());
			if (sentencia.executeUpdate() == 0) {
				throw new OperationNotSupportedException("ERROR: No existe ningún aula con ese nombre.");
			}
		} catch (SQLException e) {
			throw new OperationNotSupportedException(ERROR + e.toString());
		}
	}

}
