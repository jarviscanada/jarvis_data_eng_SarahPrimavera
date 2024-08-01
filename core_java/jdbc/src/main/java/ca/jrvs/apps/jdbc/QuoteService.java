package ca.jrvs.apps.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import okhttp3.OkHttpClient;

public class QuoteService {

  private QuoteDAO dao;
  private QuoteHttpHelper httpHelper;
private DatabaseConnectionManager dcm;

  /**
   * Fetches latest quote data from endpoint
   * @param ticker
   * @return Latest quote information or empty optional if ticker symbol not found
   */
  public Optional<Quote> fetchQuoteDataFromAPI(String ticker) throws SQLException {

    //setting up connection to the database
    Connection connection = dcm.getConnection();
    dao.setC(connection);

    try{
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
      e.printStackTrace();
    }finally {
      if (connection != null && !connection.isClosed()) {
        connection.close();
      }
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

  public DatabaseConnectionManager getDcm() {
    return dcm;
  }

  public void setDcm(DatabaseConnectionManager dcm) {
    this.dcm = dcm;
  }
}
