package org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Permanencia;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.PermanenciaPorHora;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.PermanenciaPorTramo;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Reserva;

public class Reservas {

	private List<Reserva> coleccionReservas;
	
	public Reservas() {
		coleccionReservas = new ArrayList<>();
	}
	
	public List<Reserva> get() {
		List<Reserva> reservasOrdenadas = copiaProfundaReservas();
		Comparator<Aula> comparadorAula = Comparator.comparing(Aula::getNombre);
		Comparator<Permanencia> comparadorPermanencia = (Permanencia p1, Permanencia p2) -> {
			int comparacion = -1;
			if (p1.getDia().equals(p2.getDia())) {
				if (p1 instanceof PermanenciaPorTramo && p2 instanceof PermanenciaPorTramo) {
					comparacion = Integer.compare(((PermanenciaPorTramo)p1).getTramo().ordinal(), ((PermanenciaPorTramo)p2).getTramo().ordinal());
				} else if (p1 instanceof PermanenciaPorHora && p2 instanceof PermanenciaPorHora) {
					comparacion = ((PermanenciaPorHora)p1).getHora().compareTo(((PermanenciaPorHora)p2).getHora());
				}
			} else {
				comparacion = p1.getDia().compareTo(p2.getDia());
			}
			return comparacion;
		};
		reservasOrdenadas.sort(Comparator.comparing(Reserva::getAula, comparadorAula).thenComparing(Reserva::getPermanencia, comparadorPermanencia));
		return reservasOrdenadas;
	}
	
	private List<Reserva> copiaProfundaReservas() {
		List<Reserva> copiaReservas = new ArrayList<>();
		for (Reserva reserva : coleccionReservas) {
			copiaReservas.add(new Reserva(reserva));
		}
		return copiaReservas;
	}
	
	public List<Reserva> get(Profesor profesor) {
		if (profesor == null) {
			throw new NullPointerException("ERROR: El profesor no puede ser nulo.");
		}
		List<Reserva> reservasProfesor = new ArrayList<>();
		for (Reserva reserva : coleccionReservas) {
			if (reserva.getProfesor().equals(profesor)) {
				reservasProfesor.add(new Reserva(reserva));
			}
		}
		Comparator<Aula> comparadorAula = Comparator.comparing(Aula::getNombre);
		Comparator<Permanencia> comparadorPermanencia = (Permanencia p1, Permanencia p2) -> {
			int comparacion = -1;
			if (p1.getDia().equals(p2.getDia())) {
				if (p1 instanceof PermanenciaPorTramo && p2 instanceof PermanenciaPorTramo) {
					comparacion = Integer.compare(((PermanenciaPorTramo)p1).getTramo().ordinal(), ((PermanenciaPorTramo)p2).getTramo().ordinal());
				} else if (p1 instanceof PermanenciaPorHora && p2 instanceof PermanenciaPorHora) {
					comparacion = ((PermanenciaPorHora)p1).getHora().compareTo(((PermanenciaPorHora)p2).getHora());
				}
			} else {
				comparacion = p1.getDia().compareTo(p2.getDia());
			}
			return comparacion;
		};		
		reservasProfesor.sort(Comparator.comparing(Reserva::getAula, comparadorAula).thenComparing(Reserva::getPermanencia, comparadorPermanencia));
		return reservasProfesor;
	}
	
	public List<Reserva> get(Aula aula) {
		if (aula == null) {
			throw new NullPointerException("ERROR: El aula no puede ser nula.");
		}
		List<Reserva> reservasAula = new ArrayList<>();
		for (Reserva reserva : coleccionReservas) {
			if (reserva.getAula().equals(aula)) {
				reservasAula.add(new Reserva(reserva));
			}
		}
		Comparator<Permanencia> comparadorPermanencia = (Permanencia p1, Permanencia p2) -> {
			int comparacion = -1;
			if (p1.getDia().equals(p2.getDia())) {
				if (p1 instanceof PermanenciaPorTramo && p2 instanceof PermanenciaPorTramo) {
					comparacion = Integer.compare(((PermanenciaPorTramo)p1).getTramo().ordinal(), ((PermanenciaPorTramo)p2).getTramo().ordinal());
				} else if (p1 instanceof PermanenciaPorHora && p2 instanceof PermanenciaPorHora) {
					comparacion = ((PermanenciaPorHora)p1).getHora().compareTo(((PermanenciaPorHora)p2).getHora());
				}
			} else {
				comparacion = p1.getDia().compareTo(p2.getDia());
			}
			return comparacion;
		};
		reservasAula.sort(Comparator.comparing(Reserva::getPermanencia, comparadorPermanencia));
		return reservasAula;
	}
	
	public int getTamano() {
		return coleccionReservas.size();
	}
	
	public void insertar(Reserva reserva) throws OperationNotSupportedException {
		if (reserva == null) {
			throw new NullPointerException("ERROR: No se puede insertar una reserva nula.");
		}
		int indice = coleccionReservas.indexOf(reserva);
		if (indice == -1) {
			coleccionReservas.add(new Reserva(reserva));
		} else {
			throw new OperationNotSupportedException("ERROR: Ya existe una reserva igual.");
		}		
		
	}
	
	public Reserva buscar(Reserva reserva) {
		if (reserva == null) {
			throw new IllegalArgumentException("ERROR: No se puede buscar una reserva nula.");
		}
		int indice = coleccionReservas.indexOf(reserva);
		if (indice == -1) {
			return null;
		} else {
			return new Reserva(coleccionReservas.get(indice));
		}
	}
	
	public void borrar(Reserva reserva) throws OperationNotSupportedException {
		if (reserva == null) {
			throw new IllegalArgumentException("ERROR: No se puede borrar una reserva nula.");
		}
		int indice = coleccionReservas.indexOf(reserva);
		if (indice == -1) {
			throw new OperationNotSupportedException("ERROR: No existe ninguna reserva igual.");
		} else {
			coleccionReservas.remove(indice);
		}
	}

}
