package org.iesalandalus.programacion.reservasaulas.mvc.vista.iugpestanas.controladoresvistas;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.iesalandalus.programacion.reservasaulas.mvc.controlador.IControlador;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Permanencia;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.PermanenciaPorHora;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.PermanenciaPorTramo;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Reserva;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Tramo;
import org.iesalandalus.programacion.reservasaulas.mvc.vista.iugpestanas.utilidades.Dialogos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class ControladorRealizarReservaAula {
	
	private IControlador controladorMVC;
	private ControladorVentanaPrincipal padre;
	private ObservableList<Profesor> profesores = FXCollections.observableArrayList();
	private Aula aula;
	
    @FXML private ListView<Profesor> lvProfesor;
    @FXML private DatePicker dpDia;
    @FXML private RadioButton rbTramo;
    @FXML private RadioButton rbHora;
    @FXML private ComboBox<Tramo> cbTramo;
    @FXML private TextField tfHora;
    @FXML Button btAceptar;
    @FXML Button btCancelar;
    
    private ToggleGroup tgHoraTramo = new ToggleGroup();
    
    private class CeldaProfesor extends ListCell<Profesor> {
        @Override
        public void updateItem(Profesor profesor, boolean vacio) {
            super.updateItem(profesor, vacio);
            if (vacio) {
            	setText("");
            } else {
            	setText(profesor.getNombre());
            }
        }
    }
    
    @FXML
    private void initialize() {
    	cbTramo.setItems(FXCollections.observableArrayList(Tramo.values()));
    	rbTramo.setToggleGroup(tgHoraTramo);
    	rbHora.setToggleGroup(tgHoraTramo);
		tgHoraTramo.selectedToggleProperty().addListener((observable, oldValue, newValue) -> habilitaSeleccion());
		tfHora.setDisable(true);
		cbTramo.setDisable(true);
		lvProfesor.setItems(profesores);
		lvProfesor.setCellFactory(l -> new CeldaProfesor());
    }
    
    public void setControladorMVC(IControlador controladorMVC) {
		this.controladorMVC = controladorMVC;
	}
    
    public void setPadre(ControladorVentanaPrincipal padre) {
    	this.padre = padre;
    }
    
    public void setProfesores(ObservableList<Profesor> profesores) {
    	this.profesores.setAll(profesores);
    }
    
    public void setAula(Aula aula) {
    	this.aula = aula;
    }
    
    public void inicializa() {
    	lvProfesor.getSelectionModel().clearSelection();
    	dpDia.setValue(null);
    	rbHora.setSelected(false);
    	rbTramo.setSelected(false);
    	cbTramo.getSelectionModel().clearSelection();
    	tfHora.setText("");
    }
    
    private void habilitaSeleccion() {
		RadioButton seleccionado = (RadioButton)tgHoraTramo.getSelectedToggle();
		if (seleccionado == rbHora) {
			tfHora.setDisable(false);
			cbTramo.setDisable(true);
		} else {
			tfHora.setDisable(true);
			cbTramo.setDisable(false);
		}
    }

    @FXML
    void realizarReserva(ActionEvent event) {
    	Profesor profesor = lvProfesor.getSelectionModel().getSelectedItem();
    	LocalDate dia = dpDia.getValue();
    	RadioButton seleccionado = (RadioButton)tgHoraTramo.getSelectedToggle();
		try {
	    	Permanencia permanencia;
			if (seleccionado == rbHora) {
				permanencia = new PermanenciaPorHora(dia, LocalTime.parse(tfHora.getText(), DateTimeFormatter.ofPattern("HH:mm")));
			} else {
				permanencia = new PermanenciaPorTramo(dia, cbTramo.getValue());
			}
			Reserva reserva = new Reserva(profesor, aula, permanencia);
			controladorMVC.insertar(reserva);
			padre.mostrarReservasAula(aula);
			Stage propietario = ((Stage) btAceptar.getScene().getWindow());
			Dialogos.mostrarDialogoInformacion("Realizar Reserva", "Reserva realizada satisfactoriamente", propietario);
		} catch (Exception e) {
			Dialogos.mostrarDialogoError("Realizar Reserva", e.getMessage());
		}
    }
    
    @FXML
    void cancelar(ActionEvent event) {
		((Stage) btCancelar.getScene().getWindow()).close();
    }

}
