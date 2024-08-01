package ca.jrvs.apps.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class PositionService {

  private PositionDAO dao;
  private DatabaseConnectionManager dcm;

  /**
   * Processes a buy order and updates the database accordingly
   * @param ticker
   * @param numberOfShares
   * @param price
   * @return The position in our database after processing the buy
   */
  public Position buy(String ticker, int numberOfShares, double price) throws SQLException {

    //setting up connection to the database
    Connection connection = dcm.getConnection();
    dao.setC(connection);

    try {
      //creating instance of position to add in arguments
      Position buyingPosition = new Position();
      buyingPosition.setTicker(ticker);
      buyingPosition.setNumOfShares(numberOfShares);
      buyingPosition.setValuePaid(price);

      //adding it to database using dao
      dao.save(buyingPosition);

      //find by id and return whatever the new result is
      Optional<Position> newPosition = dao.findById(buyingPosition.getTicker());
      return newPosition.get();
    }catch (Exception e){
      e.printStackTrace();
    }
    finally{
      if (connection != null && !connection.isClosed()) {
        connection.close();
      }
    }
    return null;
  }

  /**
   * Sells all shares of the given ticker symbol
   * @param ticker
   */
  public void sell(String ticker) throws SQLException {

    //setting up connection to the database
    Connection connection = dcm.getConnection();
    dao.setC(connection);

    try {
      dao.deleteById(ticker);
    }catch (Exception e){
      e.printStackTrace();
    }finally {
      if (connection != null && !connection.isClosed()) {
        connection.close();
      }
    }
  }

  public PositionDAO getDao() {
    return dao;
  }

  public void setDao(PositionDAO dao) {
    this.dao = dao;
  }

  public DatabaseConnectionManager getDcm() {
    return dcm;
  }

  public void setDcm(DatabaseConnectionManager dcm) {
    this.dcm = dcm;
  }
}
