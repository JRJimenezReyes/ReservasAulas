package org.iesalandalus.programacion.reservasaulas.mvc.vista.iugventanas.controladoresvistas;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.reservasaulas.mvc.controlador.IControlador;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.mvc.vista.iugventanas.utilidades.Dialogos;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ControladorMostrarProfesor {
	
	private IControlador controladorMVC;
	private Profesor profesor;
	
	@FXML Label lbNombre;
	@FXML Label lbCorreo;
	@FXML Label lbTelefono;
	@FXML Button btAceptar;
	@FXML Button btBorrar;

	public void setControladorMVC(IControlador controladorMVC) {
		this.controladorMVC = controladorMVC;
	}
	
	public void setProfesor(Profesor profesor) {
		this.profesor = profesor;
		lbNombre.setText(profesor.getNombre());
		lbCorreo.setText(profesor.getCorreo());
		lbTelefono.setText(profesor.getTelefono());
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
			controladorMVC.borrar(profesor);
		} catch (OperationNotSupportedException e) {
			Dialogos.mostrarDialogoError("Borrar Profesor", e.getMessage(), propietario);
		}
		Dialogos.mostrarDialogoInformacion("Borrar Profesor", "Profesor borrado satisfactoriamente", propietario);
	}

}
