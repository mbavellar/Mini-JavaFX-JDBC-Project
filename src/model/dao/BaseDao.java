package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.DB;
import db.DBException;


public abstract class BaseDao<T> implements Dao<T> {
  protected Connection conn;
  public BaseDao() {
    this.conn = DB.getConnection();
  }
  
  public void deleteById(final Integer id, final String query, PreparedStatement preSt) {
    try {
      preSt = conn.prepareStatement(query);
      preSt.setInt(ParameterIndex.ONE, id);
      preSt.executeUpdate();
    }
    catch (SQLException e) {
      throw new DBException(e.getMessage());
    }
    finally {
      DB.closeStatement(preSt);
    }
  }
  
  public Integer findAutoIncrement(final String query, PreparedStatement preSt) {
    try {
      preSt = conn.prepareStatement(query);
      ResultSet indexes = preSt.executeQuery();
      if (indexes.next()) return indexes.getInt(1);
      return null;
    }
    catch (SQLException e) {
      throw new DBException(e.getMessage());
    }
    finally {
      DB.closeStatement(preSt);
    }
  }
}
