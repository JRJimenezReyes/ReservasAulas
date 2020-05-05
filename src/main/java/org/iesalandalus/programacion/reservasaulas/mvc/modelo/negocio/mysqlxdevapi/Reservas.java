package org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mysqlxdevapi;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Permanencia;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.PermanenciaPorHora;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.PermanenciaPorTramo;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Reserva;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.IReservas;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mysqlxdevapi.utilidades.SesionBD;

import com.mysql.cj.exceptions.WrongArgumentException;
import com.mysql.cj.xdevapi.Collection;
import com.mysql.cj.xdevapi.DocResult;
import com.mysql.cj.xdevapi.FindStatement;
import com.mysql.cj.xdevapi.RemoveStatement;

public class Reservas implements IReservas {
	
	private static final float MAX_PUNTOS_PROFESOR_MES = 200.0f;
	
	private static final String COLECCION = "ReservasX";
	private static final String NOMBRE_AULA = "aula.nombre";
	private static final String NOMBRE = "nombre";
	private static final String CORREO = "correo";
	private static final String DIA = "dia";
	private static final String TRAMO = "tramo";
	private static final String HORA = "hora";
	private static final String BUSQUEDA = "aula.nombre = :nombre and dia = :dia";
	private static final String POR_TRAMO = " and tramo = :tramo";
	private static final String POR_HORA = " and hora = :hora";
	
	private Collection coleccionReservas = null;

	@Override
	public void comenzar() {
		try {
			coleccionReservas = SesionBD.getEsquema().getCollection(COLECCION, true);
		} catch (WrongArgumentException e) {
			SesionBD.getEsquema().createCollection(COLECCION);
			coleccionReservas = SesionBD.getEsquema().getCollection(COLECCION, true);
		}	
	}

	@Override
	public void terminar() {
		SesionBD.cerrarSesion();
	}

	@Override
	public List<Reserva> get() {
		List<Reserva> reservas = new ArrayList<>();
		DocResult documentos = coleccionReservas.find().orderBy(NOMBRE_AULA, DIA, TRAMO, HORA).execute();
		while (documentos.hasNext()) {
			reservas.add(SesionBD.getReserva(documentos.next()));
		}
		return reservas;
	}

	@Override
	public List<Reserva> get(Profesor profesor) {
		List<Reserva> reservas = new ArrayList<>();
		DocResult documentos = coleccionReservas.find("profesor.correo = :correo")
				.bind(CORREO, profesor.getCorreo())
				.orderBy(NOMBRE_AULA, DIA, TRAMO, HORA).execute();
		while (documentos.hasNext()) {
			reservas.add(SesionBD.getReserva(documentos.next()));
		}
		return reservas;
	}

	@Override
	public List<Reserva> get(Aula aula) {
		List<Reserva> reservas = new ArrayList<>();
		DocResult documentos = coleccionReservas.find("aula.nombre = :nombre")
				.bind(NOMBRE, aula.getNombre())
				.orderBy(NOMBRE_AULA, DIA, TRAMO, HORA).execute();
		while (documentos.hasNext()) {
			reservas.add(SesionBD.getReserva(documentos.next()));
		}
		return reservas;
	}

	@Override
	public int getTamano() {
		return (int)coleccionReservas.count();
	}

	@Override
	public void insertar(Reserva reserva) throws OperationNotSupportedException {
		if (reserva == null) {
			throw new NullPointerException("ERROR: No se puede insertar una reserva nula.");
		}
		Reserva reservaExistente = getReservaAulaDia(reserva.getAula(), reserva.getPermanencia().getDia());
		if (reservaExistente != null) { 
			if (reservaExistente.getPermanencia() instanceof PermanenciaPorTramo &&
					reserva.getPermanencia() instanceof PermanenciaPorHora) {
				throw new OperationNotSupportedException("ERROR: Ya se ha realizado una reserva de otro tipo de permanencia para este día.");
			}
			if (reservaExistente.getPermanencia() instanceof PermanenciaPorHora &&
					reserva.getPermanencia() instanceof PermanenciaPorTramo) {
				throw new OperationNotSupportedException("ERROR: Ya se ha realizado una reserva de otro tipo de permanencia para este día.");
			}
		}
		if (!esMesSiguienteOPosterior(reserva)) {
			throw new OperationNotSupportedException("ERROR: Sólo se pueden hacer reservas para el mes que viene o posteriores.");
		}
		if (getPuntosGastadosReserva(reserva) > MAX_PUNTOS_PROFESOR_MES) {
			throw new OperationNotSupportedException("ERROR: Esta reserva excede los puntos máximos por mes para dicho profesor.");
		}
		if (buscar(reserva) == null) {
			coleccionReservas.add(SesionBD.getDocumento(reserva)).execute();
		} else {
			throw new OperationNotSupportedException("ERROR: Ya existe una reserva igual.");
		}
	}
	
	
	private boolean esMesSiguienteOPosterior(Reserva reserva) {
		LocalDate diaReserva = reserva.getPermanencia().getDia();
		LocalDate dentroDeUnMes = LocalDate.now().plusMonths(1);
		LocalDate primerDiaMesSiguiente = LocalDate.of(dentroDeUnMes.getYear(), dentroDeUnMes.getMonth(), 1);
		return diaReserva.isAfter(primerDiaMesSiguiente) || diaReserva.equals(primerDiaMesSiguiente);
	}
	
	private float getPuntosGastadosReserva(Reserva reserva) {
		float puntosGastados = 0;
		for (Reserva reservaProfesor : getReservasProfesorMes(reserva.getProfesor(), reserva.getPermanencia().getDia())) {
			puntosGastados += reservaProfesor.getPuntos();
		}
		return puntosGastados + reserva.getPuntos();
	}
	
	private List<Reserva> getReservasProfesorMes(Profesor profesor, LocalDate mes) {
		if (profesor == null) {
			throw new NullPointerException("No se pueden buscar reservas de un profesor nulo.");
		}
		List<Reserva> reservasProfesor = new ArrayList<>();
		for (Reserva reserva : get(profesor)) {
			LocalDate diaReserva = reserva.getPermanencia().getDia();
			if (reserva.getProfesor().equals(profesor) && 
					diaReserva.getMonthValue() == mes.getMonthValue() &&
					diaReserva.getYear() == mes.getYear()) {
				reservasProfesor.add(new Reserva(reserva));
			}
		}
		return reservasProfesor;
	}
	
	private Reserva getReservaAulaDia(Aula aula, LocalDate dia) {
		if (dia == null) {
			throw new NullPointerException("No se puede buscar reserva para un día nulo.");
		}
		for (Reserva reserva : get(aula)) {
			LocalDate diaReserva = reserva.getPermanencia().getDia();
			Aula aulaReserva = reserva.getAula();
			if (diaReserva.equals(dia) && aulaReserva.equals(aula)) {
				return reserva;
			}
		}
		return null;
	}
	
	@Override
	public Reserva buscar(Reserva reserva) {
		Permanencia permanencia = reserva.getPermanencia();
		String dia = permanencia.getDia().toString();
		String tramo = "";
		String hora = "";
		FindStatement busqueda = null;
		if (permanencia instanceof PermanenciaPorTramo) {
			tramo = ((PermanenciaPorTramo)permanencia).getTramo().name();
			busqueda = coleccionReservas.find(BUSQUEDA + POR_TRAMO)
					.bind(NOMBRE, reserva.getAula().getNombre())
					.bind(DIA, dia)
					.bind(TRAMO, tramo);
		} else {
			hora = ((PermanenciaPorHora)permanencia).getHora().toString();
			busqueda = coleccionReservas.find(BUSQUEDA + POR_HORA)
					.bind(NOMBRE, reserva.getAula().getNombre())
					.bind(DIA, dia)
					.bind(HORA, hora);
		}
		DocResult documentos = busqueda.execute();
		return documentos.hasNext() ? SesionBD.getReserva(documentos.next()) : null;
	}

	@Override
	public void borrar(Reserva reserva) throws OperationNotSupportedException {
		if (reserva == null) {
			throw new IllegalArgumentException("ERROR: No se puede borrar una reserva nula.");
		}
		if (!esMesSiguienteOPosterior(reserva)) {
			throw new OperationNotSupportedException("ERROR: Sólo se pueden anular reservas para el mes que viene o posteriores.");
		}
		if (buscar(reserva) == null) {
			throw new OperationNotSupportedException("ERROR: No existe ninguna reserva igual.");
		} else {
			Permanencia permanencia = reserva.getPermanencia();
			String dia = permanencia.getDia().toString();
			String tramo = "";
			String hora = "";
			RemoveStatement borrado = null;
			if (permanencia instanceof PermanenciaPorTramo) {
				tramo = ((PermanenciaPorTramo)permanencia).getTramo().name();
				borrado = coleccionReservas.remove(BUSQUEDA + POR_TRAMO)
						.bind(NOMBRE, reserva.getAula().getNombre())
						.bind(DIA, dia)
						.bind(TRAMO, tramo);
			} else {
				hora = ((PermanenciaPorHora)permanencia).getHora().toString();
				borrado = coleccionReservas.remove(BUSQUEDA + POR_HORA)
						.bind(NOMBRE, reserva.getAula().getNombre())
						.bind(DIA, dia)
						.bind(HORA, hora);
			}
			borrado.execute();
		}
	}

}
