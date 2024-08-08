package ca.jrvs.apps.jdbc;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PositionService_UnitTest {

  private PositionDAO mockDao;
  private DatabaseConnectionManager mockDcm;
  private Connection mockConnection;
  private PositionService positionService;

  @Before
  public void setUp() throws Exception {
    mockDao = mock(PositionDAO.class);
    mockDcm = mock(DatabaseConnectionManager.class);
    mockConnection = mock(Connection.class);
    when(mockDcm.getConnection()).thenReturn(mockConnection);
    positionService = new PositionService();
    positionService.setDao(mockDao);
  }

  @Test
  public void testBuySuccess() throws Exception {
    String ticker = "AAPL";
    int numberOfShares = 10;
    double price = 150.0;

    Position position = new Position();
    position.setTicker(ticker);
    position.setNumOfShares(numberOfShares);
    position.setValuePaid(price);

    when(mockDao.findById(ticker)).thenReturn(Optional.of(position));

    Position result = positionService.buy(ticker, numberOfShares, price);

    assertNotNull(result);
    assertEquals(ticker, result.getTicker());
    assertEquals(numberOfShares, result.getNumOfShares());
    assertEquals(price, result.getValuePaid(), 0.01);

    verify(mockConnection).close();
  }

  @Test
  public void testBuyDatabaseConnectionError() throws Exception {
    String ticker = "AAPL";
    int numberOfShares = 10;
    double price = 150.0;

    when(mockDcm.getConnection()).thenThrow(new SQLException("Connection error"));

    try {
      positionService.buy(ticker, numberOfShares, price);
      fail("Expected SQLException");
    } catch (SQLException e) {
      assertEquals("Connection error", e.getMessage());
    }

    verify(mockDao, never()).save(any(Position.class));
  }

  @Test
  public void testBuyExceptionHandling() throws Exception {
    String ticker = "AAPL";
    int numberOfShares = 10;
    double price = 150.0;

    Position position = new Position();
    position.setTicker(ticker);
    position.setNumOfShares(numberOfShares);
    position.setValuePaid(price);

    when(mockDao.findById(ticker)).thenThrow(new RuntimeException("Unexpected error"));

    Position result = positionService.buy(ticker, numberOfShares, price);

    assertNull(result);

    verify(mockConnection).close();
  }

  @Test
  public void testSellSuccess() throws Exception {
    String ticker = "AAPL";

    positionService.sell(ticker);

    verify(mockDao).deleteById(ticker);

    verify(mockConnection).close();
  }

  @Test
  public void testSellDatabaseConnectionError() throws Exception {
    String ticker = "AAPL";

    when(mockDcm.getConnection()).thenThrow(new SQLException("Connection error"));

    try {
      positionService.sell(ticker);
      fail("Expected SQLException");
    } catch (SQLException e) {
      assertEquals("Connection error", e.getMessage());
    }

    verify(mockDao, never()).deleteById(anyString());
  }

  @Test
  public void testSellExceptionHandling() throws Exception {
    String ticker = "AAPL";

    doThrow(new RuntimeException("Unexpected error")).when(mockDao).deleteById(ticker);

    try {
      positionService.sell(ticker);
    } catch (RuntimeException e) {
      assertEquals("Unexpected error", e.getMessage());
    }

    verify(mockConnection).close();
  }
}
