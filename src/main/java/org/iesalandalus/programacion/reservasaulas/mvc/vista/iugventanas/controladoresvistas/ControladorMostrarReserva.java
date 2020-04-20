package org.iesalandalus.programacion.reservasaulas.mvc.vista.iugventanas.controladoresvistas;

import java.time.format.DateTimeFormatter;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.reservasaulas.mvc.controlador.IControlador;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Permanencia;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.PermanenciaPorHora;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.PermanenciaPorTramo;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Reserva;
import org.iesalandalus.programacion.reservasaulas.mvc.vista.iugventanas.utilidades.Dialogos;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ControladorMostrarReserva {
	
	private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	private IControlador controladorMVC;
	private Reserva reserva;
	
	@FXML Label lbNombreProfesor;
	@FXML Label lbCorreo;
	@FXML Label lbTelefono;
	@FXML Label lbNombreAula;
	@FXML Label lbPuestos;
	@FXML Label lbDia;
	@FXML Label lbHoraTramo;
	@FXML Label lbPuntos;
	@FXML Button btAceptar;
	@FXML Button btBorrar;

	public void setControladorMVC(IControlador controladorMVC) {
		this.controladorMVC = controladorMVC;
	}
	
	public void setReserva(Reserva reserva) {
		this.reserva = reserva;
		lbNombreProfesor.setText(reserva.getProfesor().getNombre());
		lbCorreo.setText(reserva.getProfesor().getCorreo());
		lbTelefono.setText(reserva.getProfesor().getTelefono());
		lbNombreAula.setText(reserva.getAula().getNombre());
		lbPuestos.setText(Integer.toString(reserva.getAula().getPuestos()));
		lbDia.setText(FORMATO_FECHA.format(reserva.getPermanencia().getDia()));
		lbHoraTramo.setText(getPermanenciaString(reserva));
		lbPuntos.setText(Float.toString(reserva.getPuntos()));
	}
	
	private String getPermanenciaString(Reserva reserva) {
		String horaOTramo;
		Permanencia permanencia = reserva.getPermanencia();
		if (permanencia instanceof PermanenciaPorHora) {
			horaOTramo = ((PermanenciaPorHora)permanencia).getHora().toString();
		} else {
			horaOTramo = ((PermanenciaPorTramo)permanencia).getTramo().toString();
		}
		return horaOTramo;
	}
	
	@FXML
	private void aceptar() {
		Stage escena = (Stage) btAceptar.getScene().getWindow();
	    escena.close();
	}
	
	@FXML
	private void borrar() {
		Stage propietario = (Stage) btBorrar.getScene().getWindow();
		try {
			controladorMVC.borrar(reserva);
		} catch (OperationNotSupportedException e) {
			Dialogos.mostrarDialogoError("Borrar Reserva", e.getMessage(), propietario);
		}
		Dialogos.mostrarDialogoInformacion("Borrar Reserva", "Reserva borrada satisfactoriamente", propietario);
	}

}
