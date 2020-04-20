package org.iesalandalus.programacion.reservasaulas.mvc.vista.iugventanas.controladoresvistas;

import java.io.IOException;

import org.iesalandalus.programacion.reservasaulas.mvc.controlador.IControlador;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Reserva;
import org.iesalandalus.programacion.reservasaulas.mvc.vista.iugventanas.utilidades.Dialogos;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ControladorVentanaPrincipal {
	
	private IControlador controladorMVC;
	
	public void setControladorMVC(IControlador controladorMVC) {
		this.controladorMVC = controladorMVC;
	}
	
	@FXML Button btAnadirProfesor;
	@FXML Button btListarProfesores;
	@FXML Button btBuscarProfesor;
	@FXML Button btAnadirAula;
	@FXML Button btListarAulas;
	@FXML Button btBuscarAula;
	@FXML Button btAnadirReserva;
	@FXML Button btListarReservas;
	@FXML Button btBuscarReserva;
	
	private Stage anadirProfesor;
    private ControladorAnadirProfesor cAnadirProfesor;
    private Stage listarProfesores;
    private ControladorListarProfesores cListarProfesores;
    private Stage mostrarProfesor;
    private ControladorMostrarProfesor cMostrarProfesor;
    private Stage anadirAula;
    private ControladorAnadirAula cAnadirAula;
    private Stage listarAulas;
    private ControladorListarAulas cListarAulas;
    private Stage mostrarAula;
    private ControladorMostrarAula cMostrarAula;
    private Stage anadirReserva;
    private ControladorAnadirReserva cAnadirReserva;
    private Stage listarReservas;
    private ControladorListarReservas cListarReservas;
    private Stage buscarReserva;
    private ControladorBuscarReserva cBuscarReserva;
    private Stage mostrarReserva;
    private ControladorMostrarReserva cMostrarReserva;
    
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
	private void anadirProfesor() throws IOException {
		crearAnadirProfesor();
		anadirProfesor.showAndWait();
	}
	
	@FXML
	private void listarProfesores() throws IOException {
		crearListarProfesores();
		listarProfesores.showAndWait();
	}
	
	@FXML
	private void buscarProfesor() {
		String correo = Dialogos.mostrarDialogoTexto("Buscar Profesor", "Correo:");
		if (correo != null) {
			Profesor profesor = null;
			try {
				profesor = controladorMVC.buscar(Profesor.getProfesorFicticio(correo));
				if (profesor != null) {
					crearMostrarProfesor(profesor);
					mostrarProfesor.showAndWait();
				} else {
					Dialogos.mostrarDialogoError("Profesor no encontrado", "No existe ningún profesor con ese correo");
				}
			} catch (Exception e) {
				Dialogos.mostrarDialogoError("Correo no válido", e.getMessage());
			}
		}
	}
	
	@FXML
	private void anadirAula() throws IOException {
		crearAnadirAula();
		anadirAula.showAndWait();
	}
	
	@FXML
	private void listarAulas() throws IOException {
		crearListarAulas();
		listarAulas.showAndWait();
	}
	
	@FXML
	private void buscarAula() {
		String nombre = Dialogos.mostrarDialogoTexto("Buscar Aula", "Nombre:");
		if (nombre != null) {
			Aula aula = null;
			try {
				aula = controladorMVC.buscar(Aula.getAulaFicticia(nombre));
				if (aula != null) {
					crearMostrarAula(aula);
					mostrarAula.showAndWait();
				} else {
					Dialogos.mostrarDialogoError("Aula no encontrada", "No existe ningún aula con ese nombre");
				}
			} catch (Exception e) {
				Dialogos.mostrarDialogoError("Nombre no válido", e.getMessage());
			}
		}
	}
	
	@FXML
	private void anadirReserva() throws IOException {
		crearAnadirReserva();
		anadirReserva.showAndWait();
	}
	
	@FXML
	private void listarReservas() throws IOException {
		crearListarReservas();
		listarReservas.showAndWait();
	}
	
	@FXML
	private void buscarReserva() throws IOException {
		crearBuscarReserva();
		buscarReserva.showAndWait();
		Reserva reserva = cBuscarReserva.getReserva();
		if (reserva != null) {
			crearMostrarReserva(reserva);
			mostrarReserva.showAndWait();
		} else {
			Dialogos.mostrarDialogoError("Reserva no encontrada", "No existe ninguna reserva para ese aula y esa permanencia");
		}
	}
	
	private void crearAnadirProfesor() throws IOException {
		if (anadirProfesor == null) {
			anadirProfesor = new Stage();
			FXMLLoader cargadorAnadirProfesor = new FXMLLoader(
						getClass().getResource("../vistas/AnadirProfesor.fxml"));
			VBox raizAnadirProfesor = cargadorAnadirProfesor.load();
			cAnadirProfesor = cargadorAnadirProfesor.getController();
			cAnadirProfesor.setControladorMVC(controladorMVC);
			cAnadirProfesor.inicializa();
			Scene escenaAnadirProfesor = new Scene(raizAnadirProfesor);
			anadirProfesor.setTitle("Añadir Profesor");
			anadirProfesor.initModality(Modality.APPLICATION_MODAL); 
			anadirProfesor.setScene(escenaAnadirProfesor);
		} else {
			cAnadirProfesor.inicializa();
		}
	}
	
	private void crearListarProfesores() throws IOException {
		if (listarProfesores == null) {
			listarProfesores = new Stage();
			FXMLLoader cargadorListarProfesores = new FXMLLoader(
						getClass().getResource("../vistas/ListarProfesores.fxml"));
			VBox raizListarProfesores = cargadorListarProfesores.load();
			cListarProfesores = cargadorListarProfesores.getController();
			cListarProfesores.setControladorMVC(controladorMVC);
			cListarProfesores.inicializa();
			Scene escenaListarProfesores = new Scene(raizListarProfesores);
			listarProfesores.setTitle("Listar Profesores");
			listarProfesores.initModality(Modality.APPLICATION_MODAL); 
			listarProfesores.setScene(escenaListarProfesores);
		} else {
			cListarProfesores.inicializa();
		}
	}
	
	private void crearMostrarProfesor(Profesor profesor) throws IOException {
		if (mostrarProfesor == null) {
			mostrarProfesor = new Stage();
			FXMLLoader cargadorMostrarProfesor = new FXMLLoader(
						getClass().getResource("../vistas/MostrarProfesor.fxml"));
			VBox raizMostrarProfesor = cargadorMostrarProfesor.load();
			cMostrarProfesor = cargadorMostrarProfesor.getController();
			cMostrarProfesor.setControladorMVC(controladorMVC);
			cMostrarProfesor.setProfesor(profesor);
			Scene escenaMostrarProfesor = new Scene(raizMostrarProfesor);
			mostrarProfesor.setTitle("Mostrar Profesor");
			mostrarProfesor.initModality(Modality.APPLICATION_MODAL); 
			mostrarProfesor.setScene(escenaMostrarProfesor);
		} else {
			cMostrarProfesor.setProfesor(profesor);
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
			cAnadirAula.inicializa();
			Scene escenaAnadirAula = new Scene(raizAnadirAula);
			anadirAula.setTitle("Añadir Aula");
			anadirAula.initModality(Modality.APPLICATION_MODAL); 
			anadirAula.setScene(escenaAnadirAula);
		} else {
			cAnadirAula.inicializa();
		}
	}
	
	private void crearListarAulas() throws IOException {
		if (listarAulas == null) {
			listarAulas = new Stage();
			FXMLLoader cargadorListarAulas = new FXMLLoader(
						getClass().getResource("../vistas/ListarAulas.fxml"));
			VBox raizListarAulas = cargadorListarAulas.load();
			cListarAulas = cargadorListarAulas.getController();
			cListarAulas.setControladorMVC(controladorMVC);
			cListarAulas.inicializa();
			Scene escenaListarProfesores = new Scene(raizListarAulas);
			listarAulas.setTitle("Listar Aulas");
			listarAulas.initModality(Modality.APPLICATION_MODAL); 
			listarAulas.setScene(escenaListarProfesores);
		} else {
			cListarAulas.inicializa();
		}
	}
	
	private void crearMostrarAula(Aula aula) throws IOException {
		if (mostrarAula == null) {
			mostrarAula = new Stage();
			FXMLLoader cargadorMostrarAula = new FXMLLoader(
						getClass().getResource("../vistas/MostrarAula.fxml"));
			VBox raizMostrarAula = cargadorMostrarAula.load();
			cMostrarAula = cargadorMostrarAula.getController();
			cMostrarAula.setControladorMVC(controladorMVC);
			cMostrarAula.setAula(aula);
			Scene escenaMostrarAula = new Scene(raizMostrarAula);
			mostrarAula.setTitle("Mostrar Aula");
			mostrarAula.initModality(Modality.APPLICATION_MODAL); 
			mostrarAula.setScene(escenaMostrarAula);
		} else {
			cMostrarAula.setAula(aula);
		}
	}
	
	private void crearAnadirReserva() throws IOException {
		if (anadirReserva == null) {
			anadirReserva = new Stage();
			FXMLLoader cargadorAnadirReserva = new FXMLLoader(
						getClass().getResource("../vistas/AnadirReserva.fxml"));
			VBox raizAnadirReserva = cargadorAnadirReserva.load();
			cAnadirReserva = cargadorAnadirReserva.getController();
			cAnadirReserva.setControladorMVC(controladorMVC);
			cAnadirReserva.inicializa();
			Scene escenaAnadirAula = new Scene(raizAnadirReserva);
			anadirReserva.setTitle("Añadir Reserva");
			anadirReserva.initModality(Modality.APPLICATION_MODAL); 
			anadirReserva.setScene(escenaAnadirAula);
		} else {
			cAnadirReserva.inicializa();
		}
	}
	
	private void crearListarReservas() throws IOException {
		if (listarReservas == null) {
			listarReservas = new Stage();
			FXMLLoader cargadorListarReservas = new FXMLLoader(
						getClass().getResource("../vistas/ListarReservas.fxml"));
			VBox raizListarReservas = cargadorListarReservas.load();
			cListarReservas = cargadorListarReservas.getController();
			cListarReservas.setControladorMVC(controladorMVC);
			cListarReservas.inicializa();
			Scene escenaListarReservas = new Scene(raizListarReservas);
			listarReservas.setTitle("Listar Reservas");
			listarReservas.initModality(Modality.APPLICATION_MODAL); 
			listarReservas.setScene(escenaListarReservas);
		} else {
			cListarReservas.inicializa();
		}
	}
	
	private void crearBuscarReserva() throws IOException {
		if (buscarReserva == null) {
			buscarReserva = new Stage();
			FXMLLoader cargadorBuscarReserva = new FXMLLoader(
						getClass().getResource("../vistas/BuscarReserva.fxml"));
			VBox raizBuscarReserva = cargadorBuscarReserva.load();
			cBuscarReserva = cargadorBuscarReserva.getController();
			cBuscarReserva.setControladorMVC(controladorMVC);
			cBuscarReserva.inicializa();
			Scene escenaBuscarAula = new Scene(raizBuscarReserva);
			buscarReserva.setTitle("Buscar Reserva");
			buscarReserva.initModality(Modality.APPLICATION_MODAL); 
			buscarReserva.setScene(escenaBuscarAula);
		} else {
			cBuscarReserva.inicializa();
		}
	}
	
	private void crearMostrarReserva(Reserva reserva) throws IOException {
		if (mostrarReserva == null) {
			mostrarReserva = new Stage();
			FXMLLoader cargadorMostrarReserva = new FXMLLoader(
						getClass().getResource("../vistas/MostrarReserva.fxml"));
			VBox raizMostrarReserva = cargadorMostrarReserva.load();
			cMostrarReserva = cargadorMostrarReserva.getController();
			cMostrarReserva.setControladorMVC(controladorMVC);
			cMostrarReserva.setReserva(reserva);
			Scene escenaMostrarReserva = new Scene(raizMostrarReserva);
			mostrarReserva.setTitle("Mostrar Reserva");
			mostrarReserva.initModality(Modality.APPLICATION_MODAL); 
			mostrarReserva.setScene(escenaMostrarReserva);
		} else {
			cMostrarReserva.setReserva(reserva);
		}
	}

}
