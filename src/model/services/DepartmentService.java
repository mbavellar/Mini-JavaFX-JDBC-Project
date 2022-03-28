package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService {

  public static List<Department> findAll() {
    return DaoFactory.createDao(DepartmentDao.class).findAll(null, null);
  }
}