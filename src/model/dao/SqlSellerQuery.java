package model.dao;

public class SqlSellerQuery {
  
  private SqlSellerQuery() {}
  
  public static final String FIND_SELLER_BY_ID =
    "SELECT seller.*, department.Name AS Department " +
    "FROM seller INNER JOIN department " +
    "ON seller.DepartmentId = Department.Id " +
    "WHERE seller.Id = ?";
  
  public static final String FIND_SELLERS_BY_DEPARTMENT_ID =
    "SELECT seller.* , department.Name AS Department " +
    "FROM seller INNER JOIN department " +
    "ON seller.DepartmentId = department.Id " +
    "WHERE DepartmentId = ? ORDER BY Name";
  
  public static final String FIND_ALL_SELLERS =
    "SELECT seller.* , department.Name AS Department " +
    "FROM seller INNER JOIN department " +
    "ON seller.DepartmentId = department.Id " +
    "ORDER BY Name";
  
  public static final String INSERT_SELLER =
    "INSERT INTO seller (Name, Email, BirthDate, BaseSalary, DepartmentId) " +
    "VALUES (?, ?, ?, ?, ?);";
  
  public static final String UPDATE_SELLER_BY_ID =
     "UPDATE seller SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? " +
     "WHERE Id = ?";
  
  public static final String DELETE_SELLER_BY_ID =
     "DELETE FROM seller WHERE Id = ?";
}
