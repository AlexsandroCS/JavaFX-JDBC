package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartmentService;

public class MainViewController implements Initializable{
	
	// Menu Botões: Registrar.
	@FXML
	private MenuItem menuItemVendedor;
	@FXML
	private MenuItem menuItemDepartamento;
	
	// Meu Botões: Ajuda.
	@FXML
	private MenuItem menuItemMais;
	
	
	// ------------------------ TRATANDO AÇÕES DOS BOTÕES ------------------------
	
	// Ação Botão: Vendedor.
	@FXML
	public void onMenuItemVendedorAction() {
		loadView("/gui/MainView.fxml");
	}
	
	// Ação Botão: Departamento.
	@FXML
	public void onMenuItemDepartamentoAction() {
		loadViewTwo("/gui/DepartmentList.fxml");
	}
	
	// Ação Botão: Mais.
	@FXML
	public void onMenuItemMaisAction() {
		loadView("/gui/About.fxml");
	}
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		
	}
	
	// Método para carregar cenas.
	public synchronized void loadView(String absolutName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutName));
			VBox newVBox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			// Pegando a referência da VBox.
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			// Pegando o primeiro "<children>" da janela principal.
			Node mainMenu = mainVBox.getChildren().get(0);
			// Limpando todos os "<children>" da mainVBox.
			mainVBox.getChildren().clear();
			
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());
		}
		catch(IOException error) {
			Alerts.showAlert("IOException","Error Loading View!",error.getMessage(), AlertType.ERROR);
		}
	}
	
	// LoadviewTwo Testando --> apagar depois.
		public synchronized void loadViewTwo(String absolutName) {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutName));
				VBox newVBox = loader.load();
				
				Scene mainScene = Main.getMainScene();
				VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
				Node mainMenu = mainVBox.getChildren().get(0);
				
				mainVBox.getChildren().clear();
				mainVBox.getChildren().add(mainMenu);
				mainVBox.getChildren().addAll(newVBox.getChildren());
				
				DepartmentListController controller = loader.getController();
				controller.setDepartmentService(new DepartmentService());
				controller.updateTableView();
			}
			catch(IOException error) {
				Alerts.showAlert("IOException","Error Loading View!",error.getMessage(), AlertType.ERROR);
			}
		}
	
}