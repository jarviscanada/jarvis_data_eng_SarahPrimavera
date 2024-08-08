package ca.jrvs.apps.jdbc;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class QuoteDAOTest {

  private DatabaseConnectionManager dcm;
  private Connection connection;
  private QuoteDAO quoteDAO;

  @Before
  public void setUp() throws SQLException {
    dcm = new DatabaseConnectionManager("localhost", "stock_quote", "postgres", "password");
    connection = dcm.getConnection();
    quoteDAO = new QuoteDAO();
    quoteDAO.setC(connection);
  }

  @After
  public void tearDown() throws Exception {
    if (connection != null && !connection.isClosed()) {
      connection.close();
    }
  }

  @Test
  public void save() {
    Quote mockQuote = createMockQuote();

    Quote result = quoteDAO.save(mockQuote);

    assertNotNull(result);
    assertEquals(mockQuote.getTicker(), result.getTicker());
    assertEquals(mockQuote.getOpen(), result.getOpen(), 0.01);
    assertEquals(mockQuote.getHigh(), result.getHigh(),0.01);
    assertEquals(mockQuote.getLow(), result.getLow(), 0.01);
    assertEquals(mockQuote.getPrice(), result.getPrice(), 0.01);
    assertEquals(mockQuote.getVolume(), result.getVolume());
    assertEquals(mockQuote.getLatestTradingDay(), result.getLatestTradingDay());
    assertEquals(mockQuote.getPreviousClose(), result.getPreviousClose(), 0.01);
    assertEquals(mockQuote.getChange(), result.getChange(), 0.01);
    assertEquals(mockQuote.getChangePercent(), result.getChangePercent());
  }

  @Test
  public void update() {
    Quote mockQuote = createMockQuote();

    mockQuote.setPrice(105.50);
    when(mockQuote.getPrice()).thenReturn(150.0);

    Quote result = quoteDAO.update(mockQuote);

    assertNotNull(result);
    assertEquals(mockQuote.getTicker(), result.getTicker());
    assertEquals(mockQuote.getOpen(), result.getOpen(), 0.01);
    assertEquals(mockQuote.getHigh(), result.getHigh(),0.01);
    assertEquals(mockQuote.getLow(), result.getLow(), 0.01);
    assertEquals(mockQuote.getPrice(), result.getPrice(), 0.01);
    assertEquals(mockQuote.getVolume(), result.getVolume());
    assertEquals(mockQuote.getLatestTradingDay(), result.getLatestTradingDay());
    assertEquals(mockQuote.getPreviousClose(), result.getPreviousClose(), 0.01);
    assertEquals(mockQuote.getChange(), result.getChange(), 0.01);
    assertEquals(mockQuote.getChangePercent(), result.getChangePercent());
  }

  @Test
  public void create() {
    Quote mockQuote = createMockQuote();

    Quote result = quoteDAO.create(mockQuote);

    assertNotNull(result);
    assertEquals(mockQuote.getTicker(), result.getTicker());
    assertEquals(mockQuote.getOpen(), result.getOpen(), 0.01);
    assertEquals(mockQuote.getHigh(), result.getHigh(),0.01);
    assertEquals(mockQuote.getLow(), result.getLow(), 0.01);
    assertEquals(mockQuote.getPrice(), result.getPrice(), 0.01);
    assertEquals(mockQuote.getVolume(), result.getVolume());
    assertEquals(mockQuote.getLatestTradingDay(), result.getLatestTradingDay());
    assertEquals(mockQuote.getPreviousClose(), result.getPreviousClose(), 0.01);
    assertEquals(mockQuote.getChange(), result.getChange(), 0.01);
    assertEquals(mockQuote.getChangePercent(), result.getChangePercent());

  }

  @Test
  public void findById() {
    Quote mockQuote = createMockQuote();

    Optional<Quote> result = quoteDAO.findById(mockQuote.getTicker());
    assertTrue(result.isPresent());
  }

  @Test
  public void findAll() {
    Iterable<Quote> quotes = quoteDAO.findAll();
    assertTrue(quotes.iterator().hasNext());
  }

  @Test
  public void deleteById() {
    Quote mockQuote = createMockQuote();

    quoteDAO.deleteById(mockQuote.getTicker());

    Optional<Quote> result = quoteDAO.findById(mockQuote.getTicker());
    assertFalse(result.isPresent());
  }

  @Test
  public void deleteAll() {
    quoteDAO.deleteAll();
    Iterable<Quote> quotes = quoteDAO.findAll();
    assertFalse(quotes.iterator().hasNext());
  }

  private Quote createMockQuote() {
    Quote mockQuote = mock(Quote.class);

    when(mockQuote.getTicker()).thenReturn("IBM");
    when(mockQuote.getOpen()).thenReturn(100.0);
    when(mockQuote.getHigh()).thenReturn(105.0);
    when(mockQuote.getLow()).thenReturn(95.0);
    when(mockQuote.getPrice()).thenReturn(102.0);
    when(mockQuote.getVolume()).thenReturn(1000);
    when(mockQuote.getLatestTradingDay()).thenReturn(new java.sql.Date(System.currentTimeMillis()));
    when(mockQuote.getPreviousClose()).thenReturn(101.0);
    when(mockQuote.getChange()).thenReturn(1.0);
    when(mockQuote.getChangePercent()).thenReturn("1.0%");
    when(mockQuote.getTimestamp()).thenReturn(new java.sql.Timestamp(System.currentTimeMillis()));

    return mockQuote;
  }
}