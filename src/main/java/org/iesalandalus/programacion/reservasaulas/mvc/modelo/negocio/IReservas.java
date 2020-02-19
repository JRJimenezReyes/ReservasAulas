package org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio;

import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Reserva;

public interface IReservas {
	
	void comenzar();
	
	void terminar();

	List<Reserva> get();

	List<Reserva> get(Profesor profesor);

	List<Reserva> get(Aula aula);

	int getTamano();

	void insertar(Reserva reserva) throws OperationNotSupportedException;

	Reserva buscar(Reserva reserva);

	void borrar(Reserva reserva) throws OperationNotSupportedException;

}