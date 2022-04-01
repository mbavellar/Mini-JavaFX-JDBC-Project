package model.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public interface Dao<T> {
  
  void insert(T obj, PreparedStatement preSt);
  void update(T obj, PreparedStatement preSt);
  void deleteById(final Integer id, final String query, PreparedStatement preSt);
  T findById(Integer id, PreparedStatement preSt, ResultSet rs);
  List<T> findAll(PreparedStatement preSt, ResultSet rs);
}