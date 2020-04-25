package org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mysql;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.IAulas;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mysql.utilidades.MySQL;

public class Aulas implements IAulas {
	
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
		return new ArrayList<>();
	}
	
	@Override
	public int getTamano() {
		return 0;
	}
	
	
	@Override
	public void insertar(Aula aula) throws OperationNotSupportedException {
	}
	
	@Override
	public Aula buscar(Aula aula) {
		return null;
	}
	
	@Override
	public void borrar(Aula aula) throws OperationNotSupportedException {
	}

}
