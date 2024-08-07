package ca.jrvs.apps.jdbc;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import okhttp3.OkHttpClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class QuoteService_IntTest {

  private QuoteService quoteService;
  private QuoteDAO quoteDAO;
  private QuoteHttpHelper quoteHttpHelper;
  private DatabaseConnectionManager dcm;
  private Connection connection;

  @Before
  public void setUp() throws Exception {
    dcm = new DatabaseConnectionManager("localhost", "stock_quote", "postgres", "password");
    connection = dcm.getConnection();
    quoteDAO = new QuoteDAO();
    quoteDAO.setC(connection);
    quoteHttpHelper = new QuoteHttpHelper();
    quoteHttpHelper.setClient(new OkHttpClient());
    quoteHttpHelper.setApiKey(System.getenv("StockApiKey"));
    quoteService = new QuoteService();
    quoteService.setDao(quoteDAO);
    quoteService.setHttpHelper(quoteHttpHelper);

  }

  @After
  public void tearDown() throws Exception {
    if (quoteDAO.getC() != null && !quoteDAO.getC().isClosed()) {
      quoteDAO.getC().close();
    }
  }

  @Test
  public void FetchQuoteDataFromAPISuccess() throws SQLException {
    String ticker = "AAPL";

    Optional<Quote> result = quoteService.fetchQuoteDataFromAPI(ticker);

    assertTrue(result.isPresent());
    assertEquals(ticker, result.get().getTicker());
  }

  @Test
  public void FetchQuoteDataFromAPITickerNotFound() throws SQLException {
    String ticker = "INVALID";

    Optional<Quote> result = quoteService.fetchQuoteDataFromAPI(ticker);

    assertFalse(result.isPresent());
  }

  @Test
  public void FetchQuoteDataFromAPIException() throws SQLException {
    String ticker = "AAPL";
     
    quoteHttpHelper.setApiKey("invalid_api_key");

    Optional<Quote> result = quoteService.fetchQuoteDataFromAPI(ticker);

    assertFalse(result.isPresent());
  }
}