package org.iesalandalus.programacion.reservasaulas.mvc.modelo;

import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Reserva;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.IAulas;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.IProfesores;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.IReservas;


public class Modelo implements IModelo {

	private IProfesores profesores;
	private IAulas aulas;
	private IReservas reservas;
	
	public Modelo(IFuenteDatos fuenteDatos) {
		profesores = fuenteDatos.crearProfesores();
		aulas = fuenteDatos.crearAulas();
		reservas = fuenteDatos.crearReservas();
	}
	
	@Override
	public void comenzar() {
		aulas.comenzar();
	}

	@Override
	public void terminar() {
		aulas.terminar();
	}
	
	@Override
	public void insertar(Profesor profesor) throws OperationNotSupportedException {
		profesores.insertar(profesor);
	}
	
	@Override
	public void insertar(Aula aula) throws OperationNotSupportedException {
		aulas.insertar(aula);
	}
	
	@Override
	public void insertar(Reserva reserva) throws OperationNotSupportedException {
		if (reserva == null) {
			throw new NullPointerException("ERROR: No se puede insertar una reserva nula.");
		}
		Profesor profesor = profesores.buscar(reserva.getProfesor());
		if (profesor == null) {
			throw new OperationNotSupportedException("ERROR: No existe ningún profesor con ese DNI.");
		}
		Aula aula = aulas.buscar(reserva.getAula());
		if (aula == null) {
			throw new OperationNotSupportedException("ERROR: No existe ningún aula con ese nombre.");
		}
		reservas.insertar(new Reserva(profesor, aula, reserva.getPermanencia()));
	}
	
	@Override
	public Profesor buscar(Profesor profesor) {
		return profesores.buscar(profesor);
	}
	
	@Override
	public Aula buscar(Aula aula) {
		return aulas.buscar(aula);
	}
	
	@Override
	public Reserva buscar(Reserva reserva) {
		return reservas.buscar(reserva);
	}
	
	@Override
	public void borrar(Profesor profesor) throws OperationNotSupportedException {
		List<Reserva> reservasProfesor = reservas.get(profesor);
		for (Reserva reserva : reservasProfesor) {
			reservas.borrar(reserva);
		}
		profesores.borrar(profesor);
	}
	
	@Override
	public void borrar(Aula aula) throws OperationNotSupportedException {
		List<Reserva> reservasAula = reservas.get(aula);
		for (Reserva reserva : reservasAula) {
			reservas.borrar(reserva);
		}
		aulas.borrar(aula);
	}
	
	@Override
	public void borrar(Reserva reserva) throws OperationNotSupportedException {
		reservas.borrar(reserva);
	}

	@Override
	public List<Profesor> getProfesores() {
		return profesores.get();
	}
	
	@Override
	public List<Aula> getAulas() {
		return aulas.get();
	}
	
	@Override
	public List<Reserva> getReservas() {
		return reservas.get();
	}
	
	@Override
	public List<Reserva> getReservas(Profesor profesor) {
		return reservas.get(profesor);
	}
	
	@Override
	public List<Reserva> getReservas(Aula aula) {
		return reservas.get(aula);
	}

}

