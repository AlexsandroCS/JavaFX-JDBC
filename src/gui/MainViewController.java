package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

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
		System.out.println("Cadastrando Vendedor!");
	}
	
	// Ação Botão: Departamento.
	@FXML
	public void onMenuItemDepartamentoAction() {
		System.out.println("Cadastrando Departamento!");
	}
	
	// Ação Botão: Mais.
	@FXML
	public void onMenuItemMaisAction() {
		System.out.println("Evento botão Mais!");
	}
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		
	}
	
}
