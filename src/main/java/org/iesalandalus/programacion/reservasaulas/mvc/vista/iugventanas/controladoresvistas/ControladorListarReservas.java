package org.iesalandalus.programacion.reservasaulas.mvc.vista.iugventanas.controladoresvistas;

import java.time.format.DateTimeFormatter;

import org.iesalandalus.programacion.reservasaulas.mvc.controlador.IControlador;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Permanencia;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.PermanenciaPorHora;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.PermanenciaPorTramo;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Reserva;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ControladorListarReservas {
	
 	private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	private IControlador controladorMVC;
	
	@FXML private TableView<Reserva> tvReservas;
    @FXML private TableColumn<Reserva, String> tcProfesor;
    @FXML private TableColumn<Reserva, String> tcAula;
    @FXML private TableColumn<Reserva, String> tcDia;
    @FXML private TableColumn<Reserva, String> tcHoraTramo;
    @FXML private TableColumn<Reserva, String> tcPuntos;
	@FXML Button btAceptar;
	
	public void setControladorMVC(IControlador controladorMVC) {
		this.controladorMVC = controladorMVC;
	}
	
	public void inicializa() {
    	tvReservas.setItems(FXCollections.observableList(controladorMVC.getReservas()));
	}

	@FXML
	private void initialize() {
		tcProfesor.setCellValueFactory(reserva -> new SimpleStringProperty(reserva.getValue().getProfesor().getNombre()));
    	tcAula.setCellValueFactory(reserva -> new SimpleStringProperty(reserva.getValue().getAula().getNombre()));
    	tcDia.setCellValueFactory(reserva -> new SimpleStringProperty(FORMATO_FECHA.format(reserva.getValue().getPermanencia().getDia())));
    	tcHoraTramo.setCellValueFactory(reserva -> new SimpleStringProperty(getPermanenciaString(reserva.getValue())));
    	tcPuntos.setCellValueFactory(new PropertyValueFactory<>("puntos"));
	}
	
	@FXML
	private void aceptar() {
		Stage escena = (Stage) btAceptar.getScene().getWindow();
	    escena.close();
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

}
