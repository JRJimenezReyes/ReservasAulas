package org.iesalandalus.programacion.reservasaulas.mvc.modelo;

import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Reserva;

public interface IModelo {
	
	void comenzar();
	
	void terminar();

	void insertar(Profesor profesor) throws OperationNotSupportedException;

	void insertar(Aula aula) throws OperationNotSupportedException;

	void insertar(Reserva reserva) throws OperationNotSupportedException;

	Profesor buscar(Profesor profesor);

	Aula buscar(Aula aula);

	Reserva buscar(Reserva reserva);

	void borrar(Profesor profesor) throws OperationNotSupportedException;

	void borrar(Aula aula) throws OperationNotSupportedException;

	void borrar(Reserva reserva) throws OperationNotSupportedException;

	List<Profesor> getProfesores();

	List<Aula> getAulas();

	List<Reserva> getReservas();

	List<Reserva> getReservas(Profesor profesor);

	List<Reserva> getReservas(Aula aula);

}