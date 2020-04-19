package org.iesalandalus.programacion.reservasaulas.mvc.vista.iugventanas.controladoresvistas;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.reservasaulas.mvc.controlador.IControlador;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.mvc.vista.iugventanas.utilidades.Dialogos;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ControladorMostrarAula {
	
	private IControlador controladorMVC;
	private Aula aula;
	
	@FXML Label lbNombre;
	@FXML Label lbPuestos;
	@FXML Button btAceptar;
	@FXML Button btBorrar;

	public void setControladorMVC(IControlador controladorMVC) {
		this.controladorMVC = controladorMVC;
	}
	
	public void setAula(Aula aula) {
		this.aula = aula;
		lbNombre.setText(aula.getNombre());
		lbPuestos.setText(Integer.toString(aula.getPuestos()));
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
			controladorMVC.borrar(aula);
		} catch (OperationNotSupportedException e) {
			Dialogos.mostrarDialogoError("Borrar Aula", e.getMessage(), propietario);
		}
		Dialogos.mostrarDialogoInformacion("Borrar Aula", "Aula borrada satisfactoriamente", propietario);
	}

}
