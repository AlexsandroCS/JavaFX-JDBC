package model.services;

import java.util.List;

import model.dao.DAOFactory;
import model.dao.DepartmentDAO;
import model.entities.Department;

public class DepartmentService {
	
	private DepartmentDAO dao = DAOFactory.createDepartmentDAO();
	
	// Encontrando departamentos existentes.
	public List<Department> findAll(){
		return dao.findAll();
	}
	
	// Salvando ou fazendo Update de departamentos.
	public void saveOrUpdate(Department department) {
		if(department.getId() == null) {
			dao.insert(department);
		}
		else {
			dao.update(department);
		}
	}
	
	// Removendo um departamento.
	public void remove(Department department) {
		dao.deleteById(department.getId());
	}
}
