package gui;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;

public class DepartmentListController implements Initializable{
	
	
	@FXML
	private TableView<Department> tableViewDepartment;
	
	// Coluna ID.
	@FXML
	private TableColumn<Department, Integer> tableColumnId;
	
	// Coluna Nome.
	@FXML
	private TableColumn<Department, String> tableColumnNome;
	
	// Botão Adicionar.
	@FXML
	private Button buttonNovo;
	
	// ------------------------ TRATANDO AÇÕES ------------------------
	
	@FXML
	public void onButtonNovoAction() {
		System.out.println("Botão Clickado!");
	}
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		inicializeNodes();
	}

	private void inicializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("Nome"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
	}

}