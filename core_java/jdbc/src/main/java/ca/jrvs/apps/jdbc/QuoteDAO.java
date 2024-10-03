package ca.jrvs.apps.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class QuoteDAO implements CrudDao<Quote, String>{

  private Connection c;

  private static final String GET_ONE = "SELECT symbol, open, high, low, price, volume, latest_trading_day, previous_close, change, change_percent, timestamp FROM quote WHERE symbol=?";

  private static final String GET_ALL = "SELECT symbol, open, high, low, price, volume, latest_trading_day, previous_close, change, change_percent, timestamp FROM quote";

  private static final String DELETE_ONE = "DELETE FROM quote WHERE symbol = ?";

  private static final String DELETE_ALL = "DELETE FROM quote";

  private static final String UPDATE = "UPDATE quote SET open = ?, high = ?, low = ?, price = ?, volume = ?, latest_trading_day = ?, previous_close = ?, change = ?, change_percent = ?, timestamp = ? WHERE symbol = ?";

  private static final String INSERT = "INSERT INTO quote (symbol, open, high, low, price, volume, latest_trading_day, previous_close, change, change_percent, timestamp) VALUES (?,?,?,?,?,?,?,?,?,?,?)";

  private static final Logger loggerInfo = LogManager.getLogger("StockLogInfo");
  private static final Logger loggerError = LogManager.getLogger("StockLogError");

  @Override
  public Quote save(Quote entity) throws IllegalArgumentException {
    //find entity by id, if is present/exists update, if not create the quote
    Quote quoteSaved;
    if(findById(entity.getTicker()).isPresent()){
      loggerInfo.info("updating already existing quote");
      quoteSaved = update(entity);
    }else{
      loggerInfo.info("creating quote");
      quoteSaved = create(entity);
    }
    return quoteSaved;
  }

  public Quote update(Quote entity) {
    loggerInfo.info("Preparing statement to update");
    try(PreparedStatement statement = this.c.prepareStatement(UPDATE);){
      statement.setDouble(1, entity.getOpen());
      statement.setDouble(2, entity.getHigh());
      statement.setDouble(3, entity.getLow());
      statement.setDouble(4, entity.getPrice());
      statement.setInt(5, entity.getVolume());
      statement.setDate(6, entity.getLatestTradingDay());
      statement.setDouble(7, entity.getPreviousClose());
      statement.setDouble(8, entity.getChange());
      statement.setString(9, entity.getChangePercent());
      statement.setTimestamp(10, entity.getTimestamp());
      statement.setString(11, entity.getTicker());
      loggerInfo.info("executing prepared statement to update");
      statement.execute();
      return entity;
    }catch (SQLException e){
      loggerError.error("Quote DAO Update Error: Unable to process because ", e);
      throw new RuntimeException(e);
    }
  }

  public Quote create(Quote entity) {
    loggerInfo.info("Preparing statement to create");
    try(PreparedStatement statement = this.c.prepareStatement(INSERT);){
      statement.setString(1, entity.getTicker());
      statement.setDouble(2,entity.getOpen());
      statement.setDouble(3,entity.getHigh());
      statement.setDouble(4,entity.getLow());
      statement.setDouble(5,entity.getPrice());
      statement.setInt(6,entity.getVolume());
      statement.setDate(7,entity.getLatestTradingDay());
      statement.setDouble(8,entity.getPreviousClose());
      statement.setDouble(9,entity.getChange());
      statement.setString(10,entity.getChangePercent());
      statement.setTimestamp(11,entity.getTimestamp());
      loggerInfo.info("executing prepared statement to create");
      statement.execute();
      return entity;
    }catch(SQLException e){
      loggerError.error("Quote DAO Create Error: Unable to process because ", e);
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<Quote> findById(String s) throws IllegalArgumentException {
    Quote quote = new Quote();
    loggerInfo.info("Preparing statement to find by id");
    try(PreparedStatement statement = this.c.prepareStatement(GET_ONE);){
      statement.setString(1, s);
      loggerInfo.info("executing prepared statement to find by id");
      ResultSet rs = statement.executeQuery();
      while(rs.next()){
        quote.setTicker(rs.getString("symbol"));
        quote.setOpen(rs.getDouble("open"));
        quote.setHigh(rs.getDouble("high"));
        quote.setLow(rs.getDouble("low"));
        quote.setPrice(rs.getDouble("price"));
        quote.setVolume(rs.getInt("volume"));
        quote.setLatestTradingDay(rs.getDate("latest_trading_day"));
        quote.setPreviousClose(rs.getDouble("previous_close"));
        quote.setChange(rs.getDouble("change"));
        quote.setChangePercent(rs.getString("change_percent"));
        quote.setTimestamp(rs.getTimestamp("timestamp"));
      }
    }catch(SQLException e){
      loggerError.error("Quote DAO Find By Id Error: Unable to process because ", e);
      throw new RuntimeException(e);
    }
    return Optional.ofNullable(quote);
  }

  @Override
  public Iterable<Quote> findAll() {
    List<Quote> quotes = new ArrayList<>();
    loggerInfo.info("Preparing statement to find all");
    try(PreparedStatement statement = this.c.prepareStatement(GET_ALL);){
      loggerInfo.info("executing prepared statement to find all");
      ResultSet rs = statement.executeQuery();
      while(rs.next()){
        Quote quote = new Quote();
        quote.setTicker(rs.getString("symbol"));
        quote.setOpen(rs.getDouble("open"));
        quote.setHigh(rs.getDouble("high"));
        quote.setLow(rs.getDouble("low"));
        quote.setPrice(rs.getDouble("price"));
        quote.setVolume(rs.getInt("volume"));
        quote.setLatestTradingDay(rs.getDate("latest_trading_day"));
        quote.setPreviousClose(rs.getDouble("previous_close"));
        quote.setChange(rs.getDouble("change"));
        quote.setChangePercent(rs.getString("change_percent"));
        quote.setTimestamp(rs.getTimestamp("timestamp"));

        quotes.add(quote);
      }
    }catch(SQLException e){
      loggerError.error("Quote DAO Find All Error: Unable to process because ", e);
      throw new RuntimeException(e);
    }
    return quotes;
  }

  @Override
  public void deleteById(String s) throws IllegalArgumentException {
    loggerInfo.info("Preparing statement to delete by id");
    try(PreparedStatement statement = this.c.prepareStatement(DELETE_ONE);){
      statement.setString(1, s);
      loggerInfo.info("executing prepared statement to delete by id");
      statement.execute();
    }catch(SQLException e){
      loggerError.error("Quote DAO Delete By Id Error: Unable to process because ", e);
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteAll() {
    loggerInfo.info("Preparing statement to delete all");
    try(PreparedStatement statement = this.c.prepareStatement(DELETE_ALL);){
      loggerInfo.info("executing prepared statement to delete all");
      statement.execute();
    }catch(SQLException e){
      loggerError.error("Quote DAO Delete All Error: Unable to process because ", e);
      throw new RuntimeException(e);
    }
  }

  public Connection getC() {
    return c;
  }

  public void setC(Connection c) {
    this.c = c;
  }
}
