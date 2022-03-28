package model.dao;

import db.DB;
import db.DBException;
import model.enums.DEPARTMENT_COLUMNS;
import model.enums.SELLER_COLUMNS;
import model.entities.Department;
import model.entities.Seller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDao extends BaseDao<Seller> {
  
  Map<Integer, Department> departmentIds = new HashMap<>();
  
  @Override
  public void insert(Seller obj, PreparedStatement preSt) {
    executeUpdate(obj, SqlSellerQuery.INSERT_SELLER, preSt);
  }
  
  @Override
  public void update(Seller obj, PreparedStatement preSt) {
    executeUpdate(obj, SqlSellerQuery.UPDATE_SELLER_BY_ID, preSt);
  }
  
  @Override
  public Seller findById(Integer id, PreparedStatement preSt, ResultSet rs) {
    
    try {
      preSt = conn.prepareStatement(SqlSellerQuery.FIND_SELLER_BY_ID);
      preSt.setInt(ParameterIndex.ONE, id);
      
      rs = preSt.executeQuery();
      if (rs.next()) {
        Department department = instantiateDepartment(rs);
        Seller seller = instantiateSeller(rs, department);
        return seller;
      }
      throw new DBException("Seller not found!");
      
    } catch (SQLException e) {
      throw new DBException(e.getMessage());
    } finally {
      DB.closeStatementAndResultSet(preSt, rs);
    }
  }
  
  @Override
  public List<Seller> findAll(PreparedStatement preSt, ResultSet rs) {
    return getSellers(preSt, rs,null, SqlSellerQuery.FIND_ALL_SELLERS);
  }
  
  public List<Seller> findByDepartmentId(
    PreparedStatement preSt, ResultSet rs, Integer departmentId) {
    return getSellers(preSt, rs, departmentId, SqlSellerQuery.FIND_SELLERS_BY_DEPARTMENT_ID);
  }
  
  private Seller instantiateSeller(ResultSet rs, Department department) throws SQLException{
    return new Seller(
      rs.getInt(SELLER_COLUMNS.Id.name()),
      rs.getString(SELLER_COLUMNS.Name.name()),
      rs.getString(SELLER_COLUMNS.Email.name()),
      rs.getDate(SELLER_COLUMNS.BirthDate.name()),
      rs.getDouble(SELLER_COLUMNS.BaseSalary.name()),
      department);
  }
  
  private Department instantiateDepartment(final ResultSet rs) throws SQLException {
    return new Department(
      rs.getInt(DEPARTMENT_COLUMNS.DepartmentId.name()),
      rs.getString(DEPARTMENT_COLUMNS.Department.name()));
  }
  
  private Department getDepartment(final ResultSet rs) throws SQLException {
    int departmentId = rs.getInt(DEPARTMENT_COLUMNS.DepartmentId.name());
    Department dep = departmentIds.get(departmentId);
    if (dep == null) {
      dep = instantiateDepartment(rs);
      departmentIds.put(departmentId, dep);
    }
    return dep;
  }
  
  private void executeUpdate(Seller obj, final String query, PreparedStatement preSt) {
    try {
      preSt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
      preSt.setString(ParameterIndex.ONE, obj.getName());
      preSt.setString(ParameterIndex.TWO, obj.getEmail());
      preSt.setDate(ParameterIndex.THREE, new java.sql.Date(obj.getBirthDate().getTime()));
      preSt.setDouble(ParameterIndex.FOUR, obj.getBaseSalary());
      preSt.setInt(ParameterIndex.FIVE, obj.getDepartment().getId());
      if (obj.getId() != null) {
        preSt.setInt(ParameterIndex.SIX, obj.getId());
        preSt.executeUpdate();
        return;
      }
      if (preSt.executeUpdate() > 0) {
        ResultSet resultSet = preSt.getGeneratedKeys();
        if (resultSet.next()) {
          obj.setId(resultSet.getInt(1));
        }
        DB.closeResultSet(resultSet);
      }
      else {
          throw new DBException("Unexpected Error! No Rows Affected!");
      }
    }
    catch (SQLException e) {
      throw new DBException(e.getMessage());
    }
    finally {
      DB.closeStatement(preSt);
    }
  }
  
  private List<Seller> getSellers (PreparedStatement preSt, ResultSet rs,
    final Integer departmentId, final String query) {
    try {
      preSt = conn.prepareStatement(query);
      
      if (departmentId != null)
        preSt.setInt(ParameterIndex.ONE, departmentId);
  
      rs = preSt.executeQuery();
    
      List<Seller> sellers = new ArrayList<>();
      while (rs.next()) {
        Department department = getDepartment(rs);
        Seller seller = instantiateSeller(rs, department);
        sellers.add(seller);
      }
      return sellers;
    } catch (SQLException e) {
      throw new DBException(e.getMessage());
    } finally {
      DB.closeStatementAndResultSet(preSt, rs);
    }
  }
}