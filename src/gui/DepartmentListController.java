package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
	private Button buttonNovo;
	
	private ObservableList<Department> obsList;
	
	// ------------------------ TRATANDO AÇÕES ------------------------
	@FXML
	public void onButtonNovoAction() {
		System.out.println("Botão Clickado!");
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

}