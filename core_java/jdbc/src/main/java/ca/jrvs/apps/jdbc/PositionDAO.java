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

public class PositionDAO implements CrudDao<Position, String>{

  private Connection c;

  private static final String GET_ONE = "SELECT symbol, number_of_shares, value_paid FROM position WHERE symbol=?";

  private static final String GET_ALL = "SELECT symbol, number_of_shares, value_paid FROM position";

  private static final String DELETE_ONE = "DELETE FROM position WHERE symbol = ?";

  private static final String DELETE_ALL = "DELETE FROM position";

  private static final String UPDATE = "UPDATE position SET number_of_shares = ?, value_paid = ? WHERE symbol = ?";

  private static final String INSERT = "INSERT INTO position (symbol, number_of_shares, value_paid) VALUES (?,?,?)";

  private static final Logger loggerInfo = LogManager.getLogger("StockLogInfo");
  private static final Logger loggerError = LogManager.getLogger("StockLogError");

  @Override
  public Position save(Position entity) throws IllegalArgumentException {
    //find entity by id, if null create the position, if not null update the position
    Position positionSaved = new Position();
    if(findById(entity.getTicker()).isPresent()){
      loggerInfo.info("updating already existing quote");
      positionSaved = create(entity);
    }else{
      loggerInfo.info("creating quote");
      positionSaved = update(entity);
    }
    return positionSaved;
  }

  public Position update(Position entity) {
    loggerInfo.info("Preparing statement to update");
    try(PreparedStatement statement = this.c.prepareStatement(UPDATE);){
      statement.setInt(1, entity.getNumOfShares());
      statement.setDouble(2, entity.getValuePaid());
      statement.setString(3, entity.getTicker());
      loggerInfo.info("executing prepared statement to update");
      statement.execute();
      return entity;
    }catch (SQLException e){
      loggerError.error("Position Update Error: Unable to process because ", e);
      throw new RuntimeException(e);
    }
  }

  public Position create(Position entity) {
    loggerInfo.info("Preparing statement to create");
    try(PreparedStatement statement = this.c.prepareStatement(INSERT);){
      statement.setString(1, entity.getTicker());
      statement.setInt(2,entity.getNumOfShares());
      statement.setDouble(3,entity.getValuePaid());
      loggerInfo.info("executing prepared statement to create");
      statement.execute();
      return entity;
    }catch(SQLException e){
      loggerError.error("Position Create Error: Unable to process because ", e);
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<Position> findById(String s) throws IllegalArgumentException {
    Position position = new Position();
    loggerInfo.info("Preparing statement to find by id");
    try(PreparedStatement statement = this.c.prepareStatement(GET_ONE);){
      statement.setString(1, s);
      loggerInfo.info("executing prepared statement to find by id");
      ResultSet rs = statement.executeQuery();
      while(rs.next()){
        position.setTicker(rs.getString("symbol"));
        position.setNumOfShares(rs.getInt("number_of_shares"));
        position.setValuePaid(rs.getDouble("value_paid"));
      }
    }catch(SQLException e){
      loggerError.error("Position Find By Id Error: Unable to process because ", e);
      throw new RuntimeException(e);
    }
    return Optional.ofNullable(position);
  }

  @Override
  public Iterable<Position> findAll() {
    List<Position> positions = new ArrayList<>();
    loggerInfo.info("Preparing statement to find all");
    try(PreparedStatement statement = this.c.prepareStatement(GET_ALL);){
      loggerInfo.info("executing prepared statement to find all");
      ResultSet rs = statement.executeQuery();
      while(rs.next()){
        Position position = new Position();
        position.setTicker(rs.getString("symbol"));
        position.setNumOfShares(rs.getInt("number_of_shares"));
        position.setValuePaid(rs.getDouble("value_paid"));

        positions.add(position);
      }
    }catch(SQLException e){
      loggerError.error("Position Find All Error: Unable to process because ", e);
      throw new RuntimeException(e);
    }
    return positions;
  }

  @Override
  public void deleteById(String s) throws IllegalArgumentException {
    loggerInfo.info("Preparing statement to delete by id");
    try(PreparedStatement statement = this.c.prepareStatement(DELETE_ONE);){
      statement.setString(1, s);
      loggerInfo.info("executing prepared statement to delete by id");
      statement.execute();
    }catch(SQLException e){
      loggerError.error("Position Delete By Id Error: Unable to process because ", e);
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
      loggerError.error("Position Delete All Error: Unable to process because ", e);
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
