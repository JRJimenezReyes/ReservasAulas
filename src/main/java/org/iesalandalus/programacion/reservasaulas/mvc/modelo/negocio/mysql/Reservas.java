package org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mysql;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.sql.Time;
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
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Tramo;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.IReservas;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.mysql.utilidades.MySQL;

public class Reservas implements IReservas {
	
	private static final String HORA_NULA = "00:00:00";
	private static final String HORA = "HORA";
	private static final String TRAMO = "TRAMO";
	private static final String SELECT_RESERVAS = 
			"select Profesores.nombre, correo, telefono, Aulas.nombre, puestos, dia, tipo, tramo, hora "
			+ "from Profesores, Aulas, Reservas "
			+ "where Profesores.idProfesor = Reservas.idProfesor and Aulas.idAula = Reservas.idAula";
	private static final String ORDEN_RESERVAS = " order by Aulas.nombre, dia, tramo, hora";
	private static final float MAX_PUNTOS_PROFESOR_MES = 200.0f;
	private static final String ERROR = "ERROR: ";
	
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
		List<Reserva> reservas = new ArrayList<>();
		try {
			String sentenciaStr = SELECT_RESERVAS + ORDEN_RESERVAS;
			Statement sentencia = conexion.createStatement();
			ResultSet filas = sentencia.executeQuery(sentenciaStr);
			while (filas.next()) {
				String nombreProfesor = filas.getString(1);
				String correo = filas.getString(2);
				String telefono = filas.getString(3);
				String nombreAula = filas.getString(4);
				int puestos = filas.getInt(5);
				LocalDate dia = filas.getDate(6).toLocalDate();
				String tipo = filas.getString(7);
				String tramo = filas.getString(8);
				Time hora = filas.getTime(9);
				
				Profesor profesor = new Profesor(nombreProfesor, correo, telefono);
				Aula aula = new Aula(nombreAula, puestos);
				Reserva reserva = getReserva(dia, tipo, tramo, hora, profesor, aula);
				reservas.add(reserva);
			}
		} catch (SQLException e) {
			throw new IllegalArgumentException(ERROR + e.getMessage());
		}
		return reservas;
	}

	private Reserva getReserva(LocalDate dia, String tipo, String tramo, Time hora, Profesor profesor, Aula aula) {
		Reserva reserva = null;
		Permanencia permanencia = null;
		if (tipo.equals(TRAMO)) {
			permanencia = new PermanenciaPorTramo(dia, Tramo.valueOf(tramo));
			reserva = new Reserva(profesor, aula, permanencia);
		} else if (tipo.equals(HORA)) {
			permanencia = new PermanenciaPorHora(dia, hora.toLocalTime());
			reserva = new Reserva(profesor, aula, permanencia);
		}
		return reserva;
	}
	
	@Override
	public List<Reserva> get(Profesor profesor) {
		if (profesor == null) {
			throw new NullPointerException("ERROR: El profesor no puede ser nulo.");
		}
		List<Reserva> reservas = new ArrayList<>();
		try {
			String sentenciaStr = SELECT_RESERVAS + " and Profesores.correo = ?" + ORDEN_RESERVAS;
			PreparedStatement sentencia = conexion.prepareStatement(sentenciaStr);
			sentencia.setString(1, profesor.getCorreo());
			ResultSet filas = sentencia.executeQuery();
			while (filas.next()) {
				String nombreProfesor = filas.getString(1);
				String correo = filas.getString(2);
				String telefono = filas.getString(3);
				String nombreAula = filas.getString(4);
				int puestos = filas.getInt(5);
				LocalDate dia = filas.getDate(6).toLocalDate();
				String tipo = filas.getString(7);
				String tramo = filas.getString(8);
				Time hora = filas.getTime(9);
				
				profesor = new Profesor(nombreProfesor, correo, telefono);
				Aula aula = new Aula(nombreAula, puestos);
				Reserva reserva = getReserva(dia, tipo, tramo, hora, profesor, aula);
				reservas.add(reserva);
			}
		} catch (SQLException e) {
			throw new IllegalArgumentException(ERROR + e.getMessage());
		}
		return reservas;
	}
	
	@Override
	public List<Reserva> get(Aula aula) {
		if (aula == null) {
			throw new NullPointerException("ERROR: El aula no puede ser nula.");
		}
		List<Reserva> reservas = new ArrayList<>();
		try {
			String sentenciaStr = SELECT_RESERVAS + " and Aulas.nombre = ?" + ORDEN_RESERVAS;
			PreparedStatement sentencia = conexion.prepareStatement(sentenciaStr);
			sentencia.setString(1, aula.getNombre());
			ResultSet filas = sentencia.executeQuery();
			while (filas.next()) {
				String nombreProfesor = filas.getString(1);
				String correo = filas.getString(2);
				String telefono = filas.getString(3);
				String nombreAula = filas.getString(4);
				int puestos = filas.getInt(5);
				LocalDate dia = filas.getDate(6).toLocalDate();
				String tipo = filas.getString(7);
				String tramo = filas.getString(8);
				Time hora = filas.getTime(9);
				
				Profesor profesor = new Profesor(nombreProfesor, correo, telefono);
				aula = new Aula(nombreAula, puestos);
				Reserva reserva = getReserva(dia, tipo, tramo, hora, profesor, aula);
				reservas.add(reserva);
			}
		} catch (SQLException e) {
			throw new IllegalArgumentException(ERROR + e.getMessage());
		}
		return reservas;
	}
	
	@Override
	public int getTamano() {
		int tamano = 0;
		try {
			String sentenciaStr = "select count(*) from Reservas";
			Statement sentencia = conexion.createStatement();
			ResultSet filas = sentencia.executeQuery(sentenciaStr);
			if (filas.next()) {
				tamano = filas.getInt(1);
			}
		} catch (SQLException e) {
			throw new IllegalArgumentException(ERROR + e.getMessage());
		}
		return tamano;
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
		int idAula = getIdAula(reserva);
		int idProfesor = getIdProfesor(reserva);
		try {
			String sentenciaStr = "insert into Reservas values (?, ?, ?, ?, ?, ?)";
			PreparedStatement sentencia = conexion.prepareStatement(sentenciaStr);
			sentencia.setInt(1, idProfesor);
			sentencia.setInt(2, idAula);
			sentencia.setDate(3, Date.valueOf(reserva.getPermanencia().getDia()), java.util.Calendar.getInstance());
			Permanencia permanencia = reserva.getPermanencia();
			if (permanencia instanceof PermanenciaPorTramo) {
				sentencia.setString(4, TRAMO);
				sentencia.setString(5, ((PermanenciaPorTramo)permanencia).getTramo().name());
				sentencia.setTime(6, Time.valueOf(HORA_NULA));
			} else {
				sentencia.setString(4, HORA);
				sentencia.setString(5, Tramo.MANANA.name());
				sentencia.setTime(6, Time.valueOf(((PermanenciaPorHora)permanencia).getHora()));
			}
			sentencia.setString(4, reserva.getPermanencia() instanceof PermanenciaPorTramo ? TRAMO : HORA);
			
			sentencia.executeUpdate();
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new OperationNotSupportedException("ERROR: Ya existe una reserva igual.");
		} catch (SQLException e) {
			throw new OperationNotSupportedException(ERROR + e.getMessage());
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

	private int getIdProfesor(Reserva reserva) {
		int idProfesor = 0;
		try {
			String sentenciaStr = "select idProfesor from Profesores where correo=?";
			PreparedStatement sentencia = conexion.prepareStatement(sentenciaStr);
			sentencia.setString(1, reserva.getProfesor().getCorreo());
			ResultSet filas = sentencia.executeQuery();
			if (filas.next()) {
				idProfesor = filas.getInt(1);
			}
		} catch (SQLException e) {
			throw new IllegalArgumentException(ERROR + e.getMessage());
		}
		return idProfesor;
	}

	private int getIdAula(Reserva reserva) {
		int idAula = 0;
		try {
			String sentenciaStr = "select idAula from Aulas where nombre=?";
			PreparedStatement sentencia = conexion.prepareStatement(sentenciaStr);
			sentencia.setString(1, reserva.getAula().getNombre());
			ResultSet filas = sentencia.executeQuery();
			if (filas.next()) {
				idAula = filas.getInt(1);
			}
		} catch (SQLException e) {
			throw new IllegalArgumentException(ERROR + e.getMessage());
		}
		return idAula;
	}
	
	@Override
	public Reserva buscar(Reserva reserva) {
		if (reserva == null) {
			throw new IllegalArgumentException("ERROR: No se puede buscar una reserva nula.");
		}
		try {
			String sentenciaStr = SELECT_RESERVAS + " and Aulas.nombre = ? and Profesores.correo = ? and dia = ? and tipo = ? and tramo = ? and hora = ?";
			PreparedStatement sentencia = conexion.prepareStatement(sentenciaStr);
			sentencia.setString(1, reserva.getAula().getNombre());
			sentencia.setString(2, reserva.getProfesor().getCorreo());
			sentencia.setDate(3, Date.valueOf(reserva.getPermanencia().getDia()), java.util.Calendar.getInstance());
			Permanencia permanencia = reserva.getPermanencia();
			if (permanencia instanceof PermanenciaPorTramo) {
				sentencia.setString(4, TRAMO);
				sentencia.setString(5, ((PermanenciaPorTramo)permanencia).getTramo().name());
				sentencia.setTime(6, Time.valueOf(HORA_NULA));
			} else {
				sentencia.setString(4, HORA);
				sentencia.setString(5, Tramo.MANANA.name());
				sentencia.setTime(6, Time.valueOf(((PermanenciaPorHora)permanencia).getHora()));
			}
			ResultSet filas = sentencia.executeQuery();
			if (filas.next()) {
				String nombreProfesor = filas.getString(1);
				String correo = filas.getString(2);
				String telefono = filas.getString(3);
				String nombreAula = filas.getString(4);
				int puestos = filas.getInt(5);
				LocalDate dia = filas.getDate(6).toLocalDate();
				String tipo = filas.getString(7);
				String tramo = filas.getString(8);
				Time hora = filas.getTime(9);
				
				Profesor profesor = new Profesor(nombreProfesor, correo, telefono);
				Aula aula = new Aula(nombreAula, puestos);
				reserva = getReserva(dia, tipo, tramo, hora, profesor, aula);
			}
		} catch (SQLException e) {
			throw new IllegalArgumentException(ERROR + e.getMessage());
		}
		return reserva;
	}
	
	@Override
	public void borrar(Reserva reserva) throws OperationNotSupportedException {
		if (reserva == null) {
			throw new IllegalArgumentException("ERROR: No se puede borrar una reserva nula.");
		}
		if (!esMesSiguienteOPosterior(reserva)) {
			throw new OperationNotSupportedException("ERROR: Sólo se pueden anular reservas para el mes que viene o posteriores.");
		}
		try {
			String sentenciaStr = "delete from Reservas where idAula = ? and idProfesor = ? and dia = ? and tipo = ? and tramo = ? and hora = ?";
			PreparedStatement sentencia = conexion.prepareStatement(sentenciaStr);
			sentencia.setInt(1, getIdAula(reserva));
			sentencia.setInt(2, getIdProfesor(reserva));
			sentencia.setDate(3, Date.valueOf(reserva.getPermanencia().getDia()), java.util.Calendar.getInstance());
			Permanencia permanencia = reserva.getPermanencia();
			if (permanencia instanceof PermanenciaPorTramo) {
				sentencia.setString(4, TRAMO);
				sentencia.setString(5, ((PermanenciaPorTramo) permanencia).getTramo().name());
				sentencia.setTime(6, Time.valueOf(HORA_NULA));
			} else {
				sentencia.setString(4, HORA);
				sentencia.setString(5, Tramo.MANANA.name());
				sentencia.setTime(6, Time.valueOf(((PermanenciaPorHora) permanencia).getHora()));
			}
			sentencia.executeUpdate();
		} catch (SQLException e) {
			throw new OperationNotSupportedException(ERROR + e.getMessage());
		}
	}

}
