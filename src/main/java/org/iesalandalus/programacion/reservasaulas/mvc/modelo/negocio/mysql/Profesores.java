package org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mysql;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.IProfesores;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mysql.utilidades.MySQL;

public class Profesores implements IProfesores {
	
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
		return new ArrayList<>();

	}
	
	@Override
	public int getTamano() {
		return 0;
	}
	
	@Override
	public void insertar(Profesor profesor) throws OperationNotSupportedException {
	}

	@Override
	public Profesor buscar(Profesor profesor) {
		return null;
	}
	
	@Override
	public void borrar(Profesor profesor) throws OperationNotSupportedException {
	}

}
