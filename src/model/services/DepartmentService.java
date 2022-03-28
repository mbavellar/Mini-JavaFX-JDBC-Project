package model.services;

import java.util.Arrays;
import java.util.List;

import model.entities.Department;

public class DepartmentService {

  public List<Department> findAll() {
    return Arrays.asList(
        new Department(1, "books"),
        new Department(2, "Computer"),
        new Department(3, "Electronics"));
  }
}