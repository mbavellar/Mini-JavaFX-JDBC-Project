package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.dao.SqlDepartmentQuery;
import model.entities.Department;

public class DepartmentService {
  
  private final static String DEPARTMENT = "department";

  private DepartmentDao dao = DaoFactory.createDao(DepartmentDao.class);
  
  public List<Department> findAll() {
    return dao.findAll(null, null);
  }
  
  public void saveOrUpdate(Department obj) {
    if (obj.getId() == null) {
      dao.insert(obj, null);
    }
    else {
      dao.update(obj, null);
    } 
  }
  
  public void remove(Department obj) {
    dao.deleteById(obj.getId(), SqlDepartmentQuery.DELETE_DEPARTMENT_BY_ID, null);
  }
  
  public Integer findAutoIncrement() {
    return dao.findAutoIncrement(SqlDepartmentQuery.findNextAutoIncrement(DEPARTMENT), null);
  }
}