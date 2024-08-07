package ca.jrvs.apps.jdbc;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import okhttp3.OkHttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StockQuoteApp {

  private static final Logger loggerInfo = LogManager.getLogger("StockLogInfo");
  private static final Logger loggerError = LogManager.getLogger("StockLogError");

  public static void main(String[] args) throws SQLException {
    loggerInfo.info("Reading properties text file for database and apikey info");
    Map<String, String> properties = new HashMap<>();
    String filePath = "src/resources/PropertiesEx.txt";
    try(BufferedReader br = new BufferedReader(new FileReader(filePath))){
      String line;
      while((line = br.readLine()) != null){
        if(line.trim().isEmpty()){
          continue;
        }
        String[] parts = line.split(":", 2);
        if (parts.length == 2) {
          properties.put(parts[0].trim(), parts[1].trim());
        }
      }
    } catch (FileNotFoundException e) {
      loggerError.error("file not found: ", e);
      throw new RuntimeException(e);
    } catch (IOException e) {
      loggerError.error("reading properties file error: ", e);
      throw new RuntimeException(e);
    }

    //setting up connections and classes
    OkHttpClient client = new OkHttpClient();
    DatabaseConnectionManager dcm = new DatabaseConnectionManager(properties.get("server"), properties.get("database"), properties.get("username"), properties.get("password"));
    try(Connection connection = dcm.getConnection()) {
      loggerInfo.info("setting up dao connections and http helper");
      QuoteDAO quoteDao = new QuoteDAO();
      quoteDao.setC(connection);
      PositionDAO positionDao = new PositionDAO();
      positionDao.setC(connection);
      QuoteHttpHelper quoteHttpHelper = new QuoteHttpHelper();
      quoteHttpHelper.setApiKey(properties.get("api-key"));
      quoteHttpHelper.setClient(client);
      QuoteService quoteService = new QuoteService();
      quoteService.setDao(quoteDao);
      quoteService.setHttpHelper(quoteHttpHelper);
      PositionService positionService = new PositionService();
      positionService.setDao(positionDao);

      loggerInfo.info("initiating controller");
      StockQuoteController controller = new StockQuoteController();
      controller.setQuoteService(quoteService);
      controller.setPositionService(positionService);
      controller.initClient();
    } catch (SQLException e) {
      loggerError.error("stock quote app error: ", e);
      throw new RuntimeException(e);
    }

  }

}
