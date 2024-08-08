package ca.jrvs.apps.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;

public class PositionService {

  private PositionDAO dao;
  private static final Logger loggerInfo = LogManager.getLogger("StockLogInfo");
  private static final Logger loggerError = LogManager.getLogger("StockLogError");

  /**
   * Processes a buy order and updates the database accordingly
   * @param ticker
   * @param numberOfShares
   * @param price
   * @return The position in our database after processing the buy
   */
  public Position buy(String ticker, int numberOfShares, double price) throws SQLException {
    try {
      loggerInfo.info("creating instance of position and adding in arguents");
      Position buyingPosition = new Position();
      buyingPosition.setTicker(ticker);
      buyingPosition.setNumOfShares(numberOfShares);
      buyingPosition.setValuePaid(price);

      loggerInfo.info("saving the position instance to the database");
      dao.save(buyingPosition);

      loggerInfo.info("giving back new result of the position");
      Optional<Position> newPosition = dao.findById(buyingPosition.getTicker());
      return newPosition.get();
    }catch (Exception e){
      loggerError.error("Position Service Buy Error: Unable to process because ", e);
    }
    return null;
  }

  /**
   * Sells all shares of the given ticker symbol
   * @param ticker
   */
  public void sell(String ticker) throws SQLException {
    try {
      loggerInfo.info("deleting position by id");
      dao.deleteById(ticker);
    }catch (Exception e){
      loggerError.error("Position Service Sell Error: Unable to process because ", e);
    }
  }

  public PositionDAO getDao() {
    return dao;
  }

  public void setDao(PositionDAO dao) {
    this.dao = dao;
  }

}
