package model.dao;

import db.DB;
import db.DBException;
import model.entities.Department;
import model.enums.DEPARTMENT_COLUMNS;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDao extends BaseDao<Department> {
  
  @Override
  public void insert(Department obj, PreparedStatement preSt) {
    executeUpdate(obj, preSt, SqlDepartmentQuery.INSERT_DEPARTMENT);
  }
  
  @Override
  public void update(Department obj, PreparedStatement preSt) {
    executeUpdate(obj, preSt, SqlDepartmentQuery.UPDATE_DEPARTMENT_BY_ID);
  }
  
  @Override
  public Department findById(Integer id, PreparedStatement preSt, ResultSet rs) {
    
    try {
      preSt = conn.prepareStatement(SqlDepartmentQuery.FIND_DEPARTMENT_BY_ID);
      preSt.setInt(ParameterIndex.ONE, id);
      rs = preSt.executeQuery();
      
      if (rs.next())
        return getDepartment(rs);
      else
        throw new DBException("Department not found!!! ID = " + id);
    }
    catch (SQLException e) {
      throw new DBException(e.getMessage());
    }
    finally {
      DB.closeStatementAndResultSet(preSt, rs);
    }
  }
  
  @Override
  public List<Department> findAll(PreparedStatement preSt, ResultSet rs) {
    
    List<Department> departments = new ArrayList<>();
    try {
      preSt = conn.prepareStatement(SqlDepartmentQuery.FIND_ALL_DEPARTMENTS);
      rs = preSt.executeQuery();
      while (rs.next()) {
        departments.add(getDepartment(rs));
      }
      return departments;
    }
    catch (SQLException e) {
      throw new DBException(e.getMessage());
    }
    finally {
      DB.closeStatementAndResultSet(preSt, rs);
    }
  }
  
  private void executeUpdate(Department obj, PreparedStatement preSt, final String query) {
    try {
      preSt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
      preSt.setString(ParameterIndex.ONE, obj.getName());
  
      if (obj.getId() != null) {
        preSt.setInt(ParameterIndex.TWO, obj.getId());
        preSt.executeUpdate();
        return;
      }
      
      if (preSt.executeUpdate() > 0) {
        ResultSet rs = preSt.getGeneratedKeys();
        if (rs.next()) {
          obj.setId(rs.getInt(ParameterIndex.ONE));
        }
        DB.closeResultSet(rs);
      }
    }
    catch (SQLException e) {
      throw new DBException(e.getMessage());
    }
    finally {
      DB.closeStatement(preSt);
    }
  }
  
  private Department getDepartment(ResultSet rs) throws SQLException {
    return new Department(
      rs.getInt(DEPARTMENT_COLUMNS.Id.name()),
      rs.getString(DEPARTMENT_COLUMNS.Name.name()));
  }
}
