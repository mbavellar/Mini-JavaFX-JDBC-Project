package model.dao;

import java.lang.reflect.InvocationTargetException;

public class DaoFactory {
  
  public static <D> D createDao(Class<D> daoClass) {
    try {
      return daoClass.getDeclaredConstructor().newInstance();
    } catch (InstantiationException | IllegalAccessException |
      NoSuchMethodException | InvocationTargetException e) {
      System.out.println(e.getCause().getMessage());
    }
    return null;
  }
}
