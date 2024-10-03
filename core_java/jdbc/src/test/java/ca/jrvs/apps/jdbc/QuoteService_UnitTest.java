package ca.jrvs.apps.jdbc;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;

public class QuoteService_UnitTest {

  private QuoteDAO mockDao;
  private QuoteHttpHelper mockHttpHelper;
  private DatabaseConnectionManager mockDcm;
  private Connection mockConnection;
  private QuoteService quoteService;

  @Before
  public void setUp() throws Exception {
    mockDao = mock(QuoteDAO.class);
    mockHttpHelper = mock(QuoteHttpHelper.class);
    mockDcm = mock(DatabaseConnectionManager.class);
    mockConnection = mock(Connection.class);
    when(mockDcm.getConnection()).thenReturn(mockConnection);
    mockDao.setC(mockConnection);
    quoteService = new QuoteService();
    quoteService.setDao(mockDao);
    quoteService.setHttpHelper(mockHttpHelper);

  }

  @Test
  public void fetchQuoteDataFromAPISuccess() throws SQLException {
    String ticker = "AAPL";
    Quote quote = new Quote();
    quote.setTicker(ticker);

    when(mockHttpHelper.fetchQuoteInfo(ticker)).thenReturn(quote);
    when(mockDao.save(any(Quote.class))).thenReturn(quote);

    Optional<Quote> result = quoteService.fetchQuoteDataFromAPI(ticker);

    assertTrue(result.isPresent());
    assertEquals(ticker, result.get().getTicker());

    verify(mockDao).save(quote);
  }

  @Test
  public void fetchQuoteDataFromAPITickerNotFound() throws SQLException {
    String ticker = "INVALID";
    Quote quote = new Quote();
    quote.setTicker(null);

    when(mockHttpHelper.fetchQuoteInfo(ticker)).thenReturn(quote);

    Optional<Quote> result = quoteService.fetchQuoteDataFromAPI(ticker);

    assertFalse(result.isPresent());

    verify(mockDao, never()).save(any(Quote.class));
  }

  @Test
  public void fetchQuoteDataFromAPIException() throws SQLException {
    String ticker = "AAPL";

    when(mockHttpHelper.fetchQuoteInfo(ticker)).thenThrow(new RuntimeException("API error"));

    Optional<Quote> result = quoteService.fetchQuoteDataFromAPI(ticker);

    assertFalse(result.isPresent());

    verify(mockDao, never()).save(any(Quote.class));
  }
}