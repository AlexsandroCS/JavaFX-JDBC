package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable{
	
	// Dependências
	private DepartmentService ddpService;
	
	@FXML
	private TableView<Department> tableViewDepartment;
	
	// Coluna ID.
	@FXML
	private TableColumn<Department, Integer> tableColumnId;
	
	// Coluna Nome.
	@FXML
	private TableColumn<Department, String> tableColumnName;
	
	// Botão Adicionar.
	@FXML
	private Button buttonNew;
	
	private ObservableList<Department> obsList;
	
	// ------------------------ TRATANDO AÇÕES ------------------------
	@FXML
	public void onButtonNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		createdDialogForm("/gui/DepartmentForm.fxml", parentStage);
	}
	
	// --------------------- INVERSÃO DE CONTROLE ---------------------
	public void setDepartmentService(DepartmentService ddpService) {
		this.ddpService = ddpService;
	}
	
	// -------------------- PASSANDO DADOS DA LISTA --------------------
	public void updateTableView() {
		if (ddpService == null) {
			throw new IllegalStateException("Services was null!");
		}
		List<Department> list = ddpService.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewDepartment.setItems(obsList);
	}
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		inicializeNodes();
	}

	private void inicializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("Name"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
	}
	
	private void createdDialogForm(String absolutName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutName));
			Pane pane = loader.load();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter department data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		}
		catch (IOException e) {
			Alerts.showAlert("IOException", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}
}