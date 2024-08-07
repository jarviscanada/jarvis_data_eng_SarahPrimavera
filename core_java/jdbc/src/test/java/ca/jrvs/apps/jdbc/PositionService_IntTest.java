package ca.jrvs.apps.jdbc;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import okhttp3.OkHttpClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PositionService_IntTest {

  private PositionService positionService;
  private PositionDAO positionDAO;
  private DatabaseConnectionManager dcm;
  private Connection connection;

  @Before
  public void setUp() throws Exception {
    dcm = new DatabaseConnectionManager("localhost", "stock_quote", "postgres", "password");
    connection = dcm.getConnection();
    positionDAO = new PositionDAO();
    positionDAO.setC(connection);
    positionService = new PositionService();
    positionService.setDao(positionDAO);
  }

  @After
  public void tearDown() throws Exception {
    if (connection != null && !connection.isClosed()) {
      connection.close();
    }
  }

  @Test
  public void testBuySuccess() throws Exception {
    String ticker = "AAPL";
    int numberOfShares = 10;
    double price = 150.0;

    Position result = positionService.buy(ticker, numberOfShares, price);

    assertNotNull(result);
    assertEquals(ticker, result.getTicker());
    assertEquals(numberOfShares, result.getNumOfShares());
    assertEquals(price, result.getValuePaid(), 0.01);

    Optional<Position> newPosition = positionDAO.findById(ticker);
    assertTrue(newPosition.isPresent());
    assertEquals(ticker, newPosition.get().getTicker());
    assertEquals(numberOfShares, newPosition.get().getNumOfShares());
    assertEquals(price, newPosition.get().getValuePaid(), 0.01);
  }

  @Test
  public void testBuyDatabaseConnectionError() {

    try {
      connection.close();
    } catch (SQLException e) {
      fail("Failed to close connection: " + e.getMessage());
    }

    String ticker = "AAPL";
    int numberOfShares = 10;
    double price = 150.0;

    try {
      positionService.buy(ticker, numberOfShares, price);
      fail("Expected SQLException");
    } catch (SQLException e) {
      assertEquals("Connection is closed.", e.getMessage());
    }
  }

  @Test
  public void testBuyExceptionHandling() {

    String ticker = "AAPL";
    int numberOfShares = 10;
    double price = 150.0;

    Position position = new Position();
    position.setTicker(ticker);
    position.setNumOfShares(numberOfShares);
    position.setValuePaid(price);

    try {
      positionService.buy(ticker, numberOfShares, price);
    } catch (Exception e) {
      fail("Unexpected exception: " + e.getMessage());
    }

    Optional<Position> newPosition = positionDAO.findById(ticker);
    assertFalse(newPosition.isPresent());
  }

  @Test
  public void testSellSuccess() throws Exception {
    String ticker = "AAPL";
    int numberOfShares = 10;
    double price = 150.0;

    positionService.buy(ticker, numberOfShares, price);

    positionService.sell(ticker);

    Optional<Position> newPosition = positionDAO.findById(ticker);
    assertFalse(newPosition.isPresent());
  }

  @Test
  public void testSell_DatabaseConnectionError() {

    try {
      connection.close();
    } catch (SQLException e) {
      fail("Failed to close connection: " + e.getMessage());
    }

    String ticker = "AAPL";

    try {
      positionService.sell(ticker);
      fail("Expected SQLException");
    } catch (SQLException e) {
      assertEquals("Connection is closed.", e.getMessage());
    }
  }

  @Test
  public void testSell_ExceptionHandling() {

    String ticker = "AAPL";

    try {
      positionService.sell(ticker);
    } catch (Exception e) {
      fail("Unexpected exception: " + e.getMessage());
    }

    Optional<Position> newPosition = positionDAO.findById(ticker);
    assertFalse(newPosition.isPresent());
  }
}