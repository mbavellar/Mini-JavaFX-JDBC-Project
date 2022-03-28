package model.dao;

public final class SqlDepartmentQuery {
  
  private SqlDepartmentQuery() {}
  
  public static final String FIND_DEPARTMENT_BY_ID = "SELECT * FROM department WHERE Id = ?";
  public static final String FIND_ALL_DEPARTMENTS = "SELECT * FROM department ORDER BY Name";
  public static final String INSERT_DEPARTMENT = "INSERT INTO department (Name) VALUES (?)";
  public static final String UPDATE_DEPARTMENT_BY_ID = "UPDATE department SET Name = ? WHERE Id = ?";
  public static final String DELETE_DEPARTMENT_BY_ID = "DELETE FROM department WHERE Id = ?";
}
