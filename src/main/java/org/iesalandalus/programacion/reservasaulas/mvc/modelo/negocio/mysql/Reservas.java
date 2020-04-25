package org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mysql;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Reserva;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.IReservas;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mysql.utilidades.MySQL;

public class Reservas implements IReservas {
	
	Connection conexion = null;
	
	public Reservas() {
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
	public List<Reserva> get() {
		return new ArrayList<>();
	}
	
	@Override
	public List<Reserva> get(Profesor profesor) {
		return new ArrayList<>();
	}
	
	@Override
	public List<Reserva> get(Aula aula) {
		return new ArrayList<>();
	}
	
	@Override
	public int getTamano() {
		return 0;
	}
	
	@Override
	public void insertar(Reserva reserva) throws OperationNotSupportedException {
	}
	
	@Override
	public Reserva buscar(Reserva reserva) {
		return null;
	}
	
	@Override
	public void borrar(Reserva reserva) throws OperationNotSupportedException {
	}

}
