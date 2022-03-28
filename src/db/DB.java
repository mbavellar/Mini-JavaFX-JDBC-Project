package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DB {
  
  private static Connection conn = null;
  
  public static Connection getConnection() {
    if (conn == null) {
      Properties properties = loadProperties();
      try {
        conn = DriverManager.getConnection(properties.getProperty("dburl"), properties);
      } catch (SQLException e){
        throw new DBException(e.getMessage());
      }
    }
    return conn;
  }
  
  public static void closeConnection() {
    if (conn != null)
      try {
        conn.close();
      } catch (SQLException e) {
        throw new DBException(e.getMessage());
      }
  }
  
  private static Properties loadProperties() {
    try (FileInputStream fis = new FileInputStream("db.properties")) {
      Properties properties = new Properties();
      properties.load(fis);
      return properties;
    } catch (IOException e) {
      throw new DBException(e.getMessage());
    }
  }
  
  public static void closeStatement(Statement st) {
    if (st != null) {
      try {
        st.close();
      } catch (SQLException e) {
        throw new DBException(e.getMessage());
      }
    }
  }
  
  public static void closeResultSet(ResultSet rs) {
    if (rs != null) {
      try {
        rs.close();
      } catch (SQLException e) {
        throw new DBException(e.getMessage());
      }
    }
  }
  
  public static void closeStatementAndResultSet(PreparedStatement preSt, ResultSet rs) {
    closeStatement(preSt);
    closeResultSet(rs);
  }
}