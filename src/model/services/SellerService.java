package model.services;

import java.util.List;
import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.dao.SqlSellerQuery;
import model.entities.Seller;

public class SellerService {

  private SellerDao dao = DaoFactory.createDao(SellerDao.class);
  
  public List<Seller> findAll() {
    return dao.findAll(null, null);
  }
  
  public void saveOrUpdate(Seller obj) {
    if (obj.getId() == null) {
      dao.insert(obj, null);
    }
    else {
      dao.update(obj, null);
    } 
  }
  
  public void remove(Seller obj) {
    dao.deleteById(obj.getId(), SqlSellerQuery.DELETE_SELLER_BY_ID, null);
  }
}