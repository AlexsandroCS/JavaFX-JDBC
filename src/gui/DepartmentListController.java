package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DBException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable, DataChangeListener {

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

	// Tabela de Edição de Departamento.
	@FXML
	private TableColumn<Department, Department> tableColumnEDIT;

	// Tablea exclusão de Departamento.
	@FXML
	private TableColumn<Department, Department> tableColumnREMOVE;

	// Botão Adicionar.
	@FXML
	private Button buttonNew;

	private ObservableList<Department> obsList;

	// ------------------------ TRATANDO AÇÕES ------------------------
	@FXML
	public void onButtonNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Department newDepartment = new Department();
		createdDialogForm(newDepartment, "/gui/DepartmentForm.fxml", parentStage);
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
		initEditButtons();
		initRemoveButtons();
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

	private void createdDialogForm(Department newDepartment, String absolutName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutName));
			Pane pane = loader.load();

			// Referência do controlador
			DepartmentFormController controller = loader.getController();
			controller.setDepartment(newDepartment);
			controller.setDepartmentService(new DepartmentService());
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter department data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IOException", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	// Evento de atualização dos dados da tabela.
	@Override
	public void onDataChanged() {
		updateTableView();
	}

	// Botão de Editar.
	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Department, Department>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Department obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createdDialogForm(obj, "/gui/DepartmentForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	// Botão de Remover.
	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Department, Department>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Department obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(Department department) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");
		
		if (result.get() == ButtonType.OK) {
			if (ddpService == null) {
				throw new IllegalStateException("Service was null!");
			}
			try {
				ddpService.remove(department);
				updateTableView(); 
			}
			catch(DBException e) {
				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}
}