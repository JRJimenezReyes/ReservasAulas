package org.iesalandalus.programacion.reservasaulas.mvc.vista.iugpestanas.controladoresvistas;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

import org.iesalandalus.programacion.reservasaulas.mvc.controlador.IControlador;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Permanencia;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.PermanenciaPorHora;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.PermanenciaPorTramo;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Reserva;
import org.iesalandalus.programacion.reservasaulas.mvc.vista.iugpestanas.utilidades.Dialogos;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ControladorVentanaPrincipal {
	
	private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	private IControlador controladorMVC;
	
    private ObservableList<Profesor> profesores = FXCollections.observableArrayList();
    private ObservableList<Reserva> reservasProfesor = FXCollections.observableArrayList();
    private ObservableList<Aula> aulas = FXCollections.observableArrayList();
    private ObservableList<Reserva> reservasAula = FXCollections.observableArrayList();
	
	public void setControladorMVC(IControlador controladorMVC) {
		this.controladorMVC = controladorMVC;
	}
	
    @FXML private TableView<Profesor> tvProfesores;
	@FXML private TableColumn<Profesor, String> tcCorreo;
    @FXML private TableColumn<Profesor, String> tcTelefono;
    @FXML private TableColumn<Profesor, String> tcNombreProfesor;
    
    @FXML private TableView<Reserva> tvReservasProfesor;
    @FXML private TableColumn<Reserva, String> tcRPAula;
    @FXML private TableColumn<Reserva, String> tcRPDia;
    @FXML private TableColumn<Reserva, String> tcRPHoraTramo;
    @FXML private TableColumn<Reserva, String> tcRPPuntos;
    
    @FXML private TableView<Aula> tvAulas;
    @FXML private TableColumn<Aula, String> tcNombreAula;
    @FXML private TableColumn<Aula, String> tcPuestos;
    
    @FXML private TableView<Reserva> tvReservasAula;
    @FXML private TableColumn<Reserva, String> tcRAProfesor;
    @FXML private TableColumn<Reserva, String> tcRADia;
    @FXML private TableColumn<Reserva, String> tcRAHoraTramo;
    @FXML private TableColumn<Reserva, String> tcRAPuntos;
    
    private Stage anadirProfesor;
    private ControladorAnadirProfesor cAnadirProfesor;
    private Stage realizarReservaProfesor;
	private ControladorRealizarReservaProfesor cRealizarReservaProfesor;
	private Stage anadirAula;
	private ControladorAnadirAula cAnadirAula;
	private Stage realizarReservaAula;
	private ControladorRealizarReservaAula cRealizarReservaAula;
	
    @FXML
    private void initialize() {
    	tcNombreProfesor.setCellValueFactory(new PropertyValueFactory<>("nombre"));
    	tcCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
    	tcTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
    	tvProfesores.setItems(profesores);
    	tvProfesores.getSelectionModel().selectedItemProperty().addListener((ob, ov, nv) -> mostrarReservasProfesor(nv));
    	
    	tcRPAula.setCellValueFactory(reserva -> new SimpleStringProperty(reserva.getValue().getAula().getNombre()));
    	tcRPDia.setCellValueFactory(reserva -> new SimpleStringProperty(FORMATO_FECHA.format(reserva.getValue().getPermanencia().getDia())));
    	tcRPHoraTramo.setCellValueFactory(reserva -> new SimpleStringProperty(getPermanenciaString(reserva.getValue())));
    	tcRPPuntos.setCellValueFactory(new PropertyValueFactory<>("puntos"));
    	tvReservasProfesor.setItems(reservasProfesor);
    	
    	tcNombreAula.setCellValueFactory(new PropertyValueFactory<>("nombre"));
    	tcPuestos.setCellValueFactory(new PropertyValueFactory<>("puestos"));
    	tvAulas.setItems(aulas);
    	tvAulas.getSelectionModel().selectedItemProperty().addListener((ob, ov, nv) -> mostrarReservasAula(nv));
    	
    	tcRAProfesor.setCellValueFactory(reserva -> new SimpleStringProperty(reserva.getValue().getProfesor().getNombre()));
    	tcRADia.setCellValueFactory(reserva -> new SimpleStringProperty(FORMATO_FECHA.format(reserva.getValue().getPermanencia().getDia())));
    	tcRAHoraTramo.setCellValueFactory(reserva -> new SimpleStringProperty(getPermanenciaString(reserva.getValue())));
    	tcRAPuntos.setCellValueFactory(new PropertyValueFactory<>("puntos"));
    	tvReservasAula.setItems(reservasAula);
    }
	
    @FXML
	private void salir() {
		if (Dialogos.mostrarDialogoConfirmacion("Salir", "¿Estás seguro de que quieres salir de la aplicación?", null)) {
			controladorMVC.terminar();
			System.exit(0);
		}
	}
	
	@FXML
	private void acercaDe() throws IOException {
		VBox contenido = FXMLLoader.load(getClass().getResource("../vistas/AcercaDe.fxml"));
		Dialogos.mostrarDialogoInformacionPersonalizado("Reservas Aulas", contenido);
	}
	
    @FXML
    void anadirProfesor(ActionEvent event) throws IOException {
    	crearAnadirProfesor();
		anadirProfesor.showAndWait();
    }

    @FXML
    void borrarProfesor(ActionEvent event) {
    	final String BORRAR_PROFESOR = "Borrar Profesor";
    	Profesor profesor = null;
		try {
			profesor = tvProfesores.getSelectionModel().getSelectedItem();
			if (profesor != null && Dialogos.mostrarDialogoConfirmacion(BORRAR_PROFESOR, "¿Estás seguro de que quieres borrar el profesor?", null)) {
				controladorMVC.borrar(profesor);
				profesores.remove(profesor);
				Dialogos.mostrarDialogoInformacion(BORRAR_PROFESOR, "Profesor borrado satisfactoriamente");
			}
		} catch (Exception e) {
			Dialogos.mostrarDialogoError(BORRAR_PROFESOR, e.getMessage());
		}
    }

    @FXML
    void realizarReservaProfesor(ActionEvent event) throws IOException {
    	crearRealizarReservaProfesor();
		realizarReservaProfesor.showAndWait();
    }
    
    @FXML
    void anularReservaProfesor(ActionEvent event) {
		final String ANULAR_RESERVA = "Anular Reserva";
    	Reserva reserva = null;
		try {
			reserva = tvReservasProfesor.getSelectionModel().getSelectedItem();
			if (reserva != null && Dialogos.mostrarDialogoConfirmacion(ANULAR_RESERVA, "¿Estás seguro de que quieres anular la reserva?", null)) {
				controladorMVC.borrar(reserva);
				mostrarReservasProfesor(reserva.getProfesor());
				Dialogos.mostrarDialogoInformacion(ANULAR_RESERVA, "Reserva anulada satisfactoriamente");
			}
		} catch (Exception e) {
			Dialogos.mostrarDialogoError(ANULAR_RESERVA, e.getMessage());
		}
    }
    
    @FXML
    void anadirAula(ActionEvent event) throws IOException {
    	crearAnadirAula();
    	anadirAula.showAndWait();
    }

    @FXML
    void borrarAula(ActionEvent event) {
    	final String BORRAR_AULA = "Borrar Aula";
    	Aula aula = null;
		try {
			aula = tvAulas.getSelectionModel().getSelectedItem();
			if (aula != null && Dialogos.mostrarDialogoConfirmacion(BORRAR_AULA, "¿Estás seguro de que quieres borrar el aula?", null)) {
				controladorMVC.borrar(aula);
				aulas.remove(aula);
				mostrarReservasProfesor(tvProfesores.getSelectionModel().getSelectedItem());
				Dialogos.mostrarDialogoInformacion(BORRAR_AULA, "Aula borrada satisfactoriamente");
			}
		} catch (Exception e) {
			Dialogos.mostrarDialogoError(BORRAR_AULA, e.getMessage());
		}
    }

    @FXML
    void realizarReservaAula(ActionEvent event) throws IOException {
    	crearRealizarReservaAula();
		realizarReservaAula.showAndWait();
    }
    
    @FXML
    void anularReservaAula(ActionEvent event) {
		final String ANULAR_RESERVA = "Anular Reserva";
    	Reserva reserva = null;
		try {
			reserva = tvReservasAula.getSelectionModel().getSelectedItem();
			if (reserva != null && Dialogos.mostrarDialogoConfirmacion(ANULAR_RESERVA, "¿Estás seguro de que quieres anular la reserva?", null)) {
				controladorMVC.borrar(reserva);
				mostrarReservasAula(reserva.getAula());
				Dialogos.mostrarDialogoInformacion(ANULAR_RESERVA, "Reserva anulada satisfactoriamente");
			}
		} catch (Exception e) {
			Dialogos.mostrarDialogoError(ANULAR_RESERVA, e.getMessage());
		}
    }
    
    public void actualizaProfesores() {
    	profesores.setAll(controladorMVC.getProfesores());
    }
    
    public void actualizaAulas() {
    	aulas.setAll(controladorMVC.getAulas());
    }
    
    public void mostrarReservasProfesor(Profesor profesor) {
    	try {
			reservasProfesor.setAll(controladorMVC.getReservas(profesor));
		} catch (IllegalArgumentException e) {
			reservasProfesor.setAll(FXCollections.observableArrayList());
		}
    }
    
    public void mostrarReservasAula(Aula aula) {
    	try {
			reservasAula.setAll(controladorMVC.getReservas(aula));
		} catch (IllegalArgumentException e) {
			reservasAula.setAll(FXCollections.observableArrayList());
		}
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
	
	private void crearAnadirProfesor() throws IOException {
		if (anadirProfesor == null) {
			anadirProfesor = new Stage();
			FXMLLoader cargadorAnadirProfesor = new FXMLLoader(
						getClass().getResource("../vistas/AnadirProfesor.fxml"));
			VBox raizAnadirProfesor = cargadorAnadirProfesor.load();
			cAnadirProfesor = cargadorAnadirProfesor.getController();
			cAnadirProfesor.setControladorMVC(controladorMVC);
			cAnadirProfesor.setProfesores(profesores);
			cAnadirProfesor.inicializa();
			Scene escenaAnadirProfesor = new Scene(raizAnadirProfesor);
			anadirProfesor.setTitle("Añadir Profesor");
			anadirProfesor.initModality(Modality.APPLICATION_MODAL); 
			anadirProfesor.setScene(escenaAnadirProfesor);
		} else {
			cAnadirProfesor.inicializa();
		}
	}
	
	private void crearRealizarReservaProfesor() throws IOException {
		if (realizarReservaProfesor == null) {
			realizarReservaProfesor = new Stage();
			FXMLLoader cargadorRealizarReservaProfesor = new FXMLLoader(
						getClass().getResource("../vistas/RealizarReservaProfesor.fxml"));
			VBox raizRealizarReservaProfesor = cargadorRealizarReservaProfesor.load();
			cRealizarReservaProfesor = cargadorRealizarReservaProfesor.getController();
			cRealizarReservaProfesor.setControladorMVC(controladorMVC);
			cRealizarReservaProfesor.setAulas(aulas);
			cRealizarReservaProfesor.setPadre(this);
			cRealizarReservaProfesor.setProfesor(tvProfesores.getSelectionModel().getSelectedItem());
			Scene escenaRealizarReservaProfesor = new Scene(raizRealizarReservaProfesor);
			realizarReservaProfesor.setTitle("Realizar Reserva Profesor");
			realizarReservaProfesor.initModality(Modality.APPLICATION_MODAL); 
			realizarReservaProfesor.setScene(escenaRealizarReservaProfesor);
			cRealizarReservaProfesor.inicializa();
		} else {
			cRealizarReservaProfesor.setProfesor(tvProfesores.getSelectionModel().getSelectedItem());
			cRealizarReservaProfesor.inicializa();
		}
	}
	
	private void crearAnadirAula() throws IOException {
		if (anadirAula == null) {
			anadirAula = new Stage();
			FXMLLoader cargadorAnadirAula = new FXMLLoader(
						getClass().getResource("../vistas/AnadirAula.fxml"));
			VBox raizAnadirAula = cargadorAnadirAula.load();
			cAnadirAula = cargadorAnadirAula.getController();
			cAnadirAula.setControladorMVC(controladorMVC);
			cAnadirAula.setAulas(aulas);
			cAnadirAula.inicializa();
			Scene escenaAnadirProfesor = new Scene(raizAnadirAula);
			anadirAula.setTitle("Añadir Aula");
			anadirAula.initModality(Modality.APPLICATION_MODAL); 
			anadirAula.setScene(escenaAnadirProfesor);
		} else {
			cAnadirAula.inicializa();
		}
	}
	
	private void crearRealizarReservaAula() throws IOException {
		if (realizarReservaAula == null) {
			realizarReservaAula = new Stage();
			FXMLLoader cargadorRealizarReservaAula = new FXMLLoader(
						getClass().getResource("../vistas/RealizarReservaAula.fxml"));
			VBox raizRealizarReservaAula = cargadorRealizarReservaAula.load();
			cRealizarReservaAula = cargadorRealizarReservaAula.getController();
			cRealizarReservaAula.setControladorMVC(controladorMVC);
			cRealizarReservaAula.setProfesores(profesores);
			cRealizarReservaAula.setPadre(this);
			cRealizarReservaAula.setAula(tvAulas.getSelectionModel().getSelectedItem());
			Scene escenaRealizarReservaAula = new Scene(raizRealizarReservaAula);
			realizarReservaAula.setTitle("Realizar Reserva Aula");
			realizarReservaAula.initModality(Modality.APPLICATION_MODAL); 
			realizarReservaAula.setScene(escenaRealizarReservaAula);
			cRealizarReservaAula.inicializa();
		} else {
			cRealizarReservaAula.setAula(tvAulas.getSelectionModel().getSelectedItem());
			cRealizarReservaAula.inicializa();
		}
	}

}
