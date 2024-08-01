package ca.jrvs.apps.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuoteDAO implements CrudDao<Quote, String>{

  private Connection c;

  private static final String GET_ONE = "SELECT symbol, open, high, low, price, volume, latest_trading_day, previous_close, change, change_percent, timestamp FROM quote WHERE symbol=?";

  private static final String GET_ALL = "SELECT symbol, open, high, low, price, volume, latest_trading_day, previous_close, change, change_percent, timestamp FROM quote";

  private static final String DELETE_ONE = "DELETE FROM quote WHERE symbol = ?";

  private static final String DELETE_ALL = "DELETE FROM quote";

  private static final String UPDATE = "UPDATE quote SET open = ?, high = ?, low = ?, price = ?, volume = ?, latest_trading_day = ?, previous_close = ?, change = ?, change_percent = ?, timestamp = ? WHERE symbol = ?";

  private static final String INSERT = "INSERT INTO quote (symbol, open, high, low, price, volume, latest_trading_day, previous_close, change, change_percent, timestamp) VALUES (?,?,?,?,?,?,?,?,?,?,?)";

  @Override
  public Quote save(Quote entity) throws IllegalArgumentException {
    //find entity by id, if is present/exists update, if not create the quote
    Quote quoteSaved;
    if(findById(entity.getTicker()).isPresent()){
      quoteSaved = update(entity);
    }else{
      quoteSaved = create(entity);
    }
    return quoteSaved;
  }

  public Quote update(Quote entity) {
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
      statement.execute();
      return entity;
    }catch (SQLException e){
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  public Quote create(Quote entity) {
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
      statement.execute();
      return entity;
    }catch(SQLException e){
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<Quote> findById(String s) throws IllegalArgumentException {
    Quote quote = new Quote();
    try(PreparedStatement statement = this.c.prepareStatement(GET_ONE);){
      statement.setString(1, s);
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
      e.printStackTrace();
      throw new RuntimeException(e);
    }
    return Optional.ofNullable(quote);
  }

  @Override
  public Iterable<Quote> findAll() {
    List<Quote> quotes = new ArrayList<>();
    try(PreparedStatement statement = this.c.prepareStatement(GET_ALL);){
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
      e.printStackTrace();
      throw new RuntimeException(e);
    }
    return quotes;
  }

  @Override
  public void deleteById(String s) throws IllegalArgumentException {
    try(PreparedStatement statement = this.c.prepareStatement(DELETE_ONE);){
      statement.setString(1, s);
      statement.execute();
    }catch(SQLException e){
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteAll() {
    try(PreparedStatement statement = this.c.prepareStatement(DELETE_ALL);){
      statement.execute();
    }catch(SQLException e){
      e.printStackTrace();
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
