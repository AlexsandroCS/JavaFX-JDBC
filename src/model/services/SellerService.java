package model.services;

import java.util.List;

import model.dao.DAOFactory;
import model.dao.SellerDAO;
import model.entities.Seller;

public class SellerService {
	
	private SellerDAO dao = DAOFactory.createSellerDAO();
	
	// Encontrando departamentos existentes.
	public List<Seller> findAll(){
		return dao.findAll();
	}
	
	// Salvando ou fazendo Update de departamentos.
	public void saveOrUpdate(Seller seller) {
		if(seller.getId() == null) {
			dao.insert(seller);
		}
		else {
			dao.update(seller);
		}
	}
	
	// Removendo um departamento.
	public void remove(Seller seller) {
		dao.deleteById(seller.getId());
	}
}
