package org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mysqlxdevapi;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Permanencia;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.PermanenciaPorHora;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.PermanenciaPorTramo;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Reserva;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Tramo;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.IReservas;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mysqlxdevapi.utilidades.SesionBD;

import com.mysql.cj.protocol.x.XProtocolError;
import com.mysql.cj.xdevapi.Row;
import com.mysql.cj.xdevapi.RowResult;
import com.mysql.cj.xdevapi.Table;

public class Reservas implements IReservas {
	
	private static final float MAX_PUNTOS_PROFESOR_MES = 200.0f;
	private static final String TABLA_AULAS = "Aulas";
	private static final String NOMBRE = "nombre";
	private static final String PUESTOS = "puestos";
	private static final String TABLA_PROFESORES = "Profesores";
	private static final String CORREO = "correo";
	private static final String TELEFONO = "telefono";
	private static final String TABLA_RESERVAS = "Reservas";
	private static final String ID_PROFESOR = "idProfesor";
	private static final String ID_AULA = "idAula";
	private static final String DIA = "dia";
	private static final String TIPO = "tipo";
	private static final String TRAMO = "tramo";
	private static final String HORA = "hora";
	
	private Table tablaAulas = null;
	private Table tablaProfesores = null;
	private Table tablaReservas = null;

	@Override
	public void comenzar() {
		tablaAulas = SesionBD.getEsquema().getTable(TABLA_AULAS);
		tablaProfesores = SesionBD.getEsquema().getTable(TABLA_PROFESORES);
		tablaReservas = SesionBD.getEsquema().getTable(TABLA_RESERVAS);
	}

	@Override
	public void terminar() {
		SesionBD.cierraSesion();
	}

	@Override
	public List<Reserva> get() {
		RowResult filas = tablaReservas.select("*").execute();
		filas.fetchAll();
		List<Reserva> reservas = new ArrayList<>();
		for (Row fila : filas) {
			Reserva reserva = getReservaDesdeFila(fila);
			reservas.add(reserva);
		}
		return ordenar(reservas);
	}

	@Override
	public List<Reserva> get(Profesor profesor) {
		RowResult filas = tablaReservas.select("*").where("idProfesor = :idProfesor").bind(ID_PROFESOR, getIdDesdeProfesor(profesor)).execute();
		filas.fetchAll();
		List<Reserva> reservas = new ArrayList<>();
		for (Row fila : filas) {
			Reserva reserva = getReservaDesdeFila(fila);
			reservas.add(reserva);
		}
		return ordenar(reservas);
	}

	@Override
	public List<Reserva> get(Aula aula) {
		RowResult filas = tablaReservas.select("*").where("idAula = :idAula").bind(ID_AULA, getIdDesdeAula(aula)).execute();
		filas.fetchAll();
		List<Reserva> reservas = new ArrayList<>();
		for (Row fila : filas) {
			Reserva reserva = getReservaDesdeFila(fila);
			reservas.add(reserva);
		}
		return ordenar(reservas);
	}
	
	private Reserva getReservaDesdeFila(Row fila) {
		int idProfesor = fila.getInt(ID_PROFESOR);
		int idAula = fila .getInt(ID_AULA);
		Date dia = fila.getDate(DIA);
		String tipo = fila.getString(TIPO);
		String tramo = fila.getString(TRAMO);
		Time hora = fila.getTime(HORA);
		Permanencia permanencia = null;
		if (tipo.equals("TRAMO")) {
			permanencia = new PermanenciaPorTramo(dia.toLocalDate(), Tramo.valueOf(tramo));
		} else if (tipo.equals("HORA")) {
			permanencia = new PermanenciaPorHora(dia.toLocalDate(), hora.toLocalTime());
		}
		return new Reserva(getProfesorDesdeId(idProfesor), getAulaDesdeId(idAula), permanencia);
	}
	
	private Profesor getProfesorDesdeId(int idProfesor) {
		RowResult filas = tablaProfesores.select("*")
				.where("idProfesor = :idProfesor").bind(ID_PROFESOR, idProfesor).execute();
		Row fila = filas.fetchOne();
		Profesor profesor = null;
		if (fila != null) {
			String nombre = fila.getString(NOMBRE);
			String correo = fila.getString(CORREO);
			String telefono = fila.getString(TELEFONO);
			profesor = new Profesor(nombre, correo, telefono);
		}
		return profesor;
	}
	
	private int getIdDesdeProfesor(Profesor profesor) {
		RowResult filas = tablaProfesores.select(ID_PROFESOR)
				.where("correo = :correo").bind(CORREO, profesor.getCorreo()).execute();
		Row fila = filas.fetchOne();
		int idProfesor = 0;
		if (fila != null) {
			idProfesor = fila.getInt(ID_PROFESOR);
		}
		return idProfesor;
	}
	
	private Aula getAulaDesdeId(int idAula) {
		RowResult filas = tablaAulas.select("*")
				.where("idAula = :idAula").bind(ID_AULA, idAula).execute();
		Row fila = filas.fetchOne();
		Aula aula = null;
		if (fila != null) {
			String nombre = fila.getString(NOMBRE);
			int puestos = fila.getInt(PUESTOS);
			aula = new Aula(nombre, puestos);
		}
		return aula;
	}
	
	private int getIdDesdeAula(Aula aula) {
		RowResult filas = tablaAulas.select(ID_AULA)
				.where("nombre = :nombre").bind(NOMBRE, aula.getNombre()).execute();
		Row fila = filas.fetchOne();
		int idAula = 0;
		if (fila != null) {
			idAula = fila.getInt(ID_AULA);
		}
		return idAula;
	}
	
	private List<Reserva> ordenar(List<Reserva> reservas) {
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
		reservas.sort(Comparator.comparing(Reserva::getAula, comparadorAula).thenComparing(Reserva::getPermanencia, comparadorPermanencia));
		return reservas;
	}

	@Override
	public int getTamano() {
		return (int)tablaReservas.count();
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
		try {
			Permanencia permanencia = reserva.getPermanencia();
			Date dia = Date.valueOf(permanencia.getDia());
			String tipo = null;
			String tramo = "MANANA";
			Time hora = Time.valueOf("00:00:00");
			if (permanencia instanceof PermanenciaPorTramo) {
				tipo = "TRAMO";
				tramo = ((PermanenciaPorTramo)permanencia).getTramo().name();
			} else {
				tipo = "HORA";
				hora = Time.valueOf(((PermanenciaPorHora)permanencia).getHora());
			}
			tablaReservas.insert(ID_PROFESOR, ID_AULA, DIA, TIPO, TRAMO, HORA)
			.values(getIdDesdeProfesor(reserva.getProfesor()), getIdDesdeAula(reserva.getAula()), dia, tipo, tramo, hora).execute();
		} catch (XProtocolError e) {
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
		return null;
	}

	@Override
	public void borrar(Reserva reserva) throws OperationNotSupportedException {
	}

}
