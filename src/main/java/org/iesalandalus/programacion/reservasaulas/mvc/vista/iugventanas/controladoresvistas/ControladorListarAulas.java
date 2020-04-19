package org.iesalandalus.programacion.reservasaulas.mvc.vista.iugventanas.controladoresvistas;

import org.iesalandalus.programacion.reservasaulas.mvc.controlador.IControlador;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class ControladorListarAulas {
	
	private IControlador controladorMVC;
	
	@FXML TableColumn<Aula, String> tcNombre;
	@FXML TableColumn<Aula, String> tcPuestos;
	@FXML TableView<Aula> tvAulas;
	@FXML Button btAceptar;
	
	public void setControladorMVC(IControlador controladorMVC) {
		this.controladorMVC = controladorMVC;
	}
	
	public void inicializa() {
		tvAulas.setItems(FXCollections.observableArrayList(controladorMVC.getAulas()));
	}

	@FXML
	private void initialize() {
		tcNombre.setCellValueFactory(aula -> new SimpleStringProperty(aula.getValue().getNombre()));
		tcPuestos.setCellValueFactory(aula -> new SimpleStringProperty(Integer.toString(aula.getValue().getPuestos())));
	}
	
	@FXML
	private void aceptar() {
		Stage escena = (Stage) btAceptar.getScene().getWindow();
	    escena.close();
	}

}
