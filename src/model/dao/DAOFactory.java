package model.dao;

import db.DB;
import model.dao.implement.DepartmentDAOimpJDBC;
import model.dao.implement.SellerDAOimpJDBC;

public class DAOFactory {
    public static SellerDAO createSellerDAO(){
        return new SellerDAOimpJDBC(DB.getConnection());
    }
    public static DepartmentDAO createDepartmentDAO(){return new DepartmentDAOimpJDBC(DB.getConnection());}
}