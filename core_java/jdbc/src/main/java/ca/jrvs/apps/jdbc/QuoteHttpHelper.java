package ca.jrvs.apps.jdbc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.sql.Timestamp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import okhttp3.ResponseBody;

public class QuoteHttpHelper {

  private String apiKey;
  private OkHttpClient client;
  private static final Logger loggerInfo = LogManager.getLogger("StockLogInfo");
  private static final Logger loggerError = LogManager.getLogger("StockLogError");

  /**
   * Fetch latest quote data from Alpha Vantage endpoint
   * @param symbol
   * @return Quote with latest data
   * @throws IllegalArgumentException - if no data was found for the given symbol
   */
  public Quote fetchQuoteInfo(String symbol) throws IllegalArgumentException{
    loggerInfo.info("Building request to fetch quote info");
    Request request = new Request.Builder()
        .url("https://alpha-vantage.p.rapidapi.com/query?function=GLOBAL_QUOTE&symbol="+symbol+"&datatype=json")
        .header("X-RapidAPI-Key", apiKey)
        .header("X-RapidAPI-Host", "alpha-vantage.p.rapidapi.com")
        .get()
        .build();
    loggerInfo.info("Sending request to fetch quote info");
    try(ResponseBody responseBody = client.newCall(request).execute().body()) {
      JSONObject jsonObject = new JSONObject(responseBody.string());
      JSONObject globalQuote = jsonObject.getJSONObject("Global Quote");
      ObjectMapper objectMapper = new ObjectMapper();
      Quote quote = objectMapper.readValue(globalQuote.toString(), Quote.class);
      quote.setTimestamp(new Timestamp(System.currentTimeMillis()));
      return quote;
    }catch (JsonMappingException e) {
      loggerError.error("Quote Http Helper Json Mapping Error: Unable to process because ", e);
    } catch (JsonProcessingException e) {
      loggerError.error("Quote Http Helper Json Processing Error: Unable to process because ", e);
    } catch (IOException e) {
      loggerError.error("Quote Http Helper IO Error: Unable to process because ", e);
    }
    return null;
  }

  public String getApiKey() {
    return apiKey;
  }

  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }

  public OkHttpClient getClient() {
    return client;
  }

  public void setClient(OkHttpClient client) {
    this.client = client;
  }
}
