package org.iesalandalus.programacion.reservasaulas.mvc.vista.iugventanas.controladoresvistas;

import org.iesalandalus.programacion.reservasaulas.mvc.controlador.IControlador;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Profesor;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class ControladorListarProfesores {
	
	private IControlador controladorMVC;
	
	@FXML TableColumn<Profesor, String> tcNombre;
	@FXML TableColumn<Profesor, String> tcCorreo;
	@FXML TableColumn<Profesor, String> tcTelefono;
	@FXML TableView<Profesor> tvProfesores;
	@FXML Button btAceptar;
	
	public void setControladorMVC(IControlador controladorMVC) {
		this.controladorMVC = controladorMVC;
	}
	
	public void inicializa() {
		tvProfesores.setItems(FXCollections.observableArrayList(controladorMVC.getProfesores()));
	}

	@FXML
	private void initialize() {
		tcNombre.setCellValueFactory(profesor -> new SimpleStringProperty(profesor.getValue().getNombre()));
		tcCorreo.setCellValueFactory(profesor -> new SimpleStringProperty(profesor.getValue().getCorreo()));
		tcTelefono.setCellValueFactory(profesor -> new SimpleStringProperty(profesor.getValue().getTelefono()));
	}
	
	@FXML
	private void aceptar() {
		Stage escena = (Stage) btAceptar.getScene().getWindow();
	    escena.close();
	}

}
