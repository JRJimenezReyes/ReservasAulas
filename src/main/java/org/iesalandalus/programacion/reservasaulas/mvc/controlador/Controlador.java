package org.iesalandalus.programacion.reservasaulas.mvc.controlador;

import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.reservasaulas.mvc.modelo.IModelo;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Reserva;
import org.iesalandalus.programacion.reservasaulas.mvc.vista.IVista;

public class Controlador implements IControlador {

	private IVista vista;
	private IModelo modelo;
	
	public Controlador(IModelo modelo, IVista vista) {
		if (modelo == null) {
			throw new IllegalArgumentException("ERROR: El modelo no puede ser nulo.");
		}
		if (vista == null) {
			throw new IllegalArgumentException("ERROR: La vista no puede ser nula.");
		}
		this.modelo = modelo;
		this.vista = vista;
		this.vista.setControlador(this);
	}
	
	@Override
	public void comenzar() {
		vista.comenzar();
	}
	
	@Override
	public void terminar() {
		System.out.println("Hasta luego Lucas!!!");
	}
	
	@Override
	public void insertar(Profesor profesor) throws OperationNotSupportedException {
		modelo.insertar(profesor);
	}
	
	@Override
	public void insertar(Aula aula) throws OperationNotSupportedException {
		modelo.insertar(aula);
	}
	
	@Override
	public void insertar(Reserva reserva) throws OperationNotSupportedException {
		modelo.insertar(reserva);
	}
	
	@Override
	public Profesor buscar(Profesor profesor) {
		return modelo.buscar(profesor);
	}
	
	@Override
	public Aula buscar(Aula aula) {
		return modelo.buscar(aula);
	}
	
	@Override
	public Reserva buscar(Reserva reserva) {
		return modelo.buscar(reserva);
	}
	
	@Override
	public void borrar(Profesor profesor) throws OperationNotSupportedException {
		modelo.borrar(profesor);
	}
	
	@Override
	public void borrar(Aula aula) throws OperationNotSupportedException {
		modelo.borrar(aula);
	}
	
	@Override
	public void borrar(Reserva aula) throws OperationNotSupportedException {
		modelo.borrar(aula);
	}
	
	@Override
	public List<Profesor> getProfesores() {
		return modelo.getProfesores();
	}
	
	@Override
	public List<Aula> getAulas() {
		return modelo.getAulas();
	}
	
	@Override
	public List<Reserva> getReservas() {
		return modelo.getReservas();
	}
	
	@Override
	public List<Reserva> getReservas(Profesor profesor) {
		return modelo.getReservas(profesor);
	}
	
	@Override
	public List<Reserva> getReservas(Aula aula) {
		return modelo.getReservas(aula);
	}

}
