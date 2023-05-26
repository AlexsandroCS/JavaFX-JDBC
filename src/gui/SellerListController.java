package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
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
import model.entities.Seller;
import model.services.SellerService;

public class SellerListController implements Initializable, DataChangeListener {

	// Dependências
	private SellerService ddpService;

	@FXML
	private TableView<Seller> tableViewSeller;

	// Coluna ID.
	@FXML
	private TableColumn<Seller, Integer> tableColumnId;

	// Coluna Nome.
	@FXML
	private TableColumn<Seller, String> tableColumnName;
	
	// Email.
	@FXML
	private TableColumn<Seller, String> tableColumnEmail;
	
	// Data de Nascimento.
	@FXML
	private TableColumn<Seller, Date> tableColumnBirthDate;
	
	// Salário.
	@FXML
	private TableColumn<Seller, Double> tableColumnBaseSalary;

	// Tabela de Edição de Departamento.
	@FXML
	private TableColumn<Seller, Seller> tableColumnEDIT;

	// Tablea exclusão de Departamento.
	@FXML
	private TableColumn<Seller, Seller> tableColumnREMOVE;

	// Botão Adicionar.
	@FXML
	private Button buttonNew;

	private ObservableList<Seller> obsList;

	// ------------------------ TRATANDO AÇÕES ------------------------
	@FXML
	public void onButtonNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Seller newSeller = new Seller();
		createdDialogForm(newSeller, "/gui/SellerForm.fxml", parentStage);
	}

	// --------------------- INVERSÃO DE CONTROLE ---------------------
	public void setSellerService(SellerService ddpService) {
		this.ddpService = ddpService;
	}

	// -------------------- PASSANDO DADOS DA LISTA --------------------
	public void updateTableView() {
		if (ddpService == null) {
			throw new IllegalStateException("Services was null!");
		}
		List<Seller> list = ddpService.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewSeller.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		inicializeNodes();
	}

	private void inicializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("Email"));
		tableColumnBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
		tableColumnBaseSalary.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
		Utils.formatTableColumnDate(tableColumnBirthDate,"dd/MM/yyyy");
		Utils.formatTableColumnDouble(tableColumnBaseSalary, 2);
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewSeller.prefHeightProperty().bind(stage.heightProperty());
	}

	private void createdDialogForm(Seller newSeller, String absolutName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutName));
			Pane pane = loader.load();

			// Referência do controlador
			SellerFormController controller = loader.getController();
			controller.setSeller(newSeller);
			controller.setSellerService(new SellerService());
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter seller data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
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
		tableColumnEDIT.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createdDialogForm(obj, "/gui/SellerForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	// Botão de Remover.
	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
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

	private void removeEntity(Seller seller) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");
		
		if (result.get() == ButtonType.OK) {
			if (ddpService == null) {
				throw new IllegalStateException("Service was null!");
			}
			try {
				ddpService.remove(seller);
				updateTableView(); 
			}
			catch(DBException e) {
				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}
}