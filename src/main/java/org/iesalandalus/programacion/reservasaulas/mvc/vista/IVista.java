package org.iesalandalus.programacion.reservasaulas.mvc.vista;

import org.iesalandalus.programacion.reservasaulas.mvc.controlador.IControlador;

public interface IVista {

	void setControlador(IControlador controlador);

	void comenzar();

	void terminar();

	void insertarProfesor();

	void buscarProfesor();

	void borrarProfesor();

	void listarProfesores();

	void insertarAula();

	void buscarAula();

	void borrarAula();

	void listarAulas();

	void insertarReserva();

	void buscarReserva();

	void borrarReserva();

	void listarReservas();

	void listarReservasProfesor();

	void listarReservasAula();

}