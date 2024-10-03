package ca.jrvs.apps.jdbc;

import java.sql.SQLException;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class QuoteService {

  private QuoteDAO dao;
  private QuoteHttpHelper httpHelper;
  private static final Logger loggerInfo = LogManager.getLogger("StockLogInfo");
  private static final Logger loggerError = LogManager.getLogger("StockLogError");

  /**
   * Fetches latest quote data from endpoint
   * @param ticker
   * @return Latest quote information or empty optional if ticker symbol not found
   */
  public Optional<Quote> fetchQuoteDataFromAPI(String ticker) throws SQLException {
    try{
      loggerInfo.info("Starting the fetching");
      //fetching quote info
      Quote data = httpHelper.fetchQuoteInfo(ticker);

      //see if ticker is not null
      if(data.getTicker() != null){
        //if not null save it in database
        dao.save(data);
      }else{
        //isn't valid, returning empty optional
        return Optional.empty();
      }
      return Optional.of(data);
    }catch (Exception e){
      loggerError.error("Quote Service Fetch Data from API Error: Unable to process because ", e);
    }
    return Optional.empty();
  }

  public QuoteDAO getDao() {
    return dao;
  }

  public void setDao(QuoteDAO dao) {
    this.dao = dao;
  }

  public QuoteHttpHelper getHttpHelper() {
    return httpHelper;
  }

  public void setHttpHelper(QuoteHttpHelper httpHelper) {
    this.httpHelper = httpHelper;
  }
}
