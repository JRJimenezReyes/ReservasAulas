package org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mysqlxdevapi;

import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Reserva;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.IReservas;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mysqlxdevapi.utilidades.SesionBD;

import com.mysql.cj.xdevapi.Schema;

public class Reservas implements IReservas {
	
	private Schema esquemaBD = null;

	@Override
	public void comenzar() {
		esquemaBD = SesionBD.getEsquema();	
	}

	@Override
	public void terminar() {
		SesionBD.cierraSesion();
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
